//
//  YPRechargeViewController.m
//  HJLive
//
//  Created by feiyin on 2020/6/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRechargeViewController.h"
#import "YPAvatarControl.h"
#import "YPPurseCore.h"
#import "HJPurseCoreClient.h"
#import "YPRechargeViewCollectionViewCell.h"
#import "YPAuthCoreHelp.h"
#import "YPUserCoreHelp.h"
#import "YPRechargeInfo.h"
#import "YPYYDefaultTheme.h"
#import "YPRechargeInfo.h"
#import "YPPurseCore.h"
#import "YPBalanceInfo.h"
#import "YPVersionCoreHelp.h"
#import "YPPurseViewControllerFactory.h"
#import "MMAlertView.h"
#import "MMSheetView.h"
#import "UIColor+UIColor_Hex.h"

#import "YPBillMenuViewController.h"

#import "YPHttpRequestHelper+User.h"

#import "YPAlterShower.h"
#import "YPBindingWXVC.h"
#import "YPBillCatalogueVC.h"
#import "YPFYCashAndExchangeVC.h"

@interface YPRechargeViewController ()<UICollectionViewDelegate, UICollectionViewDataSource, HJPurseCoreClient, HJRechargeViewCellDelegate,UIGestureRecognizerDelegate>
@property (weak, nonatomic) IBOutlet UILabel *balanceLabel;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;

@property (strong, nonatomic) NSArray *productList;
@property (strong, nonatomic) YPBalanceInfo *balanceInfo;

@property (weak, nonatomic) IBOutlet UIButton *reChargeBtn;

- (IBAction)onRechargeBtnClicked:(id)sender;

@property (nonatomic, assign) BOOL first;
@property (weak, nonatomic) IBOutlet UIView *countView;

@end

@implementation YPRechargeViewController
{
    NSInteger _index;
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    NSString *uid = GetCore(YPAuthCoreHelp).getUid;
    [GetCore(YPPurseCore) requestBalanceInfo:uid.userIDValue];
    [self initView];
    YPLightStatusBar
    
//    [self.navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
//    [self.navigationController.navigationBar setShadowImage:[UIImage new]];
//    [[UINavigationBar appearance] setBarTintColor:[UIColor colorWithHexString:@"#8A58FF"]];
//       [[UINavigationBar appearance] setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
//    [[UINavigationBar appearance] setBarTintColor:[UIColor colorWithHexString:@"#FFFFFF"]];
//    [[UINavigationBar appearance] setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor blackColor]}];
}
-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
   
//    [[UINavigationBar appearance] setBarTintColor:[UIColor colorWithHexString:@"#FFFFFF"]];
//    [[UINavigationBar appearance] setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor blackColor]}];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    YPLightStatusBar
    UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];

        [rightBtn setTitle:@"账单"  forState:UIControlStateNormal];
    rightBtn.titleLabel.font = [UIFont systemFontOfSize:14];
        rightBtn.frame = CGRectMake(0, 0, 50, 30);

        [rightBtn addTarget:self action:@selector(onClickedOKbtn) forControlEvents:UIControlEventTouchUpInside];

    UIButton *rightBtn2 = [UIButton buttonWithType:UIButtonTypeCustom];

        [rightBtn2 setTitle:@"我的收益"  forState:UIControlStateNormal];

        rightBtn2.frame = CGRectMake(50, 0, 60, 30);
    rightBtn2.titleLabel.font = [UIFont systemFontOfSize:14];
        [rightBtn2 addTarget:self action:@selector(onClickedOKbtn2) forControlEvents:UIControlEventTouchUpInside];
        
    UIView* vieww = [[UIView alloc] init];
    vieww.frame = CGRectMake(kScreenWidth-122, iPhoneX?40:30, 110, 30);
    [vieww addSubview:rightBtn];
    [vieww addSubview:rightBtn2];
    
    UIButton *backBtn = [UIButton buttonWithType:UIButtonTypeCustom];

        [backBtn setImage:[UIImage imageNamed:@"yp_space_back"] forState:UIControlStateNormal];
  
    backBtn.frame = CGRectMake(5, iPhoneX?40:30, 50, 30);

        [backBtn addTarget:self action:@selector(onClickBackBtn) forControlEvents:UIControlEventTouchUpInside];
    
    UILabel* titleLabel = [[UILabel alloc] init];
    titleLabel.frame = CGRectMake(kScreenWidth/2-40, iPhoneX?40:30, 80, 30);
    titleLabel.text = @"我的金币";
    titleLabel.textColor = [UIColor whiteColor];
    titleLabel.font = [UIFont boldSystemFontOfSize:18];
    
    [self.view addSubview:vieww];
    [self.view addSubview:backBtn];
    [self.view addSubview:titleLabel];
    
    
    
   
    

    

    self.first = YES;
    // Do any additional setup after loading the view.
    AddCoreClient(HJPurseCoreClient, self);
    self.title = @"我的金币";
    
    self.balanceInfo = GetCore(YPPurseCore).balanceInfo;
    self.reChargeBtn.layer.cornerRadius = 20;
    self.reChargeBtn.layer.masksToBounds = YES;
    
}
-(void)onClickBackBtn{
    [self.navigationController popViewControllerAnimated:NO];
}

- (void)dealloc
{
    RemoveCoreClientAll(self);
}


