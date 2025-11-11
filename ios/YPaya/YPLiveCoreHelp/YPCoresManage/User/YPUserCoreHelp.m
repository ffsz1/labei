//
//  YPUserCoreHelp.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPUserCoreHelp.h"

#import "NSObject+YYModel.h"
#import "YPHttpRequestHelper+User.h"

#import "NSDictionary+Safe.h"
#import "HJUserCoreClient.h"
#import "HJAuthCoreClient.h"

#import "YPAuthCoreHelp.h"
#import "HJDBManager.h"

#import "YPUserCache.h"
#import "JPUSHService.h"

@interface YPUserCoreHelp ()<HJAuthCoreClient>

@end


@implementation YPUserCoreHelp
- (instancetype)init
{
    self = [super init];
    if (self) {
        AddCoreClient(HJAuthCoreClient, self);
        
        self.allBanned = false;
        self.chatBanned = false;
        self.roomBanned = false;
        self.broadcastBanned = false;
        
        [self _configLastLoginUserInfo];
        
        @weakify(self);
        [[self rac_signalForSelector:@selector(onLoginSuccess) fromProtocol:@protocol(HJAuthCoreClient)] subscribeNext:^(RACTuple *tuple) {
            @strongify(self);
            UserID userId = [GetCore(YPAuthCoreHelp) getUid].userIDValue;//当前用户id
            [self _updateCurrentUserInfo:userId];
            [JPUSHService setAlias:[GetCore(YPAuthCoreHelp) getUid] completion:^(NSInteger iResCode, NSString *iAlias, NSInteger seq) {
                NSLog(@"别名设置结果2---%zd",iResCode);
            } seq:1];
            
        }];
        
        [[self rac_signalForSelector:@selector(onLogout) fromProtocol:@protocol(HJAuthCoreClient)] subscribeNext:^(id x) {
            NotifyCoreClient(HJUserCoreClient, @selector(onCurrentUserInfoLogout), onCurrentUserInfoLogout);
            [JPUSHService deleteAlias:^(NSInteger iResCode, NSString *iAlias, NSInteger seq) {
                NSLog(@"删除别名---%zd",iResCode);
            } seq:1];
        }];
    }
    return self;
}

- (void)getVoiceText:(UserID)uid {
    [YPHttpRequestHelper getVoiceTextWithUid:uid success:^(NSString *text) {
        NotifyCoreClient(HJUserCoreClient, @selector(onGetVoiceTextSuccess:), onGetVoiceTextSuccess:text);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJUserCoreClient, @selector(onGetVoiceTextFailth:), onGetVoiceTextFailth:message);
    }];
}

- (void)_configLastLoginUserInfo
{
    //获取上次登录用户
    //    UserID userId = [GetCore(AuthCore) getUid].userIDValue;
    //    [GetCoreI(IRealmConfig) setDefaultRealmWithUserID:userId];
    //    NSPredicate *pred = [NSPredicate predicateWithFormat:@"uid = %lld", userId];
    //    RLMResults<UserInfo *> *result = [UserInfo objectsWithPredicate:pred];
    //    UserInfo *userInfo = nil;
    //    if (result && [result count] > 0) {
    //        userInfo = [result firstObject];
    //    }
    //
    //    self.currentUserInfo = userInfo;
}

- (void)_updateCurrentUserInfo:(UserID)userId
{
    @weakify(self);
    [[self requestUserInfo:userId] subscribeNext:^(id x) {
        UserInfo *userInfo = (UserInfo *)x;
        
        @strongify(self);
        [self cacheUserInfo:userInfo complete:^{
            @strongify(self);

            [self getUserInfo:userId refresh:NO success:^(UserInfo * info) {
                if (info.nick.length <= 0 || info.avatar.length <= 0 || info == nil) {
//                    NotifyCoreClient(HJUserCoreClient, @selector(onCurrentUserInfoNeedComplete:), onCurrentUserInfoNeedComplete:userId);
                } else {
                    NotifyCoreClient(HJUserCoreClient, @selector(onCurrentUserInfoUpdate:), onCurrentUserInfoUpdate:info);
                }
            }];
            
        }];
    } error:^(NSError *error) {
        [[HJDBManager defaultManager]getUserWithUserID:userId success:^(UserInfo *userInfo) {
            if (userInfo.avatar.length <= 0 || userInfo.nick.length <= 0 || userInfo == nil) {
                [GetCore(YPAuthCoreHelp)logout];
            }
        }];
    }];
}

