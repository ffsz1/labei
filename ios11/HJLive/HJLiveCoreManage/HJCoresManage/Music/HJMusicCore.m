//
//  HJMusicCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMusicCore.h"
#import "HJMusicCoreClient.h"
#import "HJMeetingCore.h"
#import <YYCategories.h>
#import "HJMusicInfoModel.h"

@interface HJMusicCore ()
@property (nonatomic, strong) NSMutableArray<HJMusicInfoModel *> *musicInfoItems;//音乐的基本i信息
@property (nonatomic, strong) HJMusicInfoModel *currentMusicInfo;//音乐的基本i信息
@end

@implementation HJMusicCore

- (void)changeMusicVolume:(CGFloat)volume {
    

    
    [GetCore(HJMeetingCore).engine adjustAudioMixingVolume:volume * 100];
    self.musicSounds = volume;
//    if (self.backgroundMusicPlayer) {
//        self.backgroundMusicPlayer.volume = volume;
//        self.musicSounds = volume;
//    }
}

- (void)changeManVolume:(CGFloat)volume
{
    
    
    
    [GetCore(HJMeetingCore).engine adjustRecordingSignalVolume:GetCore(HJMusicCore).manSounds*255];
}

- (void)playNextSong {
    
   
    
    [self.items enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj isEqualToString:self.indexUrl]) {
            if (self.items.count > (idx + 1)) {
                [self playWithUrl:[self.items safeObjectAtIndex:idx+1] withTitle:[self.titleItems safeObjectAtIndex:idx+1]];
            } else {
                [self playWithUrl:[self.items safeObjectAtIndex:0] withTitle:[self.titleItems safeObjectAtIndex:0]];
            }
            *stop = YES;
        }
    }];
}

- (void)playLastSong {
    
   
    
    [self.items enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj isEqualToString:self.indexUrl]) {
            if (idx - 1 >= 0) {
                [self playWithUrl:[self.items safeObjectAtIndex:idx-1] withTitle:[self.titleItems safeObjectAtIndex:idx-1]];
            } else {
                [self playWithUrl:self.items.lastObject withTitle:self.titleItems.lastObject];
            }
            *stop = YES;
        }
    }];
}

- (instancetype)init {
    self = [super init];
    if (self) {
        
        
        
        self.manSounds = 1.0;
        self.musicSounds = 1.0;
        self.hasPlayer = false;
        self.isPlaying = false;
    }
    return self;
}

- (void)stopMusic {
    

    
    [GetCore(HJMeetingCore).engine stopAudioMixing];
    self.isPlaying = false;
    self.hasPlayer = false;
    self.indexUrl = @"";
    self.indexSongTitle = @"";
    NotifyCoreClient(HJMusicCoreClient, @selector(stopMusicNoti), stopMusicNoti);
}

- (void)resumeMusic {
    
    
   
    
//    [self.backgroundMusicPlayer pause];
    [GetCore(HJMeetingCore).engine resumeAudioMixing];
    self.isPlaying = YES;
}

- (void)pauseMusic {
    
    
    
    [GetCore(HJMeetingCore).engine pauseAudioMixing];
    self.isPlaying = false;
//    [self.backgroundMusicPlayer stop];
}

- (void)removeBackgroundMusic:(NSString *)fileName {
    
    
   
    
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSError *error;
    if ([fileManager fileExistsAtPath:fileName]) {
        [fileManager removeItemAtPath:fileName error:&error];
        if (!error) {
            if ([_indexUrl isEqualToString:fileName]) {
                [self stopMusic];
            }
            [self playBackgroundMusicList];
        }
    }
}

- (void)playOneSong {
    
    
    
    
    if (self.items.count > 0) {
        [self playWithUrl:[self.items safeObjectAtIndex:0] withTitle:[self.titleItems safeObjectAtIndex:0]];
    }
}

- (void)playBackgroundMusicList {
    
    
    
    //获取播放文件的位置,通过文件名和后缀
    NSString *docDirPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask, YES) objectAtIndex:0];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if (![fileManager fileExistsAtPath:docDirPath]) {return;}
    NSArray *tempFileList = [[NSArray alloc] initWithArray:[fileManager contentsOfDirectoryAtPath:docDirPath error:nil]];
    
    [self.items removeAllObjects];
    [self.titleItems removeAllObjects];
    [self.musicInfoItems removeAllObjects];
    
    for(NSString *string in tempFileList) {
        if ([string containsString:@"mp3"]) {
            NSString *url = [NSString stringWithFormat:@"%@/%@",docDirPath,string];
            [self.items addObject:url];
            [self.titleItems addObject:string];
            [self getMusicInfoWithUrl:url title:string];
        }
    }
    NotifyCoreClient(HJMusicCoreClient, @selector(updateList), updateList);
}

