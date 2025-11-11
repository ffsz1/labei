//
//  RoomQueueCoreV2.m
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomQueueCoreV2Help.h"
#import "HJRoomQueueCoreClient.h"
#import <NIMSDK/NIMSDK.h>

#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "HJMeetingCore.h"
#import "HJRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "HJImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "HJImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"

#import "HJInviteMicAttachment.h"
#import "HJRoomQueueCustomAttachment.h"

#import "HJHttpRequestHelper+Room.h"
#import "NSObject+YYModel.h"
#import "NSDictionary+JSON.h"
#import "NSString+JsonToDic.h"
#import "HJMusicCore.h"
#import <YYCategories.h>
#import <YYModel.h>
#import "HJWebSocketReceiveCoreClient.h"
#import "HJIMRequestManager+RoomQueue.h"
#import "HJWebSocketCoreClient.h"

#define MIC_COUNT 9

@interface HJRoomQueueCoreV2Help()<
HJImRoomCoreClient,
HJImRoomCoreClientV2,
HJRoomCoreClient,
HJImMessageCoreClient,
HJWebSocketReceiveCoreClient,
HJWebSocketCoreClient
>

@property (nonatomic, strong) NSMutableArray<HJIMQueueItem *> *micQueue;
@property (assign, nonatomic) BOOL needCloseMicro;
@property (copy, nonatomic) NSString *position;
@property (assign, nonatomic) BOOL isReConnect;
@end

@implementation HJRoomQueueCoreV2Help

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
- (void)onGetRoomQueueSuccessV2:(NSMutableArray<HJIMQueueItem *> *)info{
    self.micQueue = info;
}

- (void)sendLeaveRoomSuccess:(void (^)(void))successs failure:(void (^)(NSString *))failure {
    UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
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
            HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]];
            HJRoomQueueInfo *micsequence = item.queueInfo;
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
   
    if (uid.userIDValue == GetCore(HJImRoomCoreV2).currentRoomInfo.uid
        ) {
        NotifyCoreClient(HJImRoomCoreClientV2, @selector(checkRoomerOnline:), checkRoomerOnline:false);

        NotifyCoreClient(HJImRoomCoreClientV2, @selector(onRoomerExitChatRoomSuccessV2), onRoomerExitChatRoomSuccessV2);
        //                GetCore(ImRoomCoreV2).roomOwner = nil;
    }
    
    if ([self findTheRoomQueueMemberInfo:uid.userIDValue]) {
        NSString *position = [self findThePositionByUid:uid.userIDValue];
        if (position) {
            HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]];
            HJRoomQueueInfo *micsequence = item.queueInfo;
            micsequence.chatRoomMember = nil;
            NotifyCoreClient(HJRoomCoreClient, @selector(bekillSuccess), bekillSuccess);
        }
    }
    
    if (uid.userIDValue == [[GetCore(HJAuthCoreHelp) getUid] longLongValue]) {
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
                HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]];
                HJRoomQueueInfo *micsequence = item.queueInfo;
                micsequence.chatRoomMember = nil;
                NotifyCoreClient(HJRoomCoreClient, @selector(bekillSuccess), bekillSuccess);
            }
        }
    }
}

//是否可以置顶
- (BOOL)hasMadeTopAuthority {
    ChatRoomMember *member = GetCore(HJImRoomCoreV2).myMember;
    if (member.is_creator || member.is_manager) {
        return YES;
    }
    return false;
}

//房主不排麦
- (BOOL)checkIsRoomer {
    NSString *userId = [GetCore(HJAuthCoreHelp) getUid];
    if ([GetCore(HJImRoomCoreV2).roomOwner.account isEqualToString:userId]) {
        return YES;
    }
    return false;
}

