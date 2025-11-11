//
//  YPIMMessage.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"
#import "YPChatRoomMember.h"
#import "YPAttachment.h"

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, HJIMMessageType){ ///< 消息类型
    HJIMMessageTypeText         = 0,         ///< 文本类型消息
    HJIMMessageTypeNotification = 5,         ///< 通知类型消息
    HJIMMessageTypeCustom       = 100,       ///< 自定义类型消息
};

typedef NS_ENUM(NSInteger, JXIMSessionType){ ///< 会话类型
    JXIMSessionTypeP2P      = 0,             ///< 点对点
    JXIMSessionTypeChatroom = 2,             ///< 聊天室
};

typedef NS_ENUM(NSInteger, JXIMNotificationType){ ///< 通知类型
    JXIMNotificationTypeChatroom = 3,             ///< 聊天室通知
};

typedef NS_ENUM(NSInteger, JXIMChatroomEventType){ ///< 聊天室操作类型
    JXIMChatroomEventTypeEnter = 301,              ///< 成员进入聊天室
};

@protocol YPIMMessage <NSObject>

@end

/**
 *  聊天室通知内容
 */
@interface JXIMChatroomNotificationContent : YPBaseObject

@property (nonatomic,assign) JXIMChatroomEventType eventType; ///< 聊天室操作类型

@property (nonatomic,assign) JXIMNotificationType notificationType; ///< 通知类型

@end


/**
 自定义消息内容
 */
@interface JXIMCustomObject : YPBaseObject

@property (nonatomic, strong) YPAttachment *attachment; ///< 自定义消息附件内容
@property (nonatomic, strong) JXIMChatroomNotificationContent *notificationContent; ///< 聊天室通知内容

@end


@interface JXIMSession : YPBaseObject

@property (nonatomic, copy) NSString *sessionId;           ///< 会话Id
@property (nonatomic, assign) JXIMSessionType sessionType; ///< 会话类型

@end


@interface YPIMMessage : YPBaseObject<YPIMMessage>

@property (nonatomic, copy) NSString *messageId;          ///< 消息ID,唯一标识
@property (nonatomic,assign) HJIMMessageType messageType; ///< 消息类型
@property (nullable,nonatomic,copy) NSString *text;       ///< 消息文本
@property (nonatomic, strong) JXIMSession *session;       ///< 会话类型
@property (nullable,nonatomic,copy) NSString *from;       ///< 消息来源
@property (nonatomic,assign) NSTimeInterval timestamp;    ///< 消息发送时间

@property (nonatomic, strong) YPChatRoomMember *member;          ///< 消息来源(房间)
@property (nonatomic, strong) JXIMCustomObject *messageObject; ///< 消息附件内容

@property (nonatomic, strong) NSDictionary *NIMRawData; ///< 云信数据原始类型

@property (nonatomic, copy) NSString *server_msg_id; ///< 服务器生成消息Id

@end

NS_ASSUME_NONNULL_END
