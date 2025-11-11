//
//  YPPurseBillCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPurseBillCore.h"
#import "YPHttpRequestHelper+Bill.h"
#import "HJPurseBillCoreClient.h"
#import "HJPurseCoreClient.h"
#import "YPPurseCore.h"

@implementation YPPurseBillCore


- (void)getOutGiftListPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize {
    [YPHttpRequestHelper getGiftOutBillWithPageNo:pageNo time:time pageSize:pageSize Success:^(NSMutableArray *arr,NSMutableArray *keys) {
        NotifyCoreClient(HJPurseBillCoreClient, @selector(getOutGiftListSuccess:keys:pageNo:), getOutGiftListSuccess:arr keys:keys pageNo:pageNo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseBillCoreClient, @selector(getOutGiftListFailth:), getOutGiftListFailth:message);
    }];
}

- (void)getInGiftListPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize {
    [YPHttpRequestHelper getGiftInBillWithPageNo:pageNo time:time pageSize:pageSize Success:^(NSMutableArray *arr,NSMutableArray *keys) {
        NotifyCoreClient(HJPurseBillCoreClient, @selector(getInGiftListSuccess:keys:pageNo:), getInGiftListSuccess:arr keys:keys pageNo:pageNo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseBillCoreClient, @selector(getOutGiftListFailth:), getInGiftListFailth:message);
    }];
}

/**
 密聊记录
 
 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 */
- (void)getChatListPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize  {
    [YPHttpRequestHelper getChatBillWithPageNo:pageNo time:time pageSize:pageSize Success:^(NSMutableArray *arr,NSMutableArray *keys) {
        NotifyCoreClient(HJPurseBillCoreClient, @selector(getChatListSuccess:keys:pageNo:), getChatListSuccess:arr keys:keys pageNo:pageNo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseBillCoreClient, @selector(getChatListFailth:), getChatListFailth:message);
    }];
}

/**
 充值记录
 
 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 */
- (void)getRechargeListPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize {
    [YPHttpRequestHelper getRechargeBillWithPageNo:pageNo time:time pageSize:pageSize Success:^(NSMutableArray *arr,NSMutableArray *keys) {
        
        NotifyCoreClient(HJPurseBillCoreClient, @selector(getRechargeListSuccess:keys:pageNo:), getRechargeListSuccess:arr keys:keys pageNo:pageNo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseBillCoreClient, @selector(getRechargeListFailth:), getRechargeListFailth:message);
    }];
}


/**
 提现记录
 
 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 */
- (void)getWithdrawListPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize {
    [YPHttpRequestHelper getWithdrawBillWithPageNo:pageNo time:time pageSize:pageSize Success:^(NSMutableArray *arr,NSMutableArray *keys) {
        
        NotifyCoreClient(HJPurseBillCoreClient, @selector(getWithdrawlListSuccess:keys:pageNo:), getWithdrawlListSuccess:arr keys:keys pageNo:pageNo);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJPurseBillCoreClient, @selector(getWithdrawlListFailth:), getWithdrawlListFailth:message);
    }];
}









@end