- (BOOL)checkIsRoomerWith:(NSString *)uid {
    if ([GetCore(HJImRoomCoreV2).roomOwner.account isEqualToString:uid]) {
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
        Attachment *attachement = [[Attachment alloc]init];
        attachement.first = Custom_Noti_Header_WaitQueue;
        attachement.second = Custom_Noti_Header_WaitQueue;
        attachement.data = dic;
        [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
    }
}

- (void)onQueueMicUpdateNotifyMessageRoomId:(NSString *)roomId key:(NSString *)key micInfo:(HJRoomMicInfo *)micInfo {
    if ([roomId longLongValue] != GetCore(HJImRoomCoreV2).currentRoomInfo.roomId) {return;}
    [self changeMicState:key roomQueue:micInfo];
    HJIMQueueItem *item = [self.micQueue safeObjectAtIndex:[key intValue]+1];
    HJRoomQueueInfo *micSequence = item.queueInfo;
    if (micSequence != nil) {
        micSequence.mic_info = micInfo;
        NotifyCoreClient(HJRoomQueueCoreClient, @selector(onMicroQueueUpdate:), onMicroQueueUpdate:self.micQueue);
    }
}

- (void)onQueueMemberUpdateNotifyMessageRoomId:(NSString *)roomId type:(NSInteger)type position:(NSString *)position member:(ChatRoomMember *)member {
    if ([roomId longLongValue] != GetCore(HJImRoomCoreV2).currentRoomInfo.roomId) {return;}
    //去重复 如果该uid在房间先清空
    for (HJIMQueueItem *item in self.micQueue) {
        if ([item.queueInfo.chatRoomMember.account longLongValue] == [member.account longLongValue]) {
            item.queueInfo.chatRoomMember = nil;
        }
    }
    
    if (type == 1) {//上麦
        
        HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:[position integerValue] + 1];
        HJRoomQueueInfo *sequence = item.queueInfo;
        if (sequence.chatRoomMember) {
            NSString *uid = sequence.chatRoomMember.account;
            if ([uid isEqualToString:[GetCore(HJAuthCoreHelp) getUid]]) {
                [self setUpMicStateWith:nil userId:[sequence.chatRoomMember.account longLongValue]];
                NotifyCoreClient(HJImRoomCoreClientV2, @selector(showIsBeDownMic), showIsBeDownMic);
            }
        }
        sequence.chatRoomMember = member;
    }
    
    [self setUpMicStateWith:position userId:[member.account longLongValue]];
//    [GetCore(HJImRoomCoreV2) queryChartRoomMembersWithUids:member.account];
    
//    //找出自己的
//    __block HJIMQueueItem *mySeq = nil;
//    for (ChatRoomMember *member in roomMembers) {
//        [self.micQueue enumerateObjectsUsingBlock:^(HJIMQueueItem * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
//            if ([obj.queueInfo.chatRoomMember.account longLongValue] == [member.account longLongValue]) {
//                obj.queueInfo.chatRoomMember = member;
//                if ([obj.queueInfo.chatRoomMember.account longLongValue] != 0 && [obj.queueInfo.chatRoomMember.account longLongValue] == [GetCore(HJAuthCoreHelp) getUid].integerValue) {
//                    mySeq = obj;
//                }
//                *stop = YES;
//            }
//        }];
//    }
//    
//    if (mySeq) {
//        [GetCore(HJRoomQueueCoreV2Help) setUpMicStateWith:[NSString stringWithFormat:@"%ld",(long)mySeq.position] userId:[mySeq.queueInfo.chatRoomMember.account longLongValue]];
//    }
    
    NotifyCoreClient(HJImRoomCoreClientV2, @selector(onGetRoomQueueSuccessV2:), onGetRoomQueueSuccessV2:self.micQueue);
    
}

