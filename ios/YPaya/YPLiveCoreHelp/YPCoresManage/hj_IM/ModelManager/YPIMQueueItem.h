//
//  YPIMQueueItem.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"
#import "YPRoomQueueInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPIMQueueItem : YPBaseObject

@property (nonatomic, assign) NSInteger position;
@property (nonatomic, strong) YPRoomQueueInfo *queueInfo;

@end

NS_ASSUME_NONNULL_END
