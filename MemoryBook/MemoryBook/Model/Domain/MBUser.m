//
//  MBUser.m
//  MemoryBook
//
//  Created by Sam Tsang on 16/5/25.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "MBUser.h"

@implementation MBUser

@synthesize userId = userId_;
@synthesize name = name_;
@synthesize age = age_;
@synthesize cellPhoneNum = cellPhoneNum_;

- (id)init{
    self = [super init];
    if(self){
        // 设置userId的值唯一
        userId_ = @"1";
    }
    return self;
}

@end
