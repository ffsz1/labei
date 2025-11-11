//
//  YPIMRequestManager+Room.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMRequestManager+Room.h"
#import "YPIMDefines.h"
#import "YPImRoomCoreV2.h"

@implementation YPIMRequestManager (Room)
/**
 进入聊天室
 */
+ (void)enterRoomWithRoomId:(NSString *)roomId
                    success:(JXIMRequestEnterRoomSuccessHander)success
                    failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             };

    [GetCore(YPWebSocketCore) send:JX_IM_ROUTE_ENTER_ROOM content:params success:^(id data) {
        YPChatRoomInfo *roomInfo = [YPChatRoomInfo yy_modelWithJSON:data[@"room_info"]];
        YPChatRoomMember *member = [YPChatRoomMember yy_modelWithJSON:data[@"member"]];
        NSArray<YPIMQueueItem *> *queueInfos = [NSArray yy_modelArrayWithClass:[YPIMQueueItem class] json:data[@"queue_list"]];
        !success ?: success(roomInfo, member, queueInfos);
    } failure:^(NSInteger code, NSString *errmsg) {
        !failure ?: failure(code, errmsg);
    }];
}

/**
 退出聊天室
 */
+ (void)exitRoomWithRoomId:(NSString *)roomId
                   success:(JXIMRequestSuccessHander)success
                   failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             };
    
    [GetCore(YPWebSocketCore) send:JX_IM_ROUTE_EXIT_ROOM content:params success:^(id data) {
        !success ?: success();
    } failure:^(NSInteger code, NSString *errmsg) {
        !failure ?: failure(code, errmsg);
    }];
}


/**
 获取聊天室成员信息
 */
