//
//  HJNotMusicView.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJNotMusicView.h"
#import "HJMusicCore.h"

@interface HJNotMusicView()

@end

@implementation HJNotMusicView

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
    self.tipSongLabel.text = GetCore(HJMusicCore).indexSongTitle;
}

- (IBAction)configClick:(UIButton *)sender {
    if (self.configMusicBlock) {
        self.configMusicBlock();
    }
}

- (IBAction)startClick:(UIButton *)sender {
    sender.selected = !sender.selected;
    
    if (GetCore(HJMusicCore).hasPlayer) {
        if (GetCore(HJMusicCore).isPlaying) {
            [GetCore(HJMusicCore) pauseMusic];
        } else {
            [GetCore(HJMusicCore) resumeMusic];
        }
    } else {
        [GetCore(HJMusicCore) playOneSong];
        if (GetCore(HJMusicCore).titleItems.count > 0) {
            self.tipSongLabel.text = GetCore(HJMusicCore).titleItems[0];
        }
    }
}

- (IBAction)sliderAction:(UISlider *)sender { 
    self.sliderBlock(sender.value);
}

@end
