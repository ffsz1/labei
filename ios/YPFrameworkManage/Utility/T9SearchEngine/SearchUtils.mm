//
//  SearchUtils.m
//  YY2
//
//  Created by YiXiang Yu on 3/19/13.
//  Copyright (c) 2013 YY Inc. All rights reserved.
//

#import "SearchUtils.h"
#import "T9SearchEngine.h"

@interface SearchUtils()
{
    CT9SearchEngine* t9engine;
}

@end

@implementation SearchUtils

- (void)addSentence:(NSString*)sentence flag:(unsigned)sentenceFlags token:(const unsigned long long)token
{
    if (t9engine == nil ) {
        t9engine =  new CT9SearchEngine;
    }
    if (t9engine == nil){
        return;
    }
    if (sentence == nil){
        return;
    }
    LPCWSTR text = (LPCWSTR)[sentence cStringUsingEncoding:NSUnicodeStringEncoding];
    t9engine->addSentence(text, sentenceFlags, token);
}

- (void)removeAllSentences
{
    if (t9engine == nil ) {
        t9engine =  new CT9SearchEngine;
    }
    t9engine->removeAllSentences();
}

- (NSArray*)search:(NSString*)t9key falg:(unsigned)searchingFlags
{
    if (t9engine == nil ) {
        t9engine =  new CT9SearchEngine;
    }
    std::deque<unsigned long long> result;
    LPCWSTR text = (LPCWSTR)[t9key cStringUsingEncoding:NSUnicodeStringEncoding];
    t9engine->search(text, searchingFlags, &result);
    
    NSMutableArray* array = [NSMutableArray arrayWithCapacity:(result.size())];
    for (std::deque<unsigned long long>::iterator it = result.begin(); it != result.end(); ++it) {
        [array addObject:[NSNumber numberWithUnsignedLongLong:*it]];
    }
    return array;
}

@end
