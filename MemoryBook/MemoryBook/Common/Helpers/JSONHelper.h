//
//  JSONHelper.h
//  MemoryBook
//
//  Created by Sam Tsang on 16/5/31.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "MBHelper.h"

@interface JSONHelper : MBHelper

/*
 * 从JSON字符串转成OC对象(NSDictionary/NSArray)
 * @param jsonString JSON字符串
 */
+ (id)dictionaryFromJsonString:(NSString *)jsonString;

/*
 * 从JSON文件转成OC对象(NSDictionary/NSArray)
 * @param path JSON文件路劲
 */
+ (id)dictionaryFromJsonFile:(NSString *)path;

+ (void)writeToFileWithJsonString:(NSString *)jsonString;

+ (void)writeToFileWithDictionary:(NSDictionary *)dic;

@end
