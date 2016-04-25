//
//  ViewController.m
//  Pandora
//
//  Created by Mac Pro_C on 12-12-26.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//
#import "ViewController.h"
#import "PDRToolSystem.h"
#import "PDRToolSystemEx.h"
#import "PDRCoreAppFrame.h"
#import "PDRCoreAppManager.h"
#import "PDRCoreAppWindow.h"
#import "PDRCoreAppInfo.h"

#define kStatusBarHeight 20.f

@implementation ViewController
@synthesize defalutStausBarColor;


#pragma mark 应用集成
PDRCoreAppFrame* appFrame = nil;

-(IBAction)ShowWebViewPageOne:(id)sender{
    
    // 如果想在加载的页面返回原生层,那么还得移除_containerView,否则只是移除appFrame,原生这层还盖了一层_containerView
    // 解决方法1:不用_containerView,直接[self.view addSubview:appFrame]
//    [self.view addSubview:_containerView];
    
    PDRCore*  pCoreHandle = [PDRCore Instance];
    if (pCoreHandle != nil){
            
        NSString* pFilePath = [NSString stringWithFormat:@"file://%@/%@", [NSBundle mainBundle].bundlePath, @"Pandora/apps/HelloH5/www/plugin.html"];
        
        // 如果路径中包含中文，或Xcode工程的targets名为中文则需要对路径进行编码
        //NSString* pFilePath =  (NSString *)CFURLCreateStringByAddingPercentEscapes( kCFAllocatorDefault, (CFStringRef)pTempString, NULL, NULL,  kCFStringEncodingUTF8 );
        
        // 单页面集成时可以设置打开的页面是本地文件或者是网络路径
        //NSString* pFilePath = @"http://www.163.com";
        [[PDRCore Instance] startAsWebClient];
        CGRect StRect = CGRectMake(0, 20, self.view.frame.size.width, self.view.frame.size.height - 20);
        
        appFrame = [[PDRCoreAppFrame alloc] initWithName:@"WebViewID1" loadURL:pFilePath frame:StRect];
        // 单页面运行时设置Document目录
        NSString* pStringDocumentpath = [NSString stringWithFormat:@"%@/Pandora/apps/HelloH5/www/", [NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES) objectAtIndex:0]];
        [pCoreHandle.appManager.activeApp.appInfo setWwwPath:pStringDocumentpath];
        [pCoreHandle.appManager.activeApp.appWindow registerFrame:appFrame];
            
//        [_containerView addSubview:appFrame];
        [self.view addSubview:appFrame];
    }
    
}

// 解决widge集成方式的返回原生app的问题,页面层通过推送一个Notification到原生层,原生层这边在处理这个通知是移除widget
#define kWidgetCloseNotification @"WidgetCloseNotification"
#define kAppIdCustom   @"widgetId001"
#define kAppIdFromManifest @"HelloH5"
- (void)registerNotification{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(handleWidgetCloseNotification:)
                                                 name:kWidgetCloseNotification
                                               object:nil];
}

- (void)handleWidgetCloseNotification:(id)sender{
    NSLog(@"%@",NSStringFromSelector(_cmd));
//    [self getWebapp];
    PDRCoreApp *widget = [[[PDRCore Instance] appManager] getAppByID:kAppIdFromManifest];
    if (widget) {
        // 回到主线程移除widget
        dispatch_async(dispatch_get_main_queue(), ^{
            [[[PDRCore  Instance] appManager] endWithAppid:kAppIdFromManifest animated:YES];
            [_containerView removeFromSuperview];
        });
    }else{
        NSLog(@"App with id:%@ not found",kAppIdFromManifest);
    }
    
}

- (void)getWebapp{
    // 通过 SDK5+ 手动设置的appid获取app
    PDRCoreApp *widget = [[[PDRCore Instance] appManager] getAppByID:kAppIdCustom];
    if (widget) {
        NSLog(@"Find app with id:%@",kAppIdCustom);
    }else{
        NSLog(@"Cannot find app with id :%@",kAppIdCustom);
    }

    // 通过www的manifest.json 中定义的appid获取的app
    PDRCoreApp *app = [[[PDRCore Instance] appManager] getAppByID:kAppIdFromManifest];
    if (app) {
        NSLog(@"Find app with id:%@",kAppIdFromManifest);
    }else{
        NSLog(@"Cannot find app with id:%@",kAppIdFromManifest);
    }
}

