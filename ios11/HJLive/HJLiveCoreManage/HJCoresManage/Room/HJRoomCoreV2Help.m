//
//  HJRoomCoreV2Help.m
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomCoreV2Help.h"
#import "HJHttpRequestHelper+Room.h"

#import "HJRoomQueueCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "HJRoomQueueCoreClient.h"
#import "HJAuthCoreHelp.h"
#import "HJAuthCoreClient.h"
#import "HJMeetingCore.h"
#import "HJMeetingCoreClient.h"
#import "HJImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "HJFaceCore.h"
#import "HJFaceCoreClient.h"
#import "HJImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"
#import "HJGiftCore.h"
#import "HJUserCoreHelp.h"

#import "GiftReceiveInfo.h"
#import "HJShareSendInfo.h"
#import "Attachment.h"
#import "HJRoomMemberCharmInfoModel.h"

#import "NSObject+YYModel.h"
#import "YYUtility.h"
#import "HJImMessageSendCoreClient.h"

#import "HJHttpRequestHelper+Egg.h"


@interface HJRoomCoreV2Help()<HJImRoomCoreClient, HJImRoomCoreClientV2, HJImMessageCoreClient,HJAuthCoreClient, HJMeetingCoreClient, HJRoomQueueCoreClient,HJFaceCoreClient>
{
    /*
     用来记录魅力值数据时间戳
     */
    NSUInteger latestTimestamps;
}

@property (nonatomic, strong) dispatch_source_t timer; //计时器

@property (nonatomic, strong) NSMutableDictionary *charmInfoMap;//房间魅力值Map


@end

@implementation HJRoomCoreV2Help
{
    /*
     用来通知公屏更新
     */
    NSTimer *_notifyChannelTextTimer;
}

- (void)userGiftPurseDraw:(NSString *)type {
    [HJHttpRequestHelper userGiftPurseDraw:type success:^(id data) {
        NotifyCoreClient(HJRoomCoreClient, @selector(userGiftPurseDrawSuccess:), userGiftPurseDrawSuccess:data);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(userGiftPurseDrawFail:code:), userGiftPurseDrawFail:message code:resCode);
    }];
}

- (instancetype)init{
    self = [super init];
    if (self) {
        AddCoreClient(HJImRoomCoreClient, self);
        AddCoreClient(HJImMessageCoreClient, self);
        AddCoreClient(HJAuthCoreClient, self);
        AddCoreClient(HJMeetingCoreClient, self);
        AddCoreClient(HJImRoomCoreClientV2, self);
        AddCoreClient(HJRoomQueueCoreClient, self);
        AddCoreClient(HJFaceCoreClient, self);
        AddCoreClient(HJImMessageSendCoreClient, self);
    }
    return self;
}
- (void)dealloc{
    RemoveCoreClientAll(self);
}

#pragma mark - ImMessageCoreClient
//收到text消息
- (void)onRecvChatRoomTextMsg:(HJIMMessage *)msg {
    if (msg.text && msg.text.length > 0) {
        [self addMessageToArray:msg];
    }
}

//收到自定义消息
- (void)onRecvChatRoomCustomMsg:(HJIMMessage *)msg {
    [self handlerCustomMsg:msg];
}

- (void)onWillSendMessage:(NIMMessage *)msg {
    if (msg.messageType == NIMMessageTypeCustom) {
        NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
        Attachment *attachment = (Attachment *)obj.attachment;
        if (attachment.first != Custom_Noti_Header_Queue &&
            attachment.first != Custom_Noti_Header_Face &&
            attachment.first != Custom_Noti_Header_PK) {
            
            if (attachment.first == Custom_Noti_Header_ALLMicroSend || attachment.first == Custom_Noti_Header_Gift) {
//                if (GetCore(HJGiftCore).isOpenAnimation) {
                    [self addMessageToArray:msg];
//                }
            }
            else {
                [self addMessageToArray:msg];
            }
            
        }
        else if (attachment.first == Custom_Noti_Header_Queue) {
            if (attachment.second == Custom_Noti_Sub_Gift_Effect_Open || attachment.second == Custom_Noti_Sub_Gift_Effect_Close ||
                attachment.second == Custom_Noti_Sub_Car_Effect_Close ||
                attachment.second == Custom_Noti_Sub_Car_Effect_Open ||
                attachment.second == Custom_Noti_Sub_Message_Open || attachment.second == Custom_Noti_Sub_Message_Close) {
                [self addMessageToArray:msg];
            }
        }
        else if (attachment.first == Custom_Noti_Header_PK) {
            
            if (attachment.second == Custom_Noti_Header_Room_PK_Start) {
                [self addMessageToArray:msg];
            }
        }
    }else {
        [self addMessageToArray:msg];
    }
}

