//
//  YPHttpRequestHelper+Im.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"

@interface YPHttpRequestHelper (Im)

+(void)getAgoraKeyWith:(NSString *)channel uid:(NSString *)uid success:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure;

/**
 获取违禁词正则
 */
+ (void)sensitiveWordRegexWithSuccess:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure;

@end
