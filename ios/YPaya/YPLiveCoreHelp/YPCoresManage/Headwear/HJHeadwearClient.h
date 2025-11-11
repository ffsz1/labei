//
//  HJHeadwearClient.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJHeadwearClient<NSObject>

- (void)getHeadwearListSuccessWithArr:(NSArray *)HeadwearList;
- (void)getHeadwearListFailWithMessage:(NSString *)message;
- (void)buyHeadwearSuccess:(id)data;
- (void)buyHeadwearFailWithCode:(NSNumber *)code Message:(NSString *)msg;
- (void)continueHeadwearSuccess:(id)data;
- (void)continueHeadwearFailWithCode:(NSNumber *)code WithMessage:(NSString *)msg;

- (void)userHeadwearSuccess;
- (void)userHeadwearFail:(NSString *)msg;

- (void)sendHeadwearSuccess;
- (void)sendHeadwearFail:(NSString *)msg;
@end
