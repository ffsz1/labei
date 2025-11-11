//
//  YPIMRequestManager.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMRequestManager.h"
#import "YPWebSocketCore.h"
#import "HJWebSocketCoreClient.h"

@implementation YPIMRequestManager

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (instancetype)init {
    self = [super init];
    if (self) {
         AddCoreClient(HJWebSocketCoreClient, self);
    }
    return self;
}

+ (instancetype)defaultManager {
    static YPIMRequestManager *instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[[self class] alloc] init];
    });
    return instance;
}

@end
