//
//  NSString+JXBase.m
//  JXCategories
//
//  Created by Colin on 16/12/31.
//  Copyright © 2016年 Colin. All rights reserved.
//

#import "NSString+JXBase.h"
#import "NSCharacterSet+JXBase.h"
#import "NSData+JXBase.h"
#import "NSDecimalNumber+JXBase.h"
#import "NSNumber+JXBase.h"

@implementation NSString (JXBase)

#pragma mark - Base
- (NSRange)jx_rangeOfAll {
    return NSMakeRange(0, self.length);
}

+ (NSString *)jx_stringNamed:(NSString *)name {
    NSString *path = [[NSBundle mainBundle] pathForResource:name ofType:@""];
    NSString *str = [NSString stringWithContentsOfFile:path encoding:NSUTF8StringEncoding error:NULL];
    if (!str) {
        path = [[NSBundle mainBundle] pathForResource:name ofType:@"txt"];
        str = [NSString stringWithContentsOfFile:path encoding:NSUTF8StringEncoding error:NULL];
    }
    return str;
}

- (NSUInteger)jx_lengthOfUsingNonASCIICharacterAsTwoEncoding {
    NSUInteger length = 0;
    for (NSUInteger i = 0, l = self.length; i < l; i++) {
        unichar character = [self characterAtIndex:i];
        if (isascii(character)) {
            length += 1;
        } else {
            length += 2;
        }
    }
    return length;
}

+ (NSString *)jx_stringByConcat:(id)obj, ... {
    if (obj) {
        NSMutableString *result = [[NSMutableString alloc] initWithFormat:@"%@", obj];

        va_list argumentList;
        va_start(argumentList, obj);
        id argument;
        while ((argument = va_arg(argumentList, id))) {
            [result appendFormat:@"%@", argument];
        }
        va_end(argumentList);

        return [result copy];
    }
    return @"";
}

#pragma mark - Substring
- (NSArray<NSValue *> *)jx_rangesOfSubstring:(NSString *)substring {
    return [self jx_rangesOfSubstring:substring inRange:self.jx_rangeOfAll];
}

- (NSArray<NSValue *> *)jx_rangesOfSubstring:(NSString *)substring inRange:(NSRange)range {
    __block NSMutableArray<NSValue *> *buffer = @[].mutableCopy;
    if (!substring.length) return buffer.copy;
    
    [self enumerateSubstringsInRange:range options:NSStringEnumerationByComposedCharacterSequences usingBlock:^(NSString * _Nullable aSubstring, NSRange substringRange, NSRange enclosingRange, BOOL * _Nonnull stop) {
        if ([aSubstring isEqualToString:substring]) {
            [buffer addObject:[NSValue valueWithRange:substringRange]];
        }
    }];
    return buffer.copy;
}

- (NSArray<NSString *> *)jx_substrings {
    return [self jx_substringsInRange:self.jx_rangeOfAll];
}

- (NSArray<NSString *> *)jx_substringsInRange:(NSRange)range {
    NSMutableArray<NSString *> *buffer = @[].mutableCopy;
    for (NSInteger i = range.location; i < range.length; i++) {
        NSString *substring = [self substringWithRange:NSMakeRange(i, 1)];
        [buffer addObject:substring];
    }
    return buffer.copy;
}

- (NSArray<NSString *> *)jx_substringsInRange:(NSRange)range usingBlock:(BOOL (^)(NSString * _Nullable substring, NSRange substringRange, BOOL *stop))block {
    NSMutableArray<NSString *> *buffer = @[].mutableCopy;
    BOOL contains = YES;
    BOOL stop = NO;
    
    for (NSInteger i = range.location; i < range.length; i++) {
        NSRange range = NSMakeRange(i, 1);
        NSString *substring = [self substringWithRange:range];
        if (block) {
            contains = block(substring, range, &stop);
        }
        
        if (contains) {
            [buffer addObject:substring];
        }
        
        if (stop) break;
    }
    return buffer.copy;
}

