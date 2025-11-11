////
////  HttpRequestHelper+Recharge.m
////  HJLive
////
////  Created by feiyin on 2020/7/5.
////  Copyright © 2020 com.wdqj.gz. All rights reserved.
////
//
//#import "HJHttpRequestHelper+Purse.h"
//#import "NSObject+YYModel.h"
//#import "YYUtility.h"
//#import "NSString+Base64.h"
//#import "HJAuthCoreHelp.h"
//#import "HJRedWithdrawalsListInfo.h"
//#import "BalanceInfo.h"
//#import "HJVersionCoreHelp.h"
//
//@implementation HJHttpRequestHelper (Purse)
//
//
//
//
//+ (void)requestChargeListWith:(NSNumber *)channel success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
//    NSString *method = @"chargeprod/list";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    [params setObject:channel forKey:@"channelType"];
//
//    [HJHttpRequestHelper GET:method params:params success:^(id data) {
//        NSArray *rechargeList = [NSArray yy_modelArrayWithClass:[RechargeInfo class] json:data];
//        success(rechargeList);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
//}
//
//+ (void)requestBalanceInfo:(UserID)uid success:(void (^)(BalanceInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
//{
//    NSString *method = @"purse/query";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
//    [params setObject:ticket forKey:@"ticket"];
//    [params setObject:@(uid) forKey:@"uid"];
//
//    [HJHttpRequestHelper GET:method params:params success:^(id data) {
//        BalanceInfo *balanceInfo = [BalanceInfo yy_modelWithJSON:data];
//        success(balanceInfo);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
//}
//
//+ (void)checkReceiptWithData:(NSData *)jsonData orderID:(NSString *)orderID trancid:(NSString *)trancid success:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure {
//
//    NSString *receiptBase64 = [NSString base64StringFromData:jsonData length:[jsonData length]];
//
//    NSString *method = @"verify/setiap";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
//    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
//    [params setObject:receiptBase64 forKey:@"receipt"];
//#ifdef DEBUG
//    [params setObject:@"false" forKey:@"chooseEnv"];
//#else
//    [params setObject:@"true" forKey:@"chooseEnv"];
//#endif
//    [params setObject:orderID forKey:@"chargeRecordId"];
//    [params setObject:uid forKey:@"uid"];
//    [params setObject:ticket forKey:@"ticket"];
//    params[@"trancid"] = trancid;
//
//    [HJHttpRequestHelper POST:method params:params success:^(id data) {
//        success(data);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
//}
//
////内购下单
//+ (void)requestInAppRechargeWithChargeProdId:(NSString *)chargeProdId success:(void (^)(BOOL , NSString *))success failure:(void (^)(NSNumber *, NSString *))failure {
//    NSString *method = @"order/place";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    NSString* uid = [GetCore(HJAuthCoreHelp) getUid];
//    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
//    [params setObject:ticket forKey:@"ticket"];
//    [params setObject:uid forKey:@"uid"];
//    [params setObject:chargeProdId forKey:@"chargeProdId"];
//    [params setObject:[YYUtility ipAddress] forKey:@"clientIp"];
//
//    [HJHttpRequestHelper POST:method params:params success:^(id data) {
//        NSString *orderid = (NSString *)data[@"recordId"];
//        success(YES,orderid);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
//}
//
//
//
////获取手机验证码
//+ (void)getMsmWithType:(NSInteger)type Success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
//    NSString *method = @"withDraw/getSms";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
//    [params setObject:uid forKey:@"uid"];
//
//    [HJHttpRequestHelper GET:method params:params success:^(id data) {
//        success(YES);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        failure(resCode, message);
//    }];
//}
//
////通过手机号获取验证码
//+ (void)getCodeWithPhoneNum:(NSString *)phoneNum Success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
//    NSString *method = @"withDraw/phoneCode";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    [params setObject:phoneNum forKey:@"phone"];
//    [HJHttpRequestHelper GET:method params:params success:^(id data) {
//        success(YES);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        failure(resCode,message);
//    }];
//}
//
////绑定手机
//+ (void)bindingPhoneNumber:(NSString *)phoneNum verifyCode:(NSString *)verifyCode Success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
//    NSString *method = @"withDraw/phone";
//    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
//    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    [params setObject:uid forKey:@"uid"];
//    [params setObject:ticket forKey:@"ticket"];
//    [params setObject:phoneNum forKey:@"phone"];
//    [params setObject:verifyCode forKey:@"code"];
//    [HJHttpRequestHelper POST:method params:params success:^(id data) {
//        success(YES);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        failure(resCode,message);
//    }];
//
//}
//
//+ (void)queryFirstWithSuccess:(void (^)(BOOL first))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
//
//    NSString *method = @"/purse/queryFirst";
//    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    params[@"uid"] = uid;
//
//    [HJHttpRequestHelper GET:method params:params success:^(id data) {
//        BOOL first = [data boolValue];
//        success(first);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        failure(resCode,message);
//    }];
//
//}
//
//+ (void)checkReceiptWithData:(NSData *)jsonData orderID:(NSString *)orderID tranID:(NSString *)tranID success:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure {
//
//    NSString *receiptBase64 = [NSString base64StringFromData:jsonData length:[jsonData length]];
//
//    NSString *method = @"verify/setiap";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
//    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
//    [params setObject:receiptBase64 forKey:@"receipt"];
//
//    if (GetCore(HJVersionCoreHelp).isReleaseEnv) {
//        [params setObject:@"true" forKey:@"chooseEnv"];
//    } else {
//        [params setObject:@"false" forKey:@"chooseEnv"];
//    }
//
//    [params setObject:tranID forKey:@"trancid"];
//    [params setObject:orderID forKey:@"chargeRecordId"];
//    [params setObject:uid forKey:@"uid"];
//    [params setObject:ticket forKey:@"ticket"];
//
//    [HJHttpRequestHelper POST:method params:params success:^(id data) {
//        success(data);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
//}
//
//@end