- (NSString *)findFreeMicroPosition {
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        for (HJIMQueueItem *item in self.micQueue) {
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
- (void)onRoomInfoUpdatedNotifyMessage:(ChatRoomInfo *)roomInfo {
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        GetCore(HJImRoomCoreV2).currentRoomInfo = roomInfo;
        NotifyCoreClient(HJRoomCoreClient, @selector(onGameRoomInfoUpdateSuccess:isFromMessage:), onGameRoomInfoUpdateSuccess:roomInfo isFromMessage:YES);
    }
}

//麦序状态
- (void)onRoomInfoUpdate:(NIMMessage *)message{
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        if (message.messageType == NIMMessageTypeNotification) {
            NIMNotificationObject *notiMsg = (NIMNotificationObject *)message.messageObject;
            NIMChatroomNotificationContent *content = (NIMChatroomNotificationContent *)notiMsg.content;
            if (content.eventType == NIMChatroomEventTypeInfoUpdated) {
                NSDictionary *roomInfoDictionary = [NSString dictionaryWithJsonString:content.notifyExt];
                
                int type = [roomInfoDictionary[@"type"] intValue];
                switch (type) {
                    case 1:{
//                        ChatRoomInfo *roomInfo = [ChatRoomInfo yy_modelWithJSON:roomInfoDictionary[@"roomInfo"]];
                        
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
            HJIMQueueItem *item = [self.micQueue safeObjectAtIndex:[self.position intValue]+1];
            HJRoomQueueInfo *sequence = item.queueInfo;
            if (sequence.mic_info.posState == JXIMRoomMicPostionStateUnlock && !sequence.chatRoomMember) {
                [self upMic:self.position.intValue];
            } else if ([sequence.chatRoomMember.account longLongValue] == [GetCore(HJAuthCoreHelp).getUid longLongValue]) {
                
                //判断是否开mic功能
                if (sequence.mic_info.micState == JXIMRoomMicStateOpen) {
                    [GetCore(HJMeetingCore) setMeetingRole:YES];
                }else{
                    [GetCore(HJMeetingCore) setMeetingRole:NO];
                }
                
            }
            self.position = nil;
        }
        NotifyCoreClient(HJRoomQueueCoreClient, @selector(microresetNetSuccess), microresetNetSuccess);
        self.isReConnect = false;
    }
}

- (void)onSocLoginSuccess {
    ChatRoomInfo *roomInfo = GetCore(HJImRoomCoreV2).currentRoomInfo;
    if (roomInfo != nil) {
        [GetCore(HJImRoomCoreV2) enterChatRoom:roomInfo.roomId];
    }
}

- (void)onDisconnect:(NSInteger)code reason:(NSString *)reason {
    if ([self isOnMicro:[GetCore(HJAuthCoreHelp)getUid].userIDValue]) {
        self.position = [self findThePositionByUid:[GetCore(HJAuthCoreHelp) getUid].userIDValue];
    }
//    self.needCloseMicro = GetCore(MeetingCore).isCloseMicro;
    [GetCore(HJMeetingCore) setMeetingRole:false];
    self.isReConnect = true;
}


#pragma mark - ImMessageCoreClient
- (void)onRecvChatRoomCustomMsg:(HJIMMessage *)msg {
    JXIMCustomObject *customObject = msg.messageObject;
    if (customObject.attachment) {
        Attachment *attachment = (Attachment *)customObject.attachment;
        [self handelCustomerChatRoomMessage:attachment];
    }
}

#pragma mark - puble method
//解除麦位封锁
- (void)freeMicPlace:(int)position{
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        [self handleMicPlace:position state:0];
    }
}

//封锁麦位
- (void)lockMicPlace:(int)position{
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        [self handleMicPlace:position state:1];
    }
}

//1：锁坑位，0取消锁（即取消锁坑位）
- (void)handleMicPlace:(int)position state:(int)state{
    UserID uid = GetCore(HJImRoomCoreV2).currentRoomInfo.uid;
    [HJHttpRequestHelper micPlace:position roomOwnerUid:uid state:state success:^{
        
    } failure:^(NSNumber *resCode, NSString *message) {
    }];
}

//麦位静音
- (void)closeMic:(int)position{
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        [self handleMicState:position state:1];
    }
}
//取消麦位静音
- (void)openMic:(int)position{
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        [self handleMicState:position state:0];
    }
}

