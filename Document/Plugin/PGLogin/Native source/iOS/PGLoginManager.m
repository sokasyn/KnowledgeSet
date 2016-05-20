//
//  PGLoginManager.m
//  Plugin-test
//
//  Created by Samson on 16/5/18.
//  Copyright © 2016年 Emin. All rights reserved.
//

#import "PGLoginManager.h"

@implementation PGLoginManager

#define kUserName @"Samson"
#define kPasswrod @"123"

#pragma mark -userLogin
/*
 * 用户登陆
 * js将成功和失败回调函数封装在一起,只生成一个callbackId,
 * 需要通过PDRPluginResult中的status:PDRCommandStatusOK和PDRCommandStatusError来分别执行成功和失败的回调
 * 比如,如果两个PDRPluginResult都用PDRCommandStatusError这个状态,
 * 则执行的JS中的回调都是bridge.callbackId(success,failure)都中的failure,即第二个回调函数
 */
- (void)userLogin:(PGMethod *)param{

    // 关于param对象,详见[printParam]方法的打印结果
    if(!param || [param.arguments isEqual:[NSNull null]] ){
        return;
    }
    [self printParam:param];
    
    NSString *userName = [param.arguments objectAtIndex:0];
    NSString *password = [param.arguments objectAtIndex:1];
    NSString *callbackId = [param.arguments objectAtIndex:2];
    
    BOOL success = [self loginWithName:userName passwrod:password];
    NSString *message = nil;
    PDRPluginResult *result = nil;
    if (success) {
        message = @"登陆成功";
        result = [PDRPluginResult resultWithStatus:PDRCommandStatusOK messageAsString:message];
    }else{
        message = @"登陆失败";
        result = [PDRPluginResult resultWithStatus:PDRCommandStatusError messageAsString:message];
    }
    [self toCallback:callbackId withReslut:[result toJSONString]];  //返回的只是message,不是JSON格式的message
}

#pragma mark -userRegister
/*
 * 用户注册
 * js将成功和失败的回调函数都单独生成了callbackId,
 * 则toSucessCallback方法执行回调只需要通过对应的callbackId即可,PDRCommandStatusOK/Error则没有影响
 */
- (void)userRegister:(PGMethod *)param{
    
    if(!param || [param.arguments isEqual:[NSNull null]] ){
        return;
    }
    NSString *userName = [param.arguments objectAtIndex:0];
    NSString *successCallbackId = [param.arguments objectAtIndex:1];
    NSString *failCallbackId = [param.arguments objectAtIndex:2];
    BOOL success = NO;
    if([userName isEqualToString:kUserName]){
        success = YES;
    }
    NSString *message = nil;
    if(success){
        message = [NSString stringWithFormat:@"用户名[%@]注册成功!",userName];
        PDRPluginResult *result = [PDRPluginResult resultWithStatus:PDRCommandStatusOK messageAsString:message];
        
        // toSucessCallback 在status为PDRCommandStatusOK和PDRCommandStatusError都ok,并且返回的是JSON格式的数据,包括status,message等
        [self toSucessCallback:successCallbackId withString:[result toJSONString]];
        //[self toCallback:successCallbackId withReslut:[result toJSONString]];  // OK,PDRCommandStatusError行不通,不知道是不是SDK的bug
    }else{
        message = [NSString stringWithFormat:@"用户名[%@]非法,注册失败!",userName];
        PDRPluginResult *result = [PDRPluginResult resultWithStatus:PDRCommandStatusError messageAsString:message];
        [self toSucessCallback:failCallbackId withString:[result toJSONString]];  // OK,StatusOK和StatusError都行
        //[self toCallback:failCallbackId withReslut:[result toJSONString]];  // 该方式在PDRCommandStatusError NG
        //[self toErrorCallback:failCallbackId withCode:101 withMessage:[result toJSONString]]; // 这种方式NG
    }
}

#pragma mark -forgetPassword
/*
 * 忘记密码
 * 参数只有一个,无回调,直接返回结果,js得用同步调用方式exeSync()才能确保得到正确结果
 */
