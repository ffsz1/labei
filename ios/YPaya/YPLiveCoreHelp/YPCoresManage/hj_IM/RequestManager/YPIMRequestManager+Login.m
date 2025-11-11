//
//  YPIMRequestManager+Login.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMRequestManager+Login.h"

#import "YPIMRequestManager+Private.h"
#import <objc/runtime.h>

#import "YPIMDefines.h"

static int JXIMLoginParamsKey;
static int JXIMLoginSuccessHandlerKey;
static int JXIMLoginFailureHandlerKey;
static int JXIMRepeatConnectKey;
static int JXIMRepeatTimeIntervalKey;
static int JXIMRepeatTimerKey;

@implementation YPIMRequestManager (Login)
/**
 客户端登录IM服务器
 */
+ (void)loginWithUid:(NSString *)uid
              ticket:(NSString *)ticket
               success:(JXIMRequestSuccessHander)success
               failure:(JXIMRequestFailureHander)failure {
    [[self defaultManager] loginWithUid:uid ticket:ticket success:success failure:failure];
}

- (void)loginWithUid:(NSString *)uid
              ticket:(NSString *)ticket
             success:(JXIMRequestSuccessHander)success
             failure:(JXIMRequestFailureHander)failure {
//    if (self.repeatTimer) {
//        dispatch_cancel(self.repeatTimer);
//        self.repeatTimer = nil;
//    }
    self.loginParams = nil;
    self.loginSuccessHandler = nil;
    self.loginFailureHandler = nil;
    
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    dic[@"uid"] = JX_STR_AVOID_nil(uid);
    dic[@"ticket"] = JX_STR_AVOID_nil(ticket);
    dic[@"page_name"] = @(JXIMPageTypeiOS);
    dic[@"appVersion"] = [UIApplication sharedApplication].appVersion;
    dic[@"appid"] = JX_APPID_VERSION;
    
    self.loginParams = dic;
    self.loginSuccessHandler = success;
    self.loginFailureHandler = failure;
//    self.repeatConnect = YES;
//    self.repeatTimeInterval = 10;
    
    [GetCore(YPWebSocketCore) connect];
}

/**
 自动登录
 */
+ (void)autoLoginWithSuccess:(JXIMRequestSuccessHander)success
                     failure:(JXIMRequestFailureHander)failure {
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    
    [self loginWithUid:uid ticket:ticket success:^{
        [YYLogger info:@"autoLogin_WebSocketLoginManager" message:@"autoLogin_WebSocketLoginManager"];
        !success ?: success();
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        !failure ?: failure(code, errorMessage);
    }];
}

+ (void)logout {
    [GetCore(YPWebSocketCore) disConnect];
}

static void jx_im_dispatchTimer(id target, double timeInterval,void (^handler)(dispatch_source_t timer))
{
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t timer =dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER,0, 0, queue);
    dispatch_source_set_timer(timer, dispatch_walltime(NULL, 0), (uint64_t)(timeInterval *NSEC_PER_SEC), 0);
    __weak __typeof(target) weaktarget  = target;
    dispatch_source_set_event_handler(timer, ^{
        if (!weaktarget)  {
            dispatch_cancel(timer);
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                if (handler) handler(timer);
            });
        }
    });

    dispatch_resume(timer);
}

#pragma mark - <HJWebSocketCoreClient>
- (void)onConnectSuccess {
//    self.repeatConnect = NO;
//    if (self.repeatTimer) {
//        dispatch_cancel(self.repeatTimer);
//        self.repeatTimer = nil;
//    }
    
    @weakify(self);
    [GetCore(YPWebSocketCore) send:JX_IM_ROUTE_LOGIN content:[YPIMRequestManager defaultManager].loginParams success:^(id data) {
        @strongify(self);
        !self.loginSuccessHandler ?: self.loginSuccessHandler();
    } failure:^(NSInteger code, NSString *errmsg) {
        !self.loginFailureHandler ?: self.loginFailureHandler(code, errmsg);
    }];
}

- (void)onDisconnect:(NSInteger)code reason:(NSString *)reason {
//    if (self.repeatConnect == YES) {
//        if (!self.repeatTimer) {
//            jx_im_dispatchTimer(self, self.repeatTimeInterval, ^(dispatch_source_t timer) {
//                [GetCore(YPWebSocketCore) connect];
//            });
//        }
//    }
}

#pragma mark - setter/getter
- (void)setLoginParams:(id)loginParams {
    objc_setAssociatedObject(self, &JXIMLoginParamsKey, loginParams, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (id)loginParams {
    return objc_getAssociatedObject(self, &JXIMLoginParamsKey);
}

- (void)setLoginSuccessHandler:(JXIMRequestSuccessHander)loginSuccessHandler {
    objc_setAssociatedObject(self, &JXIMLoginSuccessHandlerKey, loginSuccessHandler, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (JXIMRequestSuccessHander)loginSuccessHandler {
    return objc_getAssociatedObject(self, &JXIMLoginSuccessHandlerKey);
}

- (void)setLoginFailureHandler:(JXIMRequestFailureHander)loginFailureHandler {
    objc_setAssociatedObject(self, &JXIMLoginFailureHandlerKey, loginFailureHandler, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (JXIMRequestFailureHander)loginFailureHandler {
    return objc_getAssociatedObject(self, &JXIMLoginFailureHandlerKey);
}

- (void)setRepeatConnect:(BOOL)repeatConnect {
    objc_setAssociatedObject(self, &JXIMRepeatConnectKey, @(repeatConnect), OBJC_ASSOCIATION_ASSIGN);
}

- (BOOL)repeatConnect {
    return [objc_getAssociatedObject(self, &JXIMRepeatConnectKey) boolValue];
}

- (void)setRepeatTimeInterval:(NSTimeInterval)repeatTimeInterval {
    objc_setAssociatedObject(self, &JXIMRepeatTimeIntervalKey, @(repeatTimeInterval), OBJC_ASSOCIATION_ASSIGN);
}

- (NSTimeInterval)repeatTimeInterval {
    return [objc_getAssociatedObject(self, &JXIMRepeatTimeIntervalKey) doubleValue];
}

- (void)setRepeatTimer:(dispatch_source_t)repeatTimer {
    objc_setAssociatedObject(self, &JXIMRepeatTimerKey, repeatTimer, OBJC_ASSOCIATION_ASSIGN);
}

- (dispatch_source_t)repeatTimer {
    return objc_getAssociatedObject(self, &JXIMRepeatTimerKey);
}

@end
