//
//  NSObject+JXDataBind.h
//  JXCategories
//
//  Created by Colin on 2019/2/12.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

/**
 绑定对象, 可作为临时对象供后续使用
 
 [A jx_setBindingObject:obj forKey:@"key"]
 id object = [A jx_bindingObjectForKey:@"key"]
 */
@interface NSObject (JXDataBind)

@property (readonly, copy) NSArray<NSString *> *jx_allBindingKeys; ///< 获取所有绑定对象的Key集合

/**
 判断是否包含绑定对象的Key

 @param key Key
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsBindingKey:(NSString *)key;

/**
 移除所有绑定对象
 */
- (void)jx_removeAllBindingObjects;

/**
 移除Key对应的绑定对象

 @param key Key
 */
- (void)jx_revomeBindingObjectForKey:(NSString *)key;

/**
 根据Key, 绑定对象(nil则移除)

 @param object 对象
 @param key Key
 */
- (void)jx_setBindingObject:(nullable id)object forKey:(NSString *)key;

/**
 根据Key, 绑定弱引用对象(nil则移除)

 @param object 对象
 @param key Key
 */
- (void)jx_setBindingWeakObject:(nullable id)object forKey:(NSString *)key;

/**
 获取Key对应的绑定对象

 @param key Key
 @return 对象
 */
- (nullable id)jx_bindingObjectForKey:(NSString *)key;

- (void)jx_setBindingBoolValue:(BOOL)value forKey:(NSString *)key;
- (BOOL)jx_bindingBoolValueForKey:(NSString *)key;

- (void)jx_setBindingCharValue:(char)value forKey:(NSString *)key;
- (char)jx_bindingCharValueForKey:(NSString *)key;

- (void)jx_setBindingUnsignedCharValue:(unsigned char)value forKey:(NSString *)key;
- (unsigned char)jx_bindingUnsignedCharValueForKey:(NSString *)key;

- (void)jx_setBindingShortValue:(short)value forKey:(NSString *)key;
- (short)jx_bindingShortValueForKey:(NSString *)key;

- (void)jx_setBindingUnsignedShortValue:(unsigned short)value forKey:(NSString *)key;
- (unsigned short)jx_bindingUnsignedShortValueForKey:(NSString *)key;

- (void)jx_setBindingIntValue:(int)value forKey:(NSString *)key;
- (int)jx_bindingIntValueForKey:(NSString *)key;

- (void)jx_setBindingUnsignedIntValue:(unsigned int)value forKey:(NSString *)key;
- (unsigned int)jx_bindingUnsignedIntValueForKey:(NSString *)key;

- (void)jx_setBindingLongValue:(long)value forKey:(NSString *)key;
- (long)jx_bindingLongValueForKey:(NSString *)key;

- (void)jx_setBindingUnsignedLongValue:(unsigned long)value forKey:(NSString *)key;
- (unsigned long)jx_bindingUnsignedLongValueForKey:(NSString *)key;

- (void)jx_setBindingLongLongValue:(long long)value forKey:(NSString *)key;
- (long long)jx_bindingLongLongValueForKey:(NSString *)key;

- (void)jx_setBindingUnsignedLongLongValue:(unsigned long long)value forKey:(NSString *)key;
- (unsigned long long)jx_bindingUnsignedLongLongValueForKey:(NSString *)key;

- (void)jx_setBindingFloatValue:(float)value forKey:(NSString *)key;
- (float)jx_bindingFloatValueForKey:(NSString *)key;

- (void)jx_setBindingDoubleValue:(double)value forKey:(NSString *)key;
- (double)jx_bindingDoubleValueForKey:(NSString *)key;

- (void)jx_setBindingIntegerValue:(NSInteger)value forKey:(NSString *)key;
- (NSInteger)jx_bindingIntegerValueForKey:(NSString *)key;

- (void)jx_setBindingUnsignedIntegerValue:(NSUInteger)value forKey:(NSString *)key;
- (NSUInteger)jx_bindingUnsignedIntegerValueForKey:(NSString *)key;

- (void)jx_setBindingCGFloatValue:(CGFloat)value forKey:(NSString *)key;
- (CGFloat)jx_bindingCGFloatValueForKey:(NSString *)key;

@end

NS_ASSUME_NONNULL_END
