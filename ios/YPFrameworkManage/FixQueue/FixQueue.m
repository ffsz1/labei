//
//  FixQueue.m
//  YYMobileFramework
//
//  Created by mldongs on 14-6-26.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//  队首插入，超过长度队尾删除
//

#import "FixQueue.h"

@interface FixQueue ()
@property (nonatomic,strong) NSMutableArray *internalArray;
@property (nonatomic, copy) FixQueueComparator comparator;
@property (nonatomic,assign) int len;

@end

@implementation FixQueue
{
    Class _class;
}

-(instancetype)initWithLenght:(int)len cls:(Class)cls comparator:(FixQueueComparator)comparator
{
    NSParameterAssert(cls && comparator);
    self = [super init];
    if (self) {
        self.internalArray = [NSMutableArray array];
        _len = len;
        _class = cls;
        _comparator = comparator;
    }
    return self;
}

- (NSArray *)array
{
    return [self.internalArray copy];
}

-(void)add:(NSObject *)obj
{
    NSParameterAssert([obj isKindOfClass:_class]);
    if (![obj isKindOfClass:_class]) {
        
        [YYLogger error:TDatabase message:@"wrong class type"];
        NSException *exp = [NSException exceptionWithName:@"wrong class exception" reason:@"wrong class type" userInfo:nil];
        [exp raise];
        
        return;
    }
    
    int index = [self index:obj];
    NSUInteger count = [self.array count];
    
    if (index == -1) {
        if (count >= self.len) {
            [self.internalArray removeLastObject];
        }
    }
    else{
        [self.internalArray removeObjectAtIndex:index];
    }
    
    [self.internalArray insertObject:obj atIndex:0];
}

- (void)setData:(NSArray *)arr
{
    [self.internalArray removeAllObjects];
    [self.internalArray addObjectsFromArray:arr];
}

-(int)index:(NSObject *)obj
{
    NSUInteger len = [self.array count];
    for(int i=0; i<len; ++i)
    {
        if(self.comparator([self.array objectAtIndex:i], obj))
        {
            return i;
        }
    }
    return -1;
}

@end
