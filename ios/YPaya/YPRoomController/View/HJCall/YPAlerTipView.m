//
//  YPAlerTipView.m
//  HJLive
//
//  Created by feiyin on 2020/8/21.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAlerTipView.h"
@interface YPAlerTipView()

@property (copy, nonatomic) AlerTipBlock menuBlock;
@property (weak, nonatomic) IBOutlet UIView *effectView;
@property (weak, nonatomic) IBOutlet UIView *bgview;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mobileCenterYLayout;
@property (weak, nonatomic) IBOutlet UILabel *desLabel;

@property (weak, nonatomic) IBOutlet UIButton *confirmBtn;


@end
@implementation YPAlerTipView


+ (void)show:(AlerTipBlock)menuBlock content:(NSString*)content nick:(NSString*)nick isAttribute:(BOOL)isAttribute
{
    YPAlerTipView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPAlerTipView" owner:self options:nil][0];
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
  
   UITapGestureRecognizer* tapGest = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction)];
    [self.effectView addGestureRecognizer:tapGest];
    
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




- (IBAction)confirmAction:(id)sender {
    self.menuBlock(HJAlerTipfirmType);
    [self close];
}


- (IBAction)cancelBtnAction:(id)sender {
    self.menuBlock(HJAlerTipCancelType);
    [self close];
}



-(void)tapAction{
     [self close];
    
}

@end
