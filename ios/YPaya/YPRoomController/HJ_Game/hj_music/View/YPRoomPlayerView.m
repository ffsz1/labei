//
//  YPRoomPlayerView.m
//  HJLive
//
//  Created by feiyin on 2020/7/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomPlayerView.h"

#import "YPMusicViewController.h"

#import "YPMusicCore.h"
#import "YPMusicCoreClient.h"
#import "UIView+getTopVC.h"

#import "YPRoomViewControllerCenter.h"


@interface YPRoomPlayerView ()<YPMusicCoreClient>
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIButton *playBtn;
@property (weak, nonatomic) IBOutlet UISlider *slider;

@property (weak, nonatomic) IBOutlet UISlider *manVoiceSlider;

@property (weak, nonatomic) IBOutlet UIView *superView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_musicView;
@end

@implementation YPRoomPlayerView

+ (void)show
{
    YPRoomPlayerView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPRoomPlayerView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    shareView.left_musicView.constant = - shareView.width;
    
    [shareView layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        shareView.left_musicView.constant = 0;
        [shareView layoutIfNeeded];
    }];
    
    [shareView configMusic];
}

- (void)close
{
//    [self removeFromSuperview];
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.left_musicView.constant = - self.width;
        [self layoutIfNeeded];
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
    
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    _superView.layer.masksToBounds = YES;
    _superView.layer.cornerRadius = 10;
    AddCoreClient(YPMusicCoreClient, self);
    
    [[self.slider rac_newValueChannelWithNilValue:nil] subscribeNext:^(NSString * x) {
        [GetCore(YPMusicCore) changeMusicVolume:[x floatValue]];
    }];
    [[self.manVoiceSlider rac_newValueChannelWithNilValue:nil] subscribeNext:^(NSString * x) {
        [GetCore(YPMusicCore) changeManVolume:[x floatValue]];
    }];
    
}

- (void)configMusic {
    [self.slider setThumbImage:[UIImage imageNamed:@"yp_room_music_point"] forState:UIControlStateNormal];
    
    [self.manVoiceSlider setThumbImage:[UIImage imageNamed:@"yp_room_music_point"] forState:UIControlStateNormal];
    
    self.playBtn.selected = GetCore(YPMusicCore).isPlaying;
    if (GetCore(YPMusicCore).indexSongTitle.length > 0) {
        self.nameLabel.text = GetCore(YPMusicCore).indexSongTitle;
    }
    
    self.slider.value = GetCore(YPMusicCore).musicSounds;
    
    self.manVoiceSlider.value = GetCore(YPMusicCore).manSounds;
}

- (IBAction)musicListAction:(id)sender {
    
    
    [self removeFromSuperview];
    
//    UIViewController *vc = YPRoomStoryBoard(@"YPRoomMusciListVC");
//
//    [[[YPRoomViewControllerCenter defaultCenter]getCurrentVC].navigationController pushViewController:vc animated:YES];
//
////    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        UIViewController *vc = YPRoomStoryBoard(@"YPRoomMusciListVC");
        [[self topViewController].navigationController pushViewController:vc animated:YES];
//    });
    
    
    
    

    
    
//    [self close];
    
//    YPMusicViewController *m = [[YPMusicViewController alloc] init];
//    [[self topViewController].navigationController pushViewController:m animated:YES];
//
//
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//        [self removeFromSuperview];
//    });
    
}


- (IBAction)lastSongBtnAction:(id)sender {
    
    if (GetCore(YPMusicCore).hasPlayer) {
        [GetCore(YPMusicCore) playLastSong];
    } else {
        [GetCore(YPMusicCore) playOneSong];
        if (GetCore(YPMusicCore).titleItems.count > 0) {
            self.nameLabel.text = GetCore(YPMusicCore).titleItems[0];
        }
    }
}

- (IBAction)playBtnAction:(id)sender {
    
    if (GetCore(YPMusicCore).titleItems.count == 0) {
        [MBProgressHUD showSuccess:@"暂无歌曲～" toView:[UIApplication sharedApplication].keyWindow];
        return;
    }
    
    self.playBtn.selected = !self.playBtn.selected;
    if (GetCore(YPMusicCore).hasPlayer) {
        if (GetCore(YPMusicCore).isPlaying) {
            [GetCore(YPMusicCore) pauseMusic];
        } else {
            [GetCore(YPMusicCore) resumeMusic];
        }
    } else {
        [GetCore(YPMusicCore) playOneSong];
    }
    
}
- (IBAction)nextBtnAction:(id)sender {
    
    if (GetCore(YPMusicCore).hasPlayer) {
        [GetCore(YPMusicCore) playNextSong];
    } else {
        [GetCore(YPMusicCore) playOneSong];
        if (GetCore(YPMusicCore).titleItems.count > 0) {
            self.nameLabel.text = GetCore(YPMusicCore).titleItems[0];
        }
    }
}
- (IBAction)tapAction:(id)sender {
    
    [self close];
    
}

- (void)changeIndexTitle
{
    [self configMusic];
}

- (void)stopMusicNoti
{
    [self configMusic];
}

@end
