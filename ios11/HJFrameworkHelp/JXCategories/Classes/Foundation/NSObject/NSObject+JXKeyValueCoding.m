//
//  NSObject+JXKeyValueCoding.m
//  Pods
//
//  Created by Colin on 17/2/10.
//
//

#import "NSObject+JXKeyValueCoding.h"
#import "NSNumber+JXBase.h"

@implementation NSObject (JXKeyValueCoding)

- (void)jx_setBoolValue:(BOOL)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithBool:value] forKey:key];  // @() may be undefine value
}

- (BOOL)jx_boolValueForKey:(NSString *)key {
    return [[self valueForKey:key] boolValue];
}

- (void)jx_setBoolValue:(BOOL)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithBool:value] forKeyPath:keyPath];
}

- (BOOL)jx_boolValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] boolValue];
}

- (void)jx_setCharValue:(char)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithChar:value] forKey:key];
}

- (char)jx_charValueForKey:(NSString *)key {
    return [[self valueForKey:key] charValue];
}

- (void)jx_setCharValue:(char)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithChar:value] forKeyPath:keyPath];
}

- (char)jx_charValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] charValue];
}

- (void)jx_setUnsignedCharValue:(unsigned char)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithUnsignedChar:value] forKey:key];
}

- (unsigned char)jx_unsignedCharValueForKey:(NSString *)key {
    return [[self valueForKey:key] unsignedCharValue];
}

- (void)jx_setUnsignedCharValue:(unsigned char)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithUnsignedChar:value] forKeyPath:keyPath];
}

- (unsigned char)jx_unsignedCharValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] unsignedCharValue];
}

- (void)jx_setShortValue:(short)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithShort:value] forKey:key];
}

- (short)jx_shortValueForKey:(NSString *)key {
    return [[self valueForKey:key] shortValue];
}

- (void)jx_setShortValue:(short)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithShort:value] forKeyPath:keyPath];
}

- (short)jx_shortValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] shortValue];
}

- (void)jx_setUnsignedShortValue:(unsigned short)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithUnsignedShort:value] forKey:key];
}

- (unsigned short)jx_unsignedShortValueForKey:(NSString *)key {
    return [[self valueForKey:key] unsignedShortValue];
}

- (void)jx_setUnsignedShortValue:(unsigned short)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithUnsignedShort:value] forKeyPath:keyPath];
}

- (unsigned short)jx_unsignedShortValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] unsignedShortValue];
}

- (void)jx_setIntValue:(int)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithInt:value] forKey:key];
}

- (int)jx_intValueForKey:(NSString *)key {
    return [[self valueForKey:key] intValue];
}

- (void)jx_setIntValue:(int)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithInt:value] forKeyPath:keyPath];
}

- (int)jx_intValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] intValue];
}

- (void)jx_setUnsignedIntValue:(unsigned int)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithUnsignedInt:value] forKey:key];
}

- (unsigned int)jx_unsignedIntValueForKey:(NSString *)key {
    return [[self valueForKey:key] unsignedIntValue];
}

- (void)jx_setUnsignedIntValue:(unsigned int)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithUnsignedInt:value] forKeyPath:keyPath];
}

- (unsigned int)jx_unsignedIntValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] unsignedIntValue];
}

- (void)jx_setLongValue:(long)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithLong:value] forKey:key];
}

- (long)jx_longValueForKey:(NSString *)key {
    return [[self valueForKey:key] longValue];
}

- (void)jx_setLongValue:(long)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithLong:value] forKeyPath:keyPath];
}

- (long)jx_longValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] longValue];
}

- (void)jx_setUnsignedLongValue:(unsigned long)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithUnsignedLong:value] forKey:key];
}

- (unsigned long)jx_unsignedLongValueForKey:(NSString *)key {
    return [[self valueForKey:key] unsignedLongValue];
}

- (void)jx_setUnsignedLongValue:(unsigned long)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithUnsignedLong:value] forKeyPath:keyPath];
}

- (unsigned long)jx_unsignedLongValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] unsignedLongValue];
}

- (void)jx_setLongLongValue:(long long)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithLongLong:value] forKey:key];
}

- (long long)jx_longLongValueForKey:(NSString *)key {
    return [[self valueForKey:key] longLongValue];
}

- (void)jx_setLongLongValue:(long long)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithLongLong:value] forKeyPath:keyPath];
}

- (long long)jx_longLongValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] longLongValue];
}

- (void)jx_setUnsignedLongLongValue:(unsigned long long)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithUnsignedLongLong:value] forKey:key];
}

- (unsigned long long)jx_unsignedLongLongValueForKey:(NSString *)key {
    return [[self valueForKey:key] unsignedLongLongValue];
}

- (void)jx_setUnsignedLongLongValue:(unsigned long long)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithUnsignedLongLong:value] forKeyPath:keyPath];
}

- (unsigned long long)jx_unsignedLongLongValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] unsignedLongLongValue];
}

- (void)jx_setFloatValue:(float)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithFloat:value] forKey:key];
}

- (float)jx_floatValueForKey:(NSString *)key {
    return [[self valueForKey:key] floatValue];
}

- (void)jx_setFloatValue:(float)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithFloat:value] forKeyPath:keyPath];
}

- (float)jx_floatValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] floatValue];
}

- (void)jx_setDoubleValue:(double)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithDouble:value] forKey:key];
}

- (double)jx_doubleValueForKey:(NSString *)key {
    return [[self valueForKey:key] doubleValue];
}

- (void)jx_setDoubleValue:(double)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithDouble:value] forKeyPath:keyPath];
}

- (double)jx_doubleValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] doubleValue];
}

- (void)jx_setIntegerValue:(NSInteger)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithInteger:value] forKey:key];
}

- (NSInteger)jx_integerValueForKey:(NSString *)key {
    return [[self valueForKey:key] integerValue];
}

- (void)jx_setIntegerValue:(NSInteger)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithInteger:value] forKeyPath:keyPath];
}

- (NSInteger)jx_integerValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] integerValue];
}

- (void)jx_setUnsignedIntegerValue:(NSUInteger)value forKey:(NSString *)key {
    [self setValue:[NSNumber numberWithUnsignedInteger:value] forKey:key];
}

- (NSUInteger)jx_unsignedIntegerValueForKey:(NSString *)key {
    return [[self valueForKey:key] unsignedIntegerValue];
}

- (void)jx_setUnsignedIntegerValue:(NSUInteger)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber numberWithUnsignedInteger:value] forKeyPath:keyPath];
}

- (NSUInteger)jx_unsignedIntegerValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] unsignedIntegerValue];
}

- (void)jx_setCGFloatValue:(CGFloat)value forKey:(NSString *)key {
    [self setValue:[NSNumber jx_numberWithCGFloat:value] forKey:key];
}

- (CGFloat)jx_CGFloatValueForKey:(NSString *)key {
    return [[self valueForKey:key] jx_CGFloatValue];
}

- (void)jx_setCGFloatValue:(CGFloat)value forKeyPath:(NSString *)keyPath {
    [self setValue:[NSNumber jx_numberWithCGFloat:value] forKeyPath:keyPath];
}

- (CGFloat)jx_CGFloatValueForKeyPath:(NSString *)keyPath {
    return [[self valueForKeyPath:keyPath] jx_CGFloatValue];
}

@end