- (NSArray<NSString *> *)jx_substringsInRange:(NSRange)range options:(NSStringEnumerationOptions)opts {
    return [self jx_substringsInRange:range options:opts usingBlock:^BOOL(NSString * _Nonnull substring, NSRange substringRange, NSRange enclosingRange, BOOL * _Nonnull stop) {
        return YES;
    }];
}

- (NSArray<NSString *> *)jx_substringsInRange:(NSRange)range options:(NSStringEnumerationOptions)opts usingBlock:(BOOL (^)(NSString * _Nullable substring, NSRange substringRange, NSRange enclosingRange, BOOL *stop))block {
    __block NSMutableArray<NSString *> *buffer = @[].mutableCopy;
    __block BOOL shoudAdd = YES;
    [self enumerateSubstringsInRange:range options:opts usingBlock:^(NSString * _Nullable aSubstring, NSRange substringRange, NSRange enclosingRange, BOOL * _Nonnull stop) {
        if (block) {
            shoudAdd = block(aSubstring, substringRange, enclosingRange, stop);
        }
        
        if (shoudAdd) {
            [buffer addObject:aSubstring];
        }
    }];
    return buffer.copy;
}

- (NSRange)_jx_rangeOfComposedCharacterSequencesForRange:(NSRange)range roundDown:(BOOL)roundDown {
    if (!roundDown) {
        return [self rangeOfComposedCharacterSequencesForRange:range];
    }
    
    if (range.length == 0) {
        return range;
    }
    
    NSRange buffer = [self rangeOfComposedCharacterSequencesForRange:range];
    if (NSMaxRange(buffer) > NSMaxRange(range)) {
        return [self _jx_rangeOfComposedCharacterSequencesForRange:NSMakeRange(range.location, range.length - 1) roundDown:YES];
    }
    return buffer;
}

- (NSUInteger)_jx_indexByUsingNonASCIICharacterAsTwoEncodingForIndex:(NSUInteger)index {
    CGFloat strlength = 0.f;
    NSUInteger i = 0;
    for (i = 0; i < self.length; i++) {
        unichar character = [self characterAtIndex:i];
        if (isascii(character)) {
            strlength += 1;
        } else {
            strlength += 2;
        }
        if (strlength >= index + 1) return i;
    }
    return 0;
}

- (NSRange)_jx_rangeByUsingNonASCIICharacterAsTwoEncodingForRange:(NSRange)range {
    CGFloat strlength = 0.f;
    NSRange resultRange = NSMakeRange(NSNotFound, 0);
    NSUInteger i = 0;
    for (i = 0; i < self.length; i++) {
        unichar character = [self characterAtIndex:i];
        if (isascii(character)) {
            strlength += 1;
        } else {
            strlength += 2;
        }
        if (strlength >= range.location + 1) {
            if (resultRange.location == NSNotFound) {
                resultRange.location = i;
            }
            
            if (range.length > 0 && strlength >= NSMaxRange(range)) {
                resultRange.length = i - resultRange.location + (strlength == NSMaxRange(range) ? 1 : 0);
                return resultRange;
            }
        }
    }
    return resultRange;
}

- (NSString *)jx_substringFromIndex:(NSUInteger)from option:(JXNSStringSubstringConversionOptions)option {
    if (!option) {
        return [self substringFromIndex:from];
    }
    
    if (option & JXNSStringSubstringConversionByAvoidBreakingUpCharacterSequences) {
        NSUInteger aIndex = (option & JXNSStringSubstringConversionByUsingNonASCIICharacterAsTwoEncoding) ? [self _jx_indexByUsingNonASCIICharacterAsTwoEncodingForIndex:from] : from;
        
        NSRange buffer = [self rangeOfComposedCharacterSequenceAtIndex:aIndex];
        aIndex = (option & JXNSStringSubstringConversionByCharacterSequencesLengthRequired) ? NSMaxRange(buffer) : buffer.location;
        
        return [self substringFromIndex:aIndex];
    }
    return @"";
}

