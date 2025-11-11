//
//  HJHttpRequestHelper.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"
#import "YYHttpClient.h"
#import "ErrorInfo.h"
#import "NSObject+YYModel.h"
#import "HJHttpErrorClient.h"
#import "HJBalanceErrorClient.h"
#import "HJAuthCoreHelp.h"
#import <AFNetworkReachabilityManager.h>
#import "HTTPRequestConfiguration.h"
//#import <JXCategories/JXCategories.h>
#import "JXCategories.h"
#import "NSData+JXEncryptAndDecrypt.h"
#import <YYCategories/NSDictionary+YYAdd.h>

static dispatch_once_t onceToken;

typedef NS_ENUM(NSInteger, HttpResponseResult) {
    HttpResponseSuccess   = 200,
    HttpResponseIMSuccess = 0,
    //TODO
};


static YYHttpClient *httpClient() {
    static YYHttpClient *client = nil;
    
    dispatch_once(&onceToken, ^{
        NSString *url;
//#if !OFFICIAL_RELEASE
//        url = TEST_ENV_HOST;
//#else
//        url = RELEASE_ENV_HOST;
//#endif
//        NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
//        if ([[userDefault objectForKey:EnvID] isEqualToString:@"1"]) {
//            url = RELEASE_ENV_HOST;
//        } else if ([[userDefault objectForKey:EnvID] isEqualToString:@"0"]){
//            url = TEST_ENV_HOST;
//        }

        NSURL *baseURL = [NSURL URLWithString:[HJHttpRequestHelper getHostUrl]];
        client = [[YYHttpClient alloc] initWithBaseURL:baseURL];
    });
    return client;
}

static HTTPRequestConfiguration *HTTPConfiguration() {
    static HTTPRequestConfiguration *buffer = nil;
    static dispatch_once_t configurationOnceToken;
    dispatch_once(&configurationOnceToken, ^{
        buffer = [[HTTPRequestConfiguration alloc] init];
    });
    return buffer;
}

static dispatch_once_t orignOnceToken;
static YYHttpClient *orignHttpClient() {
    static YYHttpClient *client = nil;
    
    dispatch_once(&orignOnceToken, ^{
        NSURL *baseURL = [NSURL URLWithString:@""];
        client = [[YYHttpClient alloc] initWithBaseURL:baseURL];
    });
    return client;
}

static NSString * const kResponseResultKey = @"code";
static NSString * const kResponseDataKey = @"data";
static NSString * const kResponseMessageKey = @"message";

static NSString * const kIMResponseCodeKey = @"errno";
static NSString * const kIMResponseDataKey = @"data";
static NSString * const kIMResponseMessageKey = @"errmsg";

static NSString * const kResponseDataCryptKey = @"ed";
@implementation HJHttpRequestHelper
+ (NSString *)getHostUrl {
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *ID = [[userDefaults objectForKey:EnvID] description];
    if (ID.length && ([ID isEqualToString:@"0"] || [ID isEqualToString:@"1"])) {
        if ([ID isEqualToString:@"0"]) {
            return JX_HOST_URL_DEBUG;
        } else {
            return JX_HOST_URL_RELEASE;
        }
    }
    else {
#ifdef DEBUG
        [userDefaults setObject:@"0" forKey:EnvID]; //设置默认环境为证实环境 0测试 1正式
        return JX_HOST_URL_DEBUG;
#else
        [userDefaults setObject:@"1" forKey:EnvID]; //设置默认环境为证实环境 0测试 1正式
        return JX_HOST_URL_RELEASE;
#endif
    }
    
}

+ (NSString *)getPicHostUrl {
    return JX_IMAGE_HOST_URL;
}


+ (void)resetClient {
    onceToken = 0;
    [GetCore(HJAuthCoreHelp) logout];
}

