//
//  YYJSONResponseSerializer.m
//  YYMobileCore
//
//  Created by wuwei on 14/6/19.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYJSONResponseSerializer.h"

@implementation YYJSONResponseSerializer

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.acceptableContentTypes = nil;
    }
    return self;
}

@end
