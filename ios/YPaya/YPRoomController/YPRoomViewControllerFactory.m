//
//  YPRoomViewControllerFactory.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomViewControllerFactory.h"

@implementation YPRoomViewControllerFactory
+(instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"YPRoom" bundle:[NSBundle mainBundle]]];
    });
    return instance;
}

- (UINavigationController *)instantiateRoomNavigationController
{
    return self.storyboard.instantiateInitialViewController;
}

- (UIViewController *)instantiateOpenRoomViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPOpenRoomViewController"];
}



- (UIViewController *)instantiateGameRoomContainerViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPGameRoomContainerVC"];
}

- (UIViewController *)instantiateGameRoomSettingViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPRoomSettingVC"];
}

- (UIViewController *)instantiateGameHJRoomSettingTopicVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPRoomSettingTopicVC"];
}





- (UIViewController *)instantiateManagerSettingViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPManagerSettingController"];
}

- (UIViewController *)instantiateOnlineListViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPOnlineListVC"];
}

- (UIViewController *)instantiateHJRoomSettingPlayInfoVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPRoomSettingPlayInfoVC"];
}

@end
