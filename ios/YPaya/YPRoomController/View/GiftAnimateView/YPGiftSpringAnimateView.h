//
//  YPGiftSpringAnimateView.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPGiftReceiveInfo.h"
#import "YPGiftAllMicroSendInfo.h"

@interface YPGiftSpringAnimateView : UIView
+ (instancetype)loadFromNib;
@property (strong, nonatomic) YPGiftAllMicroSendInfo *giftReceiveInfo;
@property (weak, nonatomic) IBOutlet UIImageView *giftCycleView;
@end
