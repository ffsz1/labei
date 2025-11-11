//
//  YPYYMainHomeViewControllerFactory.m
//  YYMobile
//
//  Created by James Pend on 15/8/15.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import "YPYYMainHomeViewControllerFactory.h"

@implementation YPYYMainHomeViewControllerFactory


+ (instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
//    #define BUILD_FOR_CORE_TESTING 0
//    #if !BUILD_FOR_CORE_TESTING
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]]];
//    #else
//        //RootView
//        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"RootViewStoryboard" bundle:[NSBundle mainBundle]]];
//    #endif

    });
    return instance;
}

- (UIViewController *)instantiatetabBarViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPYYMainTabBarController"];

}

@end
