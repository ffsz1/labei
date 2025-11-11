//
//  ParsersManager.h
//  YYMobileFramework
//
//  Created by 小城 on 14-7-2.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol RichTextParser;
@interface ParsersManager : NSObject

- (void) addParser:(NSObject<RichTextParser> *)parser;
- (void) removeParser:(NSObject<RichTextParser> *)parser;
- (void) setParsers:(NSArray *)parsers;

- (NSAttributedString*) parseText:(NSString*)text;
@end
