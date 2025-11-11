//
//  HJSettingViewControllerFactory.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSettingViewControllerFactory.h"

@implementation HJSettingViewControllerFactory

+ (instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"HJSetting" bundle:[NSBundle mainBundle]]];
    });
    return instance;
}

- (UINavigationController *)instantiateMainNavigationController
{
    return self.storyboard.instantiateInitialViewController;
}

- (UIViewController *)instantiateSettingViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJSettingViewController"];
}

- (UIViewController *)instantiateFeedbackViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJFeedbackViewController"];
}


@end
