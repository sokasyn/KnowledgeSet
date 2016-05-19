document.addEventListener( "plusready",  function()
{
    // Android : data/properties.xml 配置的PGLogin对应的原生java类PGLoginManager.java
    // iOS :PandoraAPI.bundle 下的feature.plist配置的PGLogin对应原生的插件类PGLoginManager.h
    var pgLoginManager = "PGLogin";

    // Dcloud Plugin Bridge层中的API
    var bridge = window.plus.bridge;

    // Java Class中方法的名字
    // iOS .h文件中接口方法的名字
    var aUserLogin = "userLogin";
    var aRegister = "userRegister";
    var aForgetPassword = "forgetPassword";
    var aLogout = "logout";
    var aCheckin = "checkin";

    // 对象:提供方法接口以供调用
    var loginManager = {
        // 通过bridge.callbackId(success,failure)合并成功与失败回调,并只生成一个callbackId传入
        login : function(userName,password,succCallback,failCallback){
            var success = (typeof(succCallback) !="function") ? null : function(result){
                succCallback(result);
            };
            var failure = (typeof(failCallback) != "function") ? null : function(result){
                failCallback(result);
            };
            callBackID = bridge.callbackId(success,failure);
            return bridge.exec(pgLoginManager,aUserLogin,[userName,password,callBackID]);
        },

        // 分别生成成功与失败回调函数的callbackId,并传入
        register : function(userName,succCallback,failCallback){
            var success = (typeof(succCallback) !="function") ? null : function(result){
                succCallback(result);
            };
            var failure = (typeof(failCallback) != "function") ? null : function(result){
                failCallback(result);
            };
            sCallbackId = bridge.callbackId(success);
            fCallbackId = bridge.callbackId(failure);
            return bridge.exec(pgLoginManager,aRegister,[userName,sCallbackId,fCallbackId]);
        },

        // 测试点:无回调,只有一个参数,插件类方法有返回值
        // 采用同步调用方式,如果采用异步exec()方式,可能Undefined的结果
        forgetPassword : function(userName){
           var result = bridge.execSync(pgLoginManager,aForgetPassword,[userName]);
           alert("forgetPassword result:" + result);
        },

        // 无参数，调用时候不写也可以
        logout : function(){
            var result = bridge.execSync(pgLoginManager,aLogout);
            alert("logout result:" + result);
        },

        // 参数只有一个回调函数
        checkin : function(callback){
            callBackID = bridge.callbackId(callback);
            bridge.exec(pgLoginManager,aCheckin,[callBackID]);
        }
    };

    // 供HTML调用的接口对象
    window.plus.loginManager = loginManager;

}, true );