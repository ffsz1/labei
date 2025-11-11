//
//  NSAttributedString+JXParagraphStyleAttributes.m
//  Pods
//
//  Created by Colin on 17/2/10.
//
//

#import "NSAttributedString+JXParagraphStyleAttributes.h"
#import "NSAttributedString+JXBase.h"

@implementation NSAttributedString (JXParagraphStyleAttributes)

#define JXParagraphStyleAttributeAtIndex(_attr_) \
NSParagraphStyle *paragraphstyle = [self jx_paragraphStyleAtIndex:index]; \
if (!paragraphstyle) paragraphstyle = [NSParagraphStyle defaultParagraphStyle]; \
return paragraphstyle. _attr_; \

#pragma mark - Line Spacing
- (CGFloat)jx_lineSpacingAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(lineSpacing);
}

#pragma mark - Paragraph Spacing
- (CGFloat)jx_paragraphSpacingAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(paragraphSpacing);
}

#pragma mark - Alignment
- (NSTextAlignment)jx_alignmentAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(alignment);
}

#pragma mark - Head Indent
- (CGFloat)jx_headIndentAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(headIndent);
}

#pragma mark - Tail Indent
- (CGFloat)jx_tailIndentAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(tailIndent);
}

#pragma mark - First Line Head Indent
- (CGFloat)jx_firstLineHeadIndentAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(firstLineHeadIndent);
}

#pragma mark - MinimumLine Height
- (CGFloat)jx_minimumLineHeightAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(minimumLineHeight);
}

#pragma mark - MaximumLine Height
- (CGFloat)jx_maximumLineHeightAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(maximumLineHeight);
}

#pragma mark - Line Break Mode
- (NSLineBreakMode)jx_lineBreakModeAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(lineBreakMode);
}

#pragma mark - Base Writing Direction
- (NSWritingDirection)jx_baseWritingDirectionAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(baseWritingDirection);
}

#pragma mark - Line Height Multiple
- (CGFloat)jx_lineHeightMultipleAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(lineHeightMultiple);
}

#pragma mark - Paragraph Spacing Before
- (CGFloat)jx_paragraphSpacingBeforeAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(paragraphSpacingBefore);
}

#pragma mark - Hyphenation Factor
- (float)jx_hyphenationFactorAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(hyphenationFactor);
}

#pragma mark - Tab Stops
- (NSArray<NSTextTab *> *)jx_tabStopsAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(tabStops);
}

#pragma mark - Default Tab Interval
- (CGFloat)jx_defaultTabIntervalAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(defaultTabInterval);
}

#pragma mark - Allows Default Tightening For Truncation
- (BOOL)jx_allowsDefaultTighteningForTruncationAtIndex:(NSUInteger)index {
    JXParagraphStyleAttributeAtIndex(allowsDefaultTighteningForTruncation);
}

#undef JXParagraphStyleAttributeAtIndex

@end


@implementation NSMutableAttributedString (JXParagraphStyleAttributes)

#define JXParagraphStyleSetAttributeInRange(_attr_) \
NSParagraphStyle *paragraphStyle = [self jx_paragraphStyleInRange:range]; \
NSMutableParagraphStyle *mutableParagraphStyle = nil; \
if (paragraphStyle) { \
if ([paragraphStyle isKindOfClass:[NSMutableParagraphStyle class]]) { \
mutableParagraphStyle = (id)paragraphStyle; \
} else { \
mutableParagraphStyle = paragraphStyle.mutableCopy; \
} \
} else { \
mutableParagraphStyle = [NSParagraphStyle defaultParagraphStyle].mutableCopy; \
} \
if (mutableParagraphStyle. _attr_ != _attr_) mutableParagraphStyle. _attr_ = _attr_; \
[self jx_setParagraphStyle:mutableParagraphStyle range:range];

