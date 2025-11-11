//
//  HJReachabilityCore.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJReachabilityCore.h"
#import "YYReachability.h"
#import "CoreManager.h"
#import "YYLogger.h"
@interface HJReachabilityCore ()
{
    YYReachability *_reachability;
    ReachabilityStatus _status;
    ReachabilityNetState _netState;
}

@end
@implementation HJReachabilityCore
- (id)init
{
    if (self = [super init])
    {
        _reachability = [YYReachability reachabilityForInternetConnection];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reachabilityChanged) name:kYYReachabilityChangedNotification object:nil];
        [_reachability startNotifier];
        _status = (ReachabilityStatus)[_reachability currentReachabilityStatus];
        _netState = (_status == ReachableViaWiFi || _status == ReachableViaWWAN) ? ReachabilityNetReachable : ReachabilityNetNoReachable;
        [YYLogger info:TChannel message:@"current reachability status %d", (int)_status];
    }
    return self;
}

- (ReachabilityStatus)currentStatus
{
    return _status;
}

- (BOOL)isReachable
{
    return [self currentStatus] != ReachabilityStatusNotReachable;
}

- (void)reachabilityChanged
{
    _status = (ReachabilityStatus)[_reachability currentReachabilityStatus];
    [YYLogger debug:TReachability message:@"reachability status:%ld", (long)_status];
    NotifyCoreClient(ReachabilityClient, @selector(reachabilityDidChange:), reachabilityDidChange:_status);
    
    ReachabilityNetState tmpNetState = (_status == ReachableViaWiFi || _status == ReachableViaWWAN) ? ReachabilityNetReachable : ReachabilityNetNoReachable;
    if (tmpNetState != _netState) {
        _netState = tmpNetState;
        NotifyCoreClient(ReachabilityClient, @selector(reachabilityNetStateDidChange:), reachabilityNetStateDidChange:_netState);
    }
}
@end
