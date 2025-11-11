//
//  HJWebSocketReceiveCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ChatRoomMember.h"
#import "ChatRoomInfo.h"
#import "HJRoomMicInfo.h"

@protocol HJWebSocketReceiveCoreClient <NSObject>

@optional
#pragma mark 登录/注销
/**
 服务器提示将断开socket通知

 @param code   错误码（0：成功，其它：失败）
 @param errmsg 错误描述（用于客户端显示）（可无，err=0则无）
 */
- (void)onSocketWillKickOffNotifyMessage:(NSInteger)code errmsg:(NSString *)errmsg;

#pragma mark 房间管理
/**
 进入房间通知

 @param member 进入的成员信息
 */
- (void)onEnterRoomNotifyMessage:(ChatRoomMember *)member;

/**
 退出房间通知

 @param roomId   房间id
 @param uid 退出房间的用户uid
 @param nickName 退出房间的用户昵称
 */
- (void)onExitRoomNotifyMessage:(NSString *)roomId
                            uid:(NSString *)uid
                       nickName:(NSString *)nickName;

/**
 通知：聊天室信息更新通知

 @param roomInfo 房间基本信息
 */
- (void)onRoomInfoUpdatedNotifyMessage:(ChatRoomInfo *)roomInfo;

/**
 通知：聊天室黑名单添加通知

 @param roomId 房间id
 @param member 加入黑名单的用户
 */
- (void)onRoomBlacklistAddNotifyMessage:(NSString *)roomId
                                 member:(ChatRoomMember *)member;

/**
 通知：聊天室黑名单移除通知

 @param roomId 房间id
 @param member 加入黑名单的用户
*/
- (void)onRoomBlacklistRemoveNotifyMessage:(NSString *)roomId
                                    member:(ChatRoomMember *)member;

/**
 通知：聊天室黑名单添加通知
 
 @param roomId 房间id
 @param member 加入管理员的用户
 */
- (void)onRoomManagerAddNotifyMessage:(NSString *)roomId
                               member:(ChatRoomMember *)member;

/**
 通知：聊天室黑名单移除通知
 
 @param roomId 房间id
 @param member 加入管理员的用户
 */
- (void)onRoomManagerRemoveNotifyMessage:(NSString *)roomId
                                  member:(ChatRoomMember *)member;

/**
 通知：踢除特定成员通知
 
 @param roomId        房间id
 @param uid           要移除管理员的用户id
 @param nickName      昵称
 @param avater        头像
 @param reasonType    踢出原因类型
 @param reasonMessage 踢出原因描述
 */
- (void)onRoomMemberKickedNotifyMessage:(NSString *)roomId
                                    uid:(NSString *)uid
                               nickName:(NSString *)nickName
                                 avater:(NSString *)avater
                             reasonType:(JXIMUserKickedReasonType)reasonType
                          reasonMessage:(NSString *)reasonMessage;

/**
 通知：禁言用户添加通知

 @param roomId 房间id
 @param member 加入禁言的用户
 */
- (void)onRoomMemberMuteAddNotifyMessage:(NSString *)roomId
                                  member:(ChatRoomMember *)member;

/**
 通知：禁言用户移除通知

 @param roomId 房间id
 @param member 移除禁言的用户
 */
- (void)onRoomMemberMuteRemoveNotifyMessage:(NSString *)roomId
                                     member:(ChatRoomMember *)member;

#pragma mark 房间队列管理

/**
 更新坑位信息

 @param roomId 当前房间id
 @param key 更新或删除的key
 @param micInfo 麦位信息
 */
- (void)onQueueMicUpdateNotifyMessageRoomId:(NSString *)roomId
                                           key:(NSString *)key
                                       micInfo:(HJRoomMicInfo *)micInfo;

/**
 队列成员被更新通知

 @param roomId 当前房间id
 @param type  整形 1：更新key 2：删除key
 @param key 更新或删除的key
 @param member 房间成员
 */
- (void)onQueueMemberUpdateNotifyMessageRoomId:(NSString *)roomId
                                          type:(NSInteger)type
                                           position:(NSString *)key
                                       member:(ChatRoomMember *)member;


#pragma mark 房间消息管理
/**
 通知：非文本消息转发通知

 @param roomId 房间id
 @param member 成员信息
 @param data   任意json
 */
- (void)onReceiveMessageNotifyMessage:(NSString *)roomId
                               member:(ChatRoomMember *)member
                                 data:(id)data;

/**
 通知：文本消息转发通知

 @param roomId  房间id
 @param member  成员信息
 @param content 文本内容
 */
- (void)onReceiveTextNotifyMessage:(NSString *)roomId
                            member:(ChatRoomMember *)member
                           content:(NSString *)content;

#pragma mark 公屏管理
/**
 通知：公平消息转发通知(含全服消息)

 @param roomId 房间id
 @param data   任意json
 */
- (void)onReceivePublicRoomMessageNotifyMessage:(NSString *)roomId
                                           data:(id)data;

@end