#define JXParagraphStyleAddAttributeInRange(_attr_) \
[self enumerateAttribute:NSParagraphStyleAttributeName \
inRange:range \
options:kNilOptions usingBlock:^(NSParagraphStyle *paragraphStyle, NSRange subRange, BOOL * _Nonnull stop) { \
NSMutableParagraphStyle *mutableParagraphStyle = nil; \
if (paragraphStyle) { \
if ([paragraphStyle isKindOfClass:[NSMutableParagraphStyle class]]) { \
mutableParagraphStyle = (id)paragraphStyle; \
} else { \
mutableParagraphStyle = paragraphStyle.mutableCopy; \
} \
} else { \
mutableParagraphStyle = [NSParagraphStyle defaultParagraphStyle].mutableCopy; \
} \
if (mutableParagraphStyle. _attr_ != _attr_) mutableParagraphStyle. _attr_ = _attr_; \
[self addAttribute:NSParagraphStyleAttributeName value:mutableParagraphStyle range:range]; \
}];

#define JXParagraphStyleRemoveAttributeInRange(_attr_) \
[self enumerateAttribute:NSParagraphStyleAttributeName \
inRange:range \
options:kNilOptions usingBlock:^(NSParagraphStyle *paragraphStyle, NSRange subRange, BOOL * _Nonnull stop) { \
NSMutableParagraphStyle *mutableParagraphStyle = nil; \
if (paragraphStyle) { \
if ([paragraphStyle isKindOfClass:[NSMutableParagraphStyle class]]) { \
mutableParagraphStyle = (id)paragraphStyle; \
} else { \
mutableParagraphStyle = paragraphStyle.mutableCopy; \
} \
} \
if (mutableParagraphStyle. _attr_ != [NSParagraphStyle defaultParagraphStyle]. _attr_) mutableParagraphStyle. _attr_ = [NSParagraphStyle defaultParagraphStyle]. _attr_; \
[self jx_setParagraphStyle:mutableParagraphStyle range:range]; \
}];

#pragma mark - Line Spacing
- (void)jx_setLineSpacing:(CGFloat)lineSpacing range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(lineSpacing);
}

- (void)jx_setLineSpacing:(CGFloat)lineSpacing {
    [self jx_setLineSpacing:lineSpacing range:self.jx_rangeOfAll];
}

- (void)jx_addLineSpacing:(CGFloat)lineSpacing range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(lineSpacing);
}

- (void)jx_addLineSpacing:(CGFloat)lineSpacing {
    [self jx_addLineSpacing:lineSpacing range:self.jx_rangeOfAll];
}

- (void)jx_removeLineSpacingInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(lineSpacing);
}

- (void)jx_removeLineSpacing {
    [self jx_removeLineSpacingInRange:self.jx_rangeOfAll];
}

#pragma mark - Paragraph Spacing
- (void)jx_setParagraphSpacing:(CGFloat)paragraphSpacing range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(paragraphSpacing);
}

- (void)jx_setParagraphSpacing:(CGFloat)paragraphSpacing {
    [self jx_setParagraphSpacing:paragraphSpacing range:self.jx_rangeOfAll];
}

- (void)jx_addParagraphSpacing:(CGFloat)paragraphSpacing range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(paragraphSpacing);
}

- (void)jx_addParagraphSpacing:(CGFloat)paragraphSpacing {
    [self jx_addParagraphSpacing:paragraphSpacing range:self.jx_rangeOfAll];
}

- (void)jx_removeParagraphSpacingInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(paragraphSpacing);
}

- (void)jx_removeParagraphSpacing {
    [self jx_removeParagraphSpacingInRange:self.jx_rangeOfAll];
}

#pragma mark - Alignment
- (void)jx_setAlignment:(NSTextAlignment)alignment range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(alignment);
}

- (void)jx_setAlignment:(NSTextAlignment)alignment {
    [self jx_setAlignment:alignment range:self.jx_rangeOfAll];
}

- (void)jx_addAlignment:(NSTextAlignment)alignment range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(alignment);
}

- (void)jx_addAlignment:(NSTextAlignment)alignment {
    [self jx_addAlignment:alignment range:self.jx_rangeOfAll];
}

- (void)jx_removeAlignmentInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(alignment);
}

- (void)jx_removeAlignment {
    [self jx_removeAlignmentInRange:self.jx_rangeOfAll];
}

