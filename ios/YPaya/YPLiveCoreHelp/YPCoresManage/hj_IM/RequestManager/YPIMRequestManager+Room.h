//
//  YPIMRequestManager+Room.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMRequestManager.h"

#import "YPChatRoomMember.h"
#import "YPChatRoomInfo.h"
#import "YPIMQueueItem.h"
#import "UserInfo.h"

#import "YPIMDefines.h"

typedef void(^JXIMRequestEnterRoomSuccessHander)(YPChatRoomInfo *roomInfo, YPChatRoomMember *member, NSArray<YPIMQueueItem *> *queueInfos);
typedef void(^JXIMRequestRoomInfoSuccessHander)(YPChatRoomInfo *roomInfo);
typedef void(^JXIMRequestRoomMembersSuccessHander)(NSArray<YPChatRoomMember *> *roomMembers);

/**
 房间管理接口
 */
@interface YPIMRequestManager (Room)

/**
 进入聊天室

 @param roomId  房间id
 @param success success description
 @param failure failure description
 */
+ (void)enterRoomWithRoomId:(NSString *)roomId
                    success:(JXIMRequestEnterRoomSuccessHander)success
                    failure:(JXIMRequestFailureHander)failure;

/**
 退出聊天室

 @param roomId  房间id
 @param success success description
 @param failure failure description
 */
+ (void)exitRoomWithRoomId:(NSString *)roomId
                   success:(JXIMRequestSuccessHander)success
                   failure:(JXIMRequestFailureHander)failure;


/**
 获取聊天室成员信息
 @param count   个数
 @param success success description
 @param failure failure description
 */
+ (void)getRoomMembersWithStart:(NSInteger)start
                         count:(NSInteger)count
                       success:(JXIMRequestRoomMembersSuccessHander)success
                       failure:(JXIMRequestFailureHander)failure;

/**
 获取聊天室黑名单列表
 */
+ (void)getRoomBlackMembersWithStart:(NSInteger)start
                               count:(NSInteger)count
                              roomId:(NSString *)roomId
                             success:(JXIMRequestRoomMembersSuccessHander)success
                             failure:(JXIMRequestFailureHander)failure;

/**
 获取房间管理员列表
 
 @param roomId  房间id
 @param success success description
 @param failure failure description
 */
+ (void)getRoomManagersWithRoomId:(NSString *)roomId
                          success:(JXIMRequestRoomMembersSuccessHander)success
                          failure:(JXIMRequestFailureHander)failure;

/**
 根据ID获取聊天室成员信息

 @param roomId   房间id
 @param accounts 用户id string 示例: ‘123,345’
 @param uid      发请求的用户id
 @param ticket   用户Ticket
 @param success success description
 @param failure failure description
 */
+ (void)getRoomMembersByIdsWithRoomId:(NSString *)roomId
                             accounts:(NSString *)accounts
                                  uid:(NSString *)uid
                               ticket:(NSString *)ticket
                              success:(JXIMRequestRoomMembersSuccessHander)success
                              failure:(JXIMRequestFailureHander)failure;

/**
 添加移除聊天室黑名单

 @param type    黑名单操作类型
 @param roomId  房间id
 @param account 要添加或移除的uid
 @param uid     发请求的用户id
 @param ticket  用户Ticket
 @param success success description
 @param failure failure description
 */
+ (void)markRoomBlackListWithActionType:(JXIMBlacklistMarkActionType)type
                                 roomId:(NSString *)roomId
                                account:(NSString *)account
                                    uid:(NSString *)uid
                                 ticket:(NSString *)ticket
                                success:(JXIMRequestSuccessHander)success
                                failure:(JXIMRequestFailureHander)failure;

/**
 设置/取消聊天室管理员

 @param type    聊天室管理员操作类型
 @param roomId  房间id
 @param account 要设置/取消聊天室管理员的uid
 @param uid     发请求的用户id
 @param ticket  用户Ticket
 @param success success description
 @param failure failure description
 */
+ (void)markRoomMamagerWithActionType:(JXIMRoomManagerMarkActionType)type
                               roomId:(NSString *)roomId
                              account:(NSString *)account
                                  uid:(NSString *)uid
                               ticket:(NSString *)ticket
                              success:(JXIMRequestSuccessHander)success
                              failure:(JXIMRequestFailureHander)failure;

/**
 踢除特定成员

 @param roomId  房间id
 @param account 要踢除的uid
 @param uid     发请求的用户id
 @param ticket  用户Ticket
 @param success success description
 @param failure failure description
 */
+ (void)kickRoomMemberWithRoomId:(NSString *)roomId
                         account:(NSString *)account
                             uid:(NSString *)uid
                          ticket:(NSString *)ticket
                         success:(JXIMRequestSuccessHander)success
                         failure:(JXIMRequestFailureHander)failure;

/**
 禁言房间用户

 @param roomId  房间id
 @param account 要禁言的uid
 @param isMute  是否禁言用户 1：是 0：否
 @param success success description
 @param failure failure description
 */
+ (void)markRoomMemberMuteWithRoomId:(NSString *)roomId
                             account:(NSString *)account
                              isMute:(BOOL)isMute
                             success:(JXIMRequestSuccessHander)success
                             failure:(JXIMRequestFailureHander)failure;

/**
 查询房间禁言用户
 
 @param roomId  房间id
 @param success success description
 @param failure failure description
 */
+ (void)getRoomMuteMemberWithRoomId:(NSString *)roomId
                            success:(JXIMRequestRoomMembersSuccessHander)success
                            failure:(JXIMRequestFailureHander)failure;

/**
 上报HLS地址接口

 @param roomId  房间Id
 @param success success description
 @param failure failure description
 */
+ (void)reportHLSAddressWithRoomId:(NSString *)roomId
                           success:(JXIMRequestSuccessHander)success
                           failure:(JXIMRequestFailureHander)failure;

@end
