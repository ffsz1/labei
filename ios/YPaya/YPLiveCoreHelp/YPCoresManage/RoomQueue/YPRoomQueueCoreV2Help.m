//
//  RoomQueueCoreV2.m
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomQueueCoreV2Help.h"
#import "HJRoomQueueCoreClient.h"
#import <NIMSDK/NIMSDK.h>

#import "YPAuthCoreHelp.h"
#import "YPUserCoreHelp.h"
#import "YPMeetingCore.h"
#import "YPRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "YPImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "YPImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"

#import "YPInviteMicAttachment.h"
#import "YPRoomQueueCustomAttachment.h"

#import "YPHttpRequestHelper+Room.h"
#import "NSObject+YYModel.h"
#import "NSDictionary+JSON.h"
#import "NSString+JsonToDic.h"
#import "YPMusicCore.h"
#import <YYCategories.h>
#import <YYModel.h>
#import "HJWebSocketReceiveCoreClient.h"
#import "YPIMRequestManager+RoomQueue.h"
#import "HJWebSocketCoreClient.h"

#define MIC_COUNT 9

@interface YPRoomQueueCoreV2Help()<
HJImRoomCoreClient,
HJImRoomCoreClientV2,
HJRoomCoreClient,
HJImMessageCoreClient,
HJWebSocketReceiveCoreClient,
HJWebSocketCoreClient
>

@property (nonatomic, strong) NSMutableArray<YPIMQueueItem *> *micQueue;
@property (assign, nonatomic) BOOL needCloseMicro;
@property (copy, nonatomic) NSString *position;
@property (assign, nonatomic) BOOL isReConnect;
@end

@implementation YPRoomQueueCoreV2Help

- (instancetype)init {
    self = [super init];
    if (self) {
        AddCoreClient(HJImRoomCoreClient, self);
        AddCoreClient(HJImRoomCoreClientV2, self);
        AddCoreClient(HJRoomCoreClient, self);
        AddCoreClient(HJImMessageCoreClient, self);
        AddCoreClient(HJWebSocketReceiveCoreClient, self);
        AddCoreClient(HJWebSocketCoreClient, self);
    }
    return self;
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

#pragma mark - ImRoomCoreClientV2
- (void)onGetRoomQueueSuccessV2:(NSMutableArray<YPIMQueueItem *> *)info{
    self.micQueue = info;
}

- (void)sendLeaveRoomSuccess:(void (^)(void))successs failure:(void (^)(NSString *))failure {
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
//    [GetCore(RoomQueueCoreV2) removeInMicQueUserArrWith:[GetCore(AuthCore) getUid] roomid:nil islossNet:false];
    if ([self findTheRoomQueueMemberInfo:uid]) {
        NSString *position = [self findThePositionByUid:uid];
        if (position) {
            [self removeChatroomQueueWithPosition:position success:^(BOOL suc) {
                if (successs) {
                    successs();
                }
            } failure:^(NSString *message) {
                if (failure) {
                    failure(@"");
                }
            }];
            YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]];
            YPRoomQueueInfo *micsequence = item.queueInfo;
            micsequence.chatRoomMember = nil;
        } else {
            if (failure) {
                failure(@"");
            }
        }
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(onGetRoomQueueSuccessV2:), onGetRoomQueueSuccessV2:self.micQueue);
    } else {
        if (failure) {
            failure(@"");
        }
    }
}

