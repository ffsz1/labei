//
//  ConferenceCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMeetingCore.h"
//#import "NIMAVChat.h"
//#import "NIMNetCallMeeting.h"
#import "YPAuthCoreHelp.h"
#import "HJMeetingCoreClient.h"
#import "UserInfo.h"
#import "YPUserCoreHelp.h"

#import "YPMusicCore.h"
#import "HJImRoomCoreClient.h"

#import "YPHttpRequestHelper+Im.h"




@interface YPMeetingCore()<HJImRoomCoreClient, AgoraRtcEngineDelegate>
@property(nonatomic, strong) NSMutableArray *speakingArray;
@property(nonatomic, assign) BOOL isSpeaking;
@end

@implementation YPMeetingCore

@synthesize actor = _actor;

- (instancetype)init
{
    self = [super init];
    if (self) {
        
        
        
         
        

        
        self.isMute = NO;
        _engine = [AgoraRtcEngineKit sharedEngineWithAppId:JX_AGORA_APP_ID delegate:self];
        //        [_engine setParameters:@"{\"che.audio.mixable.option\":true}"];
        //        [_engine setLogFilter:0];
        [_engine setParameters:@"{\"che.audio.keep.audiosession\":true}"];
    }
    return self;
}

- (void)rtcEngineLocalAudioMixingDidFinish:(AgoraRtcEngineKit *)engine {
    [GetCore(YPMusicCore) playNextSong];
}

- (void)dealloc
{
    //    [[NIMAVChatSDK sharedSDK].netCallManager removeDelegate:self];
}

- (void)reserveMeeting:(NSString *)name {
    //    NIMNetCallMeeting *meeting = [[NIMNetCallMeeting alloc] init];
    //    meeting.name = name;
    //    meeting.type = NIMNetCallMediaTypeAudio;
    //
    //    [[NIMAVChatSDK sharedSDK].netCallManager reserveMeeting:meeting completion:^(NIMNetCallMeeting * _Nonnull meeting, NSError * _Nonnull error) {
    //        if (error == nil) {
    //            [YYLogger info:@"reserve meeting" message:@"reserve meeting success-->%@", name];
    //            NotifyCoreClient(MeetingCoreClient, @selector(onReserveMeetingSuccess), onReserveMeetingSuccess);
    //        } else {
    //            [YYLogger info:@"reserve meeting" message:@"reserve meeting failth-->%@", name];
    //            NotifyCoreClient(MeetingCoreClient, @selector(onReserveMeetingFailth), onReserveMeetingFailth);
    //        }
    //        [self joinMeeting:name actor:YES];
    //    }];
}

- (void)joinMeeting:(NSString *)name actor:(BOOL) actor
{
    
//    getAgoraKeyWith
    [self.engine setChannelProfile:AgoraChannelProfileLiveBroadcasting];
    if (actor) {
        //[self.engine enableAudio];
        [self.engine disableVideo];
        [self.engine setClientRole:AgoraClientRoleBroadcaster];
    }else {
        // [self.engine enableAudio];
        [self.engine disableVideo];
        [self.engine setClientRole:AgoraClientRoleAudience];
    }

    
    [self.engine setDefaultAudioRouteToSpeakerphone:YES];
    [self.engine setAudioProfile:AgoraAudioProfileDefault scenario:AgoraAudioScenarioChatRoomEntertainment];
    
    self.isMute = NO;
    
    //    [self.engine setEnableSpeakerphone:false];
    NSUInteger uid = [[GetCore(YPAuthCoreHelp) getUid] integerValue];
//    [self.engine enableAudioVolumeIndication:600 smooth:3];
    [self.engine enableAudioVolumeIndication:600 smooth:3 report_vad:YES];
    
    NSString *path = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) safeObjectAtIndex:0];
    path = [path stringByAppendingString:@"/Agorasdk.log"];
    [self.engine setLogFile:path];
    
    __weak typeof(name) weakName = name;
    __weak typeof(self) weakSelf = self;
    [YPHttpRequestHelper getAgoraKeyWith:name uid:[GetCore(YPAuthCoreHelp) getUid] success:^(NSString *token) {
        
        int state = [weakSelf.engine joinChannelByToken:token channelId:weakName info:@"" uid:uid joinSuccess:^(NSString * _Nonnull channel, NSUInteger uid, NSInteger elapsed) {
            [YYLogger info:@"JoinAgoraChannelSuccess" message:@"success--->stat:%ld",(long)elapsed];
            NotifyCoreClient(HJMeetingCoreClient, @selector(onJoinMeetingSuccess), onJoinMeetingSuccess);
        }];
        
        if (state != 0) {
            [YYLogger info:@"JoinAgoraChannelFailure" message:@"failure--->stat:%d",state];
            NotifyCoreClient(HJMeetingCoreClient, @selector(onJoinMeetingFailth), onJoinMeetingFailth);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJMeetingCoreClient, @selector(onJoinMeetingFailth), onJoinMeetingFailth);
    }];
    
    
    
