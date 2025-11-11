//
//  YPCoreManager.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CommonServiceCenter.h"

#define GetCore(className) ((className *)[YPCoreManager getCore:[className class]])
#define GetCoreI(InterfaceName) ((id<InterfaceName>)[YPCoreManager getCoreFromProtocol:@protocol(InterfaceName)])

#define AddCoreClient(protocolName, client) ([YPCoreManager addClient:client for:@protocol(protocolName)])

#define RemoveCoreClient(protocolName, client) ([YPCoreManager removeClient:client for:@protocol(protocolName)])

#define RemoveCoreClientAll(client) ([YPCoreManager removeClient:client])

#define NotifyCoreClient(protocolName, selector, func) NOTIFY_SERVICE_CLIENT(protocolName, selector, func)


@interface YPCoreManager : NSObject
+ (void)initCore;

+ (id)getCore:(Class)cls;
+ (id)getCoreFromProtocol:(Protocol *)protocol;

+ (void)addClient:(id)obj for:(Protocol *)protocol;
+ (void)removeClient:(id)obj for:(Protocol *)protocol;

+ (void)removeClient:(id)obj;
@end


//全局toast提示
static NSString * const showYYToastKey    = @"show_toast_key";
static NSString * const toastYYContentKey = @"toast_content_key";
