//
//  ArrayHelper.m
//  MemoryBook
//
//  Created by Sam Tsang on 16/5/25.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "ArrayHelper.h"
#import <objc/runtime.h>

@implementation ArrayHelper


+ (NSArray *)sortStringArray:(NSArray<NSString *> *)array ascending:(BOOL)ascending{
    NSArray<NSString *> *sortedArray = nil;
    if(!array) return sortedArray;
    if(array && ([array count] == 1)) return array;
    
    NSComparator comparator = ^(NSString *leftString , NSString *rightString){
        if(ascending){
            NSLog(@"ascending");
            return [leftString compare:rightString];
        }else{
            NSLog(@"descending");
            return [rightString compare:leftString];
        }
        
    };
    sortedArray = [array sortedArrayUsingComparator:comparator];
    return sortedArray;
}

+ (NSArray *)sortArray:(NSArray *)array withKey:(NSString *)key ascending:(BOOL)ascending{
    NSArray *sortedArray = array;
    
    id obj = [array objectAtIndex:0];
    NSLog(@"Class of obj:%@",[obj class]);
    if([self key:key isPropertyOfClass:[obj class]]){
        NSLog(@"key:[%@] is property of class:%@",key,[obj class]);
        NSSortDescriptor *sortdescriptor = [[NSSortDescriptor alloc] initWithKey:key ascending:ascending];
        sortedArray = [array sortedArrayUsingDescriptors:@[sortdescriptor]];
    }else{
        NSLog(@"对象不存在该属性:%@,不做排序处理,原数组放回",key);
    }
    return sortedArray;
}

+ (NSArray *)sortArray:(NSArray *)array withKeys:(NSArray *)keys ascending:(BOOL)ascending{
    NSArray *sortedArray = nil;
    NSMutableArray *sortdescriptors = [[NSMutableArray alloc] init];
    for(NSString *key in keys){
        NSSortDescriptor *sortdescriptor = [[NSSortDescriptor alloc] initWithKey:key ascending:ascending];
        [sortdescriptors addObject:sortdescriptor];
    }
    sortedArray = [array sortedArrayUsingDescriptors:sortdescriptors];
    return sortedArray;
}

+(BOOL)key:(NSString *)key isPropertyOfClass:(Class)class{
    BOOL isProperty = NO;
    
    unsigned int outCount, i;
    objc_property_t *properties = class_copyPropertyList(class, &outCount);
    for (i=0; i<outCount; i++) {
        objc_property_t property = properties[i];
        NSString * key = [[NSString alloc]initWithCString:property_getName(property)  encoding:NSUTF8StringEncoding];
        NSLog(@"property[%d] :%@ \n", i, key);
    }
    
    objc_property_t property = class_getProperty(class, [key cStringUsingEncoding:NSUTF8StringEncoding]);
    if(property != NULL){
        NSLog(@"Got property");
        isProperty = YES;
    }else{
        NSLog(@"No such property");
    }
    return isProperty;
}



@end
