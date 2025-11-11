//
//  YPZaJinDanRankListCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPZaJinDanRankListCell.h"

#import <UIImageView+WebCache.h>

@interface YPZaJinDanRankListCell ()

@property (weak, nonatomic) IBOutlet UIImageView *photoView;
@property (weak, nonatomic) IBOutlet UIImageView *sexView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *moneyLabel;
@property (weak, nonatomic) IBOutlet UIImageView *lvView;
@property (weak, nonatomic) IBOutlet UIImageView *mlView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lvW;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mlW;

@end


@implementation YPZaJinDanRankListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.photoView.contentMode = UIViewContentModeScaleAspectFill;
    self.photoView.clipsToBounds = YES;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(YPGiftPurseRank *)model {
    
    _model = model;
    [self.contentView bringSubviewToFront:self.rankingLabel];
    [self.photoView sd_setImageWithURL:[NSURL URLWithString:model.avatar] placeholderImage:[UIImage imageNamed:default_avatar]];
    self.sexView.image = model.gender == 1 ? [UIImage imageNamed:@"room_game_online_male"] : [UIImage imageNamed:@"room_game_online_female"];
    self.nameLabel.text = model.nick.length ? model.nick : @"";
    self.moneyLabel.text = model.tol;
    if (model.charmLevel > 0) {
        
//        self.mlView.image = [UIImage imageNamed:[NSString stringWithFormat:@"ml%zd",model.charmLevel]];
        self.mlView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:model.charmLevel]];
        self.mlW.constant = 37;
    }
    else {
        self.mlView.image = [UIImage imageNamed:@""];
        self.mlW.constant = 0;
    }
    
    if (model.experLevel > 0) {
        
//        self.lvView.image = [UIImage imageNamed:[NSString stringWithFormat:@"Lv%zd",model.experLevel]];
        self.lvView.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:model.experLevel]];
        self.lvW.constant = 37;
    }
    else {
        self.lvView.image = [UIImage imageNamed:@""];
        self.lvW.constant = 0;
    }
}

@end