//进入聊天室成功
- (void)onMeInterChatRoomSuccess {
    NSLog(@"%@",GetCore(HJImRoomCoreV2).currentRoomInfo);
    if (GetCore(HJImRoomCoreV2).currentRoomInfo == nil) {
        return;
    }

    [self reportUserInterRoom];//上报进入房间
    [self startRoomTimer];
    if (GetCore(HJImRoomCoreV2).currentRoomInfo.uid == GetCore(HJAuthCoreHelp).getUid.userIDValue) {
        [GetCore(HJMeetingCore) joinMeeting:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] actor:YES];
    } else {
        [GetCore(HJMeetingCore) joinMeeting:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] actor:NO];
    }
}

//退出房
- (void)onMeExitChatRoomSuccessV2 {
    [self cancelRoomRecord];
    if (GetCore(HJImRoomCoreV2).currentRoomInfo.uid == [GetCore(HJAuthCoreHelp) getUid].userIDValue && GetCore(HJImRoomCoreV2).currentRoomInfo.type != RoomType_Game) {
        [self closeRoom:GetCore(HJImRoomCoreV2).currentRoomInfo.uid];
    }
    [self reportUserOuterRoom];
    [GetCore(HJMeetingCore) leaveMeeting:[NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId]];
    self.speakingList = nil;
    self.messages = nil;
}

- (BOOL)isBeKickedWithRoomid:(NSString *)roomid {
    NSUserDefaults *ur = [NSUserDefaults standardUserDefaults];
    if ([ur objectForKey:[NSString stringWithFormat:@"beKicked%@",roomid]]) {
        NSDictionary *dic = [ur objectForKey:[NSString stringWithFormat:@"beKicked%@",roomid]];
        if ([dic[@"roomId"] isEqualToString:roomid]) {
            NSString *saveTime = dic[@"time"];
            NSDate* dat = [NSDate dateWithTimeIntervalSinceNow:0];
            NSTimeInterval a =[dat timeIntervalSince1970]*1000;
            NSString *currentTime = [NSString stringWithFormat:@"%f",a];
            NSString *giveTime = @"0";
            if ([ur objectForKey:@"kickWaiting"]) {
                giveTime = [ur objectForKey:@"kickWaiting"];
            }
            if ([currentTime longLongValue] - [saveTime longLongValue] > [giveTime longLongValue] * 1000) {
                return YES;
            } else {
                return NO;
            }
        } else {
            return YES;
        }
    }
        return YES;
}

//用户被踢
- (void)onUserBeKicked:(NSString *)roomid
{
    [GetCore(HJMeetingCore) leaveMeeting:[NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId]];
    self.speakingList = nil;
    self.messages = nil;
}

//用户进入聊天室
- (void)onUserInterChatRoom:(ChatRoomMember *)member{
    HJIMMessage *message = [HJIMMessage new];
    message.messageType = HJIMMessageTypeNotification;
    
    JXIMSession *session = [JXIMSession new];
    session.sessionId = [NSString stringWithFormat:@"%ld", (long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
    session.sessionType = JXIMSessionTypeChatroom;
    message.session = session;
    
    message.from = member.account;
    message.member = member;
    
    JXIMChatroomNotificationContent *content = [JXIMChatroomNotificationContent new];
    content.eventType = JXIMChatroomEventTypeEnter;
    content.notificationType = JXIMNotificationTypeChatroom;
    
    JXIMCustomObject *messageObject = [JXIMCustomObject new];
    messageObject.notificationContent = content;
    
    message.messageObject = messageObject;
    
    [self addMessageToArray:message];
    NotifyCoreClient(HJRoomCoreClient, @selector(onUserEnterChatRoomWith:), onUserEnterChatRoomWith:message);
    /*
     迟点
     [self addMessageToArray:message];
     NotifyCoreClient(RoomCoreClient, @selector(onUserEnterChatRoomWith:), onUserEnterChatRoomWith:message);
     */
}

//收到移除管理更新通知
- (void)managerRemove:(NSString *)uid {
    [[GetCore(HJImRoomCoreV2) rac_queryChartRoomMemberByUid:uid] subscribeNext:^(id x) {
        ChatRoomMember *chatRoomMember = (ChatRoomMember *)x;
        NSString *position = [self findThePositionByUid:chatRoomMember.account.userIDValue];
        
        if (position) {
            HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]+1];
            HJRoomQueueInfo *sequence = item.queueInfo;
            sequence.chatRoomMember = chatRoomMember;
        }
        NotifyCoreClient(HJRoomCoreClient, @selector(onManagerRemove:), onManagerRemove:chatRoomMember);
    }];
}

