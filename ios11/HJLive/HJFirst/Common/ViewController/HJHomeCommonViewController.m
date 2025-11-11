//
//  HJHomeCommonViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHomeCommonViewController.h"
#import "HJRoomViewControllerCenter.h"

#import "HJHomeRoomCell.h"
#import "HJHomeCommonTopCell.h"

#import "HJHomePageInfo.h"
#import "HJHomeHeaderView.h"
#import "HJBannerInfo.h"
#import "HJHttpRequestHelper+Home.h"

#import "UITableView+MJRefresh.h"
#import "UIView+XCToast.h"
#import "HJWKWebViewController.h"
#import "HJEmptyView.h"

@interface HJHomeCommonViewController ()<UITableViewDelegate,UITableViewDataSource,SDCycleScrollViewDelegate>

@property (nonatomic, strong) NSMutableArray *roomArr;//其他房间列表
@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic,strong) HJEmptyView *tipView;
@property (nonatomic, strong) SDCycleScrollView *banaCommonView;//banaer
@end

@implementation HJHomeCommonViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor clearColor];
//    self.tableView.tableHeaderView = self.banaCommonView;
    __weak typeof(self)weakSelf = self;
    [self.tableView setHeaderBlock:^{
        [weakSelf getCommonListData];
    }];
    
    [self.tableView setFooterBlock:^{
        [weakSelf getCommonListData];
    }];
    
    [self getCommonListData];
    
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.mas_equalTo(self.view);
        make.height.equalTo(@(kScreenHeight-(iPhoneX?180:120)));
    }];
//    [self.banaCommonView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.size.mas_equalTo(CGSizeMake(kScreenWidth-26, 120));
//        make.left.mas_equalTo(self.view.mas_left).offset(13);
//        make.right.mas_equalTo(self.view.mas_right).offset(-13);
//    }];
//      [self setTableViewHeader];
    [self getBanaerData];
}
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
#pragma mark - UITableViewDelegate,UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
//    if (self.roomArr.count>3 && self.roomArr.count>0) {
//        return self.roomArr.count - 2;
//    }
    return self.roomArr.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //小于3全部显示正常样式
//    if (self.roomArr.count>3) {
//        return indexPath.row> 0 ? 114 : JXHomeCommonTopCellHeight;
//
//    }
    
    return 100*(kScreenWidth/375.0);
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
//    if (self.roomArr.count>3 && indexPath.row==0) {
//        HJHomePageInfo *info = [self.roomArr safeObjectAtIndex:indexPath.row];
//        HJHomeCommonTopCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJHomeCommonTopCell" forIndexPath:indexPath];
//        cell.selectionStyle = UITableViewCellSelectionStyleNone;
//        cell.roomArr = self.roomArr;
//        return cell;
//    }

    HJHomeRoomCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJHomeRoomCell"];

    //如果超过2，顶部样式有2个房间，会导致数组差
//    int isShowTop = self.roomArr.count>3?2:0;
//    cell.model = self.roomArr[indexPath.row+isShowTop];
     cell.model = self.roomArr[indexPath.row];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
//    if (self.roomArr.count>3 &&indexPath.row==0) {
//        return;
//    }

//    int isShowTop = self.roomArr.count>3?2:0;
    
    HJHomePageInfo *info = [self.roomArr safeObjectAtIndex:indexPath.row];
    [self showAlerView:info];
}


#pragma mark - private method
//- (void)setTableViewHeader
//{
//
//    CGFloat height = XC_SCREE_W/375*250 + 16 +(XC_SCREE_W-18)/357*83;
//
////    self.headerView.frame = CGRectMake(0, 0, XC_SCREE_W, height);
//    self.headerView.frame = CGRectMake(0, 0, XC_SCREE_W, 150);
//    self.headerView.bannerContainerView.localizationImageNamesGroup = @[placeholder_image_rectangle];
//    self.headerView.bannerContainerView.delegate = self;
//
////    self.headerView.bannerContainerView.pageControlStyle = SDCycleScrollViewPageContolStyleAnimated;
////    self.headerView.bannerContainerView.pageControlAliment = SDCycleScrollViewPageContolAlimentCenter;
//    self.headerView.bannerContainerView.autoScroll = YES;
//    self.headerView.bannerContainerView.autoScrollTimeInterval = 3;
//    self.headerView.bannerContainerView.layer.cornerRadius = 7;
//    self.headerView.bannerContainerView.layer.masksToBounds = YES;
//    self.headerView.controlView.hidden = YES;
//    self.headerView.msgView1.hidden = YES;
//    self.headerView.msgView2.hidden = YES;
//    if (self.bannerInfos != nil && self.bannerInfos.count > 0) {
//        NSMutableArray *array = [NSMutableArray array];
//        for (HJBannerInfo *info in self.bannerInfos) {
//            [array addObject:info.bannerPic];
//        }
//        self.headerView.bannerContainerView.imageURLStringsGroup = [array copy];
//    }
//    self.tableView.tableHeaderView = self.headerView;
//
//    self.headerView.naviController = self.navigationController;
//
//    self.headerView.backgroundColor = [UIColor redColor];
//
//
//
//}



