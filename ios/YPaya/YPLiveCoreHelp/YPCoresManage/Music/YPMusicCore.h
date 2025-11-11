//
//  YPMusicCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"
#import "YPMusicInfoModel.h"

//#import <AVFoundation/AVFoundation.h>

@interface YPMusicCore : YPBaseObject
//@property (nonatomic, strong) AVAudioPlayer *backgroundMusicPlayer;
@property (nonatomic, strong) NSMutableArray *items;
@property (nonatomic, strong) NSMutableArray *titleItems;
@property (nonatomic, strong, readonly) NSMutableArray<YPMusicInfoModel *> *musicInfoItems;//音乐的基本i信息
@property (nonatomic, strong) NSString *indexUrl;
@property (nonatomic, strong) NSString *indexSongTitle;
@property (nonatomic, assign) CGFloat musicSounds;
@property (nonatomic, assign) CGFloat manSounds;
@property (nonatomic, assign) BOOL hasPlayer;
@property (nonatomic, assign) BOOL isPlaying;
@property (nonatomic, strong, readonly) NSString *musicName;//歌曲名
@property (nonatomic, strong, readonly) NSString *musicSinger;//歌手名

- (void)playBackgroundMusicList;
- (void)removeBackgroundMusic:(NSString *)fileName;
- (void)playWithUrl:(NSString *)url withTitle:(NSString *)titleStr;
- (void)resumeMusic;
- (void)pauseMusic;
- (void)stopMusic;
- (void)playOneSong;
- (void)playNextSong;
- (void)playLastSong;
//- (BOOL)isPlayMusic;
- (void)changeMusicVolume:(CGFloat)volume;

- (void)changeManVolume:(CGFloat)volume;
/**
 获取当前播放的进度
 @return 当前播放进度，单位毫秒
 */
- (long)getCurrentDuration;
/**
 获取整个文件的播放时长
 @return 文件的播放时长，单位毫秒
 */
- (long)getDuration;
/*
 设置指定的进度进行播放
 @param millisecond 指定的进度，单位毫秒
 */
- (void)seekTo:(long)millisecond;

@end
