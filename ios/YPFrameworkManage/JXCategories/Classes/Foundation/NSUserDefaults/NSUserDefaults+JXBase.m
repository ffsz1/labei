//
//  NSUserDefaults+JXBase.m
//  JXCategories
//
//  Created by Colin on 2017/12/6.
//

#import "NSUserDefaults+JXBase.h"
#import "NSDictionary+JXBase.h"

@implementation NSUserDefaults (JXBase)

#pragma mark - Base
- (BOOL)jx_containsKey:(NSString *)key {
    return [[self dictionaryRepresentation] jx_containsKey:key];
}

- (BOOL)jx_containsObjectForKey:(id)key {
    return [[self dictionaryRepresentation] jx_containsObjectForKey:key];
}

@end
