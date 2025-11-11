//
//  HJMediaCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMediaCore.h"
#import <NIMSDK/NIMSDK.h>
#import "HJMediaCoreClient.h"

@interface HJMediaCore()<NIMMediaManagerDelegate>

@end
@implementation HJMediaCore
- (instancetype)init
{
    self = [super init];
    if (self) {
        [[NIMSDK sharedSDK].mediaManager addDelegate:self];
        [[NIMSDK sharedSDK].mediaManager setNeedProximityMonitor:NO];
        [[NIMSDK sharedSDK].mediaManager switchAudioOutputDevice:NIMAudioOutputDeviceSpeaker];
        [NIMSDK sharedSDK].mediaManager.recordProgressUpdateTimeInterval = 1;
    }
    return self;
}

- (void) record
{
    if (![[NIMSDK sharedSDK].mediaManager isRecording]) {
        [[NIMSDK sharedSDK].mediaManager record:NIMAudioTypeAAC duration:61];
    }
}

- (void) stopRecord
{
    [[NIMSDK sharedSDK].mediaManager stopRecord];
}

- (void)cancelRecord
{
    [[NIMSDK sharedSDK].mediaManager cancelRecord];
}

- (BOOL)isRecording
{
    return [[NIMSDK sharedSDK].mediaManager isRecording];
}

- (void) play:(NSString *)filePath
{
    if (filePath.length <= 0) {
        return;
    }
    [[NIMSDK sharedSDK].mediaManager play:filePath];
}

- (void) stopPlay
{
    [[NIMSDK sharedSDK].mediaManager stopPlay];
}

- (BOOL)isPlaying
{
    return [[NIMSDK sharedSDK].mediaManager isPlaying];
}

#pragma mark - NIMMediaManagerDelegate
- (void)recordAudio:(NSString *)filePath didBeganWithError:(NSError *)error
{
    if (error == nil && filePath.length > 0) {
        NotifyCoreClient(HJMediaCoreClient, @selector(onRecordAudioBegan:), onRecordAudioBegan:filePath);
    }
}

- (void)recordAudio:(NSString *)filePath didCompletedWithError:(NSError *)error
{
    if (error == nil && filePath.length > 0) {
        NotifyCoreClient(HJMediaCoreClient, @selector(onRecordAudioComplete:), onRecordAudioComplete:filePath);
    }
}

- (void)recordAudioDidCancelled
{
    NotifyCoreClient(HJMediaCoreClient, @selector(onRecordAudioCancel), onRecordAudioCancel);
}

- (void)recordAudioProgress:(NSTimeInterval)currentTime
{
    NotifyCoreClient(HJMediaCoreClient, @selector(onRecordAudioProgress:), onRecordAudioProgress:currentTime);
}

- (void)playAudio:(NSString *)filePath didBeganWithError:(NSError *)error
{
    NotifyCoreClient(HJMediaCoreClient, @selector(onPlayAudioBegan:), onPlayAudioBegan:filePath);
}

- (void)playAudio:(NSString *)filePath didCompletedWithError:(NSError *)error
{
    NotifyCoreClient(HJMediaCoreClient, @selector(onPlayAudioComplete:), onPlayAudioComplete:filePath);
}
@end
