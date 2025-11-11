//
//  YPLoginBindingPhoneView.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPLoginBindingPhoneView.h"

#import "HJAuthCoreClient.h"

#import "HJDBManager.h"
#import "YPHttpRequestHelper+Purse.h"
#import "UIView+getTopVC.h"

@implementation YPLoginBindingPhoneView


- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self setBottomViewStytle];
    
    [self.phoneTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.codeTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    
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
    }else{
        if (self.nextBtn.enabled) [self nextBtnAction:nil];
        
        [self.codeTextF resignFirstResponder];
        return YES;
    }
}

- (void)textFieldDidChange :(UITextField *)theTextField{
    
    //限制输入的长度
    NSUInteger subStr = 0;
    if (theTextField == self.phoneTextF) {
        subStr = 11;
    }else if (theTextField == self.codeTextF) {
        subStr = 6;
    }
    if (theTextField.text.length >subStr) {
        theTextField.text = [theTextField.text substringToIndex:subStr];
    }
    
    //更新注册按钮状态
    if (self.phoneTextF.text.length >= 5 && self.codeTextF.text.length>=5) {
        self.nextBtn.enabled = YES;
    } else {
        self.nextBtn.enabled = NO;
    }
}

#pragma mark IBAction

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

- (IBAction)nextBtnAction:(id)sender {
    
    [MBProgressHUD showMessage:@"绑定中..."];
    
    @weakify(self);
    [YPHttpRequestHelper bindingPhoneNumber:self.phoneTextF.text verifyCode:self.codeTextF.text Success:^(BOOL success) {
        
        UserID userId = [GetCore(YPAuthCoreHelp) getUid].userIDValue;//当前用户id
        
        [GetCore(YPUserCoreHelp) getUserInfo:userId refresh:YES success:^(UserInfo *info) {
            @strongify(self);
            
            [[HJDBManager defaultManager] updateUser:info];
            [MBProgressHUD showSuccess:@"绑定成功"];
            
            self.bindingBlock();
            
        }];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD hideHUD];
    }];
    
}

#pragma -mark AuthCoreClient

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

- (void)dealloc
{
    RemoveCoreClient(HJAuthCoreClient, self);
}

@end
