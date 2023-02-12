/**
 *  Copyright 2021
 *
 *  Based on the original work done by https://github.com/Wattos/hubitat
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Home Connect Integration (APP for Home Conection API integration)
 *
 *  Author: Rangner Ferraz Guimaraes (rferrazguimaraes)
 *  Date: 2021-11-28
 *  Version: 1.0 - Initial commit
 */

import groovy.transform.Field
import groovy.json.JsonSlurper

@Field static String messageBuffer = ""
@Field static Integer messageScopeCount = 0

definition(
    name: 'Home Connect Integration',
    namespace: 'rferrazguimaraes',
    author: 'Rangner Ferraz Guimaraes',
    description: 'Integrates with Home Connect',
    category: 'My Apps',
    iconUrl: '',
    iconX2Url: '',
    iconX3Url: ''
)

@Field HomeConnectAPI = HomeConnectAPI_create(oAuthTokenFactory: {return getOAuthAuthToken()}, language: {return getLanguage()});
@Field Utils = Utils_create();
@Field List<String> LOG_LEVELS = ["error", "warn", "info", "debug", "trace"]
@Field String DEFAULT_LOG_LEVEL = LOG_LEVELS[1]
def driverVer() { return "1.0" }

//  ===== Settings =====
private getClientId() { settings.clientId }
private getClientSecret() { settings.clientSecret }

//  ===== Lifecycle methods ====
def installed() {
    Utils.toLogger("info", "installing Home Connect")
    synchronizeDevices();
}

def uninstalled() {
    Utils.toLogger("info", "uninstalled Home Connect")
    deleteChildDevicesByDevices(getChildDevices());
}

def updated() {	
    Utils.toLogger("info", "updating with settings")
    synchronizeDevices();
}

//  ===== Helpers =====
def getHomeConnectAPI() {
    return HomeConnectAPI;
}

def getUtils() {
    return Utils;
}

//  ===== Pages =====
preferences {
    page(name: "pageIntro")
    page(name: "pageAuthentication")
    page(name: "pageDevices")
}

def pageIntro() {
    Utils.toLogger("debug", "Showing Introduction Page");

    def countries = HomeConnectAPI.getSupportedLanguages();
    def countriesList = Utils.toFlattenMap(countries);
    if (region != null) {
		atomicState.langCode = region
        Utils.toLogger("debug", "atomicState.langCode: ${region}")
		atomicState.countryCode = countriesList.find { it.key == region}?.value
        Utils.toLogger("debug", "atomicState.countryCode(${atomicState.countryCode})")
    }

    return dynamicPage(
        name: 'pageIntro',
        title: 'Home Connect Introduction', 
        nextPage: 'pageAuthentication',
        install: false, 
        uninstall: true) {
        section("""\
                    |This application connects to the Home Connect service.
                    |It will allow you to monitor your smart appliances from Home Connect within Hubitat.
                    |
                    |Please note, before you can proceed, you will need to:
                    | 1. Sign up at <a href="https://developer.home-connect.com/" target='_blank'>Home Connect Developer Portal</a>.
                    | 2. Go to <a href="https://developer.home-connect.com/applications" target='_blank'>Home Connect Applications</a>.
                    | 3. Register a new application with the following values:
                    |    * <b>Application ID</b>: hubitat-homeconnect-integration
                    |    * <b>OAuth Flow</b>: Authorization Code Grant Flow
                    |    * <b>Redirect URI</b>: ${getFullApiServerUrl()}/oauth/callback
                    |    * You can leave the rest as blank
                    | 4. Copy the following values down below.
                    |""".stripMargin()) {}
            section('Enter your Home Connect Developer Details.') {
                input name: 'clientId', title: 'Client ID', type: 'text', required: true
                input name: 'clientSecret', title: 'Client Secret', type: 'text', required: true
       			input "region", "enum", title: "Select your region", options: countriesList, required: true
       			input "logLevel", "enum", title: "Log Level", options: LOG_LEVELS, defaultValue: DEFAULT_LOG_LEVEL, required: false
            }
            section('''\
                    |Press 'Next' to connect to your Home Connect Account.
                    |'''.stripMargin()) {}
    }
}

def pageAuthentication() {
    Utils.toLogger("debug", "Showing Authentication Page");

    if (!atomicState.accessToken) {
        atomicState.accessToken = createAccessToken();
    }

    return dynamicPage(
        name: 'pageAuthentication', 
        title: 'Home Connect Authentication',
        nextPage: 'pageDevices',
        install: false, 
        uninstall: false) {
        section() {
            def title = "Connect to Home Connect"
            if (atomicState.oAuthAuthToken) {
                Utils.showHideNextButton(true);
                title = "Re-connect to Home Connect"
                paragraph '<b>Success!</b> You are connected to Home Connect. Please press the Next button.'
            } else {
                Utils.showHideNextButton(false);
                paragraph 'To continue, you need to connect your hubitat to Home connect. Please press the button below to connect'
            }
            
            href url: generateOAuthUrl(), style:'external', required:false, 'title': title
        }
    }
}

def pageDevices() {
    HomeConnectAPI.getHomeAppliances() { devices -> homeConnectDevices = devices}
    def deviceList = [:]
	state.foundDevices = []
	homeConnectDevices.each {
        deviceList << ["${it.haId}":"${it.name} (${it.type}) (${it.haId})"]
		state.foundDevices << [haId: it.haId, name: it.name, type: it.type]
	}

    return dynamicPage(
        name: 'pageDevices', 
        title: 'Home Connect Devices',
        install: true, 
        uninstall: true) {
        section() {
            paragraph 'Select the following devices';
            input name: 'devices', title: 'Select Devices', type: 'enum', required: true, multiple:true, options: deviceList
        }
    }
}

// ==== App behaviour ====

