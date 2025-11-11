//
//  HJIMRequestManager.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^JXIMRequestSuccessHander)(void);
typedef void(^JXIMRequestFailureHander)(NSInteger code, NSString *errorMessage);

@interface HJIMRequestManager : NSObject <HJWebSocketCoreClient>

+ (instancetype)defaultManager;

@end

NS_ASSUME_NONNULL_END
