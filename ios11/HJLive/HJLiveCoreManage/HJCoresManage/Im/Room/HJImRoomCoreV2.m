
//
//  HJImRoomCoreV2.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"
#import "HJIMRequestManager+RoomQueue.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "HJImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "HJImLoginCore.h"
#import "HJImLoginCoreClient.h"
#import "HJPhoneCallCoreClient.h"

#import "NSObject+YYModel.h"
#import "NSMutableDictionary+Safe.h"
#import "NSString+JsonToDic.h"

#import <YYCategories.h>

#import "HJRoomQueueCoreV2Help.h"
#import "HJRoomQueueCoreClient.h"
#import "HJNotiFriendCore.h"
#import "HJIMRequestManager+Room.h"
#import "HJWebSocketReceiveCoreClient.h"
#import "HJIMQueueItem.h"
#import "HJIMRequestManager.h"

#import "HJMeetingCore.h"

#import "HJRoomQueueCoreV2Help.h"

#define MIC_COUNT 9
///每页个数
#define PageCount 50

@interface HJImRoomCoreV2()<NIMChatroomManagerDelegate, HJImMessageCoreClient, HJImLoginCoreClient, HJPhoneCallCoreClient,HJWebSocketReceiveCoreClient>
@property (nonatomic, assign) int state;//记录刷新的方式

@end

@implementation HJImRoomCoreV2

- (instancetype)init
{
    self = [super init];
    if (self) {
        AddCoreClient(HJImMessageCoreClient, self);
        AddCoreClient(HJImLoginCoreClient, self);
        AddCoreClient(HJPhoneCallCoreClient, self);
        AddCoreClient(HJWebSocketReceiveCoreClient, self);
        [[NIMSDK sharedSDK].chatroomManager addDelegate:self];
    }
    return self;
}

- (void)dealloc{
    RemoveCoreClient(HJImMessageCoreClient, self);
    RemoveCoreClient(HJImLoginCoreClient, self);
    RemoveCoreClient(HJPhoneCallCoreClient, self);
    RemoveCoreClientAll(self);
    [[NIMSDK sharedSDK].chatroomManager removeDelegate:self];
}


#pragma mark - ImLoginCoreClient
- (void)onImKick{
    if (self.currentRoomInfo != nil) {
        [self exitChatRoom:self.currentRoomInfo.roomId];
    }
}

- (void)onImLogoutSuccess{
    if (self.currentRoomInfo != nil) {
        [self exitChatRoom:self.currentRoomInfo.roomId];
    }
}

#pragma mark - NIMChatroomManagerDelegate
- (void)chatroom:(NSString *)roomId connectionStateChanged:(NIMChatroomConnectionState)state{
      NotifyCoreClient(HJImRoomCoreClient, @selector(onConnectionStateChanged:), onConnectionStateChanged:state);
}

#pragma mark - <HJWebSocketReceiveCoreClient>
/**
 服务器提示将断开socket通知
 
 @param code   错误码（0：成功，其它：失败）
 @param errmsg 错误描述（用于客户端显示）（可无，err=0则无）
 */
- (void)onSocketWillKickOffNotifyMessage:(NSInteger)code errmsg:(NSString *)errmsg {
    
    NSLog(@"%@",errmsg);
    
    if (code == 10037) {
        [GetCore(HJAuthCoreHelp) kicked];
    }else{
        [GetCore(HJAuthCoreHelp) logout];
    }
    
}

- (void)onEnterRoomNotifyMessage:(ChatRoomMember *)member {
    if ([member.account longLongValue] != [GetCore(HJAuthCoreHelp)getUid].userIDValue) {
        [self updateOnLineNmuber:YES];
    }
    if (member.is_creator) {
        self.roomOwner = member;
    }
    if ([member.account longLongValue] == [GetCore(HJAuthCoreHelp)getUid].userIDValue) {
        self.myMember = member;
    }
    NotifyCoreClient(HJImRoomCoreClient, @selector(onUserInterChatRoom:), onUserInterChatRoom:member);
}

- (void)onExitRoomNotifyMessage:(NSString *)roomId
                            uid:(NSString *)uid
                       nickName:(NSString *)nickName {
    [self updateOnLineNmuber:NO];
    NotifyCoreClient(HJImRoomCoreClient, @selector(onUserExitChatRoom:uid:), onUserExitChatRoom:roomId uid:uid);
}