//用户离开聊天室
- (void)onUserExitChatRoom:(NSString *)roomId uid:(NSString *)uid {
#ifdef DEBUG
    if ([roomId isEqualToString:@"25501704"]) {
        return;
    }
#else
    if ([roomId isEqualToString:@"25561748"]) {
        return;
    }
#endif
   
    if (uid.userIDValue == GetCore(YPImRoomCoreV2).currentRoomInfo.uid
        ) {
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(checkRoomerOnline:), checkRoomerOnline:false);

        NotifyCoreClient(HJImRoomCoreClientV2, @selector(onRoomerExitChatRoomSuccessV2), onRoomerExitChatRoomSuccessV2);
        //                GetCore(ImRoomCoreV2).roomOwner = nil;
    }
    
    if ([self findTheRoomQueueMemberInfo:uid.userIDValue]) {
        NSString *position = [self findThePositionByUid:uid.userIDValue];
        if (position) {
            YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]];
            YPRoomQueueInfo *micsequence = item.queueInfo;
            micsequence.chatRoomMember = nil;
            NotifyCoreClient(HJRoomCoreClient, @selector(bekillSuccess), bekillSuccess);
        }
    }
    
    if (uid.userIDValue == [[GetCore(YPAuthCoreHelp) getUid] longLongValue]) {
//        [GetCore(RoomQueueCoreV2) removeUserInWaitMicrQueue:[GetCore(AuthCore) getUid] roomid:nil islossNet:false];
    }
}

- (void)removeUserPositionLocation:(NSString *)userId {
    if ([self isOnMicro:[userId longLongValue]]) {
        if ([self findTheRoomQueueMemberInfo:[userId longLongValue]]) {
            NSString *position = [self findThePositionByUid:[userId longLongValue]];
            if (position) {
                
                [self removeChatroomQueueWithPosition:position success:^(BOOL suc) {
                   
                } failure:^(NSString *message) {

                }];
                YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]];
                YPRoomQueueInfo *micsequence = item.queueInfo;
                micsequence.chatRoomMember = nil;
                NotifyCoreClient(HJRoomCoreClient, @selector(bekillSuccess), bekillSuccess);
            }
        }
    }
}

//是否可以置顶
- (BOOL)hasMadeTopAuthority {
    YPChatRoomMember *member = GetCore(YPImRoomCoreV2).myMember;
    if (member.is_creator || member.is_manager) {
        return YES;
    }
    return false;
}

//房主不排麦
- (BOOL)checkIsRoomer {
    NSString *userId = [GetCore(YPAuthCoreHelp) getUid];
    if ([GetCore(YPImRoomCoreV2).roomOwner.account isEqualToString:userId]) {
        return YES;
    }
    return false;
}

- (BOOL)checkIsRoomerWith:(NSString *)uid {
    if ([GetCore(YPImRoomCoreV2).roomOwner.account isEqualToString:uid]) {
        return YES;
    }
    return false;
}

