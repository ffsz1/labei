//
//  CALayer+JXAnimation.m
//  JXCategories
//
//  Created by Colin on 2019/1/25.
//

#import "CALayer+JXAnimation.h"
#import <UIKit/UIKit.h>

@implementation CALayer (JXAnimation)

#pragma mark - Animation
- (CGFloat)jx_transformRotation {
    NSNumber *value = [self valueForKeyPath:@"transform.rotation"];
    return value.doubleValue;
}

- (void)setJx_transformRotation:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.rotation"];
}

- (CGFloat)jx_transformRotationX {
    NSNumber *value = [self valueForKeyPath:@"transform.rotation.x"];
    return value.doubleValue;
}

- (void)setJx_transformRotationX:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.rotation.x"];
}

- (CGFloat)jx_transformRotationY {
    NSNumber *value = [self valueForKeyPath:@"transform.rotation.y"];
    return value.doubleValue;
}

- (void)setJx_transformRotationY:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.rotation.y"];
}

- (CGFloat)jx_transformRotationZ {
    NSNumber *value = [self valueForKeyPath:@"transform.rotation.z"];
    return value.doubleValue;
}

- (void)setJx_transformRotationZ:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.rotation.z"];
}

- (CGFloat)jx_transformScaleX {
    NSNumber *value = [self valueForKeyPath:@"transform.scale.x"];
    return value.doubleValue;
}

- (void)setJx_transformScaleX:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.scale.x"];
}

- (CGFloat)jx_transformScaleY {
    NSNumber *value = [self valueForKeyPath:@"transform.scale.y"];
    return value.doubleValue;
}

- (void)setJx_transformScaleY:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.scale.y"];
}

- (CGFloat)jx_transformScaleZ {
    NSNumber *value = [self valueForKeyPath:@"transform.scale.z"];
    return value.doubleValue;
}

- (void)setJx_transformScaleZ:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.scale.z"];
}

- (CGFloat)jx_transformScale {
    NSNumber *value = [self valueForKeyPath:@"transform.scale"];
    return value.doubleValue;
}

- (void)setJx_transformScale:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.scale"];
}

- (CGFloat)jx_transformTranslationX {
    NSNumber *value = [self valueForKeyPath:@"transform.translation.x"];
    return value.doubleValue;
}

- (void)setJx_transformTranslationX:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.translation.x"];
}

- (CGFloat)jx_transformTranslationY {
    NSNumber *value = [self valueForKeyPath:@"transform.translation.y"];
    return value.doubleValue;
}

- (void)setJx_transformTranslationY:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.translation.y"];
}

- (CGFloat)jx_transformTranslationZ {
    NSNumber *value = [self valueForKeyPath:@"transform.translation.z"];
    return value.doubleValue;
}

- (void)setJx_transformTranslationZ:(CGFloat)value {
    [self setValue:@(value) forKeyPath:@"transform.translation.z"];
}

- (CGFloat)jx_transformDepth {
    return self.transform.m34;
}

- (void)setJx_transformDepth:(CGFloat)value {
    CATransform3D transform = self.transform;
    transform.m34 = value;
    self.transform = transform;
}

