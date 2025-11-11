//
//  YYImageVIewControllerFactory.h
//  YYMobile
//
//  Created by James Pend on 14-8-27.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYStoryboardViewControllerFactory.h"


@interface YYImageViewControllerFactory : YYStoryboardViewControllerFactory

+ (instancetype)sharedFactory;

- (UIViewController *)imageViewController;


@end
