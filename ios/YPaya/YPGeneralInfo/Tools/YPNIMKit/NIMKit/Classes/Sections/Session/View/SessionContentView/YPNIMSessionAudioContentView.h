//
//  NIMSessionAudioCententView.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import "YPNIMSessionMessageContentView.h"

@protocol NIMPlayAudioUIDelegate <NSObject>
-(void)startPlayingAudioUI;  //点击一开始就要显示
@optional
- (void)retryDownloadMsg; //重收消息
@end

@interface YPNIMSessionAudioContentView : YPNIMSessionMessageContentView

@property (nonatomic, strong) UILabel     *audioDurationLable; //语音时长

@property (nonatomic, weak) id<NIMPlayAudioUIDelegate> audioUIDelegate;

- (void)setPlaying:(BOOL)isPlaying;

@end
