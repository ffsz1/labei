//
//  YPZaJinDanRankListCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPGiftPurseRank.h"

@interface YPZaJinDanRankListCell : UITableViewCell

@property (nonatomic, strong) YPGiftPurseRank *model;

@property (weak, nonatomic) IBOutlet UILabel *rankingLabel;
@property (weak, nonatomic) IBOutlet UIImageView *crownIcon;
@property (weak, nonatomic) IBOutlet UIImageView *cellBg;
@end