- (void)playWithUrl:(NSString *)url withTitle:(NSString *)titleStr {
    
    
    
    
    //如果找不到文件则返回错误
    if (url == nil || url.length == 0) {
        return;
    }
    //获取音乐信息
    for (int i=0; i<self.items.count; i++) {
        if ([[self.items safeObjectAtIndex:i] isEqualToString:url]) {
            self.currentMusicInfo = [self.musicInfoItems safeObjectAtIndex:i];
        }
    }
    
//    NSError *error = nil;
    
    //将播放文件赋值给播放器
//    self.backgroundMusicPlayer = nil;
//    self.backgroundMusicPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:url error:&error];
//
//    //如果建立不了播放器则，提错
//    if (self.backgroundMusicPlayer == nil) {
//        return;
//    }
    
    if ([url containsString:@"file://"] && url.length > 7) {
        url = [url substringFromIndex:7];
    }
    
    int result = [GetCore(HJMeetingCore).engine startAudioMixing:url loopback:false replace:false cycle:1];
    result == 0 ? (self.hasPlayer = YES) : (self.hasPlayer = false);
    if (self.hasPlayer) {
        self.isPlaying = YES;
        self.indexUrl = url;
        self.indexSongTitle = titleStr;
    }
    
    NotifyCoreClient(HJMusicCoreClient, @selector(changeIndexTitle), changeIndexTitle);
//    [self.backgroundMusicPlayer play];
    //    [self.navigationController popViewControllerAnimated:YES];
}

- (void)getMusicInfoWithUrl:(NSString *)url title:(NSString *)title{
    //获取基本信息
    HJMusicInfoModel *infoModel = [[HJMusicInfoModel alloc] init];
    AVURLAsset *musicAsset = [AVURLAsset URLAssetWithURL:[NSURL fileURLWithPath:url] options:nil];
    infoModel.duration = CMTimeGetSeconds(musicAsset.duration);
    for (NSString *format in [musicAsset availableMetadataFormats]) {
        for (AVMetadataItem *metadataItem in [musicAsset metadataForFormat:format]) {
            if([metadataItem.commonKey isEqualToString:@"title"]){
                NSString *buffer = [self convertGBStringToUTF8String:(NSString *)metadataItem.value];
                infoModel.musicName = buffer.length ? buffer : title;//歌曲名
            }else if ([metadataItem.commonKey isEqualToString:@"artist"]){
                NSString *buffer = [self convertGBStringToUTF8String:(NSString *)metadataItem.value];
                infoModel.musicSinger = buffer.length ? buffer : @"未知";;//歌手
            }
        }
    }
    if (!infoModel.musicName) {
        infoModel.musicName = title;//歌曲名
    }
    if (!infoModel.musicSinger) {
        infoModel.musicSinger = @"未知";//歌手
    }
    [self.musicInfoItems addObject:infoModel];
}

/**
 获取当前播放的进度
 @return 当前播放进度，单位毫秒
 */
- (long)getCurrentDuration {
  
        return GetCore(HJMeetingCore).engine.getAudioMixingCurrentPosition;
}
/**
 获取整个文件的播放时长
 @return 文件的播放时长，单位毫秒
 */
- (long)getDuration {
    
        return GetCore(HJMeetingCore).engine.getAudioMixingDuration;
}
/**
 设置指定的进度进行播放
 @param millisecond 指定的进度，单位毫秒
 */
- (void)seekTo:(long)millisecond {
    
        [GetCore(HJMeetingCore).engine setAudioMixingPosition:millisecond];
}

/**
 GB转为UTF8
 
 @param string GBString
 @return UTF8String
 */
- (NSString *)convertGBStringToUTF8String:(NSString *)string {
    if (!string) return nil;
    if (!string.length) return nil;
    
    return string;
    
//    NSData *data = [string dataUsingEncoding:NSUTF8StringEncoding];
//    NSStringEncoding GBEncoding = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
//    NSString *GBString = [[NSString alloc] initWithData:data encoding:GBEncoding];
//    if (GBString) {
//        NSInteger max = [string length];
//        char *bytes = malloc(max + 1);
//        for (int i = 0; i < max; i++) {
//            unichar character = [string characterAtIndex:i];
//            bytes[i] = (char)character;
//        }
//        bytes[max] = '\0';
//        NSString *buffer = [NSString stringWithCString:bytes encoding:GBEncoding];
//        free(bytes);
//        if (buffer) return buffer;
//    }
//
//    return [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
}

#pragma mark - getter/setter

//- (BOOL)isPlayMusic {
//    return false;
//    if (GetCore(HJMusicCore).backgroundMusicPlayer) {
//        if ([GetCore(HJMusicCore).backgroundMusicPlayer isPlaying]) {
//            return YES;
//        } else {
//            return false;
//        }
//    }
//        return false;
//}

- (NSMutableArray *)items {
    
    
    
    if (!_items) {
        _items = [NSMutableArray array];
    }
    return _items;
}

- (NSMutableArray *)titleItems {
    
    
    
    if (!_titleItems) {
        _titleItems = [NSMutableArray array];
    }
    return _titleItems;
}

- (NSMutableArray<HJMusicInfoModel *> *)musicInfoItems{
    
   
    if (!_musicInfoItems) {
        _musicInfoItems = [NSMutableArray array];
    }
    return _musicInfoItems;
}

- (NSString *)indexUrl
{
    
    return _indexUrl;
}

- (NSString *)indexSongTitle
{
    
    return _indexSongTitle;
}

- (CGFloat)musicSounds
{
    
    return _musicSounds;
}

- (CGFloat)manSounds
{
    
    return _manSounds;
}

- (BOOL)hasPlayer
{
    
    return _hasPlayer;
}

- (BOOL)isPlaying
{
    
    return _isPlaying;
}

- (NSString *)musicName {
    
    
    //    return self.indexSongTitle;
    return self.currentMusicInfo.musicName;
}

- (NSString *)musicSinger {
    
    
    return self.currentMusicInfo.musicSinger;
}


//@property (nonatomic, assign) BOOL hasPlayer;
//@property (nonatomic, assign) BOOL isPlaying;

@end
