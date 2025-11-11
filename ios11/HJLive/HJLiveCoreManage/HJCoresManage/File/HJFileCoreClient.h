//
//  HJFileCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserPhoto.h"

@protocol HJFileCoreClient <NSObject>
@optional
- (void)onUploadProgress:(float) progress;
- (void)onUploadSuccess:(NSString *)url;
- (void)onUploadFailth:(NSError *)error;

- (void)onUploadVoiceSuccess:(NSString *)url;
- (void)onUploadVoiceFailth:(NSError *)error;

- (void)onUploadCoverSuccess:(NSString *)url;
- (void)onUploadCoverFailth:(NSError *)error;

- (void)onDownloadVoiceSuccess:(NSString *)filePath;
- (void)onDownloadVoiceFailth:(NSError *)error;

- (void)onUploadImageSuccess;
- (void)onUploadImageFailth:(NSString *)message;

//qiniu
- (void)didUploadAvtorImageSuccessUseQiNiu:(NSString *)key;
- (void)didUploadAvtorImageFailUseQiNiu:(NSString *)message;

- (void)didUploadPhotoImageSuccessUseQiNiu:(NSString *)key;
- (void)didUploadPhotoImageFailUseQiNiu:(NSString *)message;

- (void)didUploadChatRoomImageSuccessUseQiNiu:(NSString *)key;
- (void)didUploadChatRoomImageFailUseQiNiu:(NSString *)message;

- (void)didUploadIDCardImageSuccessUseQiNiu:(NSString *)key;
- (void)didUploadIDCardImageFailUseQiNiu:(NSString *)message;
@end
