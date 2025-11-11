//
//  YYAlertViewController.h
//  CustomAlertView
//
//  Created by yangmengjun on 15/5/11.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class YYAlertViewController;

typedef void (^YYAlertViewControllerButtonBlock)(YYAlertViewController *controller);

@protocol YYAlertViewControllerDelegate <NSObject>

- (UIViewController *)currentVisiableRootViewController;

@end

@interface YYAlertViewController : UIViewController

@property (assign, nonatomic)id<YYAlertViewControllerDelegate> delegate;
@property (strong, nonatomic) UIColor *dialogBackgroundColor;
@property (strong, nonatomic) UIColor *separatorLineColor;

- (void)addButtonWithTitle:(NSString *)title block:(YYAlertViewControllerButtonBlock) block;

- (void)addButtonWithView:(UIView *)view block:(YYAlertViewControllerButtonBlock)block;

- (void)show;

- (void)dismiss;

- (void)dismissCompletion:(dispatch_block_t)completion;

- (void)addContentView:(UIView *)contentView;

/**
 *  设置是否监听键盘变化
 *
 *  @param isObserveKeyBoard 是否监听
 */
- (void)setIsObserveKeyBoard:(BOOL)isObserveKeyBoard;

@end
