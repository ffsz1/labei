//
//  HJIMQueueItem.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"
#import "HJRoomQueueInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJIMQueueItem : BaseObject

@property (nonatomic, assign) NSInteger position;
@property (nonatomic, strong) HJRoomQueueInfo *queueInfo;

@end

NS_ASSUME_NONNULL_END
