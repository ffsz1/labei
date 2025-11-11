//
//  YPHttpRequestHelper+Binding.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Binding.h"

@implementation YPHttpRequestHelper (Binding)

+ (void)getBindingSmsCode:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"accounts/getSmsCode";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    NSString *uid = GetCore(YPAuthCoreHelp).getUid;
    NSString *ticket = GetCore(YPAuthCoreHelp).getTicket;
    
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)bindingValidateCode:(NSString *)code success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"accounts/validateCode";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    NSString *uid = GetCore(YPAuthCoreHelp).getUid;
    NSString *ticket = GetCore(YPAuthCoreHelp).getTicket;

    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:code forKey:@"code"];

    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)bindThird:(NSString *)openId unionId:(NSString *)unionId accessToken:(NSString *)accessToken type:(NSInteger)type success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"accounts/bindThird";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    NSString *uid = GetCore(YPAuthCoreHelp).getUid;
    NSString *ticket = GetCore(YPAuthCoreHelp).getTicket;
    
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:openId forKey:@"openId"];
    [params setObject:unionId forKey:@"unionId"];
    [params setObject:accessToken forKey:@"accessToken"];
    [params setObject:[NSNumber numberWithInteger:type] forKey:@"type"];

    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)untiedThird:(NSInteger)type success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"accounts/untiedThird";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    NSString *uid = GetCore(YPAuthCoreHelp).getUid;
    NSString *ticket = GetCore(YPAuthCoreHelp).getTicket;
    
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:[NSNumber numberWithInteger:type] forKey:@"type"];

    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

@end
