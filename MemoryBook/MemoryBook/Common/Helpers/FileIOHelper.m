//
//  FileIOHelper.m
//  MemoryBook
//
//  Created by Samson on 16/6/14.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "FileIOHelper.h"

@implementation FileIOHelper

+ (id)sharedInstance{
    static dispatch_once_t onceToken;
    static FileIOHelper *instance = nil;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

- (NSString *)getDocumentDirectory{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    return path;
}

- (BOOL)createFileAtPath:(NSString *)path{
    BOOL success = NO;
    NSFileManager *fileMgr = [NSFileManager defaultManager];
    if(![fileMgr fileExistsAtPath:path]){
        NSLog(@"file is not exist");
        BOOL createSuccess = [fileMgr createFileAtPath:path contents:nil attributes:nil];
        if(createSuccess){
            NSLog(@"file created success");
            success = YES;
        }else{
            NSLog(@"file created failed");
        }
    }else{
        NSLog(@"file exist");
    }
    return success;
}

- (void)writeContent:(NSString *)content toFile:(NSString *)filePath{
    NSFileManager *fileMgr = [NSFileManager defaultManager];
    if(![fileMgr fileExistsAtPath:filePath]){
        if(![self createFileAtPath:filePath]){
            return;
        }
    }
    
    NSFileHandle *fileHandler = [NSFileHandle fileHandleForWritingAtPath:filePath];
    if(fileHandler == nil){
        NSLog(@"Open of file for writing failed");
    }
    [fileHandler seekToEndOfFile];
    NSData *data = [content dataUsingEncoding:NSUTF8StringEncoding];
    [fileHandler writeData:data];
    [fileHandler closeFile];
}

@end
