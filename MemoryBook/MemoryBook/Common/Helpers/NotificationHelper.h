//
//  NotificationHelper.h
//  MemoryBook
//
//  Created by Samson on 16/5/30.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//
/*
 应用开发中需要用到自定义Notification,为了能方便的管理这些自定义的Notification,
 使得统一,不重复定义等
 */

#import <Foundation/Foundation.h>

@interface NotificationHelper : NSObject

/*
 * 注册广播通知,block回调是在发送通知的线程中执行
 * 如果发送通知是在子线程,那么需要更新UI的时候,则需要在block中回到主线程来操作
 * @param name 名字
 * @param obj Notification所携带的参数对象
 * @param block 接收到该Notification的回调,直接在注册的时候可以用,取代了传统的单独一个方法的方式
 */
+ (void)registerNotification:(NSString *)name paramObject:(id)obj usingBlock:(void(^)(NSNotification *note))block;

/*
 * 注册广播通知,block回调是在queue中执行
 * @param name 名字
 * @param obj Notification所携带的参数对象
 * @param queue 执行回调所在的queue,如[NSOperationQueue mainQueue]则是在主线程中执行回调
 * @param block 接收到该Notification的回调,直接在注册的时候可以用,取代了传统的单独一个方法的方式
 */
+ (void)registerNotification:(NSString *)name
                 paramObject:(id)obj
                       queue:(NSOperationQueue *)queue
                  usingBlock:(void(^)(NSNotification *note))block;

/*
 * 发送广播通知
 */
+ (void)postNotificatinName:(NSString *)name;
+ (void)postNotificatinName:(NSString *)name paramObject:(id)obj userInfo:(NSDictionary *)userInfo;

/*
 * 移除所有的广播通知监听
 * @param notificationObserver 注册广播通知的对象
 */
+ (void)removeObserver:(id)notificationObserver;

/*
 * 取消监听某个对象发出的广播通知
 * @param notificationObserver 注册广播通知的对象
 * @param notificationName 通知的名字
 * @param notificationSender 发送通知的对象
 */
+ (void)removeObserver:(id)notificationObserver name:(NSString *)notificationName object:(id)notificationSender;

/*
 * 批量移除广播通知的监听(待定)
 */
+ (void)removeNotifications:(NSArray *)array;


@end
