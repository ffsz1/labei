//
//  YPNIMKitDataProviderImpl.m
//  YPNIMKit
//
//  Created by chris on 2016/10/31.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPNIMKit.h"
#import "YPNIMKitDataProviderImpl.h"
#import "YPNIMKitInfoFetchOption.h"
#import "UIImage+YPNIMKit.h"

#pragma mark - kit data request
@interface NIMKitDataRequest : NSObject

@property (nonatomic,strong) NSMutableSet *failedUserIds;

@property (nonatomic,assign) NSInteger maxMergeCount; //最大合并数

- (void)requestUserIds:(NSArray *)userIds;

@end


@implementation NIMKitDataRequest{
    NSMutableArray *_requstUserIdArray; //待请求池
    BOOL _isRequesting;
}

- (instancetype)init{
    self = [super init];
    if (self) {
        _failedUserIds = [[NSMutableSet alloc] init];
        _requstUserIdArray = [[NSMutableArray alloc] init];
    }
    return self;
}


- (void)requestUserIds:(NSArray *)userIds
{
    for (NSString *userId in userIds)
    {
        if (![_requstUserIdArray containsObject:userId] && ![_failedUserIds containsObject:userId])
        {
            [_requstUserIdArray addObject:userId];
        }
    }
    [self request];
}


- (void)request
{
    static NSUInteger MaxBatchReuqestCount = 10;
    if (_isRequesting || [_requstUserIdArray count] == 0) {
        return;
    }
    _isRequesting = YES;
    NSArray *userIds = [_requstUserIdArray count] > MaxBatchReuqestCount ?
    [_requstUserIdArray subarrayWithRange:NSMakeRange(0, MaxBatchReuqestCount)] : [_requstUserIdArray copy];
    
    __weak typeof(self) weakSelf = self;
    [[NIMSDK sharedSDK].userManager fetchUserInfos:userIds
                                        completion:^(NSArray *users, NSError *error) {
                                            [weakSelf afterReuquest:userIds];
                                            if (!error && users.count)
                                            {
                                                [[YPNIMKit sharedKit] notfiyUserInfoChanged:userIds];
                                            }
                                            else if ([weakSelf shouldAddToFailedUsers:error])
                                            {
                                                [weakSelf.failedUserIds addObjectsFromArray:userIds];
                                            }
                                        }];
}

- (void)afterReuquest:(NSArray *)userIds
{
    _isRequesting = NO;
    [_requstUserIdArray removeObjectsInArray:userIds];
    [self request];
    
}

- (BOOL)shouldAddToFailedUsers:(NSError *)error
{
    //没有错误这种异常情况走到这个路径里也不对，不再请求
    return error.code != NIMRemoteErrorCodeTimeoutError || !error;
}

@end

#pragma mark - data provider impl

@interface YPNIMKitDataProviderImpl()<NIMUserManagerDelegate,NIMTeamManagerDelegate,NIMLoginManagerDelegate>

@property (nonatomic,strong) UIImage *defaultUserAvatar;

@property (nonatomic,strong) UIImage *defaultTeamAvatar;

@property (nonatomic,strong) NIMKitDataRequest *request;

@end


@implementation YPNIMKitDataProviderImpl

- (instancetype)init{
    self = [super init];
    if (self) {
        _request = [[NIMKitDataRequest alloc] init];
        _request.maxMergeCount = 20;
        [[NIMSDK sharedSDK].userManager addDelegate:self];
        [[NIMSDK sharedSDK].teamManager addDelegate:self];
        [[NIMSDK sharedSDK].loginManager addDelegate:self];
    }
    return self;
}

- (void)dealloc
{
    [[NIMSDK sharedSDK].userManager removeDelegate:self];
    [[NIMSDK sharedSDK].teamManager removeDelegate:self];
    [[NIMSDK sharedSDK].loginManager removeDelegate:self];
}


#pragma mark - public api
- (YPNIMKitInfo *)infoByUser:(NSString *)userId
                    option:(YPNIMKitInfoFetchOption *)option
{
    //优先检测是否为机器人
    YPNIMKitInfo *info = [self infoByRobot:userId];
    if (info == nil)
    {
        NIMSession *session = option.message.session?:option.session;
        info = [self infoByUser:userId session:session option:option];
    }
    return info;
}

