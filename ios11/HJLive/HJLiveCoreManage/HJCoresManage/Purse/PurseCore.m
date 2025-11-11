//
//  RechargeCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "PurseCore.h"
#import "HJPurseCoreClient.h"
#import "HJHttpRequestHelper+Purse.h"
#import "HJAuthCoreHelp.h"
#import "HJBalanceInfoStorage.h"
#import "HJAuthCoreClient.h"
#import "HJNotificationCoreClient.h"
#import "Attachment.h"
#import "NSObject+YYModel.h"

#import "IAPHelper.h"
#import "IAPShare.h"

#import "YYUtility.h"
#import "HJVersionCoreHelp.h"

@interface PurseCore()<HJAuthCoreClient, HJNotificationCoreClient>

@end
@implementation PurseCore
- (instancetype)init
{
    self = [super init];
    if (self) {
        AddCoreClient(HJAuthCoreClient, self);
        AddCoreClient(HJNotificationCoreClient, self);
        _balanceInfo = [HJBalanceInfoStorage getCurrentBalanceInfo];
        if (_balanceInfo == nil) {
            _balanceInfo = [[BalanceInfo alloc] init];
        }
    }
    return self;
}



- (void)dealloc
{
    RemoveCoreClient(HJAuthCoreClient, self);
    RemoveCoreClient(HJNotificationCoreClient, self);
}

- (void)requestInAppPurseProductAndBuy:(NSString *)productID {
    
    [HJHttpRequestHelper requestInAppRechargeWithChargeProdId:productID success:^(BOOL orderStatus, NSString *orderID) {
        if (orderStatus) {
            
            //============================================下单成功调起苹果支付======================================
            
            
            
            NSSet* dataSet = [[NSSet alloc] initWithObjects:productID, nil];
            
            [IAPShare sharedHelper].iap = [[IAPHelper alloc] initWithProductIdentifiers:dataSet];
            
            //
            //    - (void)entryRequestProductProgressStatus:(BOOL)Status;// 查询商品
            //    - (void)entryPurchaseProcessStatus:(XCPaymentStatus)Status; //进入购买流程
            //    - (void)entryCheckReceiptStatus:(BOOL)Status;//进入验签流程
            
            [[IAPShare sharedHelper].iap requestProductsWithCompletion:^(SKProductsRequest *request, SKProductsResponse *response) {
                
                if (response != nil) {
                    NotifyCoreClient(HJPurseCoreClient, @selector(entryRequestProductProgressStatus:), entryRequestProductProgressStatus:YES);
                } else {
                    NotifyCoreClient(HJPurseCoreClient, @selector(entryRequestProductProgressStatus:), entryRequestProductProgressStatus:NO);
                }
                
                SKProduct *selProduct = nil;
                
                if (response.products.count) {
                    for (SKProduct *pro in response.products) {
                        if ([pro.productIdentifier isEqualToString:productID]) {
                            selProduct = pro;
                            break;
                        }
                    }
                }
                
                if (selProduct) {
                    [[IAPShare sharedHelper].iap buyProduct:selProduct onCompletion:^(SKPaymentTransaction *transcation) {
                        
                        
                        /**
                         SKPaymentTransactionStatePurchasing,    // 正在付款当中 (进行当中)
                         SKPaymentTransactionStatePurchased,     // 付款操作已经完成 , 客户端应该完成购买的相关处理
                         SKPaymentTransactionStateFailed,        // 付款操作失败或者取消
                         SKPaymentTransactionStateRestored,      // 从用户的购买历史当中恢复. 客户端也要完成购买的相关处理
                         SKPaymentTransactionStateDeferred   // 未知的状态
                         */
                        NSLog(@"%@",transcation.error.description);
                        // transactionState 交易状态
                        switch(transcation.transactionState) {
                            case SKPaymentTransactionStatePurchased: {
                                NSLog(@"付款完成状态, 要做出相关的处理");
                                NotifyCoreClient(HJPurseCoreClient, @selector(entryPurchaseProcessStatus:), entryPurchaseProcessStatus:XCPaymentStatusPurchased);
                                
                                //同步返回购买成功后，需要请求服务器二次校验
                                NSData *receipt = [NSData dataWithContentsOfURL:[[NSBundle mainBundle] appStoreReceiptURL]];
                                
                                [HJHttpRequestHelper checkReceiptWithData:receipt orderID:orderID tranID:transcation.transactionIdentifier success:^(NSString *orderStatus) {
                                    NotifyCoreClient(HJPurseCoreClient, @selector(entryCheckReceiptSuccess), entryCheckReceiptSuccess);
                                } failure:^(NSNumber *resCode, NSString *message) {
                                    NSLog(@"message%@",message);
                                    NotifyCoreClient(HJPurseCoreClient, @selector(entryCheckReceiptFaildWithMessage:), entryCheckReceiptFaildWithMessage:message);
                                }];
                                break;
                            }
                            case SKPaymentTransactionStateRestored: {
                                NSLog(@"恢复状态, 要做出相关的处理");
                                break;
                            }
                            case SKPaymentTransactionStateFailed: {
                                NSLog(@"购买失败");
                                NotifyCoreClient(HJPurseCoreClient, @selector(entryPurchaseProcessStatus:), entryPurchaseProcessStatus:XCPaymentStatusFailed);
                                break;
                            }
                            case SKPaymentTransactionStatePurchasing: {
                                NSLog(@"正在购买中");
                                NotifyCoreClient(HJPurseCoreClient, @selector(entryPurchaseProcessStatus:), entryPurchaseProcessStatus:XCPaymentStatusPurchasing);
                                break;
                            }
                            default: {
                                NotifyCoreClient(HJPurseCoreClient, @selector(entryPurchaseProcessStatus:), entryPurchaseProcessStatus:XCPaymentStatusDeferred);
                                NSLog(@"其它");
                            }
                        }
                        
                        
                        
                        
                        //                //本地二次验证  调试用
                        //                [[IAPShare sharedHelper].iap checkReceipt:receipt onCompletion:^(NSString *response, NSError *error) {
                        //                    NSLog(@"%@",response);
                        //
                        //                }];
                    }];
                }
            }];
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(addRechargeOrderFail:), addRechargeOrderFail:message);
    }];
}

