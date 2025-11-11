//
//  HJGiftSpringAnimateView.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GiftReceiveInfo.h"
#import "HJGiftAllMicroSendInfo.h"

@interface HJGiftSpringAnimateView : UIView
+ (instancetype)loadFromNib;
@property (strong, nonatomic) HJGiftAllMicroSendInfo *giftReceiveInfo;
@property (weak, nonatomic) IBOutlet UIImageView *giftCycleView;
@end
