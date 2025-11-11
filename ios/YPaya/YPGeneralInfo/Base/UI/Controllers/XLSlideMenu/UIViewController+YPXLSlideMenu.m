//
//  UIViewController+YPXLSlideMenu.m
//  XLSlideMenuExample
//
//  Created by MengXianLiang on 2017/5/8.
//  Copyright © 2017年 MengXianLiang. All rights reserved.
//  GitHub:https://github.com/mengxianliang/YPXLSlideMenu

#import "UIViewController+YPXLSlideMenu.h"
#import "YPXLSlideMenu.h"

@implementation UIViewController (YPXLSlideMenu)

- (YPXLSlideMenu *)xl_sldeMenu {
    UIViewController *sldeMenu = self.parentViewController;
    while (sldeMenu) {
        if ([sldeMenu isKindOfClass:[YPXLSlideMenu class]]) {
            return (YPXLSlideMenu *)sldeMenu;
        } else if (sldeMenu.parentViewController && sldeMenu.parentViewController != sldeMenu) {
            sldeMenu = sldeMenu.parentViewController;
        } else {
            sldeMenu = nil;
        }
    }
    return nil;
}


@end
