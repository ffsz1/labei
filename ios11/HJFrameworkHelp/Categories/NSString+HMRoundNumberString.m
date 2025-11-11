//
//  NSString+HMRoundNumberString.m
//  Pods
//
//  Created by Colin on 17/2/10.
//
//

#import "NSString+HMRoundNumberString.h"
#import "NSDecimalNumber+HMRoundNumber.h"

@implementation NSString (HMRoundNumberString)

+ (NSString *)hm_stringFromFileSize:(int64_t)byte {
    if (byte < 0) return nil;
    // Byte
    if (byte < pow(10, 3)) return [NSString stringWithFormat:@"%lldByte", byte];
    
    NSDecimalNumber *number = nil;
    // KB
    if (byte < pow(10, 6)) {
        number = [NSDecimalNumber hm_decimalNumberWithDouble:byte/pow(10, 3) roundingScale:1];
        return [NSString stringWithFormat:@"%@KB", number];
    }
    // MB
    if (byte < pow(10, 9)) {
        number = [NSDecimalNumber hm_decimalNumberWithDouble:byte/pow(10, 6) roundingScale:1];
        return [NSString stringWithFormat:@"%@MB", number];
    }
    // GB
    number = [NSDecimalNumber hm_decimalNumberWithDouble:byte/pow(10, 9) roundingScale:1];
    return [NSString stringWithFormat:@"%@GB", number];
}

+ (NSString *)hm_stringFromFloat:(float)value fractionDigits:(NSUInteger)fractionDigits {
    NSNumber *number = [[NSNumber alloc] initWithFloat:value];
    return [NSString hm_stringFromNumber:number fractionDigits:fractionDigits];
}

+ (NSString *)hm_stringFromDouble:(double)value fractionDigits:(NSUInteger)fractionDigits {
    NSNumber *number = [[NSNumber alloc] initWithDouble:value];
    return [NSString hm_stringFromNumber:number fractionDigits:fractionDigits];
}

+ (NSString *)hm_stringFromNumber:(NSNumber *)number fractionDigits:(NSUInteger)fractionDigits {
    NSNumberFormatter *numberFormatter = [NSNumberFormatter new];
    [numberFormatter setMinimumIntegerDigits:1];
    [numberFormatter setMaximumFractionDigits:fractionDigits];
    [numberFormatter setMinimumFractionDigits:fractionDigits];
    return [numberFormatter stringFromNumber:number];
}

+ (NSString *)hm_stringFromFloat:(float)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode fractionDigits:(NSUInteger)fractionDigits {
    NSDecimalNumber *decimalNumber = [NSDecimalNumber hm_decimalNumberWithFloat:value roundingScale:scale roundingMode:mode];
    return [NSString hm_stringFromNumber:decimalNumber fractionDigits:fractionDigits];
}

+ (NSString *)hm_stringFromFloat:(float)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode fractionDigitsPadded:(BOOL)isPadded {
    NSDecimalNumber *decimalNumber = [NSDecimalNumber hm_decimalNumberWithFloat:value roundingScale:scale roundingMode:mode];
    if (!isPadded) return [NSString stringWithFormat:@"%@", decimalNumber];
    
    return [NSString hm_stringFromNumber:decimalNumber fractionDigits:scale];
}

+ (NSString *)hm_stringFromFloat:(float)value roundingScale:(short)scale fractionDigitsPadded:(BOOL)isPadded {
    return [NSString hm_stringFromFloat:value roundingScale:scale roundingMode:NSRoundPlain fractionDigitsPadded:isPadded];
}

+ (NSString *)hm_stringFromFloat:(float)value roundingScale:(short)scale {
    return [NSString hm_stringFromFloat:value roundingScale:scale roundingMode:NSRoundPlain fractionDigitsPadded:NO];
}

+ (NSString *)hm_stringFromDouble:(double)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode fractionDigits:(NSUInteger)fractionDigits {
    NSDecimalNumber *decimalNumber = [NSDecimalNumber hm_decimalNumberWithFloat:value roundingScale:scale roundingMode:mode];
    return [NSString hm_stringFromNumber:decimalNumber fractionDigits:fractionDigits];
}

+ (NSString *)hm_stringFromDouble:(double)value roundingScale:(short)scale roundingMode:(NSRoundingMode)mode fractionDigitsPadded:(BOOL)isPadded {
    NSDecimalNumber *decimalNumber = [NSDecimalNumber hm_decimalNumberWithFloat:value roundingScale:scale roundingMode:mode];
    if (!isPadded) return [NSString stringWithFormat:@"%@", decimalNumber];
    
    return [NSString hm_stringFromNumber:decimalNumber fractionDigits:scale];
}

+ (NSString *)hm_stringFromDouble:(double)value roundingScale:(short)scale fractionDigitsPadded:(BOOL)isPadded {
    return [NSString hm_stringFromDouble:value roundingScale:scale roundingMode:NSRoundPlain fractionDigitsPadded:isPadded];
}

+ (NSString *)hm_stringFromDouble:(double)value roundingScale:(short)scale {
    return [NSString hm_stringFromDouble:value roundingScale:scale roundingMode:NSRoundPlain fractionDigitsPadded:NO];
}

@end