- (NSString *)jx_substringToIndex:(NSUInteger)to option:(JXNSStringSubstringConversionOptions)option {
    if (!option) {
        return [self substringToIndex:to];
    }
    
    if (option & JXNSStringSubstringConversionByAvoidBreakingUpCharacterSequences) {
        NSUInteger aIndex = (option & JXNSStringSubstringConversionByUsingNonASCIICharacterAsTwoEncoding) ? [self _jx_indexByUsingNonASCIICharacterAsTwoEncodingForIndex:to] : to;
        
        NSRange buffer = [self rangeOfComposedCharacterSequenceAtIndex:aIndex];
        aIndex = (option & JXNSStringSubstringConversionByCharacterSequencesLengthRequired) ? buffer.location : NSMaxRange(buffer);
        
        return [self substringToIndex:aIndex];
    }
    return @"";
}

- (NSString *)jx_substringWithRange:(NSRange)range option:(JXNSStringSubstringConversionOptions)option {
    if (!option) {
        return [self substringWithRange:range];
    }
    
    if (option & JXNSStringSubstringConversionByAvoidBreakingUpCharacterSequences) {
        NSRange aRange = range;
        if (option & JXNSStringSubstringConversionByUsingNonASCIICharacterAsTwoEncoding) {
            aRange = [self _jx_rangeByUsingNonASCIICharacterAsTwoEncodingForRange:aRange];
        }
        
        aRange = (option & JXNSStringSubstringConversionByCharacterSequencesLengthRequired) ? [self _jx_rangeOfComposedCharacterSequencesForRange:aRange roundDown:YES] : [self rangeOfComposedCharacterSequencesForRange:aRange];

        return [self substringWithRange:aRange];
    }
    return @"";
}

#pragma mark - Composed Character Sequences
- (NSUInteger)jx_lengthOfComposedCharacterSequences {
    if (!self.length) return 0;
    
    __block NSUInteger length = 0;
    [self enumerateSubstringsInRange:self.jx_rangeOfAll options:NSStringEnumerationByComposedCharacterSequences usingBlock:^(NSString * _Nullable substring, NSRange substringRange, NSRange enclosingRange, BOOL * _Nonnull stop) {
        if (substring) {
            length++;
        }
    }];
    return length;
}

- (NSString *)jx_firstComposedCharacterSequences {
    return [self jx_composedCharacterSequencesAtIndex:0];
}

- (NSString *)jx_lastComposedCharacterSequences {
    return [self jx_composedCharacterSequencesAtIndex:self.length-1];
}

- (NSString *)jx_composedCharacterSequencesAtIndex:(NSUInteger)index {
    if (!self.length) return @"";

    return [self substringWithRange:[self rangeOfComposedCharacterSequenceAtIndex:index]];
}

- (NSString *)jx_composedCharacterSequencesFromIndex:(NSUInteger)index {
    if (!self.length) return @"";
    
    return [self substringFromIndex:[self rangeOfComposedCharacterSequenceAtIndex:index].location];
}

- (NSString *)jx_composedCharacterSequencesToIndex:(NSUInteger)index {
    if (!self.length) return @"";
    
    return [self substringToIndex:NSMaxRange([self rangeOfComposedCharacterSequenceAtIndex:index])];
}

- (NSString *)jx_composedCharacterSequencesWithRange:(NSRange)range {
    if (!self.length) return @"";
    
    return [self substringWithRange:[self rangeOfComposedCharacterSequencesForRange:range]];
}

- (NSArray<NSString *> *)jx_composedCharacterSequences {
    return [self jx_substringsInRange:self.jx_rangeOfAll options:NSStringEnumerationByComposedCharacterSequences];
}