- (void)onRoomMemberKickedNotifyMessage:(NSString *)roomId
                                    uid:(NSString *)uid
                               nickName:(NSString *)nickName
                                 avater:(NSString *)avater
                             reasonType:(JXIMUserKickedReasonType)reasonType
                          reasonMessage:(NSString *)reasonMessage {
    self.currentRoomInfo = nil;
    NSUserDefaults *ur = [NSUserDefaults standardUserDefaults];
    NSDate* dat = [NSDate dateWithTimeIntervalSinceNow:0];
    NSTimeInterval a=[dat timeIntervalSince1970]*1000;
    NSString *timeString = [NSString stringWithFormat:@"%f",a];
    if (!roomId) return;
    
    NSDictionary *dic = @{
                          @"roomId":roomId,
                          @"time":timeString
                          };
    if (!dic) {return;}
    [ur setObject:dic forKey:[NSString stringWithFormat:@"beKicked%@",roomId]];
    [ur synchronize];
    
    if ([uid integerValue] == [[GetCore(HJAuthCoreHelp) getUid] integerValue]) {
        
        NotifyCoreClient(HJImRoomCoreClient, @selector(onUserBeKicked:), onUserBeKicked:roomId);
    }
}

- (void)onRoomManagerAddNotifyMessage:(NSString *)roomId
                               member:(ChatRoomMember *)member {
    [self handleMemagerNotificationMessage:member.account];
    NotifyCoreClient(HJImRoomCoreClient, @selector(managerAdd:), managerAdd:member.account);
}

- (void)onRoomManagerRemoveNotifyMessage:(NSString *)roomId
                                  member:(ChatRoomMember *)member {
    [self handleMemagerNotificationMessage:member.account];
    NotifyCoreClient(HJImRoomCoreClient, @selector(managerRemove:), managerRemove:member.account);
}

- (void)onRoomBlacklistAddNotifyMessage:(NSString *)roomId
                                 member:(ChatRoomMember *)member {
    [self updateOnLineNmuber:NO];
    NotifyCoreClient(HJImRoomCoreClient, @selector(onUserBeAddBlack:), onUserBeAddBlack:member.account);
}

- (void)onRoomBlacklistRemoveNotifyMessage:(NSString *)roomId
                                    member:(ChatRoomMember *)member {
    NotifyCoreClient(HJImRoomCoreClient, @selector(onUserBeRemoveBlack:), onUserBeRemoveBlack:member.account);
}

#pragma mark - PhoneCallCoreClient
- (void)onStartPhoneCallSuccess:(UserID)callUid{
    if (self.currentRoomInfo !=nil) {
        [self exitChatRoom:self.currentRoomInfo.roomId];
    }
}

- (void)onResponsePhoneCall:(NSString *)from accept:(BOOL)accept{
    if (accept && self.currentRoomInfo != nil) {
        [self exitChatRoom:self.currentRoomInfo.roomId];
    }
}

#pragma mark - ImMessageCoreClient
- (void)onRecvChatRoomNotiMsg:(NIMMessage *)msg{
    if ([msg.session.sessionId isEqualToString:GetCore(HJNotiFriendCore).getRoomid]) {return;}
    
    NIMNotificationObject *notiMsg = (NIMNotificationObject *)msg.messageObject;
    
    NIMChatroomNotificationContent *content = (NIMChatroomNotificationContent *)notiMsg.content;
    
    NSUserDefaults *ur = [NSUserDefaults standardUserDefaults];
    BOOL micInListOption = NO;
    if ([ur objectForKey:@"micInListOption"]) {
        NSString *tag = [ur objectForKey:@"micInListOption"];
        micInListOption = [tag boolValue];
    }
    
    [YYLogger info:@"收到消息" message:@"是否排麦：%d--roomid：%zd---在线人数：%zd---消息类型：%zd--消息来源：%@",micInListOption,self.currentRoomInfo.roomId,self.displayMembers.count,content.eventType,msg.from];
    
    switch (content.eventType) {
        case NIMChatroomEventTypeEnter:
            break;
        case NIMChatroomEventTypeExit:
            break;
        case NIMChatroomEventTypeAddBlack:
            break;
        case NIMChatroomEventTypeRemoveBlack:
            break;
        case NIMChatroomEventTypeAddMute:
            break;
        case NIMChatroomEventTypeRemoveMute:
            break;
        case NIMChatroomEventTypeMemberUpdateInfo:
            break;
        case NIMChatroomEventTypeRemoveManager:
            break;
        case NIMChatroomEventTypeAddManager:
            break;
        case NIMChatroomEventTypeKicked:
            break;
        case NIMChatroomEventTypeInfoUpdated://麦序状态
            NotifyCoreClient(HJImRoomCoreClient, @selector(onRoomInfoUpdate:), onRoomInfoUpdate:msg);
            break;
        case NIMChatroomEventTypeQueueChange://队列上下麦
            NotifyCoreClient(HJImRoomCoreClient, @selector(onRoomQueueUpdate:), onRoomQueueUpdate:msg);
            break;
        case NIMChatroomEventTypeQueueBatchChange:
            NotifyCoreClient(HJImRoomCoreClient, @selector(letFirstMicroHasUserTokill:), letFirstMicroHasUserTokill:msg);
            break;
        default:
            break;
    }
}

