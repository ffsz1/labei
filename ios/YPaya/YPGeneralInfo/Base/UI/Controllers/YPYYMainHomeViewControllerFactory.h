//
//  YPYYMainHomeViewControllerFactory.h
//  YYMobile
//
//  Created by James Pend on 15/8/15.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import "YPYYStoryboardViewControllerFactory.h"

@interface YPYYMainHomeViewControllerFactory : YPYYStoryboardViewControllerFactory

+ (instancetype)sharedFactory;

- (UIViewController *)instantiatetabBarViewController;


@end
