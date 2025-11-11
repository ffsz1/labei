//
//  SearchUtils.h
//  YY2
//
//  Created by YiXiang Yu on 3/19/13.
//  Copyright (c) 2013 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SearchUtils : NSObject

- (void)addSentence:(NSString*)sentence flag:(unsigned)sentenceFlags token:(const unsigned long long)token;

- (void)removeAllSentences;

- (NSArray*)search:(NSString*)t9key falg:(unsigned)searchingFlags;

@end
