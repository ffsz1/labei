//
//  HJMallViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMallViewController.h"
#import "HJSendSysViewController.h"
//view
#import "ZJScrollSegmentView.h"
#import "HJCarCardCell.h"
#import "HJMallHeaderView.h"
#import "HJMallBottomView.h"
//other
#import "HJUserCoreHelp.h"
#import "SVGAParser.h"
#import "SVGAImageView.h"
#import "DESEncrypt.h"

#import "HJHttpRequestHelper+CarSys.h"
#import "HJHttpRequestHelper+Headwear.h"
#import "UIImage+_1x1Color.h"
#import "HJCarSysCoreClient.h"
#import "HJHeadwearClient.h"
#import "HJCarSysCore.h"
#import "HJHeadwear.h"

#import "HJPackViewController.h"
#import "HJCountPopViewController.h"

#import "HJKeyBoardView.h"
#define getRectNavAndStatusHight  self.navigationController.navigationBar.frame.size.height+[[UIApplication sharedApplication] statusBarFrame].size.height
#define XCPropsSegementTop  (145)
#define XCPropsSegementH    (44)
#define XCPropsBottomViewH    (44)

//#define XCPropsScrollViewH  (XC_SCREE_H - XC_Height_NavBar - XCPropsSegementTop - XCPropsSegementH - (49 + (iPhoneX?64:0))) - XCPropsBottomViewH
#define XCPropsScrollViewH  (XC_SCREE_H - (55+XC_Height_NavBar)) - XCPropsBottomViewH-50

@interface HJMallViewController ()<
UICollectionViewDelegate,
UICollectionViewDataSource,
SVGAPlayerDelegate,
HJCarSysCoreClient,
HJHeadwearClient,
UIPopoverPresentationControllerDelegate>

@property (nonatomic, strong) UIImageView *bgImgView;
@property (nonatomic, strong) ZJScrollSegmentView *segmentView;
@property (nonatomic, strong) UIScrollView *bgScrollView;
@property (nonatomic, strong) UICollectionView *carCollectionView;
@property (nonatomic, strong) UICollectionView *headCollectionView;
@property (nonatomic, strong) UICollectionView *popularityCollectionView;
@property (nonatomic, strong) HJMallHeaderView *headerView;
@property (nonatomic, strong) HJMallBottomView *bottomView;

@property (nonatomic, strong) UICollectionViewFlowLayout *flowLayout;
@property (nonatomic, strong) UICollectionViewFlowLayout *flowLayout2;
@property (nonatomic, strong) UICollectionViewFlowLayout *flowLayout3;

@property (nonatomic, assign) UserID uid;
@property (nonatomic, strong) NSMutableArray *carArr;
@property (nonatomic, strong) NSMutableArray *headArr;
@property (nonatomic, strong) NSMutableArray *popularityArr;

@property (strong, nonatomic) SVGAParser *parser;
@property (strong, nonatomic) SVGAImageView *svgaDisplayView;
@property (assign, nonatomic) BOOL animationLodding;

@property (nonatomic,assign) NSInteger carSelItem;
@property (nonatomic,assign) NSInteger headSelItem;
@property (nonatomic,assign) NSInteger popularitySelItem;

@property (nonatomic,assign) NSInteger segmentIndex;
@property (nonatomic, strong) NSString *avatar;
@property (nonatomic, strong) UIButton *backBtn;
//@property (nonatomic, strong) UIImageView *avatarImgView;
//@property (nonatomic, strong) UIImageView *selectAvatarImgView;
@property (nonatomic, strong) UILabel *titleMallLabel;
@property (nonatomic, strong) UIButton *backPackBtn;

//装头饰 tab菜单
@property (nonatomic, strong) UIImageView *topView;
@property (nonatomic, strong) UIImageView *topViewImg;
@property (nonatomic, strong) UIView *topViewLine;
@property (nonatomic, strong) UIView *topViewLine2;

//毛玻璃
@property (nonatomic, strong) UIVisualEffectView *effectView;
@end

@implementation HJMallViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self initView];
    [self setupLayout];
    [self addCore];
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    if (@available(iOS 11.0, *)) {
//        self.scrollView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
    } else {
        self.automaticallyAdjustsScrollViewInsets = NO;
    }
}
- (UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent; //返回白色
//    return UIStatusBarStyleDefault;    //返回黑色
}
- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
}

- (void)dealloc {
    [self.svgaDisplayView removeFromSuperview];
    RemoveCoreClientAll(self);
}

- (void)addCore {
    AddCoreClient(HJCarSysCoreClient, self);
    AddCoreClient(HJHeadwearClient, self);
}

- (void)initView {
    self.carSelItem = -1;
    self.headSelItem = -1;
    
    self.title = @"商城";
    self.view.backgroundColor = [UIColor whiteColor];
    [self.carCollectionView registerNib:[UINib nibWithNibName:@"HJCarCardCell" bundle:nil] forCellWithReuseIdentifier:@"HJCarCardCell"];
    [self.headCollectionView registerNib:[UINib nibWithNibName:@"HJCarCardCell" bundle:nil] forCellWithReuseIdentifier:@"HJCarCardCell"];
    [self.popularityCollectionView registerNib:[UINib nibWithNibName:@"HJCarCardCell" bundle:nil] forCellWithReuseIdentifier:@"HJCarCardCell"];

    [self.view addSubview:self.bgImgView];
    [self.bgImgView addSubview:self.effectView];
    [self.view addSubview:self.topViewImg];
    
    [self.topViewImg addSubview:self.backBtn];
     [self.topViewImg addSubview:self.titleMallLabel];
     [self.topViewImg addSubview:self.backPackBtn];
     
    
    [self.view addSubview:self.topView];
    [self.topView addSubview:self.topViewLine];
    [self.topView addSubview:self.topViewLine2];
//    [self.topView addSubview:self.headerView];
//    [self.topView addSubview:self.avatarImgView];
//    [self.topView addSubview:self.selectAvatarImgView];
    
    
    [self.view addSubview:self.bottomView];
    [self.topView addSubview:self.segmentView];
    [self.view addSubview:self.bgScrollView];
    [self.bgScrollView addSubview:self.carCollectionView];
    [self.bgScrollView addSubview:self.headCollectionView];
//    [self.bgScrollView addSubview:self.popularityCollectionView];
    
    [[UIApplication sharedApplication].keyWindow addSubview:self.svgaDisplayView];
    [self.svgaDisplayView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.bottom.top.equalTo([UIApplication sharedApplication].keyWindow);
    }];
    
    self.svgaDisplayView.delegate = self;
    self.svgaDisplayView.alpha = 0;
    [self loadData];
}

