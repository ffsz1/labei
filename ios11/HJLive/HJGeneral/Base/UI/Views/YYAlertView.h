//
//  YYAlertView.h
//  YYMobile
//
//  Created by wuwei on 13-10-29.
//  Copyright (c) 2013年 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

extern NSString *const YYAlertViewDismissAllAlertsNotification;
extern NSString *const YYAlertViewAnimatedKey;

@interface YYAlertView : NSObject

@property(nonatomic, assign) UIAlertViewStyle alertViewStyle;
@property(copy) dispatch_block_t dismissAction;

- (instancetype)initWithTitle:(NSString *)title message:(NSString *)message;

- (void)addButtonWithTitle:(NSString *)title block:(dispatch_block_t) block;

- (void)show;
//- (void)showInViewController:(UIViewController *)viewController NS_CLASS_AVAILABLE_IOS(8_0);
- (void)dismissAnimated:(BOOL)animated;

- (void)showWithTimeInterval:(CGFloat)timeinterval timeout:(dispatch_block_t)timeoutblock;
- (void)clearTimer;

- (UITextField *)textFieldAtIndex:(NSInteger)textFieldIndex;

@end
