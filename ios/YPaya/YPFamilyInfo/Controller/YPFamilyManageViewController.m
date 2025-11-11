//
//  YPFamilyManageViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyManageViewController.h"
#import "YPFamilySettingVC.h"
#import "YPFamilyApplicationRecordVC.h"

#import "YPFamilyManageItemCell.h"
#import "YPFamilyManagerHeaderView.h"
#import "YPFamilyAlertView.h"

#import "YPFamilyManageItemModel.h"

#import "YPFamilyCore.h"
#import "HJFamilyCoreClient.h"
#import "YPAlertControllerCenter.h"
#import <NIMSDK/NIMSDK.h>

#import "FamilyDefines.h"

@interface YPFamilyManageViewController ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic, strong) NSMutableArray<YPFamilyManageItemModel *> *items;

@end

@implementation YPFamilyManageViewController

#pragma mark - Life cycle
- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (instancetype)init {
    self = [super init];
    if (self) {
        [self commonInit];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self addCores];
    [self setupDatas];
    [self setupNaivgation];
    [self addControls];
    [self layoutControls];
    [self updateControls];
}

#pragma mark - Event
- (void)exitFamily {
    YPFamilyAlertView *exitView  = [[YPFamilyAlertView alloc] initWithFrame:CGRectMake(0, 0, 290, 199)];
    exitView.title = @"退出家族风险告知";
    exitView.message = [NSString stringWithFormat:@"1.家族一经推出将不可逆\n\n2.你家族拥有的权限将全部失去"];
    exitView.actionTitle = @"确认退出";
    @weakify(self);
    YPAlertControllerCenter *alertCenter = [YPAlertControllerCenter defaultCenter];
    exitView.didTapActionHandler = ^{
        @strongify(self);
        [alertCenter dismissAlertNeedBlock:YES];
        [MBProgressHUD showMessage:@"申请中"];
        [GetCore(YPFamilyCore) applyExitFamilyWithFamilyId:self.familyInfo.familyId];
    };
    exitView.didTapCancelHandler = ^{
        [alertCenter dismissAlertNeedBlock:YES];
    };
    [alertCenter presentAlertWith:self.navigationController view:exitView preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:nil completionBlock:^{
    }];
}

- (void)skipToManageFamily {
    YPFamilySettingVC *viewController = [[YPFamilySettingVC alloc] init];
    viewController.familyInfo = self.familyInfo;
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)skipToApplicationRecord {
    YPFamilyApplicationRecordVC *viewController = [[YPFamilyApplicationRecordVC alloc] init];
    viewController.familyId = self.familyInfo.familyId;
    [self.navigationController pushViewController:viewController animated:YES];
}

#pragma mark - FamilyCoreClient
// 申请退出家族
- (void)applyExitFamilySuccessWithData:(id)data {
    [MBProgressHUD showSuccess:@"提交成功"];
}

- (void)applyExitFamilyFailedWithDataFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

#pragma mark - <UITableViewDataSource && UITableViewDelegate>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.items.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    YPFamilyManageItemCell *cell = [tableView dequeueReusableCellWithIdentifier:HJFamilyManageItemCellID forIndexPath:indexPath];
    [self configureCell:cell atIndexPath:indexPath];
    return cell;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    YPFamilyManagerHeaderView *headerView = [tableView dequeueReusableHeaderFooterViewWithIdentifier:HJFamilyManagerHeaderViewID];
    [self configureHeader:headerView inSection:section];
    return headerView;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    YPFamilyManageItemModel *item = [self.items objectOrNilAtIndex:indexPath.row];
    switch (item.type) {
        case XCFamilyManageItemTypeManageFamily:
        {
            [self skipToManageFamily];
        }
            break;
        case XCFamilyManageItemTypeApplicationRecord:
        {
            [self skipToApplicationRecord];
        }
            break;
        case XCFamilyManageItemTypeExit:
        {
            [self exitFamily];
        }
            break;
        case XCFamilyManageItemTypeMessageNotice:
            break;
    }
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)addCores {
    AddCoreClient(HJFamilyCoreClient, self);
}

