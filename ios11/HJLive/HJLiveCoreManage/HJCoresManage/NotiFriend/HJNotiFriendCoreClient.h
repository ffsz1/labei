//
//  HJNotiFriendCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HJIMMessage.h"
#import "HJIMPublicRoomInfo.h"

@protocol HJNotiFriendCoreClient <NSObject>
@optional
- (void)enterNotiFriendSuccess;
- (void)enterNotiFriendFail;

- (void)showBanndMessage;

- (void)onPublicMessagesDidUpdate:(NSArray<HJIMMessage *> *)messages;

- (void)getPublicRoomInfoSuccess:(HJIMPublicRoomInfo *)roomInfo;
- (void)getPublicRoomInfoFail;


- (void)getLobbyRoomInfoSuccess:(NSArray *)arr;
- (void)getLobbyRoomInfoFail;

@end
