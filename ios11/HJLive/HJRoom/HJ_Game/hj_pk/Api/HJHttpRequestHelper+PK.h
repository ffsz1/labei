//
//  HJHttpRequestHelper+PK.h
//  HJLive
//
//  Created by apple on 2019/6/18.
//

#import "HJHttpRequestHelper.h"

#import "HJRoomPKGiftModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJHttpRequestHelper (PK)


/**
 
 获取猜拳显示状态

 */
+ (void)pk_getState:(void (^)(BOOL open))success
            failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 猜拳概率

 */
+ (void)pk_getProbability:(void (^)(NSArray* arr))success
                  failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 猜拳礼物

 @param probability 概率(1.高 2.中 3.低) 默认低
 */
+ (void)pk_getMoraInfo:(NSInteger)probability
               success:(void (^)(NSArray* arr,int num,int moraTime))success
               failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 确认发起pk

 @param probability 概率(1.⾼ 2.中 3.低)
 @param choose 选择(1.剪刀 2.石头 3.布)
 @param giftId 礼物ID
 @param giftNum 礼物数量
 */
+ (void)pk_confirmPk:(NSString *)probability
              choose:(NSInteger)choose
              giftId:(NSInteger)giftId
             giftNum:(NSInteger)giftNum
             success:(void (^)(id data))success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 参与PK

 @param recordId 发起记录ID
 */
+ (void)pk_joinPk:(NSString *)recordId
          success:(void (^)(id data))success
          failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 确认加⼊入PK

 @param recordId 发起记录ID
 @param choose 选择(1.剪刀 2.⽯头 3.布)
 */
+ (void)pk_confirmJoinPk:(NSString *)recordId
                  choose:(NSInteger)choose
                 success:(void (^)(id data))success
                 failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 猜拳记录

 @param current 页码
 */
+ (void)pk_record:(NSInteger)current
          success:(void (^)(NSArray *arr))success
          failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 获取房间内未被PK的猜拳消息

 */
+ (void)pk_unSureRecord:(void (^)(NSArray *arr))success
                failure:(void (^)(NSNumber *resCode, NSString *message))failure;

@end

NS_ASSUME_NONNULL_END
