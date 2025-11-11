//
//  CALayer+JXBase.m
//  JXCategories
//
//  Created by Colin on 17/1/6.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "CALayer+JXBase.h"
#import "JXCoreGraphicHelper.h"
#import <UIKit/UIKit.h>

@implementation CALayer (JXBase)

#pragma mark - Base
- (UIViewContentMode)jx_contentMode {
    return JXCAGravityToUIViewContentMode(self.contentsGravity);
}

- (void)setJx_contentMode:(UIViewContentMode)contentMode {
    self.contentsGravity = JXUIViewContentModeToCAGravity(contentMode);
}

- (void)jx_exchangeSublayerAtIndex:(unsigned)index1 withSublayerAtIndex:(unsigned)index2 {
    CALayer *sublayer1 = [self.sublayers objectAtIndex:index1];
    if (!sublayer1) return;
    
    CALayer *sublayer2 = [self.sublayers objectAtIndex:index2];
    if (!sublayer2) return;
    
    if (index1 == index2) return;

    [sublayer1 removeFromSuperlayer];
    [sublayer2 removeFromSuperlayer];
    if (index1 > index2) {
        [self insertSublayer:sublayer1 atIndex:index2];
        [self insertSublayer:sublayer2 atIndex:index1];
    } else {
        [self insertSublayer:sublayer2 atIndex:index1];
        [self insertSublayer:sublayer1 atIndex:index2];
    }
}

- (void)jx_bringSublayerToFront:(CALayer *)sublayer {
    if (!sublayer) return;
    if (self == sublayer) return;
    if (![self.sublayers containsObject:sublayer]) return;
    
    [sublayer removeFromSuperlayer];
    [self insertSublayer:sublayer atIndex:(unsigned)self.sublayers.count];
}

- (void)jx_sendSublayerToBack:(CALayer *)sublayer {
    if (!sublayer) return;
    if (self == sublayer) return;
    if (![self.sublayers containsObject:sublayer]) return;
    
    [sublayer removeFromSuperlayer];
    [self insertSublayer:sublayer atIndex:0];
}

- (void)jx_bringToFront {
    if (self.superlayer) {
        [self.superlayer jx_bringSublayerToFront:self];
    }
}

- (void)jx_sendToBack {
    if (self.superlayer) {
        [self.superlayer jx_sendSublayerToBack:self];
    }
}

- (void)jx_removeSublayer:(CALayer *)sublayer {
    if ([self.sublayers containsObject:sublayer]) {
        [sublayer removeFromSuperlayer];
    }
}

- (void)jx_removeAllSublayers {
    while (self.sublayers.count) {
        [self.sublayers.lastObject removeFromSuperlayer];
    }
}

#pragma mark - Layout
- (CGFloat)jx_x {
    return self.frame.origin.x;
}

- (void)setJx_x:(CGFloat)x {
    CGRect frame = self.frame;
    frame.origin.x = x;
    self.frame = frame;
}

- (CGFloat)jx_y {
    return self.frame.origin.y;
}

- (void)setJx_y:(CGFloat)jx_y {
    CGRect frame = self.frame;
    frame.origin.y = jx_y;
    self.frame = frame;
}

- (CGPoint)jx_origin {
    return self.frame.origin;
}

- (void)setJx_origin:(CGPoint)origin {
    CGRect frame = self.frame;
    frame.origin = origin;
    self.frame = frame;
}

- (CGFloat)jx_width {
    return self.frame.size.width;
}

- (void)setJx_width:(CGFloat)width {
    CGRect frame = self.frame;
    frame.size.width = width;
    self.frame = frame;
}

- (CGFloat)jx_height {
    return self.frame.size.height;
}

- (void)setJx_height:(CGFloat)height {
    CGRect frame = self.frame;
    frame.size.height = height;
    self.frame = frame;
}

- (CGSize)jx_frameSize {
    return self.frame.size;
}

- (void)setJx_frameSize:(CGSize)size {
    CGRect frame = self.frame;
    frame.size = size;
    self.frame = frame;
}

- (CGFloat)jx_centerX {
    return self.frame.origin.x + self.frame.size.width * 0.5;
}

- (void)setJx_centerX:(CGFloat)centerX {
    CGRect frame = self.frame;
    frame.origin.x = centerX - frame.size.width * 0.5;
    self.frame = frame;
}

- (CGFloat)jx_centerY {
    return self.frame.origin.y + self.frame.size.height * 0.5;
}

- (void)setJx_centerY:(CGFloat)centerY {
    CGRect frame = self.frame;
    frame.origin.y = centerY - frame.size.height * 0.5;
    self.frame = frame;
}

- (CGPoint)jx_center {
    return CGPointMake(self.frame.origin.x + self.frame.size.width * 0.5,
                       self.frame.origin.y + self.frame.size.height * 0.5);
}

- (void)setJx_center:(CGPoint)center {
    CGRect frame = self.frame;
    frame.origin.x = center.x - frame.size.width * 0.5;
    frame.origin.y = center.y - frame.size.height * 0.5;
    self.frame = frame;
}

- (CGFloat)jx_left {
    return self.frame.origin.x;
}

- (void)setJx_left:(CGFloat)left {
    CGRect frame = self.frame;
    frame.origin.x = left;
    self.frame = frame;
}

- (CGFloat)jx_top {
    return self.frame.origin.y;
}

- (void)setJx_top:(CGFloat)top {
    CGRect frame = self.frame;
    frame.origin.y = top;
    self.frame = frame;
}

- (CGFloat)jx_right {
    return self.frame.origin.x + self.frame.size.width;
}

- (void)setJx_right:(CGFloat)right {
    CGRect frame = self.frame;
    frame.origin.x = right - frame.size.width;
    self.frame = frame;
}

- (CGFloat)jx_bottom {
    return self.frame.origin.y + self.frame.size.height;
}

- (void)setJx_bottom:(CGFloat)bottom {
    CGRect frame = self.frame;
    frame.origin.y = bottom - frame.size.height;
    self.frame = frame;
}

#pragma mark - Shadow
- (void)jx_setLayerShadow:(UIColor *)color offset:(CGSize)offset radius:(CGFloat)radius {
    [self jx_setLayerShadow:color offset:offset radius:radius opacity:1];
}

- (void)jx_setLayerShadow:(UIColor *)color offset:(CGSize)offset radius:(CGFloat)radius opacity:(CGFloat)opacity {
    self.shadowColor = color.CGColor;
    self.shadowOffset = offset;
    self.shadowRadius = radius;
    self.shadowOpacity = opacity;
    self.shouldRasterize = YES;
    self.rasterizationScale = [UIScreen mainScreen].scale;
}

#pragma mark - Snapshot
- (UIImage *)jx_snapshotImage {
    UIGraphicsBeginImageContextWithOptions(self.bounds.size, self.opaque, 0);
    CGContextRef context = UIGraphicsGetCurrentContext();
    [self renderInContext:context];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return image;
}

@end
