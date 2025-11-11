//
//  YPChatBillCell.m
//  HJLive
//
//  Created by feiyin on 2020/6/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPChatBillCell.h"

@implementation YPChatBillCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.firstAvatarImageView.layer.masksToBounds = YES;
    self.firstAvatarImageView.layer.cornerRadius = self.firstAvatarImageView.frame.size.width / 2;
    self.firstAvatarImageView.layer.borderColor = [[YPYYTheme defaultTheme] colorWithHexString:@"#FFC160" alpha:1].CGColor;
    self.firstAvatarImageView.layer.borderWidth = 2;
    
    self.secondAvatarImageView.layer.masksToBounds = YES;
    self.secondAvatarImageView.layer.cornerRadius = self.secondAvatarImageView.frame.size.width / 2;
    self.secondAvatarImageView.layer.borderColor = [[YPYYTheme defaultTheme] colorWithHexString:@"#7EE7F6" alpha:1].CGColor;
    self.secondAvatarImageView.layer.borderWidth = 2;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
