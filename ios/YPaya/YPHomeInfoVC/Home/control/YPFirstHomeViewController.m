//
//  YPFirstHomeViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//


#import "YPFirstHomeViewController.h"
#import "YPHomeCommonViewController.h"
#import "YPSearchRoomController.h"
#import "YPHomeFollowVC.h"

#import "YPHomeTagView.h"
#import "YPHomeHeaderView.h"
#import "YPEmptyView.h"
#import "YPFirstHomeSectionView.h"
#import "YPHomeRecommedTableCell.h"
#import "YPHomeRoomCell.h"

#import "YPHomeTag.h"
#import "YPHomePageInfo.h"
#import "YPBannerInfo.h"

#import "HJHomeCoreClient.h"
#import "HJAuthCoreClient.h"
#import "HJImLoginCoreClient.h"
#import "HJIReachability.h"
#import "YPReachabilityCore.h"
#import "HJRoomCoreClient.h"
#import "YPVersionCoreHelp.h"
#import "HJUserCoreClient.h"

#import "UITableView+MJRefresh.h"
#import "UIButton+JXLayout.h"
#import "YPRoomViewControllerCenter.h"
#import "YPHttpRequestHelper+Home.h"
#import "YPWKWebViewController.h"

#import "YPIMRequestManager.h"
#import "YPIMRequestManager+PublicRoom.h"
#import "YPHttpRequestHelper+Alert.h"
#import "YPPushVC.h"

#import "YPSignnAlterView.h"
#import "YPYouthAlterView.h"

#import "HJWebSocketCoreClient.h"

#import "YPGiftCore.h"
#import "Healp.h"
#import "YPHomePeipeiModel.h"
#import "YPFileCore.h"
#import "YPMediaCore.h"
#import "YPHomeRoomCell.h"
#import "HJMediaCoreClient.h"
#import "HJFileCoreClient.h"
#import "YPRoomPusher.h"
#import "YPSessionViewController.h"
#import "YPMySpaceVC.h"
#import "YPHomeHotTableCell.h"
#import "YPHomePeipeiTableCell.h"
#import "YPSexPopVC.h"
#import "YPNotiFriendVC.h"

#define HJHomeTopViewHeight (74 + (iPhoneX?15:0))
#define HJHomeTableViewHeight XC_SCREE_H - XC_Height_TabBar +18

@interface YPFirstHomeViewController ()<UIScrollViewDelegate,UITableViewDelegate,UITableViewDataSource,SDCycleScrollViewDelegate,HJWebSocketCoreClient,HJMediaCoreClient,HJFileCoreClient,UIPopoverPresentationControllerDelegate>

@property (nonatomic,strong) UIView *topView;

@property (nonatomic,strong) UIImageView *topImageView;
@property (nonatomic,strong) YPHomeTagView *topTagView;
@property (nonatomic,strong) UIButton *searchBtn;
@property (nonatomic,strong) UIButton *rankBtn;

@property (nonatomic,strong) UIScrollView *bgScrollView;

@property (nonatomic, strong) YPHomeHeaderView *headerView;//banaer
@property (nonatomic, strong) YPHomeHeaderView *radioFriendView;//交友滚栏
@property (nonatomic, strong) UIView *leftView;
@property (nonatomic, strong) UIView *rightView;
//闲聊
@property (nonatomic,strong) UITableView *hotTableView;
//数据
@property (nonatomic, strong) NSArray *bannerInfos;//广告图
@property (nonatomic, strong) NSArray *hotRooms;//推荐房间
@property (nonatomic, strong) NSMutableArray *listRooms;//热门房间列表
@property (nonatomic, strong) NSMutableArray *peipeiRooms;//
@property (nonatomic, strong) NSMutableArray *mengxinRooms;//


@property (nonatomic, strong) NSMutableArray *tagsListArr;//顶部标签栏数组
@property (nonatomic,strong) YPEmptyView *tipView;
@property (nonatomic, strong) NSArray *activityArr;
@property (nonatomic, strong) YPSDCycleScrollView *activityView;
@property (assign,nonatomic) BOOL isShowingSign;

@property (nonatomic,assign) int retryTime_IMRoomMsg;
@property (nonatomic,assign) int hotPage;
@property (nonatomic,assign) int peipeiPage;
@property (nonatomic,assign) int mengxinPage;
@property (nonatomic,assign) BOOL isSelectHotFlag;
@property (nonatomic,assign) BOOL isSelectPeipeiFlag;
@property (nonatomic,assign) BOOL isSelectMengxinFlag;

@property (nonatomic,assign) BOOL isHotEndData;
@property (nonatomic,assign) BOOL isPeipeiEndData;
@property (nonatomic,assign) BOOL isMengxinEndData;
@property (nonatomic, strong)YPFirstHomeSectionView *homeSectionView;
@property (nonatomic, strong)YPHomePeipeiTableCell * _Nonnull voiceCell;
@property (nonatomic, strong)YPHomePeipeiModel * _Nonnull peipeiModel;
@property (nonatomic, strong)NSMutableArray* tempAllPeipeiArray;
@property (nonatomic,assign) BOOL isThis;
@property (nonatomic, strong) NSString *selectSexForPeipei;//1 男 2女 0全部

@end

@implementation YPFirstHomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.view addSubview:self.topImageView];
    self.view.backgroundColor = UIColorHex(FAFAFA);
    self.isThis = YES;
     self.selectSexForPeipei = @"0";
    [self setTopTag];
   
    self.view.backgroundColor = [UIColor whiteColor];
    self.homeSectionView.lineView.hidden = YES;
    self.listRooms = [[NSMutableArray alloc] init];
    self.peipeiRooms = [[NSMutableArray alloc] init];
    self.mengxinRooms = [[NSMutableArray alloc] init];
    self.tempAllPeipeiArray =[NSMutableArray array];
    self.bgScrollView.contentSize = CGSizeMake(XC_SCREE_W*2, 0);
//    self.bgScrollView.backgroundColor = [UIColor clearColor];
    AddCoreClient(HJHomeCoreClient, self);
    AddCoreClient(HJAuthCoreClient, self);
    AddCoreClient(HJImLoginCoreClient, self);
    AddCoreClient(HJAuthCoreClient, self);
    AddCoreClient(HJUserCoreClient, self);
    AddCoreClient(HJWebSocketCoreClient, self);
    AddCoreClient(HJMediaCoreClient, self);
    AddCoreClient(HJFileCoreClient, self);
    
    YPLightStatusBar
    
    __weak typeof(self)weakSelf = self;
    [self.hotTableView setHeaderBlock:^{
        weakSelf.hotPage = 1;
        weakSelf.peipeiPage = 1;
        weakSelf.mengxinPage = 1;
        
       
        [weakSelf getIMRoomMsgData];
        
        if (weakSelf.isSelectPeipeiFlag) {
//             [weakSelf requestPeipeiData];//请求陪陪数据
        }else if (weakSelf.isSelectMengxinFlag){
//             [weakSelf requestMengxinData];
        }else{
             [weakSelf requestRoomDatas];
        }
       
          
    }];
    
    [self.hotTableView setFooterBlock:^{
       
        if (weakSelf.isSelectHotFlag) {
            weakSelf.hotPage += 1;
             [weakSelf requestRoomDatas];
        }else if (weakSelf.isSelectPeipeiFlag){
//            weakSelf.peipeiPage += 1;
//             [weakSelf requestPeipeiData];
        }else if (weakSelf.isSelectMengxinFlag){
//            weakSelf.mengxinPage += 1;
//            [weakSelf requestMengxinData];
        }

    }];
    
    [GetCore(YPHomeCore) requestHomeOtherMenuData];
   
    
    [self setMasonryLayout];
    