- (void)sendUpMicroMessage {
    NSString *postion = [self findFreeMicroPosition];
    if (postion) {
        NSDictionary *valueDic = @{@"key":postion};
        NSString *value = valueDic.yy_modelToJSONString;
        NSDictionary *dic = @{@"params":value};
        YPAttachment *attachement = [[YPAttachment alloc]init];
        attachement.first = Custom_Noti_Header_WaitQueue;
        attachement.second = Custom_Noti_Header_WaitQueue;
        attachement.data = dic;
        [GetCore(YPImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
    }
}

- (void)onQueueMicUpdateNotifyMessageRoomId:(NSString *)roomId key:(NSString *)key micInfo:(YPRoomMicInfo *)micInfo {
    if ([roomId longLongValue] != GetCore(YPImRoomCoreV2).currentRoomInfo.roomId) {return;}
    [self changeMicState:key roomQueue:micInfo];
    YPIMQueueItem *item = [self.micQueue safeObjectAtIndex:[key intValue]+1];
    YPRoomQueueInfo *micSequence = item.queueInfo;
    if (micSequence != nil) {
        micSequence.mic_info = micInfo;
        NotifyCoreClient(HJRoomQueueCoreClient, @selector(onMicroQueueUpdate:), onMicroQueueUpdate:self.micQueue);
    }
}

- (void)onQueueMemberUpdateNotifyMessageRoomId:(NSString *)roomId type:(NSInteger)type position:(NSString *)position member:(YPChatRoomMember *)member {
    if ([roomId longLongValue] != GetCore(YPImRoomCoreV2).currentRoomInfo.roomId) {return;}
    //去重复 如果该uid在房间先清空
    for (YPIMQueueItem *item in self.micQueue) {
        if ([item.queueInfo.chatRoomMember.account longLongValue] == [member.account longLongValue]) {
            item.queueInfo.chatRoomMember = nil;
        }
    }
    
    if (type == 1) {//上麦
        
        YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:[position integerValue] + 1];
        YPRoomQueueInfo *sequence = item.queueInfo;
        if (sequence.chatRoomMember) {
            NSString *uid = sequence.chatRoomMember.account;
            if ([uid isEqualToString:[GetCore(YPAuthCoreHelp) getUid]]) {
                [self setUpMicStateWith:nil userId:[sequence.chatRoomMember.account longLongValue]];
                NotifyCoreClient(HJImRoomCoreClientV2, @selector(showIsBeDownMic), showIsBeDownMic);
            }
        }
        sequence.chatRoomMember = member;
    }
    
    [self setUpMicStateWith:position userId:[member.account longLongValue]];
//    [GetCore(YPImRoomCoreV2) queryChartRoomMembersWithUids:member.account];
    
//    //找出自己的
//    __block YPIMQueueItem *mySeq = nil;
//    for (YPChatRoomMember *member in roomMembers) {
//        [self.micQueue enumerateObjectsUsingBlock:^(YPIMQueueItem * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
//            if ([obj.queueInfo.chatRoomMember.account longLongValue] == [member.account longLongValue]) {
//                obj.queueInfo.chatRoomMember = member;
//                if ([obj.queueInfo.chatRoomMember.account longLongValue] != 0 && [obj.queueInfo.chatRoomMember.account longLongValue] == [GetCore(YPAuthCoreHelp) getUid].integerValue) {
//                    mySeq = obj;
//                }
//                *stop = YES;
//            }
//        }];
//    }
//    
//    if (mySeq) {
//        [GetCore(YPRoomQueueCoreV2Help) setUpMicStateWith:[NSString stringWithFormat:@"%ld",(long)mySeq.position] userId:[mySeq.queueInfo.chatRoomMember.account longLongValue]];
//    }
    
    NotifyCoreClient(HJImRoomCoreClientV2, @selector(onGetRoomQueueSuccessV2:), onGetRoomQueueSuccessV2:self.micQueue);
    
}

- (NSString *)findFreeMicroPosition {
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        for (YPIMQueueItem *item in self.micQueue) {
            if (item.position != -1 && item.queueInfo.mic_info.posState == JXIMRoomMicPostionStateUnlock && !item.queueInfo.chatRoomMember) {
                return [NSString stringWithFormat:@"%ld",(long)item.position];
            }
        }
        return nil;
    }
    return nil;
}

/**
 通知：聊天室信息更新通知
 @param roomInfo 房间基本信息
 */
- (void)onRoomInfoUpdatedNotifyMessage:(YPChatRoomInfo *)roomInfo {
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        GetCore(YPImRoomCoreV2).currentRoomInfo = roomInfo;
        NotifyCoreClient(HJRoomCoreClient, @selector(onGameRoomInfoUpdateSuccess:isFromMessage:), onGameRoomInfoUpdateSuccess:roomInfo isFromMessage:YES);
    }
}

//麦序状态
- (void)onRoomInfoUpdate:(NIMMessage *)message{
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        if (message.messageType == NIMMessageTypeNotification) {
            NIMNotificationObject *notiMsg = (NIMNotificationObject *)message.messageObject;
            NIMChatroomNotificationContent *content = (NIMChatroomNotificationContent *)notiMsg.content;
            if (content.eventType == NIMChatroomEventTypeInfoUpdated) {
                NSDictionary *roomInfoDictionary = [NSString dictionaryWithJsonString:content.notifyExt];
                
                int type = [roomInfoDictionary[@"type"] intValue];
                switch (type) {
                    case 1:{
//                        YPChatRoomInfo *roomInfo = [YPChatRoomInfo yy_modelWithJSON:roomInfoDictionary[@"roomInfo"]];
                        
                    }
                        break;
                        
                    case 2:{
                    }
                        break;
                }
            }
        }
        
    }
}

