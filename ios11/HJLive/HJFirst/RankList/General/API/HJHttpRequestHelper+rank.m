//
//  HJHttpRequestHelper+rank.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+rank.h"

#import "NSObject+YYModel.h"


@implementation HJHttpRequestHelper (rank)


+ (void)getRichRankData:(NSInteger)datetype
                success:(void (^)(NSArray *))success
                failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"allrank/geth5";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = GetCore(HJAuthCoreHelp).getUid;
    
//    @param type 排行榜类型 1、魅力榜  2、土豪榜  3、房间榜
    params[@"type"] = @(2);
    params[@"datetype"] = @(datetype);
    params[@"pageSize"] = @(50);
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *allrankList = [NSArray yy_modelArrayWithClass:[HJChartsModel class] json:data[@"rankVoList"]];
        NSLog(@"%@",data);
        if (success) {
            success(allrankList);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)getCharmRankData:(NSInteger)datetype
                success:(void (^)(NSArray *))success
                failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"allrank/geth5";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = GetCore(HJAuthCoreHelp).getUid;
    
    //    @param type 排行榜类型 1、魅力榜  2、土豪榜  3、房间榜
    params[@"type"] = @(1);
    params[@"datetype"] = @(datetype);
    params[@"pageSize"] = @(50);
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *allrankList = [NSArray yy_modelArrayWithClass:[HJChartsModel class] json:data[@"rankVoList"]];
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
    NSString *uid = GetCore(HJAuthCoreHelp).getUid;
    NSString *ticket = GetCore(HJAuthCoreHelp).getTicket;
    params[@"uid"] = uid;
    params[@"ticket"] = ticket;
    
    
    params[@"type"] = @(type);
    params[@"datetype"] = @(datetype);
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        HJChartsModel *model = [HJChartsModel yy_modelWithJSON:data];
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