//    UIViewController *followVC = YPHomeStoryBoard(@"YPHomeFollowVC");
//
//    [self addChildViewController:followVC];
//    [self.bgScrollView addSubview:followVC.view];
//    followVC.view.frame = CGRectMake(kScreenWidth, HJHomeTopViewHeight, kScreenWidth, HJHomeTableViewHeight-HJHomeTopViewHeight);
    //请求页码
    self.hotPage = 1;
    self.peipeiPage = 1;
    self.mengxinPage = 1;
    self.isSelectHotFlag = YES;
     [self requestRoomDatas];
//    [self requestPeipeiData];//请求陪陪数据
//    [self requestMengxinData];
    //通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(jumpPrivateChatPageAction:) name:@"jumpPrivateChatPageNotification" object:nil];
}
- (void)jumpPrivateChatPageAction:(NSNotification *)noti {
    if (!self.isThis) {
        return;
    }
    if (noti) {
        if (noti.object) {
            UserID uid = [noti.object longLongValue];
            NIMSession *session = [NIMSession session:[NSString stringWithFormat:@"%lld",uid] type:0];
                      YPSessionViewController *vc = [[YPSessionViewController alloc] initWithSession:session];
                      [self.navigationController pushViewController:vc animated:YES];
        }
    }
}
-(void)viewDidAppear:(BOOL)animated{
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    self.isThis = NO;
    if ([GetCore(YPMediaCore) isPlaying]) {
        [GetCore(YPMediaCore) stopPlay];
    }
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self preferredStatusBarStyle];
    self.isThis = YES;
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    if (_topTagView) {
        if (_topTagView.sel == 0) {
//            [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
        }else{
//            [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
        }
    }
     [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
    if (@available(iOS 11.0, *)) {
        self.bgScrollView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
        self.topTagView.collectView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
        self.hotTableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
        
    } else {
        self.automaticallyAdjustsScrollViewInsets = NO;
    }
    
    [YPYouthAlterView check];
    
}
- (UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent; //返回白色
//    return UIStatusBarStyleDefault;    //返回黑色
}




- (void)setMasonryLayout
{
    [self.topView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.mas_equalTo(self.view);
        make.height.mas_equalTo(HJHomeTopViewHeight);
    }];
    
 [self.rankBtn mas_makeConstraints:^(MASConstraintMaker *make) {
     make.top.equalTo(self.topView).mas_offset(iPhoneX?47:32);
     make.right.equalTo(self.topView).mas_offset(-16);
     make.width.mas_equalTo(29);
     make.height.mas_equalTo(26);
 }];
    
    [self.searchBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.topView).mas_offset(iPhoneX?47:32);
        make.right.equalTo(self.rankBtn.mas_left).mas_offset(-15);
        make.height.mas_equalTo(24);
         make.width.mas_equalTo(24);
    }];
    

    [self.topTagView mas_makeConstraints:^(MASConstraintMaker *make) {
         make.top.equalTo(self.topView).mas_offset(iPhoneX?42:25);
         make.left.equalTo(self.topView).mas_offset(13);
        make.right.equalTo(self.searchBtn.mas_left).mas_offset(-20);
         make.height.mas_equalTo(38);
        _topTagView.backgroundColor = [UIColor clearColor];
     }];
    
    [self.activityView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(90, 90));
        
        make.top.mas_equalTo(self.view.mas_top).offset(kScreenHeight - XC_Height_TabBar-100-90);
        
        make.right.mas_equalTo(self.view.mas_right).offset(-10);
    }];
    self.activityView.hidden = YES;
    
}

//设置顶部标签
- (void)setTopTag
{
    YPHomeTag *tag1 = [[YPHomeTag alloc] init];
    tag1.name = @"热门";
    
//    YPHomeTag *tag2 = [[YPHomeTag alloc] init];
//    tag2.name = @"关注";
    
    self.tagsListArr = [[NSMutableArray alloc] initWithObjects:tag1, nil];
    
    self.topTagView.roomTagList = self.tagsListArr ;
    
    [self.topTagView setScrollTag:0];
//    [self.bgScrollView setContentOffset:CGPointMake(XC_SCREE_W, 0) animated:NO];
    
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (scrollView == self.hotTableView && self.topTagView.sel == 1) {

        CGFloat offsetY = scrollView.contentOffset.y;

        if (offsetY >= 0 && self.hotTableView.header.state != MJRefreshStateIdle) {
            self.hotTableView.header.state = MJRefreshStateIdle;
        }

        [self updateNavBar:offsetY];

    }

    //过滤switchBar点击的回调
    if (!scrollView.isDecelerating && !scrollView.tracking) {

        return;
    }
    if (scrollView == self.bgScrollView) {

        CGFloat offsetX = scrollView.contentOffset.x;
        CGFloat index = offsetX / XC_SCREE_W;


        if (index == ceilf(index)) {


            if (index == 1) {
                CGFloat offsetY = self.hotTableView.contentOffset.y;
                [self updateNavBar:offsetY];
            }else{
                [self setStytle:NO];
            }
            [self.topTagView setScrollTag:index];
        }else{
            [self setStytle:NO];
        }
    }
    
}

//更新导航条状态
- (void)updateNavBar:(CGFloat)offsetY
{

    [self setStytle:NO];
//           self.topView.backgroundColor = [UIColor colorWithWhite:1 alpha:1];
    self.topView.backgroundColor = [UIColor clearColor];
}


#pragma mark - UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {

        return self.hotRooms.count/3+(self.hotRooms.count%3>0?1:0);
    }
    if (section == 1) {
        return 1;
    }
    
    if (self.isSelectMengxinFlag) {
           return self.mengxinRooms.count;
      }else if (self.isSelectPeipeiFlag){
//           return self.peipeiRooms.count;
           return self.peipeiRooms.count/2+(self.peipeiRooms.count%2>0?1:0);
      }else{
//           return self.listRooms.count;
           return self.listRooms.count/2+(self.listRooms.count%2>0?1:0);
      }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{

    if (section == 2 && self.listRooms.count>0) {
        return 50;
    }
    
    return CGFLOAT_MIN;
}
- (void)showPopView {
//    self.bottomView.sendBtn.selected = YES;
  __block YPSexPopVC *contentVC = [[YPSexPopVC alloc] init];
    
    contentVC.modalPresentationStyle = UIModalPresentationPopover;
    contentVC.popoverPresentationController.sourceView = self.homeSectionView.sexImageView;
    contentVC.popoverPresentationController.sourceRect = CGRectMake(0, 0, 0, self.homeSectionView.sexImageView.bounds.size.height+5);
    contentVC.popoverPresentationController.permittedArrowDirections = UIPopoverArrowDirectionUp;
    contentVC.popoverPresentationController.backgroundColor = [UIColor whiteColor];
    // 设置代理
    contentVC.popoverPresentationController.delegate = self;
    @weakify(contentVC);
    
    contentVC.allBtnBlock = ^{
        self.selectSexForPeipei = @"0";
        self.peipeiPage = 1;
        [self.homeSectionView.sexImageView setImage:[UIImage imageNamed:@"home_peipei_tegetder"]];
        [self.homeSectionView.sexImageViewFlag setImage:[UIImage imageNamed:@"home_peipei_down"]];
        [self requestPeipeiData];

        [contentVC_weak_ dismissViewControllerAnimated:YES completion:nil];
    };
    contentVC.manBtnBlock = ^{
         self.selectSexForPeipei = @"1";
        self.peipeiPage = 1;
        [self.homeSectionView.sexImageView setImage:[UIImage imageNamed:@"home_peipei_man"]];
        [self.homeSectionView.sexImageViewFlag setImage:[UIImage imageNamed:@"home_peipei_down"]];
        [self requestPeipeiData];

        [contentVC_weak_ dismissViewControllerAnimated:YES completion:nil];
    };
    contentVC.wemanBtnBlock = ^{
         self.selectSexForPeipei = @"2";
        self.peipeiPage = 1;
        [self.homeSectionView.sexImageView setImage:[UIImage imageNamed:@"home_peipei_weman"]];
        [self.homeSectionView.sexImageViewFlag setImage:[UIImage imageNamed:@"home_peipei_down"]];
        [self requestPeipeiData];

        [contentVC_weak_ dismissViewControllerAnimated:YES completion:nil];
    };
    
    [self.homeSectionView.sexImageViewFlag setImage:[UIImage imageNamed:@"home_peipei_up"]];
    [self presentViewController:contentVC animated:YES completion:nil];
}

