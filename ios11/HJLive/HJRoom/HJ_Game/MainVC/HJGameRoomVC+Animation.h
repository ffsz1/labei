//
//  HJGameRoomVC+Animation.h
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC.h"

@interface HJGameRoomVC (Animation)

- (void)updateMainPositionMicroAnimation;//更新房主说话状态动画
- (void)addTheGiftAnimationWith:(CGPoint)orginPoint destinationPoint:(CGPoint)destinationPoint withGiftPic:(NSURL *)giftPic; //添加礼物动画
@end