#pragma mark - Animations
- (void)jx_removeDefaultAnimations {
    NSMutableDictionary<NSString *, id<CAAction>> *actions = @{
                                                               NSStringFromSelector(@selector(bounds)): [NSNull null],
                                                               NSStringFromSelector(@selector(position)): [NSNull null],
                                                               NSStringFromSelector(@selector(zPosition)): [NSNull null],
                                                               NSStringFromSelector(@selector(anchorPoint)): [NSNull null],
                                                               NSStringFromSelector(@selector(anchorPointZ)): [NSNull null],
                                                               NSStringFromSelector(@selector(transform)): [NSNull null],
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wundeclared-selector"
                                                               NSStringFromSelector(@selector(hidden)): [NSNull null],
                                                               NSStringFromSelector(@selector(doubleSided)): [NSNull null],
#pragma clang diagnostic pop
                                                               NSStringFromSelector(@selector(sublayerTransform)): [NSNull null],
                                                               NSStringFromSelector(@selector(masksToBounds)): [NSNull null],
                                                               NSStringFromSelector(@selector(contents)): [NSNull null],
                                                               NSStringFromSelector(@selector(contentsRect)): [NSNull null],
                                                               NSStringFromSelector(@selector(contentsScale)): [NSNull null],
                                                               NSStringFromSelector(@selector(contentsCenter)): [NSNull null],
                                                               NSStringFromSelector(@selector(minificationFilterBias)): [NSNull null],
                                                               NSStringFromSelector(@selector(backgroundColor)): [NSNull null],
                                                               NSStringFromSelector(@selector(cornerRadius)): [NSNull null],
                                                               NSStringFromSelector(@selector(borderWidth)): [NSNull null],
                                                               NSStringFromSelector(@selector(borderColor)): [NSNull null],
                                                               NSStringFromSelector(@selector(opacity)): [NSNull null],
                                                               NSStringFromSelector(@selector(compositingFilter)): [NSNull null],
                                                               NSStringFromSelector(@selector(filters)): [NSNull null],
                                                               NSStringFromSelector(@selector(backgroundFilters)): [NSNull null],
                                                               NSStringFromSelector(@selector(shouldRasterize)): [NSNull null],
                                                               NSStringFromSelector(@selector(rasterizationScale)): [NSNull null],
                                                               NSStringFromSelector(@selector(shadowColor)): [NSNull null],
                                                               NSStringFromSelector(@selector(shadowOpacity)): [NSNull null],
                                                               NSStringFromSelector(@selector(shadowOffset)): [NSNull null],
                                                               NSStringFromSelector(@selector(shadowRadius)): [NSNull null],
                                                               NSStringFromSelector(@selector(shadowPath)): [NSNull null]
                                                               }.mutableCopy;
    self.actions = actions.copy;
}

#pragma mark - Fade
- (void)jx_addFadeAnimationWithDuration:(NSTimeInterval)duration curve:(UIViewAnimationCurve)curve {
    if (duration <= 0) return;
    
    NSString *mediaFunction;
    switch (curve) {
        case UIViewAnimationCurveEaseInOut:
        {
            mediaFunction = kCAMediaTimingFunctionEaseInEaseOut;
        }
            break;
        case UIViewAnimationCurveEaseIn:
        {
            mediaFunction = kCAMediaTimingFunctionEaseIn;
        }
            break;
        case UIViewAnimationCurveEaseOut:
        {
            mediaFunction = kCAMediaTimingFunctionEaseOut;
        }
            break;
        case UIViewAnimationCurveLinear:
        {
            mediaFunction = kCAMediaTimingFunctionLinear;
        }
            break;
        default:
        {
            mediaFunction = kCAMediaTimingFunctionLinear;
        } break;
    }
    
    CATransition *transition = [CATransition animation];
    transition.duration = duration;
    transition.timingFunction = [CAMediaTimingFunction functionWithName:mediaFunction];
    transition.type = kCATransitionFade;
    [self addAnimation:transition forKey:@"calayer.fade"];
}

- (void)jx_removePreviousFadeAnimation {
    [self removeAnimationForKey:@"calayer.fade"];
}

#pragma mark - Opacity Forever
- (void)jx_addOpacityForeverAnimation:(NSTimeInterval)duration fromValue:(CGFloat)fromValue toValue:(CGFloat)toValue repeatCount:(int)repeatCount
{
    CABasicAnimation *animation=[CABasicAnimation animationWithKeyPath:@"opacity"];
    animation.fromValue=[NSNumber numberWithFloat:fromValue];
    animation.toValue=[NSNumber numberWithFloat:toValue];
    animation.autoreverses=YES;
    animation.duration=duration;
    animation.repeatCount=repeatCount;
    animation.removedOnCompletion=NO;
    animation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
    animation.fillMode=kCAFillModeForwards;
    [self addAnimation:animation forKey:@"JXOpacityForeverAnimation"];
}

- (void)jx_removeOpacityForeverAnimation {
    [self removeAnimationForKey:@"JXOpacityForeverAnimation"];
}

#pragma mark - Rotation
- (void)jx_addRotationAnimation:(NSTimeInterval)duration degree:(float)degree direction:(JXCALayerAxis)axis repeatCount:(int)repeatCount
{
    CABasicAnimation* animation;
    NSArray *axisArr = @[@"transform.rotation.x", @"transform.rotation.y", @"transform.rotation.z"];
    animation = [CABasicAnimation animationWithKeyPath:axisArr[axis]];
    animation.fromValue = [NSNumber numberWithFloat:0];
    animation.toValue= [NSNumber numberWithFloat:degree];
    animation.duration= duration;
    animation.autoreverses= NO;
    animation.cumulative= YES;
    animation.removedOnCompletion=NO;
    animation.fillMode=kCAFillModeForwards;
    animation.repeatCount= repeatCount;
    [self addAnimation:animation forKey:@"JXRotationAnimation"];
}

