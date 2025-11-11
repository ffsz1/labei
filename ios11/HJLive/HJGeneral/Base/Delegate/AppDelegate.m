//
//  AppDelegate.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "AppDelegate.h"
#import "YYWindow.h"

#import "HJAuthViewControllerCenter.h"
#import "HJAuthCoreHelp.h"

#import "UIImage+Utils.h"


#import "HJImLoginCore.h"
#import "HJRoomViewControllerCenter.h"
#import "HJPurseViewControllerCenter.h"
#import "HJGuidView.h"
#import "IAPShare.h"
#import "PurseCore.h"
#import "HJVersionCoreHelp.h"

#import "HJLinkedMeCore.h"
#import <LinkedME_iOS/LinkedME.h>

#import <NIMSDK/NIMSDK.h>
#import "AppDelegate+HJConfiguration.h"
#import "AppDelegate+HJUISetting.h"
#import "UIColor+UIColor_Hex.h"
#import "HJDBManager.h"
#import "AppDelegate+HJJpush.h"

#import "HJTopAlertViewTool.h"
#import "HJUserCoreHelp.h"
#import "HJImRoomCoreV2.h"

@interface AppDelegate ()
@property (nonatomic, assign) BOOL needHandlerNotification;
@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
#ifdef DEBUG
#else
    if ([UIDevice currentDevice].isJailbroken) {exit(0);}
