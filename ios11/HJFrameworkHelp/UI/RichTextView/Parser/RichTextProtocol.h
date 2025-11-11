//
//  RichTextProtocol.h
//  YYMobileFramework
//
//  Created by 小城 on 14-7-1.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

@protocol RichTextParser <NSObject>

@required
- (NSArray *)parseText:(NSString*)text;

@end

@protocol RichTextPrefixParser <RichTextParser>

@required
- (NSString *)prefix;

@end