//
//  HJHttpRequestHelper+CarSys.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"

@interface HJHttpRequestHelper (CarSys)

+ (void)sendCarWithCarId:(NSString *)carId
               targetUid:(NSString *)targetUid
                 Success:(void (^)(id))success
                 failure:(void (^)(NSNumber *, NSString *))failure;

+ (void)getCarSysListWithPageNum:(NSString *)pageNum
                    withPageSize:(NSString *)pageSize
                          userId:(NSString *)userId
                         success:(void (^)(NSArray *list))success
                         failure:(void (^)(NSNumber *, NSString *))failure;

+ (void)getCarSysPurseWithType:(NSString *)type
                         CarId:(NSString *)carId
                       Success:(void (^)(id))success
                       failure:(void (^)(NSNumber *, NSString *))failure;

+ (void)getCarSysUseWithCarId:(NSString *)carId
                       Success:(void (^)(id))success
                       failure:(void (^)(NSNumber *, NSString *))failure;


+ (void)getMyCarSysListWithPageNum:(NSString *)pageNum
                      withPageSize:(NSString *)pageSize
                            userId:(NSString *)userId
                           success:(void (^)(NSArray *list))success
                           failure:(void (^)(NSNumber *, NSString *))failure;

//座驾
+ (void)getUserCarList:(UserID)userId
               PageNum:(NSString *)pageNum
              PageSize:(NSString *)pageSize
               success:(void(^)(NSArray *list))success
               failure:(void(^)(NSNumber *resCode,NSString *message))failure;

//头饰
+ (void)getUserHeadList:(UserID)userId
                PageNum:(NSString *)pageNum
               PageSize:(NSString *)pageSize
                success:(void(^)(NSArray *list))success
                failure:(void(^)(NSNumber *resCode,NSString *message))failure;

@end
