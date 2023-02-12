﻿// *********************************************************
//
//    Copyright (c) Microsoft. All rights reserved.
//    This code is licensed under the Apache License, Version 2.0.
//    THIS CODE IS PROVIDED *AS IS* WITHOUT WARRANTY OF
//    ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING ANY
//    IMPLIED WARRANTIES OF FITNESS FOR A PARTICULAR
//    PURPOSE, MERCHANTABILITY, OR NON-INFRINGEMENT.
//
// *********************************************************

namespace Microsoft.Research.Dkal

open NLog
open System
open System.IO
open System.Diagnostics
open System.Text
open System.Threading
open System.Text.RegularExpressions
open System.Collections.Generic


open Microsoft.Research.Dkal.Interfaces
open Microsoft.Research.Dkal.Infostrate.Factories
open Microsoft.Research.Dkal.Executor.Factories
open Microsoft.Research.Dkal.LogicEngine.Factories
open Microsoft.Research.Dkal.MailBox.Factories
open Microsoft.Research.Dkal.SignatureProvider.Factories
open Microsoft.Research.Dkal.Router.Factories
open Microsoft.Research.Dkal.Router.Local
open Microsoft.Research.Dkal.Ast.Infon.Syntax.Factories
open Microsoft.Research.Dkal.Ast
open Microsoft.Research.Dkal.Ast.Infon
open Microsoft.Research.Dkal.Factories.Initializer
open Microsoft.Research.Dkal.Utils.Exceptions
open Microsoft.Research.Dkal.Utils.ErrorCodes
open Microsoft.Research.Dkal.Globals
open Microsoft.Research.Dkal.LogicEngine.UFOL
open Microsoft.Research.Dkal.LogicEngine.Datalog

/// Console front-end for many principals
module MultiMain =
  
  // TODO: change log format to match RiSE4fun.com standards
  // (it is used in Code Contracts, for instance)
  let private log = LogManager.GetLogger("MultiMain")

  let private messagesLimitExceeded = new AutoResetEvent(false)

  // TODO see how to avoid linking to UFOLEngine just to check the appropriate infostrate
  let private createExec(router: IRouter, assembly: Assembly, assemblyInfo: MultiAssembly, logicEngineKind: string) =
    
    let logicEngine = LogicEngineFactory.LogicEngine (logicEngineKind, Some assemblyInfo)
    let kind =
      match logicEngine with
      | :? UFOLogicEngine as engine -> "ufol"
      | :? DatalogLogicEngine as engine -> "datalog"
      | _ -> "simple"
    let infostrate = InfostrateFactory.Infostrate kind
    let signatureProvider = SignatureProviderFactory.SignatureProvider kind 
    let mailbox = MailBoxFactory.MailBox kind logicEngine
    let executor = ExecutorFactory.Executor (kind, router, logicEngine, signatureProvider, infostrate, mailbox)

