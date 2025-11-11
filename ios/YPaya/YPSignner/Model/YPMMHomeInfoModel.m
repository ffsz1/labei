//
//  YPMMHomeInfoModel.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMMHomeInfoModel.h"

@implementation YPMMHomeInfoModel

+ (nullable NSDictionary<NSString *, id> *)modelContainerPropertyGenericClass {
    return @{
             @"beginnerMissions" : [YPMMHomeItemModel class],
             @"dailyMissions" : [YPMMHomeItemModel class],
             @"weeklyMissions" : [YPMMHomeItemModel class],
             };
}

@end
