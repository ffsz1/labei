//
//  NSObject+JXKeyValueCoding.h
//  Pods
//
//  Created by Colin on 17/2/10.
//
//  KVC

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSObject (JXKeyValueCoding)

- (void)jx_setBoolValue:(BOOL)value forKey:(NSString *)key;
- (BOOL)jx_boolValueForKey:(NSString *)key;
- (void)jx_setBoolValue:(BOOL)value forKeyPath:(NSString *)keyPath;
- (BOOL)jx_boolValueForKeyPath:(NSString *)keyPath;

- (void)jx_setCharValue:(char)value forKey:(NSString *)key;
- (char)jx_charValueForKey:(NSString *)key;
- (void)jx_setCharValue:(char)value forKeyPath:(NSString *)keyPath;
- (char)jx_charValueForKeyPath:(NSString *)keyPath;

- (void)jx_setUnsignedCharValue:(unsigned char)value forKey:(NSString *)key;
- (unsigned char)jx_unsignedCharValueForKey:(NSString *)key;
- (void)jx_setUnsignedCharValue:(unsigned char)value forKeyPath:(NSString *)keyPath;
- (unsigned char)jx_unsignedCharValueForKeyPath:(NSString *)keyPath;

- (void)jx_setShortValue:(short)value forKey:(NSString *)key;
- (short)jx_shortValueForKey:(NSString *)key;
- (void)jx_setShortValue:(short)value forKeyPath:(NSString *)keyPath;
- (short)jx_shortValueForKeyPath:(NSString *)keyPath;

- (void)jx_setUnsignedShortValue:(unsigned short)value forKey:(NSString *)key;
- (unsigned short)jx_unsignedShortValueForKey:(NSString *)key;
- (void)jx_setUnsignedShortValue:(unsigned short)value forKeyPath:(NSString *)keyPath;
- (unsigned short)jx_unsignedShortValueForKeyPath:(NSString *)keyPath;

- (void)jx_setIntValue:(int)value forKey:(NSString *)key;
- (int)jx_intValueForKey:(NSString *)key;
- (void)jx_setIntValue:(int)value forKeyPath:(NSString *)keyPath;
- (int)jx_intValueForKeyPath:(NSString *)keyPath;

- (void)jx_setUnsignedIntValue:(unsigned int)value forKey:(NSString *)key;
- (unsigned int)jx_unsignedIntValueForKey:(NSString *)key;
- (void)jx_setUnsignedIntValue:(unsigned int)value forKeyPath:(NSString *)keyPath;
- (unsigned int)jx_unsignedIntValueForKeyPath:(NSString *)keyPath;

- (void)jx_setLongValue:(long)value forKey:(NSString *)key;
- (long)jx_longValueForKey:(NSString *)key;
- (void)jx_setLongValue:(long)value forKeyPath:(NSString *)keyPath;
- (long)jx_longValueForKeyPath:(NSString *)keyPath;

- (void)jx_setUnsignedLongValue:(unsigned long)value forKey:(NSString *)key;
- (unsigned long)jx_unsignedLongValueForKey:(NSString *)key;
- (void)jx_setUnsignedLongValue:(unsigned long)value forKeyPath:(NSString *)keyPath;
- (unsigned long)jx_unsignedLongValueForKeyPath:(NSString *)keyPath;

- (void)jx_setLongLongValue:(long long)value forKey:(NSString *)key;
- (long long)jx_longLongValueForKey:(NSString *)key;
- (void)jx_setLongLongValue:(long long)value forKeyPath:(NSString *)keyPath;
- (long long)jx_longLongValueForKeyPath:(NSString *)keyPath;

- (void)jx_setUnsignedLongLongValue:(unsigned long long)value forKey:(NSString *)key;
- (unsigned long long)jx_unsignedLongLongValueForKey:(NSString *)key;
- (void)jx_setUnsignedLongLongValue:(unsigned long long)value forKeyPath:(NSString *)keyPath;
- (unsigned long long)jx_unsignedLongLongValueForKeyPath:(NSString *)keyPath;

- (void)jx_setFloatValue:(float)value forKey:(NSString *)key;
- (float)jx_floatValueForKey:(NSString *)key;
- (void)jx_setFloatValue:(float)value forKeyPath:(NSString *)keyPath;
- (float)jx_floatValueForKeyPath:(NSString *)keyPath;

- (void)jx_setDoubleValue:(double)value forKey:(NSString *)key;
- (double)jx_doubleValueForKey:(NSString *)key;
- (void)jx_setDoubleValue:(double)value forKeyPath:(NSString *)keyPath;
- (double)jx_doubleValueForKeyPath:(NSString *)keyPath;

- (void)jx_setIntegerValue:(NSInteger)value forKey:(NSString *)key;
- (NSInteger)jx_integerValueForKey:(NSString *)key;
- (void)jx_setIntegerValue:(NSInteger)value forKeyPath:(NSString *)keyPath;
- (NSInteger)jx_integerValueForKeyPath:(NSString *)keyPath;

- (void)jx_setUnsignedIntegerValue:(NSUInteger)value forKey:(NSString *)key;
- (NSUInteger)jx_unsignedIntegerValueForKey:(NSString *)key;
- (void)jx_setUnsignedIntegerValue:(NSUInteger)value forKeyPath:(NSString *)keyPath;
- (NSUInteger)jx_unsignedIntegerValueForKeyPath:(NSString *)keyPath;

- (void)jx_setCGFloatValue:(CGFloat)value forKey:(NSString *)key;
- (CGFloat)jx_CGFloatValueForKey:(NSString *)key;
- (void)jx_setCGFloatValue:(CGFloat)value forKeyPath:(NSString *)keyPath;
- (CGFloat)jx_CGFloatValueForKeyPath:(NSString *)keyPath;

@end

NS_ASSUME_NONNULL_END