- (void)checkRoomerIsInRoom {
    if(self.currentRoomInfo) {
        [[self rac_queryChartRoomMemberByUid:[NSString stringWithFormat:@"%lld",self.currentRoomInfo.uid]] subscribeNext:^(id x) {
            ChatRoomMember *roomOwner = x;
            if (roomOwner) {
                NotifyCoreClient(HJImRoomCoreClientV2, @selector(checkRoomerOnline:), checkRoomerOnline:YES);
                self.roomOwner = roomOwner;
                self.roomOwnerleave = roomOwner;
            } else {
                NotifyCoreClient(HJImRoomCoreClientV2, @selector(checkRoomerOnline:), checkRoomerOnline:false);
            }
        }];
    }
}



#pragma mark - puble method
- (void)enterChatRoom:(NSInteger )roomId{
    if (roomId <= 0) {
        return;
    }

    self.isLoading = YES;
    
    [HJIMRequestManager enterRoomWithRoomId:[NSString stringWithFormat:@"%ld",(long)roomId] success:^(ChatRoomInfo *roomInfo, ChatRoomMember *member, NSArray<HJIMQueueItem *> *queueItem) {
        
        int tagId = self.currentRoomInfo.tagId;
        NSString *tagStr = [self.currentRoomInfo.tagPict copy];
        NSString *backPicUrl = self.currentRoomInfo.backPicUrl.length ? [self.currentRoomInfo.backPicUrl copy] : @"";
        
        self.onlineNumber = roomInfo.onlineNum;
        self.myMember = member;
        self.currentRoomInfo = roomInfo;
        [self checkRoomerIsInRoom];
        
//        BOOL isInRoom = roomInfo.operatorStatus == JXIMRoomerStatusOffline?NO:YES;
//        NotifyCoreClient(HJImRoomCoreClientV2, @selector(checkRoomerOnline:), checkRoomerOnline:isInRoom);

        
        if (self.currentRoomInfo.tagId <= 0 || !self.currentRoomInfo.tagPict.length) {
            self.currentRoomInfo.tagId = tagId;
            self.currentRoomInfo.tagPict = tagStr;
        }
        
        if (!self.currentRoomInfo.backPicUrl.length) {
            self.currentRoomInfo.backPicUrl = backPicUrl;
        }
        
        self.micQueue = [NSMutableArray arrayWithArray:queueItem];
        
        [self queryQueueWithRoomId:[NSString stringWithFormat:@"%ld",(long)self.currentRoomInfo.roomId]];
//        [self fetchRoomRegularMembers];
        
        NotifyCoreClient(HJImRoomCoreClient, @selector(onMeInterChatRoomSuccess), onMeInterChatRoomSuccess);
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        NSLog(@"%ld %@",(long)code, errorMessage);
        
        if (code == 100202) {
            // 用户已在黑名单
            NotifyCoreClient(HJImRoomCoreClient, @selector(onMeInterChatRoomInBlackList), onMeInterChatRoomInBlackList);
        }
        else if (code == 100201) {
            // 用户房间socketId不存在
            NotifyCoreClient(HJImRoomCoreClient, @selector(onMeInterChatRoomFailth), onMeInterChatRoomFailth);
        }
        else if (code == 100200) {
            // 获取房间信息失败
            NotifyCoreClient(HJImRoomCoreClient, @selector(onMeInterChatRoomFailth), onMeInterChatRoomFailth);
        }
        else {
            NotifyCoreClient(HJImRoomCoreClient, @selector(onMeInterChatRoomBadNetWork), onMeInterChatRoomBadNetWork)
        }
    }];
}

- (void)exitChatRoom:(NSInteger)roomId{
    
    //更改切换房间，即构还在上个房间的问题
    [GetCore(HJMeetingCore) leaveMeeting:[NSString stringWithFormat:@"%ld", (long)roomId]];
    
    if (self.currentRoomInfo.uid == GetCore(HJAuthCoreHelp).getUid.userIDValue) {
        self.roomOwner = nil;
    }
    
    if (self.currentRoomInfo == nil || roomId != self.currentRoomInfo.roomId) {
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(onMeExitChatRoomSuccessV2), onMeExitChatRoomSuccessV2);
        self.currentRoomInfo = nil;
        [self resetAllQueue];
        return;
    }
    
    @weakify(self);
    [HJIMRequestManager exitRoomWithRoomId:[NSString stringWithFormat:@"%ld",(long)roomId] success:^{
        @strongify(self);
        self.currentRoomInfo = nil;
        [self resetAllQueue];
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(onMeExitChatRoomSuccessV2), onMeExitChatRoomSuccessV2);
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        @strongify(self);
        self.currentRoomInfo = nil;
        [self resetAllQueue];
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(onMeExitChatRoomSuccessV2), onMeExitChatRoomSuccessV2);
    }];
}

