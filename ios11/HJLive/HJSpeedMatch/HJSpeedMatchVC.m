//
//  HJSpeedMatchVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSpeedMatchVC.h"
#import "HJSphereView.h"
#import "HJSeppdMatchMenuView.h"
#import "HJMICMatchUserVC.h"
#import "HJMICRecordVC.h"
#import "HJNotiFriendVC.h"
#import "HJMICCore.h"
#import "HJMICCoreClient.h"
#import "HJActivityViewController.h"
#import "HJMICUserInfo.h"
#import "HJMySpaceVC.h"
#import "HJUserViewControllerFactory.h"
#import "HJNotiFriendCore.h"
#import "HJNotiFriendCoreClient.h"
#import "HJNotiFriendInfo.h"

@interface HJSpeedMatchVC ()<HJMICCoreClient,HJNotiFriendCoreClient>
@property (weak, nonatomic) IBOutlet UIView *view1;
@property (weak, nonatomic) IBOutlet UIView *view2;
@property (strong, nonatomic) HJSphereView *sphereView;
@property (weak, nonatomic) IBOutlet GGButton *changeBtn;
@property (nonatomic, strong) NSMutableArray<HJMICUserInfo *> *users;
@property (nonatomic, strong) NSArray *publicArr;
@property (nonatomic, weak) NSTimer *timer;


@end

@implementation HJSpeedMatchVC
{
    int i;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    
//    self.automaticallyAdjustsScrollViewInsets = NO;
//    self.edgesForExtendedLayout = UIRectEdgeNone;

    
    [GetCore(HJMICCore) getLinkPool];
    [self addCores];
    
   
    
    [GetCore(HJMICCore) getCharmUserList];
    
    
    
    [GetCore(HJMICCore) getRandomUserList];
    
    [self.view bringSubviewToFront:self.view1];
    
    [GetCore(HJNotiFriendCore) getLobbyChatInfo];
}

- (void)getLobbyRoomInfoSuccess:(NSArray *)arr {
    self.publicArr = arr;
    
    i = 0;
    
    HJNotiFriendInfo *info = [self.publicArr safeObjectAtIndex:i];
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
    HJNotiFriendInfo *info = [self.publicArr safeObjectAtIndex:i];
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
        HJMICUserInfo *cuserInfo = list[i];
        HJSeppdMatchMenuView *sepView = [[NSBundle mainBundle] loadNibNamed:@"HJSeppdMatchMenuView" owner:nil options:nil][0];
        sepView.frame = CGRectMake(0, 0, 60, 40);
        sepView.title.text = cuserInfo.nick;
        [sepView.avatar sd_setImageWithURL:[NSURL URLWithString:cuserInfo.avatar]];
        [array addObject:sepView];
        
        @weakify(self);
        [sepView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
            @strongify(self);
            
            
            HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
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
    HJActivityViewController *vc = [HJActivityViewController new];
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
    HJMICMatchUserVC *vc = [[HJMICMatchUserVC alloc] init];
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
    HJNotiFriendVC *viewController = [[HJNotiFriendVC alloc] init];
    [viewController updateData];
    [self.navigationController pushViewController:viewController animated:YES];
}

//换一换
- (IBAction)changeBtnAction:(id)sender {
    
    [MBProgressHUD showMessage:@"请稍候..."];
    [GetCore(HJMICCore) getCharmUserList];

}


- (HJSphereView *)sphereView
{
    if (!_sphereView) {
        _sphereView = [[HJSphereView alloc] initWithFrame:CGRectMake(35, iPhoneX ? 135 : 85, kScreenWidth-70, kScreenHeight-300)];
    }
    return _sphereView;
}

@end