+ (void)OrignGET:(NSString *)method
          params:(NSDictionary *)params
         success:(void (^)(id))success
         failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
    if ([AFNetworkReachabilityManager sharedManager].networkReachabilityStatus == 0) {
        NotifyCoreClient(HJHttpErrorClient, @selector(networkDisconnect), networkDisconnect);
    }
    [orignHttpClient() OrignGET:method parameters:params success:^(NSURLSessionDataTask *task, id responseObject) {
        
        if (success) {
            success(responseObject);
        }
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        if (failure) {
            failure(task,error);
        }
    }];
}

+ (void)OrignPOST:(NSString *)method
           params:(NSDictionary *)params
          success:(void (^)(id data))success
          failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
    
    
    if ([AFNetworkReachabilityManager sharedManager].networkReachabilityStatus == 0) {
        NotifyCoreClient(HJHttpErrorClient, @selector(networkDisconnect), networkDisconnect);
    }
    
    [orignHttpClient() POST:method parameters:[self encryptFromData:params] success:^(NSURLSessionDataTask *task, id responseObject) {
        if (success) {
            success(responseObject);
        }
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        if (failure) {
            failure(task,error);
        }
    }];
}

+ (void)GET:(NSString *)method
     params:(NSDictionary *)params
    success:(void (^)(id data))success
    failure:(void (^)(NSNumber *resCode, NSString *message))failure
{
    if ([AFNetworkReachabilityManager sharedManager].networkReachabilityStatus == 0) {
        NotifyCoreClient(HJHttpErrorClient, @selector(networkDisconnect), networkDisconnect);
    }
    
    @weakify(self);
    [httpClient() GET:method parameters:params parametersConstructionBlock:^NSDictionary *(NSDictionary *parameters) {
        @strongify(self);
        return [self encryptFromData:parameters];
    } success:^(NSURLSessionDataTask *task, id aResponseObject) {
        id responseObject = [self decryptFromData:aResponseObject];
//        [YYLogger info:@"get请求成功" message:@"method:%@--params:%@--responseObject:%@",method,params,responseObject];
        NSNumber *resCode = [responseObject valueForKey:kResponseResultKey];
        if ([resCode integerValue] == HttpResponseSuccess) {
            id data = [responseObject valueForKey:kResponseDataKey];
            if (success) {
                success(data);
            }
        }else {
            NSString *message = [responseObject valueForKey:kResponseMessageKey];
            [HJHttpRequestHelper logWithTask:task code:resCode.integerValue message:message error:nil];
            if (failure) {
                if (![method isEqualToString:@"user/isBindPhone"] && ![method isEqualToString:@"gift/sendV3"] && ![method isEqualToString:@"room/rcmd/get"] && ![method isEqualToString:@"room/pkvote/get"]&&![method isEqualToString:@"version/get"]) {
                    NotifyCoreClient(HJHttpErrorClient, @selector(requestFailureWithMsg:), requestFailureWithMsg:message);
                }
                failure(resCode, message);
            }
        }
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = [error.userInfo objectForKey:@"com.alamofire.serialization.response.error.data"];
        NSString *info = [[NSString alloc] initWithData:errorData encoding:NSUTF8StringEncoding];
        ErrorInfo *errorInfo = [ErrorInfo yy_modelWithJSON:info];
        [HJHttpRequestHelper logWithTask:task code:errorInfo.code.integerValue message:errorInfo.message error:error];
        if (errorInfo != nil) {
            NSInteger code = errorInfo.code.integerValue;
            NSString *message = @"";
            if (code == 107) {
                message =  NSLocalizedString(HttpRequstPswWrong, nil);
            } else if (code == 109) {
                message = NSLocalizedString(HttpRequstUserNotExit, nil);
            } else if (code == 110) {
                message = NSLocalizedString(HttpRequstUserLocked, nil);
            } else if (code == 111) {
                message = NSLocalizedString(HttpRequstPswWrongOrNotExit, nil);
            } else if (code == 150) {
                message = NSLocalizedString(HttpRequstVelifyWrong, nil);
            } else if (code == 151) {
                message = NSLocalizedString(HttpRequstPhoneUnUsed, nil);
            } else if (code == 153) {
                message = NSLocalizedString(HttpRequstReSetPswFailed, nil);
            } else if (code == 161) {
                message = NSLocalizedString(HttpRequstNickNameUnUsed, nil);
            } else {
                message = NSLocalizedString(HttpRequstServeiceFailed, nil);
            }
            if (![method isEqualToString:@"user/isBindPhone"] && ![method isEqualToString:@"gift/sendV3"] && ![method isEqualToString:@"room/rcmd/get"] && ![method isEqualToString:@"room/pkvote/get"]) {
                NotifyCoreClient(HJHttpErrorClient, @selector(requestFailureWithMsg:), requestFailureWithMsg:message);
            }
            failure(errorInfo.code, message);
        } else {
            if (error.code == -1009) {
                failure(nil, NSLocalizedString(HttpRequstServeiceFailedAndTry, nil));
            } else {
                failure(nil, [NSString stringWithFormat:@"%@%@",NSLocalizedString(HttpRequstServeiceWrong, nil) , error.localizedDescription]);
            }
        }
    }];
}