//kick
- (void)kickUser:(UserID)beKickedUid didFinish:(void(^)())didFinish {
//    [GetCore(RoomQueueCoreV2) removeInMicQueUserArrWith:[NSString stringWithFormat:@"%lld",beKickedUid] roomid:nil islossNet:YES];
    [GetCore(HJRoomQueueCoreV2Help) removeUserPositionLocation:[NSString stringWithFormat:@"%lld",beKickedUid]];
    @weakify(self);
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        @strongify(self);
        NIMChatroomMemberKickRequest *request = [[NIMChatroomMemberKickRequest alloc] init];
        request.roomId = [NSString stringWithFormat:@"%ld",(long)self.currentRoomInfo.roomId];
        request.userId = [NSString stringWithFormat:@"%lld",beKickedUid];
        [[NIMSDK sharedSDK].chatroomManager kickMember:request completion:^(NSError * _Nullable error) {
            if (error == nil) {
                if (didFinish) {
                    didFinish();
                }
            }
        }];
        
        [HJIMRequestManager kickRoomMemberWithRoomId:[NSString stringWithFormat:@"%ld", (long)self.currentRoomInfo.roomId] account:[NSString stringWithFormat:@"%lld",beKickedUid] uid:[GetCore(HJAuthCoreHelp) getUid] ticket:[GetCore(HJAuthCoreHelp) getTicket] success:^{
            if (didFinish) {
                didFinish();
            }
        } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
            
        }];
    });
}

- (void)queryChatRoomMembersWithPage:(int)page state:(int)state{
    self.state = state;
    [self.displayMembers removeAllObjects];
    if (page ==1) {
        self.lastPosition = 0;
        if (self.roomOwner != nil && ![self isOnMicro:self.currentRoomInfo.uid]) {
            [self.displayMembers addObject:self.roomOwner];
        }
//        [self.displayMembers addObjectsFromArray:[self getMicMembersFromMicQueue]];
        [self fetchRoomRegularMembers];
    } else {
//        self.lastPosition += 1;
        [self fetchRoomRegularMembers];
    }
}

- (void)queryNoMicChatRoomMembersWithPage:(int)page state:(int)state{
    self.state = state;
    [self.noMicMembers removeAllObjects];
//    if (page==1) {
//        if (self.roomOwner != nil && ![self isOnMicro:self.currentRoomInfo.uid]) {
//            [self.noMicMembers addObject:self.roomOwner];
//        }
//    }
    [self fetchRoomRegularMembers];
}

- (RACSignal *)rac_queryQueueWithRoomId:(NSString *)roomId{
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        [HJIMRequestManager fetchRoomQueueSuccess:^(NSMutableArray<HJIMQueueItem *> * _Nonnull queueList) {
            [subscriber sendNext:queueList];
            [subscriber sendCompleted];
        } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
            [subscriber sendCompleted];
        }];
        return nil;
    }];
}

//拿麦序成员
- (void)queryQueueWithRoomId:(NSString *)roomId{
    if ([roomId isEqualToString:@"25561748"]) {return;}
    @weakify(self);

    [HJIMRequestManager fetchRoomQueueSuccess:^(NSMutableArray<HJIMQueueItem *> * _Nonnull queueList) {
        @strongify(self);
        self.micQueue = queueList;
        HJIMQueueItem *micSequence = [self.micQueue safeObjectAtIndex:0];
        [self.micQueue replaceObjectAtIndex:0 withObject:micSequence];
        
        HJIMQueueItem *mySeq = nil;
        for (HJIMQueueItem *item in queueList) {
            HJRoomQueueInfo *sequence = ((HJIMQueueItem *)[self.micQueue safeObjectAtIndex:item.position+1]).queueInfo;
            if (sequence == nil) {
                sequence = [HJRoomQueueInfo new];
            }
            if ([sequence.chatRoomMember.account longLongValue] == [item.queueInfo.chatRoomMember.account longLongValue]) {
                sequence = item.queueInfo;
                if ([sequence.chatRoomMember.account longLongValue] != 0 && [sequence.chatRoomMember.account longLongValue] == [GetCore(HJAuthCoreHelp) getUid].integerValue) {
                    mySeq = item;
                }
                break;
            }
        }
        
        [self performSelector:@selector(setLoading) withObject:nil afterDelay:1.0];
        if (mySeq) {
            [GetCore(HJRoomQueueCoreV2Help) setUpMicStateWith:[NSString stringWithFormat:@"%ld",(long)mySeq.position] userId:[mySeq.queueInfo.chatRoomMember.account longLongValue]];
        }
        
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(onGetRoomQueueSuccessV2:), onGetRoomQueueSuccessV2:self.micQueue);
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        NotifyCoreClient(HJImRoomCoreClient, @selector(onGetRoomQueueFailth:), onGetRoomQueueFailth:errorMessage);

    }];
}

