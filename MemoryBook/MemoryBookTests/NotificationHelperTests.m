//
//  NotificationHelperTests.m
//  MemoryBook
//
//  Created by Samson on 16/6/15.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "MacroNotification.h"
#import "NotificationHelper.h"

@interface NotificationHelperTests : XCTestCase

@end

@implementation NotificationHelperTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testExample {
    // This is an example of a functional test case.
    // Use XCTAssert and related functions to verify your tests produce the correct results.
}

- (void)testPerformanceExample {
    // This is an example of a performance test case.
    [self measureBlock:^{
        // Put the code you want to measure the time of here.
    }];
}

- (void)testNotification{
    [NotificationHelper registerNotification:kNotificationTest paramObject:nil usingBlock:^(NSNotification *note){
        NSLog(@"received Notifitcation named NotificatinTest");
        dispatch_async(dispatch_get_main_queue(), ^{
            // Update UI Code.etc
            /*
            UILabel *label = (UILabel *)[self.view viewWithTag:100];
            if(label){
                [label setText:kNotificationTest];
            }*/
        });
    }];
    
    //    id observer = [[NSNotificationCenter defaultCenter] addObserverForName:kNotificationTest object:nil queue:[NSOperationQueue mainQueue] usingBlock:^(NSNotification *note){
    //        NSLog(@"received Notifitcation named%@",kNotificationTest);
    //    }];
}

@end
