//
//  YPNotiFriendTableViewCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/19.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <NIMSDK/NIMSDK.h>
#import "YPIMMessage.h"

@interface YPNotiFriendTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *deautLabel;
@property (weak, nonatomic) IBOutlet UIImageView *userHeadImg;
@property (weak, nonatomic) IBOutlet UIImageView *LvImg;
@property (weak, nonatomic) IBOutlet UIImageView *MlImg;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lvWidCons;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mlWidCons;
@property (weak, nonatomic) IBOutlet UILabel *nickLabel;
@property (nonatomic, strong) YPIMMessage *message;
@end
