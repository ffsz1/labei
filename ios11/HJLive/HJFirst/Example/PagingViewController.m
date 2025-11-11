//
//  OCExampleViewController.m
//  JXPagingView
//
//  Created by jiaxin on 2018/8/27.
//  Copyright © 2018年 jiaxin. All rights reserved.
//

#import "PagingViewController.h"
//#import <JXPagingView/JXPagerView.h>
#import "PagingViewTableHeaderView.h"
#import "TestListBaseView.h"
#import "JXPagerView.h"
#import "JXCategoryView.h"
#import "HJSearchRoomController.h"
#import "UITableView+MJRefresh.h"
#import "HJHomeCommonViewController.h"
#import "HJFirstHomeViewController.h"
#import "HJRoomViewControllerCenter.h"
#import "HJHomeTag.h"
#import "HJRankListVC.h"

static const CGFloat JXTableHeaderViewHeight = 200-20;
static const CGFloat JXheightForHeaderInSection = 50;

@interface PagingViewController () <JXPagerViewDelegate, JXCategoryViewDelegate,SDCycleScrollViewDelegate>
@property (nonatomic, strong) JXPagerView *pagingView;
@property (nonatomic, strong) PagingViewTableHeaderView *userHeaderView;
@property (nonatomic, strong) JXCategoryTitleView *categoryView;
@property (nonatomic, strong) NSArray <NSString *> *titles;
@property (nonatomic, strong) HJFirstHomeViewController* homevc;
@end

@implementation PagingViewController

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    HJBlackStatusBar
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    AddCoreClient(HJHomeCoreClient, self);
    AddCoreClient(HJAuthCoreClient, self);
    AddCoreClient(HJImLoginCoreClient, self);
    AddCoreClient(HJAuthCoreClient, self);
    AddCoreClient(HJUserCoreClient, self);
    AddCoreClient(HJWebSocketCoreClient, self);
//    [self.view addSubview:self.banaCommonView];
    [self setConstarin];
    [self getBanaerData];
    [GetCore(HJHomeCore) requestHomeOtherMenuData];
}

-(void)addTitles{

        [self.navigationController setNavigationBarHidden:YES animated:YES];
        _userHeaderView = [[PagingViewTableHeaderView alloc] initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, JXTableHeaderViewHeight)];

        _categoryView = [[JXCategoryTitleView alloc] initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, JXheightForHeaderInSection)];
        self.categoryView.titles = self.titles;
        self.categoryView.backgroundColor = [UIColor whiteColor];
        self.categoryView.delegate = self;
    self.categoryView.titleSelectedColor = [UIColor colorWithHexString:@"#FF81A4"];
        self.categoryView.titleColor = [UIColor colorWithHexString:@"#999999"];
//    self.categoryView.titleFont = [UIFont systemFontOfSize:14];
    self.categoryView.titleSelectedFont = [UIFont boldSystemFontOfSize:28];
        self.categoryView.titleColorGradientEnabled = YES;
        self.categoryView.titleLabelZoomEnabled = YES;
 
//        JXCategoryIndicatorLineView *lineView = [[JXCategoryIndicatorLineView alloc] init];
//        lineView.indicatorColor = [UIColor colorWithRed:105/255.0 green:144/255.0 blue:239/255.0 alpha:1];
//        lineView.indicatorWidth = 30;
//        self.categoryView.indicators = @[lineView];

        _pagingView = [[JXPagerView alloc] initWithDelegate:self];
        [self.view addSubview:self.pagingView];

        self.categoryView.listContainer = (id<JXCategoryViewListContainer>)self.pagingView.listContainerView;

        self.navigationController.interactivePopGestureRecognizer.enabled = (self.categoryView.selectedIndex == 0);
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    HJBlackStatusBar
//    self.pagingView.frame = self.view.bounds;
    self.pagingView.frame = CGRectMake(0, iPhoneX?50:30, kScreenWidth, kScreenHeight);
}

#pragma mark - HomeCore
- (void)requestHomeOtherMenuDataSuccess:(NSArray *)data {
    NSMutableArray *dataArr = [NSMutableArray arrayWithArray:data];
    HJHomeTag *tag1 = [[HJHomeTag alloc] init];
    tag1.name = @"热门";
    self.tagsListArr = [[NSMutableArray alloc] initWithObjects:tag1, nil];
    
    if (self.tagsListArr.count == 1) {
        [self.tagsListArr addObjectsFromArray:dataArr];
        NSMutableArray* arr = [NSMutableArray array];
        [arr addObject:@"热门"];
        for (HJHomeTag* model in dataArr) {
            [arr addObject:model.name];
        }

        _titles = arr;
        [self addTitles];
       
    }
}

