//
//  HJMyViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMyViewController.h"
#import "HJMySpaceVC.h"
#import "HJPersonLevelVC.h"
#import "HJActivityViewController.h"
#import "HJMallViewController.h"

#import "HJMineCell.h"

#import "HJPointCoinWalletModel.h"

#import "HJPurseViewControllerFactory.h"
#import "HJSettingViewControllerFactory.h"
#import "HJRoomViewControllerCenter.h"
#import "HJWKWebViewController.h"

#import "NSString+GGImage.h"
#import "HJAuthCoreClient.h"
#import "HJHttpRequestHelper+Sign.h"
#import "PurseCore.h"
#import "HJUserViewControllerFactory.h"
#import "HJFriendsListVC.h"
#import "HJPersonalEditViewController.h"
#import "HJMyWalletVC.h"
#import "HJLoginFillDataVC.h"
@interface HJMyViewController ()<UITableViewDelegate,UITableViewDataSource,HJAuthCoreClient>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *levelImageView;
@property (weak, nonatomic) IBOutlet UIImageView *meiliImageView;
@property (weak, nonatomic) IBOutlet UILabel *idLabel;
@property (weak, nonatomic) IBOutlet UILabel *followLabel;
@property (weak, nonatomic) IBOutlet UILabel *fanLabel;
@property (weak, nonatomic) IBOutlet UILabel *activeLabel;

@property (weak, nonatomic) IBOutlet UILabel *coinLabel;
@property (weak, nonatomic) IBOutlet UILabel *pointLabel;
@property (weak, nonatomic) IBOutlet UIView *coinView;
@property (weak, nonatomic) IBOutlet UIView *pointView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_rich;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *desSignLabel;
@property (strong,nonatomic) NSArray *titleArr;
@property (strong,nonatomic) NSArray *imageArr;

@property (weak, nonatomic) IBOutlet GGImageView *headWearImgView;

@end

@implementation HJMyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    AddCoreClient(HJAuthCoreClient, self);
        self.titleArr = @[@"装扮商城",@"我的钱包",@"我的等级",@"青少年模式",@"实名认证",@"设置"];//@"热门活动",

        self.imageArr = @[@"hj_mall_icon",@"hj_wallet_icon",@"hj_mylevel_icon",@"hj_me_youth",@"hj_shimingrenzheng_icon",@"hj_my_setting_icon"];//@"hj_my_gift_icon",

}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
   
    
    HJLightStatusBar
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    if (@available(iOS 11.0, *)) {
        self.tableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
    } else {
        self.automaticallyAdjustsScrollViewInsets = NO;
    }
    
    if (GetCore(HJAuthCoreHelp).isLogin) {
        [self onLoginSuccess];
    }
}
- (UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent; //返回白色
//    return UIStatusBarStyleDefault;    //返回黑色
}
#pragma mark <UITableViewDelegate,UITableViewDataSource>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.titleArr.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 0;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view = [UIView new];
    view.backgroundColor = [UIColor clearColor];
    return view;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 45;;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 20;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    UIView *view = [UIView new];
    view.backgroundColor = [UIColor clearColor];
    return view;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    HJMineCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJMineCell" forIndexPath:indexPath];
    
    cell.tipLabel.text = self.titleArr[indexPath.row];
    cell.logoImageView.image = [UIImage imageNamed:self.imageArr[indexPath.row]];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    switch (indexPath.row) {
//            case 0:
//            {
//                //热门活动
//                HJActivityViewController *vc = [HJActivityViewController new];
//                   [self.navigationController pushViewController:vc animated:YES];
//            }
            //                break;
            
        case 0:
        {
            [self mallAction:nil];
        }
            break;
            
        case 1:
        {
            
            [self walletAction:nil];
        }
            break;
            
        case 2:
        {
            [self levelAction:nil];
        }
             break;
        case 3:
        {
            //青少年模式
            [self loadWebView:@"/front/teenager/index.html"];
        }
            break;
            
        case 4:
            {
                //实名认证
                 [self loadWebView:@"/front/real_name/index.html"];
            }
                break;
            
        case 5:
        {
// 设置
            UIViewController *vc = [[HJSettingViewControllerFactory sharedFactory] instantiateSettingViewController];
            [self.navigationController pushViewController:vc animated:YES];
         
//            [self.navigationController pushViewController:HJLoginStoryBoard(@"HJLoginFillDataVC") animated:YES];
          
            
        }
        default:
            break;
    }
}



- (void)loadWebView:(NSString *)url
{
    NSString *urlSting = [NSString stringWithFormat:@"%@%@",[HJHttpRequestHelper getHostUrl],url];

    HJWKWebViewController *webView = [[HJWKWebViewController alloc]init];
    webView.url = [NSURL URLWithString:urlSting];
    [self.navigationController pushViewController:webView animated:YES];
}


#pragma mark IBAction


//复制
- (IBAction)copyAction:(id)sender {
    UIPasteboard * pastboard = [UIPasteboard generalPasteboard];
    UserInfo *info = [GetCore(HJUserCoreHelp) getUserInfoInDB:[GetCore(HJAuthCoreHelp) getUid].userIDValue];
    pastboard.string = info.erbanNo;
    [MBProgressHUD showSuccess:@"复制成功"];
}

//关注
- (IBAction)followListAction:(id)sender {
    
    UIViewController *vc = HJMessageBoard(@"HJFollowListViewController");
    [self.navigationController pushViewController:vc animated:YES];
}

//粉丝
- (IBAction)fanListAction:(id)sender {
    
    UIViewController *vc = HJMessageBoard(@"HJFansListViewController");
    [self.navigationController pushViewController:vc animated:YES];
}

