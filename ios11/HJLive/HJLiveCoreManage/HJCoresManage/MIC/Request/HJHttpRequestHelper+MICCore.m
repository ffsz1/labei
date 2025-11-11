//
//  HJHttpRequestHelper+MICCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+MICCore.h"
#import "HJUserCoreHelp.h"

@implementation HJHttpRequestHelper (MICCore)

+ (void)getSoundMatchCharmUserWithSuccess:(void (^)(NSArray *list))success
                       failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"user/soundMatch/randomUser";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    UserID uid = [[GetCore(HJAuthCoreHelp) getUid] userIDValue];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
//    UserInfo *info = [GetCore(UserCore) getUserInfoInDB:uid];
//    [params setObject:[NSString stringWithFormat:@"%d",info.gender] forKey:@"gender"];
    [params setObject:@"0" forKey:@"gender"];

    [params setObject:@"99" forKey:@"maxAge"];
    [params setObject:@"0" forKey:@"minAge"];

    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getCharmUserListWithSuccess:(void (^)(NSArray *list))success
                            failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"user/soundMatch/charmUser";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    UserID uid = [[GetCore(HJAuthCoreHelp) getUid] userIDValue];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@"1" forKey:@"pageNum"];
    [params setObject:@"50" forKey:@"pageSize"];
        UserInfo *info = [GetCore(HJUserCoreHelp) getUserInfoInDB:uid];
        [params setObject:[NSString stringWithFormat:@"%d",info.gender] forKey:@"gender"];
   
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getLinkPoolWithSuccess:(void (^)(NSArray *list))success
                       failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"room/linkPool";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    UserID uid = [[GetCore(HJAuthCoreHelp) getUid] userIDValue];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getMICLinkUserSuccess:(void (^)(NSDictionary *userInfo))success
                      failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"room/link";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    UserID uid = [[GetCore(HJAuthCoreHelp) getUid] userIDValue];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

@end
