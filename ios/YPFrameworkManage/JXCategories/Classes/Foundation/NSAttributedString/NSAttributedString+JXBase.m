//
//  NSAttributedString+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/12.
//
//

#import "NSAttributedString+JXBase.h"
#import "NSString+JXBase.h"
#import "NSValue+JXBase.h"
#import <UIKit/UIKit.h>
#import <CoreText/CoreText.h>

@implementation NSAttributedString (JXBase)

#pragma mark - Base
- (NSDictionary *)_jx_defaultAttributes {
    static NSDictionary *defaultAttributes = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        NSInteger writingDirectionEmbedding = (0 << 1); // NSTextWritingDirectionEmbedding/NSWritingDirectionEmbedding
        defaultAttributes = @{
                              NSFontAttributeName:[UIFont fontWithName:@"HelveticaNeue" size:12.f],
                              NSParagraphStyleAttributeName:[NSParagraphStyle defaultParagraphStyle],
                              NSForegroundColorAttributeName:[UIColor blackColor],
                              // NSBackgroundColorAttributeName // default nil: no background
                              NSLigatureAttributeName:[NSNumber numberWithInteger:1], // default 1: default ligatures
                              NSKernAttributeName:[NSNumber numberWithFloat:0],
                              NSStrikethroughStyleAttributeName:[NSNumber numberWithInteger:NSUnderlineStyleNone], // default 0: no underline
                              NSUnderlineStyleAttributeName:[NSNumber numberWithInteger:NSUnderlineStyleNone], // default 0: no underline
                              // NSStrokeColorAttributeName // default nil: same as foreground color
                              NSStrokeWidthAttributeName:[NSNumber numberWithFloat:0], // default 0: no stroke
                              // NSShadowAttributeName // default nil
                              // NSTextEffectAttributeName // default nil
                              // NSAttachmentAttributeName // default nil
                              // NSLinkAttributeName // default nil
                              NSBaselineOffsetAttributeName:[NSNumber numberWithFloat:0], // default 0
                              // NSUnderlineColorAttributeName     // default nil: same as foreground color
                              // NSStrikethroughColorAttributeName // default nil: same as foreground color
                              NSObliquenessAttributeName:[NSNumber numberWithFloat:0], // default 0: no skew
                              NSExpansionAttributeName:[NSNumber numberWithFloat:0], // default 0: no expansion
                              NSWritingDirectionAttributeName:@[@(NSWritingDirectionLeftToRight|writingDirectionEmbedding)], // LRE
                              NSVerticalGlyphFormAttributeName:[NSNumber numberWithBool:0] // 0 means horizontal text.
                              };
    });
    return defaultAttributes;
}

- (NSRange)jx_rangeOfAll {
    return NSMakeRange(0, self.length);
}

- (NSUInteger)jx_lengthOfUsingNonASCIICharacterAsTwoEncoding {
    return self.string.jx_lengthOfUsingNonASCIICharacterAsTwoEncoding;
}

- (NSDictionary<NSString *, id> *)jx_attributesAtIndex:(NSUInteger)index {
    if (index >= self.length || self.length == 0) return @{};
    return [self attributesAtIndex:index effectiveRange:NULL];
}

- (NSDictionary<NSString *, id> *)jx_attributesInRange:(NSRange)range {
    NSAssert(JXNSRangeInRange(self.jx_rangeOfAll, range), @"The range, which to search for continuous presence of attributeName, must not exceed the bounds of the receiver.");
    
    __block NSMutableDictionary *attrs = @{}.mutableCopy;
    [self enumerateAttributesInRange:self.jx_rangeOfAll options:kNilOptions usingBlock:^(NSDictionary<NSString *,id> * _Nonnull subAttrs, NSRange range, BOOL * _Nonnull stop) {
        if (attrs) {
            [attrs addEntriesFromDictionary:subAttrs];
        }
    }];
    
    __weak typeof(attrs) weakAttrs = attrs;
    [attrs enumerateKeysAndObjectsWithOptions:NSEnumerationConcurrent usingBlock:^(NSString *attrName, id attr, BOOL * _Nonnull stop) {
        if (![self jx_attribute:attrName inRange:self.jx_rangeOfAll]) {
            [weakAttrs removeObjectForKey:attrName];
        }
    }];
    
    return attrs.copy;
}

- (NSDictionary<NSString *, id> *)jx_attributes {
    return [self jx_attributesInRange:self.jx_rangeOfAll];
}

- (id)jx_attribute:(NSString *)attrName atIndex:(NSUInteger)index {
    if (!attrName) return nil;
    if (index > self.length || self.length == 0) return nil;
    if (self.length > 0 && index == self.length) index--;
    return [self attribute:attrName atIndex:index effectiveRange:NULL];
}

