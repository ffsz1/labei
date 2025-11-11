//
//  YPIMRequestManager+RoomMessage.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMRequestManager.h"

#import "YPChatRoomMember.h"

/**
 房间消息管理
 */
@interface YPIMRequestManager (RoomMessage)
/**
 发送非文本消息

 @param roomId  房间Id
 @param data    任意json
 @param success success description
 @param failure failure description
 */
+ (void)sendMessageWithRoomId:(NSString *)roomId
                       member:(YPChatRoomMember *)member
                         data:(id)data
                      success:(JXIMRequestSuccessHander)success
                      failure:(JXIMRequestFailureHander)failure;

/**
 发送文本消息

 @param roomId  房间Id
 @param member  成员信息
 @param content 文本内容
 @param success success description
 @param failure failure description
 */
+ (void)sendTextWithRoomId:(NSString *)roomId
                    member:(YPChatRoomMember *)member
                   content:(NSString *)content
                   success:(JXIMRequestSuccessHander)success
                   failure:(JXIMRequestFailureHander)failure;

@end