- (YPNIMKitInfo *)infoByTeam:(NSString *)teamId
                    option:(YPNIMKitInfoFetchOption *)option
{
    NIMTeam *team    = [[NIMSDK sharedSDK].teamManager teamById:teamId];
    YPNIMKitInfo *info = [[YPNIMKitInfo alloc] init];
    info.showName    = team.teamName;
    info.infoId      = teamId;
    info.avatarImage = self.defaultTeamAvatar;
    info.avatarUrlString = team.thumbAvatarUrl;
    return info;
}


#pragma mark - 用户信息拼装
//会话中用户信息
- (YPNIMKitInfo *)infoByUser:(NSString *)userId
                   session:(NIMSession *)session
                    option:(YPNIMKitInfoFetchOption *)option
{
    NIMSessionType sessionType = session.sessionType;
    YPNIMKitInfo *info;
    
    switch (sessionType) {
        case NIMSessionTypeP2P:
        {
            info = [self userInfoInP2P:userId option:option];
        }
            break;
        case NIMSessionTypeTeam:
        {
            info = [self userInfo:userId inTeam:session.sessionId option:option];
        }
            break;
        case NIMSessionTypeChatroom:
        {
            info = [self userInfo:userId inChatroom:session.sessionId option:option];
        }
            break;
        default:
            NSAssert(0, @"invalid type");
            break;
    }
    
    if (!info)
    {
        if (!userId.length)
        {
            NSLog(@"warning: fetch user failed because userid is empty");
        }
        else
        {
            [self.request requestUserIds:@[userId]];
        }
        
        info = [[YPNIMKitInfo alloc] init];
        info.infoId = userId;
        info.showName = userId; //默认值
        info.avatarImage = self.defaultUserAvatar;
    }
    return info;
}



#pragma mark - P2P 用户信息
- (YPNIMKitInfo *)userInfoInP2P:(NSString *)userId
                       option:(YPNIMKitInfoFetchOption *)option
{
    NIMUser *user = [[NIMSDK sharedSDK].userManager userInfo:userId];
    NIMUserInfo *userInfo = user.userInfo;
    YPNIMKitInfo *info;
    if (userInfo)
    {
        info = [[YPNIMKitInfo alloc] init];
        info.infoId = userId;
        NSString *name = [self nickname:user
                             memberInfo:nil
                                 option:option];
        info.showName = name?:userId;
        info.avatarUrlString = userInfo.thumbAvatarUrl;
        info.avatarImage = self.defaultUserAvatar;
    }
    return info;
}


#pragma mark - 群组用户信息
- (YPNIMKitInfo *)userInfo:(NSString *)userId
                  inTeam:(NSString *)teamId
                  option:(YPNIMKitInfoFetchOption *)option
{
    NIMUser *user = [[NIMSDK sharedSDK].userManager userInfo:userId];
    NIMUserInfo *userInfo = user.userInfo;
    NIMTeamMember *member =  [[NIMSDK sharedSDK].teamManager teamMember:userId
                                                                 inTeam:teamId];
    
    YPNIMKitInfo *info;
    
    if (userInfo || member)
    {
        info = [[YPNIMKitInfo alloc] init];
        info.infoId = userId;
        
        NSString *name = [self nickname:user
                             memberInfo:member
                                 option:option];
        info.showName = name?:userId;
        info.avatarUrlString = userInfo.thumbAvatarUrl;
        info.avatarImage = self.defaultUserAvatar;
    }
    return  info;
}


#pragma mark - 聊天室用户信息
- (YPNIMKitInfo *)userInfo:(NSString *)userId
              inChatroom:(NSString *)roomId
                  option:(YPNIMKitInfoFetchOption *)option
{
    YPNIMKitInfo *info = [[YPNIMKitInfo alloc] init];
    info.infoId = userId;
    
    if ([userId isEqualToString:[NIMSDK sharedSDK].loginManager.currentAccount])
    {
        
        switch ([NIMSDK sharedSDK].loginManager.currentAuthMode) {
            case NIMSDKAuthModeChatroom:
            {
                NSAssert([YPNIMKit sharedKit].independentModeExtraInfo, @"in mode NIMSDKAuthModeChatroom , must has independentModeExtraInfo");
                info.showName        = [YPNIMKit sharedKit].independentModeExtraInfo.myChatroomNickname;
                info.avatarUrlString = [YPNIMKit sharedKit].independentModeExtraInfo.myChatroomAvatar;
            }
                break;
            case NIMSDKAuthModeIM:
            {
                NIMUser *user = [[NIMSDK sharedSDK].userManager userInfo:userId];
                info.showName        = user.userInfo.nickName;
                info.avatarUrlString = user.userInfo.thumbAvatarUrl;
            }
                break;
            default:
            {
                NSAssert(0, @"invalid mode");
            }
                break;
        }
        
    }
    else
    {
        NSAssert(option.message, @"message must has value in chatroom");
        NIMMessageChatroomExtension *ext = [option.message.messageExt isKindOfClass:[NIMMessageChatroomExtension class]] ?
        (NIMMessageChatroomExtension *)option.message.messageExt : nil;
        info.showName = ext.roomNickname;
        info.avatarUrlString = ext.roomAvatar;
    }
    info.avatarImage = self.defaultUserAvatar;
    return info;
}


