//
//  HJGameRoomVC+ToolBar.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC.h"

@interface HJGameRoomVC (ToolBar) <XCToolBarDelegate>

- (void)updateToolBar;
- (void)addBadge;

- (void)updateMicAndSpeakerStauts;


@end
