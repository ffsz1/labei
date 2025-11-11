//
//  NIMKitDataProvider.h
//  YPNIMKit
//
//  Created by amao on 8/13/15.
//  Copyright (c) 2015 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>

@class NIMSession;
@class YPNIMKitInfo;
@class YPNIMKitInfoFetchOption;

@protocol NIMKitDataProvider <NSObject>

@optional

/**
 *  上层提供用户信息的接口
 *
 *  @param userId  用户ID
 *  @param option  获取选项
 *
 *  @return 用户信息
 */
- (YPNIMKitInfo *)infoByUser:(NSString *)userId
                    option:(YPNIMKitInfoFetchOption *)option;


/**
 *  上层提供群组信息的接口
 *
 *  @param teamId 群组ID
 *  @param option 获取选项
 *
 *  @return 群组信息
 */
- (YPNIMKitInfo *)infoByTeam:(NSString *)teamId
                    option:(YPNIMKitInfoFetchOption *)option;

@end
