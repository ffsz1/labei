//
//  YPZhiFuBaoAndBankInfoVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "NSString+HMLifeRegular.h"
#import "YPZhiFuBaoAndBankInfoVC.h"
#import "YPHttpRequestHelper+Purse.h"
#import "YPAuthCoreHelp.h"

@interface YPZhiFuBaoAndBankInfoVC ()<UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UITextField *zhifubaoNameTF;

@property (weak, nonatomic) IBOutlet UITextField *zhifubaoPassTF;

@property (weak, nonatomic) IBOutlet UIButton *zhifubaoBindBtn;

@property (weak, nonatomic) IBOutlet UITextField *bankNameTF;

@property (weak, nonatomic) IBOutlet UITextField *bankPassTF;

@property (weak, nonatomic) IBOutlet UIButton *bankBindBtn;
@property (weak, nonatomic) IBOutlet UIButton *tijiaoBtn;

@property (assign, nonatomic)  NSInteger accountType;
@property (strong, nonatomic)  NSString* account;
@property (strong, nonatomic)  NSString* accountName;


@end

@implementation YPZhiFuBaoAndBankInfoVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self getFinancialAccountData];
    self.title = @"选择平台与对应账号";
//     _zhifubaoBtn.selected = YES;
//     _accountType = 1;
//    _bankNameTF.userInteractionEnabled = NO;
//    _bankPassTF.userInteractionEnabled = NO;
    self.bankPassTF.delegate = self;
    self.zhifubaoPassTF.delegate = self;
    
    self.tijiaoBtn.layer.cornerRadius = 8;
    self.tijiaoBtn.layer.masksToBounds = YES;
   
    [self.zhifubaoNameTF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
       [self.zhifubaoPassTF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.bankNameTF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.bankPassTF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
}

-(void)getFinancialAccountData{
    
    [YPHttpRequestHelper requestGetFinancialAccount:[GetCore(YPAuthCoreHelp).getUid userIDValue] success:^(HJFinancialAccountDataModel *model) {
        self.zhifubaoNameTF.text = model.alipayAccountName;
        self.zhifubaoPassTF.text = model.alipayAccount;
        self.bankNameTF.text = model.bankCardName;
        self.bankPassTF.text = model.bankCard;
        
        if (model.alipayAccount.length>0) {
            self.accountType = 1;
            self.accountName = model.alipayAccountName;
            self.account = model.alipayAccount;
//            self.tijiaoBtn.enabled = YES;
//            self.tijiaoBtn.backgroundColor = [UIColor colorWithHexString:@"#159CFE"];
         
        }else if (model.bankCard.length>0){
            self.accountType = 2;
             self.accountName = model.bankCardName;
              self.account = model.bankCard;
         
//            self.tijiaoBtn.enabled = YES;
//            self.tijiaoBtn.backgroundColor = [UIColor colorWithHexString:@"#159CFE"];
            
        }
        [self updateEnterBtnStatus];
        
    } failure:^(NSNumber *code, NSString *errorMessage) {
        
    }];
}



- (void)updateEnterBtnStatus
{
    
    if (_zhifubaoBtn.selected) {
        if (self.zhifubaoNameTF.text.length>0 && self.zhifubaoPassTF.text.length>0) {
               self.tijiaoBtn.enabled = YES;
               self.tijiaoBtn.backgroundColor = [UIColor colorWithHexString:@"#159CFE"];
           }else{
               self.tijiaoBtn.enabled = NO;
               self.tijiaoBtn.backgroundColor = [UIColor colorWithHexString:@"#CCCCCC"];
           }
    }else if (_bankBtn.selected){
        if (self.bankNameTF.text.length>0 && self.bankPassTF.text.length>0) {
                      self.tijiaoBtn.enabled = YES;
                      self.tijiaoBtn.backgroundColor = [UIColor colorWithHexString:@"#159CFE"];
                  }else{
                      self.tijiaoBtn.enabled = NO;
                      self.tijiaoBtn.backgroundColor = [UIColor colorWithHexString:@"#CCCCCC"];
                  }
    }else{
         self.tijiaoBtn.enabled = NO;
    }
    
    
    
   
}

//delegate
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
    if (textField == self.bankPassTF) {
           if ([string isValidateNumber]) {
               if (textField.text.length>=25 ) {
                   if ([string isEqualToString:@""]) {
                       return YES;
                   }else{
                       return NO;
                   }
               }
                  return YES;
              }else{
                  return NO;
              }
       }
    if (textField == self.zhifubaoPassTF) {
      if ([string isChineseCharacter]) {
                    if (textField.text.length>=25 ) {
                        if ([string isEqualToString:@""]) {
                            return YES;
                        }else{
                            return NO;
                        }
                    }
                       return YES;
                   }else{
                       if ([string isEqualToString:@""]) {
                            return YES;
                                               }
                       return NO;
                   }
    }
    
    
    
    return YES;
}


- (void)textFieldDidChange :(UITextField *)theTextField{

    [self updateEnterBtnStatus];
}


- (IBAction)zhifubaoBtnAction:(id)sender {
    _accountType = 1;
    _bankBtn.selected = NO;
    _zhifubaoBtn.selected = YES;
    [_zhifubaoBtn setImage:[UIImage imageNamed:@"yp_tixianway_icon_yes"] forState:UIControlStateNormal];
     [_bankBtn setImage:[UIImage imageNamed:@"yp_tixianway_icon_no"] forState:UIControlStateNormal];
    
    _zhifubaoNameTF.userInteractionEnabled = YES;
    _zhifubaoPassTF.userInteractionEnabled = YES;
    _bankNameTF.userInteractionEnabled = NO;
    _bankPassTF.userInteractionEnabled = NO;
    [self updateEnterBtnStatus];
}

- (IBAction)bankBtnAction:(id)sender {
    _accountType = 2;
    _bankBtn.selected = YES;
     _zhifubaoBtn.selected = NO;
     [_bankBtn setImage:[UIImage imageNamed:@"yp_tixianway_icon_yes"] forState:UIControlStateNormal];
     [_zhifubaoBtn setImage:[UIImage imageNamed:@"yp_tixianway_icon_no"] forState:UIControlStateNormal];
    
    _zhifubaoNameTF.userInteractionEnabled = NO;
    _zhifubaoPassTF.userInteractionEnabled = NO;
    _bankNameTF.userInteractionEnabled = YES;
    _bankPassTF.userInteractionEnabled = YES;
     [self updateEnterBtnStatus];
    
    
}



- (IBAction)zhifubaoBindBtn:(id)sender {
}

- (IBAction)bankBindBtnAction:(id)sender {
}

- (IBAction)tijiaoBtnAction:(id)sender {
    if (_cashPayWayBlock) {
        if (_accountType == 1) {
            _account = _zhifubaoPassTF.text;
             _accountName = _zhifubaoNameTF.text;
        }else if (_accountType ==2){
            _account = _bankPassTF.text;
            _accountName = _bankNameTF.text;
        }
        _cashPayWayBlock(_accountType,_account,_accountName);
    }
    [self.navigationController popViewControllerAnimated:YES];
    
}



@end
