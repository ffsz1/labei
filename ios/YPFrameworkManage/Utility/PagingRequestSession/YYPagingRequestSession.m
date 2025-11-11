//
//  YYPagingRequestSession.m
//  YYMobileFramework
//
//  Created by wuwei on 14/7/21.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import "YYPagingRequestSession.h"

__unused static NSNotificationCenter *PagingRequestSessionNotificationCenter() {
    static id notificationCenter;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        notificationCenter = [[NSNotificationCenter alloc] init];
    });
    return notificationCenter;
}

@interface YYPagingRequestSession ()

@property (nonatomic, strong) NSMutableOrderedSet *resultSetM;

@property (nonatomic, assign) BOOL isResultSetInvalid;

@end

@implementation YYPagingRequestSession
{
    __weak id _delegate;
    BOOL _isRequesting;
}

@synthesize delegate = _delegate;
@synthesize isRequesting = _isRequesting;

+ (instancetype)instantiatePagingRequestSessionWithDelegate:(id)delegate
{
    return [[self alloc] initWithDelegate:delegate];
}

- (instancetype)initWithDelegate:(id)delegate
{
    self = [super init];
    if (self) {
        _delegate = delegate;
        
        self.resultSetM = [NSMutableOrderedSet orderedSet];
        self.pageSize = 10;
        self.timeout = 15;
    }
    return self;
}

- (void)invalidateData
{
    self.isResultSetInvalid = YES;
}

- (BOOL)invalidateDataAndRequest
{
    [self invalidateData];
    return [self _beginRequestingWithBlock:^{
        if (self.requestBlock) {
            self.requestBlock(0, self.pageSize);
        }
    }];
}

- (void)removeObject:(id)object
{
    [self.resultSetM removeObject:object];
}

- (BOOL)requestNextPage
{
    return [self _beginRequestingWithBlock:^{
        if (self.requestBlock) {
            self.requestBlock(self.isResultSetInvalid ? 0 : self.resultSet.count, self.pageSize);
        }
    }];
}

- (BOOL)_beginRequestingWithBlock:(dispatch_block_t)block
{
    if (!self.isRequesting) {
        _isRequesting = YES;
        
        block();
        
        [self performSelector:@selector(onTimeout) withObject:nil afterDelay:self.timeout];
        
        return YES;
    }
    return NO;
}

- (void)endRequestingAndAppendObjectsFromArray:(NSArray *)objects
{
    if (self.isRequesting) {
        [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(onTimeout) object:nil];
        _isRequesting = NO;
        
        if (self.isResultSetInvalid) {
            [self.resultSetM removeAllObjects];
            self.isResultSetInvalid = NO;
        }
        
        if (objects) {
            [self.resultSetM addObjectsFromArray:objects];
        }
    }
}

- (void)onTimeout
{
    [self endRequestingAndAppendObjectsFromArray:nil];
}

- (NSArray *)resultArray
{
    return [self.resultSetM array];
}

- (NSSet *)resultSet
{
    return [self.resultSetM set];
}

@end
