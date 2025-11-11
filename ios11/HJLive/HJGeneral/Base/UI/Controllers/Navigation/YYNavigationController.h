//
//  YYNavigationController.h
//  YYMobile
//
//  Created by wuwei on 14/7/2.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YYNavigationBar.h"
#import "YYInteractivePopTransition.h"

@class YYNavigationController;

@protocol YYNavigationControllerDelegate <UINavigationControllerDelegate>

@end

@interface YYNavigationController : UINavigationController

@property (nonatomic, weak) id<YYNavigationControllerDelegate> delegate;

@property (nonatomic, strong, readonly) YYInteractivePopTransition *interativePopTransition;

@property (nonatomic, readonly) YYNavigationBar *navigationBar;

/**
 *  获取当前栈顶的NavigationBarAppearance
 *  请务必在主线程调用此函数
 *
 *  @return 当前线顶的NavigationBarAppearance
 */
- (YYNavigationBarAppearance *)topNavigationBarAppearance;

- (void)pushBottomBarViewController:(UIViewController *)viewController animated:(BOOL)animated;

/**
 *
 *  将当前NavigationBar的Appearance压栈, 使用指定的上下文.
 *  若当前栈顶Appearance的上下文与参数指定的一致, 则push会被忽略, 并返回NO; 否则返回YES
 *
 *  @param context 上下文对象, 不允许为空
 */
//- (BOOL)pushAppearanceStateWithContext:(id)context;

/**
 *  将当前栈顶的Appearance弹出, 使用指定的上下文进行校验.
 *  若当前栈顶Appearance的上下文与参数指定的一致, 则pop成功, 返回YES; 否则返回NO
 *  若成功弹出, 则会使用栈顶的Appearance对NavigationBar进行设置
 *
 *  @param context  上下文对象, 不允许为空
 *  @param animated 恢复过程是否要动画
 */
//- (BOOL)popAppearanceStateWithContext:(id)context animated:(BOOL)animated;

@end
