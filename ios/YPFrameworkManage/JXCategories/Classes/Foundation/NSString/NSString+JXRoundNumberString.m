//
//  NSString+JXRoundNumberString.m
//  Pods
//
//  Created by Colin on 17/2/10.
//
//

#import "NSString+JXRoundNumberString.h"
#import "NSDecimalNumber+JXBase.h"

@implementation NSString (JXRoundNumberString)

+ (NSString *)jx_stringFromFloat:(float)value fractionDigits:(NSUInteger)fractionDigits {
    NSNumber *number = [[NSNumber alloc] initWithFloat:value];
    return [NSString jx_stringFromNumber:number fractionDigits:fractionDigits];
}

+ (NSString *)jx_stringFromDouble:(double)value fractionDigits:(NSUInteger)fractionDigits {
    NSNumber *number = [[NSNumber alloc] initWithDouble:value];
    return [NSString jx_stringFromNumber:number fractionDigits:fractionDigits];
}

+ (NSString *)jx_stringFromNumber:(NSNumber *)number fractionDigits:(NSUInteger)fractionDigits {
    NSNumberFormatter *numberFormatter = [NSNumberFormatter new];
    [numberFormatter setMinimumIntegerDigits:1];
    [numberFormatter setMaximumFractionDigits:fractionDigits];
    [numberFormatter setMinimumFractionDigits:fractionDigits];
    return [numberFormatter stringFromNumber:number];
}

+ (NSString *)jx_stringFromFloat:(float)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode fractionDigits:(NSUInteger)fractionDigits {
    NSDecimalNumber *decimalNumber = [NSDecimalNumber jx_decimalNumberWithFloat:value roundingScale:scale roundingMode:mode];
    return [NSString jx_stringFromNumber:decimalNumber fractionDigits:fractionDigits];
}

+ (NSString *)jx_stringFromFloat:(float)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode fractionDigitsPadded:(BOOL)isPadded {
    NSDecimalNumber *decimalNumber = [NSDecimalNumber jx_decimalNumberWithFloat:value roundingScale:scale roundingMode:mode];
    if (!isPadded) return [NSString stringWithFormat:@"%@", decimalNumber];
    
    return [NSString jx_stringFromNumber:decimalNumber fractionDigits:scale];
}

+ (NSString *)jx_stringFromFloat:(float)value roundingScale:(short)scale fractionDigitsPadded:(BOOL)isPadded {
    return [NSString jx_stringFromFloat:value roundingScale:scale roundingMode:NSRoundPlain fractionDigitsPadded:isPadded];
}

+ (NSString *)jx_stringFromFloat:(float)value roundingScale:(short)scale {
    return [NSString jx_stringFromFloat:value roundingScale:scale roundingMode:NSRoundPlain fractionDigitsPadded:NO];
}

+ (NSString *)jx_stringFromDouble:(double)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode fractionDigits:(NSUInteger)fractionDigits {
    NSDecimalNumber *decimalNumber = [NSDecimalNumber jx_decimalNumberWithFloat:value roundingScale:scale roundingMode:mode];
    return [NSString jx_stringFromNumber:decimalNumber fractionDigits:fractionDigits];
}

+ (NSString *)jx_stringFromDouble:(double)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode fractionDigitsPadded:(BOOL)isPadded {
    NSDecimalNumber *decimalNumber = [NSDecimalNumber jx_decimalNumberWithFloat:value roundingScale:scale roundingMode:mode];
    if (!isPadded) return [NSString stringWithFormat:@"%@", decimalNumber];
    
    return [NSString jx_stringFromNumber:decimalNumber fractionDigits:scale];
}

+ (NSString *)jx_stringFromDouble:(double)value roundingScale:(short)scale fractionDigitsPadded:(BOOL)isPadded {
    return [NSString jx_stringFromDouble:value roundingScale:scale roundingMode:NSRoundPlain fractionDigitsPadded:isPadded];
}

+ (NSString *)jx_stringFromDouble:(double)value roundingScale:(short)scale {
    return [NSString jx_stringFromDouble:value roundingScale:scale roundingMode:NSRoundPlain fractionDigitsPadded:NO];
}

@end
