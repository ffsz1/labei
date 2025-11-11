//
//  UITableViewCell+JXBase.h
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UITableViewCell (JXBase)

#pragma mark - Base
/**
 Cell选中时的背景颜色, SelectionStyle为UITableViewCellSelectionStyleNone时为nil
 */
@property (nullable, nonatomic, readonly) UIColor *jx_selectedBackgroundViewColor;

/**
 设置Cell选中时的背景颜色, SelectionStyle为UITableViewCellSelectionStyleNone时无效

 @param color 背景颜色
 */
- (void)jx_setSelectedBackgroundViewColor:(UIColor *)color;

#pragma mark - Accessory
@property (nonatomic, readonly) UIView *jx_defaultAccessoryView;        ///< 获取系统的默认AccessoryView
@property (nonatomic, readonly) UIView *jx_defaultEditingAccessoryView; ///< 获取编辑状态下系统的默认EditingAccessoryView

/**
 获取当前的AccessoryView(优先级:编辑状态下自定义的EditingAccessoryView -> 编辑状态下系统的默认EditingAccessoryView -> 自定义的AccessoryView -> 系统的默认AccessoryView)
 */
@property (nonatomic, readonly) UIView *jx_currentAccessoryView;

/**
 准备设置Cell的AccessoryDisclosureIndicator的颜色, 在Cell调用"setTintColor:"方法前调用
 Usage:
 
 [cell jx_prepareForAccessoryDisclosureIndicatorColor];
 [cell setTintColor:[UIColor blackColor]];
 */
- (void)jx_prepareForAccessoryDisclosureIndicatorColor;

@end

NS_ASSUME_NONNULL_END
