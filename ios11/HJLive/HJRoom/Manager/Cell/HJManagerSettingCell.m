//
//  HJManagerSettingCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
//

#import "HJManagerSettingCell.h"

@implementation HJManagerSettingCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.avatar.layer.cornerRadius = self.avatar.frame.size.width / 2;
    self.avatar.layer.masksToBounds = YES;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)removeBtnClick:(UIButton *)sender {
//    if ([_delegate respondsToSelector:@selector(removeBy:)]) {
    if (_delegate) {
        [_delegate removeBy:self.indexPath];
    }
    
//    }
}

@end
