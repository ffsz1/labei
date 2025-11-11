//
//  HJFamilyModel.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyModel.h"
#import "UserInfo.h"

@implementation HJFamilyModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"familyUsersDTOS":[UserInfo class]
             };
}

@end
