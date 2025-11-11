//
//  YPMyWalletVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "YPPublicRechargeView.h"
#import "YPBillMenuViewController.h"
#import "YPPurseViewControllerFactory.h"
//#import "FYWithdrawDepositVC.h"
#import "YPFYCashAndExchangeVC.h"
//#import "FYBillCatalogueVC.h"

#import "YPPurseCore.h"
#import "HJPurseCoreClient.h"
#import "YPAuthCoreHelp.h"
#import "YPUserCoreHelp.h"
#import "YPBalanceInfo.h"
#import "YPMyWalletVC.h"
#import "YPBillCatalogueVC.h"
@interface YPMyWalletVC ()
@property (weak, nonatomic) IBOutlet UILabel *goldLabel;
@property (weak, nonatomic) IBOutlet UILabel *diamondLabel;

@property (weak, nonatomic) IBOutlet UILabel *duiHuanLabel;
@end

@implementation YPMyWalletVC

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
     [GetCore(YPPurseCore) requestBalanceInfo:GetCore(YPAuthCoreHelp).getUid.userIDValue];
     YPBalanceInfo* balanceInfo = GetCore(YPPurseCore).balanceInfo;
//    self.goldLabel.text = balanceInfo.goldNum;
    self.goldLabel.text = [NSString stringWithFormat:@"%.1f",[balanceInfo.goldNum floatValue]];
//    self.diamondLabel.text = balanceInfo.diamondNum;
     self.diamondLabel.text = [NSString stringWithFormat:@"%.1f",[balanceInfo.diamondNum floatValue]];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    YPBlackStatusBar

}
- (void)onBalanceInfoUpdate:(YPBalanceInfo *)balanceInfo
{
    self.goldLabel.text = [NSString stringWithFormat:@"%.1f",balanceInfo.goldNum.floatValue];
   
}
//MARK: - Action

-(void)duihuanAction{
    
//    YPFYCashAndExchangeVC* vc = [[YPFYCashAndExchangeVC alloc] init];
//    vc.isDuiHuanPage = YES;
//               [self.navigationController pushViewController:vc animated:YES];
    
}

//账单Action XCBillMenuViewController
-(void)billListAction{
         YPBillCatalogueVC *VC = (YPBillCatalogueVC *)[[YPPurseViewControllerFactory sharedFactory]instantiateHJBillCatalogueVC];
            [self.navigationController pushViewController:VC animated:YES];
     }
//充值提现
- (IBAction)takePayAction:(id)sender {
    
    [self showRealNameAuth];
    
//        UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateRechargeViewController];
//        [self.navigationController pushViewController:vc animated:YES];
    
//     FYWithdrawDepositVC* vc = [[FYWithdrawDepositVC alloc] init];
//         [self.navigationController pushViewController:vc animated:YES];
}
//提现兑换
- (IBAction)takeCashAction:(id)sender {
//    YPFYCashAndExchangeVC* vc = [[YPFYCashAndExchangeVC alloc] init];
//            [self.navigationController pushViewController:vc animated:YES];
    YPFYCashAndExchangeVC* vc = [[YPFYCashAndExchangeVC alloc] init];
               [self.navigationController pushViewController:vc animated:YES];
}

//任务中心 MeMe币
- (IBAction)takeBoboAction:(id)sender {
    UIViewController *vc = YPSignStoryBoard(@"HJSignHomeVC");
    [self.navigationController pushViewController:vc animated:YES];
}


//MARK: - 个人信息更新
//- (void)onDrawExchangeInfoUpdate:(DrawExchangeModel *)balanceInfo
//{
//
//    self.diamondLabel.text = [NSString stringWithFormat:@"%.0lf", [balanceInfo.diamondNum doubleValue]];
//    GetCore(YPPurseCore).balanceInfo.diamondNum = balanceInfo.diamondNum;
//
//
//
//
//}

-(void)showRealNameAuth{
    [YPPublicRechargeView show:^(HJPublicRechargeType type) {
               switch (type) {
                          case HJPublicRechargeConfirmType:
                          {
                            //jump 认证页面H5
//                              [self loadWebView:@"/front/real_name/index.html"];
                              UIPasteboard * pastboard = [UIPasteboard generalPasteboard];
                                
                                 pastboard.string = @"haijiaoxingqiu6";
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




@end
