//
//  ViewController.m
//  MemoryBook
//
//  Created by Samson Tseng on 16/4/5.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "ViewController.h"
#import "MacroNotification.h"
#import "NotificationHelper.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self test];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

// 测试入口
- (void)test{
    [self testNotification];
}

// ------------  测试NotificationHelper
- (void)testNotification{
    NSLog(@"testNotification begin..");
    [NotificationHelper registerNotification:kNotificationTest paramObject:nil usingBlock:^(NSNotification *note){
        NSLog(@"received Notifitcation named NotificatinTest");
        
        dispatch_async(dispatch_get_main_queue(), ^{
            UILabel *label = (UILabel *)[self.view viewWithTag:100];
            if(label){
                [label setText:kNotificationTest];
            }
        });
    }];
    
//    id observer = [[NSNotificationCenter defaultCenter] addObserverForName:kNotificationTest object:nil queue:[NSOperationQueue mainQueue] usingBlock:^(NSNotification *note){
//        NSLog(@"received Notifitcation named%@",kNotificationTest);
//    }];
}

// ------------  测试ArrayHelper
- (void)testSortStringArray{
    NSArray *array = @[@"bcd",@"123",@"013",@"a000"];
    NSArray *sortedArray = [ArrayHelper sortStringArray:array ascending:NO];
    NSLog(@"sortedArray:%@",sortedArray);
    
    NSArray *array2 = [ArrayHelper sortArray:array withKey:nil ascending:YES];
    NSLog(@"sortedArray 2:%@",array2);
}

- (void)testSortCustomObjectArray{
    MBUser *user1 = [[MBUser alloc] init];
    user1.name = @"bcd";
    user1.age = 18;
    user1.cellPhoneNum = @"123456789";
    
    MBUser *user2 = [[MBUser alloc] init];
    user2.name = @"123";
    user2.age = 20;
    user2.cellPhoneNum = @"123456789";
    
    MBUser *user3 = [[MBUser alloc] init];
    user3.name = @"bcd";
    user3.age = 8;
    user3.cellPhoneNum = @"123456789";
    
    MBUser *user4 = [[MBUser alloc] init];
//    user4.name = @"a000";
    user4.age = 50;
    user4.cellPhoneNum = @"123456789";
    
    NSArray *array = @[user1,user2,user3,user4];
//    [self printArray:array];
//    NSArray *sortedArray = [ArrayHelper sortArray:array withKey:@"name" ascending:YES];
     NSArray *sortedArray = [ArrayHelper sortArray:array withKeys:@[@"name",@"age"] ascending:YES];
    
    NSLog(@"sort done");
    [self printArray:sortedArray];
}

- (void)printArray:(NSArray *)array{
    for(MBUser *itr in array){
        NSLog(@"%@:[name:%@,age:%d,cellPhoneNum:%@]",itr,itr.name,itr.age,itr.cellPhoneNum);
    }
}

@end