//根据uids获取对应的ChatRoomMember
- (void)queryChartRoomMembersWithUids:(NSString *)uid {
    if ([uid isEqualToString:@"25561748"]) {
        return;
    }
    
    [HJIMRequestManager getRoomMembersByIdsWithRoomId:[NSString stringWithFormat:@"%ld",(long)self.currentRoomInfo.roomId] accounts:uid uid:GetCore(HJAuthCoreHelp).getUid ticket:GetCore(HJAuthCoreHelp).getTicket success:^(NSArray<ChatRoomMember *> *roomMembers) {
        //找出自己的
        __block HJIMQueueItem *mySeq = nil;
        for (ChatRoomMember *member in roomMembers) {
            [self.micQueue enumerateObjectsUsingBlock:^(HJIMQueueItem * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                if ([obj.queueInfo.chatRoomMember.account longLongValue] == [member.account longLongValue]) {
                    obj.queueInfo.chatRoomMember = member;
                    if ([obj.queueInfo.chatRoomMember.account longLongValue] != 0 && [obj.queueInfo.chatRoomMember.account longLongValue] == [GetCore(HJAuthCoreHelp) getUid].integerValue) {
                        mySeq = obj;
                    }
                    *stop = YES;
                }
            }];
        }

        [self performSelector:@selector(setLoading) withObject:nil afterDelay:1.0];
        if (mySeq) {
            [GetCore(HJRoomQueueCoreV2Help) setUpMicStateWith:[NSString stringWithFormat:@"%ld",(long)mySeq.position] userId:[mySeq.queueInfo.chatRoomMember.account longLongValue]];
        }

        NotifyCoreClient(HJImRoomCoreClientV2, @selector(onGetRoomQueueSuccessV2:), onGetRoomQueueSuccessV2:self.micQueue);
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {

    }];
}

- (void)queryManagerorBackList{
    [self getBackListWithPage:1];
}

//根据uid获取chartroommember
- (RACSignal *)rac_queryChartRoomMemberByUid:(NSString *)uid {
    if (uid == nil || uid.length<=0) {
        return nil;
    }
    
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        [HJIMRequestManager getRoomMembersByIdsWithRoomId:[NSString stringWithFormat:@"%ld",(long)self.currentRoomInfo.roomId] accounts:uid uid:GetCore(HJAuthCoreHelp).getUid ticket:GetCore(HJAuthCoreHelp).getTicket success:^(NSArray<ChatRoomMember *> *roomMembers) {
            [subscriber sendNext:roomMembers.firstObject];
            [subscriber sendCompleted];
        } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
            NSError *error = [NSError errorWithDomain:JX_STR_AVOID_nil(errorMessage) code:code userInfo:nil];
            [subscriber sendError:error];
        }];
        return nil;
    }];
}

//设置/取消管理员
- (void)markManagerList:(UserID)userID enable:(BOOL)enable{
    JXIMRoomManagerMarkActionType type = enable ? JXIMRoomManagerMarkActionTypeAdd : JXIMRoomManagerMarkActionTypeRemove;
    [HJIMRequestManager markRoomMamagerWithActionType:type roomId:[NSString stringWithFormat:@"%ld",(long)self.currentRoomInfo.roomId] account:[NSString stringWithFormat:@"%lld",userID] uid:GetCore(HJAuthCoreHelp).getUid ticket:GetCore(HJAuthCoreHelp).getTicket success:^{
        if (enable) {
             NotifyCoreClient(HJImRoomCoreClientV2, @selector(setManagerMemberSuccess:uid:), setManagerMemberSuccess:YES uid:userID);
        }else{
             NotifyCoreClient(HJImRoomCoreClientV2, @selector(setManagerMemberSuccess:uid:), setManagerMemberSuccess:NO uid:userID);
        }
        
        
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        
    }];
}

//设置/取消黑名单
- (void)markBlackList:(UserID)userID enable:(BOOL)enable{
    
//    [GetCore(RoomQueueCoreV2) removeInMicQueUserArrWith:[NSString stringWithFormat:@"%lld",userID] roomid:nil islossNet:YES];
    @weakify(self);
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        @strongify(self);
        JXIMBlacklistMarkActionType type = enable ? JXIMBlacklistMarkActionTypeAdd :  JXIMBlacklistMarkActionTypeRemove;
        [HJIMRequestManager markRoomBlackListWithActionType:type roomId:[NSString stringWithFormat:@"%ld",(long)self.currentRoomInfo.roomId] account:[NSString stringWithFormat:@"%lld",userID] uid:GetCore(HJAuthCoreHelp).getUid ticket:GetCore(HJAuthCoreHelp).getTicket success:^{
            
        } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
            
        }];
    });
}

