//
//  YPRoomPusher.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//  房间跳转管理类

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomPusher : NSObject
//根据uid进房
+ (void)pushRoomByID:(UserID)userID;

//根据uid所在的房间进房
+ (void)pushUserInRoomByID:(UserID)userID;

@end

NS_ASSUME_NONNULL_END
