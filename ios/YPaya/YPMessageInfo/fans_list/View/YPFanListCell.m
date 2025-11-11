//
//  YPFanListCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFanListCell.h"

#import "YPRoomPusher.h"

#import "NSString+GGImage.h"

#import <NIMSDK/NIMSDK.h>
#import "YPPraiseCore.h"

#import "YPMySpaceVC.h"

#import "UIView+getTopVC.h"

@implementation YPFanListCell

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

- (IBAction)findAction:(id)sender {
    
    [GetCore(YPPraiseCore) praise:[GetCore(YPAuthCoreHelp) getUid].userIDValue bePraisedUid:self.info.uid];
    
}

- (void)setInfo:(YPAttention *)info
{
    _info = info;
    if (info) {
        
        
        
        [self.avatarImageView qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        self.nameLabel.text = info.nick;
        self.sexImageView.image = [UIImage imageNamed:info.gender==2?@"yp_home_attend_woman":@"yp_home_attend_man"];
        self.idLabel.text = [NSString stringWithFormat:@"ID：%@",info.erbanNo];
        
        self.charmImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:info.charmLevel]];
        self.richImageView.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:info.experLevel]];
        
//        self.onlineLabel.hidden = info.valid?NO:YES;
        
        if (info.charmLevel == 0) {
            self.left_richImageView.constant = -48;
        }else{
            self.left_richImageView.constant = 0;
        }
        
//        self.followBtn.selected = info.isf
        
        
        CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, 20) options:(NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading) attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;
        
        if ((kScreenWidth-254)<size.width) {
            self.width_name.constant = kScreenWidth - 254;
        }else{
            self.width_name.constant = size.width;
        }
        
        BOOL isMyFriend = [[NIMSDK sharedSDK].userManager isMyFriend:[NSString stringWithFormat:@"%lld",info.uid]];
        self.followBtn.selected = isMyFriend;
        self.followBtn.userInteractionEnabled = !isMyFriend;
        
    }
}


@end
