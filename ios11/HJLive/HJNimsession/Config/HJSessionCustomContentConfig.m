//
//  HJSessionCustomContentConfig.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSessionCustomContentConfig.h"
#import "HJCustomAttachmentInfo.h"
#import "Attachment.h"
#import "HJOpenLiveAttachment.h"
#import "HJRedPacketInfoAttachment.h"
#import "HJNewsInfoAttachment.h"
#import "HJGiftAttachment.h"
#import "HJInviteMicAttachment.h"
#import "NSObject+YYModel.h"
#import "HJInviteFriendToRoomAttachment.h"
#import "HJSendGoldAttachment.h"
@implementation HJSessionCustomContentConfig

- (CGSize)contentSize:(CGFloat)cellWidth message:(NIMMessage *)message
{
    NIMCustomObject *object = (NIMCustomObject *)message.messageObject;
    NSAssert([object isKindOfClass:[NIMCustomObject class]], @"message must be custom");
    id<HJCustomAttachmentInfo> info = (id<HJCustomAttachmentInfo>)object.attachment;
    Attachment *att = (Attachment *)info;
    if (att.first == Custom_Noti_Header_CustomMsg) {
        if (att.second == Custom_Noti_Sub_Online_alert) {
            HJOpenLiveAttachment *attachment = [HJOpenLiveAttachment yy_modelWithJSON:att.data];
            return [attachment contentSize:message cellWidth:cellWidth];
        }
    }else if (att.first == Custom_Noti_Header_Gift) {
        if (att.second == Custom_Noti_Sub_Gift_Send) {
            HJGiftAttachment *attachment = [HJGiftAttachment yy_modelWithJSON:att.data];
            return [attachment contentSize:message cellWidth:cellWidth];
        }
    }else if (att.first == Custom_Noti_Header_NotiInviteRoom) {
        if (att.second == Custom_Noti_Header_NotiInviteRoom) {
            HJInviteFriendToRoomAttachment *attachment = [HJInviteFriendToRoomAttachment yy_modelWithJSON:att.data];
            return [attachment contentSize:message cellWidth:cellWidth];
        }
    }else if (att.first == Custom_Noti_Header_RedPacket) {
        HJRedPacketInfoAttachment *attachment = [HJRedPacketInfoAttachment yy_modelWithJSON:att.data];
        return [attachment contentSize:message cellWidth:cellWidth];
    }else if (att.first == Custom_Noti_Header_News) {
        if (att.second == Custom_Noti_Sub_News) {
            HJNewsInfoAttachment *attachment = [HJNewsInfoAttachment yy_modelWithJSON:att.data];
            return [attachment contentSize:message cellWidth:cellWidth];
        }
    }else if (att.first == Custom_Noti_Header_Queue){
        if (att.second == Custom_Noti_Sub_Queue_Invite) {
            HJInviteMicAttachment *attachment = [HJInviteMicAttachment yy_modelWithJSON:att.data];
            return [attachment contentSize:message cellWidth:cellWidth];
        }
    }else if (att.first == 51){
        if (att.second == 1) {
            HJSendGoldAttachment *attachment = [HJSendGoldAttachment yy_modelWithJSON:att.data];
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
    
    Attachment *att = (Attachment *)info;
    if (att.first == Custom_Noti_Header_CustomMsg) {
        if (att.second == Custom_Noti_Sub_Online_alert) {
            HJOpenLiveAttachment *attachment = [HJOpenLiveAttachment yy_modelWithJSON:att.data];;
            return [attachment cellContent:message];
        }
    }else if (att.first == Custom_Noti_Header_Gift) {
        if (att.second == Custom_Noti_Sub_Gift_Send) {
            HJGiftAttachment *attachment = [HJGiftAttachment yy_modelWithJSON:att.data];
            return [attachment cellContent:message];
        }
    }else if (att.first == Custom_Noti_Header_NotiInviteRoom) {
        if (att.second == Custom_Noti_Header_NotiInviteRoom) {
            HJInviteFriendToRoomAttachment *attachment = [HJInviteFriendToRoomAttachment yy_modelWithJSON:att.data];
            return [attachment cellContent:message];
        }
    }else if (att.first == Custom_Noti_Header_RedPacket) {
        HJRedPacketInfoAttachment *attachment = [HJRedPacketInfoAttachment yy_modelWithJSON:att.data];
        return [attachment cellContent:message];
    }else if (att.first == Custom_Noti_Header_News) {
        if (att.second == Custom_Noti_Sub_News) {
            HJNewsInfoAttachment *attachment = [HJNewsInfoAttachment yy_modelWithJSON:att.data];
            return [attachment cellContent:message];
        }
    }else if (att.first == Custom_Noti_Header_Queue){
        if (att.second == Custom_Noti_Sub_Queue_Invite) {
            HJInviteMicAttachment *attachment = [HJInviteMicAttachment yy_modelWithJSON:att.data];
            return [attachment cellContent:message];
        }
    }else if (att.first == 51){
        if (att.second == 1) {
            HJSendGoldAttachment *attachment = [HJSendGoldAttachment yy_modelWithDictionary:att.data];
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
