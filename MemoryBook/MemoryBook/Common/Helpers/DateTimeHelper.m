//
//  DateTimeHelper.m
//  MemoryBook
//
//  Created by Samson on 16/6/14.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "DateTimeHelper.h"

@implementation DateTimeHelper

+ (id)sharedInstance{
    static dispatch_once_t onceToken;
    static DateTimeHelper *instance = nil;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

- (NSString *)getCurrentTime{
    NSDate *date = [NSDate date];
    NSDateFormatter *format = [[NSDateFormatter alloc] init];
    NSString *template = @"yyyy-MM-dd hh:mm:ss";
    [format setDateFormat:template];
    NSString *dateString = [format stringFromDate:date];
    return dateString;
}

@end
