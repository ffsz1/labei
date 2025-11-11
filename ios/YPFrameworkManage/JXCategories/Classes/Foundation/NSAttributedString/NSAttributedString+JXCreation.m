//
//  NSAttributedString+JXCreation.m
//  Pods
//
//  Created by Colin on 17/2/10.
//
//

#import "NSAttributedString+JXCreation.h"
#import "NSAttributedString+JXBase.h"
#import "NSValue+JXBase.h"
#import <UIKit/UIKit.h>

@implementation NSAttributedString (JXCreation)

#pragma mark - Creation
+ (NSAttributedString *)jx_attributedStringWithString:(NSString *)string textColor:(UIColor *)textColor {
    return [self jx_attributedStringWithString:string textColor:textColor backgroundColor:nil font:nil paragraphStyle:nil];
}

+ (NSAttributedString *)jx_attributedStringWithString:(NSString *)string backgroundColor:(UIColor *)backgroundColor {
    return [self jx_attributedStringWithString:string textColor:nil backgroundColor:backgroundColor font:nil paragraphStyle:nil];
}

+ (NSAttributedString *)jx_attributedStringWithString:(NSString *)string font:(UIFont *)font {
    return [self jx_attributedStringWithString:string textColor:nil backgroundColor:nil font:font paragraphStyle:nil];
}

+ (NSAttributedString *)jx_attributedStringWithString:(NSString *)string paragraphStyle:(NSParagraphStyle *)paragraphStyle {
    return [self jx_attributedStringWithString:string textColor:nil backgroundColor:nil font:nil paragraphStyle:paragraphStyle];
}

+ (NSAttributedString *)jx_attributedStringWithString:(NSString *)string textColor:(UIColor *)textColor backgroundColor:(UIColor *)backgroundColor font:(UIFont *)font paragraphStyle:(NSParagraphStyle *)paragraphStyle {
    NSMutableAttributedString *mutableAttributedString = [[NSMutableAttributedString alloc] initWithString:string];
    if (textColor) {
        [mutableAttributedString jx_addForegroundColor:textColor];
    }
    if (backgroundColor) {
        [mutableAttributedString jx_addBackgroundColor:backgroundColor];
    }
    if (font) {
        [mutableAttributedString jx_addFont:font];
    }
    if (paragraphStyle) {
        [mutableAttributedString jx_addParagraphStyle:paragraphStyle];
    }
    return mutableAttributedString.copy;
}

#pragma mark - Mask
+ (NSAttributedString *)jx_attributedStringWithString:(NSString *)string textColor:(UIColor *)color font:(UIFont *)font maskRange:(NSRange)maskRange {
    NSMutableDictionary *attributes = @{}.mutableCopy;
    if (color) {
        [attributes setObject:color forKey:NSForegroundColorAttributeName];
    }
    if (font) {
        [attributes setObject:font forKey:NSFontAttributeName];
    }
    return [NSAttributedString jx_attributedStringWithString:string attributes:attributes maskRange:maskRange maskAttributes:@{NSForegroundColorAttributeName:[UIColor clearColor]}];
}

+ (NSAttributedString *)jx_attributedStringWithString:(NSString *)string attributes:(NSDictionary<NSString *, id> *)attrs maskRange:(NSRange)maskRange maskAttributes:(NSDictionary<NSString *, id> *)maskAttrs {
    NSMutableAttributedString *mutableAttributedString = [[NSMutableAttributedString alloc] initWithString:string attributes:attrs];
    
    if (NSEqualRanges(maskRange, JXNSRangeZero)) return mutableAttributedString.copy;
    
    if ([mutableAttributedString jx_containsRange:maskRange]) {
        [mutableAttributedString addAttributes:maskAttrs range:maskRange];
    }
    return mutableAttributedString.copy;
}

#pragma mark - Size
+ (NSAttributedString *)jx_attributedStringWithWidth:(CGFloat)width {
    return [self jx_attributedStringWithSize:CGSizeMake(width, 1)];
}

+ (NSAttributedString *)jx_attributedStringWithHeight:(CGFloat)height {
    return [self jx_attributedStringWithSize:CGSizeMake(1, height)];
}

+ (NSAttributedString *)jx_attributedStringWithSize:(CGSize)size {
    NSTextAttachment *attachment = [[NSTextAttachment alloc] init];
    attachment.bounds = CGRectMake(0, 0, size.width, size.height);
    return [NSAttributedString attributedStringWithAttachment:attachment];
}

#pragma mark - Image
+ (NSAttributedString *)jx_attributedStringWithImage:(UIImage *)image {
    return [self jx_attributedStringWithImage:image baselineOffset:0 leftSpacing:0 rightSpacing:0];
}

+ (NSAttributedString *)jx_attributedStringWithImage:(UIImage *)image baselineOffset:(CGFloat)baselineOffset leftSpacing:(CGFloat)leftSpacing rightSpacing:(CGFloat)rightSpacing {
    if (!image) return nil;
    
    NSTextAttachment *attachment = [[NSTextAttachment alloc] init];
    attachment.image = image;
    attachment.bounds = CGRectMake(0, 0, image.size.width, image.size.height);
    NSMutableAttributedString *mutableAttributedString = [NSAttributedString attributedStringWithAttachment:attachment].mutableCopy;
    [mutableAttributedString jx_addBaselineOffset:@(baselineOffset)];
    if (leftSpacing > 0) {
        [mutableAttributedString insertAttributedString:[self jx_attributedStringWithWidth:leftSpacing] atIndex:0];
    }
    if (rightSpacing > 0) {
        [mutableAttributedString appendAttributedString:[self jx_attributedStringWithWidth:rightSpacing]];
    }
    return mutableAttributedString.copy;
}

@end
