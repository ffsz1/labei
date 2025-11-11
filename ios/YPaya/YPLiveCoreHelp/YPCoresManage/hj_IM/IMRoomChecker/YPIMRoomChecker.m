//
//  YPIMRoomChecker.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMRoomChecker.h"

#import "YPAuthCoreHelp.h"
#import "YPImRoomCoreV2.h"
#import "YPMeetingCore.h"




@implementation YPIMRoomChecker

+ (void)startChecker
{
    //
    [GetCore(YPMeetingCore) leaveMeeting:@""];
    
    NSTimer *timer = [NSTimer timerWithTimeInterval:2.0f target:self selector:@selector(checkLogin) userInfo:nil repeats:YES];
    [[NSRunLoop mainRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
}

//检查是否登录
+ (void)checkLogin
{
    if (GetCore(YPAuthCoreHelp).isLogin) {
        [self checkInRoom];
    }else{
        [GetCore(YPMeetingCore) leaveMeeting:@""];
    }
}

//检测是否在房间
+ (void)checkInRoom
{
    if (GetCore(YPImRoomCoreV2).currentRoomInfo == nil) {
        [GetCore(YPMeetingCore) leaveMeeting:@""];
    }else{
        [self checkOnMicQueue];
        [self checkSocketAudioSync];
    }
}

//检测是否在麦上
+ (void)checkOnMicQueue
{
    UserID uid = GetCore(YPAuthCoreHelp).getUid.userIDValue;
    BOOL isOnMicro = [GetCore(YPImRoomCoreV2) isOnMicro:uid];
    
    if (isOnMicro) {
//        [self checkDeviceMicStatus];
        [self checkDeviceSpeakerStatus];
    }else{
        [GetCore(YPMeetingCore) setMeetingRole:NO];
    }
    
}

//检测设备麦克风的状态是否一致
+ (void)checkDeviceMicStatus
{
    [[NSNotificationCenter defaultCenter] postNotificationName:IM_ROOM_DEVICE_MIC_STATUS object:nil userInfo:nil];
}

//检测设备扬声器的状态是否一致
+ (void)checkDeviceSpeakerStatus
{
    [[NSNotificationCenter defaultCenter] postNotificationName:IM_ROOM_DEVICE_SPEAKER_STATUS object:nil userInfo:nil];
//    GetCore(MeetingCore).isMute;
}

//检测socket、即构状态是否同步
+ (void)checkSocketAudioSync
{
    
}



@end
