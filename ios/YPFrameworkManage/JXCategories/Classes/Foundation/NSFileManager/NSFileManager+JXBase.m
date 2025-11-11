//
//  NSFileManager+JXBase.m
//  JXCategories
//
//  Created by Colin on 17/1/4.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "NSFileManager+JXBase.h"

@implementation NSFileManager (JXBase)

#pragma mark - Base
- (int64_t)jx_fileSizeWithPath:(NSString *)path {
    BOOL isDirectory = NO;
    BOOL exists = [self fileExistsAtPath:path isDirectory:&isDirectory];
    
    if (exists == NO) return -1; // 文件夹或文件不存在
    // 文件
    if (!isDirectory)  {
        NSDictionary *attributes = [self attributesOfItemAtPath:path error:nil];
        return [attributes[NSFileSize] unsignedLongLongValue];
    }
    
    // 文件夹
    int64_t totalByteSize = 0;
    NSDirectoryEnumerator *enumerator = [self enumeratorAtPath:path];
    
    for (NSString *subpath in enumerator) {
        NSString *fullSubpath = [path stringByAppendingPathComponent:subpath]; // 获得文件全路径
        BOOL isDirectory = NO;
        [self fileExistsAtPath:fullSubpath isDirectory:&isDirectory];
        // 文件
        if (!isDirectory) {
            // 获取文件属性
            NSDictionary *attributes = [self attributesOfItemAtPath:fullSubpath error:nil];
            // 获取文件大小
            totalByteSize += [attributes[NSFileSize] unsignedLongLongValue];
        }
    }
    return totalByteSize;
}

@end
