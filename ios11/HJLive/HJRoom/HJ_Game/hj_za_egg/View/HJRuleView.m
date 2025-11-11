//
//  HJRuleView.m
//  HJLive
//
//  Created by MacBook on 2020/8/21.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRuleView.h"

@implementation HJRuleView

+ (void)showRule
{
    [HJRuleView loadXib];
}


+ (void)loadXib
{
    HJRuleView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJRuleView" owner:self options:nil].lastObject;
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    shareView.backgroundColor = [UIColor clearColor];
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    [shareView show];
    
    
    CGFloat height = -360;
    shareView.bottom_bgView.constant = height;
    
    //展示底部上浮动画
    [shareView layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        shareView.bottom_bgView.constant = -12;
        [shareView layoutIfNeeded];
    }];
    
    //展示底部下沉动画
    __weak typeof(shareView)weakView = shareView;
    shareView.dismissBlock = ^{
        [weakView layoutIfNeeded];
        [UIView animateWithDuration:0.3 animations:^{
            
            weakView.bottom_bgView.constant = height;
            
            [weakView layoutIfNeeded];
        }];
    };
}

- (void)awakeFromNib
{
    [super awakeFromNib];
}

- (IBAction)closeBtnAction:(id)sender {
    
    [self dismiss];
}
@end
