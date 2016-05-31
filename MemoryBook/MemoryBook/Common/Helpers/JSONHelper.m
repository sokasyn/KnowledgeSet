//
//  JSONHelper.m
//  MemoryBook
//
//  Created by Sam Tsang on 16/5/31.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "JSONHelper.h"

@implementation JSONHelper

+ (id)dictionaryFromJsonString:(NSString *)jsonString{
    
    NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    id obj = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
    return obj;
}

+ (NSDictionary *)dictionaryFromJsonFile:(NSString *)path{
    // NSData
    NSData *data = [NSData dataWithContentsOfFile:path];
    id object = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
    return object;
    // NSStream
}

@end
