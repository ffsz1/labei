//
//  HJUserHandler.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJUserHandler : NSObject

/**
 举报、拉黑列表

 @param tagUID 目标uid
 @param cancelFollowBlock 已关注传值、未关注传nil
 */
+ (void)showReport:(UserID)tagUID cancelFollowBlock:(void (^)())cancelFollowBlock;



/**
 举报

 @param tagUID 目标id
 */
+ (void)report:(UserID)tagUID;


/**
 拉黑

 @param tagUID 目标id
 @param cancelFollowBlock 已关注传值、未关注传nil
 */
+ (void)showAddBlackList:(UserID)tagUID cancelFollowBlock:(void (^)())cancelFollowBlock;

+ (void)showCancelAttentionAlert:(UserID)tagUID cancelFollowBlock:(void (^)())cancelFollowBlock;


/**
 取消关注

 @param tagUID 目标id
 @param cancelFollowBlock 未关注传值、关注传nil
 */
+ (void)cancelFollow:(UserID)tagUID cancelFollowBlock:(void (^)())cancelFollowBlock;

/**
 关注

 @param followUID 目标id
 @param followBlock 未关注传值、关注传nil
 */
+ (void)follow:(UserID)followUID followSucceed:(void (^)())followBlock;


@end

NS_ASSUME_NONNULL_END
