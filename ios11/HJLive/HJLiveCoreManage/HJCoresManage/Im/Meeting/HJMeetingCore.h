//
//  ConferenceCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import "HJImRoomCoreV2.h"
//#import <AgoraRtcEngineKit/AgoraRtcEngineKit.h>
#import <AgoraRtcKit/AgoraRtcEngineKit.h>
//即构 
#define isUseZego (GetCore(HJImRoomCoreV2).currentRoomInfo.audioChannel == JXIMRoomAudioChannelZego)

typedef enum {
    Meeting_Action_Join= 0,//加入
    Meeting_Action_Create = 1//创建
}MeetingAction;
@interface HJMeetingCore : BaseCore
@property(nonatomic, strong) AgoraRtcEngineKit *engine;
@property(nonatomic, assign) BOOL isCloseMicro;
@property(nonatomic, assign) BOOL actor;
@property(nonatomic, assign) BOOL isMute;

@property (nonatomic, assign) BOOL isPlaying;//ktv是否在播放

@property(nonatomic, strong)NSMutableArray<NIMChatroomMember *> *membersArray; //成员

- (void)joinMeeting:(NSString *)name actor:(BOOL) actor;//进入房间
- (void)leaveMeeting:(NSString *)name;//离开房间
- (void)fetchChatRoomMembers:(NSArray *)member;

- (BOOL)setMeetingRole:(BOOL)actor;//设置为主播
- (BOOL)setCloseMicro:(BOOL)close;//关扬声器，不听其他人的声音
- (BOOL)setMute:(BOOL)isMute;//关麦 不能说话
@end
