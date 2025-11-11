//
//  AuthCore.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import "TicketListInfo.h"
#import "AccountInfo.h"
#import "HJHttpRequestHelper+Auth.h"
#import "HJWeChatUserInfo.h"
#import "HJQqUserInfo.h"
#import "HJQQTool.h"
#import "HJWechatTool.h"

@interface HJAuthCoreHelp : BaseCore

@property (nonatomic, strong) HJWeChatUserInfo *info;
@property (nonatomic, strong) HJQqUserInfo *qqInfo;
@property (nonatomic, assign) BOOL isNewRegister;
@property (nonatomic, assign, readonly) NSInteger isLoginSuccess; ///< 是否登录成功(校验Ticket后)

@property (nonatomic, strong) HJQQTool *qqTool;
@property (nonatomic, strong) HJWechatTool *wechtTool;
@property (nonatomic, assign) NSInteger shareType;

@property (nonatomic, copy) void(^didShareSuccess)(NSInteger shareType);
@property (nonatomic, copy) void(^didShareFailed)();

@property (assign, nonatomic) XCThirdPartLoginType type;

- (BOOL)isLogin;
-(NSString *)getTicket;
-(NSString *)getUid;
-(NSString *)getNetEaseToken;

- (void)regist:(NSString *)phone password:(NSString *)password smsCode:(NSString *)smsCode;
- (void)login:(NSString *)phone password:(NSString *)password;
- (void)autoLogin;
- (void)logout;
- (void)kicked;
- (void)requestTicket;
- (void)openCountdown;
- (void)requestRegistSmsCode:(NSString *)phone;
- (void)requestLoginSmsCode:(NSString *)phone;
- (void)requestResetSmsCode:(NSString *)phone;
- (void)requestSmsCode:(NSString *)phone type:(NSNumber *)type;
- (void)requestReplacePhone:(NSString *)phone smsCode:(NSString *)smsCode;
- (void)requestResetPwd:(NSString *)phone newPwd:(NSString *)newPwd smsCode:(NSString *)smsCode;
- (void)requestModifyPwd:(NSString *)phone pwd:(NSString *)pwd newPwd:(NSString *)newPwd;
- (void)stopCountDown; //关闭定时器

- (void)requestConfirmCode:(NSString *)phone smsCode:(NSString *)smsCode success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure;
/**
 第三方登录

 @param openID 第三方openID
 @param unionID 第三方unionID
 */
- (void)loginWithOpenID:(NSString *)openID
             andUnionID:(NSString *)unionID
           access_token:(NSString *)access_token
                andType:(XCThirdPartLoginType)type;

- (void)loginWithQQ;
- (void)loginWithWc;

- (void)shareToQQFriendsWithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr;
- (void)shareToQQQzoneWithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr;
- (void)shareToWechatFriendsWithTitle:(NSString *)title description:(NSString *)description webpageUrl:(NSString *)webpageUrl imageUrl:(NSString *)imageUrl;
- (void)shareToWechatFriendsCircleWithTitle:(NSString *)title description:(NSString *)description webpageUrl:(NSString *)webpageUrl imageUrl:(NSString *)imageUrl;


/**
 统计接口

 @param url 渠道URL
 */
- (void)statisticsWith:(NSURL *)url;

//是否绑定手机do
- (void)isBindingPhoneSuccess:(void (^)(BOOL isbinding))success;

//是否绑定手机
- (void)isBindingPhoneSuccess:(void (^)(BOOL isbinding))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure;

@end
