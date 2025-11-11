//
//  NSURLComponents+JXBase.h
//  Pods
//
//  Created by Colin on 17/8/8.
//
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSURLComponents (JXBase)

#pragma mark - Base
/**
 为URLComponentst添加指定的URLQueryItem

 @param queryItem 指定的URLQueryItem
 */
- (void)jx_addQueryItem:(NSURLQueryItem *)queryItem;

/**
 根据URLQueryItem名称及值, 为URLComponentst添加指定的URLQueryItem

 @param name  URLQueryItem名称
 @param value URLQueryItem值
 */
- (void)jx_addQueryItemWithName:(NSString *)name value:(NSString *)value;

@end

NS_ASSUME_NONNULL_END