+ (void)POST:(NSString *)method
      params:(NSDictionary *)params
     success:(void (^)(id data))success
     failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    if ([AFNetworkReachabilityManager sharedManager].networkReachabilityStatus == 0) {
        NotifyCoreClient(HJHttpErrorClient, @selector(networkDisconnect), networkDisconnect);
    }

    @weakify(self);
    [httpClient() POST:method parameters:params parametersConstructionBlock:^NSDictionary *(NSDictionary *parameters) {
        @strongify(self);
        return [self encryptFromData:parameters];
    } success:^(NSURLSessionDataTask *task, id aResponseObject) {
        id responseObject = [self decryptFromData:aResponseObject];
//        [YYLogger info:@"post请求成功" message:@"method:%@--params:%@--responseObject:%@",method,params,responseObject];
        NSNumber *resCode = [responseObject valueForKey:kResponseResultKey];
        if ([resCode integerValue] == HttpResponseSuccess) {
            id data = [responseObject valueForKey:kResponseDataKey];
            if (success) {
                success(data);
            }
        }else {
            NSString *message = [responseObject valueForKey:kResponseMessageKey];
            [HJHttpRequestHelper logWithTask:task code:resCode.integerValue message:message error:nil];
            if (resCode.longValue == 2103) {
                NotifyCoreClient(HJBalanceErrorClient, @selector(onBalanceNotEnough), onBalanceNotEnough);
            }
            if (failure) {
                if (![method isEqualToString:@"user/isBindPhone"] && ![method isEqualToString:@"gift/sendV3"] && ![method isEqualToString:@"room/rcmd/get"] && ![method isEqualToString:@"room/pkvote/get"]) {
                    NotifyCoreClient(HJHttpErrorClient, @selector(requestFailureWithMsg:), requestFailureWithMsg:message);
                }
                failure(resCode, message);
            }
        }
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = [error.userInfo objectForKey:@"com.alamofire.serialization.response.error.data"];
        NSString *info = [[NSString alloc] initWithData:errorData encoding:NSUTF8StringEncoding];
        ErrorInfo *errorInfo = [ErrorInfo yy_modelWithJSON:info];
        [HJHttpRequestHelper logWithTask:task code:errorInfo.code.integerValue message:errorInfo.message error:error];
        if (errorInfo != nil) {
            NSInteger code = errorInfo.code.integerValue;
            NSString *message = @"";
            if (code == 107 || code == 109 || code == 111) {
                message =  NSLocalizedString(HttpRequstPswWrongOrNotExit, nil);
            }  else if (code == 110) {
                message = NSLocalizedString(HttpRequstUserLocked, nil);
            }  else if (code == 150) {
                message = NSLocalizedString(HttpRequstVelifyWrong, nil);
            } else if (code == 151) {
                message = NSLocalizedString(HttpRequstPhoneUnUsed, nil);
            } else if (code == 153) {
                message = NSLocalizedString(HttpRequstReSetPswFailed, nil);
            } else if (code == 161) {
                message = NSLocalizedString(HttpRequstNickNameUnUsed, nil);
            } else if (code == 401) {
                message = [NSString stringWithFormat:@"ticket %@",NSLocalizedString(HttpRequstOutLine, nil)];
                NotifyCoreClient(HJHttpErrorClient, @selector(onTicketInvalid), onTicketInvalid);
                failure(errorInfo.code, message);
                return;
            } else {
                message = NSLocalizedString(HttpRequstServeiceFailed, nil);
            }
            if (![method isEqualToString:@"user/isBindPhone"] && ![method isEqualToString:@"gift/sendV3"] && ![method isEqualToString:@"room/rcmd/get"] && ![method isEqualToString:@"room/pkvote/get"]) {
                NotifyCoreClient(HJHttpErrorClient, @selector(requestFailureWithMsg:), requestFailureWithMsg:message);
            }
            failure(errorInfo.code, message);
        } else {
            if (error.code == -1009) {
                NSLog(@"%@",error.description);
                [YYLogger info:@"request-error--->" message:@"%@",error.description];
                failure(nil, NSLocalizedString(HttpRequstServeiceFailedAndTry, nil));
            } else {
                failure(nil, [NSString stringWithFormat:@"%@%@",NSLocalizedString(HttpRequstServeiceWrong, nil), error.localizedDescription]);
            }
        }
        
    }];
}

