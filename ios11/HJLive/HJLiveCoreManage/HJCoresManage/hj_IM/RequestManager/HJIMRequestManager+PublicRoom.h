//
//  HJIMRequestManager+PublicRoom.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJIMRequestManager.h"
#import "HJIMDefines.h"
#import "HJIMPublicRoomInfo.h"

NS_ASSUME_NONNULL_BEGIN

typedef void(^JXIMRequestPublicRoomSuccessHander)(NSArray *messages);
typedef void(^JXIMRequestPublicRoomMembersSuccessHander)(HJIMPublicRoomInfo *roomInfo);

@interface HJIMRequestManager (PublicRoom)

/**
 进入公屏聊天室

 @param roomId  房间id
 @param success success description
 @param failure failure description
 */
+ (void)enterPublicWithRoomId:(NSString *)roomId
                      success:(JXIMRequestPublicRoomSuccessHander)success
                      failure:(JXIMRequestFailureHander)failure;

/**
 发送公屏聊天室消息

 @param roomId  房间id
 @param content 消息内容(任意json)
 @param success success description
 @param failure failure description
 */
+ (void)sendPublicRoomMessage:(NSString *)roomId
                      content:(id)content
                      success:(JXIMRequestSuccessHander)success
                      failure:(JXIMRequestFailureHander)failure;

/**
 获取公聊大厅信息

 @param roomId  房间id
 @param success success description
 @param failure failure description
 */
+ (void)getPublicRoomMembersWithRoomId:(NSString *)roomId
                               success:(JXIMRequestPublicRoomMembersSuccessHander)success
                               failure:(JXIMRequestFailureHander)failure;

@end

NS_ASSUME_NONNULL_END