- (id)jx_attribute:(NSString *)attrName inRange:(NSRange)range {
    if (!attrName) return nil;
    NSAssert(JXNSRangeInRange(self.jx_rangeOfAll, range), @"The range, which to search for continuous presence of attributeName, must not exceed the bounds of the receiver.");
    
    NSRange effectiveRange = JXNSRangeZero;
    id attr = [self attribute:attrName atIndex:range.location longestEffectiveRange:&effectiveRange inRange:range];
    if (!attr) return nil;
    if (!NSEqualRanges(effectiveRange, range)) return nil;
    
    return attr;
}

- (id)jx_attribute:(NSString *)attrName {
    return [self jx_attribute:attrName inRange:self.jx_rangeOfAll];
}

#pragma mark - Attributed Substrings

- (NSAttributedString *)jx_attributedSubstringToIndex:(NSUInteger)to {
    NSRange range = NSMakeRange(0, to);
    return [self attributedSubstringFromRange:range];
}

- (NSAttributedString *)jx_attributedSubstringFromIndex:(NSUInteger)from {
    NSRange range = NSMakeRange(from, self.string.length-from);
    return [self attributedSubstringFromRange:range];
}

#pragma mark - Check
- (BOOL)jx_containsRange:(NSRange)range {
    if (!self.string || !self.length) return NO;
    if (!JXNSRangeInRange(self.jx_rangeOfAll, range)) return NO;
    
    return YES;
}

- (BOOL)jx_isSharedAttributesInAllRange {
    __block BOOL isShared = YES;
    __block NSDictionary *firstAttrs = nil;
    [self enumerateAttributesInRange:self.jx_rangeOfAll options:NSAttributedStringEnumerationLongestEffectiveRangeNotRequired usingBlock:^(NSDictionary *attrs, NSRange range, BOOL *stop) {
        if (range.location == 0) {
            firstAttrs = attrs;
        } else {
            if (firstAttrs.count != attrs.count) {
                isShared = NO;
                *stop = YES;
            } else if (firstAttrs) {
                if (![firstAttrs isEqualToDictionary:attrs]) {
                    isShared = NO;
                    *stop = YES;
                }
            }
        }
    }];
    return isShared;
}

#pragma mark - Font
- (UIFont *)jx_fontAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSFontAttributeName atIndex:index];
}

- (UIFont *)jx_fontInRange:(NSRange)range {
    return [self jx_attribute:NSFontAttributeName inRange:range];
}

- (UIFont *)jx_font {
    return [self jx_attribute:NSFontAttributeName];
}

#pragma mark - Paragraph Style
- (NSParagraphStyle *)jx_paragraphStyleAtIndex:(NSUInteger)index {
    NSParagraphStyle *paragraphStyle = [self jx_attribute:NSParagraphStyleAttributeName atIndex:index];
    if (!paragraphStyle) {
        paragraphStyle = [NSParagraphStyle defaultParagraphStyle];
    }
    return paragraphStyle;
}

- (NSParagraphStyle *)jx_paragraphStyleInRange:(NSRange)range {
    return [self jx_attribute:NSParagraphStyleAttributeName inRange:range];
}

- (NSParagraphStyle *)jx_paragraphStyle {
    return [self jx_paragraphStyleInRange:self.jx_rangeOfAll];
}

#pragma mark - Foreground Color
- (UIColor *)jx_foregroundColorAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSForegroundColorAttributeName atIndex:index];
}

- (UIColor *)jx_foregroundColorInRange:(NSRange)range {
    return [self jx_attribute:NSForegroundColorAttributeName inRange:range];
}

- (UIColor *)jx_foregroundColor {
    return [self jx_attribute:NSForegroundColorAttributeName];
}

#pragma mark - Background Color
- (UIColor *)jx_backgroundColorAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSBackgroundColorAttributeName atIndex:index];
}

- (UIColor *)jx_backgroundColorInRange:(NSRange)range {
    return [self jx_attribute:NSBackgroundColorAttributeName inRange:range];
}

- (UIColor *)jx_backgroundColor {
    return [self jx_attribute:NSBackgroundColorAttributeName];
}

#pragma mark - Ligature
- (NSNumber *)jx_ligatureAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSLigatureAttributeName atIndex:index];
}

- (NSNumber *)jx_ligatureInRange:(NSRange)range {
    return [self jx_attribute:NSLigatureAttributeName inRange:range];
}

- (NSNumber *)jx_ligature {
    return [self jx_attribute:NSLigatureAttributeName];
}

#pragma mark - Kern
- (NSNumber *)jx_kernAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSKernAttributeName atIndex:index];
}

- (NSNumber *)jx_kernInRange:(NSRange)range {
    return [self jx_attribute:NSKernAttributeName inRange:range];
}

- (NSNumber *)jx_kern {
    return [self jx_attribute:NSKernAttributeName];
}

#pragma mark - Strikethrough Style
- (NSNumber *)jx_strikethroughStyleAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSStrikethroughStyleAttributeName atIndex:index];
}

- (NSNumber *)jx_strikethroughStyleInRange:(NSRange)range {
    return [self jx_attribute:NSStrikethroughStyleAttributeName inRange:range];
}

