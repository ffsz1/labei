//
//  YPGameRoomVC+CoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomVC.h"
#import "HJPraiseCoreClient.h"

@interface YPGameRoomVC (CoreClient)
<
    HJRoomQueueCoreClient,
    HJRoomCoreClient,
    HJMeetingCoreClient,
    HJImMessageCoreClient,
    HJPraiseCoreClient,
    HJActivityCoreClient,
    HJImRoomCoreClient,
    HJImMessageSendCoreClient
>

- (void)updateRoomOwnerMicState;
@end
