//
//  YPHttpRequestHelper+Room.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"
#import "YPRewardInfo.h"
#import "YPChatRoomInfo.h"
#import "YPMicroListInfo.h"
#import "YPDaCallIntoModel.h"
#import "YPDaCallModel.h"
@interface YPHttpRequestHelper (Room)

#pragma mark - 更新房间信息




/**
 更新房间信息
 
 @param uid 房主uid
 @param title 标题
 @param roomDesc 房间简介
 @param backPic 背景图
 @param success 成功
 @param failure 失败
 */
+ (void)updateRoomInfo:(UserID)uid title:(NSString *)title roomDesc:(NSString *)roomDesc roomNotice:(NSString *)roomNotice backPic:(NSString *)backPic roomPassword:(NSString *)roomPassword tag:(int)tag playInfo:(NSString *)playInfo giftEffectSwitch:(BOOL)giftEffectSwitch giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch success:(void (^)(YPChatRoomInfo *))success failure:(void (^)(NSNumber *, NSString *))failure;
/**
 管理员修改房间信息

 @param uid uid
 @param title 房间标题
 @param roomDesc 房间话题／描述
 @param backPic 房间背景
 @param roomPassword 房间密码
 @param tag 房间标签
 @param success 成功
 @param failure 失败
 */
+ (void)managerUpdateRoomInfo:(UserID)uid title:(NSString *)title roomDesc:(NSString *)roomDesc roomNotice:(NSString *)roomNotice backPic:(NSString *)backPic roomPassword:(NSString *)roomPassword playInfo:(NSString *)playInfo tag:(int)tag giftEffectSwitch:(BOOL)giftEffectSwitch giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch success:(void (^)(YPChatRoomInfo *))success failure:(void (^)(NSNumber *, NSString *))failure;

#pragma mark - 统计相关
/**
 用户退出房间上报

 @param success 成功
 @param failure 失败
 */
+ (void)reportUserOutRoomSuccess:(void (^)(BOOL success))success failure:(void (^)(NSNumber *resCode, NSString *message))failure;
/**
 用户进入房间上报

 @param success 成功
 @param failure 失败
 */
+ (void)reportUserInterRoomSuccess:(void (^)(BOOL success))success failure:(void (^)(NSNumber *resCode, NSString *message))failure;
/**
 房间统计埋点
 
 @param uid uid
 @param roomUid 房主uid
 */
+ (void)recordTheRoomTime:(UserID)uid roomUid:(UserID)roomUid;

#pragma mark - 开房&关房

/**
 开悬赏房交悬赏金接口
 
 @param uid 房主UID
 @param servDura 服务时长
 @param success 成功
 @param failure 失败
 */
+ (void) rewardForRoom:(UserID)uid servDura:(NSInteger)servDura rewardMonye:(NSInteger)rewardMonye
               success:(void (^)(YPRewardInfo *))success
               failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 开房
 
 @param uid uid
 @param type 房间类型 1、竞拍房 2、悬赏房
 @param title 房间标题
 @param roomDesc 房间介绍
 @param backPic 背景图
 @param rewardId 悬赏id
 @param success 成功
 @param failure 失败
 */
+ (void) openRoom:(UserID)uid type:(RoomType)type title:(NSString *)title roomDesc:(NSString *)roomDesc backPic:(NSString*)backPic rewardId:(NSString *)rewardId
          success:(void (^)(YPChatRoomInfo *))success
          failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 关闭房间
 
 @param uid 房主uid
 */
+ (void) closeRoom:(UserID)uid
           success:(void (^)(void))success
           failure:(void (^)(NSNumber *resCode, NSString *message))failure;


#pragma mark - 获取房间信息相关
/**
 通过userId拿房间信息
 
 @param uid 房主uid
 @param success 成
 @param failure 失败
 */
+ (void) getRoomInfo:(UserID) uid
             success:(void (^)(YPChatRoomInfo *))success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 通过uids批量获取房间信息
 
 @param uids 房主uids集合
 @param success 成功
 @param failure 失败
 */
+ (void) getRoomInfoByUids:(NSArray *)uids
                   success:(void (^)(NSArray<YPChatRoomInfo *> *))success
                   failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 获取用户所在房间信息

 @param uid uid
 @param success 成功
 @param failure 失败
 */
+ (void)requestUserInRoomInfoBy:(UserID)uid Success:(void (^)(YPChatRoomInfo *roomInfo))success failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 获取房间榜单

 @param success 成功
 @param failure 失败
 */
+ (void)requestRoomBounsListSuccess:(void (^)(NSMutableArray *bounsInfoList))success failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 获取房间魅力财富榜单
 
 @param success 成功
 @param failure 失败
 */
+ (void)requestNewRoomBounsListWithType:(NSString *)type
                           withDataType:(NSString *)dataType
                                Success:(void (^)(NSMutableArray *))success
                                failure:(void (^)(NSNumber *, NSString *))failure;


/**
 获取房间神豪榜首

 @param roomUid 房间uid

 */
+ (void)getRoomShenHaoTop:(NSString *)roomUid
                  Success:(void (^)(NSMutableArray *))success
                  failure:(void (^)(NSNumber *, NSString *))failure;


