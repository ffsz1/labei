//
//  HJHttpRequestHelper+Sign.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Sign.h"

#import "HJMMHomeInfoModel.h"
#import "HJPointCoinWalletModel.h"

@implementation HJHttpRequestHelper (Sign)

+ (void)requestMengCoinListSuccess:(void(^)(HJMMHomeInfoModel *data))success
                           failure:(void(^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"mcoin/v1/getInfo";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        HJMMHomeInfoModel *info = [HJMMHomeInfoModel yy_modelWithJSON:data];;

        !success ?: success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode, message);
    }];
}

+ (void)requestRecevieMengCoinWithMissionId:(NSString *)missionId
                                    success:(void(^)(void))success
                                    failure:(void(^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"mcoin/v1/gainMcoin";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"missionId"] = missionId;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        !success ?: success();
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode, message);
    }];
}

+ (void)requestMengCoinMissionCountWithSuccess:(void(^)(id data))success
                                       failure:(void(^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"mcoin/v1/getMissionCount";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        !success ?: success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode, message);
    }];
}

+ (void)requestDiandianCoinNum:(void(^)(id data))success
                       failure:(void(^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"mcoin/v1/getMcoinNum";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        HJPointCoinWalletModel *info = [HJPointCoinWalletModel yy_modelWithJSON:data];
        !success ?: success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode, message);
    }];
}


@end
