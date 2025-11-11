//
//  YPImMessageCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPImMessageCore.h"
#import <NIMSDK/NIMSDK.h>
#import "HJImMessageCoreClient.h"
#import "NSObject+YYModel.h"
#import "NSString+Regex.h"
#import <YYCategories/YYCategories.h>
#import "NSString+Utils.h"
#import "YPImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImMessageSendCoreClient.h"
#import "YPHttpRequestHelper+Im.h"
#import "YPVersionCoreHelp.h"
#import "YPUserCoreHelp.h"
#import "YPIMRequestManager+RoomMessage.h"
#import "YPWebSocketReceiveCore.h"
#import "HJWebSocketReceiveCoreClient.h"
#import "YPIMMessage.h"
#import "YPIMRequestManager+PublicRoom.h"

@interface YPImMessageCore ()<NIMChatManagerDelegate, HJWebSocketReceiveCoreClient>
@end

@implementation YPImMessageCore
- (instancetype)init
{
    self = [super init];
    if (self) {
        [[NIMSDK sharedSDK].chatManager addDelegate:self];
        AddCoreClient(HJWebSocketReceiveCoreClient, self);
    }
    return self;
}

-(void)dealloc
{
    [[NIMSDK sharedSDK].chatManager removeDelegate:self];
    RemoveCoreClient(HJWebSocketCoreClient, self);
}

- (void)sendRoomTextMessage:(NSString *)text
                     roomId:(NSString *)roomId
                     member:(YPChatRoomMember *)member
                    success:(void (^)())success
                    failure:(void (^)(NSInteger code, NSString *errorMessage))failure {
    if ([GetCore(YPUserCoreHelp) checkUserHasBanned:1]){
        NotifyCoreClient(HJImMessageSendCoreClient, @selector(onSendMessageBanned), onSendMessageBanned);
        !failure ?: failure(-1, @"禁言");
        return;
    }
    
    //判断审核版本
    if (!GetCore(YPVersionCoreHelp).checkIn && [roomId isEqualToString:@"25610962"]) return;
    
    BOOL isEmpty = [NSString isEmpty:text];
    if (isEmpty) return;
    if (!member) return;
    if (!roomId.length) return;
    
    [YPIMRequestManager sendTextWithRoomId:roomId member:member content:text success:^{
        !success ?: success();
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        !failure ?: failure(code, errorMessage);
    }];
}

- (void)sendTextMessage:(NSString *)text nick:(NSString *)nick  sessionId:(NSString *)sessionId type:(NIMSessionType)type
{
    
    if ([GetCore(YPUserCoreHelp) checkUserHasBanned:1]){
        NotifyCoreClient(HJImMessageSendCoreClient, @selector(onSendMessageBanned), onSendMessageBanned);
        return;
    }
    
    //判断 版本
    if (![GetCore(YPVersionCoreHelp) checkIn] && [sessionId isEqualToString:@"25610962"]) {
        return;
    }
    BOOL isEmpty = [NSString isEmpty:text];
    if (isEmpty) {
        return;
    }
    
    //构造消息
    NIMMessage *message = [[NIMMessage alloc] init];
//    if (![text nim_isEmpty]) {
        message.text = text;
//    }
    
    //构造会话
    NIMSession *session = [NIMSession session:sessionId type:type];
    
    NIMAntiSpamOption *antiSpamOption = [[NIMAntiSpamOption alloc] init];
    antiSpamOption.yidunEnabled = NO;
    message.antiSpamOption = antiSpamOption;
    NSLog(@"%@ %@",[GetCore(YPAuthCoreHelp) getUid], message.session.sessionId);
    //发送消息
    [[NIMSDK sharedSDK].chatManager sendMessage:message toSession:session error:nil];
}

//发送自定义消息
- (void)sendCustomMessageAttachement:(YPAttachment *)attachment
                           sessionId:(NSString *)sessionId
                                type:(JXIMSessionType)type {
    //判断 版本
    if (![GetCore(YPVersionCoreHelp) checkIn] && [sessionId isEqualToString:@"25610962"]) {
        return;
    }
    [self sendCustomMessageAttachement:attachment sessionId:sessionId type:type needApns:NO apnsContent:nil yidunEnable:false];
}

