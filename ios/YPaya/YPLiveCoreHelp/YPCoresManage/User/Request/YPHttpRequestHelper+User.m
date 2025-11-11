//
//  YPHttpRequestHelper+Auth.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+User.h"
#import "NSObject+YYModel.h"
#import "YPAuthCoreHelp.h"
#import "YPLinkedMeCore.h"
#import "YYUtility.h"

@implementation YPHttpRequestHelper (User)

+ (void)checkUserHasBannedWith:(NSInteger)uid success:(void (^)(id data))success
                       failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"banned/checkBanned";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *userUid = [GetCore(YPAuthCoreHelp) getUid];
    [params setObject:userUid forKey:@"uid"];
    
    [YPHttpRequestHelper  GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+(void)getUserInfo:(UserID)userId success:(void (^)(UserInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    if (userId <= 0) {
        return;
    }
    
    NSString *method = @"user/get";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(userId) forKey:@"uid"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        UserInfo *userInfo = [UserInfo yy_modelWithDictionary:data];
        if (success) {
            success(userInfo);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)getUserInfos:(NSArray *)userIds success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    if (userIds.count <= 0) {
        return;
    }
    
    NSString *method = @"user/list";
    NSMutableString *strUidList = [[NSMutableString alloc] init];
    for (int index = 0; index < [userIds count]; index++) {
        [strUidList appendString:[NSString stringWithFormat:@"%@", userIds[index]]];
        if (index != [userIds count] - 1) {
            [strUidList appendString:@","];
        }
    }
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:strUidList forKey:@"uids"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *userInfos = [NSArray yy_modelArrayWithClass:[UserInfo class] json:data];
        if (success) {
            success(userInfos);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)getReceiveGift:(UserID)userId orderType:(OrderType)orderType type:(NSInteger)type success:(void(^)(NSArray *))success failure:(void(^)(NSNumber *resCode,NSString *message)) failure{
    if (userId <= 0) {
        return;
    }
    NSMutableDictionary * params = [NSMutableDictionary dictionary];
    NSString *method = nil;
    if (type == 0) {
        method = @"giftwall/get";
        [params setObject:@(userId) forKey:@"uid"];
//        params[@"uid"] = [GetCore(YPAuthCoreHelp) getUid];
//        params[@"queryUid"] = @(userId);
//        params[@"ticket"] = [GetCore(YPAuthCoreHelp) getTicket];
    }
    else {
        method = @"giftwall/listMystic";
        params[@"uid"] = [GetCore(YPAuthCoreHelp) getUid];
        params[@"queryUid"] = @(userId);
        params[@"ticket"] = [GetCore(YPAuthCoreHelp) getTicket];
    }
    [params setObject:@(orderType) forKey:@"orderType"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray * userGifts = [NSArray yy_modelArrayWithClass:[YPUserGift class] json:data];
        if (success) {
            success(userGifts);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}



+ (void)getRichLevel:(void (^)(YPLevelModel *))success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure
{
    NSString *ticket = GetCore(YPAuthCoreHelp).getTicket;
    NSString *uid = GetCore(YPAuthCoreHelp).getUid;
    
    NSString *method = @"level/exeperience/get";
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    
    if (uid.length == 0) {
        return;
    }
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        YPLevelModel *userInfo = [YPLevelModel yy_modelWithDictionary:data];
        if (success) {
            success(userInfo);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)getMeiliLevel:(void (^)(YPLevelModel *))success
              failure:(void (^)(NSNumber *resCode, NSString *message))failure
{
    NSString *ticket = GetCore(YPAuthCoreHelp).getTicket;
    NSString *uid = GetCore(YPAuthCoreHelp).getUid;
    
    NSString *method = @"level/charm/get";
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    if (uid.length == 0) {
        return;
    }
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        YPLevelModel *userInfo = [YPLevelModel yy_modelWithDictionary:data];
        if (success) {
            success(userInfo);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)updateUserInfo:(UserID)userId
              userInfos:(NSDictionary *)userInfos
                success:(void (^)(UserInfo *))success
                failure:(void (^)(NSNumber *resCode, NSString *message))failure
{
    if (userId <= 0) {
        return;
    }
    
    NSString *ticket = GetCore(YPAuthCoreHelp).getTicket;
    
    NSString *method = @"user/v2/update";
    NSMutableDictionary *params = [userInfos mutableCopy];
    [params setObject:@(userId) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    if (GetCore(YPLinkedMeCore).linkme) {
        if (GetCore(YPLinkedMeCore).linkme.roomuid != nil) {
            [params setObject:GetCore(YPLinkedMeCore).linkme.roomuid forKey:@"roomUid"];
        }
        if (GetCore(YPLinkedMeCore).linkme.uid != nil) {
            [params setObject:GetCore(YPLinkedMeCore).linkme.uid forKey:@"shareUid"];
        }
        if (GetCore(YPLinkedMeCore).channel != nil) {
            [params setObject:GetCore(YPLinkedMeCore).channel forKey:@"shareChannel"];
        }
    }
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        UserInfo *userInfo = [UserInfo yy_modelWithDictionary:data];
        if (success) {
            success(userInfo);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

//上传用户相册图片到服务器
+ (void)uploadImageURLToServerWithURL:(NSString *)urlStr success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"photo/v2/upload";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:uid forKey:@"uid"];
    [params setObject:urlStr forKey:@"photoStr"];
    [params setObject:ticket forKey:@"ticket"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

//删除用户图片
+ (void)deleteImageToServerWithpid:(NSString *)pid success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"photo/delPhoto";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp)getTicket];
    [params setObject:uid forKey:@"uid"];
    [params setObject:pid forKey:@"pid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}


/**
 举报用户/房间

 @param uid 被举报的用户
 @param reportType 举报类型:1.政治敏感 2.⾊情低俗 3.⼴告骚扰 4.人身攻击 5.其他
 6.昵称 7.房间标题
 
 @param type 类型 1. 用户 2. 房间
 @param phoneNo 手机号-不是必填
 */
+ (void)userReportSaveWithUid:(NSInteger)uid
                   reportType:(NSInteger)reportType
                         type:(NSInteger)type
                      phoneNo:(NSString *)phoneNo
                      success:(void (^)())success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    NSString *method = @"user/report/save";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = @(uid);
    params[@"reportUid"] = @([GetCore(YPAuthCoreHelp) getUid].integerValue);
    params[@"reportType"] = @(reportType);
    params[@"type"] = @(type);
    params[@"deviceId"] = [YYUtility deviceUniqueIdentification];
    params[@"phoneNo"] = phoneNo;
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}


/**
 举报头像

 @param uid 被举报UID
 */
+ (void)reportAvatar:(NSInteger)uid
             success:(void (^)())success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    NSString *method = @"user/report/save";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = @(uid);
    params[@"reportUid"] = @([GetCore(YPAuthCoreHelp) getUid].integerValue);
    params[@"reportType"] = @(5);
    params[@"type"] = @(1);
    params[@"deviceId"] = [YYUtility deviceUniqueIdentification];
    params[@"phoneNo"] = @"";
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

/**
 举报相册
 
 @param uid 被举报UID
 */
+ (void)reportAlbum:(NSInteger)uid
             success:(void (^)())success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    NSString *method = @"user/report/save";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = @(uid);
    params[@"reportUid"] = @([GetCore(YPAuthCoreHelp) getUid].integerValue);
    params[@"reportType"] = @(7);
    params[@"type"] = @(1);
    params[@"deviceId"] = [YYUtility deviceUniqueIdentification];
    params[@"phoneNo"] = @"";
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

/**
 获取新人房间推荐
 */
+ (void)roomRcmdGetWithsuccess:(void (^)(NSDictionary *result))success
                       failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    NSString *method = @"room/rcmd/get";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(YPAuthCoreHelp) getUid];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];

}


/**
 保存是否发送关注消息

 @param likedSend 是否接受关注用户上线消息：1，发送；2，不发送
 */
+ (void)userSettingSaveWithLikedSend:(BOOL)likedSend
                             success:(void (^)())success
                             failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"user/setting/v1/save";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(YPAuthCoreHelp) getUid];
    params[@"likedSend"] = likedSend ? @"1" : @"2";
    params[@"ticket"] = [GetCore(YPAuthCoreHelp) getTicket];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        if (success) {
            success();
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
    
}


/**
 获取用户是否关闭关注消息提醒
 */
+ (void)userSettingV1GetWithSuccess:(void (^)(BOOL likedSend))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    NSString *method = @"user/setting/v1/get";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(YPAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(YPAuthCoreHelp) getTicket];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        NSInteger send = [data[@"likedSend"] integerValue];
        if (success) {
            success(send != 2);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}

+ (void)getVoiceTextWithUid:(UserID)uid
                    success:(void (^)(NSString *text))success
                    failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"user/getVoiceText";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = [GetCore(YPAuthCoreHelp)getTicket];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

@end
