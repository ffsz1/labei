//
//  YPGameRoomVC+ToolBar.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomVC.h"

@interface YPGameRoomVC (ToolBar) <XCToolBarDelegate>

- (void)updateToolBar;
- (void)addBadge;

- (void)updateMicAndSpeakerStauts;


@end