//发送自定义消息-带推送
- (void)sendCustomMessageAttachement:(YPAttachment *)attachment
                           sessionId:(NSString *)sessionId
                                type:(JXIMSessionType)type
                            needApns:(BOOL)needApns
                         apnsContent:(NSString *)apnsContent
                         yidunEnable:(BOOL)yidunEnable {
    
    //判断 版本
    if (![GetCore(YPVersionCoreHelp) checkIn] && [sessionId isEqualToString:@"25610962"]) {
        return;
    }
    
    if (type == JXIMSessionTypeChatroom) {
        if (attachment.first == Custom_Noti_Header_NotiFriendChat) {
            NSDictionary *data = JX_OBJECT_AVOID_nil([attachment yy_modelToJSONObject], NSDictionary);
            [YPIMRequestManager sendPublicRoomMessage:sessionId content:data success:^{
                NotifyCoreClient(HJImMessageSendCoreClient, @selector(onSendPublicMessageDidSuccess:), onSendPublicMessageDidSuccess:sessionId);
            } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
                NotifyCoreClient(HJImMessageSendCoreClient, @selector(onSendPublicMessageDidFailure:code:errorMessage:), onSendPublicMessageDidFailure:sessionId code:code errorMessage:errorMessage);
            }];
        } else {
            if (attachment.first == Custom_Noti_Header_Gift) {
                if ([attachment.data isKindOfClass:[NSDictionary class]]) {
                    NSMutableDictionary *dic = attachment.data;
                    if ([dic objectForKey:@"roomId"]) {
                        [dic removeObjectForKey:@"roomId"];
                        attachment.data = dic;
                    }
                }
            }
            
            YPChatRoomMember *member = GetCore(YPImRoomCoreV2).myMember;
            member.room_id = sessionId;
            NSDictionary *data = JX_OBJECT_AVOID_nil([attachment yy_modelToJSONObject], NSDictionary);
            [YPIMRequestManager sendMessageWithRoomId:sessionId member:member data:data success:^{
                
            } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
                
            }];
        }
    } else if (type == JXIMSessionTypeP2P) {
        NIMMessage *message = [[NIMMessage alloc]init];
        if (attachment.first == Custom_Noti_Header_Gift) {
            if ([attachment.data isKindOfClass:[NSDictionary class]]) {
                NSMutableDictionary *dic = attachment.data;
                if ([dic objectForKey:@"roomId"]) {
                    [dic removeObjectForKey:@"roomId"];
                    attachment.data = dic;
                }
            }
        }
        
        if (attachment.first == Custom_Noti_Header_NotiInviteRoom) {
            message.text = attachment.data;
        }
        
        NIMCustomObject *customObject = [[NIMCustomObject alloc]init];
        customObject.attachment = attachment;
        message.messageObject = customObject;
        if (needApns) {
            message.apnsContent = apnsContent;
        }
        NIMMessageSetting *setting = [[NIMMessageSetting alloc]init];
        setting.apnsEnabled = needApns;
        message.setting = setting;
        
        if (yidunEnable) {
            NIMAntiSpamOption *antiSpamOption = [[NIMAntiSpamOption alloc] init];
            antiSpamOption.yidunEnabled = YES;
            NSMutableDictionary *dic = attachment.data;
            if ([dic objectForKey:@"msg"]) {
                NSString *content = dic[@"msg"];
                if (content.length > 0) {
                    antiSpamOption.content = content;
                }
            }
            message.antiSpamOption = antiSpamOption;
        }
        
        //构造会话
        NIMSession *session = [NIMSession session:sessionId type:type];
        
        [[NIMSDK sharedSDK].chatManager sendMessage:message toSession:session error:nil];
    }
}

//发送消息成功回调
- (void)sendMessage:(NIMMessage *)message didCompleteWithError:(NSError *)error {
    if (error == nil) {
        NotifyCoreClient(HJImMessageSendCoreClient, @selector(onSendMessageSuccess:), onSendMessageSuccess:message);
    }
}

