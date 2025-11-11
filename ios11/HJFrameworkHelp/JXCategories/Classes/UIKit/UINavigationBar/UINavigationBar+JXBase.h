//
//  UINavigationBar+JXBase.h
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UINavigationBar (JXBase)

#pragma mark - Base
/**
 获取UINavigationBar的背景界面
 iOS10以前 -> _UINavigationBarBackground
 iOS10以后 -> _UIBarBackground
 */
@property (nonatomic, readonly) UIView *jx_backgroundView;

/**
 获取UINavigationBar的背景内容界面
 iOS10以后:
    显示磨砂 -> UIVisualEffectView
    显示背景图 -> UIImageView
 iOS10以前:
    显示磨砂 -> _UIBackdropView
    显示背景图 -> _UINavigationBarBackground(jx_backgroundView)
 
 */
@property (nonatomic, readonly) __kindof UIView *jx_backgroundContentView;

@property (nonatomic, readonly) UIImageView *jx_shadowImageView; ///< UINavigationBar的底部分割线
@property (nonatomic, strong) UIColor *jx_shadowImageViewBackgroundColor; ///< UINavigationBar的底部分割线颜色

@end

NS_ASSUME_NONNULL_END