- (void)loadData {
    
    [MBProgressHUD showMessage:@"请稍后"];
    if (self.sendToUid>0 && self.sendToUserName.length) {
        //他人
        self.uid = self.sendToUid;
        if (GetCore(HJAuthCoreHelp).isLogin) {
            [GetCore(HJUserCoreHelp) getUserInfo:self.uid refresh:YES success:^(UserInfo *info) {
                self.avatar= info.avatar;
//                [self.avatarImgView sd_setImageWithURL:[NSURL URLWithString:info.avatar] placeholderImage:[UIImage imageNamed:@"default_avatar"]];
                [self.headerView setupAvatar:info.avatar headWear:info.headwearUrl];
                [MBProgressHUD hideHUD];
            }];
        }
    } else {
        //自己
        if (GetCore(HJAuthCoreHelp).isLogin) {
            self.uid = [GetCore(HJAuthCoreHelp).getUid userIDValue];
            [GetCore(HJUserCoreHelp) getUserInfo:self.uid refresh:YES success:^(UserInfo *info) {
                 self.avatar= info.avatar;
//                  [self.avatarImgView sd_setImageWithURL:[NSURL URLWithString:info.avatar] placeholderImage:[UIImage imageNamed:@"default_avatar"]];
                [self.headerView setupAvatar:info.avatar headWear:info.headwearUrl];
                [MBProgressHUD hideHUD];
            }];
        }
    }
    [self getCarData];
    [self getHeadData:@"1000"];
    [self getPopularityData];
}

- (void)setupLayout{
    
    [self.backBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.view).offset(iPhoneX?38: 28);
        make.left.equalTo(self.view).offset(0);
        make.width.height.equalTo(@40);
       }];
    
    [self.titleMallLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.view).offset(iPhoneX?44: 34);
            make.centerX.equalTo(self.view);
           make.width.height.equalTo(@80);
         make.height.equalTo(@30);
          }];
    
    [self.backPackBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.view).offset(iPhoneX?50: 30);
              make.right.equalTo(self.view).offset(-15);
             make.width.height.equalTo(@50);
           make.height.equalTo(@24);
            }];
//    self.backPackBtn.hidden = YES;
//  [self.avatarImgView mas_makeConstraints:^(MASConstraintMaker *make) {
//                 make.top.equalTo(self.view).offset(160);
//                make.right.equalTo(self.view).offset(-30);
//              make.width.height.equalTo(@72);
//            make.height.equalTo(@72);
//             }];
//    [self.selectAvatarImgView mas_makeConstraints:^(MASConstraintMaker *make) {
//                   make.top.equalTo(self.view).offset(160-3);
//                  make.right.equalTo(self.view).offset(-30+3);
//                make.width.height.equalTo(@78);
//              make.height.equalTo(@78);
//               }];
//    self.avatarImgView.hidden = YES;
//    self.selectAvatarImgView.hidden = YES;
    
    [self.bgImgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.view).offset(-15);
        make.left.right.equalTo(self.view);
        make.height.equalTo(@210);
    }];
    
    [self.effectView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.bgImgView);
    }];
    
    
    
    [self.topViewImg mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.view).offset(0);
        make.left.equalTo(@0);
        make.right.equalTo(@0);
        make.height.mas_equalTo(55+XC_Height_NavBar);//210
          self.topViewImg.userInteractionEnabled = YES;
        
    }];
    [self.topView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.topViewImg.mas_bottom).offset(-25);
        make.left.equalTo(@0);
        make.right.equalTo(@0);
        make.height.equalTo(@52);
       
    }];
    [self.topViewLine mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.topView).offset(120);
        make.left.right.equalTo(self.topView);
        make.height.equalTo(@1);
    }];
    
    [self.topViewLine2 mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(self.topView).offset(-15);
        make.height.equalTo(@22);
        make.width.equalTo(@1);
        make.centerX.equalTo(self.topView);
    }];
    self.topViewLine2.hidden = YES;
    
    [self.segmentView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.mas_equalTo(48);
        make.bottom.equalTo(self.topView);
        make.left.right.equalTo(self.topView);
    }];
    
//    [self.headerView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.top.mas_equalTo(self.topView).offset(10);
//        make.left.right.equalTo(self.topView);
//        make.bottom.equalTo(self.topViewLine.mas_top);
//    }];
    
    [self.bottomView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.mas_equalTo(XCPropsBottomViewH);
        make.left.right.equalTo(self.view);
        if (@available(iOS 11.0, *)) {
            make.bottom.equalTo(self.view.mas_safeAreaLayoutGuideBottom);
        } else {
            make.bottom.equalTo(self.view);
        }
    }];
    
    [self.bgScrollView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.segmentView.mas_bottom);
        make.width.mas_equalTo(XC_SCREE_W);
        make.left.right.equalTo(self.view);
//        make.bottom.equalTo(self.bottomView.mas_top);
        make.height.mas_equalTo(XCPropsScrollViewH-(iPhoneX?30:0));
    }];
}

- (void)svgaPlayerDidFinishedAnimation:(SVGAPlayer *)player {
    [UIView animateWithDuration:0.5 animations:^{
        self.svgaDisplayView.alpha = 0;
        self.animationLodding = false;
    }];
}

#pragma mark - <CarSysCoreClient>
- (void)sendCarSysSuccess {
    [MBProgressHUD showSuccess:@"赠送成功"];
}

- (void)sendCarSysFail:(NSString *)msg {
    [MBProgressHUD showError:msg];
}

- (void)sendHeadwearSuccess {
    [MBProgressHUD showSuccess:@"赠送成功"];
}

- (void)sendHeadwearFail:(NSString *)msg {
    [MBProgressHUD showError:msg];
}

#pragma mark - <UICollectionViewDelegate,UICollectionViewDataSource>
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    if (collectionView == self.carCollectionView) {
        return self.carArr.count;
    }else if (collectionView == self.headCollectionView) {
        return self.headArr.count;
    }
    
    
    return self.popularityArr.count;
}



- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    HJCarCardCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"HJCarCardCell" forIndexPath:indexPath];
    
    BOOL isSel = NO;
    NSInteger isPurse = 0;
    
    if (collectionView == self.carCollectionView) {//座驾
        cell.isCarSys = YES;
        cell.dic = self.carArr[indexPath.row];
        isSel = (self.carSelItem == indexPath.item);
        
        isPurse = [self.carArr[indexPath.row][@"isPurse"] intValue];
        
        @weakify(self);
        [cell setPlayBlock:^{
            @strongify(self);
            [self showSVGWithindexPath:indexPath];
            self.carSelItem = indexPath.item;
            [self.carCollectionView reloadData];
            [self updateBottomViewData];
        }];
    }else if (collectionView == self.headCollectionView) {//头饰
        cell.isCarSys = NO;
        cell.dic = self.headArr[indexPath.row];
        isSel = (self.headSelItem == indexPath.item);
        
        isPurse = [self.headArr[indexPath.row][@"isPurse"] intValue];
    } else { //人气票
        cell.isCarSys = NO;
        cell.dic = self.popularityArr[indexPath.row];
        isSel = (self.popularitySelItem == indexPath.item);
        
        isPurse = [self.popularityArr[indexPath.row][@"isPurse"] intValue];
    }
    
    cell.bgImageView.image = [UIImage imageNamed:isSel?@"hj_prop_icon_yixuanzhon":@"hj_prop_icon_weixuanzhon"];
    cell.isSel = isSel;
    
    @weakify(self);
    [cell setIsUseBlock:^{
        @strongify(self);
        //            NSInteger isPurse = [self.carArr[indexPath.row][@"isPurse"] intValue];
        
        
        
        if (collectionView == self.carCollectionView) {
            if (isPurse == 0) {
                
                [self showSVGWithindexPath:indexPath];
                
            } else if (isPurse == 1) {
                
                [self useCarPost:self.carArr[indexPath.row][@"carId"]];
                
            } else if (isPurse == 2) {
                [self useCarPost:@"-1"];
                
            }
            
        }else if (collectionView == self.headCollectionView) {
            NSString *url = self.headArr[indexPath.row][@"picUrl"];
            if (isPurse == 0) {
                [self.headerView setupheadWear:url];
                //            [.headWearImageView sd_setImageWithURL:[NSURL URLWithString:url]];
                
            } else if (isPurse == 1) {
                [self.headerView setupheadWear:url];
                //            [self.headerView.headWearImageView sd_setImageWithURL:[NSURL URLWithString:url]];
                
                NSDictionary *dict = self.headArr[indexPath.row];
                [self useHeadPost:dict[@"headwearId"]];
                
            } else if (isPurse == 2) {
                
                [self useHeadPost:@"-1"];
                
            }
        } else {
            
            NSString *url = self.popularityArr[indexPath.row][@"picUrl"];
            if (isPurse == 0) {
                [self.headerView setupheadWear:url];
                //            [.headWearImageView sd_setImageWithURL:[NSURL URLWithString:url]];
                
            } else if (isPurse == 1) {
                [self.headerView setupheadWear:url];
                //            [self.headerView.headWearImageView sd_setImageWithURL:[NSURL URLWithString:url]];
                
                NSDictionary *dict = self.popularityArr[indexPath.row];
                [self useHeadPost:dict[@"headwearId"]];
                
            } else if (isPurse == 2) {
                
                [self useHeadPost:@"-1"];
                
            }
        }
        
    }];
    
    
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    //点击座驾cell
    if (collectionView == self.carCollectionView) {
        
//        [self showSVGWithindexPath:indexPath];
        self.carSelItem = indexPath.item;
        [self.carCollectionView reloadData];
        [self updateBottomViewData];
        return;
    } else if (collectionView == self.headCollectionView){
        //点击头饰cell
//        NSString *url = self.headArr[indexPath.row][@"picUrl"];
//        if (url == nil) {
//            [self.headerView setupheadWear:@""];
//        }else{
//            [self.headerView setupheadWear:url];
//            [self  setupSelectHeadWear:url];
//        }
        
        //选中
        self.headSelItem = indexPath.item;
        [self.headCollectionView reloadData];
        [self updateBottomViewData];
    }else {
        //点击人气票cell
        NSString *url = self.popularityArr[indexPath.row][@"picUrl"];
        if (url == nil) {
            [self.headerView setupheadWear:@""];
        }else{
            //            [self.headerView setupheadWear:url];
//            [self  setupSelectHeadWear:url];
        }
        
        //选中
        self.popularitySelItem = indexPath.item;
        [self.popularityCollectionView reloadData];
        [self updateBottomViewData];
    }
}

//- (void)setupSelectHeadWear:(NSString *)headWear {
//    if (headWear.length) {
//        self.selectAvatarImgView.hidden = NO;
//        [self.selectAvatarImgView qn_setImageImageWithUrl:headWear placeholderImage:nil type:ImageTypeUserIcon];
//    } else {
//        self.selectAvatarImgView.hidden = YES;
//    }
//}



- (void)updateBottomViewData
{
    //座驾价格
    if (self.segmentIndex == 0) {
        self.bottomView.isPopularitySel = NO;
        if (self.carSelItem != -1) {
            NSDictionary *dic = self.carArr[self.carSelItem];
            NSInteger isPurse = [dic[@"isPurse"] integerValue];
            
            if (isPurse) {
                [self.bottomView setupBuyButtonText:@"续费"];
            } else {
                [self.bottomView setupBuyButtonText:@"购买"];
            }
            [self.bottomView setupGold:dic[@"goldPrice"] day:dic[@"effectiveTime"]];
//            self.priceLabel.text = [NSString stringWithFormat:@"%@/%@天", dic[@"goldPrice"],dic[@"effectiveTime"]];
//            [self.buyBtn setImage:[UIImage imageNamed:isPurse?@"mall_buy2":@"mall_buy"] forState:UIControlStateNormal];
        }else{
            [self.bottomView setupBuyButtonText:@"购买"];
            [self.bottomView setupGold:@"--" day:@"--"];
        }
        
    }
    
    //头饰
    if (self.segmentIndex == 1) {
        self.bottomView.isPopularitySel = NO;
        if (self.headSelItem != -1) {
            NSDictionary *dic = self.headArr[self.headSelItem];
            NSInteger isPurse = [dic[@"isPurse"] integerValue];
            
            if (isPurse) {
                [self.bottomView setupBuyButtonText:@"续费"];
            } else {
                [self.bottomView setupBuyButtonText:@"购买"];
            }
            [self.bottomView setupGold:dic[@"goldPrice"] day:dic[@"effectiveTime"]];
        }else{
            [self.bottomView setupBuyButtonText:@"购买"];
            [self.bottomView setupGold:@"--" day:@"--"];
        }
    }
    
    //人气票
    if (self.segmentIndex == 2) {
        if (self.popularitySelItem != -1) {
            NSDictionary *dic = self.popularityArr[self.popularitySelItem];
            NSInteger isPurse = [dic[@"isPurse"] integerValue];
            
            if (isPurse) {
                [self.bottomView setupBuyButtonText:@"续费"];
            } else {
                [self.bottomView setupBuyButtonText:@"购买"];
            }
            [self.bottomView setupGold:dic[@"goldPrice"] day:dic[@"effectiveTime"]];
        }else{
            [self.bottomView setupBuyButtonText:@"购买"];
            [self.bottomView setupGold:@"--" day:@"--"];
        }
        
        self.bottomView.isPopularitySel = YES;
    }
    
}

