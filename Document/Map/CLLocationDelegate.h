//
//  LocationManager.h
//  TestLocation
//
//  Created by Sam Tsang on 16/6/16.
//  Copyright © 2016年 sokasyn. All rights reserved.
//

#import "TLObject.h"
#import <CoreLocation/CoreLocation.h>

typedef void(^UpdateLocationBlock)(CLLocation *loc);

@interface CLLocationDelegate : TLObject<CLLocationManagerDelegate>

@property (nonatomic , copy) UpdateLocationBlock updateLocationBlock;

- (id)initWithUpdateBlock:(UpdateLocationBlock)block;

@end
