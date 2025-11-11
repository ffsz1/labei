//
//  YPNotiFriendCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPNotiFriendCore.h"
#import "HJNotiFriendCoreClient.h"
#import "YPAttachment.h"
#import <YYCategories/YYCategories.h>
#import "YPImRoomCoreV2.h"
#import "YPImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "YPAuthCoreHelp.h"
#import "YPUserCoreHelp.h"
#import <YYModel/YYModel.h>
#import "YPVersionCoreHelp.h"
#import "YPIMRequestManager+PublicRoom.h"
#import "YPWebSocketCore.h"
#import "HJWebSocketCoreClient.h"
#import "YPIMRequestManager+PublicRoom.h"
#import "YPHttpRequestHelper+NotiFriend.h"

typedef NS_ENUM(NSInteger, NotiFriendCoreActionStatus) {
    NotiFriendCoreActionStatusUndefine,
    NotiFriendCoreActionStatusPending,
    NotiFriendCoreActionStatusSuccess,
    NotiFriendCoreActionStatusFailure,
};

@interface YPNotiFriendCore () <HJImMessageCoreClient, HJWebSocketCoreClient>

@property (nonatomic, assign) NotiFriendCoreActionStatus enterStatus;

@end

@implementation YPNotiFriendCore

#pragma mark - Life cycle
- (void)dealloc{
    RemoveCoreClientAll(self);
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (instancetype)init{
    self = [super init];
    if (self) {
        AddCoreClient(HJImMessageCoreClient, self);
        AddCoreClient(HJWebSocketCoreClient, self);
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(enterNotiFriendRoom) name:UIApplicationWillEnterForegroundNotification object:nil];
        _enterStatus = NotiFriendCoreActionStatusUndefine;
    }
    return self;
}

#pragma mark - Event

#pragma mark - <ImMessageCoreClient>
- (void)onRecvChatRoomCustomMsg:(YPIMMessage *)msg {
    [self addNewMessage:msg];
}

#pragma mark - <HJWebSocketCoreClient>
- (void)onSocLoginSuccess {
    [self enterNotiFriendRoom];
}

#pragma mark - Public methods
- (void)sendChatMessage:(NSString *)message withUserInfo:(UserInfo *)info success:(void (^)())success
    failure:(void (^)(NSInteger code, NSString *message))failure {
    NSString *text = [message stringByReplacingOccurrencesOfString:@" " withString:@""];
    if (!text.length) {
        return;
    }
    
    if ([GetCore(YPUserCoreHelp) checkUserHasBanned:2]){
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(showBanndMessage), showBanndMessage);
        return;
    }
    
    YPAttachment *attachement = [[YPAttachment alloc]init];
    attachement.first = Custom_Noti_Header_NotiFriendChat;
    attachement.second = Custom_Noti_Header_NotiFriendChat;
    //life-rh @"avatar": info.avatar,
    NSDictionary *attMessageDic = @{@"msg":message,
                                    @"params": @{
//                                            @"avatar": info.avatar,
                                            @"uid" : [NSNumber numberWithLong:info.uid],
                                            @"charmLevel" : [NSNumber numberWithLong:info.charmLevel],
                                            @"experLevel" : [NSNumber numberWithLong:info.experLevel],
                                            @"nick" : info.nick
                                            }
                                    };
    attachement.data = attMessageDic;
    NSDictionary *data = JX_OBJECT_AVOID_nil([attachement yy_modelToJSONObject], NSDictionary);
    [YPIMRequestManager sendPublicRoomMessage:[self getRoomid] content:data success:^{
        !success ?: success();
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        !failure ?: failure(code, errorMessage);
    }];
}

- (void)exitNotiFriendRoom {
    if (!GetCore(YPVersionCoreHelp).checkIn) {
        [[NIMSDK sharedSDK].chatroomManager exitChatroom:[self getRoomid] completion:nil];
    }
}

- (void)enterNotiFriendRoom {
    self.enterStatus = NotiFriendCoreActionStatusPending;
    NSString *roomId = [self getRoomid];
    @weakify(self);
    [YPIMRequestManager enterPublicWithRoomId:roomId success:^(NSArray * _Nonnull messages) {
        @strongify(self);
        [YYLogger info:@"join room" message:@"join room success-->%@", roomId];
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(enterNotiFriendSuccess), enterNotiFriendSuccess);
        [self updateMessages:roomId datas:messages];
        self.enterStatus = NotiFriendCoreActionStatusSuccess;
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        @strongify(self);
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(enterNotiFriendFail), enterNotiFriendFail);
        self.enterStatus = NotiFriendCoreActionStatusFailure;
    }];
}

