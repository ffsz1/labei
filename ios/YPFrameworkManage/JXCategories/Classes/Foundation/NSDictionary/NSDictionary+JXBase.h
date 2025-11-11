//
//  NSDictionary+JXBase.h
//  JXCategories
//
//  Created by Colin on 17/1/3.
//  Copyright © 2017年 JuXiao. All rights reserved.
//  Base

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSDictionary<KeyType, ObjectType> (JXBase)

#pragma mark - Base
/**
 返回包含字典所有Keys的排序数组(升序排序)

 @return 包含字典所有Keys的排序数组(升序排序)
 */
- (NSArray<KeyType> *)jx_allKeysSorted;

/**
 返回包含字典所有Values的排序数组(根据字典Key升序排序)

 @return 包含字典所有Values的排序数组(根据字典Key升序排序)
 */
- (NSArray<ObjectType> *)jx_allValuesSortedByKeys;

/**
 根据指定Keys, 返回包含Keys及与之对应的实体的新字典

 @param keys 指定Keys
 @return 含Keys及与之对应的实体的新字典
 */
- (NSDictionary<KeyType, ObjectType> *)jx_entriesForKeys:(NSArray<KeyType> *)keys;

#pragma mark - Check
/**
 判断字典是否含有Key键

 @param key key
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsKey:(KeyType)key;

/**
 判断字典是否含有Key键对应的值(nil返回NO)

 @param key key
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsObjectForKey:(KeyType)key;

#pragma mark - Store
/**
 获取key对应的bool值(若无返回默认值)

 @param key          key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (BOOL)jx_boolValueForKey:(KeyType)key default:(BOOL)defaultValue;

/**
 获取key对应的char值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (char)jx_charValueForKey:(KeyType)key default:(char)defaultValue;

/**
 获取key对应的unsignedChar值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (unsigned char)jx_unsignedCharValueForKey:(KeyType)key default:(unsigned char)defaultValue;

/**
 获取key对应的short值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (short)jx_shortValueForKey:(KeyType)key default:(short)defaultValue;

/**
 获取key对应的unsignedShort值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (unsigned short)jx_unsignedShortValueForKey:(KeyType)key default:(unsigned short)defaultValue;

/**
 获取key对应的int值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (int)jx_intValueForKey:(KeyType)key default:(int)defaultValue;

/**
 获取key对应的unsignedInt值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (unsigned int)jx_unsignedIntValueForKey:(KeyType)key default:(unsigned int)defaultValue;

/**
 获取key对应的long值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (long)jx_longValueForKey:(KeyType)key default:(long)defaultValue;

/**
 获取key对应的unsignedLong值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (unsigned long)jx_unsignedLongValueForKey:(KeyType)key default:(unsigned long)defaultValue;

/**
 获取key对应的longLongValue值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (long long)jx_longLongValueForKey:(KeyType)key default:(long long)defaultValue;

/**
 获取key对应的unsignedLongLong值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (unsigned long long)jx_unsignedLongLongValueForKey:(KeyType)key default:(unsigned long long)defaultValue ;

/**
 获取key对应的float值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (float)jx_floatValueForKey:(KeyType)key default:(float)defaultValue;

/**
 获取key对应的double值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (double)jx_doubleValueForKey:(KeyType)key default:(double)defaultValue;

/**
 获取key对应的integer值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (NSInteger)jx_integerValueForKey:(KeyType)key default:(NSInteger)defaultValue;

/**
 获取key对应的unsignedInteger值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (NSUInteger)jx_unsignedIntegerValueForKey:(KeyType)key default:(NSUInteger)defaultValue;

/**
 获取key对应的CGFloat值(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (CGFloat)jx_CGFloatValueForKey:(KeyType)key default:(CGFloat)defaultValue;

/**
 获取key对应的NSNumber对象(若无返回默认值)

 @param key key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (NSNumber *)jx_numberValueForKey:(KeyType)key default:(NSNumber *)defaultValue;

/**
 获取key对应的string(若无返回默认值)

 @param key          key
 @param defaultValue 默认值
 @return key对应的值(若无返回默认值)
 */
- (NSString *)jx_stringValueForKey:(KeyType)key default:(NSString *)defaultValue;

#pragma mark - JSON Dictionary
/**
 将字典编码为JSON可用string
 
 @return Json string
 */
- (nullable NSString *)jx_JSONStringEncode;

/**
 将字典编码为JSON可用string(带空格)
 
 @return Json string
 */
- (nullable NSString *)jx_JSONPrettyStringEncoded;

#pragma mark - Property List
/**
 根据属性列表data(Property List), 创建字典
 
 @param plist root元素为Dictionary的属性列表data(Property List)
 @return 包含属性列表元素的字典
 */
+ (nullable NSDictionary *)jx_dictionaryWithPlistData:(NSData *)plist;

/**
 根据属性列表XML字符(Property List), 创建可变字典
 
 @param plist root元素为Dictionary的属性列表XML字符(Property List)
 @return 包含属性列表元素的字典
 */
+ (nullable NSDictionary *)jx_dictionaryWithPlistString:(NSString *)plist;

/**
 将字典序列化为属性列表data(Property List)
 
 @return 属性列表data
 */
- (nullable NSData *)jx_plistData;

/**
 将字典序列化为属性列表XML字符(XML Property List)
 
 @return 属性列表XML字符
 */
- (nullable NSString *)jx_plistString;

@end


@interface NSMutableDictionary<KeyType, ObjectType> (JXBase)

#pragma mark - Base
/**
 将key对应的对象添加进字典(key或对象为空不执行)

 @param anObject 对象
 @param aKey Key
 */
- (void)jx_setObject:(ObjectType)anObject forKey:(KeyType<NSCopying>)aKey;

/**
 返回字典内key对应的value, 同时将其移除出字典(value为空返回nil)
 
 @param aKey Key
 @return key对应的value(value为空返回nil)
 */
- (nullable ObjectType)jx_popObjectForKey:(KeyType)aKey;

/**
 返回字典内一组keys对应的value, 同时将其移除出字典(keys为空返回空字典)
 
 @param keys keys 一组keys
 @return 包含指定keys对应的values的新字典(keys为空返回空字典)
 */
- (NSDictionary<KeyType, ObjectType> *)jx_popEntriesForKeys:(NSArray<KeyType> *)keys;

#pragma mark - Property List
/**
 根据属性列表data(Property List), 创建可变字典
 
 @param plist root元素为Dictionary的属性列表data(Property List)
 @return 包含属性列表元素的可变字典
 */
+ (nullable NSMutableDictionary *)jx_dictionaryWithPlistData:(NSData *)plist;

/**
 根据属性列表XML字符(Property List), 创建可变字典
 
 @param plist root元素为Dictionary的属性列表XML字符(Property List)
 @return 包含属性列表元素的可变字典
 */
+ (nullable NSMutableDictionary *)jx_dictionaryWithPlistString:(NSString *)plist;

@end

NS_ASSUME_NONNULL_END
