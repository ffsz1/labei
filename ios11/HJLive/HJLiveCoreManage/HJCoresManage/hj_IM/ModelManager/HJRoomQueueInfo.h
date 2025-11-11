//
//  HJRoomQueueInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"
#import "HJRoomMicInfo.h"
#import "ChatRoomMember.h"

/**
 麦序信息
 */
@interface HJRoomQueueInfo : BaseObject

@property (nonatomic, strong) HJRoomMicInfo *mic_info; ///< 坑位信息
@property (nonatomic, strong) ChatRoomMember *chatRoomMember; ///< 房间用户信息, 如果没有member则无

@end
