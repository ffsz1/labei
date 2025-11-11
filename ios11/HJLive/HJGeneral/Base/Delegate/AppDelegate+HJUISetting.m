//
//  AppDelegate+HJUISetting.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "AppDelegate+HJUISetting.h"
#import "HJImMessageCore.h"
#import "CoreManager.h"
#import "HJRoomCoreV2Help.h"

#import "HJImRoomCoreV2.h"
#import "HJRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"
#import "HJRoomViewControllerCenter.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "HJImRoomCoreClient.h"
@implementation AppDelegate (HJUISetting)

- (void)setTabBar{
    HJTabBarController *tabBarController = [[HJTabBarController alloc] init];
    self.tabBarController = tabBarController;
    [self.window setRootViewController:self.tabBarController];
    
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    AddCoreClient(HJImRoomCoreClient, self);
    @weakify(self);

    [self.tabBarController setSelectedIndex:0];
}


- (void)addMessageBadge {
    if ([GetCore(HJImMessageCore) getUnreadCount] > 0) {
        
        [self.tabBarController showBadgeOnItemIndex:1 num:[GetCore(HJImMessageCore)getUnreadCount]];
    }
}

#pragma mark - ImLoginCoreClient
- (void)onImSyncSuccess {
    [self addMessageBadge];
}

@end
