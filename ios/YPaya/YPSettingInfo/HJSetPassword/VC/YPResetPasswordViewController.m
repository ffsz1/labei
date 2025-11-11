//
//  YPResetPasswordViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPResetPasswordViewController.h"

#import "YPHttpRequestHelper+Auth.h"
#import "YPUserCoreHelp.h"
#import "UserInfo.h"

@interface YPResetPasswordViewController ()
@property (weak, nonatomic) IBOutlet UITextField *nowPasswordTextField;
@property (weak, nonatomic) IBOutlet UITextField *changePswTextField;
@property (weak, nonatomic) IBOutlet UITextField *surePswTextField;
@property (weak, nonatomic) IBOutlet UIButton *nowSecurityBtn;
@property (weak, nonatomic) IBOutlet UIButton *changeSecurityBtn;
@property (weak, nonatomic) IBOutlet UIButton *sureSecurityBtn;

@end

@implementation YPResetPasswordViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
}


- (IBAction)nowBtnAction:(id)sender {
    
    self.nowSecurityBtn.selected = !self.nowSecurityBtn.selected;
    
    self.nowPasswordTextField.secureTextEntry = !self.nowSecurityBtn.selected;
}
- (IBAction)changeBtnAction:(id)sender {
    
    self.changeSecurityBtn.selected = !self.changeSecurityBtn.selected;
    
    self.changePswTextField.secureTextEntry = !self.changeSecurityBtn.selected;
}
- (IBAction)sureBtnAction:(id)sender {
    
    self.sureSecurityBtn.selected = !self.sureSecurityBtn.selected;
    
    self.surePswTextField.secureTextEntry = !self.sureSecurityBtn.selected;
}

- (IBAction)commitBtnAction:(id)sender {
    
    if (self.nowPasswordTextField.text.length == 0) {
        [MBProgressHUD showError:@"请输入当前密码"];
        return;
    }
    
    if (self.changePswTextField.text.length == 0) {
        [MBProgressHUD showError:@"请次输入新的登陆密码"];
        return;
    }
    
    if (self.changePswTextField.text.length < 6 || self.changePswTextField.text.length > 16) {
        [MBProgressHUD showError:@"请输入6-16位新的登陆密码"];
        return;
    }
    
    if (self.surePswTextField.text.length == 0) {
        [MBProgressHUD showError:@"再次输入新的登陆密码"];
        return;
    }
    
    if (![self.changePswTextField.text isEqualToString:self.surePswTextField.text]) {
        [MBProgressHUD showError:@"两次输入新的登陆密码不一致"];
        return;
    }
    
    if ([self.nowPasswordTextField.text isEqualToString:self.surePswTextField.text]) {
        [MBProgressHUD showError:@"新密码与当前密码一致，请重新输入"];
        return;
    }
    
    [MBProgressHUD showMessage:@"设置中..."];
    __weak typeof(self)weakSelf = self;
    
    [YPHttpRequestHelper requestResetPwd:self.changePswTextField.text oldPwd:self.nowPasswordTextField.text confirmPwd:self.surePswTextField.text success:^{
        
        [MBProgressHUD showSuccess:@"设置成功"];
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [weakSelf.navigationController popViewControllerAnimated:YES];
        });
        
    } failure:^(NSNumber *code, NSString *msg) {
        
        [MBProgressHUD hideHUD];
        
    }];
    
    
    
}
- (IBAction)forgetBtnAction:(id)sender {
    
    UIViewController *vc = GGAuthBoard(@"XBDResetPwdVC");
    [self.navigationController pushViewController:vc animated:YES];
}




@end