- (void)initView
{

    UserID myUid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
//    UserInfo *userInfo = [GetCore(UserCore) getUserInfo:myUid refresh:NO];
//    @weakify(self);
    self.balanceLabel.text = [NSString stringWithFormat:@"%.2lf", [self.balanceInfo.goldNum doubleValue]];
    
    [GetCore(YPPurseCore) requestRechargeList];
    [self.collectionView registerNib:[UINib nibWithNibName:@"YPRechargeViewCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"YPRechargeViewCollectionViewCell"];
    
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
//    [GetCore(YPPurseCore) queryFirst];
}


#pragma mark - CollectionViewDelegate & Datasource

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    YPRechargeViewCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPRechargeViewCollectionViewCell" forIndexPath:indexPath];
    YPRechargeInfo *rechargeInfo = [self.productList safeObjectAtIndex:indexPath.row];
    cell.titleLable.text = rechargeInfo.prodDesc;
    cell.rmbLabel.text = [NSString stringWithFormat:@"¥ %ld元", (long)rechargeInfo.money.integerValue];
    cell.goldlabel.text = rechargeInfo.prodName;
    cell.index = indexPath.row;
    cell.delegate = self;
    

    if (self.first) {
        cell.titleLable.hidden = YES;
    }
    else {
        cell.titleLable.hidden = NO;
    }
    
    if (indexPath.row == _index) {

        cell.goldlabel.textColor = [UIColor colorWithHexString:@"#FFFFFF"];
        cell.rmbLabel.textColor = [UIColor colorWithHexString:@"#FFFFFF"];
        [cell.bgImageView setImage:[UIImage imageNamed:@"yp_recharge_icon_yes"]];
    } else {

        cell.goldlabel.textColor = [UIColor colorWithHexString:@"#333333"];
        cell.rmbLabel.textColor = [UIColor colorWithHexString:@"#999999"];
        [cell.bgImageView setImage:[UIImage imageNamed:@"yp_recharge_icon_no"]];
    }
    return cell;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.productList.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake((XC_SCREE_W-(15*4))/3, XC_SCREE_W/375.0 *95);

}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    [collectionView deselectItemAtIndexPath:indexPath animated:YES];
    [self onRmbSelected:indexPath.item];
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    return UIEdgeInsetsMake(0, 7.5, 0, 7.5);

}

// 账单明细
- (IBAction)billDetailAction:(id)sender {
    
    YPBillMenuViewController *VC = (YPBillMenuViewController *)[[YPPurseViewControllerFactory sharedFactory]instantiateBillMenuViewController];
    [self.navigationController pushViewController:VC animated:YES];
}


- (IBAction)onRechargeBtnClicked:(id)sender {
    if (self.productList.count > 0) {
        YPRechargeInfo *info = [self.productList objectAtIndex:_index];
        
        [MBProgressHUD showMessage:NSLocalizedString(XCPurseConectingAppStore, nil)];
        
        [GetCore(YPPurseCore) requestInAppPurseProductAndBuy:info.chargeProdId];
    }
    
}

- (void)onRmbSelected:(NSInteger)index
{
    _index = index;
    [self.collectionView reloadData];
    
}
#pragma mark - private function
//账单
-(void)onClickedOKbtn{
    YPBillCatalogueVC *VC = (YPBillCatalogueVC *)[[YPPurseViewControllerFactory sharedFactory]instantiateHJBillCatalogueVC];
       [self.navigationController pushViewController:VC animated:YES];
}
//我的收益
-(void)onClickedOKbtn2{
    YPFYCashAndExchangeVC* vc = [[YPFYCashAndExchangeVC alloc] init];
               [self.navigationController pushViewController:vc animated:YES];
}



#pragma mark - HJPurseCoreClient
- (void)onBalanceInfoUpdate:(YPBalanceInfo *)balanceInfo
{
    self.balanceInfo = balanceInfo;
    self.balanceLabel.text = [NSString stringWithFormat:@"%.2lf", [self.balanceInfo.goldNum doubleValue]];
    
//    [GetCore(YPPurseCore) queryFirst];
}

- (void)onRequestRechargeListSuccess:(NSArray *)list
{
    self.productList = list;
    [self.collectionView reloadData];
}

- (void)onRequestRechargeListFailth
{
    
}


//MARK:苹果内购流程
- (void)entryRequestProductProgressStatus:(BOOL)Status {
    if (Status == YES) {
        [MBProgressHUD showMessage:NSLocalizedString(XCPurseStartBuying, nil)];
    }else {
        [MBProgressHUD showMessage:NSLocalizedString(XCPurseBuyingFailed, nil)];
    }
}

- (void)entryPurchaseProcessStatus:(XCPaymentStatus)Status {
    if (Status == XCPaymentStatusPurchased) {
        [MBProgressHUD showMessage:NSLocalizedString(XCPurseVerifying, nil)];
    }else if (Status == XCPaymentStatusPurchasing) {
        [MBProgressHUD showMessage:NSLocalizedString(XCPurseBuying, nil)];
    }else if (Status == XCPaymentStatusFailed) {
        [MBProgressHUD showError:NSLocalizedString(XCPurseBuyedFailed, nil)];
    }else if (Status == XCPaymentStatusDeferred) {
        [MBProgressHUD showError:NSLocalizedString(XCPurseBuyedNoKownError, nil)];
    }
    
}

- (void)entryCheckReceiptSuccess {
    NSString *uid = GetCore(YPAuthCoreHelp).getUid;
    [GetCore(YPPurseCore) requestBalanceInfo:uid.userIDValue];
    
    [MBProgressHUD showSuccess:NSLocalizedString(XCPurseBuyedSuccess, nil)];
    [MBProgressHUD hideHUD];
    
}

- (void)entryCheckReceiptFaildWithMessage:(NSString *)message {
    [MBProgressHUD showError:NSLocalizedString(XCPurseVerifyedFailed, nil)];
    [MBProgressHUD hideHUD];
}

- (void)addRechargeOrderFail:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}


- (void)onCodeRecharge {
    
}

#pragma mark - UIGestureRecognizerDelegate

- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer {
    //   BOOL ok = YES; // 默认为支持右滑反回
    return YES;
}



@end