+ (void)getRoomMembersWithStart:(NSInteger)start
                          count:(NSInteger)count
                        success:(JXIMRequestRoomMembersSuccessHander)success
                        failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(@(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId)),
                             @"start" : @(start*count),
                             @"limit" : @(count),
                             @"uid" : JX_STR_AVOID_nil([GetCore(YPAuthCoreHelp) getUid]),
                             @"ticket" : JX_STR_AVOID_nil([GetCore(YPAuthCoreHelp) getTicket]),
                           };
    
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_MEMBERS params:params success:^(id data) {
        NSArray *roomMembers = [NSArray yy_modelArrayWithClass:[YPChatRoomMember class] json:data];
        
        NSLog(@"%@",data);
        !success ?: success(roomMembers);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

/**
 获取聊天室黑名单列表
 */
+ (void)getRoomBlackMembersWithStart:(NSInteger)start
                               count:(NSInteger)count
                              roomId:(NSString *)roomId
                             success:(JXIMRequestRoomMembersSuccessHander)success
                             failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"start" : @(start),
                             @"limit" : @(count),
                             @"uid" : JX_STR_AVOID_nil([GetCore(YPAuthCoreHelp) getUid]),
                             @"ticket" : JX_STR_AVOID_nil([GetCore(YPAuthCoreHelp) getTicket]),
                             };
    
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_BLACK_MEMBERS params:params success:^(id data) {
        NSArray *roomMembers = [NSArray yy_modelArrayWithClass:[YPChatRoomMember class] json:data];
        !success ?: success(roomMembers);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

/**
 获取房间管理员列表
 
 @param roomId  房间id
 @param success success description
 @param failure failure description
 */
+ (void)getRoomManagersWithRoomId:(NSString *)roomId
                          success:(JXIMRequestRoomMembersSuccessHander)success
                          failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"uid" : JX_STR_AVOID_nil([GetCore(YPAuthCoreHelp) getUid]),
                             @"ticket" : JX_STR_AVOID_nil([GetCore(YPAuthCoreHelp) getTicket]),
                             };
    
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_MANAGERS params:params success:^(id data) {
        NSArray *roomMembers = [NSArray yy_modelArrayWithClass:[YPChatRoomMember class] json:data];
        !success ?: success(roomMembers);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

/**
 根据ID获取聊天室成员信息
 */
+ (void)getRoomMembersByIdsWithRoomId:(NSString *)roomId
                             accounts:(NSString *)accounts
                                  uid:(NSString *)uid
                               ticket:(NSString *)ticket
                              success:(JXIMRequestRoomMembersSuccessHander)success
                              failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"accounts" : JX_STR_AVOID_nil(accounts),
                             @"uid" : JX_STR_AVOID_nil(uid),
                             @"ticket" : JX_STR_AVOID_nil(ticket),
                             };
    
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_MEMBERS_BY_IDS params:params success:^(id data) {
        NSArray *roomMembers = nil;
        if ([data isKindOfClass:[NSArray class]]) {
            roomMembers = [NSArray yy_modelArrayWithClass:[YPChatRoomMember class] json:data];
        }
        !success ?: success(roomMembers);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

/**
 添加移除聊天室黑名单
 */
+ (void)markRoomBlackListWithActionType:(JXIMBlacklistMarkActionType)type
                                 roomId:(NSString *)roomId
                                account:(NSString *)account
                                    uid:(NSString *)uid
                                 ticket:(NSString *)ticket
                                success:(JXIMRequestSuccessHander)success
                                failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"is_add" : @(type),
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"account" : JX_STR_AVOID_nil(account),
                             @"uid" : JX_STR_AVOID_nil(uid),
                             @"ticket" : JX_STR_AVOID_nil(ticket),
                             };
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_MARK_BLACKLIST params:params success:^(id data) {
        !success ?: success();
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

/**
 设置/取消聊天室管理员
 */
+ (void)markRoomMamagerWithActionType:(JXIMRoomManagerMarkActionType)type
                                 roomId:(NSString *)roomId
                                account:(NSString *)account
                                    uid:(NSString *)uid
                                 ticket:(NSString *)ticket
                                success:(JXIMRequestSuccessHander)success
                                failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"is_add" : @(type),
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"account" : JX_STR_AVOID_nil(account),
                             @"uid" : JX_STR_AVOID_nil(uid),
                             @"ticket" : JX_STR_AVOID_nil(ticket),
                             };
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_MARK_ROOM_MANAGER params:params success:^(id data) {
        !success ?: success();
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

/**
 踢除特定成员
 */
+ (void)kickRoomMemberWithRoomId:(NSString *)roomId
                         account:(NSString *)account
                             uid:(NSString *)uid
                          ticket:(NSString *)ticket
                         success:(JXIMRequestSuccessHander)success
                         failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"account" : JX_STR_AVOID_nil(account),
                             @"uid" : JX_STR_AVOID_nil(uid),
                             @"ticket" : JX_STR_AVOID_nil(ticket),
                             };
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_KICK_MEMBER params:params success:^(id data) {
        !success ?: success();
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

/**
 禁言房间用户
 */
+ (void)markRoomMemberMuteWithRoomId:(NSString *)roomId
                             account:(NSString *)account
                              isMute:(BOOL)isMute
                             success:(JXIMRequestSuccessHander)success
                             failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"account" : JX_STR_AVOID_nil(account),
                             @"is_mute" : @(isMute),
                             @"uid" : JX_STR_AVOID_nil(GetCore(YPAuthCoreHelp).getUid),
                             @"ticket" : JX_STR_AVOID_nil(GetCore(YPAuthCoreHelp).getTicket),
                             };
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_MARK_MUTE_MEMBER params:params success:^(id data) {
        !success ?: success();
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

/**
 查询房间禁言用户
 */
+ (void)getRoomMuteMemberWithRoomId:(NSString *)roomId
                            success:(JXIMRequestRoomMembersSuccessHander)success
                            failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"uid" : JX_STR_AVOID_nil(GetCore(YPAuthCoreHelp).getUid),
                             @"ticket" : JX_STR_AVOID_nil(GetCore(YPAuthCoreHelp).getTicket),
                             };
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_GET_MUTE_MEMBERS params:params success:^(id data) {
        NSArray *roomMembers = [NSArray yy_modelArrayWithClass:[YPChatRoomMember class] json:data];
        !success ?: success(roomMembers);
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

/**
 上报HLS地址接口
 */
+ (void)reportHLSAddressWithRoomId:(NSString *)roomId
                           success:(JXIMRequestSuccessHander)success
                           failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"uid" : JX_STR_AVOID_nil(GetCore(YPAuthCoreHelp).getUid),
                             @"ticket" : JX_STR_AVOID_nil(GetCore(YPAuthCoreHelp).getTicket),
                             };
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_REPORT_HLS_ADDRESS params:params success:^(id data) {
        !success ?: success();
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}

@end