//UIPopoverPresentationControllerDelegate
-(UIModalPresentationStyle)adaptivePresentationStyleForPresentationController:(UIPresentationController *)controller{
    return UIModalPresentationNone; //不适配
}

 - (BOOL)popoverPresentationControllerShouldDismissPopover:(UIPopoverPresentationController *)popoverPresentationController{
//    self.bottomView.sendBtn.selected = NO;
     [self.homeSectionView.sexImageViewFlag setImage:[UIImage imageNamed:@"home_peipei_down"]];
    return YES;   //点击蒙版popover消失， 默认YES
}
- (UIView*)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    if (section == 0 ) {
//        if (self.hotRooms.count ==0) {
//            return [UIView new];
//        }
         return [UIView new];
    }
    
    if (section == 2) {
        if (self.listRooms.count ==0) {
            return [UIView new];
        }else{
               __weak typeof(self)weakself = self;
            self.homeSectionView.sexBtnBlock = ^{
                
                [weakself showPopView];
                
            };
            self.homeSectionView.leftBtnBlock = ^{
                 
                if (weakself.isHotEndData) {
                [weakself.hotTableView.mj_footer endRefreshingWithNoMoreData];
                }else{
                     [weakself.hotTableView.mj_footer resetNoMoreData];
                }

                weakself.isSelectHotFlag = YES;
                weakself.isSelectPeipeiFlag = NO;
                weakself.isSelectMengxinFlag = NO;
             //UI处理
//                [weakself.homeSectionView.leftBtn setTitleColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_wenzhi_img_80"]] forState:UIControlStateNormal];
                [weakself.homeSectionView.leftBtn setTitleColor:[UIColor colorWithHexString:@"#333333"] forState:UIControlStateNormal];
                
                
                [weakself.homeSectionView.middleBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
                 [weakself.homeSectionView.rightBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
                weakself.homeSectionView.lineView.hidden = YES;
                weakself.homeSectionView.lineView2.hidden = YES;
                weakself.homeSectionView.lineView3.hidden = YES;
                weakself.homeSectionView.leftBtn.titleLabel.font = JXFontPingFangSCRegular(18);
                 weakself.homeSectionView.middleBtn.titleLabel.font = JXFontPingFangSCRegular(16);
                 weakself.homeSectionView.rightBtn.titleLabel.font = JXFontPingFangSCRegular(16);
//                  [weakself.hotTableView reloadData];
                [UIView performWithoutAnimation:^{
                    [tableView beginUpdates];
                     NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:2];
                      [weakself.hotTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
                    [tableView endUpdates];
                }];
                //事件处理
            };
//            _homeSectionView.middleBtnBlock = ^{
//
//                if (weakself.isPeipeiEndData) {
//                              [weakself.hotTableView.mj_footer endRefreshingWithNoMoreData];
//                              }else{
//                                   [weakself.hotTableView.mj_footer resetNoMoreData];
//                              }
//
//              weakself.isSelectPeipeiFlag = YES;
//            weakself.isSelectMengxinFlag = NO;
//            weakself.isSelectHotFlag = NO;
////                [weakself.homeSectionView.middleBtn setTitleColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_wenzhi_img_80"]] forState:UIControlStateNormal];
//                  [weakself.homeSectionView.middleBtn setTitleColor:[UIColor colorWithHexString:@"#333333"] forState:UIControlStateNormal];
//
//
//                [weakself.homeSectionView.leftBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
//                [weakself.homeSectionView.rightBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
//                weakself.homeSectionView.lineView.hidden = YES;
//                              weakself.homeSectionView.lineView2.hidden = NO;
//                              weakself.homeSectionView.lineView3.hidden = YES;
//                              weakself.homeSectionView.leftBtn.titleLabel.font = JXFontPingFangSCRegular(16);
//                               weakself.homeSectionView.middleBtn.titleLabel.font = JXFontPingFangSCRegular(18);
//                               weakself.homeSectionView.rightBtn.titleLabel.font = JXFontPingFangSCRegular(16);
////                [weakself.hotTableView reloadData];
//               [UIView performWithoutAnimation:^{
//                   [tableView beginUpdates];
//                   NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:2];
//                     [weakself.hotTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
//                   [tableView endUpdates];
//               }];
//
//
//            };
//            _homeSectionView.rightBtnBlock = ^{
//
//                if (weakself.isMengxinEndData) {
//                              [weakself.hotTableView.mj_footer endRefreshingWithNoMoreData];
//                              }else{
//                                   [weakself.hotTableView.mj_footer resetNoMoreData];
//                              }
//
//                weakself.isSelectMengxinFlag = YES;
//                weakself.isSelectPeipeiFlag = NO;
//                weakself.isSelectHotFlag = NO;
////               [weakself.homeSectionView.rightBtn setTitleColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_wenzhi_img_80"]] forState:UIControlStateNormal];
//                [weakself.homeSectionView.rightBtn setTitleColor:[UIColor colorWithHexString:@"#333333"] forState:UIControlStateNormal];
//
//                [weakself.homeSectionView.middleBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
//                [weakself.homeSectionView.leftBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
//                weakself.homeSectionView.lineView.hidden = YES;
//                              weakself.homeSectionView.lineView2.hidden = YES;
//                              weakself.homeSectionView.lineView3.hidden = NO;
//                              weakself.homeSectionView.leftBtn.titleLabel.font = JXFontPingFangSCRegular(16);
//                               weakself.homeSectionView.middleBtn.titleLabel.font = JXFontPingFangSCRegular(16);
//                               weakself.homeSectionView.rightBtn.titleLabel.font = JXFontPingFangSCRegular(18);
//
//                [UIView performWithoutAnimation:^{
//                    [tableView beginUpdates];
//                    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:2];
//                      [weakself.hotTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
//                    [tableView endUpdates];
//                }];
//            };
             return _homeSectionView;
        }
    }
    
   
    return [UIView new];
    
    

   
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return CGFLOAT_MIN;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
       
//        return JXHomeRecommedCellWidth*(113.3/109.0)+30;
          return JXHomeRecommedCellWidth*(147/116.0);
    }
    if (indexPath.section == 1) {
        return (kScreenWidth/2-10)/184.0*80 +12;
    }
    if (self.isSelectHotFlag) {
        return 187*(kScreenWidth/375.0);
    }
    if (self.isSelectPeipeiFlag) {
           return 187*(kScreenWidth/375.0);
       }
    
    return 114;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        
        YPHomeRecommedTableCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPHomeRecommedTableCell"];
        if (indexPath.row*3+1 == self.hotRooms.count) {
            cell.roomArr = @[self.hotRooms[indexPath.row*3]];
        }else{
            
            if (indexPath.row*3+2 == self.hotRooms.count) {
                cell.roomArr = @[self.hotRooms[indexPath.row*3],self.hotRooms[indexPath.row*3+1]];
            }else{
                cell.roomArr = @[self.hotRooms[indexPath.row*3],self.hotRooms[indexPath.row*3+1],self.hotRooms[indexPath.row*3+2]];
                
            }
            
        }
        
        return cell;
    }
    if (indexPath.section ==1) {
        UITableViewCell* cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"cellID"];
        self.leftView = [self getFirstViewForCellWithTitle:@"交友广场" des:@"聊天打招呼" imgName:@"yp_home_jiaoyou_icon"];
        self.rightView = [self getFirstViewForCellWithTitle:@"排行榜" des:@"喜翻名人榜" imgName:@"yp_home_phb_icon"];
        [cell.contentView addSubview:self.leftView];
        [cell.contentView addSubview:self.rightView];
        cell.backgroundColor = [UIColor clearColor];
        
        UITapGestureRecognizer *tapGesLeft = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesLeftClick:)];
               [self.leftView addGestureRecognizer:tapGesLeft];
        
        UITapGestureRecognizer *tapGesRight = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesRightClick:)];
               [self.rightView addGestureRecognizer:tapGesRight];
        
        
