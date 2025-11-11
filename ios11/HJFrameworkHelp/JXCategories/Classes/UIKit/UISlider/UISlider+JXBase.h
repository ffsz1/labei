//
//  UISlider+JXBase.h
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UISlider (JXBase)

#pragma mark - Base
@property (nonatomic, readonly) CGRect jx_trackRect; ///< 获取Slider的Track图片的Rect, Rect根据Slider的Bounds计算
@property (nonatomic, readonly) CGRect jx_thumbRect; ///< 获取Slider的Thumb图片的Rect, Rect根据Slider的Bounds计算

@end

NS_ASSUME_NONNULL_END
