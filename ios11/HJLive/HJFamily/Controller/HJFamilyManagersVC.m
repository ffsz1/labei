//
//  HJFamilyManagersVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyManagersVC.h"

#import "HJFamilyInfoDetail.h"
#import "HJFamilyMemberModel.h"

#import "HJFamilyManagerSettingCell.h"
#import "HJFamilyAlertView.h"

#import "HJFamilyCore.h"
#import "HJFamilyCoreClient.h"
#import "UITableView+MJRefresh.h"
#import "UIView+XCToast.h"
#import "HJAlertControllerCenter.h"

#import "FamilyDefines.h"

typedef NS_ENUM(NSInteger, XCFamilyManageManagersActionType) {
    XCFamilyManageManagersActionTypeNormal,
    XCFamilyManageManagersActionTypeRemove,
};

@interface HJFamilyManagersVC ()<HJFamilyCoreClient, UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic, strong) NSMutableArray<HJFamilyInfoDetail *> *members;
@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger count;
@property (nonatomic, assign) NSInteger limitedManagerNumbers;
@property (nonatomic, strong) __block NSMutableArray<NSNumber *> *selectedUids;
@property (nonatomic, strong) NSMutableArray<NSNumber *> *orignalSelectedUids;
@property (nonatomic, strong) __block NSIndexPath *selectedIndexPath;
@property (nonatomic, assign) XCFamilyManageManagersActionType actionType;

@end

@implementation HJFamilyManagersVC

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
    [self loadMembers];
}

#pragma mark - Event
- (void)loadMembers {
    [GetCore(HJFamilyCore) familyMemberInfoByFamilyID:self.familyInfo.familyId page:self.page onceStr:nil];
}

- (void)loadNewMembers {
    self.page = 0;
    [self loadMembers];
}

- (void)loadMoreMembers {
    [self loadMembers];
}

//设置管理员
- (void)setupManagers {
    if (!self.selectedUids.count) return;
    
    if (self.selectedUids.count == 1) {
        NSString *uid = [[self.selectedUids objectOrNilAtIndex:0] stringValue];
        [MBProgressHUD showMessage:@"处理中"];
        [GetCore(HJFamilyCore) setManagerWithFamilyId:self.familyInfo.familyId uid:uid];
        return;
    }
    
    NSMutableArray *buffer = @[].mutableCopy;
    for (NSNumber *uidValue in self.selectedUids) {
        [buffer addObject:uidValue.stringValue];
    }
    
    NSString *uids = [buffer componentsJoinedByString:@","];
    if (!uids.length) return;
    
    [MBProgressHUD showMessage:@"处理中"];
    [GetCore(HJFamilyCore) setManagerWithFamilyId:self.familyInfo.familyId uids:uids];
}

- (void)showSetupManagersAlert {
    HJFamilyAlertView *alertView  = [[HJFamilyAlertView alloc] initWithFrame:CGRectMake(0, 0, 290, 199)];
    alertView.title = @"是否将该成员设置为副族长";
    alertView.actionTitle = @"确认添加";
    @weakify(self);
    HJAlertControllerCenter *alertCenter = [HJAlertControllerCenter defaultCenter];
    alertView.didTapActionHandler = ^{
        @strongify(self);
        [alertCenter dismissAlertNeedBlock:YES];
        [self setupManagers];
    };
    alertView.didTapCancelHandler = ^{
        [alertCenter dismissAlertNeedBlock:YES];
    };
    [alertCenter presentAlertWith:self.navigationController view:alertView preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:nil completionBlock:^{
    }];
}

