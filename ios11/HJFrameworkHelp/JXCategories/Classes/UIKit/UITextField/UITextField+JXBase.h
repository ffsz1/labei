//
//  UITextField+JXBase.h
//  Pods
//
//  Created by Colin on 17/1/10.
//
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UITextField (JXBase)

#pragma mark - Base
@property (nonatomic, weak, readonly) UIButton *jx_clearButton; ///< 输入框的清除按钮
@property (nullable, nonatomic, strong) UIImage *jx_clearButtonImage; ///< 输入框的清除按钮图片

@property (nonatomic, assign) NSRange jx_selectedRange; ///< 文本的选中范围

@property (nonatomic, assign) BOOL jx_adjustsPlaceholderFontSizeToFitWidth; ///< 占位文本是否根据宽度调整字体

/**
 选中所有的文本
 */
- (void)jx_selectAllText;

/**
 设置输入文本的最大长度

 @param length 最大长度
 */
- (void)jx_limitMaxLength:(int)length;

@end

NS_ASSUME_NONNULL_END
