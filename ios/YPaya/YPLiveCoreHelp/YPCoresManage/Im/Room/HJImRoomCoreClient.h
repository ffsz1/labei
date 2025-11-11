//
//  HJImRoomCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
#import "YPChatRoomMember.h"
#import "YPIMMessage.h"

@protocol HJImRoomCoreClient <NSObject>
@optional

//进入最小化的房间
- (void)minimizeEnterSuccess;


//我进入房间
- (void)onMeInterChatRoomSuccess;
- (void)onMeInterChatRoomFailth;
- (void)onMeInterChatRoomInBlackList;
- (void)onMeInterChatRoomBadNetWork;
//链接状态改变
- (void)onConnectionStateChanged:(NIMChatroomConnectionState)state;
//退出房间
- (void)onMeExitChatRoomFailth;
//获取队列
- (void)onGetRoomQueueSuccess:(NSArray<NSDictionary<NSString *,NSString *> *> *)info;
- (void)onGetRoomQueueFailth:(NSString *)message;

//user进入
- (void)onUserInterChatRoom:(YPChatRoomMember *)member;
//user退出
- (void)onUserExitChatRoom:(NSString *)roomId uid:(NSString *)uid;
//user被踢
- (void)onUserBeKicked:(NSString *)roomid;

//manager
- (void)managerAdd:(NSString *)uid;
- (void)managerRemove:(NSString *)uid;
//black
- (void)onUserBeAddBlack:(NSString *)uid;
- (void)onUserBeRemoveBlack:(NSString *)uid;
//麦序
- (void)onRoomQueueUpdate:(NIMMessage *)message;
//micstate
- (void)onRoomInfoUpdate:(NIMMessage *)message;


- (void)letFirstMicroHasUserTokill:(NIMMessage *)message;

- (void)notiMicroQueueKeyPositionHasDown:(NSString *)key message:(YPIMMessage *)message;

//v2 不需要
- (void)onUpdateRoomMemberInfoSuccess:(NIMChatroomMemberInfoUpdateRequest *)request;
- (void)onUpdateRoomMemberInfoFailth:(NSInteger)message;
- (void)onReceiveChatRoomMemberInfoUpdateMessages:(NIMMessage *)message;




@end