- (NSString *)jx_stringByReplacingComposedCharacterSequencesWithString:(NSString *)replacement {
    __block NSString *tempString = [NSString stringWithString:self];
    [self enumerateSubstringsInRange:self.jx_rangeOfAll options:NSStringEnumerationByComposedCharacterSequences usingBlock:^(NSString * _Nullable substring, NSRange substringRange, NSRange enclosingRange, BOOL * _Nonnull stop) {
        if (![substring isEqualToString:replacement]) {
            tempString = [tempString stringByReplacingOccurrencesOfString:substring withString:replacement];
        }
    }];
    return tempString;
}

- (NSString *)jx_stringByRemovingFirstComposedCharacterSequence {
    NSMutableString *string = [[NSMutableString alloc] initWithString:self];
    [string jx_deleteFirstComposedCharacterSequence];
    return string.copy;
}

- (NSString *)jx_stringByRemovingLastComposedCharacterSequence {
    NSMutableString *string = [[NSMutableString alloc] initWithString:self];
    [string jx_deleteLastComposedCharacterSequence];
    return string.copy;
}

- (NSString *)jx_stringByRemovingComposedCharacterSequenceAtIndex:(NSUInteger)index {
    NSMutableString *string = [[NSMutableString alloc] initWithString:self];
    [string jx_deleteComposedCharacterSequenceAtIndex:index];
    return string.copy;
}

- (NSString *)jx_stringByRemovingComposedCharacterSequenceInRange:(NSRange)range {
    NSMutableString *string = [[NSMutableString alloc] initWithString:self];
    [string jx_deleteComposedCharacterSequenceInRange:range];
    return string.copy;
}

#pragma mark - Check
- (BOOL)jx_isNotBlank {
    NSCharacterSet *blank = [NSCharacterSet whitespaceAndNewlineCharacterSet];
    for (NSInteger i = 0; i < self.length; ++i) {
        unichar c = [self characterAtIndex:i];
        if (![blank characterIsMember:c]) {
            return YES;
        }
    }
    return NO;
}

- (BOOL)jx_isPureInt {
    NSScanner *scan = [NSScanner scannerWithString:self];
    int val;
    return [scan scanInt:&val] && [scan isAtEnd];
}

- (BOOL)jx_isPureFloat {
    NSScanner *scan = [NSScanner scannerWithString:self];
    float val;
    return [scan scanFloat:&val] && [scan isAtEnd];
}

- (BOOL)jx_containsString:(NSString *)string {
    if (string == nil) return NO;
    return [self rangeOfString:string].location != NSNotFound;
}

- (BOOL)jx_containsCharacterSet:(NSCharacterSet *)set {
    if (set == nil) return NO;
    return [self rangeOfCharacterFromSet:set].location != NSNotFound;
}

#pragma mark - Hex String
- (NSString *)jx_hexString {
    NSData *data = self.jx_dataValue;
    return data.jx_hexString;
}

+ (NSString *)jx_stringWithHexString:(NSString *)hexString {
    NSData *data = [NSData jx_dataWithHexString:hexString];
    return data.jx_utf8String;
}

