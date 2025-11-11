//
//  HJFeedbackCore.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFeedbackCore.h"
#import "HJHttpRequestHelper+Feedback.h"
#import "HJFeedbackCoreClient.h"
@implementation HJFeedbackCore
- (void)requestFeedback:(NSString *)content contact:(NSString *)contact
{
    [HJHttpRequestHelper requestFeedback:content contact:contact success:^{
        NotifyCoreClient(HJFeedbackCoreClient, @selector(onRequestFeedbackSuccess), onRequestFeedbackSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJFeedbackCoreClient, @selector(onRequestFeedbackFailth), onRequestFeedbackFailth);
    }];
}
@end