- (void)onMeExitChatRoomSuccessV2 {
    self.micQueue = nil;
    self.myMember = nil;
    self.needCloseMicro = NO;
}

- (void)onMeInterChatRoomSuccess {
    if (self.isReConnect) {
        if (self.isReConnect && self.position.length > 0) {
            YPIMQueueItem *item = [self.micQueue safeObjectAtIndex:[self.position intValue]+1];
            YPRoomQueueInfo *sequence = item.queueInfo;
            if (sequence.mic_info.posState == JXIMRoomMicPostionStateUnlock && !sequence.chatRoomMember) {
                [self upMic:self.position.intValue];
            } else if ([sequence.chatRoomMember.account longLongValue] == [GetCore(YPAuthCoreHelp).getUid longLongValue]) {
                
                //判断是否开mic功能
                if (sequence.mic_info.micState == JXIMRoomMicStateOpen) {
                    [GetCore(YPMeetingCore) setMeetingRole:YES];
                }else{
                    [GetCore(YPMeetingCore) setMeetingRole:NO];
                }
                
            }
            self.position = nil;
        }
        NotifyCoreClient(HJRoomQueueCoreClient, @selector(microresetNetSuccess), microresetNetSuccess);
        self.isReConnect = false;
    }
}

- (void)onSocLoginSuccess {
    YPChatRoomInfo *roomInfo = GetCore(YPImRoomCoreV2).currentRoomInfo;
    if (roomInfo != nil) {
        [GetCore(YPImRoomCoreV2) enterChatRoom:roomInfo.roomId];
    }
}

- (void)onDisconnect:(NSInteger)code reason:(NSString *)reason {
    if ([self isOnMicro:[GetCore(YPAuthCoreHelp)getUid].userIDValue]) {
        self.position = [self findThePositionByUid:[GetCore(YPAuthCoreHelp) getUid].userIDValue];
    }
//    self.needCloseMicro = GetCore(MeetingCore).isCloseMicro;
    [GetCore(YPMeetingCore) setMeetingRole:false];
    self.isReConnect = true;
}


#pragma mark - ImMessageCoreClient
- (void)onRecvChatRoomCustomMsg:(YPIMMessage *)msg {
    JXIMCustomObject *customObject = msg.messageObject;
    if (customObject.attachment) {
        YPAttachment *attachment = (YPAttachment *)customObject.attachment;
        [self handelCustomerChatRoomMessage:attachment];
    }
}

#pragma mark - puble method
//解除麦位封锁
- (void)freeMicPlace:(int)position{
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        [self handleMicPlace:position state:0];
    }
}

//封锁麦位
- (void)lockMicPlace:(int)position{
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        [self handleMicPlace:position state:1];
    }
}

//1：锁坑位，0取消锁（即取消锁坑位）
- (void)handleMicPlace:(int)position state:(int)state{
    UserID uid = GetCore(YPImRoomCoreV2).currentRoomInfo.uid;
    [YPHttpRequestHelper micPlace:position roomOwnerUid:uid state:state success:^{
        
    } failure:^(NSNumber *resCode, NSString *message) {
    }];
}

//麦位静音
- (void)closeMic:(int)position{
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        [self handleMicState:position state:1];
    }
}
//取消麦位静音
- (void)openMic:(int)position{
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        [self handleMicState:position state:0];
    }
}

//1：锁麦，0开麦（即取消锁麦）
- (void)handleMicState:(int)position state:(int)state{
    UserID uid = GetCore(YPImRoomCoreV2).currentRoomInfo.uid;
    [YPHttpRequestHelper micState:position roomOwnerUid:uid state:state success:^{
    } failure:^(NSNumber *resCode, NSString *message) {
    }];
}

