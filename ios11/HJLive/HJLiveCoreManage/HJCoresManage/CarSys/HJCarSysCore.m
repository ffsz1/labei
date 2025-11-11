//
//  CarSysCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJCarSysCore.h"
#import "HJHttpRequestHelper+CarSys.h"
#import "HJCarSysCoreClient.h"

@implementation HJCarSysCore

- (void)sendCarWithCarId:(NSString *)carId
               targetUid:(NSString *)targetUid {
    [HJHttpRequestHelper sendCarWithCarId:carId targetUid:targetUid Success:^(id json) {
        NotifyCoreClient(HJCarSysCoreClient, @selector(sendCarSysSuccess), sendCarSysSuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        if ([code intValue] != 2103) {
            NotifyCoreClient(HJCarSysCoreClient, @selector(sendCarSysFail:), sendCarSysFail:msg);
        }
    }];
}


- (void)giftCarUse:(NSString *)carId {
    [HJHttpRequestHelper getCarSysUseWithCarId:carId Success:^(id json) {
        NotifyCoreClient(HJCarSysCoreClient, @selector(userCarSysSuccess), userCarSysSuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJCarSysCoreClient, @selector(userCarSysFail:), userCarSysFail:msg);
    }];
}

- (void)getCarSysListWithPageNum:(NSString *)pageNum PageSize:(NSString *)pageSize {
    
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    [HJHttpRequestHelper getCarSysListWithPageNum:pageNum withPageSize:pageSize userId:uid success:^(NSArray *list) {
        NotifyCoreClient(HJCarSysCoreClient, @selector(getCarSysListSuccessWithArr:), getCarSysListSuccessWithArr:list);
    } failure:^(NSNumber *regCode, NSString *msg) {
        NotifyCoreClient(HJCarSysCoreClient, @selector(getCarSysListFailWithMessage:), getCarSysListFailWithMessage:msg);
    }];
}

- (void)buyCarSysWithCarId:(NSString *)carId withType:(NSString *)type {
    [HJHttpRequestHelper getCarSysPurseWithType:type CarId:carId Success:^(id data) {
        if ([type intValue] == 1) {
            NotifyCoreClient(HJCarSysCoreClient, @selector(buyCarSuccess:), buyCarSuccess:data);
        } else if ([type intValue] == 2) {
            NotifyCoreClient(HJCarSysCoreClient, @selector(continueCarSuccess:), continueCarSuccess:data);
        }
    } failure:^(NSNumber *code, NSString *msg) {
        if ([type intValue] == 1) {
            NotifyCoreClient(HJCarSysCoreClient, @selector(buyFailWithCode:Message:), buyFailWithCode:code Message:msg);
        } else if ([type intValue] == 2) {
            NotifyCoreClient(HJCarSysCoreClient, @selector(continueFailWithCode:WithMessage:), continueFailWithCode:code WithMessage:msg);
        }
    }];
}

- (void)getUserCarSysList:(UserID)userId
                  PageNum:(NSString *)pageNum
                 PageSize:(NSString *)pageSize {
    if (userId > 0) {
        [HJHttpRequestHelper getUserCarList:userId PageNum:pageNum PageSize:pageSize success:^(NSArray *list) {
            NotifyCoreClient(HJCarSysCoreClient, @selector(onGetReceiveCarSuccess:uid:), onGetReceiveCarSuccess:list uid:userId);
        } failure:^(NSNumber *resCode, NSString *message) {
            NotifyCoreClient(HJCarSysCoreClient, @selector(onGetReceiveCarFailth:), onGetReceiveCarFailth:message);
        }];
    }
}

- (void)getUserHeadList:(UserID)userId
                PageNum:(NSString *)pageNum
               PageSize:(NSString *)pageSize {
    if (userId > 0) {
        [HJHttpRequestHelper getUserHeadList:userId PageNum:pageNum PageSize:pageSize success:^(NSArray *list) {
            NotifyCoreClient(HJCarSysCoreClient, @selector(onGetReceiveHeadSuccess:uid:), onGetReceiveHeadSuccess:list uid:userId);
        } failure:^(NSNumber *resCode, NSString *message) {
            NotifyCoreClient(HJCarSysCoreClient, @selector(onGetReceiveHeadFailth:), onGetReceiveHeadFailth:message);
        }];
    }
}


@end
