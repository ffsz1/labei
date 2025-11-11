//
//  HJHttpRequestHelper+rank.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"

#import "HJChartsModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJHttpRequestHelper (rank)



/**
 财富排行榜
 
 @param datetype 周期类型 1、日榜  2、周榜  3、总榜
 */
+ (void)getRichRankData:(NSInteger)datetype
                success:(void (^)(NSArray *))success
                failure:(void (^)(NSNumber *, NSString *))failure;

/**
 魅力排行榜
 
 @param datetype 周期类型 1、日榜  2、周榜  3、总榜
 */
+ (void)getCharmRankData:(NSInteger)datetype
                 success:(void (^)(NSArray *))success
                 failure:(void (^)(NSNumber *, NSString *))failure;


+ (void)getMyRankData:(NSInteger)type
             datetype:(NSInteger)datetype
              success:(void (^)(id model))success
              failure:(void (^)(NSNumber *, NSString *))failure;

@end

NS_ASSUME_NONNULL_END