PDRCoreApp* pAppHandle = nil;

-(IBAction)ShowWebViewPageTwo:(id)sender
{
    //
    [self.view addSubview:_containerView];
    [_containerView setBackgroundColor:[UIColor grayColor]];
    
    // 设置WebApp所在的目录，该目录下必须有mainfest.json
    NSString* pWWWPath = [[[NSBundle mainBundle] bundlePath] stringByAppendingPathComponent:@"Pandora/apps/HelloH5/www"];
    
    // 如果路径中包含中文，或Xcode工程的targets名为中文则需要对路径进行编码
    //NSString* pWWWPath2 =  (NSString *)CFURLCreateStringByAddingPercentEscapes( kCFAllocatorDefault, (CFStringRef)pTempString, NULL, NULL,  kCFStringEncodingUTF8 );
    
    // 设置5+SDK运行的View
    [[PDRCore Instance] setContainerView:_containerView];
    // 设置appid
//    [[PDRCore Instance].appManager.activeApp.appInfo setAppID:kAppIdCustom]; // 这里设置的appId无效
    // 传入参数可以在页面中通过plus.runtime.arguments参数获取
    NSString* pArgus = @"id=plus.runtime.arguments";
    // 启动该应用
    pAppHandle = [[[PDRCore Instance] appManager] openAppAtLocation:pWWWPath withIndexPath:@"index.html" withArgs:pArgus withDelegate:nil];
    
    // 如果应用可能会重复打开的话建议使用restart方法
    //[[[PDRCore Instance] appManager] restart:pAppHandle];
}


- (void)viewDidLoad
{
    [self registerNotification];
    
    PDRCore *h5Engine = [PDRCore Instance];
    [super loadView];
    [self setStatusBarStyle:h5Engine.settings.statusBarStyle];
    _isFullScreen = [UIApplication sharedApplication].statusBarHidden;
    if ( _isFullScreen != h5Engine.settings.fullScreen ) {
        _isFullScreen = h5Engine.settings.fullScreen;
        if ( [self  respondsToSelector:@selector(setNeedsStatusBarAppearanceUpdate)] ) {
            [self setNeedsStatusBarAppearanceUpdate];
        } else {
            [[UIApplication sharedApplication] setStatusBarHidden:_isFullScreen];
        }
    }
    CGRect newRect = self.view.bounds;
    if ( [self reserveStatusbarOffset] && [PTDeviceOSInfo systemVersion] > PTSystemVersion6Series) {
        if ( !_isFullScreen ) {
            newRect.origin.y += kStatusBarHeight;
            newRect.size.height -= kStatusBarHeight;
        }
        _statusBarView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, newRect.size.width, kStatusBarHeight+1)];
        _statusBarView.backgroundColor = h5Engine.settings.statusBarColor;
        _statusBarView.autoresizingMask =  UIViewAutoresizingFlexibleWidth;
        [self.view addSubview:_statusBarView];
    }
    _containerView = [[UIView alloc] initWithFrame:newRect];
    _containerView.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight;
    
    h5Engine.coreDeleagete = self;
    h5Engine.persentViewController = self;
    [h5Engine showLoadingPage];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}
#pragma mark -
- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
                                         duration:(NSTimeInterval)duration
{
    [[PDRCore Instance] handleSysEvent:PDRCoreSysEventInterfaceOrientation
                            withObject:[NSNumber numberWithInt:toInterfaceOrientation]];
    if ([PTDeviceOSInfo systemVersion] >= PTSystemVersion8Series) {
        [[UIApplication sharedApplication] setStatusBarHidden:_isFullScreen ];
    }
}

- (BOOL)shouldAutorotate
{
    return TRUE;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return [[PDRCore Instance].settings supportedInterfaceOrientations];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( [PDRCore Instance].settings ) {
        return [[PDRCore Instance].settings supportsOrientation:interfaceOrientation];
    }
    return UIInterfaceOrientationPortrait == interfaceOrientation;
}

