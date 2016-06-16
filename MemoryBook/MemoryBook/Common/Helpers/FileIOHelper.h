//
//  FileIOHelper.h
//  MemoryBook
//
//  Created by Samson on 16/6/14.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "MBHelper.h"

@interface FileIOHelper : MBHelper

+ (id)sharedInstance;
- (NSString *)getDocumentDirectory;
- (BOOL)createFileAtPath:(NSString *)path;

// 如果文件不存在,则先穿件再追加写入内容
- (void)writeContent:(NSString *)content toFile:(NSString *)filePath;

@end
