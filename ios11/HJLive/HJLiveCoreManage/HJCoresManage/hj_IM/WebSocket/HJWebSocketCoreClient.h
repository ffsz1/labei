//
//  HJWebSocketCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

//@class WebSocketCore;

@protocol HJWebSocketCoreClient <NSObject>

@optional
/**
 *  断线及其原因
 */
- (void)onDisconnect:(NSInteger)code reason:(NSString *)reason;

/**
 *  收到数据
 */
- (void)onReceiveMessage:(id)msg;

/**
 *  error
 */
- (void)onError:(NSString *)msg;

/**
 *  连接成功
 */
- (void)onConnectSuccess;

/**
 *  socket登录成功
 */
- (void)onSocLoginSuccess;

/**
 *  socket登录失败
 */
- (void)onSocLoginFail;

/**
 *  创建新的socket
 */
- (void)createNewConnectSocket;

@end