//收到添加管理更新通知
- (void)managerAdd:(NSString *)uid {
    [[GetCore(HJImRoomCoreV2) rac_queryChartRoomMemberByUid:uid] subscribeNext:^(id x) {
        ChatRoomMember *chatRoomMember = (ChatRoomMember *)x;
        NSString *position = [self findThePositionByUid:chatRoomMember.account.userIDValue];
        
        if (position) {
            HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]+1];
            HJRoomQueueInfo *sequence = item.queueInfo;
            sequence.chatRoomMember = chatRoomMember;
        }
        NotifyCoreClient(HJRoomCoreClient, @selector(onManagerAdd:), onManagerAdd:chatRoomMember);
    }];
}

//black
- (void)onUserBeAddBlack:(NSString *)uid {
    [[GetCore(HJImRoomCoreV2) rac_queryChartRoomMemberByUid:uid] subscribeNext:^(id x) {
        ChatRoomMember *chatRoomMember = (ChatRoomMember *)x;
        if ([GetCore(HJRoomQueueCoreV2Help) isOnMicro:[chatRoomMember.account longLongValue]]) {
            NSString *position = [GetCore(HJRoomQueueCoreV2Help) findThePositionByUid:[chatRoomMember.account longLongValue]];
            
            //修改队列
            [GetCore(HJRoomQueueCoreV2Help) removeChatroomQueueWithPosition:position success:^(BOOL success) {
                
            } failure:^(NSString *message) {
                
            }];
            HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]];
            HJRoomQueueInfo *sequence = item.queueInfo;
            sequence.chatRoomMember = nil;
        }
        NotifyCoreClient(HJRoomCoreClient, @selector(userBeAddBlack:), userBeAddBlack:chatRoomMember);
    }];
}

- (void)onUserBeRemoveBlack:(NSString *)uid {
    [[GetCore(HJImRoomCoreV2) rac_queryChartRoomMemberByUid:uid] subscribeNext:^(id x) {
        ChatRoomMember *chatRoomMember = (ChatRoomMember *)x;
        if ([GetCore(HJRoomQueueCoreV2Help) isOnMicro:[chatRoomMember.account longLongValue]]) {
            NSString *position = [GetCore(HJRoomQueueCoreV2Help) findThePositionByUid:[chatRoomMember.account longLongValue]];
            
            HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]];
            HJRoomQueueInfo *sequence = item.queueInfo;
            sequence.chatRoomMember = chatRoomMember;
        }
        NotifyCoreClient(HJRoomCoreClient, @selector(userBeRemoveBlack:), userBeRemoveBlack:chatRoomMember);
    }];
}

#pragma mark - MeetingCoreClient
//加入声网成功
- (void)onJoinMeetingSuccess {
    UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
    if (GetCore(HJImRoomCoreV2).currentRoomInfo.uid == uid) {
        if (GetCore(HJImRoomCoreV2).currentRoomInfo.type != RoomType_Game) {
            
                [GetCore(HJMeetingCore) setMeetingRole:YES];
                [GetCore(HJMeetingCore) setCloseMicro:NO];
        }
    }
    [YYUtility checkMicPrivacy:^(BOOL succeed) {
        if (!succeed) {
            NotifyCoreClient(HJRoomCoreClient, @selector(thereIsNoMicoPrivacy), thereIsNoMicoPrivacy);
        }
    }];
}
//说话回调
- (void)onSpeakingUsersReport:(NSMutableArray *)userInfos
{
    self.speakingList = userInfos;
    NotifyCoreClient(HJRoomCoreClient, @selector(onSpeakUsersReport:), onSpeakUsersReport:self.speakingList);
}

- (void)onMySpeakingStateUpdate:(BOOL)speaking
{
    NotifyCoreClient(HJRoomCoreClient, @selector(onMySpeakStateUpdate:), onMySpeakStateUpdate:speaking);
}

#pragma mark - FaceCoreClient
- (void)onFaceIsResult:(HJFacePlayInfo *)info {
    if ([NSThread isMainThread]){
        [self addMessageToArray:info.message];
    }
}

//开启房间
- (void)openRoom:(UserID)uid type:(RoomType)type title:(NSString *)title roomDesc:(NSString *)roomDesc backPic:(NSString *)backPic rewardId:(NSString *)rewardId
{
    if (uid <= 0) {
        return;
    }
    [HJHttpRequestHelper openRoom:uid type:type title:title roomDesc:roomDesc backPic:backPic rewardId:rewardId success:^(ChatRoomInfo *roomInfo) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onOpenRoomSuccess:), onOpenRoomSuccess:roomInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onOpenRoomFailth:message:), onOpenRoomFailth:resCode message:message);
    }];
}

