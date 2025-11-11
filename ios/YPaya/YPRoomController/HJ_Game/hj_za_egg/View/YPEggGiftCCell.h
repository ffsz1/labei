//
//  YPEggGiftCCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "YPGiftInfo.h"
#import "YYLabel.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPEggGiftCCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet YYLabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *priceLabel;

@property (strong,nonatomic) YPGiftInfo *info;

@end

NS_ASSUME_NONNULL_END
