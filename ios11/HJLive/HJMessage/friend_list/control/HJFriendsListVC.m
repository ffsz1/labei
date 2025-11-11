//
//  HJFriendsListVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFriendsListVC.h"
#import "HJSessionViewController.h"
#import "HJMySpaceVC.h"

#import "HJFriendsListCell.h"

#import "HJImFriendCoreClient.h"
#import "HJImLoginCoreClient.h"
#import "HJImFriendCore.h"

#import "UIView+XCToast.h"
#import "HJEmptyView.h"


@interface HJFriendsListVC ()

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic,strong) NSMutableArray *fansArr;

@property (nonatomic,strong) HJEmptyView *tipView;


@end

@implementation HJFriendsListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    AddCoreClient(HJImFriendCoreClient, self);
    AddCoreClient(HJImLoginCoreClient, self);
    [GetCore(HJImFriendCore) updateMyFriends];
    
    self.title = @"我的好友";
    
    [self getData];
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
    HJFriendsListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJFriendsListCell" forIndexPath:indexPath];
    
    if (indexPath.row<self.fansArr.count) {
        UserInfo *info = self.fansArr[indexPath.row];
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
    HJSessionViewController *vc = [[HJSessionViewController alloc] initWithSession:session];
    [self.navigationController pushViewController:vc animated:YES];
    
}

- (void)onFriendChanged {
    [self getData];
}

#pragma mark - ImLoginCoreClient
- (void)onImLogoutSuccess {
    [self.fansArr removeAllObjects];
}

- (void)onImSyncSuccess {
    [self getData];
}


- (void)getData
{
    self.fansArr = [[GetCore(HJImFriendCore)getMyFriends] mutableCopy];
    if (self.fansArr.count > 0) {
        self.tableView.tableFooterView = nil;
    }else {
        self.tableView.tableFooterView = self.tipView;
    }
    [self.tableView reloadData];
}


- (HJEmptyView *)tipView
{
    if (!_tipView) {
        _tipView = [[HJEmptyView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 428)];
        [_tipView setTitle:@"你还没有小熊语音好友喔" image:@"blank"];
    }
    return _tipView;
}


@end