//    if assembly.Signature.Substrates.Length > 0 then // TODO: forbid SQL substrate
//      printfn "%s: Substrate declarations are fobidden" router.Me
    
    for ka in assembly.Policy.KA do
      infostrate.Learn ka |> ignore
    for rule in assembly.Policy.Rules do
      executor.InstallRule rule |> ignore
    executor
  
  let lineCount (s: string) = s.Split('\n').Length

  let splitPolicies (policy : string) =
    let rec splitrec (s: string) =
      let mutable i = 0
      while i < s.Length && s.[i]='-' do
        i <- i + 1
      let ppalName = StringBuilder()
      while i < s.Length && s.[i]<>'-' do
        ppalName.Append(s.[i]) |> ignore
        i <- i + 1
      if ppalName.Length = 0 then failwithf "invalid policy header:\n%s" s
      while i < s.Length && s.[i]='-' do
        i <- i + 1
      let s = s.Substring(i)
      let j = s.IndexOf("---")
      if j<0 then
        [(ppalName.ToString(), s)]
      else
        (ppalName.ToString(), s.Substring(0, j)) :: splitrec(s.Substring(j))
    let i = policy.IndexOf("---")
    if i<0 then failwith "headers not found in policy file"
    let commonPolicy = policy.Substring(0, i)
    (commonPolicy, splitrec (policy.Substring(i)))

  let private execute (policy: string, timeLimit: int, msgsLimit: int, logicEngineKind: string) =
    // Populate factories
    FactoriesInitializer.Init()

    let (commonPolicy,ppals) = splitPolicies policy
    let mlogic = Regex.Match(commonPolicy, "^///\s*logic:\s*(\w+)", RegexOptions.IgnoreCase)
    let logicEngineKind = if mlogic.Success then mlogic.Groups.[1].Value else logicEngineKind
    let routers = RouterFactory.LocalRouters (ppals |> List.map fst)

    let ppalsWithOffset = ppals |> List.fold (fun acc x -> 
      match acc with
      | [] -> [fst x, snd x, 0]
      | (_,pol,l) :: _ -> (fst x, snd x, l + lineCount(pol)-1) :: acc) [] |> List.rev
    let parse (ppal,policy,lineOffset) =
      let parser = ParserFactory.InfonParser("simple", routers.[ppal].Me)
      try
        (ppal, parser.ParseAssembly (commonPolicy + policy))
      with
      | ParseException(msg, text, line, col) ->
        log.Error("{0}.dkal({1},{2}): error {3}: {4}", ppal, lineOffset + line, col, errorParsing, msg)
        Environment.Exit(1); failwith ""
      | SemanticCheckException(desc, o) ->
        log.Error("{0}.dkal(0,0): error {1}: {2} at {3}", ppal, errorSemanticCheck, desc, o)
        Environment.Exit(1); failwith ""
    let assemblies =  ppalsWithOffset |> List.map parse
    
    // collect all distinct relation declarations from the assemblies
    let signatures=
      assemblies |>
      List.collect (fun assembly -> (snd assembly).Signature.Relations) |>
      List.fold (fun acc sign ->
        match acc with
        | _ when List.exists (fun sign' -> sign' = sign) acc -> acc
        | _ -> sign::acc ) []
    let multiAssembly= { TypeRenames = Dictionary<string,string>(); Relations= signatures; PrincipalPolicies= 
      assemblies |>
      List.fold (fun acc x -> 
                    acc.[fst x] <- snd x
                    acc) (Dictionary<string,Assembly>()) }

    let fixedPointCounter = new CountdownEvent(assemblies.Length)
    let executors = assemblies |> List.mapi (fun i x ->
      let exec = createExec(routers.[fst x], snd x, multiAssembly, logicEngineKind)
      exec.FixedPointCallback 
        (fun _ ->
          let reachedGlobalFixedPoint = try fixedPointCounter.Signal() with e -> true
          log.Trace("{0} went to sleep (reached global fixed-point = {1} -- counter = {2})", fst x, reachedGlobalFixedPoint, fixedPointCounter.CurrentCount)
          if reachedGlobalFixedPoint then
            messagesLimitExceeded.Set() |> ignore)
      exec.WakeUpCallback 
        (fun _ -> 
          let success = fixedPointCounter.TryAddCount()
          log.Trace("{0} woke up (success = {1} -- counter = {2})", fst x, success, fixedPointCounter.CurrentCount))
      if i = 0 then
        let localMailer = (routers.[fst x] :?> LocalRouter).LocalMailer
        localMailer.AddCallback msgsLimit 
          (fun _ -> messagesLimitExceeded.Set() |> ignore)
      exec
    )
    executors |> List.iter (fun x -> x.Start())
    let reachedTimeLimit = not <| messagesLimitExceeded.WaitOne(timeLimit) 
    executors |> List.iter (fun x -> x.Stop())
    if reachedTimeLimit then
      log.Info("Time limit exceeded at {0} milliseconds", timeLimit)
    elif fixedPointCounter.IsSet then
      log.Info("Fixed-point reached")
    else
      log.Info("Message limit exceeded at {0} messages", msgsLimit)
    // Do a hard kill (in case the last round takes too long after stop was signaled)
    Thread.Sleep(500)
    Process.GetCurrentProcess().Kill()

  let private args = System.Environment.GetCommandLineArgs() |> Seq.toList
  match args with
  | exe :: "-ws" :: tail ->
    Rise4FunWebService.startWebService tail
  | exe :: policyFile :: timeLimit :: msgsLimit :: tail ->
    if not (File.Exists (policyFile)) then
      log.Fatal("File not found: {0}", policyFile)
    else
      try
        execute(File.ReadAllText(policyFile), Int32.Parse(timeLimit), Int32.Parse(msgsLimit), LogicEngineFactory.parseLogicEngineKind tail)
      with
        e -> log.ErrorException("Something went wrong", e)
  | _ -> log.Fatal("Wrong number of parameters; expecting multi-policy file, time limit (ms), messages limit")
