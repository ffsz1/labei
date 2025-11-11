//
//  HJCustomAttachmentDecoder.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJCustomAttachmentDecoder.h"
#import "Attachment.h"
#import "HJOpenLiveAttachment.h"
#import "HJRedPacketInfoAttachment.h"
#import "HJNewsInfoAttachment.h"
#import "HJGiftAttachment.h"
#import "HJInviteMicAttachment.h"
#import "NSObject+YYModel.h"
#import "UserInfo.h"
#import "HJGiftCore.h"
#import "GiftReceiveInfo.h"
#import "NSString+JsonToDic.h"
#import "TurntableAttachment.h"
#import "HJInviteFriendToRoomAttachment.h"

@implementation HJCustomAttachmentDecoder

- (id<NIMCustomAttachment>)decodeAttachment:(NSString *)content {
    id<NIMCustomAttachment> attachment = nil;
    NSData *data = [content dataUsingEncoding:NSUTF8StringEncoding];
    if (data) {
        NSDictionary *dict = [NSJSONSerialization JSONObjectWithData:data
                                                             options:0
                                                           error:nil];
        if ([dict isKindOfClass:[NSDictionary class]])
        {
            NSInteger first = [dict[@"first"] integerValue];
            NSInteger second = [dict[@"second"] integerValue];
            
            NSDictionary *data = dict[@"data"];
            if ([data isKindOfClass:[NSString class]]) {
                data = [NSString dictionaryWithJsonString:(NSString *)data];
            }
            if ([data isKindOfClass:[NSDictionary class]]) {
                if (first == Custom_Noti_Header_CustomMsg) {
                    if (second == Custom_Noti_Sub_Online_alert) {
                        
                        UserInfo *user = [[UserInfo alloc]init];
                        if (dict[@"userVo"] && [dict[@"userVo"] isKindOfClass:[NSDictionary class]]) {
                            user = [UserInfo yy_modelWithJSON:dict];
                        }
                        
                        NSString *uid = [NSString stringWithFormat:@"%@",data[@"uid"]];
                        
                        if (user.nick.length > 0) {
                            attachment = [[HJOpenLiveAttachment alloc]init];
                            ((HJOpenLiveAttachment *)attachment).avatar = user.avatar;
                            ((HJOpenLiveAttachment *)attachment).nick = user.nick;
                            ((HJOpenLiveAttachment *)attachment).uid = user.uid;
                            return attachment;
                        }else {
                            attachment = [[HJOpenLiveAttachment alloc]init];
                            ((HJOpenLiveAttachment *)attachment).uid = uid.userIDValue;
                            
                        }
                        ((HJOpenLiveAttachment *)attachment).first = (short)first;
                        ((HJOpenLiveAttachment *)attachment).second = (short)second;
                        ((HJOpenLiveAttachment *)attachment).data = data;
                    }
                    
                }else if (first == Custom_Noti_Header_Gift) {
                    if (second == Custom_Noti_Sub_Gift_Send) {
                        attachment = [[HJGiftAttachment alloc]init];
                        GiftInfo * info = [GetCore(HJGiftCore) findGiftInfoByGiftId:[data[@"giftId"]integerValue] giftyType:GiftTypeNormal];
                        if (info == nil) {
                            info = [GetCore(HJGiftCore) findGiftInfoByGiftId:[data[@"giftId"]integerValue] giftyType:GiftTypeMystic];
                        }
                        ((HJGiftAttachment *)attachment).giftName = [NSString stringWithFormat:@"%@",info.giftName];
                        ((HJGiftAttachment *)attachment).giftPic = [NSString stringWithFormat:@"%@",info.giftUrl];
                        ((HJGiftAttachment *)attachment).giftNum = [NSString stringWithFormat:@"%@",data[@"giftNum"]];
                        ((HJGiftAttachment *)attachment).first = (short)first;
                        ((HJGiftAttachment *)attachment).second = (short)second;
                        ((HJGiftAttachment *)attachment).data = data;
                    }
                }else if (first == Custom_Noti_Header_NotiInviteRoom) {
                    if (second == Custom_Noti_Header_NotiInviteRoom) {
                        attachment = [[HJInviteFriendToRoomAttachment alloc]init];
                        ((HJGiftAttachment *)attachment).first = (short)first;
                        ((HJGiftAttachment *)attachment).second = (short)second;
                        ((HJGiftAttachment *)attachment).data = data;
                    }
                } else if (first == Custom_Noti_Header_RedPacket){
//                    if (second == Custom_Noti_Sub_NewRedPacket) {
                        attachment = [[HJRedPacketInfoAttachment alloc]init];
                        ((HJRedPacketInfoAttachment *)attachment).title = [NSString stringWithFormat:@"%@",data[@"packetName"]];
                        ((HJRedPacketInfoAttachment *)attachment).first = (short)first;
                        ((HJRedPacketInfoAttachment *)attachment).second = (short)second;
                        ((HJRedPacketInfoAttachment *)attachment).data = data;
//                    }
                }else if (first == Custom_Noti_Header_News) {
                    if (second == Custom_Noti_Sub_News) {
                        attachment = [[HJNewsInfoAttachment alloc]init];
                        ((HJNewsInfoAttachment *)attachment).title = [NSString stringWithFormat:@"%@",data[@"title"]];
                        ((HJNewsInfoAttachment *)attachment).subTitle = [NSString stringWithFormat:@"%@",data[@"desc"]];
                        ((HJNewsInfoAttachment *)attachment).pic = [NSString stringWithFormat:@"%@",data[@"picUrl"]];
                        ((HJNewsInfoAttachment *)attachment).skipUrl = [NSString stringWithFormat:@"%@",data[@"webUrl"]];
                        ((HJNewsInfoAttachment *)attachment).first = (short)first;
                        ((HJNewsInfoAttachment *)attachment).second = (short)second;
                        ((HJNewsInfoAttachment *)attachment).data = data;
                    }
                }else if (first == Custom_Noti_Header_Queue) {
                    if (second == Custom_Noti_Sub_Queue_Invite || second == Custom_Noti_Sub_Queue_Kick || second == Custom_Noti_Sub_Gift_Effect_Open ||
                        second == Custom_Noti_Sub_Gift_Effect_Close ||
                        second == Custom_Noti_Sub_Car_Effect_Close ||
                        second == Custom_Noti_Sub_Car_Effect_Open ||
                        second == Custom_Noti_Sub_Message_Open || second == Custom_Noti_Sub_Message_Close) {
                        attachment = [[HJInviteMicAttachment alloc]init];
                        ((HJInviteMicAttachment *)attachment).inviteUid = [NSString stringWithFormat:@"%@",data[@"inviteUid"]];
                        ((HJInviteMicAttachment *)attachment).position = [NSString stringWithFormat:@"%@",data[@"position"]];
                        
                        ((HJInviteMicAttachment *)attachment).micPosition = [NSString stringWithFormat:@"%@",data[@"micPosition"]];
                        ((HJInviteMicAttachment *)attachment).uid = [NSString stringWithFormat:@"%@",data[@"uid"]];
                        ((HJInviteMicAttachment *)attachment).first = (short)first;
                        ((HJInviteMicAttachment *)attachment).second = (short)second;
                        ((HJInviteMicAttachment *)attachment).data = data;
                    }
                }else if (first == Custom_Noti_Header_Turntable) {
                    if (second == Custom_Noti_Sub_Turntable) {
                        attachment = [[TurntableAttachment alloc]init];
                        ((TurntableAttachment *)attachment).leftDrawNum = [data[@"leftDrawNum"] integerValue];
                        ((TurntableAttachment *)attachment).totalDrawNum = [data[@"totalDrawNum"] integerValue];
                        ((TurntableAttachment *)attachment).totalWinDrawNum = [data[@"totalWinDrawNum"] integerValue];
                        ((TurntableAttachment *)attachment).uid = [NSString stringWithFormat:@"%@",data[@"uid"]].userIDValue;
                        ((TurntableAttachment *)attachment).first = (short)first;
                        ((TurntableAttachment *)attachment).second = (short)second;

                    }
                }
                else {
                    attachment = [[Attachment alloc]init];
                    ((Attachment *)attachment).first = (short)first;
                    ((Attachment *)attachment).second = (short)second;
                    ((Attachment *)attachment).data = data;
                }
            }
            
        }
    }
    return attachment;
}

@end