//展示vgg动画
- (void)showSVGWithindexPath:(NSIndexPath *)index {
    if (self.carArr.count == 0) {return;};
    NSString *vga = self.carArr[index.row][@"vggUrl"];
    
    vga = [DESEncrypt decryptUseDES:vga key:@"MIIBIjANBgkqhkiG9w0B"];
    if (!vga || vga.length == 0) {return;}
    //    BOOL hasVggPic = [self.itemList[index.row][@"hasVggPic"] boolValue];
    //    if (hasVggPic) {
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




- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    //过滤switchBar点击的回调
    if (!scrollView.isDecelerating && !scrollView.tracking) {
        return;
    }
    if (scrollView == self.bgScrollView) {
        
        CGFloat offsetX = scrollView.contentOffset.x;
        CGFloat index = offsetX / XC_SCREE_W;
        
        if (index == ceilf(index)) {
            [self.segmentView setSelectedIndex:index animated:YES];
        }
        
    }
}

#pragma mark - action
-(void)backPackBtnAction{
    HJPackViewController *pack = [[HJPackViewController alloc] init];
    [self.navigationController pushViewController:pack animated:YES];
}
-(void)backBtnAction{
     [self.navigationController popViewControllerAnimated:YES];
}
//赠送
- (void)giftBtnAction {
    
    if (self.sendToUid>0 && self.sendToUserName.length) {
        if (self.carSelItem != -1 || self.headSelItem != -1) {
            
            NSString *message;
            if (self.segmentIndex == 0) {
                NSDictionary *dic = self.carArr[self.carSelItem];
                message = [NSString stringWithFormat:@"%@%@%@%@",NSLocalizedString(XCCarSysSureBuyCar, nil),dic[@"carName"],NSLocalizedString(XCCarSysSendToSomeone, nil),self.sendToUserName];
            } else if (self.segmentIndex == 1){
                NSDictionary *dic = self.headArr[self.headSelItem];
                message = [NSString stringWithFormat:@"%@%@%@%@",NSLocalizedString(XCCarSysSureBuyHant, nil),dic[@"headwearName"],NSLocalizedString(XCCarSysSendToSomeone, nil),self.sendToUserName];
            }else {
                NSDictionary *dic = self.popularityArr[self.popularitySelItem];
                message = [NSString stringWithFormat:@"%@%@%@%@",NSLocalizedString(XCCarSysSureBuyHant, nil),dic[@"headwearName"],NSLocalizedString(XCCarSysSendToSomeone, nil),self.sendToUserName];
            }
            
            UIAlertController *alertDialog = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertNiceTip,nil) message:message preferredStyle:UIAlertControllerStyleAlert];
            
            @weakify(self);
            UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(XCAlertConfirm,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                @strongify(self);
                if (self.segmentIndex == 0) {
                    if (self.carSelItem != -1) {
                        NSDictionary *dic = self.carArr[self.carSelItem];
                        [GetCore(HJCarSysCore) sendCarWithCarId:dic[@"carId"] targetUid:[NSString stringWithFormat:@"%lld",self.sendToUid]];
                    }
                } else if (self.segmentIndex == 1) {
                    if (self.headSelItem != -1) {
                        NSDictionary *dic = self.headArr[self.headSelItem];
                        [GetCore(HJHeadwear) sendHeadwearWithHeadwearId:dic[@"headwearId"] targetUid:[NSString stringWithFormat:@"%lld",self.sendToUid]];
                        
                    }
                } else {
                    if (self.headSelItem != -1) {
                        NSDictionary *dic = self.popularityArr[self.headSelItem];
                        [GetCore(HJHeadwear) sendHeadwearWithHeadwearId:dic[@"headwearId"] targetUid:[NSString stringWithFormat:@"%lld",self.sendToUid]];
                        
                    }
                }
            }];
            
            UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(MMPopViewCancel,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {}];
            [alertDialog addAction:cancelAction];
            [alertDialog addAction:okAction];
            UIViewController *presentingViewController = nil;
            presentingViewController = self.navigationController;
            if (presentingViewController) {
                [presentingViewController presentViewController:alertDialog animated:YES completion:nil];
            }
        }
        
    } else {
        //座驾价格
        if (self.segmentIndex == 0) {
            
            if (self.carSelItem != -1) {
                NSDictionary *dic = self.carArr[self.carSelItem];
                HJSendSysViewController *vc = [[HJSendSysViewController alloc] init];
                vc.isCarSys = YES;
                vc.proId = [NSString stringWithFormat:@"%@",dic[@"carId"]];
                vc.sendName = [NSString stringWithFormat:@"%@",dic[@"carName"]];
                [self.navigationController pushViewController:vc animated:YES];
                
            }else{
                [MBProgressHUD showError:@"请选择赠送的座驾"];
            }
        }
        
        //头饰
        if (self.segmentIndex == 1) {
            if (self.headSelItem != -1) {
                NSDictionary *dic = self.headArr[self.headSelItem];
                HJSendSysViewController *vc = [[HJSendSysViewController alloc] init];
                vc.isCarSys = NO;
                vc.proId = [NSString stringWithFormat:@"%@",dic[@"headwearId"]];
                vc.sendName = [NSString stringWithFormat:@"%@",dic[@"headwearName"]];
                [self.navigationController pushViewController:vc animated:YES];
                
            }else{
                [MBProgressHUD showError:@"请选择赠送的头饰"];
            }
        }
        
        //人气票
        if (self.segmentIndex == 2) {
            if (self.popularitySelItem != -1) {
                NSDictionary *dic = self.popularityArr[self.popularitySelItem];
                HJSendSysViewController *vc = [[HJSendSysViewController alloc] init];
                vc.isCarSys = NO;
                vc.proId = [NSString stringWithFormat:@"%@",dic[@"headwearId"]];
                vc.sendName = [NSString stringWithFormat:@"%@",dic[@"headwearName"]];
                [self.navigationController pushViewController:vc animated:YES];
                
            }else{
                [MBProgressHUD showError:@"请选择赠送的头饰"];
            }
        }
    }
}