//MARK: - 打call /gift/callForUser  给单个用户打call
+ (void)getRoomCallForUser:(YPDaCallIntoModel *)model
                  Success:(void (^)(YPDaCallModel *))success
                   failure:(void (^)(NSNumber *, NSString *))failure;
#pragma mark - 麦序操作
/**
 直接上麦
 
 @param uid 用户UID
 @param roomOwnerUid 房主uid
 @param success 成功
 @param failure 失败
 */
+(void)upMicro:(UserID)uid roomId:(UserID)roomId position:(NSInteger)position
       success:(void (^)(void))success
       failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 直接下麦
 
 @param uid 用户UID
 @param roomOwnerUid 房主uid
 @param success 成功
 @param failure 失败
 */
+(void)leftMicro:(UserID)uid roomId:(UserID)roomId position:(NSInteger)position
         success:(void (^)(void))success
         failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 锁坑位/取消操作
 @param roomOwnerUid 房主uid
 @param position 位置
 @param state 锁/开坑
 @param success 成功
 @param failure 失败
 */
+ (void)micPlace:(NSInteger)position roomOwnerUid:(UserID)roomOwnerUid state:(NSInteger)state
         success:(void (^)(void))success
         failure:(void (^)(NSNumber *resCode, NSString *message))failure;
/**
 锁麦/开麦操作
 @param roomOwnerUid 房主uid
 @param position 位置
 @param state 锁/开坑
 @param success 成功
 @param failure 失败
 */
+ (void)micState:(NSInteger)position roomOwnerUid:(UserID)roomOwnerUid state:(NSInteger)state
         success:(void (^)(void))success
         failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 踢用户下麦

 @param uid 被踢用户ID
 @param position 位置
 @param roomId 房间id
 @param success 成功
 @param failure 失败
 */
+ (void)ownerKickUserByUid:(NSString *)uid position:(int)position roomId:(int)roomId success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure ;

/**
 邀请上麦

 @param uid 被邀请人的UID
 @param position 位置
 @param roomId 房间id
 @param success 成功
 @param failure 失败
 */
+ (void)inviteUpMicroWithUid:(UserID)uid position:(int)position roomId:(int)roomId success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure ;







#pragma mark - 暂时未使用
/**
 用户离开麦序
 
 @param roomUid 房主uid
 */
+ (void)userLeftMicroWithRoomUid:(UserID)roomUid;



/**
 用户同意上麦
 
 @param roomUid 房间id
 @param success 成功
 @param failure 失败
 */
+ (void)userAgreeUpMicroRoomUid:(NSString *)roomUid success:(void (^)(BOOL isSuccess))success failure:(void (^)(NSNumber *, NSString *))failure;


/**
 申请上麦
 
 @param uid 申请人
 @param roomOwnerUid 房主uid
 @param success 成功
 @param failure 失败
 */
+(void)applyMicro:(UserID)uid roomOwnerUid:(UserID)roomOwnerUid
          success:(void (^)(void))success
          failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 上麦、申请列表
 
 @param roomOwnerUid 房主
 @param success 成功
 @param failure 失败
 */
+(void) requestMicroList:(UserID)roomOwnerUid
                 success:(void (^)(YPMicroListInfo *microListInfo))success
                 failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 拒绝上麦
 
 @param uid 被拒绝人
 @param roomOwnerUid 房主uid
 @param success 成功
 @param failure 失败
 */
+(void) denyApplyMicro:(UserID)uid roomOwnerUid:(UserID)roomOwnerUid
               success:(void (^)(void))success
               failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 获取房间麦序列表
 
 @param ownerUid 房主UID
 @param success 成功
 @param failure 失败
 */
+ (void)fetchMicroListInfoByOwnerUid:(NSString *)ownerUid success:(void (^)(NSMutableArray *userList))success failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 更新麦序列表

 @param roomOwnerUid 房主uid
 @param type 上麦类型，44更新交换麦序，45房主给用户上麦，46房主踢用户下麦，47房主置顶用户麦序
 @param success 成功
 @param failure 失败
 */
+(void)updateMicroList:(UserID)roomOwnerUid curUids:(NSArray *)curUids type:(NSInteger)type
                success:(void (^)(void))success
                failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 砸蛋排行榜
 
 @param type  1代表今天 2代表昨天
 */
+ (void)userGiftPurseGetRankWithType:(NSInteger)type
                             success:(void (^)(NSMutableArray *list))success
                             failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 砸蛋中奖纪录
 */
+ (void)userGiftPurseRecordWithPageNum:(NSInteger)pageNum
                              pageSize:(NSInteger)pageSize
                               success:(void (^)(NSMutableArray *list))success
                               failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 房间背景列表
 
 @param roomId 房间id
 */
+ (void)roomBgListWithRoomId:(NSInteger)roomId
                     success:(void (^)(NSArray *list))success
                     failure:(void (^)(NSNumber *resCode, NSString *message))failure;
//MARK: - 清空魅力值
+ (void)userReceiveRoomMicMsg:(UserID)uid roomUid:(NSInteger)roomUid
                             
                               success:(void (^)(NSInteger list))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure;
@end
