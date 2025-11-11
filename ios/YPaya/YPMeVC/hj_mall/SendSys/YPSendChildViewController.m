//
//  YPSendChildViewController.m
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPSendChildViewController.h"
#import "ZJScrollPageViewDelegate.h"
#import "YPFriendListCell.h"
#import "HJImFriendCoreClient.h"
#import "YPImFriendCore.h"
#import "UIView+XCToast.h"
#import "YPAttention.h"
#import "YPPraiseCore.h"
#import "UITableView+Refresh.h"
#import "HJPraiseCoreClient.h"
#import "HJUserCoreClient.h"
#import "HJRoomCoreClient.h"
#import "YPCarSysCore.h"
#import "YPHeadwear.h"
#import "HJCarSysCoreClient.h"
#import "HJHeadwearClient.h"
#import "YPHeadMallViewController.h"

@interface YPSendChildViewController ()<
ZJScrollPageViewChildVcDelegate,
HJImFriendCoreClient,
HJPraiseCoreClient,
HJUserCoreClient,
HJRoomCoreClient,
HJCarSysCoreClient,
HJHeadwearClient
>

@property (strong, nonatomic) NSMutableArray<UserInfo *> *friendsList;
@property (nonatomic, strong) NSMutableArray<YPAttention *> *attentionList;
@end

@implementation YPSendChildViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    AddCoreClient(HJImFriendCoreClient, self);
    if (!self.isFriend) {
        AddCoreClient(HJPraiseCoreClient, self);
        AddCoreClient(HJUserCoreClient, self);
        AddCoreClient(HJRoomCoreClient, self);
    }

    AddCoreClient(HJCarSysCoreClient, self);
    AddCoreClient(HJHeadwearClient, self);
    
    [self.tableView registerNib:[UINib nibWithNibName:@"YPFriendListCell" bundle:nil] forCellReuseIdentifier:@"YPFriendListCell"];
    
    self.tableView.rowHeight = 80;
    self.tableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectZero];
    
    if (!self.isFriend) {
        [self setupRefreshTarget:self.tableView];
    }
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
}

#pragma mark - PraiseCore
- (void)onRequestAttentionListState:(int)state success:(NSArray *)attentionList{
    if (attentionList.count>0) {
        [self.attentionList addObjectsFromArray:attentionList];
        [self.tableView endRefreshStatus:state hasMoreData:YES];
    }else{
        [self.tableView endRefreshStatus:state hasMoreData:NO];
    }
    
    [self.tableView reloadData];
    
    if (self.attentionList.count == 0) {
        [self.tableView showEmptyContentToastWithTitle:NSLocalizedString(XCCarSysSendNoFriend, nil) andImage:[UIImage imageNamed:placeholder_image_square]];
    }else{
        [self.tableView hideToastView];
    }
}
- (void)onRequestAttentionListState:(int)state failth:(NSString *)msg{
    [MBProgressHUD hideHUD];
    [self.tableView endRefreshStatus:state];
}

#pragma mark - Getter
- (NSMutableArray<YPAttention *> *)attentionList{
    if (!_attentionList) {
        _attentionList = [NSMutableArray array];
    }
    return _attentionList;
}


- (void)zj_viewWillAppearForIndex:(NSInteger)index{
    self.navigationController.navigationBar.translucent = NO;
    if (index == 0) {
        [self updateView];
    } else {
        [self pullDownRefresh:1];
    }
}

#pragma mark - private method
- (void)setupRefreshTarget:(UITableView *)tableView{
    
    [tableView setupRefreshFunctionWith:RefreshTypeHeaderAndFooter];
    
    [tableView pullUpRefresh:^(int page, BOOL isLastPage) {
        
        [self pullUpRefresh:page lastPage:isLastPage];
    }];
    
    [tableView pullDownRefresh:^(int page)
     {
         [self pullDownRefresh:page];
     }];
}

//下拉刷新
- (void)pullDownRefresh:(int)page{
    NSLog(@"pullDownRefresh");
    [self.attentionList removeAllObjects];
    [GetCore(YPPraiseCore) requestAttentionListState:0 page:page];
    
}

//上拉刷新
- (void)pullUpRefresh:(int)page lastPage:(BOOL)isLastPage{
    NSLog(@"pullUpRefresh");
    if (isLastPage) {
        NSLog(@"已经最后一页了");
        return;
    }
    [GetCore(YPPraiseCore) requestAttentionListState:1 page:page];
}

