//
//  ViewController.m
//  TestLocation
//
//  Created by Sam Tsang on 16/6/9.
//  Copyright © 2016年 sokasyn. All rights reserved.
//

#import "ViewController.h"
#import "CLLocationDelegate.h"

@interface ViewController ()

@property (nonatomic,strong) CLLocationManager *locationMgr;
@property (nonatomic,strong) CLLocationDelegate *locationDelegate;
@property (nonatomic,assign) BOOL fileExist;

@property (weak, nonatomic) IBOutlet UILabel *lblLongitude;
@property (weak, nonatomic) IBOutlet UILabel *lblLatitude;

@end

@implementation ViewController

@synthesize locationDelegate = _locationDelegate;
@synthesize fileExist = _fileExist;

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
//    [self getCurrentTime];
//    [self setupFile];
    [self setupData];
}

#define kFileName @"location.log"
- (void)setupFile{
//    self.fileExist = [self createFileAtPath:kFileName];
}

- (void)setupData{
    self.locationDelegate = [[CLLocationDelegate alloc] initWithUpdateBlock:^(CLLocation *loc){
        dispatch_async(dispatch_get_main_queue(), ^{
            
            NSString *longitude = [NSString stringWithFormat:@"%f",loc.coordinate.longitude];
            NSString *latitude = [NSString stringWithFormat:@"%f",loc.coordinate.latitude];
            self.lblLatitude.text = latitude;
            self.lblLongitude.text = longitude;
            NSString *dateString = [self getCurrentTime];
            
            NSString *log = [NSString stringWithFormat:@"%@ %@ %@",dateString,longitude,latitude];
            NSString *cachedPath = [self getCachedDir];
            NSString *filePath = [cachedPath stringByAppendingPathComponent:kFileName];
            NSError *error = nil;
            BOOL writeOk = [log writeToFile:filePath atomically:YES encoding:NSUTF8StringEncoding error:&error];
            if(error){
                NSLog(@"Error occured:%@",error.userInfo);
            }
            if(writeOk){
                NSLog(@"write success");
            }else{
                NSLog(@"write failed");
            }
            
        });
    }];
    self.locationMgr = [[CLLocationManager alloc] init];
    self.locationMgr.desiredAccuracy = kCLLocationAccuracyBest;
//    self.locationMgr.distanceFilter = kCLDistanceFilterNone; // default
    self.locationMgr.distanceFilter = 0.5;
    self.locationMgr.delegate = self.locationDelegate;
}

- (IBAction)btnGetLocationClicked:(id)sender {
    if([CLLocationManager locationServicesEnabled]){
        NSLog(@"startUpdatingLocation");
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

#pragma mark -

- (BOOL)createFileAtPath:(NSString *)path{
    BOOL success = NO;
//    NSFileManager *fileMgr = [NSFileManager defaultManager];
    NSString *cachedPath = [self getCachedDir];
    NSString *filePath = [cachedPath stringByAppendingPathComponent:kFileName];
    NSString *initStr = @"#location log";
    NSError *error = nil;
    BOOL writeOk = [initStr writeToFile:filePath atomically:YES encoding:NSUTF8StringEncoding error:&error];
    if(error){
        NSLog(@"Error occured:%@",error.userInfo);
    }
    if(writeOk){
        NSLog(@"write success");
    }else{
        NSLog(@"write failed");
    }
    
    /*
    NSData *data = [initStr dataUsingEncoding:NSUTF8StringEncoding];
    if(![fileMgr fileExistsAtPath:filePath]){
        BOOL createSuccess = [fileMgr createFileAtPath:cachedPath contents:data attributes:nil];
        if(createSuccess){
            success = YES;
        }else{
            NSLog(@"create file failed");
        }
    }*/
    return success;
}

- (NSString *)getCachedDir{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    NSLog(@"dir :%@",path);
    return path;
}

- (NSString *)getCurrentTime{
    NSDate *date = [NSDate date];
    NSDateFormatter *format = [[NSDateFormatter alloc] init];
    NSString *template = @"yyyy-MM-dd hh:mm:ss";
    [format setDateFormat:template];
    NSString *dateString = [format stringFromDate:date];
    NSLog(@"dateString:%@",dateString);
    return dateString;
}

@end
