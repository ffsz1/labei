//
//  YPUserCache.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <YYCache.h>
#import "UserInfo.h"

@interface YPUserCache : NSObject

+ (instancetype)shareCache;

/**
查询用户信息

 @param uid 用户id
 @return rac
 */
- (RACSignal *)getUserInfoFromCacheWith:(UserID)uid;


/**
 缓存用户信息

 @param userInfo 用户信息
 */
- (void)saveUserInfo:(UserInfo *)userInfo;


/**
 移除用户信息

 @param key key
 */
- (void)removeUserInfoWithKey:(NSString *)key;


/**
 移除所有用户信息
 */
- (void)removeAllObject;

@end
