//
//  UIView+JXTouchInset.m
//  Pods
//
//  Created by Colin on 17/2/10.
//
//

#import "UIView+JXTouchInset.h"
#import "NSObject+JXBase.h"

static const int JX_UI_VIEW_TOUCH_INSET_KEY;

@implementation UIView (JXTouchInset)

+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        SEL selectors[] = {
            @selector(pointInside:withEvent:),
        };
        JXNSObjectSwizzleInstanceMethodsWithNewMethodPrefix(self, selectors, @"_jx_ui_view_");
    });
}

- (void)setJx_touchInset:(UIEdgeInsets)touchInset {
    [self jx_setAssociatedValue:[NSValue valueWithUIEdgeInsets:touchInset] withKey:&JX_UI_VIEW_TOUCH_INSET_KEY];
}

- (UIEdgeInsets)jx_touchInset {
    return [[self jx_getAssociatedValueForKey:&JX_UI_VIEW_TOUCH_INSET_KEY] UIEdgeInsetsValue];
}

#pragma mark - Swizzle
- (BOOL)_jx_ui_view_pointInside:(CGPoint)point withEvent:(UIEvent *)event {
    if (UIEdgeInsetsEqualToEdgeInsets(self.jx_touchInset, UIEdgeInsetsZero) || self.hidden || ([self isKindOfClass:UIControl.class] && !((UIControl *)self).enabled)) {
        return [self _jx_ui_view_pointInside:point withEvent:event]; // original implementation
    }
    
    CGRect hitFrame = UIEdgeInsetsInsetRect(self.bounds, self.jx_touchInset);
    hitFrame.size.width = MAX(hitFrame.size.width, 0); // don't allow negative sizes
    hitFrame.size.height = MAX(hitFrame.size.height, 0);
    return CGRectContainsPoint(hitFrame, point);
}

@end
