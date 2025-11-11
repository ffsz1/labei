//
//  UriMatcher.m
//  YY2
//
//  Created by wuwei on 13-12-11.
//  Copyright (c) 2013年 YY Inc. All rights reserved.
//

#import "UriMatcher.h"

@interface UriMatchResult ()

@end

@implementation UriMatchResult

@synthesize code;
@synthesize keyValues;

@end

@interface UriMatcher ()

/**
 * 此枚举值除了作为唯一标识外，值的大小还作为匹配冲突时的权重，权重越小，越先匹配
 * 比如 yymobile://1931/Home 跟 yymobile://1931/{*encodedURL} 这两个是会冲突的，Home 可能会被当成 {*encodedURL}
 * 那可以将 Home 的值设置的比 {*encodedURL} 的值高，从而你保证优先匹配 Home
 * by 镇波
 */
@property (nonatomic, assign) NSInteger code;
@property (nonatomic, assign) NSInteger which;
@property (nonatomic, strong) NSString *text;
@property (nonatomic, strong) NSString *key;
@property (nonatomic, strong, readonly) NSMutableArray *children;

@end

@implementation UriMatcher
{
@private
    NSInteger _code;
    NSInteger _which;
    NSString *_text;
    NSMutableArray *_children;
    NSString *_key;
}

@synthesize code = _code;
@synthesize which = _which;
@synthesize text = _text;
@synthesize children = _children;
@synthesize key = _key;

// Internal match codes
static const NSInteger EXACT     = 0;
static const NSInteger NUMBER    = 1;
static const NSInteger TEXT      = 2;

static NSString * const PATH_SEPARATOR = @"/";
static NSString * const NUMBER_MATCH_PATTERN = @"^\\{#[\\w]*\\}$";
static NSString * const TEXT_MATCH_PATTERN = @"^\\{\\*[\\w]*\\}$";

- (instancetype)initWithCode:(NSInteger)code
{
    self = [super init];
    if (self) {
        _code = code;
        _which = -1;
        _children = [NSMutableArray array];
        _text = nil;
    }
    return self;
}

- (instancetype)init
{
    self = [self initWithCode:NO_MATCH];
    return self;
}

- (void)addURIWithAuthority:(NSString *)authority path:(NSString *)path code:(NSInteger)code
{
    if (code < 0) {
        @throw [NSException exceptionWithName:@"IllegalArgumentException" reason:[NSString stringWithFormat:@"Code %ld is invalid: it must be positive", (long)code] userInfo:nil];
    }
    
    NSArray *tokens = [path componentsSeparatedByString:PATH_SEPARATOR];
    NSUInteger numberTokens = tokens.count;
    UriMatcher *node = self;
    UriMatcher *parentNode = nil;
    
    for (int i = -1; i < (int)numberTokens; i++)
    {
        NSString *token = i < 0 ? authority : [tokens objectAtIndex:i];
        NSArray *children = node.children;
        NSUInteger numberChildren = children.count;
        int j;
        UriMatcher *child;
        for (j = 0; j < numberChildren; j++) {
            child = [children objectAtIndex:j];
            if (child.text != nil && [token isEqualToString:child.text]) {
                parentNode = node;
                node = child;
                break;
            }
        }
        
        if (j == numberChildren) {
            // Child not found, create it
            child = [[UriMatcher alloc] init];
            // like {#uid}
            
            do {
                NSRegularExpression *numberReg = [NSRegularExpression regularExpressionWithPattern:NUMBER_MATCH_PATTERN options:NSRegularExpressionCaseInsensitive error:nil];
                NSRange tokenRange = NSMakeRange(0, token.length);
                NSRange matchRange = [numberReg rangeOfFirstMatchInString:token options:NSMatchingAnchored range:tokenRange];
                if (matchRange.location != NSNotFound)
                {
                    if (NSEqualRanges(matchRange, tokenRange)) {
                        // which = 1
                        child.which = NUMBER;
                        child.text = token;
                        child.key = [token substringWithRange:NSMakeRange(matchRange.location + 2, matchRange.length - 3)];
                    }
                    else
                    {
                        @throw [NSException exceptionWithName:@"IllegalArgumentException" reason:[NSString stringWithFormat:@"Path component %@ must full match with {#[\\w]*}", token] userInfo:nil];
                    }
                    break;
                }
                
                NSRegularExpression *textReg = [NSRegularExpression regularExpressionWithPattern:TEXT_MATCH_PATTERN options:NSRegularExpressionCaseInsensitive error:nil];
                matchRange = [textReg rangeOfFirstMatchInString:token options:NSMatchingAnchored range:tokenRange];
                if (matchRange.location != NSNotFound)
                {
                    if (NSEqualRanges(matchRange, tokenRange)) {
                        // which = 2
                        child.which = TEXT;
                        child.text = token;
                        child.key = [token substringWithRange:NSMakeRange(matchRange.location + 2, matchRange.length - 3)];
                    }
                    else
                    {
                        @throw [NSException exceptionWithName:@"IllegalArgumentException" reason:[NSString stringWithFormat:@"Path component %@ must full match with {*[\\w]*}", token] userInfo:nil];
                    }
                    break;
                }
                
                child.which = EXACT;
                child.text = token;
                
            } while (0);
            
            [node.children addObject:child];
            parentNode = node;
            node = child;
        }
    }

    node.code = code;
    
    // 按照 Code 值的大小进行降序排序  by 镇波
    [parentNode.children sortUsingComparator:^NSComparisonResult(id obj1, id obj2) {
        UriMatcher *node1 = (UriMatcher *)obj1;
        UriMatcher *node2 = (UriMatcher *)obj2;
        NSComparisonResult result = NSOrderedDescending;
        if ([node1 isKindOfClass:[UriMatcher class]] && [node2 isKindOfClass:[UriMatcher class]]) {
            result = node1.code > node2.code;
        }
        return result;
    }];
}

