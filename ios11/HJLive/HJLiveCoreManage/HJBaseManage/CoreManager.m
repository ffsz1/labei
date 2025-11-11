//
//  CoreManager.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <AdSupport/AdSupport.h>

#import "CoreManager.h"
#import "CoreFactory.h"

#import "HJAuthCoreHelp.h"
#import "PurseCore.h"
#import "HJImLoginCore.h"
#import "HJImFriendCore.h"
#import "HJImMessageCore.h"
#import "HJImRoomCoreV2.h"
#import "HJRoomCoreV2Help.h"

#import "HJUserCoreHelp.h"
#import "HJMediaCore.h"
#import "HJLinkedMeCore.h"
#import "HJAPNSCore.h"



#import "HJReachabilityCore.h"
#import "HJIReachability.h"

#import "HJPhoneCallCore.h"

#import "HJGiftCore.h"
#import "HJMeetingCore.h"
#import "HJNotificationCore.h"

#import "HJFaceCore.h"
#import "HJActivityCore.h"
#import "HJVersionCoreHelp.h"

#import "WebSocketCore.h"
#import "WebSocketReceiveCore.h"
@implementation CoreManager
+ (void)initCore
{
//    [self _registerProtocols];
    GetCore(WebSocketCore);
    GetCore(WebSocketReceiveCore);
    GetCore(HJReachabilityCore);
    GetCore(HJAuthCoreHelp);
    GetCore(PurseCore);
    GetCore(HJGiftCore);
//    GetCore(HJMeetingCore);
    GetCore(HJImLoginCore);
//    GetCore(HJImFriendCore);
    GetCore(HJImMessageCore);
//    GetCore(HJImRoomCoreV2);
    GetCore(HJNotificationCore);
//    GetCore(HJRoomCoreV2Help);
    GetCore(HJUserCoreHelp);
    GetCore(HJLinkedMeCore);
    GetCore(HJAPNSCore);
    GetCore(HJFaceCore);
    GetCore(HJActivityCore);
    GetCore(HJVersionCoreHelp);
    //TODO 优化：减少不必要启动初始化的模块
}

+ (id)getCore:(Class)cls
{
    return [CoreFactory getCoreFromClass:cls];
}

+ (id)getCoreFromProtocol:(Protocol *)protocol
{
    return [CoreFactory getCoreFromProtocol:protocol];
}

+ (void)addClient:(id)obj for:(Protocol *)protocol
{
    [[CommonServiceCenter defaultCenter] addServiceClient:obj withKey:protocol];
}

+ (void)removeClient:(id)obj for:(Protocol *)protocol
{
    [[CommonServiceCenter defaultCenter] removeServiceClient:obj withKey:protocol];
}

+ (void)removeClient:(id)obj
{
    [[CommonServiceCenter defaultCenter] removeServiceClient:obj];
}

+ (void)_registerProtocols
{
#define REGISTER_CORE_PROTOCOL(c, p) [CoreFactory registerClass:([c class]) forProtocol:(@protocol(p))]

#undef REGISTER_CORE_PROTOCOL
}
@end
