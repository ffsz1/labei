//
//  HJRoomRankCell.h
//  HJLive
//
//  Created by apple on 2019/7/5.
//

#import <UIKit/UIKit.h>

#import "HJRoomBounsListInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJRoomRankCell : UITableViewCell

@property (strong,nonatomic) HJRoomBounsListInfo *model;

@property (assign,nonatomic) BOOL isCharm;

@property (strong,nonatomic) NSIndexPath *indexPath;

@end

NS_ASSUME_NONNULL_END
