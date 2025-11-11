//
//  AuthCore.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAuthCoreHelp.h"
#import "HJAuthCoreClient.h"
#import "HJLinkedMeCore.h"
#import "HJLinkedMeClient.h"
#import "HJVersionCoreHelp.h"

#import "HJAccountInfoStorage.h"
#import "HJTicketListInfoStorage.h"
#import "TicketListInfo.h"
#import "HJTicketInfo.h"


#import "HJIReachability.h"

#import "NSObject+YYModel.h"
#import "HJHomeCoreClient.h"
#import "HJVerifyCodeCore.h"
#import "HJIMRequestManager+Login.h"

@interface HJAuthCoreHelp()<ReachabilityClient,HJHomeCoreClient>
@property(nonatomic, strong) AccountInfo *accountInfo;
@property(nonatomic, strong) TicketListInfo *ticketListInfo;

@property (strong, nonatomic) dispatch_source_t timer;
@property (strong, nonatomic) NSTimer *requestTimer;

@property (copy, nonatomic) NSString *openID;
@property (copy, nonatomic) NSString *unionID;
@property (copy, nonatomic) NSString *access_token;

@property (nonatomic, copy) NSString *phone;
@property (nonatomic, copy) NSString *password;
@property (nonatomic, copy) NSString *smsCode;
@property (nonatomic, copy) NSString *changePwd;

@property (nonatomic, assign) NSInteger failRepeatCount;//请求失败重试次数
@property (nonatomic, assign) NSInteger failRepeatMaxCount;//请求失败重试最大次数
@property (nonatomic, assign) NSInteger failRepeatTime;//请求重试时间

@property (nonatomic, assign, readwrite) NSInteger isLoginSuccess;

@end

@implementation HJAuthCoreHelp
- (instancetype)init
{
    self = [super init];
    if (self) {
        [self.wechtTool registerApp];
        _accountInfo = [HJAccountInfoStorage getCurrentAccountInfo];
        _ticketListInfo = [HJTicketListInfoStorage getCurrentTicketListInfo];
        if (_accountInfo == nil) {
            _accountInfo = [[AccountInfo alloc] init];
        }
        if (_ticketListInfo == nil) {
            _ticketListInfo = [[TicketListInfo alloc] init];
        }
        AddCoreClient(HJHomeCoreClient, self);
        
        _failRepeatCount = 0;
        _failRepeatMaxCount = 10;
        _failRepeatTime = 10;
        _isLoginSuccess = NO;
    }
    return self;
}

- (NSString *)getTicket
{
    if (self.ticketListInfo.tickets.count > 0) {
        NSString * ticketStr =[self.ticketListInfo.tickets safeObjectAtIndex:0];
        HJTicketInfo *info = [HJTicketInfo yy_modelWithJSON:ticketStr];
        if (info.ticket.length > 0) {
            return info.ticket;
        }else {
//            NotifyCoreClient(AuthCoreClient, @selector(onLogout), onLogout);
        }
        
    }
    return @"";
}

#pragma mark - share
- (void)shareToQQFriendsWithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr {
    self.qqTool.didShareSuccess = self.didShareSuccess;
    self.qqTool.didShareFailed = self.didShareFailed;
    [self.qqTool shareToQQFriendsWithTitle:title description:description photoUrlStr:photoUrlStr urlStr:urlStr];
}

- (void)shareToQQQzoneWithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr {
    self.qqTool.didShareSuccess = self.didShareSuccess;
    self.qqTool.didShareFailed = self.didShareFailed;
    [self.qqTool shareToQQQzoneWithTitle:title description:description photoUrlStr:photoUrlStr urlStr:urlStr];
}

- (void)shareToWechatFriendsWithTitle:(NSString *)title description:(NSString *)description webpageUrl:(NSString *)webpageUrl imageUrl:(NSString *)imageUrl {
    self.wechtTool.didShareSuccess = self.didShareSuccess;
    self.wechtTool.didShareFailed = self.didShareFailed;
    [self.wechtTool shareToWechatFriendsWithTitle:title description:description webpageUrl:webpageUrl imageUrl:imageUrl];
}
- (void)shareToWechatFriendsCircleWithTitle:(NSString *)title description:(NSString *)description webpageUrl:(NSString *)webpageUrl imageUrl:(NSString *)imageUrl {
    self.wechtTool.didShareSuccess = self.didShareSuccess;
    self.wechtTool.didShareFailed = self.didShareFailed;
    [self.wechtTool shareToWechatFriendsCircleWithTitle:title description:description webpageUrl:webpageUrl imageUrl:imageUrl];
}

#pragma mark - thirdLogin
- (void)loginWithQQ {
    [self.qqTool loginWithQQ];
}

