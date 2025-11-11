//
//  YYHttpClient.m
//  YYMobileCore
//
//  Created by wuwei on 14/6/11.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYHttpClient.h"
#import "AFHTTPSessionManager.h"
#import "YYUtility.h"
#import "YYReachability.h"
#import "YYJSONResponseSerializer.h"
#import <CommonCrypto/CommonDigest.h>
#import <CoreTelephony/CTCarrier.h>
#import "YPVersionCoreHelp.h"

#import "UIDevice+YYAdd.h"
//#import <JXCategories/JXCategories.h>
#import "JXCategories.h"
@interface YYSharedHttpClient : YYHttpClient

@end

@implementation YYSharedHttpClient

- (void)setResponseSerializer:(AFHTTPResponseSerializer<AFURLResponseSerialization> *)responseSerializer
{
    //    NSAssert(0, @"不要设置sharedClient的responseSerializer");
    LogDebug(@"Http", @"不要设置sharedClient的responseSerializer");
}

@end

static NSString * const kHttpClientBasicParameterOSKey         = @"os";
static NSString * const kHttpClientBasicParameterOSVersionKey  = @"osVersion";
static NSString * const kHttpClientBasicParameterISPTypeKey    = @"ispType";
static NSString * const kHttpClientBasicParameterNetTypeKey    = @"netType";
static NSString * const kHttpClientBasicParameterChannelKey    = @"channel";
static NSString * const kHttpClientBasicParameterModelKey      = @"model";
static NSString * const kHttpClientBasicParameterDeviceIdKey = @"deviceId";
static NSString * const kHttpClientBasicParameterIDFAKey = @"idfa";
static NSString * const kHttpClientBasicParameterAPpVersionKey = @"appVersion";
static NSString * const kHttpClientBasicParameterAPPIDKey = @"appid";

static NSString * const kHttpClientBasicParameterUserIdKey = @"commonUserId";
static NSString * const kHttpClientBasicParameterIsJailbrokenKey = @"isJailbroken";
@interface YYHttpClient ()

@property (nonatomic, strong, readonly) AFHTTPSessionManager *httpSessionManager;
@property (nonatomic, strong) NSURLSessionConfiguration *sessionConfiguration;
@property (nonatomic, strong) NSURL *baseURL;

@property (nonatomic, strong) NSUserDefaults *ud;

@end

@implementation YYHttpClient
{
    
}

@synthesize baseURL = _baseURL;
@synthesize httpSessionManager = _httpSessionManager;
@synthesize basicParameterConstructor = _basicParameterConstructor;
@synthesize responseSerializer = _responseSerializer;

- (NSUserDefaults *)ud {
    if (!_ud) {
        _ud = [NSUserDefaults standardUserDefaults];
    }
    return _ud;
}


+ (instancetype)sharedClient
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [YYSharedHttpClient instantiateHttpClient];
    });
    return instance;
}

+ (instancetype)instantiateHttpClient
{
    return [[self alloc] initWithBaseURL:nil];
}

