//
//  ParsersManager.m
//  YYMobileFramework
//
//  Created by 小城 on 14-7-2.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import "ParsersManager.h"
#import "RichTextProtocol.h"
#import "SegmentInfomation.h"

@interface ParsersManager()
{
    NSMutableArray *_parsers;
}

@end

@implementation ParsersManager

- (instancetype) init
{
    if (self = [super init]) {
        [self _commonInit];
    }
    return self;
}

- (void) _commonInit
{
    _parsers = [[NSMutableArray alloc] init];
}

- (void) addParser:(NSObject<RichTextParser> *)parser
{
    [_parsers addObject:parser];
}

- (void) removeParser:(NSObject<RichTextParser> *)parser
{
    [_parsers removeObject:parser];
}

- (void) setParsers:(NSArray *)parsers
{
    [_parsers removeAllObjects];
    
    for (id obj in parsers)
    {
        if ([obj conformsToProtocol:@protocol(RichTextParser)])
        {
            [self addParser:obj];
        }
    }
}

- (NSAttributedString*) parseText:(NSString*)text
{
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:text];
    for (NSObject<RichTextParser> * parser in _parsers) {
        NSArray * values = [parser parseText:attributedString.string];
        NSInteger rangeVariable = 0;
        for (SegmentInfomation * seg in values) {
            NSInteger preLength = attributedString.length;
            @try {
                [attributedString replaceCharactersInRange:NSMakeRange(seg.range.location-rangeVariable, seg.range.length) withAttributedString:seg.attrString];
            }
            @catch (NSException *exception) {
                NSLog(@"parse text %@,reason %@",exception.name,exception.reason);
            }
            @finally {
            }
            NSInteger length = attributedString.length;
            rangeVariable += (preLength - length);
        }
    }
    
    return [[NSAttributedString alloc] initWithAttributedString:attributedString];
}

@end
