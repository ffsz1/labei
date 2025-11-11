//
//  NSDecimalNumber+HMRoundNumber.m
//  Pods
//
//  Created by Colin on 17/2/10.
//
//

#import "NSDecimalNumber+HMRoundNumber.h"

@implementation NSDecimalNumber (HMRoundNumber)

#pragma mark - Create
+ (NSDecimalNumber *)hm_decimalNumberWithFloat:(float)value roundingScale:(short)scale {
    return [[[NSDecimalNumber alloc] initWithFloat:value] hm_roundToScale:scale];
}

+ (NSDecimalNumber *)hm_decimalNumberWithFloat:(float)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode {
    return [[[NSDecimalNumber alloc] initWithFloat:value] hm_roundToScale:scale mode:mode];
}

+ (NSDecimalNumber *)hm_decimalNumberWithDouble:(double)value roundingScale:(short)scale {
    return [[[NSDecimalNumber alloc] initWithDouble:value] hm_roundToScale:scale];
}

+ (NSDecimalNumber *)hm_decimalNumberWithDouble:(double)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode {
    return [[[NSDecimalNumber alloc] initWithDouble:value] hm_roundToScale:scale mode:mode];
}

#pragma mark - Round
- (NSDecimalNumber *)hm_roundToScale:(short)scale {
    return [self hm_roundToScale:scale mode:NSRoundPlain];
}

- (NSDecimalNumber *)hm_roundToScale:(short)scale mode:(NSRoundingMode)roundingMode {
    NSDecimalNumberHandler *handler = [NSDecimalNumberHandler decimalNumberHandlerWithRoundingMode:roundingMode scale:scale raiseOnExactness:NO raiseOnOverflow:YES raiseOnUnderflow:YES raiseOnDivideByZero:YES];
    return [self decimalNumberByRoundingAccordingToBehavior:handler];
}

@end
