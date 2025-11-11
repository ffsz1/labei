//
//  HJHeadwear.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHeadwear.h"
#import "HJHeadwearClient.h"
#import "HJHttpRequestHelper+Headwear.h"

@implementation HJHeadwear

- (void)sendHeadwearWithHeadwearId:(NSString *)headwearId
                         targetUid:(NSString *)targetUid {
    [HJHttpRequestHelper sendHeadwearWithHeadwearId:headwearId targetUid:targetUid Success:^(id json) {
        NotifyCoreClient(HJHeadwearClient, @selector(sendHeadwearSuccess), sendHeadwearSuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        if ([code intValue] != 2103) {
            NotifyCoreClient(HJHeadwearClient, @selector(sendHeadwearFail:), sendHeadwearFail:msg);
        }
    }];
}

- (void)giftHeadwearUse:(NSString *)HeadwearID {
    [HJHttpRequestHelper getHeadwearUseWithHeadwearId:HeadwearID Success:^(id json) {
        NotifyCoreClient(HJHeadwearClient, @selector(userHeadwearSuccess), userHeadwearSuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJHeadwearClient, @selector(userHeadwearFail:), userHeadwearFail:msg);
    }];
}

- (void)getHeadwearListWithPageNum:(NSString *)pageNum PageSize:(NSString *)pageSize {
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid]; 
    [HJHttpRequestHelper getHeadwearListWithPageNum:pageNum withPageSize:pageSize userId:uid success:^(NSArray *list) {
        NotifyCoreClient(HJHeadwearClient, @selector(getHeadwearListSuccessWithArr:), getHeadwearListSuccessWithArr:list);
    } failure:^(NSNumber *regCode, NSString *msg) {
        NotifyCoreClient(HJHeadwearClient, @selector(getHeadwearListFailWithMessage:), getHeadwearListFailWithMessage:msg);
    }];
}

- (void)buyHeadwearWithHeadwearID:(NSString *)HeadwearID withType:(NSString *)type {
    [HJHttpRequestHelper getHeadwearPurseWithType:type CarId:HeadwearID Success:^(id data) {
        if ([type intValue] == 1) {
            NotifyCoreClient(HJHeadwearClient, @selector(buyHeadwearSuccess:), buyHeadwearSuccess:data);
        } else if ([type intValue] == 2) {
            NotifyCoreClient(HJHeadwearClient, @selector(continueHeadwearSuccess:), continueHeadwearSuccess:data);
        }
    } failure:^(NSNumber *code, NSString *msg) {
        if ([type intValue] == 1) {
            NotifyCoreClient(HJHeadwearClient, @selector(buyHeadwearFailWithCode:Message:), buyHeadwearFailWithCode:code Message:msg);
        } else if ([type intValue] == 2) {
            NotifyCoreClient(HJHeadwearClient, @selector(continueHeadwearFailWithCode:WithMessage:), continueHeadwearFailWithCode:code WithMessage:msg);
        }
    }];
}

@end
