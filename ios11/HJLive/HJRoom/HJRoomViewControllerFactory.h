//
//  HJRoomViewControllerFactory.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YYStoryboardViewControllerFactory.h"

@interface HJRoomViewControllerFactory : YYStoryboardViewControllerFactory
+ (instancetype)sharedFactory;
- (UINavigationController *)instantiateRoomNavigationController;//房间导航控制器
- (UIViewController *)instantiateOpenRoomViewController;
- (UIViewController *)instantiateGameRoomContainerViewController; //轰趴房
- (UIViewController *)instantiateGameRoomSettingViewController;
- (UIViewController *)instantiateManagerSettingViewController;
- (UIViewController *)instantiateOnlineListViewController;//在线列表，不包含麦上的人
- (UIViewController *)instantiateGameHJRoomSettingTopicVC;

- (UIViewController *)instantiateHJRoomSettingPlayInfoVC;
@end
