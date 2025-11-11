//
//  JXCoreGraphicHelper.h
//  Pods
//
//  Created by Colin on 17/1/9.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import <Foundation/Foundation.h>

#if defined(__LP64__) && __LP64__
# define JXCGFLOAT_EPSILON DBL_EPSILON
#else
# define JXCGFLOAT_EPSILON FLT_EPSILON
#endif

typedef NS_ENUM(NSInteger, JXCGPointQuadrant) { ///< 点相对矩形中心点所处的象限
    JXCGPointQuadrantOrigin = 0,                ///< 原点
    JXCGPointQuadrantFirst  = 1,                ///< 第一象限
    JXCGPointQuadrantSecond = 2,                ///< 第二象限
    JXCGPointQuadrantThird  = 3,                ///< 第三象限
    JXCGPointQuadrantForth  = 4,                ///< 第四象限
};

#pragma mark - Info
/**
 获取屏幕的Scale
 
 @return 屏幕的Scale
 */
CGFloat JXScreenScale(void);


/**
 获取屏幕的尺寸
 
 @return 屏幕的尺寸
 */
CGSize JXScreenSize(void);

#pragma mark - Utilities
/**
 判断两个CGFloat值是否相等

 @param float1 float1
 @param float2 float2
 @return 相等返回YES, 否则返回NO
 */
CG_INLINE BOOL JXCGFloatEqualeToFloat(CGFloat float1, CGFloat float2) {
    return float1 == float2 || fabs(float1 - float2) < __FLT_EPSILON__;
}

/**
 将角度值转换为弧度值

 @param degrees 角度值
 @return 弧度值
 */
CG_INLINE CGFloat JXDegreesToRadians(CGFloat degrees) {
    return degrees * M_PI / 180;
}

/**
 将弧度值转换为角度值

 @param radians 弧度值
 @return 角度值
 */
CG_INLINE CGFloat JXRadiansToDegrees(CGFloat radians) {
    return radians * 180 / M_PI;
}

/**
 反转UIEdgeInsets

 @param insets 原UIEdgeInsets
 @return 反转后的UIEdgeInsets
 */
CG_INLINE UIEdgeInsets JXUIEdgeInsetsInvert(UIEdgeInsets insets) {
    return UIEdgeInsetsMake(-insets.top, -insets.left, -insets.bottom, -insets.right);
}

/**
 获取UIEdgeInsets在水平方向的值
 
 @param insets UIEdgeInsets
 @return UIEdgeInsets在水平方向的值
 */
CG_INLINE CGFloat JXUIEdgeInsetsGetValuesInHorizontal(UIEdgeInsets insets) {
    return insets.left + insets.right;
}

/**
 获取UIEdgeInsets在垂直方向的值

 @param insets UIEdgeInsets
 @return UIEdgeInsets在垂直方向的值
 */
CG_INLINE CGFloat JXUIEdgeInsetsGetValuesInVertical(UIEdgeInsets insets) {
    return insets.top + insets.bottom;
}

/**
 判断尺寸是否为空(长或宽小于等于0)
 
 @param size 尺寸
 @return 为空返回YES, 否则返回NO(长或宽小于等于0)
 */
CG_INLINE BOOL JXCGSizeIsEmpty(CGSize size) {
    return size.width <= 0 || size.height <= 0;
}


/**
 根据尺寸创建CGRect

 @param size 尺寸
 @return CGRect
 */
CG_INLINE CGRect JXCGRectMakeWithSize(CGSize size) {
    return CGRectMake(0, 0, size.width, size.height);
}

/**
 设置Rect的X值

 @param rect Rect
 @param x X值
 @return CGRect
 */
CG_INLINE CGRect JXCGRectSetX(CGRect rect, CGFloat x) {
    rect.origin.x = x;
    return rect;
}

/**
 设置Rect的Y值

 @param rect Rect
 @param y Y值
 @return CGRect
 */
CG_INLINE CGRect JXCGRectSetY(CGRect rect, CGFloat y) {
    rect.origin.y = y;
    return rect;
}

/**
 设置Rect的宽度

 @param rect Rect
 @param width 宽度
 @return CGRect
 */
CG_INLINE CGRect JXCGRectSetWidth(CGRect rect, CGFloat width) {
    rect.size.width = width;
    return rect;
}

/**
 设置Rect的高度

 @param rect Rect
 @param height 宽度
 @return CGRect
 */
CG_INLINE CGRect JXCGRectSetHeight(CGRect rect, CGFloat height) {
    rect.size.height = height;
    return rect;
}

/**
 根据宽度偏移量及高度偏移量, 伸缩Rect

 @param rect Rect
 @param widthOffset 宽度偏移量
 @param heightOffset 高度偏移量
 @return CGRect
 */
