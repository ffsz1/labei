//
//  NSParagraphStyle+JXBase.h
//  Pods
//
//  Created by Colin on 17/1/14.
//
//

#import <UIKit/UIKit.h>
#import <CoreText/CTParagraphStyle.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSParagraphStyle (JXBase)

#pragma mark - Base
/**
 根据CTParagraphStyleRef, 创建NSParagraphStyle
 
 @param CTParagraphStyle CTParagraphStyleRef
 
 @return NSParagraphStyle对象
 */
+ (nullable NSParagraphStyle *)jx_paragraphstyleWithCTParagraphStyle:(CTParagraphStyleRef)CTParagraphStyle;

/**
 创建CTParagraphStyleRef(需调用CFRelease()方法释放)

 @return CTParagraphStyleRef
 */
- (CTParagraphStyleRef)jx_CTParagraphStyle CF_RETURNS_RETAINED;

#pragma mark - Creation
/**
 根据行高(最小及最大)、分割模式, 创建NSParagraphStyle

 @param lineHeight 行高(最小及最大)
 @return NSParagraphStyle对象
 */
+ (instancetype)jx_paragraphStyleWithLineHeight:(CGFloat)lineHeight;

/**
 根据行高(最小及最大)及分割模式, 创建NSParagraphStyle

 @param lineHeight    行高(最小及最大)
 @param lineBreakMode 分割模式
 @return NSParagraphStyle对象
 */
+ (instancetype)jx_paragraphStyleWithLineHeight:(CGFloat)lineHeight lineBreakMode:(NSLineBreakMode)lineBreakMode;

/**
 根据行高(最小及最大)、分割模式及对齐方式, 创建NSParagraphStyle

 @param lineHeight    行高(最小及最大)
 @param lineBreakMode 分割模式
 @param textAlignment 对齐方式
 @return NSParagraphStyle对象
 */
+ (instancetype)jx_paragraphStyleWithLineHeight:(CGFloat)lineHeight
                                  lineBreakMode:(NSLineBreakMode)lineBreakMode
                                  textAlignment:(NSTextAlignment)textAlignment;

@end


@interface NSMutableParagraphStyle (JXBase)

#pragma mark - Base
/**
 添加TabStop

 @param tabStop TabStop
 */
- (void)jx_addTabStop:(NSTextTab *)tabStop;

/**
 移除TabStop

 @param tabStop TabStop
 */
- (void)jx_removeTabStop:(NSTextTab *)tabStop;

/**
 根据指定的ParagraphStyle, 替换子属性

 @param paragraphStyle 指定的ParagraphStyl
 */
- (void)jx_setParagraphStyle:(NSParagraphStyle *)paragraphStyle;

@end

NS_ASSUME_NONNULL_END
