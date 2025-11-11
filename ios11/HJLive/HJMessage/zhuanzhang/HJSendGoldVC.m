//
//  HJSendGoldVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/27.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "UIView+XCToast.h"
#import "HJSendGoldVC.h"
#import "HJHttpRequestHelper+Home.h"
#import "NSString+HMLifeRegular.h"
@interface HJSendGoldVC ()<UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UIButton *sendGoldBtn;
@property (weak, nonatomic) IBOutlet UITextField *sendGoldTF;

@property (weak, nonatomic) IBOutlet UIImageView *arvatarImg;

@property (weak, nonatomic) IBOutlet UILabel *nickLabel;

@property (weak, nonatomic) IBOutlet UIView *downView;


@end

@implementation HJSendGoldVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"转账";
    self.nickLabel.text = _userInfo.nick;
    [self.arvatarImg sd_setImageWithURL:[NSURL URLWithString:_userInfo.avatar] placeholderImage:[UIImage imageNamed:@"blank"]];
    [self setBgRaduis];
    _sendGoldTF.delegate = self;

    _sendGoldBtn.layer.cornerRadius = 8;
    _sendGoldBtn.layer.masksToBounds = YES;
}


//delegate
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
    if (textField == self.sendGoldTF) {
           if ([string isValidateNumber]) {
               if (textField.text.length>=8 ) {
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
    
    return YES;
}


- (void)setBgRaduis
{
    CGRect frame = CGRectMake(0, 0, kScreenWidth, 400);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.downView.layer.mask = maskLayer;
 
}

-(void)sendGoldData{

    
    [HJHttpRequestHelper requestChangeGoldTogold:_sendGoldTF.text.userIDValue recvUid:_recvUid success:^(HJSendGoldModel *model) {

        self.sendGoldTF.text = @"";
        [self.sendGoldTF resignFirstResponder];
        if (self.sendGlodBlock) {
            self.sendGlodBlock(model);
        }
           [UIView showToastInKeyWindow:@"发红包成功" duration:2.0 position:(YYToastPosition)YYToastPositionCenter];
        [self dismissViewControllerAnimated:YES completion:nil];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
    

}

- (IBAction)backMotaiBtnAction:(id)sender {
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)sendGoldBtnAction:(id)sender {
    
 
    
    NSString* message = [NSString stringWithFormat:@"确认赠送 %@ 开心给用户'%@'吗？",_sendGoldTF.text,_userInfo.nick];
      UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:message preferredStyle:UIAlertControllerStyleAlert];
        
        [alter addAction:[UIAlertAction actionWithTitle:@"确认" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
             [self sendGoldData];
        }]];
        
        [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            
        }]];
    
   
    [self presentViewController:alter animated:YES completion:nil];
    
}


@end
