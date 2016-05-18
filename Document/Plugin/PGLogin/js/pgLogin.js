document.addEventListener( "plusready",  function()
{
    // Android : data/properties.xml 配置的PGLogin对应的原生java类PGLoginManager.java
    // iOS :PandoraAPI.bundle 下的feature.plist配置的PGLogin对应原生的插件类PGLoginManager.h
    var pgLoginManager = "PGLogin";

    // Dcloud Plugin Bridge层中的API
    var bridge = window.plus.bridge;

    // Java Class中方法的名字,该js调用的方法必须要一致
    var aUserLogin = "userLogin";
    var aRegister = "userRegister";
    var aForgetPassword = "forgetPassword";
    var aLogout = "logout";
    var aCheckin = "checkin";

    // 对象:提供方法接口以供调用
    var loginManager = {
        login : function(userName,password,callback){
                          
            callBackID = bridge.callbackId(callback);
            alert("callBackID:" + callBackID);
            return bridge.exec(pgLoginManager,aUserLogin,[callBackID,userName,password]);
        },
        register : function(userName){
            var result = bridge.execSync(pgLoginManager,aRegister,[userName]);
            alert(result);
        },
        forgetPassword : function(userName){
           var result = bridge.execSync(pgLoginManager,aForgetPassword,[]);
           alert(result);
        },
        logout : function(){
            alert("pulgin js logout called");
            var result = bridge.execSync(pgLoginManager,aLogout);
            alert(result);
        },
        checkin : function(successCallback){
            callBackID = bridge.callbackId(successCallback);
            var userName = "Mick";
            bridge.exec(pgLoginManager,aCheckin,[callBackID,userName]);
            alert(666);
        }
    };

    // 供HTML调用的接口对象
    window.plus.loginManager = loginManager;
    /*
    var _BARCODE = 'plugintest',
		B = window.plus.bridge;
    var plugintest = 
    {
    	PluginTestFunction : function (Argus1, Argus2, Argus3, Argus4, successCallback, errorCallback ) 
		{
			var success = typeof successCallback !== 'function' ? null : function(args) 
			{
				successCallback(args);
			},
			fail = typeof errorCallback !== 'function' ? null : function(code) 
			{
				errorCallback(code);
			};
			callbackID = B.callbackId(success, fail);

			return B.exec(_BARCODE, "PluginTestFunction", [callbackID, Argus1, Argus2, Argus3, Argus4]);
		},
		PluginTestFunctionArrayArgu : function (Argus, successCallback, errorCallback ) 
		{
			var success = typeof successCallback !== 'function' ? null : function(args) 
			{
				successCallback(args);
			},
			fail = typeof errorCallback !== 'function' ? null : function(code) 
			{
				errorCallback(code);
			};
			callbackID = B.callbackId(success, fail);
			return B.exec(_BARCODE, "PluginTestFunctionArrayArgu", [callbackID, Argus]);
		},		
        PluginTestFunctionSync : function (Argus1, Argus2, Argus3, Argus4) 
        {                                	
            return B.execSync(_BARCODE, "PluginTestFunctionSync", [Argus1, Argus2, Argus3, Argus4]);
        },
        PluginTestFunctionSyncArrayArgu : function (Argus) 
        {                                	
            return B.execSync(_BARCODE, "PluginTestFunctionSyncArrayArgu", [Argus]);
        }
    };
    window.plus.plugintest = plugintest;*/

}, true );