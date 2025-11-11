//
//  YPSettingViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "YPBlackListViewController.h"
#import "YPSettingViewController.h"
#import "YPWKWebViewController.h"
#import "YYUtility.h"
#import "UIColor+UIColor_Hex.h"
//core
#import "YPAuthCoreHelp.h"
#import "YPPurseCore.h"
#import "YPVersionCoreHelp.h"

#import "YPBindingPhoneVC.h"

#import "YPPhoneViewController.h"

@interface YPSettingViewController ()<UIGestureRecognizerDelegate>
@property (weak, nonatomic) IBOutlet UIView *footerView;
@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (weak, nonatomic) IBOutlet UIButton *logoutBtn;
@property (weak, nonatomic) IBOutlet UILabel *versionLabel;
@property (assign, nonatomic) BOOL isBindingPhone;

@end

@implementation YPSettingViewController

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    YPBlackStatusBar
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = NSLocalizedString(XCMeSettingTitle, nil);
    self.versionLabel.text = [NSString stringWithFormat:@"V %@",[YYUtility appVersion]];
    self.tableView.backgroundColor = RGBACOLOR(245, 245, 245, 1);
    [self initTableView];
    
    [GetCore(YPAuthCoreHelp)isBindingPhoneSuccess:^(BOOL isbinding) {
        self.isBindingPhone = isbinding;
        [self.tableView reloadData];
    }];

}
- (UIStatusBarStyle)preferredStatusBarStyle
{
//    return UIStatusBarStyleLightContent; //返回白色
    return UIStatusBarStyleDefault;    //返回黑色
}
- (void)initTableView {
    self.tableView.tableHeaderView = _headerView;
    self.tableView.tableFooterView = self.footerView;
    self.tableView.separatorColor = [UIColor colorWithHexString:@"#e5e5e5"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)logoutBtnClick:(UIButton *)sender {
    
    [GetCore(YPAuthCoreHelp) logout];
    [self.navigationController popToRootViewControllerAnimated:NO];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 9;
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.row == 0) {
        
        [self pushToBindingPhone];
        
    } else if (indexPath.row == 6) {
//        YPWKWebViewController *vc = [[YPWKWebViewController alloc]init];
//        NSString *urlSting = [NSString stringWithFormat:@"%@/front/agreement/agreement.html",[YPHttpRequestHelper getHostUrl]];
//        vc.url = [NSURL URLWithString:urlSting];
//        [self.navigationController pushViewController:vc animated:YES];
    } else if (indexPath.row == 3) {
        NSString *urlSting = [NSString stringWithFormat:@"%@/front/client/index.html",[YPHttpRequestHelper getHostUrl]];
        [self loadH5URL:urlSting];
    } else if (indexPath.row == 4) {
          YPBlackListViewController* vc = YPMessageBoard(@"YPBlackListViewController");
                 [self.navigationController pushViewController:vc animated:YES];
    } else if (indexPath.row == 1) {
        
        [self pushToSetPasswordVC];
        
    }else if(indexPath.row == 5){
        //系统权限
        NSURL *url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
        if([[UIApplication sharedApplication] canOpenURL:url]){
            [[UIApplication sharedApplication] openURL:url options:@{} completionHandler:^(BOOL success) {
                
            }];
        }
    }else if (indexPath.row == 8){
        //注销账号
        UIAlertController *alter = [UIAlertController alertControllerWithTitle:@"注销账号请联系微信客服" message:@"haijiaoxingqiu6" preferredStyle:UIAlertControllerStyleAlert];
            
            [alter addAction:[UIAlertAction actionWithTitle:@"复制微信号" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                UIPasteboard *pastboard = [UIPasteboard generalPasteboard];
                [pastboard setString:@"haijiaoxingqiu6"];
                [MBProgressHUD showSuccess:@"复制成功"];
                
            }]];
        [self presentViewController:alter animated:YES completion:nil];
    }

}

- (void)pushToBindingPhone
{
    [MBProgressHUD showMessage:@"请稍后..."];
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper judgeIsBindingPhoneWithsuccess:^(BOOL isbinding) {
        
        [MBProgressHUD hideHUD];
        YPBindingPhoneVC *vc = (YPBindingPhoneVC *)YPWalletStoryBoard(@"YPBindingPhoneVC");
        vc.isPush = YES;
        vc.isBindPhone = isbinding;
        [weakSelf.navigationController pushViewController:vc animated:YES];
        
        
    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD hideHUD];
    }];
}

//设置登录密码
- (void)pushToSetPasswordVC
{
    [MBProgressHUD showMessage:@"请稍候..."];
    
    __weak typeof(self)weakSelf = self;
    
    [self checkIsBind];
    
}

//判断是否绑定手机
- (void)checkIsBind
{
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper judgeIsBindingPhoneWithsuccess:^(BOOL isbinding) {
        
        if (isbinding) {
            [weakSelf checkSetPassword];
        }else{
            [MBProgressHUD hideHUD];
            YPBindingPhoneVC *vc = (YPBindingPhoneVC *)YPWalletStoryBoard(@"YPBindingPhoneVC");
            vc.isPush = YES;
            vc.isBindPhone = NO;
            [weakSelf.navigationController pushViewController:vc animated:YES];
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD hideHUD];
    }];
}

- (void)checkSetPassword
{
    __weak typeof(self)weakSelf = self;
    //判断是否设置过密码
    [YPHttpRequestHelper checkPwd:^(BOOL isSet) {
        
        if (!isSet) {
            [MBProgressHUD hideHUD];
            YPPhoneViewController *vc = YPSetPasswordtoryBoard(@"YPPhoneViewController");
            vc.isBindingPhone = YES;
            [weakSelf.navigationController pushViewController:vc animated:YES];
        }else{
            [MBProgressHUD hideHUD];
            UIViewController *vc = YPSetPasswordtoryBoard(@"YPResetPasswordViewController");
            [weakSelf.navigationController pushViewController:vc animated:YES];
        }
        
    } failure:^(NSNumber *code, NSString *msg) {
        [MBProgressHUD hideHUD];
    }];
}


- (void)loadH5URL:(NSString *)url{

    YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
    webView.url = [NSURL URLWithString:url];
    [self.navigationController pushViewController:webView animated:YES];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.row == 6) {
#ifdef DEBUG
        return 55;
#else
        return 0;
#endif
    }else {
        return 55;
    }
}

#pragma mark - UIGestureRecognizerDelegate

- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer {
    //   BOOL ok = YES; // 默认为支持右滑反回
    return YES;
}

@end
