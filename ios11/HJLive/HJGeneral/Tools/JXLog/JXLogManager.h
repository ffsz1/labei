//
//  JXLogManager.h
//  XChat
//
//  Created by apple on 2019/4/16.
//  Copyright © 2019 XC. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface JXLogManager : NSObject

/**
 *  获取单例实例
 *
 *  @return 单例实例
 */
+ (instancetype) sharedInstance;

/**
 *  清空过期的日志
 */
- (void)clearExpiredLog;

/**
 *  写入日志
 *
 *  @param module 模块名称
 *  @param logStr 日志信息,动态参数
 */
- (void)logInfo:(NSString*)module logStr:(NSString*)logStr, ...;

@end

NS_ASSUME_NONNULL_END
