//
//  UINavigationBar+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import "UINavigationBar+JXBase.h"
#import "NSObject+JXBase.h"

static const int JX_UI_NAVIGATION_SHADOW_IMAGE_VIEW_BACKGROUND_COLOR_KEY;

@implementation UINavigationBar (JXBase)

#pragma mark - Base
+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        SEL selectors[] = {
            @selector(layoutSubviews),
        };
        JXNSObjectSwizzleInstanceMethodsWithNewMethodPrefix(self, selectors, @"_jx_ui_navigation_bar_");
    });
}
//life
- (UIView *)jx_backgroundView {
    NSLog(@"==================feiy====3==================");
    UIView *barBackgroundView = self.subviews.firstObject;
       if (@available(iOS 13.0, *)) {
//           barBackgroundView.alpha = 0.5;
            NSLog(@"==================feiy====3=========1=========");
           NSLog(@"-00000---%@----",barBackgroundView.subviews);
           return barBackgroundView;
       }
    NSLog(@"==================feiy====4==================");
    return [self valueForKey:@"_backgroundView"];
}

- (__kindof UIView *)jx_backgroundContentView {
    UIView *view = nil;
    if (@available(iOS 10, *)) {
        UIImageView *imageView = [self.jx_backgroundView valueForKey:@"_backgroundImageView"];
        UIVisualEffectView *visualEffectView = [self.jx_backgroundView valueForKey:@"_backgroundEffectView"];
        UIView *customView = [self.jx_backgroundView valueForKey:@"_customBackgroundView"];
        view = customView && customView.superview ? customView : (imageView && imageView.superview ? imageView : visualEffectView);
    } else {
        UIView *backdropView = [self.jx_backgroundView valueForKey:@"_adaptiveBackdrop"];
        view = backdropView && backdropView.superview ? backdropView : self.jx_backgroundView;
    }
    return view;
}

- (UIImageView *)jx_shadowImageView {
     NSLog(@"==================feiy=1=====================");
    if (@available(iOS 13.0, *)) {
           return [[UIImageView alloc] init];
       }
    return [self.jx_backgroundView valueForKey:@"_shadowView"];
}

- (void)setJx_shadowImageViewBackgroundColor:(UIColor *)color {
    [self jx_setAssociatedValue:color withKey:&JX_UI_NAVIGATION_SHADOW_IMAGE_VIEW_BACKGROUND_COLOR_KEY];
}

- (UIColor *)jx_shadowImageViewBackgroundColor {
    UIColor *color = [self jx_getAssociatedValueForKey:&JX_UI_NAVIGATION_SHADOW_IMAGE_VIEW_BACKGROUND_COLOR_KEY];
    if (!color) {
        color = self.jx_shadowImageView.backgroundColor;
        [self setJx_shadowImageViewBackgroundColor:color];
    }
    return color;
}

#pragma mark - Swizzle
- (void)_jx_ui_navigation_bar_layoutSubviews {
    [self _jx_ui_navigation_bar_layoutSubviews];
    
    self.jx_shadowImageView.backgroundColor = self.jx_shadowImageViewBackgroundColor;
}

@end