#pragma mark - <HJWebSocketReceiveCoreClient>
- (void)onReceiveTextNotifyMessage:(NSString *)roomId member:(YPChatRoomMember *)member content:(NSString *)content {
    YPIMMessage *message = [YPIMMessage new];
    message.text = content;
    message.messageType = HJIMMessageTypeText;
    message.timestamp = [NSDate date].timeIntervalSince1970;
    
    JXIMSession *session = [JXIMSession new];
    session.sessionId = roomId;
    session.sessionType = JXIMSessionTypeChatroom;
    message.session = session;
    
    message.from = member.account;
    message.member = member;
    
    NotifyCoreClient(HJImMessageCoreClient, @selector(onRecvChatRoomTextMsg:), onRecvChatRoomTextMsg:message);
}

- (void)onReceiveMessageNotifyMessage:(NSString *)roomId member:(YPChatRoomMember *)member data:(id)data {
    YPIMMessage *message = [YPIMMessage new];
    message.messageType = HJIMMessageTypeCustom;
    message.timestamp = [NSDate date].timeIntervalSince1970;
    message.member = member;
    
    JXIMSession *session = [JXIMSession new];
    session.sessionId = roomId;
    session.sessionType = JXIMSessionTypeChatroom;
    message.session = session;
    
    YPAttachment *attachment = [YPAttachment yy_modelWithJSON:data];
    if (attachment.first == Custom_Noti_Header_NotiFriendChat) {
    } else if (attachment.first == Custom_Noti_Header_WaitQueue) {
        NSDictionary *data = attachment.data;
        if ([data containsObjectForKey:@"params"]) {
            NSDictionary *dataDic = [JSONTools ll_dictionaryWithJSON:data[@"params"]];
            if ([dataDic containsObjectForKey:@"key"]) {
                NotifyCoreClient(HJImRoomCoreClient, @selector(notiMicroQueueKeyPositionHasDown:message:), notiMicroQueueKeyPositionHasDown:dataDic[@"key"] message:message);
            }
        }
    } else if (attachment.first == Custom_Noti_Header_Charm) {
        NSDictionary *data = attachment.data;
    }
    JXIMCustomObject *messageObject = [JXIMCustomObject new];
    messageObject.attachment = attachment;
    message.messageObject = messageObject;
    
    NotifyCoreClient(HJImMessageCoreClient, @selector(onRecvChatRoomCustomMsg:), onRecvChatRoomCustomMsg:message);
}

- (void)onReceivePublicRoomMessageNotifyMessage:(NSString *)roomId data:(id)data {
    YPIMMessage *message = [YPIMMessage new];
    message.messageType = HJIMMessageTypeCustom;
    message.timestamp = [NSDate date].timeIntervalSince1970;
    
    JXIMSession *session = [JXIMSession new];
    session.sessionId = roomId;
    session.sessionType = JXIMSessionTypeChatroom;
    message.session = session;
    
    YPAttachment *attachment = [YPAttachment yy_modelWithJSON:data];
    
    JXIMCustomObject *messageObject = [JXIMCustomObject new];
    messageObject.attachment = attachment;
    message.messageObject = messageObject;
    
    NotifyCoreClient(HJImMessageCoreClient, @selector(onRecvChatRoomCustomMsg:), onRecvChatRoomCustomMsg:message);
}

#pragma mark - <NIMChatManagerDelegate>
- (void)willSendMessage:(NIMMessage *)message
{
    if (message.session.sessionType == NIMSessionTypeChatroom) {
        NotifyCoreClient(HJImMessageSendCoreClient, @selector(onWillSendMessage:), onWillSendMessage:message);
    }else if (message.session.sessionType == NIMSessionTypeP2P) {
        if (message.messageType == NIMMessageTypeCustom) {
            NIMCustomObject *obj = (NIMCustomObject *)message.messageObject;
            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_Gift) {
                NotifyCoreClient(HJImMessageCoreClient, @selector(onRecvP2PCustomMsg:), onRecvP2PCustomMsg:message);
            }
            
        }
        NotifyCoreClient(HJImMessageSendCoreClient, @selector(onWillSendMessage:), onWillSendMessage:message);
    }
}

