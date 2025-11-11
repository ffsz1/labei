//
//  YPHttpRequestHelper+Auth.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Auth.h"
#import "NSObject+YYModel.h"
#import "YPTicketListInfo.h"
#import "DESEncrypt.h"
#import "YPAuthCoreHelp.h"

@implementation YPHttpRequestHelper (Auth)
+ (void)getVisitor:(void (^)(YPAccountInfo *))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"acc/getvisitor";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        YPAccountInfo *accoutInfo = [YPAccountInfo yy_modelWithDictionary:data];
        if (accoutInfo != nil) {
            success(accoutInfo);
        } else {
            failure(nil, NSLocalizedString(HttpRequstUserError, nil));
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)regist:(NSString *)phone password:(NSString *)password smsCode:(NSString *)smsCode validateCode:(NSString *)validateCode success:(void (^)(void))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"acc/signup";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:phone forKey:@"phone"];
    [params setObject:phone forKey:@"username"];
    [params setObject:smsCode forKey:@"smsCode"];
    [params setObject:[DESEncrypt encryptUseDES:password key:JX_PWD_ENCRYPT_KEY] forKey:@"password"];
    [params setObject:JX_STR_AVOID_nil(validateCode) forKey:@"validate"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
            success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)login:(NSString *)phone password:(NSString *)password validateCode:(NSString *)validateCode success:(void (^)(YPAccountInfo *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"oauth/token";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:phone forKey:@"phone"];
    [params setObject:[DESEncrypt encryptUseDES:password key:JX_PWD_ENCRYPT_KEY] forKey:@"password"];
    [params setObject:@"uyzjdhds" forKey:@"client_secret"];
    [params setObject:@"password" forKey:@"grant_type"];
    [params setObject:@"erban-client" forKey:@"client_id"];
    [params setObject:@"1.0" forKey:@"version"];
    [params setObject:JX_STR_AVOID_nil(validateCode) forKey:@"validate"];

    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        YPAccountInfo *accoutInfo = [YPAccountInfo yy_modelWithDictionary:data];
        if (accoutInfo != nil) {
            success(accoutInfo);
        } else {
            failure(nil, NSLocalizedString(HttpRequstUserError, nil));
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
    
}

+ (void)logout:(NSString *)accessToken success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"acc/logout";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    if (accessToken.length == 0 || accessToken == nil) {
        return;
    }
    
    [params setObject:JX_STR_AVOID_nil(accessToken) forKey:@"access_token"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//微信登录
+ (void)loginWithWeChatOpenID:(NSString *)openID
                   andUnionID:(NSString *)unionID
                 access_token:(NSString *)access_token
                      andType:(XCThirdPartLoginType)type
                 validateCode:(NSString *)validateCode
                      success:(void (^)(YPAccountInfo *))success
                      failure:(void (^)(NSNumber *, NSString *))failure {
    
    NSString *method = @"acc/third/login";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    if (openID.length > 0) {
        [params setObject:openID forKey:@"openid"];
    }
    if (unionID.length > 0) {
        [params setObject:unionID forKey:@"unionid"];
    }
    [params safeSetObject:access_token forKey:@"access_token"];
    [params setObject:@(type) forKey:@"type"];
    [params setObject:JX_STR_AVOID_nil(validateCode) forKey:@"validate"];
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        YPAccountInfo *info = [YPAccountInfo yy_modelWithDictionary:data];
        success(info);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)requestTicket:(NSString *)accessToken
              success:(void (^)(YPTicketListInfo *ticketListInfo))success
              failure:(void (^)(NSNumber *resCode, NSString *message))failure;
{
    NSString *method = @"oauth/ticket";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@"multi" forKey:@"issue_type"];
    if (accessToken.length == 0 && accessToken == nil) {
        return;
    }
    [params setObject:accessToken forKey:@"access_token"];
#warning todo
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        YPTicketListInfo *listListInfo = [YPTicketListInfo yy_modelWithDictionary:data];

        if (listListInfo != nil && listListInfo.tickets.count > 0) {
            success(listListInfo);
        } else {
            failure(@(10), @"ticket为空");
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
    
    
//    [TestHttpRequestHelper testPostWithFullURL:@"http://beta.erbanyy.com/oauth/ticket" parameter:params success:^(id resposeObject) {
//        YPTicketListInfo *listListInfo = [YPTicketListInfo yy_modelWithDictionary:resposeObject[@"data"]];
//
//        if (listListInfo != nil && listListInfo.tickets.count > 0) {
//            success(listListInfo);
//        } else {
//            failure(@(10), @"ticket为空");
//        }
//    } failure:^(NSError *error) {
//        if (failure) {
//
//        }
//    }];
    
}

+ (void)requestSmsCode:(NSString *)phone type:(NSNumber *)type success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"acc/sms";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:phone forKey:@"phone"];
    [params setObject:type forKey:@"type"];

    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    if (uid.length > 0) {
        [params setObject:uid forKey:@"uid"];
    }
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];

    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+(void)requestResetPwd:(NSString *)phone newPwd:(NSString *)newPwd smsCode:(NSString *)smsCode validateCode:(NSString *)validateCode success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"acc/pwd/reset";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:phone forKey:@"phone"];
    [params setObject:newPwd forKey:@"newPwd"];
    [params setObject:[DESEncrypt encryptUseDES:newPwd key:JX_PWD_ENCRYPT_KEY] forKey:@"newPwd"];
    [params setObject:smsCode forKey:@"smsCode"];
    [params setObject:JX_STR_AVOID_nil(validateCode) forKey:@"validate"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+(void)requestConfirmCode:(NSString *)phone smsCode:(NSString *)smsCode success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"user/confirm";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:phone forKey:@"phone"];
    [params setObject:smsCode forKey:@"smsCode"];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    if (uid.length > 0) {
        [params setObject:uid forKey:@"uid"];
    }
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+(void)requestModifyPwd:(NSString *)phone pwd:(NSString *)pwd newPwd:(NSString *)newPwd success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
//    NSString *method = @"acc/pwd/modify";
//    NSMutableDictionary *params = [NSMutableDictionary dictionary];
//    [params setObject:phone forKey:@"phone"];
//    [params setObject:newPwd forKey:@"newPwd"];
//    [params setObject:smsCode forKey:@"smsCode"];
//    
//    [HttpRequestHelper POST:method params:params success:^(id data) {
//        success();
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
}


