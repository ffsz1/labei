//
//  YYViewControllerCenter.m
//  YYMobile
//
//  Created by zhenby on 7/9/14.
//  Copyright (c) 2014 YY.inc. All rights reserved.
//

/**
 *  用于方便获取应用运行状态下，常用的ViewController对象，以及ViewController的跳转
 */
#import "YYViewControllerCenter.h"
#import "AppDelegate.h"
//#import "RESideMenu.h"
#import "YYActionSheetViewController.h"
#import "TYAlertController.h"
#import "HJBaseNavigationController.h"

@implementation YYViewControllerCenter


//获取到当前所在的视图
+ (UIViewController *)currentViewController{
    UIWindow * window = [[UIApplication sharedApplication] keyWindow];
    if (window.windowLevel != UIWindowLevelNormal){
        NSArray *windows = [[UIApplication sharedApplication] windows];
        for(UIWindow * tmpWin in windows){
            if (tmpWin.windowLevel == UIWindowLevelNormal){
                window = tmpWin;
                break;
            }
        }
    }
    UIViewController *result = window.rootViewController;
    while (result.presentedViewController) {
        result = result.presentedViewController;
    }

    if ([result isKindOfClass:[HJBaseNavigationController class]]) {
        result = [(HJBaseNavigationController *)result topViewController];
    }
    
    return result;
}


+ (UIViewController*) currentVisiableRootViewController
{
    __block UIViewController *result = nil;
    // Try to find the root view controller programmically
    // Find the top window (that is not an alert view or other window)
    UIWindow *topWindow = [[UIApplication sharedApplication] keyWindow];
    if (topWindow.windowLevel != UIWindowLevelNormal)
    {
        NSArray *windows = [[UIApplication sharedApplication] windows];
        for(topWindow in windows)
        {
            if (topWindow.windowLevel == UIWindowLevelNormal)
                break;
        }
    }

    NSArray *windowSubviews = [topWindow subviews];

    [windowSubviews enumerateObjectsWithOptions:NSEnumerationReverse usingBlock:
     ^(id obj, NSUInteger idx, BOOL *stop) {
         UIView *rootView = obj;

         if ([NSStringFromClass([rootView class]) isEqualToString:@"UITransitionView"]) {

             NSArray *aSubViews = rootView.subviews;

             [aSubViews enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
                 UIView *tempView = obj;

                 id nextResponder = [tempView nextResponder];

                 if ([nextResponder isKindOfClass:[UIViewController class]]) {
                     result = nextResponder;
                     *stop = YES;
                 }
             }];
             *stop = YES;
         } else {

             id nextResponder = [rootView nextResponder];

             if ([nextResponder isKindOfClass:[UIViewController class]]) {
                 result = nextResponder;
                 *stop = YES;
             }
         }
     }];


    return result;
}
@end
