//
//  YPSetPasswordViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPSetPasswordViewController.h"

#import "YPHttpRequestHelper+Auth.h"
#import "YPSettingViewController.h"

@interface YPSetPasswordViewController ()

@end

@implementation YPSetPasswordViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
}

- (IBAction)pswSecurityBtnAction:(id)sender {
    
    self.pswSecurityBtn.selected = !self.pswSecurityBtn.selected;
    
    self.pswTextField.secureTextEntry = !self.pswSecurityBtn.selected;
}

- (IBAction)sureSecurityBtnAction:(id)sender {
    
    self.sureSecurityBtn.selected = !self.sureSecurityBtn.selected;
    
    self.sureTextField.secureTextEntry = !self.sureSecurityBtn.selected;
}

- (IBAction)commitBtnAction:(id)sender {
    
    if (self.pswTextField.text.length == 0) {
        [MBProgressHUD showError:@"请输入密码"];
        return;
    }
    
    if (self.pswTextField.text.length < 6 || self.pswTextField.text.length > 16) {
        [MBProgressHUD showError:@"请输入6-16位密码"];
        return;
    }
    
    if (self.sureTextField.text.length == 0) {
        [MBProgressHUD showError:@"请输入确认密码"];
        return;
    }
    
    if (![self.pswTextField.text isEqualToString:self.sureTextField.text]) {
        [MBProgressHUD showError:@"两次输入的密码不一致"];
        return;
    }
    
    [MBProgressHUD showMessage:@"设置中..."];
    __weak typeof(self)weakSelf = self;
    
    
    [YPHttpRequestHelper setPwd:self.phoneStr newPwd:self.pswTextField.text success:^{
        
        [MBProgressHUD showSuccess:@"设置成功"];
        
        for (UIViewController *vc in weakSelf.navigationController.viewControllers) {
            if ([vc isKindOfClass:[YPSettingViewController class]]) {
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    [weakSelf.navigationController popToViewController:vc animated:YES];
                });
            }
        }
        
    } failure:^(NSNumber *code, NSString *message) {
        [MBProgressHUD hideHUD];

    }];
    
//    [HttpRequestHelper requestResetPwd:self.phoneStr newPwd:self.pswTextField.text smsCode:self.codeStr success:^{
//
//        [MBProgressHUD showSuccess:@"设置成功"];
//
//
//
//        for (UIViewController *vc in weakSelf.navigationController.viewControllers) {
//            if ([vc isKindOfClass:[XCSettingViewController class]]) {
//                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//                    [weakSelf.navigationController popToViewController:vc animated:YES];
//                });
//            }
//        }
//
//    } failure:^(NSNumber *resCode, NSString *message) {
//        [MBProgressHUD hideHUD];
//    }];
    
    
    
}


@end
