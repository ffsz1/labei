//
//  YPFaceInfo.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFaceInfo.h"

@implementation YPFaceInfo

+ (nullable NSDictionary<NSString *, id> *)modelContainerPropertyGenericClass{
    return @{
             @"children"       : YPFaceInfo.class,
             };
}

@end