-(void)loginWithWc
{
    [self.wechtTool loginWithWechat];
}

#pragma mark - setter&&getter
- (HJQQTool *)qqTool {
    if (!_qqTool) {
        _qqTool = [HJQQTool new];
    }
    return _qqTool;
}

- (HJWechatTool *)wechtTool {
    if (!_wechtTool) {
        _wechtTool = [HJWechatTool new];
    }
    return _wechtTool;
}

- (NSString *)getUid
{
    if (self.accountInfo.uid.length > 0) {
        return self.accountInfo.uid;
    }else {
//        NotifyCoreClient(AuthCoreClient, @selector(onLogout), onLogout);
    }
    return @"";
}

- (NSString *)getNetEaseToken
{
    return self.accountInfo.netEaseToken;
}

-(BOOL)isLogin
{
    return _accountInfo.access_token.length > 0;
}

-(void)regist:(NSString *)phone password:(NSString *)password smsCode:(NSString *)smsCode {
    self.type = XCPhoneRegister;
    self.phone = phone;
    self.password = password;
    self.smsCode = smsCode;
    [self configViewShow];

}


- (void)login:(NSString *)phone password:(NSString *)password
{
    self.type = XCPhoneLogin;
    self.phone = phone;
    self.password = password;
    [self configViewShow];


}

-(void)logout
{
    self.isLoginSuccess = NO;
    [HJHttpRequestHelper logout:self.accountInfo.access_token success:^{
        
    } failure:^(NSNumber *resCode, NSString *message) {
            
    }];
    [self reset];
    [HJIMRequestManager logout];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        NotifyCoreClient(HJAuthCoreClient, @selector(onLogout), onLogout);
    });
}

- (void)kicked
{
    [self reset];
    [HJIMRequestManager logout];
    NotifyCoreClient(HJAuthCoreClient, @selector(onKicked), onKicked);
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        NotifyCoreClient(HJAuthCoreClient, @selector(onLogout), onLogout);
    });
    
}


- (void) autoLogin
{
    if (![self isLogin]) {
        NotifyCoreClient(HJAuthCoreClient, @selector(onNeedLogin), onNeedLogin);
        return;
    }

    [self requestTicket];
}


- (void)requestTicket
{
    @weakify(self);
    [HJHttpRequestHelper requestTicket:self.accountInfo.access_token success:^(TicketListInfo *ticketListInfo) {
        @strongify(self);

            self.failRepeatCount = 0;
            if (ticketListInfo != nil) {
                self.ticketListInfo = ticketListInfo;
                [HJTicketListInfoStorage saveTicketListInfo:ticketListInfo];
                self.isLoginSuccess = YES;
                NotifyCoreClient(HJAuthCoreClient, @selector(onLoginSuccess), onLoginSuccess);
            }

    } failure:^(NSNumber *resCode, NSString *message) {
        @strongify(self);
        if (resCode != nil) {
            self.isLoginSuccess = NO;
            [self reset];
            NotifyCoreClient(HJAuthCoreClient, @selector(onLogout), onLogout);
        } else {
            if (self.failRepeatCount < self.failRepeatMaxCount) {
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(self.failRepeatTime * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    [self retry];
                });
                self.failRepeatCount ++;
            }
        }
    }];
}

- (void)reset
{
    if (self.info) {
        self.info = nil;
    }
    [HJAccountInfoStorage saveAccountInfo:nil];
    [HJTicketListInfoStorage saveTicketListInfo:nil];
    _accountInfo = [[AccountInfo alloc] init];
    _ticketListInfo = [[TicketListInfo alloc] init];
    _isLoginSuccess = NO;
    
    [self.qqTool logout];
    [self.wechtTool logout];
    _openID = nil;
    _unionID = nil;
    _access_token = nil;
    _type = 0;

}

- (void)setAccountInfo:(AccountInfo *)accountInfo {
    _accountInfo = accountInfo;
    NSUserDefaults *ud = [NSUserDefaults standardUserDefaults];
    [ud setValue:accountInfo.uid forKey:@"commonUserId"];
    [ud synchronize];
}

- (void) retry
{
    [self requestTicket];
}

- (void)requestResetSmsCode:(NSString *)phone
{
    [self requestSmsCode:phone type:@(3)];
}

- (void)requestLoginSmsCode:(NSString *)phone
{
    [self requestSmsCode:phone type:@(2)];
}

- (void)requestRegistSmsCode:(NSString *)phone
{
    [self requestSmsCode:phone type:@(1)];
}

