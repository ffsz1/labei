//
//  YPAppDelegate+HJUISetting.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAppDelegate+YPUISetting.h"
#import "YPImMessageCore.h"
#import "YPCoreManager.h"
#import "YPRoomCoreV2Help.h"

#import "YPImRoomCoreV2.h"
#import "HJRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"
#import "YPRoomViewControllerCenter.h"
#import "YPAuthCoreHelp.h"
#import "YPUserCoreHelp.h"
#import "HJImRoomCoreClient.h"
@implementation YPAppDelegate (YPUISetting)

- (void)setTabBar{
    YPTabBarController *tabBarController = [[YPTabBarController alloc] init];
    self.tabBarController = tabBarController;
    [self.window setRootViewController:self.tabBarController];
    
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    AddCoreClient(HJImRoomCoreClient, self);
    @weakify(self);

    [self.tabBarController setSelectedIndex:0];
}


- (void)addMessageBadge {
    if ([GetCore(YPImMessageCore) getUnreadCount] > 0) {
        
        [self.tabBarController showBadgeOnItemIndex:2 num:[GetCore(YPImMessageCore)getUnreadCount]];
    }
}

#pragma mark - ImLoginCoreClient
- (void)onImSyncSuccess {
    [self addMessageBadge];
}

@end
