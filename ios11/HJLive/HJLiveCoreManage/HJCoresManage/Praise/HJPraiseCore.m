//
//  PraiseCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPraiseCore.h"
#import "HJPraiseCoreClient.h"
#import "HJAuthCoreHelp.h"
#import "HJAuthCoreClient.h"
#import "HJUserCoreHelp.h"
#import "HJImFriendCoreClient.h"
#import "HJHttpRequestHelper+Praise.h"
#import "HJShareSendInfo.h"
#import "Attachment.h"
#import "HJImRoomCoreV2.h"
#import "HJImMessageCore.h"

@interface HJPraiseCore()
@end

@implementation HJPraiseCore
- (instancetype)init
{
    self = [super init];
    if (self) {
        
    }
    return self;
}

- (void)praise:(UserID)paiseUid bePraisedUid:(UserID)bePraisedUid
{
    
    if (bePraisedUid  == GetCore(HJImRoomCoreV2).currentRoomInfo.uid) {
        UserInfo *userInfo = [GetCore(HJUserCoreHelp) getUserInfoInDB:bePraisedUid];
        UserInfo *myInfo = [GetCore(HJUserCoreHelp) getUserInfoInDB:paiseUid];
        HJShareSendInfo *info = [[HJShareSendInfo alloc]init];
        info.uid = paiseUid;
        info.targetNick = userInfo.nick;
        info.targetUid = bePraisedUid;
        info.nick = myInfo.nick;
        
        Attachment *attachment = [[Attachment alloc]init];
        attachment.first = Custom_Noti_Header_Room_Tip;
        attachment.second = Custom_Noti_Header_Room_Tip_Attentent_Owner;
        attachment.data = info.encodeAttachemt;
        
        NSString *sessionId = [NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
        [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachment sessionId:sessionId type:NIMSessionTypeChatroom];
        
        
    }
    
    
    __block UserID bePraiseuid = bePraisedUid;
    [HJHttpRequestHelper praise:paiseUid bePraisedUid:bePraisedUid success:^{
        NotifyCoreClient(HJPraiseCoreClient, @selector(onPraiseSuccess:), onPraiseSuccess:bePraiseuid);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onPraiseFailth:), onPraiseFailth:message);
    }];
}

- (void) cancel:(UserID)cancelUid beCanceledUid:(UserID)beCanceledUid;
{
//    __block UserID beCancelUid = beCanceledUid;
    [HJHttpRequestHelper cancel:cancelUid beCanceledUid:beCanceledUid success:^{
        NotifyCoreClient(HJPraiseCoreClient, @selector(onCancelSuccess:), onCancelSuccess:beCanceledUid);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onCancelFailth:), onCancelFailth:message);
    }];
}

- (void)deleteFriend:(UserID)deleteUid beDeletedUid:(UserID)beDeletedUid
{
    __block UserID beDeleteuid = beDeletedUid;
    [HJHttpRequestHelper deleteFriend:deleteUid beDeletedFriendUid:beDeletedUid success:^{
        NotifyCoreClient(HJPraiseCoreClient, @selector(onDeleteFriendSuccess:), onDeleteFriendSuccess:beDeleteuid);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onDeleteFriendFailth:), onDeleteFriendFailth:message);
    }];
}

- (void) requestAttentionListState:(int)state page:(int)page;
{
    UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;

    [HJHttpRequestHelper requestAttentionList:uid state:state page:page success:^(NSArray *userInfos) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onRequestAttentionListState:success:), onRequestAttentionListState:state success:userInfos);
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

- (void)isLike:(UserID)uid isLikeUid:(UserID)isLikeUid
{
    __block UserID islikeUid = isLikeUid;
    [HJHttpRequestHelper isLike:uid isLikeUid:isLikeUid success:^(BOOL isLike) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onRequestIsLikeSuccess:islikeUid:), onRequestIsLikeSuccess:isLike islikeUid:islikeUid);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onRequestIsLikeFailth:), onRequestIsLikeFailth:message);
    }];
}

//获取粉丝列表
- (void) requestFansListState:(int)state page:(NSInteger)page{
    UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
    [HJHttpRequestHelper getFansListWithUid:uid page:page success:^(NSArray *userInfos) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onRequestFansListState:success:), onRequestFansListState:state success:userInfos);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPraiseCoreClient, @selector(onRequestFansListState:failth:), onRequestFansListState:state failth:message);
    }];
}

@end
