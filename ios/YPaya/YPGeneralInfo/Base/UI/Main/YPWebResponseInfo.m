//
//  YPWebResponseInfo.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPWebResponseInfo.h"
#import <YYModel/YYModel.h>
@implementation YPWebResponseInfo
+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
             @"id" : @"Id",
             };
}
@end
