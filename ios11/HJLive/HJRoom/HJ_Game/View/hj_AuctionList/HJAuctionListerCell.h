//
//  HJAuctionListerCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol HJAuctionListerCellDelegate <NSObject>

- (void)avatarClick:(UserID)uid;

@end

@interface HJAuctionListerCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIView *noBottomView;
@property (weak, nonatomic) IBOutlet UIImageView *rankListImageView;
@property (weak, nonatomic) IBOutlet UIImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nicknameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageView;
@property (weak, nonatomic) IBOutlet UILabel *coinNumberlabel;
@property (weak, nonatomic) IBOutlet UILabel *rankNumLabel;
@property (weak, nonatomic) IBOutlet UIImageView *lvImageView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lvImageCons;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *colingWidCons;
@property (weak, nonatomic) IBOutlet UIImageView *countLeftImg;

@property (assign, nonatomic) UserID uid;

@property (weak, nonatomic) id<HJAuctionListerCellDelegate> delegate;

@end
