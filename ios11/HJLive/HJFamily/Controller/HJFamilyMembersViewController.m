//
//  HJFamilyMembersViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyMembersViewController.h"
#import "HJFamilyMemberTableCell.h"

#import "HJFamilyMemberModel.h"
#import "HJFamilyInfoDetail.h"

#import "HJFamilyCore.h"
#import "HJFamilyCoreClient.h"
#import "UITableView+MJRefresh.h"
#import "UIView+XCToast.h"

@interface HJFamilyMembersViewController ()<HJFamilyCoreClient, UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic, strong) NSMutableArray<HJFamilyInfoDetail *> *members;
@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger count;

@end

@implementation HJFamilyMembersViewController

#pragma mark - Life cycle
- (void)dealloc {
    RemoveCoreClient(HJFamilyCoreClient, self);
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
    [self setupNavigation];
    [self addControls];
    [self layoutControls];
    [self updateControls];
    [self loadNewMembers];
}

#pragma mark - Request datas
- (void)loadMembers {
    [GetCore(HJFamilyCore) familyMemberInfoByFamilyID:self.familyId page:self.page onceStr:nil];
}

- (void)loadNewMembers {
    self.page = 0;
    [self loadMembers];
}

- (void)loadMoreMembers {
    [self loadMembers];
}

#pragma mark - <FamilyCoreClient>
- (void)familyMemberInfoSuccessWithData:(HJFamilyMemberModel *)data once:(NSString *)once {
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    
    if (self.page == 0) {
        [self.members removeAllObjects];
    }
    
    if (data.familyTeamJoinDTOS.count) {
        [self.members addObjectsFromArray:data.familyTeamJoinDTOS];
    }
    
    if (data.familyTeamJoinDTOS.count < self.count) {
        [self.tableView.mj_footer endRefreshingWithNoMoreData];
    } else {
        self.tableView.mj_footer.state = MJRefreshStateIdle;
        self.page++;
    }
    [self updateControls];
}

- (void)familyApplyFamilyFailedWithMessage:(NSString *)message {
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    [self updateControls];
}

#pragma mark - <UITableViewDataSource && UITableViewDelegate>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.members.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HJFamilyMemberTableCell *cell = [tableView dequeueReusableCellWithIdentifier:HJFamilyMemberTableCellID forIndexPath:indexPath];
    [self configureCell:cell atIndexPath:indexPath];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    HJFamilyInfoDetail *member = [self.members objectOrNilAtIndex:indexPath.row];
}

#pragma mark - Event

#pragma mark - Public methods

#pragma mark - Private methods
- (void)updateControls {
    if (!self.members.count) {
        [self.tableView showEmptyContentToastWithTitle:@"暂无家族成员" andImage:[UIImage imageNamed:@"blank"]];
        self.tableView.mj_footer.hidden = YES;;
        return;
    }
    
    self.tableView.mj_footer.hidden = NO;;
    [self.tableView hideToastView];
    [self.view hideToastView];
    [self.tableView reloadData];
}

- (void)addCores {
    AddCoreClient(HJFamilyCoreClient, self);
}

- (void)resetDatas {
    [self.members removeAllObjects];
}

- (void)setupNavigation {
    self.navigationItem.title = @"家族成员";
}

- (void)commonInit {
    self.page = 0;
    self.count = 20;
}

- (void)configureCell:(HJFamilyMemberTableCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    HJFamilyInfoDetail *member = [self.members objectOrNilAtIndex:indexPath.row];
    [cell configureWithMemberInfo:member];
}

#pragma mark - Layout
- (void)addControls {
    [self.view addSubview:self.tableView];
}

- (void)layoutControls {
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.bottom.equalTo(self.view);
    }];
}

#pragma mark - setters/getters
- (NSMutableArray<HJFamilyInfoDetail *> *)members {
    if (!_members) {
        _members = @[].mutableCopy;
    }
    return _members;
}

- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        [_tableView registerClass:[HJFamilyMemberTableCell class] forCellReuseIdentifier:HJFamilyMemberTableCellID];
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
