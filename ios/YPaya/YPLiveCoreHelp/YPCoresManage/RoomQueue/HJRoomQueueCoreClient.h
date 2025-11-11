//
//  RoomQueueCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPIMQueueItem.h"

@protocol HJRoomQueueCoreClient <NSObject>
@optional

- (void)thereIsNoFreePosition;//没有足够的空位
- (void)onMicroQueueUpdate:(NSMutableArray<YPIMQueueItem *> *)micQueue;//麦序状态变化


- (void)onHJRoomMicInfoChange;//micstate改变
- (void)onMicroLocked;//锁麦
- (void)onMicroUnLocked;//解锁麦
- (void)onMicroBeInvite;//被邀请
- (void)onMicroBeKicked;//被踢

- (void)updateMicroQueueView;
- (void)microresetNetSuccess;


- (void)showRoomerTip;
- (void)showInMicroQueue;

- (void)intoMicroSuccess;

//v2放到imroomcorev2
- (void)fetchRoomUserListSuccess;//获取房间用户列表完成





@end
