//
//  YPBlackListCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBlackListCell.h"

#import "YPImFriendCore.h"

#import <NIMSDK/NIMSDK.h>

@implementation YPBlackListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
}
- (IBAction)removeAction:(id)sender {
    
    [MBProgressHUD showMessage:@"移除中..."];
    [GetCore(YPImFriendCore) removeFromBlackList:[NSString stringWithFormat:@"%lld",_info.uid]];

    
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
        
    }
}


@end