- (UriMatchResult *)match:(NSURL *)url
{
    UriMatchResult *result = [UriMatchResult new];
    NSMutableDictionary *keyValues = [NSMutableDictionary dictionary];
    
    NSPredicate *filter = [NSPredicate predicateWithFormat:@"NOT (SELF == %@)", PATH_SEPARATOR];
    NSArray *pathComponents = [[url pathComponents] filteredArrayUsingPredicate:filter];
    [YYLogger info:TNavigate message:@"pathCompoents: %@",pathComponents];
    NSUInteger pathComponentsCount = pathComponents.count;
    if (pathComponentsCount == 0 && url.host == nil)
    {
        result.code = self.code;
    }
    else
    {
        UriMatcher *node = self;
        int n = (int)pathComponentsCount;
        for (int i = -1; i < n; ++i)
        {
            NSString * component = i < 0 ? url.host : pathComponents[i];
            NSArray *children = node.children;
            if (children == nil) {
                break;
            }
            node = nil;
            for (UriMatcher *child in children)
            {
                switch (child.which) {
                    case EXACT:
                    {
                        // TODO：openUrl打开app时 url 全是小写
                        if ([child.text compare:component options:NSCaseInsensitiveSearch] == NSOrderedSame) {
                            node = child;
                        }
                        break;
                    }
                    case NUMBER:
                    {
                        NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
                        NSNumber *number = [numberFormatter numberFromString:component];
                        if (number != nil) {
                            // Right number format
                            [keyValues setObject:number forKey:child.key];
                            node = child;
                        }
                        break;
                    }
                        
                    case TEXT:
                    {
                        [keyValues setObject:component forKey:child.key];
                        node = child;
                        break;
                    }
                    default:
                        break;
                }
                if (node != nil) {
                    break;
                }
            }
            if (node == nil) {
                result.code = NO_MATCH;
            }
        }
        if (node != nil) {
            result.code = node.code;
            result.keyValues = keyValues;
        }
    }
    return result;
}

- (UriMatchResult *)matchURLString:(NSString *)urlString
{
    if (urlString.length == 0) {
        UriMatchResult *result = [[UriMatchResult alloc] init];
        result.code = NO_MATCH;
        return result;
    }
    return [self match:[NSURL URLWithString:urlString]];
}


- (NSString *)description
{
    return [NSString stringWithFormat:@"Text:%@, Code:%@, Children:%@, Which:%@",
            self.text, @(self.code), self.children, @(self.which)];
}

@end