//        [cell.contentView addSubview:self.radioFriendView];
//        [self setupRadioFriendView];

//        YPFirstHomeSectionView* qualityView = [[YPFirstHomeSectionView alloc] initWithFrame:CGRectMake(0, 10, XC_SCREE_W, 30)];
//                    qualityView.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
//           qualityView.logoImageView.image = [UIImage imageNamed:@"yp_home_guangbojiaoyou"];
//           qualityView.tipLabel.text = @"广播交友";
//          [cell.contentView addSubview:qualityView];
        cell.contentView.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
    }
    
    if (self.isSelectHotFlag) {
        YPHomeHotTableCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPHomeHotTableCell"];
        if (indexPath.row*2+1 == self.listRooms.count) {
                   cell.roomArr = @[self.listRooms[indexPath.row*2]];
               }else{
                    cell.roomArr = @[self.listRooms[indexPath.row*2],self.listRooms[indexPath.row*2+1]];
               }
        return cell;
    }else if (self.isSelectPeipeiFlag){

        YPHomePeipeiTableCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPHomePeipeiTableCell"];
        //去找ta
        
            cell.tapGotoTaLeftBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel, YPHomePeipeiTableCell * _Nonnull voiceCell) {
                [YPRoomPusher pushUserInRoomByID:peipeiModel.uid];
            };
        
        cell.tapGotoTaRightBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel, YPHomePeipeiTableCell * _Nonnull voiceCell) {
            [YPRoomPusher pushUserInRoomByID:peipeiModel.uid];
        };
        
        cell.tapTilteLeftBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel, YPHomePeipeiTableCell * _Nonnull voiceCell) {
            YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
                                vc.userID = peipeiModel.uid;
                                [self.navigationController pushViewController:vc animated:YES];
        };
        cell.tapTilteRightBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel, YPHomePeipeiTableCell * _Nonnull voiceCell) {
            YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
            vc.userID = peipeiModel.uid;
                                [self.navigationController pushViewController:vc animated:YES];
        };
        
        
        //点击语音播放
           cell.tapVoiceBtnForPeiPeiBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel ,YPHomePeipeiTableCell * _Nonnull voiceCell){
               if (self.peipeiModel.isLeft) {
                   [self.voiceCell.peopleImageView stopAnimating];
                   if ([GetCore(YPMediaCore) isPlaying]) {
                       [self.voiceCell.voiceButton setImage:[UIImage imageNamed:@"home_first_peipei_play"] forState:UIControlStateNormal];
                   }
               }
               
               if (self.peipeiModel && !self.peipeiModel.isLeft){
                   [self.voiceCell.peopleImageView2 stopAnimating];
                   if ([GetCore(YPMediaCore) isPlaying]) {
                       [self.voiceCell.voiceButton2 setImage:[UIImage imageNamed:@"home_first_peipei_play"] forState:UIControlStateNormal];
                   }
               }
               
               BOOL isSameCell = NO;
               if (self.voiceCell && [self.voiceCell.filePath isEqualToString:voiceCell.filePath]) {
                   isSameCell = YES;
               }
               
                self.voiceCell = voiceCell;
               self.peipeiModel = peipeiModel;
               if (voiceCell.filePath == nil) {
                      if (peipeiModel.voiceDuration==0) {
                                      [MBProgressHUD showError:@"TA还没有录制声音，快去提醒TA吧~"];
                                     return;
                                 }
                      [MBProgressHUD showMessage:@"下载中..."];
                      [GetCore(YPFileCore) downloadVoice:peipeiModel.userVoice];
                  } else{
                      if ([GetCore(YPMediaCore) isPlaying]) {
                          if (isSameCell) {
                              [GetCore(YPMediaCore) stopPlay];
                          }else{
                              [GetCore(YPMediaCore) play:voiceCell.filePath];
                          }
                      } else {
                          [GetCore(YPMediaCore) play:voiceCell.filePath];
                      }
                  }
           };
        
        
        if (indexPath.row*2+1 == self.peipeiRooms.count) {
                   cell.roomArr = @[self.peipeiRooms[indexPath.row*2]];
               }else{
                    cell.roomArr = @[self.peipeiRooms[indexPath.row*2],self.peipeiRooms[indexPath.row*2+1]];
               }
        cell.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
        return cell;
    }
    
    
    
    
    
    
    YPHomeRoomCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPHomeRoomCell"];
    cell.tapAvatarBlock = ^(UserID uid, YPHomeRoomCell * _Nonnull voiceCell, NSInteger type) {
          YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
                     vc.userID = uid;
                     [self.navigationController pushViewController:vc animated:YES];
      };
//    //去找ta
//    cell.tapGotoTaBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel, YPHomeRoomCell * _Nonnull voiceCell) {
//        [YPRoomPusher pushUserInRoomByID:peipeiModel.uid];
//    };
    //聊聊天
    cell.tapChatBlock = ^(YPHomeMengxinModel * _Nonnull mengxinModel, YPHomeRoomCell * _Nonnull voiceCell) {
        NIMSession *session = [NIMSession session:[NSString stringWithFormat:@"%lld",mengxinModel.uid] type:0];
                  YPSessionViewController *vc = [[YPSessionViewController alloc] initWithSession:session];
                  [self.navigationController pushViewController:vc animated:YES];
    };
//    //点击语音播放
//    cell.clickVoiceBtnBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel ,YPHomeRoomCell * _Nonnull voiceCell){
//         [self.voiceCell.gifImageview stopAnimating];
//         self.voiceCell = voiceCell;
//        if (voiceCell.filePath == nil) {
//               if (peipeiModel.voiceDuration==0) {
//                               [MBProgressHUD showError:@"TA还没有录制声音，快去提醒TA吧~"];
//                              return;
//                          }
//               [MBProgressHUD showMessage:@"下载中..."];
//               [GetCore(YPFileCore) downloadVoice:peipeiModel.userVoice];
//           } else {
//               if ([GetCore(YPMediaCore) isPlaying]) {
//                   [GetCore(YPMediaCore) stopPlay];
//               } else {
//                   [GetCore(YPMediaCore) play:voiceCell.filePath];
//               }
//           }
//    };
   
 //判断按钮
    if (self.isSelectMengxinFlag) {
         cell.mengxinModel = self.mengxinRooms[indexPath.row];
    }else if (self.isSelectPeipeiFlag){
//         cell.peipeiModel = self.peipeiRooms[indexPath.row];
    }else{
//         cell.model = self.listRooms[indexPath.row];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 2) {
        
          if (self.isSelectMengxinFlag) {
//             YPHomeMengxinModel* mengxinModel = self.mengxinRooms[indexPath.row];
//               [self showAlerView:mengxinModel.uid];
         }else if (self.isSelectPeipeiFlag){
//             YPHomePeipeiModel* peipeiModel = self.peipeiRooms[indexPath.row];
//               [self showAlerView:peipeiModel.uid];
         }else{
             YPHomePageInfo *info = [self.listRooms safeObjectAtIndex:indexPath.row];
              if (info) {
                  [self showAlerView:info.uid];
              }
         }

    }
}


#pragma mark - FileCoreClient
- (void)onDownloadVoiceSuccess:(NSString *)filePath
{
    self.voiceCell.filePath = filePath;
    
    if ([GetCore(YPMediaCore) isPlaying]) {
        [GetCore(YPMediaCore) stopPlay];
    }
    
    [MBProgressHUD hideHUD];
    
    
    [GetCore(YPMediaCore) play:filePath];
}

- (void)onDownloadVoiceFailth:(NSError *)error
{
    [MBProgressHUD showError:XCHudNetError];
}

