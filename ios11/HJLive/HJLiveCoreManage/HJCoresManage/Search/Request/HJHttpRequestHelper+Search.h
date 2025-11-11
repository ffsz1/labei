//
//  HJHttpRequestHelper+Search.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"

@interface HJHttpRequestHelper (Search)

/**
 通过关键字搜索
 
 @param key 关键字
 @param success 成功
 @param failure 失败
 */
+ (void)requestInfoWithKey:(NSString *)key
                   Success:(void(^)(NSArray *list))success
                   failure:(void(^)(NSNumber *resCode, NSString *message))failure;

/**
 用户搜索

 @param key 关键字
 @param pageNo 页数
 @param pageSize 每页数量
 */
+ (void) searchUser:(NSString *)key pageNo:(NSInteger)pageNo pageSize:(NSInteger)pageSize
            success:(void (^)(NSArray * userInfos))success
            failure:(void (^)(NSNumber *resCode, NSString *message))failure;

@end
