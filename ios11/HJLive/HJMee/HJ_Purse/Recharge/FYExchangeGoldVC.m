//
//  FYExchangeGoldVC.m
//  XBD
//
//  Created by feiyin on 2019/10/30.
//
#import "PurseCore.h"
#import "HJPurseCoreClient.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "FYExchangeGoldVC.h"
#import "BalanceInfo.h"
#import "NSString+HMLifeRegular.h"
#import "HJHttpRequestHelper+Purse.h"

#import "HJAuthCoreClient.h"
//#import "XCBillListController.h"
#import "HJPurseViewControllerFactory.h"
#import "HJAlerVerificationCodeView.h"
#import "FYRealNameAuthView.h"
#import "HJWKWebViewController.h"

@interface FYExchangeGoldVC ()<UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UILabel *goldLabel;//开心余额

@property (weak, nonatomic) IBOutlet UILabel *diamondLabel;//砖石余额

@property (weak, nonatomic) IBOutlet UITextField *exchangeTextField;

@property (weak, nonatomic) IBOutlet UILabel *changLabel;


@property (weak, nonatomic) IBOutlet UITextField *idTextField;

@property (weak, nonatomic) IBOutlet UILabel *roomIdLabel;

@property (weak, nonatomic) IBOutlet UIButton *validationCodeBtn;//验证码

@property (weak, nonatomic) IBOutlet UITextField *validationTF;
@property (weak, nonatomic) IBOutlet UIButton *queryRecordBtn;


@property (strong, nonatomic) DrawExchangeModel *balanceInfo;
@property (strong, nonatomic) BalanceInfo *balanceInfo2;
@property (assign, nonatomic) BOOL isRealName;//是否实名认证
@property (assign, nonatomic) BOOL isZhuanzengOther;//是否能兑换别人
@property (weak, nonatomic) IBOutlet UILabel *desCodeLabel;

@property (weak, nonatomic) IBOutlet UIView *upView;

@property (nonatomic, assign) NSInteger failCode;
@property (nonatomic, strong) NSString* failMessage;

@end

@implementation FYExchangeGoldVC

- (void)viewDidLoad {
    [super viewDidLoad];
     AddCoreClient(HJPurseCoreClient, self);
     AddCoreClient(HJAuthCoreClient, self);
//    _exchangeTextField.layer.cornerRadius = 12;
//    _exchangeTextField.layer.masksToBounds = YES;
//    _exchangeTextField.layer.borderColor = [UIColor colorWithHexString:@"CCCCCC"].CGColor;
//    _exchangeTextField.layer.borderWidth = 1.0;
    _exchangeTextField.delegate = self;
//    _exchangeTextField.leftView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 16, 0)];
//     _exchangeTextField.leftViewMode = UITextFieldViewModeAlways;
    
//    _idTextField.layer.cornerRadius = 12;
//       _idTextField.layer.masksToBounds = YES;
//       _idTextField.layer.borderColor = [UIColor colorWithHexString:@"CCCCCC"].CGColor;
//       _idTextField.layer.borderWidth = 1.0;
       _idTextField.delegate = self;
//       _idTextField.leftView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 16, 0)];
//    _idTextField.leftViewMode = UITextFieldViewModeAlways;
    
      self.idTextField.delegate = self;
     NSString *uid = GetCore(HJAuthCoreHelp).getUid;
//    [GetCore(PurseCore)  requestCheckWhiteListWithUid:uid.userIDValue]; //life
    self.roomIdLabel.hidden = YES;
    self.idTextField.hidden = YES;
     _validationTF.hidden = YES;
    self.validationCodeBtn.hidden = YES;
    self.desCodeLabel.hidden = YES;
     self.queryRecordBtn.hidden = YES;
     self.validationCodeBtn.layer.cornerRadius = 4;
    self.validationCodeBtn.layer.masksToBounds = YES;
    self.upView.layer.borderColor = [UIColor colorWithHexString:@"#7A9DFF"].CGColor;
    self.upView.layer.borderWidth = 1;
//    _queryRecordBtn.layer.borderColor = [UIColor colorWithHexString:@"CCCCCC"].CGColor;
//        _queryRecordBtn.layer.borderWidth = 1.0;
//      _queryRecordBtn.layer.cornerRadius = 13;
//         _queryRecordBtn.layer.masksToBounds = YES;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.balanceInfo = GetCore(PurseCore).drawExchangeModel;
       self.balanceInfo2 = GetCore(PurseCore).balanceInfo;

      _goldLabel.text = [NSString stringWithFormat:@"%.1f",[self.balanceInfo2.goldNum floatValue]];
          _diamondLabel.text = [NSString stringWithFormat:@"%.1f",[self.balanceInfo2.diamondNum floatValue]];
    
}
- (void)viewDidDisappear:(BOOL)animated {
      [super viewDidDisappear:animated];
      [GetCore(HJAuthCoreHelp) stopCountDown];
  }
