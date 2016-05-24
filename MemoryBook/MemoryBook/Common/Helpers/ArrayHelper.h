//
//  ArrayHelper.h
//  MemoryBook
//
//  Created by Sam Tsang on 16/5/25.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ArrayHelper : NSObject

/*
 * NSStrin对象数组的排序
 */
+ (NSArray<NSString *> *)sortStringArray:(NSArray<NSString *> *)array;

/*
 * 自定义对象数组的排序
 */
+ (NSArray *)sortCustomObjectArray:(NSArray *)array;

@end
