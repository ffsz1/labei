//
//  YPIMRequestManager+PublicRoom.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMRequestManager+PublicRoom.h"

@implementation YPIMRequestManager (PublicRoom)

/**
 进入公屏聊天室
 */
+ (void)enterPublicWithRoomId:(NSString *)roomId
                      success:(JXIMRequestPublicRoomSuccessHander)success
                      failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" :JX_STR_AVOID_nil(roomId),
                             };
    
    [GetCore(YPWebSocketCore) send:JX_IM_ROUTE_ENTER_PUBLIC_ROOM content:params success:^(id data) {
        NSArray *message = data[@"his_list"];
        !success ?: success(message);
    } failure:^(NSInteger code, NSString *errmsg) {
        !failure ?: failure(code, errmsg);
    }];
}

/**
 发送公屏聊天室消息
 */
+ (void)sendPublicRoomMessage:(NSString *)roomId
                      content:(id)content
                      success:(JXIMRequestSuccessHander)success
                      failure:(JXIMRequestFailureHander)failure {
    NSDictionary *dataBuffer = [content yy_modelToJSONObject];
    NSDictionary *params = @{
                             @"room_id" :JX_STR_AVOID_nil(roomId),
                             @"custom" : JX_OBJECT_AVOID_nil(dataBuffer, NSDictionary),
                             };
    
    [GetCore(YPWebSocketCore) send:JX_IM_ROUTE_SEND_PUBLIC_ROOM_MESSAGE content:params success:^(id data) {
        !success ?: success();
    } failure:^(NSInteger code, NSString *errmsg) {
        !failure ?: failure(code, errmsg);
    }];
}

+ (void)getPublicRoomMembersWithRoomId:(NSString *)roomId
                               success:(JXIMRequestPublicRoomMembersSuccessHander)success
                               failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" :JX_STR_AVOID_nil(roomId),
                             @"uid" : JX_STR_AVOID_nil([GetCore(YPAuthCoreHelp) getUid]),
                             @"ticket" : JX_STR_AVOID_nil([GetCore(YPAuthCoreHelp) getTicket]),
                             };
    
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_PUBLIC_ROOM_INFO params:params success:^(id data) {
        YPIMPublicRoomInfo *roomInfo = [YPIMPublicRoomInfo yy_modelWithJSON:data];
        !success ?: success(roomInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

@end
