//
//  YPHttpRequestHelper+Activity.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Activity.h"
#import "NSObject+YYModel.h"
#import "YPActivityInfo.h"

@implementation YPHttpRequestHelper (Activity)

+ (void)getAllActivitySuccess:(void (^)(NSArray *infoArr))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"activity/queryAll";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *userId = [GetCore(YPAuthCoreHelp) getUid];
    [params setObject:userId forKey:@"uid"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *activityInfo = [NSArray yy_modelArrayWithClass:[YPActivityInfo class] json:data];
        success(activityInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getActivityWithType:(NSInteger)type success:(void (^)(YPActivityInfo *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"activity/query";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(type) forKey:@"type"];
    NSString *userId = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:userId forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];

    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *arr = (NSArray *)data;
        if (arr.count > 0) {
            YPActivityInfo *info = [YPActivityInfo yy_modelWithJSON:arr[0]];
            success(info);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getNewActivity:(void (^)(NSArray *arr))success failure:(void (^)(NSNumber *code, NSString *msg))failure{
    NSString *method = @"activity/query";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(1) forKey:@"type"];
    NSString *userId = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:userId forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *arr = (NSArray *)data;
        if (arr.count > 0) {
            NSArray *activityInfo = [NSArray yy_modelArrayWithClass:[YPActivityInfo class] json:data];
            success(activityInfo);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

@end
