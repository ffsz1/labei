//
//  HJMessageVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMessageVC.h"
#import "HJSessionListViewController.h"
#import "HJTabBarController.h"
#import "HJFansListViewController.h"
#import "HJFollowListViewController.h"
#import "HJFriendsListVC.h"
#import "HJBlackListViewController.h"

#import "HJHomeTagView.h"

#import "HJHomeTag.h"

#import "HJImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "HJFirstHomeTagStytleModel.h"
@interface HJMessageVC ()<HJImMessageCoreClient>
@property (weak, nonatomic) IBOutlet UIView *topView;
@property (strong, nonatomic) HJHomeTagView *topTagView;
@property (weak, nonatomic) IBOutlet UIButton *readBtn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_arrow;
@property (weak, nonatomic) IBOutlet UIScrollView *bgScrollView;
@property (weak, nonatomic) IBOutlet UIView *contentView;

@property (nonatomic, strong) HJSessionListViewController *msgVC;

@property (nonatomic, strong) HJFriendsListVC *friendVC;


@property (nonatomic,strong) HJFansListViewController *fanVC;
@property (nonatomic,strong) HJFollowListViewController *followVC;
@property (nonatomic,strong) HJBlackListViewController *blackVC;




@end

@implementation HJMessageVC

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    HJLightStatusBar
    
    [self addBadge];
    
    AddCoreClient(HJImMessageCoreClient, self);
    
}
- (UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent; //返回白色
//    return UIStatusBarStyleDefault;    //返回黑色
}
- (void)dealloc
{
    RemoveCoreClientAll(self);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.topTagView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(300, 33));
        make.left.mas_equalTo(self.view).mas_offset(10);
        make.top.mas_equalTo(self.view).mas_offset(64);
    }];
    
    [self.msgVC.view mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.bottom.mas_equalTo(self.contentView);
        make.width.mas_equalTo(kScreenWidth);
    }];
    
    
    
    [self.friendVC.view mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.msgVC.view.mas_right);
        make.top.bottom.mas_equalTo(self.contentView);
        make.width.mas_equalTo(kScreenWidth);
    }];
    
    [self.followVC.view mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.friendVC.view.mas_right);
        make.top.bottom.mas_equalTo(self.contentView);
        make.width.mas_equalTo(kScreenWidth);
    }];
    
    [self.fanVC.view mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.followVC.view.mas_right);
        make.top.bottom.mas_equalTo(self.contentView);
        make.width.mas_equalTo(kScreenWidth);
    }];
    
//    [self.blackVC.view mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.left.mas_equalTo(self.fanVC.view.mas_right);
//        make.top.bottom.mas_equalTo(self.contentView);
//        make.width.mas_equalTo(kScreenWidth);
//    }];
    
    
    [self setTagList];
    
}

- (void)setTagList
{
    HJHomeTag *tag1 = [[HJHomeTag alloc] init];
    tag1.name = @"消息";
    
    HJHomeTag *tag2 = [[HJHomeTag alloc] init];
    tag2.name = @"好友";
    
    HJHomeTag *tag3 = [[HJHomeTag alloc] init];
    tag3.name = @"关注";
    
    HJHomeTag *tag4 = [[HJHomeTag alloc] init];
    tag4.name = @"粉丝";
    
//    HJHomeTag *tag5 = [[HJHomeTag alloc] init];
//    tag5.name = @"黑名单";
    
    self.topTagView.roomTagList = [NSMutableArray arrayWithArray:@[tag1,tag2,tag3,tag4]];
}

- (IBAction)readBtnAction:(id)sender {
    
    if ([GetCore(HJImMessageCore) getUnreadCount] == 0) {
        [MBProgressHUD showError:@"暂无未读消息"];
        return;
    }
    
    [[NIMSDK sharedSDK].conversationManager markAllMessagesRead];
    
    HJTabBarController *controller = (HJTabBarController *)self.tabBarController;
    [controller showBadgeOnItemIndex:1 num:0];
}


- (void)setArrowAnimation:(CGFloat)distance
{
    
    [self.view layoutIfNeeded];
    [UIView animateWithDuration:0.5 animations:^{
        self.left_arrow.constant = distance;
        [self.view layoutIfNeeded];
    }];

}

- (void)addBadge {
    
    NSInteger count = [GetCore(HJImMessageCore)getUnreadCount];
    
//    NSLog(@"getUnreadCount:%ld",(long));
    
    HJTabBarController *controller = (HJTabBarController *)self.tabBarController;
    if (count > 0) {
        [controller showBadgeOnItemIndex:1 num:count];
    }else {
        [controller showBadgeOnItemIndex:1 num:0];
        
    }
}

- (void)onRecvAnMsg:(NIMMessage *)msg
{
    [self addBadge];
}

#pragma mark - setter/getter
- (HJHomeTagView *)topTagView
{
    if (!_topTagView) {
        _topTagView = [[HJHomeTagView alloc] initWithFrame:CGRectZero];
        _topTagView.backgroundColor = [UIColor clearColor];
        __weak typeof(self)weakSelf = self;
        
        HJFirstHomeTagStytleModel *model = [[HJFirstHomeTagStytleModel alloc] init];
        model.selFont = JXFontPingFangSCMedium(22);
         model.isPictureTitleColor = NO;
        model.selColor = UIColorHex(FFFFFF);
       
        model.normalFont = JXFontPingFangSCRegular(14);
        model.normalColor = [UIColor colorWithWhite:1.0 alpha:0.7];
        model.lineHeight = 2;
        model.lineWidth = 30;
        model.lineColor = [UIColor whiteColor];
        model.verticalAlignment = 2;
        

        
        _topTagView.stytle = model;
        

        _topTagView.selItemCallBack = ^(NSInteger item) {

            [weakSelf.bgScrollView setContentOffset:CGPointMake(item*XC_SCREE_W, 0) animated:YES];
            
            weakSelf.readBtn.hidden = item==0?NO:YES;
            
            [weakSelf setArrowAnimation:25+item *48];
            
        };
        [self.topView addSubview:_topTagView];
    }
    return _topTagView;
}



- (HJSessionListViewController *)msgVC {
    if (!_msgVC) {
        _msgVC = [[HJSessionListViewController alloc] init];
        [self addChildViewController:_msgVC];
        [self.contentView addSubview:_msgVC.view];
    }
    return _msgVC;
}

- (HJFriendsListVC *)friendVC
{
    if (!_friendVC) {
        _friendVC = HJMessageBoard(@"HJFriendsListVC");
        [self addChildViewController:_friendVC];
        [self.contentView addSubview:_friendVC.view];
    }
    return _friendVC;
}

- (HJFollowListViewController *)followVC
{
    if (!_followVC) {
        _followVC = HJMessageBoard(@"HJFollowListViewController");
        _followVC.isFromMessageHome = YES;
        [self addChildViewController:_followVC];
        [self.contentView addSubview:_followVC.view];
    }
    return _followVC;
}

- (HJFansListViewController *)fanVC
{
    if (!_fanVC) {
        _fanVC = HJMessageBoard(@"HJFansListViewController");
        _fanVC.isFromMessageHome = YES;

        [self addChildViewController:_fanVC];
        [self.contentView addSubview:_fanVC.view];
    }
    return _fanVC;
}

- (HJBlackListViewController *)blackVC
{
    if (!_blackVC) {
        _blackVC = HJMessageBoard(@"HJBlackListViewController");
        [self addChildViewController:_blackVC];
        [self.contentView addSubview:_blackVC.view];
    }
    return _blackVC;
}

@end