- (RACSignal *)rac_fetchMemberUserInfoByUid:(NSString *)uid {
    if (uid == nil || uid.length<=0) {
        return nil;
    }
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        NIMUser *user = [[NIMSDK sharedSDK].userManager userInfo:uid];//从本地获取用户资料
        if (user.userInfo) {
            [subscriber sendNext:user];
            [subscriber sendCompleted];
        }else {
            NSArray *uids = @[uid];
            //从云信服务器批量获取用户资料
            [[NIMSDK sharedSDK].userManager fetchUserInfos:uids completion:^(NSArray<NIMUser *> * _Nullable users, NSError * _Nullable error) {
                if (error == nil) {
                    [subscriber sendNext:users[0]];
                    [subscriber sendCompleted];
                }else {
                    [subscriber sendError:error];
                }
                
            }];
        }
        return nil;
    }];
}

- (RACSignal *)rac_fetchChatRoomInfoByRoomId:(NSString *)roomId{
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        @weakify(self);
        [[NIMSDK sharedSDK].chatroomManager fetchChatroomInfo:roomId completion:^(NSError * _Nullable error, NIMChatroom * _Nullable chatroom) {
            @strongify(self);
            NSString *roomExt = chatroom.ext;
            NSDictionary *roomExtD = [JSONTools ll_dictionaryWithJSON:roomExt];
            if ([roomExtD containsObjectForKey:@"factor"]) {
                self.onlineNumber = chatroom.onlineUserCount + [roomExtD[@"factor"] intValue];
            } else {
                self.onlineNumber = chatroom.onlineUserCount;
            }
            if (error==nil) {
                [subscriber sendNext:chatroom];
                [subscriber sendCompleted];
            }else{
                [subscriber sendError:error];
            }
        }];

        return nil;
    }];
}


//判断是否在房间
- (BOOL)isInRoom {
    if (_currentRoomInfo != nil) {
        return YES;
    }else {
        return NO;
    }
}

//发送消息
//发送消息
- (void)sendMessage:(NSString *)message success:(void (^)())success
            failure:(void (^)(NSInteger code, NSString *errorMessage))failure {
    NSString *roomId = [NSString stringWithFormat:@"%ld", (long)self.currentRoomInfo.roomId];
    ChatRoomMember *member = GetCore(HJImRoomCoreV2).myMember;
    member.room_id = roomId;
    [GetCore(HJImMessageCore) sendRoomTextMessage:message roomId:roomId member:member success:success failure:failure];
}

#pragma mark - private method
- (void)setLoading{
    self.isLoading = NO;
}
- (void)updateOnLineNmuber:(BOOL)isAdd{
    
}


/**
 获取聊天室黑名单
 */
- (void)getBackListWithPage:(NSInteger)page {
    if (page == 1) {self.lastBlackPosition = 0;}
    @weakify(self);
    [HJIMRequestManager getRoomBlackMembersWithStart:self.lastBlackPosition count:PageCount roomId:[NSString stringWithFormat:@"%ld", GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] success:^(NSArray<ChatRoomMember *> *roomMembers) {
        @strongify(self);
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(fetchBlackMemberSuccess:page:), fetchBlackMemberSuccess:roomMembers page:page);
        if (roomMembers.count < PageCount) {
            NotifyCoreClient(HJImRoomCoreClientV2, @selector(fetchBlackMemberNoMoreData), fetchBlackMemberNoMoreData);
            self.lastBlackPosition = self.lastBlackPosition + roomMembers.count;
        } else {
            self.lastBlackPosition = self.lastBlackPosition + PageCount;
        }
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(fetchBlackMemberfailure), fetchBlackMemberfailure);
    }];
}

- (void)getManagerList {
    
    [HJIMRequestManager getRoomManagersWithRoomId:[NSString stringWithFormat:@"%ld", GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] success:^(NSArray<ChatRoomMember *> *roomMembers) {
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(fetchManagerMemberSuccess:), fetchManagerMemberSuccess:roomMembers);
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(fetchManagerMemberfailure), fetchManagerMemberfailure);
    }];
}


/**
 获取聊天室在线成员
 */
