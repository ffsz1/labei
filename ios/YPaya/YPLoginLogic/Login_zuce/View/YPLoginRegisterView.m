//
//  YPLoginRegisterView.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPLoginRegisterView.h"

#import "HJAuthCoreClient.h"

@implementation YPLoginRegisterView

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self setBottomViewStytle];
    
    [self.phoneTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.codeTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.passwordTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.againPasswordTF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    _phoneImgView.layer.borderColor = [UIColor colorWithHexString:@"#CFBBFF"].CGColor;
       _phoneImgView.layer.borderWidth = 1;
       _passImgView.layer.borderColor = [UIColor colorWithHexString:@"#CFBBFF"].CGColor;
       _passImgView.layer.borderWidth = 1;
    _codeImgView.layer.borderColor = [UIColor colorWithHexString:@"#CFBBFF"].CGColor;
    _codeImgView.layer.borderWidth = 1;
    _confirmPassImgView.layer.borderColor = [UIColor colorWithHexString:@"#CFBBFF"].CGColor;
    _confirmPassImgView.layer.borderWidth = 1;
    
    AddCoreClient(HJAuthCoreClient, self);
}

#pragma mark private mothed
- (void)setBottomViewStytle
{
    CGRect frame = CGRectMake(0, 0, kScreenWidth, 435);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(30, 30)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.layer.mask = maskLayer;
}

#pragma mark UITextFieldDelegate

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    if (textField == self.phoneTextF || textField == self.codeTextF) {
        NSString *regex =@"[0-9]*";
        NSPredicate *pred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@",regex];
        if ([pred evaluateWithObject:string]) {
            return YES;
        }
        return NO;
    }
    return YES;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if (textField == self.phoneTextF) {
        [self.codeTextF becomeFirstResponder];
        return YES;
    }else if (textField == self.codeTextF){
        [self.passwordTextF becomeFirstResponder];
        return YES;
    }else{
        
        if (self.registerBtn.enabled) [self registerbtnAction:nil];
        
        [self.passwordTextF resignFirstResponder];
        [self.againPasswordTF resignFirstResponder];
        return YES;
    }
}

- (void)textFieldDidChange :(UITextField *)theTextField{
    
    //限制输入的长度
    NSUInteger subStr = 0;
    if (theTextField == self.phoneTextF) {
        subStr = 11;
    } else if (theTextField == self.passwordTextF) {
        subStr = 16;
    } else if (theTextField == self.againPasswordTF) {
        subStr = 16;
    } else if (theTextField == self.codeTextF) {
        subStr = 6;
    }
    if (theTextField.text.length >subStr) {
        theTextField.text = [theTextField.text substringToIndex:subStr];
    }
    
    //更新注册按钮状态
    if (self.phoneTextF.text.length >= 5 && self.passwordTextF.text.length >= 6 && self.codeTextF.text.length>=5) {
        
        if ([self.passwordTextF.text isEqualToString:self.againPasswordTF.text]) {
            self.registerBtn.enabled = YES;
        }else{
            self.registerBtn.enabled = NO;
        }
        
       
    } else {
        self.registerBtn.enabled = NO;
    }
}

#pragma mark IBAction

- (IBAction)codeBtnAction:(id)sender {
    
    if (self.phoneTextF.text.length != 11) {
        [MBProgressHUD showError:@"请输入正确的手机号"];
        return;
    }
    
    [MBProgressHUD showMessage:@"获取中..."];
    [GetCore(YPAuthCoreHelp) requestRegistSmsCode:self.phoneTextF.text];
    
}

- (IBAction)secuteBtnAction:(id)sender {
    
    self.secuteBtn.selected = !self.secuteBtn.selected;
    self.passwordTextF.secureTextEntry = !self.secuteBtn.selected;
    
}


- (IBAction)againSecteBtnAction:(id)sender {
    self.againSecuteBtn.selected = !self.againSecuteBtn.selected;
    self.againPasswordTF.secureTextEntry = !self.againSecuteBtn.selected;
}




- (IBAction)registerbtnAction:(id)sender {
    NSLog(@"ddddddddddddd");
    [MBProgressHUD showMessage:@"注册中..."];
    [GetCore(YPAuthCoreHelp) regist:self.phoneTextF.text password:self.passwordTextF.text smsCode:self.codeTextF.text];
}


#pragma -mark AuthCoreClient
- (void)onRegistSuccess
{
    [GetCore(YPAuthCoreHelp) login:self.phoneTextF.text password:self.passwordTextF.text];
    self.phoneTextF.text = @"";
    self.codeTextF.text = @"";
    self.passwordTextF.text = @"";
    self.againPasswordTF.text = @"";
    
}

- (void)onRegistFailth:(NSString *)erroMsg
{
    [MBProgressHUD hideHUD];
}

- (void)onLoginSuccess
{
    if (self.isShow) {
        [MBProgressHUD hideHUD];
    }
}

- (void)onLoginFailth:(NSString *)errorMsg
{
    if (self.isShow) {
        [MBProgressHUD hideHUD];
    }
}

- (void)onRequestSmsCodeSuccess:(NSNumber *)type
{
    if (self.isShow) {
        [MBProgressHUD showSuccess:@"验证码发送成功，请查收"];
    }
}

- (void)onRequestSmsCodeFailth:(NSString *)errorMsg
{
    if (self.isShow) {
        [MBProgressHUD hideHUD];
    }
}

- (void)onCutdownOpen:(NSNumber *)number
{
    if (self.isShow) {
        [self.codeBtn setTitle:[NSString stringWithFormat:@"%ds 重试", number.intValue] forState:UIControlStateDisabled];
        [self.codeBtn setEnabled:NO];
    }
    
}

- (void)onCutdownFinish
{
    if (self.isShow) {
        [self.codeBtn setTitle:@"重新获取" forState:UIControlStateNormal];
        [self.codeBtn setEnabled:YES];
    }
    
}

- (IBAction)denluBtnAction:(id)sender {
    
    if (_closeBlock) {
        _closeBlock();
    }
    
    
}


- (void)dealloc
{
    RemoveCoreClient(HJAuthCoreClient, self);
}


@end