//下麦
- (void)downMic{
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
        if ([self isOnMicro:uid]) {
            NSString *position = [self findThePositionByUid:uid];
            
            
            [self removeChatroomQueueWithPosition:position success:^(BOOL success) {
                [GetCore(YPMusicCore) stopMusic];
            } failure:^(NSString *message) {
            }];
        }
    }
}

//上指定麦
- (void)upMic:(int)position invite:(BOOL)isInvite{
    if (GetCore(YPRoomCoreV2Help).isInRoom && !GetCore(YPImRoomCoreV2).isLoading) {
        YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:position + 1];
        YPRoomQueueInfo *queueInfo = item.queueInfo;
        YPRoomMicInfo *micstate = queueInfo.mic_info;
        UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
        UserInfo *userInfo = [GetCore(YPUserCoreHelp) getUserInfoInDB:uid];
        [[GetCore(YPImRoomCoreV2) rac_queryChartRoomMemberByUid:[NSString stringWithFormat:@"%lld",uid]] subscribeNext:^(id x) {
            YPChatRoomMember *member = (YPChatRoomMember *)x;
            
            if (member.is_manager || member.is_creator) {
                [self updateChatroomQueueWithPosition:[NSString stringWithFormat:@"%d",position] userInfo:userInfo success:^(BOOL success) {
                } failure:^(NSString *message) {
                }];
            }else{
                if (micstate.posState == JXIMRoomMicPostionStateUnlock || isInvite) {
                    if (userInfo != nil) {
                        [self updateChatroomQueueWithPosition:[NSString stringWithFormat:@"%d",position] userInfo:userInfo success:^(BOOL success) {
                            
                        } failure:^(NSString *message) {
                            
                        }];
                    }
                }
            }
        }];
    }
}

- (void)upMic:(int)position{
    [self upMic:position invite:NO];
}
//上一个空闲麦
- (void)upFreeMic{
    NSString *position = [self findFreeMicroPosition];
    if (position.length > 0) {
        [self upMic:[position intValue]];
    }
}

//踢它下麦
- (void)kickDownMic:(UserID)uid position:(int)position {
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        //request
        //        [HttpRequestHelper ownerKickUserByUid:[NSString stringWithFormat:@"%lld",uid] position:position roomId:(int)GetCore(ImRoomCoreV2).currentRoomInfo.roomId success:^(BOOL isSuccess) {
        //        } failure:^(NSNumber *resCode, NSString *message) {
        //        }];
        NSString *pos = [NSString stringWithFormat:@"%d",position];
        [self removeChatroomQueueWithPosition:pos success:^(BOOL success) {
            //NIMSDK
            [self kiedMircoWithUid:uid];
        } failure:^(NSString *message) {
        }];
    }
}

//邀请上指定麦位
- (void)inviteUpMic:(UserID)uid postion:(NSString *)position{
    //request
    //    [HttpRequestHelper inviteUpMicroWithUid:uid position:position.intValue roomId:(int)GetCore(ImRoomCoreV2).currentRoomInfo.roomId success:^(BOOL isSuccess) {
    //    } failure:^(NSNumber *resCode, NSString *message) {
    //    }];
    //NIMSDK
    [self inviteOnMic:uid postion:position];
}

//邀请上空闲麦
- (void)inviteUpFreeMic:(UserID)uid{
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        NSString *position = [self findFreeMicroPosition];
        if (position.length == 0) {
            NotifyCoreClient(HJRoomCoreClient, @selector(thereIsNoFreePosition), thereIsNoFreePosition);
        }else {
            [self inviteUpMic:uid postion:position];
        }
    }
}

//判断是否在麦上
- (BOOL)isOnMicro:(UserID)uid{
    NSArray *micMembers = [GetCore(YPImRoomCoreV2).micMembers copy] ;
    if (micMembers != nil && micMembers.count > 0) {
        for (int i = 0; i < micMembers.count; i ++) {
            YPChatRoomMember *chatRoomMember = micMembers[i];
            if ([chatRoomMember.account longLongValue] == uid) {
                return YES;
            }
        }
    }
    return NO;
}

