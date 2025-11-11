//
//  UITabBar+JXBase.h
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UITabBar (JXBase)

#pragma mark - Base
/**
 获取UITabBar的背景界面
 iOS10以前 -> _UITabBarBackgroundView
 iOS10以后 -> _UIBarBackground
 */
@property (nonatomic, readonly) UIView *jx_backgroundView;

@property (nonatomic, readonly) UIImageView *jx_shadowImageView; ///< UITabBar的顶部分割线
@property (nonatomic, strong) UIColor *jx_shadowImageViewBackgroundColor; ///< UITabBar的顶部分割线颜色

@end

NS_ASSUME_NONNULL_END