//MARK: - 个人信息更新
- (void)onDrawExchangeInfoUpdate:(DrawExchangeModel *)balanceInfo
{
    self.balanceInfo = balanceInfo;
    self.diamondLabel.text = [NSString stringWithFormat:@"%.1lf", [balanceInfo.diamondNum doubleValue]];
    

}


//delegate
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
    if (textField == self.idTextField) {
           if ([string isValidateNumber]) {
                  return YES;
              }else{
                  return NO;
              }
       }
    
    
    if (textField == self.exchangeTextField) {
             if ([string isValidateNumber]) {
                 if ([string isEqualToString:@"0"] && textField.text.length<1) {
                     self.exchangeTextField.text = @"";
                     return NO;
                 }
                 
                 
                 
                }else{
                    return NO;
                }
         }
    
    
      NSString* numStr = [textField.text stringByAppendingString:string];
    
    if (numStr.length>0) {
        if ([string isEqualToString:@""]) {
           numStr = [numStr substringToIndex:numStr.length-1];
            if (![numStr integerValue]%10) {
                _changLabel.text = numStr;
                if ([numStr isEqualToString:@""]) {
                    _changLabel.text = @"0";
                }
            }else{
                _changLabel.text =[NSString stringWithFormat:@"%ld",[numStr integerValue] - [numStr integerValue]%10] ;
            }
            return YES;
        }
    }
    if (!([numStr integerValue]%10)) {
         _changLabel.text = numStr;
    }else{
        if ([numStr isEqualToString:@""]) {
            _changLabel.text = @"0";
        }else{
            _changLabel.text =[NSString stringWithFormat:@"%ld",[numStr integerValue] - [numStr integerValue]%10] ;
        }
    
    }

    return YES;
}

-(void)textFieldDidEndEditing:(UITextField *)textField{
    if (textField == self.exchangeTextField) {
        if ([_changLabel.text isEqualToString:@"0"]) {
             self.exchangeTextField.text = @"";
        }else{
             self.exchangeTextField.text = _changLabel.text;
        }
       
    }
    
}


- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    
    [textField resignFirstResponder];
    return YES;
    
}




//MARK: - 请求回调
//查询是否能兑换开心
- (void)onCheckWhiteListSuccess:(BOOL)isSuccess{
    if (isSuccess) {
        _isZhuanzengOther = YES;
         _roomIdLabel.hidden = NO;
           _idTextField.hidden = NO;
//        _validationTF.hidden = NO;
//        self.validationCodeBtn.hidden = NO;
//       self.desCodeLabel.hidden = NO;
        self.queryRecordBtn.hidden = NO;
    }else{
        _isZhuanzengOther = NO;
        _roomIdLabel.hidden = YES;
        _idTextField.hidden = YES;
         _validationTF.hidden = YES;
        self.validationCodeBtn.hidden = YES;
        self.desCodeLabel.hidden = YES;
        self.queryRecordBtn.hidden = YES;
    }
   
}
- (void)onCheckWhiteListFail:(NSString*)message{
    _roomIdLabel.hidden = YES;
       _idTextField.hidden = YES;
     _validationTF.hidden = YES;
    self.validationCodeBtn.hidden = YES;
     _isZhuanzengOther = NO;
    self.desCodeLabel.hidden = YES;
    self.queryRecordBtn.hidden = YES;
}

- (void)onChargeGoldInfoUpdateFail:(NSString*)message{
    
//     [MBProgressHUD showError:message];
}
//兑换成功回调
- (void)onChargeGoldInfoUpdate:(ChargeGoldModel*)balanceInfo{
    _changLabel.text =@"0";
    _exchangeTextField.text = @"";
    _validationTF.text = @"";
     [GetCore(HJAuthCoreHelp) stopCountDown];
     [self.validationCodeBtn setTitle:NSLocalizedString(@"验证码", nil) forState:UIControlStateNormal];
       [self.validationCodeBtn setEnabled:YES];
    _goldLabel.text = [NSString stringWithFormat:@"%ld",balanceInfo.goldNum];
    _diamondLabel.text = [NSString stringWithFormat:@"%.0f",balanceInfo.diamondNum];
     [MBProgressHUD showError:@"兑换成功"];
    if (_changeGoldBlock) {
        _changeGoldBlock(_diamondLabel.text);
    }
    
}

//MARK: - 判断实名认证
-(void)succUserRealNameStatus:(BOOL)code{
    self.failCode = 200;
     self.isRealName = YES;

}
-(void)failUserRealNameStatus:(NSNumber *)code message:(NSString*)message{
    self.failCode = [code integerValue];
    self.failMessage = message;

  
}

