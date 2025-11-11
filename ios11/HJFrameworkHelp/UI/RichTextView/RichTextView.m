//
//  RichTextView.m
//  YYMobileFramework
//
//  Created by 小城 on 14-7-1.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import "RichTextView.h"
#import "UIImageView+YYWebImage.h"
#import "ParsersManager.h"
#import "SmileyParser.h"
#import "NSDictionary+Safe.h"

NSString *const YYRichTextViewAttachmentTypeKey = @"YYRichTextViewAttachmentTypeKey";
NSString *const YYRichTextViewAttachmentValueKey = @"YYRichTextViewAttachmentValueKey";

NSString *const RichTextViewFontPropertyKey = @"font";
NSString *const RichTextViewColorPropertyKey = @"color";
NSString *const RichTextViewLineFragmentPadding = @"lineFragmentPadding";

@interface RichTextView()
{
    ParsersManager *_parserManager;
    NSAttributedString * _attributedString;
}

@property(strong, nonatomic) NSMutableArray *subNodes; //子view。
@property(strong, nonatomic) NSMutableArray *subNodesRanges;
@property (nonatomic) UIFont *font;
@property (nonatomic) UIColor *textColor;
@property (nonatomic) CGFloat lineFragmentPadding;

@end

@implementation RichTextView

+ (NSArray *) defaultParsers
{
    return @[[SmileyParser shareObject]];
}

+ (UIFont *) defaultFont
{
    return [UIFont systemFontOfSize:14.0];
}

+ (UIView *)getCustomViewWithAttamentType:(YYRichTextViewAttachmentType)type
                          attachmentValue:(id)value
{
    UIView *customView = [[UIView alloc] init];
    switch (type) {
        case YYRichTextViewWebImagePath: {
            UIImageView *imageView = [[UIImageView alloc] init];
            imageView.contentMode = UIViewContentModeScaleAspectFit;
            
            NSString *webImagePath = value;
            
            if(webImagePath && [webImagePath isKindOfClass:NSString.class]){
//                [imageView setImageWithURL:[NSURL URLWithString:webImagePath]  placeholderImage:nil options:0 progress:^(int64_t received, int64_t expected, CGFloat progress) {
//
//                } completed:^(UIImage *image, NSError *error, YYWebImageCacheType cacheType) {
//                    
//                }];
                
                [imageView yy_setImageWithURL:[NSURL URLWithString:webImagePath] placeholder:nil options:kNilOptions completion:^(UIImage * _Nullable image, NSURL * _Nonnull url, YYWebImageFromType from, YYWebImageStage stage, NSError * _Nullable error) {
                    
                }];
            }
            customView = imageView;
            break;
        }
        default: {
            break;
        }
    }
    return customView;
}

- (instancetype) init
{
    self = [super init];
    if ( self ) {
        [self _configureParsersManager];
        [self _commonInit];
        [self _createTextView];
    }
    return self;
}

- (instancetype) initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if ( self ) {
        [self _configureParsersManager];
        [self _commonInit];
        [self _createTextView];
    }
    return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    if (self = [super initWithCoder:aDecoder]) {
        [self _configureParsersManager];
        [self _commonInit];
        [self _createTextView];
    }
    return self;
}

- (void) _commonInit
{
    self.subNodes = [NSMutableArray array];
    self.subNodesRanges = [NSMutableArray array];
    self.font = [[self class] defaultFont];
    self.textColor = [UIColor blackColor];
}

- (void) _configureParsersManager
{
    if (_parserManager) {
        return;
    }
    
    _parserManager = [[ParsersManager alloc] init];
    [_parserManager setParsers:[[self class] defaultParsers]];
}

