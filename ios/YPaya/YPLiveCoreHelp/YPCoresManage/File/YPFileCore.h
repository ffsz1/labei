//
//  YPFileCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"

typedef enum : NSUInteger {
    UploadImageTypeAvtor,
    UploadImageTypeLibary,
    UploadImageTypeChatRoot,
    UploadImageTypeIDCard,
} UploadImageType;

@interface YPFileCore : YPBaseCore
//- (void) uploadAvatar:(UIImage *)image;
- (void) downloadVoice:(NSString *)urlString;
- (void) uploadVoice:(NSString *)filePath;
- (void) uploadCover:(UIImage *)image;
- (void) uploadImage:(UIImage *)image;
- (void) cancelVoiceTask:(NSString *)filePath;

- (void) qiNiuUploadImage:(UIImage *)image uploadType:(UploadImageType)uploadType;

/**
 上传文件
 
 @param path 文件路径
 @param fileName 文件名（要带后缀）
 */
- (void)uploadFileWithPath:(NSString *)path
                  fileName:(NSString *)fileName
                   success:(void (^)(NSString *url))success
                   failure:(void (^)(NSNumber *resCode, NSString *message))failure;

@end
