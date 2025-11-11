//
//  YYBaseViewControllerFactory.m
//  YYMobile
//
//  Created by wuwei on 14/6/20.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYStoryboardViewControllerFactory.h"

@implementation YYStoryboardViewControllerFactory

- (instancetype)initWithStoryboard:(UIStoryboard *)storyboard
{
    NSParameterAssert(storyboard);
    
    self = [super init];
    if (self) {
        _storyboard = storyboard;
    }
    return self;
}

@end
