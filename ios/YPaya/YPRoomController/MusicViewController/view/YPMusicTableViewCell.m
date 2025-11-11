//
//  YPMusicTableViewCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMusicTableViewCell.h"

@implementation YPMusicTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}
    
- (IBAction)Click:(UIButton *)sender {
    if (self.deleMusicBlock) {
        self.deleMusicBlock();
    }
}
    
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
