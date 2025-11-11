//
//  YPRoomMusciListVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomMusciListVC.h"
#import "YPAddMusicViewController.h"

#import "YPRoomMusicCell.h"

#import "YPMusicCore.h"
#import "YPMusicCoreClient.h"

#define XBDRoomMusiceSafeHeight (iPhoneX?14:0)

@interface YPRoomMusciListVC ()<YPMusicCoreClient>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UISlider *musicSlider;
@property (weak, nonatomic) IBOutlet UISlider *manVoiceSlider;
@property (weak, nonatomic) IBOutlet UISlider *playSlider;
@property (weak, nonatomic) IBOutlet UILabel *musicPercentLabel;
@property (weak, nonatomic) IBOutlet UILabel *manPercentLabel;
@property (weak, nonatomic) IBOutlet UIButton *playBtn;
@property (weak, nonatomic) IBOutlet UILabel *songLabel;
@property (weak, nonatomic) IBOutlet UIView *songView;
@property (weak, nonatomic) IBOutlet UILabel *noSongTipLabel;
@property (weak, nonatomic) IBOutlet UIImageView *noSongImageView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *height_navi;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *height_playView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *height_musicControlView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_playView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_controlView;

@property (nonatomic,strong) NSTimer *timer;
@property (nonatomic, strong) NSMutableArray *items;
@property (nonatomic, strong) NSMutableArray *titleItems;
@property (nonatomic, strong) NSMutableArray *musicInfoItems;



@end

@implementation YPRoomMusciListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.height_navi.constant = 64 + XBDRoomMusiceSafeHeight;
    self.height_playView.constant = 64 + XBDRoomMusiceSafeHeight;
    self.height_musicControlView.constant = 81 + XBDRoomMusiceSafeHeight;
    self.bottom_controlView.constant = - 81 - XBDRoomMusiceSafeHeight;
    
    AddCoreClient(YPMusicCoreClient, self);
    [GetCore(YPMusicCore) playBackgroundMusicList];
    
    if (GetCore(YPMusicCore).isPlaying) {
        [self startTimer];
    }
    
    UIImage *muimg = [UIImage imageNamed:@"xc_room_music_icon_jinduquan"];
    [self.manVoiceSlider setThumbImage:muimg forState:UIControlStateNormal];
    [self.manVoiceSlider setThumbImage:muimg forState:UIControlStateHighlighted];
    
    [self.musicSlider setThumbImage:muimg forState:UIControlStateNormal];
    [self.musicSlider setThumbImage:muimg forState:UIControlStateHighlighted];
    
    [self.playSlider setThumbImage:muimg forState:UIControlStateNormal];
    [self.playSlider setThumbImage:muimg forState:UIControlStateHighlighted];
    
    self.tableView.tableFooterView = [UIView new];
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    YPLightStatusBar
    
    [self updateMusic];
    
    
    
}

- (void)updateMusic
{
    
    if (GetCore(YPMusicCore).indexSongTitle.length > 0) {
        self.noSongTipLabel.hidden = YES;
        self.songView.hidden = NO;
        self.songLabel.text =  GetCore(YPMusicCore).indexSongTitle;
    }else{
        self.noSongTipLabel.hidden = NO;
        self.songView.hidden = YES;
    }
    
    self.playBtn.selected = GetCore(YPMusicCore).isPlaying;
    self.musicSlider.value = GetCore(YPMusicCore).musicSounds;
    self.manVoiceSlider.value = GetCore(YPMusicCore).manSounds;
    self.musicPercentLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(YPMusicCore).musicSounds*100];
    self.manPercentLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(YPMusicCore).manSounds*100];
}

#pragma mark - <tableView delegate>
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    YPRoomMusicCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPRoomMusicCell"];
    YPMusicInfoModel *infoModel = self.musicInfoItems[indexPath.row];
    cell.model = infoModel;
    cell.url = self.items[indexPath.row];
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.items.count;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.items.count > 0) {
        [GetCore(YPMusicCore) playWithUrl:self.items[indexPath.row] withTitle:self.titleItems[indexPath.row]];
    }
}


