//
//  JSONHelperTests.m
//  MemoryBook
//
//  Created by Samson on 16/6/14.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "JSONHelper.h"
#import "FileIOHelper.h"

@interface JSONHelperTests : XCTestCase

@end

@implementation JSONHelperTests

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

// ------------ 测试JSONHelper
- (void)DISABLE_testJSONHelper{
    
//    [self testDic];
    
    // 来源JOSN文件,转成OC对象
    //    [self testJsonFile];
    
    // 来源JSON字符串,转成OC对象
    //    [self testJsonString];
    
    // OC对象转成JSON字符串
    
    // OC对象写入文件,成json文件
}

- (void)testJsonFile{
    NSString *fileName = @"update";
    NSString *fileType = @"json";
    NSString *path = [[NSBundle mainBundle] pathForResource:fileName ofType:fileType];
    id object = [JSONHelper dictionaryFromJsonFile:path];
    NSLog(@"object:%@",object);
    
    [self printDic:(NSDictionary *)object];
    
    
    if([object isKindOfClass:[NSDictionary class]]){
        NSLog(@"is NSDictionay type");
    }else{
        NSLog(@"is not NSDictionay type");
    }
    
    if([object isKindOfClass:[NSMutableDictionary class]]){
        NSLog(@"is NSMutableDictionary type");
        NSMutableDictionary *dic = (NSMutableDictionary *)object;
        NSString *appId = [dic valueForKey:@"appid"];
        NSLog(@"appId:%@",appId);
        
        id iOS= [dic valueForKey:@"iOS"];
        NSLog(@"iOS class%@",[iOS class]);
        NSLog(@"iOS:%@",iOS);
        
        NSString *note = [(NSDictionary *)[dic valueForKey:@"iOS"] valueForKey:@"note"];
        NSLog(@"note:%@",note);
        
    }else{
        NSLog(@"is not NSMutableDictionary type");
    }
}

- (void)printDic:(NSDictionary *)dic
{
    NSString *tempStr1 = [[dic description] stringByReplacingOccurrencesOfString:@"\\u" withString:@"\\U"];
    NSLog(@"tempStr1:%@",tempStr1);
    
    
    NSString *tempStr2 = [tempStr1 stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""];
    
    NSLog(@"tempStr2:%@",tempStr2);
    
    NSString *tempStr3 = [[@"\"" stringByAppendingString:tempStr2] stringByAppendingString:@"\""];
    NSLog(@"tempStr3:%@",tempStr3);
    
    NSData *tempData = [tempStr3 dataUsingEncoding:NSUTF8StringEncoding];
    NSString *str = [NSPropertyListSerialization propertyListFromData:tempData mutabilityOption:NSPropertyListImmutable format:NULL errorDescription:NULL];
    NSLog(@"dic:%@",str);
}

- (void)testDic{
    NSString *name = @"张三";
    NSDictionary *dic = @{
                          @"name": name,
                          @"number": @{
                                  @"phone": @"123456",
                                  @"电话" : @"座机208"
                                  }
                          };
    NSDictionary *dic2 = @{@"name": name,@"number": @{@"phone": @"123456",@"电话" : @"座机208"}};
    NSLog(@"original dictionary:%@", dic);
    [self printDic:dic];
}

- (void)testJsonString{
    // {"name" : "Samson","telNum": "1234"}
    NSString *jsonString = @"{\"name\" : \"Samson\",\"telNum\": \"1234\"}";
    NSLog(@"jsonString:%@",jsonString);
    if([NSJSONSerialization isValidJSONObject:jsonString]){
        NSLog(@"YES");
    }else{
        NSLog(@"NO");
    }
    
    id object = [JSONHelper dictionaryFromJsonString:jsonString];
    NSLog(@"%@",object);
    NSLog(@"%@",[object class]);
    if([object isKindOfClass:[NSDictionary class]]){
        NSLog(@"is NSDictionay type");
    }else{
        NSLog(@"is not NSDictionay type");
    }
    if([NSJSONSerialization isValidJSONObject:object]){
        NSLog(@"YES");
    }else{
        NSLog(@"NO");
    }
    
    // 带数组的json字符串:{"name" : "Samson","telNum": ["1234","789"]}
    NSString *jsonString2 = @"{\"name\" : \"Samson\",\"telNum\": [\"1234\",\"789\"]}";
    NSLog(@"jsonString2:%@",jsonString2);
    id object2 = [JSONHelper dictionaryFromJsonString:jsonString2];
    NSLog(@"%@",object2);
    NSLog(@"%@",[object2 class]);
}

- (void)testWriteJsonToFile{
    NSString *jsonString = @"{\"name\" : \"Samson\",\"telNum\": \"1234\"}";
    NSString *path = [[FileIOHelper sharedInstance] getDocumentDirectory];
    NSString *filePath = [path stringByAppendingPathComponent:@"user.json"];
    [JSONHelper writeToFile:filePath withJsonString:jsonString];
    
}

@end
