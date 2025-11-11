//
//  HJNotiFriendVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBaseViewController.h"
#import "YYViewController.h"
#import "ZJScrollPageViewDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJNotiFriendVC : HJBaseViewController <ZJScrollPageViewChildVcDelegate>
- (void)updateData;
@end

NS_ASSUME_NONNULL_END