//房主更新房间信息
- (void)updateRoomInfo:(UserID)uid title:(NSString *)title roomDesc:(NSString *)roomDesc roomNotice:(NSString *)roomNotice backPic:(NSString *)backPic playInfo:(NSString *)playInfo giftEffectSwitch:(BOOL)giftEffectSwitch  giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch
{
    if (uid <= 0) {
        return;
    }
    @weakify(self)
    [HJHttpRequestHelper updateRoomInfo:uid title:title roomDesc:roomDesc roomNotice:roomNotice backPic:backPic roomPassword:nil tag:0 playInfo:playInfo giftEffectSwitch:giftEffectSwitch giftCardSwitch:giftCardSwitch publicChatSwitch:publicChatSwitch success:^(ChatRoomInfo *roomInfo) {
        @strongify(self)
        [GetCore(HJImRoomCoreV2) setCurrentRoomInfo:roomInfo];
        NotifyCoreClient(HJRoomCoreClient, @selector(onUpdateRoomInfoSuccess:), onUpdateRoomInfoSuccess:roomInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onUpdateRoomInfoFailth:), onUpdateRoomInfoFailth:message);
    }];
}

//房主修改游戏房间信息
- (void)updateGameRoomInfo:(UserID)uid backPic:(NSString *)backPick title:(NSString *)title roomTopic:(NSString *)roomTopic roomNotice:(NSString *)roomNotice roomPassword:(NSString *)roomPassword tag:(int)tag playInfo:(NSString *)playInfo giftEffectSwitch:(BOOL)giftEffectSwitch giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch  {
    [HJHttpRequestHelper updateRoomInfo:uid title:title roomDesc:roomTopic roomNotice:roomNotice backPic:backPick roomPassword:roomPassword tag:tag playInfo:playInfo giftEffectSwitch:giftEffectSwitch giftCardSwitch:giftCardSwitch publicChatSwitch:publicChatSwitch success:^(ChatRoomInfo *roomInfo) {
        [GetCore(HJImRoomCoreV2) setCurrentRoomInfo:roomInfo];
        if (roomTopic.length > 0) {
            NotifyCoreClient(HJRoomCoreClient, @selector(onGameRoomInfoUpdateSuccessV2:), onGameRoomInfoUpdateSuccessV2:roomInfo);
        } else {
            NotifyCoreClient(HJRoomCoreClient, @selector(onGameRoomInfoUpdateSuccess:isFromMessage:), onGameRoomInfoUpdateSuccess:roomInfo isFromMessage:NO);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onGameRoomInfoUpdateFailth:), onGameRoomInfoUpdateFailth:message);
    }];
}

//管理员修改房间信息
- (void)managerUpdateGameRoomInfo:(UserID)uid backPic:(NSString *)backPick title:(NSString *)title roomTopic:(NSString *)roomTopic roomNotice:(NSString *)roomNotice roomPassword:(NSString *)roomPassword tag:(int)tag playInfo:(NSString *)playInfo giftEffectSwitch:(BOOL)giftEffectSwitch giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch {
    
    [HJHttpRequestHelper managerUpdateRoomInfo:uid title:title roomDesc:roomTopic roomNotice:roomNotice backPic:backPick roomPassword:roomPassword playInfo:playInfo tag:tag giftEffectSwitch:giftEffectSwitch giftCardSwitch:giftCardSwitch publicChatSwitch:publicChatSwitch success:^(ChatRoomInfo *roomInfo) {
        [GetCore(HJImRoomCoreV2) setCurrentRoomInfo:roomInfo];
        if (roomTopic.length > 0) {
            NotifyCoreClient(HJRoomCoreClient, @selector(onGameRoomInfoUpdateSuccessV2:), onGameRoomInfoUpdateSuccessV2:roomInfo);
        } else {
            NotifyCoreClient(HJRoomCoreClient, @selector(onGameRoomInfoUpdateSuccess:isFromMessage:), onGameRoomInfoUpdateSuccess:roomInfo isFromMessage:NO);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onGameRoomInfoUpdateFailth:), onGameRoomInfoUpdateFailth:message);
    }];
}

//关闭房间
- (void)closeRoom:(UserID)uid
{
    if (uid <= 0) {
        return;
    }
    [HJHttpRequestHelper closeRoom:uid success:^{
        NotifyCoreClient(HJRoomCoreClient, @selector(onCloseRoomSuccess), onCloseRoomSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onCloseRoomFailth:code:), onCloseRoomFailth:message code:resCode);
    }];
}


#pragma mark -- mic
//获取麦上人员
- (NSMutableArray *)getAdminMembers {
    return GetCore(HJImRoomCoreV2).micMembers;
}



