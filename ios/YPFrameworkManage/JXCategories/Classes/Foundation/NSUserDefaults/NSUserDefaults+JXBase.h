//
//  NSUserDefaults+JXBase.h
//  JXCategories
//
//  Created by Colin on 2017/12/6.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSUserDefaults (JXBase)

#pragma mark - Base
/**
 判断UserDefaults是否含有Key键

 @param key key
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsKey:(NSString *)key;

/**
 判断UserDefaults是否含有Key键对应的值(nil返回NO)
 
 @param key key
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsObjectForKey:(id)key;

@end

NS_ASSUME_NONNULL_END
