//
//  HJFamilyAlertView.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^HJFamilyAlertViewDidTapCancelHandler)(void);
typedef void(^HJFamilyAlertViewDidTapActionHandler)(void);

@interface HJFamilyAlertView : UIView

@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *message;
@property (nonatomic, copy) NSString *actionTitle;
@property (nonatomic, copy) HJFamilyAlertViewDidTapCancelHandler didTapCancelHandler;
@property (nonatomic, copy) HJFamilyAlertViewDidTapActionHandler didTapActionHandler;

@end
NS_ASSUME_NONNULL_END