#pragma mark - MediaCoreClient
- (void) onPlayAudioBegan:(NSString *)filePath
{
    if (self.peipeiModel.isLeft) {
        [self.voiceCell.voiceButton setImage:[UIImage imageNamed:@"home_first_peipei_stop"] forState:UIControlStateNormal];
    }else{
        [self.voiceCell.voiceButton2 setImage:[UIImage imageNamed:@"home_first_peipei_stop"] forState:UIControlStateNormal];
    }
    
    if (self.voiceCell.filePath != nil && [self.voiceCell.filePath isEqualToString:filePath]) {
    }
    
    [self setPlayGif];
    
}

- (void)onPlayAudioComplete:(NSString *)filePath
{
    if (self.peipeiModel.isLeft) {
         [self.voiceCell.peopleImageView stopAnimating];
        [self.voiceCell.voiceButton setImage:[UIImage imageNamed:@"home_first_peipei_play"]  forState:UIControlStateNormal];
    }else{
        [self.voiceCell.voiceButton2 setImage:[UIImage imageNamed:@"home_first_peipei_play"]  forState:UIControlStateNormal];
        [self.voiceCell.peopleImageView2 stopAnimating];
    }
    
   
    
}

- (void)setPlayGif {
    NSMutableArray *arr = [NSMutableArray new];
    for (int i = 0; i < 2; i ++) {
        [arr addObject:[UIImage imageNamed:[NSString stringWithFormat:@"home_first_peipei_bolang%d",i+1]]];
    }
    
    if (self.peipeiModel.isLeft) {
        [self.voiceCell.peopleImageView setAnimationImages:arr];
           [self.voiceCell.peopleImageView setAnimationDuration:1];
           self.voiceCell.peopleImageView.animationRepeatCount = 0;
           [self.voiceCell.peopleImageView startAnimating];
        
    }else{
        
        [self.voiceCell.peopleImageView2 setAnimationImages:arr];
           [self.voiceCell.peopleImageView2 setAnimationDuration:1];
           self.voiceCell.peopleImageView2.animationRepeatCount = 0;
           [self.voiceCell.peopleImageView2 startAnimating];
        
    }

}

- (void)showAlerView:(UserID )uid{
    NSString *isFirst = [[NSUserDefaults standardUserDefaults]objectForKey:@"isFirstEnterRoom"];
    if (![isFirst isEqualToString:@"1"]) {
        
        @weakify(self);
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"房间使用协议" message:NSLocalizedString(@"      房间是你与别人互动的地方，官方倡导绿色健康的房间体验，请务必文明用语。严禁涉及色情，政治等不良信息，若封面、背景含低俗、引导、暴露等不良内容都将会被永久封号，对于引起不适的内容请用户及时举报，我们会迅速响应处理！\n同意即可开始使用房间功能！", nil) preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *enter = [UIAlertAction actionWithTitle:@"同意" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            @strongify(self);
            [self pushToRoom:uid];
            [[NSUserDefaults standardUserDefaults]setObject:@"1" forKey:@"isFirstEnterRoom"];
            [[NSUserDefaults standardUserDefaults]synchronize];
        }];
        UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            
        }];
        [alert addAction:cancel];
        [alert addAction:enter];
        [self presentViewController:alert animated:YES completion:nil];
    }else{
        [self pushToRoom:uid];
    }
    
}

