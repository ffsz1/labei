//
//  YPIMRoomChecker.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

#define IM_ROOM_DEVICE_MIC_STATUS @"IMRoomDeviceMicStatus"
#define IM_ROOM_DEVICE_SPEAKER_STATUS @"IMRoomDeviceSpeakerStatus"

NS_ASSUME_NONNULL_BEGIN

@interface YPIMRoomChecker : NSObject

+ (void)startChecker;

+ (void)checkDeviceMicStatus;

+ (void)checkDeviceSpeakerStatus;

@end

NS_ASSUME_NONNULL_END