//弹框实名认证
-(void)showRealNameAuth{
    [FYRealNameAuthView show:^(FYRealNameAuthType type) {
               switch (type) {
                          case FYConfirmType:
                          {
                            //jump 认证页面H5
                              [self loadWebView:@"/front/real_name/index.html"];
                          }
                              break;
                          case FYRealNameCancelType:
                          {
    
    
                          }
                              break;
                       default:
                        break;
               }
    
    
           }];
}

- (void)loadWebView:(NSString *)url
{
    NSString *urlSting = [NSString stringWithFormat:@"%@%@",[HJHttpRequestHelper getHostUrl],url];

    HJWKWebViewController *webView = [[HJWKWebViewController alloc]init];
    webView.url = [NSURL URLWithString:urlSting];
    [self.navigationController pushViewController:webView animated:YES];
}
//立即兑换
- (IBAction)qucklyChangeAction:(id)sender {

     //判断是否实名认证
        
         if (self.failCode ==2501) {
                [self showRealNameAuth];

            }else if (self.failCode ==2511){
                //提示未绑定手机
                 [MBProgressHUD showMessage:self.failMessage];
        //         [self showRealNameAuth];
            }else if(self.failCode ==2508){
                [MBProgressHUD showMessage:self.failMessage];
            }else if (self.failCode == 200){
                
                if (!_isRealName) {
                            [MBProgressHUD showError:@"您还未实名认证，无法兑换给别人哦~"];
                            return;
                       }
                   
                       if ([_diamondLabel.text floatValue]<10) {
                            [MBProgressHUD showError:@"钻石余额不足，无法兑换哦~"];
                           return;
                       }
                       if (_exchangeTextField.text.length<1) {
                           [MBProgressHUD showError:@"请输入兑换金额哦~"];
                                  return;
                       }
                       if ([_exchangeTextField.text floatValue]<10) {
                           [MBProgressHUD showError:@"请输入正确的兑换金额哦~"];
                           return;
                       }
    //                   if (_idTextField.text.length<1) {
    //                        [MBProgressHUD showError:@"请输入对方ID哦~"];
    //                        return;
    //                   }
                
              
                [HJAlerVerificationCodeView show:^(HJAlerVerificationCodeType type,NSString* code) {
                    switch (type) {
                            case HJAlerVerificationCodeFirmType:
                            //兑换请求
                            [self requestChangeGoldWithCode:code];
                            
                            break;
                        case HJAlerGetVeriCodeType:
                            //获取验证码
                            [self getValidationCode];
                            break;
                            case HJAlerVerificationCodeCancelType:
                            
                            break;
                            
                        default:
                            break;
                    }
                    
                    
                } content:@"" nick:@"" isAttribute:NO];
                
              
                
                
    //            [GetCore(PurseCore) requestWithDrawCash:[GetCore(HJAuthCoreHelp).getUid userIDValue] pid:self.selectModel.cashProdId type:_accountType account:_account accountName:_accountName];

            }else{
                 [MBProgressHUD showMessage:self.failMessage];
            }
        


}
//兑换请求
-(void)requestChangeGoldWithCode:(NSString*)code{
     NSString *uid = GetCore(HJAuthCoreHelp).getUid;
    [GetCore(PurseCore) requestChangeGold:uid.userIDValue smsCode:code diamondNum:[self.changLabel.text doubleValue] roomId:self.idTextField.text];
    
}
//获取验证码
-(void)getValidationCode{
       
    
    
        [HJHttpRequestHelper getMsmWithType:0 Success:^(BOOL succeed) {
                         [GetCore(HJAuthCoreHelp) openCountdown];
    
                     } failure:^(NSNumber *code, NSString *msg) {
    
                     }];
}

//获取验证码
- (IBAction)validationCodeAction:(id)sender {
    
}

- (void)onCutdownOpen:(NSNumber *)number
{
    [self.validationCodeBtn setTitle:[NSString stringWithFormat:@"%ds", number.intValue] forState:UIControlStateNormal];
    [self.validationCodeBtn setEnabled:NO];
}

- (void)onCutdownFinish
{
    //设置按钮的样式
    [self.validationCodeBtn setTitle:NSLocalizedString(XCPurseVerifySendAgain, nil) forState:UIControlStateNormal];
    [self.validationCodeBtn setEnabled:YES];
}
//MARK: - 代充记录查询
- (IBAction)queryRecordBtnAction:(id)sender {
    
//    XCBillListController *vc = (XCBillListController *)[[XCPurseViewControllerFactory sharedFactory]instantiateBillListViewController];
//                vc.type = BillType_daiChong;
//          vc.title = @"转赠记录";
//                 [self.navigationController pushViewController:vc animated:YES];
}


//MARK: - 判断实名认证
//-(void)succUserRealNameStatus:(BOOL)code{
//    //
//    self.isRealName = YES;
//}

@end
