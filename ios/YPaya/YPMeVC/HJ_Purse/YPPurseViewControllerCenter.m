//
//  YPPurseViewControllerCenter.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPurseViewControllerCenter.h"
#import "HJBalanceErrorClient.h"
#import "YPYYViewControllerCenter.h"
#import "YPPurseViewControllerFactory.h"
@interface YPPurseViewControllerCenter()<HJBalanceErrorClient>

@end
@implementation YPPurseViewControllerCenter
+ (instancetype)defaultCenter
{
    static dispatch_once_t onceToken = 0;
    static id instance;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
//        AddCoreClient(BalanceErrorClient, self);
    }
    return self;
}

- (void)dealloc
{
    RemoveCoreClient(HJBalanceErrorClient, self);
}

- (void)showRechargeAlert
{
    // 准备初始化配置参数
    NSString *title = NSLocalizedString(XCPurseNoMoneyTitle, nil);
    NSString *message = NSLocalizedString(XCPurseNoMoneyTip1, nil);
    NSString *okButtonTitle = NSLocalizedString(XCPurseGoToRecharge1, nil);
    NSString *cancelButtonTitle = NSLocalizedString(NIMKitCancel, nil);
    
    // 初始化
    UIAlertController *alertDialog = [UIAlertController alertControllerWithTitle:title message:message preferredStyle:UIAlertControllerStyleAlert];
    
    // 创建操作
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:okButtonTitle style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
        UIViewController *current = [YPYYViewControllerCenter currentViewController];
        [current presentViewController:vc animated:YES completion:nil];
    }];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:cancelButtonTitle style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        
    }];
    
    // 添加操作
    [alertDialog addAction:okAction];
    [alertDialog addAction:cancelAction];
    
    // 呈现警告视图
    UIViewController *current = [YPYYViewControllerCenter currentVisiableRootViewController];
    [current presentViewController:alertDialog animated:YES completion:nil];
}

- (void)toRecharge
{
    [self showRechargeAlert];
}

#pragma mark - BalanceErrorClient
- (void)onBalanceNotEnough
{
    [self toRecharge];
}

@end
