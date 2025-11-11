//
//  UserCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserInfo.h"
#import "YPLevelModel.h"

@protocol HJUserCoreClient <NSObject>

@optional
- (void)onCurrentUserInfoUpdate:(UserInfo *)userInfo;
- (void)onCurrentUserInfoNeedComplete:(UserID)uid;
- (void)onCurrentUserInfoLogout;

//展示礼物墙
- (void)onGetReceiveGiftSuccess:(NSArray *)userGiftList uid: (UserID)uid type:(NSInteger)type;
- (void)onGetReceiveGiftFailth:(NSString *)message type:(NSInteger)type;

//获取财富等级
- (void)onGetRichLevelSuccess:(YPLevelModel *)model;
- (void)onGetRichLevelFailth:(NSString *)message;

//获取魅力等级
- (void)onGetMeiliLevelSuccess:(YPLevelModel *)model;
- (void)onGetMeiliLevelFailth:(NSString *)message;

//上传用户图片
- (void)onUploadImageUrlToServerSuccess;
- (void)onUploadImageUrlToServerFailth:(NSString *)message;

//删除用户图片
- (void)deleteImageToServerSuccess;
- (void)deleteImageUrlToServerFailth:(NSString *)message;

//举报用户/房间
- (void)userReportSaveSuccessWithType:(NSInteger)type requestId:(NSString *)requestId;
- (void)userReportSaveFailth:(NSString *)message type:(NSInteger)type requestId:(NSString *)requestId;

//获取新人房间推荐
- (void)roomRcmdGetSuccessWithResult:(NSDictionary *)result;
- (void)roomRcmdGetFailedWithMessage:(NSString *)message;

- (void)checkUserhasBanndFail:(NSString *)message;

//获取用户录音文本
- (void)onGetVoiceTextSuccess:(NSString *)text;
- (void)onGetVoiceTextFailth:(NSString *)message;

@end
