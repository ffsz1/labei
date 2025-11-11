//
//  XCFamilyManageJoinWayModel.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyManageJoinWayModel.h"

@implementation HJFamilyManageJoinWayModel

+ (instancetype)itemWithType:(XCFamilyJoinWayType)type selected:(BOOL)selected {
    HJFamilyManageJoinWayModel *item = [HJFamilyManageJoinWayModel new];
    NSString *title = nil;
    switch (type) {
        case XCFamilyJoinWayTypeNormal:
        {
            title = @"无需验证";
        }
            break;
        case XCFamilyJoinWayTypeSH :
        {
            title = @" 通过";
        }
            break;
    }
    item.type = type;
    item.title = title;
    item.selected = selected;
    return item;
}

@end
