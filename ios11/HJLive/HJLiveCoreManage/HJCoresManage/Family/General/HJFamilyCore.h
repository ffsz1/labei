//
//  FamilyCore.h
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import "HJFamilyModel.h"

@interface HJFamilyCore : BaseCore

@property (nonatomic, strong) HJFamilyModel *myFamily;

/**
 获取家族列表
 
 @param familyId 家族id （为空默认全部）
 */
- (void)familyGetListWithFamilyId:(NSString *)familyId page:(NSInteger)page;

/**
 创建家族
 
 @param bgImg 背景图(暂不需要)
 @param logo logo
 @param name 家族名称
 @param notice 家族公告
 @param hall 房间id
 */
- (void)familyCreateFamilyTeamWithBgImg:(NSString *)bgImg
                                   logo:(NSString *)logo
                                   name:(NSString *)name
                                 notice:(NSString *)notice
                                   hall:(NSString *)hall;

/**
 获取加入家族信息
 */
- (void)familyGetJoinFamilyInfo;

/**
 获取家族信息
 */
- (void)familyGetFamilyInfoByFamilyId:(NSString *)familyId;

/**
 获取家族成员信息
 */
- (void)familyMemberInfoByFamilyID:(NSString *)familyId page:(NSInteger)page onceStr:(NSString *)onceStr;

/**
 申请加入家族
 */
- (void)applyJoinFamilyWithFamilyId:(NSString *)familyId;

/**
 申请退出家族
 */
- (void)applyExitFamilyWithFamilyId:(NSString *)familyId;

/**
 设置申请加入方式
 */
- (void)setApplyJoinMethodWithFamilyId:(NSString *)familyId joinmode:(NSString *)joinmode;

/**
 设置管理员(单人)
 */
- (void)setManagerWithFamilyId:(NSString *)familyId uid:(NSString *)uid;

/**
 设置管理员(多人)
 */
- (void)setManagerWithFamilyId:(NSString *)familyId uids:(NSString *)uids;

/**
 踢出家族
 */
- (void)setOutWithFamilyId:(NSString *)familyId userID:(NSString *)userID;

/**
 移除管理员
 */
- (void)removeManagerWithFamilyId:(NSString *)familyId userID:(NSString *)userID;

/**
 家族消息
 
 @param familyId 家族id
 */
- (void)familyGetFamilyMessageWithFamilyId:(NSString *)familyId page:(NSInteger)page count:(NSInteger)count;

/**
  操作家族
 
 @param familyId 家族ID
 @param status 状态(1.同意,2.拒绝)
 */
- (void)familyApplyFamilyWithFamilyId:(NSString *)familyId
                               userId:(NSString *)userId
                                 type:(NSInteger)type
                               status:(NSInteger)status;

/**
 家族首页banner
 */
- (void)homeV2FamilyBanner;

/**
 编辑家族信息
 
 @param familyId 家族ID
 @param logo logo
 @param notice 家族公告
 */
- (void)familyEditFamilyTeamWithFamilyId:(NSString *)familyId
                                    logo:(NSString *)logo
                         backgroundImage:(NSString *)backgroundImage
                                  notice:(NSString *)notice;

/**
 检测是否加入家族
 */
- (void)familyCheckFamilyJoin;

@end
