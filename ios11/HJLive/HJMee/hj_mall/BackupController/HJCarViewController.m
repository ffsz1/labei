//
//  HJCarViewController.m
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJCarViewController.h"
#import "HJCarCardCell.h"
#import "HJCarSysCoreClient.h"
//#import <MJRefresh.h>
#import "HJCarSysCore.h"
#import "UIView+XCToast.h"
#import <SVGAParser.h>
#import <SVGAImageView.h>
#import "DESEncrypt.h"
#import "HJAlertControllerCenter.h"
#import "ZJScrollPageViewDelegate.h"
#import "HJHeadwearClient.h"
#import "HJHeadwear.h"
#import "YYActionSheetViewController.h"
#import "HJSendSysViewController.h"

static NSString * const pageNumInCar = @"100";

@interface HJCarViewController ()
<
UICollectionViewDelegate,
UICollectionViewDataSource,
HJCarSysCoreClient,
HJHeadwearClient,
SVGAPlayerDelegate,
ZJScrollPageViewChildVcDelegate
>
{
    int pageNo;
}
@property (nonatomic, strong) UICollectionView *collectionView;
@property (nonatomic, strong) UICollectionViewFlowLayout *flowLayout;
@property (nonatomic, strong) NSMutableArray *itemList;
@property (strong, nonatomic) SVGAParser *parser;
@property (strong, nonatomic) SVGAImageView *svgaDisplayView;
@property (assign, nonatomic) BOOL animationLodding;
@end

@implementation HJCarViewController

#pragma mark - Getter & Setter
- (SVGAParser *)parser {
    if (_parser == nil) {
        _parser = [[SVGAParser alloc]init];
    }
    return _parser;
}

- (SVGAImageView *)svgaDisplayView {
    if (!_svgaDisplayView) {
        _svgaDisplayView = [SVGAImageView new];
        _svgaDisplayView.userInteractionEnabled = false;
    }
    return _svgaDisplayView;
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (UICollectionView *)collectionView {
    if (!_collectionView) {
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectZero collectionViewLayout:self.flowLayout];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
    }
    return _collectionView;
}

- (UICollectionViewFlowLayout *)flowLayout {
    if (!_flowLayout) {
        _flowLayout = [UICollectionViewFlowLayout new];
    }
    return _flowLayout;
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    if (self.isCarSys) {
        self.animationLodding = false;
        AddCoreClient(HJCarSysCoreClient, self);
    } else {
        AddCoreClient(HJHeadwearClient, self);
    }
    
    self.collectionView.backgroundColor = UIColorHex(f5f5f5);
    [self.view addSubview:self.collectionView];
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.top.left.right.equalTo(self.view);
    }];
    
    [self registerNib];
    
    //    @weakify(self);
    //    MJRefreshNormalHeader *header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
    //        @strongify(self);
    [self reloadView];
    
    //    }];
    //    header.lastUpdatedTimeLabel.hidden = YES;
    //    header.stateLabel.hidden = YES;
    //
    //    MJRefreshAutoNormalFooter *footer = [MJRefreshAutoNormalFooter footerWithRefreshingBlock:^{
    //        @strongify(self);
    //        if (self.isCarSys) {
    //            [GetCore(CarSysCore) getCarSysListWithPageNum:[NSString stringWithFormat:@"%d",pageNo] PageSize:pageNumInCar];
    //        } else {
    //            [GetCore(HJHeadwear) getHeadwearListWithPageNum:[NSString stringWithFormat:@"%d",pageNo] PageSize:pageNumInCar];
    //        }
    //    }];
    
    //    self.collectionView.mj_footer = footer;
    //    self.collectionView.mj_header = header;
    //
    //    [self.collectionView.mj_header beginRefreshing];
    
    if (self.isCarSys) {
        [self.view addSubview:self.svgaDisplayView];
        [self.svgaDisplayView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.bottom.top.equalTo(self.view);
        }];
        
        self.svgaDisplayView.delegate = self;
        self.svgaDisplayView.alpha = 0;
    }
}

- (void)svgaPlayerDidFinishedAnimation:(SVGAPlayer *)player {
    [UIView animateWithDuration:0.5 animations:^{
        self.svgaDisplayView.alpha = 0;
        self.animationLodding = false;
    }];
}