//通过uid判断麦位
- (NSString *)findThePositionByUid:(UserID)uid{
    for (YPIMQueueItem *item in self.micQueue) {
        if ([item.queueInfo.chatRoomMember.account longLongValue] == uid) {
            return [NSString stringWithFormat:@"%ld",(long)item.position];
        }
    }
    return nil;
}

//通过uid查找member
- (YPChatRoomMember *)findTheMemberByUserId:(UserID)uid{
    for (YPIMQueueItem *item in self.micQueue) {
        if ([item.queueInfo.chatRoomMember.account longLongValue] == uid) {
            return item.queueInfo.chatRoomMember;
        }
    }
    return nil;
}

//通过uid查找麦序信息
- (YPRoomQueueInfo *)findTheRoomQueueMemberInfo:(UserID)uid{
    for (YPIMQueueItem *item in self.micQueue) {
        if ([item.queueInfo.chatRoomMember.account longLongValue] == uid) {
            return item.queueInfo;
        }
    }
    return nil;
}

- (NSMutableArray *)findSendGiftMember {
    NSMutableArray *temp = [NSMutableArray array];
    for (int i = 0; i < MIC_COUNT; i++) {
        YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:i];
        YPRoomQueueInfo *sequence = item.queueInfo;
        if (sequence.chatRoomMember) {
            [temp addObject:sequence];
        }
    }
    return temp;
}

- (NSMutableArray *)findOnMicMember
{
    NSMutableArray *temp = [NSMutableArray array];
    for (int i = 0; i < MIC_COUNT; i++) {
        YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:i];
        YPRoomQueueInfo *sequence = item.queueInfo;
        if (sequence.chatRoomMember) {
            [temp addObject:item];
        }
    }
    return temp;
}

#pragma mark - private method
//上麦后设置角色
- (void)setUpMicStateWith:(NSString *)position userId:(UserID)userId {
    if (position) {
        YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]+1];
        YPRoomQueueInfo *sequence = item.queueInfo;
        if (sequence && [sequence.chatRoomMember.account longLongValue] == GetCore(YPAuthCoreHelp).getUid.userIDValue) {
            YPRoomMicInfo *state = sequence.mic_info;
            if (self.needCloseMicro) {
                if (state.micState == JXIMRoomMicStateClose) {
                    [GetCore(YPMeetingCore) setMeetingRole:NO];
                }else {
                    [GetCore(YPMeetingCore) setMeetingRole:YES];
//                    [GetCore(MeetingCore) setCloseMicro:YES];
                }
                self.needCloseMicro = false;
            } else {
                if (state.micState == JXIMRoomMicStateClose) {
                    [GetCore(YPMeetingCore) setMeetingRole:NO];
                }else {
                    [GetCore(YPMeetingCore) setMeetingRole:YES];
//                    [GetCore(MeetingCore) setCloseMicro:NO];
                }
            }
        } else {
            if (userId == GetCore(YPAuthCoreHelp).getUid.userIDValue) {
                [GetCore(YPMeetingCore) setMeetingRole:NO];
            }
        }
    } else {
        [GetCore(YPMeetingCore)setMeetingRole:NO];
    }
}

