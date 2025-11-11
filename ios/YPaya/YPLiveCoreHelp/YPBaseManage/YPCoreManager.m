//
//  YPCoreManager.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <AdSupport/AdSupport.h>

#import "YPCoreManager.h"
#import "YPCoreFactory.h"

#import "YPAuthCoreHelp.h"
#import "YPPurseCore.h"
#import "YPImLoginCore.h"
#import "YPImFriendCore.h"
#import "YPImMessageCore.h"
#import "YPImRoomCoreV2.h"
#import "YPRoomCoreV2Help.h"

#import "YPUserCoreHelp.h"
#import "YPMediaCore.h"
#import "YPLinkedMeCore.h"
#import "YPAPNSCore.h"



#import "YPReachabilityCore.h"
#import "HJIReachability.h"

#import "YPPhoneCallCore.h"

#import "YPGiftCore.h"
#import "YPMeetingCore.h"
#import "YPNotificationCore.h"

#import "YPFaceCore.h"
#import "YPActivityCore.h"
#import "YPVersionCoreHelp.h"

#import "YPWebSocketCore.h"
#import "YPWebSocketReceiveCore.h"
@implementation YPCoreManager
+ (void)initCore
{
//    [self _registerProtocols];
    GetCore(YPWebSocketCore);
    GetCore(YPWebSocketReceiveCore);
    GetCore(YPReachabilityCore);
    GetCore(YPAuthCoreHelp);
    GetCore(YPPurseCore);
    GetCore(YPGiftCore);
//    GetCore(YPMeetingCore);
    GetCore(YPImLoginCore);
//    GetCore(YPImFriendCore);
    GetCore(YPImMessageCore);
//    GetCore(YPImRoomCoreV2);
    GetCore(YPNotificationCore);
//    GetCore(YPRoomCoreV2Help);
    GetCore(YPUserCoreHelp);
    GetCore(YPLinkedMeCore);
    GetCore(YPAPNSCore);
    GetCore(YPFaceCore);
    GetCore(YPActivityCore);
    GetCore(YPVersionCoreHelp);
    //TODO 优化：减少不必要启动初始化的模块
}

+ (id)getCore:(Class)cls
{
    return [YPCoreFactory getCoreFromClass:cls];
}

+ (id)getCoreFromProtocol:(Protocol *)protocol
{
    return [YPCoreFactory getCoreFromProtocol:protocol];
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
#define REGISTER_CORE_PROTOCOL(c, p) [YPCoreFactory registerClass:([c class]) forProtocol:(@protocol(p))]

#undef REGISTER_CORE_PROTOCOL
}
@end
