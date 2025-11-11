//
//  YPRefreshFactory.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//  隔离mjrefresh

#import <Foundation/Foundation.h>
#import "MJRefresh.h"
NS_ASSUME_NONNULL_BEGIN

@interface YPRefreshFactory : NSObject
+ (MJRefreshHeader *)headerRefreshWithTarget:(id)target refreshingAction:(SEL)action;
+ (MJRefreshFooter *)footerRefreshWithTarget:(id)target refreshingAction:(SEL)action;

+ (MJRefreshHeader *)headerRefreshWithTarget:(id)target refreshingAction:(SEL)action color:(UIColor *)color;
+ (MJRefreshFooter *)footerRefreshWithTarget:(id)target refreshingAction:(SEL)action color:(UIColor *)color;
@end

NS_ASSUME_NONNULL_END