- (void)handelCustomerChatRoomMessage:(YPAttachment *)attachment {
    //request
    //    if (attachment.first == Custom_Noti_Header_Queue) {
    //        YPInviteMicAttachment *inviteMicAttachment = [YPInviteMicAttachment yy_modelWithJSON:attachment.data];
    //        UserID uid = [GetCore(AuthCore) getUid].userIDValue;
    //        if (attachment.second == Custom_Noti_Sub_Queue_Invite) {
    //            if (uid == inviteMicAttachment.inviteUid.intValue) {
    //                if (![self isOnMicro:uid]) {
    //                    if ([GetCore(YPRoomCoreV2Help) getCurrentRoomInfo].type == RoomType_Game) {
    //                        self.needCloseMicro = YES;
    //                        [self upMic:inviteMicAttachment.position.intValue];
    //                    }
    //                }
    //                NotifyCoreClient(RoomQueueCoreClient, @selector(onMicroBeInvite), onMicroBeInvite);
    //            }
    //        }
    //    }
    // NIMSDK
    if (attachment.first == Custom_Noti_Header_Queue) {
        YPRoomQueueCustomAttachment *roomQueueAttachment = [YPRoomQueueCustomAttachment yy_modelWithJSON:attachment.data];
        UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
        if (attachment.second == Custom_Noti_Sub_Queue_Invite) {
            if (uid == roomQueueAttachment.uid) {
                if (GetCore(YPImRoomCoreV2).currentRoomInfo.type == RoomType_Game) {
                    if (![self isOnMicro:uid]) {
                        self.needCloseMicro = YES;
                        [self upMic:roomQueueAttachment.micPosition invite:YES];
                    }
                }
                //需求：被抱上麦，关闭麦克风
                [GetCore(YPMeetingCore) setCloseMicro:YES];

                NotifyCoreClient(HJRoomQueueCoreClient, @selector(onMicroBeInvite), onMicroBeInvite);
            }
        } else if (attachment.second == Custom_Noti_Sub_Queue_Kick) {
            if (uid == roomQueueAttachment.uid) {
                [GetCore(YPMusicCore) stopMusic];
                NotifyCoreClient(HJRoomQueueCoreClient, @selector(onMicroBeKicked), onMicroBeKicked);
            }
        }
    }
}

- (void)changeMicState:(NSString *)key roomQueue:(YPRoomMicInfo *)YPRoomMicInfo {
    YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:[key intValue]+1];
    YPRoomQueueInfo *micSequence = item.queueInfo;
    if (micSequence != nil) {
        YPChatRoomMember *chatRoomMember = micSequence.chatRoomMember;
        if (chatRoomMember != nil) {
            UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
            if (uid == [chatRoomMember.account longLongValue]) {
                if (YPRoomMicInfo.micState == JXIMRoomMicPostionStateLock) {
                    [GetCore(YPMeetingCore) setActor:NO];
                    [GetCore(YPMeetingCore) setCloseMicro:YES];
                    [GetCore(YPMusicCore) stopMusic];
                    NotifyCoreClient(HJRoomQueueCoreClient, @selector(onMicroLocked), onMicroLocked);
                } else {
                    [GetCore(YPMeetingCore) setActor:YES]; 
                    NotifyCoreClient(HJRoomQueueCoreClient, @selector(onMicroUnLocked), onMicroUnLocked);
                }
                NotifyCoreClient(HJRoomQueueCoreClient, @selector(onHJRoomMicInfoChange), onHJRoomMicInfoChange);
            }
        }
    }
}

//判断云信的是否已修改/本地
#pragma mark - NIMSDK
//通过NIMSDK修改队列上麦
- (void)updateChatroomQueueWithPosition:(NSString *)position
                               userInfo:(UserInfo *)userInfo
                                success:(void (^)(BOOL success))success
                                failure:(void (^)(NSString *message))failure {
    if ([position isEqualToString:@"-1"] && ![GetCore(YPRoomQueueCoreV2Help) checkIsRoomerWith:[NSString stringWithFormat:@"%lld",userInfo.uid]]) {return;}
    if (![position isEqualToString:@"-1"] && [GetCore(YPRoomQueueCoreV2Help) checkIsRoomerWith:[NSString stringWithFormat:@"%lld",userInfo.uid]]) {return;}

    //清空该uid之前在队列中的信息
    if ([self isOnMicro:userInfo.uid]) {
        @weakify(self);
        [YPIMRequestManager addOrUpdateMemberWithKey:position Success:^{
            NotifyCoreClient(HJImRoomCoreClientV2, @selector(onGetRoomQueueSuccessV2:), onGetRoomQueueSuccessV2:self.micQueue);
            [YYLogger info:@"上麦成功" message:@"key:%@--value:%@--roomId%zd",position,[userInfo yy_modelToJSONString],GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
        } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
            failure(errorMessage);
            [YYLogger info:@"上麦失败" message:@"error:%@--class:%@--function:updateChatroomQueueWithPosition:userInfo:success:failure:",errorMessage,NSStringFromClass([self class])];
        }];
    } else {
//        request.key = position;
//        request.value = [userInfo yy_modelToJSONString];
//        request.roomId = [NSString stringWithFormat:@"%d",(int)GetCore(ImRoomCoreV2).currentRoomInfo.roomId];
        @weakify(self);
        [YPIMRequestManager addOrUpdateMemberWithKey:position Success:^{
            NotifyCoreClient(HJImRoomCoreClientV2, @selector(onGetRoomQueueSuccessV2:), onGetRoomQueueSuccessV2:self.micQueue);
            [YYLogger info:@"上麦成功" message:@"key:%@--value:%@--roomId%zd",position,[userInfo yy_modelToJSONString],GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
        } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
            failure(errorMessage);
            [YYLogger info:@"上麦失败" message:@"error:%@--class:%@--function:updateChatroomQueueWithPosition:userInfo:success:failure:",errorMessage,NSStringFromClass([self class])];
        }];
    }
}

