//
//  NSString+Utils.m
//  YYMobileFramework
//
//  Created by 小城 on 14-6-24.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//
#import "NSString+Utils.h"

@implementation NSString (Utils)

static NSNumberFormatter* numberFormatter()
{
    static id formatter = nil;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        formatter = [[NSNumberFormatter alloc] init];
    });
    return formatter;
}


- (BOOL)nim_isEmpty {
    if (!self) {
        return true;
    } else {
        NSCharacterSet *set = [NSCharacterSet whitespaceAndNewlineCharacterSet];
        NSString *trimedString = [self stringByTrimmingCharactersInSet:set];
        if ([trimedString length] == 0) {
            return true;
        } else {
            return false;
        }
    }
}

- (uint32_t)unsignedIntValue
{
    NSNumber *number = nil;
    @synchronized(numberFormatter()) {
        NSNumberFormatter * formatter = numberFormatter();
        number = [formatter numberFromString:self];
    }
    return [number unsignedIntValue];
}

- (uint16_t)unsignedShortValue {
    
    NSNumber *number = nil;
    @synchronized(numberFormatter()) {
        NSNumberFormatter * formatter = numberFormatter();
        number = [formatter numberFromString:self];
    }
    return [number unsignedShortValue];
}

+ (BOOL)stringContainsEmoji:(NSString *)string
{
    __block BOOL returnValue = NO;
    [string enumerateSubstringsInRange:NSMakeRange(0, [string length]) options:NSStringEnumerationByComposedCharacterSequences usingBlock:
     ^(NSString *substring, NSRange substringRange, NSRange enclosingRange, BOOL *stop) {
         
         const unichar hs = [substring characterAtIndex:0];
         // surrogate pair
         if (0xd800 <= hs && hs <= 0xdbff) {
             if (substring.length > 1) {
                 const unichar ls = [substring characterAtIndex:1];
                 const int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                 if (0x1d000 <= uc && uc <= 0x1f77f) {
                     returnValue = YES;
                 }
             }
         } else if (substring.length > 1) {
             const unichar ls = [substring characterAtIndex:1];
             if (ls == 0x20e3) {
                 returnValue = YES;
             }
             
         } else {
             // non surrogate
             if (0x2100 <= hs && hs <= 0x27ff) {
                 returnValue = YES;
             } else if (0x2B05 <= hs && hs <= 0x2b07) {
                 returnValue = YES;
             } else if (0x2934 <= hs && hs <= 0x2935) {
                 returnValue = YES;
             } else if (0x3297 <= hs && hs <= 0x3299) {
                 returnValue = YES;
             } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b || hs == 0x2b50) {
                 returnValue = YES;
             }
         }
     }];
    return returnValue;
}

+ (NSString*)numberStringFromNumber:(NSNumber*)number withPrefixString:(NSString*)prefix{
    if (number == nil)
    {
        return nil;
    }
    NSString *tmpPrefix = prefix;
    if (prefix != nil && ![prefix isEqualToString:@""])
    {
        tmpPrefix = [NSString stringWithFormat:@"%@：", prefix];
    }
    else
    {
        tmpPrefix = @"";
    }
    NSString *ret = [NSString stringWithFormat:@"%@%@", tmpPrefix, number];
    if (number.unsignedIntValue / 10000 >= 1)
    {
        ret = [NSString stringWithFormat:@"%@%.1f万", tmpPrefix, number.unsignedIntValue / 10000.0];
    }
    return ret;
}

+ (NSString *)convertTime:(CGFloat)second{
    NSUInteger hour = second/3600;
    NSUInteger minute = (second - hour * 3600)/60;
    NSUInteger seconds = (second - hour * 3600 - minute * 60);
    
    NSString *hourFormat = (hour >= 10 ? @"%lu" : @"0%lu");
    NSString *minuteFormat = (minute >= 10 ? @":%lu" : @":0%lu");
    NSString *secondFormat = (seconds >= 10 ? @":%lu" : @":0%lu");
    
    return [NSString stringWithFormat:[[hourFormat stringByAppendingString:minuteFormat] stringByAppendingString:secondFormat], hour, minute, seconds];
}
@end
