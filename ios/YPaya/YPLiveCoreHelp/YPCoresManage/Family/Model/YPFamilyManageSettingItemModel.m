//
//  XCFamilyManageSettingItemModel.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyManageSettingItemModel.h"

@implementation YPFamilyManageSettingItemModel

+ (instancetype)itemWithType:(XCFamilyManageSettingItemType)type enable:(BOOL)enable {
    YPFamilyManageSettingItemModel *item = [YPFamilyManageSettingItemModel new];
    NSString *title = nil;
    switch (type) {
        case XCFamilyManageSettingItemTypeRemoveMembers:
        {
            title = @"踢出成员";
        }
            break;
        case XCFamilyManageSettingItemTypeSetupManager:
        {
            title = @"设置副族长";
        }
            break;
        case XCFamilyManageSettingItemTypeJoinWay:
        {
            title = @"加入方式";
        }
            break;
    }
    item.type = type;
    item.title = title;
    item.enable = enable;
    return item;
}

@end
