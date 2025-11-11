//
//  UITextField+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/10.
//
//

#import "UITextField+JXBase.h"

#import <objc/objc.h>
#import <objc/runtime.h>

@interface UITextField ()

@property (nonatomic, strong) UILabel *_jx_ui_placeholderLabel;

@end

@implementation UITextField (JXBase)

static NSString *kLimitTextLengthKey = @"JXLimitTextLengthKey";

#pragma mark - Base
- (UIButton *)jx_clearButton {
    return [self valueForKey:@"clearButton"];
}

- (void)setJx_clearButtonImage:(UIImage *)jx_clearButtonImage {
    [self.jx_clearButton setImage:jx_clearButtonImage forState:UIControlStateNormal];
}

- (UIImage *)jx_clearButtonImage {
    return [self.jx_clearButton imageForState:UIControlStateNormal];
}

- (void)setJx_selectedRange:(NSRange)range {
    UITextPosition *beginning = self.beginningOfDocument; // (0-3)
    UITextPosition *startPosition = [self positionFromPosition:beginning offset:range.location]; // (0-1)
    UITextPosition *endPosition = [self positionFromPosition:beginning offset:NSMaxRange(range)]; // (0-2)
    UITextRange *selectionRange = [self textRangeFromPosition:startPosition toPosition:endPosition]; // (1-2)
    [self setSelectedTextRange:selectionRange];
}

- (NSRange)jx_selectedRange {
    NSInteger location = [self offsetFromPosition:self.beginningOfDocument toPosition:self.selectedTextRange.start];
    NSInteger length = [self offsetFromPosition:self.selectedTextRange.start toPosition:self.selectedTextRange.end];
    return NSMakeRange(location, length);
}

- (void)jx_selectAllText {
    UITextRange *range = [self textRangeFromPosition:self.beginningOfDocument toPosition:self.endOfDocument];
    [self setSelectedTextRange:range];
}

- (void)jx_limitMaxLength:(int)length {
    objc_setAssociatedObject(self, (const void *)CFBridgingRetain(kLimitTextLengthKey), [NSNumber numberWithInt:length], OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    
    [self addTarget:self action:@selector(_jx_textFieldTextLengthLimit:) forControlEvents:UIControlEventEditingChanged];
}

- (void)_jx_textFieldTextLengthLimit:(id)sender {
    NSNumber *lengthNumber = objc_getAssociatedObject(self, (const void *)CFBridgingRetain(kLimitTextLengthKey));
    int maxLength = [lengthNumber intValue];
    NSString *toBeString = self.text;
    NSString *lang = self.textInputMode.primaryLanguage; // 键盘输入模式
    if ([lang isEqualToString:@"zh-Hans"] || [lang isEqualToString:@"zh-Hant"]) { // 简繁体中文
        UITextRange *selectedRange = [self markedTextRange];
        //获取高亮部分
        UITextPosition *position = [self positionFromPosition:selectedRange.start offset:0];
        // 没有高亮选择的字，则对已输入的文字进行字数统计和限制
        if (!position){// || location >= maxLength) {
            if (toBeString.length > maxLength) {
                self.text = [toBeString substringToIndex:maxLength];
            }
        }
    } else {
        // 中文输入法以外的直接对其统计限制即可，不考虑其他语种情况
        if (toBeString.length > maxLength && self.markedTextRange == nil) {
            //用字符串的字符编码指定索引查找位置
            NSRange rangeIndex = [toBeString rangeOfComposedCharacterSequenceAtIndex:maxLength];
            if (rangeIndex.length == 1) {
                self.text = [toBeString substringToIndex:maxLength];
            } else {
                //用字符串的字符编码指定区域段查找位置
                self.text = [toBeString substringWithRange:NSMakeRange(0, toBeString.length - rangeIndex.length)];
            }
        }
    }
}

- (BOOL)shouldChangeTextInRange:(UITextRange *)range replacementText:(NSString *)text {
    [self _jx_textFieldTextLengthLimit:nil];
    return YES;
}

- (UILabel *)_jx_placeholderLabel {
    return [self valueForKey:@"_placeholderLabel"];
}

- (void)setJx_adjustsPlaceholderFontSizeToFitWidth:(BOOL)jx_adjustsPlaceholderFontSizeToFitWidth {
    self._jx_placeholderLabel.adjustsFontSizeToFitWidth = jx_adjustsPlaceholderFontSizeToFitWidth;
}

- (BOOL)jx_adjustsPlaceholderFontSizeToFitWidth {
    return self._jx_placeholderLabel.adjustsFontSizeToFitWidth;
}

@end
