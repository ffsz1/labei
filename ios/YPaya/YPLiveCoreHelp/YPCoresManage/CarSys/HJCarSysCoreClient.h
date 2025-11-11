//
//  CarSysCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJCarSysCoreClient<NSObject>

- (void)getCarSysListSuccessWithArr:(NSArray *)CarSysList;
- (void)getCarSysListFailWithMessage:(NSString *)message;
- (void)buyCarSuccess:(id)data;
- (void)buyFailWithCode:(NSNumber *)code Message:(NSString *)msg;
- (void)continueCarSuccess:(id)data;
- (void)continueFailWithCode:(NSNumber *)code WithMessage:(NSString *)msg;

- (void)userCarSysSuccess;
- (void)userCarSysFail:(NSString *)msg;

- (void)sendCarSysSuccess;
- (void)sendCarSysFail:(NSString *)msg;

//展示座驾
- (void)onGetReceiveCarSuccess:(NSArray *)userGiftList uid: (UserID)uid;
- (void)onGetReceiveCarFailth:(NSString *)message;

//展示头饰
- (void)onGetReceiveHeadSuccess:(NSArray *)userGiftList uid: (UserID)uid;
- (void)onGetReceiveHeadFailth:(NSString *)message;


@end
