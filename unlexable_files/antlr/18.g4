lexer grammar MmLexer ;

Comment : '(*' .*? '*)' -> skip ;
WS : [ \t\r\n]+ -> skip ;

CommandStart : '(' -> pushMode(CommandMode) ;
Text : ~('\'' | '"' | '(' | ')')+ ;

mode CommandMode;
CommandWS : WS -> skip ; 
Role : 'Role' -> mode(RoleCommandMode) ;

mode RoleCommandMode ;
RoleCommandWS : [ \t\r\n]+ -> skip ;
RoleName : [a-zA-Z0-9._] ;
RoleParameterStart : '(' ;
RolePitch : 'pitch' -> pushMode(IntegerParameterMode) ;
RoleCommandEnd : ')' -> popMode ; 

mode IntegerParameterMode ;
IntegerWS : [ \t\r\n]+  -> skip ;
Integer : [+\-]?[0-9]+ ;
IntegerParameterEnd : ')' -> popMode ;