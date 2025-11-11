//
//  YPAboutViewControllerFactory.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPYYStoryboardViewControllerFactory.h"

@interface YPAboutViewControllerFactory : YPYYStoryboardViewControllerFactory

+ (instancetype)sharedFactory;

- (UIViewController *)instantiateAboutController;

@end
