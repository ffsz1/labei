//
//  NSNumber+JXBase.h
//  JXCategories
//
//  Created by Colin on 17/1/3.
//  Copyright © 2017年 JuXiao. All rights reserved.
//  Base

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSNumber (JXBase)

#pragma mark - Base
/**
 获取NSNumber对象对应的CGFloat值
 */
@property (nonatomic, readonly) CGFloat jx_CGFloatValue;

/**
 获取NSNumber对象对应的字符串(@1 -> @"1");
 */
@property (nonatomic, readonly) NSString *jx_stringNumber;

/**
 根据CGFloat值, 创建NSNumber对象
 
 @param value CGFloat值
 @return NSNumber对象
 */
- (NSNumber *)initWithCGFloat:(CGFloat)value;

/**
 根据CGFloat值, 创建NSNumber对象

 @param value CGFloat值
 @return NSNumber对象
 */
+ (NSNumber *)jx_numberWithCGFloat:(CGFloat)value;

/**
 根据数字字符串(@"12", @"12.345", @" -0xFF", @" .23e99 ")创建NSNumber对象(字符对象为nil/null返回nil)

 @param string 数字字符串
 @return NSNumber对象(字符对象为nil/null返回nil)
 */
+ (nullable NSNumber *)jx_numberWithString:(NSString *)string;

@end

NS_ASSUME_NONNULL_END