- (void)pushToRoom:(UserID )uid
{
    //根据房间所有者id。获取房间信息
    [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
    [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomOwnerUid:uid succ:^(YPChatRoomInfo *roomInfo) {
        if (roomInfo != nil && roomInfo.title.length > 0) {
            //            [MBProgressHUD hideHUD];
            //根据房间信息开房
            [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
        }else {
            [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
        }
    } fail:^(NSString *errorMsg) {
        [MBProgressHUD showError:errorMsg];
    }];
    
}

- (void)setTableViewHeader
{
    
//    CGFloat height = XC_SCREE_W/375*250 + 16 +(XC_SCREE_W-18)/357*83;
    
//    self.headerView.frame = CGRectMake(0, 0, XC_SCREE_W, height);
    self.headerView.frame = CGRectMake(0, 0, XC_SCREE_W, (kScreenWidth>400)?144: 134);
//    self.headerView.bannerContainerView.localizationImageNamesGroup = @[placeholder_image_rectangle];
    self.headerView.bannerContainerView.delegate = self;
    
//    self.headerView.bannerContainerView.pageControlStyle = SDCycleScrollViewPageContolStyleAnimated;
//    self.headerView.bannerContainerView.pageControlAliment = SDCycleScrollViewPageContolAlimentCenter;
    self.headerView.bannerContainerView.autoScroll = YES;
    self.headerView.bannerContainerView.autoScrollTimeInterval = 3;
    self.headerView.bannerContainerView.layer.cornerRadius = 7;
    self.headerView.bannerContainerView.layer.masksToBounds = YES;
    self.headerView.controlView.hidden = YES;
    self.headerView.msgView1.hidden = YES;
    self.headerView.msgView2.hidden = YES;
    if (self.bannerInfos != nil && self.bannerInfos.count > 0) {
        NSMutableArray *array = [NSMutableArray array];
        for (YPBannerInfo *info in self.bannerInfos) {
            [array addObject:info.bannerPic];
        }
        self.headerView.bannerContainerView.imageURLStringsGroup = [array copy];
    }
    self.hotTableView.tableHeaderView = self.headerView;
    
    self.headerView.naviController = self.navigationController;
    
//    YPFirstHomeSectionView* qualityView = [[YPFirstHomeSectionView alloc] initWithFrame:CGRectMake(0, (kScreenWidth>380)?130: 120, XC_SCREE_W, 30)];
//             qualityView.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
//    qualityView.logoImageView.image = [UIImage imageNamed:@"yp_home_youzhituijian"];
//    qualityView.tipLabel.text = @"优质推荐";
//   [self.headerView addSubview:qualityView];
    
}

-(void)setupRadioFriendView{
    
    
    self.radioFriendView.frame = CGRectMake(0, -110, XC_SCREE_W, 150);
    self.radioFriendView.bannerContainerView.hidden = YES;
    self.radioFriendView.naviController = self.navigationController;
}

//MARK: - getData数据
//萌新数据
-(void)requestMengxinData{
    
     [YPHttpRequestHelper requestHomeNewUsersDataWithPage:self.mengxinPage success:^(NSArray *homeDataArray) {
                
                [self.hotTableView.mj_header endRefreshing];
                [self.hotTableView.mj_footer endRefreshing];
                
                if (self.mengxinPage == 1) {
                    [self.mengxinRooms removeAllObjects];
                }
             [self.mengxinRooms addObjectsFromArray:homeDataArray];

                [self.hotTableView reloadData];

         if (homeDataArray != nil) {
                         if (homeDataArray.count<25 ) {
                             [self.hotTableView.mj_footer endRefreshingWithNoMoreData];
                             self.isMengxinEndData = YES;
                         }else{
                             self.isMengxinEndData = NO;
                              [self.hotTableView.mj_footer resetNoMoreData];
                         }
                     }else{
                          [self.hotTableView.mj_footer endRefreshingWithNoMoreData];
                         self.isMengxinEndData = YES;
                     }
         
                [MBProgressHUD hideHUD];
                
            } failure:^(NSNumber *resCode, NSString *message) {
                if (self.listRooms.count == 0) {
                    self.hotTableView.tableFooterView = self.tipView;
                }
                [self.hotTableView.mj_header endRefreshing];
                [self.hotTableView.mj_footer endRefreshing];
                
                [MBProgressHUD hideHUD];
            }];
}

//陪陪数据
-(void)requestPeipeiData{
    
     [YPHttpRequestHelper requestHomeBestCompaniesDataWithPage:self.peipeiPage gender:[self.selectSexForPeipei integerValue] success:^(NSArray *homeDataArray) {
            
            [self.hotTableView.mj_header endRefreshing];
            [self.hotTableView.mj_footer endRefreshing];
            
            if (self.peipeiPage == 1 ) {
                [self.peipeiRooms removeAllObjects];
                [self.tempAllPeipeiArray removeAllObjects];
    //            self.bannerInfos = nil;
               
            }
         if ([self.selectSexForPeipei isEqualToString:@"1"]) {//男 1 男 2女 3全部
             for (YPHomePeipeiModel* model in homeDataArray) {
                 if (model.gender == 1) {
                     [self.peipeiRooms addObject:model];
                 }
             }
         }else if ([self.selectSexForPeipei isEqualToString:@"2"]){
             for (YPHomePeipeiModel* model in homeDataArray) {
                 if (model.gender == 2) {
                     [self.peipeiRooms addObject:model];
                 }
             }
         }else{
              [self.peipeiRooms addObjectsFromArray:homeDataArray];
         }
        
         [self.tempAllPeipeiArray addObjectsFromArray:homeDataArray];
            [self.hotTableView reloadData];
            
         if (homeDataArray != nil) {
                   if (homeDataArray.count<25 ) {
                       [self.hotTableView.mj_footer endRefreshingWithNoMoreData];
                       self.isPeipeiEndData = YES;
                   }else{
                       self.isPeipeiEndData = NO;
                        [self.hotTableView.mj_footer resetNoMoreData];
                   }
               }else{
                    [self.hotTableView.mj_footer endRefreshingWithNoMoreData];
                   self.isPeipeiEndData = YES;
               }
            
            [MBProgressHUD hideHUD];
            
//         if (self.peipeiPage == 1 ) {
//              if (self.isSelectPeipeiFlag) {
//                                self.homeSectionView.middleBtnBlock();
//                            }
//         }
         
        } failure:^(NSNumber *resCode, NSString *message) {
            if (self.listRooms.count == 0) {
                self.hotTableView.tableFooterView = self.tipView;
            }
            [self.hotTableView.mj_header endRefreshing];
            [self.hotTableView.mj_footer endRefreshing];
            
            [MBProgressHUD hideHUD];
        }];
}
- (void)requestRoomDatas
{
    [YPHttpRequestHelper requestBannerList:^(NSArray *list) {
        
        NSMutableArray *bannerArray = [NSMutableArray array];
        NSMutableArray *buffer = @[].mutableCopy;
        for (YPBannerInfo *bannerInfo in list) {
            [bannerArray addObject:bannerInfo.bannerPic];
            [buffer addObject:bannerInfo];
        }
        self.bannerInfos = buffer.copy;
        self.headerView.bannerContainerView.imageURLStringsGroup = bannerArray;
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
    
    
    [YPHttpRequestHelper requestHomeHotDataState:0 page:self.hotPage success:^(NSMutableDictionary *homeDataDictonary) {
        
        [self.hotTableView.mj_header endRefreshing];
        [self.hotTableView.mj_footer endRefreshing];
        
        if (self.hotPage == 1) {
            [self.listRooms removeAllObjects];
//            self.bannerInfos = nil;
//
//            if ([homeDataDictonary[@"banners"] count]>0) {
//                self.bannerInfos = homeDataDictonary[@"banners"];
//
//            }
        }
          NSArray* tempArr = [[NSArray alloc] init];
        if ([homeDataDictonary[@"listRoom"] count]>0) {
          
            tempArr = homeDataDictonary[@"listRoom"];
            [self.listRooms addObjectsFromArray:homeDataDictonary[@"listRoom"]];
         
            self.hotTableView.tableFooterView = nil;
        }else{
            
            if (self.listRooms.count == 0) {
                self.hotTableView.tableFooterView = self.tipView;
            }
        }
        
        if ([homeDataDictonary[@"agreeRecommendRooms"] count] > 0) {
            self.hotRooms = homeDataDictonary[@"agreeRecommendRooms"];
            
        }
        
        
        [self.hotTableView reloadData];
        
        if (tempArr != nil) {
            if (tempArr.count<25 ) {
                [self.hotTableView.mj_footer endRefreshingWithNoMoreData];
                self.isHotEndData = YES;
            }else{
                self.isHotEndData = NO;
                 [self.hotTableView.mj_footer resetNoMoreData];
            }
        }else{
             [self.hotTableView.mj_footer endRefreshingWithNoMoreData];
            self.isHotEndData = YES;
        }
        
        [self setTableViewHeader];
        
        [MBProgressHUD hideHUD];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (self.listRooms.count == 0) {
            self.hotTableView.tableFooterView = self.tipView;
        }
        [self.hotTableView.mj_header endRefreshing];
        [self.hotTableView.mj_footer endRefreshing];
        
        [MBProgressHUD hideHUD];
    }];
}

//获取悬浮入口的活动数据
- (void)getActivityData
{
    [YPHttpRequestHelper requestActivityList:3 Success:^(NSArray *arr) {
        if (arr.count>0) {
            NSMutableArray *titles = [NSMutableArray new];
            for (int i = 0; i<arr.count; i++) {
                YPAlertInfo *info = arr[i];
                [titles addObject:info.alertWinPic];
            }
            self.activityView.hidden = NO;
            self.activityArr = arr;
            self.activityView.imageURLStringsGroup = titles;
        }else{
            self.activityView.hidden = YES;
        }
        self.activityView.hidden = YES;//life-hj
    } failure:^(NSNumber *resCode, NSString *message) {
        self.activityView.hidden = YES;
    }];
}

//签到
- (void)pushToSignHome
{
    self.activityView.userInteractionEnabled = NO;
    
    __weak typeof(self)weakSelf = self;
    [YPSignnAlterView showFromHome:^{
        weakSelf.activityView.userInteractionEnabled = YES;
    }];
}

#pragma mark - SDCycleScrollViewDelegate
- (void)cycleScrollView:(YPSDCycleScrollView *)cycleScrollView didSelectItemAtIndex:(NSInteger)index {
    
    if (cycleScrollView == self.activityView) {
        if (index<self.activityArr.count) {
            YPAlertInfo *info = self.activityArr[index];
            
            if ([info.actName isEqualToString:@"XBDSign"]) {
                [self pushToSignHome];
                return;
            }
            
            [[YPPushVC shared] pushWithJson:[info yy_modelToJSONObject] withvc:self];
        }
        
        return;
    }
    
    YPBannerInfo *info = [self.bannerInfos safeObjectAtIndex:index];
    if (info.skipType == BannerInfoSkipTypeWeb) {
        YPWKWebViewController *vc = [[YPWKWebViewController alloc]init];
        NSString *url = [NSString stringWithFormat:@"%@?uid=%@",info.skipUri,[GetCore(YPAuthCoreHelp)getUid]];
        vc.url = [NSURL URLWithString:url];
        [self.navigationController pushViewController:vc animated:YES];
    }else if (info.skipType == BannerInfoSkipTypeRoom) {
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomOwnerUid:info.skipUri.userIDValue succ:^(YPChatRoomInfo *roomInfo) {
            if (roomInfo != nil) {
                [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomInfo:roomInfo];
            }else {
                [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
            }
        } fail:^(NSString *errorMsg) {
            [MBProgressHUD showError:errorMsg];
        }];
    }
}

#pragma mark - HomeCore
- (void)requestHomeOtherMenuDataSuccess:(NSArray *)data {
    NSMutableArray *dataArr = [NSMutableArray arrayWithArray:data];
    
    if (self.tagsListArr.count == 1) {
        [self.tagsListArr addObjectsFromArray:dataArr];
        self.topTagView.roomTagList = self.tagsListArr;
        [self setTagChildVC];
    }
}

- (void)reachabilityNetStateDidChange:(ReachabilityNetState)currentNetState {
    
    if (self.tagsListArr.count == 2) {
        [GetCore(YPHomeCore) requestHomeOtherMenuData];
    }
}

- (void)onLoginSuccess
{
//    [YPSignnAlterView show];
    
    [YPYouthAlterView updateYouthData];
    
    if (self.listRooms.count == 0) {
        [self requestRoomDatas];
        
    }
    
    if (self.tagsListArr.count == 2) {
        [GetCore(YPHomeCore) requestHomeOtherMenuData];
    }
    
    [self getActivityData];
    
    NSMutableArray *giftArr = [GetCore(YPGiftCore) getNormalRoomGift];
    if (giftArr.count == 0) {
        [GetCore(YPGiftCore) requestGiftList];
    }
    
}

//socket登陆成功后拉取公聊消息
- (void)onSocLoginSuccess
{
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self getIMRoomMsgData];
    });
}

- (void)setTagChildVC
{
    if (self.tagsListArr.count>1) {
        self.bgScrollView.contentSize = CGSizeMake(XC_SCREE_W*self.tagsListArr.count, 0);
    }
    
    for (int i = 1; i<self.tagsListArr.count; i++) {
        YPHomeTag *tagModel = self.tagsListArr[i];
        YPHomeCommonViewController *vc = [[YPHomeCommonViewController alloc] init];
        vc.type = tagModel.id;
     
        
        [self addChildViewController:vc];
        [self.bgScrollView addSubview:vc.view];
        vc.view.frame = CGRectMake(XC_SCREE_W*i, HJHomeTopViewHeight, XC_SCREE_W, HJHomeTableViewHeight-HJHomeTopViewHeight);
        
    }
    
    


}

#pragma mark - private method
- (void)searchBtnAction
{
    YPSearchRoomController *vc = YPHomeStoryBoard(@"YPSearchRoomController");
    vc.mainNavicationController = self.navigationController;
    UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:vc];
    if (@available(iOS 13.0, *)) {
        
            nav.modalPresentationStyle = UIModalPresentationFullScreen;
       }
    [self.navigationController presentViewController:nav animated:YES completion:nil];
//    [self.navigationController presentViewController:[[UINavigationController alloc] initWithRootViewController:vc] animated:YES completion:nil];
}

- (void)rankBtnAction
{
    [[YPRoomViewControllerCenter defaultCenter] openRoonWithType:RoomType_Game];
//    NSString *isFirst = [[NSUserDefaults standardUserDefaults]objectForKey:@"isPaiHangbang"];
//    if (![isFirst isEqualToString:@"1"]) {
//
//       UIViewController *jumpVC = YPHomeStoryBoard(@"YPRankListVC");
//       [self.navigationController pushViewController:jumpVC animated:YES];
//    }else{
//        UIViewController *jumpVC = YPHomeStoryBoard(@"YPRankListVC");
//        [self.navigationController pushViewController:jumpVC animated:YES];
//    }
}

- (void)setStytle:(BOOL)isClearStytle
{
    //更新tagview 样式
    YPFirstHomeTagStytleModel *model = [[YPFirstHomeTagStytleModel alloc] init];
//    model.isPictureTitleColor =YES;
    model.selFont = JXFontPingFangSCMedium(22);
    model.selColor = isClearStytle?UIColorHex(FFFFFF):UIColorHex(FFFFFF);
    
    model.normalFont = JXFontPingFangSCRegular(14);
    model.normalColor = [UIColor colorWithHexString:@"#FFFFFF"];
    model.selColor = [UIColor colorWithHexString:@"#FFFFFF"];
    model.lineHeight = 0;
    model.verticalAlignment = 1;
    self.topTagView.stytle = model;
    
    isClearStytle = NO;
//    [_searchBtn setImage:[UIImage imageNamed:isClearStytle?@"yp_home_search":@"yp_home_search_gray"] forState:UIControlStateNormal];
//    [_searchBtn setTitleColor:isClearStytle?[UIColor colorWithWhite:1 alpha:0.5]:UIColorHex(999999) forState:UIControlStateNormal];
//    _searchBtn.backgroundColor = isClearStytle?[UIColor colorWithWhite:1 alpha:0.3]:UIColorHex(F2F2F2);
    
    self.topView.backgroundColor = isClearStytle?[UIColor clearColor]:[UIColor clearColor];
    
//    if (isClearStytle) {
//        [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
//    }else{
//        [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
//    }
//    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
}

- (void)getIMRoomMsgData
{
    NSString *roomId = [self getRoomid];
    __weak typeof(self)weakSelf = self;
    [YPIMRequestManager enterPublicWithRoomId:roomId success:^(NSArray * _Nonnull messages) {
        NSMutableArray *tmpArr = [[NSMutableArray alloc] initWithArray:messages];
        if (messages.count>20) {
            [tmpArr removeObjectsInRange:NSMakeRange(20, messages.count-20)];
        }
        
//        weakSelf.headerView.imRoomMsgArr = tmpArr;
        weakSelf.radioFriendView.imRoomMsgArr = tmpArr;
        
        [[NIMSDK sharedSDK].chatroomManager exitChatroom:roomId completion:nil];
        
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {

    }];
}

- (NSString *)getRoomid {
    if (GetCore(YPVersionCoreHelp).isReleaseEnv) {
        if (GetCore(YPVersionCoreHelp).checkIn) return JXIMPublicDebugAuditRoomId;
        
        return JXIMPublicReleaseNormalRoomId;
    } else {
        if (GetCore(YPVersionCoreHelp).checkIn) return JXIMPublicDebugAuditRoomId;
        return JXIMPublicDebugNormalRoomId;
    }
}
//MARK: - 交友大厅 排行榜 入口
- (void)tapGesLeftClick:(UITapGestureRecognizer *)tapGes {
    YPNotiFriendVC *viewController = [[YPNotiFriendVC alloc] init];
    [viewController updateData];
    [self.navigationController pushViewController:viewController animated:YES];
}
- (void)tapGesRightClick:(UITapGestureRecognizer *)tapGes {
    NSString *isFirst = [[NSUserDefaults standardUserDefaults]objectForKey:@"isPaiHangbang"];
    if (![isFirst isEqualToString:@"1"]) {
        
       UIViewController *jumpVC = YPHomeStoryBoard(@"YPRankListVC");
       [self.navigationController pushViewController:jumpVC animated:YES];
    }else{
        UIViewController *jumpVC = YPHomeStoryBoard(@"YPRankListVC");
        [self.navigationController pushViewController:jumpVC animated:YES];
    }
}

#pragma mark - setter/getter
- (YPHomeTagView *)topTagView
{
    if (!_topTagView) {
        _topTagView = [[YPHomeTagView alloc] initWithFrame:CGRectZero];
//        _topTagView.backgroundColor = UIColorHex(FFFFFF);
        __weak typeof(self)weakSelf = self;
       
//        YPFirstHomeTagStytleModel *model = [[YPFirstHomeTagStytleModel alloc] init];
//        model.selFont = JXFontPingFangSCMedium(20);
//        model.selColor = UIColorHex(FFFFFF);
//
//        model.normalFont = JXFontPingFangSCRegular(13);
//        model.normalColor = [UIColor colorWithWhite:1.0 alpha:0.7];
//        model.lineHeight = 0;
//        model.verticalAlignment = 1;
        
        //        model.lineWidth = 18;
        //        model.lineHeight = 0;
        //        model.lineColor = UIColorHex(FF57B3);
        
        
        YPFirstHomeTagStytleModel *model = [[YPFirstHomeTagStytleModel alloc] init];
          model.selFont = JXFontPingFangSCMedium(22);
          model.normalFont = JXFontPingFangSCRegular(14);
        model.normalColor = [UIColor colorWithHexString:@"#FFFFFF"];//RGBACOLOR(0, 0, 0, 0.7);
          model.lineHeight = 0;
          model.verticalAlignment = 1;
//         model.selColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_home_peopleBg"]];
          model.selColor = [UIColor colorWithHexString:@"#FFFFFF"];
        
        
          self.topTagView.stytle = model;
        
        _topTagView.selItemCallBack = ^(NSInteger item) {
            
            [weakSelf.bgScrollView setContentOffset:CGPointMake(item*XC_SCREE_W, 0) animated:YES];
            
            if (item == 1) {
                CGFloat offsetY = weakSelf.hotTableView.contentOffset.y;
                [weakSelf updateNavBar:offsetY];
            }else{
                [weakSelf setStytle:NO];
            }
            
            //            [weakSelf setStytle:item==0?YES:NO];
            
            
        };
        [self.topView addSubview:_topTagView];
    }
    return _topTagView;
}



- (UIButton *)searchBtn
{
    if (!_searchBtn) {
        _searchBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_searchBtn addTarget:self action:@selector(searchBtnAction) forControlEvents:UIControlEventTouchUpInside];
         [_searchBtn setImage:[UIImage imageNamed:@"yp_home_search_icon"] forState:UIControlStateNormal];
//        [_searchBtn setImage:[UIImage imageNamed:@"yp_home_search_gray"] forState:UIControlStateNormal];
//        [_searchBtn setTitle:@" 输入ID/昵称/房间号" forState:UIControlStateNormal];
//        _searchBtn.titleLabel.font = JXFontPingFangSCRegular(12);
//        [_searchBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
//        _searchBtn.backgroundColor = UIColorHex(F2F2F2);
//        _searchBtn.layer.cornerRadius = 13;
//        _searchBtn.layer.masksToBounds = YES;
        [self.topView addSubview:_searchBtn];
    }
    return _searchBtn;
}

- (UIButton *)rankBtn
{
    if (!_rankBtn) {
        _rankBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_rankBtn addTarget:self action:@selector(rankBtnAction) forControlEvents:UIControlEventTouchUpInside];
//        [_rankBtn setImage:[UIImage imageNamed:@"yp_home_rank"] forState:UIControlStateNormal];
        [_rankBtn setImage:[UIImage imageNamed:@"yp_home_rankBg"] forState:UIControlStateNormal];
        [self.topView addSubview:_rankBtn];
    }
    return _rankBtn;
}