#pragma mark --房间信息相关
//根据房主id请求房间信息
- (RACSignal *)requestRoomInfo:(UserID)uid
{
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        [HJHttpRequestHelper getRoomInfo:uid success:^(ChatRoomInfo *roomInfo) {
            [subscriber sendNext:roomInfo];
            [subscriber sendCompleted];
        } failure:^(NSNumber *resCode, NSString *message) {
            [subscriber sendError:CORE_API_ERROR(RoomCoreErrorDomain, resCode.integerValue, message)];
        }];
        return nil;
    }];
}

- (void)getRoomInfo:(UserID)uid success:(void (^)(ChatRoomInfo *roomInfo))success failure:(void (^)(NSInteger resCode, NSString *message))failure {
    [HJHttpRequestHelper getRoomInfo:uid success:^(ChatRoomInfo *roomInfo) {
        !success ?: success(roomInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode.integerValue, message);
    }];
}

- (ChatRoomInfo *)getCurrentRoomInfo {
    return GetCore(HJImRoomCoreV2).currentRoomInfo;
}

//获取房间贡献榜
- (void)getRoomBounsList {
//    [HttpRequestHelper requestRoomBounsListSuccess:^(NSMutableArray *bounsInfoList) {
//        if (bounsInfoList.count > 0) {
//            NotifyCoreClient(HJRoomCoreClient, @selector(onGetRoomBounsListSuucess:), onGetRoomBounsListSuucess:bounsInfoList);
//        }
//    } failure:^(NSNumber *resCode, NSString *message) {
//        NotifyCoreClient(HJRoomCoreClient, @selector(onGetRoomBounsListFailth:), onGetRoomBounsListFailth:message);
//    }];
}

- (void)getNewRoomBounsList:(NSString *)type dataType:(NSString *)dataTpye{
    [HJHttpRequestHelper requestNewRoomBounsListWithType:type withDataType:dataTpye Success:^(NSMutableArray *bounsInfoList) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onGetRoomBounsListSuucess:type:dateType:), onGetRoomBounsListSuucess:bounsInfoList type:type dateType:dataTpye);
    } failure:^(NSNumber *code, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onGetRoomBounsListFailth:type:dateType:), onGetRoomBounsListFailth:message type:type dateType:dataTpye);
    }];
}

//获取用户进入了的房间的信息
- (void)getUserInterRoomInfo:(UserID)uid {
    [HJHttpRequestHelper requestUserInRoomInfoBy:uid Success:^(ChatRoomInfo *roomInfo) {
        NotifyCoreClient(HJRoomCoreClient, @selector(requestUserRoomInterInfo: uid:), requestUserRoomInterInfo:roomInfo uid:uid);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(requestUserRoomInterInfoFailth:), requestUserRoomInterInfoFailth:message);
    }];
}

#pragma mark - blacklist
//判断自己是否在黑名单
- (void)judgeIsInBlackList:(NSString *)roomID {
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    NIMChatroomMembersByIdsRequest *request = [[NIMChatroomMembersByIdsRequest alloc]init];
    request.roomId = roomID;
    request.userIds = @[uid];
    @weakify(self);
    [[NIMSDK sharedSDK].chatroomManager fetchChatroomMembersByIds:request completion:^(NSError * _Nullable error, NSArray<ChatRoomMember *> * _Nullable members) {
        @strongify(self);
        for (ChatRoomMember *member in members) {
//            if (member.isInBlackList) {
//                NotifyCoreClient(AuthCoreClient, @selector(mySelfIsInBalckList:), mySelfIsInBalckList:YES);
//            }else {
//                NotifyCoreClient(AuthCoreClient, @selector(mySelfIsInBalckList:), mySelfIsInBalckList:NO);
//            }
        }
    }];
}

- (void)sendLotteryMessageWithGiftInfo:(GiftInfo *)giftInfo nick:(NSString *)nick{
    if (!giftInfo) return;
    
    Attachment *attachement = [[Attachment alloc]init];
    attachement.first = Custom_Noti_Header_Winning;
    attachement.second = Custom_Noti_Header_Winning;

    NSDictionary *giftDict = [giftInfo yy_modelToJSONObject];
    NSMutableDictionary *buffer = [NSMutableDictionary dictionaryWithDictionary:giftDict];
    [buffer setObject:JX_STR_AVOID_nil(nick) forKey:@"nick"];
    [buffer setObject:@(giftInfo.giftNum) forKey:@"count"];
    NSDictionary *attMessageDic = @{@"params": buffer};
    attachement.data = attMessageDic;
    
//    [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:(JXIMSessionType) NIMSessionTypeChatroom];
}


//保存HJGameRoomPositionView计算的游戏房cell的位置
- (void)savePosition:(NSMutableArray *)list {
    self.positionArr = list;
}

//保存房间魅力值Map
- (void)saveCharmInfoMap:(NSMutableDictionary<NSString *,id> *)charmInfoMap {
    self.charmInfoMap = charmInfoMap;
}

