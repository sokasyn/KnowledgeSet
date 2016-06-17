//
//  LocationManager.h
//  TestLocation
//
//  Created by Sam Tsang on 16/6/16.
//  Copyright © 2016年 sokasyn. All rights reserved.
//
/*
 * 实现苹果的CLLocationManagerDelegate的封装,调用的时候传入block,定位成功与失败,通过block回调的方式处理
 */

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

typedef void(^UpdateLocationSuccessBlock)(CLLocation *loc);
typedef void(^UpdateLocatinErrorBlock)(NSError *error);

@interface CLLocationDelegate : NSObject<CLLocationManagerDelegate>

@property (nonatomic , copy) UpdateLocationSuccessBlock successBlock;
@property (nonatomic , copy) UpdateLocatinErrorBlock failedBlock;

- (id)initWithUpdateSuccess:(UpdateLocationSuccessBlock)successblock failed:(UpdateLocatinErrorBlock)failedBlock;

@end
