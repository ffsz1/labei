//
//  XCManagerSettingControllerViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YYViewController.h"

@interface HJManagerSettingController : YYViewController
typedef enum {
    RoomBlackList = 1,
    RoomManager = 2,
}PageType;


@property (assign, nonatomic) PageType type; // 0

@end
