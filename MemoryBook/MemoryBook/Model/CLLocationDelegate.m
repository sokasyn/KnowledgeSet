//
//  LocationManager.m
//  TestLocation
//
//  Created by Sam Tsang on 16/6/16.
//  Copyright © 2016年 sokasyn. All rights reserved.
//

#import "CLLocationDelegate.h"

@implementation CLLocationDelegate

@synthesize successBlock = _successBlock;
@synthesize failedBlock = _failedBlock;

- (id)initWithUpdateSuccess:(UpdateLocationSuccessBlock)successblock
                     failed:(UpdateLocatinErrorBlock)failedBlock{
    self = [super init];
    if(self){
        _successBlock = [successblock copy];
        _failedBlock = [failedBlock copy];
    }
    return self;
}

#pragma mark -CLLocationManagerDelegate
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations{
    NSLog(@"%@,locations count:%lu",NSStringFromSelector(_cmd),(unsigned long)locations.count);
    CLLocation *location = [locations firstObject];
    NSString *longitude = [NSString stringWithFormat:@"%f",location.coordinate.longitude];
    NSString *latitude = [NSString stringWithFormat:@"%f",location.coordinate.latitude];
    NSLog(@"longitude:%@ latitude:%@",longitude,latitude);
    self.successBlock(location);
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status{
    switch (status) {
        case kCLAuthorizationStatusNotDetermined:
            if ([manager respondsToSelector:@selector(requestAlwaysAuthorization)]){
                [manager requestWhenInUseAuthorization];
            }
            break;
        default:
            break;
    }
}

-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    NSString *errorMsg = nil;
    if ([error code] == kCLErrorDenied) {
        errorMsg = @"访问被拒绝";
    }
    if ([error code] == kCLErrorLocationUnknown) {
        errorMsg = @"获取位置信息失败";
    }
}

@end