def synchronizeDevices() {
    def childDevices = getChildDevices();
    def childrenMap = childDevices.collectEntries {
        [ "${it.deviceNetworkId}": it ]
    };

    for (homeConnectDeviceId in settings.devices) {
        def hubitatDeviceId = homeConnectIdToDeviceNetworkId(homeConnectDeviceId);

        if (childrenMap.containsKey(hubitatDeviceId)) {
            childrenMap.remove(hubitatDeviceId)
            continue;
        }

        def homeConnectDevice = state.foundDevices.find({it.haId == homeConnectDeviceId})
        switch(homeConnectDevice.type) {
            case "Dishwasher":
                device = addChildDevice('rferrazguimaraes', 'Home Connect Dishwasher', hubitatDeviceId);
            break
            case "Dryer":
                device = addChildDevice('rferrazguimaraes', 'Home Connect Dryer', hubitatDeviceId);
            break
            case "Washer":
                device = addChildDevice('rferrazguimaraes', 'Home Connect Washer', hubitatDeviceId);
            break
            case "WasherDryer":
                device = addChildDevice('rferrazguimaraes', 'Home Connect WasherDryer', hubitatDeviceId);
            break
            case "FridgeFreezer":
                device = addChildDevice('rferrazguimaraes', 'Home Connect FridgeFreezer', hubitatDeviceId);
            break
            case "Oven":
                device = addChildDevice('rferrazguimaraes', 'Home Connect Oven', hubitatDeviceId);
            break
            case "CoffeeMaker":
                device = addChildDevice('rferrazguimaraes', 'Home Connect CoffeeMaker', hubitatDeviceId);
            break
            case "Hood":
                device = addChildDevice('rferrazguimaraes', 'Home Connect Hood', hubitatDeviceId);
            break
            case "Hob":
                device = addChildDevice('rferrazguimaraes', 'Home Connect Hob', hubitatDeviceId);
            break
            default:
                Utils.toLogger("error", "Not supported: ${homeConnectDevice.type}");
            break
        }
    }

    deleteChildDevicesByDevices(childrenMap.values());
}

def deleteChildDevicesByDevices(devices) {
    for (d in devices) {
        deleteChildDevice(d.deviceNetworkId);
    }
}

def homeConnectIdToDeviceNetworkId(haId) {
    return "${haId}"
}

def intializeStatus(device) {
    def haId = device.deviceNetworkId
    
    Utils.toLogger("info", "Initializing the status of the device ${haId}")
    HomeConnectAPI.getStatus(haId) { status ->
        device.deviceLog("info", "Status received: ${status}")
        processMessage(device, status);
    }
    
    HomeConnectAPI.getSettings(haId) { settings ->
        device.deviceLog("info", "Settings received: ${settings}")
        processMessage(device, settings);
    }

    try {
        HomeConnectAPI.getActiveProgram(haId) { activeProgram ->
            device.deviceLog("info", "ActiveProgram received: ${activeProgram}")
            processMessage(device, activeProgram);
        }
    } catch (Exception e) {
        // no active program
        if(device.getDataValue("ActiveProgram") != "") {
            device.sendEvent(name: "ActiveProgram", value: "newStat", descriptionText: "Active Program changed to: ${newStat}", displayed: true, isStateChange: true)
        }
    }
}

def startProgram(device, programKey, optionKey = "") {
    Utils.toLogger("debug", "startProgram device: ${device}")

    HomeConnectAPI.setActiveProgram(device.deviceNetworkId, programKey, optionKey) { availablePrograms ->
        Utils.toLogger("info", "setActiveProgram availablePrograms: ${availablePrograms}")
    }
}

def stopProgram(device) {
    Utils.toLogger("debug", "stopProgram device: ${device}")

    HomeConnectAPI.setStopProgram(device.deviceNetworkId) { availablePrograms ->
        Utils.toLogger("info", "stopProgram availablePrograms: ${availablePrograms}")
    }
}

def setPowertate(device, boolean state) {
    Utils.toLogger("debug", "setPowertate from ${device} - ${state}")

    HomeConnectAPI.setSettings(device.deviceNetworkId, "BSH.Common.Setting.PowerState", state ? "BSH.Common.EnumType.PowerState.On" : "BSH.Common.EnumType.PowerState.Off") { settings ->
        device.deviceLog("info", "Settings Sent: ${updateState}")
    }
}

def getAvailableProgramList(device) {
    Utils.toLogger("debug", "getAvailableProgramList device: ${device}")
    def availableProgramList = []
    
    HomeConnectAPI.getAvailablePrograms(device.deviceNetworkId) { availablePrograms ->
        Utils.toLogger("info", "updateAvailableProgramList availablePrograms: ${availablePrograms}")
        availableProgramList = availablePrograms
    }
    
    return availableProgramList
}

def setSelectedProgram(device, programKey, optionKey = "") {
    Utils.toLogger("debug", "setSelectedProgram device: ${device}")
    
    HomeConnectAPI.setSelectedProgram(device.deviceNetworkId, programKey, optionKey) { availablePrograms ->
        Utils.toLogger("info", "setSelectedProgram availablePrograms: ${availablePrograms}")
    }
}

def getAvailableProgramOptionsList(device, programKey) {
    Utils.toLogger("debug", "getAvailableProgramOptionsList device: ${device} - programKey: ${programKey}")
    def availableProgramOptionsList = []
    
    HomeConnectAPI.getAvailableProgram(device.deviceNetworkId, "${programKey}") { availableProgram ->
        Utils.toLogger("debug", "updateAvailableOptionList availableProgram: ${availableProgram}")
        if(availableProgram) {                
            Utils.toLogger("debug", "updateAvailableOptionList availableProgram.options: ${availableProgram.options}")
            availableProgramOptionsList = availableProgram.options
        }
    }
    
    return availableProgramOptionsList
}

def setSelectedProgramOption(device, optionKey, optionValue) {
    Utils.toLogger("debug", "setSelectedProgramOption device: ${device} - optionKey: ${optionKey} - optionValue: ${optionValue}")
    
    HomeConnectAPI.setSelectedProgramOption(device.deviceNetworkId, optionKey, optionValue) { availablePrograms ->
        Utils.toLogger("info", "setSelectedProgramOption availablePrograms: ${availablePrograms}")
    }
}

def processMessage(device, ArrayList textArrayList) {
    Utils.toLogger("debug", "processMessage from ${device} message array list: ${textArrayList}")

    if (textArrayList == null || textArrayList.isEmpty()) { 
        device.deviceLog("debug", "Ignore eventstream message")
        return
    }
    
    def text = new groovy.json.JsonBuilder(textArrayList).toString()
    Utils.toLogger("debug", "processMessage from ${device} message text: ${text}")
    
    if(!text.contains('items:')) {
        text = "data:{\"items\":" + text + "}"
    }
    
    processMessage(device, text);
}

