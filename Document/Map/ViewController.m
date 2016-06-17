//
//  ViewController.m
//  AppleLocation
//
//  Created by Samson on 16/6/15.
//  Copyright © 2016年 emin.digit. All rights reserved.
//

#import "ViewController.h"
#import "FileIOHelper.h"
#import "DateTimeHelper.h"
#import "CLLocationDelegate.h"

@interface ViewController ()

@property (nonatomic,strong) CLLocationManager *locationMgr;
@property (nonatomic,strong) CLLocationDelegate *locationDelegate;
@property (nonatomic,strong) NSString *logFilePath;
@property (nonatomic,assign) NSUInteger locateCount;

@property (weak, nonatomic) IBOutlet UILabel *lblLongitude;
@property (weak, nonatomic) IBOutlet UILabel *lblLatitude;

@property (strong, nonatomic) IBOutlet UILabel *lblLocatCount;

@end

@implementation ViewController

@synthesize locationDelegate = _locationDelegate;
@synthesize logFilePath = _logFilePath;
@synthesize locateCount = _locateCount;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self setup];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setup{
    // 初始化数据
    [self setupData];
    
    // log文件配置
    [self setupFile];
    
    // 定位管理器配置
    if([CLLocationManager locationServicesEnabled]){
        NSLog(@"locationService is enable");
        [self setupLocationManager];
    }else{
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Tips"
                                                            message:@"location service is disable"
                                                           delegate:self
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil, nil];
        [alertView show];
    }
}

- (void)setupData{
    self.locateCount = 0;
}


#define kFileName @"location.log"
- (void)setupFile{
    FileIOHelper *fileHelper = [FileIOHelper sharedInstance];
    NSString *docDir = [fileHelper getDocumentDirectory];
    NSString *filePath = [docDir stringByAppendingPathComponent:kFileName];
    [fileHelper createFileAtPath:filePath];
    self.logFilePath = filePath;
}

#define kDistanceFilter  10
// 配置定位相关信息
- (void)setupLocationManager{
//    self.locationDelegate = [[CLLocationDelegate alloc] initWithUpdateSuccess:<#^(CLLocation *loc)successblock#> failed:<#^(NSError *error)failedBlock#>];
    self.locationDelegate = [[CLLocationDelegate alloc] initWithUpdateSuccess:^(CLLocation *loc){
        [self updateSuceess:loc];
    } failed:^(NSError *error){
        NSLog(@"error:%@",[error userInfo]);
    }];
    
    self.locationMgr = [[CLLocationManager alloc] init];
    self.locationMgr.desiredAccuracy = kCLLocationAccuracyBest;
//    self.locationMgr.distanceFilter = kCLDistanceFilterNone; // default
//    self.locationMgr.distanceFilter = kDistanceFilter;
    self.locationMgr.delegate = self.locationDelegate;
}

- (void)updateSuceess:(CLLocation *)loc{
    self.locateCount++;
    NSString *longitude = [NSString stringWithFormat:@"%f",loc.coordinate.longitude];
    NSString *latitude = [NSString stringWithFormat:@"%f",loc.coordinate.latitude];
    NSLog(@"longitude:%@ latitude:%@",longitude,latitude);
    // 子线程写文件
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        NSLog(@"longitude:%@ latitude:%@",longitude,latitude);
        NSLog(@"===================");
        DateTimeHelper *dateTimeHelper = [DateTimeHelper sharedInstance];
        NSString *time = [dateTimeHelper getCurrentTime];
        NSString *content = [NSString stringWithFormat:@"%@ %@ %@ \n",time,longitude,latitude];
        [[FileIOHelper sharedInstance] writeContent:content toFile:self.logFilePath];
    });
    
    // 主线程更新ui
    dispatch_async(dispatch_get_main_queue(), ^{
        self.lblLocatCount.text = [NSString stringWithFormat:@"%lu",(unsigned long)self.locateCount];
        self.lblLatitude.text = latitude;
        self.lblLongitude.text = longitude;
    });
}

- (IBAction)btnGetLocationClicked:(id)sender {
    if([CLLocationManager locationServicesEnabled]){
        NSLog(@"locationService is enable,will startUpdatingLocation");
        // 子线程获取定位数据
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            [self.locationMgr startUpdatingLocation];
        });
    }else{
        NSLog(@"location Services is disabled");
    }
}

- (IBAction)btnStopClicked:(id)sender {
    [self.locationMgr stopUpdatingLocation];
}


#pragma mark -Mapkit
//http://restapi.amap.com/v3/geocode/geo?key=xxxxxxxxxxxxxxxx&s=rsv3&city=35&address=福建省霞浦县水门畲族乡可用key=389880a06e3f893ea46036f030c94700测试
// {"status":"0","info":"INVALID_USER_KEY","infocode":"10001"}



@end
