//
//  HJBindingPhoneVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBindingPhoneVC.h"
#import "UIColor+UIColor_Hex.h"
#import "UIImage+_1x1Color.h"
#import "HJAuthCoreHelp.h"
#import "UIView+Toast.h"
#import "HJAuthCoreClient.h"
#import "YYDefaultTheme.h"
#import "DESEncrypt.h"
#import "StateButton.h"
#import "UIViewController+Cloudox.h"
#import "UINavigationController+Cloudox.h"

#import "PurseCore.h"
#import "HJPurseCoreClient.h"
#import "HJUserCoreHelp.h"

#import "HJHttpRequestHelper+Purse.h"
#import "HJAuthCoreHelp.h"

#import "HJDBManager.h"

@interface HJBindingPhoneVC ()<HJAuthCoreClient,HJPurseCoreClient>

@property (weak, nonatomic) IBOutlet UIButton *authCodeBtn;
@property (weak, nonatomic) IBOutlet UITextField *phoneField;
@property (weak, nonatomic) IBOutlet UITextField *smsCodeField;
@property (weak, nonatomic) IBOutlet UIButton *registerBtn;
@property (strong, nonatomic) dispatch_source_t timer;
@property (assign, nonatomic) BOOL isConfirm;

- (IBAction)onSmsCodeBtnClicked:(id)sender;
- (IBAction)onRegisterBtnClicked:(id)sender;

@end

@implementation HJBindingPhoneVC

