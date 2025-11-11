//
//  YPHttpRequestHelper+Headwear.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.

#import "YPHttpRequestHelper+Headwear.h"

@implementation YPHttpRequestHelper (Headwear)

+ (void)sendHeadwearWithHeadwearId:(NSString *)headwearId
                         targetUid:(NSString *)targetUid
                           Success:(void (^)(id))success
                           failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"headwear/give";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:uid forKey:@"uid"];
    [params setObject:targetUid forKey:@"targetUid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:headwearId forKey:@"headwearId"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getHeadwearListWithPageNum:(NSString *)pageNum
                      withPageSize:(NSString *)pageSize
                            userId:(NSString *)userId
                           success:(void (^)(NSArray *list))success
                           failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"headwear/listMall";//@"headwear/list";
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

+ (void)getMyHeadwearListWithPageNum:(NSString *)pageNum
                        withPageSize:(NSString *)pageSize
                              userId:(NSString *)userId
                             success:(void (^)(NSArray *list))success
                             failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"headwear/list";
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

+ (void)getHeadwearPurseWithType:(NSString *)type
                           CarId:(NSString *)headwearId
                         Success:(void (^)(id))success
                         failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"headwear/purse";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:headwearId forKey:@"headwearId"];
    [params setObject:type forKey:@"type"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getHeadwearUseWithHeadwearId:(NSString *)headwearId
                        Success:(void (^)(id))success
                        failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"headwear/use";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:headwearId forKey:@"headwearId"];

    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

@end
