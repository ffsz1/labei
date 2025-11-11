//
//  YPLiveGiftShowModel.h
//  LiveSendGift
//
//  Created by Jonhory on 2016/11/11.
//  Copyright © 2016年 com.wujh. All rights reserved.
//  包含用户信息和礼物信息

#import <Foundation/Foundation.h>
#import "YPLiveGiftListModel.h"
#import "YPLiveUserModel.h"


@interface YPLiveGiftShowModel : NSObject

@property (nonatomic ,strong) YPLiveGiftListModel * giftModel;

@property (nonatomic ,strong) YPLiveUserModel * user;

+ (instancetype)giftModel:(YPLiveGiftListModel *)giftModel userModel:(YPLiveUserModel *)userModel;

@end
