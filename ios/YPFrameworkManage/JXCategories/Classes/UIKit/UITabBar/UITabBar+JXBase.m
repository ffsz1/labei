//
//  UITabBar+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import "UITabBar+JXBase.h"
#import "NSObject+JXBase.h"

static const int JX_TAB_BAR_SHADOW_IMAGE_VIEW_BACKGROUND_COLOR_KEY;

@implementation UITabBar (JXBase)

#pragma mark - Base
+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        SEL selectors[] = {
            @selector(layoutSubviews),
        };
        JXNSObjectSwizzleInstanceMethodsWithNewMethodPrefix(self, selectors, @"jx_ui_tab_bar_");
    });
}

- (UIView *)jx_backgroundView {
     NSLog(@"==================feiy===5===================");
    return [self valueForKey:@"_backgroundView"];
     NSLog(@"==================feiy===6===================");
}

- (UIImageView *)jx_shadowImageView {
    NSLog(@"==================feiy===2===================");
    if (@available(iOS 10, *)) {
        return [self.jx_backgroundView valueForKey:@"_shadowView"];
    }
    
    //iOS10以前, shadowView在首次layoutSubviews后才创建
    UIImageView *shadowView = [self valueForKey:@"_shadowView"];
    if (!shadowView) {
        [self setNeedsLayout];
        [self layoutIfNeeded];
        shadowView = [self valueForKey:@"_shadowView"];
    }
    return shadowView;
}

- (void)setJx_shadowImageViewBackgroundColor:(UIColor *)color {
    [self jx_setAssociatedValue:color withKey:&JX_TAB_BAR_SHADOW_IMAGE_VIEW_BACKGROUND_COLOR_KEY];
}

- (UIColor *)jx_shadowImageViewBackgroundColor {
    UIColor *color = [self jx_getAssociatedValueForKey:&JX_TAB_BAR_SHADOW_IMAGE_VIEW_BACKGROUND_COLOR_KEY];
    if (!color) {
        color = self.jx_shadowImageView.backgroundColor;
        [self setJx_shadowImageViewBackgroundColor:color];
    }
    return color;
}

#pragma mark - Swizzle
- (void)_jx_ui_tab_bar_layoutSubviews {
    [self _jx_ui_tab_bar_layoutSubviews];
    
    self.jx_shadowImageView.backgroundColor = self.jx_shadowImageViewBackgroundColor;
}

@end
