//
//  SmileyParser.m
//  YYMobileFramework
//
//  Created by 小城 on 14-7-1.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import "SmileyParser.h"
#import "SegmentInfomation.h"
#import <UIKit/UIKit.h>

@implementation SmileyParser

+(instancetype)shareObject
{
    static id instance;
    static dispatch_once_t token;
    dispatch_once(&token,^{
        instance = [[self alloc] init];
    });
    
    return instance;
}

+ (NSArray *)smileyArr
{
    static NSArray *arr = nil;
    if (arr == nil ) {
        arr = @[
                @[@"/{wx",  @"[微笑]"],
                @[@"/{tp",  @"[调皮]"],
                @[@"/{dx",  @"[大笑]"],
                @[@"/{ll",  @"[流泪]"],
                @[@"/{dy",  @"[得意]"],
                @[@"/{tx",  @"[偷笑]"],
                @[@"/{ka",  @"[可爱]"],
                @[@"/{lh",  @"[流汗]"],
                @[@"/{kun", @"[困]"],
                @[@"/{jy",  @"[惊讶]"],
                @[@"/{pz",  @"[撇嘴]"],
                @[@"/{yun", @"[晕]"],
                @[@"/{ng",  @"[难过]"],
                @[@"/{zs",  @"[衰]"],
                @[@"/{se",  @"[色]"],
                @[@"/{cy",  @"[抽烟]"],
                @[@"/{qd",  @"[敲打]"],
                @[@"/{yb",  @"[拥抱]"],
                @[@"/{mg",  @"[玫瑰]"],
                @[@"/{kw",  @"[枯萎]"],
                @[@"/{zt",  @"[猪头]"],
                @[@"/{wen", @"[吻]"],
                @[@"/{xd",  @"[心动]"],
                @[@"/{xs",  @"[心碎]"],
                @[@"/{zd",  @"[炸弹]"],
                @[@"/{dao", @"[刀]"],
                @[@"/{cc",  @"[臭臭]"],
                @[@"/{kl",  @"[骷髅]"],
                @[@"/{sj",  @"[睡觉]"],
                @[@"/{hx",  @"[害羞]"],
                @[@"/{88",  @"[拜拜]"],
                @[@"/{hk",  @"[很酷]"],
                @[@"/{xu",  @"[嘘]"],
                @[@"/{yw",  @"[疑问]"],
                @[@"/{by",  @"[白眼]"],
                @[@"/{am",  @"[傲慢]"],
                @[@"/{ot",  @"[呕吐]"],
                @[@"/{fd",  @"[奋斗]"],
                @[@"/{kz",  @"[口罩]"],
                @[@"/{hp",  @"[害怕]"],
                @[@"/{dai", @"[发呆]"],
                @[@"/{bz",  @"[闭嘴]"],
                @[@"/{kx",  @"[开心]"],
                @[@"/{fn",  @"[发怒]"],
                @[@"/{zan", @"[赞]"],
                @[@"/{ruo", @"[弱]"],
                @[@"/{ws",  @"[握手]"],
                @[@"/{sl",  @"[胜利]"],
                @[@"/{lw",  @"[礼物]"],
                @[@"/{sd",  @"[闪电]"],
                @[@"/{zk",  @"[抓狂]"],
                @[@"/{bq",  @"[抱拳]"],
                @[@"/{bs",  @"[鄙视]"],
                @[@"/{gz",  @"[鼓掌]"],
                @[@"/{kb",  @"[抠鼻]"],
                @[@"/{kel", @"[可怜]"],
                @[@"/{ok",  @"[OK]"],
                @[@"/{qq",  @"[亲亲]"],
                @[@"/{wq",  @"[委屈]"],
                @[@"/{yx",  @"[阴险]"]];
    }
    return arr;
}

+ (NSUInteger) smileyCount
{
    return [SmileyParser smileyArr].count;
}

+ (NSString *) GetSmileyFilename:(const int)index
{
    NSArray *arr = [SmileyParser smileyArr];
    if (index >= 0 && index < [arr count]) {
        return arr[index][0];
    } else {
        return @"";
    }
}

+ (NSString*) GetSmileyText:(const int) index
{
    NSArray *arr = [SmileyParser smileyArr];
    if (index >= 0 && index < [arr count]) {
        return arr[index][1];
    } else {
        return @"";
    }
}

+ (NSString*) convertSmiley:(NSString*)text
{
    if( [text rangeOfString:@"["].length == 0 || [text rangeOfString:@"]"].length == 0 ) {
        return text;
    }
    
    NSString* str = [NSString stringWithString:text];
    NSArray *arr = [SmileyParser smileyArr];
    for (int i = 0; i < [arr count]; ++i)
    {
        str = [str stringByReplacingOccurrencesOfString:arr[i][1] withString:arr[i][0]];
    }
    return str;
}

