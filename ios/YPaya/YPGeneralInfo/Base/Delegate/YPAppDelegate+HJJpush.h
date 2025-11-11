//
//  YPAppDelegate+HJJpush.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAppDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPAppDelegate (HJJpush)
- (void)registerJpushWithOptions:(NSDictionary *)launchOptions;
- (void)jpushDidRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;

@end

NS_ASSUME_NONNULL_END
