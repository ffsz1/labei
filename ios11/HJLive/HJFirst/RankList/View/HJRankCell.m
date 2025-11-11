//
//  HJRankCell.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRankCell.h"

@implementation HJRankCell

- (void)setRichModel:(HJChartsModel *)richModel
{
    _richModel = richModel;
    if (richModel) {
        [self.avatarImageView qn_setImageImageWithUrl:richModel.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        self.nameLabel.text = richModel.nick;
        self.sexLabel.image = [UIImage imageNamed:richModel.gender==1?@"hj_rank_man":@"hj_rank_women"];
        self.levelImageView.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:richModel.experLevel]];
        self.distanceLabel.text = [NSString stringWithFormat:@"%0.f",richModel.distance];
    }
}

- (void)setCharmModel:(HJChartsModel *)charmModel
{
    _charmModel = charmModel;
    if (charmModel) {
        [self.avatarImageView qn_setImageImageWithUrl:charmModel.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        self.nameLabel.text = charmModel.nick;
        self.sexLabel.image = [UIImage imageNamed:charmModel.gender==1?@"hj_rank_man":@"hj_rank_women"];
        self.levelImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:charmModel.charmLevel]];
        self.distanceLabel.text = [NSString stringWithFormat:@"%0.f",charmModel.distance];
    }
}

- (void)setNum:(NSInteger)num
{
    _num = num;
    self.numLabel.text = [NSString stringWithFormat:@"%ld",num+1];
}

@end
