//
//  HJRoomManagerCell.h
//  HJLive
//
//  Created by apple on 2019/7/8.
//

#import <UIKit/UIKit.h>

typedef void(^XBDRoomManagerRemove)(NSIndexPath* _Nullable index);

NS_ASSUME_NONNULL_BEGIN

@interface HJRoomManagerCell : UITableViewCell
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;

@property (strong,nonatomic) NSIndexPath *indexPath;

@property (copy,nonatomic) XBDRoomManagerRemove removeBlock;

@end

NS_ASSUME_NONNULL_END