//通过NIMSDK修改队列下麦
- (void)removeChatroomQueueWithPosition:(NSString *)position
                                success:(void (^)(BOOL success))success
                                failure:(void (^)(NSString *message))failure{
    @weakify(self);
    [YPIMRequestManager removeQueueWithKey:position Success:^{
        @strongify(self);
        success(YES);
        [YYLogger info:@"下麦成功" message:@"key:%@--roomId%zd",position,GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        failure(errorMessage);
        [YYLogger info:@"下麦失败" message:@"error:%@--class:%@--function:removeChatroomQueueWithPosition:success:failure:",errorMessage,NSStringFromClass([self class])];
    }];
}

//邀请上麦
- (void)inviteOnMic:(UserID)uid postion:(NSString *)position{
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        YPRoomMicInfo *state = [[YPRoomMicInfo alloc]init];
        state.posState = JXIMRoomMicPostionStateUnlock;
        YPRoomQueueCustomAttachment *roomQueueAttachment = [[YPRoomQueueCustomAttachment alloc]init];
        roomQueueAttachment.uid = uid;
        roomQueueAttachment.YPRoomMicInfo = state;
        roomQueueAttachment.micPosition = position.intValue;
        
        YPAttachment *attachement = [[YPAttachment alloc]init];
        attachement.first = Custom_Noti_Header_Queue;
        attachement.second = Custom_Noti_Sub_Queue_Invite;
        attachement.data = roomQueueAttachment.encodeAttachment;
        
        [GetCore(YPImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
    }
}

//踢人下麦
- (void)kiedMircoWithUid:(UserID)uid {
    if (GetCore(YPRoomCoreV2Help).isInRoom) {
        YPRoomMicInfo *state = [[YPRoomMicInfo alloc]init];
        state.posState = JXIMRoomMicPostionStateUnlock;
        
        YPRoomQueueCustomAttachment *roomQueueAttachment = [[YPRoomQueueCustomAttachment alloc]init];
        roomQueueAttachment.uid = uid;
        roomQueueAttachment.YPRoomMicInfo = state;
        
        YPAttachment *attachement = [[YPAttachment alloc]init];
        attachement.first = Custom_Noti_Header_Queue;
        attachement.second = Custom_Noti_Sub_Queue_Kick;
        attachement.data = roomQueueAttachment.encodeAttachment;
        
        [GetCore(YPImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
    }
}

#pragma mark - Getter & Setter
- (NSMutableArray<YPIMQueueItem *> *)micQueue{
    return GetCore(YPImRoomCoreV2).micQueue;
}

- (YPChatRoomMember *)myMember{
    return GetCore(YPImRoomCoreV2).myMember;
}

- (YPChatRoomMember *)roomOwner{
    return GetCore(YPImRoomCoreV2).roomOwner;
}

@end
