//
//  YPHttpRequestHelper+Feedback.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Feedback.h"
#import "YPAuthCoreHelp.h"

@implementation YPHttpRequestHelper (Feedback)
+ (void)requestFeedback:(NSString *)content contact:(NSString *)contact
                success:(void (^)(void))success
                failure:(void (^)(NSNumber *resCode, NSString *message))failure;
{
    NSString *method = @"feedback";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:content forKey:@"feedbackDesc"];
    [params setObject:contact forKey:@"contact"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        if (success) {
            success();
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
@end
