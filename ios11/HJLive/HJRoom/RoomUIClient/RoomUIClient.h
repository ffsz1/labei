//
//  RoomUIClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol RoomUIClient <NSObject>
@optional

- (void)roomVCWillDisappear;
- (void)scrollToContributionView;
- (void)dismissFaceView;

@end
