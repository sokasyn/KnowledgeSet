//
//  RuntimeHelper.m
//  MemoryBook
//
//  Created by Samson Tseng on 16/5/26.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//
/*
 * Modified 2016-05-31 class_copyPropertyList或class_copyIvarList等要free();
 */

#import "RuntimeHelper.h"

@implementation RuntimeHelper

// 判断key是否是某个类的属性
+ (BOOL)key:(NSString *)key isPropertyOfClass:(Class)cls{
    BOOL isProperty = NO;
    objc_property_t property = class_getProperty(cls,[key cStringUsingEncoding:NSUTF8StringEncoding]);
    if(property != NULL){
        isProperty = YES;
    }
    return isProperty;
}

// 获取某个类的属性列表(区别于成员变量列表)
+ (NSArray *)getPropertyListOfClass:(Class)cls{
    
    unsigned int outCount;
    objc_property_t *plist = class_copyPropertyList(cls, &outCount);
    if(outCount == 0){
        return nil;
    }
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for(int i = 0 ; i < outCount ; i++){
        objc_property_t oc_property = plist[i];
        // objc_property_t是一个结构,将其转换成字符
        const char *property_name = property_getName(oc_property);
        NSString *key = [NSString stringWithUTF8String:property_name];
        [array addObject:key];
    }
    free(plist);
    return array;
}

// 获取某个类的成员变量名列表(区别于属性列表)
+ (NSArray *)getInstanceVariableList:(Class)cls{
    
    unsigned int outCount;
    Ivar *ivarList = class_copyIvarList(cls,&outCount);
    if(outCount == 0) return nil;
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for(unsigned int i = 0 ; i < outCount ; i++){
        Ivar ivar = ivarList[i];
        // Ivar是一个结构,将其转换成字符
        const char *name = ivar_getName(ivar);
        [array addObject:[NSString stringWithUTF8String:name]];
    }
    free(ivarList);
    return array;
}

@end
