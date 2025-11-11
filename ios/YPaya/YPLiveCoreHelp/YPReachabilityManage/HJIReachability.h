//
//  HJIReachability.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#ifndef HJIReachability_h
#define HJIReachability_h
typedef enum : NSInteger {
    ReachabilityStatusNotReachable = 0,
    ReachabilityStatusReachableViaWiFi = 2,
    ReachabilityStatusReachableViaWWAN = 1
} ReachabilityStatus;

typedef enum : NSInteger {
    ReachabilityNetNoReachable = 0,
    ReachabilityNetReachable = 1
} ReachabilityNetState;


@protocol HJIReachability <NSObject>

- (ReachabilityStatus)currentStatus;
- (BOOL)isReachable;

@end

@protocol ReachabilityClient <NSObject>

@optional
- (void)reachabilityDidChange:(ReachabilityStatus)currentStatus;
- (void)reachabilityNetStateDidChange:(ReachabilityNetState)currentNetState;

@end
#endif /* HJIReachability_h */
