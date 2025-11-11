//
//  YPRoomBackgroundSettingVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

typedef void(^HJRoomBackgroundSettingVCDidSelectedItemHandler)(NSInteger itemId);

@interface YPRoomBackgroundSettingVC : YPBaseViewController

@property (nonatomic, assign) NSInteger itemId;

@property (nonatomic, copy) HJRoomBackgroundSettingVCDidSelectedItemHandler didSelectedItemHandler;

@end

NS_ASSUME_NONNULL_END