- (NSNumber *)jx_strikethroughStyle {
    return [self jx_attribute:NSStrikethroughStyleAttributeName];
}

#pragma mark - Underline Style
- (NSNumber *)jx_underlineStyleAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSUnderlineStyleAttributeName atIndex:index];
}

- (NSNumber *)jx_underlineStyleInRange:(NSRange)range {
    return [self jx_attribute:NSUnderlineStyleAttributeName inRange:range];
}

- (NSNumber *)jx_underlineStyle {
    return [self jx_attribute:NSUnderlineStyleAttributeName];
}

#pragma mark - Stroke Color
- (UIColor *)jx_strokeColorAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSStrokeColorAttributeName atIndex:index];
}

- (UIColor *)jx_strokeColorInRange:(NSRange)range {
    return [self jx_attribute:NSStrokeColorAttributeName inRange:range];
}

- (UIColor *)jx_strokeColor {
    return [self jx_attribute:NSStrokeColorAttributeName];
}

#pragma mark - Stroke Width
- (NSNumber *)jx_strokeWidthAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSStrokeWidthAttributeName atIndex:index];
}

- (NSNumber *)jx_strokeWidthInRange:(NSRange)range {
    return [self jx_attribute:NSStrokeWidthAttributeName inRange:range];
}

- (NSNumber *)jx_strokeWidth {
    return [self jx_attribute:NSStrokeWidthAttributeName];
}

#pragma mark - Shadow
- (NSShadow *)jx_shadowAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSShadowAttributeName atIndex:index];
}

- (NSShadow *)jx_shadowInRange:(NSRange)range {
    return [self jx_attribute:NSShadowAttributeName inRange:range];
}

- (NSShadow *)jx_shadow {
    return [self jx_attribute:NSShadowAttributeName];
}

#pragma mark - Text Effect
- (NSString *)jx_textEffectAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSTextEffectAttributeName atIndex:index];
}

- (NSString *)jx_textEffectInRange:(NSRange)range {
    return [self jx_attribute:NSTextEffectAttributeName inRange:range];
}

- (NSString *)jx_textEffect {
    return [self jx_attribute:NSTextEffectAttributeName];
}

#pragma mark - Attachment
- (NSTextAttachment *)jx_attachmentAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSAttachmentAttributeName atIndex:index];
}

- (NSTextAttachment *)jx_attachmentInRange:(NSRange)range {
    return [self jx_attribute:NSAttachmentAttributeName inRange:range];
}

- (NSTextAttachment *)jx_attachment {
    return [self jx_attribute:NSAttachmentAttributeName];
}

#pragma mark - Link
- (id)jx_linkAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSLinkAttributeName atIndex:index];
}

- (id)jx_linkInRange:(NSRange)range {
    return [self jx_attribute:NSLinkAttributeName inRange:range];
}

- (id)jx_link {
    return [self jx_attribute:NSLinkAttributeName];
}

#pragma mark - Baseline Offset
- (NSNumber *)jx_baselineOffsetAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSBaselineOffsetAttributeName atIndex:index];
}

- (NSNumber *)jx_baselineOffsetInRange:(NSRange)range {
    return [self jx_attribute:NSBaselineOffsetAttributeName inRange:range];
}

- (NSNumber *)jx_baselineOffset {
    return [self jx_attribute:NSBaselineOffsetAttributeName];
}

#pragma mark - Underline Color
- (UIColor *)jx_underlineColorAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSUnderlineColorAttributeName atIndex:index];
}

- (UIColor *)jx_underlineColorInRange:(NSRange)range {
    return [self jx_attribute:NSUnderlineColorAttributeName inRange:range];
}

- (UIColor *)jx_underlineColor {
    return [self jx_attribute:NSUnderlineColorAttributeName];
}

#pragma mark - Strikethrough Color
- (UIColor *)jx_strikethroughColorAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSStrikethroughColorAttributeName atIndex:index];
}

- (UIColor *)jx_strikethroughColorInRange:(NSRange)range {
    return [self jx_attribute:NSStrikethroughColorAttributeName inRange:range];
}

- (UIColor *)jx_strikethroughColor {
    return [self jx_attribute:NSStrikethroughColorAttributeName];
}

#pragma mark - Obliqueness
- (NSNumber *)jx_obliquenessAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSObliquenessAttributeName atIndex:index];
}

- (NSNumber *)jx_obliquenessInRange:(NSRange)range {
    return [self jx_attribute:NSObliquenessAttributeName inRange:range];
}

- (NSNumber *)jx_obliqueness {
    return [self jx_attribute:NSObliquenessAttributeName];
}

#pragma mark - Expansion
- (NSNumber *)jx_expansionAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSExpansionAttributeName atIndex:index];
}

- (NSNumber *)jx_expansionInRange:(NSRange)range {
    return [self jx_attribute:NSExpansionAttributeName inRange:range];
}

- (NSNumber *)jx_expansion {
    return [self jx_attribute:NSExpansionAttributeName];
}