- (void)jx_removeRotationAnimation {
    [self removeAnimationForKey:@"JXRotationAnimation"];
}

#pragma mark - Scale
- (void)jx_addScaleAnimation:(CGFloat)fromScale toScale:(CGFloat)toScale duration:(NSTimeInterval)duration repeatCount:(float)repeatCount
{
    CABasicAnimation *animation=[CABasicAnimation animationWithKeyPath:@"transform.scale"];
    animation.fromValue = @(fromScale);
    animation.toValue = @(toScale);
    animation.duration = duration;
    animation.autoreverses = YES;
    animation.repeatCount = repeatCount;
    animation.removedOnCompletion = NO;
    animation.fillMode = kCAFillModeForwards;
    [self addAnimation:animation forKey:@"JXScaleAnimation"];
}

- (void)jx_removeScaleAnimation {
    [self removeAnimationForKey:@"JXScaleAnimation"];
}

#pragma mark - Shake
- (void)jx_addShakeAnimation:(NSTimeInterval)duration repeatCount:(float)repeatCount
{
    NSAssert([self isKindOfClass:[CALayer class]] , @"invalid target");
    CGPoint originPos = CGPointZero;
    CGSize originSize = CGSizeZero;
    if ([self isKindOfClass:[CALayer class]]) {
        originPos = [(CALayer *)self position];
        originSize = [(CALayer *)self bounds].size;
    }
    CGFloat hOffset = originSize.width / 4;
    CAKeyframeAnimation* anim=[CAKeyframeAnimation animation];
    anim.keyPath=@"position";
    anim.values=@[
                  [NSValue valueWithCGPoint:CGPointMake(originPos.x, originPos.y)],
                  [NSValue valueWithCGPoint:CGPointMake(originPos.x-hOffset, originPos.y)],
                  [NSValue valueWithCGPoint:CGPointMake(originPos.x, originPos.y)],
                  [NSValue valueWithCGPoint:CGPointMake(originPos.x+hOffset, originPos.y)],
                  [NSValue valueWithCGPoint:CGPointMake(originPos.x, originPos.y)]
                  ];
    anim.repeatCount = repeatCount;
    anim.duration = duration;
    anim.fillMode = kCAFillModeForwards;
    [self addAnimation:anim forKey:@"JXShakeAnimation"];
}

- (void)jx_removeShakeAnimation {
    [self removeAnimationForKey:@"JXShakeAnimation"];
}

#pragma mark - Bounce
- (void)jx_addBounceAnimation:(NSTimeInterval)duration repeatCount:(float)repeatCount
{
    NSAssert([self isKindOfClass:[CALayer class]] , @"invalid target");
    CGPoint originPos = CGPointZero;
    CGSize originSize = CGSizeZero;
    if ([self isKindOfClass:[CALayer class]]) {
        originPos = [(CALayer *)self position];
        originSize = [(CALayer *)self bounds].size;
    }
    CGFloat hOffset = originSize.height / 4;
    CAKeyframeAnimation* anim=[CAKeyframeAnimation animation];
    anim.keyPath=@"position";
    anim.values=@[
                  [NSValue valueWithCGPoint:CGPointMake(originPos.x, originPos.y)],
                  [NSValue valueWithCGPoint:CGPointMake(originPos.x, originPos.y-hOffset)],
                  [NSValue valueWithCGPoint:CGPointMake(originPos.x, originPos.y)],
                  [NSValue valueWithCGPoint:CGPointMake(originPos.x, originPos.y+hOffset)],
                  [NSValue valueWithCGPoint:CGPointMake(originPos.x, originPos.y)]
                  ];
    anim.repeatCount=repeatCount;
    anim.duration=duration;
    anim.fillMode = kCAFillModeForwards;
    [self addAnimation:anim forKey:@"JXBounceAnimation"];

}

- (void)jx_removeBounceAnimation {
    [self removeAnimationForKey:@"JXBounceAnimation"];
}

@end
