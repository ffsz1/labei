//
//  YPBindingPhoneVC.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBindingPhoneVC.h"

#import "YPHttpRequestHelper+Purse.h"

#import "YPUserCoreHelp.h"
#import "HJDBManager.h"
#import "HJAuthCoreClient.h"

@interface YPBindingPhoneVC ()
@property (weak, nonatomic) IBOutlet UITextField *phoneTextF;
@property (weak, nonatomic) IBOutlet UITextField *codeTextF;
@property (weak, nonatomic) IBOutlet GGButton *codeBtn;

@end

@implementation YPBindingPhoneVC

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
    [self.navigationController dismissViewControllerAnimated:NO completion:^{
        [GetCore(YPAuthCoreHelp) logout];
    }];
}

- (void)keyboardHide:(UITapGestureRecognizer*)tap {
    [self.phoneTextF resignFirstResponder];
    [self.codeTextF resignFirstResponder];
}

- (IBAction)codeBtnAction:(id)sender {
    
    if (self.phoneTextF.text.length != 11) {
        [MBProgressHUD showError:@"请输入正确的手机号码"];
        return;
    }
    
    [MBProgressHUD showMessage:@"获取中..."];
    
    @weakify(self)
    [YPHttpRequestHelper requestSmsCode:self.phoneTextF.text type:@2 success:^{
        @strongify(self)
        
        [MBProgressHUD showSuccess:@"发送验证码成功"];
        
        [GetCore(YPAuthCoreHelp) openCountdown];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD hideHUD];
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
    
    if (self.phoneTextF.text.length != 11) {
        [MBProgressHUD showError:@"请输入正确的手机号码"];
        return;
    }
    
    if (self.codeTextF.text.length == 0) {
        [MBProgressHUD showError:@"请输入验证码"];
        return;
    }
    
    [MBProgressHUD showMessage:@"绑定中..."];
    
    @weakify(self);
    [YPHttpRequestHelper bindingPhoneNumber:self.phoneTextF.text verifyCode:self.codeTextF.text Success:^(BOOL success) {
        
        UserID userId = [GetCore(YPAuthCoreHelp) getUid].userIDValue;//当前用户id
        
        [GetCore(YPUserCoreHelp) getUserInfo:userId refresh:YES success:^(UserInfo *info) {
            @strongify(self);
            
            [[HJDBManager defaultManager] updateUser:info];
            [self.navigationController dismissViewControllerAnimated:YES completion:nil];
            
        }];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD hideHUD];

    }];
}

@end
