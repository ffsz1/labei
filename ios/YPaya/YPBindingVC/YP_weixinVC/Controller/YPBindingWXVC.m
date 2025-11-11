//
//  YPBindingWXVC.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBindingWXVC.h"

#import "YPHttpRequestHelper+Binding.h"

#import "YPUserCoreHelp.h"
#import "HJDBManager.h"
#import "HJAuthCoreClient.h"

#import "YPAlterShower.h"

@interface YPBindingWXVC ()

@property (weak, nonatomic) IBOutlet UITextField *phoneTextF;
@property (weak, nonatomic) IBOutlet UITextField *codeTextF;
@property (weak, nonatomic) IBOutlet GGButton *codeBtn;

@end

@implementation YPBindingWXVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(keyboardHide:)];
    tapGestureRecognizer.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:tapGestureRecognizer];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    
    UIBarButtonItem * leftButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"yp_nav_bar_back"] style:UIBarButtonItemStylePlain target:self action:@selector(backAction)];
    leftButtonItem.tintColor = [UIColor blackColor];
    self.navigationItem.leftBarButtonItem = leftButtonItem;
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
    
    
    AddCoreClient(HJAuthCoreClient, self);
    
    
    [GetCore(YPUserCoreHelp) getUserInfo:GetCore(YPAuthCoreHelp).getUid.userIDValue refresh:NO success:^(UserInfo *info) {
        self.phoneTextF.text = info.phone;
    }];
    
}


- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void)backAction
{
    [self.navigationController popViewControllerAnimated:YES];
    
    [GetCore(YPAuthCoreHelp) stopCountDown];
}

- (void)keyboardHide:(UITapGestureRecognizer*)tap {
    [self.phoneTextF resignFirstResponder];
    [self.codeTextF resignFirstResponder];
}

- (IBAction)codeBtnAction:(id)sender {
    
    if (self.phoneTextF.text.length == 0) {
        [MBProgressHUD showError:@"请输入微信账户"];
        return;
    }
    
    [MBProgressHUD showMessage:@"获取中..."];
    
    __weak typeof(self)weakSelf = self;
    [GetCore(YPUserCoreHelp) getUserInfo:GetCore(YPAuthCoreHelp).getUid.userIDValue refresh:NO success:^(UserInfo *info) {
        
        [YPHttpRequestHelper getBindingSmsCode:^{
            [MBProgressHUD showSuccess:@"发送验证码成功"];
            
            [GetCore(YPAuthCoreHelp) openCountdown];
        } failure:^(NSNumber * _Nonnull code, NSString * _Nonnull msg) {
            [MBProgressHUD hideHUD];

        }];

        
    }];
    
}


- (void)onCutdownOpen:(NSNumber *)number
{
    [self.codeBtn setTitle:[NSString stringWithFormat:@"%ds", number.intValue] forState:UIControlStateNormal];
    [self.codeBtn setEnabled:NO];
}

- (void)onCutdownFinish
{
    [self.codeBtn setTitle:@"重新发送" forState:UIControlStateNormal];
    [self.codeBtn setEnabled:YES];
}


- (IBAction)bindingBtnAction:(id)sender {
    
    if (self.phoneTextF.text.length == 0) {
        [MBProgressHUD showError:@"请输入微信账户"];
        return;
    }
    
    if (self.codeTextF.text.length == 0) {
        [MBProgressHUD showError:@"请输入验证码"];
        return;
    }
    
    [MBProgressHUD showMessage:@"绑定中..."];
    
    __weak typeof(self)weakSelf = self;
    [GetCore(YPUserCoreHelp) getUserInfo:GetCore(YPAuthCoreHelp).getUid.userIDValue refresh:NO success:^(UserInfo *info) {
        [YPHttpRequestHelper bindingValidateCode:self.codeTextF.text success:^{
            [weakSelf bindingPost];
        } failure:^(NSNumber * _Nonnull code, NSString * _Nonnull msg) {
            [MBProgressHUD showError:msg];
        }];
    }];
}


- (void)bindingPost
{

    @weakify(self);
    
    [YPHttpRequestHelper bindThird:self.openId unionId:self.unionId accessToken:self.accessToken type:self.isQQ?2:1 success:^{
        
        UserID userId = [GetCore(YPAuthCoreHelp) getUid].userIDValue;//当前用户id
        
        [GetCore(YPUserCoreHelp) getUserInfo:userId refresh:YES success:^(UserInfo *info) {
            @strongify(self);
            [MBProgressHUD hideHUD];
            [YPAlterShower showText:@"绑定成功" sure:nil cancel:nil];
            
            [[HJDBManager defaultManager] updateUser:info];
            [self.navigationController popViewControllerAnimated:YES];
            [GetCore(YPAuthCoreHelp) stopCountDown];

            
        }];
        
    } failure:^(NSNumber * _Nonnull code, NSString * _Nonnull msg) {
        [MBProgressHUD hideHUD];
    }];
    
}

- (void)setIsQQ:(BOOL)isQQ
{
    _isQQ = isQQ;
    if (self.isQQ) {
        self.title = @"绑定QQ";
    }
}


@end
