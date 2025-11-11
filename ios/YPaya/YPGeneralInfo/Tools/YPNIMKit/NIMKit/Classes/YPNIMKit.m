//
//  YPNIMKit.m
//  YPNIMKit
//
//  Created by amao on 8/14/15.
//  Copyright (c) 2015 NetEase. All rights reserved.
//

#import "YPNIMKit.h"
#import "YPNIMKitTimerHolder.h"
#import "YPNIMKitNotificationFirer.h"
#import "YPNIMKitDataProviderImpl.h"
#import "YPNIMCellLayoutConfig.h"
#import "YPNIMKitInfoFetchOption.h"

extern NSString *const NIMKitUserInfoHasUpdatedNotification;
extern NSString *const NIMKitTeamInfoHasUpdatedNotification;


@interface YPNIMKit(){
    NSRegularExpression *_urlRegex;
}
@property (nonatomic,strong)    YPNIMKitNotificationFirer *firer;
@property (nonatomic,strong)    id<YPNIMCellLayoutConfig> layoutConfig;
@end


@implementation YPNIMKit
- (instancetype)init
{
    if (self = [super init]) {
        _resourceBundleName  = @"NIMKitResource.bundle";
        _emoticonBundleName  = @"NIMKitEmoticon.bundle";
        
        _firer = [[YPNIMKitNotificationFirer alloc] init];
        _provider = [[YPNIMKitDataProviderImpl alloc] init];   //默认使用 YPNIMKit 的实现
        
        _layoutConfig = [[YPNIMCellLayoutConfig alloc] init];
        _robotTemplateParser = [[YPNIMKitRobotDefaultTemplateParser alloc] init];
    }
    return self;
}

+ (instancetype)sharedKit
{
    static YPNIMKit *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[YPNIMKit alloc] init];
    });
    return instance;
}

- (void)registerLayoutConfig:(YPNIMCellLayoutConfig *)layoutConfig
{
    if ([layoutConfig isKindOfClass:[YPNIMCellLayoutConfig class]])
    {
        self.layoutConfig = layoutConfig;
    }
    else
    {
        NSAssert(0, @"class should be subclass of NIMLayoutConfig");
    }
}


- (id<YPNIMCellLayoutConfig>)layoutConfig
{
    return _layoutConfig;
}

- (YPNIMKitConfig *)config
{
    //不要放在 YPNIMKit 初始化里面，因为 UIConfig 初始化会使用 YPNIMKit, 防止死循环
    if (!_config)
    {
        _config = [[YPNIMKitConfig alloc] init];
        _config.cellBackgroundColor = [UIColor clearColor];
        
    }
    return _config;
}

- (void)notfiyUserInfoChanged:(NSArray *)userIds{
    if (!userIds.count) {
        return;
    }
    for (NSString *userId in userIds) {
        NIMSession *session = [NIMSession session:userId type:NIMSessionTypeP2P];
        NIMKitFirerInfo *info = [[NIMKitFirerInfo alloc] init];
        info.session = session;
        info.notificationName = NIMKitUserInfoHasUpdatedNotification;
        [self.firer addFireInfo:info];
    }
}

- (void)notifyTeamInfoChanged:(NSArray *)teamIds{
    if (teamIds.count)
    {
        for (NSString *teamId in teamIds)
        {
            [self notifyTeam:teamId];
        }
    }
    else
    {
        [self notifyTeam:nil];
    }
}

- (void)notifyTeamMemebersChanged:(NSArray *)teamIds
{
    if (teamIds.count)
    {
        for (NSString *teamId in teamIds)
        {
            [self notifyTeamMemebers:teamId];
        }
    }
    else
    {
        [self notifyTeamMemebers:nil];
    }
}


- (void)notifyTeam:(NSString *)teamId
{
    NIMKitFirerInfo *info = [[NIMKitFirerInfo alloc] init];
    if (teamId.length) {
        NIMSession *session = [NIMSession session:teamId type:NIMSessionTypeTeam];
        info.session = session;
    }
    info.notificationName = NIMKitTeamInfoHasUpdatedNotification;
    [self.firer addFireInfo:info];
}

- (void)notifyTeamMemebers:(NSString *)teamId
{
    NIMKitFirerInfo *info = [[NIMKitFirerInfo alloc] init];
    if (teamId.length) {
        NIMSession *session = [NIMSession session:teamId type:NIMSessionTypeTeam];
        info.session = session;
    }
    extern NSString *NIMKitTeamMembersHasUpdatedNotification;
    info.notificationName = NIMKitTeamMembersHasUpdatedNotification;
    [self.firer addFireInfo:info];
}

- (YPNIMKitInfo *)infoByUser:(NSString *)userId option:(YPNIMKitInfoFetchOption *)option
{
    YPNIMKitInfo *info = nil;
    if (self.provider && [self.provider respondsToSelector:@selector(infoByUser:option:)]) {
        info = [self.provider infoByUser:userId option:option];
    }
    return info;
}

- (YPNIMKitInfo *)infoByTeam:(NSString *)teamId option:(YPNIMKitInfoFetchOption *)option
{
    YPNIMKitInfo *info = nil;
    if (self.provider && [self.provider respondsToSelector:@selector(infoByTeam:option:)]) {
        info = [self.provider infoByTeam:teamId option:option];
    }
    return info;

}


@end