- (void)getLobbyChatInfo {
    [YPHttpRequestHelper requestLobbyChatInfo:^(NSArray * items) {
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(getLobbyRoomInfoSuccess:), getLobbyRoomInfoSuccess:items);
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(getLobbyRoomInfoFail), getLobbyRoomInfoFail);
    }];
}

- (void)getPublcRoomInfo {
    [YPIMRequestManager getPublicRoomMembersWithRoomId:[self getRoomid] success:^(YPIMPublicRoomInfo * _Nonnull roomInfo) {
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(getPublicRoomInfoSuccess:), getPublicRoomInfoSuccess:roomInfo);
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(getPublicRoomInfoFail), getPublicRoomInfoFail);
    }];
}

#pragma mark - Private methods
- (void)updateMessages:(NSString *)roomId datas:(NSArray *)datas {
    [self.historyMessage removeAllObjects];
    for (id data in datas) {
        YPIMMessage *message = [self messageWithRoomId:roomId data:data];
        [self.historyMessage addObject:message];
    }
    NotifyCoreClient(HJNotiFriendCoreClient, @selector(onPublicMessagesDidUpdate:), onPublicMessagesDidUpdate:self.historyMessage.copy);
}

- (YPIMMessage *)messageWithRoomId:(NSString *)roomId data:(id)data {
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
    
    message.server_msg_id = attachment.data[@"server_msg_id"];
    
    return message;
}

- (void)addNewMessage:(YPIMMessage *)message {
    if (![message.session.sessionId isEqualToString:[self getRoomid]]) return;
    if (message.messageType != HJIMMessageTypeCustom) return;
    
    JXIMCustomObject *obj = message.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
        YPAttachment *attachment = (YPAttachment *)obj.attachment;
        if (attachment.first != Custom_Noti_Header_NotiFriendChat) return;
    }
    
    if (!message.server_msg_id.length) {
        [self _jx_addNewMessage:message];
        return;
    }
    
    YPIMMessage *lastMessage = [self.historyMessage lastObject];
    if (!lastMessage) {
        [self _jx_addNewMessage:message];
        return;
    }
    
    if (!lastMessage.server_msg_id) {
        [self _jx_addNewMessage:message];
        return;
    }
    
    if (lastMessage.server_msg_id.integerValue < message.server_msg_id.integerValue) {
        [self _jx_addNewMessage:message];
        return;
    }
    
    __block BOOL shouldAdd = YES;
    [self.historyMessage enumerateObjectsWithOptions:NSEnumerationReverse usingBlock:^(YPIMMessage * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj.server_msg_id isEqualToString:message.server_msg_id]) {
            shouldAdd = NO;
            *stop = YES;
        }
    }];
    
    if (shouldAdd) {
        [self.historyMessage addObject:message];
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(onPublicMessagesDidUpdate:), onPublicMessagesDidUpdate:self.historyMessage.copy);
    }
}

- (void)_jx_addNewMessage:(YPIMMessage *)message {
    if (message) {
        [self.historyMessage addObject:message];
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(onPublicMessagesDidUpdate:), onPublicMessagesDidUpdate:self.historyMessage.copy);
    }
}

#pragma mark - Layout

#pragma mark - setters/getters
- (NSMutableArray<YPIMMessage *> *)historyMessage {
    if (!_historyMessage) {
        _historyMessage = [NSMutableArray array];
    }
    return _historyMessage;
}

- (NSString *)getRoomid {
    if (GetCore(YPVersionCoreHelp).isReleaseEnv) {
        if (GetCore(YPVersionCoreHelp).checkIn) return JXIMPublicDebugAuditRoomId;
        
        return JXIMPublicReleaseNormalRoomId;
    } else {
        if (GetCore(YPVersionCoreHelp).checkIn) return JXIMPublicDebugAuditRoomId;
        
        return JXIMPublicDebugNormalRoomId;
    }
}

@end
