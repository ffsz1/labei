//
//  YPHttpRequestHelper+MICCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+MICCore.h"
#import "YPUserCoreHelp.h"

@implementation YPHttpRequestHelper (MICCore)

+ (void)getSoundMatchCharmUserWithSuccess:(void (^)(NSArray *list))success
                       failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"user/soundMatch/randomUser";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    UserID uid = [[GetCore(YPAuthCoreHelp) getUid] userIDValue];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
//    UserInfo *info = [GetCore(UserCore) getUserInfoInDB:uid];
//    [params setObject:[NSString stringWithFormat:@"%d",info.gender] forKey:@"gender"];
    [params setObject:@"0" forKey:@"gender"];

    [params setObject:@"99" forKey:@"maxAge"];
    [params setObject:@"0" forKey:@"minAge"];

    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getCharmUserListWithSuccess:(void (^)(NSArray *list))success
                            failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"user/soundMatch/charmUser";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    UserID uid = [[GetCore(YPAuthCoreHelp) getUid] userIDValue];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@"1" forKey:@"pageNum"];
    [params setObject:@"50" forKey:@"pageSize"];
        UserInfo *info = [GetCore(YPUserCoreHelp) getUserInfoInDB:uid];
        [params setObject:[NSString stringWithFormat:@"%d",info.gender] forKey:@"gender"];
   
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getLinkPoolWithSuccess:(void (^)(NSArray *list))success
                       failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"room/linkPool";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    UserID uid = [[GetCore(YPAuthCoreHelp) getUid] userIDValue];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getMICLinkUserSuccess:(void (^)(NSDictionary *userInfo))success
                      failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"room/link";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    UserID uid = [[GetCore(YPAuthCoreHelp) getUid] userIDValue];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

@end
