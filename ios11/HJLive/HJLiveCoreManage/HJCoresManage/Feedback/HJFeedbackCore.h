//
//  HJFeedbackCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"

@interface HJFeedbackCore : BaseCore
- (void)requestFeedback:(NSString *)content contact:(NSString *)contact;
@end