CG_INLINE CGRect JXCGRectStretch(CGRect rect, CGFloat widthOffset, CGFloat heightOffset) {
    rect.size.width += widthOffset;
    rect.size.height += heightOffset;
    return rect;
}

/**
 判断Rect是否包含特定的Point(类似`CGRectContainsPoint()`, 边界点亦判断包含)

 @param rect Rect
 @param point Point
 @return 包含返回YES, 否则返回NO
 */
CG_INLINE BOOL JXCGRectContainsPoint(CGRect rect, CGPoint point) {
   return point.x <= CGRectGetMaxX(rect) && point.x >= CGRectGetMinX(rect) && point.y <= CGRectGetMaxY(rect) && point.y >= CGRectGetMinY(rect);
}

/**
 获取矩形的中心点

 @param rect 矩形
 @return 矩形rect的中心点
 */
CG_INLINE CGPoint JXCGRectGetCenter(CGRect rect) {
    return CGPointMake(CGRectGetMidX(rect), CGRectGetMidY(rect));
}

/**
 获取矩形的面积

 @param rect 矩形
 @return 矩形的面积
 */
CG_INLINE CGFloat JXCGRectGetArea(CGRect rect) {
    if (CGRectIsNull(rect)) return 0;
    rect = CGRectStandardize(rect); // 根据一个矩形创建一个标准的矩形
    return rect.size.width * rect.size.height;
}

/**
 判断矩形是否为正方形

 @param rect 矩形
 @return 是返回YES, 否则返回NO
 */
CG_INLINE BOOL JXCGRectIsSquare(CGRect rect) {
    if (CGRectIsNull(rect)) return NO;
    if (CGRectIsEmpty(rect)) return NO;
    
    rect = CGRectStandardize(rect);
    return rect.size.width == rect.size.height;
}

/**
 获取矩形中间的正方形(无法获取, 返回CGRectZero)

 @param rect 矩形
 @return 正方形的矩形(无法获取, 返回CGRectZero)
 */
CG_INLINE CGRect JXCGRectGetCenterSquareRect(CGRect rect) {
    if (CGRectIsNull(rect)) return CGRectZero;
    if (CGRectIsEmpty(rect)) return CGRectZero;
    
    rect = CGRectStandardize(rect);
    CGFloat rectX = rect.origin.x, rectY = rect.origin.y, rectWidth = rect.size.width, rectHeight = rect.size.height;
    
    if (rectWidth > rectHeight) {
        rectX = (rectWidth - rectHeight)/2;
        rectWidth = rectHeight;
    } else if (rectWidth < rectHeight) {
        rectY = (rectHeight - rectWidth)/2;
        rectHeight = rectWidth;
    }
    
    return CGRectMake(rectX, rectY, rectWidth, rectHeight);
}

/**
 获取两点的直线距离

 @param point1 point1
 @param point2 point2
 @return 两点的直线距离
 */
CG_INLINE CGFloat JXCGPointGetDistanceToPoint(CGPoint point1, CGPoint point2) {
    return sqrt((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));
}

/**
 获取点到矩形的最小距离(垂直/水平)

 @param point 点
 @param rect 矩形
 @return 点到矩形的最小距离
 */
CG_INLINE CGFloat JXCGPointGetDistanceToRect(CGPoint point, CGRect rect) {
    rect = CGRectStandardize(rect);
    if (CGRectContainsPoint(rect, point)) return 0; //  Rect是否包含Point
    CGFloat distV, distH; // 垂直距离, 水平距离
    // Point in [rect.origin.y, rect.origin.y + rect.size.height]
    if (CGRectGetMinY(rect) <= point.y && point.y <= CGRectGetMaxY(rect)) {
        distV = 0;
    } else {
        distV = point.y < CGRectGetMinY(rect) ? CGRectGetMinY(rect) - point.y : point.y - CGRectGetMaxY(rect); // Rect上/下
    }
    
    // Point in [rect.origin.x, rect.origin.x + rect.size.width]
    if (CGRectGetMinX(rect) <= point.x && point.x <= CGRectGetMaxX(rect)) {
        distH = 0;
    } else {
        distH = point.x < CGRectGetMinX(rect) ? CGRectGetMinX(rect) - point.x : point.x - CGRectGetMaxX(rect); // Rect左/右
    }
    return MAX(distV, distH); // (0, 10) -> 10; (10, 20) -> 20
}

/**
 获取某点相对于矩形中心点所处的象限

 @param point 点
 @param rect 矩形
 @return 点相对于矩形中心点所处的象限
 */