- (void)fetchRoomRegularMembers {
    @weakify(self);
    [HJIMRequestManager getRoomMembersWithStart:self.lastPosition count:PageCount success:^(NSArray<ChatRoomMember *> *roomMembers) {
        @strongify(self);
        
        NSLog(@"lastPosition:%ld",(long)self.lastPosition);
        
        [self.displayMembers removeAllObjects];
        [self.noMicMembers removeAllObjects];

        
        if (self.lastPosition == 0) {
            [self.onLineManagerMembers removeAllObjects];
            [self.normalMembers removeAllObjects];
        }
        
//        NSArray *noMicArr = [self removeMicMember:[roomMembers mutableCopy]];
        
        
        NSMutableArray *robotArr = [[NSMutableArray alloc] init];
        
        for (ChatRoomMember *item in roomMembers) {
            if (item.is_manager) {
                [self.onLineManagerMembers addObject:item];
            }else if (item.defUser == 3){
                [robotArr addObject:item];
            }else if (!item.is_creator){
                [self.normalMembers addObject:item];
            }
        }
        
//        if (self.roomOwner.is_online && self.roomOwner!=nil) {
//            [self.displayMembers insertObject:self.roomOwner atIndex:0];
//        }
        
        [self.displayMembers addObjectsFromArray:self.micMembers];
        [self.displayMembers addObjectsFromArray:[self removeMicMember:self.onLineManagerMembers]];
        [self.displayMembers addObjectsFromArray:[self removeMicMember:self.normalMembers]];
        [self.displayMembers addObjectsFromArray:robotArr];
        
        //添加到未上麦的数组，用于抱麦
        [self.noMicMembers addObjectsFromArray:[self removeMicMember:self.onLineManagerMembers]];
        [self.noMicMembers addObjectsFromArray:[self removeMicMember:self.normalMembers]];

        //manager
//        [self.displayMembers addObjectsFromArray:[self removeMicMember:self.onLineManagerMembers]];
//        [self.noMicMembers addObjectsFromArray:[self removeMicMember:self.onLineManagerMembers]];
//        //normal
//        [self.displayMembers addObjectsFromArray:[self removeMicMember:self.normalMembers]];
//        [self.noMicMembers addObjectsFromArray:[self removeMicMember:self.normalMembers]];
        
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(fetchRoomUserListSuccess:), fetchRoomUserListSuccess:self.state);
        if (roomMembers.count < PageCount) {
            NotifyCoreClient(HJImRoomCoreClientV2, @selector(fetchRoomUserListNoMoreData), fetchRoomUserListNoMoreData);
        } else {
            self.lastPosition += 1;
        }
        
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        NSLog(@"获取在线列表成员失败");
    }];
}

- (void)handleMemagerNotificationMessage:(NSString *)uid {
    [[self rac_queryChartRoomMemberByUid:uid] subscribeNext:^(id x) {
        ChatRoomMember *member = (ChatRoomMember *)x;
        if ([member.account longLongValue] == [GetCore(HJAuthCoreHelp)getUid].userIDValue) {
            self.myMember = member;
        }
    }];
}

//去除麦上的成员
- (NSArray *)removeMicMember:(NSMutableArray *)array{
    NSArray *arrayFilter = [self getMicMembersFromMicQueue];
    if (arrayFilter.count <= 0) {
        return [array copy];
    }
    NSMutableArray *temp = [NSMutableArray arrayWithArray:array];
    NSMutableArray *delMembers = [NSMutableArray array];
    [temp enumerateObjectsUsingBlock:^(ChatRoomMember *member, NSUInteger idx, BOOL * _Nonnull stop) {
        for (ChatRoomMember *filterMember in arrayFilter) {
            if ([member.account isEqualToString:filterMember.account]) {
                [delMembers addObject:member];
            }
        }
    }];
    
    return [self delSameItem:temp inArray:delMembers];
}

- (NSArray *)delSameItem:(NSMutableArray *)array inArray:(NSMutableArray *)filter{
    NSMutableSet *set = [[NSMutableSet alloc] initWithArray:array];
    NSMutableSet *filterSet = [[NSMutableSet alloc] initWithArray:filter];
    [set minusSet:filterSet];
    return set.allObjects;
}

//判断是否在麦上
- (BOOL)isOnMicro:(UserID)uid{
    NSArray *micMembers = [self getMicMembersFromMicQueue];
    if (micMembers != nil && micMembers.count > 0) {
        for (int i = 0; i < micMembers.count; i ++) {
            ChatRoomMember *chatRoomMember = micMembers[i];
            if ([chatRoomMember.account longLongValue] == uid) {
                return YES;
            }
        }
    }
    return NO;
}

//获取麦上人
- (NSMutableArray *)getMicMembersFromMicQueue{
    NSMutableArray *temp = [NSMutableArray arrayWithCapacity:0];
    for (HJIMQueueItem *item in self.micQueue) {
        if (item.queueInfo.chatRoomMember) {
            if (![temp containsObject:item.queueInfo.chatRoomMember]) {
                if ([item.queueInfo.chatRoomMember.account longLongValue] == self.currentRoomInfo.uid) {
                    [temp insertObject:item.queueInfo.chatRoomMember atIndex:0];
                } else {
                    [temp addObject:item.queueInfo.chatRoomMember];
                }
            }
        }
    }
    return temp;
}

