//
//  UIImageView+JXBase.m
//  JXCategories
//
//  Created by Colin on 2019/7/9.
//

#import "UIImageView+JXBase.h"
#import "NSObject+JXBase.h"

static const int JX_UI_IMAGE_VIEW_SCALE_INTRINSIC_CONTENT_SIZE_ENABLED_KEY;
static const int JX_UI_IMAGE_VIEW_PREFERRED_SCALE_INTRINSIC_CONTENT_HEIGHT_KEY;
static const int JX_UI_IMAGE_VIEW_PREFERRED_SCALE_INTRINSIC_CONTENT_WIDTH_KEY;

@implementation UIImageView (JXBase)

#pragma mark - Base
+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        SEL selectors[] = {
            @selector(intrinsicContentSize),
        };
        JXNSObjectSwizzleInstanceMethodsWithNewMethodPrefix(self, selectors, @"_jx_ui_image_view_");
    });
}

- (void)setJx_scaleIntrinsicContentSizeEnabled:(BOOL)jx_scaleIntrinsicContentSizeEnabled {
    [self jx_setAssociatedValue:@(jx_scaleIntrinsicContentSizeEnabled) withKey:&JX_UI_IMAGE_VIEW_SCALE_INTRINSIC_CONTENT_SIZE_ENABLED_KEY];
}

- (BOOL)jx_scaleIntrinsicContentSizeEnabled {
    return [[self jx_getAssociatedValueForKey:&JX_UI_IMAGE_VIEW_SCALE_INTRINSIC_CONTENT_SIZE_ENABLED_KEY] boolValue];
}

- (void)setJx_preferredScaleIntrinsicContentHeight:(CGFloat)jx_preferredScaleIntrinsicContentHeight {
    [self jx_setAssociatedValue:@(jx_preferredScaleIntrinsicContentHeight) withKey:&JX_UI_IMAGE_VIEW_PREFERRED_SCALE_INTRINSIC_CONTENT_HEIGHT_KEY];
}

- (CGFloat)jx_preferredScaleIntrinsicContentHeight {
    id value = [self jx_getAssociatedValueForKey:&JX_UI_IMAGE_VIEW_PREFERRED_SCALE_INTRINSIC_CONTENT_HEIGHT_KEY];
    if (!value) {
        value = @(-1);
    }
    return [value doubleValue];
}

- (void)setJx_preferredScaleIntrinsicContentWidth:(CGFloat)jx_preferredScaleIntrinsicContentWidth {
    [self jx_setAssociatedValue:@(jx_preferredScaleIntrinsicContentWidth) withKey:&JX_UI_IMAGE_VIEW_PREFERRED_SCALE_INTRINSIC_CONTENT_WIDTH_KEY];
}

- (CGFloat)jx_preferredScaleIntrinsicContentWidth {
    id value = [self jx_getAssociatedValueForKey:&JX_UI_IMAGE_VIEW_PREFERRED_SCALE_INTRINSIC_CONTENT_WIDTH_KEY];
    if (!value) {
        value = @(-1);
    }
    return [value doubleValue];
}

#pragma mark - Swizzle
- (CGSize)_jx_ui_image_view_intrinsicContentSize {
    CGSize size = [self _jx_ui_image_view_intrinsicContentSize];
    
    if (!self.jx_scaleIntrinsicContentSizeEnabled) return size;
    CGFloat imageWidth = self.image.size.width;
    CGFloat imageHeight = self.image.size.height;
    if (self.jx_preferredScaleIntrinsicContentHeight >= 0) {
        CGFloat scaledWidth = 0;
        if (imageHeight) {
            scaledWidth = imageWidth * self.jx_preferredScaleIntrinsicContentHeight / imageHeight;
        }
        size = CGSizeMake(scaledWidth, self.jx_preferredScaleIntrinsicContentHeight);
        return size;
    }
    
    if (self.jx_preferredScaleIntrinsicContentWidth >= 0) {
        CGFloat scaledHeight = 0;
        if (imageWidth) {
            scaledHeight = imageHeight * self.jx_preferredScaleIntrinsicContentWidth / imageWidth;
        }
        size = CGSizeMake(self.jx_preferredScaleIntrinsicContentWidth, scaledHeight);
        return size;
    }
    return size;
}

@end
