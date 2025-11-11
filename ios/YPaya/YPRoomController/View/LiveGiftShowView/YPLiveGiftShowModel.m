//
//  YPLiveGiftShowModel.m
//  LiveSendGift
//
//  Created by Jonhory on 2016/11/11.
//  Copyright © 2016年 com.wujh. All rights reserved.
//

#import "YPLiveGiftShowModel.h"

@implementation YPLiveGiftShowModel

+ (instancetype)giftModel:(YPLiveGiftListModel *)giftModel userModel:(YPLiveUserModel *)userModel{
    YPLiveGiftShowModel * model = [[YPLiveGiftShowModel alloc]init];
    model.giftModel = giftModel;
    model.user = userModel;
    return model;
}


@end