- (void)requestRechargeList {
    [HJHttpRequestHelper requestChargeListWith:@(5) success:^(NSArray *rechargeInfo) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestRechargeListSuccess:), onRequestRechargeListSuccess:rechargeInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestRechargeListFailth), onRequestRechargeListFailth);
    }];
}



- (void)requestBalanceInfo:(UserID)uid
{
    @weakify(self)
    [HJHttpRequestHelper requestBalanceInfo:uid success:^(BalanceInfo *balance) {
        @strongify(self)
        self.balanceInfo = balance;
        [HJBalanceInfoStorage saveBalanceInfo:balance];
        NotifyCoreClient(HJPurseCoreClient, @selector(onBalanceInfoUpdate:), onBalanceInfoUpdate:balance);
    } failure:^(NSNumber *resCode, NSString *message) {
    }];
}

//获取已绑定的银行卡信息，当未绑卡时会返回16003错误 /withDraw/myBankCard
- (void)requestWithDrawMyBankCardWith:(UserID)uid {
    [HJHttpRequestHelper requestPostWithDrawMyBankCardWith:uid success:^(NSArray *bankInfoList) {
         NotifyCoreClient(HJPurseCoreClient, @selector(onRequestWithDrawMyBankCardSuccess:), onRequestWithDrawMyBankCardSuccess:bankInfoList);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestWithDrawMyBankCardFailth:), onRequestWithDrawMyBankCardFailth:resCode);
    }];
    
}


//绑定银行卡 /withDraw/bankCardBind
- (void)requestWithDrawBankCardBindWith:(NSMutableDictionary*)dict {
    [HJHttpRequestHelper requestPostWithDrawBankCardBindWith:dict success:^(NSArray *bankInfoList) {
         NotifyCoreClient(HJPurseCoreClient, @selector(onRequestWithDrawBankCardBindSuccess:), onRequestWithDrawBankCardBindSuccess:bankInfoList);
    } failure:^(NSNumber *resCode, NSString *message) {
         NotifyCoreClient(HJPurseCoreClient, @selector(onRequestWithDrawBankCardBindFailth), onRequestWithDrawBankCardBindFailth);
    }];
    
    
    
}
//获取银行列表
- (void)requestBankList {
    [HJHttpRequestHelper requestBankListSuccess:^(NSArray *bankInfoList) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestBankInfoListSuccess:), onRequestBankInfoListSuccess:bankInfoList);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestBankInfoListFailth), onRequestBankInfoListFailth);
    }];
}

