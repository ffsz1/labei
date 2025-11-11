//
//  YPRankCell.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "YPChartsModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPRankCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *numLabel;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *sexLabel;
@property (weak, nonatomic) IBOutlet UIImageView *levelImageView;
@property (weak, nonatomic) IBOutlet UIImageView *coinImageView;
@property (weak, nonatomic) IBOutlet UILabel *distanceLabel;

@property (nonatomic,strong) YPChartsModel* richModel;
@property (nonatomic,strong) YPChartsModel* charmModel;

@property (nonatomic,assign) NSInteger num;

@end

NS_ASSUME_NONNULL_END
