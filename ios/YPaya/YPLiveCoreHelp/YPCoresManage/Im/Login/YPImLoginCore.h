//
//  ImLoginCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"
#import <Foundation/Foundation.h>

@interface YPImLoginCore : YPBaseCore
- (void)updateApnsToken:(NSData *)token;
- (void)handleRemoteNotification:(NSDictionary *)userInfo;
- (BOOL)isImLogin;
- (void)markNotificationRead;
@end
