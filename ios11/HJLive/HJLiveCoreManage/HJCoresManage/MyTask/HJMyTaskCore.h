//
//  HJMyTaskCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"

@interface HJMyTaskCore : BaseCore

/**
 我的任务列表
 */
- (void)getDutyList;

/**
 领取奖励
 
 @param dutyId 任务id
 */
- (void)dutyAchieveWithDutyId:(NSInteger)dutyId;

/**
 去大厅发言，发言上传接口
 */
- (void)dutyFreshPublic;

/**
 微信QQ分享上传
 */
- (void)dutyDailyShare;

@end
