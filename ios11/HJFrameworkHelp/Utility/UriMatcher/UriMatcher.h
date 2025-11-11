//
//  UriMatcher.h
//  YY2
//
//  Created by wuwei on 13-12-11.
//  Copyright (c) 2013年 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSInteger, UriMatcherCode)
{
    NO_MATCH = -1,
};

@interface UriMatchResult : NSObject

@property (nonatomic, assign) NSInteger code;
@property (nonatomic, strong) NSDictionary *keyValues;

@end

@interface UriMatcher : NSObject

- (instancetype)initWithCode:(NSInteger)code;

- (void)addURIWithAuthority:(NSString *)authority path:(NSString *)path code:(NSInteger)code;
- (UriMatchResult *)match:(NSURL *)url;
- (UriMatchResult *)matchURLString:(NSString *)urlString;

@end
