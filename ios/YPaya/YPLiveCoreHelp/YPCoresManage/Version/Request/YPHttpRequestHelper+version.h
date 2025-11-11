//
//  YPHttpRequestHelper+version.h
//  HJLive
//
//  Created by FF on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"
#import "YPVersionInfo.h"

@interface YPHttpRequestHelper (version)

+ (void)configClientSuccess:(void (^)(id json))success failure:(void(^)(NSNumber *resCode, NSString *message))failure;

/**
 查询当前版本号是否有在 

 @param version 版本号
 @param success 成功
 @param failure 失败
 */
+ (void)checkVersion:(NSString *)version success:(void (^)(YPVersionInfo *info))success failure:(void(^)(NSNumber *resCode, NSString *message))failure;


@end