//
//  HttpRequestHelper+Recharge.m
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Purse.h"
#import "NSObject+YYModel.h"
#import "YYUtility.h"
#import "NSString+Base64.h"
#import "HJAuthCoreHelp.h"
#import "HJRedWithdrawalsListInfo.h"
#import "BalanceInfo.h"
#import "HJVersionCoreHelp.h"
#import "FYGetCashInfoModel.h"
#import "FYBankBindInfoModel.h"
#import "FYBankInfoListModel.h"
@implementation HJHttpRequestHelper (Purse)

////邀请好友
//+ (void)requestGetStatpacketGetWith:(UserID)uid success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
//    NSString *method = @"statpacket/get";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//
//
//    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
//       [params setObject:ticket forKey:@"ticket"];
//       [params setObject:@(uid) forKey:@"uid"];
//
//
//    [HJHttpRequestHelper GET:method params:params success:^(id data) {
//
//        FYInvitationFriendModel* model = [FYInvitationFriendModel yy_modelWithJSON:data];
//        NSArray *rechargeList = [NSArray arrayWithObject:model];
//        success(rechargeList);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
//}


//支付渠道
+ (void)requestChargeChannelsSuccess:(void (^)(id))success failure:(void (^)(NSNumber *, NSString *))failure {
//    NSString *method = @"apip/charge/channels";//旧的
//     NSString *method = @"apip/charge/channelList";//不加密
     NSString *method = @"apip/charge/channelList?en=1";//加密
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *userId = [GetCore(HJAuthCoreHelp) getUid];
    UserInfo* uinfo =     [GetCore(HJUserCoreHelp) getUserInfoInDB:[userId intValue]];
    [params setObject:uinfo.erbanNo forKey:@"pay4id"];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
       [params setObject:ticket forKey:@"ticket"];
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
      
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


//支付渠道Wxapi类型
+ (void)requestChargeChannelsForWxapiTpye:(NSString*)prod_id pay4id:(NSString*)pay4id path:(NSString*)path success:(void (^)(id))success failure:(void (^)(NSNumber *, NSString *))failure {

     NSString *method = path;//加密
    NSMutableDictionary *params = [NSMutableDictionary dictionary];

    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
       [params setObject:ticket forKey:@"ticket"];
     [params setObject:prod_id forKey:@"prod_id"];
     [params setObject:pay4id forKey:@"pay4id"];
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
      
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}



//提现列表
+ (void)requestGetCashListWith:(UserID)uid success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"withDraw/findList";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];

    
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
       [params setObject:ticket forKey:@"ticket"];
       [params setObject:@(uid) forKey:@"uid"];
    
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *rechargeList = [NSArray yy_modelArrayWithClass:[FYGetCashInfoModel class] json:data];
        success(rechargeList);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//红包提现列表
