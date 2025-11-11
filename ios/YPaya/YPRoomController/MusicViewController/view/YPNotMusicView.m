//
//  YPNotMusicView.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPNotMusicView.h"
#import "YPMusicCore.h"

@interface YPNotMusicView()

@end

@implementation YPNotMusicView

- (void)awakeFromNib {
    [super awakeFromNib];
    [self.slider setThumbImage:[UIImage imageNamed:@"xc_room_music_icon_jinduquan"] forState:UIControlStateNormal];
}

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self setView];
    }
    return self;
}

- (void)setView {
    self.tipSongLabel.text = GetCore(YPMusicCore).indexSongTitle;
}

- (IBAction)configClick:(UIButton *)sender {
    if (self.configMusicBlock) {
        self.configMusicBlock();
    }
}

- (IBAction)startClick:(UIButton *)sender {
    sender.selected = !sender.selected;
    
    if (GetCore(YPMusicCore).hasPlayer) {
        if (GetCore(YPMusicCore).isPlaying) {
            [GetCore(YPMusicCore) pauseMusic];
        } else {
            [GetCore(YPMusicCore) resumeMusic];
        }
    } else {
        [GetCore(YPMusicCore) playOneSong];
        if (GetCore(YPMusicCore).titleItems.count > 0) {
            self.tipSongLabel.text = GetCore(YPMusicCore).titleItems[0];
        }
    }
}

- (IBAction)sliderAction:(UISlider *)sender { 
    self.sliderBlock(sender.value);
}

@end
