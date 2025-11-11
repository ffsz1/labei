//
//  HttpRequestHelper+Recharge.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"
#import "YPRechargeInfo.h"
#import "YPBalanceInfo.h"
#import "YPWithDrawalInfo.h"

@interface YPHttpRequestHelper (Purse)


 

/**
 通过手机号获取验证码

 @param phoneNum 手机号
 @param success 成功
 @param failure 失败
 */
+ (void)getCodeWithPhoneNum:(NSString *)phoneNum
                    Success:(void (^)(BOOL))success
                    failure:(void (^)(NSNumber *, NSString *))failure;

/**
 绑定手机号

 @param phoneNum 手机号
 @param success 成功
 @param failure 失败
 */
+ (void)bindingPhoneNumber:(NSString *)phoneNum
                verifyCode:(NSString *)verifyCode
                   Success:(void (^)(BOOL))success
                   failure:(void (^)(NSNumber *, NSString *))failure;


/**
 获取手机验证码

 @param type 类型
 @param success 成功
 @param failure 失败
 */
+ (void)getMsmWithType:(NSInteger)type
               Success:(void (^)(BOOL))success
               failure:(void (^)(NSNumber *, NSString *))failure;












/**
 苹果内购下单（服务端）

 @param chargeProdId 内购商品ID
 @param success 成功
 @param failure 失败
 */
+ (void)requestInAppRechargeWithChargeProdId:(NSString *)chargeProdId
                                     success:(void (^)(BOOL , NSString *))success
                                     failure:(void (^)(NSNumber *, NSString *))failure;

/**
 苹果内购二次验证

 @param jsonData 购买成功同步返回的收据数据
 @param success 成功
 @param failure 失败
 */
+ (void)checkReceiptWithData:(NSData *)jsonData orderID:(NSString *)orderID trancid:(NSString *)trancid success:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure;


/**
 获取充值产品列表

 @param success 成功
 @param failure 失败
 */
+(void)requestChargeListWith:(NSNumber *)channel
                     success:(void (^)(NSArray *rechargeInfo))success
                     failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 获余额信息

 @param uid 用户ui
 @param success 成功
 @param failure 失败
 */
+ (void)requestBalanceInfo:(UserID)uid
                   success:(void (^)(YPBalanceInfo* balance))success
                   failure:(void (^)(NSNumber *resCode, NSString *message))failure;

+ (void)queryFirstWithSuccess:(void (^)(BOOL first))success failure:(void (^)(NSNumber *resCode, NSString *message))failure;


+ (void)checkReceiptWithData:(NSData *)jsonData orderID:(NSString *)orderID tranID:(NSString *)tranID success:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure;

//绑定银行卡
+ (void)requestPostWithDrawBankCardBindWith:(NSMutableDictionary*)params  success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure;
//获取已绑定的银行卡信息，当未绑卡时会返回16003错误；参数：uid
+ (void)requestPostWithDrawMyBankCardWith:(UserID)uid  success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure;


+(void)requestGetCashListWith:(UserID)uid
                     success:(void (^)(NSArray *rechargeInfo))success
                     failure:(void (^)(NSNumber *resCode, NSString *message))failure;
//红包提现列表
+ (void)requestRedPacketGetCashListWith:(UserID)uid success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure ;

//银行列表
+ (void)requestBankListSuccess:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure;
//获取提现页用户信息
+ (void)requestBalanceInfoWithDrawExchange:(UserID)uid
success:(void (^)(DrawExchangeModel* balance))success
failure:(void (^)(NSNumber *resCode, NSString *message))failure;
//邀请好友
+ (void)requestGetStatpacketGetWith:(UserID)uid success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure ;

//邀请码
+ (void)requestInvitationUserUpdate:(NSString*)code success:(void (^)(UserInfo *))success failure:(void (^)(NSNumber *, NSString *))failure;
// 实名认证信息
+(void)requestGetUserRealNameStatus:(UserID)uid  type:(NSString*)type success:(void (^)(DrawExchangeModel *))success failure:(void (^)(NSNumber *, NSString *))failure;
//提现请求
+(void)requestWithDrawCash:(UserID)uid pid:(long)pid type:(NSInteger)type account:(NSString*)account accountName:(NSString*)accountName smsCode:(NSString*)smsCode success:(void (^)(DrawExchangeModel *))success failure:(void (^)(NSNumber *, NSString *))failure;
//获取所有提现账户方法
+(void)requestGetFinancialAccount:(UserID)uid success:(void (^)(HJFinancialAccountDataModel *))success failure:(void (^)(NSNumber *, NSString *))failure;
//红包提现请求

+(void)requestRedPacketWithDrawCash:(UserID)uid pid:(long)pid type:(NSInteger)type success:(void (^)(redPacketcashModel *))success failure:(void (^)(NSNumber *, NSString *))failure;
//查询是否可以兑换金币
+(void)requestCheckWhiteListWithUid:(UserID)uid success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure;
//兑换金币
+(void)requestChangeGold:(UserID)uid smsCode:(NSString*)smsCode diamondNum:(long)diamondNum roomId:(NSString*)roomId success:(void (^)(ChargeGoldModel *))success failure:(void (^)(NSNumber *, NSString *))failure;
//+ (void)queryFirstWithSuccess:(void (^)(BOOL first))success failure:(void (^)(NSNumber *resCode, NSString *message))failure;


//+ (void)checkReceiptWithData:(NSData *)jsonData orderID:(NSString *)orderID tranID:(NSString *)tranID success:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure;

//支付渠道
+ (void)requestChargeChannelsSuccess:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure;
//支付渠道Wxapi类型
+ (void)requestChargeChannelsForWxapiTpye:(NSString*)prod_id pay4id:(NSString*)pay4id path:(NSString*)path success:(void (^)(id))success failure:(void (^)(NSNumber *, NSString *))failure;
// 提交支付宝信息
+(void)requestPostWithDrawBound:(UserID)uid  code:(NSString*)code aliPayAccount:(NSString*)aliPayAccount aliPayAccountName:(NSString*)aliPayAccountName success:(void (^)( id))success failure:(void (^)(NSNumber *, NSString *))failure;
// 判断支付宝支付绑定
+(void)requestPostCheckBindAliPay:(UserID)uid  success:(void (^)(id))success failure:(void (^)(NSNumber *, NSString *))failure;
//苹果Appstore充值列表 channel =1
+ (void)requestVerifySetiapChargeListWith:(NSNumber *)channel success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure;



@end
