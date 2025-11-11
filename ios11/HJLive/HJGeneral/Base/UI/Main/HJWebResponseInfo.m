//
//  HJWebResponseInfo.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJWebResponseInfo.h"
#import <YYModel/YYModel.h>
@implementation HJWebResponseInfo
+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
             @"id" : @"Id",
             };
}
@end
