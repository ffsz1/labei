//
//  HJRechargeViewCollectionViewCell.m
//  HJLive
//
//  Created by feiyin on 2020/6/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRechargeViewCollectionViewCell.h"
#import "UIColor+UIColor_Hex.h"
@interface HJRechargeViewCollectionViewCell()

@end

@implementation HJRechargeViewCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
//    self.bgView.layer.masksToBounds = YES;
//    self.bgView.layer.cornerRadius = 25;
//    self.bgView.layer.borderWidth = 0.5;
//    self.bgView.layer.borderColor = [UIColor colorWithHexString:@"#e5e5e5"].CGColor;
    [self.bgImageView setImage:[UIImage imageNamed:@"hj_recharge_icon_no"]];
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(selectCell)];
    [self.bgView addGestureRecognizer:tap];
}

- (void)selectCell {
    if (self.delegate && [self.delegate respondsToSelector:@selector(onRmbSelected:)]) {
        [self.delegate onRmbSelected:self.index];
    }
}

@end
