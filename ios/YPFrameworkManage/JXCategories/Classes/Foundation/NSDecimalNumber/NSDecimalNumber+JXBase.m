//
//  NSDecimalNumber+JXBase.m
//  JXCategories
//
//  Created by Colin on 17/1/3.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "NSDecimalNumber+JXBase.h"

@implementation NSDecimalNumber (JXBase)

#pragma maek - Base

#pragma mark - Round
+ (NSDecimalNumber *)jx_decimalNumberWithFloat:(float)value roundingScale:(short)scale {
    return [[[NSDecimalNumber alloc] initWithFloat:value] jx_roundToScale:scale];
}

+ (NSDecimalNumber *)jx_decimalNumberWithFloat:(float)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode {
    return [[[NSDecimalNumber alloc] initWithFloat:value] jx_roundToScale:scale mode:mode];
}

+ (NSDecimalNumber *)jx_decimalNumberWithDouble:(double)value roundingScale:(short)scale {
    return [[[NSDecimalNumber alloc] initWithDouble:value] jx_roundToScale:scale];
}

+ (NSDecimalNumber *)jx_decimalNumberWithDouble:(double)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode {
    return [[[NSDecimalNumber alloc] initWithDouble:value] jx_roundToScale:scale mode:mode];
}

- (NSDecimalNumber *)jx_roundToScale:(short)scale {
    return [self jx_roundToScale:scale mode:NSRoundPlain];
}

- (NSDecimalNumber *)jx_roundToScale:(short)scale mode:(NSRoundingMode)roundingMode {
    NSDecimalNumberHandler *handler = [NSDecimalNumberHandler decimalNumberHandlerWithRoundingMode:roundingMode scale:scale raiseOnExactness:NO raiseOnOverflow:YES raiseOnUnderflow:YES raiseOnDivideByZero:YES];
    return [self decimalNumberByRoundingAccordingToBehavior:handler];
}

@end
