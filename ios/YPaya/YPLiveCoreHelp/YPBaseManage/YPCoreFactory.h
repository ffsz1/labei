//
//  YPCoreFactory.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPCoreFactory : NSObject
+ (void)registerClass:(Class)cls forProtocol:(Protocol *)protocol;
+ (Class)classForProtocol:(Protocol *)protocol;
+ (BOOL)hasRegisteredProtocol:(Protocol *)protocol;

+ (id)getCoreFromClass:(Class)cls;
+ (id)getCoreFromProtocol:(Protocol *)protocol;
@end

NS_ASSUME_NONNULL_END