- (void)getHeadwearListSuccessWithArr:(NSArray *)HeadwearList {
    //    [self.collectionView.mj_header endRefreshing];
    //    [self.collectionView.mj_footer endRefreshing];
    if (HeadwearList.count != 0) {
        if (pageNo == 1) {
            self.itemList = [[NSMutableArray alloc] initWithArray:HeadwearList];
            [self.collectionView reloadData];
            if (HeadwearList.count < [pageNumInCar intValue]) {
                //                [self.collectionView.mj_footer endRefreshingWithNoMoreData];
            }
        } else {
            [self.itemList addObjectsFromArray:HeadwearList];
            [self.collectionView reloadData];
        }
        //        pageNo++;
    } else {
        //        [self.collectionView.mj_footer endRefreshingWithNoMoreData];
    }
}

- (void)getCarSysListSuccessWithArr:(NSArray *)CarSysList {
    //    [self.collectionView.mj_header endRefreshing];
    //    [self.collectionView.mj_footer endRefreshing];
    if (CarSysList.count != 0) {
        if (pageNo == 1) {
            self.itemList = [[NSMutableArray alloc] initWithArray:CarSysList];
            [self.collectionView reloadData];
            if (CarSysList.count < [pageNumInCar intValue]) {
                //                [self.collectionView.mj_footer endRefreshingWithNoMoreData];
            }
        } else {
            [self.itemList addObjectsFromArray:CarSysList];
            [self.collectionView reloadData];
        }
        //        pageNo++;
    } else {
        //        [self.collectionView.mj_footer endRefreshingWithNoMoreData];
    }
}

- (void)getHeadwearListFailWithMessage:(NSString *)message {
    //    [self.collectionView.mj_header endRefreshing];
    //    [self.collectionView.mj_footer endRefreshing];
}

- (void)getCarSysListFailWithMessage:(NSString *)message {
    //    [self.collectionView.mj_header endRefreshing];
    //    [self.collectionView.mj_footer endRefreshing];
}

- (void)registerNib {
    [self.collectionView registerNib:[UINib nibWithNibName:@"HJCarCardCell" bundle:nil] forCellWithReuseIdentifier:@"HJCarCardCell"];
}

#pragma mark ------------------------- UICollectionViewDataSource
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.itemList.count;
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {
    return UIEdgeInsetsMake(10, 10, 0, 10);
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HJCarCardCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"HJCarCardCell" forIndexPath:indexPath];
    cell.isCarSys = self.isCarSys;
    cell.dic = self.itemList[indexPath.row];
    
    @weakify(self);
    [cell setBuyBlock:^{
        @strongify(self);
        [self sendOrbuyWithIndexPath:indexPath];
    }];
    
    [cell setIsUseBlock:^{
        @strongify(self);
        NSInteger isPurse = [self.itemList[indexPath.row][@"isPurse"] intValue];
        if (isPurse == 0) {
            if (self.isCarSys) {
                [self showSVGWithindexPath:indexPath];
            }
        } else if (isPurse == 1) {
            if (self.isCarSys) {
                [GetCore(HJCarSysCore) giftCarUse:[NSString stringWithFormat:@"%@",self.itemList[indexPath.row][@"carId"]]];
            } else {
                [GetCore(HJHeadwear) giftHeadwearUse:[NSString stringWithFormat:@"%@",self.itemList[indexPath.row][@"headwearId"]]];
            }
        } else if (isPurse == 2) {
            if (self.isCarSys) {
                [GetCore(HJCarSysCore) giftCarUse:@"-1"];
            } else {
                [GetCore(HJHeadwear) giftHeadwearUse:@"-1"];
            }
        }
    }];
    
    [cell setImgBlock:^{
        @strongify(self);
        if (self.isCarSys) {
            [self showSVGWithindexPath:indexPath];
        }
    }];
    
    return cell;
}

- (void)userCarSysSuccess {
    [self reloadView];
    //    [self.collectionView.mj_header beginRefreshing];
}

- (void)userHeadwearSuccess {
    //    [self.collectionView.mj_header beginRefreshing];
    [self reloadView];
    
}

