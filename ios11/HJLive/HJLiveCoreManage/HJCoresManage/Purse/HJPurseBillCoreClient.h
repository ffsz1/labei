//
//  HJPurseBillCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HJRedPageDetailInfo.h"
#import "BalanceInfo.h"

@protocol HJPurseBillCoreClient <NSObject>
@optional
- (void)getOutGiftListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo;
- (void)getOutGiftListFailth:(NSString *)message;

- (void)getInGiftListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo;
- (void)getInGiftListFailth:(NSString *)message;

- (void)getChatListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo;
- (void)getChatListFailth:(NSString *)message;

- (void)getRechargeListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo;
- (void)getRechargeListFailth:(NSString *)message;

- (void)getWithdrawlListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo;
- (void)getWithdrawlListFailth:(NSString *)message;


- (void)getRedWithdrawlListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo;

- (void)getRedWithdrawlListFailth:(NSString *)message;


- (void)getRedGiftListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo;
- (void)getRedGiftListFailth:(NSString *)message;


- (void)requestCodeExchangeSuccess:(BalanceInfo *)balanceInfo;
- (void)requestCodeExchangeFailth:(NSString *)message;


@end
