//
//  PraiseCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPraiseCore.h"
#import "HJPraiseCoreClient.h"
#import "YPAuthCoreHelp.h"
#import "HJAuthCoreClient.h"
#import "YPUserCoreHelp.h"
#import "HJImFriendCoreClient.h"
#import "YPHttpRequestHelper+Praise.h"
#import "YPShareSendInfo.h"
#import "YPAttachment.h"
#import "YPImRoomCoreV2.h"
#import "YPImMessageCore.h"

@interface YPPraiseCore()
@end

@implementation YPPraiseCore
- (instancetype)init
{
    self = [super init];
    if (self) {
        
    }
    return self;
}

- (void)praise:(UserID)paiseUid bePraisedUid:(UserID)bePraisedUid
{
    
    if (bePraisedUid  == GetCore(YPImRoomCoreV2).currentRoomInfo.uid) {
        UserInfo *userInfo = [GetCore(YPUserCoreHelp) getUserInfoInDB:bePraisedUid];
        UserInfo *myInfo = [GetCore(YPUserCoreHelp) getUserInfoInDB:paiseUid];
        YPShareSendInfo *info = [[YPShareSendInfo alloc]init];
        info.uid = paiseUid;
        info.targetNick = userInfo.nick;
        info.targetUid = bePraisedUid;
        info.nick = myInfo.nick;
        
        YPAttachment *attachment = [[YPAttachment alloc]init];
        attachment.first = Custom_Noti_Header_Room_Tip;
        attachment.second = Custom_Noti_Header_Room_Tip_Attentent_Owner;
        attachment.data = info.encodeAttachemt;
        
        NSString *sessionId = [NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
        [GetCore(YPImMessageCore)sendCustomMessageAttachement:attachment sessionId:sessionId type:NIMSessionTypeChatroom];
        
        
    }
    
    
    __block UserID bePraiseuid = bePraisedUid;
    [YPHttpRequestHelper praise:paiseUid bePraisedUid:bePraisedUid success:^{
        NotifyCoreClient(HJPraiseCoreClient, @selector(onPraiseSuccess:), onPraiseSuccess:bePraiseuid);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onPraiseFailth:), onPraiseFailth:message);
    }];
}

- (void) cancel:(UserID)cancelUid beCanceledUid:(UserID)beCanceledUid;
{
//    __block UserID beCancelUid = beCanceledUid;
    [YPHttpRequestHelper cancel:cancelUid beCanceledUid:beCanceledUid success:^{
        NotifyCoreClient(HJPraiseCoreClient, @selector(onCancelSuccess:), onCancelSuccess:beCanceledUid);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onCancelFailth:), onCancelFailth:message);
    }];
}

- (void)deleteFriend:(UserID)deleteUid beDeletedUid:(UserID)beDeletedUid
{
    __block UserID beDeleteuid = beDeletedUid;
    [YPHttpRequestHelper deleteFriend:deleteUid beDeletedFriendUid:beDeletedUid success:^{
        NotifyCoreClient(HJPraiseCoreClient, @selector(onDeleteFriendSuccess:), onDeleteFriendSuccess:beDeleteuid);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onDeleteFriendFailth:), onDeleteFriendFailth:message);
    }];
}

- (void) requestAttentionListState:(int)state page:(int)page;
{
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;

    [YPHttpRequestHelper requestAttentionList:uid state:state page:page success:^(NSArray *userInfos) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onRequestAttentionListState:success:), onRequestAttentionListState:state success:userInfos);
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

- (void)isLike:(UserID)uid isLikeUid:(UserID)isLikeUid
{
    __block UserID islikeUid = isLikeUid;
    [YPHttpRequestHelper isLike:uid isLikeUid:isLikeUid success:^(BOOL isLike) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onRequestIsLikeSuccess:islikeUid:), onRequestIsLikeSuccess:isLike islikeUid:islikeUid);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onRequestIsLikeFailth:), onRequestIsLikeFailth:message);
    }];
}

//获取粉丝列表
- (void) requestFansListState:(int)state page:(NSInteger)page{
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    [YPHttpRequestHelper getFansListWithUid:uid page:page success:^(NSArray *userInfos) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onRequestFansListState:success:), onRequestFansListState:state success:userInfos);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onRequestFansListState:failth:), onRequestFansListState:state failth:message);
    }];
}

@end
