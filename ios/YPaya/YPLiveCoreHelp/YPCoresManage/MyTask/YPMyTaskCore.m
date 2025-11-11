//
//  YPMyTaskCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMyTaskCore.h"

#import "HJMyTaskCoreClient.h"
#import "YPHttpRequestHelper+MyTask.h"

@implementation YPMyTaskCore

/**
 我的任务列表
 */
- (void)getDutyList {
    
    [YPHttpRequestHelper getDutyListWithSuccess:^(id list) {
        NotifyCoreClient(HJMyTaskCoreClient, @selector(getDutyListSuccessWithList:), getDutyListSuccessWithList:list);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJMyTaskCoreClient, @selector(getDutyListFailedWithMsg:), getDutyListFailedWithMsg:msg);
    }];
}

/**
 领取奖励
 
 @param dutyId 任务id
 */
- (void)dutyAchieveWithDutyId:(NSInteger)dutyId {
    [YPHttpRequestHelper dutyAchieveWithDutyId:dutyId success:^{
        NotifyCoreClient(HJMyTaskCoreClient, @selector(dutyAchieveSuccessWithDutyId:), dutyAchieveSuccessWithDutyId:dutyId);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJMyTaskCoreClient, @selector(dutyAchieveFailedWithDutyId:msg:), dutyAchieveFailedWithDutyId:dutyId msg:msg);
    }];
}

/**
 去大厅发言，发言上传接口
 */
- (void)dutyFreshPublic {
    [YPHttpRequestHelper dutyFreshPublicWithSuccess:^{
        NotifyCoreClient(HJMyTaskCoreClient, @selector(dutyFreshPublicSuccess), dutyFreshPublicSuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        
    }];
}


/**
 微信QQ分享上传
 */
- (void)dutyDailyShare {
    [YPHttpRequestHelper dutyDailyShareWithSuccess:^{
        NotifyCoreClient(HJMyTaskCoreClient, @selector(dutyDailyShareSuccess), dutyDailyShareSuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        
    }];
}

@end
