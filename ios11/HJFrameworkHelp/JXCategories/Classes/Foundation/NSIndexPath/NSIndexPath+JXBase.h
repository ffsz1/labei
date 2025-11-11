//
//  NSIndexPath+JXBase.h
//  JXCategories
//
//  Created by Colin on 17/1/4.
//  Copyright © 2017年 JuXiao. All rights reserved.
//  Base

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSIndexPath (JXBase)

#pragma mark - Base
/**
 判断两个IndexPath是否相等

 @param other 另一个indexPath
 @return 相等返回YES, 否则返回NO
 */
- (BOOL)jx_isEqualToIndexPath:(NSIndexPath *)other;

@end

NS_ASSUME_NONNULL_END
