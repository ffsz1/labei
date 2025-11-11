//
//  HJHttpRequestHelper+Room.m
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Room.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "ChatRoomInfo.h"
#import "HJImRoomCoreV2.h"
#import "HJRewardInfo.h"
#import "MicroListInfo.h"
#import "HJMicroUserListInfo.h"
#import "NSObject+YYModel.h"
#import "HJRoomBounsListInfo.h"
#import "HJGiftPurseRank.h"
#import "HJGiftPurseRecord.h"
#import "HJRoomBgModel.h"
#import "HJDaCallIntoModel.h"
#import "HJDaCallModel.h"
@implementation HJHttpRequestHelper (Room)

#pragma mark - 更新房间信息



//管理更新房间信息
+ (void)managerUpdateRoomInfo:(UserID)uid title:(NSString *)title roomDesc:(NSString *)roomDesc roomNotice:(NSString *)roomNotice backPic:(NSString *)backPic roomPassword:(NSString *)roomPassword playInfo:(NSString *)playInfo tag:(int)tag giftEffectSwitch:(BOOL)giftEffectSwitch giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch success:(void (^)(ChatRoomInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"room/updateByAdmin";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    UserID roomUid = GetCore(HJImRoomCoreV2).currentRoomInfo.uid;
    [params setObject:@(roomUid) forKey:@"roomUid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(uid) forKey:@"uid"];
    
    if (title) {
        if (title.length > 0) {
            [params setObject:title forKey:@"title"];
        } else {
            [params setObject:@"" forKey:@"title"];
        }
    }
    
    if (roomDesc) {
        if (roomDesc.length > 0) {
            [params setObject:roomDesc forKey:@"roomDesc"];
        } else {
            [params setObject:@"" forKey:@"roomDesc"];
        }
    }
    
    if (backPic) {
        if (backPic.length > 0) {
            [params setObject:backPic forKey:@"backPic"];
        } else {
            [params setObject:@"" forKey:@"backPic"];
        }
    }
    
    if (roomPassword) {
        if (roomPassword.length > 0) {
            [params setObject:roomPassword forKey:@"roomPwd"];
        } else {
            [params setObject:@"" forKey:@"roomPwd"];
        }
    }
    
    
    if (tag > 0) {
        [params setObject:@(tag) forKey:@"tagId"];
    }
    
    if (roomNotice) {
        if (roomNotice.length > 0) {
            [params setObject:roomNotice forKey:@"roomNotice"];
        } else {
            [params setObject:@"" forKey:@"roomNotice"];
        }
    }
    
    if (playInfo) {
        if (playInfo.length > 0) {
            [params setObject:playInfo forKey:@"playInfo"];
        } else {
            [params setObject:@"" forKey:@"playInfo"];
        }
    }
    
    if (!giftEffectSwitch) {
        params[@"giftEffectSwitch"] = @1;
    }
    else {
        params[@"giftEffectSwitch"] = @0;
    }
    
    if (publicChatSwitch) {
        params[@"publicChatSwitch"] = @1;
    }
    else {
        params[@"publicChatSwitch"] = @0;
    }
    
    if (!giftCardSwitch) {
        params[@"giftCardSwitch"] = @1;
    }
    else {
        params[@"giftCardSwitch"] = @0;
    }
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        ChatRoomInfo *roomInfo = [ChatRoomInfo yy_modelWithDictionary:data];
        if (roomInfo != nil) {
            success(roomInfo);
        } else {
            failure(@(10), @"ticket为空");
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//更新房间信息
+ (void)updateRoomInfo:(UserID)uid title:(NSString *)title roomDesc:(NSString *)roomDesc roomNotice:(NSString *)roomNotice backPic:(NSString *)backPic roomPassword:(NSString *)roomPassword tag:(int)tag playInfo:(NSString *)playInfo giftEffectSwitch:(BOOL)giftEffectSwitch giftCardSwitch:(BOOL)giftCardSwitch publicChatSwitch:(BOOL)publicChatSwitch success:(void (^)(ChatRoomInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"room/update";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    if (title) {
        if (title.length > 0) {
            [params setObject:title forKey:@"title"];
        } else {
            [params setObject:@"" forKey:@"title"];
        }
    }
    
    if (roomDesc) {
        if (roomDesc.length > 0) {
            [params setObject:roomDesc forKey:@"roomDesc"];
        } else {
            [params setObject:@"" forKey:@"roomDesc"];
        }
    }
    
    if (backPic) {
        if (backPic.length > 0) {
            [params setObject:backPic forKey:@"backPic"];
        } else {
            [params setObject:@"" forKey:@"backPic"];
        }
    }
    
    if (roomPassword) {
        if (roomPassword.length > 0) {
            [params setObject:roomPassword forKey:@"roomPwd"];
        } else {
            [params setObject:@"" forKey:@"roomPwd"];
        }
    }
    
    
    if (tag > 0) {
        [params setObject:@(tag) forKey:@"tagId"];
    }
    
    if (roomNotice) {
        if (roomNotice.length > 0) {
            [params setObject:roomNotice forKey:@"roomNotice"];
        } else {
            [params setObject:@"" forKey:@"roomNotice"];
        }
    }
    
    if (playInfo) {
        if (playInfo.length > 0) {
            [params setObject:playInfo forKey:@"playInfo"];
        } else {
            [params setObject:@"" forKey:@"playInfo"];
        }
    }
    
    if (!giftEffectSwitch) {
        params[@"giftEffectSwitch"] = @1;
    }
    else {
        params[@"giftEffectSwitch"] = @0;
    }
    
    if (publicChatSwitch) {
        params[@"publicChatSwitch"] = @1;
    }
    else {
        params[@"publicChatSwitch"] = @0;
    }
    
    if (!giftCardSwitch) {
        params[@"giftCardSwitch"] = @1;
    }
    else {
        params[@"giftCardSwitch"] = @0;
    }
    
    
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        ChatRoomInfo *roomInfo = [ChatRoomInfo yy_modelWithDictionary:data];
        if (roomInfo != nil) {
            success(roomInfo);
        } else {
            failure(@(10), @"ticket为空");
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}



#pragma mark - 统计相关
/**
 用户退出房间上报
 
 @param success 成功
 @param failure 失败
 */
+ (void)reportUserOutRoomSuccess:(void (^)(BOOL success))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"userroom/out";
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    NSString *ticket = [GetCore(HJAuthCoreHelp)getTicket];
    UserID roomUid = GetCore(HJImRoomCoreV2).currentRoomInfo.uid;
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(roomUid) forKey:@"roomUid"];
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

//房间统计埋点
+ (void)recordTheRoomTime:(UserID)uid roomUid:(UserID)roomUid {
    NSString *method = @"basicusers/v2/record";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    if (uid > 0) {
        [params setObject:@(uid) forKey:@"uid"];
    }
    if (roomUid > 0) {
        [params setObject:@(roomUid) forKey:@"roomUid"];
    }else{
        return;
    }
    
    if (ticket.length > 0) {
        [params setObject:ticket forKey:@"ticket"];
    }
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init] ;
    
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    
    [formatter setDateFormat:@"YYYY-MM-dd HH:mm:ss SSS"]; //
    NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    
    [formatter setTimeZone:timeZone];
    
    NSDate *datenow = [NSDate date];//现在时间,你可以输出来看下是什么格式
    
    NSString *timeSp = [NSString stringWithFormat:@"%ld", (long)[datenow timeIntervalSince1970]*1000];
    
    [params setObject:timeSp forKey:@"time"];
    
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

/**
 用户进入房间上报
 
 @param success 成功
 @param failure 失败
 */
+ (void)reportUserInterRoomSuccess:(void (^)(BOOL success))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"userroom/in";
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    NSString *ticket = [GetCore(HJAuthCoreHelp)getTicket];
    UserID roomUid = GetCore(HJImRoomCoreV2).currentRoomInfo.uid;
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(roomUid) forKey:@"roomUid"];
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
    
}



#pragma mark - 开房&关房
//开竞拍房
+ (void) rewardForRoom:(UserID)uid servDura:(NSInteger)servDura rewardMonye:(NSInteger)rewardMonye success:(void (^)(HJRewardInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"reward/save";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:@(servDura) forKey:@"servDura"];
    [params setObject:@(rewardMonye) forKey:@"rewardMonye"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        HJRewardInfo *rewardInfo = [HJRewardInfo yy_modelWithDictionary:data];
        if (rewardInfo != nil) {
            success(rewardInfo);
        } else {
            failure(@(10), @"ticket为空");
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//开房
+ (void)openRoom:(UserID)uid type:(RoomType)type title:(NSString *)title roomDesc:(NSString *)roomDesc backPic:(NSString *)backPic rewardId:(NSString *)rewardId success:(void (^)(ChatRoomInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"room/open";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:@(type) forKey:@"type"];
    if (title.length <= 0) {
        NSString *nick = [GetCore(HJUserCoreHelp) getUserInfoInDB:uid].nick;
        title = [NSString stringWithFormat:@"%@的房间", nick];
    }
    [params setObject:title forKey:@"title"];
    
    if (type != RoomType_Game) {
        [params setObject:@"" forKey:@"roomPwd"];
    }
    
    if (roomDesc.length > 0) {
        [params setObject:roomDesc forKey:@"roomDesc"];
    }else {
        [params setObject:@"" forKey:@"roomDesc"];
    }
    
    if (backPic.length > 0) {
        [params setObject:backPic forKey:@"backPic"];
    }else {
        [params setObject:@"" forKey:@"backPic"];
    }
    
    if (rewardId.length > 0) {
        [params setObject:rewardId forKey:@"rewardId"];
    }
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        ChatRoomInfo *roomInfo = [ChatRoomInfo yy_modelWithDictionary:data];
        if (roomInfo != nil) {
            success(roomInfo);
        } else {
            failure(@(10), @"ticket为空");
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


+ (void)closeRoom:(UserID)uid success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"room/close";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

#pragma mark - 获取房间信息相关
//获取房间信息
+ (void)getRoomInfo:(UserID)uid success:(void (^)(ChatRoomInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"room/get";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(uid) forKey:@"uid"];
    params[@"visitorUid"] = GetCore(HJAuthCoreHelp).getUid;
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket ];
    [params setObject:ticket forKey:@"ticket"];

    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        ChatRoomInfo *roomInfo = [ChatRoomInfo yy_modelWithDictionary:data];
        if (roomInfo != nil && roomInfo.uid > 0 && roomInfo.title != nil) {
            success(roomInfo);
        }else if (roomInfo == nil || roomInfo.title == nil || roomInfo.title.length == 0) {
            success(nil);
        }else {
            failure(@(10), @"ticket为空");
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//获取房间信息
+ (void)getRoomInfoByUids:(NSArray *)uids success:(void (^)(NSArray<ChatRoomInfo *> *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"/room/list";
    NSString *uidsStr = [[NSString alloc]init];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    uidsStr = [uids componentsJoinedByString:@","];
    [params setObject:uidsStr forKey:@"uids"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSMutableArray *tempArr = [[NSMutableArray alloc]init];
        for (NSDictionary *item in data) {
            ChatRoomInfo *roominfo = [ChatRoomInfo yy_modelWithDictionary:item];
            [tempArr addObject:roominfo];
        }
        success(tempArr);
        
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode,message);
    }];
}
/**
 获取用户所在房间信息
 
 @param uid uid
 @param success 成功
 @param failure 失败
 */
+ (void)requestUserInRoomInfoBy:(UserID)uid Success:(void (^)(ChatRoomInfo *roomInfo))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"userroom/get";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(uid) forKey:@"uid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        ChatRoomInfo *info = [ChatRoomInfo yy_modelWithJSON:data];
        success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

//贡献榜
+ (void)requestRoomBounsListSuccess:(void (^)(NSMutableArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"roomctrb/query";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *roomUid = [NSString stringWithFormat:@"%lld",GetCore(HJImRoomCoreV2).currentRoomInfo.uid];
    [params setObject:roomUid forKey:@"uid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *arr = [NSArray yy_modelArrayWithClass:[HJRoomBounsListInfo class] json:data];
        success([arr mutableCopy]);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)requestNewRoomBounsListWithType:(NSString *)type
                           withDataType:(NSString *)dataType
                                Success:(void (^)(NSMutableArray *))success
                                failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"roomctrb/queryByType";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *roomUid = [NSString stringWithFormat:@"%lld",GetCore(HJImRoomCoreV2).currentRoomInfo.uid];
    [params setObject:roomUid forKey:@"uid"];
    [params setObject:dataType forKey:@"dataType"];
    [params setObject:type forKey:@"type"];

    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *arr = [NSArray yy_modelArrayWithClass:[HJRoomBounsListInfo class] json:data];
        success([arr mutableCopy]);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getRoomShenHaoTop:(NSString *)roomUid
                  Success:(void (^)(NSMutableArray *))success
                  failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"roomctrb/queryByType";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:roomUid forKey:@"uid"];
    [params setObject:@"1" forKey:@"dataType"];
    [params setObject:@"1" forKey:@"type"];
    
    
    
    
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *arr = [NSArray yy_modelArrayWithClass:[HJRoomBounsListInfo class] json:data];
        success([arr mutableCopy]);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}
//MARK: - 打call /gift/callForUser  给单个用户打call
+ (void)getRoomCallForUser:(HJDaCallIntoModel *)model
                  Success:(void (^)(HJDaCallModel *))success
                  failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"gift/callForUser";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];

     [params setObject:[NSNumber numberWithInteger:model.giftId]  forKey:@"giftId"];
     [params setObject:[NSNumber numberWithInteger:model.giftNum]  forKey:@"giftNum"];
     [params setObject:model.targetUid forKey:@"targetUid"];
      NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
     [params setObject:ticket forKey:@"ticket"];
     [params setObject: [NSNumber numberWithInteger:model.roomUid] forKey:@"roomUid"];
    [params setObject: [NSNumber numberWithInteger:model.uid] forKey:@"uid"];
    [params setObject:[NSNumber numberWithInteger:model.type] forKey:@"type"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        HJDaCallModel* model = [HJDaCallModel yy_modelWithDictionary:data];
        success(model);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}






#pragma mark - 麦序操作
//上麦
+ (void)upMicro:(UserID)uid roomId:(UserID)roomId position:(NSInteger)position success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"room/mic/upmic";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(uid) forKey:@"micUid"];
    [params setObject:@(roomId) forKey:@"roomId"];
    [params setObject:@(position) forKey:@"position"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

//下麦/用户自行离开麦序
+ (void)leftMicro:(UserID)uid roomId:(UserID)roomId position:(NSInteger)position
          success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure {
    
    NSString *method = @"room/mic/downmic";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(uid) forKey:@"micUid"];
    [params setObject:@(roomId) forKey:@"roomId"];
    [params setObject:@(position) forKey:@"position"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

/**
 锁坑位/取消操作
 */
+ (void)micPlace:(NSInteger)position roomOwnerUid:(UserID)roomOwnerUid state:(NSInteger)state
         success:(void (^)(void))success
         failure:(void (^)(NSNumber *resCode, NSString *message))failure{
    NSString *method = @"room/mic/lockpos";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(roomOwnerUid) forKey:@"roomUid"];
    [params setObject:@(state) forKey:@"state"];
    [params setObject:@(position) forKey:@"position"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:[GetCore(HJAuthCoreHelp) getUid] forKey:@"uid"];

    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
/**
 锁麦/开麦操作
 */
+ (void)micState:(NSInteger)position roomOwnerUid:(UserID)roomOwnerUid state:(NSInteger)state
         success:(void (^)(void))success
         failure:(void (^)(NSNumber *resCode, NSString *message))failure{
    NSString *method = @"room/mic/lockmic";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(roomOwnerUid) forKey:@"roomUid"];
    [params setObject:@(state) forKey:@"state"];
    [params setObject:@(position) forKey:@"position"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:[GetCore(HJAuthCoreHelp) getUid] forKey:@"uid"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

//房主邀请上麦
+ (void)inviteUpMicroWithUid:(UserID)uid position:(int)position roomId:(int)roomId success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"room/mic/invitemic";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *ownerUid = [GetCore(HJAuthCoreHelp) getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ownerUid forKey:@"roomUid"];
    [params setObject:@(uid) forKey:@"micUid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(position) forKey:@"position"];
    [params setObject:@(roomId) forKey:@"roomId"];
    
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
    
}

//房主踢用户下麦
+ (void)ownerKickUserByUid:(NSString *)uid position:(int)position roomId:(int)roomId success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"room/mic/kickmic";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *ownerUid = [GetCore(HJAuthCoreHelp) getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ownerUid forKey:@"roomUid"];
    [params setObject:uid forKey:@"micUid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(position) forKey:@"position"];
    [params setObject:@(roomId) forKey:@"roomId"];
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}


#pragma mark - 暂时未使用

//申请上麦

+(void)applyMicro:(UserID)uid roomOwnerUid:(UserID)roomOwnerUid success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"micro/apply";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:@(roomOwnerUid) forKey:@"roomUid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//请求麦序
+ (void)requestMicroList:(UserID)roomOwnerUid success:(void (^)(MicroListInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"micro/list";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(roomOwnerUid) forKey:@"uid"];
    [params setObject:@(3) forKey:@"type"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        MicroListInfo *microListInfo = [MicroListInfo yy_modelWithJSON:data];
        success(microListInfo);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//拒绝申请
+ (void)denyApplyMicro:(UserID)uid roomOwnerUid:(UserID)roomOwnerUid success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"micro/denyapply";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(roomOwnerUid) forKey:@"uid"];
    [params setObject:@(uid) forKey:@"applyUid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//更新麦序
+(void)updateMicroList:(UserID)roomOwnerUid curUids:(NSArray *)curUids type:(NSInteger)type
                success:(void (^)(void))success
                failure:(void (^)(NSNumber *resCode, NSString *message))failure;
{
    NSString *method = @"micro/update";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    
    NSMutableString *strUidList = [[NSMutableString alloc] init];
    for (int i=0; i<curUids.count; i++) {
        NSNumber *uid = curUids[i];
        if (uid.userIDValue > 0) {
            [strUidList appendString:[NSString stringWithFormat:@"%lld",uid.userIDValue]];
            [strUidList appendString:@","];
        }
    }
    [strUidList deleteCharactersInRange:NSMakeRange([strUidList length]-1, 1)];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(roomOwnerUid) forKey:@"uid"];
    [params setObject:strUidList forKey:@"curUids"];
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

//用户同意上麦
+ (void)userAgreeUpMicroRoomUid:(NSString *)roomUid success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"microV2/accept";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:roomUid forKey:@"roomUid"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);

    }];

}

//获取麦序列表
+ (void)fetchMicroListInfoByOwnerUid:(NSString *)ownerUid success:(void (^)(NSMutableArray *userList))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"microV2/list";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ownerUid forKey:@"uid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSMutableArray *userList = [NSMutableArray array];
//        [NSArray yy_modelArrayWithClass:[UserInfo class] json:data];
        [userList addObjectsFromArray:[NSArray yy_modelArrayWithClass:[HJMicroUserListInfo class] json:data]];
        NSMutableArray *finallyUserList = [NSMutableArray array];
        
        for (HJMicroUserListInfo *info in userList) {
            if (info.status == 2) {
                [finallyUserList addObject:info];
            }
        }
        
        success(finallyUserList);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

//用户离开麦序
+ (void)userLeftMicroWithRoomUid:(UserID)roomUid {
    NSString *method = @"microV2/userleft";
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(roomUid) forKey:@"roomUid"];
    [params setObject:uid forKey:@"uid"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}


/**
 砸蛋排行榜

 @param type  1代表今天 2代表昨天
 */
+ (void)userGiftPurseGetRankWithType:(NSInteger)type
                             success:(void (^)(NSMutableArray *list))success
                             failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    NSString *method = @"user/giftPurse/getRank";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"type"] = @(type);
    params[@"roomId"] = @(GetCore(HJImRoomCoreV2).currentRoomInfo.roomId);
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSMutableArray *dataList = [NSMutableArray array];
        [dataList addObjectsFromArray:[NSArray yy_modelArrayWithClass:[HJGiftPurseRank class] json:data]];
        
        if (success) {
            success(dataList);
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}

//MARK: - 清空魅力值
+ (void)userReceiveRoomMicMsg:(UserID)uid roomUid:(NSInteger)roomUid
                             
                               success:(void (^)(NSInteger list))success
                               failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    NSString *method = @"room/receiveRoomMicMsg";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
  
    params[@"roomUid"] = [NSNumber numberWithInteger:uid];//roomUid
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
      params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        if (success) {
            success(1);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}
/**
 砸蛋中奖纪录
 */
+ (void)userGiftPurseRecordWithPageNum:(NSInteger)pageNum
                              pageSize:(NSInteger)pageSize
                               success:(void (^)(NSMutableArray *list))success
                               failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    NSString *method = @"user/giftPurse/record";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"pageNum"] = @(pageNum);
    params[@"pageSize"] = @(pageSize);
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSMutableArray *dataList = [NSMutableArray array];
        [dataList addObjectsFromArray:[NSArray yy_modelArrayWithClass:[HJGiftPurseRecord class] json:data]];
        
        if (success) {
            success(dataList);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}

/**
 房间背景列表
 
 @param roomId 房间id
 */
+ (void)roomBgListWithRoomId:(NSInteger)roomId
                     success:(void (^)(NSArray *list))success
                     failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"room/bg/list";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"roomId"] = @(roomId);
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *arr = [NSArray yy_modelArrayWithClass:[HJRoomBgModel class] json:data];
        if (success) {
            success(arr);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}

@end
