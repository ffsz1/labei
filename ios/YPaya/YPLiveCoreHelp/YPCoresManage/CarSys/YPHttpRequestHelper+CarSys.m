//
//  YPHttpRequestHelper+CarSys.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+CarSys.h"
#import "YPAuthCoreHelp.h"

@implementation YPHttpRequestHelper (CarSys)

+ (void)sendCarWithCarId:(NSString *)carId
               targetUid:(NSString *)targetUid
                 Success:(void (^)(id))success
                 failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"giftCar/give";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:uid forKey:@"uid"];
    [params setObject:targetUid forKey:@"targetUid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:carId forKey:@"carId"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getCarSysUseWithCarId:(NSString *)carId
                      Success:(void (^)(id))success
                      failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"giftCar/use";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:carId forKey:@"carId"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getCarSysPurseWithType:(NSString *)type
                         CarId:(NSString *)carId
                        Success:(void (^)(id))success
                        failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"giftCar/purse";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:carId forKey:@"carId"];
    [params setObject:type forKey:@"type"];

    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getCarSysListWithPageNum:(NSString *)pageNum
                    withPageSize:(NSString *)pageSize
                          userId:(NSString *)userId
                         success:(void (^)(NSArray *list))success
                         failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"giftCar/listMall";//@"giftCar/list";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    [params setObject:uid forKey:@"uid"];
    [params setObject:userId forKey:@"queryUid"];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:pageNum forKey:@"pageNum"];
    [params setObject:@"25" forKey:@"pageSize"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getMyCarSysListWithPageNum:(NSString *)pageNum
                      withPageSize:(NSString *)pageSize
                            userId:(NSString *)userId
                           success:(void (^)(NSArray *list))success
                           failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"giftCar/list";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    [params setObject:uid forKey:@"uid"];
    [params setObject:userId forKey:@"queryUid"];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:pageNum forKey:@"pageNum"];
    [params setObject:@"25" forKey:@"pageSize"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}


+ (void)getUserCarList:(UserID)userId
               PageNum:(NSString *)pageNum
              PageSize:(NSString *)pageSize
               success:(void(^)(NSArray *list))success
               failure:(void(^)(NSNumber *resCode,NSString *message))failure {
    if (userId <= 0) {
        return;
    }
    
//    NSString *method = @"giftCar/getList";
    NSString *method = @"giftCar/user/list";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    [params setObject:uid forKey:@"uid"];
    [params setObject:@(userId) forKey:@"queryUid"];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:pageNum forKey:@"pageNum"];
    [params setObject:@"25" forKey:@"pageSize"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }]; 
}


+ (void)getUserHeadList:(UserID)userId
                PageNum:(NSString *)pageNum
               PageSize:(NSString *)pageSize
                success:(void(^)(NSArray *list))success
                failure:(void(^)(NSNumber *resCode,NSString *message))failure {
    if (userId <= 0) {
        return;
    }
    //    NSString *method = @"headwear/getList";
    NSString *method = @"headwear/user/list";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    [params setObject:uid forKey:@"uid"];
    [params setObject:@(userId) forKey:@"queryUid"];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:pageNum forKey:@"pageNum"];
    [params setObject:@"25" forKey:@"pageSize"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}


@end
