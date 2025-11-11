//
//  HJHttpRequestHelper+LongZhu.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+LongZhu.h"

@implementation HJHttpRequestHelper (LongZhu)


/**
 获取速配状态

 @param roomId 房间id
 */
+ (void)getStateWithRoomId:(NSInteger)roomId success:(void (^)(NSDictionary *result))success failure:(void (^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"room/game/getState";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];

    params[@"roomId"] = @(roomId);
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        if (success) {
            success(data);
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

/**
 放弃解签
 
 @param roomId 房间id
 @param type 1 速配  2 选择
 @param result 如果type=1传空  type=2传选择的数
 */
+ (void)cancelChooseResultWithRoomId:(NSInteger)roomId
                                type:(NSInteger)type
                              result:(NSString *)result
                             success:(void (^)())success
                             failure:(void (^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"room/game/cancel";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"roomId"] = @(roomId);
    params[@"type"] = @(type);
    params[@"result"] = result;
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
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
 获取速配随机数/保存自己选择的数

 @param roomId 房间id
 @param type 1 速配  2 选择
 @param result 如果type=1传空  type=2传选择的数
 */
+ (void)getChooseResultWithRoomId:(NSInteger)roomId
                             type:(NSInteger)type
                           result:(NSString *)result
                          success:(void (^)(NSInteger state))success
                          failure:(void (^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"room/game/choose";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    params[@"roomId"] = @(roomId);
    params[@"type"] = @(type);
    params[@"result"] = result;
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
        NSInteger state = 0;
        
        NSArray *arr = [[data description] componentsSeparatedByString:@","];
        
        if (arr.count >= 3) {
            NSInteger num1 = [arr[0] integerValue];
            NSInteger num2 = [arr[1] integerValue];
            NSInteger num3 = [arr[2] integerValue];
            state = num1 * 100 + num2 * 10 + num3;
        }
        
        if (success) {
            success(state);
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
    
}


/**
 展示结果

 @param result 展示的数字
 @param roomId 房间id
 @param type type 1 速配  2 选择
 */
+ (void)confirmResult:(NSString *)result
               roomId:(NSInteger)roomId
                 type:(NSInteger)type
              success:(void (^)())success
              failure:(void (^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"room/game/confirm";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    params[@"roomId"] = @(roomId);
    params[@"type"] = @(type);
    params[@"result"] = result;
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
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