//获取房间魅力值Map
- (NSMutableDictionary *)getRoomCharmInfoMap {
    return self.charmInfoMap;
}

#pragma mark - private method
//处理聊天室下发通知
- (void)handlerCustomMsg:(HJIMMessage *)msg
{
    // 小熊语音语音需要进行单独处理消息 消息类型：礼物 房间进入

    JXIMCustomObject *obj = msg.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
        Attachment *attachment = (Attachment *)obj.attachment;
        if (attachment.first == Custom_Noti_Header_Winning) {
            [self addMessageToArray:msg];
        }else if (attachment.first == Custom_Noti_Header_Mora) {
            [self addMessageToArray:msg];
        }
        else if (attachment.first == Custom_Noti_Header_LongZhu) {
            [self addMessageToArray:msg];
        } else if (attachment.first == Custom_Noti_Header_PK) { 
            if (attachment.second == Custom_Noti_Header_Room_PK_Start || attachment.second == Custom_Noti_Header_Room_PK_End || attachment.second == Custom_Noti_Header_Room_PK_Cancel) {
                [self addMessageToArray:msg];
            }
        }
        else if (attachment.first == Custom_Noti_Header_SecretGift) {
            if (attachment.second == Custom_Noti_Header_SecretGift) {
                [self addMessageToArray:msg];
            }
        }
        else if (attachment.first == Custom_Noti_Header_Charm){
            
            [self updateMicCharm:attachment];
        }
        else if (attachment.first == Custom_Noti_Header_Gift) {
            
//            if (GetCore(HJGiftCore).isOpenAnimation) {
            
                GiftReceiveInfo *info = [GiftReceiveInfo yy_modelWithDictionary:attachment.data];
                GiftInfo *giftInfo = [GetCore(HJGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeNormal];
                if (giftInfo.goldPrice > 0) {
                    [self addMessageToArray:msg];
                }
//            }
        }
        else if (attachment.first == Custom_Noti_Header_ALLMicroSend) {
            if (attachment.second == Custom_Noti_Sub_AllMicroSend) {
                
//                if (GetCore(HJGiftCore).isOpenAnimation) {
                
                    [self addMessageToArray:msg];
//                }
            }
        }
        else if (attachment.first == Custom_Noti_Header_Queue) {
            if (attachment.second == Custom_Noti_Sub_Gift_Effect_Open || attachment.second == Custom_Noti_Sub_Gift_Effect_Close ||
                attachment.second == Custom_Noti_Sub_Car_Effect_Close ||
                attachment.second == Custom_Noti_Sub_Car_Effect_Open ||
                attachment.second == Custom_Noti_Sub_Message_Open || attachment.second == Custom_Noti_Sub_Message_Close|| attachment.second == Custom_Noti_Header_CHANGE_ROOM_LOCK|| attachment.second == Custom_Noti_Header_CHANGE_ROOM_NO_LOCK) {
                
                [self updateEffect:attachment.second];

                [self addMessageToArray:msg];
            }
            if (attachment.second == Custom_Noti_Header_ClearCharmValue || attachment.second == Custom_Noti_Header_Set_Second_Manager_Open|| attachment.second == Custom_Noti_Header_Set_Second_Manager_Close) {
                 [self addMessageToArray:msg];
            }

        }else if (attachment.first == Custom_Noti_Header_Wanfa){
             [self addMessageToArray:msg];
        }else if (attachment.first == Custom_Noti_Header_Playcall){
            if (attachment.second == Custom_Noti_Header_Playcall){
                 [self addMessageToArray:msg];
            }
            
        }else if (attachment.first == Custom_Noti_Header_ChargeRoomName){
            if (attachment.second == Custom_Noti_Header_ChargeRoomName){
                 [self addMessageToArray:msg];
            }
            
        }
        
    }
}

- (void)updateEffect:(NSInteger)second
{
    if (second == Custom_Noti_Sub_Car_Effect_Open) {
        GetCore(HJImRoomCoreV2).enterRoomInfo.giftCardSwitch = NO;
    }else if (second == Custom_Noti_Sub_Car_Effect_Close){
        GetCore(HJImRoomCoreV2).enterRoomInfo.giftCardSwitch = YES;
    }
    
    //    if (second == Custom_Noti_Sub_Gift_Effect_Open) {
    //        GetCore(HJImRoomCoreV2).enterRoomInfo.giftEffectSwitch = YES;
    //    }else if (second == Custom_Noti_Sub_Gift_Effect_Close){
    //        GetCore(HJImRoomCoreV2).enterRoomInfo.giftEffectSwitch = NO;
    //    }
    
    
    
}


