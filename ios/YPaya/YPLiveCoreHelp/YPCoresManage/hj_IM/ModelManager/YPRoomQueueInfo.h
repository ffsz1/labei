//
//  YPRoomQueueInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"
#import "YPRoomMicInfo.h"
#import "YPChatRoomMember.h"

/**
 麦序信息
 */
@interface YPRoomQueueInfo : YPBaseObject

@property (nonatomic, strong) YPRoomMicInfo *mic_info; ///< 坑位信息
@property (nonatomic, strong) YPChatRoomMember *chatRoomMember; ///< 房间用户信息, 如果没有member则无

@end