//好友
- (IBAction)friendListAction:(id)sender {
//    UIViewController *vc = HJMessageBoard(@"HJFriendsListVC");
//    [self.navigationController pushViewController:vc animated:YES];
    
}

//我的空间
- (IBAction)mySpaceAction:(id)sender {
    
    HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
    vc.userID = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
    [self.navigationController pushViewController:vc animated:YES];

    
}

//我的钱包
- (IBAction)walletAction:(id)sender {
//    UIViewController *vc = [[HJPurseViewControllerFactory sharedFactory] instantiateRechargeViewController];
//    [self.navigationController pushViewController:vc animated:YES];
    
    UIViewController *vc = [[HJPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
       HJMyWalletVC* walletVC = (HJMyWalletVC*)vc;
       walletVC.goldNum = GetCore(PurseCore).balanceInfo.goldNum ;
        walletVC.diamondNum = GetCore(PurseCore).balanceInfo.diamondNum;
       walletVC.boboNum = self.pointLabel.text;
           [self.navigationController pushViewController:vc animated:YES];
    
}

//我的等级
- (IBAction)levelAction:(id)sender {
    
    HJPersonLevelVC *vc = [[HJPersonLevelVC alloc] init];
    [self.navigationController pushViewController:vc animated:YES];
}

//我的房间
- (IBAction)roomAction:(id)sender {
    
    [[HJRoomViewControllerCenter defaultCenter] openRoonWithType:RoomType_Game];
}
//装扮商城
- (IBAction)mallAction:(id)sender {
    HJMallViewController *car = [[HJMallViewController alloc] init];
    [self.navigationController pushViewController:car animated:YES];
}

//实名认证
- (IBAction)authAction:(id)sender {
    [self loadWebView:@"/front/real_name/index.html"];

}

//热门活动
- (IBAction)hotActivityAction:(id)sender {
    HJActivityViewController *vc = [HJActivityViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

//客服中心
- (IBAction)kefuAction:(id)sender {
    [self loadWebView:@"/front/links/links.html"];
}

//签到
- (IBAction)signAction:(id)sender {
    UIViewController *vc = HJSignStoryBoard(@"HJSignHomeViewController");
    [self.navigationController pushViewController:vc animated:YES];
}

//编辑
- (IBAction)setAction:(id)sender {
    HJPersonalEditViewController *vc = (HJPersonalEditViewController *)[[HJUserViewControllerFactory sharedFactory] instantiatePersonalEditViewController];
       
       [self.navigationController pushViewController:vc animated:YES];
}

//AuthCoreClient
- (void)onLoginSuccess
{
    __weak typeof(self)weakSelf = self;
    [GetCore(HJUserCoreHelp) getUserInfo:[GetCore(HJAuthCoreHelp).getUid userIDValue] refresh:YES success:^(UserInfo *info) {
        
        [weakSelf.avatarImageView qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        if (info.headwearUrl != nil && info.headwearUrl.length>0) {
            weakSelf.headWearImgView.hidden = NO;
             [weakSelf.headWearImgView qn_setImageImageWithUrl:info.headwearUrl placeholderImage:default_avatar type:ImageTypeUserIcon];
        }else{
            weakSelf.headWearImgView.hidden = YES;
        }
        weakSelf.sexImageView.image = [UIImage imageNamed:info.gender==UserInfo_Male?@"hj_home_attend_man":@"hj_home_attend_woman"];
        
        //自适应宽度、调整约束
        if (info.charmLevel>=0) {
            weakSelf.meiliImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:info.charmLevel]];
            weakSelf.left_rich.constant = 5;

        }else{
            weakSelf.meiliImageView.image = nil;
            weakSelf.left_rich.constant = -43;

        }
        
        if (info.experLevel>=0) {
            weakSelf.levelImageView.image = [UIImage imageNamed:[NSString getLevelImageName:info.experLevel]];
        }else{
            weakSelf.levelImageView.image = nil;
        }
        
        weakSelf.nameLabel.text = info.nick;
        
        CGSize size = [weakSelf.nameLabel.text boundingRectWithSize:CGSizeMake(0, weakSelf.nameLabel.height) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:weakSelf.nameLabel.font} context:nil].size;
       
        CGFloat width = size.width + 5;
        
        
        
        weakSelf.width_nameLabel.constant = (kScreenWidth - width)<134?kScreenWidth-134:width;
        
        
        weakSelf.idLabel.text = [NSString stringWithFormat:@"ID:%@",info.erbanNo];

        weakSelf.fanLabel.text = [NSString stringWithFormat:@"%ld",info.fansNum];
        weakSelf.followLabel.text = [NSString stringWithFormat:@"%ld",info.followNum];
        weakSelf.activeLabel.text = [NSString stringWithFormat:@"%ld",info.liveness];
        if (info.userDesc!=nil && info.userDesc.length>0) {
                   weakSelf.desSignLabel.text = info.userDesc;
               }
    }];
    
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self updateDiandianCoinData];
        
        self.coinLabel.text = [NSString stringWithFormat:@"%.0f",[GetCore(PurseCore).balanceInfo.goldNum doubleValue]];
    });
    
}


- (void)updateDiandianCoinData
{
    __weak typeof(self)weakSelf = self;
    [HJHttpRequestHelper requestDiandianCoinNum:^(id  _Nonnull data) {
        
        HJPointCoinWalletModel *model = data;
        weakSelf.pointLabel.text = JX_STR_AVOID_nil(model.mcoinNum);
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        weakSelf.pointLabel.text = 0;
    }];
}
@end
