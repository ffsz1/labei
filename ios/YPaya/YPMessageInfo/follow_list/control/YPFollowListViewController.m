//
//  YPFollowListViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFollowListViewController.h"
#import "YPMySpaceVC.h"

#import "YPFollowListCell.h"

#import "YPAttention.h"

#import "YPHttpRequestHelper+Praise.h"
#import "UITableView+MJRefresh.h"
#import "UIView+XCToast.h"
#import "YPEmptyView.h"

#import "YPSessionViewController.h"


@interface YPFollowListViewController ()

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic, strong) NSMutableArray<YPAttention *> *attentionList;

@property (nonatomic,strong) YPEmptyView *tipView;


@end

@implementation YPFollowListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"我的关注";
    
    __weak typeof(self)weakSelf = self;
    self.tableView.headerBlock = ^{
        [weakSelf getData];
    };
    
    self.tableView.footerBlock = ^{
        [weakSelf getData];
    };
     [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(jumpPrivateChatPageAction:) name:@"jumpPrivateChatPageNotification" object:nil];
}
- (void)jumpPrivateChatPageAction:(NSNotification *)noti {
   
    if (noti) {
        if (noti.object) {
            UserID uid = [noti.object longLongValue];
            NIMSession *session = [NIMSession session:[NSString stringWithFormat:@"%lld",uid] type:0];
                      YPSessionViewController *vc = [[YPSessionViewController alloc] initWithSession:session];
                      [self.navigationController pushViewController:vc animated:YES];
        }
    }
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

#pragma mark - Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return self.attentionList.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    YPFollowListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPFollowListCell" forIndexPath:indexPath];
    
    if (indexPath.row<self.attentionList.count) {
        
        YPAttention *attention = [self.attentionList safeObjectAtIndex:indexPath.row];

        cell.info = attention;
    }
    return cell;
}


#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    UserInfo *info = [self.attentionList safeObjectAtIndex:indexPath.row];
    
    NIMSession *session = [NIMSession session:[NSString stringWithFormat:@"%lld",info.uid] type:0];
    YPSessionViewController *vc = [[YPSessionViewController alloc] initWithSession:session];
    [self.navigationController pushViewController:vc animated:YES];
    
    
}


- (void)getData
{
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    [YPHttpRequestHelper requestAttentionList:uid state:0 page:self.tableView.page success:^(NSArray *userInfos) {
        
        [self.tableView.mj_header endRefreshing];
        [self.tableView.mj_footer endRefreshing];
        
        if (self.tableView.page == 0) {
            self.attentionList = [[NSMutableArray alloc] init];
        }
        
        [self.attentionList addObjectsFromArray:userInfos];
        
        [self.tableView reloadData];
        
        
        
        if (self.attentionList.count == 0) {
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

- (YPEmptyView *)tipView
{
    if (!_tipView) {
        _tipView = [[YPEmptyView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 428)];
        [_tipView setTitle:@"你还没有关注任何喜翻鸭好友喔" image:@"blank"];
    }
    return _tipView;
}

@end
