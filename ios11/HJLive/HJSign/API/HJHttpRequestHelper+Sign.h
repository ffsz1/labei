//
//  HJHttpRequestHelper+Sign.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"

#import "HJMMHomeInfoModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJHttpRequestHelper (Sign)


/**
 请求获取萌币列表
 
 @param success success description
 @param failure failure description
 */
+ (void)requestMengCoinListSuccess:(void(^)(HJMMHomeInfoModel *data))success
                           failure:(void(^)(NSNumber *resCode, NSString *message))failure;

/**
 请求领取萌币
 
 @param missionId 任务Id
 @param success success description
 @param failure failure description
 */
+ (void)requestRecevieMengCoinWithMissionId:(NSString *)missionId
                                    success:(void(^)(void))success
                                    failure:(void(^)(NSNumber *resCode, NSString *message))failure;

/**
 请求萌币红点
 
 @param success success description
 @param failure failure description
 */
+ (void)requestMengCoinMissionCountWithSuccess:(void(^)(id data))success
                                       failure:(void(^)(NSNumber *resCode, NSString *message))failure;

//获取点点币余额
+ (void)requestDiandianCoinNum:(void(^)(id data))success
                       failure:(void(^)(NSNumber *resCode, NSString *message))failure;

@end

NS_ASSUME_NONNULL_END
