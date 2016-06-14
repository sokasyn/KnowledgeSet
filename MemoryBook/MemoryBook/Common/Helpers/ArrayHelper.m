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
    if(!array) return nil;
    
    id obj = [array objectAtIndex:0];
    Class cls = [obj class];
    
    if([RuntimeHelper key:key isPropertyOfClass:cls]){
        NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:key ascending:ascending];
        NSArray *sortedArray = [array sortedArrayUsingDescriptors:@[sortDescriptor]];
        return sortedArray;
    }else{
        NSLog(@"undefined key:%@ in Class %@",key,[obj class]);
        return array;
    }
}

+ (NSArray *)sortArray:(NSArray *)array withKeys:(NSArray *)keys ascending:(BOOL)ascending{
    if(!array) return nil;
    id obj = [array objectAtIndex:0];
    Class cls = [obj class];
    NSMutableArray *sortdescriptors = [[NSMutableArray alloc] init];
    for(NSString *key in keys){
        if([RuntimeHelper key:key isPropertyOfClass:cls]){
            NSSortDescriptor *sortdescriptor = [[NSSortDescriptor alloc] initWithKey:key ascending:ascending];
            [sortdescriptors addObject:sortdescriptor];
        }else{
            NSLog(@"undefined key:%@ in Class %@",key,[obj class]);
        }
    }
    NSArray *sortedArray = [array sortedArrayUsingDescriptors:sortdescriptors];
    return sortedArray;
}


+ (NSArray *)subArray:(NSArray *)array atIndex:(NSUInteger)fromIndex toIndex:(NSUInteger)toIndex{
    if(fromIndex > toIndex || toIndex > [array count] - 1){
        return nil;
    }
    NSUInteger len = (toIndex - fromIndex + 1);
    NSLog(@"len:%lu",(unsigned long)len);
    NSRange range = NSMakeRange(fromIndex, len);
    NSArray *newArray = [array subarrayWithRange:range];
    return newArray;
}

+ (NSArray *)subArray:(NSArray *)array atIndex:(NSUInteger)fromIndex length:(NSUInteger)len{
    if((fromIndex + len) > [array count]) return nil;
    NSRange range = NSMakeRange(fromIndex, len);
    NSArray *newArray = [array subarrayWithRange:range];
    return newArray;
}

@end
