//
//  HJRoomPKRecordModelCell.h
//  HJLive
//
//  Created by apple on 2019/6/19.
//

#import <UIKit/UIKit.h>

#import "HJRoomPKRecordModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJRoomPKRecordModelCell : UITableViewCell
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageVIew;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *resultLabel;
@property (weak, nonatomic) IBOutlet UIImageView *giftImageView;
@property (weak, nonatomic) IBOutlet UILabel *giftLabel;

@property (strong,nonatomic) HJRoomPKRecordModel *model;

@end

NS_ASSUME_NONNULL_END