//更新麦位魅力值
- (void)updateMicCharm:(Attachment *)attachment {
    XCRoomCharmDataModel *charmDataModel = [XCRoomCharmDataModel yy_modelWithJSON:attachment.data];
    if ([charmDataModel.roomId integerValue] != self.getCurrentRoomInfo.roomId) {
        return;
    }
    
    if (charmDataModel.timestamps < latestTimestamps) {
        return;
    }
    
    latestTimestamps = charmDataModel.timestamps;
    NSDictionary *latestCharm = charmDataModel.latestCharm;
    
    NSMutableArray <HJRoomMemberCharmInfoModel *> *memberCharmInfoList = [NSMutableArray array];
    NSMutableArray<HJIMQueueItem *> *mircoList = GetCore(HJImRoomCoreV2).micQueue;
    [mircoList enumerateObjectsUsingBlock:^(HJIMQueueItem * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([latestCharm containsObjectForKey:obj.queueInfo.chatRoomMember.account]) {
            NSDictionary *roomMemberInfo = [latestCharm valueForKey:JX_STR_AVOID_nil(obj.queueInfo.chatRoomMember.account)];
            HJRoomMemberCharmInfoModel *memberCharmInfoModel = [HJRoomMemberCharmInfoModel yy_modelWithJSON:roomMemberInfo];
            memberCharmInfoModel.uid = JX_STR_AVOID_nil(obj.queueInfo.chatRoomMember.account);
            //            obj.queueInfo.chatRoomMember.charmValue = memberCharmInfoModel.value;
            //            obj.queueInfo.chatRoomMember.hasHat = memberCharmInfoModel.withHat;
            [memberCharmInfoList addObject:memberCharmInfoModel];
        }
    }];
    NotifyCoreClient(HJRoomCoreClient, @selector(updateMicCharm:), updateMicCharm:memberCharmInfoList);
}

//添加消息到内存，最多保留200
- (void)addMessageToArray:(HJIMMessage *)msg
{
    if (msg.session.sessionType == JXIMSessionTypeChatroom && (msg.session.sessionId.integerValue == GetCore(HJImRoomCoreV2).currentRoomInfo.roomId || msg.session.sessionId == nil)) { // 全服消息
        
        
        BOOL isRepeat = NO;
        // 消息去重
        if (self.messages.count) {
            for (NIMMessage *buffer in self.messages) {
                if ([buffer.messageId isEqualToString:msg.messageId]) {
                    isRepeat = YES;
                    break;
                }
            }
        }
        if (isRepeat) return;
        
        
        BOOL force = NO;
        if (self.messages == nil) {
            self.messages = [NSMutableArray array];
            force = YES;
        }
//        if (self.messages.count > 200) {
//            [self.messages removeObjectAtIndex:0];
//        }
        [self.messages addObject:msg];
        
        [self _notifyReceiveChannelText:force];
    }
}

- (void) _notifyReceiveChannelText:(BOOL)forceOrNot
{
    const static float UPDATE_UI_CHANNEL_TEXT_INTERVAL = 0.5; //每0.5s刷新公屏
    if (_notifyChannelTextTimer == nil) {
        _notifyChannelTextTimer = [NSTimer scheduledTimerWithTimeInterval:UPDATE_UI_CHANNEL_TEXT_INTERVAL
                                                                   target:self
                                                                 selector:@selector(onDoNotifyReceiveChannelText:)
                                                                 userInfo:nil
                                                                  repeats:NO];
    }
    if (forceOrNot) {
        [_notifyChannelTextTimer fire];
    }
}

- (void) onDoNotifyReceiveChannelText:(NSTimer *)timer
{
    [_notifyChannelTextTimer invalidate];
    _notifyChannelTextTimer = nil;
    
    NotifyCoreClient(HJRoomCoreClient, @selector(onCurrentRoomMsgUpdate:), onCurrentRoomMsgUpdate:self.messages);
}



- (void)reportUserInterRoom {
    if ([GetCore(HJAuthCoreHelp)getTicket].length > 0) {
        [HJHttpRequestHelper reportUserInterRoomSuccess:^(BOOL success) {
        } failure:^(NSNumber *resCode, NSString *message) {
        }];
    }
}

- (void)reportUserOuterRoom {
    if ([GetCore(HJAuthCoreHelp) getTicket].length > 0) {
        [HJHttpRequestHelper reportUserOutRoomSuccess:^(BOOL success) {
        } failure:^(NSNumber *resCode, NSString *message) {
        }];
    }
}

