//
//  YPIMRequestManager+RoomQueue.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMRequestManager+RoomQueue.h"
#import "YPImRoomCoreV2.h"
#import "YPIMDefines.h"

@implementation YPIMRequestManager (RoomQueue)

/**
 检查是否可以推流接口
 */
+ (void)checkPushAuthWithRoomId:(NSString *)roomId
                        success:(JXIMRequestSuccessHander)success
                        failure:(JXIMRequestFailureHander)failure {
    NSDictionary *params = @{
                             @"room_id" : JX_STR_AVOID_nil(roomId),
                             @"uid" : JX_STR_AVOID_nil(GetCore(YPAuthCoreHelp).getUid),
                             @"ticket" : JX_STR_AVOID_nil(GetCore(YPAuthCoreHelp).getTicket),
                             };
    [YPHttpRequestHelper IM_POST:JX_IM_ROUTE_ROOM_CHECK_PUSH_AUTH params:params success:^(id data) {
        !success ?: success();
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure([resCode intValue], message);
    }];
}


/**
 获取房间队列
 */
+ (void)fetchRoomQueueSuccess:(JXIMRequestRoomQueueSuccessHander)success
                      failure:(void(^)(NSInteger code, NSString *errorMessage))failure {
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    dic[@"room_id"] = @(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId);
    [GetCore(YPWebSocketCore) send:JX_IM_ROUTE_Room_Queue_AllMembers content:dic success:^(id data) {
        NSMutableArray<YPIMQueueItem *> *queueList = [NSArray yy_modelArrayWithClass:[YPIMQueueItem class] json:data];
        !success ?: success(queueList);
    } failure:^(NSInteger code, NSString *errmsg) {
        !failure ?: failure(code, errmsg);
    }];
}

/**
 加入或更新队列元素
 @param key 麦序位置
 */
+ (void)addOrUpdateMemberWithKey:(NSString *)key
                         Success:(void(^)())success
                         failure:(void(^)(NSInteger code, NSString *errorMessage))failure {
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    dic[@"room_id"] = @(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId);
    dic[@"uid"] = GetCore(YPAuthCoreHelp).getUid;
    dic[@"key"] = key;
    [GetCore(YPWebSocketCore) send:JX_IM_ROUTE_Room_Queue_Update content:dic success:^(id data) {
        !success ?: success();
    } failure:^(NSInteger code, NSString *errmsg) {
        !failure ?: failure(code, errmsg);
    }];
}

/**
 删除队列元素
 @param key 麦序位置
 */
+ (void)removeQueueWithKey:(NSString *)key
                   Success:(void(^)())success
                   failure:(void(^)(NSInteger code, NSString *errorMessage))failure {
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    dic[@"room_id"] = @(GetCore(YPImRoomCoreV2).currentRoomInfo.roomId);
    dic[@"key"] = key;
    dic[@"uid"] = GetCore(YPAuthCoreHelp).getUid;
    [GetCore(YPWebSocketCore) send:JX_IM_ROUTE_Room_Queue_Poll content:dic success:^(id data) {
        !success ?: success();
    } failure:^(NSInteger code, NSString *errmsg) {
        !failure ?: failure(code, errmsg);
    }];
}

@end