#pragma mark - URL String
- (NSString *)jx_stringByURLEncode {
    if ([self respondsToSelector:@selector(stringByAddingPercentEncodingWithAllowedCharacters:)]) {
        /**
         AFNetworking/AFURLRequestSerialization.m
         
         Returns a percent-escaped string following RFC 3986 for a query string key or value.
         RFC 3986 states that the following characters are "reserved" characters.
         - General Delimiters: ":", "#", "[", "]", "@", "?", "/"
         - Sub-Delimiters: "!", "$", "&", "'", "(", ")", "*", "+", ",", ";", "="
         In RFC 3986 - Section 3.4, it states that the "?" and "/" characters should not be escaped to allow
         query strings to include a URL. Therefore, all "reserved" characters with the exception of "?" and "/"
         should be percent-escaped in the query string.
         - parameter string: The string to be percent-escaped.
         - returns: The percent-escaped string.
         */
        static NSString * const kAFCharactersGeneralDelimitersToEncode = @":#[]@"; // does not include "?" or "/" due to RFC 3986 - Section 3.4
        static NSString * const kAFCharactersSubDelimitersToEncode = @"!$&'()*+,;=";
        
        NSMutableCharacterSet * allowedCharacterSet = [[NSCharacterSet URLQueryAllowedCharacterSet] mutableCopy];
        [allowedCharacterSet removeCharactersInString:[kAFCharactersGeneralDelimitersToEncode stringByAppendingString:kAFCharactersSubDelimitersToEncode]];
        static NSUInteger const batchSize = 50;
        
        NSUInteger index = 0;
        NSMutableString *escaped = @"".mutableCopy;
        
        while (index < self.length) {
            NSUInteger length = MIN(self.length - index, batchSize);
            NSRange range = NSMakeRange(index, length);
            // To avoid breaking up character sequences such as 👴🏻👮🏽
            range = [self rangeOfComposedCharacterSequencesForRange:range];
            NSString *substring = [self substringWithRange:range];
            NSString *encoded = [substring stringByAddingPercentEncodingWithAllowedCharacters:allowedCharacterSet];
            [escaped appendString:encoded];
            
            index += range.length;
        }
        return escaped.copy;
    } else {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
        CFStringEncoding cfEncoding = CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding);
        NSString *encoded = (__bridge_transfer NSString *)
        CFURLCreateStringByAddingPercentEscapes(
                                                kCFAllocatorDefault,
                                                (__bridge CFStringRef)self,
                                                NULL,
                                                CFSTR("!#$&'()*+,/:;=?@[]"),
                                                cfEncoding);
        return encoded;
#pragma clang diagnostic pop
    }
}

- (NSString *)jx_stringByURLDecode {
    if ([self respondsToSelector:@selector(stringByRemovingPercentEncoding)]) {
        return [self stringByRemovingPercentEncoding];
    } else {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
        CFStringEncoding en = CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding);
        NSString *decoded = [self stringByReplacingOccurrencesOfString:@"+"
                                                            withString:@" "];
        decoded = (__bridge_transfer NSString *)
        CFURLCreateStringByReplacingPercentEscapesUsingEncoding(
                                                                NULL,
                                                                (__bridge CFStringRef)decoded,
                                                                CFSTR(""),
                                                                en);
        return decoded;
#pragma clang diagnostic pop
    }
}

- (NSString *)jx_stringByURLQueryUserInputEncode {
    return [self stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet jx_URLQueryUserInputAllowedCharacterSet]];
}

- (NSString *)jx_stringByEscapingHTML {
    NSUInteger len = self.length;
    if (!len) return self;
    
    unichar *buf = malloc(sizeof(unichar) * len);
    if (!buf) return self;
    [self getCharacters:buf range:NSMakeRange(0, len)];
    
    NSMutableString *result = [NSMutableString string];
    for (int i = 0; i < len; i++) {
        unichar c = buf[i];
        NSString *esc = nil;
        switch (c) {
            case 34: esc = @"&quot;"; break;
            case 38: esc = @"&amp;"; break;
            case 39: esc = @"&apos;"; break;
            case 60: esc = @"&lt;"; break;
            case 62: esc = @"&gt;"; break;
            default: break;
        }
        if (esc) {
            [result appendString:esc];
        } else {
            CFStringAppendCharacters((CFMutableStringRef)result, &c, 1);
        }
    }
    free(buf);
    return result.copy;
}

#pragma mark - UTF32 String
+ (NSString *)jx_stringWithUTF32Char:(UTF32Char)char32 {
    char32 = NSSwapHostIntToLittle(char32);
    return [[NSString alloc] initWithBytes:&char32 length:4 encoding:NSUTF32LittleEndianStringEncoding];
}

