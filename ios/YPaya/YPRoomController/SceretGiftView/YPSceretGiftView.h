//
//  YPSceretGiftView.h
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "YPGiftSecretInfo.h"

@interface YPSceretGiftView : UIView

@property (nonatomic, strong) YPGiftSecretInfo *info;

@property (nonatomic, copy) void(^closeActionBlock)();

- (void)showPhoto;

@end