- (instancetype)initWithBaseURL:(NSURL *)baseURL
{
    self = [super init];
    if (self) {
        _baseURL = baseURL;
        _basicParameterConstructor = [self defaultBasicParameterConstructor];
        // 默认使用YYJSONResponseSerializer
        _responseSerializer = [[YYJSONResponseSerializer alloc] init];
        //        _didAppLaunched = NO;
    }
    return self;
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (AFHTTPSessionManager *)httpSessionManager
{
    // Lazy-loading
    if (_httpSessionManager == nil) {
        _httpSessionManager = [[AFHTTPSessionManager alloc] initWithBaseURL:self.baseURL sessionConfiguration:self.sessionConfiguration];
        _httpSessionManager.responseSerializer = self.responseSerializer;
        _httpSessionManager.requestSerializer.timeoutInterval = 20.f;
        // 客户端是否信任非法证书
        //        _httpSessionManager.securityPolicy.allowInvalidCertificates = YES;
        
        // 是否在证书域字段中验证域名
        //        [_httpSessionManager.securityPolicy setValidatesDomainName:NO];
        [_httpSessionManager setTaskWillPerformHTTPRedirectionBlock:^NSURLRequest *(NSURLSession *session, NSURLSessionTask *task, NSURLResponse *response, NSURLRequest *request) {
            return request;
        }];
        [YYHttpClient sharedClient].baseURL = self.baseURL;
    }
    return _httpSessionManager;
}

- (void)setResponseSerializer:(AFHTTPResponseSerializer<AFURLResponseSerialization> *)responseSerializer
{
    if (responseSerializer != _responseSerializer) {
        _responseSerializer = responseSerializer;
        
        if (_httpSessionManager) {
            _httpSessionManager.responseSerializer = responseSerializer;
        }
    }
}

- (AFHTTPRequestSerializer<AFURLRequestSerialization> *)requestSerializer
{
    return self.httpSessionManager.requestSerializer;
}

- (void)setRequestSerializer:(AFHTTPRequestSerializer<AFURLRequestSerialization> *)requestSerializer
{
    self.httpSessionManager.requestSerializer = requestSerializer;
}

- (BasicParameterConstructor)defaultBasicParameterConstructor
{
    return ^{
        return @{
                 kHttpClientBasicParameterOSKey:        @"iOS",
                 kHttpClientBasicParameterOSVersionKey: [YYUtility systemVersion],
                 kHttpClientBasicParameterNetTypeKey:   ([YYUtility networkStatus] == ReachableViaWiFi) ? @2 : @1,
                 kHttpClientBasicParameterISPTypeKey:   @([YYUtility carrierIdentifier]),
                 kHttpClientBasicParameterChannelKey:  [YYUtility getAppSource] ? : @"",
                 kHttpClientBasicParameterModelKey: [YYUtility modelName],
                 kHttpClientBasicParameterDeviceIdKey:[YYUtility deviceUniqueIdentification],
                 kHttpClientBasicParameterIDFAKey:[YYUtility idfa],
                 kHttpClientBasicParameterAPpVersionKey:[YYUtility appVersion],
                 kHttpClientBasicParameterAPPIDKey:JX_APPID_VERSION,
                 kHttpClientBasicParameterUserIdKey:([self.ud objectForKey:@"commonUserId"] ? [self.ud objectForKey:@"commonUserId"] : @""),
                 kHttpClientBasicParameterIsJailbrokenKey : [UIDevice currentDevice].isJailbroken ? @1 : @0
                 };
    };
}

#pragma mark - Http Methods

- (NSURLSessionDataTask *)GET:(NSString *)URLString
                     parameters:(NSDictionary *)parameters
                        success:(void (^)(NSURLSessionDataTask *, id))success
                        failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
    //    if (![YYHttpClient sharedClient].didAppLaunched) {
    //        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
    ////            [self.httpSessionManager GET:URLString
    ////                              parameters:[self _allParametersWithParameters:parameters]
    ////                                 success:success
    ////                                 failure:failure];
    //            [self.httpSessionManager GET:URLString parameters:[self _allParametersWithParameters:parameters] progress:nil success:success failure:failure];
    //        });
    //        return nil;
    //    }

    return [self GET:URLString parameters:parameters parametersConstructionBlock:nil success:success failure:failure];
}

- (NSURLSessionDataTask *)OrignGET:(NSString *)URLString
                   parameters:(NSDictionary *)parameters
                      success:(void (^)(NSURLSessionDataTask *, id))success
                      failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
    NSDictionary *parametersInfo = parameters;

    return [self.httpSessionManager GET:URLString parameters:parameters progress:nil success:success failure:failure];
}



- (NSURLSessionDataTask *)GET:(NSString *)URLString
                   parameters:(NSDictionary *)parameters
  parametersConstructionBlock:(NSDictionary * (^)(NSDictionary *parameters))parametersConstructionBlock
                      success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
                      failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure {
    NSDictionary *parametersInfo = [self _allParametersWithParameters:parameters];
    if (parametersConstructionBlock) {
        NSMutableDictionary *buffer = parametersInfo.mutableCopy;
        parametersInfo = parametersConstructionBlock(parametersInfo);
    }
    return [self.httpSessionManager GET:URLString parameters:parametersInfo progress:nil success:success failure:failure];
}

- (NSURLSessionDataTask *)POST:(NSString *)URLString
                     parameters:(NSDictionary *)parameters
                        success:(void (^)(NSURLSessionDataTask *, id))success
                        failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
    //    if (![YYHttpClient sharedClient].didAppLaunched) {
    //        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
    ////            [self.httpSessionManager POST:URLString
    ////                               parameters:[self _allParametersWithParameters:parameters]
    ////                                  success:success
    ////                                  failure:failure];
    //            [self.httpSessionManager POST:URLString parameters:[self _allParametersWithParameters:parameters] progress:nil success:success failure:failure];
    //        });
    //        return nil;
    //    }
    
    return [self POST:URLString parameters:parameters parametersConstructionBlock:nil success:success failure:failure];
}

- (NSURLSessionDataTask *)POST:(NSString *)URLString
                    parameters:(NSDictionary *)parameters
   parametersConstructionBlock:(NSDictionary * (^)(NSDictionary *parameters))parametersConstructionBlock
                       success:(void (^)(NSURLSessionDataTask *, id))success
                       failure:(void (^)(NSURLSessionDataTask *, NSError *))failure {
    NSDictionary *parametersInfo = [self _allParametersWithParameters:parameters];
    if (parametersConstructionBlock) {
        parametersInfo = parametersConstructionBlock(parametersInfo);
    }
    return [self.httpSessionManager POST:URLString parameters:parametersInfo progress:nil success:success failure:failure];
}

