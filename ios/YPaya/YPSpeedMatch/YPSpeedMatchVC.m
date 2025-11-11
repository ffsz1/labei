//
//  YPSpeedMatchVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPSpeedMatchVC.h"
#import "YPSphereView.h"
#import "YPSeppdMatchMenuView.h"
#import "YPMICMatchUserVC.h"
#import "YPMICRecordVC.h"
#import "YPNotiFriendVC.h"
#import "YPMICCore.h"
#import "HJMICCoreClient.h"
#import "YPActivityViewController.h"
#import "YPMICUserInfo.h"
#import "YPMySpaceVC.h"
#import "YPUserViewControllerFactory.h"
#import "YPNotiFriendCore.h"
#import "HJNotiFriendCoreClient.h"
#import "YPNotiFriendInfo.h"

@interface YPSpeedMatchVC ()<HJMICCoreClient,HJNotiFriendCoreClient>
@property (weak, nonatomic) IBOutlet UIView *view1;
@property (weak, nonatomic) IBOutlet UIView *view2;
@property (strong, nonatomic) YPSphereView *sphereView;
@property (weak, nonatomic) IBOutlet GGButton *changeBtn;
@property (nonatomic, strong) NSMutableArray<YPMICUserInfo *> *users;
@property (nonatomic, strong) NSArray *publicArr;
@property (nonatomic, weak) NSTimer *timer;


@end

@implementation YPSpeedMatchVC
{
    int i;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    
//    self.automaticallyAdjustsScrollViewInsets = NO;
//    self.edgesForExtendedLayout = UIRectEdgeNone;

    
    [GetCore(YPMICCore) getLinkPool];
    [self addCores];
    
   
    
    [GetCore(YPMICCore) getCharmUserList];
    
    
    
    [GetCore(YPMICCore) getRandomUserList];
    
    [self.view bringSubviewToFront:self.view1];
    
    [GetCore(YPNotiFriendCore) getLobbyChatInfo];
}

- (void)getLobbyRoomInfoSuccess:(NSArray *)arr {
    self.publicArr = arr;
    
    i = 0;
    
    YPNotiFriendInfo *info = [self.publicArr safeObjectAtIndex:i];
    [self.publicImg sd_setImageWithURL:[NSURL URLWithString:info.avatar] placeholderImage:nil];
    self.publicNick.text = info.nick;
    self.publicDetail.text = info.content;
    
    i = 1;
    
    [self invalidateTimer];
    NSTimer *timer = [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(countDown) userInfo:nil repeats:YES];
    _timer = timer;
    [[NSRunLoop mainRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
}

- (void)invalidateTimer
{
    [_timer invalidate];
    _timer = nil;
}

- (void)countDown {
    if (i < self.publicArr.count - 1) {
        i ++;
    } else {
        i = 0;
    }
    YPNotiFriendInfo *info = [self.publicArr safeObjectAtIndex:i];
    [self.publicImg sd_setImageWithURL:[NSURL URLWithString:info.avatar] placeholderImage:nil];
    self.publicNick.text = info.nick;
    self.publicDetail.text = info.content;
}

- (void)getLobbyRoomInfoFail {
    
}

#pragma mark - setters/getters
- (NSMutableArray *)users {
    if (!_users) {
        _users = @[].mutableCopy;
    }
    return _users;
}

- (void)getSoundMatchCharmUserSuccessWithList:(NSArray *)list {
    [self.users removeAllObjects];
    [self.users addObjectsFromArray:list];
}

- (void)getSoundMatchCharmUserFailthWithMessage:(NSString *)message {
    
}

- (void)getCharmUserListSuccessWithList:(NSArray *)list {
    
    [MBProgressHUD hideHUD];
    
    [self.sphereView removeFromSuperview];
    self.sphereView = nil;
    [self.view insertSubview:self.sphereView belowSubview:self.changeBtn];
    
    NSMutableArray *array = [[NSMutableArray alloc] initWithCapacity:0];
    for (NSInteger i = 0; i < list.count; i ++) {
        YPMICUserInfo *cuserInfo = list[i];
        YPSeppdMatchMenuView *sepView = [[NSBundle mainBundle] loadNibNamed:@"YPSeppdMatchMenuView" owner:nil options:nil][0];
        sepView.frame = CGRectMake(0, 0, 60, 40);
        sepView.title.text = cuserInfo.nick;
        [sepView.avatar sd_setImageWithURL:[NSURL URLWithString:cuserInfo.avatar]];
        [array addObject:sepView];
        
        @weakify(self);
        [sepView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
            @strongify(self);
            
            
            YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
            vc.userID = cuserInfo.uid;
            [self.navigationController pushViewController:vc animated:YES];
            
        }]];
        
        [self.sphereView addSubview:sepView];
    }
    [self.sphereView setCloudTags:array];
}

- (void)getCharmUserListFailthWithMessage:(NSString *)message
{
    [MBProgressHUD hideHUD];
}

- (IBAction)jumpActivity:(UIButton *)sender {
    YPActivityViewController *vc = [YPActivityViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)dealloc {
    [self removeCores];
}

- (void)addCores {
    AddCoreClient(HJMICCoreClient, self);
    AddCoreClient(HJNotiFriendCoreClient, self);
}

- (void)removeCores {
    RemoveCoreClientAll(self);
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

- (IBAction)micClick:(UIButton *)sender {
    [self skipToMatchUser];
}

- (void)skipToMatchUser {
    YPMICMatchUserVC *vc = [[YPMICMatchUserVC alloc] init];
    vc.users = [NSMutableArray arrayWithArray:self.users];
    [self.navigationController pushViewController:vc animated:YES];
}


- (BOOL)preferredNavigationBarHidden {
    return YES;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
}

- (IBAction)mmClick:(UITapGestureRecognizer *)sender {
    YPNotiFriendVC *viewController = [[YPNotiFriendVC alloc] init];
    [viewController updateData];
    [self.navigationController pushViewController:viewController animated:YES];
}

//换一换
- (IBAction)changeBtnAction:(id)sender {
    
    [MBProgressHUD showMessage:@"请稍候..."];
    [GetCore(YPMICCore) getCharmUserList];

}


- (YPSphereView *)sphereView
{
    if (!_sphereView) {
        _sphereView = [[YPSphereView alloc] initWithFrame:CGRectMake(35, iPhoneX ? 135 : 85, kScreenWidth-70, kScreenHeight-300)];
    }
    return _sphereView;
}

@end
