//
//  YPPhoneViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPhoneViewController.h"
#import "YPSetPasswordViewController.h"

#import "NSString+HMLifeRegular.h"
#import "YPHttpRequestHelper+Auth.h"

#import "YPHttpRequestHelper+Purse.h"
#import "YPUserCoreHelp.h"

@interface YPPhoneViewController ()
@property (weak, nonatomic) IBOutlet UITextField *phoneTextField;
@property (weak, nonatomic) IBOutlet UITextField *codeTextField;
@property (weak, nonatomic) IBOutlet GGButton *codeBtn;

@property (strong,nonatomic) NSTimer *timer;
@property (assign,nonatomic) int count;

@end

@implementation YPPhoneViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
}

//验证码
- (IBAction)codeBtnAciton:(id)sender {
    
    if ([self checkPhone]) {
        
        
        [MBProgressHUD showMessage:@"获取中..."];
        __weak typeof(self)weakSelf = self;
        
        
//        [HttpRequestHelper requestSmsCode:phone type:type success:^{
//            NotifyCoreClient(AuthCoreClient, @selector(onRequestSmsCodeSuccess:), onRequestSmsCodeSuccess:type);
//            @strongify(self)
//            [self openCountdown];
//        } failure:^(NSNumber *resCode, NSString *message) {
//            NotifyCoreClient(AuthCoreClient, @selector(onRequestSmsCodeFailth:), onRequestSmsCodeFailth:message);
//        }];
        
        [YPHttpRequestHelper requestSmsCode:self.phoneTextField.text type:self.isBindingPhone?@3:@2 success:^{
            
            [weakSelf setTimerBegin];
            
            [MBProgressHUD hideHUD];
            
        } failure:^(NSNumber *resCode, NSString *message) {
            
            [MBProgressHUD hideHUD];
        }];
        
        
    }else{
        [MBProgressHUD showError:@"请输入正确的手机号"];
    }
    
}

- (void)setTimerBegin
{
    self.count = 60;
    self.codeBtn.enabled = NO;
    self.timer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(timeDown) userInfo: nil repeats:YES];

}

- (void)timeDown
{

    if (self.count != 1) {
        self.count -= 1;
        [self.codeBtn setEnabled:NO];
        [self.codeBtn setTitle:[NSString stringWithFormat:@"%ds",self.count] forState:UIControlStateNormal];
    }else{
        [self.timer invalidate];
        [self.codeBtn setTitle:@"验证码" forState:UIControlStateNormal];
        [self.codeBtn setEnabled:YES];
    }
}


//立即绑定
- (IBAction)bindingBtnAction:(id)sender {
    
    if (![self checkPhone]) {
        [MBProgressHUD showError:@"请输入正确的手机号"];
        return;
    }
    
    if (_codeTextField.text.length == 0) {
        [MBProgressHUD showError:@"请输入验证码"];;
        return;
    }
    
    if (self.isBindingPhone) {
        
        [MBProgressHUD showError:@"验证中..."];
        __weak typeof(self)weakSelf = self;
        [YPHttpRequestHelper requestConfirmCode:self.phoneTextField.text smsCode:self.codeTextField.text success:^{
            
            YPSetPasswordViewController *vc = YPSetPasswordtoryBoard(@"YPSetPasswordViewController");
            vc.phoneStr = weakSelf.phoneTextField.text;
            vc.codeStr = weakSelf.codeTextField.text;
            [weakSelf.navigationController pushViewController:vc animated:YES];
            
            [MBProgressHUD hideHUD];
            
        } failure:^(NSNumber *code, NSString *msg) {
            [MBProgressHUD hideHUD];
        }];
        
    }else{
        [MBProgressHUD showError:@"绑定中..."];
        __weak typeof(self)weakSelf = self;
        [YPHttpRequestHelper bindingPhoneNumber:self.phoneTextField.text verifyCode:self.codeTextField.text Success:^(BOOL result) {
            
            YPSetPasswordViewController *vc = YPSetPasswordtoryBoard(@"YPSetPasswordViewController");
            vc.phoneStr = weakSelf.phoneTextField.text;
            vc.codeStr = weakSelf.codeTextField.text;
            [weakSelf.navigationController pushViewController:vc animated:YES];
            
            [MBProgressHUD hideHUD];
            
        } failure:^(NSNumber *code, NSString *msg) {
            [MBProgressHUD hideHUD];

        }];
    }
    
    
    
//    [MBProgressHUD showError:@"检验中..."];
//    __weak typeof(self)weakSelf = self;
//    [HttpRequestHelper validateCode:self.phoneTextField.text smsCode:self.codeTextField.text success:^{
//
//        YPSetPasswordViewController *vc = YPSetPasswordtoryBoard(@"YPSetPasswordViewController");
//        vc.phoneStr = weakSelf.phoneTextField.text;
//        vc.codeStr = weakSelf.codeTextField.text;
//        [weakSelf.navigationController pushViewController:vc animated:YES];
//
//        [MBProgressHUD hideHUD];
//    } failure:^(NSNumber *code, NSString *msg) {
//        [MBProgressHUD hideHUD];
//    }];
    
}

- (BOOL)checkPhone
{
   return [self.phoneTextField.text isPhoneNumber];
}


- (void)setIsBindingPhone:(BOOL)isBindingPhone
{
    _isBindingPhone = isBindingPhone;
    if (_isBindingPhone) {
        self.title = @"验证手机号";
    }else{
        self.title = @"绑定手机号";
        
        
        NSString *userId = [GetCore(YPAuthCoreHelp) getUid];
        UserInfo *info = [GetCore(YPUserCoreHelp) getUserInfoInDB:[userId intValue]];
        if (info.phone.length == 11) {
            self.phoneTextField.text = info.phone;
        }
        
    }
}

@end
