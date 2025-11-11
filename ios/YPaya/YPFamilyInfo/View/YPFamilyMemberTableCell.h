//
//  YPFamilyMemberTableCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

UIKIT_EXTERN NSString *const HJFamilyMemberTableCellID;

@class YPFamilyInfoDetail;

@interface YPFamilyMemberTableCell : UITableViewCell

- (void)configureWithMemberInfo:(YPFamilyInfoDetail *)memberInfo;

@end
NS_ASSUME_NONNULL_END
