//
//  HJLiveGiftShowModel.h
//  LiveSendGift
//
//  Created by Jonhory on 2016/11/11.
//  Copyright © 2016年 com.wujh. All rights reserved.
//  包含用户信息和礼物信息

#import <Foundation/Foundation.h>
#import "HJLiveGiftListModel.h"
#import "HJLiveUserModel.h"


@interface HJLiveGiftShowModel : NSObject

@property (nonatomic ,strong) HJLiveGiftListModel * giftModel;

@property (nonatomic ,strong) HJLiveUserModel * user;

+ (instancetype)giftModel:(HJLiveGiftListModel *)giftModel userModel:(HJLiveUserModel *)userModel;

@end