#pragma mark - Head Indent
- (void)jx_setHeadIndent:(CGFloat)headIndent range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(headIndent);
}

- (void)jx_setHeadIndent:(CGFloat)headIndent {
    [self jx_setHeadIndent:headIndent range:self.jx_rangeOfAll];
}

- (void)jx_addHeadIndent:(CGFloat)headIndent range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(headIndent);
}

- (void)jx_addHeadIndent:(CGFloat)headIndent {
    [self jx_addHeadIndent:headIndent range:self.jx_rangeOfAll];
}

- (void)jx_removeHeadIndentInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(headIndent);
}

- (void)jx_removeHeadIndent {
    [self jx_removeHeadIndentInRange:self.jx_rangeOfAll];
}

#pragma mark - Tail Indent
- (void)jx_setTailIndent:(CGFloat)tailIndent range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(tailIndent);
}

- (void)jx_setTailIndent:(CGFloat)tailIndent {
    [self jx_setTailIndent:tailIndent range:self.jx_rangeOfAll];
}

- (void)jx_addTailIndent:(CGFloat)tailIndent range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(tailIndent);
}

- (void)jx_addTailIndent:(CGFloat)tailIndent {
    [self jx_addTailIndent:tailIndent range:self.jx_rangeOfAll];
}

- (void)jx_removeTailIndentInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(tailIndent);
}

- (void)jx_removeTailIndent {
    [self jx_removeTailIndentInRange:self.jx_rangeOfAll];
}

#pragma mark - First Line Head Indent
- (void)jx_setFirstLineHeadIndent:(CGFloat)firstLineHeadIndent range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(firstLineHeadIndent);
}

- (void)jx_setFirstLineHeadIndent:(CGFloat)firstLineHeadIndent {
    [self jx_setFirstLineHeadIndent:firstLineHeadIndent range:self.jx_rangeOfAll];
}

- (void)jx_addFirstLineHeadIndent:(CGFloat)firstLineHeadIndent range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(firstLineHeadIndent);
}

- (void)jx_addFirstLineHeadIndent:(CGFloat)firstLineHeadIndent {
    [self jx_addFirstLineHeadIndent:firstLineHeadIndent range:self.jx_rangeOfAll];
}

- (void)jx_removeFirstLineHeadIndentInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(firstLineHeadIndent);
}

- (void)jx_removeFirstLineHeadIndent {
    [self jx_removeFirstLineHeadIndentInRange:self.jx_rangeOfAll];
}

#pragma mark - Minimum Line Height
- (void)jx_setMinimumLineHeight:(CGFloat)minimumLineHeight range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(minimumLineHeight);
}

- (void)jx_setMinimumLineHeight:(CGFloat)minimumLineHeight {
    [self jx_setMinimumLineHeight:minimumLineHeight range:self.jx_rangeOfAll];
}

- (void)jx_addMinimumLineHeight:(CGFloat)minimumLineHeight range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(minimumLineHeight);
}

- (void)jx_addMinimumLineHeight:(CGFloat)minimumLineHeight {
    [self jx_addMinimumLineHeight:minimumLineHeight range:self.jx_rangeOfAll];
}

- (void)jx_removeMinimumLineHeightInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(minimumLineHeight);
}

- (void)jx_removeMinimumLineHeight {
    [self jx_removeMinimumLineHeightInRange:self.jx_rangeOfAll];
}

#pragma mark - Maximum Line Height
- (void)jx_setMaximumLineHeight:(CGFloat)maximumLineHeight range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(maximumLineHeight);
}

- (void)jx_setMaximumLineHeight:(CGFloat)maximumLineHeight {
    [self jx_setMaximumLineHeight:maximumLineHeight range:self.jx_rangeOfAll];
}

- (void)jx_addMaximumLineHeight:(CGFloat)maximumLineHeight range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(maximumLineHeight);
}

- (void)jx_addMaximumLineHeight:(CGFloat)maximumLineHeight {
    [self jx_addMaximumLineHeight:maximumLineHeight range:self.jx_rangeOfAll];
}

