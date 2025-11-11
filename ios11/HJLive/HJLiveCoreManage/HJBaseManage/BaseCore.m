//
//  BaseCore.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "YYLogger.h"
#import "BaseCore.h"

@implementation BaseCore
- (id)init
{
    if (self = [super init])
    {
        _logger = [YYLogger getYYLogger:[NSString stringWithCString:class_getName(object_getClass(self)) encoding:NSUTF8StringEncoding]];
    }
    return self;
}
@end
