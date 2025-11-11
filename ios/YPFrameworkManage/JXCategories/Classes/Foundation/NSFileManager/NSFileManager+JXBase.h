//
//  NSFileManager+JXBase.h
//  JXCategories
//
//  Created by Colin on 17/1/4.
//  Copyright © 2017年 JuXiao. All rights reserved.
//  Base

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSFileManager (JXBase)

#pragma mark - Base
/**
 获取路径下所有文件或文件夹的大小(字节, error -> -1)

 @param path 文件或文件夹路径
 @return 文件或文件夹的大小(字节, error -> -1)
 */
- (int64_t)jx_fileSizeWithPath:(NSString *)path;

@end

NS_ASSUME_NONNULL_END
