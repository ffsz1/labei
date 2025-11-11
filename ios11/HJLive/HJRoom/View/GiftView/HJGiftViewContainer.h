//
//  HJGiftViewContainer.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GiftInfo.h"
@protocol HJGiftViewContainerDelegate<NSObject>
- (void)onHJGiftViewContainerRechargeClicked;
- (void)onSendGiftClicked:(GiftInfo *)giftInfo;
@end
@interface HJGiftViewContainer : UIView
@property(nonatomic, weak)id<HJGiftViewContainerDelegate>delegate;
+ (instancetype)loadFromNIB;
@end
