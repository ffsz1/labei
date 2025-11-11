//
//  YPNotiFriendCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"
#import <NIMSDK/NIMSDK.h>
#import "UserInfo.h"
#import "YPIMMessage.h"

@interface YPNotiFriendCore : YPBaseCore

@property (nonatomic, strong)  NSMutableArray <YPIMMessage *> *historyMessage;

- (void)sendChatMessage:(NSString *)message withUserInfo:(UserInfo *)info success:(void (^)(void))success
    failure:(void (^)(NSInteger code, NSString *message))failure;
- (void)enterNotiFriendRoom;
- (void)exitNotiFriendRoom;
- (NSString *)getRoomid;

- (void)getPublcRoomInfo;

- (void)getLobbyChatInfo;

@end
