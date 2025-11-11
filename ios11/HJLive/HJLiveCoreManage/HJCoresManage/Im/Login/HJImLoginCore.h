//
//  ImLoginCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import <Foundation/Foundation.h>

@interface HJImLoginCore : BaseCore
- (void)updateApnsToken:(NSData *)token;
- (void)handleRemoteNotification:(NSDictionary *)userInfo;
- (BOOL)isImLogin;
- (void)markNotificationRead;
@end
