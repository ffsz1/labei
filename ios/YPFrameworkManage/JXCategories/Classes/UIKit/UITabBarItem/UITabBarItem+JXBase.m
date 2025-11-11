//
//  UITabBarItem+JXBase.m
//  JXCategories
//
//  Created by colin on 2019/2/16.
//

#import "UITabBarItem+JXBase.h"
#import "UIBarItem+JXBase.h"
#import "UIDevice+JXMachineInfo.h"

@implementation UITabBarItem (JXBase)

#pragma mark - Base
- (UIImageView *)jx_imageView {
    return [self _jx_imageViewInTabBarButton:self.jx_view];
}

- (UIImageView *)_jx_imageViewInTabBarButton:(UIView *)tabBarButton {
    if (!tabBarButton) return nil;
    
    for (UIView *subview in tabBarButton.subviews) {
        if ([NSStringFromClass([subview class]) isEqualToString:@"UITabBarSwappableImageView"]) {
            return (UIImageView *)subview;
        }
        
        if (![UIDevice currentDevice].jx_isiOS10Later) {
            if ([subview isKindOfClass:[UIImageView class]] && ![NSStringFromClass([subview class]) isEqualToString:@"UITabBarSelectionIndicatorView"]) {
                return (UIImageView *)subview;
            }
        }
    }
    return nil;
}

@end
