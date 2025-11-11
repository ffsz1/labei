//
//  HJHttpRequestHelper+MyTask.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"

@interface HJHttpRequestHelper (MyTask)

/**
 我的任务列表
 */
+ (void)getDutyListWithSuccess:(void (^)(id tasks))success failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 领取奖励
 
 @param dutyId 任务id
 */
+ (void)dutyAchieveWithDutyId:(NSInteger)dutyId success:(void (^)())success failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 去大厅发言，发言上传接口
 */
+ (void)dutyFreshPublicWithSuccess:(void (^)())success failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 微信QQ分享上传
 */
+ (void)dutyDailyShareWithSuccess:(void (^)())success failure:(void (^)(NSNumber *code, NSString *msg))failure;

@end
