//
//  HJQQTool.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "TencentOpenAPI/TencentOAuth.h"
#import "TencentOpenAPI/QQApiInterfaceObject.h"
#import "TencentOpenAPI/QQApiInterface.h"
#import "HJQqUserInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJQQTool : NSObject<TencentSessionDelegate, QQApiInterfaceDelegate>

- (void)loginWithQQ;
- (void)logout;

- (void)shareToQQFriendsWithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr;

- (void)shareToQQQzoneWithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr;

@property (nonatomic, copy) void(^didShareSuccess)();
@property (nonatomic, copy) void(^didShareFailed)();

@end

NS_ASSUME_NONNULL_END
