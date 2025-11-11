//
//  NSString+JXEmoji.h
//  Pods
//
//  Created by Colin on 17/2/10.
//
//  字符串Emoji处理

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSString (JXEmoji)

/**
 字符串是否Emoji
 
 @return 仅当字符串是单个Emoji返回YES, 否则返回NO
 */
- (BOOL)jx_isEmoji;

/**
 是否包含Emoji
 
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsEmoji;

/**
 去除字符串所有的Emoji
 
 @return 新字符串
 */
- (NSString *)jx_stringByTrimmingEmoji;

@end

NS_ASSUME_NONNULL_END
