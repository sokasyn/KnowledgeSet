//
//  PGLoginManager.m
//  HBuilder-Hello
//
//  Created by 李星月 on 16/5/18.
//  Copyright © 2016年 DCloud. All rights reserved.
//

#import "PGLoginManager.h"

@implementation PGLoginManager

/* 用户登陆
 * 同步执行成功回调/失败回调
 */
- (void)userLogin:(PGMethod *)param{

    NSString *callbackId = [param.arguments objectAtIndex:0];
    NSString *userName = [param.arguments objectAtIndex:1];
    NSString *password = [param.arguments objectAtIndex:2];
    BOOL success = [self loginWithName:userName passwrod:password];
    if (success) {
        // 执行js的成功回调
        NSLog(@"验证成功,callbackId:%@",callbackId);
        NSString *message = @"登陆成功";
        PDRPluginResult *result = [PDRPluginResult resultWithStatus:PDRCommandStatusOK messageAsString:message];
        [self toCallback:callbackId withReslut:[result toJSONString]];
    }else{
        // 执行js的失败回调
        NSLog(@"验证失败");
        NSString *message = @"登陆失败";
        PDRPluginResult *result = [PDRPluginResult resultWithStatus:PDRCommandStatusError messageAsString:message];
        [self toCallback:callbackId withReslut:[result toJSONString]];
    }
}

// 用户注册
- (NSData *)userRegister:(PGMethod *)param{
    return nil;
}

// 忘记密码
- (NSData *)forgetPassword:(PGMethod *)param{
    NSString *password = @"1234";
    return [self resultWithString:password];
}

/* 用户登出
 * JS传入
 */
- (void)logout:(PGMethod *)param{
    
}

NSInteger static CHECKIN_TIMES = 0;
// 用户签到
- (void)checkin:(PGMethod *)param{
    NSLog(@"%@ called",NSStringFromSelector(_cmd));
    NSString *callbackId = [param.arguments objectAtIndex:0];
    CHECKIN_TIMES ++;
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSLog(@"checkin processing start.....");
        [self processingForTimeInterval:8.0f];
        NSString *message = [NSString stringWithFormat:@"您签到了%ld次",(long)CHECKIN_TIMES];
        PDRPluginResult *result = [PDRPluginResult resultWithStatus:PDRCommandStatusOK messageAsString:message];
        dispatch_async(dispatch_get_main_queue(), ^{
            NSLog(@"checkin processing end call back.....");
           [self toCallback:callbackId withReslut:[result toJSONString]];
        });
    });
    NSLog(@"checkin end");
}

- (BOOL)loginWithName:(NSString *)userName passwrod:(NSString *)password{
    
    [NSThread sleepForTimeInterval:8.0f];
    NSString *USER_NAME = @"Samson";
    NSString *PASSWORD  = @"1234";
    
    BOOL success = NO;
    if ([userName isEqualToString:USER_NAME] && [password isEqualToString:PASSWORD] ) {
        success = YES;
    }
    return success;
}

- (void)processingForTimeInterval:(NSTimeInterval)time{
    [NSThread sleepForTimeInterval:time];
}

@end