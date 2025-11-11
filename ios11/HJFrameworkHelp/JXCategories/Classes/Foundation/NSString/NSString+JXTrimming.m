//
//  NSString+JXTrimming.m
//  Pods
//
//  Created by Colin on 17/2/10.
//
//

#import "NSString+JXTrimming.h"
#import "NSString+JXBase.h"
#import "NSString+JXRegularExpression.h"

@implementation NSString (JXTrimming)

- (NSString *)jx_stringByTrimmingDecimalNumbers {
    static NSString *regex;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        regex = @"[0-9]";
    });
    return [self jx_stringByReplacingRegex:regex options:0 withString:@""];
}

- (NSString *)jx_stringByTrimmingLetters {
    static NSString *regex;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        regex = @"[a-zA-Z]";
    });
    return [self jx_stringByReplacingRegex:regex options:0 withString:@""];
}

- (NSString *)jx_stringByTrimmingUppercaseLetters {
    static NSString *regex;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        regex = @"[A-Z]";
    });
    return [self jx_stringByReplacingRegex:regex options:0 withString:@""];
}

- (NSString *)jx_stringByTrimmingLowercaseLetters {
    static NSString *regex;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        regex = @"[a-z]";
    });
    return [self jx_stringByReplacingRegex:regex options:0 withString:@""];
}

- (NSString *)jx_stringByTrimmingAlphanumericCharacters {
    static NSString *regex;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        regex = @"[0-9a-zA-Z]";
    });
    return [self jx_stringByReplacingRegex:regex options:0 withString:@""];
}

- (NSString *)jx_stringByTrimmingPunctuation {
    /*
     https://zh.wikipedia.org/wiki/Unicode%E5%AD%97%E7%AC%A6%E5%88%97%E8%A1%A8
     !"#$%&'()*+,-./
     :;<=>?@
     [\]^_`
     {|}~
     */
    static NSString *regex;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        regex = @"[\\U00000021-\\U0000002F\\U0000003A-\\U00000040\\U0000005B-\\U00000060\\U0000007B-\\U0000007E]";
    });
    return [self jx_stringByReplacingRegex:regex options:0 withString:@""];
}

- (NSString *)jx_stringByTrimmingCharacter:(unichar)character {
    NSString *string = [NSString stringWithFormat:@"%c", character];
    return [self stringByReplacingOccurrencesOfString:string withString:@""];
}

- (NSString *)jx_stringByTrimmingAllWhitespace {
    return [self stringByReplacingOccurrencesOfString:@" " withString:@""];
}

- (NSString *)jx_stringByTrimmingExtraWhitespace {
    if (!self.length) return self;
    
    NSString *string = [self jx_stringByTrimmingWhitespace];
    
    NSArray *components = [string componentsSeparatedByCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    components = [components filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"self <> ''"]];
    
    return [components componentsJoinedByString:@" "];
}

- (NSString *)jx_stringByTrimmingWhitespace {
    NSCharacterSet *set = [NSCharacterSet whitespaceCharacterSet];
    return [self stringByTrimmingCharactersInSet:set];
}

- (NSString *)jx_stringByTrimmingWhitespaceAndNewline {
    NSCharacterSet *set = [NSCharacterSet whitespaceAndNewlineCharacterSet];
    return [self stringByTrimmingCharactersInSet:set];
}

- (NSString *)jx_stringByTrimmingLineBreakCharacters {
    static NSString *regex;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        regex = @"[\r\n]";
    });
    return [self jx_stringByReplacingRegex:regex options:0 withString:@""];
}

- (NSString *)jx_stringByTrimmingUnknownCharacters {
    static NSString *regex;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        regex = @"[\u0300-\u036F]";
    });
    return [self jx_stringByReplacingRegex:regex options:0 withString:@""];
}

- (NSArray<NSString *> *)jx_substringsByTrimmingWhitespaceAndNewline {
    return [self jx_substringsInRange:self.jx_rangeOfAll usingBlock:^BOOL(NSString *substring, NSRange substringRange, BOOL *stop) {
        return substring.jx_stringByTrimmingWhitespaceAndNewline.length;
    }];
}

@end
