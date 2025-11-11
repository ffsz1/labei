//
//  UIView+getNavigationController.m
//  HJLive
//
//  Created by FF on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "UIView+getNavigationController.h"

@implementation UIView (getNavigationController)


- (UINavigationController *)getNavigationController{
    
    UIViewController *result = nil;
    UIWindow * window = [[UIApplication sharedApplication] keyWindow];
    
    if (window.windowLevel != UIWindowLevelNormal)    {
        NSArray *windows = [[UIApplication sharedApplication] windows];
        for(UIWindow * tmpWin in windows){
            if (tmpWin.windowLevel == UIWindowLevelNormal){
                window = tmpWin;
                break;
            }
        }
    }
    id  nextResponder = nil;
    UIViewController *appRootVC=window.rootViewController;
    
    if (appRootVC.presentedViewController) {
        nextResponder = appRootVC.presentedViewController;
        
    }else{
        UIView *frontView = [[window subviews] objectAtIndex:0];        nextResponder = [frontView nextResponder];
    }
    
    
    if ([nextResponder isKindOfClass:[UITabBarController class]]){
            UITabBarController * tabbar = (UITabBarController *)nextResponder;        UINavigationController * nav = (UINavigationController *)tabbar.viewControllers[tabbar.selectedIndex];
            
            result=nav.childViewControllers.lastObject;
            
        }else if ([nextResponder isKindOfClass:[UINavigationController class]]){
            UIViewController * nav = (UIViewController *)nextResponder;
            result = nav.childViewControllers.lastObject;
            
        }else{
            result = nextResponder;
            
        }
    return result == nil? nil: result.navigationController;
}
@end
