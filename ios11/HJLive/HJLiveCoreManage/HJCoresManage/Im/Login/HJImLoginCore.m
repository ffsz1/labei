//
//  ImLoginCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJImLoginCore.h"
#import "HJAuthCoreClient.h"
#import <NIMSDK/NIMSDK.h>
#import <NIMSDK/NIMSDKConfig.h>
#import <NIMSDK/NIMLoginClient.h>
#import "HJAuthCoreHelp.h"
#import "HJImLoginCoreClient.h"
#import <NIMSDK/NIMLoginManagerProtocol.h>
#import "PurseCore.h"

#import "HJNIMKitCore.h"

#import "HJIMRequestManager+Login.h"


@interface HJImLoginCore()<HJAuthCoreClient, NIMLoginManagerDelegate,HJWebSocketCoreClient>
@property (nonatomic, strong) NSData *apnsToken;
@end

@implementation HJImLoginCore
- (instancetype)init
{
    self = [super init];
    if (self) {
        AddCoreClient(HJAuthCoreClient, self);
        AddCoreClient(HJWebSocketCoreClient, self);
#ifdef DEBUG
        [[NIMSDK sharedSDK] registerWithAppID:JX_NIM_APP_KEY cerName:@"erbandevelop"];
//        [[NIMSDK sharedSDK] registerWithAppID:@"e655919f13b02388594d43451b801560" cerName:@"erbandevelop"];
#else
        [[NIMSDK sharedSDK] registerWithAppID:JX_NIM_APP_KEY cerName:@"erbanproduct"];
//        [[NIMSDK sharedSDK] registerWithAppID:@"e655919f13b02388594d43451b801560" cerName:@"erbanproduct"];

        
#endif
        
        [NIMSDKConfig sharedConfig].enabledHttpsForInfo = NO;
        [[NIMSDK sharedSDK].loginManager addDelegate:self];
        [GetCore(HJNIMKitCore)initCustomerMsgDecoder];
        [self registerAPNs];
    }
    return self;
}

- (void)dealloc
{
    RemoveCoreClient(HJAuthCoreClient, self);
    [[NIMSDK sharedSDK].loginManager removeDelegate:self];
}

- (void)registerAPNs
{
    if ([[UIApplication sharedApplication] respondsToSelector:@selector(registerForRemoteNotifications)])
    {
        UIUserNotificationType types = UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound |      UIRemoteNotificationTypeAlert;
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:types
                                                                                 categories:nil];
        [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
        [[UIApplication sharedApplication] registerForRemoteNotifications];
    }
    else
    {
        UIRemoteNotificationType types = UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeSound |        UIRemoteNotificationTypeBadge;
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:types];
    }
    
}

- (void)handleRemoteNotification:(NSDictionary *)userInfo
{
    [YYLogger info:TAPNs message:@"receive remote notification: %@", userInfo];
    if (userInfo != nil && userInfo.count > 0) {
        NotifyCoreClient(HJImLoginCoreClient, @selector(onRecieveRemoteNotification:), onRecieveRemoteNotification:userInfo);
    }
}

- (BOOL)isImLogin
{
    return [[NIMSDK sharedSDK].loginManager isLogined];
}

- (void)updateApnsToken:(NSData *)token
{
    self.apnsToken = token;
}

#pragma -mark AuthCoreClient
- (void)onLoginSuccess
{
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *token = [GetCore(HJAuthCoreHelp) getNetEaseToken];
    
    [HJIMRequestManager loginWithUid:uid ticket:ticket success:^{
        [YYLogger info:@"ImLoginCore_WebSocketLoginManager" message:@"loginSuccess_WebSocketLoginManager--->uid=%@", uid];
    
        
        //
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        NotifyCoreClient(HJImLoginCoreClient, @selector(onImLoginFailth), onImLoginFailth);
    }];
    
    NIMAutoLoginData *data = [[NIMAutoLoginData alloc] init];
    data.account = uid;
    data.token = token;
    data.forcedMode = NO;
    [[NIMSDK sharedSDK].loginManager autoLogin:data];
    [[NIMSDK sharedSDK].systemNotificationManager markAllNotificationsAsRead];
    [self markNotificationRead];
}

- (void)onLogout
{
    [[NIMSDK sharedSDK].loginManager logout:^(NSError * _Nullable error) {
        if (error == nil) {
            [YYLogger info:@"ImLoginCore" message:@"loginOutSuccess"];
            NotifyCoreClient(HJImLoginCoreClient, @selector(onImLogoutSuccess), onImLogoutSuccess);
        } else {
            [YYLogger info:@"ImLoginCore" message:@"loginFailed--->error=%ld", error.code];
        }
    }];
}

#pragma -mark 云信sdk
- (void)onLogin:(NIMLoginStep)step
{
    if (step == NIMLoginStepLoginOK) {
        [[NIMSDK sharedSDK] updateApnsToken:self.apnsToken];
        NotifyCoreClient(HJImLoginCoreClient, @selector(onImLoginSuccess), onImLoginSuccess);
    } else if (step == NIMLoginStepSyncOK) {
        NotifyCoreClient(HJImLoginCoreClient, @selector(onImSyncSuccess), onImSyncSuccess);
    }
}

- (void)onAutoLoginFailed:(NSError *)error
{
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    NSString *token = [GetCore(HJAuthCoreHelp) getNetEaseToken];
    [[[NIMSDK sharedSDK] loginManager] login:uid token:token completion:^(NSError * _Nullable error) {
        if (error != nil) {
            [YYLogger info:@"ImLoginCore" message:@"loginFailed--->uid=%@ error=%ld", uid, error.code];
            
            NotifyCoreClient(HJImLoginCoreClient, @selector(onImLoginFailth), onImLoginFailth);
        } else {
            [YYLogger info:@"ImLoginCore" message:@"loginSuccess--->uid=%@", uid];
        }
    }];
}

- (void)onKick:(NIMKickReason)code clientType:(NIMLoginClientType)clientType
{
    [YYLogger info:@"ImLoginCore" message:@"kick"];
    NotifyCoreClient(HJImLoginCoreClient, @selector(onImKick), onImKick);
}

- (void)markNotificationRead {
    [[NIMSDK sharedSDK].apnsManager registerBadgeCountHandler:^NSUInteger{
        return 0;
    }];
}
//
//#pragma mark - HJHomeCoreClient
//- (void)networkReconnect {
//    if ([GetCore(AuthCore)getTicket]) {
//        [self onLoginSuccess];
//    }
//}

@end
