//
//  RechargeCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "BaseCore.h"
#import "BalanceInfo.h"

@interface PurseCore : BaseCore
@property(nonatomic, strong)BalanceInfo *balanceInfo;
@property(nonatomic, strong)DrawExchangeModel *drawExchangeModel;
@property(nonatomic, strong)ChargeGoldModel *chargeGoldModel;
@property(nonatomic, strong)UserInfo *userInfo;

//- (void)requestRechargeList;
//- (void)requestBalanceInfo:(UserID)uid;

//获取已绑定的银行卡信息
- (void)requestWithDrawMyBankCardWith:(UserID)uid;
//绑定银行卡 /withDraw/bankCardBind
- (void)requestWithDrawBankCardBindWith:(NSMutableDictionary*)dict;
//获取银行列表
- (void)requestBankList;
//获取充值列表
- (void)requestRechargeList;
//ForAppStore获取充值列表
- (void)requestRechargeListForAppStore;
- (void)requestBalanceInfo:(UserID)uid;
//邀请码
- (void)requestInvitationUserUpdate:(NSString*)uid;
//获取提现页用户信息
- (void)requestBalanceInfoWithDrawExchange:(UserID)uid;
// 实名认证信息
- (void)requestGetUserRealNameStatus:(UserID)uid  type:(NSString*)type;
//提现请求
- (void)requestWithDrawCash:(UserID)uid pid:(long)pid type:(NSInteger)type account:(NSString*)account accountName:(NSString*)accountName smsCode:(NSString*)smsCode;
//红包提现请求 redpacket/withdraw
- (void)requestRedPacketWithDrawCash:(UserID)uid pid:(long)pid type:(NSInteger)type;
//查询是否能兑换开心
-(void)requestCheckWhiteListWithUid:(UserID)uid;
//兑换开心
- (void)requestChangeGold:(UserID)uid smsCode:(NSString*)smsCode diamondNum:(long)diamondNum roomId:(NSString*)roomId;
//提现列表
- (void)requestGetCashList:(UserID)uid;
//红包提现列表
- (void)requestRedPacketGetCashList:(UserID)uid ;
//邀请好友
- (void)requestStatpacketGet:(UserID)uid ;
//支付渠道
- (void)requestChargeChannels;
//支付渠道Wxapi类型
- (void)requestChargeChannelsForWxapiTpye:(NSString*)prod_id pay4id:(NSString*)pay4id path:(NSString*)path;
//提交支付宝信息 withDraw/bound
- (void)requestPostWithDrawBound:(UserID)uid  code:(NSString*)code aliPayAccount:(NSString*)aliPayAccount aliPayAccountName:(NSString*)aliPayAccountName;
// 判断支付宝支付绑定
- (void)requestPostCheckBindAliPay:(UserID)uid;




/**
 绑定手机号

 @param phoneNum 手机号
 */
- (void)bindingPhoneNum:(NSString *)phoneNum verifyCode:(NSString *)verifyCode;

/**
 通过手机号获取验证码

 @param phoneNum 手机号
 */
- (void)getCodeWithPhoneNum:(NSString *)phoneNum;

/**
 获取验证码
 */
- (void)getSmsWithType:(NSInteger)type;




/**
 查询并且购买内购商品

 @param productID 服务器返回的productID
 */
- (void)requestInAppPurseProductAndBuy:(NSString *)productID;








- (void)queryFirst;

@end

