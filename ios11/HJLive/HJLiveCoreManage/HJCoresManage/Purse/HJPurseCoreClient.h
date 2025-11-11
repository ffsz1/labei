//
//  RechargeCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BalanceInfo.h"
#import "HJWithDrawalInfo.h"
#import "BalanceInfo.h"

typedef NS_ENUM(NSInteger ,XCPaymentStatus) {
    XCPaymentStatusPurchasing,//付款中
    XCPaymentStatusPurchased, //付款操作已经完成
    XCPaymentStatusFailed, //付款操作失败
    XCPaymentStatusDeferred, //未知状态
};

/**
 SKPaymentTransactionStatePurchasing,    // 正在付款当中 (进行当中)
 SKPaymentTransactionStatePurchased,     // 付款操作已经完成 , 客户端应该完成购买的相关处理
 SKPaymentTransactionStateFailed,        // 付款操作失败或者取消
 SKPaymentTransactionStateRestored,      // 从用户的购买历史当中恢复. 客户端也要完成购买的相关处理
 SKPaymentTransactionStateDeferred   // 未知的状态
 */


@protocol HJPurseCoreClient <NSObject>
@optional
//- (void)onRequestRechargeListSuccess:(NSArray *)list;
//- (void)onRequestRechargeListFailth;

//- (void)onBalanceInfoUpdate:(BalanceInfo*)balanceInfo;

//绑定银行卡
- (void)onRequestWithDrawBankCardBindSuccess:(NSArray *)list;
-(void)onRequestWithDrawBankCardBindFailth;

//获取已绑定的银行卡信息
- (void)onRequestWithDrawMyBankCardSuccess:(NSArray *)list;
-(void)onRequestWithDrawMyBankCardFailth:(NSNumber *)resCode;

//是否绑定支付宝信息成功回调
- (void)onRequestcheckBindAliPaySuccess:(id )data;

//银行信息列表 life
- (void)onRequestBankInfoListSuccess:(NSArray *)list;
- (void)onRequestBankInfoListFailth;
//充值列表
- (void)onRequestRechargeListSuccess:(NSArray *)list;
- (void)onRequestRechargeListFailth;

//邀请好友
- (void)onRequestStatpacketGetSuccess:(NSArray *)list;
- (void)onRequestStatpacketGetFailth;



//提现列表回调
- (void)onRequestGetCashListSuccess:(NSArray *)list;
- (void)onRequestGetCashListFailth;

//红包提现列表回调
- (void)onRequestRedPacketGetCashListSuccess:(NSArray *)list;
- (void)onRequestRedPacketGetCashListFailth;


//支付渠道
- (void)onChargeChannels:(id)data;
- (void)failOnChargeChannels:(NSNumber *)code message:(NSString*)message;
//支付渠道Wxapi类型
- (void)onChargeChannelsForWxapiTpye:(id)data;

//提交支付宝信息接口
-(void)succUserWithDrawBound:(id)data;
-(void)failUserWithDrawBound:(NSNumber *)code message:(NSString*)message;

//实名认证
-(void)succUserRealNameStatus:(BOOL)code;
-(void)failUserRealNameStatus:(NSNumber*)code message:(NSString*)message;

//邀请好友页面 填写邀请码
-(void)onInvitationUserUpdate:(UserInfo*)userInfo;
-(void)failInvitationUserUpdate:(NSNumber *)code message:(NSString*)message;



- (void)onDrawExchangeInfoUpdate:(DrawExchangeModel*)balanceInfo;
//提现通知
- (void)onDrawCashSuccess:(DrawExchangeModel*)balanceInfo;
//红包提现通知
- (void)onRedPacketDrawCashSuccess:(redPacketcashModel*)balanceInfo;

- (void)onDrawCashFail;
//兑换成功回调
- (void)onChargeGoldInfoUpdate:(ChargeGoldModel*)balanceInfo;
- (void)onChargeGoldInfoUpdateFail:(NSString*)message;

//查询是否能兑换开心
- (void)onCheckWhiteListSuccess:(BOOL)isSuccess;
- (void)onCheckWhiteListFail:(NSString*)message;

- (void)onBalanceInfoUpdate:(BalanceInfo*)balanceInfo;
- (void)onBalanceInfoWithDrawExchangeUpdate:(BalanceInfo*)balanceInfo;






//MARK:=========苹果支付=========
- (void)addRechargeOrderFail:(NSString *)message; //下单失败
- (void)entryRequestProductProgressStatus:(BOOL)Status;// 查询商品
- (void)entryPurchaseProcessStatus:(XCPaymentStatus)Status; //进入购买流程
- (void)entryCheckReceiptSuccess;//二次验证成功
- (void)entryCheckReceiptFaildWithMessage:(NSString *)message;//二次验证失败


- (void)getSmsSuccess;
- (void)getSmsFaildWithMessage:(NSString *)message;


//MARK:=========绑定手机号==========
- (void)bindingPhoneNumberSuccess;  // 绑定手机号成功
- (void)bindingPhoneNumberFailth:(NSString *)message; //绑定手机号失败

- (void)neenVerify;

- (void)queryFirstSuccessWithFirst:(BOOL)first;
- (void)queryFirstFailth:(NSString *)message;


@end

