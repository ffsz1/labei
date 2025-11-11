//
//  YPAdCache.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <YYCache.h>
#import "YPAdInfo.h"

@interface YPAdCache : NSObject

+ (instancetype)shareCache;

- (YPAdInfo *)getAdInfoFromCacheInMainWith:(NSString *)link;
- (RACSignal *)getAdInfoFromCacheWith:(NSString *)link;
- (void)saveAdInfo:(YPAdInfo *)adInfo;
- (void)removeUserInfoWithKey:(NSString *)key;
- (void)removeAllObject;

@end
