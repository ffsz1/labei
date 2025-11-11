//
//  UIView+JXTouchInset.h
//  Pods
//
//  Created by Colin on 17/2/10.
//
//  点击区域

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIView (JXTouchInset)
/**
 扩大视图点击区域(正数->内距; 负数->外边)
 */
@property (nonatomic) UIEdgeInsets jx_touchInset;

@end

NS_ASSUME_NONNULL_END
