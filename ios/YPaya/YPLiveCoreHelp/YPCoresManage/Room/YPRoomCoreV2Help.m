//
//  YPRoomCoreV2Help.m
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomCoreV2Help.h"
#import "YPHttpRequestHelper+Room.h"

#import "YPRoomQueueCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "HJRoomQueueCoreClient.h"
#import "YPAuthCoreHelp.h"
#import "HJAuthCoreClient.h"
#import "YPMeetingCore.h"
#import "HJMeetingCoreClient.h"
#import "YPImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "YPFaceCore.h"
#import "HJFaceCoreClient.h"
#import "YPImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"
#import "YPGiftCore.h"
#import "YPUserCoreHelp.h"

#import "YPGiftReceiveInfo.h"
#import "YPShareSendInfo.h"
#import "YPAttachment.h"
#import "YPRoomMemberCharmInfoModel.h"

#import "NSObject+YYModel.h"
#import "YYUtility.h"
#import "HJImMessageSendCoreClient.h"

#import "YPHttpRequestHelper+Egg.h"


@interface YPRoomCoreV2Help()<HJImRoomCoreClient, HJImRoomCoreClientV2, HJImMessageCoreClient,HJAuthCoreClient, HJMeetingCoreClient, HJRoomQueueCoreClient,HJFaceCoreClient>
{
    /*
     用来记录魅力值数据时间戳
     */
    NSUInteger latestTimestamps;
}

@property (nonatomic, strong) dispatch_source_t timer; //计时器

@property (nonatomic, strong) NSMutableDictionary *charmInfoMap;//房间魅力值Map


@end

@implementation YPRoomCoreV2Help
{
    /*
     用来通知公屏更新
     */
    NSTimer *_notifyChannelTextTimer;
}