- (void)userCarSysFail:(NSString *)msg {
    [UIView showToastInKeyWindow:msg duration:1];
}

- (void)userHeadwearFail:(NSString *)msg {
    [UIView showToastInKeyWindow:msg duration:1];
}

- (void)showSVGWithindexPath:(NSIndexPath *)index {
    if (self.itemList.count == 0) {return;};
    NSString *vga = self.itemList[index.row][@"vggUrl"];
    
    vga = [DESEncrypt decryptUseDES:vga key:@"MIIBIjANBgkqhkiG9w0B"];
    if (!vga || vga.length == 0) {return;}
    BOOL hasVggPic = [self.itemList[index.row][@"hasVggPic"] boolValue];
    if (hasVggPic) {
        if (!self.animationLodding) {
            self.animationLodding = YES;
            [self.parser parseWithURL:[NSURL URLWithString:vga] completionBlock:^(SVGAVideoEntity * _Nullable videoItem) {
                if (videoItem != nil) {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        self.svgaDisplayView.contentMode = UIViewContentModeScaleAspectFit;
                        self.svgaDisplayView.alpha = 1;
                    });
                    self.svgaDisplayView.loops = 1;
                    self.svgaDisplayView.clearsAfterStop = YES;
                    self.svgaDisplayView.videoItem = videoItem;
                    [self.svgaDisplayView startAnimation];
                }
            } failureBlock:^(NSError * _Nullable error) {
                
            }];
        }
    }
}

- (void)buyIndexHeadwear:(NSIndexPath *)indexPath {
    if (self.itemList.count == 0) {return;};
    NSInteger isPurse = [self.itemList[indexPath.row][@"isPurse"] intValue];
    
    
    NSString *message;
    if (isPurse == 0) {
        message = [NSString stringWithFormat:@"%@\"%@\"",NSLocalizedString(XCCarSysSureBuyHant, nil),self.itemList[indexPath.row][@"headwearName"]];
    } else if (isPurse == 1 || isPurse == 2) {
        message = [NSString stringWithFormat:@"%@\"%@\"",NSLocalizedString(XCCarSysSureBuyHantAgain,nil),self.itemList[indexPath.row][@"headwearName"]];
    }
    
    NSString *headwearId = self.itemList[indexPath.row][@"headwearId"];
    
    UIAlertController *alertDialog = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertNiceTip,nil) message:message preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(XCAlertConfirm,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        if (isPurse == 0) {
            [GetCore(HJHeadwear) buyHeadwearWithHeadwearID:headwearId withType:@"1"];
        } else if (isPurse == 1 || isPurse == 2) {
            [GetCore(HJHeadwear) buyHeadwearWithHeadwearID:headwearId withType:@"2"];
        }
    }];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(MMPopViewCancel,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {}];
    [alertDialog addAction:cancelAction];
    [alertDialog addAction:okAction];
    [self.tabBarController presentViewController:alertDialog animated:YES completion:nil];
}

- (void)buyIndexCar:(NSIndexPath *)indexPath {
    if (self.itemList.count == 0) {return;};
    NSInteger isPurse = [self.itemList[indexPath.row][@"isPurse"] intValue];
    NSString *message;
    if (isPurse == 0) {
        message = [NSString stringWithFormat:@"%@\"%@\"",NSLocalizedString(XCCarSysSureBuyCar,nil),self.itemList[indexPath.row][@"carName"]];
    } else if (isPurse == 1 || isPurse == 2) {
        message = [NSString stringWithFormat:@"%@\"%@\"",NSLocalizedString(XCCarSysSureBuyCarAgain,nil),self.itemList[indexPath.row][@"carName"]];
    }
    
    NSString *carId = self.itemList[indexPath.row][@"carId"];
    
    UIAlertController *alertDialog = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertNiceTip,nil) message:message preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(XCAlertConfirm,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        if (isPurse == 0) {
            [GetCore(HJCarSysCore) buyCarSysWithCarId:carId withType:@"1"];
        } else if (isPurse == 1 || isPurse == 2) {
            [GetCore(HJCarSysCore) buyCarSysWithCarId:carId withType:@"2"];
        }
    }];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(MMPopViewCancel,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {}];
    [alertDialog addAction:cancelAction];
    [alertDialog addAction:okAction];
    [self.tabBarController presentViewController:alertDialog animated:YES completion:nil];
}

