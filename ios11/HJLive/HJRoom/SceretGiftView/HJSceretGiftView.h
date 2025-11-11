//
//  HJSceretGiftView.h
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "HJGiftSecretInfo.h"

@interface HJSceretGiftView : UIView

@property (nonatomic, strong) HJGiftSecretInfo *info;

@property (nonatomic, copy) void(^closeActionBlock)();

- (void)showPhoto;

@end
