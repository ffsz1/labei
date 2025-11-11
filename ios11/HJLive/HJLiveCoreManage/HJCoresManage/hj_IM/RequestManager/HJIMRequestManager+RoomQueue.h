//
//  HJIMRequestManager+RoomQueue.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJIMRequestManager.h"
#import "HJIMDefines.h"
#import "HJRoomMicInfo.h"
#import "HJIMQueueItem.h"

NS_ASSUME_NONNULL_BEGIN

typedef void(^JXIMRequestRoomQueueSuccessHander)(NSMutableArray<HJIMQueueItem *> *queueList);


@interface HJIMRequestManager (RoomQueue)

/**
 检查是否可以推流接口
 */
+ (void)checkPushAuthWithRoomId:(NSString *)roomId
                        success:(JXIMRequestSuccessHander)success
                        failure:(JXIMRequestFailureHander)failure;

/**
 获取房间队列
 */
+ (void)fetchRoomQueueSuccess:(JXIMRequestRoomQueueSuccessHander)success
                      failure:(void(^)(NSInteger code, NSString *errorMessage))failure;


/**
 加入或更新队列元素
 @param key 麦序位置
 */
+ (void)addOrUpdateMemberWithKey:(NSString *)key
                         Success:(void(^)())success
                         failure:(void(^)(NSInteger code, NSString *errorMessage))failure;


/**
删除队列元素
 @param key 麦序位置
 */
+ (void)removeQueueWithKey:(NSString *)key
                   Success:(void(^)())success
                   failure:(void(^)(NSInteger code, NSString *errorMessage))failure;

@end

NS_ASSUME_NONNULL_END