- (void)updateView {
    self.friendsList = [[GetCore(YPImFriendCore) getMyFriends] mutableCopy];
    if (self.friendsList.count > 0) {
        [self.tableView hideToastView];
    }else {
        [self.tableView showEmptyContentToastWithTitle:NSLocalizedString(XCCarSysSendNoPraiseFriend, nil) andImage:[UIImage imageNamed:placeholder_image_square]];
    }
    [self.tableView reloadData];
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    YPFriendListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPFriendListCell" forIndexPath:indexPath];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    [self configureCell:cell forRowAtIndexPath:indexPath];
    
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView
 numberOfRowsInSection:(NSInteger)section
{
    if (self.isFriend) {
        return self.friendsList.count;
    }
        return self.attentionList.count;
}

#pragma mark - ImLoginCoreClient
- (void)onImLogoutSuccess {
    [self.friendsList removeAllObjects];
}

- (void)onImSyncSuccess {
    [self updateView];
}

- (void)configureCell:(YPFriendListCell *)cell
    forRowAtIndexPath:(NSIndexPath *)indexPath {
    cell.sendBtn.hidden = false;
    
    if (self.isFriend) {
        UserInfo *info = self.friendsList[indexPath.row];
        cell.uid = info.uid;
        [cell.avatar qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        cell.usernameLabel.text = info.nick;
        [cell setSendBlock:^{
            [self inviteBegin:info.uid name:info.nick];
        }];
    } else {
        YPAttention *attention = [self.attentionList safeObjectAtIndex:indexPath.row];
        cell.usernameLabel.text = attention.nick;
        cell.uid = attention.uid;

        [cell.avatar qn_setImageImageWithUrl:attention.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        [cell setSendBlock:^{
            [self inviteBegin:attention.uid name:attention.nick];
        }];
    }
    cell.navigationController = self.navigationController;
}

- (void)inviteBegin:(UserID)uid name:(NSString *)sendUserName{
    NSString *message;
    if (self.isCarSys) {
        message = [NSString stringWithFormat:@"%@%@%@%@",NSLocalizedString(XCCarSysSureBuyCar, nil),self.sendName,NSLocalizedString(XCCarSysSendToSomeone, nil),sendUserName];
    } else {
        message = [NSString stringWithFormat:@"%@%@%@%@",NSLocalizedString(XCCarSysSureBuyHant, nil),self.sendName,NSLocalizedString(XCCarSysSendToSomeone, nil),sendUserName];
    }
    
    UIAlertController *alertDialog = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertNiceTip,nil) message:message preferredStyle:UIAlertControllerStyleAlert];
    
    @weakify(self);
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(XCAlertConfirm,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        @strongify(self);
        if (self.isCarSys) {
            [GetCore(YPCarSysCore) sendCarWithCarId:self.proId targetUid:[NSString stringWithFormat:@"%lld",uid]];
        } else {
            [GetCore(YPHeadwear) sendHeadwearWithHeadwearId:self.proId targetUid:[NSString stringWithFormat:@"%lld",uid]];
        }
    }];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(MMPopViewCancel,nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {}];
    [alertDialog addAction:cancelAction];
    [alertDialog addAction:okAction];
    [self.tabBarController presentViewController:alertDialog animated:YES completion:nil];
}

- (void)sendCarSysSuccess {
    [UIView showToastInKeyWindow:NSLocalizedString(XCCarSysSendSuccess, nil) duration:1];
    [self jumpBack];
}

//- (void)sendCarSysFail:(NSString *)msg {
//    [UIView showToastInKeyWindow:msg duration:1];
//}
//
//- (void)sendHeadwearFail:(NSString *)msg {
//    [UIView showToastInKeyWindow:msg duration:1];
//}

- (void)jumpBack {
    for (UIViewController *vc in self.navigationController.viewControllers) {
        if ([vc isKindOfClass:[YPHeadMallViewController class]]) {
            @weakify(self);
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                @strongify(self);
                [self.navigationController popToViewController:vc animated:YES];
            });
            break;
        }
    }
}

- (void)sendHeadwearSuccess {
//    [UIView showToastInKeyWindow:NSLocalizedString(XCCarSysSendSuccess, nil) duration:1];
    [self jumpBack];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
@end
