//
//  HJIMRequestManager+RoomMessage.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJIMRequestManager+RoomMessage.h"

#import "HJIMDefines.h"

@implementation HJIMRequestManager (RoomMessage)

/**
 发送非文本消息
 */
+ (void)sendMessageWithRoomId:(NSString *)roomId
                       member:(ChatRoomMember *)member
                         data:(id)data
                      success:(JXIMRequestSuccessHander)success
                      failure:(JXIMRequestFailureHander)failure {
    NSDictionary *buffer = [member yy_modelToJSONObject];
    NSDictionary *DataBuffer = [data yy_modelToJSONObject];
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                              @"member" : JX_OBJECT_AVOID_nil(buffer, NSDictionary),
                             @"custom" : JX_OBJECT_AVOID_nil(DataBuffer, NSDictionary),
                             };
    
    [GetCore(WebSocketCore) send:JX_IM_ROUTE_SEND_MESSAGE content:params success:^(id data) {
        !success ?: success();
    } failure:^(NSInteger code, NSString *errmsg) {
        !failure ?: failure(code, errmsg);
    }];
}

/**
 发送文本消息
 */
+ (void)sendTextWithRoomId:(NSString *)roomId
                    member:(ChatRoomMember *)member
                   content:(NSString *)content
                   success:(JXIMRequestSuccessHander)success
                   failure:(JXIMRequestFailureHander)failure {
    NSDictionary *buffer = [member yy_modelToJSONObject];
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"member" : JX_OBJECT_AVOID_nil(buffer, NSDictionary),
                             @"content" : JX_STR_AVOID_nil(content),
                             };
    
    [GetCore(WebSocketCore) send:JX_IM_ROUTE_SEND_TEXT content:params success:^(id data) {
        !success ?: success();
    } failure:^(NSInteger code, NSString *errmsg) {
        !failure ?: failure(code, errmsg);
    }];
}

@end