- (BOOL)prefersStatusBarHidden
{
    return _isFullScreen;/*
                          NSString *model = [UIDevice currentDevice].model;
                          if (UIUserInterfaceIdiomPhone == UI_USER_INTERFACE_IDIOM()
                          && (NSOrderedSame == [@"iPad" caseInsensitiveCompare:model]
                          || NSOrderedSame == [@"iPad Simulator" caseInsensitiveCompare:model])) {
                          return YES;
                          }
                          return NO;*/
}

- (UIStatusBarAnimation)preferredStatusBarUpdateAnimation {
    return UIStatusBarAnimationFade;
}

-(BOOL)getStatusBarHidden {
    if ( [PTDeviceOSInfo systemVersion] > PTSystemVersion6Series ) {
        return _isFullScreen;
    }
    return [UIApplication sharedApplication].statusBarHidden;
}

#pragma mark - StatusBarStyle
-(UIStatusBarStyle)getStatusBarStyle {
    return [self preferredStatusBarStyle];
}
-(void)setStatusBarStyle:(UIStatusBarStyle)statusBarStyle {
    if ( _statusBarStyle != statusBarStyle ) {
        _statusBarStyle = statusBarStyle;
        if ( [self  respondsToSelector:@selector(setNeedsStatusBarAppearanceUpdate)] ) {
            [self setNeedsStatusBarAppearanceUpdate];
        } else {
            [[UIApplication sharedApplication] setStatusBarStyle:_statusBarStyle];
        }
    }
}

- (UIStatusBarStyle)preferredStatusBarStyle
{
    return _statusBarStyle;
}

#pragma mark -
- (BOOL)reserveStatusbarOffset {
    return [PDRCore Instance].settings.reserveStatusbarOffset;
}

#pragma mark - StatusBarBackground iOS >=7.0
-(UIColor*)getStatusBarBackground {
    return _statusBarView.backgroundColor;
}

-(void)setStatusBarBackground:(UIColor*)newColor
{
    if ( newColor ) {
        _statusBarView.backgroundColor = newColor;
    }
}
#pragma mark -
-(void)wantsFullScreen:(BOOL)fullScreen
{
    if ( _isFullScreen == fullScreen ) {
        return;
    }
    
    _isFullScreen = fullScreen;
    [[UIApplication sharedApplication] setStatusBarHidden:_isFullScreen withAnimation:_isFullScreen?NO:YES];
    if ( [PTDeviceOSInfo systemVersion] > PTSystemVersion6Series ) {
        [self setNeedsStatusBarAppearanceUpdate];
    }// else {
    //   return;
    //  }
    CGRect newRect = self.view.bounds;
    if ( [PTDeviceOSInfo systemVersion] <= PTSystemVersion6Series ) {
        newRect = [UIApplication sharedApplication].keyWindow.bounds;
        if ( _isFullScreen ) {
            [UIView beginAnimations:nil context:nil];
            self.view.frame = newRect;
            [UIView commitAnimations];
        } else {
            UIInterfaceOrientation interfaceOrientation = [UIApplication sharedApplication].statusBarOrientation;
            if ( UIDeviceOrientationLandscapeLeft == interfaceOrientation
                || interfaceOrientation == UIDeviceOrientationLandscapeRight ) {
                newRect.size.width -=kStatusBarHeight;
            } else {
                newRect.origin.y += kStatusBarHeight;
                newRect.size.height -=kStatusBarHeight;
            }
            [UIView beginAnimations:nil context:nil];
            self.view.frame = newRect;
            [UIView commitAnimations];
        }
        
    } else {
        if ( [self reserveStatusbarOffset] ) {
            _statusBarView.hidden = _isFullScreen;
            if ( !_isFullScreen ) {
                newRect.origin.y += kStatusBarHeight;
                newRect.size.height -= kStatusBarHeight;
            }
        }
        _containerView.frame = newRect;
    }
    [[PDRCore Instance] handleSysEvent:PDRCoreSysEventInterfaceOrientation
                            withObject:[NSNumber numberWithInt:0]];
}

- (void)didReceiveMemoryWarning{
    [[PDRCore Instance] handleSysEvent:PDRCoreSysEventReceiveMemoryWarning withObject:nil];
}

- (void)dealloc {
    [_statusBarView release];
    [_containerView release];
    [super dealloc];
}


@end