//机器人
- (YPNIMKitInfo *)infoByRobot:(NSString *)userId
{
    YPNIMKitInfo *info = nil;
    if ([[NIMSDK sharedSDK].robotManager isValidRobot:userId])
    {
        NIMRobot *robot = [[NIMSDK sharedSDK].robotManager robotInfo:userId];
        if (robot)
        {
            info = [[YPNIMKitInfo alloc] init];
            info.infoId   = userId;
            info.showName = robot.nickname;
            info.avatarUrlString = robot.thumbAvatarUrl;
            info.avatarImage = self.defaultUserAvatar;
        }
    }
    return info;
}

//昵称优先级
- (NSString *)nickname:(NIMUser *)user
            memberInfo:(NIMTeamMember *)memberInfo
                option:(YPNIMKitInfoFetchOption *)option
{
    NSString *name = nil;
    do{
        if (!option.forbidaAlias && [user.alias length])
        {
            name = user.alias;
            break;
        }
        if (memberInfo && [memberInfo.nickname length])
        {
            name = memberInfo.nickname;
            break;
        }
        
        if ([user.userInfo.nickName length])
        {
            name = user.userInfo.nickName;
            break;
        }
    }while (0);
    return name;
}

#pragma mark - avatar
- (UIImage *)defaultTeamAvatar
{
    if (!_defaultTeamAvatar)
    {
        _defaultTeamAvatar = [UIImage nim_imageInKit:@"avatar_team"];
    }
    return _defaultTeamAvatar;
}

- (UIImage *)defaultUserAvatar
{
    if (!_defaultUserAvatar)
    {
        _defaultUserAvatar = [UIImage nim_imageInKit:@"avatar_user"];
    }
    return _defaultUserAvatar;
}




//将个人信息和群组信息变化通知给 YPNIMKit 。
//如果您的应用不托管个人信息给云信，则需要您自行在上层监听个人信息变动，并将变动通知给 YPNIMKit。

#pragma mark - NIMUserManagerDelegate

- (void)onFriendChanged:(NIMUser *)user
{
    [self notifyUser:user];
}

- (void)onUserInfoChanged:(NIMUser *)user
{
    [self notifyUser:user];
}

- (void)notifyUser:(NIMUser *)user
{
    if (!user)
    {
        NSLog(@"warning: notify user failed because user is empty");
    }
    else
    {
        [[YPNIMKit sharedKit] notfiyUserInfoChanged:@[user.userId]];
    }
    
}


#pragma mark - NIMTeamManagerDelegate
- (void)onTeamAdded:(NIMTeam *)team
{
    [self notifyTeamInfo:team];
}

- (void)onTeamUpdated:(NIMTeam *)team
{
    [self notifyTeamInfo:team];
}

- (void)onTeamRemoved:(NIMTeam *)team
{
    [self notifyTeamInfo:team];
}

- (void)onTeamMemberChanged:(NIMTeam *)team
{
    [self notifyTeamMember:team];
}

- (void)notifyTeamInfo:(NIMTeam *)team
{
    if (!team.teamId.length)
    {
        NSLog(@"warning: notify teamid failed because teamid is empty");
    }
    else
    {
        [[YPNIMKit sharedKit] notifyTeamInfoChanged:@[team.teamId]];
    }
}

- (void)notifyTeamMember:(NIMTeam *)team
{
    if (!team.teamId.length)
    {
        NSLog(@"warning: notify team member failed because teamid is empty");
    }
    else
    {
        [[YPNIMKit sharedKit] notifyTeamMemebersChanged:@[team.teamId]];
    }
}

#pragma mark - NIMLoginManagerDelegate
- (void)onLogin:(NIMLoginStep)step
{
    if (step == NIMLoginStepSyncOK) {
        [[YPNIMKit sharedKit] notifyTeamInfoChanged:nil];
        [[YPNIMKit sharedKit] notifyTeamMemebersChanged:nil];
    }
}



@end



