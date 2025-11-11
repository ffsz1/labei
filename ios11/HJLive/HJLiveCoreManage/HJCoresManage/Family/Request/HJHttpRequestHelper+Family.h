//
//  HJHttpRequestHelper+Family.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"

@interface HJHttpRequestHelper (Family)

/**
 获取家族列表
 
 @param familyId 家族id （为空默认全部）
 */
+ (void)familyGetListWithFamilyId:(NSString *)familyId page:(NSInteger)page success:(void (^)(NSMutableDictionary *data))success failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 创建家族
 
 @param bgImg 背景图(暂不需要)
 @param logo logo
 @param name 家族名称
 @param notice 家族公告
 @param hall 房间id
 */
+ (void)familyCreateFamilyTeamWithBgImg:(NSString *)bgImg
                                   logo:(NSString *)logo
                                   name:(NSString *)name
                                 notice:(NSString *)notice
                                   hall:(NSString *)hall
                                success:(void (^)())success
                                failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 获取加入家族信息
 */
+ (void)familyGetJoinFamilyInfoWithSuccess:(void (^)(id data))success
                                   failure:(void (^)(NSNumber *code, NSString *msg))failure;


/**
 获取家族成员信息
 */
+ (void)familyGetFamilyTeamJoinWithFamilyId:(NSString *)familyId
                                       page:(NSInteger)page
                                    success:(void (^)(id data))success
                                    failure:(void (^)(NSNumber *code, NSString *msg))failure;


/**
 申请加入家族
 
 @param familyId 家族id
 */
+ (void)applyJoinFamilyWithFamilyId:(NSString *)familyId
                            Success:(void (^)(id data))success
                            failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 申请退出家族
 
 @param familyId 家族id
 */
+ (void)applyExitFamilyWithFamilyId:(NSString *)familyId
                            Success:(void (^)(id data))success
                            failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 设置申请加入方式
 
 @param familyId 家族id
 */
+ (void)setApplyJoinMethodWithFamilyId:(NSString *)familyId
                              joinmode:(NSString *)joinmode
                               Success:(void (^)(id data))success
                               failure:(void (^)(NSNumber *code, NSString *msg))failure;


/**
 设置消息提醒
 
 @param familyId 家族id
 @param ope 1：关闭消息提醒，2：打开消息提醒，其他值无效
 
 */
+ (void)setMsgNotifyWithFamilyId:(NSString *)familyId
                             ope:(NSInteger)ope
                         Success:(void (^)(id data))success
                         failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 设置管理员(单人)
 
 @param familyId 家族id
 @param uId 成员uid
 
 */
+ (void)setManagerWithFamilyId:(NSString *)familyId
                           uid:(NSString *)uid
                       Success:(void (^)(id data))success
                       failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 设置管理员
 
 @param familyId 家族id
 @param uids 成员id
 
 */
+ (void)setManagerWithFamilyId:(NSString *)familyId
                          uids:(NSString *)uids
                       Success:(void (^)(id data))success
                       failure:(void (^)(NSNumber *code, NSString *msg))failure;


/**
 移除管理员
 
 @param familyId 家族id
 @param userId 成员id
 
 */
+ (void)removeManagerWithFamilyId:(NSString *)familyId
                           userId:(NSString *)userId
                          Success:(void (^)(id data))success
                          failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 踢出家族
 
 @param familyId 家族id
 @param userId 成员id
 
 */
+ (void)setOutWithFamilyId:(NSString *)familyId
                    userId:(NSString *)userId
                   Success:(void (^)(id data))success
                   failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 家族消息
 
 @param familyId 家族id
 */
+ (void)familyGetFamilyMessageWithFamilyId:(NSString *)familyId
                                      page:(NSInteger)page
                                     count:(NSInteger)count
                                   success:(void (^)(NSArray *dataArr))success
                                   failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
  操作家族
 
 @param familyId 家族ID
 @param status 状态(1.同意,2.拒绝)
 */
+ (void)familyApplyFamilyWithFamilyId:(NSString *)familyId
                               userId:(NSString *)userId
                                 type:(NSInteger)type
                               status:(NSInteger)status
                              success:(void (^)())success
                              failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 家族首页banner
 */
+ (void)homeV2FamilyBannerWithSuccess:(void (^)(NSArray *dataArr))success
                              failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 编辑家族信息
 
 @param familyId 家族ID
 @param logo logo
 @param notice 家族公告
 */
+ (void)familyEditFamilyTeamWithFamilyId:(NSString *)familyId
                                    logo:(NSString *)logo
                         backgroundImage:(NSString *)backgroundImage
                                  notice:(NSString *)notice
                                 success:(void (^)())success
                                 failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 根据家族Id, 获取家族信息

 @param familyId 家族Id
 @param success  success description
 @param failure  failure description
 */
+ (void)familyGetFamilyInfoWithFamilyId:(NSString *)familyId
                                success:(void (^)(id data))success
                                failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 根据uid检测是否有加入家族

 @param success success description
 @param failure failure description
 */
+ (void)familyCheckFamilyJoinSuccess:(void (^)(id data))success
                             failure:(void (^)(NSNumber *code, NSString *msg))failure;

@end
