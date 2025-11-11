//
//  SearchCore.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"

@interface HJSearchCoreHelp : BaseCore

/**
 通过关键字搜索
 
 @param key 关键字
 */
- (void)searchWithKey:(NSString *)key;


- (void)searchUser:(NSString *)key pageNo:(NSInteger)pageNo pageSize:(NSInteger)pageSize;
- (void)searchRoom:(NSString *)key;
@end
