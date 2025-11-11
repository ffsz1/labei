//
//  NSDictionary+JXBase.m
//  JXCategories
//
//  Created by Colin on 17/1/3.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "NSDictionary+JXBase.h"
#import "NSData+JXBase.h"
#import "NSNumber+JXBase.h"

@implementation NSDictionary (JXBase)

#pragma mark - Base
- (NSArray *)jx_allKeysSorted {
    return [[self allKeys] sortedArrayUsingSelector:@selector(caseInsensitiveCompare:)]; // caseInsensitiveCompare:比较两个字符串大小, 忽略大小写
}

- (NSArray *)jx_allValuesSortedByKeys {
    NSArray *sortedKeys = [self jx_allKeysSorted];
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (id key in sortedKeys) {
        [array addObject:self[key]];
    }
    return [array copy];
}

- (NSDictionary *)jx_entriesForKeys:(NSArray *)keys {
    NSMutableDictionary *dictionary = [NSMutableDictionary new];
    for (id key in keys) {
        id value = self[key];
        if (value) dictionary[key] = value;
    }
    return [dictionary copy];
}

#pragma mark - Check
- (BOOL)jx_containsKey:(id)key {
    if (!key) return NO;
    return [[self allKeys] containsObject:key];
}

- (BOOL)jx_containsObjectForKey:(id)key {
    if (!key) return NO;
    return self[key] != nil;
}

#pragma mark - Store
NSNumber *JXNumberFromID(id value) {
    static NSCharacterSet *dot;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        dot = [NSCharacterSet characterSetWithRange:NSMakeRange('.', 1)]; // dot字集
    });
    if (!value || value == [NSNull null]) return nil;
    if ([value isKindOfClass:[NSNumber class]]) return value;
    if ([value isKindOfClass:[NSString class]]) {
        NSString *lower = ((NSString *)value).lowercaseString;
        if ([lower isEqualToString:@"true"] || [lower isEqualToString:@"yes"]) return @(YES);
        if ([lower isEqualToString:@"false"] || [lower isEqualToString:@"no"]) return @(NO);
        if ([lower isEqualToString:@"nil"] || [lower isEqualToString:@"null"]) return nil;
        if ([(NSString *)value rangeOfCharacterFromSet:dot].location != NSNotFound) {
            return @(((NSString *)value).doubleValue);
        } else {
            return @(((NSString *)value).longLongValue); // No dot
        }
    }
    return nil;
}

// ARC下, 不允许基本数据类型(bool, int...)转为id类型
#define JXRETURN_VALUE(_type_)                                                   \
if (!key) return defaultValue;                                                   \
id value = self[key];                                                            \
if (!value || value == [NSNull null]) return defaultValue;                       \
if ([value isKindOfClass:[NSNumber class]]) return ((NSNumber *)value)._type_;   \
if ([value isKindOfClass:[NSString class]]) return JXNumberFromID(value)._type_; \
return defaultValue;

- (BOOL)jx_boolValueForKey:(id)key default:(BOOL)defaultValue {
    JXRETURN_VALUE(boolValue);
}

- (char)jx_charValueForKey:(id)key default:(char)defaultValue {
    JXRETURN_VALUE(charValue);
}

- (unsigned char)jx_unsignedCharValueForKey:(id)key default:(unsigned char)defaultValue {
    JXRETURN_VALUE(unsignedCharValue);
}

- (short)jx_shortValueForKey:(id)key default:(short)defaultValue {
    JXRETURN_VALUE(shortValue);
}

- (unsigned short)jx_unsignedShortValueForKey:(id)key default:(unsigned short)defaultValue {
    JXRETURN_VALUE(unsignedShortValue);
}

- (int)jx_intValueForKey:(id)key default:(int)defaultValue {
    JXRETURN_VALUE(intValue);
}

- (unsigned int)jx_unsignedIntValueForKey:(id)key default:(unsigned int)defaultValue {
    JXRETURN_VALUE(unsignedIntValue);
}

- (long)jx_longValueForKey:(id)key default:(long)defaultValue {
    JXRETURN_VALUE(longValue);
}

- (unsigned long)jx_unsignedLongValueForKey:(id)key default:(unsigned long)defaultValue {
    JXRETURN_VALUE(unsignedLongValue);
}

- (long long)jx_longLongValueForKey:(id)key default:(long long)defaultValue {
    JXRETURN_VALUE(longLongValue);
}

- (unsigned long long)jx_unsignedLongLongValueForKey:(id)key default:(unsigned long long)defaultValue {
    JXRETURN_VALUE(unsignedLongLongValue);
}

- (float)jx_floatValueForKey:(id)key default:(float)defaultValue {
    JXRETURN_VALUE(floatValue);
}

- (double)jx_doubleValueForKey:(id)key default:(double)defaultValue {
    JXRETURN_VALUE(doubleValue);
}

