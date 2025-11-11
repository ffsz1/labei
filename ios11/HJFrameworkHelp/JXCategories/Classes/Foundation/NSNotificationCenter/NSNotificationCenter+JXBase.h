//
//  NSNotificationCenter+JXBase.h
//  JXCategories
//
//  Created by Colin on 17/1/4.
//  Copyright © 2017年 JuXiao. All rights reserved.
//  Base

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSNotificationCenter (JXBase)

#pragma mark - Base
/**
 在主线程内发送通知(若当前线程为主线程, 为同步发送, 否则为异步发送)

 @param notification 通知
 */
- (void)jx_postNotificationOnMainThread:(NSNotification *)notification;

/**
 在主线程内发送通知(是否阻塞当前线程)

 @param notification 通知
 @param wait 当前线程是否要被阻塞，直到主线程将通知发送完毕
 */
- (void)jx_postNotificationOnMainThread:(NSNotification *)notification waitUntilDone:(BOOL)wait;

/**
 在主线程内发送通知(若当前线程为主线程, 为同步发送, 否则为异步发送)

 @param name   通知名称
 @param object 通知关联对象
 */
- (void)jx_postNotificationOnMainThreadWithName:(NSString *)name object:(id)object;

/**
 在主线程内发送通知(若当前线程为主线程, 为同步发送, 否则为异步发送)

 @param name     通知名称
 @param object   通知关联对象
 @param userInfo 通知携带信息(可为nil)
 */
- (void)jx_postNotificationOnMainThreadWithName:(NSString *)name
                                         object:(id)object
                                       userInfo:(nullable NSDictionary *)userInfo;

/**
 在主线程内发送通知(是否阻塞当前线程)

 @param name     通知名称
 @param object   通知关联对象
 @param userInfo 通知携带信息(可为nil)
 @param wait     当前线程是否要被阻塞，直到主线程将通知发送完毕
 */
- (void)jx_postNotificationOnMainThreadWithName:(NSString *)name
                                         object:(id)object
                                       userInfo:(nullable NSDictionary *)userInfo
                                  waitUntilDone:(BOOL)wait;

@end

NS_ASSUME_NONNULL_END