+ (NSString *)jx_stringWithUTF32Chars:(const UTF32Char *)char32 length:(NSUInteger)length {
    return [[NSString alloc] initWithBytes:(const void *)char32
                                    length:length * 4
                                  encoding:NSUTF32LittleEndianStringEncoding];
}

- (void)jx_enumerateUTF32CharInRange:(NSRange)range usingBlock:(void (^)(UTF32Char char32, NSRange range, BOOL *stop))block {
    NSString *str = self;
    if (range.location != 0 || range.length != self.length) {
        str = [self substringWithRange:range];
    }
    NSUInteger len = [str lengthOfBytesUsingEncoding:NSUTF32StringEncoding] / 4;
    UTF32Char *char32 = (UTF32Char *)[str cStringUsingEncoding:NSUTF32LittleEndianStringEncoding];
    if (len == 0 || char32 == NULL) return;
    
    NSUInteger location = 0;
    BOOL stop = NO;
    NSRange subRange;
    UTF32Char oneChar;
    
    for (NSUInteger i = 0; i < len; i++) {
        oneChar = char32[i];
        subRange = NSMakeRange(location, oneChar > 0xFFFF ? 2 : 1);
        if (block) {
            block(oneChar, subRange, &stop);
        }
        if (stop) return;
        location += subRange.length;
    }
}

#pragma mark - Security String
- (NSString *)jx_securityString {
    NSUInteger length = self.jx_lengthOfComposedCharacterSequences;
    NSString *asteriskString = @"";
    while (length--) {
        asteriskString = [asteriskString stringByAppendingString:@"*"];
    }
    return asteriskString;
}

- (NSString *)jx_stingByReplacingWithAsteriskInRange:(NSRange)range {
    return [self jx_stingByReplacingWithSecurityString:@"*" range:range];
}

- (NSString *)jx_stingByReplacingWithSecurityString:(NSString *)replacement range:(NSRange)range {
    NSString *tempString = [NSString stringWithString:self];
    NSString *securityString = @"";
    NSUInteger length = range.length;
    while (length--) {
        securityString = [securityString stringByAppendingString:replacement];
    }
    return [tempString stringByReplacingOccurrencesOfString:[tempString substringWithRange:range] withString:securityString];
}

#pragma mark - Data Value
- (NSData *)jx_dataValue {
    return [self dataUsingEncoding:NSUTF8StringEncoding];
}

#pragma mark - Case Changing
- (NSString *)jx_firstCharacterUppercaseString {
    NSMutableString *buffer = [NSMutableString stringWithString:self];
    if (!self.length) return buffer.copy;
    
    [buffer jx_replaceComposedCharacterSequenceAtIndex:0 withString:[buffer jx_firstComposedCharacterSequences].uppercaseString];
    return buffer.copy;
}

- (NSString *)jx_firstCharacterLowercaseString {
    NSMutableString *buffer = [NSMutableString stringWithString:self];
    if (!self.length) return buffer.copy;
    
    [buffer jx_replaceComposedCharacterSequenceAtIndex:0 withString:[buffer jx_firstComposedCharacterSequences].lowercaseString];
    return buffer.copy;
}

#pragma mark - Pinyin
- (NSString *)jx_pinyin {
    NSMutableString *mutableString = [NSMutableString stringWithString:self];
    CFStringTransform((CFMutableStringRef)mutableString, NULL,
                      kCFStringTransformToLatin, false);
    CFStringTransform((CFMutableStringRef)mutableString, NULL,
                      kCFStringTransformStripDiacritics, false);
    return mutableString.copy;
}

#pragma mark - UUID
+ (NSString *)jx_stringWithUUID {
    CFUUIDRef uuid = CFUUIDCreate(NULL);
    CFStringRef string = CFUUIDCreateString(NULL, uuid);
    CFRelease(uuid);
    return (__bridge_transfer NSString *)string;
}

