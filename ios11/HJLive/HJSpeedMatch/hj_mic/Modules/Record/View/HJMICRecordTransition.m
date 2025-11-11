//
//  HJMICRecordTransition.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMICRecordTransition.h"
#import "HJMICRecordVC.h"
#import "HJMICRecordVC+Private.h"

#import <Masonry.h>

static const NSTimeInterval kMICRecordTransitionDuration = 0.3f;

@implementation HJMICRecordTransition

+ (instancetype)animatedTransitionWithStyle:(HJMICRecordTransitionStyle)style {
    HJMICRecordTransition *transition = [[self alloc] init];
    transition.animationStyle = style;
    return transition;
}

- (NSTimeInterval)transitionDuration:(id<UIViewControllerContextTransitioning>)transitionContext {
    return kMICRecordTransitionDuration;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
    UIView *containerView = [transitionContext containerView];
    if (self.animationStyle == HJMICRecordTransitionStylePresenting) {
        UIViewController *toViewController = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
        if ([toViewController isKindOfClass:[HJMICRecordVC class]]) {
            HJMICRecordVC *actionSheetViewController = (HJMICRecordVC *)toViewController;
            [containerView addSubview:actionSheetViewController.backgroundView];
            [containerView addSubview:actionSheetViewController.view];
            // Initial Constraint
            [actionSheetViewController.backgroundView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.left.right.top.bottom.equalTo(containerView);
            }];
            
            __weak typeof(actionSheetViewController) weakActionSheetViewController = actionSheetViewController;
            [actionSheetViewController.view mas_makeConstraints:^(MASConstraintMaker *make) {
                make.left.right.equalTo(containerView);
                make.height.equalTo(containerView);
                weakActionSheetViewController.viewTopConstraint = make.top.equalTo(containerView.mas_bottom);
            }];
            
            actionSheetViewController.backgroundView.alpha = 0;
            
            [containerView setNeedsUpdateConstraints];
            [containerView layoutIfNeeded];
            // Animated Constraint
            [actionSheetViewController.viewTopConstraint uninstall];
            [actionSheetViewController.view mas_makeConstraints:^(MASConstraintMaker *make) {
                weakActionSheetViewController.viewTopConstraint = make.top.equalTo(containerView.mas_top);
            }];
            [containerView setNeedsUpdateConstraints];
            
            [UIView animateWithDuration:kMICRecordTransitionDuration delay:0 options:UIViewAnimationOptionBeginFromCurrentState animations:^{
                
                [containerView layoutIfNeeded];
                actionSheetViewController.backgroundView.alpha = 1;
                
            } completion:^(BOOL finished) {
                [transitionContext completeTransition:YES];
            }];
        }
    } else if (self.animationStyle == HJMICRecordTransitionStyleDismissing) {
        UIViewController *fromViewController = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
        if ([fromViewController isKindOfClass:[HJMICRecordVC class]]) {
            HJMICRecordVC *actionSheetViewController = (HJMICRecordVC *)fromViewController;
            // Animated Constraint
            __weak typeof(actionSheetViewController) weakActionSheetViewController = actionSheetViewController;
            [actionSheetViewController.viewTopConstraint uninstall];
            [actionSheetViewController.view mas_makeConstraints:^(MASConstraintMaker *make) {
                weakActionSheetViewController.viewTopConstraint = make.top.equalTo(containerView.mas_bottom);
            }];
            [containerView setNeedsUpdateConstraints];
            
            [UIView animateWithDuration:kMICRecordTransitionDuration delay:0 options:UIViewAnimationOptionBeginFromCurrentState animations:^{
                
                [containerView layoutIfNeeded];
                actionSheetViewController.backgroundView.alpha = 0;
                
            } completion:^(BOOL finished) {
                [actionSheetViewController.view removeFromSuperview];
                [actionSheetViewController.backgroundView removeFromSuperview];
                
                [transitionContext completeTransition:YES];
            }];
        }
    }
}

@end
