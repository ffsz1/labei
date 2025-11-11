//
//  HJFamilyRankListVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyRankListVC.h"
#import "HJMyFamilyVC.h"
#import "HJDiscoveryFamilyTableViewCell.h"
#import "HJFamilyModel.h"
#import "HJFamilyInfoDetail.h"

#import "NSString+NIMKit.h"
#import "UIView+XCToast.h"
#import "UITableView+MJRefresh.h"
#import "HJFamilyCore.h"
#import "HJFamilyCoreClient.h"

static NSString * const XCFamilyRankListCellID = @"XCFamilyRankListCellID";

@interface HJFamilyRankListVC ()<UITableViewDataSource, UITableViewDelegate, HJFamilyCoreClient>

@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) NSMutableArray<HJFamilyModel *> *records;

@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger count;

@end

@implementation HJFamilyRankListVC

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
    [GetCore(HJFamilyCore) familyGetListWithFamilyId:nil page:0];
}

- (void)loadNewRecords {
    self.page = 0;
    [self loadRecords];
}

- (void)loadMoreRecords {
    [self loadRecords];
}

#pragma mark - Event
- (void)skipToFamily:(HJFamilyModel *)info {
    HJMyFamilyVC *vc = [[UIStoryboard storyboardWithName:@"HJFamily" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"HJMyFamilyVC"];
    HJFamilyInfoDetail *detail = [HJFamilyInfoDetail new];
    detail.familyId = info.familyId;
    detail.familyLogo = info.familyLogo;
    detail.familyName = info.familyName;
    detail.familyNotice = info.familyNotice;
    detail.familyUsersDTOS = info.familyUsersDTOS;
    detail.member = info.member;
    
    vc.familyInfoModel = detail;
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - <FamilyCoreClient>
- (void)familyGetListSuccessWithDataArr:(NSArray *)dataArr teamModel:(id)teamModel {
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

- (void)familyGetListFailedWithMessage:(NSString *)message {
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    [self updateControls];
}

#pragma mark - <UITableViewDataSource && UITableViewDelegate>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.records.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HJDiscoveryFamilyTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:XCFamilyRankListCellID forIndexPath:indexPath];
    [self configureCell:cell atIndexPath:indexPath];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    HJFamilyModel *info = [self.records objectOrNilAtIndex:indexPath.row];
    [self skipToFamily:info];
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)addCores {
    AddCoreClient(HJFamilyCoreClient, self);
}

- (void)setupNaivgation {
    self.navigationItem.title = @"家族排行榜";
}

- (void)updateControls {
    if (!self.records.count) {
        [self.tableView showEmptyContentToastWithTitle:@"暂无家族数据" andImage:[UIImage imageNamed:@"blank"]];
        self.tableView.mj_footer.hidden = YES;;
        return;
    }
    
    self.tableView.mj_footer.hidden = NO;
    [self.tableView hideToastView];
    [self.view hideToastView];
    [self.tableView reloadData];
}

- (void)configureCell:(HJDiscoveryFamilyTableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    HJFamilyModel *info = [self.records objectOrNilAtIndex:indexPath.row];
    [cell setupAvatar:[info.familyLogo cutAvatarImageSize] title:info.familyName count:[info.prestige integerValue] memberCount:[info.member integerValue] infoText:info.familyNotice index:info.ranking-1];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
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
- (NSMutableArray<HJFamilyModel *> *)records {
    if (!_records) {
        _records = @[].mutableCopy;
    }
    return _records;
}

- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        [_tableView registerClass:[HJDiscoveryFamilyTableViewCell class] forCellReuseIdentifier:XCFamilyRankListCellID];
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
