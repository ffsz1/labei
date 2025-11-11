//
//  YPHttpRequestHelper+PK.m
//  HJLive
//
//  Created by apple on 2019/6/18.
//

#import "YPHttpRequestHelper+PK.h"

#import "YPImRoomCoreV2.h"

#import "NSObject+YYModel.h"

#import "YPRoomPKProbability.h"


@implementation YPHttpRequestHelper (PK)

+ (void)pk_getState:(void (^)(BOOL open))success
            failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"play/mora/getState";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    params[@"roomId"] = @(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId);

    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        
        BOOL openMora = NO;
        if (data != nil) {
            NSInteger state = [data integerValue];
            openMora = state==1?YES:NO;
        }
        
        if (success) {
            success(openMora);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)pk_getProbability:(void (^)(NSArray* arr))success
               failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"play/mora/getProbability";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    params[@"roomId"] = @(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId);
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *arr = [NSArray yy_modelArrayWithClass:[YPRoomPKProbability class] json:data];
        
        if (success) {
            success(arr);
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)pk_getMoraInfo:(NSInteger)probability
               success:(void (^)(NSArray* arr,int num,int moraTime))success
               failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"play/mora/getMoraInfo";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:[NSNumber numberWithInteger:probability] forKey:@"probability"];
    params[@"roomId"] = @(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId);
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *arr ;
        if ([[data allKeys] containsObject:@"giftInfoVOList"]) {
            arr = [NSArray yy_modelArrayWithClass:[YPRoomPKGiftModel class] json:data[@"giftInfoVOList"]];
        }
        
        
        int time = 0;
        if ([[data allKeys] containsObject:@"num"]) {
            time = [[data objectForKey:@"num"] intValue];
        }
        
        int moraTime = 0;
        if ([[data allKeys] containsObject:@"moraTime"]) {
            moraTime = [[data objectForKey:@"moraTime"] intValue];
        }
        
        
        
        if (success) {
            success(arr,time,moraTime);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)pk_confirmPk:(NSString *)probability
              choose:(NSInteger)choose
              giftId:(NSInteger)giftId
              giftNum:(NSInteger)giftNum
             success:(void (^)(id data))success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"play/mora/confirmPk";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:probability forKey:@"probability"];
    params[@"roomId"] = @(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId);
    
    [params setObject:[NSNumber numberWithInteger:choose] forKey:@"choose"];
    [params setObject:[NSNumber numberWithInteger:giftId] forKey:@"giftId"];
    [params setObject:[NSNumber numberWithInteger:giftNum] forKey:@"giftNum"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        NSLog(@"%@",data);
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)pk_joinPk:(NSString *)recordId
             success:(void (^)(id data))success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"play/mora/joinPk";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:recordId forKey:@"recordId"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        NSLog(@"%@",data);
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)pk_confirmJoinPk:(NSString *)recordId
                  choose:(NSInteger)choose
          success:(void (^)(id data))success
          failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"play/mora/confirmJoinPk";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:recordId forKey:@"recordId"];
    [params setObject:[NSNumber numberWithInteger:choose] forKey:@"choose"];

    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        NSLog(@"%@",data);
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


+ (void)pk_record:(NSInteger)current
          success:(void (^)(NSArray *arr))success
          failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"play/mora/record";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    params[@"roomId"] = @(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId);
    
    [params setObject:[NSNumber numberWithInteger:current] forKey:@"current"];
    [params setObject:[NSNumber numberWithInteger:20] forKey:@"pageSize"];

    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSLog(@"%@",data);
        
        
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)pk_unSureRecord:(void (^)(NSArray *arr))success
                failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"play/mora/getMoraRecord";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    params[@"roomId"] = @(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId);
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSLog(@"%@",data);
        
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

@end
