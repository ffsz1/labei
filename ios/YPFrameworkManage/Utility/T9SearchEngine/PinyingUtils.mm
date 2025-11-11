//
//  PinyingUtils.m
//  SDKTestApp
//
//  Created by Huangqun on 12-12-12.
//  Copyright (c) 2012年 YY Inc. All rights reserved.
//

#import "PinyingUtils.h"
#import "base.h"
#import "PinyinTable.h"

@implementation PinyingUtils

+(NSString *)getPinyinSortingKeyOfHanziString:(NSString*) hanziString
{
    if (hanziString == nil) {
        return nil;
    }
	NSUInteger hanziString_len = [hanziString length];
    
    NSMutableString* sortKey1 = [NSMutableString stringWithString:@""];
    //NSString* sortKey = @"";

	for (int i = 0; i < hanziString_len; ++i)
	{
		const WCHAR hanzi = [hanziString characterAtIndex:i];
		LPCSTR pinyin = CPinyinTable::getFirstPinyinOf(hanzi);
        
		if (pinyin != NULL)
		{
//            NSString* toappend = [NSString stringWithFormat:@"%s",pinyin];
//			sortKey = [sortKey stringByAppendingString:toappend];
//			sortKey = [sortKey stringByAppendingString:@"_"];
//            toappend = [NSString stringWithFormat:@"%c",hanzi];
//			sortKey = [sortKey stringByAppendingString:toappend];

            [sortKey1 appendFormat:@"%s_%c`",pinyin,hanzi];
		}
		else
		{
//            sortKey = [sortKey stringByAppendingString:@" "];
//            NSString* toappend = [NSString stringWithFormat:@"%c",hanzi];
//			sortKey = [sortKey stringByAppendingString:toappend];
            
            [sortKey1 appendFormat:@" %c`",hanzi];
            
		}
        
//        sortKey = [sortKey stringByAppendingString:@"`"];
	}
    
    
    return sortKey1;
    
}
+(NSString *) getFirstPinyinSortingKeyOfHanziString :(NSString*) hanziString
{
	//example: "王a二小b" ==> "wang_王` a`er_二`xiao_小` b`"
    
	
	NSUInteger hanziString_len = [hanziString length];
    
    
	NSString* sortKey = @"";
    
	if(hanziString_len > 0)
	{
		const WCHAR hanzi = [hanziString characterAtIndex:0];
		LPCSTR pinyin = CPinyinTable::getFirstPinyinOf(hanzi);
        
		if (pinyin != NULL)
		{
            NSString* toappend = [NSString stringWithFormat:@"%s",pinyin];
			sortKey = [sortKey stringByAppendingString:toappend];
			sortKey = [sortKey stringByAppendingString:@"_"];
            toappend = [NSString stringWithFormat:@"%c",hanzi];
			sortKey = [sortKey stringByAppendingString:toappend];
		}
		else
		{
			sortKey = [sortKey stringByAppendingString:@" "];
            NSString* toappend = [NSString stringWithFormat:@"%c",hanzi];
			sortKey = [sortKey stringByAppendingString:toappend];
		}
        
		sortKey = [sortKey stringByAppendingString:@"`"];
    }
    
    
    return sortKey;
    
}

@end
