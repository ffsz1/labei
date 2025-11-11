//
//  YPNotificationCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPNotificationCore.h"
#import "HJNotificationCoreClient.h"
#import <NIMSDK/NIMSDK.h>
@interface YPNotificationCore()<NIMSystemNotificationManagerDelegate>
@end

@implementation YPNotificationCore
- (instancetype)init
{
    self = [super init];
    if (self) {
        [[NIMSDK sharedSDK].systemNotificationManager addDelegate:self];
    }
    return self;
}

- (void)dealloc
{
    [[NIMSDK sharedSDK].systemNotificationManager removeDelegate:self];
}

#pragma mark -NIMSystemNotificationManagerDelegate
- (void)onReceiveSystemNotification:(NIMSystemNotification *)notification
{
    if (notification.type == NIMSystemNotificationTypeTeamApply) {
        NotifyCoreClient(HJNotificationCoreClient, @selector(onRecvTeamApplyNoti:), onRecvTeamApplyNoti:notification);
    } else if (notification.type == NIMSystemNotificationTypeTeamApplyReject) {
        NotifyCoreClient(HJNotificationCoreClient, @selector(onRecvTeamApplyRejectNoti:), onRecvTeamApplyRejectNoti:notification);
    } else if (notification.type == NIMSystemNotificationTypeTeamInvite) {
        NotifyCoreClient(HJNotificationCoreClient, @selector(onRecvTeamInviteNoti:), onRecvTeamInviteNoti:notification);
    } else if (notification.type == NIMSystemNotificationTypeTeamIviteReject) {
        NotifyCoreClient(HJNotificationCoreClient, @selector(onRecvTeamIviteRejectNoti:), onRecvTeamIviteRejectNoti:notification);
    } else if (notification.type == NIMSystemNotificationTypeFriendAdd) {
        NotifyCoreClient(HJNotificationCoreClient, @selector(onRecvFriendAddNoti:), onRecvFriendAddNoti:notification);
    }
}

- (void)onReceiveCustomSystemNotification:(NIMCustomSystemNotification *)notification
{
    if (notification.receiverType == NIMSessionTypeP2P) {
        NotifyCoreClient(HJNotificationCoreClient, @selector(onRecvCustomP2PNoti:), onRecvCustomP2PNoti:notification);
    } else if (notification.receiverType == NIMSessionTypeTeam) {
        NotifyCoreClient(HJNotificationCoreClient, @selector(onRecvCustomTeamNoti:), onRecvCustomTeamNoti:notification);
    } else if (notification.receiverType == NIMSessionTypeChatroom) {
        NotifyCoreClient(HJNotificationCoreClient, @selector(onRecvCustomChatRoomNoti:), onRecvCustomChatRoomNoti:notification);
    }
}
@end
