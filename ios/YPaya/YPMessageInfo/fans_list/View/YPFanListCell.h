//
//  YPFanListCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "YPAttention.h"


@interface YPFanListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *idLabel;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageView;
@property (weak, nonatomic) IBOutlet UIImageView *charmImageView;
@property (weak, nonatomic) IBOutlet UIImageView *richImageView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_richImageView;
@property (weak, nonatomic) IBOutlet GGLabel *onlineLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_name;

@property (strong,nonatomic) YPAttention *info;
@property (weak, nonatomic) IBOutlet UIButton *followBtn;

@end


