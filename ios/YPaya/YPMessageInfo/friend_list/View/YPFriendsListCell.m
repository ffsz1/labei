//
//  YPFriendsListCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFriendsListCell.h"

#import "YPMySpaceVC.h"

#import "UIView+getTopVC.h"


@implementation YPFriendsListCell

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self addAvatarTap];

    
}

- (void)addAvatarTap
{
    __weak typeof(self)weakSelf = self;
    [self.avatarImageView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        [weakSelf avatarTapAction:nil];
    }]];
}

- (void)avatarTapAction:(UITapGestureRecognizer *)sender {
    
    YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
    vc.userID = _info.uid;
    [[self topViewController].navigationController pushViewController:vc animated:YES];
    
}

- (void)setInfo:(UserInfo *)info
{
    _info = info;
    if (info) {
        [self.avatarImageView qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        
        self.nameLabel.text = info.nick;
        self.sexImageView.image = [UIImage imageNamed:info.gender==2?@"yp_home_attend_woman":@"yp_home_attend_man"];
        self.idLabel.text = [NSString stringWithFormat:@"ID：%@",info.erbanNo];
        
        self.charmImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:info.charmLevel]];
        self.richImageView.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:info.experLevel]];
        
        if (info.charmLevel == 0) {
            self.left_richImageView.constant = -43;
        }else{
            self.left_richImageView.constant = 0;
        }
        
        
    }
}



@end
