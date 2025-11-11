//
//  YPRoomShenHaoView.m
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomShenHaoView.h"

@interface YPRoomShenHaoView ()
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *tipLabel;
@property (weak, nonatomic) IBOutlet GGImageView *bgView;
@property (weak, nonatomic) IBOutlet UIImageView *topImageView;

@end

@implementation YPRoomShenHaoView

+ (void)show:(YPRoomBounsListInfo *)info
{
    YPRoomShenHaoView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPRoomShenHaoView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    
//    shareView.bgView.alpha = 0;
//    shareView.topImageView.alpha = 0;
    
    if (info) {
        shareView.nameLabel.text = info.nick;
        [shareView.avatarImageView qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
    }
    
    
    //左右晃动动画
    CAKeyframeAnimation * keyAnimaion = [CAKeyframeAnimation animation];
    keyAnimaion.keyPath = @"transform.rotation";
    keyAnimaion.values = @[@(-0.5 / 180.0 * M_PI),@(0.5 /180.0 * M_PI),@(-0.5/ 180.0 * M_PI),@(0 /180.0 * M_PI)];//度数转弧度
    keyAnimaion.removedOnCompletion = NO;
    keyAnimaion.fillMode = kCAFillModeForwards;
    keyAnimaion.duration = 0.3;
    keyAnimaion.repeatCount = 2;//动画次数
    [shareView.bgView.layer addAnimation:keyAnimaion forKey:nil];
    
    
//    [UIView animateWithDuration:0.3 animations:^{
//        shareView.bgView.alpha = 1;
//        shareView.topImageView.alpha = 1;
//    }];
    
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    
    NSMutableAttributedString *textFont = [[NSMutableAttributedString alloc] initWithString:self.tipLabel.text];
    [textFont addAttribute:NSForegroundColorAttributeName
                     value:UIColorHex(60A6FF)
                     range:[self.tipLabel.text rangeOfString:@"神豪称号"]];
    self.tipLabel.attributedText = textFont;
    
    
}

- (IBAction)closeAction:(id)sender {
    
    [self removeFromSuperview];
}


@end
