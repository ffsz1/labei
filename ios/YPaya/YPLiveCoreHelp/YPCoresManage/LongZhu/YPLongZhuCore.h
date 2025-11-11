//
//  YPLongZhuCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"

@interface YPLongZhuCore : YPBaseCore

/**
 获取速配状态
 
 @param roomId 房间id
 */
- (void)getStateWithRoomId:(NSInteger)roomId;

/**
 获取速配随机数/保存自己选择的数
 
 @param roomId 房间id
 @param type 1 速配  2 选择
 @param result 如果type=1传空  type=2传选择的数
 */
- (void)getChooseResultWithRoomId:(NSInteger)roomId
                             type:(NSInteger)type
                           result:(NSString *)result;

/**
 取消解签
 
 @param roomId 房间id
 @param type 1 速配  2 选择
 @param result 如果type=1传空  type=2传选择的数
 */
- (void)cancelChooseResultWithRoomId:(NSInteger)roomId
                                type:(NSInteger)type
                              result:(NSString *)result;

/**
 展示结果
 
 @param result 展示的数字
 @param roomId 房间id
 @param type type 1 速配  2 选择
 */
- (void)confirmResult:(NSString *)result
               roomId:(NSInteger)roomId
                 type:(NSInteger)type;

- (UIImage *)composeImgWithImge1:(UIImage *)img1 img2:(UIImage *)img2 img3:(UIImage *)img3;

@end
