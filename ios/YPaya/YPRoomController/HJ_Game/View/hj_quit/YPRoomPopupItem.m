//
//  YPRoomPopupItem.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomPopupItem.h"

@implementation YPRoomPopupItem

+ (instancetype)ItemWithTitle:(NSString *)title icon:(NSString *)icon action:(roomPopupItemBlcok)action {
    YPRoomPopupItem *item = [[YPRoomPopupItem alloc] init];
    item.title = title;
    item.icon = icon;
    item.blockAction = action;
    return item;
}

@end
