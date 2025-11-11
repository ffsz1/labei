//
//  HJAuthViewControllerCenter.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAuthViewControllerCenter.h"
#import "YYViewControllerCenter.h"
#import "HJAuthCoreClient.h"
#import "HJAuthCoreHelp.h"
#import "HJImLoginCoreClient.h"
#import "HJHttpErrorClient.h"
#import "HJLoginViewController.h"
#import "UIView+XCToast.h"
#import "HJRoomViewControllerCenter.h"
#import "HJVersionCoreClient.h"
#import "HJHomeCoreClient.h"
#import "HJVersionCoreHelp.h"

#import "HJUpdateView.h"
#import "HJNoticeView.h"
#import "HJTopAlertViewTool.h"

#import "HJBaseNavigationController.h"

@interface HJAuthViewControllerCenter ()<HJAuthCoreClient, HJImLoginCoreClient, HJHttpErrorClient,HJVersionCoreClient,HJHomeCoreClient>

@property (nonatomic, strong) UINavigationController *navController;
@property (nonatomic, strong) UIAlertController *networkAlert;
@property (nonatomic, assign) NSInteger errorCount;
@property (nonatomic, assign) BOOL hadShow;
@end

@implementation HJAuthViewControllerCenter

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
    id currentController = [YYViewControllerCenter currentViewController];
    if ([currentController isKindOfClass:[HJLoginViewController class]]) {
        return NO;
    }
    
    return YES;
}

-(void)toLogin {
//    UINavigationController *navigationController = [[XBDAuthViewControllerFactory sharedFactory] instantiateAuthNavigationController];
//    if (![self shouldSkipToLogin]) return;
//
//    self.navController = navigationController;
    
    UIViewController *cla = [YYViewControllerCenter currentViewController];
    if ([cla.className isEqualToString:@"HJLoginViewController"]) {
        return;
    }
    
    UIViewController *vc = HJLoginStoryBoard(@"HJLoginViewController");
    HJBaseNavigationController *naviController = [[HJBaseNavigationController alloc] initWithRootViewController:vc];
    if (@available(iOS 13.0, *)) {
            naviController.modalPresentationStyle = UIModalPresentationFullScreen;
       }
    [[YYViewControllerCenter currentViewController] presentViewController:naviController animated:YES completion:nil];
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
//        [GetCore(HJAuthCoreHelp) logout];
    });
}

#pragma mark - ImLoginClient
- (void)onImLoginFailth {
    [GetCore(HJAuthCoreHelp) logout];
}

- (void)onImKick {
    [GetCore(HJAuthCoreHelp) kicked];
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
    [GetCore(HJAuthCoreHelp) logout];
}

- (void)networkDisconnect {
    [MBProgressHUD hideHUD];
//    if (self.errorCount < 2) {
//        self.errorCount++;
        [[HJTopAlertViewTool shareHJTopAlertViewTool]showBadNetworkAlertView];
//    }
}

#pragma mark - VersionCoreClient
- (void)appNeedUpdateWithDesc:(NSString *)desc version:(NSString *)version {
    [[HJTopAlertViewTool shareHJTopAlertViewTool]showUpdateViewWithDesc:desc version:version];
}



- (void)appNeedNoticeWithDesc:(NSString *)desc version:(NSString *)version {
    [[HJTopAlertViewTool shareHJTopAlertViewTool]showNoticeViewWithDesc:desc version:version];
}

#pragma mark - HomeCoreClient

- (void)networkReconnect:(NSInteger)tag {
    
//    [GetCore(HJVersionCoreHelp) getVersionData];

    [self.networkAlert dismissViewControllerAnimated:NO completion:nil];
}


@end
