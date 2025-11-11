//
//  HJHttpRequestHelper+Egg.m
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Egg.h"

#import "HJImRoomCoreV2.h"

#import "GiftInfo.h"
#import "HJEggRecordModel.h"

@implementation HJHttpRequestHelper (Egg)

+ (void)userGiftPurseDraw:(NSString *)type
                  success:(void (^)(id data))success
                  failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"user/giftPurse/dy/draw";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:type forKey:@"type"];
    params[@"roomId"] = @(GetCore(HJImRoomCoreV2).currentRoomInfo.roomId);
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
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
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSLog(@"%@",data);
        
        NSArray *arr = [NSArray yy_modelArrayWithClass:[GiftInfo class] json:data];
        
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
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:@(pageNum) forKey:@"pageNum"];
    [params setObject:@(20) forKey:@"pageSize"];

    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSLog(@"%@",data);
        
        NSArray *arr = [NSArray yy_modelArrayWithClass:[HJEggRecordModel class] json:data];
        
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
