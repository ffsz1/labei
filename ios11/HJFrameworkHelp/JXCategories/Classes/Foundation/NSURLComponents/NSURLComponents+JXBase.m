//
//  NSURLComponents+JXBase.m
//  Pods
//
//  Created by Colin on 17/8/8.
//
//

#import "NSURLComponents+JXBase.h"

@implementation NSURLComponents (JXBase)

#pragma mark - Base
- (void)jx_addQueryItem:(NSURLQueryItem *)queryItem {
    if (!queryItem) return;
    
    NSMutableArray *buffer = self.queryItems ? self.queryItems.mutableCopy : @[].mutableCopy;
    [buffer addObject:queryItem];
    self.queryItems = buffer.copy;
}

- (void)jx_addQueryItemWithName:(NSString *)name value:(NSString *)value {
    NSURLQueryItem *queryItem = [NSURLQueryItem queryItemWithName:name value:value];
    [self jx_addQueryItem:queryItem];
}

@end
