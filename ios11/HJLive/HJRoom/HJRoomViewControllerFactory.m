//
//  HJRoomViewControllerFactory.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomViewControllerFactory.h"

@implementation HJRoomViewControllerFactory
+(instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"HJRoom" bundle:[NSBundle mainBundle]]];
    });
    return instance;
}

- (UINavigationController *)instantiateRoomNavigationController
{
    return self.storyboard.instantiateInitialViewController;
}

- (UIViewController *)instantiateOpenRoomViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJOpenRoomViewController"];
}



- (UIViewController *)instantiateGameRoomContainerViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJGameRoomContainerVC"];
}

- (UIViewController *)instantiateGameRoomSettingViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJRoomSettingVC"];
}

- (UIViewController *)instantiateGameHJRoomSettingTopicVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJRoomSettingTopicVC"];
}





- (UIViewController *)instantiateManagerSettingViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJManagerSettingController"];
}

- (UIViewController *)instantiateOnlineListViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJOnlineListVC"];
}

- (UIViewController *)instantiateHJRoomSettingPlayInfoVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJRoomSettingPlayInfoVC"];
}

@end