//购买
- (void)buyBtnAction {
    
    //座驾价格
    if (self.segmentIndex == 0) {
        
        if (self.carSelItem != -1) {
            NSDictionary *dic = self.carArr[self.carSelItem];
            NSInteger isPurse = [dic[@"isPurse"] integerValue];
            
            NSString *message;
            if (isPurse == 0) {
                message = [NSString stringWithFormat:@"%@\"%@\"",NSLocalizedString(XCCarSysSureBuyCar,nil),dic[@"carName"]];
            } else if (isPurse == 1 || isPurse == 2) {
                message = [NSString stringWithFormat:@"%@\"%@\"",NSLocalizedString(XCCarSysSureBuyCarAgain,nil),dic[@"carName"]];
            }
            
            NSString *carId = dic[@"carId"];
            
            UIAlertController *alertDialog = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertNiceTip,nil) message:message preferredStyle:UIAlertControllerStyleAlert];
            
            UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(XCAlertConfirm,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                if (isPurse == 0) {
                    [self buyCarPost:@"1" carId:carId];
                    
                } else if (isPurse == 1 || isPurse == 2) {
                    [self buyCarPost:@"2" carId:carId];
                }
            }];
            
            UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(MMPopViewCancel,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {}];
            [alertDialog addAction:cancelAction];
            [alertDialog addAction:okAction];
            [self.tabBarController presentViewController:alertDialog animated:YES completion:nil];
        }else{
            [MBProgressHUD showError:@"请选择购买的座驾"];
        }
        
    }
    
    //头饰
    if (self.segmentIndex == 1) {
        if (self.headSelItem != -1) {
            NSDictionary *dic = self.headArr[self.headSelItem];
            NSInteger isPurse = [dic[@"isPurse"] integerValue];
            
            NSString *message;
            if (isPurse == 0) {
                message = [NSString stringWithFormat:@"%@\"%@\"",NSLocalizedString(XCCarSysSureBuyHant, nil),dic[@"headwearName"]];
            } else if (isPurse == 1 || isPurse == 2) {
                message = [NSString stringWithFormat:@"%@\"%@\"",NSLocalizedString(XCCarSysSureBuyHantAgain,nil),dic[@"headwearName"]];
            }
            
            NSString *headwearId = dic[@"headwearId"];
            
            UIAlertController *alertDialog = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertNiceTip,nil) message:message preferredStyle:UIAlertControllerStyleAlert];
            
            UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(XCAlertConfirm,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                if (isPurse == 0) {
                    [self buyHeadPost:@"1" headwearID:headwearId];
                } else if (isPurse == 1 || isPurse == 2) {
                    [self buyHeadPost:@"2" headwearID:headwearId];
                }
            }];
            
            UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(MMPopViewCancel,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {}];
            [alertDialog addAction:cancelAction];
            [alertDialog addAction:okAction];
            [self.tabBarController presentViewController:alertDialog animated:YES completion:nil];
        }else{
            [MBProgressHUD showError:@"请选择购买的头饰"];

        }
    }
    
    //人气票
    if (self.segmentIndex == 2) {
        if (self.popularitySelItem != -1) {
            NSDictionary *dic = self.popularityArr[self.popularitySelItem];
            NSInteger isPurse = [dic[@"isPurse"] integerValue];
            
            NSString *message;
            if (isPurse == 0) {
                message = [NSString stringWithFormat:@"%@\"%@\"",NSLocalizedString(XCCarSysSureBuyHant, nil),dic[@"headwearName"]];
            } else if (isPurse == 1 || isPurse == 2) {
                message = [NSString stringWithFormat:@"%@\"%@\"",NSLocalizedString(XCCarSysSureBuyHantAgain,nil),dic[@"headwearName"]];
            }
            
            NSString *headwearId = dic[@"headwearId"];
            
            UIAlertController *alertDialog = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertNiceTip,nil) message:message preferredStyle:UIAlertControllerStyleAlert];
            
            UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(XCAlertConfirm,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                if (isPurse == 0) {
                    [self buyHeadPost:@"1" headwearID:headwearId];
                } else if (isPurse == 1 || isPurse == 2) {
                    [self buyHeadPost:@"2" headwearID:headwearId];
                }
            }];
            
            UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(MMPopViewCancel,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {}];
            [alertDialog addAction:cancelAction];
            [alertDialog addAction:okAction];
            [self.tabBarController presentViewController:alertDialog animated:YES completion:nil];
        }else{
            [MBProgressHUD showError:@"请选择购买的头饰"];

        }
    }
    
}
#pragma mark - request
//使用座驾
- (void)useCarPost:(NSString *)carId
{
    [MBProgressHUD showMessage:@"请稍候..."];
    [HJHttpRequestHelper getCarSysUseWithCarId:carId Success:^(id json) {
        [MBProgressHUD hideHUD];
        [self getCarData];
    } failure:^(NSNumber *code, NSString *msg) {
        [MBProgressHUD hideHUD];
        
        [self getCarData];
    }];
}

//使用头饰
- (void)useHeadPost:(NSString *)headwearID
{
    [MBProgressHUD showMessage:@"请稍候..."];
    [HJHttpRequestHelper getHeadwearUseWithHeadwearId:headwearID Success:^(id json) {
        [MBProgressHUD hideHUD];
        [self getHeadData:[NSString stringWithFormat:@"%@",headwearID]];
    } failure:^(NSNumber *code, NSString *msg) {
        [MBProgressHUD hideHUD];
        [self getHeadData:[NSString stringWithFormat:@"%@",headwearID]];
    }];
}

