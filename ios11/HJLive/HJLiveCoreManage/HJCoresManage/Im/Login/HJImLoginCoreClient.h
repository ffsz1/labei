//
//  ImLoginClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJImLoginCoreClient <NSObject>
@optional
- (void)onImLoginSuccess;
- (void)onImSyncSuccess;
- (void)onImLogoutSuccess;
- (void)onImLoginFailth;
- (void)onImKick;
- (void)onRecieveRemoteNotification:(NSDictionary *)payload;
@end
