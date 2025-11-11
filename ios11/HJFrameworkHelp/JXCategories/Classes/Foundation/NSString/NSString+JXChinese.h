//
//  NSString+JXChinese.h
//  Pods
//
//  Created by Colin on 17/6/15.
//
//  字符串中文处理

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_OPTIONS(NSUInteger, JXNSStringChineseType) {   // 中文字符类型
    JXNSStringChineseTypeCharacter              = 1 << 0, // 中文文字
    JXNSStringChineseTypePunctuation            = 1 << 1, // 中文标点
    JXNSStringChineseTypeRadical                = 1 << 2, // 中文部首
    JXNSStringChineseTypeStroke                 = 1 << 3, // 中文笔划
    JXNSStringChineseTypeIdeographicDescription = 1 << 4, // 中文构字描述符
    JXNSStringChineseTypeAll = JXNSStringChineseTypeCharacter | JXNSStringChineseTypePunctuation | JXNSStringChineseTypeRadical | JXNSStringChineseTypeStroke | JXNSStringChineseTypeIdeographicDescription
};

@interface NSString (JXChinese)

/**
 字符串是否为中文字符

 @param type 中文字符类型
 @return 仅当字符串是单个中文字符返回YES, 否则返回NO
 */
- (BOOL)jx_isChinese:(JXNSStringChineseType)type;

/**
 字符串是否包含中文字符

 @param type 中文字符类型
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsChinese:(JXNSStringChineseType)type;

/**
 获取指定范围内, 字符串中的所有中文字符

 @param range 指定范围
 @param type  中文字符类型
 @return 新字符串
 */
- (NSString *)jx_substringWithChinese:(JXNSStringChineseType)type inRange:(NSRange)range;

/**
 根据遍历范围, 遍历字符串中的所有中文字符
 
 @param range 遍历范围
 @param type  中文字符类型
 @param block 处理回调
 */
- (void)jx_enumerateChinese:(JXNSStringChineseType)type
                    inRange:(NSRange)range
                 usingBlock:(void (^)(NSString * _Nullable substring, NSRange substringRange, NSRange enclosingRange, BOOL *stop))block;

/**
 将字符串中的所有中文字符替换为指定字符

 @param type        中文字符类型
 @param replacement 指定字符
 @return 新字符串
 */
- (NSString *)jx_stringByReplacingChinese:(JXNSStringChineseType)type withString:(NSString *)replacement;

/**
 去除字符串中所有的中文字符

 @param type 中文字符类型
 @return 新字符串
 */
- (NSString *)jx_stringByTrimmingChinese:(JXNSStringChineseType)type;

@end

NS_ASSUME_NONNULL_END
