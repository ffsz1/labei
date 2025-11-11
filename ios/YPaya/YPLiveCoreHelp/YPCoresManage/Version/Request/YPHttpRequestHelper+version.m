//
//  YPHttpRequestHelper+version.m
//  HJLive
//
//  Created by FF on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+version.h"
#import <NSObject+YYModel.h>
#import "YPVersionInfo.h"

@implementation YPHttpRequestHelper (version)

+ (void)configClientSuccess:(void (^)(id json))success failure:(void(^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"client/configure";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

//查询版本 状态
+ (void)checkVersion:(NSString *)version success:(void (^)(YPVersionInfo *info))success failure:(void (^)(NSNumber *, NSString *))failure {
    
    NSString *method = @"version/get";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:version forKey:@"version"];
    [params setObject:@"iOS" forKey:@"os"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        YPVersionInfo *versionInfo = [YPVersionInfo yy_modelWithJSON:data];
        //        BOOL result = [(NSNumber *)data boolValue];
        //        1线上版本,2 中版本,3强制更新版本,4建议更新版本,5已删除版本
        success(versionInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}



@end
