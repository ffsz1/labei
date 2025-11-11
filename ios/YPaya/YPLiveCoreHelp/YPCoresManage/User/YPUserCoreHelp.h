//
//  YPUserCoreHelp.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"
#import "UserInfo.h"
#import "YPUserGift.h"


@interface YPUserCoreHelp : YPBaseCore
  
@property (assign, nonatomic) BOOL isSaveUserInfo;

@property (nonatomic, assign) BOOL allBanned;
@property (nonatomic, assign) BOOL chatBanned;
@property (nonatomic, assign) BOOL roomBanned;
@property (nonatomic, assign) BOOL broadcastBanned;

- (BOOL)checkUserHasBanned:(int)Type;

- (void)checkUserIsBanned:(NSString *)uid;

//cache
- (void)cacheUserInfos:(NSArray *)userInfos
              complete:(void (^)(void))complete;

- (void)cacheUserInfo:(UserInfo *)userInfo
             complete:(void (^)(void))complete;

- (void)_updateCurrentUserInfo:(UserID)userId;
//query
- (RACSignal *)getUserInfoByUidV2:(UserID)uid;
- (RACSignal *)requestUserInfo:(UserID)userId;
- (RACSignal *)requestUserInfos:(NSArray *)userIdList;
- (void)getUserInfo:(UserID)userId refresh:(BOOL)refresh success:(void (^)(UserInfo *info))success;
- (void)getUserInfos:(NSArray *)userIds refresh:(BOOL) refresh success:(void (^)(NSArray *infoArr))success;
- (UserInfo *)getUserInfoInDB:(UserID)userId;
- (void)getReceiveGift:(UserID)userId orderType:(OrderType)orderType type:(NSInteger)type;

//voice text
- (void)getVoiceText:(UserID)uid;

/**
 保存是否发送关注消息
 
 @param likedSend 是否接受关注用户上线消息：1，发送；2，不发送
 */
- (void)userSettingSaveWithLikedSend:(BOOL)likedSend
                             success:(void (^)())success
                             failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 获取用户是否关闭关注消息提醒
 */
- (void)userSettingV1GetWithSuccess:(void (^)(BOOL likedSend))success failure:(void (^)(NSNumber *resCode, NSString *message))failur;

//action
- (RACSignal *)saveUserInfoWithUserID:(UserID)userId userInfos:(NSDictionary *)userInfos;

/**
 上传URL图片到服务器

 @param url url
 */
- (void)uploadImageUrlToServer:(NSString *)url;


/**
 从服务器删除图片

 @param pid 图片ID
 */
- (void)deleteImageUrlToServerWithPid:(NSString *)pid;


/**
 用rac查询用户信息

 @param userId 用户ID
 @param refresh 是否刷新
 @return rac 信号
 */
- (RACSignal *)getUserInfoByRac:(UserID)userId refresh:(BOOL)refresh;


/**
 用rac查询用户信息，带缓存

 @param uid 用户uid
 @param refresh 是否刷新
 @return rac 信号
 */
- (RACSignal *)getUserInfoByUid:(UserID)uid refresh:(BOOL)refresh;

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
                    requestId:(NSString *)requestId;

/**
 获取新人房间推荐
 */
- (void)roomRcmdGet;


/**
 获取财富等级
 */
- (void)getRichLevel;

/**
 获取魅力等级
 */
- (void)getMeiliLevel;

@end
