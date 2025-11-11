//
//  HJHttpRequestHelper+Gift.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Gift.h"
#import "GiftInfo.h"
#import "NSObject+YYModel.h"
#import "HJAuthCoreHelp.h"
#import "HJImRoomCoreV2.h"
#import "BalanceInfo.h"
#import "HJGiftAllMicroSendInfo.h"
#import "HJImRoomCoreV2.h"

@implementation HJHttpRequestHelper (Gift)

+ (void)sendAllMicroGiftByUids:(NSString *)uids giftId:(NSInteger)giftId giftNum:(NSInteger)giftNum roomUid:(NSInteger)roomUid success:(void (^)(HJGiftAllMicroSendInfo *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"gift/sendWholeMicroV3";
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    NSString *ticket = [GetCore(HJAuthCoreHelp)getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uids forKey:@"targetUids"];
    [params setObject:@(giftId) forKey:@"giftId"];
    [params setObject:@(giftNum) forKey:@"giftNum"];
    [params setObject:@(roomUid) forKey:@"roomUid"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithJSON:data];
        success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
    
}
//MARK:- 人气票
+ (void)sendPointWholeMicro:(NSString *)uids giftId:(NSInteger)giftId giftNum:(NSInteger)giftNum roomUid:(NSInteger)roomUid success:(void (^)(HJGiftAllMicroSendInfo *))success failure:(void (^)(NSNumber *, NSString *))failure {
//    NSString *method = @"gift/sendPointWholeMicro";
    NSString *method = @"gift/sendPropWholeMicro";
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    NSString *ticket = [GetCore(HJAuthCoreHelp)getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:uids forKey:@"targetUids"];
    [params setObject:@(giftId) forKey:@"giftId"];
    [params setObject:@(giftNum) forKey:@"giftNum"];
    [params setObject:@(roomUid) forKey:@"roomUid"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithJSON:data];
        success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
    
}

+ (void)requestGiftList:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"gift/listV3";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(HJAuthCoreHelp)getTicket];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    
    if (ticket== nil) {
        return;
    }
    
    if (ticket.length==0) {
        return;
    }

    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *listData = data[@"gift"];
        NSArray *giftInfos = [NSArray yy_modelArrayWithClass:[GiftInfo class] json:listData];
        success(giftInfos);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)giftListMysticSuccess:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    
    if (![GetCore(HJAuthCoreHelp) getUid].length || ![GetCore(HJAuthCoreHelp) getTicket].length) {
        return;
    }
    
    NSString *method = @"gift/listMystic";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    [params setObject:uid forKey:@"uid"];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *giftInfos = [NSArray yy_modelArrayWithClass:[GiftInfo class] json:data];
        success(giftInfos);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)giftListDiandianCoinSuccess:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    
    if (![GetCore(HJAuthCoreHelp) getUid].length || ![GetCore(HJAuthCoreHelp) getTicket].length) {
        return;
    }
    
    NSString *method = @"gift/listPoint";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    [params setObject:uid forKey:@"uid"];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *giftInfos = [NSArray yy_modelArrayWithClass:[GiftInfo class] json:data];
        success(giftInfos);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


+(void)sendGift:(NSInteger)giftId targetUid:(UserID)targetUid type:(NSInteger)type success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *method = @"gift/send";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(giftId) forKey:@"giftId"];
    [params setObject:@(targetUid) forKey:@"targetUid"];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:@(type) forKey:@"type"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)sendGift:(NSInteger)giftId targetUid:(UserID)targetUid giftNum:(NSInteger)giftNum type:(NSInteger)type success:(void (^)(HJGiftAllMicroSendInfo *info))success failure:(void (^)(NSNumber *, NSString *))failure {
    UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *method = @"gift/sendV3";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(giftId) forKey:@"giftId"];
    [params setObject:@(targetUid) forKey:@"targetUid"];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:@(type) forKey:@"type"];
    [params setObject:ticket forKey:@"ticket"];
    
    if (giftNum == 0) {
        [params setObject:@(1) forKey:@"giftNum"];
    }else {
        [params setObject:@(giftNum) forKey:@"giftNum"];
    }
    
    NSInteger roomUid = GetCore(HJImRoomCoreV2).currentRoomInfo.uid;

    [params setObject:@(roomUid) forKey:@"roomUid"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithJSON:data];
        success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)sendPoint:(NSInteger)giftId targetUid:(UserID)targetUid giftNum:(NSInteger)giftNum type:(NSInteger)type success:(void (^)(HJGiftAllMicroSendInfo *info))success failure:(void (^)(NSNumber *, NSString *))failure {
    UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
//    NSString *method = @"gift/sendPoint";
    NSString *method = @"gift/sendProp";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(giftId) forKey:@"giftId"];
    [params setObject:@(targetUid) forKey:@"targetUid"];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:@(type) forKey:@"type"];
    [params setObject:ticket forKey:@"ticket"];
    
    if (giftNum == 0) {
        [params setObject:@(1) forKey:@"giftNum"];
    }else {
        [params setObject:@(giftNum) forKey:@"giftNum"];
    }
    
    NSInteger roomUid = GetCore(HJImRoomCoreV2).currentRoomInfo.uid;
    
    [params setObject:@(roomUid) forKey:@"roomUid"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithJSON:data];
        success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


@end
