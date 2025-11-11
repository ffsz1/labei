//
//  NSIndexPath+JXBase.m
//  JXCategories
//
//  Created by Colin on 17/1/4.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "NSIndexPath+JXBase.h"

@implementation NSIndexPath (JXBase)

#pragma mark - Base
- (BOOL)jx_isEqualToIndexPath:(NSIndexPath *)other {
    if (!other) return NO;
    if (![other isKindOfClass:[NSIndexPath class]]) return NO;
    if (self == other) return YES;
    
    return [self compare:other] == NSOrderedSame;
}

@end
