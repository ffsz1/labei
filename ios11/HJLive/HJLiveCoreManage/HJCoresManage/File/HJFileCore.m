//
//  HJFileCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFileCore.h"
#import <NIMSDK/NIMSDK.h>
#import "CommonFileUtils.h"
#import "HJFileCoreClient.h"
#import "UIImage+Resize.h"
#import <Foundation/Foundation.h>
#import "HJHttpRequestHelper+File.h"
#import "HJUserCoreHelp.h"
#import "GTMBase64.h"
#include <CommonCrypto/CommonDigest.h>
#include <CommonCrypto/CommonHMAC.h>

@implementation HJFileCore
- (void)uploadAvatar:(UIImage *)avatar {
    if (avatar == nil) {
        return;
    }
    
    UIImage *resizedImage = [avatar resizedImageWithRestrictSize:CGSizeMake(640, 640)];
    
    [self qiNiuUploadImage:resizedImage uploadType:UploadImageTypeAvtor];
    // 本地沙盒目录
    NSString *path = [CommonFileUtils cachesDirectory];
    NSString *fileName = [NSString stringWithFormat:@"avatar_%@", [self stringFromDate:[NSDate new]]];
    NSString *imageFilePath = [path stringByAppendingPathComponent:fileName];
    // 将取得的图片写入本地的沙盒中，其中0.5表示压缩比例，1表示不压缩，数值越小压缩比例越大
    BOOL success = [UIImageJPEGRepresentation(resizedImage, 1.0) writeToFile:imageFilePath  atomically:YES];
    if (success){
        [[NIMSDK sharedSDK].resourceManager upload:imageFilePath progress:^(float progress) {
            NotifyCoreClient(HJFileCoreClient, @selector(onUploadProgress:), onUploadProgress:progress);
        } completion:^(NSString * _Nullable urlString, NSError * _Nullable error) {
            if (error == nil) {
                NotifyCoreClient(HJFileCoreClient, @selector(onUploadSuccess:), onUploadSuccess:urlString);
                
                
            } else {
                NotifyCoreClient(HJFileCoreClient, @selector(onUploadSuccess:), onUploadSuccess:JX_IMAGE_DEFAULT_AVATAR_URL);
//                NotifyCoreClient(HJFileCoreClient, @selector(onUploadFailth:), onUploadFailth:nil);
            }
        }];
    } else {
        NotifyCoreClient(HJFileCoreClient, @selector(onUploadSuccess:), onUploadSuccess:JX_IMAGE_DEFAULT_AVATAR_URL);
//        NotifyCoreClient(HJFileCoreClient, @selector(onUploadFailth:), onUploadFailth:nil);
    }
}

- (void)uploadCover:(UIImage *)image
{
    if (image == nil) {
        return;
    }
    UIImage *resizedImage = [image resizedImageWithRestrictSize:CGSizeMake(720, 1280)];
    // 本地沙盒目录
    NSString *path = [CommonFileUtils cachesDirectory];
    NSString *fileName = [NSString stringWithFormat:@"cover_%@", [self stringFromDate:[NSDate new]]];
    NSString *imageFilePath = [path stringByAppendingPathComponent:fileName];
    // 将取得的图片写入本地的沙盒中，其中0.5表示压缩比例，1表示不压缩，数值越小压缩比例越大
    BOOL success = [UIImageJPEGRepresentation(resizedImage, 0.7) writeToFile:imageFilePath  atomically:YES];
    
    if (success){
        [[NIMSDK sharedSDK].resourceManager upload:imageFilePath progress:^(float progress) {
            NotifyCoreClient(HJFileCoreClient, @selector(onUploadProgress:), onUploadProgress:progress);
        } completion:^(NSString * _Nullable urlString, NSError * _Nullable error) {
            if (error == nil) {
                NotifyCoreClient(HJFileCoreClient, @selector(onUploadCoverSuccess:), onUploadCoverSuccess:urlString);
            } else {
                NotifyCoreClient(HJFileCoreClient, @selector(onUploadCoverFailth:), onUploadCoverFailth:error);
            }
        }];
    } else {
        NotifyCoreClient(HJFileCoreClient, @selector(onUploadCoverFailth:), onUploadCoverFailth:nil);
    }
}

- (void) cancelVoiceTask:(NSString *)filePath {
    if (!filePath.length) return;
    [[NIMSDK sharedSDK].resourceManager cancelTask:filePath];
}

