//
//  YPAlerVerificationCodeView.m
//  HJLive
//
//  Created by feiyin on 2020/8/21.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAlerVerificationCodeView.h"
#import "HJAuthCoreClient.h"
#import "NSString+HMLifeRegular.h"

@interface YPAlerVerificationCodeView()

@property (copy, nonatomic) HJAlerVerificationCodeBlock menuBlock;
@property (weak, nonatomic) IBOutlet UIView *effectView;
@property (weak, nonatomic) IBOutlet UIView *bgview;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mobileCenterYLayout;
@property (weak, nonatomic) IBOutlet UILabel *desLabel;

@property (weak, nonatomic) IBOutlet UIButton *confirmBtn;

@property (weak, nonatomic) IBOutlet UITextField *alerVeriCodeTF;

@property (weak, nonatomic) IBOutlet UIButton *alerVeriCodeBtn;

@property (weak, nonatomic) IBOutlet UILabel *desTipLabel;


@end
@implementation YPAlerVerificationCodeView


+ (void)show:(HJAlerVerificationCodeBlock)menuBlock content:(NSString*)content nick:(NSString*)nick isAttribute:(BOOL)isAttribute
{
    YPAlerVerificationCodeView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPAlerVerificationCodeView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    if (isAttribute) {
        NSMutableAttributedString *contentStr = [[NSMutableAttributedString alloc] initWithString:content];
            //找出特定字符在整个字符串中的位置
            NSRange redRange = NSMakeRange([[contentStr string] rangeOfString:nick].location, [[contentStr string] rangeOfString:nick].length);
            //修改特定字符的颜色
            [contentStr addAttribute:NSForegroundColorAttributeName value:[UIColor colorWithRGB:0xEE47B7 alpha:1] range:redRange];
            //修改特定字符的字体大小
            [contentStr addAttribute:NSFontAttributeName value:[UIFont boldSystemFontOfSize:18] range:redRange];
           
            shareView.desLabel.attributedText = contentStr;
    }else{
        shareView.desLabel.text = content;
    }
   
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];

    shareView.menuBlock = menuBlock;
    shareView.mobileCenterYLayout.constant = kScreenHeight;
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{

         shareView.mobileCenterYLayout.constant = 0;
        
        [shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        
        
    }];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    self.bgview.layer.cornerRadius = 20;
    self.bgview.layer.masksToBounds = YES;
    
    self.confirmBtn.layer.cornerRadius = 20;
    self.confirmBtn.layer.masksToBounds = YES;
    
    self.alerVeriCodeBtn.layer.masksToBounds = YES;
    self.alerVeriCodeBtn.layer.cornerRadius = 10;
    
    self.alerVeriCodeTF.layer.masksToBounds = YES;
    self.alerVeriCodeTF.layer.cornerRadius = 10;
    
    self.alerVeriCodeBtn.layer.borderColor = [UIColor colorWithHexString:@"67A3FF"].CGColor;
    self.alerVeriCodeBtn.layer.borderWidth = 1.0;
    
    self.alerVeriCodeTF.layer.borderColor = [UIColor colorWithHexString:@"67A3FF"].CGColor;
    self.alerVeriCodeTF.layer.borderWidth = 1.0;
   UITapGestureRecognizer* tapGest = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction)];
    [self.effectView addGestureRecognizer:tapGest];
      AddCoreClient(HJAuthCoreClient, self);
    
}

- (void)close
{
    [self layoutIfNeeded];
    
    [UIView animateWithDuration:0.4 animations:^{
        
        self.mobileCenterYLayout.constant = kScreenHeight;
   
        [self layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
    
}

//获取验证码
- (IBAction)alerVeriCodeBtnAction:(id)sender {
    self.menuBlock(HJAlerGetVeriCodeType,@"");
    
    self.desTipLabel.hidden = NO;
    NSString *userId = [GetCore(YPAuthCoreHelp) getUid];
    UserInfo *info = [GetCore(YPUserCoreHelp) getUserInfoInDB:[userId intValue]];
    NSString *phone = [info.phone stringByReplacingCharactersInRange:NSMakeRange(3, 4) withString:@"****"];
    self.desTipLabel.text = [NSString stringWithFormat:@"验证码已发送至您绑定的手机号%@",phone];
}


- (IBAction)confirmAction:(id)sender {
    if (self.alerVeriCodeTF.text.length >0 && self.alerVeriCodeTF.text.length <7 &&self.alerVeriCodeTF.text.isValidateNumber) {
        self.confirmBtn.userInteractionEnabled = YES;
        self.menuBlock(HJAlerVerificationCodeFirmType,self.alerVeriCodeTF.text);
           [self close];
    }else{
       
        [MBProgressHUD showError:@"请输入正确验证码"];
    }
   
}


- (IBAction)cancelBtnAction:(id)sender {
    self.menuBlock(HJAlerVerificationCodeCancelType,@"");
    [self close];
}



-(void)tapAction{
     [self close];
    
}


- (void)onCutdownOpen:(NSNumber *)number
{
    [self.alerVeriCodeBtn setTitle:[NSString stringWithFormat:@"%ds", number.intValue] forState:UIControlStateNormal];
    [self.alerVeriCodeBtn setEnabled:NO];
}

- (void)onCutdownFinish
{
    //设置按钮的样式
    [self.alerVeriCodeBtn setTitle:NSLocalizedString(XCPurseVerifySendAgain, nil) forState:UIControlStateNormal];
    [self.alerVeriCodeBtn setEnabled:YES];
}

@end
