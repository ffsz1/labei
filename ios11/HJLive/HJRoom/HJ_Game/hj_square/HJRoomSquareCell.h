//
//  HJRoomSquareCell.h
//  HJLive
//
//  Created by apple on 2019/4/17.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJRoomSquareCell : UITableViewCell
@property (weak, nonatomic) IBOutlet GGImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *msgLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_name;
@property (weak, nonatomic) IBOutlet UIImageView *level;
@property (weak, nonatomic) IBOutlet UIImageView *meiliLevel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *level_width;

@end

NS_ASSUME_NONNULL_END
