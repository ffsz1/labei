//
//  YPFamilyAnnouncementViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseViewController.h"
#import "YPFamilyInfoDetail.h"
NS_ASSUME_NONNULL_BEGIN
typedef NS_ENUM(NSInteger, HJFamilyAnnouncementEnterType) { ///< 公告入口
    HJFamilyAnnouncementEnterTypeNormal,                    ///< 普通
    HJFamilyAnnouncementEnterTypeEdit,                      ///< 编辑
};
@interface YPFamilyAnnouncementViewController : YPBaseViewController
@property (nonatomic, copy) NSString *familyName;
@property (nonatomic, copy) NSString *familyId;
@property (nonatomic, copy) NSString *familyLogo;
@property (nonatomic, copy) NSString *familyNotice;
@property (nonatomic, copy) NSString *familyBackground;
@property (nonatomic, assign) HJFamilyAnnouncementEnterType enterType;

@end

NS_ASSUME_NONNULL_END
