//
//  RichTextUtils.m
//  YYMobileFramework
//
//  Created by 小城 on 14-7-31.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import "RichTextUtils.h"
#import "RichTextView.h"
#import "ParsersManager.h"

@interface RichTextUtils()

@property (nonatomic,strong) UITextView *textView;

@end

@implementation RichTextUtils

+ (instancetype) shareUtils
{
    static id utils;
    static dispatch_once_t token;
    dispatch_once(&token,^{
        utils = [[self alloc] init];
    });
    return utils;
}

- (instancetype) init
{
    if (self = [super init]) {
        [self _createTextView];
    }
    return self;
}

- (void) _createTextView
{
    UITextView *textView = [[UITextView alloc] init];
    textView.scrollEnabled = NO;
    textView.editable = NO;
    textView.textContainerInset = UIEdgeInsetsMake(0, 0, 0, 0);
    self.textView = textView;
}

- (CGSize) sizeForRichTextViewWithMaxWidth:(CGFloat)maxWidth attributedText:(NSAttributedString*)text propertyMap:(NSDictionary*)propertyMap
{
    CGFloat lineFragment = [[propertyMap objectForKey:RichTextViewLineFragmentPadding] doubleValue];
    self.textView.textContainer.lineFragmentPadding = lineFragment;
    self.textView.textContainer.size = CGSizeMake(maxWidth, CGFLOAT_MAX);
    self.textView.attributedText = text;
    
    NSTextContainer* textContainer = self.textView.textContainer;
    NSLayoutManager* layoutManager = self.textView.layoutManager;
    
    NSRange glyphRange = [layoutManager glyphRangeForCharacterRange:NSMakeRange(0, text.length) actualCharacterRange:NULL];
    CGRect bound = [layoutManager boundingRectForGlyphRange:glyphRange inTextContainer:textContainer];
    
    CGSize suggestedSize = bound.size;
    
    return suggestedSize;
}

- (CGSize) sizeForRichTextViewWithMaxWidth:(CGFloat)maxWidth text:(NSString*)text propertyMap:(NSDictionary*)propertyMap
{
    return [self sizeForRichTextViewWithMaxWidth:maxWidth text:text propertyMap:propertyMap parsers:[RichTextView defaultParsers]];
}

- (CGSize) sizeForRichTextViewWithMaxWidth:(CGFloat)maxWidth text:(NSString*)text propertyMap:(NSDictionary*)propertyMap parsers:(NSArray*)parsers
{
    ParsersManager *pm = [[ParsersManager alloc] init];
    [pm setParsers:parsers];
    UIFont *font = [propertyMap objectForKey:RichTextViewFontPropertyKey];
    if (font == nil) {
        font = [RichTextView defaultFont];
    }
    
    NSAttributedString * attributedString = [pm parseText:text];
    NSMutableAttributedString * mAttributedString = [[NSMutableAttributedString alloc] initWithAttributedString:attributedString];
    [mAttributedString addAttribute:NSFontAttributeName value:font range:NSMakeRange(0, mAttributedString.length)];
    return [self sizeForRichTextViewWithMaxWidth:maxWidth attributedText:mAttributedString propertyMap:propertyMap];
}


@end
