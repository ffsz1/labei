//
//  YPFamilyManageItemCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class YPFamilyManageItemModel;

UIKIT_EXTERN NSString *const HJFamilyManageItemCellID;

typedef void(^HJFamilyManageItemCellDidSwitchHandler)(BOOL isOn);

@interface YPFamilyManageItemCell : UITableViewCell

@property (nonatomic, strong, readonly) UISwitch *switchView;
@property (nonatomic, copy) HJFamilyManageItemCellDidSwitchHandler didSwitchHandler;

- (void)configureWithItem:(YPFamilyManageItemModel *)item;

@end

NS_ASSUME_NONNULL_END
