//
//  YYAlertView.m
//  YYMobile
//
//  Created by wuwei on 13-10-29.
//  Copyright (c) 2013年 YY Inc. All rights reserved.
//

#import "YYAlertView.h"

#import "YYViewControllerCenter.h"
#import "YYUtility.h"

#define YYALERTSWITCH 0

NSString *const YYAlertViewDismissAllAlertsNotification = @"YYAlertViewDismissAllAlertsNotification";
NSString *const YYAlertViewAnimatedKey = @"YYAlertViewAnimated";

@interface YYAlertView () <UIAlertViewDelegate>

@property(strong) UIAlertView *alert;
@property(strong) NSMutableArray *blocks;
@property(strong) id keepInMemory;

@property(strong) UIAlertController *alertController;

@property (nonatomic,strong) NSTimer* timerCallback;
@property (nonatomic,strong) dispatch_block_t timeoutCallbackBlock;

@end

@implementation YYAlertView

@synthesize alert, blocks, dismissAction, keepInMemory, alertController;

- (id)initWithTitle:(NSString *)title message:(NSString *)message
{
    self = [super init];
    if (![self isShowAlertController]) {
        // ios 7
        alert = [[UIAlertView alloc] initWithTitle:title
                                           message:message
                                          delegate:self
                                 cancelButtonTitle:nil
                                 otherButtonTitles:nil];
    } else {
        // ios 8
        alertController = [UIAlertController alertControllerWithTitle:title
                                                              message:message
                                                       preferredStyle:UIAlertControllerStyleAlert];
    }
    
    blocks = [[NSMutableArray alloc] init];
    return self;
}

- (void) dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (BOOL)isShowAlertController {
    
//    // alertController 有问题先用alertView
    return NO;
    
//    if ([[YYUtility systemVersion] compare:@"8.0"] == NSOrderedAscending) {
        // ios 7
//        return NO;
//    } else {
        // ios 8
//        return YES;
//    }
}

- (void)presentAlertControllerWithController:(UIViewController *)controller {
    
    [controller presentViewController:alertController animated:YES completion:nil];
}

- (void)showAlertController {
    
    [self setKeepInMemory:self];
    
    UIViewController *current = [YYViewControllerCenter currentViewController];
    
    if (current.presentedViewController.modalPresentationStyle == UIModalPresentationOverCurrentContext ||
        current.modalPresentationStyle == UIModalPresentationOverCurrentContext) {
        
        
        BOOL neddissmiss = NO;
        
        if ([current respondsToSelector:@selector(needAlertDissmiss)]) {
            neddissmiss = [current needAlertDissmiss];
        }
        
        if (neddissmiss) {
            __weak typeof(self) weakSelf = self;
            [current dismissViewControllerAnimated:NO completion:^{
                UIViewController *controller = [YYViewControllerCenter currentViewController];
                [controller presentViewController:weakSelf.alertController animated:YES completion:nil];
            }];
            return ;
        }
    }
    
    if (current.presentedViewController) {
        if (current.presentedViewController.isBeingPresented) {
            current = current.presentedViewController;
        }
        
    } else if (current.presentingViewController) {
        if (current.isBeingDismissed) {
            current = current.presentingViewController;
        }
        
    }
    [current presentViewController:self.alertController animated:YES completion:nil];
}

- (void)show
{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(dismissFromNotification:)
                                                 name:YYAlertViewDismissAllAlertsNotification
                                               object:nil];
    
    if (![self isShowAlertController]) {
        [alert show];
        [self setKeepInMemory:self];
    } else {
        [self showAlertController];
    }
}

- (void)showWithTimeInterval:(CGFloat)timeinterval timeout:(dispatch_block_t)timeoutblock
{
    self.timerCallback = [NSTimer scheduledTimerWithTimeInterval:timeinterval target:self selector:@selector(callbackTimeout:) userInfo:nil repeats:NO];
    self.timeoutCallbackBlock = timeoutblock;
    [self show];
}

- (void)callbackTimeout:(id)sender
{
    [self dismissAnimated:YES];
    if ( self.timeoutCallbackBlock ) {
        self.timeoutCallbackBlock();
    }
    self.timerCallback = nil;
    self.timeoutCallbackBlock = nil;
}

- (void)clearTimer
{
    if ( self.timerCallback ) {
        [self.timerCallback invalidate];
        self.timerCallback = nil;
    }
    self.timeoutCallbackBlock = nil;
}

//- (void)showInViewController: (UIViewController *)viewController {
//    
//    [[NSNotificationCenter defaultCenter] addObserver:self
//                                             selector:@selector(dismissFromNotification:)
//                                                 name:YYAlertViewDismissAllAlertsNotification
//                                               object:nil];
//    
//    if ([UIDevice currentDevice].systemVersion.floatValue >= 8.0) {
//        [viewController presentViewController:self.alertController animated:YES completion:nil];
//    } else {
//        [alert show];
//    }
//}

- (void)dismissFromNotification:(NSNotification *)event
{
    id animated = [[event userInfo] objectForKey:YYAlertViewAnimatedKey];
    [self clearTimer];
    [self dismissAnimated:[animated boolValue]];
}

- (void)dismissAnimated:(BOOL)animated
{
    if (![self isShowAlertController]) {
        [alert dismissWithClickedButtonIndex:-1 animated:animated];
    } else {
        [alertController dismissViewControllerAnimated:YES completion:nil];
        
        [self setKeepInMemory:nil];
    }
}

- (void)addButtonWithTitle:(NSString *)title block:(dispatch_block_t) block
{
    if (![self isShowAlertController]) {
        if (!block) {
            block = ^{};
        }
        if (title != nil) {
        [alert addButtonWithTitle:title];
        [blocks addObject:[block copy]];
        }
    } else {
        [alertController addAction:[UIAlertAction actionWithTitle:title
                                                            style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction *action) {
                                                              if (block) {
                                                                  block();
                                                              }
                                                          }]];
    }
    
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (buttonIndex >= 0 && buttonIndex < [blocks count]) {
        dispatch_block_t block = [blocks objectAtIndex:buttonIndex];
        block();
    }
    if (dismissAction != NULL) {
        dismissAction();
    }
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [self setKeepInMemory:nil];
}

- (UITextField *)textFieldAtIndex:(NSInteger)textFieldIndex
{
    if (![self isShowAlertController]) {
        
        if ([alert respondsToSelector:@selector(textFieldAtIndex:)]) {
            return [alert textFieldAtIndex:textFieldIndex];
        } else {
            return nil;
        }
        
    } else {
        return [[alertController textFields] objectAtIndex:textFieldIndex];
    }
}

- (void)setAlertViewStyle:(UIAlertViewStyle)alertViewStyle
{
    if (![self isShowAlertController]) {
        
        if ([alert respondsToSelector:@selector(alertViewStyle)]) {
            [alert setAlertViewStyle:alertViewStyle];
        }
        
    } else {
    
        switch (alertViewStyle) {
                
            case UIAlertViewStyleSecureTextInput:
            {
                // 密码输入框
                [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField) {
                    textField.secureTextEntry = YES;
                }];
            }
                break;
            case UIAlertViewStylePlainTextInput:
            {
                // 输入框
                [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField) {
                    textField.secureTextEntry = NO;
                }];
            }
                break;

            case UIAlertViewStyleLoginAndPasswordInput:
            {
                // 登录模式 两个输入框
                [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField) {
                    
                }];
                
                [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField) {
                    textField.secureTextEntry = YES;
                }];
            }
                break;
            default:
                // 其他的不做处理
                break;
        }
    }
}

@end
