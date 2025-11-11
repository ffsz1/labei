//
//  YPRoomBackgroundSeetingCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPRoomBgModel.h"

NS_ASSUME_NONNULL_BEGIN

UIKIT_EXTERN NSString *const HJRoomBackgroundSeetingCellID;

@interface YPRoomBackgroundSeetingCell : UICollectionViewCell

- (void)configureWithBackgroundInfo:(YPRoomBgModel *)roomInfo selected:(BOOL)selected;

@end

NS_ASSUME_NONNULL_END
