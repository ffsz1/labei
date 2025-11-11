//
//  YPMyViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMyViewController.h"
#import "YPMySpaceVC.h"
#import "YPPersonLevelVC.h"
#import "YPActivityViewController.h"
#import "YPMallViewController.h"

#import "YPMineCell.h"

#import "YPPointCoinWalletModel.h"

#import "YPPurseViewControllerFactory.h"
#import "YPSettingViewControllerFactory.h"
#import "YPRoomViewControllerCenter.h"
#import "YPWKWebViewController.h"

#import "NSString+GGImage.h"
#import "HJAuthCoreClient.h"
#import "YPHttpRequestHelper+Sign.h"
#import "YPPurseCore.h"
#import "YPUserViewControllerFactory.h"
#import "YPFriendsListVC.h"
#import "YPPersonalEditViewController.h"
#import "YPMyWalletVC.h"
#import "YPRechargeViewController.h"


@interface YPMyViewController ()<UITableViewDelegate,UITableViewDataSource,HJAuthCoreClient>
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



@property (weak, nonatomic) IBOutlet NSLayoutConstraint *my_width_right_layout;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *my_width_left_layout;

@end

@implementation YPMyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    AddCoreClient(HJAuthCoreClient, self);
        self.titleArr = @[@"我的钱包",@"我的等级",@"实名认证",@"青少年模式",@"联系我们",@"设置"];//@"热门活动",

        self.imageArr = @[@"yp_qqianbao",@"yp_ddengji",@"yp_shimingrenzheng_icon",@"yp_me_youth",@"yp_lianxiwomeng",@"yp_my_setting_icon"];//@"yp_my_gift_icon",

    self.my_width_left_layout.constant = (kScreenWidth-60)/2.0;
    self.my_width_right_layout.constant = (kScreenWidth-60)/2.0;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
   
    
    YPLightStatusBar
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    if (@available(iOS 11.0, *)) {
        self.tableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
    } else {
        self.automaticallyAdjustsScrollViewInsets = NO;
    }
    
    if (GetCore(YPAuthCoreHelp).isLogin) {
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
    YPMineCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPMineCell" forIndexPath:indexPath];
    
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
//                YPActivityViewController *vc = [YPActivityViewController new];
//                   [self.navigationController pushViewController:vc animated:YES];
//            }
//                break;
        case 0:
        {
            //我的钱包
            [self jumpMyWallet];
        }
            break;
        case 1:
        {
            //我的等级
          
            [self.navigationController pushViewController:[[YPPersonLevelVC alloc] init] animated:YES];
        }
            break;
        case 2:
        {
            //实名认证
            [self loadWebView:@"/front/real_name/index.html"];
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
                //联系我们
                
            }
                break;
            
        case 5:
        {
// 设置
            UIViewController *vc = [[YPSettingViewControllerFactory sharedFactory] instantiateSettingViewController];
            [self.navigationController pushViewController:vc animated:YES];
        }
        default:
            break;
    }
}



- (void)loadWebView:(NSString *)url
{
    NSString *urlSting = [NSString stringWithFormat:@"%@%@",[YPHttpRequestHelper getHostUrl],url];

    YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
    webView.url = [NSURL URLWithString:urlSting];
    [self.navigationController pushViewController:webView animated:YES];
}
//我的钱包
-(void)jumpMyWallet{
    UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateRechargeViewController];
    YPRechargeViewController* walletVC = (YPRechargeViewController*)vc;
//       walletVC.goldNum = GetCore(YPPurseCore).balanceInfo.goldNum ;
//        walletVC.diamondNum = GetCore(YPPurseCore).balanceInfo.diamondNum;
//       walletVC.boboNum = self.pointLabel.text;
           [self.navigationController pushViewController:walletVC animated:YES];
//    UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
//       YPMyWalletVC* walletVC = (YPMyWalletVC*)vc;
//       walletVC.goldNum = GetCore(YPPurseCore).balanceInfo.goldNum ;
//        walletVC.diamondNum = GetCore(YPPurseCore).balanceInfo.diamondNum;
//       walletVC.boboNum = self.pointLabel.text;
//           [self.navigationController pushViewController:vc animated:YES];
}
#pragma mark IBAction