///< IM用
+ (void)IM_GET:(NSString *)method
        params:(NSDictionary *)params
       success:(void (^)(id data))success
       failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    if ([AFNetworkReachabilityManager sharedManager].networkReachabilityStatus == 0) {
        NotifyCoreClient(HJHttpErrorClient, @selector(networkDisconnect), networkDisconnect);
    }
    
    @weakify(self);
    [httpClient() GET:method parameters:params parametersConstructionBlock:^NSDictionary *(NSDictionary *parameters) {
        @strongify(self);
        return [self encryptFromData:parameters];
    } success:^(NSURLSessionDataTask *task, id aResponseObject) {
        id responseObject = [self decryptFromData:aResponseObject];
        //        [YYLogger info:@"get请求成功" message:@"method:%@--params:%@--responseObject:%@",method,params,responseObject];
        NSNumber *resCode = [responseObject valueForKey:kIMResponseCodeKey];
        if ([resCode integerValue] == HttpResponseIMSuccess) {
            id data = [responseObject valueForKey:kIMResponseDataKey];
            if (success) {
                success(data);
            }
        }else {
            if (failure) {
                NSString *message = [responseObject valueForKey:kResponseMessageKey];
                NotifyCoreClient(HJHttpErrorClient, @selector(requestFailureWithMsg:), requestFailureWithMsg:message);
                failure(resCode, message);
                [HJHttpRequestHelper logWithTask:task code:resCode.integerValue message:message error:nil];
            }
        }
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = [error.userInfo objectForKey:@"com.alamofire.serialization.response.error.data"];
        NSString *info = [[NSString alloc] initWithData:errorData encoding:NSUTF8StringEncoding];
        ErrorInfo *errorInfo = [ErrorInfo yy_modelWithJSON:info];
        [HJHttpRequestHelper logWithTask:task code:errorInfo.code.integerValue message:errorInfo.message error:error];
        if (errorInfo != nil) {
            NSInteger code = errorInfo.code.integerValue;
            NSString *message = @"";
            if (code == 107) {
                message =  NSLocalizedString(HttpRequstPswWrong, nil);
            } else if (code == 109) {
                message = NSLocalizedString(HttpRequstUserNotExit, nil);
            } else if (code == 110) {
                message = NSLocalizedString(HttpRequstUserLocked, nil);
            } else if (code == 111) {
                message = NSLocalizedString(HttpRequstPswWrongOrNotExit, nil);
            } else if (code == 150) {
                message = NSLocalizedString(HttpRequstVelifyWrong, nil);
            } else if (code == 151) {
                message = NSLocalizedString(HttpRequstPhoneUnUsed, nil);
            } else if (code == 153) {
                message = NSLocalizedString(HttpRequstReSetPswFailed, nil);
            } else if (code == 161) {
                message = NSLocalizedString(HttpRequstNickNameUnUsed, nil);
            } else {
                message = NSLocalizedString(HttpRequstServeiceFailed, nil);
            }
            if (![method isEqualToString:@"user/isBindPhone"] && ![method isEqualToString:@"gift/sendV3"] && ![method isEqualToString:@"room/rcmd/get"] && ![method isEqualToString:@"room/pkvote/get"]) {
                NotifyCoreClient(HJHttpErrorClient, @selector(requestFailureWithMsg:), requestFailureWithMsg:message);
            }
            failure(errorInfo.code, message);
        } else {
            if (error.code == -1009) {
                failure(nil, NSLocalizedString(HttpRequstServeiceFailedAndTry, nil));
            } else {
                failure(nil, [NSString stringWithFormat:@"%@%@",NSLocalizedString(HttpRequstServeiceWrong, nil) , error.localizedDescription]);
            }
        }
    }];
}