#pragma mark - <FamilyCoreClient>
- (void)familyMemberInfoSuccessWithData:(HJFamilyMemberModel *)data once:(NSString *)once {
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    
    if (self.page == 0) {
        self.limitedManagerNumbers = 4;
        [self.members removeAllObjects];
        [self.selectedUids removeAllObjects];
    }
    
    if (data.familyTeamJoinDTOS.count < self.count) {
        [self.tableView.mj_footer endRefreshingWithNoMoreData];
    } else {
        self.tableView.mj_footer.state = MJRefreshStateIdle;
        self.page++;
    }
    
    NSMutableArray *buffer = @[].mutableCopy;
    switch (self.familyInfo.roleStatus) {
        case XCFamilyRoleStatusLeader:
        {
            for (HJFamilyInfoDetail *model in data.familyTeamJoinDTOS) {
                if (model.uid == [[GetCore(HJAuthCoreHelp) getUid] integerValue]) continue;
                if (model.roleStatus == XCFamilyRoleStatusLeader) continue;
                
                if (model.roleStatus == XCFamilyRoleStatusManager) {
                    self.limitedManagerNumbers -= 1;
                }
                [buffer addObject:model];
            }
        }
            break;
            
        case XCFamilyRoleStatusManager:
        {
            for (HJFamilyInfoDetail *model in data.familyTeamJoinDTOS) {
                if (model.uid == [[GetCore(HJAuthCoreHelp) getUid] integerValue]) continue;
                if (model.roleStatus == XCFamilyRoleStatusLeader) continue;
                if (model.roleStatus == XCFamilyRoleStatusManager) continue;
                
                [buffer addObject:model];
            }
        }
            break;
        case XCFamilyRoleStatusMember:
            break;
    }
    
    if (buffer.count) {
        [self.members addObjectsFromArray:buffer.copy];
    }
    [self updateControls];
    [self updateNavigation];
    if (self.actionType == XCFamilyManageManagersActionTypeNormal) {
        [MBProgressHUD hideHUD];
    } else if (self.actionType == XCFamilyManageManagersActionTypeRemove) {
        [MBProgressHUD showSuccess:@"移除成功"];
        self.actionType = XCFamilyManageManagersActionTypeNormal;
    }
}

- (void)familyApplyFamilyFailedWithMessage:(NSString *)message {
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    [self updateControls];
    [MBProgressHUD hideHUD];
}

- (void)setManagerSuccessWithData:(id)data {
    [MBProgressHUD showSuccess:@"设置成功"];
    [self loadNewMembers];
}

- (void)setManagerFailedWithDataFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}
    
- (void)removeManagerSuccessWithData:(id)data {
    [self removeMemberAtIndexPath:self.selectedIndexPath];
    [self loadNewMembers];
    self.actionType = XCFamilyManageManagersActionTypeRemove;
}
    
- (void)removeManagerFailedWithDataFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

#pragma mark - <UITableViewDataSource && UITableViewDelegate>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.members.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HJFamilyManagerSettingCell *cell = [tableView dequeueReusableCellWithIdentifier:HJFamilyManagerSettingCellID forIndexPath:indexPath];
    [self configureCell:cell atIndexPath:indexPath];
    return cell;
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)updateNavigation {
    UIButton *rightButton = ({
        UIButton *buffer = [UIButton new];
        buffer.size = CGSizeMake(44, 44);
        NSString *title = @"保存";
        [buffer setTitle:title forState:UIControlStateNormal];
        [buffer setTitleColor:[UIColor colorWithHexString:@"#9574F5"] forState:UIControlStateNormal];
        buffer.titleLabel.font = [UIFont boldSystemFontOfSize:17];
        
        SEL selector = @selector(showSetupManagersAlert);
        [buffer addTarget:self action:selector forControlEvents:UIControlEventTouchUpInside];
        buffer;
    });
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:rightButton];
    self.navigationItem.rightBarButtonItem.customView.hidden = !self.members.count;
}