- (void)requestConfirmCode:(NSString *)phone smsCode:(NSString *)smsCode success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    [HJHttpRequestHelper requestConfirmCode:phone smsCode:smsCode success:^{
        NotifyCoreClient(HJAuthCoreClient, @selector(confirmSuccess), confirmSuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJAuthCoreClient, @selector(confirmFail:), confirmFail:msg);
    }];
}

- (void)requestReplacePhone:(NSString *)phone smsCode:(NSString *)smsCode
{
    [HJHttpRequestHelper requestConfirmCode:phone smsCode:smsCode success:^{
        NotifyCoreClient(HJAuthCoreClient, @selector(replaceSuccess), replaceSuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJAuthCoreClient, @selector(replaceFail:), replaceFail:msg);
    }];
}

- (void)requestSmsCode:(NSString *)phone type:(NSNumber *)type
{
    @weakify(self)
    [HJHttpRequestHelper requestSmsCode:phone type:type success:^{
        NotifyCoreClient(HJAuthCoreClient, @selector(onRequestSmsCodeSuccess:), onRequestSmsCodeSuccess:type);
        @strongify(self)
        [self openCountdown];
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJAuthCoreClient, @selector(onRequestSmsCodeFailth:), onRequestSmsCodeFailth:message);
    }];
}

- (void)stopCountDown {
    if (self.timer != nil) {
        dispatch_source_cancel(_timer);
        self.timer = nil;
    }
}

- (void)openCountdown{
    
    __block NSInteger time = 59; //倒计时时间
    
    if (_timer != nil) {
        dispatch_source_cancel(_timer);
    }
    
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    
    dispatch_source_set_timer(_timer,dispatch_walltime(NULL, 0),1.0*NSEC_PER_SEC, 0); //每秒执行
    
    dispatch_source_set_event_handler(_timer, ^{
        
        if(time <= 0){ //倒计时结束，关闭
            
            dispatch_source_cancel(_timer);
            dispatch_async(dispatch_get_main_queue(), ^{
                NotifyCoreClient(HJAuthCoreClient, @selector(onCutdownFinish), onCutdownFinish);
            });
        }else{
            
            int seconds = time % 60;
            dispatch_async(dispatch_get_main_queue(), ^{
                //设置按钮显示读秒效果
                NotifyCoreClient(HJAuthCoreClient, @selector(onCutdownOpen:), onCutdownOpen:@(seconds));
            });
            time--;
        }
    });
    dispatch_resume(_timer);
}

- (void)requestModifyPwd:(NSString *)phone pwd:(NSString *)pwd newPwd:(NSString *)newPwd {
    
}

-(void)requestResetPwd:(NSString *)phone newPwd:(NSString *)newPwd smsCode:(NSString *)smsCode
{
    self.type = XCPhonePwd;
    self.phone = phone;
    self.changePwd = newPwd;
    self.smsCode = smsCode;
    
//    [self verifySuccess:@""];
    [self configViewShow];


}

- (void)reachabilityDidChange:(ReachabilityStatus)currentStatus
{
    
}

- (void)configViewShow {
    //---life-->
       [self verifySuccess:nil];
      return;
     //---life-->
    if (GetCore(HJVersionCoreHelp).checkIn) {
        [self verifySuccess:@""];
        return;
    }
    
    NSUserDefaults *ud = [NSUserDefaults standardUserDefaults];
    if ([ud objectForKey:@"savaVerifyCode"]) {
        NSString *save = [ud objectForKey:@"savaVerifyCode"];
        NSLog(@"%f",[[self currentTimeStr] doubleValue] - [save doubleValue]);
        if ([[self currentTimeStr] doubleValue] - [save doubleValue] < 300000) {
            @weakify(self);
            
            [GetCore(HJVerifyCodeCore) openVerifyCodeViewWithCompletionHandler:^(JXVerifyState state, NSString *valideCode, NSString *errorMessage) {
                @strongify(self);
                switch (state) {
                    case JXVerifyStateSuccess:
                    {
                        [self verifySuccess:valideCode];
                    }
                        break;
                    case JXVerifyStateFailure:
                    {
                        if (self.type == XCPhoneRegister) {
                            NotifyCoreClient(HJAuthCoreClient, @selector(onRegistFailth:), onRegistFailth:errorMessage);
                        }
                        
                        if (self.type == XCPhoneLogin) {
                            NotifyCoreClient(HJAuthCoreClient, @selector(onLoginFailth:), onLoginFailth:errorMessage);
                        }
                        
                        if (self.type == XCPhonePwd) {
                            NotifyCoreClient(HJAuthCoreClient, @selector(onResetPwdFailth:), onResetPwdFailth:errorMessage);
                        }
                        
                        if (self.type == XCThirdPartLoginQQ || self.type == XCThirdPartLoginWc) {
                            NotifyCoreClient(HJAuthCoreClient, @selector(onLoginFailth:), onLoginFailth:errorMessage);
                        }
                    }
                        break;
                        
                    default:
                        break;
                }
            }];
        } else {
            [self verifySuccess:nil];
        }
        [ud setObject:[self currentTimeStr] forKey:@"savaVerifyCode"];
    } else {
        [self verifySuccess:nil];
        [ud setObject:[self currentTimeStr] forKey:@"savaVerifyCode"];
    }
}

