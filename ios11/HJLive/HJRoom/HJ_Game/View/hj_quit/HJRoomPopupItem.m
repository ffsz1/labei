//
//  HJRoomPopupItem.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomPopupItem.h"

@implementation HJRoomPopupItem

+ (instancetype)ItemWithTitle:(NSString *)title icon:(NSString *)icon action:(roomPopupItemBlcok)action {
    HJRoomPopupItem *item = [[HJRoomPopupItem alloc] init];
    item.title = title;
    item.icon = icon;
    item.blockAction = action;
    return item;
}

@end
