//
//  JSONHelper.h
//  MemoryBook
//
//  Created by Sam Tsang on 16/5/31.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface JSONHelper : NSObject

+ (id)dictionaryFromJsonString:(NSString *)jsonString;
+ (NSDictionary *)dictionaryFromJsonFile:(NSString *)path;


@end