////充值列表
//- (void)requestRechargeList {
//
//    [HJHttpRequestHelper requestChargeListWith:@(5) success:^(NSArray *rechargeInfo) {
//        NotifyCoreClient(PurseCoreClient, @selector(onRequestRechargeListSuccess:), onRequestRechargeListSuccess:rechargeInfo);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        NotifyCoreClient(PurseCoreClient, @selector(onRequestRechargeListFailth), onRequestRechargeListFailth);
//    }];
//
//}
//充值列表ForAppStore
- (void)requestRechargeListForAppStore {

    [HJHttpRequestHelper requestVerifySetiapChargeListWith:@(1) success:^(NSArray *rechargeInfo) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestRechargeListSuccess:), onRequestRechargeListSuccess:rechargeInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestRechargeListFailth), onRequestRechargeListFailth);
    }];

}

//邀请好友
- (void)requestStatpacketGet:(UserID)uid {
    [HJHttpRequestHelper requestGetStatpacketGetWith:uid success:^(NSArray *rechargeInfo) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestStatpacketGetSuccess:), onRequestStatpacketGetSuccess:rechargeInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestStatpacketGetFailth), onRequestStatpacketGetFailth);
    }];
}

//提现列表
- (void)requestGetCashList:(UserID)uid {
    [HJHttpRequestHelper requestGetCashListWith:uid success:^(NSArray *rechargeInfo) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestGetCashListSuccess:), onRequestGetCashListSuccess:rechargeInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestGetCashListFailth), onRequestGetCashListFailth);
    }];
}
//红包提现列表
- (void)requestRedPacketGetCashList:(UserID)uid {
    [HJHttpRequestHelper requestRedPacketGetCashListWith:uid success:^(NSArray *rechargeInfo) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestRedPacketGetCashListSuccess:), onRequestRedPacketGetCashListSuccess:rechargeInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(onRequestRedPacketGetCashListFailth), onRequestRedPacketGetCashListFailth);
    }];
}