- (void) downloadVoice:(NSString *)urlString
{
    if (urlString.length <= 0) {
        return;
    }
    
    NSString *voiceFilePath = [self read:urlString];
    if (voiceFilePath.length > 0) {
        NSFileManager *fileManager = [NSFileManager defaultManager];
        if([fileManager fileExistsAtPath:voiceFilePath]) {
            NotifyCoreClient(HJFileCoreClient, @selector(onDownloadVoiceSuccess:), onDownloadVoiceSuccess:voiceFilePath);
            return;
        }
    }
    
    NSString *path = [CommonFileUtils cachesDirectory];
    NSString *fileName = [NSString stringWithFormat:@"voice_%@.aac", [self stringFromDate:[NSDate new]]];
    voiceFilePath = [path stringByAppendingPathComponent:fileName];
    @weakify(self);
    [[NIMSDK sharedSDK].resourceManager download:urlString filepath:voiceFilePath progress:^(float progress) {
        
    } completion:^(NSError * _Nullable error) {
        @strongify(self);
        if (error == nil) {
            [self save:urlString value:voiceFilePath];
            NotifyCoreClient(HJFileCoreClient, @selector(onDownloadVoiceSuccess:), onDownloadVoiceSuccess:voiceFilePath);
        } else {
            NotifyCoreClient(HJFileCoreClient, @selector(onDownloadVoiceFailth:), onDownloadVoiceSuccess:voiceFilePath);
        }
    }];
}

- (void)save:(NSString *)key value:(NSString *)value
{
    if (key.length <= 0 || value.length <= 0) {
        return;
    }
    
    NSUserDefaults *useDefaults = [NSUserDefaults standardUserDefaults];
    [useDefaults setObject:value forKey:key];
}

- (NSString *)read:(NSString *)key
{
    if (key.length <= 0) {
        return nil;
    }
    return [[NSUserDefaults standardUserDefaults] objectForKey:key];
}

- (void)uploadVoice:(NSString *)filePath
{
    if (filePath.length <= 0) {
        return;
    }
    
    [[NIMSDK sharedSDK].resourceManager upload:filePath progress:^(float progress) {
        NotifyCoreClient(HJFileCoreClient, @selector(onUploadProgress:), onUploadProgress:progress);
    } completion:^(NSString * _Nullable urlString, NSError * _Nullable error) {
        if (error == nil) {
            NotifyCoreClient(HJFileCoreClient, @selector(onUploadVoiceSuccess:), onUploadVoiceSuccess:urlString);
        } else {
            NotifyCoreClient(HJFileCoreClient, @selector(onUploadVoiceFailth:), onUploadVoiceFailth:error);
        }
    }];
}

//上传图片
- (void)uploadImage:(UIImage *)image {
    //本地沙盒目录
//    UIImage *resizedImage = [image resizedImageWithRestrictSize:CGSizeMake(720, 1280)];
    NSString *path = [CommonFileUtils cachesDirectory];
    NSString *fileName = [NSString stringWithFormat:@"userPhoto_%@", [self stringFromDate:[NSDate new]]];
    NSString *imageFilePath = [path stringByAppendingPathComponent:fileName];
    
    [UIImageJPEGRepresentation(image, 0.6) writeToFile:imageFilePath atomically:YES];
    
    [[NIMSDK sharedSDK].resourceManager upload:imageFilePath progress:^(float progress) {
        
    } completion:^(NSString * _Nullable urlString, NSError * _Nullable error) {
        if (error == nil) {
            [GetCore(HJUserCoreHelp)uploadImageUrlToServer:urlString];
            NotifyCoreClient(HJFileCoreClient, @selector(onUploadImageSuccess), onUploadImageSuccess);
        }else {
            NotifyCoreClient(HJFileCoreClient, @selector(onUploadImageFailth:), onUploadImageFailth:error.description);
        }
        
    }];
    
}


- (NSString *)stringFromDate:(NSDate *)date
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyyMMddHHmmss"];
    NSString *destDateString = [dateFormatter stringFromDate:date];
    return destDateString;
}


