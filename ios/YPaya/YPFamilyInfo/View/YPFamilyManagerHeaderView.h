//
//  YPFamilyManagerHeaderView.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

UIKIT_EXTERN NSString *const HJFamilyManagerHeaderViewID;

@interface YPFamilyManagerHeaderView : UITableViewHeaderFooterView

- (void)configureWithFamilyLogo:(NSString *)familyLogo
                     familyName:(NSString *)familyName
                       familyId:(NSString *)familyId
                           time:(NSTimeInterval)time;

@end

NS_ASSUME_NONNULL_END