#pragma mark - IUserInfoAction
- (RACSignal *)saveUserInfoWithUserID:(UserID)userId userInfos:(NSDictionary *)userInfos
{
    @weakify(self);
    return [[RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        [YPHttpRequestHelper updateUserInfo:userId userInfos:userInfos success:^(UserInfo *userInfo) {
            [subscriber sendNext:userInfo];
            [subscriber sendCompleted];
            self.isSaveUserInfo = YES;
            [self getUserInfo:userId refresh:YES success:^(UserInfo *info) {
                NotifyCoreClient(HJUserCoreClient, @selector(onCurrentUserInfoUpdate:), onCurrentUserInfoUpdate:userInfo);
            }];
            GetCore(YPAuthCoreHelp).info = nil;
            GetCore(YPAuthCoreHelp).qqInfo = nil;
        } failure:^(NSNumber *resCode, NSString *message) {
            [subscriber sendError:CORE_API_ERROR(UserCoreErrorDomain, resCode.integerValue, message)];
        }];
        return nil;
    }] doNext:^(UserInfo *userInfo) {
        //保存当前用户信息
        @strongify(self);
        //        [self getUserInfo:userId refresh:YES];
        [self cacheUserInfos:@[userInfo] complete:nil];
    }];
}

#pragma mark - IUserInfoQuery

- (RACSignal *)requestUserInfo:(UserID)userId
{
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        [YPHttpRequestHelper getUserInfo:userId success:^(UserInfo *userInfo) {
            [subscriber sendNext:userInfo];
            [subscriber sendCompleted];
        } failure:^(NSNumber *resCode, NSString *message) {
            [subscriber sendError:CORE_API_ERROR(UserCoreErrorDomain, resCode.integerValue, message)];
        }];
        return nil;
    }];
}

- (RACSignal *)requestUserInfos:(NSArray *)userIdList
{
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        [YPHttpRequestHelper getUserInfos:userIdList success:^(NSArray *userInfoList) {
            [subscriber sendNext:userInfoList];
            [subscriber sendCompleted];
        } failure:^(NSNumber *resCode, NSString *message) {
            [subscriber sendError:CORE_API_ERROR(UserCoreErrorDomain, resCode.integerValue, message)];
        }];
        return nil;
    }];
}

- (void)getUserInfo:(UserID)userId refresh:(BOOL)refresh success:(void (^)(UserInfo *))success
{
    //    UserInfo *userInfo = [[HJDBManager defaultManager]getUserWithUserID:userId];
    [[HJDBManager defaultManager] getUserWithUserID:userId success:^(UserInfo *userInfo) {
        if (userInfo == nil || userInfo.erbanNo.length == 0){
            [self _syncServerUserInfo:userId success:^(UserInfo *info) {
                success(info);
            }];
        } else {
            if (refresh){
                [self _syncServerUserInfo:userId success:^(UserInfo *info) {
                    
                    success(info);
                }];
            }else {
                dispatch_async(dispatch_get_main_queue(), ^{
                    success(userInfo);
                    
                });
            }
        }
        
    }];
}

- (RACSignal *)getUserInfoByRac:(UserID)userId refresh:(BOOL)refresh {
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        [[HJDBManager defaultManager]getUserWithUserID:userId success:^(UserInfo *userInfo) {
            if (userInfo == nil || userInfo.erbanNo.length == 0){
                [self _syncServerUserInfo:userId success:^(UserInfo *info) {
                    [subscriber sendNext:info];
                }];
            } else {
                if (refresh){
                    [self _syncServerUserInfo:userId success:^(UserInfo *info) {
                        [subscriber sendNext:info];
                    }];
                }else {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [subscriber sendNext:userInfo];
                    });
                }
            }
            
        }];
        return nil;
    }];
}

- (RACSignal *)getUserInfoByUidV2:(UserID)uid {
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        [self _syncServerUserInfo:uid success:^(UserInfo *info) {
            [subscriber sendNext:info];
            [subscriber sendCompleted];
        }];
        return nil;
    }];
}

- (RACSignal *)getUserInfoByUid:(UserID)uid refresh:(BOOL)refresh {
    return [RACSignal createSignal:^RACDisposable *(id<RACSubscriber> subscriber) {
        [[[YPUserCache shareCache] getUserInfoFromCacheWith:uid] subscribeNext:^(id x) {
            if (x) { //缓存或者DB里面有
                [subscriber sendNext:x];
                
                if (refresh) {
                    [self _syncServerUserInfo:uid success:^(UserInfo *info) {
                        [subscriber sendNext:info];
                        [subscriber sendCompleted];
                    }];
                }else {
                    [subscriber sendCompleted];
                }
                
            }else { //缓存或者DB里面都没有,从网络请求
                [self _syncServerUserInfo:uid success:^(UserInfo *info) {
                    [subscriber sendNext:info];
                    [subscriber sendCompleted];
                }];
            }
        }];
        return nil;
    }];
}

