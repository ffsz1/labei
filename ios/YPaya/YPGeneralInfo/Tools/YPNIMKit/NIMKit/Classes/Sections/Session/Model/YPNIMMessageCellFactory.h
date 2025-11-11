//
//  NIMMessageCellMaker.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPNIMMessageCell.h"
#import "YPNIMSessionTimestampCell.h"
#import "NIMCellConfig.h"
#import "NIMMessageCellProtocol.h"

@interface YPNIMMessageCellFactory : NSObject

- (YPNIMMessageCell *)cellInTable:(UITableView*)tableView
                 forMessageMode:(YPNIMMessageModel *)model;

- (YPNIMSessionTimestampCell *)cellInTable:(UITableView *)tableView
                            forTimeModel:(YPNIMTimestampModel *)model;

@end
