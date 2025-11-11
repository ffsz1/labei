//
//  CALayer+JXAnimation.h
//  JXCategories
//
//  Created by Colin on 2019/1/25.
//

#import <QuartzCore/QuartzCore.h>
#import <UIKit/UIView.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, JXCALayerAxis) { ///< 轴线类型
    JXCALayerAxisX = 0, ///< X轴
    JXCALayerAxisY,     ///< X轴
    JXCALayerAxisZ      ///< Z轴
};

@interface CALayer (JXAnimation)

#pragma mark - Animation
@property (nonatomic) CGFloat jx_transformRotation;     ///< transform旋转角度(key path "tranform.rotation")
@property (nonatomic) CGFloat jx_transformRotationX;    ///< transformX轴方向旋转(key path "tranform.rotation.x")
@property (nonatomic) CGFloat jx_transformRotationY;    ///< transformY轴方向旋转(key path "tranform.rotation.y")
@property (nonatomic) CGFloat jx_transformRotationZ;    ///< transformZ轴方向旋转(key path "tranform.rotation.z")
@property (nonatomic) CGFloat jx_transformScale;        ///< transform放大系数(key path "tranform.scale")
@property (nonatomic) CGFloat jx_transformScaleX;       ///< transformX轴方向放大(key path "tranform.scale.x")
@property (nonatomic) CGFloat jx_transformScaleY;       ///< transformY轴方向放大(key path "tranform.scale.y")
@property (nonatomic) CGFloat jx_transformScaleZ;       ///< transformZ轴方向放大(key path "tranform.scale.z")
@property (nonatomic) CGFloat jx_transformTranslationX; ///< transformX轴方向平移(key path "tranform.translation.x")
@property (nonatomic) CGFloat jx_transformTranslationY; ///< transformY轴方向平移(key path "tranform.translation.y")
@property (nonatomic) CGFloat jx_transformTranslationZ; ///< transformZ轴方向平移(key path "tranform.translation.z")
@property (nonatomic) CGFloat jx_transformDepth;        ///< transform透视效果(transform.m34, 可选用-1/1000, 应在其他transform前设置)

#pragma mark - Animations
/**
 移除Layer所有支持动画的属性的默认动画
 */
- (void)jx_removeDefaultAnimations;

#pragma mark - Fade
/**
 为layer的内容添加Fade(淡入淡出)动画, 内容改变时触发
 
 @param duration 动画时长
 @param curve    动画变化曲线
 */
- (void)jx_addFadeAnimationWithDuration:(NSTimeInterval)duration curve:(UIViewAnimationCurve)curve;

/**
 移除Fade(淡入淡出)动画(通过addFadeAnimationWithDuration:curve:方法添加)
 */
- (void)jx_removePreviousFadeAnimation;

#pragma mark - Opacity Forever
/**
 设置透明值动画

 @param duration 动画时长
 @param fromValue 初始透明值(0~1.0)
 @param toValue 结束透明值(0~1.0)

 */
- (void)jx_addOpacityForeverAnimation:(NSTimeInterval)duration
                            fromValue:(CGFloat)fromValue
                              toValue:(CGFloat)toValue
                          repeatCount:(int)repeatCount;

/**
 移除OpacityForever动画
 */
- (void)jx_removeOpacityForeverAnimation;

#pragma mark - Rotation
/**
 设置旋转动画

 @param duration 动画时长
 @param degree 旋转角度
 @param axis 轴线值(x=0,y=1,z=3)
 @param repeatCount 重复次数
 */
- (void)jx_addRotationAnimation:(NSTimeInterval)duration
                         degree:(float)degree
                      direction:(JXCALayerAxis)axis
                    repeatCount:(int)repeatCount;

/**
 移除RotationAnimation动画
 */
- (void)jx_removeRotationAnimation;

#pragma mark - Scale
/**
 设置缩放动画

 @param fromScale 初始大小值
 @param toScale 结束大小值
 @param duration 动画时长
 @param repeatCount 重复次数
 */
- (void)jx_addScaleAnimation:(CGFloat)fromScale
                     toScale:(CGFloat)toScale
                    duration:(NSTimeInterval)duration
                 repeatCount:(float)repeatCount;

/**
 移除ScaleAnimation动画
 */
- (void)jx_removeScaleAnimation;

#pragma mark - Shake
/**
 设置抖动动画

 @param duration 动画时长
 @param repeatCount 重复次数
 */
- (void)jx_addShakeAnimation:(NSTimeInterval)duration repeatCount:(float)repeatCount;

/**
 移除ShakeAnimation动画
 */
- (void)jx_removeShakeAnimation;

#pragma mark - Bounce
/**
 设置弹跳动画

 @param duration 动画时长
 @param repeatCount 重复次数
 */
- (void)jx_addBounceAnimation:(NSTimeInterval)duration repeatCount:(float)repeatCount;

/**
 移除BounceAnimation动画
 */
- (void)jx_removeBounceAnimation;

@end

NS_ASSUME_NONNULL_END