- (void)sendOrBuyWithIsPurse:(NSInteger)isPurse {
    if (isPurse == 0) {
        
    }
}

- (void)sendOrbuyWithIndexPath:(NSIndexPath *)indexPath {
    YYActionSheetViewController *sheet = [[YYActionSheetViewController alloc]init];
    @weakify(self);
    [sheet addButtonWithTitle:NSLocalizedString(XCCarSysBuyForMyself, nil) block:^(YYActionSheetViewController *controller) {
        @strongify(self);
        if (self.isCarSys) {
            [self buyIndexCar:indexPath];
        } else {
            [self buyIndexHeadwear:indexPath];
        }
    }];
    
    [sheet addButtonWithTitle:NSLocalizedString(XCCarSysBuyForHimself, nil) block:^(YYActionSheetViewController *controller) {
        HJSendSysViewController *vc = [[HJSendSysViewController alloc] init];
        vc.isCarSys = self.isCarSys;
        if (self.isCarSys) {
            vc.proId = [NSString stringWithFormat:@"%@",self.itemList[indexPath.row][@"carId"]];
            vc.sendName = [NSString stringWithFormat:@"%@",self.itemList[indexPath.row][@"carName"]];
        } else {
            vc.proId = [NSString stringWithFormat:@"%@",self.itemList[indexPath.row][@"headwearId"]];
            vc.sendName = [NSString stringWithFormat:@"%@",self.itemList[indexPath.row][@"headwearName"]];
        }
        [self.navigationController pushViewController:vc animated:YES];
    }];
    
    [sheet addCancelButtonWithTitle:NSLocalizedString(MMPopViewCancel, nil) block:^(YYActionSheetViewController *controller) {
        
    }];
    
    [sheet show];
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake((kScreenWidth-30)/2, 197);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)buyHeadwearSuccess:(id)data {
    [UIView showToastInKeyWindow:NSLocalizedString(XCCarSysBuySuccess, nil) duration:1];
    //    [self.collectionView.mj_header beginRefreshing];
    [self reloadView];
    
}

- (void)reloadView {
    pageNo = 1;
    if (self.isCarSys) {
        [GetCore(HJCarSysCore) getCarSysListWithPageNum:[NSString stringWithFormat:@"%d",pageNo] PageSize:pageNumInCar];
    } else {
        [GetCore(HJHeadwear) getHeadwearListWithPageNum:[NSString stringWithFormat:@"%d",pageNo] PageSize:pageNumInCar];
    }
}

- (void)buyCarSuccess:(id)data {
    [UIView showToastInKeyWindow:NSLocalizedString(XCCarSysBuySuccess, nil) duration:1];
    //    [self.collectionView.mj_header beginRefreshing];
    [self reloadView];
}

- (void)buyHeadwearFailWithCode:(NSNumber *)code Message:(NSString *)msg {
    if ([code intValue] != 2103) {
        [UIView showToastInKeyWindow:msg duration:1];
    }
}

- (void)buyFailWithCode:(NSNumber *)code Message:(NSString *)msg {
    if ([code intValue] != 2103) {
        [UIView showToastInKeyWindow:msg duration:1];
    }
}

- (void)continueFailWithCode:(NSNumber *)code WithMessage:(NSString *)msg {
    if ([code intValue] != 2103) {
        [UIView showToastInKeyWindow:msg duration:1];
    }
}

- (void)continueHeadwearFailWithCode:(NSNumber *)code WithMessage:(NSString *)msg {
    if ([code intValue] != 2103) {
        [UIView showToastInKeyWindow:msg duration:1];
    }
}

- (void)continueHeadwearSuccess:(id)data {
    [UIView showToastInKeyWindow:NSLocalizedString(XCCarSysBuyAgainSuccess, nil) duration:1];
    //    [self.collectionView.mj_header beginRefreshing];
    [self reloadView];
}

- (void)continueCarSuccess:(id)data {
    [UIView showToastInKeyWindow:NSLocalizedString(XCCarSysBuyAgainSuccess, nil) duration:1];
    //    [self.collectionView.mj_header beginRefreshing];
    [self reloadView];
}

@end
