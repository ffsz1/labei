//
//  HJFamilyManagerSettingCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class HJFamilyInfoDetail;

UIKIT_EXTERN NSString *const HJFamilyManagerSettingCellID;

typedef void(^HJFamilyManagerSettingCellDidTapActionHandler)(void);
typedef void(^HJFamilyManagerSettingCellDidTapDeleteHandler)(void);

@interface HJFamilyManagerSettingCell : UITableViewCell

@property (nonatomic, copy) HJFamilyManagerSettingCellDidTapActionHandler didTapActionHandler;
@property (nonatomic, copy) HJFamilyManagerSettingCellDidTapDeleteHandler didTapDeleteHandler;

- (void)configureWithMemberInfo:(HJFamilyInfoDetail *)memberInfo;

@end

NS_ASSUME_NONNULL_END
