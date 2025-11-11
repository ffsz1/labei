//
//  YPHttpRequestHelper+Activity.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"
#import "YPActivityInfo.h"

@interface YPHttpRequestHelper (Activity)


/**
 获取活动配置

 @param type 类型
 @param success 成功
 @param failure 失败
 */
+ (void)getActivityWithType:(NSInteger)type
                    success:(void (^)(YPActivityInfo *info))success
                    failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 获取所有活动
 
 @param type 类型
 @param success 成功
 @param failure 失败
 */
+ (void)getAllActivitySuccess:(void (^)(NSArray *infoArr))success
                    failure:(void (^)(NSNumber *resCode, NSString *message))failure;

//获取最新活动
+ (void)getNewActivity:(void (^)(NSArray *arr))success failure:(void (^)(NSNumber *code, NSString *msg))failure;

@end
