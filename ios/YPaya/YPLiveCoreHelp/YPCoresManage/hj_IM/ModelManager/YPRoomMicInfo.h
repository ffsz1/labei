//
//  YPRoomMicInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"

#import "YPIMDefines.h"

/**
 坑位信息
 */
@interface YPRoomMicInfo : YPBaseObject

@property (nonatomic, assign) JXIMRoomMicPostionState posState; ///< 麦位状态, 默认开锁
@property (nonatomic, assign) JXIMRoomMicState micState; ///< 麦状态, 默认开麦

@end
