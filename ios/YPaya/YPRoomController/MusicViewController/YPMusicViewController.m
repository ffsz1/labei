//
//  YPMusicViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMusicViewController.h"
#import "YPNotMusicView.h"
#import "YPAddMusicViewController.h"
#import "UIViewController+Cloudox.h"
#import "YPMusicTableViewCell.h"
#import "YPMusicCore.h"
#import "YPMusicCoreClient.h"
#import "YPMusicConfigSoundView.h"
#import "YPRoomViewControllerCenter.h"
#import "YPAlertControllerCenter.h"

@interface YPMusicViewController ()<
UITableViewDelegate,
UITableViewDataSource,
YPMusicCoreClient
>

#define timerDuration       0.6

@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) YPNotMusicView *notMusicView;
@property (nonatomic, strong) NSMutableArray *items;
@property (nonatomic, strong) NSMutableArray *titleItems;
@property (nonatomic, strong) NSMutableArray *musicInfoItems;
@property (nonatomic, strong) YPMusicConfigSoundView *musicConfig;
@property (nonatomic, strong) UIView *emptyView;
@property (nonatomic,strong) NSTimer *timer;
@end

@implementation YPMusicViewController
    
- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navBarBgAlpha = @"0.0";
    [self.navigationController setNavigationBarHidden:YES animated:YES];

    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
    
    
    if (GetCore(YPMusicCore).indexSongTitle.length > 0) {
        self.notMusicView.currentSong.text = GetCore(YPMusicCore).indexSongTitle;
        self.notMusicView.songName.text = self.notMusicView.currentSong.text;
        self.notMusicView.sliderContentView.hidden = NO;
    } else {
        self.notMusicView.currentSong.text = NSLocalizedString(XCRoomMyVideoPlayerNoMusic, nil);
        self.notMusicView.sliderContentView.hidden = YES;
    }
    
    self.notMusicView.startBtn.selected = GetCore(YPMusicCore).isPlaying;
    self.musicConfig.sli1.value = GetCore(YPMusicCore).musicSounds;
    self.musicConfig.sli2.value = GetCore(YPMusicCore).manSounds;
    self.musicConfig.musicSoundsLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(YPMusicCore).musicSounds*100];
    self.musicConfig.manSoundsLabel.text = [NSString stringWithFormat:@"%.0f%%",GetCore(YPMusicCore).manSounds*100];
}
    
- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    self.navBarBgAlpha = @"0.0";
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    AddCoreClient(YPMusicCoreClient, self);
    [GetCore(YPMusicCore) playBackgroundMusicList];

    UIImageView *bg = [UIImageView new];
    bg.image = [UIImage imageNamed:@"yp_room_music_bg"];
    bg.contentMode = UIViewContentModeScaleAspectFill;
    [self.view addSubview:bg];
    [bg mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.bottom.equalTo(self.view);
    }];
    
    UIView *bottomView = [UIView new];
    bottomView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:bottomView];
    [bottomView mas_makeConstraints:^(MASConstraintMaker *make) {
        if (@available(iOS 11.0, *)) {
            make.top.equalTo(self.view.mas_safeAreaLayoutGuideTop);
        } else {
            make.top.equalTo(self.view).offset(20);
        }
        make.left.right.equalTo(self.view);
        make.height.equalTo(@44);
    }];
    
    UIButton *back = [UIButton new];
    [back setImage:[UIImage imageNamed:@"yp_back_rollow_white_icon"] forState:UIControlStateNormal];
    [back addTarget:self action:@selector(backClick) forControlEvents:UIControlEventTouchUpInside];
    [bottomView addSubview:back];
    [back mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.height.equalTo(@50);
        make.left.equalTo(@0);
        make.centerY.equalTo(bottomView.mas_centerY);
    }];
    
    UIButton *add = [UIButton new];
    [add setImage:[UIImage imageNamed:@"xc_room_music_icon_tianjia"] forState:UIControlStateNormal];
    [add addTarget:self action:@selector(barClick) forControlEvents:UIControlEventTouchUpInside];
    [bottomView addSubview:add];
    [add mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.height.equalTo(@50);
        make.right.equalTo(@0);
        make.centerY.equalTo(bottomView.mas_centerY);
    }];
    
    UILabel *ti = [UILabel new];
    ti.text = @"我的播放器";
    ti.font = [UIFont systemFontOfSize:16];
    ti.textColor = [UIColor whiteColor];
    ti.textAlignment = NSTextAlignmentCenter;
    [bottomView addSubview:ti];
    [ti mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.equalTo(@100);
        make.height.equalTo(@30);
        make.center.equalTo(bottomView);
    }];
    
    
    //    UIBarButtonItem *bar = [[UIBarButtonItem alloc] initWithTitle:@"添加" style:UIBarButtonItemStylePlain target:self action:@selector(barClick)];
    //    self.navigationItem.rightBarButtonItem = bar;
    
    [self.view addSubview:self.notMusicView];
    [self.notMusicView mas_makeConstraints:^(MASConstraintMaker *make) {
        if (@available(iOS 11.0, *)) {
            make.bottom.equalTo(self.view.mas_safeAreaLayoutGuideBottom);
        } else {
            make.bottom.equalTo(self.view);
        }
        make.left.right.equalTo(self.view);
        make.height.equalTo(@60);
    }];
    @weakify(self);
    [self.notMusicView setConfigMusicBlock:^{
        @strongify(self);
        [self showMusicConfigView];
    }];
    self.notMusicView.sliderBlock = ^(CGFloat progress) {
        [GetCore(YPMusicCore) seekTo:progress * GetCore(YPMusicCore).getDuration];
    };
    if (GetCore(YPMusicCore).isPlaying) {
        [self startTimer];
    }
    
    [self.view addSubview:self.tableView];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.tableFooterView = [UIView new];
    
    self.tableView.backgroundColor = [UIColor clearColor];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(bottomView.mas_bottom);
        make.left.right.equalTo(self.view);
        make.bottom.equalTo(self.notMusicView.mas_top);
    }];
    
    [self.tableView registerNib:[UINib nibWithNibName:@"YPMusicTableViewCell" bundle:nil] forCellReuseIdentifier:@"YPMusicTableViewCell"];

}