- (void)showAlerView:(HJHomePageInfo *)info{
    NSString *isFirst = [[NSUserDefaults standardUserDefaults]objectForKey:@"isFirstEnterRoom"];
    if (![isFirst isEqualToString:@"1"]) {
        
        @weakify(self);
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"房间使用协议" message:NSLocalizedString(@"      房间是你与别人互动的地方，官方倡导绿色健康的房间体验，请务必文明用语。严禁涉及色情，政治等不良信息，若封面、背景含低俗、引导、暴露等不良内容都将会被永久封号，对于引起不适的内容请用户及时举报，我们会迅速响应处理！\n同意即可开始使用房间功能！", nil) preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *enter = [UIAlertAction actionWithTitle:@"同意" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            @strongify(self);
            [self pushToRoom:info];
            [[NSUserDefaults standardUserDefaults]setObject:@"1" forKey:@"isFirstEnterRoom"];
            [[NSUserDefaults standardUserDefaults]synchronize];
        }];
        UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            
        }];
        [alert addAction:cancel];
        [alert addAction:enter];
        [self presentViewController:alert animated:YES completion:nil];
    }else{
        [self pushToRoom:info];
    }
    
}

- (void)pushToRoom:(HJHomePageInfo *)info
{
    //根据房间所有者id。获取房间信息
    [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
    [[HJRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomOwnerUid:info.uid succ:^(ChatRoomInfo *roomInfo) {
        if (roomInfo != nil && roomInfo.title.length > 0) {
            //            [MBProgressHUD hideHUD];
            //根据房间信息开房
            [[HJRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
        }else {
            [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
        }
    } fail:^(NSString *errorMsg) {
        [MBProgressHUD showError:errorMsg];
    }];
    
}



#pragma mark - http method
- (void)getCommonListData
{
    
    [HJHttpRequestHelper requestHomeCommonData:self.type state:0 page:self.tableView.page+1 success:^(NSArray *list,int type) {
        
        [self.tableView.mj_header endRefreshing];
        [self.tableView.mj_footer endRefreshing];
        
        if (self.tableView.page == 0) {
            self.roomArr = [NSMutableArray array];
        }
        
        [self.roomArr addObjectsFromArray:list];
        
        [self.tableView reloadData];
        
        [MBProgressHUD hideHUD];
        
        
        if (self.roomArr.count == 0) {
            self.tableView.tableFooterView = self.tipView;
        }else{
            self.tableView.tableFooterView = nil;
        }
        
        if (list.count<25) {
            [self.tableView.mj_footer endRefreshingWithNoMoreData];
        }        
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (self.roomArr.count == 0) {
            self.tableView.tableFooterView = self.tipView;
        }

        [self.tableView.mj_header endRefreshing];
        [self.tableView.mj_footer endRefreshing];
        [self.tableView.mj_footer endRefreshingWithNoMoreData];

        [MBProgressHUD hideHUD];
        
    }];
}



#pragma mark - setter/getter
- (UITableView *)tableView
{
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.backgroundColor = [UIColor clearColor];
        [_tableView registerNib:[UINib nibWithNibName:@"HJHomeRoomCell" bundle:nil] forCellReuseIdentifier:@"HJHomeRoomCell"];
        [_tableView registerClass:[HJHomeCommonTopCell class] forCellReuseIdentifier:@"HJHomeCommonTopCell"];
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        _tableView.showsVerticalScrollIndicator = NO;
        [self.view addSubview:_tableView];
    }
    return _tableView;
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
          
        
        

    }
    return _banaCommonView;
}
- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    self.scrollCallback(scrollView);
}
#pragma mark - JXPagingViewListViewDelegate

- (UIScrollView *)listScrollView {
    return self.tableView;
}

- (void)listViewDidScrollCallback:(void (^)(UIScrollView *))callback {
    self.scrollCallback = callback;
}

- (UIView *)listView {
    return self.view;
}

@end

