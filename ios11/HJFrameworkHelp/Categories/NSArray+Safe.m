//
//  NSArray+Safe.m
//  HJLive
//
//  Created by FF on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "NSArray+Safe.h"

@implementation NSArray (Safe)
-(id)safeObjectAtIndex:(NSUInteger)index
{
    if ([self isKindOfClass:[NSArray class]])
    {
        if (self.count)
        {
            if (self.count>index)
            {
                return self[index];
            }
        }
    }

    return nil;
}

@end
