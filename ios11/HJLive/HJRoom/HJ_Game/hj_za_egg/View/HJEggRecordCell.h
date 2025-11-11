//
//  HJEggRecordCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "HJEggRecordModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJEggRecordCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *priceLabel;
@property (weak, nonatomic) IBOutlet UIImageView *cellBg;

@property (nonatomic,strong) HJEggRecordModel *model;

@end

NS_ASSUME_NONNULL_END
