//
//  YPIMPublicRoomInfo.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMPublicRoomInfo.h"

@implementation YPIMPublicRoomInfo

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"ulist" : [UserInfo class]
             };
}

@end