#pragma mark - Writing Direction
- (NSArray<NSNumber *> *)jx_writingDirectionAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSWritingDirectionAttributeName atIndex:index];
}

- (NSArray<NSNumber *> *)jx_writingDirectionInRange:(NSRange)range {
    return [self jx_attribute:NSWritingDirectionAttributeName inRange:range];
}

- (NSArray<NSNumber *> *)jx_writingDirection {
    return [self jx_attribute:NSWritingDirectionAttributeName];
}

#pragma mark - Vertical Glyph Form
- (NSNumber *)jx_verticalGlyphFormAtIndex:(NSUInteger)index {
    return [self jx_attribute:NSVerticalGlyphFormAttributeName atIndex:index];
}

- (NSNumber *)jx_verticalGlyphFormInRange:(NSRange)range {
    return [self jx_attribute:NSVerticalGlyphFormAttributeName inRange:range];
}

- (NSNumber *)jx_verticalGlyphForm {
    return [self jx_attribute:NSVerticalGlyphFormAttributeName];
}

@end


@implementation NSMutableAttributedString (JXBase)

#pragma mark - Base
- (void)jx_setAttribute:(NSString *)name value:(id)value range:(NSRange)range {
    if (!name || [NSNull isEqual:name]) return;
    if (value && ![NSNull isEqual:value]) {
        [self setAttributes:@{name:value} range:range];
    } else {
        [self removeAttribute:name range:range];
    }
}

- (void)jx_setAttribute:(NSString *)name value:(id)value {
    [self jx_setAttribute:name value:(id)value range:self.jx_rangeOfAll];
}

- (void)jx_setAttributes:(NSDictionary<NSString *, id> *)attrs {
    if (attrs == (id)[NSNull null]) attrs = nil;
    [self setAttributes:@{} range:self.jx_rangeOfAll];
    [attrs enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
        [self jx_setAttribute:key value:obj];
    }];
}

- (void)jx_addAttribute:(NSString *)name value:(id)value {
    [self addAttribute:name value:value range:self.jx_rangeOfAll];
}

- (void)jx_addAttributes:(NSDictionary<NSString *, id> *)attrs {
    if (attrs == (id)[NSNull null]) attrs = nil;
    [attrs enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
        [self jx_addAttribute:key value:obj];
    }];
}

- (void)jx_removeAttribute:(NSString *)name {
    [self removeAttribute:name range:self.jx_rangeOfAll];
}

- (void)jx_removeAttributes:(NSArray<NSString *> *)attrNames range:(NSRange)range {
    if (!attrNames || attrNames.count) return;
    
    for (NSString *attrName in attrNames) {
        [self removeAttribute:attrName range:range];
    }
}

- (void)jx_removeAttributes:(NSArray<NSString *> *)attrNames {
    [self jx_removeAttributes:attrNames range:self.jx_rangeOfAll];
}

- (void)jx_removeAttributesInRange:(NSRange)range {
    [self setAttributes:nil range:range];
}

- (void)jx_removeAttributes {
    [self setAttributes:nil range:self.jx_rangeOfAll];
}

- (void)jx_insertString:(NSString *)string atIndex:(NSUInteger)location {
    [self replaceCharactersInRange:NSMakeRange(location, 0) withString:string];
    [self jx_removeDiscontinuousAttributesInRange:NSMakeRange(location, string.length)];
}

- (void)jx_appendString:(NSString *)string {
    NSUInteger length = self.length;
    [self replaceCharactersInRange:NSMakeRange(length, 0) withString:string];
    [self jx_removeDiscontinuousAttributesInRange:NSMakeRange(length, string.length)];
}

- (void)jx_removeDiscontinuousAttributesInRange:(NSRange)range {
    NSArray *keys = [NSMutableAttributedString jx_allDiscontinuousAttributeKeys];
    for (NSString *key in keys) {
        [self removeAttribute:key range:range];
    }
}

+ (NSArray<NSString *> *)jx_allDiscontinuousAttributeKeys {
    static NSMutableArray *keys;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        keys = @[(id)kCTSuperscriptAttributeName,
                 (id)kCTRunDelegateAttributeName].mutableCopy;
        float version = [UIDevice currentDevice].systemVersion.floatValue;
        if (version >= 8) {
            [keys addObject:(id)kCTRubyAnnotationAttributeName];
        }
        if (version >= 7) {
            [keys addObject:NSAttachmentAttributeName];
        }
    });
    return keys.copy;
}

#pragma mark - Font
- (void)jx_setFont:(UIFont *)font range:(NSRange)range {
    [self jx_setAttribute:NSFontAttributeName value:font range:range];
}

- (void)jx_setFont:(UIFont *)font {
    [self jx_setAttribute:NSFontAttributeName value:font];
}

- (void)jx_addFont:(UIFont *)font range:(NSRange)range {
    [self addAttribute:NSFontAttributeName value:font range:range];
}

- (void)jx_addFont:(UIFont *)font {
    [self jx_addAttribute:NSFontAttributeName value:font];
}

