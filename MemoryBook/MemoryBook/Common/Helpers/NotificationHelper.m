//
//  NotificationHelper.m
//  MemoryBook
//
//  Created by Samson on 16/5/30.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "NotificationHelper.h"

@implementation NotificationHelper

/*
 * 注册广播通知
 */
+ (void)registerNotification:(NSString *)name paramObject:(id)obj usingBlock:(void(^)(NSNotification *note))block{
    NSNotificationCenter *notificationCenter = [NSNotificationCenter defaultCenter];
    [notificationCenter addObserverForName:name object:obj queue:nil usingBlock:block];
}

+ (void)registerNotification:(NSString *)name
                 paramObject:(id)obj
                       queue:(NSOperationQueue *)queue
                  usingBlock:(void(^)(NSNotification *note))block{
    NSNotificationCenter *notificationCenter = [NSNotificationCenter defaultCenter];
    [notificationCenter addObserverForName:name object:obj queue:queue usingBlock:block];
}

/*
 * 发送广播通知
 */
+ (void)postNotificatinName:(NSString *)name{
    NSNotification *note = [[NSNotification alloc] initWithName:name object:nil userInfo:nil];
    [[NSNotificationCenter defaultCenter] postNotification:note];
}

+ (void)postNotificatinName:(NSString *)name paramObject:(id)obj userInfo:(NSDictionary *)userInfo{
    NSNotification *note = [[NSNotification alloc] initWithName:name object:obj userInfo:userInfo];
    [[NSNotificationCenter defaultCenter] postNotification:note];
}


// 移除所有的广播通知监听
+ (void)removeObserver:(id)notificationObserver{
    [[NSNotificationCenter defaultCenter] removeObserver:notificationObserver];
}

// 移除所有的广播通知监听
+ (void)removeObserver:(id)notificationObserver name:(NSString *)notificationName object:(id)notificationSender{
    [[NSNotificationCenter defaultCenter] removeObserver:notificationObserver name:notificationName object:notificationSender];
}

+ (void)removeNotifications:(NSArray *)array{
    
}



@end
