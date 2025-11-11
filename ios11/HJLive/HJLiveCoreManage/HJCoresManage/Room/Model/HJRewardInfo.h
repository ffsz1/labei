//
//  HJRewardInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"

@interface HJRewardInfo : BaseObject
@property(nonatomic, strong) NSString *rewardId;
@property(nonatomic, assign) UserID uid;
@property(nonatomic, assign) NSInteger rewardMoney;
@property(nonatomic, assign) NSInteger servDura;
@end
