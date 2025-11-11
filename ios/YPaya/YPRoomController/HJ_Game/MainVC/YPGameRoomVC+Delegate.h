//
//  YPGameRoomVC+Delegate.h
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomVC.h"
#import "HJOnlineCoreClient.h"

@interface YPGameRoomVC (Delegate)
<
    MessageTableViewDelegate,
    HJGameRoomPositionViewDelegate,
    HJGiftViewContainerDelegate,
    HJRoomQueueCoreClient,
    HJOnlineCoreClient
>


@end
