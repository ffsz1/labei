//
//  YPCustomAttachmentDecoder.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPCustomAttachmentDecoder.h"
#import "YPAttachment.h"
#import "YPOpenLiveAttachment.h"
#import "YPRedPacketInfoAttachment.h"
#import "YPNewsInfoAttachment.h"
#import "YPGiftAttachment.h"
#import "YPInviteMicAttachment.h"
#import "NSObject+YYModel.h"
#import "UserInfo.h"
#import "YPGiftCore.h"
#import "YPGiftReceiveInfo.h"
#import "NSString+JsonToDic.h"
#import "YPTurntableAttachment.h"
#import "YPInviteFriendToRoomAttachment.h"

@implementation YPCustomAttachmentDecoder

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
                            attachment = [[YPOpenLiveAttachment alloc]init];
                            ((YPOpenLiveAttachment *)attachment).avatar = user.avatar;
                            ((YPOpenLiveAttachment *)attachment).nick = user.nick;
                            ((YPOpenLiveAttachment *)attachment).uid = user.uid;
                            return attachment;
                        }else {
                            attachment = [[YPOpenLiveAttachment alloc]init];
                            ((YPOpenLiveAttachment *)attachment).uid = uid.userIDValue;
                            
                        }
                        ((YPOpenLiveAttachment *)attachment).first = (short)first;
                        ((YPOpenLiveAttachment *)attachment).second = (short)second;
                        ((YPOpenLiveAttachment *)attachment).data = data;
                    }
                    
                }else if (first == Custom_Noti_Header_Gift) {
                    if (second == Custom_Noti_Sub_Gift_Send) {
                        attachment = [[YPGiftAttachment alloc]init];
                        YPGiftInfo * info = [GetCore(YPGiftCore) findGiftInfoByGiftId:[data[@"giftId"]integerValue] giftyType:GiftTypeNormal];
                        if (info == nil) {
                            info = [GetCore(YPGiftCore) findGiftInfoByGiftId:[data[@"giftId"]integerValue] giftyType:GiftTypeMystic];
                        }
                        ((YPGiftAttachment *)attachment).giftName = [NSString stringWithFormat:@"%@",info.giftName];
                        ((YPGiftAttachment *)attachment).giftPic = [NSString stringWithFormat:@"%@",info.giftUrl];
                        ((YPGiftAttachment *)attachment).giftNum = [NSString stringWithFormat:@"%@",data[@"giftNum"]];
                        ((YPGiftAttachment *)attachment).first = (short)first;
                        ((YPGiftAttachment *)attachment).second = (short)second;
                        ((YPGiftAttachment *)attachment).data = data;
                    }
                }else if (first == Custom_Noti_Header_NotiInviteRoom) {
                    if (second == Custom_Noti_Header_NotiInviteRoom) {
                        attachment = [[YPInviteFriendToRoomAttachment alloc]init];
                        ((YPGiftAttachment *)attachment).first = (short)first;
                        ((YPGiftAttachment *)attachment).second = (short)second;
                        ((YPGiftAttachment *)attachment).data = data;
                    }
                } else if (first == Custom_Noti_Header_RedPacket){
//                    if (second == Custom_Noti_Sub_NewRedPacket) {
                        attachment = [[YPRedPacketInfoAttachment alloc]init];
                        ((YPRedPacketInfoAttachment *)attachment).title = [NSString stringWithFormat:@"%@",data[@"packetName"]];
                        ((YPRedPacketInfoAttachment *)attachment).first = (short)first;
                        ((YPRedPacketInfoAttachment *)attachment).second = (short)second;
                        ((YPRedPacketInfoAttachment *)attachment).data = data;
//                    }
                }else if (first == Custom_Noti_Header_News) {
                    if (second == Custom_Noti_Sub_News) {
                        attachment = [[YPNewsInfoAttachment alloc]init];
                        ((YPNewsInfoAttachment *)attachment).title = [NSString stringWithFormat:@"%@",data[@"title"]];
                        ((YPNewsInfoAttachment *)attachment).subTitle = [NSString stringWithFormat:@"%@",data[@"desc"]];
                        ((YPNewsInfoAttachment *)attachment).pic = [NSString stringWithFormat:@"%@",data[@"picUrl"]];
                        ((YPNewsInfoAttachment *)attachment).skipUrl = [NSString stringWithFormat:@"%@",data[@"webUrl"]];
                        ((YPNewsInfoAttachment *)attachment).first = (short)first;
                        ((YPNewsInfoAttachment *)attachment).second = (short)second;
                        ((YPNewsInfoAttachment *)attachment).data = data;
                    }
                }else if (first == Custom_Noti_Header_Queue) {
                    if (second == Custom_Noti_Sub_Queue_Invite || second == Custom_Noti_Sub_Queue_Kick || second == Custom_Noti_Sub_Gift_Effect_Open ||
                        second == Custom_Noti_Sub_Gift_Effect_Close ||
                        second == Custom_Noti_Sub_Car_Effect_Close ||
                        second == Custom_Noti_Sub_Car_Effect_Open ||
                        second == Custom_Noti_Sub_Message_Open || second == Custom_Noti_Sub_Message_Close) {
                        attachment = [[YPInviteMicAttachment alloc]init];
                        ((YPInviteMicAttachment *)attachment).inviteUid = [NSString stringWithFormat:@"%@",data[@"inviteUid"]];
                        ((YPInviteMicAttachment *)attachment).position = [NSString stringWithFormat:@"%@",data[@"position"]];
                        
                        ((YPInviteMicAttachment *)attachment).micPosition = [NSString stringWithFormat:@"%@",data[@"micPosition"]];
                        ((YPInviteMicAttachment *)attachment).uid = [NSString stringWithFormat:@"%@",data[@"uid"]];
                        ((YPInviteMicAttachment *)attachment).first = (short)first;
                        ((YPInviteMicAttachment *)attachment).second = (short)second;
                        ((YPInviteMicAttachment *)attachment).data = data;
                    }
                }else if (first == Custom_Noti_Header_Turntable) {
                    if (second == Custom_Noti_Sub_Turntable) {
                        attachment = [[YPTurntableAttachment alloc]init];
                        ((YPTurntableAttachment *)attachment).leftDrawNum = [data[@"leftDrawNum"] integerValue];
                        ((YPTurntableAttachment *)attachment).totalDrawNum = [data[@"totalDrawNum"] integerValue];
                        ((YPTurntableAttachment *)attachment).totalWinDrawNum = [data[@"totalWinDrawNum"] integerValue];
                        ((YPTurntableAttachment *)attachment).uid = [NSString stringWithFormat:@"%@",data[@"uid"]].userIDValue;
                        ((YPTurntableAttachment *)attachment).first = (short)first;
                        ((YPTurntableAttachment *)attachment).second = (short)second;

                    }
                }
                else {
                    attachment = [[YPAttachment alloc]init];
                    ((YPAttachment *)attachment).first = (short)first;
                    ((YPAttachment *)attachment).second = (short)second;
                    ((YPAttachment *)attachment).data = data;
                }
            }
            
        }
    }
    return attachment;
}

@end
