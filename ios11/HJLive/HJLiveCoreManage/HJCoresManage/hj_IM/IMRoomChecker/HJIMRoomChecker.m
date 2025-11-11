//
//  HJIMRoomChecker.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJIMRoomChecker.h"

#import "HJAuthCoreHelp.h"
#import "HJImRoomCoreV2.h"
#import "HJMeetingCore.h"




@implementation HJIMRoomChecker

+ (void)startChecker
{
    //
    [GetCore(HJMeetingCore) leaveMeeting:@""];
    
    NSTimer *timer = [NSTimer timerWithTimeInterval:2.0f target:self selector:@selector(checkLogin) userInfo:nil repeats:YES];
    [[NSRunLoop mainRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
}

//检查是否登录
+ (void)checkLogin
{
    if (GetCore(HJAuthCoreHelp).isLogin) {
        [self checkInRoom];
    }else{
        [GetCore(HJMeetingCore) leaveMeeting:@""];
    }
}

//检测是否在房间
+ (void)checkInRoom
{
    if (GetCore(HJImRoomCoreV2).currentRoomInfo == nil) {
        [GetCore(HJMeetingCore) leaveMeeting:@""];
    }else{
        [self checkOnMicQueue];
        [self checkSocketAudioSync];
    }
}

//检测是否在麦上
+ (void)checkOnMicQueue
{
    UserID uid = GetCore(HJAuthCoreHelp).getUid.userIDValue;
    BOOL isOnMicro = [GetCore(HJImRoomCoreV2) isOnMicro:uid];
    
    if (isOnMicro) {
//        [self checkDeviceMicStatus];
        [self checkDeviceSpeakerStatus];
    }else{
        [GetCore(HJMeetingCore) setMeetingRole:NO];
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
