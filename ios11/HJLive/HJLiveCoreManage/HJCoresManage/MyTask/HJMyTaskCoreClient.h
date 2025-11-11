//
//  HJMyTaskCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
@protocol HJMyTaskCoreClient <NSObject>

// 任务列表
- (void)getDutyListSuccessWithList:(id)tasks;
- (void)getDutyListFailedWithMsg:(NSString *)msg;

// 领取开心
- (void)dutyAchieveSuccessWithDutyId:(NSInteger)dutyId;
- (void)dutyAchieveFailedWithDutyId:(NSInteger)dutyId msg:(NSString *)msg;

- (void)dutyFreshPublicSuccess;
- (void)dutyDailyShareSuccess;

@end
