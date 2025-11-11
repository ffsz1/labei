//
//  HJMyWalletVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "HJPublicRechargeView.h"
#import "HJBillMenuViewController.h"
#import "HJPurseViewControllerFactory.h"
//#import "FYWithdrawDepositVC.h"
#import "FYCashAndExchangeVC.h"
//#import "FYBillCatalogueVC.h"

#import "PurseCore.h"
#import "HJPurseCoreClient.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "BalanceInfo.h"
#import "HJMyWalletVC.h"
#import "HJBillCatalogueVC.h"
@interface HJMyWalletVC ()
@property (weak, nonatomic) IBOutlet UILabel *goldLabel;
@property (weak, nonatomic) IBOutlet UILabel *diamondLabel;

@property (weak, nonatomic) IBOutlet UILabel *duiHuanLabel;
@end

@implementation HJMyWalletVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title=@"我的钱包";
      AddCoreClient(HJPurseCoreClient, self);
    self.navigationItem.rightBarButtonItem =  [[UIBarButtonItem alloc] initWithTitle:@"账单" style:UIBarButtonItemStyleDone target:self action:@selector(billListAction)];
    UITapGestureRecognizer* tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(duihuanAction)];
    [self.duiHuanLabel addGestureRecognizer:tap];
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
     [GetCore(PurseCore) requestBalanceInfo:GetCore(HJAuthCoreHelp).getUid.userIDValue];
     BalanceInfo* balanceInfo = GetCore(PurseCore).balanceInfo;
//    self.goldLabel.text = balanceInfo.goldNum;
    self.goldLabel.text = [NSString stringWithFormat:@"%.1f",[balanceInfo.goldNum floatValue]];
//    self.diamondLabel.text = balanceInfo.diamondNum;
     self.diamondLabel.text = [NSString stringWithFormat:@"%.1f",[balanceInfo.diamondNum floatValue]];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    HJBlackStatusBar

}
- (void)onBalanceInfoUpdate:(BalanceInfo *)balanceInfo
{
    self.goldLabel.text = [NSString stringWithFormat:@"%.1f",balanceInfo.goldNum.floatValue];
   
}
//MARK: - Action

-(void)duihuanAction{
    
//    FYCashAndExchangeVC* vc = [[FYCashAndExchangeVC alloc] init];
//    vc.isDuiHuanPage = YES;
//               [self.navigationController pushViewController:vc animated:YES];
    
}

//账单Action XCBillMenuViewController
-(void)billListAction{
         HJBillCatalogueVC *VC = (HJBillCatalogueVC *)[[HJPurseViewControllerFactory sharedFactory]instantiateHJBillCatalogueVC];
            [self.navigationController pushViewController:VC animated:YES];
     }
//充值提现
- (IBAction)takePayAction:(id)sender {
    [self loadWebView:@"http://lbapi.haijiaoxingqiu.cn/front/pay_huiju_new/index.html"];
    
    
    
    
//    [self showRealNameAuth];
    
//        UIViewController *vc = [[HJPurseViewControllerFactory sharedFactory] instantiateRechargeViewController];
//        [self.navigationController pushViewController:vc animated:YES];
    
//     FYWithdrawDepositVC* vc = [[FYWithdrawDepositVC alloc] init];
//         [self.navigationController pushViewController:vc animated:YES];
}
//提现兑换
- (IBAction)takeCashAction:(id)sender {
//    FYCashAndExchangeVC* vc = [[FYCashAndExchangeVC alloc] init];
//            [self.navigationController pushViewController:vc animated:YES];
    FYCashAndExchangeVC* vc = [[FYCashAndExchangeVC alloc] init];
               [self.navigationController pushViewController:vc animated:YES];
}

//任务中心 MeMe币
- (IBAction)takeBoboAction:(id)sender {
    UIViewController *vc = HJSignStoryBoard(@"HJSignHomeVC");
    [self.navigationController pushViewController:vc animated:YES];
}


//MARK: - 个人信息更新
//- (void)onDrawExchangeInfoUpdate:(DrawExchangeModel *)balanceInfo
//{
//
//    self.diamondLabel.text = [NSString stringWithFormat:@"%.0lf", [balanceInfo.diamondNum doubleValue]];
//    GetCore(PurseCore).balanceInfo.diamondNum = balanceInfo.diamondNum;
//
//
//
//
//}

-(void)showRealNameAuth{
    [HJPublicRechargeView show:^(HJPublicRechargeType type) {
               switch (type) {
                          case HJPublicRechargeConfirmType:
                          {
                            //jump 认证页面H5
//                              [self loadWebView:@"/front/real_name/index.html"];
                              UIPasteboard * pastboard = [UIPasteboard generalPasteboard];
                                
                                 pastboard.string = @"xxxxxx";
                                 [MBProgressHUD showSuccess:@"复制成功"];
                          }
                              break;
                          case HJPublicRechargeCancelType:
                          {
    
    
                          }
                              break;
                       default:
                        break;
               }
    
    
           }];
}

- (void)loadWebView:(NSString *)urlSting
{
    NSURL* url = [NSURL URLWithString:urlSting];
    if( [[UIApplication sharedApplication]canOpenURL:url] ) {
        [[UIApplication sharedApplication]openURL:url options:@{}completionHandler:^(BOOL success) {
        }];
    }
}


@end
