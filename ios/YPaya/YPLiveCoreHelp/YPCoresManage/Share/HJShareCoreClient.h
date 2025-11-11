//
//  ShareCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJShareCoreClient <NSObject>
@optional
- (void)onShareRoomSuccess;
- (void)onShareRoomFailth;

- (void)onShareH5Success;
- (void)onShareH5Failth:(NSString *)message;

- (void)receiveRedPacketWith:(NSString *)packetNum;

@end

