//
//  FamilyCore.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyCore.h"
#import "HJFamilyCoreClient.h"

#import "YPFamilyInfoDetail.h"

#import "YPHttpRequestHelper+Family.h"

@implementation YPFamilyCore

/**
 获取家族列表
 
 @param familyId 家族id （为空默认全部）
 */
- (void)familyGetListWithFamilyId:(NSString *)familyId page:(NSInteger)page{
    
    [YPHttpRequestHelper familyGetListWithFamilyId:familyId page:page success:^(NSMutableDictionary *data) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyGetListSuccessWithDataArr:teamModel:), familyGetListSuccessWithDataArr:data[@"familyList"] teamModel:data[@"familyTeam"]);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyGetListFailedWithMessage:), familyGetListFailedWithMessage:msg);
    }];
}

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
                                   hall:(NSString *)hall {
    
    [YPHttpRequestHelper familyCreateFamilyTeamWithBgImg:bgImg logo:logo name:name notice:notice hall:hall success:^{
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyCreateFamilyTeamSuccess), familyCreateFamilyTeamSuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyCreateFamilyTeamFailedWithMessage:), familyCreateFamilyTeamFailedWithMessage:msg);
    }];
}

/**
 获取加入家族信息
 */
- (void)familyGetJoinFamilyInfo {
    
    [YPHttpRequestHelper familyGetJoinFamilyInfoWithSuccess:^(id data) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyGetJoinFamilyInfoSuccessWithData:), familyGetJoinFamilyInfoSuccessWithData:data);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyGetJoinFamilyInfoFailedWithMessage:), familyGetJoinFamilyInfoFailedWithMessage:msg);
    }];
}

/**
 获取家族信息
 */
- (void)familyGetFamilyInfoByFamilyId:(NSString *)familyId {
    [YPHttpRequestHelper familyGetFamilyInfoWithFamilyId:familyId success:^(id data) {
        YPFamilyInfoDetail *detail = [YPFamilyInfoDetail yy_modelWithJSON:data];
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyGetFamilyInfoSuccessWithData:), familyGetFamilyInfoSuccessWithData:detail);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyGetFamilyInfoFailedWithMessage:), familyGetFamilyInfoFailedWithMessage:msg);
    }];
}

/**
 获取家族成员信息
 */
- (void)familyMemberInfoByFamilyID:(NSString *)familyId page:(NSInteger)page onceStr:(NSString *)onceStr{
    
    [YPHttpRequestHelper familyGetFamilyTeamJoinWithFamilyId:familyId page:page success:^(id data) {
        
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyMemberInfoSuccessWithData:once:), familyMemberInfoSuccessWithData:data once:onceStr);
    
        
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyMemberInfoSuccessWithDataFailedWithMessage:), familyMemberInfoSuccessWithDataFailedWithMessage:msg);
    }];
}

/**
 申请加入家族
 */
- (void)applyJoinFamilyWithFamilyId:(NSString *)familyId{
    [YPHttpRequestHelper applyJoinFamilyWithFamilyId:familyId Success:^(id data) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(applyJoinFamilySuccessWithData:), applyJoinFamilySuccessWithData:data);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(applyJoinFamilyFailedWithDataFailedWithMessage:), applyJoinFamilyFailedWithDataFailedWithMessage:msg);
    }];
    
}

/**
 申请退出家族
 */
- (void)applyExitFamilyWithFamilyId:(NSString *)familyId{
    [YPHttpRequestHelper applyExitFamilyWithFamilyId:familyId Success:^(id data) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(applyExitFamilySuccessWithData:), applyExitFamilySuccessWithData:data);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(applyExitFamilyFailedWithDataFailedWithMessage:), applyExitFamilyFailedWithDataFailedWithMessage:msg);
    }];
    
}

/**
 设置申请加入方式
 */
- (void)setApplyJoinMethodWithFamilyId:(NSString *)familyId joinmode:(NSString *)joinmode{
    [YPHttpRequestHelper setApplyJoinMethodWithFamilyId:familyId joinmode:joinmode Success:^(id data) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(setApplyJoinMethodSuccessWithData:), setApplyJoinMethodSuccessWithData:data);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(setApplyJoinMethodFailedWithDataFailedWithMessage:), setApplyJoinMethodFailedWithDataFailedWithMessage:msg);
    }];
    
}



/**
 设置管理员(单人)
 */