//1：锁麦，0开麦（即取消锁麦）
- (void)handleMicState:(int)position state:(int)state{
    UserID uid = GetCore(HJImRoomCoreV2).currentRoomInfo.uid;
    [HJHttpRequestHelper micState:position roomOwnerUid:uid state:state success:^{
    } failure:^(NSNumber *resCode, NSString *message) {
    }];
}

//下麦
- (void)downMic{
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
        if ([self isOnMicro:uid]) {
            NSString *position = [self findThePositionByUid:uid];
            
            
            [self removeChatroomQueueWithPosition:position success:^(BOOL success) {
                [GetCore(HJMusicCore) stopMusic];
            } failure:^(NSString *message) {
            }];
        }
    }
}

//上指定麦
- (void)upMic:(int)position invite:(BOOL)isInvite{
    if (GetCore(HJRoomCoreV2Help).isInRoom && !GetCore(HJImRoomCoreV2).isLoading) {
        HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:position + 1];
        HJRoomQueueInfo *queueInfo = item.queueInfo;
        HJRoomMicInfo *micstate = queueInfo.mic_info;
        UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
        UserInfo *userInfo = [GetCore(HJUserCoreHelp) getUserInfoInDB:uid];
        [[GetCore(HJImRoomCoreV2) rac_queryChartRoomMemberByUid:[NSString stringWithFormat:@"%lld",uid]] subscribeNext:^(id x) {
            ChatRoomMember *member = (ChatRoomMember *)x;
            
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
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
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
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
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
    NSArray *micMembers = [GetCore(HJImRoomCoreV2).micMembers copy] ;
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

//通过uid判断麦位
- (NSString *)findThePositionByUid:(UserID)uid{
    for (HJIMQueueItem *item in self.micQueue) {
        if ([item.queueInfo.chatRoomMember.account longLongValue] == uid) {
            return [NSString stringWithFormat:@"%ld",(long)item.position];
        }
    }
    return nil;
}

//通过uid查找member
- (ChatRoomMember *)findTheMemberByUserId:(UserID)uid{
    for (HJIMQueueItem *item in self.micQueue) {
        if ([item.queueInfo.chatRoomMember.account longLongValue] == uid) {
            return item.queueInfo.chatRoomMember;
        }
    }
    return nil;
}

//通过uid查找麦序信息
- (HJRoomQueueInfo *)findTheRoomQueueMemberInfo:(UserID)uid{
    for (HJIMQueueItem *item in self.micQueue) {
        if ([item.queueInfo.chatRoomMember.account longLongValue] == uid) {
            return item.queueInfo;
        }
    }
    return nil;
}

- (NSMutableArray *)findSendGiftMember {
    NSMutableArray *temp = [NSMutableArray array];
    for (int i = 0; i < MIC_COUNT; i++) {
        HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:i];
        HJRoomQueueInfo *sequence = item.queueInfo;
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
        HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:i];
        HJRoomQueueInfo *sequence = item.queueInfo;
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
        HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]+1];
        HJRoomQueueInfo *sequence = item.queueInfo;
        if (sequence && [sequence.chatRoomMember.account longLongValue] == GetCore(HJAuthCoreHelp).getUid.userIDValue) {
            HJRoomMicInfo *state = sequence.mic_info;
            if (self.needCloseMicro) {
                if (state.micState == JXIMRoomMicStateClose) {
                    [GetCore(HJMeetingCore) setMeetingRole:NO];
                }else {
                    [GetCore(HJMeetingCore) setMeetingRole:YES];
//                    [GetCore(MeetingCore) setCloseMicro:YES];
                }
                self.needCloseMicro = false;
            } else {
                if (state.micState == JXIMRoomMicStateClose) {
                    [GetCore(HJMeetingCore) setMeetingRole:NO];
                }else {
                    [GetCore(HJMeetingCore) setMeetingRole:YES];
//                    [GetCore(MeetingCore) setCloseMicro:NO];
                }
            }
        } else {
            if (userId == GetCore(HJAuthCoreHelp).getUid.userIDValue) {
                [GetCore(HJMeetingCore) setMeetingRole:NO];
            }
        }
    } else {
        [GetCore(HJMeetingCore)setMeetingRole:NO];
    }
}

