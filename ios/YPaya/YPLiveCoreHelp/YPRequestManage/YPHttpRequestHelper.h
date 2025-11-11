//
//  YPHttpRequestHelper.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSMutableDictionary+Safe.h"
NS_ASSUME_NONNULL_BEGIN

@interface YPHttpRequestHelper : NSObject

// 获取域名
+ (NSString *)getHostUrl;

// 获取图片域名
+ (NSString *)getPicHostUrl;

+ (void)resetClient;

+ (void)OrignGET:(NSString *)method
          params:(NSDictionary *)params
         success:(void (^)(id))success
         failure:(void (^)(NSURLSessionDataTask *, NSError *))failure;

+ (void)OrignPOST:(NSString *)method
           params:(NSDictionary *)params
          success:(void (^)(id data))success
          failure:(void (^)(NSURLSessionDataTask *, NSError *))failure;

//async
+ (void)GET:(NSString *)method
     params:(NSDictionary *)params
    success:(void (^)(id data))success
    failure:(void (^)(NSNumber *resCode, NSString *message))failure;

+ (void)POST:(NSString *)method
     params:(NSDictionary *)params
    success:(void (^)(id data))success
    failure:(void (^)(NSNumber *resCode, NSString *message))failure;



///< IM用
+ (void)IM_GET:(NSString *)method
        params:(NSDictionary *)params
       success:(void (^)(id data))success
       failure:(void (^)(NSNumber *resCode, NSString *message))failure;

///< IM用
+ (void)IM_POST:(NSString *)method
         params:(NSDictionary *)params
        success:(void (^)(id data))success
        failure:(void (^)(NSNumber *resCode, NSString *message))failure;

+ (NSDictionary *)encryptFromData:(id)dic;

+ (id)decryptFromData:(id)data;

+ (BOOL)shoudShowErrorHUD;

+ (void)setupShouldShowErrorHUD:(BOOL)flag;
@end

NS_ASSUME_NONNULL_END
