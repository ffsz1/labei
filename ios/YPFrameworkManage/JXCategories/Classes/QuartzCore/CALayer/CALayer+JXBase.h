//
//  CALayer+JXBase.h
//  JXCategories
//
//  Created by Colin on 17/1/6.
//  Copyright © 2017年 JuXiao. All rights reserved.
//  Base

#import <QuartzCore/QuartzCore.h>

NS_ASSUME_NONNULL_BEGIN

@class UIColor, UIImage;

@interface CALayer (JXBase)

#pragma mark - Base
/**
 根据Layer的contentsGravity属性, 获取其对应的contentMode
 */
@property (nonatomic) UIViewContentMode jx_contentMode;

/**
 根据Index, 交换指定的Sublayer的位置

 @param index1 Sublayer对应的Index
 @param index2 Sublayer对应的Index
 */
- (void)jx_exchangeSublayerAtIndex:(unsigned)index1 withSublayerAtIndex:(unsigned)index2;

/**
 把指定的Sublayer移动到当前所有Sublayers的最前面(Sublayer须已添加到当前Layer上)

 @param sublayer 指定的Sublayer
 */
- (void)jx_bringSublayerToFront:(CALayer *)sublayer;

/**
 把指定的Sublayer移动到当前所有Sublayers的最后面(Sublayer须已添加到当前Layer上)

 @param sublayer 指定的Sublayer
 */
- (void)jx_sendSublayerToBack:(CALayer *)sublayer;

/**
 将Layer移动到父Layer的最前面
 */
- (void)jx_bringToFront;

/**
 将Layer移动到父Layer的最后面
 */
- (void)jx_sendToBack;

/**
 移除Layer内指定的Sublayer(勿在layer的drawInContext:方法内调用此方法)

 @param sublayer 指定的Sublayer
 */
- (void)jx_removeSublayer:(CALayer *)sublayer;

/**
 移除所有子Layers(勿在layer的drawInContext:方法内调用此方法)
 */
- (void)jx_removeAllSublayers;

#pragma mark - Layout
@property (nonatomic) CGFloat jx_x;                                                 ///< Layer的x值 -> self.frame.origin.x
@property (nonatomic) CGFloat jx_y;                                                 ///< Layer的y值 -> self.frame.origin.y
@property (nonatomic) CGPoint jx_origin;                                            ///< Layer的origin值 -> self.frame.origin
@property (nonatomic) CGFloat jx_width;                                             ///< Layer的width值 -> self.frame.size.width
@property (nonatomic) CGFloat jx_height;                                            ///< Layer的height值 -> self.frame.size.height
@property (nonatomic, getter=jx_frameSize, setter=setJx_frameSize:) CGSize jx_size; ///< Layer的size值 -> self.frame.size
@property (nonatomic) CGFloat jx_centerX;                                           ///< Layer的center值 -> self.center
@property (nonatomic) CGFloat jx_centerY;                                           ///< Layer的centerX值 -> self.center.x
@property (nonatomic) CGPoint jx_center;                                            ///< Layer的centerY值 -> self.center.y
@property (nonatomic) CGFloat jx_left;                                              ///< self.frame.origin.x
@property (nonatomic) CGFloat jx_top;                                               ///< self.frame.origin.y
@property (nonatomic) CGFloat jx_right;                                             ///< self.frame.origin.x + self.frame.size.width
@property (nonatomic) CGFloat jx_bottom;                                            ///< self.frame.origin.y + self.frame.size.height

#pragma mark - Shadow
/**
 设置layer阴影
 
 @param color  阴影颜色
 @param offset 阴影偏移量
 @param radius 阴影角度
 */
- (void)jx_setLayerShadow:(UIColor *)color offset:(CGSize)offset radius:(CGFloat)radius;

/**
 设置layer阴影
 
 @param color   阴影颜色
 @param offset  阴影偏移量
 @param radius  阴影角度
 @param opacity 阴影透明度
 */
- (void)jx_setLayerShadow:(UIColor *)color offset:(CGSize)offset radius:(CGFloat)radius opacity:(CGFloat)opacity;

#pragma mark - Snapshot
/**
 获取当前layer截图(无视transform, 根据bouns截图)
 
 @return 当前layer截图
 */
- (UIImage *)jx_snapshotImage;

@end

NS_ASSUME_NONNULL_END
