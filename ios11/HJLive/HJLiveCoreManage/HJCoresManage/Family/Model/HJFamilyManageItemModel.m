//
//  XCFamilyManageItemModel.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyManageItemModel.h"

@implementation HJFamilyManageItemModel

+ (instancetype)itemWithType:(XCFamilyManageItemType)type enable:(BOOL)enable {
    HJFamilyManageItemModel *item = [HJFamilyManageItemModel new];
    NSString *title = nil;
    switch (type) {
        case XCFamilyManageItemTypeManageFamily:
        {
            title = @"管理家族";
        }
            break;
        case XCFamilyManageItemTypeApplicationRecord:
        {
            title = @"申请记录";
        }
            break;
        case XCFamilyManageItemTypeExit:
        {
            title = @"退出家族";
        }
            break;
        case XCFamilyManageItemTypeMessageNotice:
        {
            title = @"消息免扰";
        }
            break;
    }
    item.type = type;
    item.title = title;
    item.enable = enable;
    return item;
}

@end