#pragma mark - Qiniu
- (void)qiNiuUploadImage:(UIImage *)image  uploadType:(UploadImageType)uploadType{
    NSString *token = [self token];
    [HJHttpRequestHelper uploadImage:image named:nil token:token success:^(NSString *key, NSDictionary *resp) {
        NSString *imageKey = resp[@"key"];
        switch (uploadType) {
            case UploadImageTypeAvtor:
                NotifyCoreClient(HJFileCoreClient, @selector(didUploadAvtorImageSuccessUseQiNiu:), didUploadAvtorImageSuccessUseQiNiu:imageKey);
                break;
            case UploadImageTypeLibary:
                NotifyCoreClient(HJFileCoreClient, @selector(didUploadPhotoImageSuccessUseQiNiu:), didUploadPhotoImageSuccessUseQiNiu:imageKey);
                break;
            case UploadImageTypeChatRoot:
                NotifyCoreClient(HJFileCoreClient, @selector(didUploadChatRoomImageSuccessUseQiNiu:), didUploadChatRoomImageSuccessUseQiNiu:imageKey);
                break;
            case UploadImageTypeIDCard:
                NotifyCoreClient(HJFileCoreClient, @selector(didUploadIDCardImageSuccessUseQiNiu:), didUploadIDCardImageSuccessUseQiNiu:imageKey);
                break;
            default:
                break;
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        switch (uploadType) {
            case UploadImageTypeAvtor:
                NotifyCoreClient(HJFileCoreClient, @selector(didUploadAvtorImageFailUseQiNiu:), didUploadAvtorImageFailUseQiNiu:message);
                break;
            case UploadImageTypeLibary:
                NotifyCoreClient(HJFileCoreClient, @selector(didUploadPhotoImageFailUseQiNiu:), didUploadPhotoImageFailUseQiNiu:message);
                break;
            case UploadImageTypeChatRoot:
                NotifyCoreClient(HJFileCoreClient, @selector(didUploadChatRoomImageFailUseQiNiu:), didUploadChatRoomImageFailUseQiNiu:message);
                break;
            case UploadImageTypeIDCard:
                NotifyCoreClient(HJFileCoreClient, @selector(didUploadIDCardImageFailUseQiNiu:), didUploadIDCardImageFailUseQiNiu:message);
                break;
            default:
                break;
        }
    }];
}


- (NSString *)token{
    NSMutableDictionary *authInfo = [NSMutableDictionary dictionary];
    
    [authInfo setObject:JX_QN_BUCKET_NAME forKey:@"scope"];
    [authInfo
     setObject:[NSNumber numberWithLong:[[NSDate date] timeIntervalSince1970] +  24 * 3600]
     forKey:@"deadline"];
    NSData *jsonData =
    [NSJSONSerialization dataWithJSONObject:authInfo options:NSJSONWritingPrettyPrinted error:nil];
    NSString *encodedString = [self urlSafeBase64Encode:jsonData];
    
    NSString *encodedSignedString = [self HMACSHA1:JX_QN_SK_KEY text:encodedString];
    
    NSString *token = [NSString stringWithFormat:@"%@:%@:%@", JX_QN_AK_KEY, encodedSignedString, encodedString];
    return token;
}

- (NSString *)urlSafeBase64Encode:(NSData *)text {
    
    NSString *base64 =
    [[NSString alloc] initWithData:[GTMBase64 encodeData:text] encoding:NSUTF8StringEncoding];
    base64 = [base64 stringByReplacingOccurrencesOfString:@"+" withString:@"-"];
    base64 = [base64 stringByReplacingOccurrencesOfString:@"/" withString:@"_"];
    return base64;
}



- (NSString *)HMACSHA1:(NSString *)key text:(NSString *)text {
    
    const char *cKey = [key cStringUsingEncoding:NSUTF8StringEncoding];
    const char *cData = [text cStringUsingEncoding:NSUTF8StringEncoding];
    char cHMAC[CC_SHA1_DIGEST_LENGTH];
    CCHmac(kCCHmacAlgSHA1, cKey, strlen(cKey), cData, strlen(cData), cHMAC);
    NSData *HMAC = [[NSData alloc] initWithBytes:cHMAC length:CC_SHA1_DIGEST_LENGTH];
    NSString *hash = [self urlSafeBase64Encode:HMAC];
    return hash;
}


/**
 上传文件
 
 @param path 文件路径
 @param fileName 文件名（要带后缀）
 */
- (void)uploadFileWithPath:(NSString *)path
                  fileName:(NSString *)fileName
                   success:(void (^)(NSString *url))success
                   failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    [HJHttpRequestHelper uploadFileWithPath:path fileName:fileName token:[self token] success:^(NSString *key, NSDictionary *resp) {
        NSString *urlString = [NSString stringWithFormat:@"%@/%@",kImageDomainURL,fileName];
        if (success) {
            success(urlString);
        }
    } failure:failure];
}




@end
