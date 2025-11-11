//
//  HJHttpRequestHelper+Auth.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"
#import "UserInfo.h"
#import "UserGift.h"
#import "HJLevelModel.h"

@interface HJHttpRequestHelper (User)


/**
 从服务器删除用户图片

 @param pid 图片id
 @param success 成功
 @param failure 失败
 */
+ (void)deleteImageToServerWithpid:(NSString *)pid
                           success:(void (^)(BOOL))success
                           failure:(void (^)(NSNumber *, NSString *))failure;


/**
 上传用户资料图片到服务器
 
 @param urlStr url字符串
 @param success 成功
 @param failure 失败
 */
+ (void) uploadImageURLToServerWithURL:(NSString *)urlStr
                               success:(void (^)(BOOL))success
                               failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 获取用户信息

 @param userId 用户id
 @param success 成功
 @param failure 失败
 */
+ (void) getUserInfo:(UserID )userId
             success:(void (^)(UserInfo *))success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 批量获取用户信息

 @param userIds 用户id
 @param success 成功
 @param failure 失败  
 */
+ (void) getUserInfos:(NSArray *)userIds
              success:(void (^)(NSArray *))success
              failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/*
 获取礼物墙
 
 */
+ (void)getReceiveGift:(UserID)userId orderType:(OrderType)orderType type:(NSInteger)type success:(void(^)(NSArray *))success failure:(void(^)(NSNumber *resCode,NSString *message)) failure;


/**
 更新用户信息

 @param userId 用户id
 @param success 成功
 @param failure 失败 
 */
+ (void) updateUserInfo:(UserID)userId
              userInfos:(NSDictionary *)userInfos
                success:(void (^)(UserInfo *))success
                failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 举报用户/房间
 
 @param uid 被举报的用户
 @param reportType 举报类型 1.政治 2.色情 3.广告 4.人身攻击
 @param type 类型 1. 用户 2. 房间
 @param phoneNo 手机号-不是必填
 */
+ (void)userReportSaveWithUid:(NSInteger)uid
                   reportType:(NSInteger)reportType
                         type:(NSInteger)type
                      phoneNo:(NSString *)phoneNo
                      success:(void (^)())success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 举报头像
 
 @param uid 被举报UID
 */
+ (void)reportAvatar:(NSInteger)uid
             success:(void (^)())success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 举报相册
 
 @param uid 被举报UID
 */
+ (void)reportAlbum:(NSInteger)uid
            success:(void (^)())success
            failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 获取新人房间推荐
 */
+ (void)roomRcmdGetWithsuccess:(void (^)(NSDictionary *result))success
                       failure:(void (^)(NSNumber *resCode, NSString *message))failure;

//查询用户是否被禁言
+ (void)checkUserHasBannedWith:(NSInteger)uid success:(void (^)(id data))success
                       failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 保存是否发送关注消息
 
 @param likedSend 是否接受关注用户上线消息：1，发送；2，不发送
 */
+ (void)userSettingSaveWithLikedSend:(BOOL)likedSend
                             success:(void (^)())success
                             failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 获取用户是否关闭关注消息提醒
 */
+ (void)userSettingV1GetWithSuccess:(void (^)(BOOL likedSend))success failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 获取财富等级
 */
+ (void)getRichLevel:(void (^)(HJLevelModel *))success
             failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 获取魅力等级
 */
+ (void)getMeiliLevel:(void (^)(HJLevelModel *))success
              failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 获取用户录音文本
 
 @param uid 用户id
 @param success 成功
 @param failure 失败
 */
+ (void)getVoiceTextWithUid:(UserID)uid
                    success:(void (^)(NSString *text))success
                    failure:(void (^)(NSNumber *resCode, NSString *message))failure;
@end
