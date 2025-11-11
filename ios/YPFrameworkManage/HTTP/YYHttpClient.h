//
//  YYHttpClient.h
//  YYMobileCore
//
//  Created by wuwei on 14/6/11.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AFURLResponseSerialization.h"
#import "AFURLRequestSerialization.h"

typedef NSDictionary * (^BasicParameterConstructor)();

NS_AVAILABLE_IOS(7_0) @interface YYHttpClient : NSObject

@property (nonatomic, strong, readonly) NSURL *baseURL;
@property (nonatomic, assign) BOOL didAppLaunched; //对sharedClient标识App已启动， 默认为NO

/**
 *  Basic Parameter Constructor
 *  By default, the basic parameter constructor returns a dictionary like:
 *  {
 *      "os"        : "iOS",
 *      "osVersion" : "7.1.1",
 *      "yyVersion" : "3.0.0",
 *      "ispType"   : 2,    // 0: Unknown, 1: CMCC, 2: Unicom, 3: Telecom 0xFFFF: Otherwise
 *      "netType"   : 1,    // 0: Unknown, 1: Cellular, 2: Wi-Fi
 *  }
 *  
 *  Basic Parameter will be automaticlly added to the GET/POST/PUT/PATCH/DELETE request
 */
@property (nonatomic, strong) BasicParameterConstructor basicParameterConstructor;


@property (nonatomic, strong) AFHTTPRequestSerializer<AFURLRequestSerialization> *requestSerializer;

/**
 *  设置YYHttpClient的responseSerializer, 它将用于解析返回数据
 *  如果不设置, 默认为一个YYJSONResponseSerializer对象
 *
 *  @Warning 不要对sharedClient进行设置
 */
@property (nonatomic, strong) AFHTTPResponseSerializer<AFURLResponseSerialization> *responseSerializer;

/**
 *  YYHttpClient单例对象
 */
+ (instancetype)sharedClient;

/**
 *  用空的baseURL创建一个`YYHttpClient`
 *
 *  @return 初始化完成的`YYHttpClient`对象
 */
+ (instancetype)instantiateHttpClient;

/**
 *  The designated initializer
 *  用参数指定的baseURL创建一个`YYHttpClient`
 *  该baseURL用于构建所有该Client发出请求的完整URL
 *
 *  @param baseURL e.g. [NSURL URLWithString:@"http://data.3g.yy.com"]
 *
 *  @return 初始化完成的`YYHttpClient`对象
 */
- (instancetype)initWithBaseURL:(NSURL *)baseURL;

/**
 *  创建并执行一个`NSURLSessionDataTask`(Http GET)
 *
 *  @param URLString  用于创建请求URL的字符串
 *  @param parameters 用于创建请求的参数
 *  @param success    请求成功的回调
 *  @param failure    请求失败的回调
 *
 *  @return 创建的`NSURLSessionDataTask`对象
 */
- (NSURLSessionDataTask *)GET:(NSString *)URLString
                   parameters:(NSDictionary *)parameters
                      success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
                      failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

- (NSURLSessionDataTask *)OrignGET:(NSString *)URLString
                        parameters:(NSDictionary *)parameters
                           success:(void (^)(NSURLSessionDataTask *, id))success
                           failure:(void (^)(NSURLSessionDataTask *, NSError *))failure;

/**
 *  创建并执行一个`NSURLSessionDataTask`(Http GET)
 *
 *  @param URLString  用于创建请求URL的字符串
 *  @param parameters 用于创建请求的参数
 *  @param parametersConstructionBlock 配置请求的参数回调
 *  @param success    请求成功的回调
 *  @param failure    请求失败的回调
 *
 *  @return 创建的`NSURLSessionDataTask`对象
 */
- (NSURLSessionDataTask *)GET:(NSString *)URLString
                   parameters:(NSDictionary *)parameters
  parametersConstructionBlock:(NSDictionary * (^)(NSDictionary *parameters))parametersConstructionBlock
                      success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
                      failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

/**
 *  创建并执行一个`NSURLSessionDataTask`(Http POST)
 *
 *  @param URLString  用于创建请求URL的字符串
 *  @param parameters 用于创建请求的参数
 *  @param success    请求成功的回调
 *  @param failure    请求失败的回调
 *
 *  @return 创建的`NSURLSessionDataTask`对象
 */
- (NSURLSessionDataTask *)POST:(NSString *)URLString
                    parameters:(NSDictionary *)parameters
                       success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
                       failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

/**
 创建并执行一个`NSURLSessionDataTask`(Http POST)

 @param URLString 用于创建请求URL的字符串
 @param parameters 用于创建请求的参数
 @param parametersConstructionBlock 配置请求的参数回调
 @param success 请求成功的回调
 @param failure 请求失败的回调
 @return 创建的`NSURLSessionDataTask`对象
 */
- (NSURLSessionDataTask *)POST:(NSString *)URLString
                    parameters:(NSDictionary *)parameters
   parametersConstructionBlock:(NSDictionary * (^)(NSDictionary *parameters))parametersConstructionBlock
                       success:(void (^)(NSURLSessionDataTask *, id))success
                       failure:(void (^)(NSURLSessionDataTask *, NSError *))failure;

/**
 *  创建并执行一个`NSURLSessionDataTask`(Http Multipart POST)
 *
 *  @param URLString  用于创建请求URL的字符串
 *  @param parameters 用于创建请求的参数
 *  @param block      用于组成POST Boday的block，可用于上传文件
 *  @param success    请求成功的回调
 *  @param failure    请求失败的回调
 *
 *  @return 创建的`NSURLSessionDataTask`对象
 */
- (NSURLSessionDataTask *)POST:(NSString *)URLString
                    parameters:(NSDictionary *)parameters
     constructingBodyWithBlock:(void (^)(id <AFMultipartFormData> formData))block
                       success:(void (^)(NSURLSessionDataTask *, id))success
                       failure:(void (^)(NSURLSessionDataTask *, NSError *))failure;

- (NSURLSessionDataTask *)POST:(NSString *)URLString
                    recommendData:(NSDictionary *)data
                       success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
                       failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

/**
 *  创建并执行一个`NSURLSessionDataTask`(Http PUT)
 *
 *  @param URLString  用于创建请求URL的字符串
 *  @param parameters 用于创建请求的参数
 *  @param success    请求成功的回调
 *  @param failure    请求失败的回调
 *
 *  @return 创建的`NSURLSessionDataTask`对象
 */
- (NSURLSessionDataTask *)PUT:(NSString *)URLString
                   parameters:(NSDictionary *)parameters
                      success:(void (^)(NSURLSessionDataTask *, id))success
                      failure:(void (^)(NSURLSessionDataTask *, NSError *))failure;





@end
