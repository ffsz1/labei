//
//  AppDelegate+HJJpush.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "AppDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface AppDelegate (HJJpush)
- (void)registerJpushWithOptions:(NSDictionary *)launchOptions;
- (void)jpushDidRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;

@end

NS_ASSUME_NONNULL_END
