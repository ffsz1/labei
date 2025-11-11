//
//  NSPointerArray+JXBase.m
//  JXCategories
//
//  Created by Colin on 2018/6/2.
//

#import "NSPointerArray+JXBase.h"

@implementation NSPointerArray (JXBase)

#pragma mark - Base
- (NSUInteger)jx_indexOfPointer:(nullable void *)pointer {
    if (!pointer) return NSNotFound;
    
    NSPointerArray *array = [self copy];
    for (NSUInteger i = 0; i < array.count; i++) {
        if ([array pointerAtIndex:i] == ((void *)pointer)) return i;
    }
    return NSNotFound;
}

- (BOOL)jx_containsPointer:(nullable void *)pointer {
    if (!pointer) return NO;
    if ([self jx_indexOfPointer:pointer] != NSNotFound) return YES;
    return NO;
}

@end
