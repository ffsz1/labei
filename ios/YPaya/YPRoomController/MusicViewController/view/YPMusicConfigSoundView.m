//
//  YPMusicConfigSoundView.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMusicConfigSoundView.h"
#import "YPMusicCore.h"
#import "YPMeetingCore.h"

@implementation YPMusicConfigSoundView

- (void)awakeFromNib {
    [super awakeFromNib];
    UIImage *muimg = [UIImage imageNamed:@"xc_room_music_icon_jinduquan"];
    [self.sli1 setThumbImage:muimg forState:UIControlStateNormal];
    [self.sli1 setThumbImage:muimg forState:UIControlStateHighlighted];
    
    [self.sli2 setThumbImage:muimg forState:UIControlStateNormal];
    [self.sli2 setThumbImage:muimg forState:UIControlStateHighlighted];
    
    self.musicSoundsLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(YPMusicCore).musicSounds*100];
    self.manSoundsLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(YPMusicCore).manSounds*100];


    @weakify(self);
    [[self.sli1 rac_newValueChannelWithNilValue:nil] subscribeNext:^(NSString *x) {
        @strongify(self);
        GetCore(YPMusicCore).musicSounds = [x floatValue];
        self.musicSoundsLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(YPMusicCore).musicSounds*100];
        [GetCore(YPMusicCore) changeMusicVolume:[x floatValue]];
    }];
    
    [[self.sli2 rac_newValueChannelWithNilValue:nil] subscribeNext:^(id x) {
        @strongify(self);
        GetCore(YPMusicCore).manSounds = [x floatValue];
        self.manSoundsLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(YPMusicCore).manSounds*100];
        
        
//        [GetCore(MeetingCore).engine adjustRecordingSignalVolume:GetCore(YPMusicCore).manSounds*255];

        //即构
        [GetCore(YPMusicCore) changeManVolume:[x floatValue]];
    }];
}

@end