#endif
    
    [self initEnv];
    
    [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
    
    [self _initCore];//core
    AddCoreClient(HJImLoginCoreClient, self);
    [self config];
    
    [[YYTheme defaultTheme] apply];//theme
    [self initControllerCenter];
    
    self.window = [[YYWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    self.window.backgroundColor = [UIColor colorWithHexString:@"#f5f5f5"];
    
    [GetCore(HJLinkedMeCore) initLinkedMEWithLaunchOptions:launchOptions];
    
    NSDictionary *launchOptionsRemoteNotification = [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey];
    if (launchOptionsRemoteNotification != nil) {
        [GetCore(HJImLoginCore) handleRemoteNotification:launchOptionsRemoteNotification];
    }
    
    // Jpush
    [self registerJpushWithOptions:launchOptions];
    //去掉icon小红点
    
    [self setTabBar];//先显示主页，
    [self.window makeKeyAndVisible];
    [GetCore(HJAuthCoreHelp) autoLogin];//主页中判断是否登陆
    
    //life-hj 配置广告
//    [self configADView];
    
    return YES;
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    
    [YYLogger info:TApp message:@"applicationDidBecomeActive"];
    [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
    [[NIMSDK sharedSDK].systemNotificationManager markAllNotificationsAsRead];
    [GetCore(HJImLoginCore) markNotificationRead];
    self.needHandlerNotification = NO;
    
    [[NSNotificationCenter defaultCenter] postNotificationName:kApplicationDidBecomeActiveNotification object:nil];
    
}

//初始化网络环境
- (void)initEnv {
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
#ifdef DEBUG
//    if (![userDefault objectForKey:EnvID]) { //如果没有默认环境 即第一次加载
        [userDefault setObject:@"0" forKey:EnvID]; //设置默认环境为证实环境 0测试 1正式
//    }
#else
//    if (![userDefault objectForKey:EnvID]) { //如果没有默认环境 即第一次加载
        [userDefault setObject:@"1" forKey:EnvID]; //设置默认环境为证实环境 0测试 1正式
//    }
#endif
}

- (void)showGuidView {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *version = [defaults objectForKey:@"version"];
    NSString *currentVersion = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"];
    
    if (version.length <= 0 || ![version isEqualToString:currentVersion]) {
        HJGuidView *guidView = [[HJGuidView alloc] init];
        guidView.frame = [UIScreen mainScreen].bounds;
        [self.window addSubview:guidView];
        [defaults setObject:currentVersion forKey:@"version"];
    }
}
//初始化ControllerCenter
- (void)initControllerCenter{
    [HJAuthViewControllerCenter defaultCenter];
    [HJRoomViewControllerCenter defaultCenter];
    [HJPurseViewControllerCenter defaultCenter];
}


#pragma mark - Init Core

- (void)_initCore {
    [CoreManager initCore];
    
    [GetCore(HJVersionCoreHelp) getVersionData];
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    [GetCore(HJImLoginCore) updateApnsToken:deviceToken];
    // Jpush
    [self jpushDidRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
}

//URI Scheme 实现深度链接技术
- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options
{
    //判断是否是通过LinkedME的UrlScheme唤起App
    if ([[url description] rangeOfString:@"click_id"].location != NSNotFound) {
        return [GetCore(HJLinkedMeCore)judgeDeepLinkWith:url];
    }
    else if ([url.scheme isEqualToString:JX_QQ_SEXADECIMAL]) {
        [QQApiInterface handleOpenURL:url delegate:GetCore(HJAuthCoreHelp).qqTool];
    }
    else if ([url.scheme isEqualToString:[NSString stringWithFormat:@"tencent%@",JX_QQ_APP_ID]]) {
        [QQApiInterface handleOpenURL:url delegate:GetCore(HJAuthCoreHelp).qqTool];
        return [TencentOAuth HandleOpenURL:url];
    }
    else if ([url.scheme isEqualToString:[NSString stringWithFormat:@"%@",JX_WE_CHAT_APP_ID]]) {
        return [WXApi handleOpenURL:url delegate:GetCore(HJAuthCoreHelp).wechtTool];
    }

    
    return YES;
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    //判断是否是通过LinkedME的UrlScheme唤起App
    if ([[url description] rangeOfString:@"click_id"].location != NSNotFound) {
        return [GetCore(HJLinkedMeCore)judgeDeepLinkWith:url];
    }
    else if ([url.scheme isEqualToString:JX_QQ_SEXADECIMAL]) {
        [QQApiInterface handleOpenURL:url delegate:GetCore(HJAuthCoreHelp).qqTool];
    }
    else if ([url.scheme isEqualToString:[NSString stringWithFormat:@"tencent%@",JX_QQ_APP_ID]]) {
        [QQApiInterface handleOpenURL:url delegate:GetCore(HJAuthCoreHelp).qqTool];
        return [TencentOAuth HandleOpenURL:url];
    }
    else if ([url.scheme isEqualToString:[NSString stringWithFormat:@"%@",JX_WE_CHAT_APP_ID]]) {
        return [WXApi handleOpenURL:url delegate:GetCore(HJAuthCoreHelp).wechtTool];
    }
    
    return YES;
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url{
    if ([url.scheme isEqualToString:[NSString stringWithFormat:@"tencent%@",JX_QQ_APP_ID]]) {
        [QQApiInterface handleOpenURL:url delegate:GetCore(HJAuthCoreHelp).qqTool];
        return [TencentOAuth HandleOpenURL:url];
    }
    else if ([url.scheme isEqualToString:JX_QQ_SEXADECIMAL]) {
        [QQApiInterface handleOpenURL:url delegate:GetCore(HJAuthCoreHelp).qqTool];
    }
    else if ([url.scheme isEqualToString:[NSString stringWithFormat:@"%@",JX_WE_CHAT_APP_ID]]) {
        return [WXApi handleOpenURL:url delegate:GetCore(HJAuthCoreHelp).wechtTool];
    }
    return YES;
}

//Universal Links 通用链接实现深度链接技术
- (BOOL)application:(UIApplication*)application continueUserActivity:(NSUserActivity*)userActivity restorationHandler:(void (^)(NSArray*))restorationHandler{
    
    //判断是否是通过LinkedME的Universal Links唤起App
    if ([[userActivity.webpageURL description] rangeOfString:@"lkme.cc"].location != NSNotFound) {
        return  [GetCore(HJLinkedMeCore)judgeDeepLinkWithUniversal:userActivity];
    }
    
    return YES;
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    [YYLogger info:TApp message:@"didReceiveRemoteNotification"];
    if (self.needHandlerNotification) {
        [GetCore(HJImLoginCore) handleRemoteNotification:userInfo];
    }
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    [YYLogger info:TApp message:@"applicationWillResignActive"];
}


- (void)applicationDidReceiveMemoryWarning:(UIApplication *)application
{
}

- (void) :(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    [YYLogger info:TApp message:@"applicationDidEnterBackground"];
    self.needHandlerNotification = YES;
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
//    [YYLogger info:TApp message:@"applicationWillEnterForeground"];
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    
//    [GetCore(HJRoomCoreV2Help) closeRoom:GetCore(AuthCore).getUid.userIDValue];
    
    
    //杀掉进程处理
    //退出IM房间
    [GetCore(HJImRoomCoreV2) exitChatRoom:GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
    
}



@end