- (void)onRecvMessages:(NSArray<NIMMessage *> *)messages
{
    if (messages.count <= 0) {
        return;
    }
    
    for (NIMMessage *message in messages) {
        if (message.messageType == NIMMessageTypeText) {
//            if (message.session.sessionType == NIMSessionTypeChatroom) {
//                NotifyCoreClient(ImMessageCoreClient, @selector(onRecvChatRoomTextMsg:), onRecvChatRoomTextMsg:message);
//            }
        } else if (message.messageType == NIMMessageTypeNotification) {
            NIMNotificationObject *notiMsg = (NIMNotificationObject *)message.messageObject;
            if (notiMsg.notificationType == NIMNotificationTypeChatroom) {
                NotifyCoreClient(HJImMessageCoreClient, @selector(onRecvChatRoomNotiMsg:), onRecvChatRoomNotiMsg:message);
            }
        } else if (message.messageType == NIMMessageTypeCustom) {
            NIMSession *session = message.session;
            if (session.sessionType == NIMSessionTypeChatroom) {
                NIMCustomObject *obj = (NIMCustomObject *)message.messageObject;
                if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
                    YPAttachment *attachment = (YPAttachment *)obj.attachment;
                    if (attachment.first == Custom_Noti_Header_NotiFriendChat) {
                        NIMMessageChatroomExtension *ext = (NIMMessageChatroomExtension *)message.messageExt;
                        NSDictionary *extRoomExtDic = [JSONTools ll_dictionaryWithJSON:ext.roomExt];
                        if ([extRoomExtDic containsObjectForKey:@"experLevel"]) {
//                            attachment.experLevel = [extRoomExtDic[@"experLevel"] intValue];
                        }
                    } else if (attachment.first == Custom_Noti_Header_WaitQueue) {
                        NSDictionary *data = attachment.data;
                        if ([data containsObjectForKey:@"params"]) {
                            NSDictionary *dataDic = [JSONTools ll_dictionaryWithJSON:data[@"params"]];
                            if ([dataDic containsObjectForKey:@"key"]) {
                                NotifyCoreClient(HJImRoomCoreClient, @selector(notiMicroQueueKeyPositionHasDown:message:), notiMicroQueueKeyPositionHasDown:dataDic[@"key"] message:message);
                            }
                        }
                    }
                }
                NotifyCoreClient(HJImMessageCoreClient, @selector(onRecvChatRoomCustomMsg:), onRecvChatRoomCustomMsg:message);
            } else if (session.sessionType == NIMSessionTypeP2P) {
                NotifyCoreClient(HJImMessageCoreClient, @selector(onRecvP2PCustomMsg:), onRecvP2PCustomMsg:message);
            } 
        }
        
        NotifyCoreClient(HJImMessageCoreClient, @selector(onRecvAnMsg:), onRecvAnMsg:message);
    }
}

- (NSInteger)getUnreadCount {
    return [[NIMSDK sharedSDK].conversationManager allUnreadCount];
} 


/**
 违禁词匹配
 
 @param text 发送文本
 */
- (void)sensitiveWordRegexWithText:(NSString *)text requestId:(NSString *)requestId finishBlock:(void(^)(BOOL isCanSend, NSString *msg))finishBlock {
    
    if (self.sensitiveWord.length) {
        NSPredicate *pred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", self.sensitiveWord];
        BOOL isMatch = [pred evaluateWithObject:text];
        
        if (isMatch) {
            
            if (finishBlock) {
                finishBlock(NO,NSLocalizedString(XCMesseagesensitiveWordRegexTip, nil));
            }
        }
        else {
            
            if (finishBlock) {
                finishBlock(YES,nil);
            }
        }
    }
    
    [YPHttpRequestHelper sensitiveWordRegexWithSuccess:^(NSString *str) {
        
        if (str.length) {
            NSString *regular = str;
            NSPredicate *pred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regular];
            BOOL isMatch = [pred evaluateWithObject:text];
            
            if (isMatch) {
                if (!self.sensitiveWord.length) {
                    
                    if (finishBlock) {
                        finishBlock(NO,NSLocalizedString(XCMesseagesensitiveWordRegexTip, nil));
                    }
                }
            }
            else {
                if (!self.sensitiveWord.length) {
                    
                    if (finishBlock) {
                        finishBlock(YES,nil);
                    }
                }
            }
            
        }
        else {
            
            if (!self.sensitiveWord.length) {
                
                if (finishBlock) {
                    finishBlock(YES,nil);
                }
            }
        }
        
        self.sensitiveWord = str;
        
    } failure:^(NSNumber *code, NSString *msg) {
        
        if (!self.sensitiveWord.length) {
            
            if (finishBlock) {
                finishBlock(YES,nil);
            }
        }
    }];
}


@end
