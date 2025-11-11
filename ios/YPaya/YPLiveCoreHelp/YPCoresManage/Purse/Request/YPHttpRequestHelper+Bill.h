//
//  YPHttpRequestHelper+Bill.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"
#import "YPBalanceInfo.h"
#import "YPRedPageDetailInfo.h"
#import "YPWithdrawalShowInfo.h"

@interface YPHttpRequestHelper (Bill)




/**
 礼物支出

 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页的大小
 @param success 成功回调
 @param failure 失败回调
 */
+ (void)getGiftOutBillWithPageNo:(NSInteger)pageNo
                   time:(NSInteger)time
               pageSize:(NSInteger)pageSize
                    Success:(void (^)(NSMutableArray *,NSMutableArray *))success
                    failure:(void (^)(NSNumber *, NSString *))failure;


/**
 礼物收入

 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 @param success 成功回调
 @param failure 失败回调
 */
+ (void)getGiftInBillWithPageNo:(NSInteger)pageNo
                           time:(NSInteger)time
                       pageSize:(NSInteger)pageSize
                        Success:(void (^)(NSMutableArray *,NSMutableArray *))success
                        failure:(void (^)(NSNumber *, NSString *))failure;

/**
 密聊记录
 
 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 @param success 成功回调
 @param failure 失败回调
 */
+ (void)getChatBillWithPageNo:(NSInteger)pageNo
                           time:(NSInteger)time
                       pageSize:(NSInteger)pageSize
                        Success:(void (^)(NSMutableArray *,NSMutableArray *))success
                        failure:(void (^)(NSNumber *, NSString *))failure;

/**
 充值记录
 
 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 @param success 成功回调
 @param failure 失败回调
 */
+ (void)getRechargeBillWithPageNo:(NSInteger)pageNo
                         time:(NSInteger)time
                     pageSize:(NSInteger)pageSize
                      Success:(void (^)(NSMutableArray *, NSMutableArray *))success
                      failure:(void (^)(NSNumber *, NSString *))failure;





/**
提现记录

@param pageNo 页码
@param time 时间戳
@param pageSize 每页大小
@param success 成功回调
@param failure 失败回调
*/
+ (void)getWithdrawBillWithPageNo:(NSInteger)pageNo
    time:(NSInteger)time
pageSize:(NSInteger)pageSize
 Success:(void (^)(NSMutableArray *, NSMutableArray *))success
                          failure:(void (^)(NSNumber *, NSString *))failure;






@end
