//
//  YPImMessageCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "YPBaseCore.h"
#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
#import "YPGiftReceiveInfo.h"
#import "YPGiftSendInfo.h"
#import "YPAttachment.h"
#import "YPChatRoomMember.h"
#import "YPIMMessage.h"

@interface YPImMessageCore : YPBaseCore

@property (nonatomic, copy) NSString *sensitiveWord;

/**
 发送文本消息(云信IM)

 @param text      文本内容
 @param nick      用户名
 @param sessionId 会话Id
 @param type      会话类型
 */
- (void)sendTextMessage:(NSString *)text nick:(NSString*)nick sessionId:(NSString *)sessionId type:(NIMSessionType)type;

/**
 发送文本消息(房间IM)

 @param text   文本内容
 @param roomId 房间id
 @param member 成员信息
 */
- (void)sendRoomTextMessage:(NSString *)text
                     roomId:(NSString *)roomId
                     member:(YPChatRoomMember *)member
                    success:(void (^)())success
                    failure:(void (^)(NSInteger code, NSString *errorMessage))failure;


/**
 获取未读消息数
 */
- (NSInteger)getUnreadCount;

/**
 发送自定义消息

 @param attachment 发送附件模型
 @param sessionId 回话ID=UID
 @param type 消息类型
 */
- (void)sendCustomMessageAttachement:(YPAttachment *)attachment
                           sessionId:(NSString *)sessionId
                                type:(JXIMSessionType)type;
//发送自定义消息-带推送
- (void)sendCustomMessageAttachement:(YPAttachment *)attachment
                           sessionId:(NSString *)sessionId
                                type:(JXIMSessionType)type
                            needApns:(BOOL)needApns
                         apnsContent:(NSString *)apnsContent
                         yidunEnable:(BOOL)yidunEnable;

//- (void) sendCustomMessage:(id)attachment

/**
 违禁词匹配
 
 @param text 发送文本
 */
- (void)sensitiveWordRegexWithText:(NSString *)text requestId:(NSString *)requestId finishBlock:(void(^)(BOOL isCanSend, NSString *msg))finishBlock;

@end
