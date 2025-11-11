//
//  YPFamilyManageMembersViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyManageMembersViewController.h"

#import "YPFamilyInfoDetail.h"
#import "YPFamilyMemberModel.h"

#import "YPFamilyMemberSettingCell.h"
#import "YPFamilyAlertView.h"

#import "YPFamilyCore.h"
#import "HJFamilyCoreClient.h"
#import "UITableView+MJRefresh.h"
#import "UIView+XCToast.h"
#import "YPAlertControllerCenter.h"

#import "FamilyDefines.h"

@interface YPFamilyManageMembersViewController ()<HJFamilyCoreClient, UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic, strong) NSMutableArray<YPFamilyInfoDetail *> *members;
@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger count;

@property (nonatomic, strong) NSIndexPath *selectedIndexPath;

@end


@implementation YPFamilyManageMembersViewController

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
    [self loadNewMembers];
}

#pragma mark - Event
- (void)loadMembers {
    [GetCore(YPFamilyCore) familyMemberInfoByFamilyID:self.familyInfo.familyId page:self.page onceStr:nil];
}

- (void)loadNewMembers {
    self.page = 0;
    [self loadMembers];
}

- (void)loadMoreMembers {
    [self loadMembers];
}

//踢出家族
- (void)removeMemberAtIndexPath:(NSIndexPath *)indexPath {
    if (!indexPath) return;
    
    YPFamilyInfoDetail *buffer = [self.members objectOrNilAtIndex:indexPath.row];
    if (!buffer) return;
    
    [MBProgressHUD showMessage:@"处理中"];
    NSString *userId= [NSString stringWithFormat:@"%ld",buffer.uid];
    [GetCore(YPFamilyCore) setOutWithFamilyId:self.familyInfo.familyId userID:userId];
}

- (void)showRemoveMemberAlert {
    YPFamilyAlertView *alertView  = [[YPFamilyAlertView alloc] initWithFrame:CGRectMake(0, 0, 290, 199)];
    alertView.title = @"驱逐成员";
    alertView.message = [NSString stringWithFormat:@"确认驱逐该成员，驱逐后48小时，无法申请加入家族"];
    alertView.actionTitle = @"确认驱逐";
    @weakify(self);
    YPAlertControllerCenter *alertCenter = [YPAlertControllerCenter defaultCenter];
    alertView.didTapActionHandler = ^{
        @strongify(self);
        [alertCenter dismissAlertNeedBlock:YES];
        [self removeMember];
    };
    alertView.didTapCancelHandler = ^{
        [alertCenter dismissAlertNeedBlock:YES];
    };
    [alertCenter presentAlertWith:self.navigationController view:alertView preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:nil completionBlock:^{
    }];
}

- (void)removeMember {
    [self removeMemberAtIndexPath:self.selectedIndexPath];
}

#pragma mark - <FamilyCoreClient>
- (void)familyMemberInfoSuccessWithData:(YPFamilyMemberModel *)data once:(NSString *)once {
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    
    if (self.page == 0) {
        [self.members removeAllObjects];
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
            for (YPFamilyInfoDetail *model in data.familyTeamJoinDTOS) {
                if (model.uid == [[GetCore(YPAuthCoreHelp) getUid] integerValue]) continue;
                if (model.roleStatus == XCFamilyRoleStatusLeader) continue;
                
                [buffer addObject:model];
            }
        }
            break;
            
        case XCFamilyRoleStatusManager:
        {
            for (YPFamilyInfoDetail *model in data.familyTeamJoinDTOS) {
                if (model.uid == [[GetCore(YPAuthCoreHelp) getUid] integerValue]) continue;
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
}

- (void)familyApplyFamilyFailedWithMessage:(NSString *)message {
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    [self updateControls];
}

- (void)setOutSuccessWithData:(id)data {
    [MBProgressHUD showSuccess:@"踢出成功"];
    [self updateRemoveMemberAtIndexPath:self.selectedIndexPath];
}

- (void)setOutMethodFailedWithDataFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

#pragma mark - <UITableViewDataSource && UITableViewDelegate>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.members.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    YPFamilyMemberSettingCell *cell = [tableView dequeueReusableCellWithIdentifier:HJFamilyMemberSettingCellID forIndexPath:indexPath];
    [self configureCell:cell atIndexPath:indexPath];
    return cell;
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)updateRemoveMemberAtIndexPath:(NSIndexPath *)indexPath {
    if (!indexPath) return;
    
    [self.members removeObjectAtIndex:indexPath.row];
    [self updateControls];
    self.selectedIndexPath = nil;
}

- (void)updateControls {
    if (!self.members.count) {
        [self.tableView showEmptyContentToastWithTitle:@"暂无家族成员可踢出" andImage:[UIImage imageNamed:@"blank"]];
        self.tableView.mj_footer.hidden = YES;;
        return;
    }
    
    self.tableView.mj_footer.hidden = NO;;
    [self.view hideToastView];
    [self.tableView hideToastView];
    [self.tableView reloadData];
}

- (void)addCores {
    AddCoreClient(HJFamilyCoreClient, self);
}

- (void)setupNaivgation {
    self.navigationItem.title = @"管理成员";
}

- (void)configureCell:(YPFamilyMemberSettingCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    YPFamilyInfoDetail *member = [self.members objectOrNilAtIndex:indexPath.row];
    [cell configureWithMemberInfo:member];
    @weakify(self);
    @weakify(cell);
    cell.didTapActionHandler = ^{
        @strongify(self);
        @strongify(cell);
        NSIndexPath *currentIndex = [self.tableView indexPathForCell:cell];
        self.selectedIndexPath = [NSIndexPath indexPathForRow:currentIndex.row inSection:currentIndex.section];
        [self showRemoveMemberAlert];
    };
}

- (void)commonInit {
    self.page = 0;
    self.count = 20;
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
- (NSMutableArray<YPFamilyInfoDetail *> *)members {
    if (!_members) {
        _members = @[].mutableCopy;
    }
    return _members;
}

- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        [_tableView registerClass:[YPFamilyMemberSettingCell class] forCellReuseIdentifier:HJFamilyMemberSettingCellID];
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
