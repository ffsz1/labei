//
//  HJNotiFriendCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import <NIMSDK/NIMSDK.h>
#import "UserInfo.h"
#import "HJIMMessage.h"

@interface HJNotiFriendCore : BaseCore

@property (nonatomic, strong)  NSMutableArray <HJIMMessage *> *historyMessage;

- (void)sendChatMessage:(NSString *)message withUserInfo:(UserInfo *)info success:(void (^)(void))success
    failure:(void (^)(NSInteger code, NSString *message))failure;
- (void)enterNotiFriendRoom;
- (void)exitNotiFriendRoom;
- (NSString *)getRoomid;

- (void)getPublcRoomInfo;

- (void)getLobbyChatInfo;

@end
