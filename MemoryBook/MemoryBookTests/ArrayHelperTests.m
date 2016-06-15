//
//  ArrayHelperTests.m
//  MemoryBook
//
//  Created by Samson on 16/6/14.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "ArrayHelper.h"
#import "MBUser.h"

@interface ArrayHelperTests : XCTestCase

@property (strong,nonatomic) NSArray *testArray;

@end

@implementation ArrayHelperTests

@synthesize testArray = _testArray;

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
    self.testArray  = @[@"1",@"2",@"3",@"4",@"5",@"6",@"7",@"8"];
    NSLog(@"testArray:%@",self.testArray);
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

- (void)testSubFromIndexToLength{
    NSArray *array1 = [ArrayHelper subArray:self.testArray atIndex:0 length:0];
    NSLog(@"array1:%@",array1); // Empty
    XCTAssertNotNil(array1);
    
    NSArray *array2 = [ArrayHelper subArray:self.testArray atIndex:0 length:4];
    NSLog(@"array2:%@",array2); // 1,2,3,4
    
    NSArray *array3 = [ArrayHelper subArray:self.testArray atIndex:0 length:8];
    NSLog(@"array3:%@",array3); // 1,2,3,4,5,6,7,8
    
    NSArray *array4 = [ArrayHelper subArray:self.testArray atIndex:2 length:3];
    NSLog(@"array4:%@",array4); // 3,4,5
    
    NSArray *array5 = [ArrayHelper subArray:self.testArray atIndex:2 length:6];
    NSLog(@"array5:%@",array5); // 3,4,5,6,7,8
    
    NSArray *array6 = [ArrayHelper subArray:self.testArray atIndex:4 length:4];
    NSLog(@"array6:%@",array6); // 5,6,7,8
}

- (void)testArrayHelperException{
    NSArray *array7 = [ArrayHelper subArray:self.testArray atIndex:0 length:9];
    XCTAssertNil(array7); // nil
    
    NSArray *array8 = [ArrayHelper subArray:self.testArray atIndex:2 length:7];
    XCTAssertNil(array8);
    
    NSArray *array9 = [ArrayHelper subArray:self.testArray atIndex:3 length:10];
    XCTAssertNil(array9);
}

- (void)testSubFromIndexToIndex{
    NSArray *array10 = [ArrayHelper subArray:self.testArray atIndex:0 toIndex:0];
    XCTAssertNotNil(array10);
    NSLog(@"array10:%@",array10); // 1
    
    NSArray *array11 = [ArrayHelper subArray:self.testArray atIndex:4 toIndex:4];
    XCTAssertNotNil(array11);
    NSLog(@"array11:%@",array11); // 5
    
    NSArray *array12 = [ArrayHelper subArray:self.testArray atIndex:7 toIndex:7];
    XCTAssertNotNil(array12);
    NSLog(@"array11:%@",array12); // 8
    
    NSArray *array13 = [ArrayHelper subArray:self.testArray atIndex:0 toIndex:7];
    NSLog(@"array13:%@",array13); // 1,2,3,4,5,6,7,8
    
    NSArray *array14 = [ArrayHelper subArray:self.testArray atIndex:3 toIndex:7];
    NSLog(@"array14:%@",array14); // 4,5,6,7,8
    
    NSArray *array15 = [ArrayHelper subArray:self.testArray atIndex:4 toIndex:6];
    NSLog(@"array15:%@",array15); // 5,6,7
    
}

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
    NSArray *sortedArray = [ArrayHelper sortArray:array withKey:@"name_" ascending:YES];
    //     NSArray *sortedArray = [ArrayHelper sortArray:array withKeys:@[@"name",@"age"] ascending:YES];
    
    NSLog(@"sort done");
    [self printArray:sortedArray];
}

- (void)printArray:(NSArray *)array{
    for(MBUser *itr in array){
        NSLog(@"%@:[name:%@,age:%d,cellPhoneNum:%@]",itr,itr.name,itr.age,itr.cellPhoneNum);
    }
}

@end
