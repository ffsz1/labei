//
//  HJGiftBillCell.h
//  HJLive
//
//  Created by feiyin on 2020/6/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJGiftBillCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *giftLogoImageView;
@property (weak, nonatomic) IBOutlet UILabel *giftNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *giftSenderNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *countLabel;
@property (weak, nonatomic) IBOutlet UIImageView *giftIcon;

@property (weak, nonatomic) IBOutlet UILabel *timeLabel;

@end
