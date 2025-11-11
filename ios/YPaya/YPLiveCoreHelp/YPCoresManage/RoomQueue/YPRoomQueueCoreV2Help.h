//
//  RoomQueueCoreV2.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"
#import <NIMSDK/NIMSDK.h>
#import "YPRoomQueueInfo.h"
#import "YPChatRoomMember.h"
#import "UserInfo.h"

@interface YPRoomQueueCoreV2Help : YPBaseCore

@property (nonatomic, strong) YPRoomMicInfo *micState;
@property (strong, nonatomic) YPChatRoomMember *myMember;//我自己的Member
@property (assign, nonatomic) BOOL isNeedCommon;//

- (void)freeMicPlace:(int)position;//解除麦位封锁
- (void)lockMicPlace:(int)position;//封锁麦位

//- (void)addUserInWaitMicrQueue;
- (void)removeUserInWaitMicrQueue:(NSString *)key roomid:(NSString *)roomid islossNet:(BOOL)lossNet;

- (void)closeMic:(int)position;//麦位静音
- (void)openMic:(int)position;//取消麦位静音
- (void)downMic;//下麦
- (void)upMic:(int)position;//上指定麦
- (void)upFreeMic;//上一个空闲麦
- (void)removeUserPositionLocation:(NSString *)userId;
- (void)sendLeaveRoomSuccess:(void (^)(void))successs failure:(void (^)(NSString *))failure;

- (void)kickDownMic:(UserID)uid position:(int)position;//踢它下麦
- (void)inviteUpMic:(UserID)uid postion:(NSString *)position;//邀请上指定麦位
- (void)inviteUpFreeMic:(UserID)uid;//邀请上空闲麦

- (BOOL)isInMicQueue:(NSString *)userid;

- (NSString *)findFreeMicroPosition;

- (BOOL)checkMeInMicQueue;

- (void)sortWaitMicQueArr;

- (BOOL)checkIsRoomer;

- (BOOL)checkIsRoomerWith:(NSString *)uid;

- (void)madeWaitQueueToTop:(NSInteger)index;

- (BOOL)hasMadeTopAuthority;

- (BOOL)isOnMicro:(UserID)uid;//判断是否在麦上
- (NSString *)findThePositionByUid:(UserID)uid;//通过uid判断麦位
- (YPChatRoomMember *)findTheMemberByUserId:(UserID)uid;//通过uid查找member
- (YPRoomQueueInfo *)findTheRoomQueueMemberInfo:(UserID)uid;//通过uid查找麦序信息
- (NSMutableArray *)findSendGiftMember;//获取麦上的人跟房主

- (NSMutableArray *)findOnMicMember;

- (void)removeChatroomQueueWithPosition:(NSString *)position
                                success:(void (^)(BOOL success))success
                                failure:(void (^)(NSString *message))failure;//从队列中删除

//- (void)removeInMicQueUserArrWith:(NSString *)userid roomid:(NSString *)roomid islossNet:(BOOL)lossNet;

//上麦后设置角色
- (void)setUpMicStateWith:(NSString *)position userId:(UserID)userId;

@end