- (NSData *)forgetPassword:(PGMethod *)param{
    if(!param || [param.arguments isEqual:[NSNull null]] ){
        return nil;
    }
    [self printParam:param];
    NSString *userName = [param.arguments objectAtIndex:0];
    NSString *message = nil;
    if ([userName isEqualToString:kUserName]) {
        NSString *password = kPasswrod;
        message = [NSString stringWithFormat:@"密码:[%@]",password];
    }else{
        message = [NSString stringWithFormat:@"未查询到用户[%@]的密码",userName];
    }
    return [self resultWithString:message];
}

#pragma mark -logout
/*
 * 用户登出
 * js调用时,忽略参数列表,即参数都不写的情况,返回值是boolean类型的字符串
 * 测试结果:js不写参数，param不是nil,其属性arguments也不是nil,而是一个NSNull对象
 */
- (NSData *)logout:(PGMethod *)param{
    if(param){
        NSLog(@"html id:%@",param.htmlID);            // NWindow111463731459266
        NSLog(@"featureName:%@",param.featureName);   // PGLogin
        NSLog(@"functionName:%@",param.functionName); // logout
        NSLog(@"callBackID:%@",param.callBackID);     // (null)
        NSLog(@"sid:%@",param.sid);                   // (null)
        NSLog(@"arguments:%@",param.arguments);       // <null>
        NSLog(@"arguments class:%@",NSStringFromClass([param.arguments class])); // NSNull
    }
    BOOL success = YES;
    return [self resultWithBool:success];
}

#pragma mark -checkin
NSInteger static CHECKIN_TIMES = 0;

// 用户签到
- (void)checkin:(PGMethod *)param{

    [self printParam:param];
    NSString *callbackId = [param.arguments objectAtIndex:0];
    CHECKIN_TIMES ++;
    NSString *message = [NSString stringWithFormat:@"您签到了%ld次",(long)CHECKIN_TIMES];
    PDRPluginResult *result = [PDRPluginResult resultWithStatus:PDRCommandStatusOK messageAsString:message];
    [self toCallback:callbackId withReslut:[result toJSONString]];
}


#pragma mark - pravite

- (BOOL)loginWithName:(NSString *)userName passwrod:(NSString *)password{
    BOOL success = NO;
    if ([userName isEqualToString:kUserName] && [password isEqualToString:kPasswrod] ) {
        success = YES;
    }
    return success;
}

/*
 * 查看PGMethod对象的属性
 * 需要注意的是,js无传入参数时arguments为NSNull的对象即[NSNull null]
 */
- (void)printParam:(PGMethod *)param{
    if(param){
        NSLog(@"html id:%@",param.htmlID);
        NSLog(@"featureName:%@",param.featureName);
        NSLog(@"functionName:%@",param.functionName);
        NSLog(@"callBackID:%@",param.callBackID);
        NSLog(@"sid:%@",param.sid);
        NSLog(@"arguments:%@",param.arguments);
        [self printParamArguments:param];
    }
}

// 查看PGMethod对象中的arguments属性,也就是js传入的参数数组
- (void)printParamArguments:(PGMethod *)param{
    if(!param || [param.arguments isEqual:[NSNull null]]) return;
    NSMutableString *result = [[NSMutableString alloc] init];
    NSUInteger length = [param.arguments count];
    NSLog(@"arguments' count:%lu",(unsigned long)length);
    for(int i = 0 ; i < length ; i++){
        NSString *pParam = [param.arguments objectAtIndex:i];
        if (i == 0) {
            [result appendString:@"["];
        }

        if(pParam && ![pParam isEqual:[NSNull null]]){
            [result appendString:pParam];
        }else{
            [result appendString:@"null"];
        }
        
        if (i == (length-1)) {
            [result appendString:@"]"];
        }else{
            [result appendString:@","];
        }
    }
    NSLog(@"%@",result);
}

- (void)processingForTimeInterval:(NSTimeInterval)time{
    [NSThread sleepForTimeInterval:time];
}

@end