- (void)userGiftPurseDraw:(NSString *)type {
    [YPHttpRequestHelper userGiftPurseDraw:type success:^(id data) {
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
- (void)onRecvChatRoomTextMsg:(YPIMMessage *)msg {
    if (msg.text && msg.text.length > 0) {
        [self addMessageToArray:msg];
    }
}

//收到自定义消息
- (void)onRecvChatRoomCustomMsg:(YPIMMessage *)msg {
    [self handlerCustomMsg:msg];
}

- (void)onWillSendMessage:(NIMMessage *)msg {
    if (msg.messageType == NIMMessageTypeCustom) {
        NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
        YPAttachment *attachment = (YPAttachment *)obj.attachment;
        if (attachment.first != Custom_Noti_Header_Queue &&
            attachment.first != Custom_Noti_Header_Face &&
            attachment.first != Custom_Noti_Header_PK) {
            
            if (attachment.first == Custom_Noti_Header_ALLMicroSend || attachment.first == Custom_Noti_Header_Gift) {
//                if (GetCore(YPGiftCore).isOpenAnimation) {
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
    NSLog(@"%@",GetCore(YPImRoomCoreV2).currentRoomInfo);
    if (GetCore(YPImRoomCoreV2).currentRoomInfo == nil) {
        return;
    }

    [self reportUserInterRoom];//上报进入房间
    [self startRoomTimer];
    if (GetCore(YPImRoomCoreV2).currentRoomInfo.uid == GetCore(YPAuthCoreHelp).getUid.userIDValue) {
        [GetCore(YPMeetingCore) joinMeeting:[NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] actor:YES];
    } else {
        [GetCore(YPMeetingCore) joinMeeting:[NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] actor:NO];
    }
}

//退出房
- (void)onMeExitChatRoomSuccessV2 {
    [self cancelRoomRecord];
    if (GetCore(YPImRoomCoreV2).currentRoomInfo.uid == [GetCore(YPAuthCoreHelp) getUid].userIDValue && GetCore(YPImRoomCoreV2).currentRoomInfo.type != RoomType_Game) {
        [self closeRoom:GetCore(YPImRoomCoreV2).currentRoomInfo.uid];
    }
    [self reportUserOuterRoom];
    [GetCore(YPMeetingCore) leaveMeeting:[NSString stringWithFormat:@"%ld",GetCore(YPImRoomCoreV2).currentRoomInfo.roomId]];
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
    [GetCore(YPMeetingCore) leaveMeeting:[NSString stringWithFormat:@"%ld",GetCore(YPImRoomCoreV2).currentRoomInfo.roomId]];
    self.speakingList = nil;
    self.messages = nil;
}

//用户进入聊天室
- (void)onUserInterChatRoom:(YPChatRoomMember *)member{
    YPIMMessage *message = [YPIMMessage new];
    message.messageType = HJIMMessageTypeNotification;
    
    JXIMSession *session = [JXIMSession new];
    session.sessionId = [NSString stringWithFormat:@"%ld", (long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
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
    [[GetCore(YPImRoomCoreV2) rac_queryChartRoomMemberByUid:uid] subscribeNext:^(id x) {
        YPChatRoomMember *chatRoomMember = (YPChatRoomMember *)x;
        NSString *position = [self findThePositionByUid:chatRoomMember.account.userIDValue];
        
        if (position) {
            YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]+1];
            YPRoomQueueInfo *sequence = item.queueInfo;
            sequence.chatRoomMember = chatRoomMember;
        }
        NotifyCoreClient(HJRoomCoreClient, @selector(onManagerRemove:), onManagerRemove:chatRoomMember);
    }];
}

//收到添加管理更新通知
- (void)managerAdd:(NSString *)uid {
    [[GetCore(YPImRoomCoreV2) rac_queryChartRoomMemberByUid:uid] subscribeNext:^(id x) {
        YPChatRoomMember *chatRoomMember = (YPChatRoomMember *)x;
        NSString *position = [self findThePositionByUid:chatRoomMember.account.userIDValue];
        
        if (position) {
            YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]+1];
            YPRoomQueueInfo *sequence = item.queueInfo;
            sequence.chatRoomMember = chatRoomMember;
        }
        NotifyCoreClient(HJRoomCoreClient, @selector(onManagerAdd:), onManagerAdd:chatRoomMember);
    }];
}

//black
- (void)onUserBeAddBlack:(NSString *)uid {
    [[GetCore(YPImRoomCoreV2) rac_queryChartRoomMemberByUid:uid] subscribeNext:^(id x) {
        YPChatRoomMember *chatRoomMember = (YPChatRoomMember *)x;
        if ([GetCore(YPRoomQueueCoreV2Help) isOnMicro:[chatRoomMember.account longLongValue]]) {
            NSString *position = [GetCore(YPRoomQueueCoreV2Help) findThePositionByUid:[chatRoomMember.account longLongValue]];
            
            //修改队列
            [GetCore(YPRoomQueueCoreV2Help) removeChatroomQueueWithPosition:position success:^(BOOL success) {
                
            } failure:^(NSString *message) {
                
            }];
            YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]];
            YPRoomQueueInfo *sequence = item.queueInfo;
            sequence.chatRoomMember = nil;
        }
        NotifyCoreClient(HJRoomCoreClient, @selector(userBeAddBlack:), userBeAddBlack:chatRoomMember);
    }];
}

- (void)onUserBeRemoveBlack:(NSString *)uid {
    [[GetCore(YPImRoomCoreV2) rac_queryChartRoomMemberByUid:uid] subscribeNext:^(id x) {
        YPChatRoomMember *chatRoomMember = (YPChatRoomMember *)x;
        if ([GetCore(YPRoomQueueCoreV2Help) isOnMicro:[chatRoomMember.account longLongValue]]) {
            NSString *position = [GetCore(YPRoomQueueCoreV2Help) findThePositionByUid:[chatRoomMember.account longLongValue]];
            
            YPIMQueueItem *item = [GetCore(YPImRoomCoreV2).micQueue safeObjectAtIndex:[position intValue]];
            YPRoomQueueInfo *sequence = item.queueInfo;
            sequence.chatRoomMember = chatRoomMember;
        }
        NotifyCoreClient(HJRoomCoreClient, @selector(userBeRemoveBlack:), userBeRemoveBlack:chatRoomMember);
    }];
}

#pragma mark - MeetingCoreClient
//加入声网成功
- (void)onJoinMeetingSuccess {
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    if (GetCore(YPImRoomCoreV2).currentRoomInfo.uid == uid) {
        if (GetCore(YPImRoomCoreV2).currentRoomInfo.type != RoomType_Game) {
            
                [GetCore(YPMeetingCore) setMeetingRole:YES];
                [GetCore(YPMeetingCore) setCloseMicro:NO];
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
- (void)onFaceIsResult:(YPFacePlayInfo *)info {
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
    [YPHttpRequestHelper openRoom:uid type:type title:title roomDesc:roomDesc backPic:backPic rewardId:rewardId success:^(YPChatRoomInfo *roomInfo) {
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
    [YPHttpRequestHelper updateRoomInfo:uid title:title roomDesc:roomDesc roomNotice:roomNotice backPic:backPic roomPassword:nil tag:0 playInfo:playInfo giftEffectSwitch:giftEffectSwitch giftCardSwitch:giftCardSwitch publicChatSwitch:publicChatSwitch success:^(YPChatRoomInfo *roomInfo) {
        @strongify(self)
        [GetCore(YPImRoomCoreV2) setCurrentRoomInfo:roomInfo];
        NotifyCoreClient(HJRoomCoreClient, @selector(onUpdateRoomInfoSuccess:), onUpdateRoomInfoSuccess:roomInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onUpdateRoomInfoFailth:), onUpdateRoomInfoFailth:message);
    }];
}

//房主修改游戏房间信息
- (void)updateGameRoomInfo:(UserID)uid backPic:(NSString *)backPick title:(NSString *)title roomTopic:(NSString *)roomTopic roomNotice:(NSString *)roomNotice roomPassword:(NSString *)roomPassword tag:(int)tag playInfo:(NSString *)playInfo giftEffectSwitch:(BOOL)giftEffectSwitch giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch  {
    [YPHttpRequestHelper updateRoomInfo:uid title:title roomDesc:roomTopic roomNotice:roomNotice backPic:backPick roomPassword:roomPassword tag:tag playInfo:playInfo giftEffectSwitch:giftEffectSwitch giftCardSwitch:giftCardSwitch publicChatSwitch:publicChatSwitch success:^(YPChatRoomInfo *roomInfo) {
        [GetCore(YPImRoomCoreV2) setCurrentRoomInfo:roomInfo];
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
    
    [YPHttpRequestHelper managerUpdateRoomInfo:uid title:title roomDesc:roomTopic roomNotice:roomNotice backPic:backPick roomPassword:roomPassword playInfo:playInfo tag:tag giftEffectSwitch:giftEffectSwitch giftCardSwitch:giftCardSwitch publicChatSwitch:publicChatSwitch success:^(YPChatRoomInfo *roomInfo) {
        [GetCore(YPImRoomCoreV2) setCurrentRoomInfo:roomInfo];
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
    [YPHttpRequestHelper closeRoom:uid success:^{
        NotifyCoreClient(HJRoomCoreClient, @selector(onCloseRoomSuccess), onCloseRoomSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onCloseRoomFailth:code:), onCloseRoomFailth:message code:resCode);
    }];
}


#pragma mark -- mic
//获取麦上人员
- (NSMutableArray *)getAdminMembers {
    return GetCore(YPImRoomCoreV2).micMembers;
}



#pragma mark --房间信息相关
//根据房主id请求房间信息
- (RACSignal *)requestRoomInfo:(UserID)uid
{
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        [YPHttpRequestHelper getRoomInfo:uid success:^(YPChatRoomInfo *roomInfo) {
            [subscriber sendNext:roomInfo];
            [subscriber sendCompleted];
        } failure:^(NSNumber *resCode, NSString *message) {
            [subscriber sendError:CORE_API_ERROR(RoomCoreErrorDomain, resCode.integerValue, message)];
        }];
        return nil;
    }];
}

- (void)getRoomInfo:(UserID)uid success:(void (^)(YPChatRoomInfo *roomInfo))success failure:(void (^)(NSInteger resCode, NSString *message))failure {
    [YPHttpRequestHelper getRoomInfo:uid success:^(YPChatRoomInfo *roomInfo) {
        !success ?: success(roomInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode.integerValue, message);
    }];
}

- (YPChatRoomInfo *)getCurrentRoomInfo {
    return GetCore(YPImRoomCoreV2).currentRoomInfo;
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
    [YPHttpRequestHelper requestNewRoomBounsListWithType:type withDataType:dataTpye Success:^(NSMutableArray *bounsInfoList) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onGetRoomBounsListSuucess:type:dateType:), onGetRoomBounsListSuucess:bounsInfoList type:type dateType:dataTpye);
    } failure:^(NSNumber *code, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(onGetRoomBounsListFailth:type:dateType:), onGetRoomBounsListFailth:message type:type dateType:dataTpye);
    }];
}

//获取用户进入了的房间的信息
- (void)getUserInterRoomInfo:(UserID)uid {
    [YPHttpRequestHelper requestUserInRoomInfoBy:uid Success:^(YPChatRoomInfo *roomInfo) {
        NotifyCoreClient(HJRoomCoreClient, @selector(requestUserRoomInterInfo: uid:), requestUserRoomInterInfo:roomInfo uid:uid);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(requestUserRoomInterInfoFailth:), requestUserRoomInterInfoFailth:message);
    }];
}

#pragma mark - blacklist
//判断自己是否在黑名单
- (void)judgeIsInBlackList:(NSString *)roomID {
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NIMChatroomMembersByIdsRequest *request = [[NIMChatroomMembersByIdsRequest alloc]init];
    request.roomId = roomID;
    request.userIds = @[uid];
    @weakify(self);
    [[NIMSDK sharedSDK].chatroomManager fetchChatroomMembersByIds:request completion:^(NSError * _Nullable error, NSArray<YPChatRoomMember *> * _Nullable members) {
        @strongify(self);
        for (YPChatRoomMember *member in members) {
//            if (member.isInBlackList) {
//                NotifyCoreClient(AuthCoreClient, @selector(mySelfIsInBalckList:), mySelfIsInBalckList:YES);
//            }else {
//                NotifyCoreClient(AuthCoreClient, @selector(mySelfIsInBalckList:), mySelfIsInBalckList:NO);
//            }
        }
    }];
}

- (void)sendLotteryMessageWithGiftInfo:(YPGiftInfo *)giftInfo nick:(NSString *)nick{
    if (!giftInfo) return;
    
    YPAttachment *attachement = [[YPAttachment alloc]init];
    attachement.first = Custom_Noti_Header_Winning;
    attachement.second = Custom_Noti_Header_Winning;

    NSDictionary *giftDict = [giftInfo yy_modelToJSONObject];
    NSMutableDictionary *buffer = [NSMutableDictionary dictionaryWithDictionary:giftDict];
    [buffer setObject:JX_STR_AVOID_nil(nick) forKey:@"nick"];
    [buffer setObject:@(giftInfo.giftNum) forKey:@"count"];
    NSDictionary *attMessageDic = @{@"params": buffer};
    attachement.data = attMessageDic;
    
    [GetCore(YPImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] type:(JXIMSessionType) NIMSessionTypeChatroom];
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
- (void)handlerCustomMsg:(YPIMMessage *)msg
{
    // 喜翻鸭语音需要进行单独处理消息 消息类型：礼物 房间进入

    JXIMCustomObject *obj = msg.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
        YPAttachment *attachment = (YPAttachment *)obj.attachment;
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
            
//            if (GetCore(YPGiftCore).isOpenAnimation) {
            
                YPGiftReceiveInfo *info = [YPGiftReceiveInfo yy_modelWithDictionary:attachment.data];
                YPGiftInfo *giftInfo = [GetCore(YPGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeNormal];
                if (giftInfo.goldPrice > 0) {
                    [self addMessageToArray:msg];
                }
//            }
        }
        else if (attachment.first == Custom_Noti_Header_ALLMicroSend) {
            if (attachment.second == Custom_Noti_Sub_AllMicroSend) {
                
//                if (GetCore(YPGiftCore).isOpenAnimation) {
                
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
        GetCore(YPImRoomCoreV2).enterRoomInfo.giftCardSwitch = NO;
    }else if (second == Custom_Noti_Sub_Car_Effect_Close){
        GetCore(YPImRoomCoreV2).enterRoomInfo.giftCardSwitch = YES;
    }
    
    //    if (second == Custom_Noti_Sub_Gift_Effect_Open) {
    //        GetCore(YPImRoomCoreV2).enterRoomInfo.giftEffectSwitch = YES;
    //    }else if (second == Custom_Noti_Sub_Gift_Effect_Close){
    //        GetCore(YPImRoomCoreV2).enterRoomInfo.giftEffectSwitch = NO;
    //    }
    
    
    
}


//更新麦位魅力值
- (void)updateMicCharm:(YPAttachment *)attachment {
    XCRoomCharmDataModel *charmDataModel = [XCRoomCharmDataModel yy_modelWithJSON:attachment.data];
    if ([charmDataModel.roomId integerValue] != self.getCurrentRoomInfo.roomId) {
        return;
    }
    
    if (charmDataModel.timestamps < latestTimestamps) {
        return;
    }
    
    latestTimestamps = charmDataModel.timestamps;
    NSDictionary *latestCharm = charmDataModel.latestCharm;
    
    NSMutableArray <YPRoomMemberCharmInfoModel *> *memberCharmInfoList = [NSMutableArray array];
    NSMutableArray<YPIMQueueItem *> *mircoList = GetCore(YPImRoomCoreV2).micQueue;
    [mircoList enumerateObjectsUsingBlock:^(YPIMQueueItem * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([latestCharm containsObjectForKey:obj.queueInfo.chatRoomMember.account]) {
            NSDictionary *roomMemberInfo = [latestCharm valueForKey:JX_STR_AVOID_nil(obj.queueInfo.chatRoomMember.account)];
            YPRoomMemberCharmInfoModel *memberCharmInfoModel = [YPRoomMemberCharmInfoModel yy_modelWithJSON:roomMemberInfo];
            memberCharmInfoModel.uid = JX_STR_AVOID_nil(obj.queueInfo.chatRoomMember.account);
            //            obj.queueInfo.chatRoomMember.charmValue = memberCharmInfoModel.value;
            //            obj.queueInfo.chatRoomMember.hasHat = memberCharmInfoModel.withHat;
            [memberCharmInfoList addObject:memberCharmInfoModel];
        }
    }];
    NotifyCoreClient(HJRoomCoreClient, @selector(updateMicCharm:), updateMicCharm:memberCharmInfoList);
}

//添加消息到内存，最多保留200
- (void)addMessageToArray:(YPIMMessage *)msg
{
    if (msg.session.sessionType == JXIMSessionTypeChatroom && (msg.session.sessionId.integerValue == GetCore(YPImRoomCoreV2).currentRoomInfo.roomId || msg.session.sessionId == nil)) { // 全服消息
        
        
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
    if ([GetCore(YPAuthCoreHelp)getTicket].length > 0) {
        [YPHttpRequestHelper reportUserInterRoomSuccess:^(BOOL success) {
        } failure:^(NSNumber *resCode, NSString *message) {
        }];
    }
}

- (void)reportUserOuterRoom {
    if ([GetCore(YPAuthCoreHelp) getTicket].length > 0) {
        [YPHttpRequestHelper reportUserOutRoomSuccess:^(BOOL success) {
        } failure:^(NSNumber *resCode, NSString *message) {
        }];
    }
}

//房间埋点统计
- (void)recordTheRoomTime:(UserID)uid roomUid:(UserID)roomUid {
    [YPHttpRequestHelper recordTheRoomTime:uid roomUid:roomUid];
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
        if ([GetCore(YPAuthCoreHelp) getUid].userIDValue != GetCore(YPImRoomCoreV2).currentRoomInfo.uid) {
            [self recordTheRoomTime:[GetCore(YPAuthCoreHelp) getUid].userIDValue roomUid:GetCore(YPImRoomCoreV2).currentRoomInfo.uid];
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
        NSArray *micQueue = GetCore(YPImRoomCoreV2).micQueue;
        if (micQueue.count > 0) {
            for (YPIMQueueItem *item in micQueue) {
                YPChatRoomMember *member = item.queueInfo.chatRoomMember;
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
    [YPHttpRequestHelper userGiftPurseGetRankWithType:type success:^(NSMutableArray *list) {
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
    [YPHttpRequestHelper userGiftPurseRecordWithPageNum:pageNum pageSize:pageSize success:^(NSMutableArray *list) {
        NotifyCoreClient(HJRoomCoreClient, @selector(userGiftPurseRecordSuccessWithList:), userGiftPurseRecordSuccessWithList:list);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJRoomCoreClient, @selector(userGiftPurseRecordFailWithMessage:),userGiftPurseRecordFailWithMessage:message);
    }];
}
//清空魅力值
- (void)userReceiveRoomMicMsg:(UserID)uid roomUid:(NSInteger)roomUid
                             {
    [YPHttpRequestHelper userReceiveRoomMicMsg:uid roomUid:roomUid success:^( NSInteger list) {
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
    
    [YPHttpRequestHelper roomBgListWithRoomId:roomId success:^(NSArray *list) {
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
    if (GetCore(YPImRoomCoreV2).currentRoomInfo != nil) {
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
