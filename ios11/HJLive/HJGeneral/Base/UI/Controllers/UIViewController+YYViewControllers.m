//
//  UIViewController+YYViewControllers.m
//  YYMobile
//
//  Created by wuwei on 14/7/4.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "UIViewController+YYViewControllers.h"

#import "YYNavigationController.h"
#import "FBKVOController.h"

#import "YYViewControllerClient.h"

#import <objc/runtime.h>

@interface UIViewController (YYViewControllersPrivate)

@property (nonatomic, strong) FBKVOController *navigationKVOController;

@end

@implementation UIViewController (YYViewControllers)

- (void)safePushViewController:(UIViewController *)vc animated:(BOOL)animated {
    
    if (!vc) {
        return;
    }
    
    if ([self isKindOfClass:[UINavigationController class]]) {
        
        [(UINavigationController *)self pushViewController:vc animated:animated];
        
        return;
    }
    
    BOOL shouldPush = NO;
    __weak UIViewController *tempController = self;
    do {
        if (tempController == tempController.navigationController.topViewController) {
            shouldPush = YES;
            
            break;
        } else {
            tempController = tempController.parentViewController;
        }
        
    }while (tempController.parentViewController);
    
    if (shouldPush) {
        [self.navigationController pushViewController:vc animated:animated];
    }
}

- (void)yy_viewWillDisappear {

}

- (void)yy_viewDidAppear {
    self.navigationController.navigationBarHidden = [self preferredNavigationBarHidden];
}

- (void)yy_viewDidLoad
{
    if (self.navigationKVOController == nil) {
        self.navigationKVOController = [[FBKVOController alloc] initWithObserver:self retainObserved:NO];
    }
    
    [self yy_updateRightBarButtonItems];
    
    __weak __typeof__(self) wself = self;
    [self.navigationKVOController observe:self.navigationItem keyPath:@"rightBarButtonItem" options:NSKeyValueObservingOptionNew block:^(id observer, id object, NSDictionary *change) {
        [wself yy_updateRightBarButtonItems];
    }];
    
    [self.navigationKVOController observe:self.navigationItem keyPath:@"rightBarButtonItems" options:NSKeyValueObservingOptionNew block:^(id observer, id object, NSDictionary *change) {
        [wself yy_updateRightBarButtonItems];
    }];
    
    self.navigationItem.backBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"" style:UIBarButtonItemStylePlain target:nil action:nil];
    
    
    [UINavigationBar appearance].backIndicatorTransitionMaskImage = [UIImage imageNamed:@"hj_nav_bar_back"];
    [UINavigationBar appearance].backIndicatorImage = [UIImage imageNamed:@"hj_nav_bar_back"];
    
}

- (void)yy_dealloc
{
    [self.navigationKVOController unobserveAll];
}

- (UIStatusBarStyle)yy_preferredStatusBarStyle
{
    
    return UIStatusBarStyleLightContent;
    
//    if ([self preferredNavigationBarHidden]) {
//        return UIStatusBarStyleLightContent;
//    }
//    
//    CGRect temp = [[UIApplication sharedApplication] statusBarFrame];
//    if (temp.size.height == 0) {
//        CGRect rect = self.navigationController.navigationBar.frame;
//        rect.origin.y = 0;
//        rect.size.height = 64;
//        self.navigationController.navigationBar.frame = rect;
//    } else {
//        
//        
//        CGRect rect = self.navigationController.navigationBar.frame;
//        rect.origin.y = 0;
//        rect.size.height = 64;
//        self.navigationController.navigationBar.frame = rect;
//    }
//    
//    return UIStatusBarStyleLightContent;
}

- (BOOL)needAlertDissmiss {
    return NO;
}

- (void)yy_updateRightBarButtonItems
{

}

- (YYNavigationController *)yy_navigationController
{
    return [self.navigationController isKindOfClass:[YYNavigationController class]] ? (YYNavigationController *)self.navigationController : nil;
}


@end

@implementation UIViewController (YYViewControllersPrivate)

@dynamic navigationKVOController;

static const char kNavigationKVOControllerKey;

- (void)setNavigationKVOController:(FBKVOController *)navigationKVOController
{
    objc_setAssociatedObject(self, &kNavigationKVOControllerKey, navigationKVOController, OBJC_ASSOCIATION_RETAIN);
}

- (FBKVOController *)navigationKVOController
{
    return objc_getAssociatedObject(self, &kNavigationKVOControllerKey);
}

@end

@implementation UIViewController (ViewControllerBasedNavigationBarAppearance)

- (BOOL)preferredNavigationBarHidden
{
    return NO;
}

- (BOOL)preferredNavigationBarTranslucent
{
    return YES;
}

- (UIImage *)preferredNavigationBarBackgroundImageForBarMetrics:(UIBarMetrics)barMetrics
{
    return nil;
}

- (UIColor *)preferredNavigationBarBackgroundColor
{
    return [UIColor clearColor];
}

- (UIImage *)preferredNavigationBarShadowImage
{
    return nil;
}

@end