- (NSURLSessionDataTask *)POST:(NSString *)URLString
                 recommendData:(NSDictionary *)data
                       success:(void (^)(NSURLSessionDataTask *, id))success
                       failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
    //    return [self.httpSessionManager POST:URLString
    //                              parameters:data
    //                                 success:success
    //                                 failure:failure];
    return [self.httpSessionManager POST:URLString parameters:data progress:nil success:success failure:failure];
}

- (NSURLSessionDataTask *)POST:(NSString *)URLString
                    parameters:(NSDictionary *)parameters
     constructingBodyWithBlock:(void (^)(id <AFMultipartFormData> formData))block
                       success:(void (^)(NSURLSessionDataTask *, id))success
                       failure:(void (^)(NSURLSessionDataTask *, NSError *))failure;
{
    //    return [self.httpSessionManager POST:URLString
    //                              parameters:[self _allParametersWithParameters:parameters]
    //               constructingBodyWithBlock:block
    //                                 success:success
    //                                 failure:failure];
    return [self.httpSessionManager POST:URLString parameters:[self _allParametersWithParameters:parameters] constructingBodyWithBlock:block progress:nil success:success failure:failure];
}

- (NSURLSessionDataTask *)PUT:(NSString *)URLString
                   parameters:(NSDictionary *)parameters
                      success:(void (^)(NSURLSessionDataTask *, id))success
                      failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
    return [self.httpSessionManager PUT:URLString
                             parameters:[self _allParametersWithParameters:parameters]
                                success:success
                                failure:failure];
}

#pragma mark - Priavte

- (NSDictionary *)_allParametersWithParameters:(NSDictionary *)parameters
{
    NSMutableDictionary *allParameters = [NSMutableDictionary dictionaryWithDictionary:parameters ? : @{}];
    
    if (self.basicParameterConstructor) {
        [allParameters addEntriesFromDictionary:self.basicParameterConstructor()];
    }
    
    //将所有的参数取出来
    NSMutableArray *allParaArr = [NSMutableArray array];
    for (NSString *key in allParameters.allKeys) {
        NSString *value = allParameters[key];
        NSString *paraStr = [NSString stringWithFormat:@"%@=%@",key,value];
        [allParaArr addObject:paraStr];
    }
    
    //加入当前时间戳
    NSTimeInterval time = [[NSDate date]timeIntervalSince1970];
    NSString *timeString = [NSString stringWithFormat:@"t=%d",(int)time];
    [allParaArr addObject:timeString];
    
    //排序
    NSArray *sortArray = [self sortByAesc:allParaArr];
    //拼接
    NSString *dString = [self appendinString:sortArray];
    //md5
    NSString *md5 = [self getResultSign:dString];
    
    [allParameters setObject:md5 forKey:@"sn"];
    [allParameters setObject:[NSString stringWithFormat:@"%d",(int)time] forKey:@"t"];
    
    __weak typeof(allParameters) weakAllParameters = allParameters;
    [allParameters enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, NSString  *value, BOOL * _Nonnull stop) {
        if ([value isKindOfClass:[NSString class]]) {
            value = [value stringByURLEncode];
            [weakAllParameters setValue:value forKey:key];
        }
    }];
    
    return allParameters;
}

- (NSString *)appendinString:(NSArray *)sortArray {
    NSString *dString = @"";
    for (int i = 0 ; i < sortArray.count; i++) {
        dString = [dString stringByAppendingString:sortArray[i]];
    }
    dString = [dString stringByAppendingString:[self getParametersEncryptKey]];
    return dString;
}

- (NSString *)getParametersEncryptKey {
    if (GetCore(YPVersionCoreHelp).isReleaseEnv) return JX_REQUEST_PARAMETER_ENCRYPT_KEY_RELEASE;
    
    return JX_REQUEST_PARAMETER_ENCRYPT_KEY_DEBUG;
}

#pragma mark - 排序-升序
- (NSMutableArray *)sortByAesc:(NSMutableArray *)array {
    NSSortDescriptor *descriptor = [NSSortDescriptor sortDescriptorWithKey:nil ascending:YES];
    NSArray *descriptors = [NSArray arrayWithObject:descriptor];
    [array sortUsingDescriptors:descriptors];
    return array;
}

- (NSString *)getResultSign:(NSString *)dString{
    
    NSString *md5 = [self md5:dString];
    
    md5 = [md5 substringToIndex:7];//到7但不包含7
    
    md5 = [md5 lowercaseString];
    
    NSString *returnString = [NSString stringWithFormat:@"%@",md5];
    
    return returnString;
}

- (NSString *)md5:(NSString *)str
{
    const char *cStr = [str UTF8String];
    unsigned char digest[CC_MD5_DIGEST_LENGTH];
    CC_MD5( cStr, (unsigned int)strlen(cStr), digest );
    
    NSMutableString *output = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];
    
    for(int i = 0; i < CC_MD5_DIGEST_LENGTH; i++)
        [output appendFormat:@"%02X", digest[i]];
    
    return output;
}

@end