- (void)handelCustomerChatRoomMessage:(Attachment *)attachment {
    //request
    //    if (attachment.first == Custom_Noti_Header_Queue) {
    //        HJInviteMicAttachment *inviteMicAttachment = [HJInviteMicAttachment yy_modelWithJSON:attachment.data];
    //        UserID uid = [GetCore(AuthCore) getUid].userIDValue;
    //        if (attachment.second == Custom_Noti_Sub_Queue_Invite) {
    //            if (uid == inviteMicAttachment.inviteUid.intValue) {
    //                if (![self isOnMicro:uid]) {
    //                    if ([GetCore(HJRoomCoreV2Help) getCurrentRoomInfo].type == RoomType_Game) {
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
        HJRoomQueueCustomAttachment *roomQueueAttachment = [HJRoomQueueCustomAttachment yy_modelWithJSON:attachment.data];
        UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
        if (attachment.second == Custom_Noti_Sub_Queue_Invite) {
            if (uid == roomQueueAttachment.uid) {
                if (GetCore(HJImRoomCoreV2).currentRoomInfo.type == RoomType_Game) {
                    if (![self isOnMicro:uid]) {
                        self.needCloseMicro = YES;
                        [self upMic:roomQueueAttachment.micPosition invite:YES];
                    }
                }
                //需求：被抱上麦，关闭麦克风
                [GetCore(HJMeetingCore) setCloseMicro:YES];

                NotifyCoreClient(HJRoomQueueCoreClient, @selector(onMicroBeInvite), onMicroBeInvite);
            }
        } else if (attachment.second == Custom_Noti_Sub_Queue_Kick) {
            if (uid == roomQueueAttachment.uid) {
                [GetCore(HJMusicCore) stopMusic];
                NotifyCoreClient(HJRoomQueueCoreClient, @selector(onMicroBeKicked), onMicroBeKicked);
            }
        }
    }
}

