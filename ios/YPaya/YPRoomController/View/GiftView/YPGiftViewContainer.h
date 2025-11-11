//
//  YPGiftViewContainer.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPGiftInfo.h"
@protocol HJGiftViewContainerDelegate<NSObject>
- (void)onHJGiftViewContainerRechargeClicked;
- (void)onSendGiftClicked:(YPGiftInfo *)giftInfo;
@end
@interface YPGiftViewContainer : UIView
@property(nonatomic, weak)id<HJGiftViewContainerDelegate>delegate;
+ (instancetype)loadFromNIB;
@end
