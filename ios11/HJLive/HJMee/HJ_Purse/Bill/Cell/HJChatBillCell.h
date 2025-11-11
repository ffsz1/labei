//
//  HJChatBillCell.h
//  HJLive
//
//  Created by feiyin on 2020/6/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJChatBillCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *firstAvatarImageView;
@property (weak, nonatomic) IBOutlet UIImageView *secondAvatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *orderTitle;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *moneyLabel;
@property (weak, nonatomic) IBOutlet UILabel *moneyTypeLabel;


@end
