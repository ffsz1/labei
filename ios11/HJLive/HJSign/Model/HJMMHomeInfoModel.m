//
//  HJMMHomeInfoModel.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMMHomeInfoModel.h"

@implementation HJMMHomeInfoModel

+ (nullable NSDictionary<NSString *, id> *)modelContainerPropertyGenericClass {
    return @{
             @"beginnerMissions" : [HJMMHomeItemModel class],
             @"dailyMissions" : [HJMMHomeItemModel class],
             @"weeklyMissions" : [HJMMHomeItemModel class],
             };
}

@end
