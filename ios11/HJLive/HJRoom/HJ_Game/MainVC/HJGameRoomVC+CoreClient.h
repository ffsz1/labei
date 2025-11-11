//
//  HJGameRoomVC+CoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC.h"
#import "HJPraiseCoreClient.h"

@interface HJGameRoomVC (CoreClient)
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
