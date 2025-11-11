//
//  YPYYMainTabBarController.h
//  YYMobile
//
//  Created by wuwei on 14/6/18.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, MainTabIndex)
{
    KMainTabIndex_Reserver ,
    kMainTabIndex_TinyVideo,
    kMainTabIndex_Discover,
    kMainTabIndex_Counts
};

extern NSString * const kDiscoverTabClicked;

@interface YPYYMainTabBarController : UITabBarController

@property (nonatomic, weak, readonly) UINavigationController *selectedNavigationController;

- (void) closeWelcome;


@end
