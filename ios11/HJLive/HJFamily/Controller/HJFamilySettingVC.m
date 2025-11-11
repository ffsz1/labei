//
//  HJFamilySettingVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilySettingVC.h"
#import "HJFamilyJoinWayVC.h"
#import "HJFamilyManageMembersViewController.h"
#import "HJFamilyManagersVC.h"
#import "HJFamilySettingItemCell.h"
#import "HJFamilyManageSettingItemModel.h"
#import "HJFamilyCore.h"
#import "HJFamilyCoreClient.h"

@interface HJFamilySettingVC ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) NSMutableArray<HJFamilyManageSettingItemModel *> *items;
@end

@implementation HJFamilySettingVC

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
- (void)skipToManageMember {
    HJFamilyManageMembersViewController *viewController = [HJFamilyManageMembersViewController new];
    viewController.familyInfo = self.familyInfo;
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)skipToManagerSetting {
    HJFamilyManagersVC *viewController = [[HJFamilyManagersVC alloc] init];
    viewController.familyInfo = self.familyInfo;
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)skipToJoinWaySetting {
    HJFamilyJoinWayVC *viewController = [[HJFamilyJoinWayVC alloc] init];
    viewController.familyInfo = self.familyInfo;
    [self.navigationController pushViewController:viewController animated:YES];
}

#pragma mark - <UITableViewDataSource && UITableViewDelegate>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.items.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HJFamilySettingItemCell *cell = [tableView dequeueReusableCellWithIdentifier:HJFamilySettingItemCellID forIndexPath:indexPath];
    [self configureCell:cell atIndexPath:indexPath];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    HJFamilyManageSettingItemModel *item = [self.items objectOrNilAtIndex:indexPath.row];
    switch (item.type) {
        case XCFamilyManageSettingItemTypeRemoveMembers:
        {
            [self skipToManageMember];
        }
            break;
        case XCFamilyManageSettingItemTypeSetupManager:
        {
            [self skipToManagerSetting];
        }
            break;
        case XCFamilyManageSettingItemTypeJoinWay:
        {
            [self skipToJoinWaySetting];
        }
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

- (void)configureCell:(HJFamilySettingItemCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    HJFamilyManageSettingItemModel *item = [self.items objectOrNilAtIndex:indexPath.row];
    [cell configureWithItem:item];
}

- (void)commonInit {
    
}

- (void)setupDatas {
    switch (self.familyInfo.roleStatus) {
        case XCFamilyRoleStatusLeader:
        {
            [self.items addObject:[HJFamilyManageSettingItemModel itemWithType:XCFamilyManageSettingItemTypeRemoveMembers enable:YES]];
            [self.items addObject:[HJFamilyManageSettingItemModel itemWithType:XCFamilyManageSettingItemTypeSetupManager enable:YES]];
            [self.items addObject:[HJFamilyManageSettingItemModel itemWithType:XCFamilyManageSettingItemTypeJoinWay enable:YES]];
        }
            break;
        case XCFamilyRoleStatusManager:
        {
            [self.items addObject:[HJFamilyManageSettingItemModel itemWithType:XCFamilyManageSettingItemTypeRemoveMembers enable:YES]];
            [self.items addObject:[HJFamilyManageSettingItemModel itemWithType:XCFamilyManageSettingItemTypeJoinWay enable:YES]];
        }
            break;
            
        case XCFamilyRoleStatusMember:
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
- (NSMutableArray<HJFamilyManageSettingItemModel *> *)items {
    if (!_items) {
        _items = @[].mutableCopy;
    }
    return _items;
}

- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        [_tableView registerClass:[HJFamilySettingItemCell class] forCellReuseIdentifier:HJFamilySettingItemCellID];
        _tableView.separatorStyle = UITableViewCellSelectionStyleNone;
        _tableView.delegate = self;
        _tableView.dataSource = self;
    }
    return _tableView;
}

@end