- (void)setManagerWithFamilyId:(NSString *)familyId uid:(NSString *)uid {
    
    [YPHttpRequestHelper setManagerWithFamilyId:familyId uid:uid Success:^(id data) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(setManagerSuccessWithData:), setManagerSuccessWithData:data);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(setManagerFailedWithDataFailedWithMessage:), setManagerFailedWithDataFailedWithMessage:msg);
    }];
    
}

/**
 设置管理员(多人)
 */
- (void)setManagerWithFamilyId:(NSString *)familyId uids:(NSString *)uids {
    
    [YPHttpRequestHelper setManagerWithFamilyId:familyId uids:uids Success:^(id data) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(setManagerSuccessWithData:), setManagerSuccessWithData:data);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(setManagerFailedWithDataFailedWithMessage:), setManagerFailedWithDataFailedWithMessage:msg);
    }];
    
}

/**
 移除管理员
 */
- (void)removeManagerWithFamilyId:(NSString *)familyId userID:(NSString *)userID {
    
    [YPHttpRequestHelper removeManagerWithFamilyId:familyId userId:userID Success:^(id data) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(removeManagerSuccessWithData:), removeManagerSuccessWithData:data);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(removeManagerFailedWithDataFailedWithMessage:), removeManagerFailedWithDataFailedWithMessage:msg);
    }];
    
}




/**
 踢出家族
 */
- (void)setOutWithFamilyId:(NSString *)familyId userID:(NSString *)userID {
    
    [YPHttpRequestHelper setOutWithFamilyId:familyId userId:userID Success:^(id data) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(setOutSuccessWithData:), setOutSuccessWithData:data);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(setOutMethodFailedWithDataFailedWithMessage:), setOutMethodFailedWithDataFailedWithMessage:msg);
    }];
    
}


/**
 家族消息
 
 @param familyId 家族id
 */
- (void)familyGetFamilyMessageWithFamilyId:(NSString *)familyId page:(NSInteger)page count:(NSInteger)count {
    
    [YPHttpRequestHelper familyGetFamilyMessageWithFamilyId:familyId page:page count:count success:^(NSArray *dataArr) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyGetFamilyMessageSuccessWithDataArr:), familyGetFamilyMessageSuccessWithDataArr:dataArr);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyGetFamilyMessageFailedWithMessage:), familyGetFamilyMessageFailedWithMessage:msg);
    }];
}

/**
  操作家族
 
 @param familyId 家族ID
 @param status 状态(1.同意,2.拒绝)
 */
- (void)familyApplyFamilyWithFamilyId:(NSString *)familyId
                               userId:(NSString *)userId
                                 type:(NSInteger)type
                               status:(NSInteger)status {
    
    [YPHttpRequestHelper familyApplyFamilyWithFamilyId:familyId userId:userId type:type status:status success:^{
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyApplyFamilySuccess), familyApplyFamilySuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyApplyFamilyFailedWithMessage:), familyApplyFamilyFailedWithMessage:msg);
    }];
}


/**
 家族首页banner
 */
- (void)homeV2FamilyBanner {
    
    [YPHttpRequestHelper homeV2FamilyBannerWithSuccess:^(NSArray *dataArr) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(homeV2FamilyBannerSuccessWithDataArr:), homeV2FamilyBannerSuccessWithDataArr:dataArr);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(homeV2FamilyBannerWithMessage:), homeV2FamilyBannerWithMessage:msg);
    }];
}

/**
 编辑家族信息
 
 @param familyId 家族ID
 @param logo logo
 @param backgroundImage backgroundImage
 @param notice 家族公告
 */
- (void)familyEditFamilyTeamWithFamilyId:(NSString *)familyId
                                    logo:(NSString *)logo
                         backgroundImage:(NSString *)backgroundImage
                                  notice:(NSString *)notice {
    [YPHttpRequestHelper familyEditFamilyTeamWithFamilyId:familyId logo:logo backgroundImage:backgroundImage notice:notice success:^{
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyEditFamilyTeamSuccess), familyEditFamilyTeamSuccess);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyEditFamilyTeamFailedWithMessage:), familyEditFamilyTeamFailedWithMessage:msg);
    }];
}

/**
 检测是否加入家族
 */
- (void)familyCheckFamilyJoin {
    [YPHttpRequestHelper familyCheckFamilyJoinSuccess:^(id data) {
        YPFamilyModel *model = [YPFamilyModel yy_modelWithJSON:data];
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyCheckFamilyJoinSuccess:), familyCheckFamilyJoinSuccess:model);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJFamilyCoreClient, @selector(familyCheckFamilyJoinFailedWithMessage:), familyCheckFamilyJoinFailedWithMessage:msg);
    }];
}

@end
