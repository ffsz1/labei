//
//  YYNavigationPopAnimator.m
//  YYMobile
//
//  Created by wuwei on 14/7/9.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYNavigationPopAnimator.h"

@implementation YYNavigationPopAnimator

- (NSTimeInterval)transitionDuration:(id<UIViewControllerContextTransitioning>)transitionContext
{
    return 0.333f;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext
{
    UIViewController *toViewController = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
    UIViewController *fromViewController = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
    [[transitionContext containerView] insertSubview:toViewController.view belowSubview:fromViewController.view];
    
    if ([transitionContext isInteractive]) {
//        CGRect rc = [transitionContext initialFrameForViewController:toViewController];
//        CGRect rc2 = [transitionContext finalFrameForViewController:toViewController];
//        
//        CGRect rc3 = [transitionContext initialFrameForViewController:fromViewController];
//        CGRect rc4 = [transitionContext finalFrameForViewController:toViewController];
//        NSLog(@"%@ %@ %@ %@", NSStringFromCGRect(rc), NSStringFromCGRect(rc2), NSStringFromCGRect(rc3), NSStringFromCGRect(rc4));
//        [fromViewController.tabBarController.tabBar setHidden:YES];
//        [toViewController.tabBarController.tabBar setFrame:CGRectMake(0, -49, 320, 49)];
//        toViewController.tabBarController.tabBar.y += 49;
//        toViewController.tabBarController.tabBar.x = -320;
        [UIView animateWithDuration:[self transitionDuration:transitionContext] delay:0 options:UIViewAnimationOptionCurveLinear animations:^{
            CGRect fromRect = fromViewController.view.frame;
            fromRect.origin.x += fromRect.size.width;
            fromViewController.view.frame = fromRect;
            
//            [toViewController.tabBarController.tabBar setFrame:CGRectMake(0, 0, 320, 49)];
        } completion:^(BOOL finished) {
            [transitionContext completeTransition:![transitionContext transitionWasCancelled]];
//            [toViewController.tabBarController.tabBar setHidden:NO];
        }];
    }
    else
    {
        __block CGRect toRect = [transitionContext finalFrameForViewController:toViewController];
        CGFloat originX = toRect.origin.x;
        toRect.origin.x = -toRect.size.width / 3;
        toViewController.view.frame = toRect;
        
        [UIView animateWithDuration:[self transitionDuration:transitionContext] delay:0 options:UIViewAnimationOptionCurveEaseInOut animations:^{
            [[[fromViewController.navigationController.navigationBar subviews] firstObject] setAlpha:1.0f];
            
            CGRect fromRect = fromViewController.view.frame;
            fromRect.origin.x += fromRect.size.width;
            fromViewController.view.frame = fromRect;
            
            toRect.origin.x = originX;
            toViewController.view.frame = toRect;
        } completion:^(BOOL finished) {
            [transitionContext completeTransition:![transitionContext transitionWasCancelled]];
        }];
    }
}

@end
