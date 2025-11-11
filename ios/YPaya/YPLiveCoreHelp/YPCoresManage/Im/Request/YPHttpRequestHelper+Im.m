//
//  YPHttpRequestHelper+Im.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Im.h"

@implementation YPHttpRequestHelper (Im)

+(void)getAgoraKeyWith:(NSString *)channel uid:(NSString *)uid success:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    
    NSString *method = @"agora/getKey";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = uid;
    params[@"roomId"] = channel;
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        
        NSString *token = [data description];
        
        if (token.length) {
            success(data);
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode, message);
        }
    }];
}


/**
 获取违禁词正则
 */
+ (void)sensitiveWordRegexWithSuccess:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"sensitiveWord/regex";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        
        NSString *text = [data description];
        
        success(text);

    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode, message);
        }
    }];
}

@end
