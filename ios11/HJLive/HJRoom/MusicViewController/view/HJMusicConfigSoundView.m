//
//  HJMusicConfigSoundView.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMusicConfigSoundView.h"
#import "HJMusicCore.h"
#import "HJMeetingCore.h"

@implementation HJMusicConfigSoundView

- (void)awakeFromNib {
    [super awakeFromNib];
    UIImage *muimg = [UIImage imageNamed:@"xc_room_music_icon_jinduquan"];
    [self.sli1 setThumbImage:muimg forState:UIControlStateNormal];
    [self.sli1 setThumbImage:muimg forState:UIControlStateHighlighted];
    
    [self.sli2 setThumbImage:muimg forState:UIControlStateNormal];
    [self.sli2 setThumbImage:muimg forState:UIControlStateHighlighted];
    
    self.musicSoundsLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(HJMusicCore).musicSounds*100];
    self.manSoundsLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(HJMusicCore).manSounds*100];


    @weakify(self);
    [[self.sli1 rac_newValueChannelWithNilValue:nil] subscribeNext:^(NSString *x) {
        @strongify(self);
        GetCore(HJMusicCore).musicSounds = [x floatValue];
        self.musicSoundsLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(HJMusicCore).musicSounds*100];
        [GetCore(HJMusicCore) changeMusicVolume:[x floatValue]];
    }];
    
    [[self.sli2 rac_newValueChannelWithNilValue:nil] subscribeNext:^(id x) {
        @strongify(self);
        GetCore(HJMusicCore).manSounds = [x floatValue];
        self.manSoundsLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(HJMusicCore).manSounds*100];
        
        
//        [GetCore(MeetingCore).engine adjustRecordingSignalVolume:GetCore(HJMusicCore).manSounds*255];

        //即构
        [GetCore(HJMusicCore) changeManVolume:[x floatValue]];
    }];
}

@end
