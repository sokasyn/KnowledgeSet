//
//  LocationManager.m
//  TestLocation
//
//  Created by Sam Tsang on 16/6/16.
//  Copyright © 2016年 sokasyn. All rights reserved.
//

#import "CLLocationDelegate.h"

@implementation CLLocationDelegate

@synthesize updateLocationBlock = _updateLocationBlock;

- (id)initWithUpdateBlock:(UpdateLocationBlock)block{
    self = [super init];
    if(self){
        _updateLocationBlock = [block copy];
    }
    return self;
}

#pragma mark -CLLocationManagerDelegate
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations{
    NSLog(@"%@,locations count:%lu",NSStringFromSelector(_cmd),(unsigned long)locations.count);
    CLLocation *location = [locations firstObject];
    self.updateLocationBlock(location);
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

@end
