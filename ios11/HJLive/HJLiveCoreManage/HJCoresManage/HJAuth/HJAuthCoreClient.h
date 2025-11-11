//
//  AuthCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AccountInfo.h"
#import "TicketListInfo.h"

@protocol HJAuthCoreClient <NSObject>
@optional
- (void)onNeedLogin;
- (void)onGetVisitorSuccess;
- (void)onGetVisitorFailth:(NSString *)errorMsg;
- (void)onRegistSuccess;
- (void)onRegistFailth:(NSString *)errorMsg;
- (void)onRequestSmsCodeSuccess:(NSNumber *)type;
- (void)onRequestSmsCodeFailth:(NSString *)errorMsg;
- (void)onLoginSuccess;
- (void)thirdPartLoginCancel;//第三方登录取消
- (void)thirdPartLoginFailth; //第三方登录失败
- (void)onLoginFailth:(NSString *)errorMsg;
- (void)onLogout;
- (void)onKicked;
- (void)onRequestTicket:(TicketListInfo *)ticketListInfo;
- (void)onResetPwdSuccess;
- (void)onResetPwdFailth:(NSString *)errorMsg;
- (void)onCutdownOpen:(NSNumber *)number;
- (void)onCutdownFinish;

- (void)confirmSuccess;
- (void)confirmFail:(NSString *)msg;

- (void)replaceSuccess;
- (void)replaceFail:(NSString *)msg;
@end
