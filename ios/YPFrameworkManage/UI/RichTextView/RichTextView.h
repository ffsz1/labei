//
//  RichTextView.h
//  YYMobileFramework
//
//  Created by 小城 on 14-7-1.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

//attribute name
extern NSString *const YYRichTextViewAttachmentTypeKey; //value is YYRichTextViewAttachmentType
extern NSString *const YYRichTextViewAttachmentValueKey; // value is self define

extern NSString *const RichTextViewFontPropertyKey; //default systemFont 14.0
extern NSString *const RichTextViewColorPropertyKey; //default black
extern NSString *const RichTextViewLineFragmentPadding; //default 0.0

typedef NS_ENUM(NSUInteger,YYRichTextViewAttachmentType)
{
    YYRichTextViewWebImagePath, //value is NString URL
    YYRichTextViewImWebImagePath,
    YYRichTextViewImLocalImage,//本地图片
};

@protocol RichTextParser;
@interface RichTextView : UIView
{
    @protected
    UITextView *_textView;
    NSString *_originalText;
}

+ (NSArray *) defaultParsers;
+ (UIFont *) defaultFont;

+ (UIView *)getCustomViewWithAttamentType:(YYRichTextViewAttachmentType)type
                          attachmentValue:(id)value;

- (void) addParser:(NSObject<RichTextParser> *)parser;
- (void) removeParser:(NSObject<RichTextParser> *)parser;
- (void) setParsers:(NSArray *)parsers;

- (void) setProperty:(id)value forKey:(NSString*)key;
- (void) setText:(NSString*)text;
- (void) setAttributedText:(NSAttributedString *)text;

//获取自定义的view
- (NSArray *)getCustomViews;
@end
