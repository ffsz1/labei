//
//  HttpRequestHelper+Log.m
//  XChat
//
//  Created by apple on 2019/4/16.
//  Copyright © 2019 XC. All rights reserved.
//

#import "HttpRequestHelper+Log.h"

@implementation HJHttpRequestHelper (Log)

+ (void)clientLogSaveWithUrl:(NSString *)url success:(void(^)())success failure:(void(^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"client/log/save";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    params[@"url"] = url;
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        if (success) {
            success();
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}

@end
