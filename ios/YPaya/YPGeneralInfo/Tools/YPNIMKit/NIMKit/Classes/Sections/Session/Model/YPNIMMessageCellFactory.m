//
//  NIMMessageCellMaker.m
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import "YPNIMMessageCellFactory.h"
#import "YPNIMMessageModel.h"
#import "YPNIMTimestampModel.h"
#import "YPNIMSessionAudioContentView.h"
#import "YPNIMKit.h"
#import "YPNIMKitAudioCenter.h"
#import "UIView+NIM.h"

@interface YPNIMMessageCellFactory()

@end

@implementation YPNIMMessageCellFactory

- (instancetype)init
{
    self = [super init];
    if (self) {
    }
    return self;
}

- (void)dealloc
{
    
}

- (YPNIMMessageCell *)cellInTable:(UITableView*)tableView
                 forMessageMode:(YPNIMMessageModel *)model
{
    id<YPNIMCellLayoutConfig> layoutConfig = [[YPNIMKit sharedKit] layoutConfig];
    NSString *identity = [layoutConfig cellContent:model];
    YPNIMMessageCell *cell = [tableView dequeueReusableCellWithIdentifier:identity];
    if (!cell) {
        NSString *clz = @"YPNIMMessageCell";
        [tableView registerClass:NSClassFromString(clz) forCellReuseIdentifier:identity];
        cell = [tableView dequeueReusableCellWithIdentifier:identity];
    }    
    return (YPNIMMessageCell *)cell;
}

- (YPNIMSessionTimestampCell *)cellInTable:(UITableView *)tableView
                            forTimeModel:(YPNIMTimestampModel *)model
{
    NSString *identity = @"time";
    YPNIMSessionTimestampCell *cell = [tableView dequeueReusableCellWithIdentifier:identity];
    if (!cell) {
        NSString *clz = @"YPNIMSessionTimestampCell";
        [tableView registerClass:NSClassFromString(clz) forCellReuseIdentifier:identity];
        cell = [tableView dequeueReusableCellWithIdentifier:identity];
    }
    [cell refreshData:model];
    return (YPNIMSessionTimestampCell *)cell;
}

@end