- (void)getUserInfos:(NSArray *)userIds refresh:(BOOL) refresh success:(void (^)(NSArray *))success
{
    if (userIds.count > 0) {
        NSMutableArray *userInfos = [NSMutableArray array];
        NSMutableArray *needUpdate = [NSMutableArray array];
        BOOL needRefresh = NO;
        for (NSNumber *userId in userIds) {
            UserInfo *userInfo = [[HJDBManager defaultManager] getUserWithUserID:userId.userIDValue];
            if (userInfo == nil) {
                needRefresh = YES;
                break;
            }else {
                [userInfos addObject:userInfo];
            }
        }
        
        if (refresh) {
            [self _sycServerUserInfos:userIds success:^(NSArray *infoArr) {
                success(infoArr);
                [[HJDBManager defaultManager]creatOrUpdateUsers:infoArr];
            }];
        } else {
            if (needRefresh) {
                [self _sycServerUserInfos:needUpdate success:^(NSArray *infoArr) {
                    [[HJDBManager defaultManager]creatOrUpdateUsers:infoArr];
                    success([infoArr copy]);
                }];

            }else {
                success([userInfos copy]);
            }
        }
    }
}

- (UserInfo *)getUserInfoInDB:(UserID)userId {
    return [[HJDBManager defaultManager] getUserWithUserID:userId];
}

//是否被禁言
- (BOOL)checkUserHasBanned:(int)Type {
    if (self.allBanned) {
        return YES;
    }
    //私聊
    if (Type == 0) {
        return self.chatBanned;
    }
    //房间
    if (Type == 1) {
        return self.roomBanned;
    }
    //公聊
    return self.broadcastBanned;
}

//查询用户是否被禁言
- (void)checkUserIsBanned:(NSString *)uid {
    //    if (userId == 0) {return;}
    [YPHttpRequestHelper checkUserHasBannedWith:[uid longLongValue] success:^(id data){
        @try {
            NSDictionary *dic = (NSDictionary *)data;
            self.allBanned = [dic[@"all"] boolValue];
            self.chatBanned = [dic[@"chat"] boolValue];
            self.roomBanned = [dic[@"room"] boolValue];
            self.broadcastBanned = [dic[@"broadcast"] boolValue];
        } @catch (NSException *exception) {}
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJUserCoreClient, @selector(checkUserhasBanndFail:), checkUserhasBanndFail:message);
    }];
}


//获取礼物墙
- (void)getReceiveGift:(UserID)userId orderType:(OrderType)orderType type:(NSInteger)type {
    if (userId > 0) {

        [YPHttpRequestHelper getReceiveGift:userId orderType:orderType type:type success:^(NSArray * userGiftList){
            NotifyCoreClient(HJUserCoreClient, @selector(onGetReceiveGiftSuccess:uid:type:), onGetReceiveGiftSuccess:userGiftList uid:userId type:type);
        } failure:^(NSNumber *resCode, NSString *message) {
            NotifyCoreClient(HJUserCoreClient, @selector(onGetReceiveGiftFailth:type:), onGetReceiveGiftFailth:message type:type);
        }];
    }
}



//上传图片url到服务器
- (void)uploadImageUrlToServer:(NSString *)url {
    [YPHttpRequestHelper uploadImageURLToServerWithURL:url success:^(BOOL isSuccess) {
        NotifyCoreClient(HJUserCoreClient, @selector(onUploadImageUrlToServerSuccess), onUploadImageUrlToServerSuccess);
        NotifyCoreClient(HJUserCoreClient, @selector(onCurrentUserInfoUpdate:), onCurrentUserInfoUpdate:nil);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJUserCoreClient, @selector(onUploadImageUrlToServerFailth:), onUploadImageUrlToServerFailth:message);
    }];
}

//删除用户图片
- (void)deleteImageUrlToServerWithPid:(NSString *)pid {
    [YPHttpRequestHelper deleteImageToServerWithpid:pid success:^(BOOL isSuccess) {
        NotifyCoreClient(HJUserCoreClient, @selector(deleteImageToServerSuccess), deleteImageToServerSuccess)
        NotifyCoreClient(HJUserCoreClient, @selector(onCurrentUserInfoUpdate:), onCurrentUserInfoUpdate:nil);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJUserCoreClient, @selector(deleteImageUrlToServerFailth:), deleteImageUrlToServerFailth:message);
    }];
}

