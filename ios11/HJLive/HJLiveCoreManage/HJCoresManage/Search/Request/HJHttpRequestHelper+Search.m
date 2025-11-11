//
//  HJHttpRequestHelper+Search.m
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Search.h"
#import "NSObject+YYModel.h"
#import "HJSearchResultInfo.h"
#import "UserInfo.h"

@implementation HJHttpRequestHelper (Search)

+ (void)requestInfoWithKey:(NSString *)key Success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"search/room";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:key forKey:@"key"];
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *resultList = [NSArray yy_modelArrayWithClass:[HJSearchResultInfo class] json:data];
        success(resultList);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
    
}

+ (void)searchUser:(NSString *)key pageNo:(NSInteger)pageNo pageSize:(NSInteger)pageSize success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    if (key == nil) {
        return;
    }
    
    NSString *method = @"search/user";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:key forKey:@"key"];
    [params setObject:@(25) forKey:@"pageSize"];
    [params setObject:@(pageNo) forKey:@"pageNo"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *userInfos = [NSArray yy_modelArrayWithClass:[UserInfo class] json:data];
        if (success) {
            success(userInfos);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

@end
