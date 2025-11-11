//
//  NTESSessionListCell.h
//  NIMDemo
//
//  Created by chris on 15/2/10.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import <UIKit/UIKit.h>
@class YPNIMAvatarImageView;
@class NIMRecentSession;
@class YPNIMBadgeView;

@interface YPNIMSessionListCell : UITableViewCell

@property (nonatomic,strong) YPNIMAvatarImageView *avatarImageView;

@property (nonatomic,strong) UILabel *nameLabel;

@property (nonatomic,strong) UILabel *messageLabel;

@property (nonatomic,strong) UILabel *timeLabel;

@property (nonatomic,strong) UIImageView *sexImageView;

@property (nonatomic,strong) YPNIMBadgeView *badgeView;

- (void)refresh:(NIMRecentSession*)recent;

@end