//    int state = [self.engine joinChannelByToken:nil channelId:name info:@"" uid:uid joinSuccess:^(NSString * _Nonnull channel, NSUInteger uid, NSInteger elapsed) {
//        [YYLogger info:@"JoinAgoraChannelSuccess" message:@"success--->stat:%ld",(long)elapsed];
//        NotifyCoreClient(MeetingCoreClient, @selector(onJoinMeetingSuccess), onJoinMeetingSuccess);
//    }];
//
//    if (state != 0) {
//        [YYLogger info:@"JoinAgoraChannelFailure" message:@"failure--->stat:%d",state];
//        NotifyCoreClient(MeetingCoreClient, @selector(onJoinMeetingFailth), onJoinMeetingFailth);
//    }
}

- (BOOL)setMute:(BOOL)isMute {

    BOOL result = NO;
    //    [self engine muteAllRemoteAudioStreams:isMute];
    
    if ([self.engine muteAllRemoteAudioStreams:isMute] == 0) {
        result = YES;
    }else
    {
        result = NO;
    }
    if (result) {
        self.isMute = isMute;
    }
    NSLog(@"mute%d",self.isMute);
    return result;
}

- (BOOL)setMeetingRole:(BOOL)actor {

    
    BOOL result = NO;
    if (actor) {
        //        [self.engine enableAudio];
        if ([self.engine setClientRole:AgoraClientRoleBroadcaster] == 0) {
            result = YES;
        } else {
            result = NO;
        }
        [self.engine muteLocalAudioStream:NO];
    } else {
        //        [self.engine disableAudio];
        if ([self.engine setClientRole:AgoraClientRoleAudience] == 0) {
            result = YES;
        }else {
            result = NO;
        }
        [self.engine muteLocalAudioStream:YES];
    }
    //    BOOL result = [[NIMAVChatSDK sharedSDK].netCallManager setMeetingRole:actor];
    if (result) {
        _actor = actor;
    }
    if (self.isCloseMicro) {
        [self setCloseMicro:self.isCloseMicro];
    }
    return YES;
}

- (BOOL)setCloseMicro:(BOOL)close {
    
    
//    if (GetCore(ImRoomCoreV2).currentRoomInfo.type == RoomType_Game) {
        BOOL result = NO;
        if (close) {
            if ([self.engine muteLocalAudioStream:YES] == 0) {
                [self.engine setClientRole:AgoraClientRoleAudience];
                result = YES;
            } else {
                result = NO;
            }
        } else {
            if ([self.engine muteLocalAudioStream:NO] == 0) {
                [self.engine setClientRole:AgoraClientRoleBroadcaster];
                result = YES;
            } else {
                result = NO;
            }
        }
        if (result) {
            self.isCloseMicro = close;
        }

        return result;

//    }
//    if (GetCore(ImRoomCoreV2).myMember.type == NIMChatroomMemberTypeCreator) {
//        if (!self.isPlaying) {
//            [self.engine muteLocalAudioStream:close];
//        }
//        self.isCloseMicro = close;
//        return YES;
//    }
//        [self.engine muteLocalAudioStream:close];
//        self.isCloseMicro = close;
//        return YES;
}

- (void)leaveMeeting:(NSString *)name
{
    
    
    
    //    NIMNetCallMeeting *meeting = [[NIMNetCallMeeting alloc] init];
    //    meeting.name = name;
    //    [[NIMAVChatSDK sharedSDK].netCallManager leaveMeeting:meeting];
    self.actor = NO;
    //    [self.engine leaveChannel:^(AgoraRtcStats *stat) {
    //
    //    }];
    
    int code = [self.engine leaveChannel:^(AgoraChannelStats * _Nonnull stat) {
//        if (stat == 0) {
//            [YYLogger info:@"leaveAgoraChannelSuccess" message:@"success--->stat:%@",stat];
//            NotifyCoreClient(MeetingCoreClient, @selector(onLeaveMeetingSuccess), onLeaveMeetingSuccess);
//        }else {
//            [YYLogger info:@"leaveAgoraChannelfailure" message:@"failure--->stat:%@",stat];
//        }
    }];
    if (code == 0) {
        NotifyCoreClient(HJMeetingCoreClient, @selector(onLeaveMeetingSuccess), onLeaveMeetingSuccess);
    }
}

