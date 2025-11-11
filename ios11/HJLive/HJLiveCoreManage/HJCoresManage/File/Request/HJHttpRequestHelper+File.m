//
//  HJHttpRequestHelper+File.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+File.h"
#import "NSObject+YYModel.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "UserPhoto.h"
#import <QiniuSDK.h>

@implementation HJHttpRequestHelper (File)

+ (void)uploadImage:(UIImage *)image
              named:(NSString *)imageName
              token:(NSString *)token
            success:(void (^)(NSString *key, NSDictionary *resp))success
            failure:(void (^)(NSNumber *resCode, NSString *message))failure{
    QNConfiguration *config = [QNConfiguration build:^(QNConfigurationBuilder *builder) {
//        builder.zone = [QNFixedZone zone1];
        builder.zone = [QNFixedZone zone2];
    }];
    QNUploadManager *upManager = [[QNUploadManager alloc] initWithConfiguration:config];
    NSData *data = UIImageJPEGRepresentation(image, 0.5);
    [upManager putData:data key:imageName token:token complete:^(QNResponseInfo *info, NSString *key, NSDictionary *resp) {
        NSLog(@"info %@",info);
        NSLog(@"resp %@",resp);
        NSLog(@"key %@",key);
        
        if (resp) {
            success(key,resp);
        }else{
            failure(@(info.statusCode),info.error.localizedDescription);
        }
    } option:nil];
}

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
                   failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    QNConfiguration *config = [QNConfiguration build:^(QNConfigurationBuilder *builder) {
        builder.zone = [QNFixedZone zone2];
    }];
    QNUploadManager *upManager = [[QNUploadManager alloc] initWithConfiguration:config];
    NSURL *fileUrl = [NSURL URLWithString:path];
    NSData *data = [NSData dataWithContentsOfURL:fileUrl];
    [upManager putFile:path key:fileName token:token complete:^(QNResponseInfo *info, NSString *key, NSDictionary *resp) {
        NSLog(@"info %@",info);
        NSLog(@"resp %@",resp);
        NSLog(@"key %@",key);
        
        if (resp) {
            success(key,resp);
        }else{
            failure(@(info.statusCode),info.error.localizedDescription);
        }
    } option:nil];
}

@end
