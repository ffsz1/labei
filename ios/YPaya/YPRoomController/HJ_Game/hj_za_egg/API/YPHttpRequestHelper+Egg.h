//
//  YPHttpRequestHelper+Egg.h
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPHttpRequestHelper (Egg)

+ (void)userGiftPurseDraw:(NSString *)type
                  success:(void (^)(id data))success
                  failure:(void (^)(NSNumber *resCode, NSString *message))failure;

+ (void)getPrizePoolGift:(void (^)(NSArray *arr))success
                 failure:(void (^)(NSNumber *resCode, NSString *message))failure;

+ (void)getEggRecord:(NSInteger)pageNum
             success:(void (^)(NSArray *arr))success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure;

@end

NS_ASSUME_NONNULL_END
