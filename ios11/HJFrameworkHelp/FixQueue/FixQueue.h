//
//  FixQueue.h
//  YYMobileFramework
//
//  Created by mldongs on 14-6-26.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef BOOL(^FixQueueComparator)(id obj1, id obj2);

@interface FixQueue : NSObject

@property (nonatomic, assign, readonly) int len;

-(instancetype)initWithLenght:(int)len cls:(Class)cls comparator:(FixQueueComparator)comparator;
- (void)add:(NSObject *)obj;
- (void)setData:(NSArray *)arr;

- (NSArray *)array;

@end
