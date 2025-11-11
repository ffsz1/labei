//
//  YPFamilyMemberSettingCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN
@class YPFamilyInfoDetail;

UIKIT_EXTERN NSString *const HJFamilyMemberSettingCellID;

typedef void(^HJFamilyMemberSettingCellDidTapActionHandler)(void);
@interface YPFamilyMemberSettingCell : UITableViewCell
@property (nonatomic, copy) HJFamilyMemberSettingCellDidTapActionHandler didTapActionHandler;

- (void)configureWithMemberInfo:(YPFamilyInfoDetail *)memberInfo;

@end

NS_ASSUME_NONNULL_END
