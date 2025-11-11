//
//  NSKeyedArchiver+JXBase.m
//  JXCategories
//
//  Created by Colin on 17/1/4.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "NSKeyedArchiver+JXBase.h"

@implementation NSKeyedArchiver (JXBase)

#pragma mark - Base
+ (id)jx_unarchiveObjectWithData:(NSData *)data exception:(__autoreleasing NSException **)exception {
    id object = nil;
    @try {
        object = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    } @catch (NSException *e) {
        if (exception) *exception = e;
    } @finally {
    }
    return object;
}

+ (id)jx_unarchiveObjectWithFile:(NSString *)path exception:(__autoreleasing NSException **)exception {
    id object = nil;
    @try {
        object = [NSKeyedUnarchiver unarchiveObjectWithFile:path];
    } @catch (NSException *e) {
        if (exception) *exception = e;
    } @finally {
    }
    return object;
}

@end
