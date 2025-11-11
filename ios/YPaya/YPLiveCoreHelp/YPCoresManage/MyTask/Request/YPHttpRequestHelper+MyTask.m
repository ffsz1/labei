//
//  YPHttpRequestHelper+MyTask.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+MyTask.h"

#import "YPMyTasker.h"
#import "NSObject+YYModel.h"

@implementation YPHttpRequestHelper (MyTask)


/**
 我的任务列表
 */
+ (void)getDutyListWithSuccess:(void (^)(id tasks))success failure:(void (^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"duty/list";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(YPAuthCoreHelp) getUid];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        
        YPMyTasker *tasks = [YPMyTasker yy_modelWithJSON:data];
        tasks.daily = [NSArray yy_modelArrayWithClass:[YPMyTaskModel class] json:tasks.daily];
        tasks.fresh = [NSArray yy_modelArrayWithClass:[YPMyTaskModel class] json:tasks.fresh];
        tasks.dailyTime = [NSArray yy_modelArrayWithClass:[YPMyTaskModel class] json:tasks.dailyTime];
        
        if (success) {
            success(tasks);
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


/**
 领取奖励

 @param dutyId 任务id
 */
+ (void)dutyAchieveWithDutyId:(NSInteger)dutyId success:(void (^)())success failure:(void (^)(NSNumber *code, NSString *msg))failure {
    NSString *method = @"duty/achieve";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(YPAuthCoreHelp) getUid];
    params[@"dutyId"] = @(dutyId);
    params[@"ticket"] = [GetCore(YPAuthCoreHelp) getTicket];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success();
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}



/**
 去大厅发言，发言上传接口
 */
+ (void)dutyFreshPublicWithSuccess:(void (^)())success failure:(void (^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"duty/fresh/public";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(YPAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(YPAuthCoreHelp) getTicket];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success();
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


/**
 微信QQ分享上传
 */
+ (void)dutyDailyShareWithSuccess:(void (^)())success failure:(void (^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"duty/daily/share";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(YPAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(YPAuthCoreHelp) getTicket];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success();
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

@end
