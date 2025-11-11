//
//  HJRechargeViewController.m
//  HJLive
//
//  Created by feiyin on 2020/6/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRechargeViewController.h"
#import "AvatarControl.h"
#import "PurseCore.h"
#import "HJPurseCoreClient.h"
#import "HJRechargeViewCollectionViewCell.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "RechargeInfo.h"
#import "YYDefaultTheme.h"
#import "RechargeInfo.h"
#import "PurseCore.h"
#import "BalanceInfo.h"
#import "HJVersionCoreHelp.h"
#import "HJPurseViewControllerFactory.h"
#import "MMAlertView.h"
#import "MMSheetView.h"
#import "UIColor+UIColor_Hex.h"

#import "HJBillMenuViewController.h"

#import "HJHttpRequestHelper+User.h"

#import "HJAlterShower.h"
#import "HJBindingWXVC.h"

@interface HJRechargeViewController ()<UICollectionViewDelegate, UICollectionViewDataSource, HJPurseCoreClient, HJRechargeViewCellDelegate,UIGestureRecognizerDelegate>
@property (weak, nonatomic) IBOutlet UILabel *balanceLabel;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;

@property (strong, nonatomic) NSArray *productList;
@property (strong, nonatomic) BalanceInfo *balanceInfo;

@property (weak, nonatomic) IBOutlet UIButton *reChargeBtn;

- (IBAction)onRechargeBtnClicked:(id)sender;

@property (nonatomic, assign) BOOL first;
@property (weak, nonatomic) IBOutlet UIView *countView;

@end

@implementation HJRechargeViewController
{
    NSInteger _index;
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    NSString *uid = GetCore(HJAuthCoreHelp).getUid;
    [GetCore(PurseCore) requestBalanceInfo:uid.userIDValue];
    [self initView];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.first = YES;
    // Do any additional setup after loading the view.
    AddCoreClient(HJPurseCoreClient, self);
    self.title = @"我的开心";
    
    self.balanceInfo = GetCore(PurseCore).balanceInfo;
    self.reChargeBtn.layer.cornerRadius = 20;
    self.reChargeBtn.layer.masksToBounds = YES;
    
}


- (void)dealloc
{
    RemoveCoreClientAll(self);
}

- (void)initView
{

    UserID myUid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
//    UserInfo *userInfo = [GetCore(UserCore) getUserInfo:myUid refresh:NO];
//    @weakify(self);
    self.balanceLabel.text = [NSString stringWithFormat:@"%.2lf", [self.balanceInfo.goldNum doubleValue]];
    
    [GetCore(PurseCore) requestRechargeList];
    [self.collectionView registerNib:[UINib nibWithNibName:@"HJRechargeViewCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"HJRechargeViewCollectionViewCell"];
    
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
//    [GetCore(PurseCore) queryFirst];
}


#pragma mark - CollectionViewDelegate & Datasource

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HJRechargeViewCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"HJRechargeViewCollectionViewCell" forIndexPath:indexPath];
    RechargeInfo *rechargeInfo = [self.productList safeObjectAtIndex:indexPath.row];
    cell.titleLable.text = rechargeInfo.prodDesc;
    cell.rmbLabel.text = [NSString stringWithFormat:@"%ld元", (long)rechargeInfo.money.integerValue];
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
        [cell.bgImageView setImage:[UIImage imageNamed:@"hj_recharge_icon_yes"]];
    } else {

        cell.goldlabel.textColor = [UIColor colorWithHexString:@"#333333"];
        cell.rmbLabel.textColor = [UIColor colorWithHexString:@"#DB7EFF"];
        [cell.bgImageView setImage:[UIImage imageNamed:@"hj_recharge_icon_no"]];
    }
    return cell;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.productList.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake((XC_SCREE_W-(12*4))/3, 136);

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
    
    HJBillMenuViewController *VC = (HJBillMenuViewController *)[[HJPurseViewControllerFactory sharedFactory]instantiateBillMenuViewController];
    [self.navigationController pushViewController:VC animated:YES];
}


- (IBAction)onRechargeBtnClicked:(id)sender {
    if (self.productList.count > 0) {
        RechargeInfo *info = [self.productList objectAtIndex:_index];
        
        [MBProgressHUD showMessage:NSLocalizedString(XCPurseConectingAppStore, nil)];
        
        [GetCore(PurseCore) requestInAppPurseProductAndBuy:info.chargeProdId];
    }
    
}

- (void)onRmbSelected:(NSInteger)index
{
    _index = index;
    [self.collectionView reloadData];
    
}

#pragma mark - HJPurseCoreClient
- (void)onBalanceInfoUpdate:(BalanceInfo *)balanceInfo
{
    self.balanceInfo = balanceInfo;
    self.balanceLabel.text = [NSString stringWithFormat:@"%.2lf", [self.balanceInfo.goldNum doubleValue]];
    
//    [GetCore(PurseCore) queryFirst];
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
    NSString *uid = GetCore(HJAuthCoreHelp).getUid;
    [GetCore(PurseCore) requestBalanceInfo:uid.userIDValue];
    
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