def processMessage(device, String text) {
    Utils.toLogger("debug", "processMessage from ${device} message: ${text}")
    
    // EventStream is documented here
    //  https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events#event_stream_format

    // Ignore comment lines from event stream
    if (text == null || text.trim().isEmpty() || text.startsWith(':')) { 
        device.deviceLog("debug", "Ignore eventstream message")
        return
    }
    
    if(text.contains('items')) {
        if(!text.contains('data:')) {
            text = "data:" + text
        }       
    } else {        
        if(text.contains('{')) {
            messageBuffer += text
            messageScopeCount += 1
            device.deviceLog("debug", "text contains { - messageScopeCount:${messageScopeCount} - messageBuffer:  ${messageBuffer}")            
            return // message not finished yet
        } else if(text.contains('}')) {
            messageBuffer += text
            messageScopeCount -= 1
            device.deviceLog("debug", "text contains } - messageScopeCount:${messageScopeCount} - messageBuffer: ${messageBuffer}")
            if(messageScopeCount == 0) { // message finished
                if(messageBuffer.contains('error'))
                {
                    text = "error:" + messageBuffer
                }
                device.deviceLog("debug", "messageBuffer complet: ${messageBuffer}")         
                device.deviceLog("debug", "text complet: ${text}")
            } else {
                return // message not finished yet
            }
        } else if(!messageBuffer.isEmpty()) {
            messageBuffer += text
            device.deviceLog("debug", "message buffer concat - messageScopeCount:${messageScopeCount} - messageBuffer: ${messageBuffer}")
            return // message not finished yet
        }
    }
    
    messageBuffer = ""
    
    // Parse the lines of data.  Expected types are: event, data, id, retry -- all other fields ignored.
    def (String type, String message) = text.split(':', 2)
    device.deviceLog("debug", "type: ${type}")
    device.deviceLog("debug", "message: ${message}")
    switch (type) {
        case 'id':
            device.deviceLog("debug", "Received ID: ${message}")
            break

        case 'data':
            device.deviceLog("debug", "Received data: ${message}")
            if (!message.isEmpty()) { // empty messages are just KEEP-AlIVE messages
                def result = new JsonSlurper().parseText(message)?.items
                device.deviceLog("debug", "result: ${result}")
                processData(device, result)
            }
            break

        case 'event':
            device.deviceLog("debug", "Received event: ${message}")
            break

        case 'retry':
            device.deviceLog("debug", "Received retry: ${message}")
            break

        case 'error':
            device.deviceLog("error", "Received error: ${message}")
            def result = new JsonSlurper().parseText(message)
            //device.deviceLog("error", "result: ${result}")
            processData(device, result)
            break

        default:
            device.deviceLog("debug", "Received unknown data: ${text}")
    }    
}

def processData(device, data) {
    Utils.toLogger("debug", "processData: ${device} - ${data}")

    //if(data instanceof ArrayList) {
        data.each {
            switch(it.key) {
                case "BSH.Common.Status.DoorState":
                    device.sendEvent(name: "DoorState", value: "${it.displayvalue}", displayed: true, isStateChange: true)
                    device.sendEvent(name: "contact", value: "${it.displayvalue.toLowerCase()}")
                break
                case "BSH.Common.Status.OperationState":
                    device.sendEvent(name: "OperationState", value: "${it.displayvalue}", displayed: true, isStateChange: true)
                break
                case "BSH.Common.Status.RemoteControlActive":
                    device.sendEvent(name: "RemoteControlActive", value: "${it.value}", displayed: true, isStateChange: true)
                break
                case "BSH.Common.Status.RemoteControlStartAllowed":
                    device.sendEvent(name: "RemoteControlStartAllowed", value: "${it.value}", displayed: true, isStateChange: true)
                break
                case "BSH.Common.Setting.PowerState":
                    device.sendEvent(name: "PowerState", value: "${it.displayvalue}", displayed: true, isStateChange: true)
                break
                case "BSH.Common.Root.SelectedProgram":
                    device.sendEvent(name: "SelectedProgram", value: "${it.displayvalue}", displayed: true, isStateChange: true)
                    device.updateSetting("selectedProgram", [value:"${it.displayvalue}", type:"enum"])
                break
                case "BSH.Common.Root.ActiveProgram":
                    device.sendEvent(name: "ActiveProgram", value: "${it.displayvalue}", displayed: true, isStateChange: true)
                break
                case "BSH.Common.Option.RemainingProgramTime":
                    device.sendEvent(name: "RemainingProgramTime", value: "${Utils.convertSecondsToTime(it.value)}", displayed: true, isStateChange: true)
                break
                case "BSH.Common.Option.ElapsedProgramTime":
                    device.sendEvent(name: "ElapsedProgramTime", value: "${Utils.convertSecondsToTime(it.value)}", displayed: true, isStateChange: true)
                break
                case "BSH.Common.Option.Duration":
                    device.sendEvent(name: "Duration", value: "${Utils.convertSecondsToTime(it.value)}", displayed: true, isStateChange: true)
                break                
                case "BSH.Common.Option.ProgramProgress":
                    device.sendEvent(name: "ProgramProgress", value: "${it.value}%", displayed: true, isStateChange: true)
                break
                case "BSH.Common.Option.StartInRelative":
                    device.sendEvent(name: "StartInRelative", value: "${Utils.convertSecondsToTime(it.value)}", displayed: true, isStateChange: true)
                break
                case "BSH.Common.Event.ProgramFinished":
                    device.sendEvent(name: "EventPresentState", value: "${it.displayvalue}", displayed: true, isStateChange: true)
                break
                case "Dishcare.Dishwasher.Option.IntensivZone":
                    device.sendEvent(name: "IntensivZone", value: "${it.value}", displayed: true, isStateChange: true)
                    device.updateSetting("${it.name.replaceAll("\\s","")}", [value:"${it.value}", type:"bool"])
                break
                case "Dishcare.Dishwasher.Option.BrillianceDry":
                    device.sendEvent(name: "BrillianceDry", value: "${it.value}", displayed: true, isStateChange: true)
                    device.updateSetting("${it.name.replaceAll("\\s","")}", [value:"${it.value}", type:"bool"])
                break
                case "Dishcare.Dishwasher.Option.VarioSpeedPlus":
                    device.sendEvent(name: "VarioSpeedPlus", value: "${it.value}", displayed: true, isStateChange: true)
                    device.updateSetting("${it.name.replaceAll("\\s","")}", [value:"${it.value}", type:"bool"])
                break
                case "Dishcare.Dishwasher.Option.SilenceOnDemand":
                    device.sendEvent(name: "SilenceOnDemand", value: "${it.value}", displayed: true, isStateChange: true)
                    device.updateSetting("${it.name.replaceAll("\\s","")}", [value:"${it.value}", type:"bool"])
                break
                case "Dishcare.Dishwasher.Option.HalfLoad":
                    device.sendEvent(name: "HalfLoad", value: "${it.value}", displayed: true, isStateChange: true)
                    device.updateSetting("${it.name.replaceAll("\\s","")}", [value:"${it.value}", type:"bool"])
                break
                case "Dishcare.Dishwasher.Option.ExtraDry":
                    device.sendEvent(name: "ExtraDry", value: "${it.value}", displayed: true, isStateChange: true)
                    device.updateSetting("${it.name.replaceAll("\\s","")}", [value:"${it.value}", type:"bool"])
                break
                case "Dishcare.Dishwasher.Option.HygienePlus":
                    device.sendEvent(name: "HygienePlus", value: "${it.value}", displayed: true, isStateChange: true)
                    device.updateSetting("${it.name.replaceAll("\\s","")}", [value:"${it.value}", type:"bool"])
                break
                case "Cooking.Common.Option.Hood.VentingLevel":
                    device.sendEvent(name: "VentingLevel", value: "${it.displayvalue}", displayed: true, isStateChange: true)
                break
                case "Cooking.Common.Option.Hood.IntensiveLevel":
                    device.sendEvent(name: "IntensiveLevel", value: "${it.value}", displayed: true, isStateChange: true)
                break
                case "error":
                    device.sendEvent(name: "LastErrorMessage", value: "${Utils.convertErrorMessageTime(it.value?.description)}", displayed: true)
                break
                default:
                    device.deviceLog("error", "Message not supported: (${it})")
                break
            }
        }
/*    } else if(data instanceof Map) {
        Utils.toLogger("debug", "It's a Map!")
        data.each {
            switch(it.key) {
                default:
                    device.deviceLog("debug", "Not supported: (${it})")
                break
            }
        }
    }*/
}

