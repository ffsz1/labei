//
//  HJHttpRequestHelper+Family.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Family.h"

#import "HJFamilyModel.h"
#import "HJFamilyMessage.h"
#import "HJBannerInfo.h"
#import "NSObject+YYModel.h"
#import "HJFamilyMemberModel.h"

@implementation HJHttpRequestHelper (Family)

/**
 获取家族列表

 @param familyId 家族id （为空默认全部）
 */
+ (void)familyGetListWithFamilyId:(NSString *)familyId page:(NSInteger)page success:(void (^)(NSMutableDictionary *data))success failure:(void (^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"family/getList";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    params[@"current"] = [NSNumber numberWithInteger:page];
    params[@"pageSize"] = [NSNumber numberWithInteger:20];
    
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *dataArr = [NSArray yy_modelArrayWithClass:[HJFamilyModel class] json:data[@"familyList"]];
        HJFamilyModel *model = [HJFamilyModel yy_modelWithJSON:data[@"familyTeam"]];
        NSMutableDictionary *dataDic = [NSMutableDictionary dictionary];
        dataDic[@"familyList"] = dataArr;
        dataDic[@"familyTeam"] = model;
        if (success) {
            success(dataDic);
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
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
+ (void)familyCreateFamilyTeamWithBgImg:(NSString *)bgImg
                                   logo:(NSString *)logo
                                   name:(NSString *)name
                                 notice:(NSString *)notice
                                   hall:(NSString *)hall
                                success:(void (^)())success
                                failure:(void (^)(NSNumber *code, NSString *msg))failure {
    NSString *method = @"family/createFamilyTeam";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    params[@"bgImg"] = bgImg;
    params[@"logo"] = logo;
    params[@"name"] = name;
    params[@"notice"] = notice;
    params[@"hall"] = hall;
    
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
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
 获取加入家族信息
 */
+ (void)familyGetJoinFamilyInfoWithSuccess:(void (^)(id data))success
                                   failure:(void (^)(NSNumber *code, NSString *msg))failure {
    NSString *method = @"family/getJoinFamilyInfo";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSLog(@"%@",data);
        HJFamilyInfoDetail *model = [HJFamilyInfoDetail yy_modelWithJSON:data];
        
        if (success) {
            success(model);
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
    
}

/**
 获取家族成员信息
 */
+ (void)familyGetFamilyTeamJoinWithFamilyId:(NSString *)familyId
                                       page:(NSInteger)page
                                    success:(void (^)(id data))success
                                    failure:(void (^)(NSNumber *code, NSString *msg))failure {
    NSString *method = @"family/getFamilyTeamJoin";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = [NSNumber numberWithInteger:[familyId integerValue]];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    params[@"current"] = [NSNumber numberWithInteger:page];
    params[@"pageSize"] = [NSNumber numberWithInteger:20];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *tmpArr = [NSArray yy_modelArrayWithClass:[HJFamilyInfoDetail class] json:[data objectForKey:@"familyTeamJoinDTOS"]];
        
        HJFamilyMemberModel *model = [HJFamilyMemberModel yy_modelWithJSON:data];
        model.familyTeamJoinDTOS = tmpArr;
        
        if (success) {
            success(model);
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
    
}

/**
 申请加入家族
 
 @param familyId 家族id
 */
+ (void)applyJoinFamilyWithFamilyId:(NSString *)familyId
                            Success:(void (^)(id data))success
                            failure:(void (^)(NSNumber *code, NSString *msg))failure
{
    NSString *method = @"family/applyJoinFamilyTeam";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode,message);
        }
    }];
}

/**
 申请退出家族
 
 @param familyId 家族id
 */
+ (void)applyExitFamilyWithFamilyId:(NSString *)familyId
                            Success:(void (^)(id data))success
                            failure:(void (^)(NSNumber *code, NSString *msg))failure
{
    NSString *method = @"family/applyExitTeam";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode,message);
        }
    }];
}

/**
 设置申请加入方式
 
 @param familyId 家族id
 */
+ (void)setApplyJoinMethodWithFamilyId:(NSString *)familyId
                              joinmode:(NSString *)joinmode
                            Success:(void (^)(id data))success
                            failure:(void (^)(NSNumber *code, NSString *msg))failure
{
    NSString *method = @"family/setApplyJoinMethod";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"joinmode"] = joinmode;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode,message);
        }
    }];
}

