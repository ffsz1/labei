//
//  PurseBillCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import "HJRedPageDetailInfo.h"
@interface PurseBillCore : BaseCore

@property (nonatomic, strong) HJRedPageDetailInfo *redPageDetailInfo;

/**
 礼物支出

 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 */
- (void)getOutGiftListPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize;


/**
 礼物收入

 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 */
- (void)getInGiftListPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize;


/**
 密聊记录

 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 */
- (void)getChatListPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize;

/**
 充值记录
 
 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 */
- (void)getRechargeListPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize;


/**
 提现记录
 
 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 */
- (void)getWithdrawListPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize;










@end
