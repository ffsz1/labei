//
//  NSValue+JXBase.h
//  JXCategories
//
//  Created by Colin on 17/1/3.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

/**
 获取location及length为0的NSRange。等同于NSMakeRange(0, 0)
 */
FOUNDATION_EXTERN const NSRange JXNSRangeZero;

/**
 range1是否包含range2内

 @param range1 Range1
 @param range2 Range2
 @return 包含返回YES, 否则返回NO
 */
NS_INLINE BOOL JXNSRangeInRange(NSRange range1, NSRange range2) {
    return (NSLocationInRange(range2.location, range1) && range2.length <= (range1.length - range2.location)) ? YES : NO;
}

/**
 将CFRange转换为NSRange

 @param range CFRange
 @return NSRange
 */
NS_INLINE NSRange JXNSRangeFromCFRange(CFRange range) {
    return NSMakeRange(range.location, range.length);
}

/**
 将NSRange转换为CFRange

 @param range NSRange
 @return CFRange
 */
NS_INLINE CFRange JXCFRangeFromNSRange(NSRange range) {
    return CFRangeMake(range.location, range.length);
}

@interface NSValue (JXBase)

#pragma mark - Base
/**
 根据CGColorRef, 创建NSValue对象(通过jx_CGColorValue获取)

 @param color CGColorRef
 @return NSValue对象
 */
+ (NSValue *)jx_valueWithCGColor:(CGColorRef)color;

/**
 获取NSValue对象内的CGColorRef

 @return CGColorRef
 */
- (CGColorRef)jx_CGColorValue;

@end

NS_ASSUME_NONNULL_END