//- (UserInfo *)_createNewUserInfo:(UserID)userId
//{
//    UserInfo *aUserInfo = [[UserInfo alloc] init];
//    aUserInfo.uid = userId;
//    [[HJDBManager defaultManager]creatOrUpdateUser:aUserInfo complete:nil];
//    UserInfo *userInfo = [[HJDBManager defaultManager]getUserWithUserID:userId];
//    return userInfo;
//}

- (void)_syncServerUserInfo:(UserID)userId success:(void (^)(UserInfo *))success
{
    [[self requestUserInfo:userId] subscribeNext:^(id x) {
        UserInfo *newUserInfo = (UserInfo *)x;
        [[HJDBManager defaultManager] creatOrUpdateUser:newUserInfo complete:nil];
        [[YPUserCache shareCache] saveUserInfo:newUserInfo];
        success(newUserInfo);
    }];
}

- (void)_sycServerUserInfos:(NSArray *)userIds success:(void (^)(NSArray *))success
{
    [[self requestUserInfos:userIds] subscribeNext:^(id x) {
        NSArray *newUserInfos = (NSArray *)x;
        [[HJDBManager defaultManager]creatOrUpdateUsers:newUserInfos];
        success(newUserInfos);
    }];
}

/**
 举报用户/房间
 
 @param uid 被举报的用户
 @param reportType 举报类型 1.政治 2.色情 3.广告 4.人身攻击
 @param type 类型 1. 用户 2. 房间
 @param phoneNo 手机号-不是必填
 */
- (void)userReportSaveWithUid:(NSInteger)uid
                   reportType:(NSInteger)reportType
                         type:(NSInteger)type
                      phoneNo:(NSString *)phoneNo
                    requestId:(NSString *)requestId {
    
    [YPHttpRequestHelper userReportSaveWithUid:uid reportType:reportType type:type phoneNo:phoneNo success:^{
        NotifyCoreClient(HJUserCoreClient, @selector(userReportSaveSuccessWithType:requestId:), userReportSaveSuccessWithType:type requestId:requestId);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJUserCoreClient, @selector(userReportSaveFailth:type:requestId:), userReportSaveFailth:message type:type requestId:requestId);
    }];
}


/**
 获取新人房间推荐
 */
- (void)roomRcmdGet {
    [YPHttpRequestHelper roomRcmdGetWithsuccess:^(NSDictionary *result) {
        NotifyCoreClient(HJUserCoreClient, @selector(roomRcmdGetSuccessWithResult:), roomRcmdGetSuccessWithResult:result);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJUserCoreClient, @selector(roomRcmdGetFailedWithMessage:), roomRcmdGetFailedWithMessage:message);
    }];
}

/**
 保存是否发送关注消息
 
 @param likedSend 是否接受关注用户上线消息：1，发送；2，不发送
 */
- (void)userSettingSaveWithLikedSend:(BOOL)likedSend
                             success:(void (^)())success
                             failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    [YPHttpRequestHelper userSettingSaveWithLikedSend:likedSend success:success failure:failure];
}

/**
 获取用户是否关闭关注消息提醒
 */
- (void)userSettingV1GetWithSuccess:(void (^)(BOOL likedSend))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    [YPHttpRequestHelper userSettingV1GetWithSuccess:success failure:failure];
}



/**
 获取财富等级
 */
- (void)getRichLevel {
   
    [YPHttpRequestHelper getRichLevel:^(YPLevelModel *model) {
        NotifyCoreClient(HJUserCoreClient, @selector(onGetRichLevelSuccess:), onGetRichLevelSuccess:model);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJUserCoreClient, @selector(onGetRichLevelFailth:), onGetRichLevelFailth:message);
    }];
}

/**
 获取魅力等级
 */
- (void)getMeiliLevel {
    
    [YPHttpRequestHelper getMeiliLevel:^(YPLevelModel *model) {
        NotifyCoreClient(HJUserCoreClient, @selector(onGetMeiliLevelSuccess:), onGetMeiliLevelSuccess:model);
    } failure:^(NSNumber *resCode, NSString *message) { 
        NotifyCoreClient(HJUserCoreClient, @selector(onGetMeiliLevelFailth:), onGetMeiliLevelFailth:message);
    }];
}

#pragma mark - IUserInfoCache

- (void)cacheUserInfos:(NSArray *)userInfos
              complete:(void (^)(void))complete
{
    [[HJDBManager defaultManager]creatOrUpdateUsers:userInfos];
    
}

- (void)cacheUserInfo:(UserInfo *)userInfo
             complete:(void (^)(void))complete
{
    [[HJDBManager defaultManager] creatOrUpdateUser:userInfo complete:complete];
}

@end