- (void)jx_removeMaximumLineHeightInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(maximumLineHeight);
}

- (void)jx_removeMaximumLineHeight {
    [self jx_removeMaximumLineHeightInRange:self.jx_rangeOfAll];
}

#pragma mark - Line Break Mode
- (void)jx_setLineBreakMode:(NSLineBreakMode)lineBreakMode range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(lineBreakMode);
}

- (void)jx_setLineBreakMode:(NSLineBreakMode)lineBreakMode {
    [self jx_setLineBreakMode:lineBreakMode range:self.jx_rangeOfAll];
}

- (void)jx_addLineBreakMode:(NSLineBreakMode)lineBreakMode range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(lineBreakMode);
}

- (void)jx_addLineBreakMode:(NSLineBreakMode)lineBreakMode {
    [self jx_addLineBreakMode:lineBreakMode range:self.jx_rangeOfAll];
}

- (void)jx_removeLineBreakModeInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(lineBreakMode);
}

- (void)jx_removeLineBreakMode {
    [self jx_removeLineBreakModeInRange:self.jx_rangeOfAll];
}

#pragma mark - Base Writing Direction
- (void)jx_setBaseWritingDirection:(NSWritingDirection)baseWritingDirection range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(baseWritingDirection);
}

- (void)jx_setBaseWritingDirection:(NSWritingDirection)baseWritingDirection {
    [self jx_setBaseWritingDirection:baseWritingDirection range:self.jx_rangeOfAll];
}

- (void)jx_addBaseWritingDirection:(NSWritingDirection)baseWritingDirection range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(baseWritingDirection);
}

- (void)jx_addBaseWritingDirection:(NSWritingDirection)baseWritingDirection {
    [self jx_addBaseWritingDirection:baseWritingDirection range:self.jx_rangeOfAll];
}

- (void)jx_removeBaseWritingDirectionInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(baseWritingDirection);
}

- (void)jx_removeBaseWritingDirection {
    [self jx_removeBaseWritingDirectionInRange:self.jx_rangeOfAll];
}

#pragma mark - Line Height Multiple
- (void)jx_setLineHeightMultiple:(CGFloat)lineHeightMultiple range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(lineHeightMultiple);
}

- (void)jx_setLineHeightMultiple:(CGFloat)lineHeightMultiple {
    [self jx_setLineHeightMultiple:lineHeightMultiple range:self.jx_rangeOfAll];
}

- (void)jx_addLineHeightMultiple:(CGFloat)lineHeightMultiple range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(lineHeightMultiple);
}

- (void)jx_addLineHeightMultiple:(CGFloat)lineHeightMultiple {
    [self jx_addLineHeightMultiple:lineHeightMultiple range:self.jx_rangeOfAll];
}

- (void)jx_removeLineHeightMultipleInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(lineHeightMultiple);
}

- (void)jx_removeLineHeightMultiple {
    [self jx_removeLineHeightMultipleInRange:self.jx_rangeOfAll];
}

#pragma mark - Paragraph Spacing Before
- (void)jx_setParagraphSpacingBefore:(CGFloat)paragraphSpacingBefore range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(paragraphSpacingBefore);
}

- (void)jx_setParagraphSpacingBefore:(CGFloat)paragraphSpacingBefore {
    [self jx_setParagraphSpacingBefore:paragraphSpacingBefore range:self.jx_rangeOfAll];
}

- (void)jx_addParagraphSpacingBefore:(CGFloat)paragraphSpacingBefore range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(paragraphSpacingBefore);
}

- (void)jx_addParagraphSpacingBefore:(CGFloat)paragraphSpacingBefore {
    [self jx_addParagraphSpacingBefore:paragraphSpacingBefore range:self.jx_rangeOfAll];
}

- (void)jx_removeParagraphSpacingBeforeInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(paragraphSpacingBefore);
}

- (void)jx_removeParagraphSpacingBefore {
    [self jx_removeParagraphSpacingBeforeInRange:self.jx_rangeOfAll];
}

#pragma mark - Hyphenation Factor
- (void)jx_setHyphenationFactor:(float)hyphenationFactor range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(hyphenationFactor);
}

