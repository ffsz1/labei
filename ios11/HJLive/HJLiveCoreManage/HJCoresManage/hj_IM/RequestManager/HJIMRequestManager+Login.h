//
//  HJIMRequestManager+Login.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJIMRequestManager.h"

NS_ASSUME_NONNULL_BEGIN

/**
 登录接口
 */
@interface HJIMRequestManager (Login)
/**
 客户端登录IM服务器

 @param uid     用户Id
 @param ticket  用户Ticket
 @param success success description
 @param failure failure description
 */
+ (void)loginWithUid:(NSString *)uid
              ticket:(NSString *)ticket
             success:(JXIMRequestSuccessHander)success
             failure:(JXIMRequestFailureHander)failure;

/**
 自动登录

 @param success success description
 @param failure failure description
 */
+ (void)autoLoginWithSuccess:(JXIMRequestSuccessHander)success
                     failure:(JXIMRequestFailureHander)failure;

/**
 退出登录
 */
+ (void)logout;

@end

NS_ASSUME_NONNULL_END