//购买座驾
- (void)buyCarPost:(NSString *)type carId:(NSString *)carId
{
    [MBProgressHUD showMessage:@"购买中..."];
    [HJHttpRequestHelper getCarSysPurseWithType:type CarId:carId Success:^(id data) {
        [MBProgressHUD showSuccess:NSLocalizedString(XCCarSysBuySuccess, nil)];
        [self getCarData];
//        [self.buyBtn setImage:[UIImage imageNamed:@"mall_buy2"] forState:UIControlStateNormal];
        [self.bottomView setupBuyButtonText:@"续费"];
    } failure:^(NSNumber *code, NSString *msg) {
        [MBProgressHUD hideHUD];
    }];
}

//购买头饰
- (void)buyHeadPost:(NSString *)type headwearID:(NSString *)headwearID
{
    [MBProgressHUD showMessage:@"购买中..."];
    [HJHttpRequestHelper getHeadwearPurseWithType:type CarId:headwearID Success:^(id data) {
        [MBProgressHUD showSuccess:NSLocalizedString(XCCarSysBuySuccess, nil)];
        [self getHeadData:[NSString stringWithFormat:@"%@",headwearID]];
        [self.bottomView setupBuyButtonText:@"续费"];
//        [self.buyBtn setImage:[UIImage imageNamed:@"mall_buy2"] forState:UIControlStateNormal];
    } failure:^(NSNumber *code, NSString *msg) {
        [MBProgressHUD hideHUD];
    }];
}

//获取座驾列表
- (void)getCarData
{
    if (self.uid == [GetCore(HJAuthCoreHelp).getUid userIDValue]) {
        [HJHttpRequestHelper getMyCarSysListWithPageNum:@"1" withPageSize:@"100" userId:[NSString stringWithFormat:@"%lld",self.uid]  success:^(NSArray *list) {
            self.carArr = [NSMutableArray arrayWithArray:list];
            [self.carCollectionView reloadData];
        } failure:^(NSNumber *regCode, NSString *msg) {
            
        }];
    } else {
        [HJHttpRequestHelper getCarSysListWithPageNum:@"1" withPageSize:@"100" userId:[NSString stringWithFormat:@"%lld",self.uid]  success:^(NSArray *list) {
            self.carArr = [NSMutableArray arrayWithArray:list];
            [self.carCollectionView reloadData];
        } failure:^(NSNumber *regCode, NSString *msg) {
            
        }];
    }
}

//获取头饰列表
- (void)getHeadData:(NSString *)headwearID
{
//    if (self.uid == [GetCore(HJAuthCoreHelp).getUid userIDValue]) {
//        [HJHttpRequestHelper getMyHeadwearListWithPageNum:@"1" withPageSize:@"100" userId:[NSString stringWithFormat:@"%lld",self.uid] success:^(NSArray *list) {
//            self.headArr = [NSMutableArray arrayWithArray:list];
//            [self.headCollectionView reloadData];
//            if ([headwearID isEqualToString:@"-1"]) {
//                self.selectAvatarImgView.hidden = YES;
//            }
//            else {
//                for (NSDictionary *dict in self.headArr ) {
//                    if ([[dict objectForKey:@"isPurse"] isEqual:@2]) {
//                        self.selectAvatarImgView.hidden = NO;
//                        [self.selectAvatarImgView qn_setImageImageWithUrl:[dict objectForKey:@"picUrl"] placeholderImage:nil type:ImageTypeUserIcon];
//                    } else {
//                        //                    self.selectAvatarImgView.hidden = YES;
//                    }
//                }
//            }
//
//        } failure:^(NSNumber *regCode, NSString *msg) {
//
//        }];
//    } else {
        [HJHttpRequestHelper getHeadwearListWithPageNum:@"1" withPageSize:@"100" userId:[NSString stringWithFormat:@"%lld",self.uid] success:^(NSArray *list) {
            self.headArr = [NSMutableArray arrayWithArray:list];
            [self.headCollectionView reloadData];
        } failure:^(NSNumber *regCode, NSString *msg) {
            
        }];
//    }
}

//获取头饰列表
- (void)getPopularityData
{
    if (self.uid == [GetCore(HJAuthCoreHelp).getUid userIDValue]) {
        [HJHttpRequestHelper getMyHeadwearListWithPageNum:@"1" withPageSize:@"100" userId:[NSString stringWithFormat:@"%lld",self.uid] success:^(NSArray *list) {
            self.popularityArr = [NSMutableArray arrayWithArray:list];
            [self.popularityCollectionView reloadData];
        } failure:^(NSNumber *regCode, NSString *msg) {
            
        }];
    } else {
        [HJHttpRequestHelper getHeadwearListWithPageNum:@"1" withPageSize:@"100" userId:[NSString stringWithFormat:@"%lld",self.uid] success:^(NSArray *list) {
            self.popularityArr = [NSMutableArray arrayWithArray:list];
            [self.popularityCollectionView reloadData];
        } failure:^(NSNumber *regCode, NSString *msg) {
            
        }];
    }
}

- (void)showPopView {
    self.bottomView.sendBtn.selected = YES;
    HJCountPopViewController *contentVC = [[HJCountPopViewController alloc] init];
    
    contentVC.modalPresentationStyle = UIModalPresentationPopover;
    contentVC.popoverPresentationController.sourceView = self.bottomView.sendBtn;
    contentVC.popoverPresentationController.sourceRect = CGRectMake(0, 0, self.bottomView.sendBtn.bounds.size.width / 2.0, self.bottomView.sendBtn.bounds.size.height / 2.0);
    contentVC.popoverPresentationController.permittedArrowDirections = UIPopoverArrowDirectionDown;
    // 设置代理
    contentVC.popoverPresentationController.delegate = self;
    @weakify(contentVC);
    [contentVC setSelectedCountAction:^(NSInteger index, NSString * _Nonnull count) {
        @strongify(contentVC);
        if (index == 0) {
            //弹出键盘
            [HJKeyBoardView showKeyboardWithSureBtnImgStr:@"hj_prop_icon_goumai" sureBtnTitle:@"确定" sureActionHandler:^(NSInteger count) {
                [self.bottomView.sendBtn setTitle:[NSString stringWithFormat:@"%zd",count] forState:UIControlStateNormal];
            }];
        }else {
            [self.bottomView.sendBtn setTitle:[NSString stringWithFormat:@"%zd",count.integerValue] forState:UIControlStateNormal];
        }
        self.bottomView.sendBtn.selected = NO;
        [contentVC dismissViewControllerAnimated:YES completion:nil];
    }];
    
    [self presentViewController:contentVC animated:YES completion:nil];
}

