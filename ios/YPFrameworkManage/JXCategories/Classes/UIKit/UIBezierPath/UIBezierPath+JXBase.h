//
//  UIBezierPath+JXBase.h
//  Pods
//
//  Created by Colin on 17/1/9.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIBezierPath (JXBase)

#pragma mark - Base
/**
 获取矩形圆角的贝塞尔曲线(支持四角定制圆角值及描边)

 @param rect 路径的rect
 @param cornerRadius 圆角值集(顺序为, 左上角、左下角、右下角、右上角)
 @param lineWidth 描边大小(0则不描边)
 @return 矩形圆角的贝塞尔曲线
 */
+ (UIBezierPath *)jx_bezierPathWithRoundedRect:(CGRect)rect
                                  cornerRadius:(NSArray<NSNumber *> *)cornerRadius
                                     lineWidth:(CGFloat)lineWidth;

@end

NS_ASSUME_NONNULL_END
