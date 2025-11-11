//
//  YPRechargeInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPBaseObject.h"

@interface YPRechargeInfo : YPBaseObject
@property(nonatomic, strong) NSString *chargeProdId;
@property(nonatomic, strong) NSString *prodName;
@property (copy, nonatomic) NSString *prodDesc;
@property(nonatomic, strong) NSNumber *money;
@property(nonatomic, strong) NSNumber *giftGoldNum;
@property(nonatomic, strong) NSString *channel;
@end
