//
//  HJSearchResultCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSearchResultCell.h"
#import "UIColor+UIColor_Hex.h"

@implementation HJSearchResultCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.avatar.layer.masksToBounds = YES;
    self.avatar.layer.cornerRadius = 25;
//    self.idLabel.layer.masksToBounds = YES;
//    self.idLabel.layer.cornerRadius = 2;
//    self.idLabel.layer.borderColor = [UIColor colorWithHexString:@"#cccccc"].CGColor;
//    self.idLabel.layer.borderWidth = 0.5f;
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
}

@end
