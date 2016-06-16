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

/*
 * 从JSON字符串写入到文件中,并且是以易读的josn格式写入的(而不是一行字符串的形式)
 * @param path 目标文件文件路劲
 * @param jsonString JSON字符串
 */
+ (void)writeToFile:(NSString *)filePath withJsonString:(NSString *)jsonString;

+ (void)writeToFile:(NSString *)filePath withDictionary:(NSDictionary *)dic;

@end
