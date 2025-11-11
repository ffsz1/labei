//
//  YPRoomMessageViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPSessionListViewController.h"

@interface YPRoomMessageViewController : UIViewController

@property (nonatomic, strong) UIWindow *messageListWindow;

@property (nonatomic, copy) void(^roomMessageListDidSelectCell)(NIMRecentSession *recent, NSString *title);
@property (nonatomic, copy) void(^addBadgeBlock)();
@property (nonatomic, copy) void(^didCloseBlock)();
@property (nonatomic, copy) void(^squareBlock)();


@end
