//
//  HJImMessageSendCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
#import "HJIMMessage.h"

@protocol HJImMessageSendCoreClient <NSObject>
@optional
- (void)onWillSendMessage:(NIMMessage *)msg;
- (void)onSendMessageSuccess:(NIMMessage *)msg;

- (void)onSendMessageBanned;

/**
 发送公屏消息成功
 */
- (void)onSendPublicMessageDidSuccess:(NSString *)roomId;

/**
 发送公屏消息失败
 */
- (void)onSendPublicMessageDidFailure:(NSString *)roomId code:(NSInteger)code errorMessage:(NSString *)errorMessage;

@end
