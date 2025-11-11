//
//  YPFamilyJoinWayVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyJoinWayVC.h"
#import "YPFamilyJoinWaySettingCell.h"
#import "YPFamilyManageJoinWayModel.h"
#import "YPFamilyCore.h"
#import "HJFamilyCoreClient.h"

@interface YPFamilyJoinWayVC ()<HJFamilyCoreClient, UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) NSMutableArray<YPFamilyManageJoinWayModel *> *items;
@property (nonatomic, assign) XCFamilyJoinWayType selectedJoinWayType;
@property (nonatomic, assign) XCFamilyJoinWayType tempSelectedJoinWayType;
@end

@implementation YPFamilyJoinWayVC

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
    [self updateDataType:self.selectedJoinWayType];
    [self setupNaivgation];
    [self addControls];
    [self layoutControls];
    [self updateControls];
}

#pragma mark - Event
//设置申请加入方式
- (void)setupApplyJoinAtIndexPath:(NSIndexPath *)indexPath {
    YPFamilyManageJoinWayModel *item = [self.items objectOrNilAtIndex:indexPath.row];
    if (!item) return;
    if (self.selectedJoinWayType == item.type) return;
    
    self.tempSelectedJoinWayType = item.type;
    [MBProgressHUD showMessage:@"设置中"];
    NSString *joinModel = [NSString stringWithFormat:@"%ld", item.type];
    [GetCore(YPFamilyCore) setApplyJoinMethodWithFamilyId:self.familyInfo.familyId joinmode:joinModel];
}

#pragma mark - <FamilyCoreClient>
- (void)setApplyJoinMethodSuccessWithData:(id)data {
    [MBProgressHUD showSuccess:@"切换成功"];
    [self updateDataType:self.tempSelectedJoinWayType];
    self.selectedJoinWayType = self.tempSelectedJoinWayType;
    [self updateDatas];
    [self updateControls];
}

- (void)setApplyJoinMethodFailedWithDataFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

#pragma mark - <UITableViewDataSource && UITableViewDelegate>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.items.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    YPFamilyJoinWaySettingCell *cell = [tableView dequeueReusableCellWithIdentifier:HJFamilyJoinWaySettingCellID forIndexPath:indexPath];
    [self configureCell:cell atIndexPath:indexPath];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self setupApplyJoinAtIndexPath:indexPath];
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)updateDatas {
    self.familyInfo.verification = self.selectedJoinWayType;
}

- (void)addCores {
    AddCoreClient(HJFamilyCoreClient, self);
}

- (void)setupNaivgation {
    self.navigationItem.title = @"加入方式";
}

- (void)updateControls {
    [self.tableView reloadData];
}

- (void)configureCell:(YPFamilyJoinWaySettingCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    YPFamilyManageJoinWayModel *item = [self.items objectOrNilAtIndex:indexPath.row];
    [cell configureWithItem:item];
}

- (void)commonInit {
    self.selectedJoinWayType = XCFamilyJoinWayTypeNormal;
}

- (void)setupDatas {
    switch (self.familyInfo.roleStatus) {
        case XCFamilyRoleStatusLeader:
        case XCFamilyRoleStatusManager:
        {
            [self.items addObject:[YPFamilyManageJoinWayModel itemWithType:XCFamilyJoinWayTypeNormal selected:NO]];
            [self.items addObject:[YPFamilyManageJoinWayModel itemWithType:XCFamilyJoinWayTypeSH selected:NO]];
        }
            break;
            
        case XCFamilyRoleStatusMember:
            break;
    }
    self.selectedJoinWayType = self.familyInfo.verification;
}

- (void)updateDataType:(XCFamilyJoinWayType)type {
    for (YPFamilyManageJoinWayModel *buffer in self.items) {
        if (buffer.type == type) {
            buffer.selected = YES;
            continue;
        }
        buffer.selected = NO;
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
- (NSMutableArray<YPFamilyManageJoinWayModel *> *)items {
    if (!_items) {
        _items = @[].mutableCopy;
    }
    return _items;
}

- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        [_tableView registerClass:[YPFamilyJoinWaySettingCell class] forCellReuseIdentifier:HJFamilyJoinWaySettingCellID];
        _tableView.separatorStyle = UITableViewCellSelectionStyleNone;
        _tableView.delegate = self;
        _tableView.dataSource = self;
    }
    return _tableView;
}

@end
