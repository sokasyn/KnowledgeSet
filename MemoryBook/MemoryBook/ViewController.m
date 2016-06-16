//
//  ViewController.m
//  MemoryBook
//
//  Created by Samson Tseng on 16/4/5.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "ViewController.h"
#import "FileIOHelper.h"
#import "DateTimeHelper.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self setup];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#define kFileName @"test.log"
#define kWriteCount 5
- (void)setup{

}


- (IBAction)btnGoClick:(id)sender {
    [self start];
}

- (void)start{
    FileIOHelper *fileHelper = [FileIOHelper sharedInstance];
    NSString *docDir = [fileHelper getDocumentDirectory];
    
    NSString *filePath = [docDir stringByAppendingPathComponent:kFileName];
    NSLog(@"filePath:%@",filePath);
    [fileHelper createFileAtPath:filePath];
    
    DateTimeHelper *dateTimeHelper = [DateTimeHelper sharedInstance];
    for(int i = 0 ; i < kWriteCount ; i ++){
        NSString *time = [dateTimeHelper getCurrentTime];
        NSString *content = [NSString stringWithFormat:@"%@ %d\r\n",time,i];
        [fileHelper writeContent:content toFile:filePath];
        [NSThread sleepForTimeInterval:2];
    }
}


@end
