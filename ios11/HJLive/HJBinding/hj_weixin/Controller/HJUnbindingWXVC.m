//
//  HJUnbindingWXVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJUnbindingWXVC.h"

#import "HJHttpRequestHelper+Binding.h"

#import "HJUserCoreHelp.h"
#import "HJDBManager.h"
#import "HJAuthCoreClient.h"

#import "HJAlterShower.h"


@interface HJUnbindingWXVC ()

@property (weak, nonatomic) IBOutlet UITextField *phoneTextF;
@property (weak, nonatomic) IBOutlet UITextField *codeTextF;
@property (weak, nonatomic) IBOutlet GGButton *codeBtn;

@end

@implementation HJUnbindingWXVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(keyboardHide:)];
    tapGestureRecognizer.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:tapGestureRecognizer];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    
    UIBarButtonItem * leftButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"hj_nav_bar_back"] style:UIBarButtonItemStylePlain target:self action:@selector(backAction)];
    leftButtonItem.tintColor = [UIColor blackColor];
    self.navigationItem.leftBarButtonItem = leftButtonItem;
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
    
    
    AddCoreClient(HJAuthCoreClient, self);
    
    
    [GetCore(HJUserCoreHelp) getUserInfo:GetCore(HJAuthCoreHelp).getUid.userIDValue refresh:NO success:^(UserInfo *info) {
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
    [GetCore(HJAuthCoreHelp) stopCountDown];

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
    [GetCore(HJUserCoreHelp) getUserInfo:GetCore(HJAuthCoreHelp).getUid.userIDValue refresh:NO success:^(UserInfo *info) {
        
        [HJHttpRequestHelper getBindingSmsCode:^{
            [MBProgressHUD showSuccess:@"发送验证码成功"];
            
            [GetCore(HJAuthCoreHelp) openCountdown];
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
    
    [MBProgressHUD showMessage:@"解绑中..."];
    
    __weak typeof(self)weakSelf = self;
    [GetCore(HJUserCoreHelp) getUserInfo:GetCore(HJAuthCoreHelp).getUid.userIDValue refresh:NO success:^(UserInfo *info) {
        [HJHttpRequestHelper bindingValidateCode:self.codeTextF.text success:^{
            [weakSelf unbindingPost];
        } failure:^(NSNumber * _Nonnull code, NSString * _Nonnull msg) {
            [MBProgressHUD showError:msg];
        }];
    }];
}


- (void)unbindingPost
{
    
    @weakify(self);
    [HJHttpRequestHelper untiedThird:self.isQQ?2:1 success:^{
        UserID userId = [GetCore(HJAuthCoreHelp) getUid].userIDValue;//当前用户id
        
        [GetCore(HJUserCoreHelp) getUserInfo:userId refresh:YES success:^(UserInfo *info) {
            @strongify(self);
            
            [MBProgressHUD hideHUD];
            [HJAlterShower showText:@"解绑成功" sure:nil cancel:nil];
            
            [[HJDBManager defaultManager] updateUser:info];
            [self.navigationController popViewControllerAnimated:YES];
            [GetCore(HJAuthCoreHelp) stopCountDown];

            
        }];
    } failure:^(NSNumber * _Nonnull code, NSString * _Nonnull msg) {
        [MBProgressHUD hideHUD];
    }];
    
}

- (void)setIsQQ:(BOOL)isQQ
{
    _isQQ = isQQ;
    if (self.isQQ) {
        self.title = @"解绑QQ";
    }
}

@end
