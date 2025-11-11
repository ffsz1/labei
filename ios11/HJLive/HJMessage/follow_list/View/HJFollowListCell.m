//
//  HJFollowListCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFollowListCell.h"

#import "HJRoomPusher.h"

#import "HJMySpaceVC.h"

#import "UIView+getTopVC.h"

@implementation HJFollowListCell

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
    
    HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
    vc.userID = _info.uid;
    [[self topViewController].navigationController pushViewController:vc animated:YES];
    
}
- (IBAction)findAction:(id)sender {
    
    if (self.info) {
        
        [HJRoomPusher pushUserInRoomByID:self.info.uid];
    }
    
}

- (void)setInfo:(Attention *)info
{
    _info = info;
    if (info) {
        
        
        [self.avatarImageView qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        self.nameLabel.text = info.nick;
        self.sexImageView.image = [UIImage imageNamed:info.gender==2?@"hj_home_attend_woman":@"hj_home_attend_man"];
        self.idLabel.text = [NSString stringWithFormat:@"ID：%@",info.erbanNo];

        self.charmImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:info.charmLevel]];
        self.richImageView.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:info.experLevel]];
        
        self.onlineLabel.hidden = info.valid?NO:YES;
        
        if (info.charmLevel == 0) {
            self.left_richImageView.constant = -48;
        }else{
            self.left_richImageView.constant = 0;
        }
        
        
        CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, 20) options:(NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading) attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;

        if ((kScreenWidth-254)<size.width) {
            self.width_name.constant = kScreenWidth - 254;
        }else{
            self.width_name.constant = size.width;
        }

        
    }
}

@end
