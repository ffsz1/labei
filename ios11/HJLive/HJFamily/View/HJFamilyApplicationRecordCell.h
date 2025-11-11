//
//  HJFamilyApplicationRecordCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class HJFamilyMessage;

UIKIT_EXTERN NSString *const HJFamilyApplicationRecordCellID;

typedef void(^HJFamilyApplicationRecordCellDidTapAgreeHandler)(void);
typedef void(^HJFamilyApplicationRecordCellDidTapIgnoreHandler)(void);

@interface HJFamilyApplicationRecordCell : UITableViewCell

@property (nonatomic, copy) HJFamilyApplicationRecordCellDidTapAgreeHandler didTapAgreeHandler;
@property (nonatomic, copy) HJFamilyApplicationRecordCellDidTapIgnoreHandler didTapIgnoreHandler;

- (void)configureWithApplicationInfo:(HJFamilyMessage *)info;

@end
NS_ASSUME_NONNULL_END