- (void)jx_setHyphenationFactor:(float)hyphenationFactor {
    [self jx_setHyphenationFactor:hyphenationFactor range:self.jx_rangeOfAll];
}

- (void)jx_addHyphenationFactor:(float)hyphenationFactor range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(hyphenationFactor);
}

- (void)jx_addHyphenationFactor:(float)hyphenationFactor {
    [self jx_addHyphenationFactor:hyphenationFactor range:self.jx_rangeOfAll];
}

- (void)jx_removeHyphenationFactorInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(hyphenationFactor);
}

- (void)jx_removeHyphenationFactor {
    [self jx_removeHyphenationFactorInRange:self.jx_rangeOfAll];
}

#pragma mark - Tab Stops
- (void)jx_setTabStops:(NSArray<NSTextTab *> *)tabStops range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(tabStops);
}

- (void)jx_setTabStops:(NSArray<NSTextTab *> *)tabStops {
    [self jx_setTabStops:tabStops range:self.jx_rangeOfAll];
}

- (void)jx_addTabStops:(NSArray<NSTextTab *> *)tabStops range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(tabStops);
}

- (void)jx_addTabStops:(NSArray<NSTextTab *> *)tabStops {
    [self jx_addTabStops:tabStops range:self.jx_rangeOfAll];
}

- (void)jx_removeTabStopsInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(tabStops);
}

- (void)jx_removeTabStops {
    [self jx_removeTabStopsInRange:self.jx_rangeOfAll];
}

#pragma mark - Default Tab Interval
- (void)jx_setDefaultTabInterval:(CGFloat)defaultTabInterval range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(defaultTabInterval);
}

- (void)jx_setDefaultTabInterval:(CGFloat)defaultTabInterval {
    [self jx_setDefaultTabInterval:defaultTabInterval range:self.jx_rangeOfAll];
}

- (void)jx_addDefaultTabInterval:(CGFloat)defaultTabInterval range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(defaultTabInterval);
}

- (void)jx_addDefaultTabInterval:(CGFloat)defaultTabInterval {
    [self jx_addDefaultTabInterval:defaultTabInterval range:self.jx_rangeOfAll];
}

- (void)jx_removeDefaultTabIntervalInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(defaultTabInterval);
}

- (void)jx_removeDefaultTabInterval {
    [self jx_removeDefaultTabIntervalInRange:self.jx_rangeOfAll];
}

#pragma mark - Allows Default Tightening For Truncation
- (void)jx_setAllowsDefaultTighteningForTruncation:(BOOL)allowsDefaultTighteningForTruncation range:(NSRange)range {
    JXParagraphStyleSetAttributeInRange(allowsDefaultTighteningForTruncation);
}

- (void)jx_setAllowsDefaultTighteningForTruncation:(BOOL)allowsDefaultTighteningForTruncation {
    [self jx_setAllowsDefaultTighteningForTruncation:allowsDefaultTighteningForTruncation range:self.jx_rangeOfAll];
}

- (void)jx_addAllowsDefaultTighteningForTruncation:(BOOL)allowsDefaultTighteningForTruncation range:(NSRange)range {
    JXParagraphStyleAddAttributeInRange(allowsDefaultTighteningForTruncation);
}

- (void)jx_addAllowsDefaultTighteningForTruncation:(BOOL)allowsDefaultTighteningForTruncation {
    [self jx_addAllowsDefaultTighteningForTruncation:allowsDefaultTighteningForTruncation range:self.jx_rangeOfAll];
}

- (void)jx_removeAllowsDefaultTighteningForTruncationInRange:(NSRange)range {
    JXParagraphStyleRemoveAttributeInRange(allowsDefaultTighteningForTruncation);
}

- (void)jx_removeAllowsDefaultTighteningForTruncation {
    [self jx_removeAllowsDefaultTighteningForTruncationInRange:self.jx_rangeOfAll];
}

#undef JXParagraphStyleSetAttributeInRange
#undef JXParagraphStyleAddAttributeInRange
#undef JXParagraphStyleRemoveAttributeInRange

@end
