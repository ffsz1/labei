//
//  UIButton+JXConfigureForState.m
//  Pods
//
//  Created by Colin on 17/4/19.
//
//

#import "UIButton+JXConfigureForState.h"
#import "NSObject+JXBase.h"

//static const int JX_UI_BUTTON_BACKGROUND_COLORS_KEY;

@implementation UIButton (JXConfigureForState)

///**
// 根据按钮状态, 设置按钮的背景颜色
// 
// @param backgroundColor 背景颜色
// @param state           按钮状态
// */
//- (void)jx_setBackgroundColor:(UIColor *)backgroundColor forState:(UIControlState)state;
//
///**
// 获取按钮状态对应的背景颜色
// 
// @param state 按钮状态
// @return 对应的背景颜色
// */
//- (UIColor *)jx_backgroundColorForState:(UIControlState)state;
//
//@property (nonatomic, readonly, strong) UIColor *jx_currentBackgroundColor; ///< 按钮当前的背景颜色(normal/highlighted/selected/disabled, 或为nil)

/*
#pragma mark - Base
+ (void)load {
    [self _jx_swizzleSetEnabledMethod];
    [self _jx_swizzleSetHighlightedMethod];
    [self _jx_swizzleSetSelectedMethod];
    [self _jx_swizzleSetBackgroundColorMethod];
}

- (void)jx_setBackgroundColor:(UIColor *)backgroundColor forState:(UIControlState)state {
    if (state == self.state) {
        [self setBackgroundColor:backgroundColor];
        return;
    }
    
    [self _jx_setBackgroundColor:backgroundColor forState:state];
}

- (UIColor *)jx_backgroundColorForState:(UIControlState)state {
    UIColor *backgroundColor = [self _jx_backgroundColorForState:state];
    if (!backgroundColor) return [self _jx_backgroundColorForState:UIControlStateNormal];
    
    return backgroundColor;
}

- (UIColor *)jx_currentBackgroundColor {
    return [self _jx_backgroundColorForCurrentState];
}

- (void)_jx_setBackgroundColor:(UIColor *)backgroundColor forState:(UIControlState)state {
    NSString *key = [self _jx_keyForState:state];
    if (backgroundColor) {
        [self._jx_backgroundColors setObject:backgroundColor forKey:key];
    } else {
        [self._jx_backgroundColors removeObjectForKey:key];
    }
}

- (UIColor *)_jx_backgroundColorForState:(UIControlState)state {
    NSString *key = [self _jx_keyForState:state];
    return [self._jx_backgroundColors objectForKey:key];
}

- (NSMutableDictionary *)_jx_backgroundColors {
    NSMutableDictionary *backgroundColors = [self jx_getAssociatedValueForKey:&JX_UI_BUTTON_BACKGROUND_COLORS_KEY];
    if (!backgroundColors) {
        backgroundColors = @{}.mutableCopy;
        [self jx_setAssociatedValue:backgroundColors withKey:&JX_UI_BUTTON_BACKGROUND_COLORS_KEY];
    }
    return backgroundColors;
}

- (NSString *)_jx_keyForState:(UIControlState)state {
    return [NSString stringWithFormat:@"%lu", (unsigned long)state];
}

+ (void)_jx_swizzleSetSelectedMethod {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        [self jx_swizzleInstanceMethod:@selector(setSelected:) withNewMethod:@selector(_jx_setSelected:)];
    });
}

+ (void)_jx_swizzleSetHighlightedMethod {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        [self jx_swizzleInstanceMethod:@selector(setHighlighted:) withNewMethod:@selector(_jx_setHighlighted:)];
    });
}

+ (void)_jx_swizzleSetEnabledMethod {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        [self jx_swizzleInstanceMethod:@selector(setEnabled:) withNewMethod:@selector(_jx_setEnabled:)];
    });
}

+ (void)_jx_swizzleSetBackgroundColorMethod {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        [self jx_swizzleInstanceMethod:@selector(setBackgroundColor:) withNewMethod:@selector(_jx_setBackgroundColor:)];
    });
}

- (void)_jx_setBackgroundColorForCurrentState {
    UIColor *color = [self _jx_backgroundColorForCurrentState];
    [self setBackgroundColor:color];
}

- (UIColor *)_jx_backgroundColorForCurrentState {
    return [self jx_backgroundColorForState:self.state];
}

- (void)_jx_setSelected:(BOOL)selected {
    [self _jx_setSelected:selected];
    
    [self _jx_setBackgroundColorForCurrentState];
}

- (void)_jx_setHighlighted:(BOOL)highlighted {
    [self _jx_setHighlighted:highlighted];
    
    [self _jx_setBackgroundColorForCurrentState];
}

- (void)_jx_setEnabled:(BOOL)enabled {
    [self _jx_setEnabled:enabled];
    
    [self _jx_setBackgroundColorForCurrentState];
}

- (void)_jx_setBackgroundColor:(UIColor *)backgroundColor {
    [self _jx_setBackgroundColor:backgroundColor];
    
    [self _jx_setBackgroundColor:backgroundColor forState:self.state];
}
 */

@end
