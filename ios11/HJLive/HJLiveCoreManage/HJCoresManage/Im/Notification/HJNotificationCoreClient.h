//
//  HJNotificationCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
@protocol HJNotificationCoreClient <NSObject>
@optional
- (void)onRecvTeamApplyNoti:(NIMSystemNotification *)notification;
- (void)onRecvTeamApplyRejectNoti:(NIMSystemNotification *)notification;
- (void)onRecvTeamInviteNoti:(NIMSystemNotification *)notification;
- (void)onRecvTeamIviteRejectNoti:(NIMSystemNotification *)notification;
- (void)onRecvFriendAddNoti:(NIMSystemNotification *)notification;

- (void)onRecvCustomChatRoomNoti:(NIMCustomSystemNotification *)notification;
- (void)onRecvCustomP2PNoti:(NIMCustomSystemNotification *)notification;
- (void)onRecvCustomTeamNoti:(NIMCustomSystemNotification *)notification;
@end
