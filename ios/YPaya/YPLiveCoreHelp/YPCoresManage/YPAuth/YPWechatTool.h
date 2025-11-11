//
//  YPWechatTool.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "WXApi.h"
#import "YPWeChatUserInfo.h"

NS_ASSUME_NONNULL_BEGIN


@interface YPWechatTool : NSObject<WXApiDelegate>

- (void)registerApp;
- (void)loginWithWechat;
- (void)logout;

- (void)shareToWechatFriendsWithTitle:(NSString *)title description:(NSString *)description webpageUrl:(NSString *)webpageUrl imageUrl:(NSString *)imageUrl;
- (void)shareToWechatFriendsCircleWithTitle:(NSString *)title description:(NSString *)description webpageUrl:(NSString *)webpageUrl imageUrl:(NSString *)imageUrl;

@property (nonatomic, copy) void(^didShareSuccess)();
@property (nonatomic, copy) void(^didShareFailed)();

@end

NS_ASSUME_NONNULL_END
