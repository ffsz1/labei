//
//  HJHttpRequestHelper+Charts.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"

@interface HJHttpRequestHelper (Charts)

/**
 排行榜
 
 @param uid uid
 @param type 排行榜类型 1、魅力榜  2、土豪榜  3、房间榜
 @param datetype 周期类型 1、日榜  2、周榜  3、总榜
 */
+ (void)requestaAllrankWithUid:(UserID)uid
                          type:(NSInteger)type
                      datetype:(NSInteger)datetype
                      pageSize:(NSInteger)pageSize
                       success:(void (^)(NSArray *))success
                       failure:(void (^)(NSNumber *, NSString *))failure;


+ (void)getMyRankData:(NSInteger)type
             datetype:(NSInteger)datetype
              success:(void (^)(id model))success
              failure:(void (^)(NSNumber *, NSString *))failure;

@end
