//
//  UIButton+JXLayout.h
//  HJLive
//
//  Created by feiyin on 2020/6/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIButton (JXLayout)

/**
 图片文字上线排列
 
 @param spacing 间距
 */
- (void)verticalImageAndTitle:(CGFloat)spacing;


/**
 图片文字上线排列
 */
- (void)verticalImageAndTitle;


/**
 左文字右图标排列
 
 @param spacing 间距
 */
- (void)invertedImageAndTitle:(CGFloat)spacing;

/**
 左文字右图标排列,默认间距
 */
- (void)invertedImageAndTitle;

/**
 左图右文字排列,默认左对齐
 */
- (void)alignmentLeftImageAndTitle;


/**
 左图右文字排列,默认左对齐

 @param imageSpacing 图片离左边距的距离
 @param titleSpacing 文字离图片的距离
 */
- (void)alignmentLeftImageAndTitle:(CGFloat)imageSpacing titleSpacing:(CGFloat)titleSpacing;


@end

NS_ASSUME_NONNULL_END