#pragma mark - Number Value
- (NSNumber *)jx_numberValue {
    return [NSNumber jx_numberWithString:self];
}

- (char)jx_charValue {
    return self.jx_numberValue.charValue;
}

- (unsigned char)jx_unsignedCharValue {
    return self.jx_numberValue.unsignedCharValue;
}

- (short)jx_shortValue {
    return self.jx_numberValue.shortValue;
}

- (unsigned short)jx_unsignedShortValue {
    return self.jx_numberValue.unsignedShortValue;
}

- (int)jx_intValue {
    return self.intValue;
}

- (unsigned int)jx_unsignedIntValue {
    return self.jx_numberValue.unsignedIntValue;
}

- (long)jx_longValue {
    return self.jx_numberValue.longValue;
}

- (unsigned long)jx_unsignedLongValue {
    return self.jx_numberValue.unsignedLongValue;
}

- (long long)jx_longLongValue {
    return self.longLongValue;
}

- (unsigned long long)jx_unsignedLongLongValue {
    return self.jx_numberValue.unsignedLongLongValue;
}

- (float)jx_floatValue {
    return self.floatValue;
}

- (double)jx_doubleValue {
    return self.doubleValue;
}

- (BOOL)jx_boolValue {
    return self.boolValue;
}

- (NSInteger)jx_integerValue {
    return self.integerValue;
}

- (NSUInteger)jx_unsignedIntegerValue {
    return self.jx_numberValue.unsignedIntegerValue;
}

- (CGFloat)jx_CGFloatValue {
    return self.jx_numberValue.jx_CGFloatValue;
}

#pragma mark - Path String
NSString *JXNSDocumentsDirectory(void) {
    return [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) firstObject];
}

NSString *JXNSCachesDirectory(void) {
    return [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) firstObject];
}

NSString *JXNSLibraryDirectory(void) {
    return [NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES) firstObject];
}

NSString *JXNSTemporaryDirectory(void) {
    return NSTemporaryDirectory();
}

+ (NSString *)jx_documentsPathWithComponents:(NSArray<NSString *> *)components {
    NSString *componentsString = [NSString pathWithComponents:components];
    return [JXNSDocumentsDirectory() stringByAppendingPathComponent:componentsString];
}

+ (NSString *)jx_documentsPathWithComponent:(NSString *)component {
    return [JXNSDocumentsDirectory() stringByAppendingPathComponent:component];
}

+ (NSString *)jx_documentsPathWithLastPathComponent:(NSString *)lastPathComponent {
    return [JXNSDocumentsDirectory() stringByAppendingPathComponent:[lastPathComponent lastPathComponent]];
}

+ (NSString *)jx_cachesPathWithComponents:(NSArray<NSString *> *)components {
    NSString *componentsString = [NSString pathWithComponents:components];
    return [JXNSCachesDirectory() stringByAppendingPathComponent:componentsString];
}

+ (NSString *)jx_cachesPathWithComponent:(NSString *)component {
    return [JXNSCachesDirectory() stringByAppendingPathComponent:component];
}

+ (NSString *)jx_cachesPathWithLastPathComponent:(NSString *)lastPathComponent {
    return [JXNSCachesDirectory() stringByAppendingPathComponent:[lastPathComponent lastPathComponent]];
}

+ (NSString *)jx_libraryPathWithComponents:(NSArray<NSString *> *)components {
    NSString *componentsString = [NSString pathWithComponents:components];
    return [JXNSLibraryDirectory() stringByAppendingPathComponent:componentsString];
}

+ (NSString *)jx_libraryPathWithComponent:(NSString *)component {
    return [JXNSLibraryDirectory() stringByAppendingPathComponent:component];
}

+ (NSString *)jx_libraryPathWithLastPathComponent:(NSString *)lastPathComponent {
    return [JXNSLibraryDirectory() stringByAppendingPathComponent:[lastPathComponent lastPathComponent]];
}

