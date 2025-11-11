//
//  HJNotiFriendCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJNotiFriendCore.h"
#import "HJNotiFriendCoreClient.h"
#import "Attachment.h"
#import <YYCategories/YYCategories.h>
#import "HJImRoomCoreV2.h"
#import "HJImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import <YYModel/YYModel.h>
#import "HJVersionCoreHelp.h"
#import "HJIMRequestManager+PublicRoom.h"
#import "WebSocketCore.h"
#import "HJWebSocketCoreClient.h"
#import "HJIMRequestManager+PublicRoom.h"
#import "HJHttpRequestHelper+NotiFriend.h"

typedef NS_ENUM(NSInteger, NotiFriendCoreActionStatus) {
    NotiFriendCoreActionStatusUndefine,
    NotiFriendCoreActionStatusPending,
    NotiFriendCoreActionStatusSuccess,
    NotiFriendCoreActionStatusFailure,
};

@interface HJNotiFriendCore () <HJImMessageCoreClient, HJWebSocketCoreClient>

@property (nonatomic, assign) NotiFriendCoreActionStatus enterStatus;

@end

@implementation HJNotiFriendCore

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
- (void)onRecvChatRoomCustomMsg:(HJIMMessage *)msg {
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
    
    if ([GetCore(HJUserCoreHelp) checkUserHasBanned:2]){
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(showBanndMessage), showBanndMessage);
        return;
    }
    
    Attachment *attachement = [[Attachment alloc]init];
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
    [HJIMRequestManager sendPublicRoomMessage:[self getRoomid] content:data success:^{
        !success ?: success();
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        !failure ?: failure(code, errorMessage);
    }];
}

- (void)exitNotiFriendRoom {
    if (!GetCore(HJVersionCoreHelp).checkIn) {
        [[NIMSDK sharedSDK].chatroomManager exitChatroom:[self getRoomid] completion:nil];
    }
}

- (void)enterNotiFriendRoom {
    self.enterStatus = NotiFriendCoreActionStatusPending;
    NSString *roomId = [self getRoomid];
    @weakify(self);
    [HJIMRequestManager enterPublicWithRoomId:roomId success:^(NSArray * _Nonnull messages) {
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
    [HJHttpRequestHelper requestLobbyChatInfo:^(NSArray * items) {
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(getLobbyRoomInfoSuccess:), getLobbyRoomInfoSuccess:items);
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(getLobbyRoomInfoFail), getLobbyRoomInfoFail);
    }];
}

- (void)getPublcRoomInfo {
    [HJIMRequestManager getPublicRoomMembersWithRoomId:[self getRoomid] success:^(HJIMPublicRoomInfo * _Nonnull roomInfo) {
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(getPublicRoomInfoSuccess:), getPublicRoomInfoSuccess:roomInfo);
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(getPublicRoomInfoFail), getPublicRoomInfoFail);
    }];
}

#pragma mark - Private methods
- (void)updateMessages:(NSString *)roomId datas:(NSArray *)datas {
    [self.historyMessage removeAllObjects];
    for (id data in datas) {
        HJIMMessage *message = [self messageWithRoomId:roomId data:data];
        [self.historyMessage addObject:message];
    }
    NotifyCoreClient(HJNotiFriendCoreClient, @selector(onPublicMessagesDidUpdate:), onPublicMessagesDidUpdate:self.historyMessage.copy);
}

- (HJIMMessage *)messageWithRoomId:(NSString *)roomId data:(id)data {
    HJIMMessage *message = [HJIMMessage new];
    message.messageType = HJIMMessageTypeCustom;
    message.timestamp = [NSDate date].timeIntervalSince1970;
    
    JXIMSession *session = [JXIMSession new];
    session.sessionId = roomId;
    session.sessionType = JXIMSessionTypeChatroom;
    message.session = session;
    
    Attachment *attachment = [Attachment yy_modelWithJSON:data];
    
    JXIMCustomObject *messageObject = [JXIMCustomObject new];
    messageObject.attachment = attachment;
    message.messageObject = messageObject;
    
    message.server_msg_id = attachment.data[@"server_msg_id"];
    
    return message;
}

- (void)addNewMessage:(HJIMMessage *)message {
    if (![message.session.sessionId isEqualToString:[self getRoomid]]) return;
    if (message.messageType != HJIMMessageTypeCustom) return;
    
    JXIMCustomObject *obj = message.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
        Attachment *attachment = (Attachment *)obj.attachment;
        if (attachment.first != Custom_Noti_Header_NotiFriendChat) return;
    }
    
    if (!message.server_msg_id.length) {
        [self _jx_addNewMessage:message];
        return;
    }
    
    HJIMMessage *lastMessage = [self.historyMessage lastObject];
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
    [self.historyMessage enumerateObjectsWithOptions:NSEnumerationReverse usingBlock:^(HJIMMessage * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
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

- (void)_jx_addNewMessage:(HJIMMessage *)message {
    if (message) {
        [self.historyMessage addObject:message];
        NotifyCoreClient(HJNotiFriendCoreClient, @selector(onPublicMessagesDidUpdate:), onPublicMessagesDidUpdate:self.historyMessage.copy);
    }
}

#pragma mark - Layout

#pragma mark - setters/getters
- (NSMutableArray<HJIMMessage *> *)historyMessage {
    if (!_historyMessage) {
        _historyMessage = [NSMutableArray array];
    }
    return _historyMessage;
}

- (NSString *)getRoomid {
    if (GetCore(HJVersionCoreHelp).isReleaseEnv) {
        if (GetCore(HJVersionCoreHelp).checkIn) return JXIMPublicDebugAuditRoomId;
        
        return JXIMPublicReleaseNormalRoomId;
    } else {
        if (GetCore(HJVersionCoreHelp).checkIn) return JXIMPublicDebugAuditRoomId;
        
        return JXIMPublicDebugNormalRoomId;
    }
}

@end