#pragma mark - NIMChatroomManager
- (void)fetchChatRoomMembers:(NSArray *)member {
    NIMChatroomMemberRequest *request = [[NIMChatroomMemberRequest alloc]init];
    request.roomId = [NSString stringWithFormat:@"%lld",GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
    request.limit = 200;
    @weakify(self);
    [[NIMSDK sharedSDK].chatroomManager fetchChatroomMembers:request completion:^(NSError * _Nullable error, NSArray<NIMChatroomMember *> * _Nullable members) {
        @strongify(self);
        [self.membersArray addObjectsFromArray:members];
    }];
}

#pragma mark - AgoraRtcEngineDelegate
- (void)rtcEngine:(AgoraRtcEngineKit *)engine lastmileQuality:(AgoraNetworkQuality)quality {
    if (quality >= 3) {
        NotifyCoreClient(HJMeetingCoreClient, @selector(onMeetingQualityBad), onMeetingQualityBad);
    }
}

- (void)rtcEngineConnectionDidLost:(AgoraRtcEngineKit *)engine {
    NotifyCoreClient(HJMeetingCoreClient, @selector(onMeetingQualityDown), onMeetingQualityDown);
}

- (void)rtcEngine:(AgoraRtcEngineKit *)engine
    didAudioMuted:(BOOL)muted byUid:(NSUInteger)uid{
    if (muted) {
        if (uid == 0) {
            //            [self onMyVolumeUpdate:0];
        }else {
            
        }
    }
}

- (void)rtcEngine:(AgoraRtcEngineKit *)engine didLeaveChannelWithStats:(AgoraChannelStats *)stats {
    
}

- (void)rtcEngine:(AgoraRtcEngineKit *)engine reportAudioVolumeIndicationOfSpeakers:(NSArray *)speakers totalVolume:(NSInteger)totalVolume {
    [self onSpeakingUsersReport:speakers];
}

//#pragma mark - NIMNetCallManagerDelegate
- (void)onSpeakingUsersReport:(NSArray *)report {
    NSMutableArray *uids = [NSMutableArray array];
    if (report != nil && report.count > 0) {
        for (AgoraRtcAudioVolumeInfo *userInfo in report) {
            NSString *uid = [NSString stringWithFormat:@"%ld",(long)userInfo.uid];
            if (uid.userIDValue == 0) {
                if (userInfo.volume > 30) {
                    [uids addObject:@([GetCore(YPAuthCoreHelp) getUid].userIDValue)];
                }
            }else {
                NSArray* mircoList = GetCore(YPImRoomCoreV2).micMembers;


                BOOL isOnMic = NO;
                for (YPChatRoomMember* model in mircoList) {
                    if([model.account isEqualToString:uid]){
                        isOnMic = YES;
                        break;
                    }else{
                        isOnMic = NO;
                    }
                }

                if (isOnMic) {
                    [self.engine muteRemoteAudioStream:[uid integerValue] mute:NO];
                }else{
                    [self.engine muteRemoteAudioStream:[uid integerValue] mute:YES];
                }
                if (userInfo.volume > 30) {
                    [uids addObject:@(uid.userIDValue)];
                }
            }
        }
        self.speakingArray = uids;
        NotifyCoreClient(HJMeetingCoreClient, @selector(onSpeakingUsersReport:), onSpeakingUsersReport:self.speakingArray);
    } else {
        if (self.speakingArray != nil) {
            self.speakingArray = nil;
            NotifyCoreClient(HJMeetingCoreClient, @selector(onSpeakingUsersReport:), onSpeakingUsersReport:nil);
        }
    }
}

- (void)onMyVolumeUpdate:(NSUInteger)volume {
    [YYLogger info:@"onMyVolumeUpdate" message:@"volume--->%lu",(unsigned long)volume];
    if (volume > 30) {
        self.isSpeaking = YES;
        NotifyCoreClient(HJMeetingCoreClient, @selector(onMySpeakingStateUpdate:), onMySpeakingStateUpdate:YES);
    }
}

-(BOOL)isCloseMicro
{

    
    return _isCloseMicro;
}

- (BOOL)actor
{
    
    return _actor;
}

- (void)setActor:(BOOL)actor {
    
   
        _actor = actor;
        [self setMeetingRole:actor];
}

- (BOOL)isMute
{
    
    return _isMute;
}



@end
