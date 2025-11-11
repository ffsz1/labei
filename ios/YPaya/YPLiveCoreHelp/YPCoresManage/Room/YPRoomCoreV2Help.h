//
//  YPRoomCoreV2Help.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"
#import "YPChatRoomInfo.h"
#import "YPGiftInfo.h"

@interface YPRoomCoreV2Help : YPBaseCore

@property (nonatomic, assign) BOOL isInRoom;
@property (nonatomic, strong) NSMutableArray *speakingList;//说话list
@property (nonatomic, strong) NSMutableArray *messages;//消息
@property (nonatomic, strong) NSMutableArray *positionArr; //麦序位置
@property (nonatomic, assign) CGPoint avatarPosition; //房主头像位置

- (void)getRoomInfo:(UserID)uid success:(void (^)(YPChatRoomInfo *roomInfo))success failure:(void (^)(NSInteger resCode, NSString *message))failure;

-(void)openRoom:(UserID)uid type:(RoomType)type title:(NSString *)title roomDesc:(NSString *)roomDesc backPic:(NSString*)backPic rewardId:(NSString *)rewardId;//开启房间
- (void)updateRoomInfo:(UserID)uid title:(NSString *)title roomDesc:(NSString *)roomDesc roomNotice:(NSString *)roomNotice backPic:(NSString *)backPic playInfo:(NSString *)playInfo giftEffectSwitch:(BOOL)giftEffectSwitch  giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch;
- (void)updateGameRoomInfo:(UserID)uid backPic:(NSString *)backPick title:(NSString *)title roomTopic:(NSString *)roomTopic roomNotice:(NSString *)roomNotice roomPassword:(NSString *)roomPassword tag:(int)tag playInfo:(NSString *)playInfo giftEffectSwitch:(BOOL)giftEffectSwitch giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch;//房主更新房间信息
- (void)managerUpdateGameRoomInfo:(UserID)uid backPic:(NSString *)backPick title:(NSString *)title roomTopic:(NSString *)roomTopic roomNotice:(NSString *)roomNotice roomPassword:(NSString *)roomPassword tag:(int)tag playInfo:(NSString *)playInfo giftEffectSwitch:(BOOL)giftEffectSwitch  giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch; //管理员修改房间信息
-(void) closeRoom:(UserID)uid;


//房间统计
- (void)recordTheRoomTime:(UserID)uid roomUid:(UserID)roomUid;//房间统计
- (void)reportUserInterRoom; //用户进入房间上报
- (void)reportUserOuterRoom; //用户退出房间上报
- (void)savePosition:(NSMutableArray *)list;  //保存游戏房cell的位置
- (void)saveCharmInfoMap:(NSMutableDictionary <NSString *,id> *)charmInfoMap;//保存房间魅力值Map
- (NSMutableDictionary *)getRoomCharmInfoMap;//获取房间魅力值Map

//麦序
- (NSMutableArray *)getAdminMembers;//麦序

- (BOOL)isBeKickedWithRoomid:(NSString *)roomid;


//获取房间信息
- (RACSignal *)requestRoomInfo:(UserID) uid;//请求房间信息
- (YPChatRoomInfo *) getCurrentRoomInfo;//当前房间信息
- (void)getRoomBounsList;//获取房间贡献榜
- (void)getUserInterRoomInfo:(UserID)uid;//获取用户进入了的房间的信息
- (void)getNewRoomBounsList:(NSString *)type dataType:(NSString *)dataTpye;//贡献和魅力

//黑名单
- (void)judgeIsInBlackList:(NSString *)roomID; //查询自己

/** 礼物抽奖*/
- (void)userGiftPurseDraw:(NSString *)type;

- (void)sendLotteryMessageWithGiftInfo:(YPGiftInfo *)giftInfo nick:(NSString *)nick;

/**
 砸蛋排行榜
 
 @param type  1代表今天 2代表昨天
 */
- (void)userGiftPurseGetRankWithType:(NSInteger)type;

/**
 砸蛋中奖纪录
 */
- (void)userGiftPurseRecordWithPageNum:(NSInteger)pageNum
                              pageSize:(NSInteger)pageSize;

/**
 房间背景列表
 
 @param roomId 房间id
 */
- (void)roomBgListWithRoomId:(NSInteger)roomId
                     success:(void (^)(NSArray *list))success
                     failure:(void (^)(NSString *message))failure;
//清空魅力值
- (void)userReceiveRoomMicMsg:(UserID)uid roomUid:(NSInteger)roomUid;
@end
