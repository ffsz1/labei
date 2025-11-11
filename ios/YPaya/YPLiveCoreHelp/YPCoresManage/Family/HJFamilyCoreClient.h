//
//  HJFamilyCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
@protocol HJFamilyCoreClient <NSObject>
@optional
// 获取家族列表
- (void)familyGetListSuccessWithDataArr:(NSArray *)dataArr teamModel:(id)teamModel;
- (void)familyGetListFailedWithMessage:(NSString *)message;

// 创建家族
- (void)familyCreateFamilyTeamSuccess;
- (void)familyCreateFamilyTeamFailedWithMessage:(NSString *)message;

// 获取加入家族信息
- (void)familyGetJoinFamilyInfoSuccessWithData:(id)data;
- (void)familyGetJoinFamilyInfoFailedWithMessage:(NSString *)message;

// 获取家族信息
- (void)familyGetFamilyInfoSuccessWithData:(id)data;
- (void)familyGetFamilyInfoFailedWithMessage:(NSString *)message;

// 获取家族成员信息
- (void)familyMemberInfoSuccessWithData:(id)data once:(NSString *)once;
- (void)familyMemberInfoSuccessWithDataFailedWithMessage:(NSString *)message;

// 申请加入家族
- (void)applyJoinFamilySuccessWithData:(id)data;
- (void)applyJoinFamilyFailedWithDataFailedWithMessage:(NSString *)message;

// 申请退出家族
- (void)applyExitFamilySuccessWithData:(id)data;
- (void)applyExitFamilyFailedWithDataFailedWithMessage:(NSString *)message;

// 设置申请加入方式
- (void)setApplyJoinMethodSuccessWithData:(id)data;
- (void)setApplyJoinMethodFailedWithDataFailedWithMessage:(NSString *)message;

// 设置管理员
- (void)setManagerSuccessWithData:(id)data;
- (void)setManagerFailedWithDataFailedWithMessage:(NSString *)message;

// 设置管理员
- (void)removeManagerSuccessWithData:(id)data;
- (void)removeManagerFailedWithDataFailedWithMessage:(NSString *)message;

// 踢出家族
- (void)setOutSuccessWithData:(id)data;
- (void)setOutMethodFailedWithDataFailedWithMessage:(NSString *)message;

// 家族消息
- (void)familyGetFamilyMessageSuccessWithDataArr:(NSArray *)dataArr;
- (void)familyGetFamilyMessageFailedWithMessage:(NSString *)message;

//  操作家族
- (void)familyApplyFamilySuccess;
- (void)familyApplyFamilyFailedWithMessage:(NSString *)message;

// 家族首页banner
- (void)homeV2FamilyBannerSuccessWithDataArr:(NSArray *)dataArr;
- (void)homeV2FamilyBannerWithMessage:(NSString *)message;

// 修改家族信息
- (void)familyEditFamilyTeamSuccess;
- (void)familyEditFamilyTeamFailedWithMessage:(NSString *)message;

//检测加入家族
- (void)familyCheckFamilyJoinSuccess:(id)data;
- (void)familyCheckFamilyJoinFailedWithMessage:(NSString *)message;

@end
