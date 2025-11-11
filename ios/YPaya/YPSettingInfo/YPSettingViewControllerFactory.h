//
//  YPSettingViewControllerFactory.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPYYStoryboardViewControllerFactory.h"

@interface YPSettingViewControllerFactory : YPYYStoryboardViewControllerFactory

+ (instancetype)sharedFactory;

- (UINavigationController *)instantiateMainNavigationController;
- (UIViewController *)instantiateSettingViewController;
- (UIViewController *)instantiateFeedbackViewController;

@end
