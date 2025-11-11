//
//  YPHttpRequestHelper+Egg.m
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Egg.h"

#import "YPImRoomCoreV2.h"

#import "YPGiftInfo.h"
#import "YPEggRecordModel.h"

@implementation YPHttpRequestHelper (Egg)

+ (void)userGiftPurseDraw:(NSString *)type
                  success:(void (^)(id data))success
                  failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"user/giftPurse/v2/draw";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:type forKey:@"type"];
    params[@"roomId"] = @(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId);
    
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

+ (void)getPrizePoolGift:(void (^)(NSArray *arr))success
                 failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"user/giftPurse/getPrizePoolGift";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSLog(@"%@",data);
        
        NSArray *arr = [NSArray yy_modelArrayWithClass:[YPGiftInfo class] json:data];
        
        if (success) {
            success(arr);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


+ (void)getEggRecord:(NSInteger)pageNum
             success:(void (^)(NSArray *arr))success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"user/giftPurse/record";
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:@(pageNum) forKey:@"pageNum"];
    [params setObject:@(20) forKey:@"pageSize"];

    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSLog(@"%@",data);
        
        NSArray *arr = [NSArray yy_modelArrayWithClass:[YPEggRecordModel class] json:data];
        
        if (success) {
            success(arr);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}



@end