- (void)jx_removeFontInRange:(NSRange)range {
    [self removeAttribute:NSFontAttributeName range:range];
}

- (void)jx_removeFont {
    [self jx_removeAttribute:NSFontAttributeName];
}
#pragma mark - Paragraph Style
- (void)jx_setParagraphStyle:(NSParagraphStyle *)paragraphStyle range:(NSRange)range {
    [self jx_setAttribute:NSParagraphStyleAttributeName value:paragraphStyle range:range];
}

- (void)jx_setParagraphStyle:(NSParagraphStyle *)paragraphStyle {
    [self jx_setAttribute:NSParagraphStyleAttributeName value:paragraphStyle];
}

- (void)jx_addParagraphStyle:(NSParagraphStyle *)paragraphStyle range:(NSRange)range {
    [self addAttribute:NSParagraphStyleAttributeName value:paragraphStyle range:range];
}

- (void)jx_addParagraphStyle:(NSParagraphStyle *)paragraphStyle {
    [self jx_addAttribute:NSParagraphStyleAttributeName value:paragraphStyle];
}

- (void)jx_removeParagraphStyleInRange:(NSRange)range {
    [self removeAttribute:NSParagraphStyleAttributeName range:range];
}

- (void)jx_removeParagraphStyle {
    [self jx_removeAttribute:NSParagraphStyleAttributeName];
}

#pragma mark - Foreground Color
- (void)jx_setForegroundColor:(UIColor *)color range:(NSRange)range
{
    [self jx_setAttribute:NSForegroundColorAttributeName value:color range:range];
}

- (void)jx_setForegroundColor:(UIColor *)color {
    [self jx_setAttribute:NSForegroundColorAttributeName value:color];
}

- (void)jx_addForegroundColor:(UIColor *)color range:(NSRange)range {
    [self addAttribute:NSForegroundColorAttributeName value:color range:range];
}

- (void)jx_addForegroundColor:(UIColor *)color {
    [self jx_addAttribute:NSForegroundColorAttributeName value:color];
}

- (void)jx_removeForegroundColorInRange:(NSRange)range {
    [self removeAttribute:NSForegroundColorAttributeName range:range];
}

- (void)jx_removeForegroundColor {
    [self jx_removeAttribute:NSForegroundColorAttributeName];
}

#pragma mark - Background Color
- (void)jx_setBackgroundColor:(UIColor *)color range:(NSRange)range {
    [self jx_setAttribute:NSBackgroundColorAttributeName value:color range:range];
}

- (void)jx_setBackgroundColor:(UIColor *)color {
    [self jx_setAttribute:NSBackgroundColorAttributeName value:color];
}

- (void)jx_addBackgroundColor:(UIColor *)color range:(NSRange)range {
    [self addAttribute:NSBackgroundColorAttributeName value:color range:range];
}

- (void)jx_addBackgroundColor:(UIColor *)color {
    [self jx_addAttribute:NSBackgroundColorAttributeName value:color];
}

- (void)jx_removeBackgroundColorInRange:(NSRange)range {
    [self removeAttribute:NSBackgroundColorAttributeName range:range];
}

- (void)jx_removeBackgroundColor {
    [self jx_removeAttribute:NSBackgroundColorAttributeName];
}

#pragma mark - Ligature
- (void)jx_setLigature:(NSNumber *)ligature range:(NSRange)range {
    [self jx_setAttribute:NSLigatureAttributeName value:ligature range:range];
}

- (void)jx_setLigature:(NSNumber *)ligature {
    [self jx_setAttribute:NSLigatureAttributeName value:ligature];
}

- (void)jx_addLigature:(NSNumber *)ligature range:(NSRange)range {
    [self addAttribute:NSLigatureAttributeName value:ligature range:range];
}

- (void)jx_addLigature:(NSNumber *)ligature {
    [self jx_addAttribute:NSLigatureAttributeName value:ligature];
}

- (void)jx_removeLigatureInRange:(NSRange)range {
    [self removeAttribute:NSLigatureAttributeName range:range];
}

- (void)jx_removeLigature {
    [self jx_removeAttribute:NSLigatureAttributeName];
}

#pragma mark - Kern
- (void)jx_setKern:(NSNumber *)kern range:(NSRange)range {
    [self jx_setAttribute:NSKernAttributeName value:kern range:range];
}

- (void)jx_setKern:(NSNumber *)kern {
    [self jx_setAttribute:NSKernAttributeName value:kern];
}

- (void)jx_addKern:(NSNumber *)kern range:(NSRange)range {
    [self addAttribute:NSKernAttributeName value:kern range:range];
}

- (void)jx_addKern:(NSNumber *)kern {
    [self jx_addAttribute:NSKernAttributeName value:kern];
}

- (void)jx_removeKernInRange:(NSRange)range {
    [self removeAttribute:NSKernAttributeName range:range];
}

- (void)jx_removeKern {
    [self jx_removeAttribute:NSKernAttributeName];
}