- (void)changeMicState:(NSString *)key roomQueue:(HJRoomMicInfo *)HJRoomMicInfo {
    HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:[key intValue]+1];
    HJRoomQueueInfo *micSequence = item.queueInfo;
    if (micSequence != nil) {
        ChatRoomMember *chatRoomMember = micSequence.chatRoomMember;
        if (chatRoomMember != nil) {
            UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
            if (uid == [chatRoomMember.account longLongValue]) {
                if (HJRoomMicInfo.micState == JXIMRoomMicPostionStateLock) {
                    [GetCore(HJMeetingCore) setActor:NO];
                    [GetCore(HJMeetingCore) setCloseMicro:YES];
                    [GetCore(HJMusicCore) stopMusic];
                    NotifyCoreClient(HJRoomQueueCoreClient, @selector(onMicroLocked), onMicroLocked);
                } else {
                    [GetCore(HJMeetingCore) setActor:YES]; 
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
    if ([position isEqualToString:@"-1"] && ![GetCore(HJRoomQueueCoreV2Help) checkIsRoomerWith:[NSString stringWithFormat:@"%lld",userInfo.uid]]) {return;}
    if (![position isEqualToString:@"-1"] && [GetCore(HJRoomQueueCoreV2Help) checkIsRoomerWith:[NSString stringWithFormat:@"%lld",userInfo.uid]]) {return;}

    //清空该uid之前在队列中的信息
    if ([self isOnMicro:userInfo.uid]) {
        @weakify(self);
        [HJIMRequestManager addOrUpdateMemberWithKey:position Success:^{
            NotifyCoreClient(HJImRoomCoreClientV2, @selector(onGetRoomQueueSuccessV2:), onGetRoomQueueSuccessV2:self.micQueue);
            [YYLogger info:@"上麦成功" message:@"key:%@--value:%@--roomId%zd",position,[userInfo yy_modelToJSONString],GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
        } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
            failure(errorMessage);
            [YYLogger info:@"上麦失败" message:@"error:%@--class:%@--function:updateChatroomQueueWithPosition:userInfo:success:failure:",errorMessage,NSStringFromClass([self class])];
        }];
    } else {
//        request.key = position;
//        request.value = [userInfo yy_modelToJSONString];
//        request.roomId = [NSString stringWithFormat:@"%d",(int)GetCore(ImRoomCoreV2).currentRoomInfo.roomId];
        @weakify(self);
        [HJIMRequestManager addOrUpdateMemberWithKey:position Success:^{
            NotifyCoreClient(HJImRoomCoreClientV2, @selector(onGetRoomQueueSuccessV2:), onGetRoomQueueSuccessV2:self.micQueue);
            [YYLogger info:@"上麦成功" message:@"key:%@--value:%@--roomId%zd",position,[userInfo yy_modelToJSONString],GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
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
    [HJIMRequestManager removeQueueWithKey:position Success:^{
        @strongify(self);
        success(YES);
        [YYLogger info:@"下麦成功" message:@"key:%@--roomId%zd",position,GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        failure(errorMessage);
        [YYLogger info:@"下麦失败" message:@"error:%@--class:%@--function:removeChatroomQueueWithPosition:success:failure:",errorMessage,NSStringFromClass([self class])];
    }];
}

//邀请上麦
- (void)inviteOnMic:(UserID)uid postion:(NSString *)position{
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        HJRoomMicInfo *state = [[HJRoomMicInfo alloc]init];
        state.posState = JXIMRoomMicPostionStateUnlock;
        HJRoomQueueCustomAttachment *roomQueueAttachment = [[HJRoomQueueCustomAttachment alloc]init];
        roomQueueAttachment.uid = uid;
        roomQueueAttachment.HJRoomMicInfo = state;
        roomQueueAttachment.micPosition = position.intValue;
        
        Attachment *attachement = [[Attachment alloc]init];
        attachement.first = Custom_Noti_Header_Queue;
        attachement.second = Custom_Noti_Sub_Queue_Invite;
        attachement.data = roomQueueAttachment.encodeAttachment;
        
        [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
    }
}

//踢人下麦
- (void)kiedMircoWithUid:(UserID)uid {
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        HJRoomMicInfo *state = [[HJRoomMicInfo alloc]init];
        state.posState = JXIMRoomMicPostionStateUnlock;
        
        HJRoomQueueCustomAttachment *roomQueueAttachment = [[HJRoomQueueCustomAttachment alloc]init];
        roomQueueAttachment.uid = uid;
        roomQueueAttachment.HJRoomMicInfo = state;
        
        Attachment *attachement = [[Attachment alloc]init];
        attachement.first = Custom_Noti_Header_Queue;
        attachement.second = Custom_Noti_Sub_Queue_Kick;
        attachement.data = roomQueueAttachment.encodeAttachment;
        
        [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
    }
}

#pragma mark - Getter & Setter
- (NSMutableArray<HJIMQueueItem *> *)micQueue{
    return GetCore(HJImRoomCoreV2).micQueue;
}

- (ChatRoomMember *)myMember{
    return GetCore(HJImRoomCoreV2).myMember;
}

- (ChatRoomMember *)roomOwner{
    return GetCore(HJImRoomCoreV2).roomOwner;
}

@end
