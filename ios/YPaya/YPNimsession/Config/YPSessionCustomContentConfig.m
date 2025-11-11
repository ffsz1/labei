//
//  YPSessionCustomContentConfig.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPSessionCustomContentConfig.h"
#import "HJCustomAttachmentInfo.h"
#import "YPAttachment.h"
#import "YPOpenLiveAttachment.h"
#import "YPRedPacketInfoAttachment.h"
#import "YPNewsInfoAttachment.h"
#import "YPGiftAttachment.h"
#import "YPInviteMicAttachment.h"
#import "NSObject+YYModel.h"
#import "YPInviteFriendToRoomAttachment.h"

@implementation YPSessionCustomContentConfig

- (CGSize)contentSize:(CGFloat)cellWidth message:(NIMMessage *)message
{
    NIMCustomObject *object = (NIMCustomObject *)message.messageObject;
    NSAssert([object isKindOfClass:[NIMCustomObject class]], @"message must be custom");
    id<HJCustomAttachmentInfo> info = (id<HJCustomAttachmentInfo>)object.attachment;
    YPAttachment *att = (YPAttachment *)info;
    if (att.first == Custom_Noti_Header_CustomMsg) {
        if (att.second == Custom_Noti_Sub_Online_alert) {
            YPOpenLiveAttachment *attachment = [YPOpenLiveAttachment yy_modelWithJSON:att.data];
            return [attachment contentSize:message cellWidth:cellWidth];
        }
    }else if (att.first == Custom_Noti_Header_Gift) {
        if (att.second == Custom_Noti_Sub_Gift_Send) {
            YPGiftAttachment *attachment = [YPGiftAttachment yy_modelWithJSON:att.data];
            return [attachment contentSize:message cellWidth:cellWidth];
        }
    }else if (att.first == Custom_Noti_Header_NotiInviteRoom) {
        if (att.second == Custom_Noti_Header_NotiInviteRoom) {
            YPInviteFriendToRoomAttachment *attachment = [YPInviteFriendToRoomAttachment yy_modelWithJSON:att.data];
            return [attachment contentSize:message cellWidth:cellWidth];
        }
    }else if (att.first == Custom_Noti_Header_RedPacket) {
        YPRedPacketInfoAttachment *attachment = [YPRedPacketInfoAttachment yy_modelWithJSON:att.data];
        return [attachment contentSize:message cellWidth:cellWidth];
    }else if (att.first == Custom_Noti_Header_News) {
        if (att.second == Custom_Noti_Sub_News) {
            YPNewsInfoAttachment *attachment = [YPNewsInfoAttachment yy_modelWithJSON:att.data];
            return [attachment contentSize:message cellWidth:cellWidth];
        }
    }else if (att.first == Custom_Noti_Header_Queue){
        if (att.second == Custom_Noti_Sub_Queue_Invite) {
            YPInviteMicAttachment *attachment = [YPInviteMicAttachment yy_modelWithJSON:att.data];
            return [attachment contentSize:message cellWidth:cellWidth];
        }
    }
    
    
    return [info contentSize:message cellWidth:cellWidth];
}

- (NSString *)cellContent:(NIMMessage *)message
{
    NIMCustomObject *object = (NIMCustomObject *)message.messageObject;
    NSAssert([object isKindOfClass:[NIMCustomObject class]], @"message must be custom");
    id<HJCustomAttachmentInfo> info = (id<HJCustomAttachmentInfo>)object.attachment;
    
    YPAttachment *att = (YPAttachment *)info;
    if (att.first == Custom_Noti_Header_CustomMsg) {
        if (att.second == Custom_Noti_Sub_Online_alert) {
            YPOpenLiveAttachment *attachment = [YPOpenLiveAttachment yy_modelWithJSON:att.data];;
            return [attachment cellContent:message];
        }
    }else if (att.first == Custom_Noti_Header_Gift) {
        if (att.second == Custom_Noti_Sub_Gift_Send) {
            YPGiftAttachment *attachment = [YPGiftAttachment yy_modelWithJSON:att.data];
            return [attachment cellContent:message];
        }
    }else if (att.first == Custom_Noti_Header_NotiInviteRoom) {
        if (att.second == Custom_Noti_Header_NotiInviteRoom) {
            YPInviteFriendToRoomAttachment *attachment = [YPInviteFriendToRoomAttachment yy_modelWithJSON:att.data];
            return [attachment cellContent:message];
        }
    }else if (att.first == Custom_Noti_Header_RedPacket) {
        YPRedPacketInfoAttachment *attachment = [YPRedPacketInfoAttachment yy_modelWithJSON:att.data];
        return [attachment cellContent:message];
    }else if (att.first == Custom_Noti_Header_News) {
        if (att.second == Custom_Noti_Sub_News) {
            YPNewsInfoAttachment *attachment = [YPNewsInfoAttachment yy_modelWithJSON:att.data];
            return [attachment cellContent:message];
        }
    }else if (att.first == Custom_Noti_Header_Queue){
        if (att.second == Custom_Noti_Sub_Queue_Invite) {
            YPInviteMicAttachment *attachment = [YPInviteMicAttachment yy_modelWithJSON:att.data];
            return [attachment cellContent:message];
        }
    }
    
    return [info cellContent:message];
}

- (UIEdgeInsets)contentViewInsets:(NIMMessage *)message
{
    NIMCustomObject *object = (NIMCustomObject *)message.messageObject;
    NSAssert([object isKindOfClass:[NIMCustomObject class]], @"message must be custom");
    id<HJCustomAttachmentInfo> info = (id<HJCustomAttachmentInfo>)object.attachment;
    return [info contentViewInsets:message];
}

@end
