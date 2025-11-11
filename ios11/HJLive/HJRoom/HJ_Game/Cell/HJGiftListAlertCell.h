//
//  HJGiftListAlertCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "GiftReceiveInfo.h"
#import "GiftInfo.h"

@interface HJGiftListAlertCell : UITableViewCell

@property (nonatomic, assign) BOOL isAll;
@property (nonatomic, strong) GiftReceiveInfo *model;
@property (nonatomic, strong) GiftInfo *giftInfo;
@property (nonatomic, copy) NSString *timeString;


@end