/**
 设置消息提醒
 
 @param familyId 家族id
 @param ope 1：关闭消息提醒，2：打开消息提醒，其他值无效

 */
+ (void)setMsgNotifyWithFamilyId:(NSString *)familyId
                             ope:(NSInteger)ope
                         Success:(void (^)(id data))success
                         failure:(void (^)(NSNumber *code, NSString *msg))failure
{
    NSString *method = @"family/setMsgNotify";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"ope"] = [NSNumber numberWithInteger:ope];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode,message);
        }
    }];
}

/**
 设置管理员(单人)
 
 @param familyId 家族id
 @param uId 成员uid
 
 */
+ (void)setManagerWithFamilyId:(NSString *)familyId
                           uid:(NSString *)uid
                       Success:(void (^)(id data))success
                       failure:(void (^)(NSNumber *code, NSString *msg))failure {
    NSString *method = @"family/setupAdministrator";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = uid;
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode,message);
        }
    }];
}

/**
 设置管理员(多人)
 
 @param familyId 家族id
 @param uIds 成员uids

 */
+ (void)setManagerWithFamilyId:(NSString *)familyId
                          uids:(NSString *)uids
                       Success:(void (^)(id data))success
                       failure:(void (^)(NSNumber *code, NSString *msg))failure
{
    NSString *method = @"family/setupAdministrator";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userIds"] = uids;
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode,message);
        }
    }];
}


/**
移除管理员
 
 @param familyId 家族id
 @param userId 成员id
 
 */
+ (void)removeManagerWithFamilyId:(NSString *)familyId
                        userId:(NSString *)userId
                       Success:(void (^)(id data))success
                       failure:(void (^)(NSNumber *code, NSString *msg))failure
{
    NSString *method = @"family/removeAdmin";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = userId;
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode,message);
        }
    }];
}



/**
 踢出家族
 
 @param familyId 家族id
 @param userId 成员id
 
 */
+ (void)setOutWithFamilyId:(NSString *)familyId
                    userId:(NSString *)userId
                   Success:(void (^)(id data))success
                   failure:(void (^)(NSNumber *code, NSString *msg))failure
{
    NSString *method = @"family/kickOutTeam";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = userId;
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode,message);
        }
    }];
}





/**
 家族消息

 @param familyId 家族id
 */
+ (void)familyGetFamilyMessageWithFamilyId:(NSString *)familyId
                                      page:(NSInteger)page
                                     count:(NSInteger)count
                                   success:(void (^)(NSArray *dataArr))success
                                   failure:(void (^)(NSNumber *code, NSString *msg))failure {
    NSString *method = @"family/getFamilyMessage";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"current"] = @(page);
    params[@"pageSize"] = @(count);
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *dataArr = [NSArray yy_modelArrayWithClass:[HJFamilyMessage class] json:data];
        if (success) {
            success(dataArr);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (failure) {
            failure(resCode,message);
        }
    }];
}


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
                              failure:(void (^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"family/applyFamily";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"status"] = @(status);
    params[@"type"] = @(type);
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = userId;
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
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
 家族首页banner
 */
+ (void)homeV2FamilyBannerWithSuccess:(void (^)(NSArray *dataArr))success
                              failure:(void (^)(NSNumber *code, NSString *msg))failure {
    
    NSString *method = @"home/v2/familyBanner";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *arr = [NSArray yy_modelArrayWithClass:[HJBannerInfo class] json:data];
        
        if (success) {
            success(arr);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}


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
                                 failure:(void (^)(NSNumber *code, NSString *msg))failure {
    NSString *method = @"family/editFamilyTeam";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"logo"] = logo;
    params[@"bgimg"] = backgroundImage;
    params[@"notice"] = notice;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"userId"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
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
 获取家族信息
 */
+ (void)familyGetFamilyInfoWithFamilyId:(NSString *)familyId
                                success:(void (^)(id data))success
                                failure:(void (^)(NSNumber *code, NSString *msg))failure {
    NSString *method = @"family/getFamilyInfo";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"familyId"] = familyId;
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}

+ (void)familyCheckFamilyJoinSuccess:(void (^)(id data))success
                             failure:(void (^)(NSNumber *code, NSString *msg))failure {
    NSString *method = @"family/checkFamilyJoin";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"userId"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    params[@"ticket"] = [GetCore(HJAuthCoreHelp) getTicket];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        if (success) {
            success(data);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}

@end