- (void)reachabilityNetStateDidChange:(ReachabilityNetState)currentNetState {

    if (self.tagsListArr.count == 2) {
        [GetCore(HJHomeCore) requestHomeOtherMenuData];
    }
}
#pragma mark - JXPagingViewDelegate
- (void)downLoadRefleshForPagerView:(JXPagerView *)pagerView{
    __weak typeof(self)weakSelf = self;
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        self.pagingView.mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
            [self getBanaerData];
            weakSelf.homevc.isDownLoad = YES;
            [weakSelf.pagingView.mainTableView.mj_header endRefreshing];
        }];
    });
    
    
    
}
- (UIView *)tableHeaderViewInPagerView:(JXPagerView *)pagerView {
//    return self.userHeaderView;
    UIView* upview = [[UIView alloc] init];
    [upview addSubview: self.banaCommonView];
    
    
    self.leftView = [self getFirstViewForCellWithTitle:@"一键匹配" des:@"遇见有缘人" imgName:@"lb_caifu_icon"];
    self.middelView = [self getFirstViewForCellWithTitle:@"交友广场" des:@"聊天打招呼" imgName:@"lb_meilei_icon"];
    self.rightView = [self getFirstViewForCellWithTitle:@"搜索" des:@"喜翻名人榜" imgName:@"lb_shousuo_icon"];
    self.rightView2 = [self getFirstViewForCellWithTitle:@"房间" des:@"喜翻名人榜" imgName:@"lb_fangjian_icon"];
    [upview addSubview:self.leftView];
    [upview addSubview:self.middelView];
    [upview addSubview:self.rightView];
    [upview addSubview:self.rightView2];
    UITapGestureRecognizer *tapGesLeft = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesLeftClick:)];
           [self.leftView addGestureRecognizer:tapGesLeft];
    UITapGestureRecognizer *tapGesMiddel = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesMiddelClick:)];
           [self.middelView addGestureRecognizer:tapGesMiddel];
    UITapGestureRecognizer *tapGesRight = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesRightClick:)];
           [self.rightView addGestureRecognizer:tapGesRight];
    UITapGestureRecognizer *tapGesRight2 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesRightClick2:)];
           [self.rightView2 addGestureRecognizer:tapGesRight2];
    

    return upview;
}

- (NSUInteger)tableHeaderViewHeightInPagerView:(JXPagerView *)pagerView {
    return JXTableHeaderViewHeight;
}

- (NSUInteger)heightForPinSectionHeaderInPagerView:(JXPagerView *)pagerView {
    return JXheightForHeaderInSection;
}

- (UIView *)viewForPinSectionHeaderInPagerView:(JXPagerView *)pagerView {
    return self.categoryView;
}

- (NSInteger)numberOfListsInPagerView:(JXPagerView *)pagerView {
    return self.titles.count;
}

- (id<JXPagerViewListViewDelegate>)pagerView:(JXPagerView *)pagerView initListAtIndex:(NSInteger)index {
    
    if (index == 0) {
        HJFirstHomeViewController* vc = [[HJFirstHomeViewController alloc] init];
        self.homevc = vc;
        return vc;
    }else{
        HJHomeCommonViewController* vc = [[HJHomeCommonViewController alloc] init];
        HJHomeTag* model =  self.tagsListArr[index];
        vc.type = model.id;
        return vc;
    }

    
}

- (void)mainTableViewDidScroll:(UIScrollView *)scrollView {
    [self.userHeaderView scrollViewDidScroll:scrollView.contentOffset.y];
}

#pragma mark - JXCategoryViewDelegate

- (void)categoryView:(JXCategoryBaseView *)categoryView didSelectedItemAtIndex:(NSInteger)index {
    self.navigationController.interactivePopGestureRecognizer.enabled = (index == 0);
}