//支付渠道
- (void)requestChargeChannels
{
    @weakify(self)
    [HJHttpRequestHelper requestChargeChannelsSuccess:^(id balance) {
        @strongify(self)
      
        NotifyCoreClient(HJPurseCoreClient, @selector(onChargeChannels:), onChargeChannels:balance);
    } failure:^(NSNumber *resCode, NSString *message) {
        
         NotifyCoreClient(HJPurseCoreClient, @selector(failOnChargeChannels:message:), failOnChargeChannels:resCode message:message);
    }];
}
//支付渠道Wxapi类型
- (void)requestChargeChannelsForWxapiTpye:(NSString*)prod_id pay4id:(NSString*)pay4id path:(NSString*)path
{
    @weakify(self)
    [HJHttpRequestHelper requestChargeChannelsForWxapiTpye:prod_id pay4id:pay4id path:path success:^(id data) {
          @strongify(self)
         NotifyCoreClient(HJPurseCoreClient, @selector(onChargeChannelsForWxapiTpye:), onChargeChannelsForWxapiTpye:data);
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}




// 开心数
//- (void)requestBalanceInfo:(UserID)uid
//{
//    @weakify(self)
//    [HJHttpRequestHelper requestBalanceInfo:uid success:^(BalanceInfo *balance) {
//        @strongify(self)
//        self.balanceInfo = balance;
//        [BalanceInfoStorage saveBalanceInfo:balance];
//        NotifyCoreClient(PurseCoreClient, @selector(onBalanceInfoUpdate:), onBalanceInfoUpdate:balance);
//    } failure:^(NSNumber *resCode, NSString *message) {
//    }];
//}

//获取提现页用户信息：/withDraw/exchange life
- (void)requestBalanceInfoWithDrawExchange:(UserID)uid{
    @weakify(self)
       [HJHttpRequestHelper requestBalanceInfoWithDrawExchange:uid success:^(DrawExchangeModel *balance) {
           @strongify(self)
           self.drawExchangeModel = balance;
           [HJBalanceInfoStorage saveDrawExchangeModel:balance];
           NotifyCoreClient(HJPurseCoreClient, @selector(onDrawExchangeInfoUpdate:), onDrawExchangeInfoUpdate:balance);
       } failure:^(NSNumber *resCode, NSString *message) {
       }];
}

//查询是否能兑换开心
-(void)requestCheckWhiteListWithUid:(UserID)uid{

    [HJHttpRequestHelper requestCheckWhiteListWithUid:uid success:^(BOOL isSuccess) {
         NotifyCoreClient(HJPurseCoreClient, @selector(onCheckWhiteListSuccess:), onCheckWhiteListSuccess:isSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
         NotifyCoreClient(HJPurseCoreClient, @selector(onCheckWhiteListFail:), onCheckWhiteListFail:message);
    }];
    
}
 

//兑换请求

- (void)requestChangeGold:(UserID)uid smsCode:(NSString*)smsCode diamondNum:(long)diamondNum roomId:(NSString*)roomId{
    @weakify(self)
    [HJHttpRequestHelper requestChangeGold:uid smsCode:smsCode diamondNum:diamondNum roomId:roomId success:^(ChargeGoldModel *balance) {
           @strongify(self)
           
           DrawExchangeModel* exchangeModel = GetCore(PurseCore).drawExchangeModel;
                 BalanceInfo* info = GetCore(PurseCore).balanceInfo;
           
           exchangeModel.diamondNum = [NSString stringWithFormat:@"%.0lf",balance.diamondNum];
           info.diamondNum = [NSString stringWithFormat:@"%.0lf",balance.diamondNum];
           info.goldNum = [NSString stringWithFormat:@"%ld",balance.goldNum];
           
           
           self.chargeGoldModel = balance;
           [HJBalanceInfoStorage saveDrawExchangeModel:exchangeModel];
            [HJBalanceInfoStorage saveBalanceInfo:info];
           NotifyCoreClient(HJPurseCoreClient, @selector(onChargeGoldInfoUpdate:), onChargeGoldInfoUpdate:balance);
       } failure:^(NSNumber *resCode, NSString *message) {
        
             NotifyCoreClient(HJPurseCoreClient, @selector(onChargeGoldInfoUpdateFail:), onChargeGoldInfoUpdateFail:message);
       }];
}


//提交支付宝信息 withDraw/bound
- (void)requestPostWithDrawBound:(UserID)uid  code:(NSString*)code aliPayAccount:(NSString*)aliPayAccount aliPayAccountName:(NSString*)aliPayAccountName {
    @weakify(self)
    [HJHttpRequestHelper requestPostWithDrawBound:uid code:code aliPayAccount:aliPayAccount aliPayAccountName:aliPayAccountName success:^(id data) {
         @strongify(self)
         NotifyCoreClient(HJPurseCoreClient, @selector(succUserWithDrawBound:), succUserWithDrawBound:data);
    } failure:^(NSNumber *resCode, NSString *message) {
         NotifyCoreClient(HJPurseCoreClient, @selector(failUserWithDrawBound:message:), failUserWithDrawBound:resCode message:message);
    }];

}

// 判断支付宝支付绑定
- (void)requestPostCheckBindAliPay:(UserID)uid{
    @weakify(self)
       [HJHttpRequestHelper requestPostCheckBindAliPay:uid success:^(id data) {
           @strongify(self)
         
           NotifyCoreClient(HJPurseCoreClient, @selector(onRequestcheckBindAliPaySuccess:), onRequestcheckBindAliPaySuccess:data);
       } failure:^(NSNumber *resCode, NSString *message) {
//            NotifyCoreClient(PurseCoreClient, @selector(failUserRealNameStatus:message:), failUserRealNameStatus:resCode message:message);
       }];
}

// 实名认证信息
- (void)requestGetUserRealNameStatus:(UserID)uid  type:(NSString*)type{
    @weakify(self)
       [HJHttpRequestHelper requestGetUserRealNameStatus:uid type:type success:^(DrawExchangeModel *balance) {
           @strongify(self)
         
           NotifyCoreClient(HJPurseCoreClient, @selector(succUserRealNameStatus:), succUserRealNameStatus:YES);
       } failure:^(NSNumber *resCode, NSString *message) {
            NotifyCoreClient(HJPurseCoreClient, @selector(failUserRealNameStatus:message:), failUserRealNameStatus:resCode message:message);
       }];
}


//提现请求 withDraw/withDrawCash

- (void)requestWithDrawCash:(UserID)uid pid:(long)pid type:(NSInteger)type account:(NSString*)account accountName:(NSString*)accountName smsCode:(NSString *)smsCode{
//    @weakify(self)
       [HJHttpRequestHelper requestWithDrawCash:uid  pid:pid  type:type account:account accountName:accountName smsCode:smsCode success:^(DrawExchangeModel *balance) {
//           @strongify(self)
//           self.drawExchangeModel = balance;
//           [HJBalanceInfoStorage saveDrawExchangeModel:balance];
//           NotifyCoreClient(HJPurseCoreClient, @selector(onDrawCashSuccess:), onDrawCashSuccess:balance);
           
           [[NSNotificationCenter defaultCenter] postNotificationName:@"drawCashNotification" object:@"success"];
       } failure:^(NSNumber *resCode, NSString *message) {
           [[NSNotificationCenter defaultCenter] postNotificationName:@"drawCashNotification" object:message];
//             NotifyCoreClient(HJPurseCoreClient, @selector(onDrawCashFail), onDrawCashFail);
       }];
}
//红包提现请求 redpacket/withdraw

- (void)requestRedPacketWithDrawCash:(UserID)uid pid:(long)pid type:(NSInteger)type{
    @weakify(self)
       [HJHttpRequestHelper requestRedPacketWithDrawCash:uid  pid:pid  type:type success:^(redPacketcashModel *balance) {
           @strongify(self)
         
           NotifyCoreClient(HJPurseCoreClient, @selector(onRedPacketDrawCashSuccess:), onRedPacketDrawCashSuccess:balance);
       } failure:^(NSNumber *resCode, NSString *message) {
//             NotifyCoreClient(PurseCoreClient, @selector(onRedPacketDrawCashFail), onRedPacketDrawCashFail);
       }];
}




//获取验证码
- (void)getSmsWithType:(NSInteger)type {
    [HJHttpRequestHelper getMsmWithType:type Success:^(BOOL isSuccess) {
        NotifyCoreClient(HJPurseCoreClient, @selector(getSmsSuccess), getSmsSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(getSmsFaildWithMessage:), getSmsFaildWithMessage:message);
    }];
}

//通过手机号获取验证码
- (void)getCodeWithPhoneNum:(NSString *)phoneNum {
    [HJHttpRequestHelper getCodeWithPhoneNum:phoneNum Success:^(BOOL success) {
        NotifyCoreClient(HJPurseCoreClient, @selector(getSmsSuccess), getSmsSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(getSmsFaildWithMessage:), getSmsFaildWithMessage:message);
    }];
}

//绑定手机号
- (void)bindingPhoneNum:(NSString *)phoneNum verifyCode:(NSString *)verifyCode {
    [HJHttpRequestHelper bindingPhoneNumber:phoneNum verifyCode:verifyCode Success:^(BOOL success) {
        NotifyCoreClient(HJPurseCoreClient, @selector(bindingPhoneNumberSuccess), bindingPhoneNumberSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(bindingPhoneNumberFailth:), bindingPhoneNumberFailth:message);
    }];
}


- (void)queryFirst {
    
    [HJHttpRequestHelper queryFirstWithSuccess:^(BOOL first) {
        NotifyCoreClient(HJPurseCoreClient, @selector(queryFirstSuccessWithFirst:), queryFirstSuccessWithFirst:first)
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseCoreClient, @selector(queryFirstFailth:), queryFirstFailth:message)
    }];
}



#pragma mark - AuthCoreClient
- (void)onLoginSuccess
{
    UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
    
    [self requestBalanceInfo:uid];
}

- (void)onKicked
{
    self.balanceInfo = [[BalanceInfo alloc] init];
    [HJBalanceInfoStorage saveBalanceInfo:nil];
}

- (void)onLogout
{
    self.balanceInfo = [[BalanceInfo alloc] init];
    [HJBalanceInfoStorage saveBalanceInfo:nil];
}

#pragma mark - NotificationCoreClient
- (void)onRecvCustomP2PNoti:(NIMCustomSystemNotification *)notification
{
    Attachment *attachment = [Attachment yy_modelWithJSON:notification.content];
    if (attachment.first == Custom_Noti_Header_Account){
        BalanceInfo *info = [BalanceInfo yy_modelWithDictionary:attachment.data];
        self.balanceInfo = info;
        [HJBalanceInfoStorage saveBalanceInfo:info];
        NotifyCoreClient(HJPurseCoreClient, @selector(onBalanceInfoUpdate:), onBalanceInfoUpdate:info);
    }
}
@end

