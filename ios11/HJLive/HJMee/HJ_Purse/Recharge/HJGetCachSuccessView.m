//
//  HJGetCachSuccessView.m
//  HJLive
//
//  Created by feiyin on 2020/7/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGetCachSuccessView.h"

@implementation HJGetCachSuccessView

+ (void)show:(CachSuccessBlock)menuBlock cashNum:(NSString*)cashNum
{
    
    
    HJGetCachSuccessView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJGetCachSuccessView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    shareView.cashNum = cashNum;
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];

    shareView.menuBlock = menuBlock;
    shareView.mobileCenterYLayout.constant = kScreenHeight;
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        
        
    
         shareView.mobileCenterYLayout.constant = 0;
        
        [shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        shareView.desLabel.text= [NSString stringWithFormat:@"您兑换的主播收益分成会在3个工作日内到账,请关注您的账户余额变动。"];
        
    }];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    self.bgview.layer.cornerRadius = 20;
    self.bgview.layer.masksToBounds = YES;

  
    self.canelBtn.layer.cornerRadius = 8;
    self.canelBtn.layer.masksToBounds = YES;
    
    self.desLabel.text= [NSString stringWithFormat:@"您兑换的主播收益分成会在3个工作日内到账,请关注您的账户余额变动。"];
    
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
     self.menuBlock(HJCachSuccessCancelType);
     [self close];
}


@end
