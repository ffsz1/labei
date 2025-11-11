//
//  HJHttpRequestHelper+Youth.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Youth.h"

@implementation HJHttpRequestHelper (Youth)

+ (void)getUsersTeensMode:(void(^)(BOOL hadSet))success
                  failure:(void(^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"users/teens/mode/getUsersTeensMode";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        if (data == nil) {
            success(NO);
        }else{
            success(YES);
        }
        
//        HJMMHomeInfoModel *info = [HJMMHomeInfoModel yy_modelWithJSON:data];;
//
//        !success ?: success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode, message);
    }];
}


@end