//复制
- (IBAction)copyAction:(id)sender {
    UIPasteboard * pastboard = [UIPasteboard generalPasteboard];
    UserInfo *info = [GetCore(YPUserCoreHelp) getUserInfoInDB:[GetCore(YPAuthCoreHelp) getUid].userIDValue];
    pastboard.string = info.erbanNo;
    [MBProgressHUD showSuccess:@"复制成功"];
}

//关注
- (IBAction)followListAction:(id)sender {
    
    UIViewController *vc = YPMessageBoard(@"YPFollowListViewController");
    [self.navigationController pushViewController:vc animated:YES];
}

//粉丝
- (IBAction)fanListAction:(id)sender {
    
    UIViewController *vc = YPMessageBoard(@"YPFansListViewController");
    [self.navigationController pushViewController:vc animated:YES];
}

//我的空间
- (IBAction)mySpaceAction:(id)sender {
    
    YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
    vc.userID = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    [self.navigationController pushViewController:vc animated:YES];

    
}

//我的头像
- (IBAction)walletAction:(id)sender {
//    UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateRechargeViewController];
//    [self.navigationController pushViewController:vc animated:YES];
    
    YPMallViewController *car = [[YPMallViewController alloc] init];
    car.isZuojia = NO;
    [self.navigationController pushViewController:car animated:YES];
    
}





//我的座驾
- (IBAction)levelAction:(id)sender {
    YPMallViewController *car = [[YPMallViewController alloc] init];
    car.isZuojia = YES;
    [self.navigationController pushViewController:car animated:YES];
   
}

//我的房间
- (IBAction)roomAction:(id)sender {
    
    [[YPRoomViewControllerCenter defaultCenter] openRoonWithType:RoomType_Game];
}
//装扮商城
- (IBAction)mallAction:(id)sender {
//    YPMallViewController *car = [[YPMallViewController alloc] init];
//    [self.navigationController pushViewController:car animated:YES];
}

//实名认证
- (IBAction)authAction:(id)sender {
    [self loadWebView:@"/front/real_name/index.html"];

}

//热门活动
- (IBAction)hotActivityAction:(id)sender {
    YPActivityViewController *vc = [YPActivityViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

//客服中心
- (IBAction)kefuAction:(id)sender {
    [self loadWebView:@"/front/links/links.html"];
}

//签到
- (IBAction)signAction:(id)sender {
    UIViewController *vc = YPSignStoryBoard(@"YPSignHomeViewController");
    [self.navigationController pushViewController:vc animated:YES];
}

//编辑
- (IBAction)setAction:(id)sender {
    YPPersonalEditViewController *vc = (YPPersonalEditViewController *)[[YPUserViewControllerFactory sharedFactory] instantiatePersonalEditViewController];
       
       [self.navigationController pushViewController:vc animated:YES];
}

//AuthCoreClient
- (void)onLoginSuccess
{
    __weak typeof(self)weakSelf = self;
    [GetCore(YPUserCoreHelp) getUserInfo:[GetCore(YPAuthCoreHelp).getUid userIDValue] refresh:YES success:^(UserInfo *info) {
        
        [weakSelf.avatarImageView qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        if (info.headwearUrl != nil && info.headwearUrl.length>0) {
            weakSelf.headWearImgView.hidden = NO;
             [weakSelf.headWearImgView qn_setImageImageWithUrl:info.headwearUrl placeholderImage:default_avatar type:ImageTypeUserIcon];
        }else{
            weakSelf.headWearImgView.hidden = YES;
        }
        weakSelf.sexImageView.image = [UIImage imageNamed:info.gender==UserInfo_Male?@"yp_home_attend_man":@"yp_home_attend_woman"];
        
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
        
        self.coinLabel.text = [NSString stringWithFormat:@"%.0f",[GetCore(YPPurseCore).balanceInfo.goldNum doubleValue]];
    });
    
}


- (void)updateDiandianCoinData
{
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper requestDiandianCoinNum:^(id  _Nonnull data) {
        
        YPPointCoinWalletModel *model = data;
        weakSelf.pointLabel.text = JX_STR_AVOID_nil(model.mcoinNum);
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        weakSelf.pointLabel.text = 0;
    }];
}
@end
