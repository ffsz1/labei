//
//  UIView+Toast.h
//  YYMobile
//
//  Created by 武帮民 on 14-7-21.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, YYToastPosition)
{
    YYToastPositionBottom,
    YYToastPositionBottomWithTabbar,
    YYToastPositionCenter,
    YYToastPositionTop,
    YYToastPositionAboveKeyboard,
    YYToastPositionBottomWithRecordButton,
    
    YYToastPositionDefault = YYToastPositionBottom,
};

@interface UIView (XCToast)

// 展示Toast
- (void)showToastView:(UIView *)toast
             duration:(CGFloat)interval
             position:(YYToastPosition)position;

- (void)showToastView:(UIView *)toast
             duration:(CGFloat)interval
             position:(YYToastPosition)position
             animated:(BOOL)animated;

/**
 *  显示提示视图，视图的位置将使用 toastView 的 frame 值
 *
 *  @param toastView 待显示的提示视图
 *  @param interval  提示视图显示的时间间隔
 *  @param animated  显示时是否带动画
 */
- (void)showToastView:(UIView *)toastView
             duration:(CGFloat)interval
             animated:(BOOL)animated;


// 隐藏 Toast
- (void)hideToastView;

- (void)hideToastViewAnimated:(BOOL)animated;


@end

@interface UIView (HudToast)

/**
 * message : 需要显示的文本
 * text : 需要高亮显示的文本
 * color : 高亮文本颜色
 * position: 需要显示的位置
 */
+ (void)showToastInKeyWindow:(NSString *)message;
+ (void)showToastInKeyWindow:(NSString *)message duration:(CGFloat)interval;
+ (void)showToastInKeyWindow:(NSString *)message
                    duration:(CGFloat)interval
                    position:(YYToastPosition)position;

- (void)showToast:(NSString *)message;
- (void)showToast:(NSString *)message
         position:(YYToastPosition)position;
- (void)showToastWithMessage:(NSString *)message
                    duration:(CGFloat)interval
                    position:(YYToastPosition)position;

- (void)showToast:(NSString *)message
    highlightText:(NSString *)text
   highlightColor:(UIColor *)color;

- (void)showToast:(NSString *)message
    highlightText:(NSString *)text
   highlightColor:(UIColor *)color
         position:(YYToastPosition)position;

@end


@interface UIView (LoadingToast)

// view中间 显示一个正在加载的动画
- (void)showLoadingToast;
- (void)showLoadingToastDuration:(CGFloat)interval;
- (void)showLoadingToastDuration:(CGFloat)interval completion:(void (^)(void))completion;
//坐标加偏移量
- (void)showLoadingToastWithOffsetY:(NSInteger)offsetY;


@end

@interface UIView (EmptyToast)

- (void)showToast:(NSString*)message duration:(CGFloat)dur position:(YYToastPosition)position image:(UIImage*)image;

- (void)showEmptyContentToastWithTitle:(NSString *)title;
- (void)showEmptyContentToastWithTitle:(NSString *)title andImage:(UIImage *)image;
- (void)showEmptyContentToastWithTitle:(NSString *)title tapBlock:(void (^)(void))tapBlock;

- (void)showEmptyContentToastWithAttributeString:(NSAttributedString *)attrStr;
- (void)showEmptyContentToastWithAttributeString:(NSAttributedString *)attrStr tapBlock:(void (^)(void))tapBlock;
- (void)showNoSearchResultToastWithSearchKey:(NSString *)searchKey tapBlock:(void (^)(void))tapBlock;


- (void)showNetworkDisconnectToastWithPosition:(YYToastPosition)position;
+ (void)showNetworkDisconnectToastInKeyWindowWithPosition:(YYToastPosition)position;

- (void)showNetworkErrorToastWithTitle:(NSString *)title;
- (void)showNetworkErrorToastWithTitle:(NSString *)title tapBlock:(void (^)(void))tapBlock;

@end
