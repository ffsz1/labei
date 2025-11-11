//
//  HJFamilySettingItemCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class HJFamilyManageSettingItemModel;

UIKIT_EXTERN NSString *const HJFamilySettingItemCellID;

@interface HJFamilySettingItemCell : UITableViewCell

- (void)configureWithItem:(HJFamilyManageSettingItemModel *)item;

@end
NS_ASSUME_NONNULL_END
