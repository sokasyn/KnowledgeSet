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
 */
+ (BOOL)key:(NSString *)key isPropertyOfClass:(Class)cls;

+ (NSArray *)getPropertyListOfClass:(Class)cls;

@end