//UIPopoverPresentationControllerDelegate
-(UIModalPresentationStyle)adaptivePresentationStyleForPresentationController:(UIPresentationController *)controller{
    return UIModalPresentationNone; //不适配
}

 - (BOOL)popoverPresentationControllerShouldDismissPopover:(UIPopoverPresentationController *)popoverPresentationController{
    self.bottomView.sendBtn.selected = NO;
    return YES;   //点击蒙版popover消失， 默认YES
}

#pragma  mark - setter/getter
- (ZJScrollSegmentView *)segmentView {
    if (!_segmentView) {
        __weak typeof(self) weakSelf = self;
        
        ZJSegmentStyle *style = [[ZJSegmentStyle alloc] init];
        style.showLine = YES;
        style.gradualChangeTitleColor = YES;
        style.normalTitleColor = UIColorHex(CCCCCC);
        style.selectedTitleColor = UIColorHex(333333);
        style.titleFont = [UIFont fontWithName:@"PingFang-SC-Medium" size:18];
        style.segmentViewComponent = SegmentViewComponentAdjustCoverOrLineWidth;
        style.scrollTitle = false;
        style.scrollLineColor = UIColorHex(7A9DFF);
        style.titleMargin = 8;
        style.scrollLineHeight = 3;
        
        
        ZJScrollSegmentView *segment = [[ZJScrollSegmentView alloc] initWithFrame:CGRectMake(0, 190, 260, XCPropsSegementH) segmentStyle:style delegate:nil titles:@[@"座驾",@"头饰"] titleDidClick:^(ZJTitleView *titleView, NSInteger index) {//人气票
            
            [weakSelf.bgScrollView setContentOffset:CGPointMake(XC_SCREE_W * index, 0.0) animated:YES];
            weakSelf.segmentIndex = index;
            [weakSelf updateBottomViewData];
            
        }];
//        UIView *line = [[UIView alloc] init];
//        line.backgroundColor = UIColorHex(EEEEEE);
//        [segment addSubview:line];
//        [line mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.left.bottom.right.equalTo(segment);
//            make.height.mas_equalTo(0.5);
//        }];
        _segmentView = segment;
    }
    return _segmentView;
}

- (UIScrollView *)bgScrollView {
    if (!_bgScrollView) {
        _bgScrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, XCPropsSegementTop, XC_SCREE_W*2, XCPropsScrollViewH-(iPhoneX?30:0))];
        _bgScrollView.showsVerticalScrollIndicator = NO;
        _bgScrollView.showsHorizontalScrollIndicator = NO;
        _bgScrollView.backgroundColor = [UIColor whiteColor];
        _bgScrollView.contentSize = CGSizeMake(XC_SCREE_W * 2, XCPropsScrollViewH-(iPhoneX?30:0));//3
        _bgScrollView.delegate = self;
        _bgScrollView.userInteractionEnabled = YES;
        _bgScrollView.bounces = NO;
        _bgScrollView.pagingEnabled = YES;
    }
    return _bgScrollView;
}

- (UICollectionView *)carCollectionView {
    if (!_carCollectionView) {
        _carCollectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, XCPropsScrollViewH-(iPhoneX?30:0)) collectionViewLayout:self.flowLayout];
        _carCollectionView.delegate = self;
        _carCollectionView.dataSource = self;
        _carCollectionView.userInteractionEnabled = YES;
        _carCollectionView.backgroundColor = [UIColor whiteColor];
        _carCollectionView.showsVerticalScrollIndicator = NO;
        _carCollectionView.showsHorizontalScrollIndicator = NO;

    }
    return _carCollectionView;
}

- (UICollectionView *)headCollectionView {
    if (!_headCollectionView) {
        _headCollectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(XC_SCREE_W, 0, XC_SCREE_W, XCPropsScrollViewH-(iPhoneX?30:0)) collectionViewLayout:self.flowLayout2];
        _headCollectionView.delegate = self;
        _headCollectionView.dataSource = self;
        _headCollectionView.userInteractionEnabled = YES;
        _headCollectionView.backgroundColor = [UIColor whiteColor];
        _headCollectionView.showsVerticalScrollIndicator = NO;
        _headCollectionView.showsHorizontalScrollIndicator = NO;
    }
    return _headCollectionView;
}

- (UICollectionView *)popularityCollectionView {
    if (!_popularityCollectionView) {
        _popularityCollectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(XC_SCREE_W*2, 0, XC_SCREE_W, XCPropsScrollViewH-(iPhoneX?30:0)) collectionViewLayout:self.flowLayout3];
        _popularityCollectionView.delegate = self;
        _popularityCollectionView.dataSource = self;
        _popularityCollectionView.userInteractionEnabled = YES;
        _popularityCollectionView.backgroundColor = [UIColor whiteColor];
        _popularityCollectionView.showsVerticalScrollIndicator = NO;
        _popularityCollectionView.showsHorizontalScrollIndicator = NO;
    }
    return _popularityCollectionView;
}

- (UICollectionViewFlowLayout *)flowLayout {
    if (!_flowLayout) {
        _flowLayout = [UICollectionViewFlowLayout new];
        _flowLayout.itemSize = CGSizeMake((kScreenWidth)/2, 210);
        _flowLayout.headerReferenceSize = CGSizeMake(kScreenWidth, 5);
        _flowLayout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
        _flowLayout.minimumInteritemSpacing = 0;
        _flowLayout.minimumLineSpacing = 0;
    }
    return _flowLayout;
}

- (UICollectionViewFlowLayout *)flowLayout2 {
    if (!_flowLayout2) {
        _flowLayout2 = [UICollectionViewFlowLayout new];
        _flowLayout2.itemSize = CGSizeMake((kScreenWidth)/2, 210);
        _flowLayout2.headerReferenceSize = CGSizeMake(kScreenWidth, 5);
        _flowLayout2.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
        _flowLayout2.minimumInteritemSpacing = 0;
        _flowLayout2.minimumLineSpacing = 0;
    }
    return _flowLayout2;
}

