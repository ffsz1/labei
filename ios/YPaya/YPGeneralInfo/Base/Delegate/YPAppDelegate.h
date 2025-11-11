//
//  YPAppDelegate.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "HJImLoginCoreClient.h"
#import "YPTabBarController.h"
#import "HJFaceCoreClient.h"
#import "UserInfo.h"
#import "HJImRoomCoreClient.h"

@interface YPAppDelegate : UIResponder <UIApplicationDelegate,HJImLoginCoreClient,HJFaceCoreClient,HJImRoomCoreClient>

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, strong) YPTabBarController *tabBarController;
@property (nonatomic, strong) UserInfo  *currentRoomOwner;//
@property (nonatomic, weak) UIView *responceView;
@end

