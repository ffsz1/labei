//
//  YPZaJinDanRecordeList.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPZaJinDanRecordeList.h"

#import "YPZaJinDanRecordeCell.h"

#import "YPRefreshFactory.h"
#import "YPRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "YPGiftPurseRecord.h"

@interface YPZaJinDanRecordeList ()<UITableViewDelegate,UITableViewDataSource,HJRoomCoreClient>

@property (nonatomic, strong) NSMutableArray *dataArr;

@property (weak, nonatomic) IBOutlet UIView *noContentView;
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger pageSize;

@end

@implementation YPZaJinDanRecordeList

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)removeCore {
    RemoveCoreClientAll(self);
}

- (void)awakeFromNib {
    
    [super awakeFromNib];
    
    AddCoreClient(HJRoomCoreClient, self);
    
    self.noContentView.hidden = YES;
    
    self.pageSize = 30;
    
    self.layer.cornerRadius = 8;
    self.layer.masksToBounds = YES;
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.separatorInset = UIEdgeInsetsMake(0, 15, 0, 15);
    self.tableView.tableFooterView = [UIView new];
    [self.tableView registerNib:[UINib nibWithNibName:@"YPZaJinDanRecordeCell" bundle:nil] forCellReuseIdentifier:@"YPZaJinDanRecordeCell"];
    
    self.tableView.mj_header = [YPRefreshFactory headerRefreshWithTarget:self refreshingAction:@selector(requestForNewData)];
    self.tableView.mj_footer = [YPRefreshFactory footerRefreshWithTarget:self refreshingAction:@selector(requestForMoreData)];
    self.tableView.mj_footer.hidden = YES;
    [self requestForNewData];
}


#pragma mark - UITableViewDelegate && UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return self.dataArr.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 60.f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    YPZaJinDanRecordeCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPZaJinDanRecordeCell"];
    
    cell.model = self.dataArr[indexPath.row];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (IBAction)closeAction:(id)sender {
    
    if (self.closeActionBlock) {
        self.closeActionBlock();
    }
}


#pragma mark - data
- (void)requestForNewData {
    
    self.page = 1;
    [self requestForData];
}

- (void)requestForMoreData {
    
    self.page++;
    [self requestForData];
}

- (void)requestForData {
    
    [GetCore(YPRoomCoreV2Help) userGiftPurseRecordWithPageNum:self.page pageSize:self.pageSize];
}

#pragma mark - RoomCoreClient
- (void)userGiftPurseRecordSuccessWithList:(NSMutableArray *)list {
    
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    NSArray *arr = list;
    
    if (self.page == 1) {
        self.dataArr = [NSMutableArray array];
    }
    
    [self.dataArr addObjectsFromArray:arr];
    
    [self.tableView reloadData];
    
    if (arr.count >= self.pageSize) {
        self.tableView.mj_footer.hidden = NO;
        self.tableView.mj_footer.state = MJRefreshStateIdle;
    }
    else {
        self.tableView.mj_footer.hidden = YES;
    }
    
    if (!self.dataArr.count) {
        self.tableView.hidden = YES;
        self.noContentView.hidden = NO;
    }
    else {
        self.tableView.hidden = NO;
        self.noContentView.hidden = YES;
    }
    
}

- (void)userGiftPurseRecordFailWithMessage:(NSString *)message {
    
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];
    
    if (!self.dataArr.count) {
        self.tableView.hidden = YES;
        self.noContentView.hidden = NO;
    }
    else {
        self.tableView.hidden = NO;
        self.noContentView.hidden = YES;
    }
    
}


@end
