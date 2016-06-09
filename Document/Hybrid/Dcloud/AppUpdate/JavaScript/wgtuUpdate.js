(function(w,owner){
    var updateServer = "http://192.168.0.105:8088/arm/version/getVersion";
    var appid = null;         // webapp的id
    var appVersion = null;    // webapp的版本
    var updateDir = "update"; // 升级资源包的下载目录_downloads/update
    var dir = null;           // 目录对象
    var waiting = null;       // 等待框
    var downloadTask = null;  // 下载任务对象
    var canLocalUpdateKey = "canLocalUpdate";   // 本地储存是否能离线更新key
    var canLocalUpdateValue = "true";
    var localUpdateFileKey = "localUpdateFile"; // 本地储存离线更新包路劲
 
    // 公开属性
    owner.description = "App增量升级对象";
 
   /*
    * 公开接口,执行升级
    * 外部通过 appUpdater.beginWgtuUpdate()调用
    */
    owner.beginWgtuUpdate = function(){
        updateInit();
    };
 
   /*
    * 公开接口,暂停升级
    * 外部通过 appUpdater.pauseUpdate()调用
    */
    owner.pauseWgtuUpdate = function(){
        downloadTask.pause();
    };
 
   /*
    * 公开接口,在暂停的前提下继续升级
    * 外部通过 appUpdater.resumWgtuUpdate()调用
    */
    owner.resumeWgtuUpdate = function(){
        downloadTask.resume();
    };
 
   /*
    * 公开接口,取消更新
    * 外部通过 appUpdater.cancelWgtuUpdate()调用
    */
    owner.cancelWgtuUpdate = function(){
        downloadTask.abort();
    };
 
    // 初始化配置
    function updateInit(){
         // plusready事件
         if(w.plus){
         }else{
            document.addEventListener("plusready",function(){return false;},false);
         }
 
        // 升级包的存放目录
        plus.io.requestFileSystem( plus.io.PUBLIC_DOWNLOADS, function(fileSystem){
              fileSystem.root.getDirectory(updateDir, {create:true}, function(entry){
                   dir = entry;
                   checkUpdate();
              }, function(e){
                   plus.console.log("打开update目录失败" + e.message);
              });
        }, function(e){
               plus.console.log( "打开downloads目录失败：" + e.message );
        });
    }
 
    /* 
     * 检查更新
     * 如果上一次下载了安装包,但是未安装,则本次可以离线更新,否则发起更新服务请求在线更新
     */
    function checkUpdate(){
        var canLocalUpdate = plus.storage.getItem(canLocalUpdateKey);
        if(canLocalUpdate && canLocalUpdate == canLocalUpdateValue){
             // 离线更新
             localUpdate();
        }else{
             // 在线更新
             onlineUpdate();
        }
    }
 
    /* 离线更新
     * 检查本地是否存在上一次下载完成但未安装的升级包,存在则安装升级包,否则执行在线更新
     */
    function localUpdate(){
        showWaiting("升级中,请稍候...");
        setTimeout(function(){
            getLocalUpdatePackge(function(filePath){
                if(filePath){
                    // 找到本地的安装包,直接安装更新
                    installWgtu(filePath);
                }else{
                    // 找不到本地安装包,则执行在线更新
                    closeWaiting();
                    onlineUpdate();
                }
            });
        },0);
    }
 
    // 获取本地更新包
    function getLocalUpdatePackge(callback){
        var localUpdateFile = plus.storage.getItem(localUpdateFileKey);
        plus.io.resolveLocalFileSystemURL( localUpdateFile, function(){
            // 获取文件成功
            callback(localUpdateFile);
        }, function(){
            // 获取文件失败
            callback();
        } );
    }

    // 在线更新
    function onlineUpdate(){
        appid = plus.runtime.appid;
        plus.runtime.getProperty(appid,function(wgetInfo){
              appVersion = wgetInfo.version;
              requetUpdate(appid,appVersion);
        });
    }

    // 发起更新请求
    function requetUpdate(appid,version){
        plus.nativeUI.toast("正在检查更新，请稍等...");
        var xhr = new plus.net.XMLHttpRequest();
        xhr.onreadystatechange = function () {
            switch ( xhr.readyState ) {
                case 0:
                    // 请求已初始化
                    break;
                case 1:
                    // 请求已打开
                    break;
                case 2:
                    // 请求已发送
                    break;
                case 3:
                    // 请求已响应
                    break;
                case 4:
                    if(xhr.status == 200){
                        // 请求成功,解析返回的json数据
                        responseComplete(xhr);
                    }else{
                        plus.nativeUI.alert( "网络请求失败! status:" + xhr.status );
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
        xhr.ontimeout = function(){ }
        
        // 发送HTTP请求
        var data = { versionNo : version, appId : appid };
        xhr.send(JSON.stringify(data));
    }

    // 解析http结果
    function responseComplete(xhrObj){
        if(!xhrObj){
            return;
        }
        var data = JSON.parse(xhrObj.responseText);
        if(data.success){
            var isMax = data.ismax;
            var downloadUrl = data.loadurl;
            
            // ismax:true 是最新版本; false:不是最新版本,可升级
            if(isMax!=null && isMax!="undefined" && isMax == false){
                var message = "当前版本为:" + appVersion + ",发现新的版本:" + data.to + ",建议立即下载,该过程在后台进行,完成后会通知您.";
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
 
    // 下载升级包
    function dowloadWgtu(url){
        //waiting = plus.nativeUI.showWaiting("处理中,请稍等...",{back:"none",modal:false});
        plus.nativeUI.toast("下载处理中...",{verticalAlign:"bottom",duration:"short"});
        var wgtuDir = dir.toURL() + "/";
        downloadTask = plus.downloader.createDownload(url, {method:"GET",filename:wgtuDir});
        downloadTask.addEventListener('statechanged',function(downloadObj,status){
              switch(downloadObj.state){
              case 0:
                  // 下载对象状态:下载任务已经开始
                  break;
              case 1:
                  // 下载对象状态:正在连接服务器
                  break;
              case 2:
                  // 下载对象状态:连接成功,即将下载(数据传输)
                  break;
              case 3:
                  // 下载对象状态:数据正在接收
                  //waiting.setTitle("正在为您下载,请耐心等待...\n"+((downloadObj.downloadedSize/(1024*1024)).toFixed(2))+"MB/"+((downloadObj.totalSize/(1024*1024)).toFixed(2))+"MB");
                  plus.nativeUI.toast("正在为您下载,请耐心等待...\n"+((downloadObj.downloadedSize/(1024*1024)).toFixed(2))+"MB/"+((downloadObj.totalSize/(1024*1024)).toFixed(2))+"MB");
                  break;
              case 4:
                  showWaiting();
                  if(status == 200){
                       var localFile = downloadObj.filename;
                       plus.nativeUI.confirm("下载完成!是否现在安装更新包?安装完成将会重启应用.",function(e){
                           if(e.index == 0){
                               installWgtu(localFile);
                           }else{
                              // 如果用户选择下载完成时不安装,则在下次启动的时候,检查本地该安装包以离线升级
                              plus.storage.setItem(canLocalUpdateKey, canLocalUpdateValue);
                              plus.storage.setItem(localUpdateFileKey,localFile);
                              closeWaiting();
                           }
                       },"提示",["现在安装","下次启动安装"]);
                  }else{
                      plus.nativeUI.alert("下载失败,请稍后重试...");
                      closeWaiting();
                  }
                  break;
              case 5:
                  plus.nativeUI.alert("下载已暂停...");
                  break;
              default:
                  break;
              }
         });
        downloadTask.start();
    }

    // 安装升级包
    function installWgtu(wgtuFilePath){
        plus.runtime.install(wgtuFilePath,{},function(){
             deleteWgtuFile(wgtuFilePath);
             restartWebApp();
        },function(e){
             closeWaiting();
             plus.nativeUI.alert("安装失败:" + e.message);
        });
    }
 
    function showWaiting(title){
        if(waiting == null){
             waiting = plus.nativeUI.showWaiting();
        }
        waiting.setTitle(title);
    }
 
    function closeWaiting(){
        if(waiting){
            waiting.close();
            waiting = null;
        }
    }
 
    // 删除升级资源包
    function deleteWgtuFile(filePath){
        // 安装成功,则把离线安装的key删除,避免文件删除操作失败导致下次不能在线安装
        plus.storage.removeItem(canLocalUpdateKey);
        plus.storage.removeItem(localUpdateFileKey);
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
 
 }(window,window.appUpdater = {}));