//TODO: Move out into helper library
// ===== Authentication =====
// See Home Connect Developer documentation here: https://developer.home-connect.com/docs/authorization/flow
private final OAUTH_AUTHORIZATION_URL() { 'https://api.home-connect.com/security/oauth/authorize' }
private final OAUTH_TOKEN_URL() { 'https://api.home-connect.com/security/oauth/token' }
private final ENDPOINT_APPLIANCES() { '/api/homeappliances' }

mappings {
    path("/oauth/callback") {action: [GET: "oAuthCallback"]};
}

def generateOAuthUrl() {
    atomicState.oAuthInitState = UUID.randomUUID().toString();
    def params = [
        'client_id': getClientId(),
        'redirect_uri': getOAuthRedirectUrl(),
        'response_type': 'code',
        'scope': 'IdentifyAppliance Monitor Settings Control',
        'state': atomicState.oAuthInitState
    ];
    return "${OAUTH_AUTHORIZATION_URL()}?${Utils.toQueryString(params)}";
}

def getOAuthRedirectUrl() {
    return "${getFullApiServerUrl()}/oauth/callback?access_token=${atomicState.accessToken}";
}

def oAuthCallback() {
    Utils.toLogger("debug", "Received oAuth callback");

    def code = params.code;
    def oAuthState = params.state;
    if (oAuthState != atomicState.oAuthInitState) {
        Utils.toLogger("error", "Init state did not match our state on the callback. Ignoring the request")
        return renderOAuthFailure();
    }
    
    // Prevent any replay attacks and re-initialize the state
    atomicState.oAuthInitState = null;
    atomicState.oAuthRefreshToken = null;
    atomicState.oAuthAuthToken = null;
    atomicState.oAuthTokenExpires = null;

    acquireOAuthToken(code);

    if (!atomicState.oAuthAuthToken) {
        return renderOAuthFailure();
    }
    renderOAuthSuccess();
}

def acquireOAuthToken(String code) {
    Utils.toLogger("debug", "Acquiring OAuth Authentication Token");
    apiRequestAccessToken([
        'grant_type': 'authorization_code',
        'code': code,
        'client_id': getClientId(),
        'client_secret': getClientSecret(),
        'redirect_uri': getOAuthRedirectUrl(),
    ]);
}

def refreshOAuthToken() {
    Utils.toLogger("debug", "Refreshing OAuth Authentication Token");
    apiRequestAccessToken([
        'grant_type': 'refresh_token',
        'refresh_token': atomicState.oAuthRefreshToken,
        'client_secret': getClientSecret(),
    ]);
}

def apiRequestAccessToken(body) {
    try {
        httpPost(uri: OAUTH_TOKEN_URL(), requestContentType: 'application/x-www-form-urlencoded', body: body) { response ->
            if (response && response.data && response.success) {
                atomicState.oAuthRefreshToken = response.data.refresh_token
                atomicState.oAuthAuthToken = response.data.access_token
                atomicState.oAuthTokenExpires = now() + (response.data.expires_in * 1000)
            } else {
                log.error "Failed to acquire OAuth Authentication token. Response was not successful."
            }
        }
    } catch (e) {
        log.error "Failed to acquire OAuth Authentication token due to Exception: ${e}"
    }
}

def getOAuthAuthToken() {
    // Expire the token 1 minute before to avoid race conditions
    if(now() >= atomicState.oAuthTokenExpires - 60_000) {
        refreshOAuthToken();
    }
    
    return atomicState.oAuthAuthToken;
}

def getLanguage() {
    return atomicState.langCode;
}

def renderOAuthSuccess() {
    render contentType: 'text/html', data: '''
    <p>Your Home Connect Account is now connected to Hubitat</p>
    <p>Close this window to continue setup.</p>
    '''
}

def renderOAuthFailure() {
    render contentType: 'text/html', data: '''
        <p>Unable to connect to Home Connect. You can see the logs for more information</p>
        <p>Close this window to try again.</p>
    '''
}

/**
 * The Home Connect API
 *
 * The complete documentation can be found here: https://apiclient.home-connect.com/#/
 */
