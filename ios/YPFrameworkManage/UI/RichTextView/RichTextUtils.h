//
//  RichTextUtils.h
//  YYMobileFramework
//
//  Created by 小城 on 14-7-31.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreGraphics/CoreGraphics.h>

@interface RichTextUtils : NSObject

+ (instancetype) shareUtils;

- (CGSize) sizeForRichTextViewWithMaxWidth:(CGFloat)maxWidth attributedText:(NSAttributedString*)text propertyMap:(NSDictionary*)propertyMap;
- (CGSize) sizeForRichTextViewWithMaxWidth:(CGFloat)maxWidth text:(NSString*)text propertyMap:(NSDictionary*)propertyMap;
- (CGSize) sizeForRichTextViewWithMaxWidth:(CGFloat)maxWidth text:(NSString*)text propertyMap:(NSDictionary*)propertyMap parsers:(NSArray*)parsers;

@end
