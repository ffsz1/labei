//
//  HJGiftCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import "GiftInfo.h"
#import "UserInfo.h"
#import <NIMSDK/NIMSDK.h>
#import "GiftReceiveInfo.h"
#import "HJGiftInfoStorage.h"
#import "HJIMMessage.h"

@interface HJGiftCore : BaseCore

@property (nonatomic, assign) BOOL isOpenAnimation;

@property (nonatomic, strong) NSMutableArray *currentGiftMsgArr;
@property (nonatomic, assign) BOOL lastGiftIsAll;
@property (nonatomic, strong) GiftReceiveInfo *lastGiftInfo;

- (void) requestGiftList;
//- (void)requestGiftListMystic; // 背包礼物
//- (void)requestGiftListDiandianCoin;//点点币礼物

- (void) sendRoomGift:(NSInteger)giftId targetUid:(UserID)targetUid giftyType:(GiftType)type;
- (void) sendChatGift:(NSInteger)giftID info:(UserInfo *)info giftNum:(NSInteger)giftNum targetUid:(UserID)targetUid giftyType:(GiftType)type;
- (void)sendChatPoint:(NSInteger)giftID info:(UserInfo *)info giftNum:(NSInteger)giftNum targetUid:(UserID)targetUid  giftyType:(GiftType)type;
- (void) sendRoomGift:(NSInteger)giftId targetUid:(UserID)targetUid type:(NSInteger)type giftNum:(NSInteger)giftNum giftyType:(GiftType)giftType goldPrice:(double)goldPrice;
- (void)sendPoint:(NSInteger)giftId targetUid:(UserID)targetUid type:(NSInteger)type giftNum:(NSInteger)giftNum giftyType:(GiftType)giftType goldPrice:(double)goldPrice;
- (GiftInfo *)findGiftInfoByGiftId:(NSInteger)giftId giftyType:(GiftType)type;
- (NSMutableArray *)getGiftInfoListWithType:(NSInteger)type;
- (void)sendAllMicroGiftByUids:(NSString *)uids giftId:(NSInteger)giftId giftNum:(NSInteger)giftNum roomUid:(NSInteger)roomUid isAllMicroSend:(BOOL)isAllMicroSend giftyType:(GiftType)type goldPrice:(double)goldPrice ;

- (void)sendPointWholeMicro:(NSString *)uids giftId:(NSInteger)giftId giftNum:(NSInteger)giftNum roomUid:(NSInteger)roomUid isAllMicroSend:(BOOL)isAllMicroSend giftyType:(GiftType)type goldPrice:(double)goldPrice;

/**
 获取游戏房礼物

 @return 数组
 */
- (NSMutableArray *)getGameRoomGift;


/**
 获取普通房礼物

 @return 数组
 */
- (NSMutableArray *)getNormalRoomGift;


/**
 获取背包礼物

 @return 数组
 */
- (NSMutableArray *)getMysticGift;

/**
 获取点点币礼物
 
 @return 数组
 */
- (NSMutableArray *)getDiandianGift;

//- (void)setupDefaltMysticGift;
//- (void)setupDefaltDiandianCoinGift;

/**
 自己发送自定义消息的时候，因为自己是收不到自己发送的自定义消息的，这里用来模拟自己收到一条自定义消息

 @param msg 消息实体
 */
- (void)onRecvChatRoomCustomMsg:(HJIMMessage *)msg;

/**
 自己发送自定义消息的时候，因为自己是收不到自己发送的自定义消息的，这里用来模拟自己收到一条自定义消息
 
 @param msg 消息实体
 */
- (void)onRecvP2PCustomMsg:(NIMMessage *)msg;


/**
 测试用拼命发礼物
 */
- (void)startGiftTimer;
- (void)cancelGiftTimer;
@end
