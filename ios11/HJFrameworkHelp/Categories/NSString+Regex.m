//
//  NSString+Regex.m
//  YYMobileFramework
//
//  Created by wuwei on 14/6/11.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import "NSString+Regex.h"

@implementation NSString (Regex)

- (BOOL)matchesRegex: (NSString *)pattern options:(NSRegularExpressionOptions)options
{
    NSError *error = nil;
    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:pattern options:options error:&error];
    if (regex == nil) {
        return NO;
    }
    NSUInteger n = [regex numberOfMatchesInString:self options:0 range:NSMakeRange(0, [self length])];
    return (n == 1);
}

- (BOOL)isPhoneNumber
{
    NSString *regex =@"^((1[3-8][0-9])|(147)|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    NSPredicate *pred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regex];
    return [pred evaluateWithObject:self];
}

- (BOOL)isPureNumber
{
    NSString*number=@"^[0-9]+$";
    NSPredicate*numberPre=[NSPredicate predicateWithFormat:@"SELF MATCHES %@",number];
    return [numberPre evaluateWithObject:self];
}

+ (BOOL) isEmpty:(NSString *) str {
    
    if (!str) {
        
        return true;
        
    } else {
        
        NSCharacterSet *set = [NSCharacterSet whitespaceAndNewlineCharacterSet];
        
        NSString *trimedString = [str stringByTrimmingCharactersInSet:set];
        
        if ([trimedString length] == 0) {
            
            return true;
            
        } else {
            
            return false;
            
        }
        
    }
    
}

@end