- (void)setupNaivgation {
    self.navigationItem.title = @"家族管理";
}

- (void)updateControls {
    [self.tableView reloadData];
}

- (void)configureCell:(YPFamilyManageItemCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    YPFamilyManageItemModel *item = [self.items objectOrNilAtIndex:indexPath.row];
    [cell configureWithItem:item];
    @weakify(self);
    __weak __typeof(cell) weakCell = cell;
    cell.didSwitchHandler = ^(BOOL isOn) {
        @strongify(self);
        NIMTeamNotifyState changeState = isOn ? NIMTeamNotifyStateNone : NIMTeamNotifyStateAll;
        [MBProgressHUD showMessage:@"设置中..."];
        @weakify(self);
        [[NIMSDK sharedSDK].teamManager updateNotifyState:changeState inTeam:self.familyInfo.roomId completion:^(NSError * _Nullable error) {
            @strongify(self);
            [MBProgressHUD hideHUD];
            if (error == nil) {
                [MBProgressHUD showSuccess:@"设置成功"];
            } else {
                [MBProgressHUD showSuccess:@"设置失败"];
                [weakCell.switchView setOn:!isOn animated:YES];
            }
        }];
    };
}

- (void)configureHeader:(YPFamilyManagerHeaderView *)header inSection:(NSInteger)section {
    [header configureWithFamilyLogo:self.familyInfo.familyLogo familyName:self.familyInfo.familyName familyId:self.familyInfo.familyId time:self.familyInfo.createTime];
}

- (void)commonInit {
    
}

- (void)setupDatas {
    NIMTeamNotifyState state = [[NIMSDK sharedSDK].teamManager notifyStateForNewMsg:self.familyInfo.roomId];
    BOOL open = state == NIMTeamNotifyStateAll? NO : YES;
    switch (self.familyInfo.roleStatus) {
        case XCFamilyRoleStatusLeader:
        {
            [self.items addObject:[YPFamilyManageItemModel itemWithType:XCFamilyManageItemTypeManageFamily enable:YES]];
            [self.items addObject:[YPFamilyManageItemModel itemWithType:XCFamilyManageItemTypeApplicationRecord enable:YES]];
            [self.items addObject:[YPFamilyManageItemModel itemWithType:XCFamilyManageItemTypeMessageNotice enable:open]];
        }
            break;
        case XCFamilyRoleStatusManager:
        {
            [self.items addObject:[YPFamilyManageItemModel itemWithType:XCFamilyManageItemTypeManageFamily enable:YES]];
            [self.items addObject:[YPFamilyManageItemModel itemWithType:XCFamilyManageItemTypeApplicationRecord enable:YES]];
            [self.items addObject:[YPFamilyManageItemModel itemWithType:XCFamilyManageItemTypeExit enable:YES]];
            [self.items addObject:[YPFamilyManageItemModel itemWithType:XCFamilyManageItemTypeMessageNotice enable:open]];
        }
            break;
                                   
        case XCFamilyRoleStatusMember:
        {
            [self.items addObject:[YPFamilyManageItemModel itemWithType:XCFamilyManageItemTypeExit enable:YES]];
            [self.items addObject:[YPFamilyManageItemModel itemWithType:XCFamilyManageItemTypeMessageNotice enable:open]];
        }
            break;
    }
}

#pragma mark - Layout
- (void)addControls {
    [self.view addSubview:self.tableView];
}

- (void)layoutControls {
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.top.right.bottom.equalTo(self.view);
    }];
}

#pragma mark - setters/getters
- (NSMutableArray<YPFamilyManageItemModel *> *)items {
    if (!_items) {
        _items = @[].mutableCopy;
    }
    return _items;
}

- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        [_tableView registerClass:[YPFamilyManageItemCell class] forCellReuseIdentifier:HJFamilyManageItemCellID];
        [_tableView registerClass:[YPFamilyManagerHeaderView class] forHeaderFooterViewReuseIdentifier:HJFamilyManagerHeaderViewID];
        _tableView.separatorStyle = UITableViewCellSelectionStyleNone;
        _tableView.delegate = self;
        _tableView.dataSource = self;
    }
    return _tableView;
}

@end
