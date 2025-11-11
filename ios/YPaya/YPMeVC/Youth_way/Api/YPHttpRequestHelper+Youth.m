//
//  YPHttpRequestHelper+Youth.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Youth.h"

@implementation YPHttpRequestHelper (Youth)

+ (void)getUsersTeensMode:(void(^)(BOOL hadSet))success
                  failure:(void(^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"users/teens/mode/getUsersTeensMode";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(YPAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(YPAuthCoreHelp) getTicket];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        
        if (data == nil) {
            success(NO);
        }else{
            success(YES);
        }
        
//        YPMMHomeInfoModel *info = [YPMMHomeInfoModel yy_modelWithJSON:data];;
//
//        !success ?: success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode, message);
    }];
}


@end
