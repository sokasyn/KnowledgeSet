//
//  SecondViewController.m
//  MemoryBook
//
//  Created by 李星月 on 16/5/30.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "SecondViewController.h"
#import "NotificationHelper.h"
#import "MacroNotification.h"

@interface SecondViewController ()

@end

@implementation SecondViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self inits];
}

- (void)inits{
    [self initComponents];
}

- (void)initComponents{
    [self test];
}

- (void)initData{
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)btnDoneClicked:(id)sender {
    
    [self dismissViewControllerAnimated:YES completion:nil];
}


- (void)test{
    [self testNotification];
}

- (void)testNotification{
    NSLog(@"post notification %@",kNotificationTest);
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        [NotificationHelper postNotificatinName:kNotificationTest];
    });
    
    
    
    // post Notification with name
//    [[NSNotificationCenter defaultCenter]postNotificationName:kNotificationTest object:nil];
    
    // post Notification
//    NSNotification *note = [[NSNotification alloc] initWithName:kNotificationTest object:nil userInfo:nil];
//    [[NSNotificationCenter defaultCenter] postNotification:note];
}

@end