- (void)stopMusicNoti {
    if (GetCore(YPMusicCore).indexSongTitle.length > 0) {
        self.notMusicView.currentSong.text = GetCore(YPMusicCore).indexSongTitle;
        self.notMusicView.songName.text = self.notMusicView.currentSong.text;
        self.notMusicView.sliderContentView.hidden = NO;
    } else {
        self.notMusicView.currentSong.text = NSLocalizedString(XCRoomMyVideoPlayerNoMusic, nil);
        self.notMusicView.sliderContentView.hidden = YES;
    }
    self.notMusicView.startBtn.selected = GetCore(YPMusicCore).isPlaying;
    [self stopTimer];
}

- (void)updateList {
    
    
    self.items = GetCore(YPMusicCore).items;
    self.titleItems = GetCore(YPMusicCore).titleItems;
    self.musicInfoItems = GetCore(YPMusicCore).musicInfoItems;
    [self.tableView reloadData];
}


- (void)changeIndexTitle {
    self.notMusicView.currentSong.text = GetCore(YPMusicCore).indexSongTitle;
    self.notMusicView.songName.text = self.notMusicView.currentSong.text;
    if (GetCore(YPMusicCore).hasPlayer) {
        [self.navigationController popViewControllerAnimated:YES];
    }
}


-(void)stopTimer{
    [self.timer invalidate];
    self.timer = nil;
}

-(void)startTimer{
    if(self.timer == nil){
        self.timer = [NSTimer scheduledTimerWithTimeInterval:timerDuration target:self selector:@selector(updateProgress) userInfo:nil repeats:YES];
        [[NSRunLoop mainRunLoop] addTimer:self.timer forMode:NSDefaultRunLoopMode];
    }
}

- (void)updateProgress {
    self.notMusicView.slider.value = GetCore(YPMusicCore).getCurrentDuration/(CGFloat)GetCore(YPMusicCore).getDuration;
}

- (void)backClick {
    [self.navigationController popViewControllerAnimated:YES];
}
    
- (void)showMusicConfigView
{
    if ([YPRoomViewControllerCenter defaultCenter].systemOperationStatusBarIsShow) {
        [YPAlertControllerCenter defaultCenter].alertViewOriginY = -20;
    }else {
        [YPAlertControllerCenter defaultCenter].alertViewOriginY = 0;
    }
    [[YPAlertControllerCenter defaultCenter] presentAlertWith:[YPRoomViewControllerCenter defaultCenter].current view:self.musicConfig preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleActionSheet dismissBlock:nil completionBlock:^{
        //        @strongify(self);
    }];
}

- (void)barClick {
    YPAddMusicViewController *v = [YPAddMusicViewController new];
    [self.navigationController pushViewController:v animated:YES];
}

#pragma mark - tableview
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    YPMusicTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPMusicTableViewCell"];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    YPMusicInfoModel *infoModel = self.musicInfoItems[indexPath.row];
    //    cell.titleLa.text = self.titleItems[indexPath.row];
    cell.titleLa.text = infoModel.musicName;
    cell.singerLabel.text = infoModel.musicSinger;
    @weakify(self);
    [cell setDeleMusicBlock:^{
        @strongify(self);
        [GetCore(YPMusicCore) removeBackgroundMusic:self.items[indexPath.row]];
    }];
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (self.items.count == 0) {
        [self.view addSubview:self.emptyView];
        [self.emptyView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.center.equalTo(self.view);
        }];
    } else {
        [self.emptyView removeFromSuperview];
    }
    return self.items.count;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.items.count > 0) {
        [GetCore(YPMusicCore) playWithUrl:self.items[indexPath.row] withTitle:self.titleItems[indexPath.row]];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

#pragma mark - getter/setter
- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] init];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.rowHeight = 70;
    }
    return _tableView;
}

- (YPNotMusicView *)notMusicView {
    if (!_notMusicView) {
        _notMusicView = [[NSBundle mainBundle] loadNibNamed:@"YPNotMusicView" owner:nil options:nil][0];
    }
    return _notMusicView;
}

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

- (YPMusicConfigSoundView *)musicConfig {
    if (!_musicConfig) {
        _musicConfig = [[NSBundle mainBundle] loadNibNamed:@"YPMusicConfigSoundView" owner:nil options:nil][0];
    }
    return _musicConfig;
}

- (UIView *)emptyView {
    if (!_emptyView) {
        _emptyView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, 300)];
        UIImageView *emptyIcon = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"xc_room_music_icon_yingyuewu"]];
        [_emptyView addSubview:emptyIcon];
        [emptyIcon mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerX.equalTo(_emptyView);
        }];
        
    }
    return _emptyView;
}

@end
