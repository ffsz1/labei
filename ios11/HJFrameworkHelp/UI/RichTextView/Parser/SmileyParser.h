//
//  SmileyParser.h
//  YYMobileFramework
//
//  Created by 小城 on 14-7-1.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RichTextProtocol.h"

@interface SmileyParser : NSObject<RichTextPrefixParser>

+(instancetype)shareObject;

+ (NSUInteger) smileyCount;
+ (NSString *) GetSmileyFilename:(const int)index;
+ (NSString *) GetSmileyText:(const int) index;
+ (NSString*) convertSmiley:(NSString*)text;

@end

@interface NSString (Smiley)

- (BOOL) hasSuffixSmileyWithEndLocation:(NSUInteger)endLocation startLocation:(NSUInteger*)startLocation;

@end