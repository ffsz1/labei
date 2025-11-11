//
//  YYPagingRequestSession.h
//  YYMobileFramework
//
//  Created by wuwei on 14/7/21.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void(^YYPagingRequestSessionRequestBlock)(NSUInteger start, NSUInteger count);

// Thread Unsafe
@interface YYPagingRequestSession : NSObject

@property (nonatomic, weak, readonly) id delegate;

@property (nonatomic, strong) YYPagingRequestSessionRequestBlock requestBlock;
@property (nonatomic, assign) NSTimeInterval timeout;   // default is 15 seconds
@property (nonatomic, assign) NSUInteger pageSize;      // default is 10

@property (nonatomic, assign, readonly) BOOL isRequesting;
@property (nonatomic, assign, readonly) BOOL isResultSetInvalid;

+ (instancetype)instantiatePagingRequestSessionWithDelegate:(id)delegate;

- (instancetype)initWithDelegate:(id)delegate;

- (NSArray *)resultArray;
- (NSSet *)resultSet;

- (void)invalidateData;

/**
 *  使当前数据失效并重新进行请求(请求第一页)
 *
 *  @return 如果请求发送成功, 返回YES; 如果当前有请求正在进行, 返回NO
 */
- (BOOL)invalidateDataAndRequest;

/**
 *  请求下一页
 *
 *  @return 如果请求发送成功, 返回YES; 如果当前有请求正在进行, 返回NO
 */
- (BOOL)requestNextPage;

- (void)endRequestingAndAppendObjectsFromArray:(NSArray *)objects;

@end

@interface YYPagingRequestSession (Editable)

- (void)removeObject:(id)object;

@end

@protocol YYPagingRequestSessionDelegate <NSObject>

@end

