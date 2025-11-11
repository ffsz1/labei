//
//  YPFamilySettingItemCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class YPFamilyManageSettingItemModel;

UIKIT_EXTERN NSString *const HJFamilySettingItemCellID;

@interface YPFamilySettingItemCell : UITableViewCell

- (void)configureWithItem:(YPFamilyManageSettingItemModel *)item;

@end
NS_ASSUME_NONNULL_END