- (IBAction)backAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)addBtnAction:(id)sender {
    
    YPAddMusicViewController *v = [YPAddMusicViewController new];
    [self.navigationController pushViewController:v animated:YES];
    
}

- (IBAction)closeAction:(id)sender {
    
    [self.view layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.bottom_controlView.constant = - 81 - XBDRoomMusiceSafeHeight;
        self.bottom_playView.constant = 0;
        [self.view layoutIfNeeded];
    }];
    
}

- (IBAction)playAction:(id)sender {
    
    if (GetCore(YPMusicCore).hasPlayer) {
        if (GetCore(YPMusicCore).isPlaying) {
            [GetCore(YPMusicCore) pauseMusic];
            self.playBtn.selected = NO;
        } else {
            [GetCore(YPMusicCore) resumeMusic];
            self.playBtn.selected = YES;
        }
    } else {
        [GetCore(YPMusicCore) playOneSong];
        self.playBtn.selected = YES;
        if (GetCore(YPMusicCore).titleItems.count > 0) {
            self.songLabel.text = GetCore(YPMusicCore).titleItems[0];
        }
    }
    
}

- (IBAction)musicControlAction:(id)sender {
    
    [self.view layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.bottom_controlView.constant = 0;
        self.bottom_playView.constant = -64 - XBDRoomMusiceSafeHeight;
        [self.view layoutIfNeeded];
    }];
}

- (IBAction)playSliderProgressAction:(id)sender {
    
    [GetCore(YPMusicCore) seekTo:self.playSlider.value * GetCore(YPMusicCore).getDuration];
    
}

- (IBAction)manSliderProgressAction:(id)sender {
    
    GetCore(YPMusicCore).manSounds = self.manVoiceSlider.value;
    self.manPercentLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(YPMusicCore).manSounds*100];
    
    [GetCore(YPMusicCore) changeManVolume:self.manVoiceSlider.value];
    
}
- (IBAction)musciSliderProgressAction:(id)sender {
    
    GetCore(YPMusicCore).musicSounds = self.musicSlider.value;
    self.musicPercentLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(YPMusicCore).musicSounds*100];
    [GetCore(YPMusicCore) changeMusicVolume:self.musicSlider.value];
    
}

-(void)stopTimer{
    [self.timer invalidate];
    self.timer = nil;
}

-(void)startTimer{
    if(self.timer == nil){
        self.timer = [NSTimer scheduledTimerWithTimeInterval:0.6 target:self selector:@selector(updateProgress) userInfo:nil repeats:YES];
        [[NSRunLoop mainRunLoop] addTimer:self.timer forMode:NSDefaultRunLoopMode];
    }
}

- (void)updateProgress {
    self.playSlider.value = GetCore(YPMusicCore).getCurrentDuration/(CGFloat)GetCore(YPMusicCore).getDuration;
}

#pragma mark <YPMusicCoreClient>

- (void)stopMusicNoti {
    if (GetCore(YPMusicCore).indexSongTitle.length > 0) {
        self.songLabel.text = GetCore(YPMusicCore).indexSongTitle;
        self.noSongTipLabel.hidden = YES;
        self.songView.hidden = NO;
    } else {
        self.noSongTipLabel.hidden = NO;
        self.songView.hidden = YES;
    }
    self.playBtn.selected = GetCore(YPMusicCore).isPlaying;
    [self stopTimer];
}

- (void)updateList {
    self.items = GetCore(YPMusicCore).items;
    self.titleItems = GetCore(YPMusicCore).titleItems;
    self.musicInfoItems = GetCore(YPMusicCore).musicInfoItems;
    
    if (self.items.count == 0) {
        self.tableView.hidden = YES;
        self.noSongImageView.hidden = NO;
    }else{
        self.tableView.hidden = NO;
        self.noSongImageView.hidden = YES;
    }
    
    [self.tableView reloadData];
}


- (void)changeIndexTitle {
    self.songLabel.text = GetCore(YPMusicCore).indexSongTitle;
    if (GetCore(YPMusicCore).hasPlayer) {
        [self.navigationController popViewControllerAnimated:YES];
    }
}

@end