+ (void)requestRedPacketGetCashListWith:(UserID)uid success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"redpacket/list";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];

    
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
       [params setObject:ticket forKey:@"ticket"];
       [params setObject:@(uid) forKey:@"uid"];
    
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *rechargeList = [NSArray yy_modelArrayWithClass:[FYRedPacketGetCashInfoModel class] json:data];
        success(rechargeList);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}



//获取已绑定的银行卡信息，当未绑卡时会返回16003错误；参数：uid
+ (void)requestPostWithDrawMyBankCardWith:(UserID)uid  success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *method = @"withDraw/myBankCard";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
       [params setObject:ticket forKey:@"ticket"];
       [params setObject:@(uid) forKey:@"uid"];
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
//        NSArray *rechargeList = [NSArray yy_modelArrayWithClass:[FYBankBindInfoModel class] json:data];
         FYBankBindInfoModel* model =  [FYBankBindInfoModel yy_modelWithDictionary:data];
        NSArray* rechargeList = [NSArray arrayWithObject:model];
        success(rechargeList);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

//绑定银行卡
+ (void)requestPostWithDrawBankCardBindWith:(NSMutableDictionary*)params  success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"withDraw/bankCardBind";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
       [params setObject:ticket forKey:@"ticket"];
       [params setObject:@([GetCore(HJAuthCoreHelp).getUid userIDValue]) forKey:@"uid"];

    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        NSArray *rechargeList = [NSArray yy_modelArrayWithClass:[FYBankBindInfoModel class] json:data];
        success(rechargeList);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

