//
//  YPLoginPhoneView.m
//  HJLive
//
//  Created by feiyin on 2020/6/24.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPLoginPhoneView.h"

#import "YPWKWebViewController.h"
#import "UIView+getTopVC.h"

@implementation YPLoginPhoneView

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self setBottomViewStytle];
    
    [self checkQQandWx];
    
    [self.phoneTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.passwordTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    
    _phoneImgView.layer.borderColor = [UIColor colorWithHexString:@"#CFBBFF"].CGColor;
    _phoneImgView.layer.borderWidth = 1;
    _passImgView.layer.borderColor = [UIColor colorWithHexString:@"#CFBBFF"].CGColor;
    _passImgView.layer.borderWidth = 1;
}

#pragma mark private mothed
- (void)setBottomViewStytle
{
//    CGRect frame = CGRectMake(0, 0, kScreenWidth, 435);
//    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(30, 30)];
//    
//    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
//    maskLayer.frame = frame;
//    maskLayer.path = maskPath.CGPath;
//    self.layer.mask = maskLayer;
}

- (void)checkQQandWx{
    
    BOOL hasWx = [WXApi isWXAppInstalled];
    BOOL hasQQ = ([TencentOAuth iphoneQQInstalled] || [TencentOAuth iphoneTIMInstalled]);
    
    self.wechatBtn.hidden = !hasWx;
    self.qqBtn.hidden = !hasQQ;
    
    self.center_qq.constant = (hasQQ &&!hasWx)?0:30;
    self.center_wechat.constant = (!hasQQ &&hasWx)?0:-30;
    
}

#pragma mark UITextFieldDelegate

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    if (textField == self.phoneTextF) {
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
        [self.passwordTextF becomeFirstResponder];
        return YES;
    }else{
        [self loginBtnAction:nil];
        [self.passwordTextF resignFirstResponder];
        return YES;
    }
}

- (void)textFieldDidChange :(UITextField *)theTextField{
    NSUInteger subStr = 0;
    if (theTextField == self.phoneTextF) {
        subStr = 11;
    } else if (theTextField == self.passwordTextF) {
        subStr = 16;
    }
    
    if (theTextField.text.length >subStr) {
        theTextField.text = [theTextField.text substringToIndex:subStr];
    }
    
    if (self.phoneTextF.text.length >= 5 && self.passwordTextF.text.length >= 6) {
        self.loginBtn.enabled = YES;
    } else {
        self.loginBtn.enabled = NO;
    }
}

#pragma mark IBAction

- (IBAction)secureBtnAction:(id)sender {
    
    UIButton *btn = sender;
    
    if (btn.selected) {
        btn.selected = NO;
        self.passwordTextF.secureTextEntry = YES;
    }else{
        btn.selected = YES;
        self.passwordTextF.secureTextEntry = NO;
    }
    
}

- (IBAction)loginBtnAction:(id)sender {
    
    if (!self.selBtn.selected) {
        [MBProgressHUD showError:@"请同意并勾选用户协议"];
        return;
    }
    
    if (!self.loginBtn.enabled) {
        return;
    }
    
    [MBProgressHUD showMessage:@"登录中..."];
    [GetCore(YPAuthCoreHelp) login:self.phoneTextF.text password:self.passwordTextF.text];
    
}

- (IBAction)registerBtnAction:(id)sender {
    
    self.registerBlock();
    
}

- (IBAction)forgetPasswordAction:(id)sender {
    
    self.forgetBlock();
    
}

- (IBAction)wechatAction:(id)sender {
    if (!self.selBtn.selected) {
        [MBProgressHUD showError:@"请同意并勾选用户协议"];
        return;
    }
    
    [MBProgressHUD showMessage:@"正在登录中..."];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [GetCore(YPAuthCoreHelp) loginWithWc];
    });
    
}

- (IBAction)qqAction:(id)sender {
    
    if (!self.selBtn.selected) {
        [MBProgressHUD showError:@"请同意并勾选用户协议"];
        return;
    }
    
    [MBProgressHUD showMessage:@"正在登录中..."];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [GetCore(YPAuthCoreHelp) loginWithQQ];
    });
}

- (IBAction)selAction:(id)sender {
    
    self.selBtn.selected = !self.selBtn.selected;
    
}

- (IBAction)delegateAction:(id)sender {
    
    YPWKWebViewController *vc = [[YPWKWebViewController alloc]init];
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/agreement/agreement.html",[YPHttpRequestHelper getHostUrl]];
    vc.url = [NSURL URLWithString:urlSting];
    [[self topViewController].navigationController pushViewController:vc animated:YES];
    
}

- (IBAction)zhuceBtnAction:(id)sender {
    [self registerBtnAction:nil];
}




- (void)dealloc
{
    RemoveCoreClientAll(self);
}

@end
