//
//  YPLoginResetView.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPLoginResetView.h"

#import "HJAuthCoreClient.h"

@implementation YPLoginResetView

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self setBottomViewStytle];
    
    [self.phoneTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.codeTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.passwordTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.passwordTextF2 addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];

    
    AddCoreClient(HJAuthCoreClient, self);

    _phoneImgView.layer.borderColor = [UIColor colorWithHexString:@"#CFBBFF"].CGColor;
    _phoneImgView.layer.borderWidth = 1;
    _yanzhengImgView.layer.borderColor = [UIColor colorWithHexString:@"#CFBBFF"].CGColor;
    _yanzhengImgView.layer.borderWidth = 1;
    _rePassImgView.layer.borderColor = [UIColor colorWithHexString:@"#CFBBFF"].CGColor;
    _rePassImgView.layer.borderWidth = 1;
    _passImgView.layer.borderColor = [UIColor colorWithHexString:@"#CFBBFF"].CGColor;
    _passImgView.layer.borderWidth = 1;
}

- (void)dealloc
{
    RemoveCoreClientAll(self);
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
    }else if (textField == self.passwordTextF){
        [self.passwordTextF2 becomeFirstResponder];
        return YES;
    }else{
        
        if (self.changeBtn.enabled) [self changeBtnAction:nil];
        
        [self.passwordTextF2 resignFirstResponder];
        return YES;
    }
}

- (void)textFieldDidChange :(UITextField *)theTextField{
    
    //限制输入的长度
    NSUInteger subStr = 0;
    if (theTextField == self.phoneTextF) {
        subStr = 11;
    } else if (theTextField == self.passwordTextF||theTextField == self.passwordTextF2) {
        subStr = 16;
    } else if (theTextField == self.codeTextF) {
        subStr = 6;
    }
    if (theTextField.text.length >subStr) {
        theTextField.text = [theTextField.text substringToIndex:subStr];
    }
    
    //更新注册按钮状态
    if (self.phoneTextF.text.length >= 5 && self.passwordTextF.text.length >= 6 && self.passwordTextF2.text.length >= 6 && self.codeTextF.text.length>=5) {
        self.changeBtn.enabled = YES;
    } else {
        self.changeBtn.enabled = NO;
    }
}

#pragma mark IBAction

- (IBAction)codeBtnAction:(id)sender {
    
    if (self.phoneTextF.text.length != 11) {
        [MBProgressHUD showError:@"请输入正确的手机号"];
        return;
    }
    
    [MBProgressHUD showMessage:@"获取中..."];
    [GetCore(YPAuthCoreHelp) requestResetSmsCode:self.phoneTextF.text];

}

- (IBAction)secutrBtnAction1:(id)sender {
    
    UIButton *btn = sender;
    btn.selected = !btn.selected;
    self.passwordTextF.secureTextEntry = !btn.selected;
}

- (IBAction)secuteBtnAction2:(id)sender {
    UIButton *btn = sender;
    btn.selected = !btn.selected;
    self.passwordTextF2.secureTextEntry = !btn.selected;
}

- (IBAction)changeBtnAction:(id)sender {
    
    if (![self.passwordTextF.text isEqualToString:self.passwordTextF2.text]) {
        [MBProgressHUD showError:@"两次输入的密码不一致，请重新输入"];
        return;
    }
    
    [MBProgressHUD showMessage:@"重置中..."];
    [GetCore(YPAuthCoreHelp) requestResetPwd:self.phoneTextF.text newPwd:self.passwordTextF.text smsCode:self.codeTextF.text];

}

#pragma -mark AuthCoreClient
- (void)onResetPwdSuccess
{
    [MBProgressHUD showSuccess:@"重置成功，请重新登陆"];
    
    if (self.closeBlock) self.closeBlock();
}

- (void)onResetPwdFailth:(NSString *)errorMsg
{
    //    [MBProgressHUD showError:errorMsg];
    [MBProgressHUD hideHUD];
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
        [self.codebtn setTitle:[NSString stringWithFormat:@"%ds 重试", number.intValue] forState:UIControlStateDisabled];
        [self.codebtn setEnabled:NO];
    }
}

- (void)onCutdownFinish
{
    if (self.isShow) {
        [self.codebtn setTitle:@"重新获取" forState:UIControlStateNormal];
        [self.codebtn setEnabled:YES];
    }
}

@end
