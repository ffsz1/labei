//
//  HJLongZhuCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "HJRoomLongZhuMsgModel.h"

@protocol HJLongZhuCoreClient <NSObject>

- (void)onReceiveLongZhuWithModel:(HJRoomLongZhuMsgModel *)model isSuPei:(BOOL)isSuPei;

// 获取速配状态
- (void)getStateSuccessWithResult:(NSDictionary *)result;
- (void)getStateFailedWithMessage:(NSString *)message;

// 获取速配随机数/保存自己选择的数
- (void)getChooseResultSuccessWithResult:(NSInteger)result type:(NSInteger)type;
- (void)getChooseResultFailedWithMessage:(NSString *)message type:(NSInteger)type;

// 取消解签
- (void)cancelChooseResultSuccessWithResult:(NSInteger)result type:(NSInteger)type;
- (void)cancelChooseResultFailedWithMessage:(NSString *)message type:(NSInteger)type;

// 展示结果
- (void)confirmResultSuccessWithType:(NSInteger)type;
- (void)confirmResultFailedWithMessage:(NSString *)message type:(NSInteger)type;

@optional


@end
