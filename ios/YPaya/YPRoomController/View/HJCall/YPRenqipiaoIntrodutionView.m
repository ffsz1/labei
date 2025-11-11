//
//  YPRenqipiaoIntrodutionView.m
//  HJLive
//
//  Created by feiyin on 2020/8/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRenqipiaoIntrodutionView.h"

@implementation YPRenqipiaoIntrodutionView

+ (void)showRenqipiaoIntrodutionView
{
    YPRenqipiaoIntrodutionView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPRenqipiaoIntrodutionView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
 
    YPBlackStatusBar
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];

    
//    shareView.mobileCenterYLayout.constant = kScreenHeight;
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{

//         shareView.mobileCenterYLayout.constant = 0;
        
        [shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        
        
    }];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
   
    
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

- (IBAction)closeBtnAction:(id)sender {
     YPLightStatusBar
     [self close];
}





@end
