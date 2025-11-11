//
//  YPRoomPKRecordModelCell.h
//  HJLive
//
//  Created by apple on 2019/6/19.
//

#import <UIKit/UIKit.h>

#import "YPRoomPKRecordModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomPKRecordModelCell : UITableViewCell
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageVIew;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *resultLabel;
@property (weak, nonatomic) IBOutlet UIImageView *giftImageView;
@property (weak, nonatomic) IBOutlet UILabel *giftLabel;

@property (strong,nonatomic) YPRoomPKRecordModel *model;

@end

NS_ASSUME_NONNULL_END
