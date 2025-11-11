//
//  UITableViewCell+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import "UITableViewCell+JXBase.h"
#import "NSObject+JXBase.h"

const CGSize JXUITableViewCellAccessoryDisclosureIndicatorBackgroundImageSize = {8, 13};
static const int JX_UI_TABLE_VIEW_CELL_CUSTOM_SELECTED_BACKGROUND_VIEW_KEY;

@interface UITableViewCell ()

@property (nonatomic, strong, setter=_jx_setCustomSelectedBackgroundView:) UIView *_jx_customSelectedBackgroundView;

@end

@implementation UITableViewCell (JXBase)

#pragma mark - Base
- (UIView *)_jx_customSelectedBackgroundView {
    if (self.selectionStyle == UITableViewCellSelectionStyleNone) return nil;
    
    UIView *customSelectedBackgroundView = [self jx_getAssociatedValueForKey:&JX_UI_TABLE_VIEW_CELL_CUSTOM_SELECTED_BACKGROUND_VIEW_KEY];
    if (!customSelectedBackgroundView) {
        customSelectedBackgroundView = [UIView new];
        [self _jx_setCustomSelectedBackgroundView:customSelectedBackgroundView];
    }
    return customSelectedBackgroundView;
}

- (void)_jx_setCustomSelectedBackgroundView:(UIView *)view {
    if (self.selectionStyle == UITableViewCellSelectionStyleNone) return;
    
    [self jx_setAssociatedValue:view withKey:&JX_UI_TABLE_VIEW_CELL_CUSTOM_SELECTED_BACKGROUND_VIEW_KEY];
}

- (UIColor *)jx_selectedBackgroundViewColor {
    if (self.selectionStyle == UITableViewCellSelectionStyleNone) return nil;
    
    return self.selectedBackgroundView.backgroundColor;
}

- (void)jx_setSelectedBackgroundViewColor:(UIColor *)color {
    if (self.selectionStyle == UITableViewCellSelectionStyleNone) return;
    
    self._jx_customSelectedBackgroundView.backgroundColor = color;
    self.selectedBackgroundView = self._jx_customSelectedBackgroundView;
}

#pragma mark - Accessory
- (UIView *)jx_defaultAccessoryView {
    return [self valueForKey:@"_accessoryView"];
}

- (UIView *)jx_defaultEditingAccessoryView {
    return [self valueForKey:@"_editingAccessoryView"];
}

- (UIView *)jx_currentAccessoryView {
    if (self.editing) {
        if (self.editingAccessoryView) return self.editingAccessoryView;
        
        return self.jx_defaultEditingAccessoryView;
    }
    
    if (self.accessoryView) return self.accessoryView;
    
    return self.jx_defaultAccessoryView;
}

- (void)jx_prepareForAccessoryDisclosureIndicatorColor {
    if (self.accessoryType != UITableViewCellAccessoryDisclosureIndicator) return;
    
    for (UIButton *button in self.subviews) {
        if ([button isKindOfClass:[UIButton class]]) {
            UIImage *image = [button backgroundImageForState:UIControlStateNormal];
            if (CGSizeEqualToSize(image.size, JXUITableViewCellAccessoryDisclosureIndicatorBackgroundImageSize)) {
                image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
                [button setBackgroundImage:image forState:UIControlStateNormal];
                return;
            }
        }
    }
}

@end
