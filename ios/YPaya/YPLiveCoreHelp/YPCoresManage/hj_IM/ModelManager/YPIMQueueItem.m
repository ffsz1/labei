//
//  YPIMQueueItem.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMQueueItem.h"

@implementation YPIMQueueItem

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
             @"position" : @"key",
             @"queueInfo" : @"value",
             };
}

@end
