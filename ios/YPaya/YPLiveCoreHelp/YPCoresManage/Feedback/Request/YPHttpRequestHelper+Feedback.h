//
//  YPHttpRequestHelper+Feedback.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"

@interface YPHttpRequestHelper (Feedback)
/**
 反馈

 @param content 内容
 @param contact 联系人
 @param success 成功
 @param failure 失败
 */
+ (void)requestFeedback:(NSString *)content contact:(NSString *)contact
                success:(void (^)(void))success
                failure:(void (^)(NSNumber *resCode, NSString *message))failure;
@end