- (NSString *)currentTimeStr{
    NSDate* date = [NSDate dateWithTimeIntervalSinceNow:0];//获取当前时间0秒后的时间
    NSTimeInterval time=[date timeIntervalSince1970]*1000;// *1000 是精确到毫秒，不乘就是精确到秒
    NSString *timeString = [NSString stringWithFormat:@"%.0f", time];
    return timeString;
}


- (void)loginWithOpenID:(NSString *)openID andUnionID:(NSString *)unionID access_token:(NSString *)access_token andType:(XCThirdPartLoginType)type {
    
    self.openID = openID;
    self.unionID = unionID;
    self.access_token = access_token;
    self.type = type;
    
    if (type == XCPhoneLogin) {
        GetCore(HJAuthCoreHelp).qqInfo = nil;
        GetCore(HJAuthCoreHelp).info = nil;
    }
    
//    [self verifySuccess:@""];
    
    [self configViewShow];
    
}

- (void)verifySuccess:(NSString *)code {
    if (self.type == XCPhoneRegister) {
        [HJHttpRequestHelper regist:self.phone password:self.password smsCode:self.smsCode validateCode:code success:^{
            NotifyCoreClient(HJAuthCoreClient, @selector(onRegistSuccess), onRegistSuccess);
        } failure:^(NSNumber *resCode, NSString *message) {
            NotifyCoreClient(HJAuthCoreClient, @selector(onRegistFailth:), onRegistFailth:message);
        }];
    }
    
    if (self.type == XCPhoneLogin) {
        [HJHttpRequestHelper login:self.phone password:self.password validateCode:code success:^(AccountInfo *accountInfo) {
            if (accountInfo != nil) {
                self.accountInfo = accountInfo;
                [HJAccountInfoStorage saveAccountInfo:accountInfo];
                [self requestTicket];
            }
        } failure:^(NSNumber *resCode, NSString *message) {
            NotifyCoreClient(HJAuthCoreClient, @selector(onLoginFailth:), onLoginFailth:message);
        }];
    }
    
    if (self.type == XCPhonePwd) {
        [HJHttpRequestHelper requestResetPwd:self.phone newPwd:self.changePwd smsCode:self.smsCode validateCode:code success:^{
            NotifyCoreClient(HJAuthCoreClient, @selector(onResetPwdSuccess), onResetPwdSuccess);
        } failure:^(NSNumber *resCode, NSString *message) {
            NotifyCoreClient(HJAuthCoreClient, @selector(onResetPwdFailth:), onResetPwdFailth:message);
        }];
    }
    
    if (self.type == XCThirdPartLoginQQ || self.type == XCThirdPartLoginWc) {
        [HJHttpRequestHelper loginWithWeChatOpenID:self.openID andUnionID:self.unionID access_token:self.access_token andType:self.type validateCode:code success:^(AccountInfo *accountInfo) {
            if (accountInfo != nil) {
                self.accountInfo = accountInfo;
                [HJAccountInfoStorage saveAccountInfo:accountInfo];
                [self requestTicket];
            }
        } failure:^(NSNumber *resCode, NSString *message) {
            NotifyCoreClient(HJAuthCoreClient, @selector(onLoginFailth:), onLoginFailth:message);
        }];
    }
}


//统计接口
- (void)statisticsWith:(NSURL *)url {
    [HJHttpRequestHelper statisticsWithURL:url success:^(BOOL isSuccess) {
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

//是否绑定手机do
- (void)isBindingPhoneSuccess:(void (^)(BOOL isbinding))success  {
    [HJHttpRequestHelper judgeIsBindingPhoneWithsuccess:^(BOOL isbinding) {
        success(isbinding);
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

//是否绑定手机
- (void)isBindingPhoneSuccess:(void (^)(BOOL isbinding))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure  {
    [HJHttpRequestHelper judgeIsBindingPhoneWithsuccess:^(BOOL isbinding) {
        !success ?: success(isbinding);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode, message);
    }];
}

#pragma mark - HomeCoreClient
- (void)networkReconnect:(NSInteger)tag {
//    [self requestTicket];
}




@end