+ (void)statisticsWithURL:(NSURL *)url success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"statis/logininfo";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    [params setObject:uid forKey:@"uid"];
    NSString *urlStr = url.absoluteString;
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:urlStr forKey:@"url"];
    [params setObject:ticket forKey:@"ticket"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];

}

//判断是否绑定手机
+ (void)judgeIsBindingPhoneWithsuccess:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"user/isBindPhone";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    if (uid.length > 0) {
        [params setObject:uid forKey:@"uid"];
    }else {
        success(YES);
    }
    
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success(YES);
    } failure:^(NSNumber *resCode, NSString *message) {
        success(NO);
        failure(resCode, message);
    }];
}

//检测是否设置过密码
+ (void)checkPwd:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"user/checkPwd";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    [params setObject:uid forKey:@"uid"];
    
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success([data boolValue]);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}


+(void)validateCode:(NSString *)phone smsCode:(NSString *)smsCode success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"user/validateCode";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:phone forKey:@"phone"];
    [params setObject:smsCode forKey:@"code"];
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    if (uid.length > 0) {
        [params setObject:uid forKey:@"uid"];
    }
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+(void)requestResetPwd:(NSString *)password oldPwd:(NSString *)oldPwd confirmPwd:(NSString *)confirmPwd success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"user/modifyPwd";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    
    [params setObject:password forKey:@"password"];
    [params setObject:oldPwd forKey:@"oldPwd"];
    [params setObject:confirmPwd forKey:@"confirmPwd"];
    
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}


+(void)setPwd:(NSString *)phone newPwd:(NSString *)newPwd success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    NSString *method = @"user/setPwd";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:phone forKey:@"phone"];
    [params setObject:newPwd forKey:@"password"];
    [params setObject:newPwd forKey:@"confirmPwd"];
    
    
    
    
    NSString *uid = [GetCore(YPAuthCoreHelp) getUid];
    [params setObject:uid forKey:@"uid"];
    NSString *ticket = [GetCore(YPAuthCoreHelp) getTicket];
    [params setObject:ticket forKey:@"ticket"];
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success();
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

/**
 获取微信用户信息
 
 @param accessToken accessToken
 @param openId openId
 */
+ (void)getWechtUserInfoWithAccessToken:(NSString *)accessToken
                                 openId:(NSString *)openId
                                success:(void (^)(id))success
                                failure:(void (^)(NSURLSessionDataTask *, NSError *))failure {
    NSString *method = @"https://api.weixin.qq.com/sns/userinfo";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"access_token"] = accessToken;
    params[@"openid"] = openId;
    
    [YPHttpRequestHelper OrignGET:method params:params success:^(id data) {
        if (success) {
            success(data);
        }
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        if (failure) {
            failure(task,error);
        }
    }];
}

/**
 获取微信登录AccessToken
 
 @param code 填写第一步获取的code参数
 @param appId appid
 @param appSecret AppSecret
 */
+ (void)getWechtAccessTokenWithCode:(NSString *)code
                              appId:(NSString *)appId
                          appSecret:(NSString *)appSecret
                            success:(void (^)(id))success
                            failure:(void (^)(NSURLSessionDataTask *, NSError *))failure {
    
    NSString *method = @"https://api.weixin.qq.com/sns/oauth2/access_token";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"appid"] = appId;
    params[@"secret"] = appSecret;
    params[@"code"] = code;
    params[@"grant_type"] = @"authorization_code";
    
    
    [YPHttpRequestHelper OrignGET:method params:params success:^(id data) {
        if (success) {
            success(data);
        }
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        if (failure) {
            failure(task,error);
        }
    }];
}

@end
