//
//  HJLiveGiftShowModel.m
//  LiveSendGift
//
//  Created by Jonhory on 2016/11/11.
//  Copyright © 2016年 com.wujh. All rights reserved.
//

#import "HJLiveGiftShowModel.h"

@implementation HJLiveGiftShowModel

+ (instancetype)giftModel:(HJLiveGiftListModel *)giftModel userModel:(HJLiveUserModel *)userModel{
    HJLiveGiftShowModel * model = [[HJLiveGiftShowModel alloc]init];
    model.giftModel = giftModel;
    model.user = userModel;
    return model;
}


@end
