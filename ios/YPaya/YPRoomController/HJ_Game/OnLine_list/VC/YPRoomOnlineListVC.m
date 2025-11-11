//
//  YPRoomOnlineListVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomOnlineListVC.h"

#import "YPRoomOnlineCell.h"

#import "YPImRoomCoreV2.h"
#import "UITableView+MJRefresh.h"

#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"
#import "HJRoomCoreClient.h"

#import "YPRoomQueueCoreV2Help.h"

@interface YPRoomOnlineListVC ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (strong,nonatomic) NSMutableArray *dataArr;
@end

@implementation YPRoomOnlineListVC


- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tableView.tableFooterView = [UIView new];
    
    [self getData];
    
    __weak typeof(self)weakSelf = self;
    self.tableView.headerBlock = ^{
        [weakSelf getData];
    };
    
    AddCoreClient(HJImRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    AddCoreClient(HJRoomCoreClient, self);
    
}

- (void)dealloc
{
    RemoveCoreClientAll(self);
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    YPLightStatusBar
    
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
    YPRoomOnlineCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPRoomOnlineCell" forIndexPath:indexPath];
    if (indexPath.row<self.dataArr.count) {
        YPChatRoomMember *model = [self.dataArr objectAtIndex:indexPath.row];
        cell.model = model;
        cell.indexPath = indexPath;
        __weak typeof(self)weakSelf = self;
        cell.upMicBlock = ^(NSIndexPath * _Nullable index) {
            [weakSelf setUpMic:index];
        };
        
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
}


- (IBAction)backAction:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
    
}

- (void)getData
{
    __weak typeof(self)weakSelf = self;
    [GetCore(YPImRoomCoreV2) queryNoMicChatRoomMembersWithPage:self.tableView.page state:1];

}

- (void)setUpMic:(NSIndexPath *)indexPath
{
    YPChatRoomMember *member = [self.dataArr safeObjectAtIndex:indexPath.row];

    if ([member.account longLongValue] == [GetCore(YPAuthCoreHelp) getUid].userIDValue) {
        [GetCore(YPRoomQueueCoreV2Help) upMic:self.pos];
    }else {
        [GetCore(YPRoomQueueCoreV2Help) inviteUpMic:[member.account longLongValue] postion:[NSString stringWithFormat:@"%d",self.pos]];
    }
    
    [self.navigationController popViewControllerAnimated:YES];
    
}

#pragma mark - ImRoomCoreClient
#pragma mark - RoomCoreClient
- (void)onManagerAdd:(YPChatRoomMember *)member{
    [self getData];
}
- (void)onManagerRemove:(YPChatRoomMember *)member{
    [self getData];
}
//用户被加入黑名单
- (void)userBeAddBlack:(YPChatRoomMember *)member{
    [self getData];
}
#pragma mark - ImRoomCoreClient
//user被踢 exit
- (void)onUserExitChatRoom:(NSString *)roomId uid:(NSString *)uid{
    [self getData];
}

#pragma mark - ImRoomCoreClientV2
//获取队列
- (void)onGetRoomQueueSuccessV2:(NSMutableArray<YPIMQueueItem *> *)info{
    [self getData];
}
- (void)fetchRoomUserListSuccess:(int)state {
    
    [self.tableView.mj_header endRefreshing];
    

    self.dataArr = [[NSMutableArray alloc] init];
    [self.dataArr addObjectsFromArray:GetCore(YPImRoomCoreV2).noMicMembers];
//        if (self.dataArr.count == 0) {
//            [self.tableView showEmptyContentToastWithTitle:NSLocalizedString(XCRoomNoUserUp, nil) andImage:[UIImage imageNamed:@"blank"]];
//        }else {
//            [self.tableView hideToastView];
//        }
    
    [self.tableView reloadData];
    
}



@end
