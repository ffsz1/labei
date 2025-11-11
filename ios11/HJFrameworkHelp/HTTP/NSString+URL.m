//
//  NSString+URL.m
//  YY2
//
//  Created by levyyoung on 13-6-18.
//  Copyright (c) 2013年 YY Inc. All rights reserved.
//

#import "NSString+URL.h"

#ifdef __CONSTANT_CFSTRINGS__
#define CFSTR(cStr)  ((CFStringRef) __builtin___CFStringMakeConstantString ("" cStr ""))
#else
#define CFSTR(cStr)  __CFStringMakeConstantString("" cStr "")
#endif

@implementation NSString (URL)

//+ (NSString*)encodeURL:(NSString *)string
//{
////    NSString *newString = [NSMakeCollectable(CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault, (CFStringRef)string, NULL, CFSTR(":/?#[]@!$ &'()*+,;=\"<>%{}|\\^~`"), CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding))) autorelease];
////    if (newString) {
////        return newString;
////    }
//    return @"";
//}
@end
