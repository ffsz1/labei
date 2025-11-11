//
//  YPHttpRequestHelper+NotiFriend.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+NotiFriend.h"
#import "YPNotiFriendInfo.h"

@implementation YPHttpRequestHelper (NotiFriend)
+ (void)requestLobbyChatInfo:(void (^)(NSArray *))success
               failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"room/getLobbyChatInfo";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];

    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];

    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *notiFriendInfoArr = [NSArray yy_modelArrayWithClass:[YPNotiFriendInfo class] json:data];
        success(notiFriendInfoArr);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
@end