- (NSInteger)jx_integerValueForKey:(id)key default:(NSInteger)defaultValue {
    JXRETURN_VALUE(integerValue);
}

- (NSUInteger)jx_unsignedIntegerValueForKey:(id)key default:(NSUInteger)defaultValue {
    JXRETURN_VALUE(unsignedIntegerValue);
}

- (CGFloat)jx_CGFloatValueForKey:(id)key default:(CGFloat)defaultValue {
    JXRETURN_VALUE(jx_CGFloatValue);
}

- (NSNumber *)jx_numberValueForKey:(id)key default:(NSNumber *)defaultValue {
    if (!key) return defaultValue;
    id value = self[key];
    if (!value || value == [NSNull null]) return defaultValue;
    if ([value isKindOfClass:[NSNumber class]]) return value;
    if ([value isKindOfClass:[NSString class]]) return JXNumberFromID(value);
    return defaultValue;
}

- (NSString *)jx_stringValueForKey:(id)key default:(NSString *)defaultValue {
    if (!key) return defaultValue;
    id value = self[key];
    if (!value || value == [NSNull null]) return defaultValue;
    if ([value isKindOfClass:[NSString class]]) return value;
    if ([value isKindOfClass:[NSNumber class]]) return ((NSNumber *)value).description;
    return defaultValue;
}

#pragma mark - JSON Dictionary
- (NSString *)jx_JSONStringEncode {
    if ([NSJSONSerialization isValidJSONObject:self]) {
        NSError *error;
        NSData *JSONData = [NSJSONSerialization dataWithJSONObject:self options:0 error:&error];
        NSString *JSONString = [[NSString alloc] initWithData:JSONData encoding:NSUTF8StringEncoding];
        if (!error) return JSONString;
    }
    return nil;
}

- (NSString *)jx_JSONPrettyStringEncoded {
    if ([NSJSONSerialization isValidJSONObject:self]) {
        NSError *error;
        NSData *JSONData = [NSJSONSerialization dataWithJSONObject:self options:NSJSONWritingPrettyPrinted error:&error];
        NSString *JSONString = [[NSString alloc] initWithData:JSONData encoding:NSUTF8StringEncoding];
        if (!error) return JSONString;
    }
    return nil;
}

#pragma mark - Property List
+ (NSDictionary *)jx_dictionaryWithPlistData:(NSData *)plist {
    if (!plist) return nil;
    NSDictionary *dictionary = [NSPropertyListSerialization propertyListWithData:plist options:NSPropertyListImmutable format:NULL error:NULL];
    if ([dictionary isKindOfClass:[NSDictionary class]]) return dictionary;
    return nil;
}

+ (NSDictionary *)jx_dictionaryWithPlistString:(NSString *)plist {
    if (!plist) return nil;
    NSData *data = [plist dataUsingEncoding:NSUTF8StringEncoding];
    return [self jx_dictionaryWithPlistData:data];
}

- (NSData *)jx_plistData {
    return [NSPropertyListSerialization dataWithPropertyList:self format:NSPropertyListBinaryFormat_v1_0 options:kNilOptions error:NULL];
}

- (NSString *)jx_plistString {
    NSData *xmlData = [NSPropertyListSerialization dataWithPropertyList:self format:NSPropertyListXMLFormat_v1_0 options:kNilOptions error:NULL];
    if (xmlData) return xmlData.jx_utf8String;
    return nil;
}

@end


@implementation NSMutableDictionary (JXBase)

#pragma mark - Base
- (void)jx_setObject:(id)anObject forKey:(id<NSCopying>)aKey {
    if (anObject == nil || aKey == nil) return;
    
    [self setObject:anObject forKey:aKey];
}

- (id)jx_popObjectForKey:(id)aKey {
    if (!aKey) return nil;
    id value = self[aKey];
    [self removeObjectForKey:aKey];
    return value;
}

- (NSDictionary<id, id> *)jx_popEntriesForKeys:(NSArray *)keys {
    NSMutableDictionary *dictionary = [NSMutableDictionary new];
    for (id key in keys) {
        id value = self[key];
        if (value) {
            [self removeObjectForKey:key];
            dictionary[key] = value;
        }
    }
    return [dictionary copy];
}

#pragma mark - Property List
+ (NSMutableDictionary *)jx_dictionaryWithPlistData:(NSData *)plist {
    if (!plist) return nil;
    NSMutableDictionary *dictionary = [NSPropertyListSerialization propertyListWithData:plist options:NSPropertyListMutableContainersAndLeaves format:NULL error:NULL];
    if ([dictionary isKindOfClass:[NSMutableDictionary class]]) return dictionary;
    return nil;
}

+ (NSMutableDictionary *)jx_dictionaryWithPlistString:(NSString *)plist {
    if (!plist) return nil;
    NSData *data = [plist dataUsingEncoding:NSUTF8StringEncoding];
    return [self jx_dictionaryWithPlistData:data];
}

@end