- (NSString*)prefix
{
    return @"/{";
}

- (NSArray *)parseText:(NSString *)text
{
    static int maxMatchLengthWithPrefix = 5;
    static int minMatchLengthWithPrefix = 4;
    
    if (text == nil) {
        return nil;
    }
    
    NSMutableArray *retArray;
    NSUInteger textLength = [text length];
    NSRange prefixRange;
    NSRange searchRange = { 0, textLength };
    NSString *prefix = [self prefix];
    NSString *longMatch = nil;  // for smiley with 3 charactors
    NSString *shortMatch = nil; // for smiley with 2 charactors
    
    while (YES)
    {
        prefixRange = [text rangeOfString:prefix options:NSLiteralSearch range:searchRange];
        longMatch = nil;
        shortMatch = nil;
        
        if (prefixRange.length >= prefix.length)
        {
            searchRange.location = prefixRange.location;
            searchRange.length = textLength - searchRange.location;
            
            if (searchRange.length >= maxMatchLengthWithPrefix ) {
                longMatch = [text substringWithRange:NSMakeRange(searchRange.location, maxMatchLengthWithPrefix)];
                shortMatch = [text substringWithRange:NSMakeRange(searchRange.location, minMatchLengthWithPrefix)];
            } else if ( searchRange.length >= minMatchLengthWithPrefix) {
                shortMatch = [text substringWithRange:NSMakeRange(searchRange.location, minMatchLengthWithPrefix)];
            } else {
                break;
            }
            
            NSString *matchSmiley = [self _matchSmiley:shortMatch longKey:longMatch];
            if (matchSmiley) {
                
                if (retArray == nil) {
                    retArray = [NSMutableArray arrayWithCapacity:1];
                }
                
                prefixRange.length = matchSmiley.length;
                NSAttributedString *attrString = [self _smileyAttributedString:matchSmiley];
                SegmentInfomation * segInfo = [SegmentInfomation new];
                segInfo.attrString = attrString;
                segInfo.range = prefixRange;
                [retArray addObject:segInfo];
                searchRange.location = searchRange.location + matchSmiley.length;
                searchRange.length = textLength - searchRange.location;
            }
            else {
                searchRange.location = searchRange.location + prefix.length;
                searchRange.length = textLength - searchRange.location;
            }
        }
        else
        {
            break;
        }
    }
    
    return retArray;
}

- (NSString *) _matchSmiley:(NSString *)shortKey longKey:(NSString *)longKey
{
    static NSMutableDictionary *dict = nil;
    
    if (dict == nil ) {
        dict = [[NSMutableDictionary alloc] init];
        
        NSArray *arr = [SmileyParser smileyArr];
        for (int i=0; i< arr.count; ++i) {
            NSString *smileyText = arr[i][0];
            dict[smileyText] = @YES;
        }
    }
    
    if (longKey ) {
        if (dict[longKey]) {
            return longKey;
        }
    }
    
    if (shortKey) {
        if (dict[shortKey]) {
            return shortKey;
        }
    }
    
    
    return nil;
}

- (NSAttributedString*) _smileyAttributedString:(NSString *)matchSmiley
{
    NSString *smileyImageName = [matchSmiley substringFromIndex:1];
    UIImage *smiley = [UIImage imageNamed:[NSString stringWithFormat:@"%@", smileyImageName]];
    NSTextAttachment *smileyAttachment = [[NSTextAttachment alloc] init];
    smileyAttachment.bounds = CGRectMake(0, 0, smiley.size.width, smiley.size.height);
    smileyAttachment.image = smiley;
    NSAttributedString *smileyAttachmentString = [NSAttributedString attributedStringWithAttachment:smileyAttachment];
    return smileyAttachmentString;
}

@end

@implementation NSString (Smiley)

- (BOOL) hasSuffixSmileyWithEndLocation:(NSUInteger)endLocation startLocation:(NSUInteger*)startLocation
{
    NSString * subString = [self substringToIndex:endLocation];
    if (subString.length < 3) {
        return NO;
    }
    
    NSArray *arr = [SmileyParser smileyArr];
    __block BOOL hasSmiely = NO;
    __block NSUInteger startLoc = 0;
    [arr enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
        if ([obj isKindOfClass:[NSArray class]]) {
            NSString * smileyDes = [obj objectAtIndex:1];
            if ([subString hasSuffix:smileyDes]) {
                *stop = YES;
                hasSmiely = YES;
                startLoc = subString.length - smileyDes.length;
            }
        }
    }];
    
    *startLocation = startLoc;
    
    return hasSmiely;
}

@end
