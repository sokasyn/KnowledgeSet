//
//  MBHelper.m
//  MemoryBook
//
//  Created by Sasmon on 16/6/14.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "MBHelper.h"

@implementation MBHelper

+ (id)sharedInstance{
    static dispatch_once_t onceToken;
    static MBHelper *instance = nil;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

@end
