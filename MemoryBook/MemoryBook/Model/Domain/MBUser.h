//
//  MBUser.h
//  MemoryBook
//
//  Created by Sam Tsang on 16/5/25.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MBUser : NSObject

@property (retain, nonatomic) NSString *userId;
@property (retain, nonatomic) NSString *name;
@property (assign, nonatomic) int age;
@property (retain, nonatomic) NSString *cellPhoneNum;

- (id)init;

@end
