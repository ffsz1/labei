//
//  YPAuthViewControllerCenter.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAuthViewControllerCenter.h"
#import "YPYYViewControllerCenter.h"
#import "HJAuthCoreClient.h"
#import "YPAuthCoreHelp.h"
#import "HJImLoginCoreClient.h"
#import "HJHttpErrorClient.h"
#import "YPLoginViewController.h"
#import "UIView+XCToast.h"
#import "YPRoomViewControllerCenter.h"
#import "HJVersionCoreClient.h"
#import "HJHomeCoreClient.h"
#import "YPVersionCoreHelp.h"

#import "YPUpdateView.h"
#import "YPNoticeView.h"
#import "YPTopAlertViewTool.h"

#import "YPBaseNavigationController.h"

@interface YPAuthViewControllerCenter ()<HJAuthCoreClient, HJImLoginCoreClient, HJHttpErrorClient,HJVersionCoreClient,HJHomeCoreClient>

@property (nonatomic, strong) UINavigationController *navController;
@property (nonatomic, strong) UIAlertController *networkAlert;
@property (nonatomic, assign) NSInteger errorCount;
@property (nonatomic, assign) BOOL hadShow;
@end

@implementation YPAuthViewControllerCenter

+ (instancetype)defaultCenter {
    static dispatch_once_t onceToken = 0;
    static id instance;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        self.hadShow = NO;
        AddCoreClient(HJAuthCoreClient, self);
        AddCoreClient(HJImLoginCoreClient, self);
        AddCoreClient(HJHttpErrorClient, self);
        AddCoreClient(HJVersionCoreClient, self);
        AddCoreClient(HJHomeCoreClient, self);
    }
    return self;
    
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (BOOL)shouldSkipToLogin {
    id currentController = [YPYYViewControllerCenter currentViewController];
    if ([currentController isKindOfClass:[YPLoginViewController class]]) {
        return NO;
    }
    
    return YES;
}

-(void)toLogin {
//    UINavigationController *navigationController = [[XBDAuthViewControllerFactory sharedFactory] instantiateAuthNavigationController];
//    if (![self shouldSkipToLogin]) return;
//
//    self.navController = navigationController;
    
    UIViewController *cla = [YPYYViewControllerCenter currentViewController];
    if ([cla.className isEqualToString:@"YPLoginViewController"]) {
        return;
    }
    
    UIViewController *vc = YPLoginStoryBoard(@"YPLoginViewController");
    YPBaseNavigationController *naviController = [[YPBaseNavigationController alloc] initWithRootViewController:vc];
    if (@available(iOS 13.0, *)) {
            naviController.modalPresentationStyle = UIModalPresentationFullScreen;
       }
    [[YPYYViewControllerCenter currentViewController] presentViewController:naviController animated:YES completion:nil];
}

#pragma mark - AuthCoreClient
- (void)onNeedLogin {
    [self toLogin];
}
- (void)onLogout {
    if (![self shouldSkipToLogin]) return;
    
    dispatch_time_t delayTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5/*延迟执行时间*/ * NSEC_PER_SEC));
    @weakify(self);
    dispatch_after(delayTime, dispatch_get_main_queue(), ^{
        @strongify(self);
        [self toLogin];
    });
}

- (void)onKicked
{
    dispatch_time_t delayTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.0/*延迟执行时间*/ * NSEC_PER_SEC));
    @weakify(self);
    dispatch_after(delayTime, dispatch_get_main_queue(), ^{
        @strongify(self);
        [UIView showToastInKeyWindow:NSLocalizedString(XCBeKickedTip, nil) duration:3 position:YYToastPositionCenter];
//        [GetCore(YPAuthCoreHelp) logout];
    });
}

#pragma mark - ImLoginClient
- (void)onImLoginFailth {
    [GetCore(YPAuthCoreHelp) logout];
}

- (void)onImKick {
    [GetCore(YPAuthCoreHelp) kicked];
}

#pragma mark - HttpErrorClient
- (void)requestFailureWithMsg:(NSString *)msg {
    [MBProgressHUD hideHUD];
    if ([msg isEqualToString:@"2103:账户余额不足，请充值"]) {
        
    }else{
         [UIView showToastInKeyWindow:msg duration:1.0 position:(YYToastPosition)YYToastPositionCenter];
    }
   
}

- (void)onTicketInvalid {
    [GetCore(YPAuthCoreHelp) logout];
}

- (void)networkDisconnect {
    [MBProgressHUD hideHUD];
//    if (self.errorCount < 2) {
//        self.errorCount++;
        [[YPTopAlertViewTool shareHJTopAlertViewTool]showBadNetworkAlertView];
//    }
}

#pragma mark - VersionCoreClient
- (void)appNeedUpdateWithDesc:(NSString *)desc version:(NSString *)version {
    [[YPTopAlertViewTool shareHJTopAlertViewTool]showUpdateViewWithDesc:desc version:version];
}



- (void)appNeedNoticeWithDesc:(NSString *)desc version:(NSString *)version {
    [[YPTopAlertViewTool shareHJTopAlertViewTool]showNoticeViewWithDesc:desc version:version];
}

#pragma mark - HomeCoreClient

- (void)networkReconnect:(NSInteger)tag {
    
//    [GetCore(YPVersionCoreHelp) getVersionData];

    [self.networkAlert dismissViewControllerAnimated:NO completion:nil];
}


@end
