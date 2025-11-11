//
//  YPYYActionSheetViewController.h
//  YYMobile
//
//  Created by yangmengjun on 15/5/14.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class YPYYActionSheetViewController;

typedef NS_ENUM(NSInteger, YYActionSheetButtonType){
    YYActionSheetButtonTypeDefault,
    YYActionSheetButtonTypeRed
};

typedef void (^YYActionSheetViewControllerButtonBlock)(YPYYActionSheetViewController *controller);

@interface YPYYActionSheetViewController : UIViewController

@property (strong, nonatomic) UIColor *dialogBackgroundColor;
@property (strong, nonatomic) UIColor *separatorLineColor;

- (void)addTitleText:(NSString *)title;

- (void)addTitleView:(UIView *)view;

- (void)addButtonWithTitle:(NSString *)title block:(YYActionSheetViewControllerButtonBlock) block;

- (void)addButtonWithTitle:(NSString *)title buttonType:(YYActionSheetButtonType)buttonType block:(YYActionSheetViewControllerButtonBlock) block;

- (void)addButtonWithView:(UIView *)view block:(YYActionSheetViewControllerButtonBlock)block;

- (void)addCancelButtonWithTitle:(NSString *)title block:(YYActionSheetViewControllerButtonBlock) block;

- (void)addCancelButtonWithView:(UIView *)view block:(YYActionSheetViewControllerButtonBlock) block;

- (void)show;

- (void)show:(BOOL)needBg;

- (void)dismiss;

@end
