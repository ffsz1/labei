//
//  YYStringTokenizer.m
//  YYMobileFramework
//
//  Created by wuwei on 14/7/2.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import <CoreFoundation/CoreFoundation.h>
#import "YYStringTokenizer.h"

@implementation YYStringTokenizer

+ (NSString *)determineLanuageWithString:(NSString *)string
{
    CFRange stringRange = CFRangeMake(0, string.length);
    CFLocaleRef locale = CFLocaleCopyCurrent();
    CFStringTokenizerRef tokenizerRef = CFStringTokenizerCreate(NULL, (__bridge CFStringRef)string, stringRange, kCFStringTokenizerUnitParagraph, locale);
    
    CFStringTokenizerAdvanceToNextToken(tokenizerRef);
    
    CFTypeRef languageAttr = CFStringTokenizerCopyCurrentTokenAttribute(tokenizerRef, kCFStringTokenizerAttributeLanguage);
    
    CFRelease(tokenizerRef);
    CFRelease(locale);
    
    return (NSString *)CFBridgingRelease(languageAttr);
}

+ (NSArray *)analyzeTokensWithString:(NSString *)string
{
    CFRange stringRange = CFRangeMake(0, string.length);
    
    CFLocaleRef locale = CFLocaleCopyCurrent();
    CFStringTokenizerRef tokenizerRef = CFStringTokenizerCreate(NULL, (__bridge CFStringRef)string, stringRange, kCFStringTokenizerUnitWordBoundary, locale);
    
    NSMutableArray *tokens = [NSMutableArray array];
    while (1) {
        
        CFStringTokenizerTokenType type = CFStringTokenizerAdvanceToNextToken(tokenizerRef);
        if (type == kCFStringTokenizerTokenNone) {
            break;
        }
        
        CFRange r = CFStringTokenizerGetCurrentTokenRange(tokenizerRef);
        if (r.location == kCFNotFound && r.length == 0) {
            break;
        }
        
        NSString *token = [NSString stringWithString:[string substringWithRange:NSMakeRange(r.location, r.length)]];
        CFTypeRef yomiKana = CFStringTokenizerCopyCurrentTokenAttribute(tokenizerRef, kCFStringTokenizerAttributeLatinTranscription);
        CFTypeRef languageAttr = CFStringTokenizerCopyCurrentTokenAttribute(tokenizerRef, kCFStringTokenizerAttributeLanguage);
        
        [tokens addObject:token];
        
        NSLog(@"token = %@, Yomikana = %@, language = %@.", token, (__bridge NSString *)yomiKana, (__bridge NSString *)languageAttr);
        
        if (kCFStringTokenizerTokenHasSubTokensMask & type || kCFStringTokenizerTokenHasDerivedSubTokensMask & type) {
        
            CFRange *range = NULL;
            CFIndex maxRangeLength = 0;
            NSMutableArray *strings = nil;
            CFIndex numRanges = CFStringTokenizerGetCurrentSubTokens(tokenizerRef, range, maxRangeLength, (CFMutableArrayRef)strings);
            NSLog(@"rangeCount = (%ld)", numRanges);
            [tokens addObjectsFromArray:strings];
        }
        
        CFRelease(yomiKana);
        CFRelease(languageAttr);
    }
    
    CFRelease(locale);
    CFRelease(tokenizerRef);
    
    return tokens;
}

@end
