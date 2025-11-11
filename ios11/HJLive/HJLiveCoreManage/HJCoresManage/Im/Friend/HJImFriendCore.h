//
//  HJImFriendCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "BaseCore.h"
#import "UserInfo.h"
#import <Foundation/Foundation.h>

@interface HJImFriendCore : BaseCore


- (NSArray<UserInfo *> *)getMyFriends;
- (NSArray<UserInfo *> *)getBlackList;
- (NSArray<UserInfo *> *)getMuteList;

- (void)updateMyFriends;
- (void)updateBlackList;

- (BOOL) isMyFriend:(NSString *)accid;
- (void) addFriend:(NSString *)accid;
- (void) deleteFriend:(NSString *)accid;
- (void) requestFriend:(NSString *)accid requestMsg:(NSString *)requestMsg;
- (void) addToBlackList:(NSString *)accid;
- (void) removeFromBlackList:(NSString *)accid;
- (BOOL) isUserInBlackList:(NSString *)accid;

- (void)sendInviteMessageWithUid:(NSString *)uid;
@end
