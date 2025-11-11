//
//  YPFamilyApplicationRecordVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyApplicationRecordVC.h"
#import "YPFamilyApplicationRecordCell.h"
#import "YPFamilyMessage.h"
#import "UIView+XCToast.h"
#import "UITableView+MJRefresh.h"
#import "YPFamilyCore.h"
#import "HJFamilyCoreClient.h"

@interface YPFamilyApplicationRecordVC ()<UITableViewDataSource, UITableViewDelegate, HJFamilyCoreClient>

@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) NSMutableArray<YPFamilyMessage *> *records;

@property (nonatomic, strong) __block NSIndexPath *selectedIndexPath;
@property (nonatomic, assign) XCFamilyApplicationStatus currentStatus;

@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger count;

@end

@implementation YPFamilyApplicationRecordVC

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
    [self loadNewRecords];
}

#pragma mark - Request datas
- (void)loadRecords {
    [GetCore(YPFamilyCore) familyGetFamilyMessageWithFamilyId:self.familyId page:self.page count:self.count];
}

- (void)loadNewRecords {
    self.page = 0;
    [self loadRecords];
}

- (void)loadMoreRecords {
    [self loadRecords];
}

#pragma mark - Event

#pragma mark - <FamilyCoreClient>
// 家族消息
- (void)familyGetFamilyMessageSuccessWithDataArr:(NSArray *)dataArr {
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    
    if (self.page == 0) {
        [self.records removeAllObjects];
    }
    
    if (dataArr.count) {
        [self.records addObjectsFromArray:dataArr];
    }
    
    if (dataArr.count < self.count) {
        [self.tableView.mj_footer endRefreshingWithNoMoreData];
    } else {
        self.tableView.mj_footer.state = MJRefreshStateIdle;
        self.page++;
    }
    [self updateControls];
}

- (void)familyGetFamilyMessageFailedWithMessage:(NSString *)message {
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    [self updateControls];
}

- (void)familyApplyFamilySuccess {
    [MBProgressHUD hideHUD];
    [self updateApplicationStatus:self.currentStatus atIndexPath:self.selectedIndexPath];
}

- (void)familyApplyFamilyFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

#pragma mark - <UITableViewDataSource && UITableViewDelegate>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.records.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    YPFamilyApplicationRecordCell *cell = [tableView dequeueReusableCellWithIdentifier:HJFamilyApplicationRecordCellID forIndexPath:indexPath];
    [self configureCell:cell atIndexPath:indexPath];
    return cell;
}
 
#pragma mark - Public methods

#pragma mark - Private methods
- (void)applyUser:(XCFamilyApplicationStatus)status atIndexPath:(NSIndexPath *)indexPath {
    YPFamilyMessage *buffer = [self.records objectOrNilAtIndex:indexPath.row];
    self.selectedIndexPath = [NSIndexPath indexPathForRow:indexPath.row inSection:indexPath.section];
    self.currentStatus = status;
    [GetCore(YPFamilyCore) familyApplyFamilyWithFamilyId:self.familyId userId:[@(buffer.uid) description] type:buffer.type status:status];
}

- (void)updateApplicationStatus:(XCFamilyApplicationStatus)status atIndexPath:(NSIndexPath *)indexPath {
    if (!indexPath) return;
    
    YPFamilyMessage *buffer = [self.records objectOrNilAtIndex:indexPath.row];
    if (!buffer) return;
    
    buffer.status = status;
    [self updateControls];
    self.selectedIndexPath = nil;
}

- (void)addCores {
    AddCoreClient(HJFamilyCoreClient, self);
}

- (void)setupNaivgation {
    self.navigationItem.title = @"申请记录";
}

- (void)updateControls {
    if (!self.records.count) {
        [self.tableView showEmptyContentToastWithTitle:@"暂无家族消息" andImage:[UIImage imageNamed:@"blank"]];
        self.tableView.mj_footer.hidden = YES;;
        return;
    }
    
    self.tableView.mj_footer.hidden = NO;
    [self.tableView hideToastView];
    [self.view hideToastView];
    [self.tableView reloadData];
}

- (void)configureCell:(YPFamilyApplicationRecordCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    YPFamilyMessage *record = [self.records objectOrNilAtIndex:indexPath.row];
    [cell configureWithApplicationInfo:record];
    @weakify(self);
    @weakify(cell);
    cell.didTapAgreeHandler = ^{
        @strongify(self);
        @strongify(cell);
        NSIndexPath *currentIndex = [self.tableView indexPathForCell:cell];
        [MBProgressHUD showMessage:@""];
        [self applyUser:XCFamilyApplicationStatusSuccess atIndexPath:currentIndex];
    };
    
    cell.didTapIgnoreHandler = ^{
        @strongify(self);
        @strongify(cell);
        NSIndexPath *currentIndex = [self.tableView indexPathForCell:cell];
        [MBProgressHUD showMessage:@""];
        [self applyUser:XCFamilyApplicationStatusFailure atIndexPath:currentIndex];
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
- (NSMutableArray<YPFamilyMessage *> *)records {
    if (!_records) {
        _records = @[].mutableCopy;
    }
    return _records;
}

- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        [_tableView registerClass:[YPFamilyApplicationRecordCell class] forCellReuseIdentifier:HJFamilyApplicationRecordCellID];
        _tableView.separatorStyle = UITableViewCellSelectionStyleNone;
        _tableView.delegate = self;
        _tableView.dataSource = self;
        @weakify(self);
        _tableView.headerBlock = ^{
            @strongify(self);
            [self loadNewRecords];
        };
        _tableView.footerBlock = ^{
            @strongify(self);
            [self loadMoreRecords];
        };
    }
    return _tableView;
}

@end
