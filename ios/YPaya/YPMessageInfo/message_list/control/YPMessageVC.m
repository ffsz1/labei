//
//  YPMessageVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMessageVC.h"
#import "YPSessionListViewController.h"
#import "YPTabBarController.h"
#import "YPFansListViewController.h"
#import "YPFollowListViewController.h"
#import "YPFriendsListVC.h"
#import "YPBlackListViewController.h"

#import "YPHomeTagView.h"

#import "YPHomeTag.h"

#import "YPImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "YPFirstHomeTagStytleModel.h"
@interface YPMessageVC ()<HJImMessageCoreClient>
@property (weak, nonatomic) IBOutlet UIView *topView;
@property (strong, nonatomic) YPHomeTagView *topTagView;
@property (weak, nonatomic) IBOutlet UIButton *readBtn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_arrow;
@property (weak, nonatomic) IBOutlet UIScrollView *bgScrollView;
@property (weak, nonatomic) IBOutlet UIView *contentView;

@property (nonatomic, strong) YPSessionListViewController *msgVC;

@property (nonatomic, strong) YPFriendsListVC *friendVC;


@property (nonatomic,strong) YPFansListViewController *fanVC;
@property (nonatomic,strong) YPFollowListViewController *followVC;
@property (nonatomic,strong) YPBlackListViewController *blackVC;




@end

@implementation YPMessageVC

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    YPLightStatusBar
    
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
    YPHomeTag *tag1 = [[YPHomeTag alloc] init];
    tag1.name = @"消息";
    
    YPHomeTag *tag2 = [[YPHomeTag alloc] init];
    tag2.name = @"好友";
    
    YPHomeTag *tag3 = [[YPHomeTag alloc] init];
    tag3.name = @"关注";
    
    YPHomeTag *tag4 = [[YPHomeTag alloc] init];
    tag4.name = @"粉丝";
    
//    YPHomeTag *tag5 = [[YPHomeTag alloc] init];
//    tag5.name = @"黑名单";
    
    self.topTagView.roomTagList = [NSMutableArray arrayWithArray:@[tag1,tag2,tag3,tag4]];
}

- (IBAction)readBtnAction:(id)sender {
    
    if ([GetCore(YPImMessageCore) getUnreadCount] == 0) {
        [MBProgressHUD showError:@"暂无未读消息"];
        return;
    }
    
    [[NIMSDK sharedSDK].conversationManager markAllMessagesRead];
    
    YPTabBarController *controller = (YPTabBarController *)self.tabBarController;
    [controller showBadgeOnItemIndex:2 num:0];
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
    
    NSInteger count = [GetCore(YPImMessageCore)getUnreadCount];
    
//    NSLog(@"getUnreadCount:%ld",(long));
    
    YPTabBarController *controller = (YPTabBarController *)self.tabBarController;
    if (count > 0) {
        [controller showBadgeOnItemIndex:2 num:count];
    }else {
        [controller showBadgeOnItemIndex:2 num:0];
        
    }
}

- (void)onRecvAnMsg:(NIMMessage *)msg
{
    [self addBadge];
}

#pragma mark - setter/getter
- (YPHomeTagView *)topTagView
{
    if (!_topTagView) {
        _topTagView = [[YPHomeTagView alloc] initWithFrame:CGRectZero];
        _topTagView.backgroundColor = [UIColor clearColor];
        __weak typeof(self)weakSelf = self;
        
        YPFirstHomeTagStytleModel *model = [[YPFirstHomeTagStytleModel alloc] init];
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



- (YPSessionListViewController *)msgVC {
    if (!_msgVC) {
        _msgVC = [[YPSessionListViewController alloc] init];
        [self addChildViewController:_msgVC];
        [self.contentView addSubview:_msgVC.view];
    }
    return _msgVC;
}

- (YPFriendsListVC *)friendVC
{
    if (!_friendVC) {
        _friendVC = YPMessageBoard(@"YPFriendsListVC");
        [self addChildViewController:_friendVC];
        [self.contentView addSubview:_friendVC.view];
    }
    return _friendVC;
}

- (YPFollowListViewController *)followVC
{
    if (!_followVC) {
        _followVC = YPMessageBoard(@"YPFollowListViewController");
        _followVC.isFromMessageHome = YES;
        [self addChildViewController:_followVC];
        [self.contentView addSubview:_followVC.view];
    }
    return _followVC;
}

- (YPFansListViewController *)fanVC
{
    if (!_fanVC) {
        _fanVC = YPMessageBoard(@"YPFansListViewController");
        _fanVC.isFromMessageHome = YES;

        [self addChildViewController:_fanVC];
        [self.contentView addSubview:_fanVC.view];
    }
    return _fanVC;
}

- (YPBlackListViewController *)blackVC
{
    if (!_blackVC) {
        _blackVC = YPMessageBoard(@"YPBlackListViewController");
        [self addChildViewController:_blackVC];
        [self.contentView addSubview:_blackVC.view];
    }
    return _blackVC;
}

@end