#pragma mark - Strikethrough Style
- (void)jx_setStrikethroughStyle:(NSNumber *)strikethroughStyle range:(NSRange)range {
    [self jx_setAttribute:NSStrikethroughStyleAttributeName value:strikethroughStyle range:range];
}

- (void)jx_setStrikethroughStyle:(NSNumber *)strikethroughStyle {
    [self jx_setAttribute:NSStrikethroughStyleAttributeName value:strikethroughStyle];
}

- (void)jx_addStrikethroughStyle:(NSNumber *)strikethroughStyle range:(NSRange)range {
    [self addAttribute:NSStrikethroughStyleAttributeName value:strikethroughStyle range:range];
}

- (void)jx_addStrikethroughStyle:(NSNumber *)strikethroughStyle {
    [self jx_addAttribute:NSStrikethroughStyleAttributeName value:strikethroughStyle];
}

- (void)jx_removeStrikethroughStyleInRange:(NSRange)range {
    [self removeAttribute:NSStrikethroughStyleAttributeName range:range];
}

- (void)jx_removeStrikethroughStyle {
    [self jx_removeAttribute:NSStrikethroughStyleAttributeName];
}

#pragma mark - Underline Style
- (void)jx_setUnderlineStyle:(NSNumber *)underlineStyle range:(NSRange)range {
    [self jx_setAttribute:NSUnderlineStyleAttributeName value:underlineStyle range:range];
}

- (void)jx_setUnderlineStyle:(NSNumber *)underlineStyle {
    [self jx_setAttribute:NSUnderlineStyleAttributeName value:underlineStyle];
}

- (void)jx_addUnderlineStyle:(NSNumber *)underlineStyle range:(NSRange)range {
    [self addAttribute:NSUnderlineStyleAttributeName value:underlineStyle range:range];
}

- (void)jx_addUnderlineStyle:(NSNumber *)underlineStyle {
    [self jx_addAttribute:NSUnderlineStyleAttributeName value:underlineStyle];
}

- (void)jx_removeUnderlineStyleInRange:(NSRange)range {
    [self removeAttribute:NSUnderlineStyleAttributeName range:range];
}

- (void)jx_removeUnderlineStyle {
    [self jx_removeAttribute:NSUnderlineStyleAttributeName];
}

#pragma mark - Stroke Color
- (void)jx_setStrokeColor:(UIColor *)color range:(NSRange)range {
    [self jx_setAttribute:NSStrokeColorAttributeName value:color range:range];
}

- (void)jx_setStrokeColor:(UIColor *)color {
    [self jx_setAttribute:NSStrokeColorAttributeName value:color];
}

- (void)jx_addStrokeColor:(UIColor *)color range:(NSRange)range {
    [self addAttribute:NSStrokeColorAttributeName value:color range:range];
}

- (void)jx_addStrokeColor:(UIColor *)color {
    [self jx_addAttribute:NSStrokeColorAttributeName value:color];
}

- (void)jx_removeStrokeColorInRange:(NSRange)range {
    [self removeAttribute:NSStrokeColorAttributeName range:range];
}

- (void)jx_removeStrokeColor {
    [self jx_removeAttribute:NSStrokeColorAttributeName];
}

#pragma mark - Stroke Width
- (void)jx_setStrokeWidth:(NSNumber *)strokeWidth range:(NSRange)range {
    [self jx_setAttribute:NSStrokeWidthAttributeName value:strokeWidth range:range];
}

- (void)jx_setStrokeWidth:(NSNumber *)strokeWidth {
    [self jx_setAttribute:NSStrokeWidthAttributeName value:strokeWidth];
}

- (void)jx_addStrokeWidth:(NSNumber *)strokeWidth range:(NSRange)range {
    [self addAttribute:NSStrokeWidthAttributeName value:strokeWidth range:range];
}

- (void)jx_addStrokeWidth:(NSNumber *)strokeWidth {
    [self jx_addAttribute:NSStrokeWidthAttributeName value:strokeWidth];
}

- (void)jx_removeStrokeWidthInRange:(NSRange)range {
    [self removeAttribute:NSStrokeWidthAttributeName range:range];
}

- (void)jx_removeStrokeWidth {
    [self jx_removeAttribute:NSStrokeWidthAttributeName];
}

#pragma mark - Shadow
- (void)jx_setShadow:(NSShadow *)shadow range:(NSRange)range {
    [self jx_setAttribute:NSShadowAttributeName value:shadow range:range];
}

- (void)jx_setShadow:(NSShadow *)shadow {
    [self jx_setAttribute:NSShadowAttributeName value:shadow];
}

- (void)jx_addShadow:(NSShadow *)shadow range:(NSRange)range {
    [self addAttribute:NSShadowAttributeName value:shadow range:range];
}

- (void)jx_addShadow:(NSShadow *)shadow {
    [self jx_addAttribute:NSShadowAttributeName value:shadow];
}

- (void)jx_removeShadowInRange:(NSRange)range {
    [self removeAttribute:NSShadowAttributeName range:range];
}

