//
//  HJRoomOnlineCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomOnlineCell.h"

#import "NSString+GGImage.h"


@implementation HJRoomOnlineCell

- (IBAction)deleteAction:(id)sender {
    self.upMicBlock(self.indexPath);
}

- (void)setModel:(ChatRoomMember *)model
{
    _model = model;
    
    if (model) {
        self.nameLabel.text = model.nick;
        [self.avatarImageView qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        self.sexImageView.image = [UIImage imageNamed:model.gender==1?@"hj_home_attend_man":@"hj_home_attend_woman"];
        
        if (model.exper_level>0) {
            self.richLevelImageView.image = [UIImage imageNamed:[NSString getLevelImageName:model.exper_level]];
            self.richLevelImageView.hidden = NO;
            self.left_charm.constant = 5;
        }else{
            self.richLevelImageView.hidden = YES;
            self.left_charm.constant = -43;
        }
        
        self.charmLevelImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:model.charm_level]];
        
        CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, self.nameLabel.height) options:(NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading) attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;
        
        self.width_name.constant = kScreenWidth-245>size.width?size.width:kScreenWidth-245;

        

    }
    
}

@end
