//
//  HJHomeMyFollowCCell.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "Attention.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJHomeMyFollowCCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;

@property (strong,nonatomic) Attention *model;

@end

NS_ASSUME_NONNULL_END
