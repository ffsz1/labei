//
//  NSValue+JXBase.m
//  JXCategories
//
//  Created by Colin on 17/1/3.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "NSValue+JXBase.h"
#import <CoreGraphics/CoreGraphics.h>

const NSRange JXNSRangeZero = {0, 0};

@implementation NSValue (JXBase)

#pragma mark - Base
+ (NSValue *)jx_valueWithCGColor:(CGColorRef)color {
    return [NSValue valueWithBytes:&color objCType:@encode(CGColorRef)];
}

- (CGColorRef)jx_CGColorValue {
    CGColorRef color;
    [self getValue:&color];
    return color;
}

@end
