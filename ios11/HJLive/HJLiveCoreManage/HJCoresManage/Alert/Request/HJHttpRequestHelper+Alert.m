//
//  HJHttpRequestHelper+Alert.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Alert.h"
#import "NSObject+YYModel.h"

@implementation HJHttpRequestHelper (Alert)

+ (void)requestAlertInfoByTyp:(NSInteger)type
                      Success:(void (^)(AlertInfo *info))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"activity/query";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    [params setObject:@(type) forKey:@"type"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        AlertInfo *info = [AlertInfo yy_modelWithJSON:data];
        success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode,message);
    }];
    
}

+ (void)requestActivityList:(NSInteger)type
                      Success:(void (^)(NSArray *arr))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"activity/query";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    [params setObject:@(type) forKey:@"type"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *info = [NSArray yy_modelArrayWithClass:[AlertInfo class] json:data];
        success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode,message);
    }];
}

@end
