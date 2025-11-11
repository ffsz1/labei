//
//  YPFansListViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFansListViewController.h"
#import "YPSessionViewController.h"
#import "YPMySpaceVC.h"

#import "YPFanListCell.h"

#import "HJImFriendCoreClient.h"
#import "HJImLoginCoreClient.h"
#import "YPImFriendCore.h"

#import "YPHttpRequestHelper+Praise.h"
#import "UITableView+MJRefresh.h"
#import "UIView+XCToast.h"
#import "HJPraiseCoreClient.h"
#import "YPEmptyView.h"

@interface YPFansListViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic,strong) NSMutableArray *fansArr;

@property (nonatomic,strong) YPEmptyView *tipView;


@end

@implementation YPFansListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"我的粉丝";
    
    __weak typeof(self)weakSelf = self;
    self.tableView.headerBlock = ^{
        [weakSelf getData];
    };
    
    self.tableView.footerBlock = ^{
        [weakSelf getData];
    };
    
    AddCoreClient(HJPraiseCoreClient, self);

}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self getData];
    
    
    
    if (self.isFromMessageHome) {
        YPLightStatusBar
        [self.navigationController setNavigationBarHidden:YES animated:YES];
    }else{
        YPBlackStatusBar
        [self.navigationController setNavigationBarHidden:NO animated:YES];
    }
    
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

#pragma mark <UITableViewDelegate,UITableViewDataSource>
- (NSInteger)tableView:(UITableView *)tableView
 numberOfRowsInSection:(NSInteger)section
{
    return self.fansArr.count;
    //    return 100;
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    YPFanListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPFanListCell" forIndexPath:indexPath];
    
    
    if (indexPath.row<self.fansArr.count) {
        YPAttention *info = self.fansArr[indexPath.row];
        
        cell.info = info;
        
    }
    
    
    return cell;
}



#pragma mark - UITableViewDelegate
- (void)tableView:(UITableView *)tableView
didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UserInfo *info = self.fansArr[indexPath.row];
    NIMSession *session = [NIMSession session:[NSString stringWithFormat:@"%lld",info.uid] type:0];
    YPSessionViewController *vc = [[YPSessionViewController alloc] initWithSession:session];
    [self.navigationController pushViewController:vc animated:YES];
}


- (void)getData
{
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    [YPHttpRequestHelper getFansListWithUid:uid page:self.tableView.page success:^(NSArray *userInfos) {
        
        [self.tableView.mj_header endRefreshing];
        [self.tableView.mj_footer endRefreshing];
        
        if (self.tableView.page==0) {
            self.fansArr = [[NSMutableArray alloc] init];
        }
        
        [self.fansArr addObjectsFromArray:userInfos];
        
        [self.tableView reloadData];
        
        if (userInfos.count<25) {
            [self.tableView.mj_footer endRefreshingWithNoMoreData];
        }
        
        if (self.fansArr.count == 0) {
            self.tableView.tableFooterView = self.tipView;
        }else{
            self.tableView.tableFooterView = nil;
        }
        
        if (userInfos.count<25) {
            [self.tableView.mj_footer endRefreshingWithNoMoreData];
        }
        
        
    } failure:^(NSNumber *resCode, NSString *message) {
        [self.tableView.mj_header endRefreshing];
        [self.tableView.mj_footer endRefreshing];
    }];
}

- (void)onPraiseSuccess:(UserID)uid {
    [MBProgressHUD showSuccess:NSLocalizedString(XCFansPraiseSuccessTip, nil)];
    [self.tableView reloadData];
}


- (YPEmptyView *)tipView
{
    if (!_tipView) {
        _tipView = [[YPEmptyView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 428)];
        [_tipView setTitle:@"你还没有关注任何喜翻鸭好友喔" image:@"blank"];
    }
    return _tipView;
}

@end
