//
//  HJRoomMessageTableViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "HJSessionViewController.h"

@interface HJRoomMessageTableViewController : UIViewController

@property (nonatomic, strong) UIWindow *messageListWindow;
@property (nonatomic, strong) UIWindow *messageWindow;
@property (nonatomic, copy) NSString *topTitle;
@property (nonatomic, strong) NIMRecentSession *recentSession;

@property (nonatomic, copy) void(^addBadgeBlock)();
@property (nonatomic, copy) UINavigationController *(^roomMessageListDidSelectCell)();

@property (nonatomic, copy) void(^didCloseBlock)();
@property (nonatomic, copy) void(^didCloseAllBlock)();

@end
