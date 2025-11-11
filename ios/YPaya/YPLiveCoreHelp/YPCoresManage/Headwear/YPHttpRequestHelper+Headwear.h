//
//  YPHttpRequestHelper+Headwear.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.

#import "YPHttpRequestHelper.h"

@interface YPHttpRequestHelper (Headwear)

+ (void)sendHeadwearWithHeadwearId:(NSString *)headwearId
                         targetUid:(NSString *)targetUid
                           Success:(void (^)(id))success
                           failure:(void (^)(NSNumber *, NSString *))failure;

+ (void)getHeadwearListWithPageNum:(NSString *)pageNum
                      withPageSize:(NSString *)pageSize
                            userId:(NSString *)userId
                           success:(void (^)(NSArray *list))success
                           failure:(void (^)(NSNumber *, NSString *))failure;

+ (void)getHeadwearPurseWithType:(NSString *)type
                         CarId:(NSString *)headwearId
                       Success:(void (^)(id))success
                       failure:(void (^)(NSNumber *, NSString *))failure;

+ (void)getHeadwearUseWithHeadwearId:(NSString *)headwearId
                      Success:(void (^)(id))success
                      failure:(void (^)(NSNumber *, NSString *))failure;

+ (void)getMyHeadwearListWithPageNum:(NSString *)pageNum
                        withPageSize:(NSString *)pageSize
                              userId:(NSString *)userId
                             success:(void (^)(NSArray *list))success
                             failure:(void (^)(NSNumber *, NSString *))failure;

@end