+ (NSString *)jx_temporaryPathWithComponents:(NSArray<NSString *> *)components {
    NSString *componentsString = [NSString pathWithComponents:components];
    return [NSTemporaryDirectory() stringByAppendingPathComponent:componentsString];
}

+ (NSString *)jx_temporaryPathWithComponent:(NSString *)component {
    return [JXNSTemporaryDirectory() stringByAppendingPathComponent:component];
}

+ (NSString *)jx_temporaryPathWithLastPathComponent:(NSString *)lastPathComponent {
    return [JXNSTemporaryDirectory() stringByAppendingPathComponent:[lastPathComponent lastPathComponent]];
}

#pragma mark - File Size String
+ (NSString *)jx_stringFromFileSize:(int64_t)byte {
    if (byte < 0) return @"";
    // Byte
    if (byte < pow(10, 3)) return [NSString stringWithFormat:@"%lldByte", byte];
    
    NSDecimalNumber *number = nil;
    // KB
    if (byte < pow(10, 6)) {
        number = [NSDecimalNumber jx_decimalNumberWithDouble:byte/pow(10, 3) roundingScale:1];
        return [NSString stringWithFormat:@"%@KB", number];
    }
    // MB
    if (byte < pow(10, 9)) {
        number = [NSDecimalNumber jx_decimalNumberWithDouble:byte/pow(10, 6) roundingScale:1];
        return [NSString stringWithFormat:@"%@MB", number];
    }
    // GB
    number = [NSDecimalNumber jx_decimalNumberWithDouble:byte/pow(10, 9) roundingScale:1];
    return [NSString stringWithFormat:@"%@GB", number];
}

#pragma mark - Time String
+ (NSString *)jx_stringForStopWatchFormatWithSeconds:(NSTimeInterval)seconds {
    if (seconds < 0) return @"";
    
    NSUInteger minute = floor(seconds / 60);
    NSUInteger second = floor(seconds - minute * 60);
    return [NSString stringWithFormat:@"%02ld:%02ld", (long)minute, (long)second];
}

@end


@implementation NSMutableString (JXBase)

#pragma mark - Base

#pragma mark - Composed Character Sequences
- (void)jx_replaceComposedCharacterSequenceInRange:(NSRange)range withString:(NSString *)aString {
    if (!self.length) return;
    if (!range.length) return;
    if (!aString) return;
    
    NSRange aRange = [self rangeOfComposedCharacterSequencesForRange:range];
    if (!aRange.length) return;
    
    [self replaceCharactersInRange:aRange withString:aString];
}

- (void)jx_replaceComposedCharacterSequenceAtIndex:(NSUInteger)index withString:(NSString *)aString {
    NSRange range = [self rangeOfComposedCharacterSequenceAtIndex:index];
    if (!range.length) return;
    
    [self jx_replaceComposedCharacterSequenceInRange:range withString:aString];
}

- (void)jx_deleteFirstComposedCharacterSequence {
    return [self jx_deleteComposedCharacterSequenceAtIndex:0];
}

- (void)jx_deleteLastComposedCharacterSequence {
    return [self jx_deleteComposedCharacterSequenceAtIndex:self.length-1];
}

- (void)jx_deleteComposedCharacterSequenceAtIndex:(NSUInteger)index {
    if (!self.length) return;
    if (!index) return;
    
    NSRange aRange = [self rangeOfComposedCharacterSequenceAtIndex:index];
    if (!aRange.length) return;
    
    [self deleteCharactersInRange:aRange];
}

- (void)jx_deleteComposedCharacterSequenceInRange:(NSRange)range {
    if (!self.length) return;
    if (!range.length) return;
    
    NSRange aRange = [self rangeOfComposedCharacterSequencesForRange:range];
    if (!aRange.length) return;
    
    [self deleteCharactersInRange:aRange];
}

@end
