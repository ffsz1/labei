//
//  YPHttpRequestHelper+LongZhu.h
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"

@interface YPHttpRequestHelper (LongZhu)

/**
 获取速配状态
 
 @param roomId 房间id
 */
+ (void)getStateWithRoomId:(NSInteger)roomId success:(void (^)(NSDictionary *result))success failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 放弃解签
 
 @param roomId 房间id
 @param type 1 速配  2 选择
 @param result 如果type=1传空  type=2传选择的数
 */
+ (void)cancelChooseResultWithRoomId:(NSInteger)roomId
                                type:(NSInteger)type
                              result:(NSString *)result
                             success:(void (^)())success
                             failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 获取速配随机数/保存自己选择的数
 
 @param roomId 房间id
 @param type 1 速配  2 选择
 @param result 如果type=1传空  type=2传选择的数
 */
+ (void)getChooseResultWithRoomId:(NSInteger)roomId
                             type:(NSInteger)type
                           result:(NSString *)result
                          success:(void (^)(NSInteger state))success
                          failure:(void (^)(NSNumber *code, NSString *msg))failure;

/**
 展示结果
 
 @param result 展示的数字
 @param roomId 房间id
 @param type type 1 速配  2 选择
 */
+ (void)confirmResult:(NSString *)result
               roomId:(NSInteger)roomId
                 type:(NSInteger)type
              success:(void (^)())success
              failure:(void (^)(NSNumber *code, NSString *msg))failure;

@end
