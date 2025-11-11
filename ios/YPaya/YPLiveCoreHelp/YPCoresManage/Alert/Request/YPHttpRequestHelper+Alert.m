//
//  YPHttpRequestHelper+Alert.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Alert.h"
#import "NSObject+YYModel.h"

@implementation YPHttpRequestHelper (Alert)

+ (void)requestAlertInfoByTyp:(NSInteger)type
                      Success:(void (^)(YPAlertInfo *info))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"activity/query";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    [params setObject:@(type) forKey:@"type"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        YPAlertInfo *info = [YPAlertInfo yy_modelWithJSON:data];
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
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *info = [NSArray yy_modelArrayWithClass:[YPAlertInfo class] json:data];
        success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode,message);
    }];
}

@end
