//
//  HJFeedbackCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJFeedbackCoreClient <NSObject>
- (void)onRequestFeedbackSuccess;
- (void)onRequestFeedbackFailth;
@end
