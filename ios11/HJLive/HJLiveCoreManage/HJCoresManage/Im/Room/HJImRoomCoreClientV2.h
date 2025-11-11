//
//  HJImRoomCoreClientV2.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ChatRoomInfo.h"
#import "HJIMQueueItem.h"

@protocol HJImRoomCoreClientV2 <NSObject>
@optional
- (void)onRoomerExitChatRoomSuccessV2;
//exit room
- (void)onMeExitChatRoomSuccessV2;
//获取队列
- (void)onGetRoomQueueSuccessV2:(NSMutableArray<HJIMQueueItem *> *)info;

//get onlinelist
- (void)fetchRoomUserListSuccess:(int)state;
- (void)fetchRoomUserListNoMoreData;

/**
 获取黑名单列表
 */
- (void)fetchBlackMemberSuccess:(NSArray<ChatRoomMember *> *)member page:(NSInteger)page;
- (void)fetchBlackMemberNoMoreData;
- (void)fetchBlackMemberfailure;

/**
 获取管理员列表
 @param member 成员
 */
- (void)fetchManagerMemberSuccess:(NSArray<ChatRoomMember *> *)member;
- (void)fetchManagerMemberfailure;
//设置管理员、移出管理员
- (void)setManagerMemberSuccess:(BOOL)isManager uid:(UserID)uid;
- (void)setManagerMemberfailure;

//房间信息改变
- (void)onCurrentRoomInfoChanged;
//更新房间在线人数
- (void)onCurrentRoomOnLineUserCountUpdate;
- (void)checkRoomerOnline:(BOOL)online;

- (void)showIsBeDownMic;
@end