- (void)updateMemberSelectedStateAtIndexPath:(NSIndexPath *)indexPath {
    HJFamilyInfoDetail *buffer = [self.members objectOrNilAtIndex:indexPath.row];
    if (!buffer) return;
    
    if (buffer.isSelected) {
        for (NSNumber *uidValue in self.selectedUids.reverseObjectEnumerator) {
            if (uidValue.integerValue == buffer.uid) {
                [self.selectedUids removeObject:uidValue];
                break;
            }
        }
    } else {
        if (self.selectedUids.count >= self.limitedManagerNumbers) return;
        
        [self.selectedUids addObject:@(buffer.uid)];
    }
    buffer.isSelected = !buffer.isSelected;
    [self updateControls];
}

- (void)removeMemberAtIndexPath:(NSIndexPath *)indexPath {
    [self.members removeObjectAtIndex:indexPath.row];
    self.limitedManagerNumbers += 1;
    
    [self updateControls];
}

- (void)addCores {
    AddCoreClient(HJFamilyCoreClient, self);
}

- (void)setupNaivgation {
    self.navigationItem.title = @"设置职位";
}

- (void)updateControls {
    if (!self.members.count) {
        [self.tableView showEmptyContentToastWithTitle:@"暂无家族成员可设置" andImage:[UIImage imageNamed:@"blank"]];
        self.tableView.mj_footer.hidden = YES;;
        return;
    }
    
    self.tableView.mj_footer.hidden = NO;;
    [self.tableView hideToastView];
    [self.view hideToastView];
    [self.tableView reloadData];
}

- (void)configureCell:(HJFamilyManagerSettingCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    HJFamilyInfoDetail *member = [self.members objectOrNilAtIndex:indexPath.row];
    [cell configureWithMemberInfo:member];
    @weakify(self);
    @weakify(cell);
    cell.didTapActionHandler = ^{
        @strongify(self);
        @strongify(cell);
        NSIndexPath *currentIndexPath = [self.tableView indexPathForCell:cell];
        [self updateMemberSelectedStateAtIndexPath:currentIndexPath];
    };
    cell.didTapDeleteHandler = ^{
        @strongify(self);
        @strongify(cell);
        NSIndexPath *currentIndexPath = [self.tableView indexPathForCell:cell];
        
        self.selectedIndexPath = [NSIndexPath indexPathForRow:currentIndexPath.row inSection:currentIndexPath.section];
        HJFamilyInfoDetail *buffer = [self.members objectOrNilAtIndex:currentIndexPath.row];
        [MBProgressHUD showMessage:@""];
        [GetCore(HJFamilyCore) removeManagerWithFamilyId:self.familyInfo.familyId userID:[NSString stringWithFormat:@"%ld", (long)buffer.uid]];
    };
}

- (void)commonInit {
    self.actionType = XCFamilyManageManagersActionTypeNormal;
    self.page = 0;
    self.count = 20;
    self.limitedManagerNumbers = 4;
}

- (void)setupDatas {
    
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
- (NSMutableArray<NSNumber *> *)orignalSelectedUids {
    if (!_orignalSelectedUids) {
        _orignalSelectedUids = @[].mutableCopy;
    }
    return _orignalSelectedUids;
}
    
- (NSMutableArray<NSNumber *> *)selectedUids {
    if (!_selectedUids) {
        _selectedUids = @[].mutableCopy;
    }
    return _selectedUids;
}

- (NSMutableArray<HJFamilyInfoDetail *> *)members {
    if (!_members) {
        _members = @[].mutableCopy;
    }
    return _members;
}

- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        [_tableView registerClass:[HJFamilyManagerSettingCell class] forCellReuseIdentifier:HJFamilyManagerSettingCellID];
        _tableView.separatorStyle = UITableViewCellSelectionStyleNone;
        _tableView.delegate = self;
        _tableView.dataSource = self;
        @weakify(self);
        _tableView.headerBlock = ^{
            @strongify(self);
            [self loadNewMembers];
        };
        _tableView.footerBlock = ^{
            @strongify(self);
            [self loadMoreMembers];
        };
    }
    return _tableView;
}

@end