//房间埋点统计
- (void)recordTheRoomTime:(UserID)uid roomUid:(UserID)roomUid {
    [HJHttpRequestHelper recordTheRoomTime:uid roomUid:roomUid];
}
//开始房间统计（60s之后才统计）
- (void)startRoomTimer {
    
//    if (self.timer) {
//        dispatch_resume(self.timer);
//        return;
//    }
    // 获得队列
    dispatch_queue_t queue = dispatch_get_main_queue();
    // 创建一个定时器(dispatch_source_t本质还是个OC对象)
    self.timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    // 设置定时器的各种属性（几时开始任务，每隔多长时间执行一次）
    // GCD的时间参数，一般是纳秒（1秒 == 10的9次方纳秒）
    // 何时开始执行第一个任务
    // dispatch_time(DISPATCH_TIME_NOW, 1.0 * NSEC_PER_SEC) 比当前时间晚3秒
    dispatch_time_t start = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.0 * NSEC_PER_SEC));
    uint64_t interval = (uint64_t)(60.0 * NSEC_PER_SEC);
    dispatch_source_set_timer(self.timer, start, interval, 0);
    
    // 设置回调
    @weakify(self);
    dispatch_source_set_event_handler(self.timer, ^{
        @strongify(self);
        if ([GetCore(HJAuthCoreHelp) getUid].userIDValue != GetCore(HJImRoomCoreV2).currentRoomInfo.uid) {
            [self recordTheRoomTime:[GetCore(HJAuthCoreHelp) getUid].userIDValue roomUid:GetCore(HJImRoomCoreV2).currentRoomInfo.uid];
        }
    });
    // 启动定时器
    dispatch_resume(self.timer);
    
}
- (void)cancelRoomRecord {
    
//    if (self.timer) {
//        dispatch_suspend(self.timer);
//    }
    
    self.timer = nil;
}

- (void)onRoomInvalid{
    self.speakingList = nil;
    self.messages = nil;
}


//通过uid判断麦位
- (NSString *)findThePositionByUid:(UserID)uid{
    if (uid > 0) {
        NSArray *micQueue = GetCore(HJImRoomCoreV2).micQueue;
        if (micQueue.count > 0) {
            for (HJIMQueueItem *item in micQueue) {
                ChatRoomMember *member = item.queueInfo.chatRoomMember;
                if ([member.account longLongValue] == uid) {
                    return [@(item.position) description];
                }
            }
        }
    }
    return nil;
}

/**
 砸蛋排行榜
 @param type  1代表今天 2代表昨天
 */
- (void)userGiftPurseGetRankWithType:(NSInteger)type {
    [HJHttpRequestHelper userGiftPurseGetRankWithType:type success:^(NSMutableArray *list) {
        NotifyCoreClient(HJRoomCoreClient, @selector(userGiftPurseGetRankSuccessWithType:list:), userGiftPurseGetRankSuccessWithType:type list:list)
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(userGiftPurseGetRankFailWithType:message:), userGiftPurseGetRankFailWithType:type message:message)
    }];
}

/**
 砸蛋中奖纪录
 */
- (void)userGiftPurseRecordWithPageNum:(NSInteger)pageNum
                              pageSize:(NSInteger)pageSize {
    [HJHttpRequestHelper userGiftPurseRecordWithPageNum:pageNum pageSize:pageSize success:^(NSMutableArray *list) {
        NotifyCoreClient(HJRoomCoreClient, @selector(userGiftPurseRecordSuccessWithList:), userGiftPurseRecordSuccessWithList:list);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(userGiftPurseRecordFailWithMessage:),userGiftPurseRecordFailWithMessage:message);
    }];
}
//清空魅力值
- (void)userReceiveRoomMicMsg:(UserID)uid roomUid:(NSInteger)roomUid
                             {
    [HJHttpRequestHelper userReceiveRoomMicMsg:uid roomUid:roomUid success:^( NSInteger list) {
        NotifyCoreClient(HJRoomCoreClient, @selector(userReceiveRoomMicMsgSuccess), userReceiveRoomMicMsgSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(userReceiveRoomMicMsgFail),userReceiveRoomMicMsgFail);
    }];
                                                    
}
/**
 房间背景列表
 
 @param roomId 房间id
 */
- (void)roomBgListWithRoomId:(NSInteger)roomId
                     success:(void (^)(NSArray *list))success
                     failure:(void (^)(NSString *message))failure {
    
    [HJHttpRequestHelper roomBgListWithRoomId:roomId success:^(NSArray *list) {
        if (success) {
            success(list);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(message);
        }
    }];
}


#pragma mark - Setter & Getter
//判断是否在房间
- (BOOL)isInRoom {
    if (GetCore(HJImRoomCoreV2).currentRoomInfo != nil) {
        return YES;
    }else {
        return NO;
    }
}

- (NSMutableArray *)positionArr {
    if (_positionArr == nil) {
        _positionArr = [NSMutableArray array];
    }
    return _positionArr;
}

- (NSMutableDictionary *)charmInfoMap {
    if (!_charmInfoMap) {
        _charmInfoMap = [NSMutableDictionary dictionary];
    }
    return _charmInfoMap;
}

@end