CG_INLINE JXCGPointQuadrant JXCGPointQuadrantInRect(CGPoint point, CGRect rect) {
    CGPoint mid = JXCGRectGetCenter(rect);
    if (point.x > mid.x) {
        if (point.y < mid.y) return JXCGPointQuadrantFirst;
        if (point.y > mid.y) return JXCGPointQuadrantForth;
    }

    if (point.x < mid.x) {
        if (point.y < mid.y) return JXCGPointQuadrantSecond;
        if (point.y > mid.y) return JXCGPointQuadrantThird;
    }
    return JXCGPointQuadrantOrigin;
}

#pragma mark - Point & Pixel
/**
 将PT值转为像素值

 @param value PT值
 @return 像素值
 */
CG_INLINE CGFloat JXCGFloatToPixel(CGFloat value) {
    return value * JXScreenScale();
}

/**
 将像素值转为PT值

 @param value 像素值
 @return PT值
 */
CG_INLINE CGFloat JXCGFloatFromPixel(CGFloat value) {
    return value / JXScreenScale();
}

/**
 向下取整像素值对应的PT值(像素对齐)

 @param value 像素值
 @return PT值(像素对齐)
 */
CG_INLINE CGFloat JXCGFloatPixelFloor(CGFloat value) {
    CGFloat scale = JXScreenScale();
    return floor(value * scale) / scale;
}

/**
 四舍五入像素值对应的PT值(像素对齐)

 @param value 像素值
 @return PT值(像素对齐)
 */
CG_INLINE CGFloat JXCCGFloatPixelRound(CGFloat value) {
    CGFloat scale = JXScreenScale();
    return round(value * scale) / scale;
}

/**
 向上取整像素值对应的PT值(像素对齐)

 @param value 像素值
 @return PT值(像素对齐)
 */
CG_INLINE CGFloat JXCGFloatPixelCeil(CGFloat value) {
    CGFloat scale = JXScreenScale();
    return ceil(value * scale) / scale;
}

/**
 round point value to .5 pixel for path stroke (odd pixel line width pixel-aligned)

 @param value 像素值
 @return PT值(像素对齐)
 */
CG_INLINE CGFloat JXCCGFloatPixelHalf(CGFloat value) {
    CGFloat scale = JXScreenScale();
    return (floor(value * scale) + 0.5) / scale;
}


/**
 floor point value for pixel-aligned

 @param point CGPoint 像素值
 @return CGPoint PT值(像素对齐)
 */
CG_INLINE CGPoint JXCGPointPixelFloor(CGPoint point) {
    CGFloat scale = JXScreenScale();
    return CGPointMake(floor(point.x * scale) / scale,
                       floor(point.y * scale) / scale);
}

/**
 round point value for pixel-aligned

 @param point CGPoint 像素值
 @return PT值(像素对齐)
 */
CG_INLINE CGPoint JXCGPointPixelRound(CGPoint point) {
    CGFloat scale = JXScreenScale();
    return CGPointMake(round(point.x * scale) / scale,
                       round(point.y * scale) / scale);
}

/**
 ceil point value for pixel-aligned

 @param point CGPoint 像素值
 @return PT值(像素对齐)
 */
CG_INLINE CGPoint JXCGPointPixelCeil(CGPoint point) {
    CGFloat scale = JXScreenScale();
    return CGPointMake(ceil(point.x * scale) / scale,
                       ceil(point.y * scale) / scale);
}

/**
 round point value to .5 pixel for path stroke (odd pixel line width pixel-aligned)

 @param point CGPoint 像素值
 @return PT值(像素对齐)
 */
CG_INLINE CGPoint JXCGPointPixelHalf(CGPoint point) {
    CGFloat scale = JXScreenScale();
    return CGPointMake((floor(point.x * scale) + 0.5) / scale,
                       (floor(point.y * scale) + 0.5) / scale);
}

/// floor point value for pixel-aligned
CG_INLINE CGSize JXCGSizePixelFloor(CGSize size) {
    CGFloat scale = JXScreenScale();
    return CGSizeMake(floor(size.width * scale) / scale,
                      floor(size.height * scale) / scale);
}

/**
 round point value for pixel-aligned

 @param size CGSize 像素值
 @return CGSize PT值(像素对齐)
 */
CG_INLINE CGSize JXCGSizePixelRound(CGSize size) {
    CGFloat scale = JXScreenScale();
    return CGSizeMake(round(size.width * scale) / scale,
                      round(size.height * scale) / scale);
}

/**
 ceil point value for pixel-aligned

 @param size CGSize 像素值
 @return CGSize PT值(像素对齐)
 */
CG_INLINE CGSize JXCGSizePixelCeil(CGSize size) {
    CGFloat scale = JXScreenScale();
    return CGSizeMake(ceil(size.width * scale) / scale,
                      ceil(size.height * scale) / scale);
}

/**
 round point value to .5 pixel for path stroke (odd pixel line width pixel-aligned)

 @param size CGSize 像素值
 @return CGSize PT值(像素对齐)
 */
