//
//  NSString+Regex.h
//  YYMobileFramework
//
//  Created by wuwei on 14/6/11.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (Regex)

/*!
 @method			matchesRegex:options:
 @discussion		This method determines whether the pattern is matched.
 @param pattern		The regular expression to be used.
 @param options		The options to be used.
 @return			A boolean value denoting whether the pattern was matched.
 @see				http://developer.apple.com/library/ios/#documentation/Foundation/Reference/NSRegularExpression_Class/Reference/Reference.html
 @see				http://quickies.seriot.ch/index.php?id=279
 */
- (BOOL)matchesRegex: (NSString *)pattern options:(NSRegularExpressionOptions)options;

- (BOOL)isPhoneNumber;

- (BOOL)isPureNumber;

+ (BOOL)isEmpty:(NSString *) str;

@end
