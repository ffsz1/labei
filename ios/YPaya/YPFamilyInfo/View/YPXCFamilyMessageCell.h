//
//  YPXCFamilyMessageCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "YPFamilyMessage.h"

@interface YPXCFamilyMessageCell : UITableViewCell

@property (nonatomic, copy) void(^agreeBtnActionBlock)();
@property (nonatomic, copy) void(^refuseBtnActionBlock)();

@property (nonatomic, strong) YPFamilyMessage *model;

@end
