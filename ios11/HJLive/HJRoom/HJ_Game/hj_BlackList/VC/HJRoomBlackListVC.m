//
//  HJRoomBlackListVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomBlackListVC.h"

#import "HJRoomManagerCell.h"

#import "HJImRoomCoreV2.h"
#import "HJIMRequestManager+Room.h"
#import "UITableView+MJRefresh.h"

@interface HJRoomBlackListVC ()

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (strong,nonatomic) NSMutableArray *dataArr;

@end

@implementation HJRoomBlackListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tableView.tableFooterView = [UIView new];
    
    [self getData];
    
    __weak typeof(self)weakSelf = self;
    self.tableView.headerBlock = ^{
        [weakSelf getData];
    };
    
    self.tableView.footerBlock = ^{
        [weakSelf getData];
    };
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    HJLightStatusBar
    
}

#pragma mark <UITableViewDelegate,UITableViewDataSource>

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 10;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return CGFLOAT_MIN;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view = [UIView new];
    view.backgroundColor = [UIColor clearColor];
    return view;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    HJRoomManagerCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJRoomManagerCell" forIndexPath:indexPath];
    if (indexPath.row<self.dataArr.count) {
        ChatRoomMember *model = [self.dataArr objectAtIndex:indexPath.row];
        cell.nameLabel.text = model.nick;
        [cell.avatarImageView qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        cell.sexImageView.image = [UIImage imageNamed:model.gender==1?@"hj_home_attend_man":@"hj_home_attend_woman"];
        cell.indexPath = indexPath;
        __weak typeof(self)weakSelf = self;
        cell.removeBlock = ^(NSIndexPath *index) {
            [weakSelf removeBlackList:index];
        };
    }
    
    return cell;
}

- (IBAction)backAction:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
    
}

- (void)getData
{
    __weak typeof(self)weakSelf = self;
    [HJIMRequestManager getRoomBlackMembersWithStart:1 count:20 roomId:[NSString stringWithFormat:@"%ld", GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] success:^(NSArray<ChatRoomMember *> *roomMembers) {
        
        [weakSelf.tableView.mj_header endRefreshing];
        [weakSelf.tableView.mj_footer endRefreshing];

        if (self.tableView.page == 0) {
            weakSelf.dataArr = [[NSMutableArray alloc] init];
        }
        
        [weakSelf.dataArr addObjectsFromArray:roomMembers];
        [weakSelf.tableView reloadData];
        
        if (roomMembers.count<20) {
            [weakSelf.tableView.mj_footer endRefreshingWithNoMoreData];
        }
        

    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        
    }];
}

- (void)removeBlackList:(NSIndexPath *)indexPath
{
    ChatRoomMember *user = [self.dataArr safeObjectAtIndex:indexPath.row];
    
    [GetCore(HJImRoomCoreV2) markBlackList:[user.account longLongValue] enable:NO];
    [self.dataArr removeObjectAtSafeIndex:indexPath.row];
    [self.tableView reloadData];
}

@end
