//
//  HJAdCache.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <YYCache.h>
#import "AdInfo.h"

@interface HJAdCache : NSObject

+ (instancetype)shareCache;

- (AdInfo *)getAdInfoFromCacheInMainWith:(NSString *)link;
- (RACSignal *)getAdInfoFromCacheWith:(NSString *)link;
- (void)saveAdInfo:(AdInfo *)adInfo;
- (void)removeUserInfoWithKey:(NSString *)key;
- (void)removeAllObject;

@end
