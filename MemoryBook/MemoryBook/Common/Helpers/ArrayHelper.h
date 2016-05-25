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
 * 自定义对象数组的排序
 */
+ (NSArray *)sortArray:(NSArray *)array withKey:(NSString *)key ascending:(BOOL)ascending;
+ (NSArray *)sortArray:(NSArray *)array withKeys:(NSArray *)keys ascending:(BOOL)ascending;

+ (NSArray *)sortStringArray:(NSArray<NSString *> *)array ascending:(BOOL)ascending;

@end
