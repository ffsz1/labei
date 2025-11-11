//
//  UIBezierPath+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/9.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "UIBezierPath+JXBase.h"
#import "NSArray+JXBase.h"

@implementation UIBezierPath (JXBase)

#pragma mark - Base
+ (UIBezierPath *)jx_bezierPathWithRoundedRect:(CGRect)rect cornerRadius:(NSArray<NSNumber *> *)cornerRadius lineWidth:(CGFloat)lineWidth {
    NSNumber *topLeftCorner = [cornerRadius jx_objectOrNilAtIndex:0];
    NSNumber *bottomLeftCorner = [cornerRadius jx_objectOrNilAtIndex:1];
    NSNumber *bottomRightCorner = [cornerRadius jx_objectOrNilAtIndex:2];
    NSNumber *topRightCorner = [cornerRadius jx_objectOrNilAtIndex:3];
    
    CGFloat topLeftCornerRadius = topLeftCorner ? topLeftCorner.floatValue : 0.f;
    CGFloat bottomLeftCornerRadius = bottomLeftCorner ? bottomLeftCorner.floatValue : 0.f;
    CGFloat bottomRightCornerRadius = bottomRightCorner ? bottomRightCorner.floatValue : 0.f;
    CGFloat topRightCornerRadius = topRightCorner ? topRightCorner.floatValue : 0.f;
    CGFloat lineCenter = lineWidth / 2.0;
    
    UIBezierPath *path = [UIBezierPath bezierPath];
    [path moveToPoint:CGPointMake(topLeftCornerRadius, lineCenter)];
    [path addArcWithCenter:CGPointMake(topLeftCornerRadius, topLeftCornerRadius) radius:topLeftCornerRadius - lineCenter startAngle:M_PI * 1.5 endAngle:M_PI clockwise:NO];
    [path addLineToPoint:CGPointMake(lineCenter, CGRectGetHeight(rect) - bottomLeftCornerRadius)];
    [path addArcWithCenter:CGPointMake(bottomLeftCornerRadius, CGRectGetHeight(rect) - bottomLeftCornerRadius) radius:bottomLeftCornerRadius - lineCenter startAngle:M_PI endAngle:M_PI * 0.5 clockwise:NO];
    [path addLineToPoint:CGPointMake(CGRectGetWidth(rect) - bottomRightCornerRadius, CGRectGetHeight(rect) - lineCenter)];
    [path addArcWithCenter:CGPointMake(CGRectGetWidth(rect) - bottomRightCornerRadius, CGRectGetHeight(rect) - bottomRightCornerRadius) radius:bottomRightCornerRadius - lineCenter startAngle:M_PI * 0.5 endAngle:0.0 clockwise:NO];
    [path addLineToPoint:CGPointMake(CGRectGetWidth(rect) - lineCenter, topRightCornerRadius)];
    [path addArcWithCenter:CGPointMake(CGRectGetWidth(rect) - topRightCornerRadius, topRightCornerRadius) radius:topRightCornerRadius - lineCenter startAngle:0.0 endAngle:M_PI * 1.5 clockwise:NO];
    [path closePath];
    
    return path;
}


@end