- (void)jx_removeShadow {
    [self jx_removeAttribute:NSShadowAttributeName];
}

#pragma mark - Text Effect
- (void)jx_setTextEffect:(NSString *)textEffect range:(NSRange)range {
    [self jx_setAttribute:NSTextEffectAttributeName value:textEffect range:range];
}

- (void)jx_setTextEffect:(NSString *)textEffect {
    [self jx_setAttribute:NSTextEffectAttributeName value:textEffect];
}

- (void)jx_addTextEffect:(NSString *)textEffect range:(NSRange)range {
    [self addAttribute:NSTextEffectAttributeName value:textEffect range:range];
}

- (void)jx_addTextEffect:(NSString *)textEffect {
    [self jx_addAttribute:NSTextEffectAttributeName value:textEffect];
}

- (void)jx_removeTextEffectInRange:(NSRange)range {
    [self removeAttribute:NSTextEffectAttributeName range:range];
}

- (void)jx_removeTextEffect {
    [self jx_removeAttribute:NSTextEffectAttributeName];
}

#pragma mark - Attachment
- (void)jx_setAttachment:(NSTextAttachment *)attachment range:(NSRange)range {
    [self jx_setAttribute:NSAttachmentAttributeName value:attachment range:range];
}

- (void)jx_setAttachment:(NSTextAttachment *)attachment {
    [self jx_setAttribute:NSAttachmentAttributeName value:attachment];
}

- (void)jx_addAttachment:(NSTextAttachment *)attachment range:(NSRange)range {
    [self addAttribute:NSAttachmentAttributeName value:attachment range:range];
}

- (void)jx_addAttachment:(NSTextAttachment *)attachment {
    [self jx_addAttribute:NSAttachmentAttributeName value:attachment];
}

- (void)jx_removeAttachmentInRange:(NSRange)range {
    [self removeAttribute:NSAttachmentAttributeName range:range];
}

- (void)jx_removeAttachment {
    [self jx_removeAttribute:NSAttachmentAttributeName];
}

#pragma mark - Link
- (void)jx_setLink:(id)link range:(NSRange)range {
    [self jx_setAttribute:NSLinkAttributeName value:link range:range];
}

- (void)jx_setLink:(id)link {
    [self jx_setAttribute:NSLinkAttributeName value:link];
}

- (void)jx_addLink:(id)link range:(NSRange)range {
    [self addAttribute:NSLinkAttributeName value:link range:range];
}

- (void)jx_addLink:(id)link {
    [self jx_addAttribute:NSLinkAttributeName value:link];
}

- (void)jx_removeLinkInRange:(NSRange)range {
    [self removeAttribute:NSLinkAttributeName range:range];
}

- (void)jx_removeLink {
    [self jx_removeAttribute:NSLinkAttributeName];
}

#pragma mark - Baseline Offset
- (void)jx_setBaselineOffset:(NSNumber *)baselineOffset range:(NSRange)range {
    [self jx_setAttribute:NSBaselineOffsetAttributeName value:baselineOffset range:range];
}

- (void)jx_setBaselineOffset:(NSNumber *)baselineOffset {
    [self jx_setAttribute:NSBaselineOffsetAttributeName value:baselineOffset];
}

- (void)jx_addBaselineOffset:(NSNumber *)baselineOffset range:(NSRange)range {
    [self addAttribute:NSBaselineOffsetAttributeName value:baselineOffset range:range];
}

- (void)jx_addBaselineOffset:(NSNumber *)baselineOffset {
    [self jx_addAttribute:NSBaselineOffsetAttributeName value:baselineOffset];
}

- (void)jx_removeBaselineOffsetInRange:(NSRange)range {
    [self removeAttribute:NSBaselineOffsetAttributeName range:range];
}

- (void)jx_removeBaselineOffset {
    [self jx_removeAttribute:NSBaselineOffsetAttributeName];
}

#pragma mark - Underline Color
- (void)jx_setUnderlineColor:(UIColor *)color range:(NSRange)range {
    [self jx_setAttribute:NSUnderlineColorAttributeName value:color range:range];
}

- (void)jx_setUnderlineColor:(UIColor *)color {
    [self jx_setAttribute:NSUnderlineColorAttributeName value:color];
}

- (void)jx_addUnderlineColor:(UIColor *)color range:(NSRange)range {
    [self addAttribute:NSUnderlineColorAttributeName value:color range:range];
}

- (void)jx_addUnderlineColor:(UIColor *)color {
    [self jx_addAttribute:NSUnderlineColorAttributeName value:color];
}

- (void)jx_removeUnderlineColorInRange:(NSRange)range {
    [self removeAttribute:NSUnderlineColorAttributeName range:range];
}

- (void)jx_removeUnderlineColor {
    [self jx_removeAttribute:NSUnderlineColorAttributeName];
}

#pragma mark - Strikethrough Color
- (void)jx_setStrikethroughColor:(UIColor *)color range:(NSRange)range {
    [self jx_setAttribute:NSStrikethroughColorAttributeName value:color range:range];
}

