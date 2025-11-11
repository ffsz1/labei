//
//  YPBalanceInfoStorage.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPBalanceInfo.h"
@interface YPBalanceInfoStorage : NSObject
+(YPBalanceInfo *)getCurrentBalanceInfo;
+(void)saveBalanceInfo:(YPBalanceInfo *)balanceInfo;
//获取提现页用户信息
+(NSString *) getFilePathWithDrawExchange;
+ (DrawExchangeModel *)getCurrentDrawExchangeInfo;
+ (void)saveDrawExchangeModel:(DrawExchangeModel *)balanceInfo;

@end