#pragma mark - SDCycleScrollViewDelegate
- (void)cycleScrollView:(SDCycleScrollView *)cycleScrollView didSelectItemAtIndex:(NSInteger)index {
    
    HJBannerInfo *info = [self.bannerInfos safeObjectAtIndex:index];
    if (info.skipType == BannerInfoSkipTypeWeb) {
        HJWKWebViewController *vc = [[HJWKWebViewController alloc]init];
        NSString *url = [NSString stringWithFormat:@"%@?uid=%@",info.skipUri,[GetCore(HJAuthCoreHelp)getUid]];
        vc.url = [NSURL URLWithString:url];
        [self.navigationController pushViewController:vc animated:YES];
    }else if (info.skipType == BannerInfoSkipTypeRoom) {
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        [[HJRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomOwnerUid:info.skipUri.userIDValue succ:^(ChatRoomInfo *roomInfo) {
            if (roomInfo != nil) {
                [[HJRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomInfo:roomInfo];
            }else {
                [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
            }
        } fail:^(NSString *errorMsg) {
            [MBProgressHUD showError:errorMsg];
        }];
    }
}
//MARK:- Action

- (void)tapGesLeftClick:(UITapGestureRecognizer *)tapGes {
    HJRankListVC *jumpVC = HJHomeStoryBoard(@"HJRankListVC");
    jumpVC.isToCaifuRank = YES;
    [self.navigationController pushViewController:jumpVC animated:YES];
   
    
    
}
- (void)tapGesMiddelClick:(UITapGestureRecognizer *)tapGes {
    HJRankListVC *jumpVC = HJHomeStoryBoard(@"HJRankListVC");
    jumpVC.isToCaifuRank = NO;
    [self.navigationController pushViewController:jumpVC animated:YES];
}


- (void)tapGesRightClick:(UITapGestureRecognizer *)tapGes {
    HJSearchRoomController *vc = HJHomeStoryBoard(@"HJSearchRoomController");
    vc.mainNavicationController = self.navigationController;
    UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:vc];
    if (@available(iOS 13.0, *)) {

            nav.modalPresentationStyle = UIModalPresentationFullScreen;
       }
    [self.navigationController presentViewController:nav animated:YES completion:nil];
}

- (void)tapGesRightClick2:(UITapGestureRecognizer *)tapGes {
    [[HJRoomViewControllerCenter defaultCenter] openRoonWithType:RoomType_Game];
}



- (void)rankBtnAction
{
    [[HJRoomViewControllerCenter defaultCenter] openRoonWithType:RoomType_Game];
}

- (void)searchBtnAction
{
    HJSearchRoomController *vc = HJHomeStoryBoard(@"HJSearchRoomController");
    vc.mainNavicationController = self.navigationController;
    UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:vc];
    if (@available(iOS 13.0, *)) {

            nav.modalPresentationStyle = UIModalPresentationFullScreen;
       }
    [self.navigationController presentViewController:nav animated:YES completion:nil];
}

//MARK:- pravide

//data
-(void)getBanaerData{
    [HJHttpRequestHelper requestBannerList:^(NSArray *list) {
        
        NSMutableArray *bannerArray = [NSMutableArray array];
        NSMutableArray *buffer = @[].mutableCopy;
        for (HJBannerInfo *bannerInfo in list) {
            [bannerArray addObject:bannerInfo.bannerPic];
            [buffer addObject:bannerInfo];
        }
        self.bannerInfos = buffer.copy;
        self.banaCommonView.imageURLStringsGroup = bannerArray;
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}







-(void)setConstarin{
//    [self.topView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.left.right.top.mas_equalTo(self.view);
//        make.height.mas_equalTo(iPhoneX?88:64);
//    }];
//    [self.rankBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.top.equalTo(self.topView).mas_offset(iPhoneX?47:32);
//        make.right.equalTo(self.topView).mas_offset(-16);
//        make.width.mas_equalTo(29);
//        make.height.mas_equalTo(26);
//    }];
//
//       [self.searchBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//           make.top.equalTo(self.topView).mas_offset(iPhoneX?47:32);
//           make.left.equalTo(self.topView).mas_offset(15);
//           make.right.equalTo(self.rankBtn.mas_left).mas_offset(-15);
//           make.height.mas_equalTo(30);
////            make.width.mas_equalTo(24);
//       }];
    
  
}

//MARK:- Get/set
//- (UIView *)topView
//{
//    if (!_topView) {
//        _topView = [UIView new];
//        _topView.frame = CGRectZero;
//        _topView.backgroundColor = [UIColor whiteColor];
//
//        [self.view addSubview:_topView];
//    }
//    return _topView;
//}
- (UIButton *)searchBtn
{
    if (!_searchBtn) {
        _searchBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_searchBtn addTarget:self action:@selector(searchBtnAction) forControlEvents:UIControlEventTouchUpInside];
//         [_searchBtn setImage:[UIImage imageNamed:@"hj_home_search_icon"] forState:UIControlStateNormal];
        [_searchBtn setImage:[UIImage imageNamed:@"hj_home_search_gray"] forState:UIControlStateNormal];
        [_searchBtn setTitle:@" 输入ID/昵称/房间号" forState:UIControlStateNormal];
        _searchBtn.titleLabel.font = JXFontPingFangSCRegular(12);
        [_searchBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        _searchBtn.backgroundColor = UIColorHex(F2F2F2);
        _searchBtn.layer.cornerRadius = 13;
        _searchBtn.layer.masksToBounds = YES;
        [self.topView addSubview:_searchBtn];
    }
    return _searchBtn;
}

- (UIButton *)rankBtn
{
    if (!_rankBtn) {
        _rankBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_rankBtn addTarget:self action:@selector(rankBtnAction) forControlEvents:UIControlEventTouchUpInside];
//        [_rankBtn setImage:[UIImage imageNamed:@"hj_home_rank"] forState:UIControlStateNormal];
        [_rankBtn setImage:[UIImage imageNamed:@"hj_home_rankBg"] forState:UIControlStateNormal];
        [self.topView addSubview:_rankBtn];
    }
    return _rankBtn;
}

- (HJEmptyView *)tipView
{
    if (!_tipView) {
        _tipView = [[HJEmptyView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 428)];
        [_tipView setTitle:@"列表里没有任何内容呢" image:@"blank"];
    }
    return _tipView;
}

- (SDCycleScrollView *)banaCommonView
{
    if (!_banaCommonView) {
        _banaCommonView = [[SDCycleScrollView alloc] init];
        _banaCommonView.frame= CGRectMake(12, 0, kScreenWidth-24, 120);
        _banaCommonView.delegate = self;

        _banaCommonView.pageControlAliment = SDCycleScrollViewPageContolAlimentCenter;
        _banaCommonView.backgroundColor = [UIColor clearColor];
        _banaCommonView.bannerImageViewContentMode = UIViewContentModeScaleToFill;

//        _banaCommonView.localizationImageNamesGroup = @[@"hj_home_weekStar"];
         _banaCommonView.placeholderImage = [UIImage imageNamed:@"hj_home_weekStar"];
        _banaCommonView.layer.cornerRadius = 7;
        _banaCommonView.layer.masksToBounds = YES;
        if (self.bannerInfos != nil && self.bannerInfos.count > 0) {
               NSMutableArray *array = [NSMutableArray array];
               for (HJBannerInfo *info in self.bannerInfos) {
                   [array addObject:info.bannerPic];
               }
               _banaCommonView.imageURLStringsGroup = [array copy];
           }
          
//        [self.banaCommonView mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.size.mas_equalTo(CGSizeMake(kScreenWidth-26, 120));
//            make.left.mas_equalTo(self.view.mas_left).offset(13);
//            make.right.mas_equalTo(self.view.mas_right).offset(-13);
//        }];
        

    }
    return _banaCommonView;
}

- (UIView *)getFirstViewForCellWithTitle:(NSString*)title des:(NSString*)des imgName:(NSString*)imgName{
   
    UIView*  firstViewForCell = [[UIView alloc] init];
    if ([title isEqualToString:@"一键匹配"]) {
        firstViewForCell.frame = CGRectMake(12, CGRectGetMaxY(_banaCommonView.frame)+12, (kScreenWidth-13*5)/4.0, (kScreenWidth-55)/4.0/77.0*42);
    }else if ([title isEqualToString:@"交友广场"]){
        firstViewForCell.frame = CGRectMake(CGRectGetMaxX(_leftView.frame)+12, CGRectGetMaxY(_banaCommonView.frame)+12, (kScreenWidth-13*5)/4.0, (kScreenWidth-55)/4.0/77.0*42);
    }else if ([title isEqualToString:@"搜索"]){
        firstViewForCell.frame = CGRectMake(CGRectGetMaxX(_middelView.frame)+12, CGRectGetMaxY(_banaCommonView.frame)+12, (kScreenWidth-13*5)/4.0, (kScreenWidth-55)/4.0/77.0*42);
    }else{
        firstViewForCell.frame = CGRectMake(kScreenWidth-12-(kScreenWidth-13*5)/4.0,CGRectGetMaxY(_banaCommonView.frame)+12, (kScreenWidth-13*5)/4.0, (kScreenWidth-55)/4.0/77.0*42);
    }

    UILabel* titleLabel = [[UILabel alloc] init];
    UILabel* desLabel = [[UILabel alloc] init];
    titleLabel.frame = CGRectMake(14, 14, 58, 20);
    desLabel.frame = CGRectMake(14, CGRectGetMaxY(titleLabel.frame)+4, 58, 20);
    titleLabel.font = [UIFont systemFontOfSize:13];
    titleLabel.textColor = [UIColor colorWithHexString:@"#333333"];
//    titleLabel.text = title;
//    desLabel.text = des;
    desLabel.font = [UIFont systemFontOfSize:9];
    desLabel.textColor = [UIColor colorWithHexString:@"#999999"];
    
    UIImageView* imgViewbg = [[UIImageView alloc] init];
    imgViewbg.contentMode =UIViewContentModeScaleToFill;
    imgViewbg.frame = CGRectMake(0, 0, (kScreenWidth-13*5)/4.0, (kScreenWidth-55)/4.0/77.0*42);
    imgViewbg.userInteractionEnabled = YES;
    imgViewbg.image = [UIImage imageNamed:imgName];
   
    [firstViewForCell addSubview:imgViewbg];
//    [firstViewForCell addSubview:titleLabel];
//    [firstViewForCell addSubview:desLabel];

    return firstViewForCell;
    
    
    
}




@end


