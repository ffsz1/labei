//
//  YPImFriendCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPImFriendCore.h"
#import "HJImLoginCoreClient.h"
#import "YPUserCoreHelp.h"
#import <NIMSDK/NIMSDK.h>
#import "HJImFriendCoreClient.h"
#import "HJNotificationCoreClient.h"
#import "YPAttachment.h"
#import <YYModel.h>
#import "YPImMessageCore.h"
#import "YPImRoomCoreV2.h"
#import "HJPraiseCoreClient.h"

@interface YPImFriendCore()<HJImLoginCoreClient, NIMUserManagerDelegate, HJNotificationCoreClient,NIMSystemNotificationManagerDelegate,HJPraiseCoreClient>

@end

@implementation YPImFriendCore
{
    NSMutableArray* _myFriends;
    NSMutableArray* _myBlackList;
    NSMutableArray* _myMuteUserList;
}
- (instancetype)init
{
    self = [super init];
    if (self) {
        AddCoreClient(HJImLoginCoreClient, self);
        AddCoreClient(HJNotificationCoreClient, self);
        AddCoreClient(HJPraiseCoreClient, self);
        _myFriends = [NSMutableArray array];
        _myBlackList = [NSMutableArray array];
        _myMuteUserList = [NSMutableArray array];
        [[NIMSDK sharedSDK].userManager addDelegate:self];
        [[NIMSDK sharedSDK].systemNotificationManager addDelegate:self];
    }
    return self;
}

//发送邀请信息
- (void)sendInviteMessageWithUid:(NSString *)uid {
    NSString *bg = GetCore(YPImRoomCoreV2).roomOwnerInfo.avatar;
    NSString *avatar = GetCore(YPImRoomCoreV2).roomOwnerInfo.avatar;
    NSString *rTitle = GetCore(YPImRoomCoreV2).currentRoomInfo.title;
    if (rTitle.length > 6) {
        rTitle = [rTitle substringWithRange:NSMakeRange(0, 6)];
        rTitle = [NSString stringWithFormat:@"%@...",rTitle];
    }
    NSString *title = [NSString stringWithFormat:@"我邀请你参加【%@】的房间，快来吧！", rTitle];
    NSString *roomid = [NSString stringWithFormat:@"%lld",GetCore(YPImRoomCoreV2).currentRoomInfo.uid];
    NSDictionary *valueDic = @{@"bg": bg ? bg : @"",
                               @"avatar" : avatar ? avatar : @"",
                               @"title" : title,
                               @"uid" : roomid ? roomid : @""
                               };
    NSString *value = valueDic.yy_modelToJSONString;
    YPAttachment *attachement = [[YPAttachment alloc]init];
    attachement.first = Custom_Noti_Header_NotiInviteRoom;
    attachement.second = Custom_Noti_Header_NotiInviteRoom;
    attachement.data = value;
    [GetCore(YPImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%@",uid] type:NIMSessionTypeP2P];
}

-(void)dealloc
{
    RemoveCoreClient(HJImLoginCoreClient, self);
    RemoveCoreClient(HJNotificationCoreClient, self);
    [[NIMSDK sharedSDK].userManager removeDelegate:self];
}

- (NSArray<UserInfo *> *)getMyFriends
{
    return [_myFriends copy];
}

- (NSArray<UserInfo *> *)getBlackList
{
    return [_myBlackList copy];
}

- (NSArray<UserInfo *> *)getMuteList
{
    return [_myMuteUserList copy];
}

- (void)requestFriend:(NSString *)accid requestMsg:(NSString *)requestMsg
{
    if (accid.length > 0) {
        NIMUserRequest *request = [[NIMUserRequest alloc] init];
        request.userId = accid;                            //封装用户ID
        request.operation = NIMUserOperationRequest;                    //封装验证方式
        request.message  = requestMsg;
        
        [[NIMSDK sharedSDK].userManager requestFriend:request completion:^(NSError * _Nullable error) {
            if (error == nil) {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onRequestFriendSuccess), onRequestFriendSuccess);
            } else {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onRequestFriendFailth), onRequestFriendFailth);
            }
        }];
    }
}

- (void)deleteFriend:(NSString *)accid
{
    if (accid.length > 0) {
        [[NIMSDK sharedSDK].userManager deleteFriend:accid completion:^(NSError * _Nullable error) {
            if (error == nil) {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onDeleteFriendSuccess), onDeleteFriendSuccess);
            } else {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onDeleteFirendFailth), onDeleteFirendFailth);
            }
        }];
    }
}

- (void)addFriend:(NSString *)accid
{
    if (accid.length > 0) {
        NIMUserRequest *request = [[NIMUserRequest alloc] init];
        request.userId = accid;
        request.operation = NIMUserOperationAdd;
        
        [[NIMSDK sharedSDK].userManager requestFriend:request completion:^(NSError * _Nullable error) {
            if (error == nil) {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onRequestFriendSuccess), onRequestFriendSuccess);
            } else {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onRequestFriendFailth), onRequestFriendFailth);
            }
        }];
    }
}

- (void)addToBlackList:(NSString *)accid
{
    if (accid.length > 0) {
        [[NIMSDK sharedSDK].userManager addToBlackList:accid completion:^(NSError * _Nullable error) {
            if (error == nil) {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onAddToBlackListSuccess), onAddToBlackListSuccess);
            } else {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onAddToBlackListFailth), onAddToBlackListFailth);
            }
        }];
    }
}