//绑定麦序
- (void)repairMicQueue {
    [self.micQueue removeAllObjects];
}

//重置member
- (void)resetMicQueueChatRoomMember {
    for (int i = 0 ; i < MIC_COUNT; i++) {
        HJRoomQueueInfo *micSequence = ((HJIMQueueItem *)[self.micQueue safeObjectAtIndex:i]).queueInfo;
        if (micSequence != nil) {
            micSequence.chatRoomMember = nil;
            
        }
    }
}

//重置麦序
- (void)resetMicQueueQueueInfo {
    for (int i = 0 ; i < MIC_COUNT; i++) {
        HJRoomQueueInfo *micSequence = ((HJIMQueueItem *)[self.micQueue safeObjectAtIndex:i]).queueInfo;
        if (micSequence != nil) {
            HJRoomMicInfo *state = [[HJRoomMicInfo alloc] init];
            state.posState = JXIMRoomMicPostionStateUnlock;
            state.micState = JXIMRoomMicStateOpen;
            micSequence.mic_info = state;
        }
    }
}

//重置all array
- (void)resetAllQueue {
    [self resetMicQueueChatRoomMember];
    [self resetMicQueueQueueInfo];
    [self.displayMembers removeAllObjects];
    [self.onLineManagerMembers removeAllObjects];
    [self.allManagers removeAllObjects];
    [self.backLists removeAllObjects];
    [self.micMembers removeAllObjects];
    [self.micMembersNoRoomOwner removeAllObjects];
    [self.noMicMembers removeAllObjects];
    self.roomOwner = nil;
}

#pragma mark - Getter & Setter

- (void)setCurrentRoomInfo:(ChatRoomInfo *)currentRoomInfo{
    _currentRoomInfo = currentRoomInfo;
    NotifyCoreClient(HJImRoomCoreClientV2, @selector(onCurrentRoomInfoChanged), onCurrentRoomInfoChanged);
}

- (NSMutableArray<ChatRoomMember *> *)displayMembers{
    if (!_displayMembers) {
        _displayMembers = [NSMutableArray array];
    }
    return _displayMembers;
}
- (NSMutableArray<ChatRoomMember *> *)onLineManagerMembers{
    if (!_onLineManagerMembers) {
        _onLineManagerMembers = [NSMutableArray array];
    }
    return _onLineManagerMembers;
}

- (NSMutableArray<ChatRoomMember *> *)allManagers{
    if (!_allManagers) {
        _allManagers = [NSMutableArray array];
    }
    return _allManagers;
}
- (NSMutableArray<ChatRoomMember *> *)backLists{
    if (!_backLists) {
        _backLists = [NSMutableArray array];
    }
    return _backLists;
}

- (NSMutableArray<HJIMQueueItem *> *)micQueue {
    if (!_micQueue) {
        _micQueue = [NSMutableArray array];
    }
    return _micQueue;
}

- (NSMutableArray<ChatRoomMember *> *)micMembers{
    if (!_micMembers) {
        _micMembers = [NSMutableArray array];
    }else{
        [_micMembers removeAllObjects];
    }
    [_micMembers addObjectsFromArray:[self getMicMembersFromMicQueue]];
    return _micMembers;
}
- (NSMutableArray<ChatRoomMember *> *)micMembersNoRoomOwner{
    if (!_micMembersNoRoomOwner) {
        _micMembersNoRoomOwner = [NSMutableArray array];
    }else{
        [_micMembersNoRoomOwner removeAllObjects];
    }
    [_micMembersNoRoomOwner addObjectsFromArray:[self getMicMembersFromMicQueue]];
    [_micMembersNoRoomOwner enumerateObjectsUsingBlock:^(ChatRoomMember * _Nonnull member, NSUInteger idx, BOOL * _Nonnull stop) {
        if (self.currentRoomInfo.type != RoomType_Game) {
            if ([member.account longLongValue] == self.currentRoomInfo.uid) {
                [_micMembersNoRoomOwner removeObject:member];
                *stop = YES;
            }
        }
    }];
    return _micMembersNoRoomOwner;
}

- (NSMutableArray<ChatRoomMember *> *)noMicMembers{
    if (!_noMicMembers) {
        _noMicMembers = [NSMutableArray array];
    }
    return _noMicMembers;
}



- (NSMutableArray<ChatRoomMember *> *)normalMembers{
    if (!_normalMembers) {
        _normalMembers = [NSMutableArray array];
    }
    return _normalMembers;
}
@end
