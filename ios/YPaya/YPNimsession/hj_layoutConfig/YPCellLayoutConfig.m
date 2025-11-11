//
//  YPCellLayoutConfig.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPCellLayoutConfig.h"
#import "YPSessionCustomContentConfig.h"
#import "YPOpenLiveAttachment.h"
#import "YPRedPacketInfoAttachment.h"
#import "YPNewsInfoAttachment.h"
#import "YPGiftAttachment.h"
#import "YPInviteMicAttachment.h"
#import "YPTurntableAttachment.h"

@interface YPCellLayoutConfig()
@property (nonatomic,strong)    NSArray    *types;
@property (nonatomic,strong) YPSessionCustomContentConfig *sessionCustomconfig;
@end

@implementation YPCellLayoutConfig
- (instancetype)init
{
    if (self = [super init])
    {
        _types =  @[
                    @"YPOpenLiveAttachment",
                    @"YPRedPacketInfoAttachment",
                    @"YPNewsInfoAttachment",
                    @"YPGiftAttachment",
                    @"YPInviteMicAttachment",
                    @"YPTurntableAttachment"
                    ];
        _sessionCustomconfig = [[YPSessionCustomContentConfig alloc] init];
    }
    return self;
}

#pragma mark - YPNIMCellLayoutConfig
- (CGSize)contentSize:(YPNIMMessageModel *)model cellWidth:(CGFloat)width{
    
    NIMMessage *message = model.message;
    //检查是不是当前支持的自定义消息类型
    if ([self isSupportedCustomMessage:message])
    {
        return [_sessionCustomconfig contentSize:width message:message];
    }
    
    
    
    //如果没有特殊需求，就走默认处理流程
    return [super contentSize:model
                    cellWidth:width];
    
}

- (NSString *)cellContent:(YPNIMMessageModel *)model{
    
    NIMMessage *message = model.message;
    //检查是不是当前支持的自定义消息类型
    if ([self isSupportedCustomMessage:message]) {
        return [_sessionCustomconfig cellContent:message];
    }

    
    //如果没有特殊需求，就走默认处理流程
    return [super cellContent:model];
}

- (UIEdgeInsets)contentViewInsets:(YPNIMMessageModel *)model
{
//    NIMMessage *message = model.message;
//    //检查是不是当前支持的自定义消息类型
//    if ([self isSupportedCustomMessage:message]) {
//        return [_sessionCustomconfig contentViewInsets:message];
//    }

    
    //如果没有特殊需求，就走默认处理流程
    return [super contentViewInsets:model];
}

- (UIEdgeInsets)cellInsets:(YPNIMMessageModel *)model
{
    NIMMessage *message = model.message;
    
    //检查是不是聊天室消息
    if (message.session.sessionType == NIMSessionTypeChatroom) {
        return UIEdgeInsetsZero;
    }
    
    //如果没有特殊需求，就走默认处理流程
    return [super cellInsets:model];
}

#pragma mark - Private Method
- (BOOL)isSupportedCustomMessage:(NIMMessage *)message
{
    if (message.messageType != NIMMessageTypeCustom) {//不是自定义消息返回no
        return NO;
    }
    NIMCustomObject *object = message.messageObject;
    YPAttachment *att = (YPAttachment *)object.attachment;
    if (att.first == Custom_Noti_Header_Gift) {
        if (att.second == Custom_Noti_Sub_Gift_Send) {
            return YES;
        }
    }
    
    if (att.first == Custom_Noti_Header_NotiInviteRoom) {
        if (att.second == Custom_Noti_Header_NotiInviteRoom) {
            return YES;
        }
    }
    
    if (att.first == Custom_Noti_Header_RedPacket) {
        return YES;
    }
    
    if (att.first == Custom_Noti_Header_News) {
        if (att.second == Custom_Noti_Sub_News) {
            return YES;
        }
    }
    
    if (att.first == Custom_Noti_Header_CustomMsg) {
        if (att.second == Custom_Noti_Sub_Online_alert) {
            return YES;
        }
    }
    if (att.first == Custom_Noti_Header_Queue) {
        if (att.second == Custom_Noti_Sub_Queue_Invite) {
            return YES;
        }
    }
    if (att.first == Custom_Noti_Header_Turntable) {
        if (att.second == Custom_Noti_Sub_Turntable) {
            return YES;
        }
    }
    return [object isKindOfClass:[NIMCustomObject class]] &&
    [_types indexOfObject:NSStringFromClass([object.attachment class])] != NSNotFound;
    
}


- (BOOL)isSupportedChatroomMessage:(NIMMessage *)message
{
    return message.session.sessionType == NIMSessionTypeChatroom &&
    (message.messageType == NIMMessageTypeText || message.messageType == NIMMessageTypeRobot || [self isSupportedCustomMessage:message]);
}

- (BOOL)isChatroomTextMessage:(NIMMessage *)message
{
    return message.session.sessionType == NIMSessionTypeChatroom &&
    message.messageType == NIMMessageTypeText;
}



@end