- (UIView *)topView
{
    if (!_topView) {
        _topView = [UIView new];
        _topView.frame = CGRectZero;
        _topView.backgroundColor = [UIColor clearColor];
        
        [self.view addSubview:_topView];
    }
    return _topView;
}
- (UIImageView *)topImageView
{
    if (!_topImageView) {
        _topImageView = [UIImageView new];
        _topImageView.frame = CGRectMake(0, 0, kScreenWidth, 238*(kScreenWidth/375));
        _topImageView.image = [UIImage imageNamed:@"yp_img_upbg"];
        
        
    }
    return _topImageView;
}


- (UIScrollView *)bgScrollView
{
    if (!_bgScrollView) {
        _bgScrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, HJHomeTableViewHeight)];
//        _bgScrollView.backgroundColor = UIColorHex(FAFAFA);
        _bgScrollView.backgroundColor = [UIColor clearColor];
        _bgScrollView.showsVerticalScrollIndicator = NO;
        _bgScrollView.showsHorizontalScrollIndicator = NO;
        _bgScrollView.pagingEnabled = YES;
        _bgScrollView.delegate = self;
        [self.view insertSubview:_bgScrollView belowSubview:self.topView];
//        [self.view insertSubview:_bgScrollView belowSubview:self.topImageView];
    }
    return _bgScrollView;
}

