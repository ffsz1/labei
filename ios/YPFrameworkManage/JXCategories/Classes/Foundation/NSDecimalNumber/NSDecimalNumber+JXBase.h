//
//  NSDecimalNumber+JXBase.h
//  JXCategories
//
//  Created by Colin on 17/1/3.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSDecimalNumber (JXBase)

#pragma mark - Base

#pragma mark - Round
/**
 根据float数值和四舍五入位数, 创建decimalNumber
 
 @param value float数值
 @param scale 四舍五入位数(正数为小数位, 负数为整数位)
 @return NSDecimalNumber对象
 */
+ (NSDecimalNumber *)jx_decimalNumberWithFloat:(float)value roundingScale:(short)scale;

/**
 根据float数值、舍入位数和舍入模式, 创建decimalNumber
 
 @param value float数值
 @param scale 舍入位数(正数为小数位, 负数为整数位)
 @param mode  舍入模式
 @return NSDecimalNumber对象
 */
+ (NSDecimalNumber *)jx_decimalNumberWithFloat:(float)value
                                 roundingScale:(short)scale
                                  roundingMode:(NSRoundingMode)mode;

/**
 根据double数值和四舍五入位数, 创建decimalNumber
 
 @param value double数值
 @param scale 四舍五入位数(正数为小数位, 负数为整数位)
 @return NSDecimalNumber对象
 */
+ (NSDecimalNumber *)jx_decimalNumberWithDouble:(double)value roundingScale:(short)scale;

/**
 根据double数值、舍入位数和舍入模式, 创建decimalNumber
 
 @param value double数值
 @param scale 舍入位数(正数为小数位, 负数为整数位)
 @param mode  舍入模式
 @return NSDecimalNumber对象
 */
+ (NSDecimalNumber *)jx_decimalNumberWithDouble:(double)value
                                  roundingScale:(short)scale
                                   roundingMode:(NSRoundingMode)mode;

/**
 四舍五入到指定位数
 
 @param scale 指定位数(正数为小数位, 负数为整数位)
 @return NSDecimalNumber对象
 */
- (NSDecimalNumber *)jx_roundToScale:(short)scale;

/**
 舍入到指定位数
 
 @param scale        指定位数(正数为小数位, 负数为整数位)
 @param roundingMode 舍入模式
 @return NSDecimalNumber对象
 */
- (NSDecimalNumber *)jx_roundToScale:(short)scale mode:(NSRoundingMode)roundingMode;

@end

NS_ASSUME_NONNULL_END
