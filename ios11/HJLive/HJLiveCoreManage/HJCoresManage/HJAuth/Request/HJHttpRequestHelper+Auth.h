//
//  HJHttpRequestHelper+Auth.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"
#import "AccountInfo.h"
#import "TicketListInfo.h"



@interface HJHttpRequestHelper (Auth)

/**
 判断手机是否绑定
 
 @param success 成功
 @param failure 失败
 */
+ (void)judgeIsBindingPhoneWithsuccess:(void (^)(BOOL isbinding))success
                               failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 URL渠道统计

 @param url URL
 @param success 成功
 @param failure 失败
 */
+ (void)statisticsWithURL:(NSURL *)url
                  success:(void (^)(BOOL isSuccess))success
                  failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 通过微信openID登录

 @param openID 微信openID
 @param validateCode 易盾验证码
 @param success 成功
 @param failure 失败
 */
+ (void)loginWithWeChatOpenID:(NSString *)openID
                   andUnionID:(NSString *)unionID
                 access_token:(NSString *)access_token
                      andType:(XCThirdPartLoginType)type
                 validateCode:(NSString *)validateCode
                      success:(void (^)(AccountInfo *accountInfo))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 获取游客账号

 @param success 成功
 @param failure 失败
 */
+ (void)getVisitor:(void (^)(AccountInfo *accountInfo))success
           failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 注册

 @param phone 手机
 @param password 密码
 @param smsCode 验证码
 @param validateCode 易盾验证码
 @param success 成功
 @param failure 失败
 */
+ (void)regist:(NSString *)phone
      password:(NSString *)password
       smsCode:(NSString *)smsCode
  validateCode:(NSString *)validateCode
       success:(void (^)(void))success
       failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 登录
 
 @param phone 手机
 @param password 密码
 @param validateCode 易盾验证码
 @param success 成功
 @param failure 失败
 */
+ (void)login:(NSString *)phone
     password:(NSString *)password
 validateCode:(NSString *)validateCode
      success:(void (^)(AccountInfo *accountInfo))success
      failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 登出

 @param accessToken token
 @param success 成功
 @param failure 失败
 */
+ (void)logout:(NSString *)accessToken
       success:(void (^)(void))success
       failure:(void (^)(NSNumber *resCode, NSString *message))failure;



/**
 获取ticket
 
 @param accessToken token
 @param success 成功
 @param failure 失败
 */
+ (void)requestTicket:(NSString *)accessToken
              success:(void (^)(TicketListInfo *HJTicketInfo))success
              failure:(void (^)(NSNumber *resCode, NSString *message))failure;

+(void)requestConfirmCode:(NSString *)phone smsCode:(NSString *)smsCode success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure;

/**
 获取验证码

 @param phone 手机
 @param type 类型 1注册 2登录 3修改密码
 @param success 成功
 @param failure 失败
 */
+ (void)requestSmsCode:(NSString *)phone type:(NSNumber *)type
               success:(void (^)(void))success
               failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 设置密码

 @param phone 手机
 @param newPwd 密码
 @param smsCode 短信验证码
 @param validateCode 易盾验证码
 @param success 成功
 @param failure 失败
 */
+ (void)requestResetPwd:(NSString *)phone
                 newPwd:(NSString *)newPwd
                smsCode:(NSString *)smsCode
           validateCode:(NSString *)validateCode
                success:(void (^)(void))success
                failure:(void (^)(NSNumber *resCode, NSString *message))failure;


/**
 获取微信用户信息
 
 @param accessToken accessToken
 @param openId openId
 */
+ (void)getWechtUserInfoWithAccessToken:(NSString *)accessToken
                                 openId:(NSString *)openId
                                success:(void (^)(id))success
                                failure:(void (^)(NSURLSessionDataTask *, NSError *))failure;

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
                            failure:(void (^)(NSURLSessionDataTask *, NSError *))failure;

/**
 修改密码

 @param phone 手机
 @param pwd 密码
 @param newPwd 新密码
 @param success 成功
 @param failure 失败
 */
+ (void)requestModifyPwd:(NSString *)phone pwd:(NSString *)pwd newPwd:(NSString *)newPwd
                 success:(void (^)(void))success
                 failure:(void (^)(NSNumber *resCode, NSString *message))failure;

//检测是否设置过密码
+ (void)checkPwd:(void (^)(BOOL))success
         failure:(void (^)(NSNumber *, NSString *))failure;

+(void)validateCode:(NSString *)phone
            smsCode:(NSString *)smsCode
            success:(void (^)(void))success
            failure:(void (^)(NSNumber *, NSString *))failure;

//修改登录密码
+(void)requestResetPwd:(NSString *)password
                oldPwd:(NSString *)oldPwd
            confirmPwd:(NSString *)confirmPwd
               success:(void (^)(void))success
               failure:(void (^)(NSNumber *, NSString *))failure;

//设置登录密码
+(void)setPwd:(NSString *)phone
       newPwd:(NSString *)newPwd
      success:(void (^)(void))success
      failure:(void (^)(NSNumber *, NSString *))failure;

@end
