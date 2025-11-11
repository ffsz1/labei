//
//  HJPlayRulesView.m
//  HJLive
//
//  Created by feiyin on 2020/8/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPlayRulesView.h"

@implementation HJPlayRulesView
- (void)awakeFromNib
{
    [super awakeFromNib];
    [self setBgRaduis];
}
+ (void)showPlayRulesView
{

    
    HJPlayRulesView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJPlayRulesView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
 
    shareView.bottom_manager_layout.constant = -248;
  
    
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.7 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        
        shareView.bottom_manager_layout.constant = 0;
    
        
        [shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        
    }];
}

- (void)setBgRaduis
{
    CGRect frame = CGRectMake(0, 0, kScreenWidth, 288);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.manageView.layer.mask = maskLayer;
 
}
- (void)close
{
    [self layoutIfNeeded];
    
    [UIView animateWithDuration:0.3 animations:^{
        
        self.bottom_manager_layout.constant = -248;
      
        
       
        [self layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
    
}


- (IBAction)closeBtnAction:(id)sender {
    [self close];
    
}


- (IBAction)tapGestureAction:(id)sender {
     [self close];
}



@end
