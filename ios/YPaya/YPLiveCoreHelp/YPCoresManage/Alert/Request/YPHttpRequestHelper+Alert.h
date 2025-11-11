//
//  YPHttpRequestHelper+Alert.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"
#import "YPAlertInfo.h"

@interface YPHttpRequestHelper (Alert)

/**
 请求活动弹窗配置

 @param type 弹窗位置，1首页，2直播间右下角
 @param success 成功
 @param failure 失败
 */
+ (void)requestAlertInfoByTyp:(NSInteger)type
                      Success:(void (^)(YPAlertInfo *info))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure;

+ (void)requestActivityList:(NSInteger)type
                    Success:(void (^)(NSArray *arr))success
                    failure:(void (^)(NSNumber *resCode, NSString *message))failure;

@end
