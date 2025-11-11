//
//  HJIMPublicRoomInfo.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJIMPublicRoomInfo.h"

@implementation HJIMPublicRoomInfo

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"ulist" : [UserInfo class]
             };
}

@end
