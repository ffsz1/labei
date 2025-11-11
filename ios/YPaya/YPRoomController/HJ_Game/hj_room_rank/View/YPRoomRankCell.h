//
//  YPRoomRankCell.h
//  HJLive
//
//  Created by apple on 2019/7/5.
//

#import <UIKit/UIKit.h>

#import "YPRoomBounsListInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomRankCell : UITableViewCell

@property (strong,nonatomic) YPRoomBounsListInfo *model;

@property (assign,nonatomic) BOOL isCharm;

@property (strong,nonatomic) NSIndexPath *indexPath;

@end

NS_ASSUME_NONNULL_END
