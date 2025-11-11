//
//  YPRippleAnimationView.m
//  HJLive
//
//  Created by apple on 2020/9/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRippleAnimationView.h"

static NSInteger const pulsingCount = 2;
static double const animationDuration = 1;

@interface YPRippleAnimationView ()<CAAnimationDelegate>

@end

@implementation YPRippleAnimationView

- (instancetype)initWithFrame:(CGRect)frame gender:(JXIMUserGenderType)gender{
    self = [super initWithFrame:frame];
    
    if (self) {
        _gender = gender;
        _multiple = 1.0;
    }
    return self;
}

- (void)drawRect:(CGRect)rect{
    CALayer *animationLayer = [CALayer layer];
    
    for (int i = 0; i < pulsingCount; i++) {
        NSArray *animationArray = [self animationArray];
        CAAnimationGroup *animationGroup = [self animationGroupAnimations:animationArray index:i];
        CALayer *pulsingLayer = [self pulsingLayer:rect animation:animationGroup];
        [animationLayer addSublayer:pulsingLayer];
    }
    
    [self.layer addSublayer:animationLayer];
}

- (NSArray *)animationArray{
    NSArray *animationArray = nil;
    
    CABasicAnimation *scaleAnimation = [self scaleAnimation];
    CAKeyframeAnimation *blackBorderColorAnimation = [self blackBorderColorAnimation];
    animationArray = @[scaleAnimation, blackBorderColorAnimation];
    
    return animationArray;
}

- (CAAnimationGroup *)animationGroupAnimations:(NSArray *)array index:(int)index{
    CAMediaTimingFunction *defaultCurve = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionDefault];
    CAAnimationGroup *animationGroup = [CAAnimationGroup animation];
    
    animationGroup.fillMode = kCAFillModeBackwards;
    animationGroup.beginTime = CACurrentMediaTime() + (double)(index * animationDuration) / (double)pulsingCount;
    animationGroup.duration = animationDuration;
    animationGroup.repeatCount = 1;
    animationGroup.timingFunction = defaultCurve;
    animationGroup.animations = array;
    animationGroup.removedOnCompletion = YES;
    animationGroup.delegate = self;
    return animationGroup;
}

- (CALayer *)pulsingLayer:(CGRect)rect animation:(CAAnimationGroup *)animationGroup{
    CALayer *pulsingLayer = [CALayer layer];
    
    pulsingLayer.frame = CGRectMake(0, 0, rect.size.width, rect.size.height);
    
    pulsingLayer.borderWidth = 1.f;
    if (_gender == JXIMUserGenderTypeMale){
       pulsingLayer.borderColor = ColorWithAlpha(0, 157, 255, 1.0).CGColor;
    }else{
        pulsingLayer.borderColor = ColorWithAlpha(255, 77, 127, 1.0).CGColor;
    }
    
    pulsingLayer.cornerRadius = rect.size.height/2;
    [pulsingLayer addAnimation:animationGroup forKey:@"pulsing"];
    
    return pulsingLayer;
}


- (CABasicAnimation *)scaleAnimation{
    CABasicAnimation *scaleAnimation = [CABasicAnimation animationWithKeyPath:@"transform.scale"];
    
    scaleAnimation.fromValue = @0.5;
    scaleAnimation.toValue = @(_multiple);
    return scaleAnimation;
}

- (CAKeyframeAnimation *)blackBorderColorAnimation{
    CAKeyframeAnimation *borderColorAnimation = [CAKeyframeAnimation animation];
    
    borderColorAnimation.keyPath = @"borderColor";
    if (_gender == JXIMUserGenderTypeMale) {
        borderColorAnimation.values = @[(__bridge id)ColorWithAlpha(0, 157, 255, 1.0).CGColor,
                                        (__bridge id)ColorWithAlpha(0, 157, 255, 1.0).CGColor,
                                        (__bridge id)ColorWithAlpha(0, 157, 255, 1.0).CGColor,
                                        (__bridge id)ColorWithAlpha(0, 157, 255, 1.0).CGColor];
    }else{
        borderColorAnimation.values = @[(__bridge id)ColorWithAlpha(255, 77, 127, 1.0).CGColor,
                                        (__bridge id)ColorWithAlpha(255, 77, 127, 1.0).CGColor,
                                        (__bridge id)ColorWithAlpha(255, 77, 127, 1.0).CGColor,
                                        (__bridge id)ColorWithAlpha(255, 77, 127, 1.0).CGColor];
    }
        
    borderColorAnimation.keyTimes = @[@0.3,@0.6,@0.9,@1];
    return borderColorAnimation;
}

- (void)animationDidStop:(CAAnimation *)anim finished:(BOOL)flag{
    if ([_delegate respondsToSelector:@selector(rippleAnimationFinishedWithAnimationView:)]) {
        [_delegate rippleAnimationFinishedWithAnimationView:self];
    }
}

/*
- (CAKeyframeAnimation *)backgroundColorAnimation{
    CAKeyframeAnimation *backgroundColorAnimation = [CAKeyframeAnimation animation];
    
    backgroundColorAnimation.keyPath = @"backgroundColor";
    backgroundColorAnimation.values = @[(__bridge id)ColorWithAlpha(255, 216, 87, 0.5).CGColor,
                                        (__bridge id)ColorWithAlpha(255, 231, 87, 0.5).CGColor,
                                        (__bridge id)ColorWithAlpha(255, 241, 197, 0.5).CGColor,
                                        (__bridge id)ColorWithAlpha(255, 241, 197, 0).CGColor];
    
    backgroundColorAnimation.keyTimes = @[@0.3,@0.6,@0.9,@1];
    return backgroundColorAnimation;
}

- (CAKeyframeAnimation *)borderColorAnimation{
    CAKeyframeAnimation *borderColorAnimation = [CAKeyframeAnimation animation];
    
    borderColorAnimation.keyPath = @"borderColor";
    borderColorAnimation.values = @[(__bridge id)ColorWithAlpha(255, 216, 87, 0.5).CGColor,
                                        (__bridge id)ColorWithAlpha(255, 231, 87, 0.5).CGColor,
                                        (__bridge id)ColorWithAlpha(255, 241, 197, 0.5).CGColor,
                                        (__bridge id)ColorWithAlpha(255, 241, 197, 0).CGColor];
    
    borderColorAnimation.keyTimes = @[@0.3,@0.6,@0.9,@1];
    return borderColorAnimation;
}
 */

@end
