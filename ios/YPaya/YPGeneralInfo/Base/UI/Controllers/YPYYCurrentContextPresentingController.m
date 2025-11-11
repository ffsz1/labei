//
//  YPYYCurrentContextPresentingController.m
//  YYMobile
//
//  Created by jianglinjie on 14-7-30.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YPYYCurrentContextPresentingController.h"

#import "YYUtility.h"

@interface YPYYCurrentContextPresentingController()

@end

@implementation YPYYCurrentContextPresentingController
+ (instancetype)shareController
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

- (void)currentContextPresentWithSourceViewController:(UIViewController *)sourceViewController
                            destinationViewController:(UIViewController *)destinationViewController
                            animated:(BOOL) animated
                            completion:(dispatch_block_t)completion
{
    
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 80000
    
    if ([[YYUtility systemVersion] compare:@"8.0"] == NSOrderedAscending) {
        // ios 7
        sourceViewController.modalPresentationStyle = UIModalPresentationCurrentContext;
    } else {
        // ios 8
        destinationViewController.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    }
    
#else 
    
    // xcode5 ios7
    sourceViewController.modalPresentationStyle = UIModalPresentationCurrentContext;
    
#endif
    
    [sourceViewController presentViewController:destinationViewController animated:animated completion:^{
        
        if (completion) {
            completion();
        }
        
        sourceViewController.modalPresentationStyle = UIModalPresentationFullScreen;
    }];
    
    UIViewController *vc = destinationViewController;
    
    if ([vc isKindOfClass:[UINavigationController class]]) {
        vc = [((UINavigationController *)vc) topViewController];
    }
    
    if ([vc conformsToProtocol:@protocol(YYCurrentContextPresentingControllerDelegate)]) {
        self.currentContextPresentedViewController =
        (id<YYCurrentContextPresentingControllerDelegate>)vc;
    }
}

- (void)dismissCurrentContextViewController:(BOOL)animated
{

    if (self.currentContextPresentedViewController && [self.currentContextPresentedViewController respondsToSelector:@selector(currentContextPresentingNeedDismissing:)]) {
        [self.currentContextPresentedViewController currentContextPresentingNeedDismissing:animated];
        self.currentContextPresentedViewController = nil;
    }
}



@end
