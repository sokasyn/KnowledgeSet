//
//  ArrayHelper.h
//  MemoryBook
//
//  Created by Sam Tsang on 16/5/25.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RuntimeHelper.h"

@interface ArrayHelper : NSObject

/*
 * 自定义对象数组的排序
 * 如果key不是对象的属性，返回原数组
 * @param array NSString对象数组
 * @param key 类的属性(类声明的@property,而不是成员变量_var之类)
 * @param ascending YES:升序;NO:降序
 * @return 排序后的数组
 */
+ (NSArray *)sortArray:(NSArray *)array withKey:(NSString *)key ascending:(BOOL)ascending;

/*
 * 自定义对象数组的排序
 * 如果key不是对象的属性，返回原数组
 * @param array NSString对象数组
 * @param keys 按照指定的属性列表排序
 * @param ascending YES:升序;NO:降序
 * @return 排序后的数组
 */
+ (NSArray *)sortArray:(NSArray *)array withKeys:(NSArray *)keys ascending:(BOOL)ascending;

/*
 * 对NSSring的对象数组排序
 * @param array NSString对象数组
 * @param ascending YES为升序,NO未降序
 * @return 排序后的数组
 */
+ (NSArray *)sortStringArray:(NSArray<NSString *> *)array ascending:(BOOL)ascending;

/*
 * 截取数组
 * @param array 数组对象
 * @param fromIndex 起初索引
 * @param toIndex   截止索引
 * @return 截取后的数组
 */
+ (NSArray *)subArray:(NSArray *)array atIndex:(NSUInteger)fromIndex toIndex:(NSUInteger)toIndex;

/*
 * 截取数组
 * @param array 数组对象
 * @param fromIndex 起初索引
 * @param length 从起始索引开始的长度
 * @return 截取后的数组
 */
+ (NSArray *)subArray:(NSArray *)array atIndex:(NSUInteger)fromIndex length:(NSUInteger)length;


@end