//银行列表
+ (void)requestBankListSuccess:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {

    NSString *method = @"withDraw/bankList";
    [HJHttpRequestHelper GET:method params:nil success:^(id data) {
        NSArray *bankInfoList = [NSArray yy_modelArrayWithClass:[FYBankInfoListModel class] json:data];
        success(bankInfoList);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


//充值列表
+ (void)requestChargeListWith:(NSNumber *)channel success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
//    NSString *method = @"chargeprod/list";
    NSString *method = @"apip/charge/prodlist";
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:channel forKey:@"channelType"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *rechargeList = [NSArray yy_modelArrayWithClass:[RechargeInfo class] json:data];
        success(rechargeList);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

//苹果Appstore充值列表
+ (void)requestVerifySetiapChargeListWith:(NSNumber *)channel success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {

    NSString *method = @"chargeprod/list";
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:channel forKey:@"channelType"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *rechargeList = [NSArray yy_modelArrayWithClass:[RechargeInfo class] json:data];
        success(rechargeList);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}







// 开心数
+ (void)requestBalanceInfo:(UserID)uid success:(void (^)(BalanceInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"purse/query";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(uid) forKey:@"uid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        BalanceInfo *balanceInfo = [BalanceInfo yy_modelWithJSON:data];
        success(balanceInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}



// 邀请码 UserUpdate
+ (void)requestInvitationUserUpdate:(NSString*)code success:(void (^)(UserInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"/user/update";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@([GetCore(HJAuthCoreHelp) getUid].userIDValue) forKey:@"uid"];
    [params setObject:code forKey:@"shareCode"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
         UserInfo *userInfo = [UserInfo yy_modelWithDictionary:data];
//        DrawExchangeModel *drawExchangeModel = [DrawExchangeModel yy_modelWithJSON:data];
        success(userInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


// /withDraw/exchange
+ (void)requestBalanceInfoWithDrawExchange:(UserID)uid success:(void (^)(DrawExchangeModel *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"withDraw/exchange";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(uid) forKey:@"uid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        DrawExchangeModel *drawExchangeModel = [DrawExchangeModel yy_modelWithJSON:data];
        success(drawExchangeModel);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

//查询是否可以兑换开心
+(void)requestCheckWhiteListWithUid:(UserID)uid success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"change/checkWhiteList";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
  
    [params setObject:@(uid) forKey:@"uid"];
   
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        BOOL isSuccess = [data boolValue];
        success(isSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//兑换请求

+(void)requestChangeGold:(UserID)uid smsCode:(NSString*)smsCode diamondNum:(long)diamondNum roomId:(NSString*)roomId success:(void (^)(ChargeGoldModel *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"change/gold";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(diamondNum) forKey:@"diamondNum"];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:smsCode forKey:@"smsCode"];

       NSString *userId = [GetCore(HJAuthCoreHelp) getUid];
       UserInfo *info = [GetCore(HJUserCoreHelp) getUserInfoInDB:[userId intValue]];
       [params setObject:info.phone forKey:@"phone"];
    if (roomId.length>0) {
         [params setObject:@([roomId doubleValue]) forKey:@"roomId"];
    }
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        ChargeGoldModel *chargeGoldModel = [ChargeGoldModel yy_modelWithJSON:data];
        success(chargeGoldModel);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


// 提交支付宝信息
+(void)requestPostWithDrawBound:(UserID)uid  code:(NSString*)code aliPayAccount:(NSString*)aliPayAccount aliPayAccountName:(NSString*)aliPayAccountName success:(void (^)(id))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"withDraw/bound";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:code forKey:@"code"];
    [params setObject:@(uid) forKey:@"uid"];
     [params setObject:aliPayAccount forKey:@"aliPayAccount"];
     [params setObject:aliPayAccountName forKey:@"aliPayAccountName"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
//        DrawExchangeModel *drawExchangeModel = [DrawExchangeModel yy_modelWithJSON:data];
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


// 判断支付宝支付绑定
+(void)requestPostCheckBindAliPay:(UserID)uid  success:(void (^)(id))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"wxPublic/checkBindAliPay";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
  
    [params setObject:@(uid) forKey:@"uid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
// 实名认证信息

+(void)requestGetUserRealNameStatus:(UserID)uid  type:(NSString*)type success:(void (^)(DrawExchangeModel *))success failure:(void (^)(NSNumber *, NSString *))failure
{
//    NSString *method = @"user/realname/v1/getUserRealNameStatus";
    NSString *method = @"user/realname/v1/get";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
//    [params setObject:type forKey:@"type"];
    [params setObject:@(uid) forKey:@"uid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        DrawExchangeModel *drawExchangeModel = [DrawExchangeModel yy_modelWithJSON:data];
        success(drawExchangeModel);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}



//获取所有提现账户方法
+(void)requestGetFinancialAccount:(UserID)uid success:(void (^)(HJFinancialAccountDataModel *))success failure:(void (^)(NSNumber *, NSString *))failure
{
     NSString *method = @"withDraw/getFinancialAccount";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(uid) forKey:@"uid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        HJFinancialAccountDataModel *drawExchangeModel = [HJFinancialAccountDataModel yy_modelWithJSON:data];
        success(drawExchangeModel);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}




//提现请求

+(void)requestWithDrawCash:(UserID)uid pid:(long)pid type:(NSInteger)type account:(NSString*)account accountName:(NSString*)accountName smsCode:(NSString*)smsCode success:(void (^)(DrawExchangeModel *))success failure:(void (^)(NSNumber *, NSString *))failure
{
//    NSString *method = @"withDraw/withDrawCash";
     NSString *method = @"withDraw/bindWithdrawAccount";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
//    [params setObject:@(pid) forKey:@"pid"];
    NSString* diamondId = [NSString stringWithFormat:@"%ld",pid];
    [params setObject:diamondId forKey:@"diamondId"];
     [params setObject:account forKey:@"account"];
     [params setObject:accountName forKey:@"accountName"];
    [params setObject:@(type) forKey:@"accountType"];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:smsCode forKey:@"smsCode"];

          NSString *userId = [GetCore(HJAuthCoreHelp) getUid];
          UserInfo *info = [GetCore(HJUserCoreHelp) getUserInfoInDB:[userId intValue]];
          [params setObject:info.phone forKey:@"phone"];
       
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        DrawExchangeModel *drawExchangeModel = [DrawExchangeModel yy_modelWithJSON:data];
        success(drawExchangeModel);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//红包提现请求

+(void)requestRedPacketWithDrawCash:(UserID)uid pid:(long)pid type:(NSInteger)type success:(void (^)(redPacketcashModel *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"redpacket/withdraw";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(pid) forKey:@"packetId"];
    [params setObject:@(type) forKey:@"type"];
    [params setObject:@(uid) forKey:@"uid"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        redPacketcashModel *drawExchangeModel = [redPacketcashModel yy_modelWithJSON:data];
        success(drawExchangeModel);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)checkReceiptWithData:(NSData *)jsonData orderID:(NSString *)orderID trancid:(NSString *)trancid success:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure {
    
    NSString *receiptBase64 = [NSString base64StringFromData:jsonData length:[jsonData length]];
    
    NSString *method = @"verify/setiap";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:receiptBase64 forKey:@"receipt"];
#ifdef DEBUG
    [params setObject:@"false" forKey:@"chooseEnv"];
#else
    [params setObject:@"true" forKey:@"chooseEnv"];
#endif
    [params setObject:orderID forKey:@"chargeRecordId"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    params[@"trancid"] = trancid;
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}




//+ (void)requestChargeListWith:(NSNumber *)channel success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
//    NSString *method = @"chargeprod/list";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    [params setObject:channel forKey:@"channelType"];
//
//    [HJHttpRequestHelper GET:method params:params success:^(id data) {
//        NSArray *rechargeList = [NSArray yy_modelArrayWithClass:[RechargeInfo class] json:data];
//        success(rechargeList);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
//}
//
//+ (void)requestBalanceInfo:(UserID)uid success:(void (^)(BalanceInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
//{
//    NSString *method = @"purse/query";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
//    [params setObject:ticket forKey:@"ticket"];
//    [params setObject:@(uid) forKey:@"uid"];
//
//    [HJHttpRequestHelper GET:method params:params success:^(id data) {
//        BalanceInfo *balanceInfo = [BalanceInfo yy_modelWithJSON:data];
//        success(balanceInfo);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
//}
//
//+ (void)checkReceiptWithData:(NSData *)jsonData orderID:(NSString *)orderID trancid:(NSString *)trancid success:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure {
//
//    NSString *receiptBase64 = [NSString base64StringFromData:jsonData length:[jsonData length]];
//
//    NSString *method = @"verify/setiap";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
//    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
//    [params setObject:receiptBase64 forKey:@"receipt"];
//#ifdef DEBUG
//    [params setObject:@"false" forKey:@"chooseEnv"];
//#else
//    [params setObject:@"true" forKey:@"chooseEnv"];
//#endif
//    [params setObject:orderID forKey:@"chargeRecordId"];
//    [params setObject:uid forKey:@"uid"];
//    [params setObject:ticket forKey:@"ticket"];
//    params[@"trancid"] = trancid;
//
//    [HJHttpRequestHelper POST:method params:params success:^(id data) {
//        success(data);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
//}

//内购下单
+ (void)requestInAppRechargeWithChargeProdId:(NSString *)chargeProdId success:(void (^)(BOOL , NSString *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"order/place";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString* uid = [GetCore(HJAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:chargeProdId forKey:@"chargeProdId"];
    [params setObject:[YYUtility ipAddress] forKey:@"clientIp"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        NSString *orderid = (NSString *)data[@"recordId"];
        success(YES,orderid);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}











//获取手机验证码
+ (void)getMsmWithType:(NSInteger)type Success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"withDraw/getSms";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    [params setObject:uid forKey:@"uid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

//通过手机号获取验证码
+ (void)getCodeWithPhoneNum:(NSString *)phoneNum Success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"withDraw/phoneCode";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:phoneNum forKey:@"phone"];
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode,message);
    }];
}

//绑定手机
+ (void)bindingPhoneNumber:(NSString *)phoneNum verifyCode:(NSString *)verifyCode Success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"withDraw/phone";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:phoneNum forKey:@"phone"];
    [params setObject:verifyCode forKey:@"code"];
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode,message);
    }];

}

+ (void)queryFirstWithSuccess:(void (^)(BOOL first))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    NSString *method = @"/purse/queryFirst";
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = uid;
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        BOOL first = [data boolValue];
        success(first);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode,message);
    }];
    
}

+ (void)checkReceiptWithData:(NSData *)jsonData orderID:(NSString *)orderID tranID:(NSString *)tranID success:(void (^)(NSString *))success failure:(void (^)(NSNumber *, NSString *))failure {
    
    NSString *receiptBase64 = [NSString base64StringFromData:jsonData length:[jsonData length]];
    
    NSString *method = @"verify/setiap";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    [params setObject:receiptBase64 forKey:@"receipt"];
    
    if (GetCore(HJVersionCoreHelp).isReleaseEnv) {
        [params setObject:@"true" forKey:@"chooseEnv"];
    } else {
        [params setObject:@"false" forKey:@"chooseEnv"];
    }
    
    [params setObject:tranID forKey:@"trancid"];
    [params setObject:orderID forKey:@"chargeRecordId"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}













@end