- (void) _createTextView
{
    if (_textView) {
        return;
    }
    
    _textView = [[UITextView alloc] initWithFrame:CGRectZero];
    [self addSubview:_textView];
    _textView.backgroundColor = [UIColor clearColor];
    _textView.scrollEnabled = NO;
    _textView.editable = NO;
    _textView.translatesAutoresizingMaskIntoConstraints = NO;
    _textView.textContainerInset = UIEdgeInsetsMake(0, 0, 0, 0);
    _textView.textContainer.lineFragmentPadding = self.lineFragmentPadding;
    _textView.selectable = NO;
    
    NSMutableArray *textViewConstraints = [NSMutableArray array];
    [textViewConstraints addObjectsFromArray:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[text]-0-|" options:0 metrics:nil views:@{@"text":_textView}]];
    [textViewConstraints addObjectsFromArray:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-0-[text]" options:0 metrics:nil views:@{@"text":_textView}]];
    
    [self addConstraints:textViewConstraints];
    [self layoutIfNeeded];
}

- (void) addParser:(NSObject<RichTextParser> *)parser
{
    [_parserManager addParser:parser];
}

- (void) removeParser:(NSObject<RichTextParser> *)parser
{
    [_parserManager removeParser:parser];
}

- (void) setParsers:(NSArray *)parsers
{
    [_parserManager setParsers:parsers];
}

- (void) setProperty:(id)value forKey:(NSString*)key
{
    if ([key isEqualToString:RichTextViewFontPropertyKey]) {
        if ([value isKindOfClass:[UIFont class]]) {
            self.font = value;
        }
    }
    
    if ([key isEqualToString:RichTextViewColorPropertyKey]) {
        if ([value isKindOfClass:[UIColor class]]) {
            self.textColor = value;
        }
    }
    
    if ([key isEqualToString:RichTextViewLineFragmentPadding]) {
        if ([value isKindOfClass:[NSNumber class]]) {
            NSNumber *number = value;
            self.lineFragmentPadding = number.doubleValue;
        }
    }
}

- (void) setLineFragmentPadding:(CGFloat)lineFragmentPadding
{
    _lineFragmentPadding = lineFragmentPadding;
    _textView.textContainer.lineFragmentPadding = lineFragmentPadding;
}

- (void) setText:(NSString*)text
{
    _originalText = text;
    NSAttributedString * attributedString = [_parserManager parseText:text];
    NSMutableAttributedString * mAttributedString = [[NSMutableAttributedString alloc] initWithAttributedString:attributedString];
    if (self.textColor) {
        [mAttributedString addAttribute:NSForegroundColorAttributeName value:self.textColor range:NSMakeRange(0, mAttributedString.length)];
    }
    if (self.font) {
        [mAttributedString addAttribute:NSFontAttributeName value:self.font range:NSMakeRange(0, mAttributedString.length)];
    }
    [self setAttributedText:mAttributedString];
}

- (void) setAttributedText:(NSAttributedString *)text
{
    if (![text isEqualToAttributedString:_attributedString]) {
        _attributedString = text;
        _textView.attributedText = text;
        [self.subNodes enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop)
        {
            {
                if([obj isKindOfClass:[UIView class]])
                {
                    [obj removeFromSuperview];
                    
                }
            }
        }];
        
        [self.subNodes removeAllObjects];
        [self.subNodesRanges removeAllObjects];
        
        NSTextStorage * textStorage = _textView.textStorage;
        [textStorage enumerateAttributesInRange:NSMakeRange(0, textStorage.length) options:0 usingBlock:^(NSDictionary *attrs, NSRange range, BOOL *stop) {
            
            NSTextAttachment* attachment = [attrs objectForKey:NSAttachmentAttributeName expectedClass:[NSTextAttachment class]];
            
            //有附件
            if (attachment)
            {
                NSNumber *attachmentType = [attrs numberForKey:YYRichTextViewAttachmentTypeKey];
                if(attachmentType)
                {
                    [self.subNodesRanges addObject:[NSValue valueWithRange:range]];
                    
                    UIView *customView = [[self class] getCustomViewWithAttamentType:attachmentType.unsignedIntegerValue attachmentValue:[attrs objectForKey:YYRichTextViewAttachmentValueKey]];
                    [self addSubview:customView];
                    [self.subNodes addObject:customView];
                    
                }
            }
        }];
        [self layoutIfNeeded];
    }
}

- (NSArray *)getCustomViews
{
    return [NSArray arrayWithArray:self.subNodes];
}


-(void) layoutSubviews
{
    [super layoutSubviews];
    NSTextContainer* textContainer = _textView.textContainer;
    NSLayoutManager* layoutManager = _textView.layoutManager;
    CGSize originSize = textContainer.size;
    textContainer.size = CGSizeMake(originSize.width, CGFLOAT_MAX);
    
    __block int currentNodeid = 0;
    [self.subNodesRanges enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
        NSRange range = [(NSValue*)obj rangeValue];
        NSRange glyphRange = [layoutManager glyphRangeForCharacterRange:range actualCharacterRange:NULL];
        CGRect bound = [layoutManager boundingRectForGlyphRange:glyphRange inTextContainer:textContainer];
        if(currentNodeid < [self.subNodes count])
        {
            id obj = [self.subNodes objectAtIndex:currentNodeid];
            
            if([obj isKindOfClass:[UIView class]])
            {
                ((UIView *)obj).frame = bound;
                
            }
        }
        else {
            *stop = YES;
        }
        currentNodeid++;
    }];
    
    textContainer.size = originSize;
}

@end