- (UITableView *)hotTableView
{
    if (!_hotTableView) {
        _hotTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, iPhoneX?89:74, XC_SCREE_W, HJHomeTableViewHeight-130+ (iPhoneX?24:44))];
        _hotTableView.backgroundColor = [UIColor clearColor];
        _hotTableView.delegate = self;
        _hotTableView.dataSource = self;
        _hotTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        _hotTableView.showsVerticalScrollIndicator = NO;
        [_hotTableView registerClass:[YPHomeRecommedTableCell class] forCellReuseIdentifier:@"YPHomeRecommedTableCell"];
         [_hotTableView registerClass:[YPHomeHotTableCell class] forCellReuseIdentifier:@"YPHomeHotTableCell"];
        
         [_hotTableView registerClass:[YPHomePeipeiTableCell class] forCellReuseIdentifier:@"YPHomePeipeiTableCell"];
        
        
        [_hotTableView registerNib:[UINib nibWithNibName:@"YPHomeRoomCell" bundle:nil] forCellReuseIdentifier:@"YPHomeRoomCell"];
        
        [self.bgScrollView addSubview:_hotTableView];
    }
    return _hotTableView;
}


- (YPHomeHeaderView *)headerView
{
    if (!_headerView) {
        _headerView = [[NSBundle mainBundle] loadNibNamed:@"YPHomeHeaderView" owner:self options:nil][0];
        
    }
    return _headerView;
}

- (YPHomeHeaderView *)radioFriendView
{
    if (!_radioFriendView) {
        _radioFriendView = [[NSBundle mainBundle] loadNibNamed:@"YPHomeHeaderView" owner:self options:nil][0];
        
        
    }
    return _radioFriendView;
}





- (YPEmptyView *)tipView
{
    if (!_tipView) {
        _tipView = [[YPEmptyView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 428)];
        [_tipView setTitle:@"列表里没有任何内容呢" image:@"blank"];
    }
    return _tipView;
}

- (YPSDCycleScrollView *)activityView
{
    if (!_activityView) {
        _activityView = [[YPSDCycleScrollView alloc] init];
        _activityView.delegate = self;
        _activityView.pageControlStyle = SDCycleScrollViewPageContolStyleAnimated;
        _activityView.pageControlAliment = SDCycleScrollViewPageContolAlimentCenter;
        _activityView.backgroundColor = [UIColor clearColor];
        _activityView.bannerImageViewContentMode = UIViewContentModeScaleToFill;
        _activityView.showPageControl = NO;
        [self.view addSubview:_activityView];

    }
    return _activityView;
}
-(YPFirstHomeSectionView*)homeSectionView{
    if (!_homeSectionView) {
       
         _homeSectionView = [[YPFirstHomeSectionView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, 50)];
          _homeSectionView.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
    }
    return _homeSectionView;
    
}

-(UIView*)leftView{
    if (!_leftView) {
        _leftView = [[UIView alloc] init];
        _leftView.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
    }
    return _leftView;
    
}
-(UIView*)rightView{
    if (!_rightView) {
        _rightView = [[UIView alloc] init];
        _rightView.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
    }
    return _rightView;
    
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (UIView *)getFirstViewForCellWithTitle:(NSString*)title des:(NSString*)des imgName:(NSString*)imgName{
   
    UIView*  firstViewForCell = [[UIView alloc] init];
    if ([title isEqualToString:@"交友广场"]) {
        firstViewForCell.frame = CGRectMake(9, 16, kScreenWidth/2-10, (kScreenWidth/2-10)/184.0*80);
    }else{
        firstViewForCell.frame = CGRectMake(kScreenWidth/2+7, 14, kScreenWidth/2-10, (kScreenWidth/2-10)/184.0*80);
    }

    UILabel* titleLabel = [[UILabel alloc] init];
    UILabel* desLabel = [[UILabel alloc] init];
    titleLabel.frame = CGRectMake(22, 14, 58, 20);
    desLabel.frame = CGRectMake(22, CGRectGetMaxY(titleLabel.frame)+4, 58, 20);
    titleLabel.font = [UIFont systemFontOfSize:13];
    titleLabel.textColor = [UIColor colorWithHexString:@"#333333"];
    titleLabel.text = title;
    desLabel.text = des;
    desLabel.font = [UIFont systemFontOfSize:9];
    desLabel.textColor = [UIColor colorWithHexString:@"#999999"];
    
    UIImageView* imgViewbg = [[UIImageView alloc] init];
    imgViewbg.frame = CGRectMake(0, 0, kScreenWidth/2-10, (kScreenWidth/2-10)/184.0*80);
    imgViewbg.userInteractionEnabled = YES;
    imgViewbg.image = [UIImage imageNamed:imgName];
    imgViewbg.contentMode =UIViewContentModeScaleToFill;
    [firstViewForCell addSubview:imgViewbg];
    [firstViewForCell addSubview:titleLabel];
    [firstViewForCell addSubview:desLabel];

    return firstViewForCell;
    
    
    
}

@end
