//
//  YPRoomMemberListCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomMemberListCell.h"

@implementation YPRoomMemberListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.backgroundColor = [UIColorHex(FFFFFF) colorWithAlphaComponent:1];//04000F
    self.contentView.backgroundColor = [UIColorHex(FFFFFF) colorWithAlphaComponent:1];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
