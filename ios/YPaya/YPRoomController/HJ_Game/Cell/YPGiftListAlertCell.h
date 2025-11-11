//
//  YPGiftListAlertCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "YPGiftReceiveInfo.h"
#import "YPGiftInfo.h"

@interface YPGiftListAlertCell : UITableViewCell

@property (nonatomic, assign) BOOL isAll;
@property (nonatomic, strong) YPGiftReceiveInfo *model;
@property (nonatomic, strong) YPGiftInfo *giftInfo;
@property (nonatomic, copy) NSString *timeString;


@end
