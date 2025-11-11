//
//  FaceInfo.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "FaceInfo.h"

@implementation FaceInfo

+ (nullable NSDictionary<NSString *, id> *)modelContainerPropertyGenericClass{
    return @{
             @"children"       : FaceInfo.class,
             };
}

@end