///< IM用
+ (void)IM_POST:(NSString *)method
         params:(NSDictionary *)params
        success:(void (^)(id data))success
        failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    if ([AFNetworkReachabilityManager sharedManager].networkReachabilityStatus == 0) {
        NotifyCoreClient(HJHttpErrorClient, @selector(networkDisconnect), networkDisconnect);
    }
    
    @weakify(self);
    [httpClient() POST:method parameters:params parametersConstructionBlock:^NSDictionary *(NSDictionary *parameters) {
        @strongify(self);
        return [self encryptFromData:parameters];
    } success:^(NSURLSessionDataTask *task, id aResponseObject) {
        id responseObject = [self decryptFromData:aResponseObject];
        
        NSNumber *resCode = [responseObject valueForKey:kIMResponseCodeKey];
        if ([resCode integerValue] == HttpResponseIMSuccess) {
            id data = [responseObject valueForKey:kIMResponseDataKey];
            if (success) {
                success(data);
            }
        }else {
            if (failure) {
                NSString *message = [responseObject valueForKey:kIMResponseMessageKey];
                NotifyCoreClient(HJHttpErrorClient, @selector(requestFailureWithMsg:), requestFailureWithMsg:message);
                failure(resCode, message);
                [HJHttpRequestHelper logWithTask:task code:resCode.integerValue message:message error:nil];
            }
        }
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = [error.userInfo objectForKey:@"com.alamofire.serialization.response.error.data"];
        NSString *info = [[NSString alloc] initWithData:errorData encoding:NSUTF8StringEncoding];
        ErrorInfo *errorInfo = [ErrorInfo yy_modelWithJSON:info];
        [HJHttpRequestHelper logWithTask:task code:errorInfo.code.integerValue message:errorInfo.message error:error];
        if (errorInfo != nil) {
            NSInteger code = errorInfo.code.integerValue;
            NSString *message = @"";
            if (code == 107 || code == 109 || code == 111) {
                message =  NSLocalizedString(HttpRequstPswWrongOrNotExit, nil);
            }  else if (code == 110) {
                message = NSLocalizedString(HttpRequstUserLocked, nil);
            }  else if (code == 150) {
                message = NSLocalizedString(HttpRequstVelifyWrong, nil);
            } else if (code == 151) {
                message = NSLocalizedString(HttpRequstPhoneUnUsed, nil);
            } else if (code == 153) {
                message = NSLocalizedString(HttpRequstReSetPswFailed, nil);
            } else if (code == 161) {
                message = NSLocalizedString(HttpRequstNickNameUnUsed, nil);
            } else if (code == 401) {
                message = [NSString stringWithFormat:@"ticket %@",NSLocalizedString(HttpRequstOutLine, nil)];
                NotifyCoreClient(HJHttpErrorClient, @selector(onTicketInvalid), onTicketInvalid);
                failure(errorInfo.code, message);
                return;
            } else {
                message = NSLocalizedString(HttpRequstServeiceFailed, nil);
            }
            if (![method isEqualToString:@"user/isBindPhone"] && ![method isEqualToString:@"gift/sendV3"] && ![method isEqualToString:@"room/rcmd/get"] && ![method isEqualToString:@"room/pkvote/get"]) {
                NotifyCoreClient(HJHttpErrorClient, @selector(requestFailureWithMsg:), requestFailureWithMsg:message);
            }
            failure(errorInfo.code, message);
        } else {
            if (error.code == -1009) {
                NSLog(@"%@",error.description);
                failure(nil, NSLocalizedString(HttpRequstServeiceFailedAndTry, nil));
            } else {
                failure(nil, [NSString stringWithFormat:@"%@%@",NSLocalizedString(HttpRequstServeiceWrong, nil), error.localizedDescription]);
            }
        }
        
    }];
}

