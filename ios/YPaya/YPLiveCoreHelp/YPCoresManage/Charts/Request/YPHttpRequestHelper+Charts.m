//
//  YPHttpRequestHelper+Charts.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Charts.h"
#import "YPChartsModel.h"
#import "NSObject+YYModel.h"

@implementation YPHttpRequestHelper (Charts)


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
                       failure:(void (^)(NSNumber *, NSString *))failure {
    
    NSString *method = @"allrank/geth5";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = @(uid);
    params[@"type"] = @(type);
    params[@"datetype"] = @(datetype);
    params[@"pageSize"] = @(pageSize);
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *allrankList = [NSArray yy_modelArrayWithClass:[YPChartsModel class] json:data[@"rankVoList"]];
        if (success) {
            success(allrankList);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)getMyRankData:(NSInteger)type
             datetype:(NSInteger)datetype
              success:(void (^)(id model))success
              failure:(void (^)(NSNumber *, NSString *))failure {
    
    NSString *method = @"allrank/getMeH5Rank";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = GetCore(YPAuthCoreHelp).getUid;
    NSString *ticket = GetCore(YPAuthCoreHelp).getTicket;
    params[@"uid"] = uid;
    params[@"ticket"] = ticket;

    
    params[@"type"] = @(type);
    params[@"datetype"] = @(datetype);
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        YPChartsModel *model = [YPChartsModel yy_modelWithJSON:data];
        if (success) {
            success(model);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

@end
