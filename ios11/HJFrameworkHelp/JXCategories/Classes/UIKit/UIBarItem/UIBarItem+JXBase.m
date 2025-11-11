//
//  UIBarItem+JXBase.m
//  JXCategories
//
//  Created by Colin on 2019/2/13.
//

#import "UIBarItem+JXBase.h"

@implementation UIBarItem (JXBase)

#pragma mark - Base
- (UIView *)jx_view {
    if ([self respondsToSelector:@selector(view)]) {
        return [self valueForKey:@"view"];
    }
    return nil;
}

@end
