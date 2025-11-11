//
//  HJHttpRequestHelper+version.m
//  HJLive
//
//  Created by FF on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+version.h"
#import <NSObject+YYModel.h>
#import "VersionInfo.h"

@implementation HJHttpRequestHelper (version)

+ (void)configClientSuccess:(void (^)(id json))success failure:(void(^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"client/configure";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

//查询版本 状态
+ (void)checkVersion:(NSString *)version success:(void (^)(VersionInfo *info))success failure:(void (^)(NSNumber *, NSString *))failure {
    
    NSString *method = @"version/get";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:version forKey:@"version"];
    [params setObject:@"iOS" forKey:@"os"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        VersionInfo *versionInfo = [VersionInfo yy_modelWithJSON:data];
        //        BOOL result = [(NSNumber *)data boolValue];
        //        1线上版本,2 中版本,3强制更新版本,4建议更新版本,5已删除版本
        success(versionInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}



@end