+ (void)logWithTask:(NSURLSessionDataTask *)task code:(NSInteger)code message:(NSString *)message error:(NSError *)error {
    [YYLogger info:@"request-error--->" message:@"uid:%@ host:%@ body:%@ code:%ld message:%@ error:%@", JX_STR_AVOID_nil(GetCore(HJAuthCoreHelp).getUid), JX_STR_AVOID_nil(task.originalRequest.URL.absoluteString), JX_STR_AVOID_nil(task.originalRequest.HTTPBody.utf8String), code,  JX_STR_AVOID_nil(message), JX_STR_AVOID_nil(error.description)];
}

+ (NSDictionary *)encryptFromData:(id)dic {
    if (HTTPConfiguration().encryptType == JXCryptAlgorithmAES) {
        NSString *jsonString = [dic yy_modelToJSONString];
        NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
        data = [data jx_AES256EncryptWithKey:[HTTPConfiguration().encryptKey dataUsingEncoding:NSUTF8StringEncoding] iv:[HTTPConfiguration().encryptIv dataUsingEncoding:NSUTF8StringEncoding]];
        NSString *encryptString = [data base64EncodedString];
        NSDictionary *buffer = @{
                                 kResponseDataCryptKey : JX_STR_AVOID_nil(encryptString),
                                 };
        return buffer;
    } else {
        return dic;
    }
}

+ (id)decryptFromData:(id)data {
    if (HTTPConfiguration().decryptType == JXCryptAlgorithmAES) {
        NSDictionary *buffer = [JSONTools ll_dictionaryWithJSON:data];
        if (![buffer.allKeys containsObject:kResponseDataCryptKey]) return data;
        
        NSString *encryptString = buffer[kResponseDataCryptKey];
        NSData *data = [NSData dataWithBase64EncodedString:encryptString];
        data = [data jx_AES256DecryptWithKey:[HTTPConfiguration().decryptKey dataUsingEncoding:NSUTF8StringEncoding] iv:[HTTPConfiguration().decryptIv dataUsingEncoding:NSUTF8StringEncoding]];
        return [data jsonValueDecoded];
    } else {
        return data;
    }
}

static BOOL shoudShowErrorHUD = YES;
+ (BOOL)shoudShowErrorHUD {
    return shoudShowErrorHUD;
}

+ (void)setupShouldShowErrorHUD:(BOOL)flag {
    shoudShowErrorHUD = flag;
}

@end
