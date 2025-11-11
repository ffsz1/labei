//
//  HJAdCache.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//


#import "HJAdCache.h"
#import "HJFaceCore.h"

#define CACHENAME @"XCUserCache"

@interface HJAdCache ()
@property (nonatomic, strong) YYCache *yyCache;
@end

@implementation HJAdCache

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.yyCache = [YYCache cacheWithName:CACHENAME];
    }
    return self;
}

+ (instancetype)shareCache {
    static dispatch_once_t onceToken = 0;
    static id instance;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

- (AdInfo *)getAdInfoFromCacheInMainWith:(NSString *)link {
    if (link.length > 0) {
        if ([self.yyCache containsObjectForKey:link]) {
            return [self.yyCache objectForKey:link];
        }else {
            return nil;
        }
    }else {
        return nil;
    }
    return nil;
}

- (RACSignal *)getAdInfoFromCacheWith:(NSString *)link {

    @weakify(self);
    RACSignal * signal = [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        @strongify(self);
        [self.yyCache containsObjectForKey:link withBlock:^(NSString * _Nonnull key, BOOL contains) {
            if (contains) {
                [self.yyCache objectForKey:key withBlock:^(NSString * _Nonnull key, id<NSCoding>  _Nonnull object) {
                    if (object) {
                        dispatch_async(dispatch_get_main_queue(), ^{
                            [subscriber sendNext:object];
                            [subscriber sendCompleted];
                        });
                    }
                }];
            }else {
                [subscriber sendNext:nil];
            }
        }];
        return nil;
    }];
    return signal;
}


- (void)saveAdInfo:(AdInfo *)adInfo {
    NSArray *stringArr = [adInfo.pict componentsSeparatedByString:@"/"];
    NSString *key = stringArr.lastObject;
    [self.yyCache setObject:adInfo forKey:key withBlock:^{
        
    }];
}

- (void)removeUserInfoWithKey:(NSString *)key {
    [self.yyCache removeObjectForKey:key withBlock:^(NSString * _Nonnull key) {
        
    }];
}

- (void)removeAllObject {
    [self.yyCache removeAllObjects];
}

@end
