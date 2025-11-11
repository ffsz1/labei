//
//  NSString+Utils.h
//  YYMobileFramework
//
//  Created by 小城 on 14-6-24.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//
#import <UIKit/UIKit.h>
@interface NSString (Utils)

- (BOOL)nim_isEmpty;

- (uint32_t)unsignedIntValue;

- (uint16_t)unsignedShortValue;

+ (BOOL)stringContainsEmoji:(NSString *)string;

/**
 *  显示诸如  粉丝：3000  粉丝：34.2万
 *
 *  @param number 数量
 *  @param prefix 前缀词，比如"粉丝"
 *
 *  @return 返回拼接后的词
 */
+ (NSString*)numberStringFromNumber:(NSNumber*)number withPrefixString:(NSString*)prefix;

/**
 *  秒数 转换为 xx:xx:xx 的时间格式
 *  比如 输出03:20:34  输入则为 3 * 60 * 60 + 20 * 60 + 34 = 12034
 *
 *  @param second 秒数
 *
 *  @return 返回 转换后的字符串
 */
+ (NSString *)convertTime:(CGFloat)second;
@end
