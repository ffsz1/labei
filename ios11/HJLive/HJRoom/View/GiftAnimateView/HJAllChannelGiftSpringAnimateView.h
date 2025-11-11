//
//  HJAllChannelGiftSpringAnimateView.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HJGiftAllMicroSendInfo.h"

@interface HJAllChannelGiftSpringAnimateView : UIView
@property (weak, nonatomic) IBOutlet UIImageView *giftCycleView;
@property (weak, nonatomic) IBOutlet UIImageView *bImageVIew;
@property (strong, nonatomic) HJGiftAllMicroSendInfo *giftReceiveInfo;
@property (weak, nonatomic) IBOutlet UIImageView *gitImageView;
@property (weak, nonatomic) IBOutlet UILabel *countLabel;
@property (weak, nonatomic) IBOutlet UIImageView *senderAvatarView;
@property (weak, nonatomic) IBOutlet UILabel *senderNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *receiverNameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *receiverAvaterView;
@property (weak, nonatomic) IBOutlet UIImageView *sendImageView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *quanmai_width_layout;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *quanmai_height_layout;

@property (weak, nonatomic) IBOutlet UIView *tapView;

+ (instancetype)loadFromNib;
@end
