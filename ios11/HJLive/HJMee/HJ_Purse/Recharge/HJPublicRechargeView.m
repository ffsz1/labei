//
//  HJPublicRechargeView.m
//  HJLive
//
//  Created by feiyin on 2020/7/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPublicRechargeView.h"

@implementation HJPublicRechargeView


+ (void)show:(PublicRechargeBlock)menuBlock
{
    HJPublicRechargeView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJPublicRechargeView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];

    shareView.menuBlock = menuBlock;
    shareView.mobileCenterYLayout.constant = kScreenHeight;
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:1.2 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        
        
    
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

    self.showWeixinBtn.layer.cornerRadius = 12;
    self.showWeixinBtn.layer.masksToBounds = YES;
    self.showWeixinBtn.layer.borderWidth = 1;
    self.showWeixinBtn.layer.borderColor = [UIColor colorWithHexString:@"#999999"].CGColor;
    
    self.canelBtn.layer.cornerRadius = 12;
    self.canelBtn.layer.masksToBounds = YES;
     self.canelBtn.layer.borderColor = [UIColor colorWithHexString:@"#999999"].CGColor;
     self.canelBtn.layer.borderWidth = 1;
    
    
   UITapGestureRecognizer* tapGest = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction)];
    [self.effectView addGestureRecognizer:tapGest];
    
}

- (void)close
{
    [self layoutIfNeeded];
    
    [UIView animateWithDuration:0.4 animations:^{
        
//        self.mobileCenterYLayout.constant = kScreenHeight;
   
        [self layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
    
}


-(void)tapAction{
     [self close];
}



- (IBAction)cancelBtnAction:(id)sender {
     self.menuBlock(HJPublicRechargeCancelType);
     [self close];
}


- (IBAction)showWeixinBtnAction:(id)sender {
    
     self.menuBlock(HJPublicRechargeConfirmType);
    [self close];
}



@end
