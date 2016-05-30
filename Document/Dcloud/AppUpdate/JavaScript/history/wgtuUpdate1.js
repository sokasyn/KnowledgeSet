(function(w,owner){
    var updateServer = "http://192.168.0.105:8088/arm/version/getVersion";
    var appid = null;         // webapp的id
    var appVersion = null;    // webapp的版本
    var updateDir = "update"; // 升级资源包的下载目录_downloads/update
    var dir = null;           // 目录对象
    var waiting = null;       // 等待框
    var downloadTask = null;  // 下载任务对象
    //var xhr = null;
 
    // 公开属性
    owner.description = "App增量升级对象";
 
   /*
    * 公开接口,执行升级
    * 外部通过 appUpdateObj.beginWgtuUpdate()调用
    */
    owner.beginWgtuUpdate = function(){
        updateInit();
    };
 
   /*
    * 公开接口,暂停升级
    * 外部通过 appUpdateObj.pauseUpdate()调用
    */
    owner.pauseWgtuUpdate = function(){
        alert("pauseWgtuUpdate");
    };
 
   /*
    * 公开接口,在暂停的前提下继续升级
    * 外部通过 appUpdateObj.resumWgtuUpdate()调用
    */
    owner.resumeWgtuUpdate = function(){
        alert("resumeWgtuUpdate");
    };
 
   /*
    * 公开接口,取消更新
    * 外部通过 appUpdateObj.cancelWgtuUpdate()调用
    */
    owner.cancelWgtuUpdate = function(){
        alert("cancelWgtuUpdate");
    };
 
 
    // 初始化配置
    function updateInit(){
         if(w.plus){
         }else{
            document.addEventListener("plusready",function(){return false;},false);
         }
 
        // 创建升级包的存放目录
        plus.io.requestFileSystem( plus.io.PUBLIC_DOWNLOADS, function(fileSystem){
              fileSystem.root.getDirectory(updateDir, {create:true}, function(entry){
                   dir = entry;
                   appid = plus.runtime.appid;
                   plus.runtime.getProperty(appid,function(wgetInfo){
                        appVersion = wgetInfo.version;
                        checkUpdate(appid,appVersion);
                   });
              }, function(e){
                   plus.console.log("打开update目录失败" + e.message);
              });
        }, function(e){
               plus.console.log( "打开downloads目录失败：" + e.message );
        });
    }

    // 检查更新
    function checkUpdate(appid,version){
        plus.nativeUI.toast("正在检查更新，请稍等...");
        var xhr = new plus.net.XMLHttpRequest();
        xhr.onreadystatechange = function () {
            switch ( xhr.readyState ) {
                case 0:
                    plus.console.log( "xhr请求已初始化" );
                    break;
                case 1:
                    plus.console.log( "xhr请求已打开" );
                    break;
                case 2:
                    plus.console.log( "xhr请求已发送" );
                    break;
                case 3:
                    plus.console.log( "xhr请求已响应");
                    break;
                case 4:
                    if(xhr.status == 200){
                        plus.console.log( "xhr请求成功" );
                        responseComplete(xhr);
                    }else{
                        plus.nativeUI.alert( "网络请求失败：" + xhr.status );
                    }
                    break;
                default :
                    break;
            }
        }
        xhr.open( "POST", updateServer );
        xhr.setRequestHeader('Content-Type','application/json');
        // 超时设置
        xhr.timeout = 10000; // 10s
        xhr.ontimeout = function(){
            alert("request timeout");
        }
        
        // 发送HTTP请求
        var data = {
            versionNo : version,
            appId : appid
        };
        var param = JSON.stringify(data);
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
            if(isMax!=null && isMax!="undefined" && isMax == false){
                var message = "当前版本为:" + appVersion + ",发现新的版本:" + data.to + ",建议立即更新.";
                plus.nativeUI.confirm(message, function(e){
                                      if(e.index == 0){
                                          dowloadWgtu(downloadUrl);
                                      }else{}
                },"更新提示",["现在更新","下次再说"]);
            }else if(isMax!=null && isMax!="undefined" && isMax == true){
                plus.nativeUI.alert("当前应用版本是最新的");
            }else{}
        }else{
            plus.nativeUI.alert(e.message);
        }
    }
 
 /*
    function getNameFromUrl(url){
        var urlComponents = url.split("/");
        var length = urlComponents.length;
        return urlComponents[length-1];
    }
 */
    // 下载升级包
    function dowloadWgtu(url){
        waiting = plus.nativeUI.showWaiting("处理中,请稍等...",{back:"none"});
        var wgtuDir = dir.toURL() + "/";
        downloadTask = plus.downloader.createDownload(url, {method:"GET",filename:wgtuDir});
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
                      waiting.close();
                  }
                  break;
              case 5:
                  waiting.setTitle("下载已暂停...");
                  break;
              default:
                  break;
              }
         });
        downloadTask.start();
    }

    // 安装升级包
    function installWgtu(wgtuFilePath){
        waiting.setTitle("正在安装...");
        plus.runtime.install(wgtuFilePath,{},function(){
             waiting.setTitle("安装成功！正在重启应用.");
             closeWaiting();
             deleteWgtuFile(wgtuFilePath);
             restartWebApp();
             /*
             plus.nativeUI.confirm("安装成功!是否重启应用",function(e){
                   waiting.close();
                   restartWebApp();
                   if(e.index == 0){
                       waiting.close();
                       restartWebApp();
                   }else{
                       waiting.close();
                   }
             },"提示",["现在重启","稍后再说"]);*/
        },function(e){
             closeWaiting();
             plus.nativeUI.alert("安装失败:" + e.message);
        });
    }
 
     function closeWaiting(){
         if(waiting){
             waiting.close();
             waiting = null;
         }
     }
 
    // 删除升级资源包
    function deleteWgtuFile(filePath){
        plus.console.log("delete file path:" + filePath);
        plus.io.resolveLocalFileSystemURL( filePath, function(entry){
              entry.remove(function(entry){
                           //plus.console.log("remove successfully!");
              },function(e){
                           //plus.console.log("remove failed");
              });
        }, function(e){
            plus.console.log( "Resolve file URL failed: " + e.message );
        });
    }

    // 重启应用
    function restartWebApp(){
        plus.runtime.restart();
    }
 
 }(window,window.appUpdateObj = {}));
