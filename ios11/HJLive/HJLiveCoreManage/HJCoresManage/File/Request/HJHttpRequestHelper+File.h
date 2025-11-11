//
//  HJHttpRequestHelper+File.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"
#import "UserPhoto.h"

@interface HJHttpRequestHelper (File)


/*
 * Upload Image
 */
+ (void)uploadImage:(UIImage *)image
              named:(NSString *)imageName
              token:(NSString *)token
            success:(void (^)(NSString *key, NSDictionary *resp))success
            failure:(void (^)(NSNumber *resCode, NSString *message))failure;



/**
 上传文件
 
 @param path 文件路径
 @param fileName 文件名（要带后缀）
 @param token token
 */
+ (void)uploadFileWithPath:(NSString *)path
                  fileName:(NSString *)fileName
                     token:(NSString *)token
                   success:(void (^)(NSString *key, NSDictionary *resp))success
                   failure:(void (^)(NSNumber *resCode, NSString *message))failure;

@end
