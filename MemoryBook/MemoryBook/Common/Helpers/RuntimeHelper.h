//
//  RuntimeHelper.h
//  MemoryBook
//
//  Created by Samson Tseng on 16/5/26.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <objc/runtime.h>

@interface RuntimeHelper : NSObject

/*
 * 判断key是否是某个类的属性
 * @param key 属性名称
 * @param cls 目标类Class
 * @return YES:是类的属性 NO:不是类的属性
 */
+ (BOOL)key:(NSString *)key isPropertyOfClass:(Class)cls;

/*
 * 获取某个类的属性列表(区别于成员变量列表)
 * @param cls 目标类Class
 * @return NSArray 属性列表
 */
+ (NSArray *)getPropertyListOfClass:(Class)cls;

/*
 * 获取某个类的成员变量名列表(区别于属性列表)
 * @param cls 目标类Class
 * @return NSArray 成员变量列表
 */
+ (NSArray *)getInstanceVariableList:(Class)cls;

@end