def HomeConnectAPI_create(Map params = [:]) {
    def defaultParams = [
        apiUrl: 'https://api.home-connect.com',
        language: 'en-US',
        oAuthTokenFactory: null
    ]

    def resolvedParams = defaultParams << params;
    def apiUrl = resolvedParams['apiUrl']
    def oAuthTokenFactory = resolvedParams['oAuthTokenFactory']
    def language = resolvedParams['language']

    def instance = [:];
    def json = new JsonSlurper();

    def authHeaders = {
        return ['Authorization': "Bearer ${oAuthTokenFactory()}", 'Accept-Language': "${language()}", 'accept': "application/vnd.bsh.sdk.v1+json"]
    }

     def apiGet = { path, closure ->
        Utils.toLogger("debug", "API Get Request to Home Connect with path $path")
        return httpGet(uri: apiUrl,
                'path': path,
                'headers': authHeaders()) { response -> 
            closure.call(json.parseText(response.data.text));
        }
    };

    def apiPut = { path, data, closure ->
        Utils.toLogger("debug", "API Put Request to Home Connect with path ${path}")
        Utils.toLogger("debug", "API Put original - ${data}")
        String body = new groovy.json.JsonBuilder(data).toString()
        Utils.toLogger("debug", "API Put Converted - ${body}")
    
        try {
            return httpPut(uri: apiUrl,
                           path: path,
                           contentType: "application/json",
                           requestContentType: 'application/json',
                           body: body,
                           headers: authHeaders()) { response -> 
                Utils.toLogger("debug", "API Put response.data - ${response.data}")
                if(response.data && response.data.text)
                {
                    closure.call(json.parseText(response.data.text));
                }
            }
        } catch (Exception e) {
            Utils.toLogger("error", "Error apiPut - ${e}")
        }
    };

    def apiDelete = { path, closure ->
        Utils.toLogger("debug", "API Delete Request to Home Connect with path ${path}")
        
        try {
            return httpDelete(uri: apiUrl,
                              'path': path,
                              'headers': authHeaders()) { response -> 
                if (response.status == 200) {
                    Utils.toLogger("debug", "API Delete response - ${response.data}")
                    if(response.data && response.data.text)
                    {
                        closure.call(json.parseText(response.data.text));
                    }
                }
            }
        } catch (Exception e) {
            Utils.toLogger("error", "Error apiDelete - ${e}")
        }
    };

    /**
     * Get all home appliances which are paired with the logged-in user account.
     *
     * This endpoint returns a list of all home appliances which are paired
     * with the logged-in user account. All paired home appliances are returned
     * independent of their current connection atomicState. The connection state can
     * be retrieved within the field 'connected' of the respective home appliance.
     * The haId is the primary access key for further API access to a specific
     * home appliance.
     *
     * Example return value:
     * [
     *    {
     *      "name": "My Bosch Oven",
     *      "brand": "BOSCH",
     *      "vib": "HNG6764B6",
     *      "connected": true,
     *      "type": "Oven",
     *      "enumber": "HNG6764B6/09",
     *      "haId": "BOSCH-HNG6764B6-0000000011FF"
     *    }
     * ]
     */
    instance.getHomeAppliances = { closure ->
        Utils.toLogger("info", "Retrieving Home Appliances from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}") { response ->
            closure.call(response.data.homeappliances)
        }
    };

    /**
     * Get a specfic home appliances which are paired with the logged-in user account.
     *
     * This endpoint returns a specific home appliance which is paired with the
     * logged-in user account. It is returned independent of their current
     * connection atomicState. The connection state can be retrieved within the field
     * 'connected' of the respective home appliance.
     * The haId is the primary access key for further API access to a specific
     * home appliance.
     *
     * Example return value:
     *
     * {
     *   "name": "My Bosch Oven",
     *   "brand": "BOSCH",
     *   "vib": "HNG6764B6",
     *   "connected": true,
     *   "type": "Oven",
     *   "enumber": "HNG6764B6/09",
     *   "haId": "BOSCH-HNG6764B6-0000000011FF"
     * }
     */
    instance.getHomeAppliance = { haId, closure ->
        Utils.toLogger("info", "Retrieving Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}") { response ->
            closure.call(response.data)
        }
    };

    /**
     * Get all programs of a given home appliance.
     *
     * Example return value:
     *
     * [
     *   {
     *     "key": "Cooking.Oven.Program.HeatingMode.HotAir",
     *     "constraints": {
     *       "available": true,
     *       "execution": "selectandstart"
     *     }
     *   },
     *   {
     *     "key": "Cooking.Oven.Program.HeatingMode.TopBottomHeating",
     *     "constraints": {
     *       "available": true,
     *       "execution": "selectandstart"
     *     }
     *   },
     *   {
     *     "key": "Cooking.Oven.Program.HeatingMode.PizzaSetting",
     *     "constraints": {
     *       "available": true,
     *       "execution": "selectonly"
     *     }
     *   }
     * ]
     */
    instance.getPrograms = { haId, closure ->
        Utils.toLogger("info", "Retrieving All Programs of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/programs") { response ->
            closure.call(response.data.programs)
        }
    };

    /**
     * Get all programs which are currently available on the given home appliance.
     *
     * Example return value:
     *
     * [
     *   {
     *     "key": "Cooking.Oven.Program.HeatingMode.HotAir",
     *     "constraints": {
     *       "available": true,
     *       "execution": "selectandstart"
     *     }
     *   },
     *   {
     *     "key": "Cooking.Oven.Program.HeatingMode.TopBottomHeating",
     *     "constraints": {
     *       "available": true,
     *       "execution": "selectandstart"
     *     }
     *   },
     *   {
     *     "key": "Cooking.Oven.Program.HeatingMode.PizzaSetting",
     *     "constraints": {
     *       "available": true,
     *       "execution": "selectonly"
     *     }
     *   }
     * ]
     */
    instance.getAvailablePrograms = { haId, closure ->
        Utils.toLogger("info", "Retrieving All Programs of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/programs/available") { response ->
            closure.call(response.data.programs)
        }
    };

    /**
     * Get specific available program.
     *
     * Example return value:
     *
     * {
     *     "key": "Cooking.Oven.Program.HeatingMode.HotAir",
     *     "options": [
     *       {
     *         "key": "Cooking.Oven.Option.SetpointTemperature",
     *         "type": "Int",
     *         "unit": "°C",
     *         "constraints": {
     *           "min": 30,
     *           "max": 250
     *         }
     *       },
     *       {
     *         "key": "BSH.Common.Option.Duration",
     *         "type": "Int",
     *         "unit": "seconds",
     *         "constraints": {
     *           "min": 1,
     *           "max": 86340
     *         }
     *       }
     *     ]
     * }
     */
    instance.getAvailableProgram = { haId, programKey, closure ->
        Utils.toLogger("info", "Retrieving the '${programKey}' Program of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/programs/available/${programKey}") { response ->
            closure.call(response.data)
        }
    };

    /**
     * Get program which is currently executed.
     *
     * Example return value:
     *
     * {
     *     "key": "Cooking.Oven.Program.HeatingMode.HotAir",
     *     "options": [
     *       {
     *         "key": "Cooking.Oven.Option.SetpointTemperature",
     *         "value": 230,
     *         "unit": "°C"
     *       },
     *       {
     *         "key": "BSH.Common.Option.Duration",
     *         "value": 1200,
     *         "unit": "seconds"
     *       }
     *     ]
     * }
     */
    instance.getActiveProgram = { haId, closure ->
        Utils.toLogger("info", "Retrieving the active Program of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/programs/active") { response ->
            closure.call(response.data)
        }
    };
        
    instance.setActiveProgram = { haId, programKey, options = "", closure ->
        def data = [key: "${programKey}"]
        if (options != "") {
            data.put("options", options)
        } 
        Utils.toLogger("info", "Set the active program '${programKey}' of Home Appliance '$haId' from Home Connect")
        apiPut("${ENDPOINT_APPLIANCES()}/${haId}/programs/active", [data: data]) { response ->
            closure.call(response.data)
        }
    };        

    instance.setStopProgram = { haId, closure ->
        Utils.toLogger("info", "Stop the active program of Home Appliance '$haId' from Home Connect")
        apiDelete("${ENDPOINT_APPLIANCES()}/${haId}/programs/active") { response ->
            closure.call(response.data)
        }
    };        
        /*def start_program(self, program_key, options=None):
        """Start a program."""
        if options is not None:
            return self.put(
                "/programs/active", {"data": {"key": program_key, "options": options}}
            )
        return self.put("/programs/active", {"data": {"key": program_key}})*/

    /**
     * Get all options of the active program like temperature or duration.
     *
     * Example return value:
     *
     * {
     *     "key": "Cooking.Oven.Program.HeatingMode.HotAir",
     *     "options": [
     *       {
     *         "key": "Cooking.Oven.Option.SetpointTemperature",
     *         "value": 230,
     *         "unit": "°C"
     *       },
     *       {
     *         "key": "BSH.Common.Option.Duration",
     *         "value": 1200,
     *         "unit": "seconds"
     *       }
     *     ]
     *   }
     */
    instance.getActiveProgramOptions = { haId, closure ->
        Utils.toLogger("info", "Retrieving the active Program Options of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/programs/active/options") { response ->
            //Utils.toLogger("info", "getActiveProgramOptions of Home Appliance '$haId' from Home Connect ${response.data}")
            closure.call(response.data.options)
        }
    };

    /**
     * Get one specific option of the active program, e.g. the duration.
     *
     * Example return value:
     *
     * {
     *  "key": "Cooking.Oven.Option.SetpointTemperature",
     *  "value": 180,
     *  "unit": "°C"
     * }
     */
    instance.getActiveProgramOption = { haId, optionKey, closure ->
        Utils.toLogger("info", "Retrieving the active Program Option '${optionKey}' of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/programs/active/options/${optionKey}") { response ->
            closure.call(response.data.options)
        }
    };
        
    instance.setActiveProgramOption = { haId, optionKey, value, unit = "", closure ->
        def data = [key: "${optionKey}", value: "${value}"]
        if (unit != "") {
            data.put("unit", unit)
        }
        Utils.toLogger("info", "Retrieving the active Program Option '${optionKey}' of Home Appliance '$haId' from Home Connect")
        apiPut("${ENDPOINT_APPLIANCES()}/${haId}/settings/${settingsKey}", [data: data]) { response ->
            closure.call(response.data)
        }
        
        /*apiPut("${ENDPOINT_APPLIANCES()}/${haId}/programs/active/options/${optionKey}", 
               "{\"data\": {\"key\": ${optionKey}, \"value\": ${value}, ${unit}}}") { response ->
            closure.call(response.data.options)
        }*/
        /*apiPut("${ENDPOINT_APPLIANCES()}/${haId}/programs/active/options/${optionKey}", 
               [data: [key:optionKey ]]
               "{\"data\": {\"key\": ${optionKey}, \"value\": ${value}, ${unit}}}") { response ->
            closure.call(response.data.options)
        }*/

    };

    /**
     * Get the program which is currently selected.
     *
     * In most cases the selected program is the program which is currently shown on the display of the home appliance.
     * This program can then be manually adjusted or started on the home appliance itself. 
     * 
     * Example return value:
     *
     * {
     *     "key": "Cooking.Oven.Program.HeatingMode.HotAir",
     *     "options": [
     *       {
     *         "key": "Cooking.Oven.Option.SetpointTemperature",
     *         "value": 230,
     *         "unit": "°C"
     *       },
     *       {
     *         "key": "BSH.Common.Option.Duration",
     *         "value": 1200,
     *         "unit": "seconds"
     *       }
     *     ]
     * }
     */
    instance.getSelectedProgram = { haId, closure ->
        Utils.toLogger("info", "Retrieving the selected Program of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/programs/selected") { response ->
            closure.call(response.data)
        }
    };

    instance.setSelectedProgram = { haId, programKey, options = "", closure ->
        def data = [key: "${programKey}"]
        if (options != "") {
            data.put("options", options)
        } 
        Utils.toLogger("info", "Set the selected program '${programKey}' of Home Appliance '$haId' from Home Connect")
        apiPut("${ENDPOINT_APPLIANCES()}/${haId}/programs/selected", [data: data]) { response ->
            closure.call(response.data)
        }
    };        

    /**
     * Get all options of selected program.
     *
     * Example return value:
     *
     * {
     *     "key": "Cooking.Oven.Program.HeatingMode.HotAir",
     *     "options": [
     *       {
     *         "key": "Cooking.Oven.Option.SetpointTemperature",
     *         "value": 230,
     *         "unit": "°C"
     *       },
     *       {
     *         "key": "BSH.Common.Option.Duration",
     *         "value": 1200,
     *         "unit": "seconds"
     *       }
     *     ]
     *   }
     */
    instance.getSelectedProgramOptions = { haId, closure ->
        Utils.toLogger("info", "Retrieving the selected Program Options of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/programs/selected/options") { response ->
            closure.call(response.data.options)
        }
    };

    /**
     * Get specific option of selected program
     *
     * Example return value:
     *
     * {
     *  "key": "Cooking.Oven.Option.SetpointTemperature",
     *  "value": 180,
     *  "unit": "°C"
     * }
     */
    instance.getSelectedProgramOption = { haId, optionKey, closure ->
        Utils.toLogger("info", "Retrieving the selected Program Option ${optionKey} of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/programs/selected/options/${optionKey}") { response ->
            closure.call(response.data.options)
        }
    };

    instance.setSelectedProgramOption = { haId, optionKey, optionValue, closure ->
        def data = [key: "${optionKey}", value: optionValue]
        Utils.toLogger("info", "Retrieving the selected Program Option ${optionKey} of Home Appliance '$haId' from Home Connect")
        apiPut("${ENDPOINT_APPLIANCES()}/${haId}/programs/selected/options/${optionKey}", [data: data]) { response ->
            closure.call(response.data)
        }
    };     

    /**
     * Get current status of home appliance
     *
     * A detailed description of the available status can be found here:
     *
     * https://developer.home-connect.com/docs/api/status/remotecontrolactivationstate - Remote control activation state
     * https://developer.home-connect.com/docs/api/status/remotestartallowancestate - Remote start allowance state
     * https://developer.home-connect.com/docs/api/status/localcontrolstate - Local control state
     * https://developer.home-connect.com/docs/status/operation_state - Operation state
     * https://developer.home-connect.com/docs/status/door_state - Door state
     *
     * Several more device-specific states can be found at https://developer.home-connect.com/docs/api/status/remotecontrolactivationatomicState.
     *
     * Example return value:
     *
     * [
     *  {
     *    "key": "BSH.Common.Status.OperationState",
     *    "value": "BSH.Common.EnumType.OperationState.Ready"
     *  },
     *  {
     *    "key": "BSH.Common.Status.LocalControlActive",
     *    "value": true
     *  }
     * ]
     */
    instance.getStatus = { haId, closure ->
        Utils.toLogger("info", "Retrieving the status of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/status") { response ->
            closure.call(response.data.status)
        }
    };

    /**
     * Get current status of home appliance
     *
     * A detailed description of the available status can be found here:
     *
     * https://developer.home-connect.com/docs/api/status/remotecontrolactivationstate - Remote control activation state
     * https://developer.home-connect.com/docs/api/status/remotestartallowancestate - Remote start allowance state
     * https://developer.home-connect.com/docs/api/status/localcontrolstate - Local control state
     * https://developer.home-connect.com/docs/status/operation_state - Operation state
     * https://developer.home-connect.com/docs/status/door_state - Door state
     *
     * Several more device-specific states can be found at https://developer.home-connect.com/docs/api/status/remotecontrolactivationatomicState.
     *
     * Example return value:
     *
     *  {
     *    "key": "BSH.Common.Status.OperationState",
     *    "value": "BSH.Common.EnumType.OperationState.Ready"
     *  }
     */
    instance.getSingleStatus = { haId, statusKey, closure ->
        Utils.toLogger("info", "Retrieving the status '${statusKey}' of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/status/${statusKey}") { response ->
            closure.call(response.data)
        }
    };

    /**
     * Get a list of available settings
     *
     * Get a list of available setting of the home appliance.
     * Further documentation can be found here:
     *
     *  https://developer.home-connect.com/docs/settings/power_state - Power state
     *  https://developer.home-connect.com/docs/api/settings/fridgetemperature - Fridge temperature
     *  https://developer.home-connect.com/docs/api/settings/fridgesupermode - Fridge super mode
     *  https://developer.home-connect.com/docs/api/settings/freezertemperature - Freezer temperature
     *  https://developer.home-connect.com/docs/api/settings/freezersupermode - Freezer super mode
     *
     * Example return value:
     *
     * [
     *   {
     *     "key": "BSH.Common.Setting.PowerState",
     *     "value": "BSH.Common.EnumType.PowerState.On"
     *   },
     *   {
     *     "key": "Refrigeration.FridgeFreezer.Setting.SuperModeFreezer",
     *     "value": true
     *   }
     * ]
     */
    instance.getSettings = { haId, closure ->
        Utils.toLogger("info", "Retrieving the settings of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/settings") { response ->
            closure.call(response.data.settings)
        }
    };

    /**
     * Get a specific setting
     *
     *
     * Example return value:
     *
     * {
     *   "key": "BSH.Common.Setting.PowerState",
     *   "value": "BSH.Common.EnumType.PowerState.On",
     *   "type": "BSH.Common.EnumType.PowerState",
     *   "constraints": {
     *     "allowedvalues": [
     *       "BSH.Common.EnumType.PowerState.On",
     *       "BSH.Common.EnumType.PowerState.Standby"
     *     ],
     *     "access": "readWrite"
     *   }
     * }
     */
    instance.getSetting = { haId, settingsKey, closure ->
        Utils.toLogger("info", "Retrieving the setting '${settingsKey}' of Home Appliance '$haId' from Home Connect")
        apiGet("${ENDPOINT_APPLIANCES()}/${haId}/settings/${settingsKey}") { response ->
            closure.call(response.data)
        }
    };

    instance.setSettings = { haId, settingsKey, value, closure ->
        Utils.toLogger("info", "Set the setting '${settingsKey}' of Home Appliance '$haId' from Home Connect")
        //Utils.toLogger("info", "Test: ${new groovy.json.JsonBuilder([data: [key: "${settingsKey}", value: "${value}"]]).toString()}")
        
        //def updatedState = [:]
        //updatedState.put("key", settingsKey);
        //updatedState.put("value", value);
        //"{\"data\": {\"key\": ${settingsKey}, \"value\": ${value}}}"
        //String wrapper = "{\"data\":" + desiredState + "}";
         //{"data":{"key":"BSH.Common.Setting.PowerState","value":"BSH.Common.EnumType.PowerState.On"}}
        apiPut("${ENDPOINT_APPLIANCES()}/${haId}/settings/${settingsKey}", [data: [key: "${settingsKey}", value: "${value}"]]) { response ->
            closure.call(response.data)
        }
    };
        
    /*instance.setSettings = { haId, settingsKey, value, closure ->
        Utils.toLogger("info", "Set the setting '${settingsKey}' of Home Appliance '$haId' from Home Connect")
        apiPut("${ENDPOINT_APPLIANCES()}/${haId}/settings/${settingsKey}", "{\"data\": {\"key\": ${settingsKey}, \"value\": ${value}}}") { response ->
            closure.call(response.data)
        }
    };*/        


    /**
     * Get stream of events for one appliance
     *
     * NOTE: This can only be done from within a device driver. It will not work within an app
     */
    instance.connectDeviceEvents = { haId, interfaces -> 
        Utils.toLogger("info", "Connecting to the event stream of Home Appliance '$haId' from Home Connect")
        //Utils.toLogger("info", "authHeaders '${authHeaders()}' ")
        interfaces.eventStream.connect(
            "${apiUrl}${ENDPOINT_APPLIANCES()}/${haId}/events",
            [rawData: true,
             ignoreSSLIssues: true,
             headers: ([ 'Accept': 'text/event-stream' ] << authHeaders())])
    };

    /**
     * stop stream of events for one appliance
     *
     * NOTE: This can only be done from within a device driver. It will not work within an app
     */
    instance.disconnectDeviceEvents = { haId, interfaces -> 
        Utils.toLogger("info", "Disconnecting to the event stream of Home Appliance '$haId' from Home Connect")
        interfaces.eventStream.close()
    };
        
    /**
     * Get stream of events for all appliances 
     *
     * NOTE: This can only be done from within a device driver. It will not work within an app
     */
    instance.connectEvents = { interfaces -> 
        Utils.toLogger("info", "Connecting to the event stream of all Home Appliances from Home Connect")
        interfaces.eventStream.connect(
            "${apiUrl}/api/homeappliances/events",
            [rawData: true,
             ignoreSSLIssues: true,
             headers: ([ 'Accept': 'text/event-stream' ] << authHeaders())])
    };

    instance.getSupportedLanguages = {
        Utils.toLogger("info", "Getting the list of supported languages")
        // Documentation: https://api-docs.home-connect.com/general?#supported-languages
        return ["Bulgarian": ["Bulgaria": "bg-BG"], 
                "Chinese (Simplified)": ["China": "zh-CN", "Hong Kong": "zh-HK", "Taiwan, Province of China": "zh-TW"], 
                "Czech": ["Czech Republic": "cs-CZ"], 
                "Danish": ["Denmark": "da-DK"],
                "Dutch": ["Belgium": "nl-BE", "Netherlands": "nl-NL"],
                "English": ["Australia": "en-AU", "Canada": "en-CA", "India": "en-IN", "New Zealand": "en-NZ", "Singapore": "en-SG", "South Africa": "en-ZA", "United Kingdom": "en-GB", "United States": "en-US"],
                "Finnish": ["Finland": "fi-FI"],
                "French": ["Belgium": "fr-BE", "Canada": "fr-CA", "France": "fr-FR", "Luxembourg": "fr-LU", "Switzerland": "fr-CH"],
                "German": ["Austria": "de-AT", "Germany": "de-DE", "Luxembourg": "de-LU", "Switzerland": "de-CH"],
                "Greek": ["Greece": "el-GR"],
                "Hungarian": ["Hungary": "hu-HU"],
                "Italian": ["Italy": "it-IT", "Switzerland": "it-CH"],
                "Norwegian": ["Norway": "nb-NO"],
                "Polish": ["Poland": "pl-PL"],
                "Portuguese": ["Portugal": "pt-PT"],
                "Romanian": ["Romania": "ro-RO"],
                "Russian": ["Russian Federation": "ru-RU"],
                "Serbian": ["Suriname": "sr-SR"],
                "Slovak": ["Slovakia": "sk-SK"],
                "Slovenian": ["Slovenia": "sl-SI"],
                "Spanish": ["Chile": "es-CL", "Peru": "es-PE", "Spain": "es-ES"],
                "Swedish": ["Sweden": "sv-SE"],
                "Turkish": ["Turkey": "tr-TR"],
                "Ukrainian": ["Ukraine": "uk-UA"]
               ]
    };
    
    return instance;
}

