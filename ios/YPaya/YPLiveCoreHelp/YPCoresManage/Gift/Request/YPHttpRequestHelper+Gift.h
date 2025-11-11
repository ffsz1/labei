//
//  YPHttpRequestHelper+Gift.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"
#import "YPBalanceInfo.h"
#import "YPGiftAllMicroSendInfo.h"

@interface YPHttpRequestHelper (Gift)

+ (void) sendAllMicroGiftByUids:(NSString *)uids
                        giftId:(NSInteger)giftId
                       giftNum:(NSInteger)giftNum
                       roomUid:(NSInteger)roomUid
                       success:(void (^)(YPGiftAllMicroSendInfo *info))success
                       failure:(void (^)(NSNumber *resCode, NSString *message))failure;


+ (void)sendPointWholeMicro:(NSString *)uids giftId:(NSInteger)giftId giftNum:(NSInteger)giftNum roomUid:(NSInteger)roomUid success:(void (^)(YPGiftAllMicroSendInfo *))success failure:(void (^)(NSNumber *, NSString *))failure;

/**
 获取礼物列表

 @param success 成功
 @param failure 失败
 */
+ (void) requestGiftList:(void (^)(NSArray *))success
            failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 获取背包内礼物列表
 */
+ (void)giftListMysticSuccess:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure;

//获取点点币礼物列表
+ (void)giftListDiandianCoinSuccess:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure;

/**
 发送礼物

 @param giftId 礼物id
 @param targetUid 对方id
 @param type 送礼场景1聊天室刷礼物，2一对一送礼物
 @param success 成功
 @param failure 失败
 */
+ (void) sendGift:(NSInteger)giftId targetUid:(UserID)targetUid type:(NSInteger)type
          success:(void (^)(void))success
          failure:(void (^)(NSNumber *resCode, NSString *message))failure;


+ (void)sendPoint:(NSInteger)giftId targetUid:(UserID)targetUid giftNum:(NSInteger)giftNum type:(NSInteger)type success:(void (^)(YPGiftAllMicroSendInfo *info))success failure:(void (^)(NSNumber *, NSString *))failure;


/**
 发送礼物
 
 @param giftId 礼物id
 @param targetUid 对方id
 @param type 送礼场景1聊天室刷礼物，2一对一送礼物
 @param giftNum 发送礼物的数量
 @param success 成功
 @param failure 失败
 */
+ (void)sendGift:(NSInteger)giftId targetUid:(UserID)targetUid giftNum:(NSInteger)giftNum type:(NSInteger)type success:(void (^)(YPGiftAllMicroSendInfo *info))success failure:(void (^)(NSNumber *, NSString *))failure;
@end
