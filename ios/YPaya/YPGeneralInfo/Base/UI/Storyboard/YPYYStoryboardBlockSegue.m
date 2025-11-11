//
//  YPYYStoryboardBlockSegue.m
//  YYMobile
//
//  Created by wuwei on 14/7/4.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YPYYStoryboardBlockSegue.h"

@implementation YPYYStoryboardBlockSegue

- (void)perform
{
    if (self.performBlock)
    {
        self.performBlock(self, self.sourceViewController, self.destinationViewController);
    }
}

@end
