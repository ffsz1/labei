//
//  AppDelegate.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "HJImLoginCoreClient.h"
#import "HJTabBarController.h"
#import "HJFaceCoreClient.h"
#import "UserInfo.h"
#import "HJImRoomCoreClient.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate,HJImLoginCoreClient,HJFaceCoreClient,HJImRoomCoreClient>

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, strong) HJTabBarController *tabBarController;
@property (nonatomic, strong) UserInfo  *currentRoomOwner;//
@property (nonatomic, weak) UIView *responceView;
@end

