//
//  NSURL+JXBase.m
//  JXCategories
//
//  Created by colin on 2019/2/2.
//

#import "NSURL+JXBase.h"

@implementation NSURL (JXBase)

#pragma mark - Base
- (NSDictionary<NSString *, NSString *> *)jx_queryItems {
    NSMutableDictionary *queryItems = @{}.mutableCopy;
    if (!self.absoluteString.length) return queryItems.copy;
    
    NSURLComponents *URLComponents = [[NSURLComponents alloc] initWithString:self.absoluteString];
    [URLComponents.queryItems enumerateObjectsUsingBlock:^(NSURLQueryItem * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (obj.name) {
            [queryItems setObject:obj.value ?: [NSNull null] forKey:obj.name];
        }
    }];
    return [queryItems copy];
}

@end