- (UICollectionViewFlowLayout *)flowLayout3 {
    if (!_flowLayout3) {
        _flowLayout3 = [UICollectionViewFlowLayout new];
        _flowLayout3.itemSize = CGSizeMake((kScreenWidth)/2, 210);
        _flowLayout3.headerReferenceSize = CGSizeMake(kScreenWidth, 5);
        _flowLayout3.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
        _flowLayout3.minimumInteritemSpacing = 0;
        _flowLayout3.minimumLineSpacing = 0;
    }
    return _flowLayout3;
}

- (HJMallHeaderView *)headerView {
    if (!_headerView) {
        _headerView = [[HJMallHeaderView alloc] init];
    }
    return _headerView;
}

- (HJMallBottomView *)bottomView {
    if (!_bottomView) {
        _bottomView = [[HJMallBottomView alloc] init];
        @weakify(self);
        _bottomView.buyGiftButtonAction = ^{
            @strongify(self);
            [self buyBtnAction];
        };
        _bottomView.sendGiftButtonAction = ^{
            @strongify(self);
            
            if (self.segmentIndex == 2) {
                //选择数量，
                [self showPopView];
            }else {
                //赠送礼物
                [self giftBtnAction];
            }
        };
        
        if (self.sendToUid>0 && self.sendToUserName.length) {
            if (self.sendToUid != [[GetCore(HJAuthCoreHelp) getUid] userIDValue]) {
                [_bottomView setupSendStyle];
            }
        }
    }
    return _bottomView;
}

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

- (UIImageView *)bgImgView {
    if(!_bgImgView) {
        _bgImgView = [UIImageView new];
        UserInfo *info = [GetCore(HJUserCoreHelp) getUserInfoInDB:[GetCore(HJAuthCoreHelp) getUid].userIDValue];
        [_bgImgView sd_setImageWithURL:[NSURL URLWithString:info.avatar]];
        _bgImgView.layer.masksToBounds = YES;
        _bgImgView.layer.cornerRadius = 15;
        _bgImgView.contentMode = UIViewContentModeScaleAspectFill;
    }
    return _bgImgView;
}

- (UIImageView *)topView {
    if (!_topView) {
        _topView = [UIImageView new];
//        _topView.image = [UIImage imageNamed:@"hj_mall_up_icon"];//#FF81A4
        _topView.backgroundColor = [UIColor whiteColor];
        _topView.userInteractionEnabled =YES;
    }
    return _topView;
}

- (UIView *)topViewLine {
    if (!_topViewLine) {
        _topViewLine = [UIView new];
        _topViewLine.backgroundColor = UIColorHex(F6F6F6);
    }
    return _topViewLine;
}

- (UIView *)topViewLine2 {
    if (!_topViewLine2) {
        _topViewLine2 = [UIView new];
        _topViewLine2.backgroundColor = UIColorHex(F6F6F6);
    }
    return _topViewLine2;
}

- (UIVisualEffectView *)effectView {
    if (!_effectView) {
        _effectView = [[UIVisualEffectView alloc] initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleLight]];
        _effectView.layer.masksToBounds = YES;
        _effectView.layer.cornerRadius = 15;
    }
    return _effectView;
}

- (UIImageView *)topViewImg {
    if (!_topViewImg) {
        _topViewImg = [[UIImageView alloc] init];
//        _topViewImg.image = [UIImage imageNamed:@"hj_mall_upimg_bg"];
        _topViewImg.backgroundColor = UIColorHex(#FF81A4);
        _topViewImg.contentMode = UIViewContentModeScaleToFill;
         _topView.userInteractionEnabled =YES;
    }
    return _topViewImg;
}

- (UIButton *)backBtn {
    if (!_backBtn) {
        _backBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_backBtn setImage:[UIImage imageNamed:@"hj_left_back"] forState:UIControlStateNormal];
        [_backBtn addTarget:self action:@selector(backBtnAction) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _backBtn;
}
- (UIButton *)backPackBtn {
    if (!_backPackBtn) {
        _backPackBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_backPackBtn setTitle:@"背包" forState:UIControlStateNormal];
        _backPackBtn.titleLabel.font = [UIFont systemFontOfSize:13];
        _backPackBtn.layer.cornerRadius = 12;
        _backPackBtn.layer.masksToBounds = YES;
        _backPackBtn.backgroundColor = [UIColor colorWithWhite:1 alpha:0.5];
        [_backPackBtn addTarget:self action:@selector(backPackBtnAction) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _backPackBtn;
}

//- (UIImageView *)avatarImgView {
//    if (!_avatarImgView) {
//        _avatarImgView = [[UIImageView alloc] init];
//        _avatarImgView.contentMode = UIViewContentModeScaleAspectFill;
//        _avatarImgView.layer.borderWidth = 7;
//        _avatarImgView.layer.borderColor = [UIColor whiteColor].CGColor;
//        _avatarImgView.layer.cornerRadius = 36;
//        _avatarImgView.layer.masksToBounds = YES;
//         _avatarImgView.userInteractionEnabled =YES;
//    }
//    return _avatarImgView;
//}
//
//- (UIImageView *)selectAvatarImgView {
//    if (!_selectAvatarImgView) {
//        _selectAvatarImgView = [[UIImageView alloc] init];
//        _selectAvatarImgView.contentMode = UIViewContentModeScaleToFill;//UIViewContentModeScaleAspectFill;//UIViewContentModeScaleAspectFit;// UIViewContentModeScaleToFill
//        _selectAvatarImgView.backgroundColor = [UIColor clearColor];
////        _selectAvatarImgView.layer.cornerRadius = 36;
////        _selectAvatarImgView.layer.masksToBounds = YES;
//         _selectAvatarImgView.userInteractionEnabled =YES;
//    }
//    return _selectAvatarImgView;
//}



- (UILabel *)titleMallLabel {
    if (!_titleMallLabel) {
        _titleMallLabel = [[UILabel alloc] init];
        _titleMallLabel.text = @"商城";
        _titleMallLabel.textAlignment = NSTextAlignmentCenter;
        _titleMallLabel.font = [UIFont boldSystemFontOfSize:20];
        _titleMallLabel.textColor = [UIColor whiteColor];
    }
    return _titleMallLabel;
}

@end
