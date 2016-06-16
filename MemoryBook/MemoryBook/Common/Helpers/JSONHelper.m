//
//  JSONHelper.m
//  MemoryBook
//
//  Created by Sam Tsang on 16/5/31.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//
/*
 * 关于NSJSONReadingOptions的说明
 * 1.NSJSONReadingMutableContainers 返回的是可变容器:NSMutableDictionary/NSMutableArray
 * 2.NSJSONReadingMutableLeaves:返回NSMutableString
 * 3.NSJSONReadingAllowFragments:允许JSON字符串最外层既不是NSArray也不是NSDictionary,
     但必须是有效的JSON Fragment,例如NSString, NSNumber 或者 NSNull,
     需要注意的是普通的NSString有问题,例如@"abc",应当为@"\"abc\""有引号！但是@"123"就可以,因为123是纯数字,是有效的JSON Fragment
 */
#import "JSONHelper.h"
#import "FileIOHelper.h"

@implementation JSONHelper

+ (id)dictionaryFromJsonString:(NSString *)jsonString{
    
    NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *error;
    id obj = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
    if(error){
        return nil;
    }
    return (NSMutableDictionary *)obj;
}

+ (id)dictionaryFromJsonFile:(NSString *)path{
    // NSData
    NSData *data = [NSData dataWithContentsOfFile:path];
    NSString *string = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    
//    NSString* pFilePath = (NSString *)CFURLCreateStringByAddingPercentEscapes( kCFAllocatorDefault, (CFStringRef)string, NULL, NULL,  kCFStringEncodingUTF8 );
    NSLog(@"string:%@",string);
    id object = [self dictionaryFromJsonString:string];
    
//    id object = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
    return object;
    /*
    // NSStream
    NSInputStream *inputStream = [NSInputStream inputStreamWithFileAtPath:path];
    [inputStream open];
    NSError *error;
    id object2 = [NSJSONSerialization JSONObjectWithStream:inputStream options:NSJSONReadingMutableLeaves error:&error];
    [inputStream close];
    if(error){
        NSLog(@"error occured:%@",[error domain]);
        return nil;
    }else{
        return object2;
    }*/
}

+ (void)writeToFile:(NSString *)filePath withJsonString:(NSString *)jsonString;{
    
    [[FileIOHelper sharedInstance] createFileAtPath:filePath];
    NSOutputStream *outStream = [[NSOutputStream alloc] initToFileAtPath:filePath append:NO];
    
    NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *error;
    id jsonObj = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    if(error == nil && [NSJSONSerialization isValidJSONObject:jsonObj]){
        NSError *writeError;
        [outStream open];
        [NSJSONSerialization writeJSONObject:jsonObj toStream:outStream options:NSJSONWritingPrettyPrinted error:&writeError];
        if(writeError){
            NSLog(@"======error:%@",[error userInfo]);
        }else{
            NSLog(@"write suceed");
        }
        [outStream close];
    }else{
        NSLog(@"=====error:%@",[error userInfo]);
    }
}

+ (void)writeToFile:(NSString *)filePath withDictionary:(NSDictionary *)dic{
    
}

@end
