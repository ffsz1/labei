//
//  HJBlackListViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBlackListViewController.h"
#import "HJMySpaceVC.h"

#import "HJBlackListCell.h"
#import "HJEmptyView.h"

#import "Attention.h"

#import "UITableView+MJRefresh.h"
#import "UIView+XCToast.h"
#import "HJImFriendCore.h"
#import "HJImFriendCoreClient.h"
#import <NIMSDK/NIMSDK.h>

@interface HJBlackListViewController ()<HJImFriendCoreClient>

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic,strong) NSMutableArray *fansArr;

@property (nonatomic,strong) HJEmptyView *tipView;


@end

@implementation HJBlackListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"黑名单";
    
    AddCoreClient(HJImFriendCoreClient, self);
    
//    __weak typeof(self)weakSelf = self;
//    self.tableView.headerBlock = ^{
//        [weakSelf getData];
//    };
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self getData];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

#pragma mark <UITableViewDelegate,UITableViewDataSource>
- (NSInteger)tableView:(UITableView *)tableView
 numberOfRowsInSection:(NSInteger)section
{
    return self.fansArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    HJBlackListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJBlackListCell" forIndexPath:indexPath];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    if (indexPath.row<self.fansArr.count) {
        UserInfo *info = self.fansArr[indexPath.row];
        cell.info = info;
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView
didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UserInfo *info = self.fansArr[indexPath.row];
    
    HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
    vc.userID = info.uid;
    [self.navigationController pushViewController:vc animated:YES];
    
    
}


#pragma mark - UITableViewDelegate

- (NSArray<UITableViewRowAction *> *)tableView:(UITableView *)tableView editActionsForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewRowAction *deleteAction = [UITableViewRowAction rowActionWithStyle:UITableViewRowActionStyleDestructive title:NSLocalizedString(XCBlackDelete, nil) handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
        [MBProgressHUD showMessage:@""];
        UserInfo *info = self.fansArr[indexPath.row];
        [GetCore(HJImFriendCore) removeFromBlackList:[NSString stringWithFormat:@"%lld",info.uid]];
        //        [self.items removeObjectAtIndex:indexPath.row];
        
    }];
    return @[deleteAction];
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}


- (void)getData
{
    
    
    NSArray *blackList = [[NIMSDK sharedSDK].userManager myBlackList];
    if (blackList.count > 0) {
        
        NSMutableArray *idsArr = [[NSMutableArray alloc] init];
        
        for (NIMUser *user in blackList) {
            NSString *uid = user.userId;
            [idsArr addObject:uid];
        }
        
        
        __weak typeof(self)weakSelf = self;
        [GetCore(HJUserCoreHelp)getUserInfos:idsArr refresh:YES success:^(NSArray *infoArr) {
            
            weakSelf.fansArr = [NSMutableArray array];

            [weakSelf.fansArr addObjectsFromArray:infoArr];
            if (weakSelf.fansArr.count == 0) {
                self.tableView.tableFooterView = self.tipView;
            }else{
                self.tableView.tableFooterView = nil;
            }
            [weakSelf.tableView reloadData];
        }];
    }else{
        self.fansArr = [NSMutableArray array];
        [self.tableView reloadData];
        self.tableView.tableFooterView = self.tipView;
    }
    

    
}

- (void)onRemoveFromBlackListSuccess {
    [MBProgressHUD hideHUD];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self getData];
    });
}

- (void)onRemoveFromBlackListFailth {
    [MBProgressHUD hideHUD];
}


- (HJEmptyView *)tipView
{
    if (!_tipView) {
        _tipView = [[HJEmptyView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 428)];
        [_tipView setTitle:@"您的黑名单为空喔～" image:@"blank"];
    }
    return _tipView;
}


@end
