//
//  HJAuctionListerCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAuctionListerCell.h"
#import "HJUserCoreHelp.h"
#import "HJAuthCoreHelp.h"

@implementation HJAuctionListerCell

- (void)awakeFromNib {
    [super awakeFromNib];
//    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(showUserInfoCard)];
//    [self.avatarImageView addGestureRecognizer:tap];
//    self.avatarImageView.userInteractionEnabled = YES;
    
    self.noBottomView.layer.cornerRadius = 9.0;
    self.noBottomView.layer.masksToBounds = YES;
}

//-  (void)showUserInfoCard {
//    if ([GetCore(AuthCore)getUid].userIDValue != self.uid) {
//        if ([_delegate respondsToSelector:@selector(avatarClick:)]) {
//            if (_uid > 0) {
//                [_delegate avatarClick:_uid];
//            }
//        }
//    }
//}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
