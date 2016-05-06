var updateServer = "http://192.168.0.105:8080/arm/version/getVersion";
//var xhr = null;
var waiting = null;
var appid = null;
function wgtuUpdateTest(){
    appid = plus.runtime.appid;
    plus.runtime.getProperty(appid,function(wgetInfo){
         checkUpdate(appid,wgetInfo.version);
    });
    //var url = "http://192.168.0.105:8080/arm/appload/temp/update2.0.0.wgtu";
    //dowloadWgtu(url);
}
// 检查更新
function checkUpdate(appid,version){
    plus.nativeUI.toast("正在检查更新，请稍等...");
    var xhr = new plus.net.XMLHttpRequest();
    xhr.onreadystatechange = function () {
        switch ( xhr.readyState ) {
            case 0:
                //alert( "xhr请求已初始化" );
                break;
            case 1:
                //alert( "xhr请求已打开" );
                break;
            case 2:
                //alert( "xhr请求已发送" );
                break;
            case 3:
                //alert( "xhr请求已响应");
                break;
            case 4:
                if(xhr.status == 200){
                    //alert( "xhr请求成功：");
                    responseComplete(xhr);
                }else{
                    alert( "请求失败：" + xhr.status );
                }
                break;
            default :
                break;
        }
    }
    xhr.open( "POST", updateServer );
    xhr.setRequestHeader('Content-Type','application/json');
    
    // 发送HTTP请求
    var data = {
        versionNo : version,
        appId : appid
    };
    var param = JSON.stringify(data);
    //alert("data string:" + param);
    xhr.send(param);
}

// 解析http结果
function responseComplete(xhrObj){
    if(!xhrObj){
        return;
    }
    alert("xhr.responseText:" + xhrObj.responseText);
    var data = JSON.parse(xhrObj.responseText);
    if(data.success){
        var isMax = data.ismax;
        var downloadUrl = data.loadurl;
        
        // ismax:true 是最新版本; false:不是最新版本,可升级
        if(isMax!=null && !isMax){
            var message = "当前版本为:" + appid + ",最新版本为:" + data.to;
            plus.nativeUI.confirm(message, function(e){
                                  if(e.index == 0){
                                  dowloadWgtu(downloadUrl);
                                  }else{
                                  }
                                  },"更新提示",["现在更新","下次再说"]);
        }else if(isMax!=null && isMax){
            //alert("应用版本是最新的");
        }else{}
    }else{
        alert(data.message);
    }
}
// 下载
function dowloadWgtu(url){
    waiting = plus.nativeUI.showWaiting("处理中,请稍等...",{back:"none"});
    var downloadTask = plus.downloader.createDownload(url, {method:"GET"});
    downloadTask.addEventListener('statechanged',function(downloadObj,status){
          switch(downloadObj.state){
          case 0:
              waiting.setTitle("下载任务已经开始...");
              break;
          case 1:
              waiting.setTitle("正在连接服务器...");
              break;
          case 2:
              waiting.setTitle("连接成功,准备下载...");
              break;
          case 3:
              waiting.setTitle("正在为您下载,请耐心等待...\n"+((downloadObj.downloadedSize/(1024*1024)).toFixed(2))+"MB/"+((downloadObj.totalSize/(1024*1024)).toFixed(2))+"MB");
              break;
          case 4:
              if(status == 200){
                  waiting.setTitle("下载完成!");
                  var localFile = downloadObj.filename;
                  installWgtu(localFile);
              }else{
                  waiting.setTitle("下载失败,请稍后重试...");
              }
              break;
          case 5:
              waiting.setTitle("下载已暂停...");
              break;
          default:
              alert("default");
              break;
          }
     });
    downloadTask.start();
}

// 安装
function installWgtu(wgtuFilePath){
    waiting.setTitle("正在安装...");
    plus.runtime.install(wgtuFilePath,{},function(){
         waiting.close();
         plus.nativeUI.confirm("安装成功!是否重启应用",function(e){
               if(e.index == 0){
                   waiting.close();
                   restartWebApp();
               }else{
                   waiting.close();
               }
         },"提示",["现在重启","稍后再说"]);
    },function(e){
         waiting.close();
         plus.nativeUI.alert("安装失败" + e.message + "安装包:" + filePath);
    });
    
}
// 应用重启
function restartWebApp(){
    plus.runtime.restart();
}