CG_INLINE CGSize JXCGSizePixelHalf(CGSize size) {
    CGFloat scale = JXScreenScale();
    return CGSizeMake((floor(size.width * scale) + 0.5) / scale,
                      (floor(size.height * scale) + 0.5) / scale);
}

/**
 floor point value for pixel-aligned

 @param rect CGRect 像素值
 @return CGRect PT值(像素对齐)
 */
CG_INLINE CGRect JXCGRectPixelFloor(CGRect rect) {
    CGPoint origin = JXCGPointPixelCeil(rect.origin);
    CGPoint corner = JXCGPointPixelFloor(CGPointMake(rect.origin.x + rect.size.width,
                                                   rect.origin.y + rect.size.height));
    CGRect ret = CGRectMake(origin.x, origin.y, corner.x - origin.x, corner.y - origin.y);
    if (ret.size.width < 0) ret.size.width = 0;
    if (ret.size.height < 0) ret.size.height = 0;
    return ret;
}

/**
 round point value for pixel-aligned

 @param rect CGRect 像素值
 @return CGRect PT值(像素对齐)
 */
CG_INLINE CGRect JXCGRectPixelRound(CGRect rect) {
    CGPoint origin = JXCGPointPixelRound(rect.origin);
    CGPoint corner = JXCGPointPixelRound(CGPointMake(rect.origin.x + rect.size.width,
                                                   rect.origin.y + rect.size.height));
    return CGRectMake(origin.x, origin.y, corner.x - origin.x, corner.y - origin.y);
}

/**
 ceil point value for pixel-aligned

 @param rect CGRect 像素值
 @return CGRect PT值(像素对齐)
 */
CG_INLINE CGRect JXCGRectPixelCeil(CGRect rect) {
    CGPoint origin = JXCGPointPixelFloor(rect.origin);
    CGPoint corner = JXCGPointPixelCeil(CGPointMake(rect.origin.x + rect.size.width,
                                                  rect.origin.y + rect.size.height));
    return CGRectMake(origin.x, origin.y, corner.x - origin.x, corner.y - origin.y);
}

/**
 round point value to .5 pixel for path stroke (odd pixel line width pixel-aligned)

 @param rect CGRect 像素值
 @return CGRect PT值(像素对齐)
 */
CG_INLINE CGRect JXCGRectPixelHalf(CGRect rect) {
    CGPoint origin = JXCGPointPixelHalf(rect.origin);
    CGPoint corner = JXCGPointPixelHalf(CGPointMake(rect.origin.x + rect.size.width,
                                                  rect.origin.y + rect.size.height));
    return CGRectMake(origin.x, origin.y, corner.x - origin.x, corner.y - origin.y);
}

/**
 floor UIEdgeInset for pixel-aligned

 @param insets UIEdgeInset 像素值
 @return UIEdgeInset PT值(像素对齐)
 */
CG_INLINE UIEdgeInsets JXUIEdgeInsetPixelFloor(UIEdgeInsets insets) {
    insets.top = JXCGFloatPixelFloor(insets.top);
    insets.left = JXCGFloatPixelFloor(insets.left);
    insets.bottom = JXCGFloatPixelFloor(insets.bottom);
    insets.right = JXCGFloatPixelFloor(insets.right);
    return insets;
}

/**
 ceil UIEdgeInset for pixel-aligned

 @param insets UIEdgeInset 像素值
 @return UIEdgeInset PT值(像素对齐)
 */
CG_INLINE UIEdgeInsets JXUIEdgeInsetPixelCeil(UIEdgeInsets insets) {
    insets.top = JXCGFloatPixelCeil(insets.top);
    insets.left = JXCGFloatPixelCeil(insets.left);
    insets.bottom = JXCGFloatPixelCeil(insets.bottom);
    insets.right = JXCGFloatPixelCeil(insets.right);
    return insets;
}

#pragma mark - UIViewContent
/**
 根据CALayer的gravity, 转化为与之对应的UIViewContentMode
 
 @param gravity layer的contentsGravity
 @return 对应的UIViewContentMode
 */
UIViewContentMode JXCAGravityToUIViewContentMode(NSString *gravity);

/**
 根据UIViewContentMode, 转化为与之对应的CALayer的gravity

 @param contentMode UIViewContentMode
 @return 对应的CALayer的contentsGravity
 */
NSString *JXUIViewContentModeToCAGravity(UIViewContentMode contentMode);

/**
 根据contentMode和内容尺寸, 调整并适配绘制Rect

 @param rect 绘制Rect
 @param size 内容尺寸
 @param mode contentMode(UIViewContentModeRedraw == UIViewContentModeScaleToFill)
 @return 调整后的Rect
 */
CGRect JXCGRectFitWithContentMode(CGRect rect, CGSize size, UIViewContentMode mode);