- (void)removeFromBlackList:(NSString *)accid
{
    if (accid.length > 0) {
        [[NIMSDK sharedSDK].userManager removeFromBlackBlackList:accid completion:^(NSError * _Nullable error) {
            if (error == nil) {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onRemoveFromBlackListSuccess), onRemoveFromBlackListSuccess);
            } else {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onRemoveFromBlackListFailth), onRemoveFromBlackListFailth);
            }
        }];
    }
}

- (BOOL)isUserInBlackList:(NSString *)accid
{
    if (accid.length > 0) {
        return [[NIMSDK sharedSDK].userManager isUserInBlackList:accid];
    }
    return NO;
}

- (BOOL)isMyFriend:(NSString *)accid
{
    if (accid.length > 0) {
        return [[NIMSDK sharedSDK].userManager isMyFriend:accid];
    }
    return NO;
}

- (void) updateMyFriends
{
    NSArray *friends = [[NIMSDK sharedSDK].userManager myFriends];
    if (friends.count > 0) {
        [_myFriends removeAllObjects];
        NSMutableArray *uids = [NSMutableArray array];
        for (NIMUser *user in friends) {
            NSString *uid = user.userId;
            [uids addObject:@([uid integerValue])];
        }
        [GetCore(YPUserCoreHelp) getUserInfos:uids refresh:YES success:^(NSArray *infoArr) {
            _myFriends = [infoArr mutableCopy];
            NotifyCoreClient(HJImFriendCoreClient, @selector(onFriendChanged), onFriendChanged);
        }];
    }
    else {
        _myFriends = [NSMutableArray array];
        NotifyCoreClient(HJImFriendCoreClient, @selector(onFriendChanged), onFriendChanged);
    }
    
}

- (void) updateBlackList
{
    NSArray *blackList = [[NIMSDK sharedSDK].userManager myBlackList];
    [_myBlackList removeAllObjects];
    if (blackList.count > 0) {
        for (NIMUser *user in blackList) {
            NSString *uid = user.userId;
            [GetCore(YPUserCoreHelp)getUserInfo:uid.userIDValue refresh:YES success:^(UserInfo *info) {
                [_myBlackList addObject:info];
            }];
        }
    }
    NotifyCoreClient(HJImFriendCoreClient, @selector(onBlackListChanged), onBlackListChanged);
}

- (void) updateMuteList
{
    NSArray *muteUserList = [[NIMSDK sharedSDK].userManager myMuteUserList];
    if (muteUserList.count > 0) {
        for (NIMUser *user in muteUserList) {
            NSString *uid = user.userId;
//            UserInfo *info = [GetCore(UserCore) getUserInfo:uid.userIDValue refresh:NO];
            [GetCore(YPUserCoreHelp)getUserInfo:uid.userIDValue refresh:NO success:^(UserInfo *info) {
                [_myMuteUserList addObject:info];
            }];

            
        }
    }
    NotifyCoreClient(HJImFriendCoreClient, @selector(onMuteListChanged), onMuteListChanged);
}

-(void) onFriendAdd
{
    [self updateMyFriends];
}

#pragma mark - NIMUserManagerDelegate
- (void)onFriendChanged:(NIMUser *)user
{
    [self updateMyFriends];
}

- (void)onBlackListChanged
{
    [self updateBlackList];
}

- (void)onMuteListChanged
{
    [self updateMuteList];
}

#pragma mark - ImLoginCoreClient
- (void)onImSyncSuccess
{
    [self updateMyFriends];
    [self updateBlackList];
    [self updateMuteList];
}

#pragma mark - PraiseCoreClient
- (void)onPraiseSuccess:(UserID)uid {
    
    [self updateMyFriends];
    [self updateBlackList];
    [self updateMuteList];
}

#pragma mark - NotificationCoreClient
- (void)onRecvFriendAddNoti:(NIMSystemNotification *)notification
{
    if (notification.type == NIMSystemNotificationTypeFriendAdd) {
        if ([notification.attachment isKindOfClass:[NIMUserAddAttachment class]]) {
            NIMUserAddAttachment *atta = (NIMUserAddAttachment *)notification.attachment;
            if (atta.operationType == NIMUserOperationRequest) {
                NotifyCoreClient(HJImFriendCoreClient, @selector(onRecieveFriendAddNoti:), onRecieveFriendAddNoti:notification.sourceID);
            }
        }
    }
}

- (void)onReceiveSystemNotification:(NIMSystemNotification *)notification {
//    if (notification.type == ) {
//        <#statements#>
//    }
    NIMSystemNotificationType type = notification.type;
    
    switch (type) {
        case NIMSystemNotificationTypeTeamApply: {
            
            break;
        }
        case NIMSystemNotificationTypeTeamApplyReject: {
            
            break;
        }
        case NIMSystemNotificationTypeTeamInvite: {
            
            break;
        }
        case NIMSystemNotificationTypeTeamIviteReject: {
            
            break;
        }
        case NIMSystemNotificationTypeFriendAdd:
        {
            NotifyCoreClient(HJImFriendCoreClient, @selector(onFriendAdd), onFriendAdd);
            id object = notification.attachment;
            if ([object isKindOfClass:[NIMUserAddAttachment class]]) {
                //强类型转换
                NIMUserOperation operation = [(NIMUserAddAttachment *)object operationType];
                //根据不同的操作类型去处理不同业务
                switch (operation) {
                    case NIMUserOperationAdd:
                        // 对方直接加你为好友
                        break;
                    case NIMUserOperationRequest:
                        // 对方请求加你为好友
                        break;
                    case NIMUserOperationVerify:
                        //对方通过了你的好友请求
                        break;
                    case NIMUserOperationReject:
                        //对方拒绝了你的好友请求
                        break;
                    default:
                        break;
                }
            }

        }
    }
}

@end
