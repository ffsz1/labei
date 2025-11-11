//
//  YPFeedbackCore.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFeedbackCore.h"
#import "YPHttpRequestHelper+Feedback.h"
#import "HJFeedbackCoreClient.h"
@implementation YPFeedbackCore
- (void)requestFeedback:(NSString *)content contact:(NSString *)contact
{
    [YPHttpRequestHelper requestFeedback:content contact:contact success:^{
        NotifyCoreClient(HJFeedbackCoreClient, @selector(onRequestFeedbackSuccess), onRequestFeedbackSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJFeedbackCoreClient, @selector(onRequestFeedbackFailth), onRequestFeedbackFailth);
    }];
}
@end