- (void)jx_setStrikethroughColor:(UIColor *)color {
    [self jx_setAttribute:NSStrikethroughColorAttributeName value:color];
}

- (void)jx_addStrikethroughColor:(UIColor *)color range:(NSRange)range {
    [self addAttribute:NSStrikethroughColorAttributeName value:color range:range];
}

- (void)jx_addStrikethroughColor:(UIColor *)color {
    [self jx_addAttribute:NSStrikethroughColorAttributeName value:color];
}

- (void)jx_removeStrikethroughColorInRange:(NSRange)range {
    [self removeAttribute:NSStrikethroughColorAttributeName range:range];
}

- (void)jx_removeStrikethroughColor {
    [self jx_removeAttribute:NSStrikethroughColorAttributeName];
}

#pragma mark - Obliqueness
- (void)jx_setObliqueness:(NSNumber *)obliqueness range:(NSRange)range {
    [self jx_setAttribute:NSObliquenessAttributeName value:obliqueness range:range];
}

- (void)jx_setObliqueness:(NSNumber *)obliqueness {
    [self jx_setAttribute:NSObliquenessAttributeName value:obliqueness];
}

- (void)jx_addObliqueness:(NSNumber *)obliqueness range:(NSRange)range {
    [self addAttribute:NSObliquenessAttributeName value:obliqueness range:range];
}

- (void)jx_addObliqueness:(NSNumber *)obliqueness {
    [self jx_addAttribute:NSObliquenessAttributeName value:obliqueness];
}

- (void)jx_removeObliquenessInRange:(NSRange)range {
    [self removeAttribute:NSObliquenessAttributeName range:range];
}

- (void)jx_removeObliqueness {
    [self jx_removeAttribute:NSObliquenessAttributeName];
}

#pragma mark - Expansion
- (void)jx_setExpansion:(NSNumber *)expansion range:(NSRange)range {
    [self jx_setAttribute:NSExpansionAttributeName value:expansion range:range];
}

- (void)jx_setExpansion:(NSNumber *)expansion {
    [self jx_setAttribute:NSExpansionAttributeName value:expansion];
}

- (void)jx_addExpansion:(NSNumber *)expansion range:(NSRange)range {
    [self addAttribute:NSExpansionAttributeName value:expansion range:range];
}

- (void)jx_addExpansion:(NSNumber *)expansion {
    [self jx_addAttribute:NSExpansionAttributeName value:expansion];
}

- (void)jx_removeExpansionInRange:(NSRange)range {
    [self removeAttribute:NSExpansionAttributeName range:range];
}

- (void)jx_removeExpansion {
    [self jx_removeAttribute:NSExpansionAttributeName];
}

#pragma mark - Writing Direction
- (void)jx_setWritingDirection:(NSArray<NSNumber *> *)writingDirection range:(NSRange)range {
    [self jx_setAttribute:NSWritingDirectionAttributeName value:writingDirection range:range];
}

- (void)jx_setWritingDirection:(NSArray<NSNumber *> *)writingDirection {
    [self jx_setAttribute:NSWritingDirectionAttributeName value:writingDirection];
}

- (void)jx_addWritingDirection:(NSArray<NSNumber *> *)writingDirection range:(NSRange)range {
    [self addAttribute:NSWritingDirectionAttributeName value:writingDirection range:range];
}

- (void)jx_addWritingDirection:(NSArray<NSNumber *> *)writingDirection {
    [self jx_addAttribute:NSWritingDirectionAttributeName value:writingDirection];
}

- (void)jx_removeWritingDirectionInRange:(NSRange)range {
    [self removeAttribute:NSWritingDirectionAttributeName range:range];
}

- (void)jx_removeWritingDirection {
    [self jx_removeAttribute:NSWritingDirectionAttributeName];
}

#pragma mark - Vertical Glyph Form
- (void)jx_setVerticalGlyphForm:(NSNumber *)verticalGlyphForm range:(NSRange)range {
    [self jx_setAttribute:NSVerticalGlyphFormAttributeName value:verticalGlyphForm range:range];
}

- (void)jx_setVerticalGlyphForm:(NSNumber *)verticalGlyphForm {
    [self jx_setAttribute:NSVerticalGlyphFormAttributeName value:verticalGlyphForm];
}

- (void)jx_addVerticalGlyphForm:(NSNumber *)verticalGlyphForm range:(NSRange)range {
    [self addAttribute:NSVerticalGlyphFormAttributeName value:verticalGlyphForm range:range];
}

- (void)jx_addVerticalGlyphForm:(NSNumber *)verticalGlyphForm {
    [self jx_addAttribute:NSVerticalGlyphFormAttributeName value:verticalGlyphForm];
}

- (void)jx_removeVerticalGlyphFormInRange:(NSRange)range {
    [self removeAttribute:NSVerticalGlyphFormAttributeName range:range];
}

- (void)jx_removeVerticalGlyphForm {
    [self jx_removeAttribute:NSVerticalGlyphFormAttributeName];
}

@end