- (void)configBind {
    
    self.phoneField.userInteractionEnabled = YES;
    
    if (self.isBindPhone) {
        if (self.isConfirm) {
            self.phoneField.userInteractionEnabled = NO;
            self.title = NSLocalizedString(XCPurseBindingChangBindingPhone, nil);
            self.phoneField.placeholder = NSLocalizedString(XCPurseBindingBindedFillPhoneTip, nil);
            NSString *userId = [GetCore(HJAuthCoreHelp) getUid];
            UserInfo *info = [GetCore(HJUserCoreHelp) getUserInfoInDB:[userId intValue]];
            
            if (info.phone.length == 11) {
                self.phoneField.text = info.phone;
            }

            [self.registerBtn setTitle:NSLocalizedString(XCPurseBindingSureChange, nil) forState:UIControlStateNormal];
        } else {

            self.title = NSLocalizedString(XCPurseBindingBingdingPhone, nil);
            self.phoneField.placeholder = NSLocalizedString(XCPurseBindingFillPhoneTip, nil);
            [self.registerBtn setTitle:NSLocalizedString(XCPurseBindingSureBind, nil) forState:UIControlStateNormal];
        }
        
    } else {
        self.title =NSLocalizedString(XCPurseBindingBingdingPhone, nil);
        self.phoneField.placeholder = NSLocalizedString(XCPurseBindingFillPhoneTip, nil);
        [self.registerBtn setTitle:NSLocalizedString(XCPurseBindingBindingPhoneNow, nil) forState:UIControlStateNormal];
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.isConfirm = YES;
    [self configBind];

    AddCoreClient(HJAuthCoreClient, self);
    AddCoreClient(HJPurseCoreClient, self);
    // Do any additional setup after loading the view.
    [self.phoneField addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.smsCodeField addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    
    [self.registerBtn setEnabled:NO];
    self.registerBtn.alpha = 0.3;
    self.registerBtn.layer.masksToBounds = YES;
    self.registerBtn.layer.cornerRadius = 22;
    
    
    self.authCodeBtn.layer.masksToBounds = YES;
    self.authCodeBtn.layer.cornerRadius = 10;
    
    UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(keyboardHide:)];
    //设置成NO表示当前控件响应后会传播到其他控件上，默认为YES。
    tapGestureRecognizer.cancelsTouchesInView = NO;
    //将触摸事件添加到当前view
    [self.view addGestureRecognizer:tapGestureRecognizer];

    self.view.backgroundColor = [UIColor whiteColor];
//    UIView *bgView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
//    bgView.backgroundColor = [[YYDefaultTheme defaultTheme] colorWithHexString:@"#F5F5F5" alpha:1.0];
//    [self.view insertSubview:bgView atIndex:0];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [GetCore(HJAuthCoreHelp)stopCountDown];
}

-(void)textFieldDidChange :(UITextField *)theTextField{
    NSUInteger subStr = 0;
    if (theTextField == self.phoneField) {
        subStr = 11;
    } else if (theTextField == self.smsCodeField) {
        subStr = 5;
    }
    
    if (theTextField.text.length >subStr) {
        theTextField.text = [theTextField.text substringToIndex:subStr];
    }
    
    if (self.phoneField.text.length == 11 && self.smsCodeField.text.length == 5) {
        [self.registerBtn setEnabled:YES];
        self.registerBtn.alpha = 1;
    } else {
        [self.registerBtn setEnabled:NO];
        self.registerBtn.alpha = 0.3;

    }
}

- (void)dealloc
{
    RemoveCoreClientAll(self);
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
//    self.navBarBgAlpha = @"1.0";
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

- (IBAction)onSmsCodeBtnClicked:(id)sender {
    if (self.phoneField.text.length != 11) {
        [self.view makeToast:NSLocalizedString(XCPurseBindingBindingPhoneCorectTip, nil) duration:3 position:CSToastPositionCenter];
        return;
    }
    if (self.isBindPhone) {
//        [GetCore(AuthCore) requestSmsCode:self.phoneField.text type:@3];
        
        [HJHttpRequestHelper getMsmWithType:0 Success:^(BOOL succeed) {
            
            [GetCore(HJAuthCoreHelp) openCountdown];
            
        } failure:^(NSNumber *code, NSString *msg) {
            
        }];
        
    } else {
        [GetCore(HJAuthCoreHelp) requestSmsCode:self.phoneField.text type:@2];
    }
}

- (IBAction)onRegisterBtnClicked:(id)sender {
    NSString *phone = self.phoneField.text;
    NSString *smsCode = self.smsCodeField.text;
    
    if (phone.length > 0 || smsCode.length > 0) {
        [self.phoneField resignFirstResponder];
        [self.smsCodeField resignFirstResponder];
        
        if (self.isBindPhone) {
            if (self.isConfirm) {
                [GetCore(HJAuthCoreHelp) requestConfirmCode:self.phoneField.text smsCode:self.smsCodeField.text success:nil failure:nil];
            } else {
                [GetCore(HJAuthCoreHelp) requestReplacePhone:phone smsCode:smsCode];
            }
        } else {
            [GetCore(PurseCore) bindingPhoneNum:phone verifyCode:smsCode];
        }
        
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
    }
}

- (void)replaceFail:(NSString *)msg {
//    [MBProgressHUD showSuccess:msg];
}

- (void)replaceSuccess {
    UserID userId = [GetCore(HJAuthCoreHelp) getUid].userIDValue;//当前用户id
    @weakify(self);
    [GetCore(HJUserCoreHelp) getUserInfo:userId refresh:YES success:^(UserInfo *info) {
        @strongify(self);
        self.isBindPhone = YES;
        [[HJDBManager defaultManager] updateUser:info];
        
        [self.navigationController popViewControllerAnimated:YES];
    }];
}

- (void)confirmFail:(NSString *)msg {
//    [MBProgressHUD showSuccess:msg];
}

- (void)confirmSuccess {
    self.phoneField.text = @"";
    self.isConfirm = false;
    self.smsCodeField.text = @"";
    self.isBindPhone = false;
    [GetCore(HJAuthCoreHelp) stopCountDown];
    [self onCutdownFinish];
    [self configBind];
    [MBProgressHUD hideHUD];
}

- (void)keyboardHide:(UITapGestureRecognizer*)tap {
    [self.phoneField resignFirstResponder];
    [self.smsCodeField resignFirstResponder];
}

#pragma mark - HJPurseCoreClient

- (void)getSmsSuccess {
    [GetCore(HJAuthCoreHelp) openCountdown];
    [MBProgressHUD showSuccess:NSLocalizedString(XCPurseBindingSendCodeSuccess, nil)];
}

- (void)getSmsFaildWithMessage:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}

- (void)bindingPhoneNumberSuccess {
    [MBProgressHUD showSuccess:NSLocalizedString(XCPurseBindingBidingSucceess, nil)];

    
    UserID userId = [GetCore(HJAuthCoreHelp) getUid].userIDValue;//当前用户id

    @weakify(self);
    [GetCore(HJUserCoreHelp) getUserInfo:userId refresh:YES success:^(UserInfo *info) {
        @strongify(self);
        
        
        
        
        [[HJDBManager defaultManager] updateUser:info];
        
//        [self.navigationController popViewControllerAnimated:YES];
        
        if (self.isPush) {
            
            [self.navigationController popViewControllerAnimated:YES];
        }else {
            [self.navigationController dismissViewControllerAnimated:YES completion:nil];
        }
        
    }];
    
    
}

- (void)bindingPhoneNumberFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}

#pragma -mark AuthCoreClient
- (void)onResetPwdSuccess
{
    [MBProgressHUD hideHUD];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)onResetPwdFailth:(NSString *)errorMsg
{
    [MBProgressHUD hideHUD];
}

- (void)onRequestSmsCodeSuccess:(NSNumber *)type {
    [MBProgressHUD showSuccess:NSLocalizedString(XCPurseBindingSendCodeSuccess, nil)];
}

- (void)onRequestSmsCodeFailth:(NSString *)errorMsg
{
//    [self.view makeToast:errorMsg duration:3 position:CSToastPositionCenter];
}

- (void)onCutdownOpen:(NSNumber *)number
{
    [self.authCodeBtn setTitle:[NSString stringWithFormat:@"%ds", number.intValue] forState:UIControlStateNormal];
    [self.authCodeBtn setEnabled:NO];
}

- (void)onCutdownFinish
{
    //设置按钮的样式
    [self.authCodeBtn setTitle:NSLocalizedString(XCPurseVerifySendAgain, nil) forState:UIControlStateNormal];
    [self.authCodeBtn setEnabled:YES];
}



@end