/**
 * Simple utilities for manipulation
 */

def Utils_create() {
    def instance = [:];
    
    instance.toQueryString = { Map m ->
    	return m.collect { k, v -> "${k}=${new URI(null, null, v.toString(), null)}" }.sort().join("&")
    }

    instance.toFlattenMap = { Map m ->
    	return m.collectEntries { k, v -> 
            def flattened = [:]
            if (v instanceof Map) {
                instance.toFlattenMap(v).collectEntries {  k1, v1 -> 
                    flattened << [ "${v1}": "${k} - ${k1} (${v1})"];
                } 
            } else {
                flattened << [ "${k}": v ];
            }
            return flattened;
        } 
    }

    instance.toLogger = { level, msg ->
        if (level && msg) {
            Integer levelIdx = LOG_LEVELS.indexOf(level);
            Integer setLevelIdx = LOG_LEVELS.indexOf(logLevel);
            if (setLevelIdx < 0) {
                setLevelIdx = LOG_LEVELS.indexOf(DEFAULT_LOG_LEVEL);
            }
            if (levelIdx <= setLevelIdx) {
                log."${level}" "${app.name} ${msg}";
            }
        }
    }
    
    // Converts seconds to time hh:mm:ss
    instance.convertSecondsToTime = { sec ->
                                     long millis = sec * 1000
                                     long hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(millis)
                                     long minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(millis) % java.util.concurrent.TimeUnit.HOURS.toMinutes(1)
                                     String timeString = String.format("%02dh:%02dm", Math.abs(hours), Math.abs(minutes))
                                     return timeString
    }
    
    instance.extractInts = { String input ->
                            return input.findAll( /\d+/ )*.toInteger()
    }
    
    instance.convertErrorMessageTime = { String input ->
        Integer valueInteger = instance.extractInts(input).last()
        String valueStringConverted = instance.convertSecondsToTime(valueInteger)
        return input.replaceAll( valueInteger.toString() + " seconds", valueStringConverted )
    }        
    
    instance.showHideNextButton = { show ->
	    if(show) paragraph "<script>\$('button[name=\"_action_next\"]').show()</script>";
	    else paragraph "<script>\$('button[name=\"_action_next\"]').hide()</script>";
    }
    
    return instance;
}