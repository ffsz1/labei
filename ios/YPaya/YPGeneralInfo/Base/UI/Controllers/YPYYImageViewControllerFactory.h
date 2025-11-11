//
//  YYImageVIewControllerFactory.h
//  YYMobile
//
//  Created by James Pend on 14-8-27.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YPYYStoryboardViewControllerFactory.h"


@interface YPYYImageViewControllerFactory : YPYYStoryboardViewControllerFactory

+ (instancetype)sharedFactory;

- (UIViewController *)imageViewController;


@end
