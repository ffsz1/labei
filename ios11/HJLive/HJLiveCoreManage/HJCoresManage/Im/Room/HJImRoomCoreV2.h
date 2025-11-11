//
//  HJImRoomCoreV2.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import <NIMSDK/NIMSDK.h>
#import "HJRoomQueueInfo.h"
#import "ChatRoomInfo.h"
#import "ChatRoomMember.h"
#import "HJIMQueueItem.h"

typedef NS_ENUM(NSInteger, ImRoomCoreEnterType) { ///< 房间入口类型
    ImRoomCoreEnterTypeNormal,                    ///< 普通
    ImRoomCoreEnterTypeMIC                        ///< 一键连麦
};

@interface HJImRoomCoreV2 : BaseCore
@property (strong, nonatomic)NSMutableArray<ChatRoomMember *> *displayMembers; //在线列表(第一页的owner/mic/manager)

@property (strong, nonatomic)NSMutableArray<ChatRoomMember *> *micMembers; //麦序列表
@property (strong, nonatomic)NSMutableArray<ChatRoomMember *> *micMembersNoRoomOwner; //麦序列表,不含房主
@property (strong, nonatomic)NSMutableArray <HJIMQueueItem *> *micQueue;//麦序位置信息
@property (strong, nonatomic)NSMutableArray<ChatRoomMember *> *onLineManagerMembers; //在线管理成员   待定
@property (strong, nonatomic)NSMutableArray<ChatRoomMember *> *allManagers; //管理员成员

@property (strong, nonatomic)NSMutableArray<ChatRoomMember *> *backLists; //黑名单
@property (strong, nonatomic)NSMutableArray<ChatRoomMember *> *noMicMembers; //用户返回非麦上成员
@property (strong, nonatomic)NSMutableArray<ChatRoomMember *> *normalMembers; //在线普通用户

@property (nonatomic, assign) NSInteger onlineNumber;

@property (strong, nonatomic) ChatRoomMember *roomOwner; //房主
@property (strong, nonatomic) ChatRoomMember *roomOwnerleave; //房主
@property (strong, nonatomic) UserInfo *roomOwnerInfo; //房主个人信息
@property (strong, nonatomic) ChatRoomMember *myMember;//我自己的Member
@property (nonatomic, strong) HJRoomQueueInfo *micSequence;

@property (nonatomic, assign) NSInteger lastPosition;//分页最后的位置
@property (nonatomic, assign) NSInteger lastBlackPosition;//黑名单分页最后的位置

@property (nonatomic, strong) ChatRoomInfo *currentRoomInfo;
//当前正在进入的房间信息
@property (nonatomic, strong) ChatRoomInfo *enterRoomInfo;

@property (nonatomic, assign) ImRoomCoreEnterType preferredEnterType; ///< 入口类型

@property (nonatomic, assign) BOOL isLoading;

- (void)enterChatRoom:(NSInteger) roomId;//enter
- (void)exitChatRoom:(NSInteger)roomId;//exit
- (void)kickUser:(UserID)beKickedUid didFinish:(void(^)())didFinish;//kick 被踢出房间

- (RACSignal *)rac_queryQueueWithRoomId:(NSString *)roomId;
- (void)queryQueueWithRoomId:(NSString *)roomId;//拿麦序成员
- (void)queryChatRoomMembersWithPage:(int)page state:(int)state;//通过页数查询聊天室成员，第一页包含麦序与固定成员
- (void)queryNoMicChatRoomMembersWithPage:(int)page state:(int)state;//通过页数查询不在麦上的人，第一页包含房主管理
- (void)queryManagerorBackList;
- (void)getManagerList;//查询管理员列表
- (void)queryChartRoomMembersWithUids:(NSString *)uid;//根据uids获取对应的ChatRoomMember
- (RACSignal *)rac_queryChartRoomMemberByUid:(NSString *)uid;//根据uid获取ChatRoomMember
- (RACSignal *)rac_fetchMemberUserInfoByUid:(NSString *)uid;//rac根据uid获取NIMUser
- (RACSignal *)rac_fetchChatRoomInfoByRoomId:(NSString *)roomId;//rac根据roomid获取chatroom

- (void)markManagerList:(UserID)userID enable:(BOOL)enable;//设置/取消管理员
- (void)markBlackList:(UserID)userID enable:(BOOL)enable;//设置/取消黑名单
/** 查询房主是否在线*/
- (void)checkRoomerIsInRoom;
- (void)sendMessage:(NSString *)message success:(void (^)())success
            failure:(void (^)(NSInteger code, NSString *errorMessage))failure;
- (BOOL)isInRoom ;//判断是否在房间
- (void)repairMicQueue;//绑定麦序

- (BOOL)isOnMicro:(UserID)uid;
@end
