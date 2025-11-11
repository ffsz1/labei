//
//  YPZaJinDanRankList.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPZaJinDanRankList.h"

#import "YPZaJinDanRankListCell.h"

#import "UIView+XCToast.h"
#import "HJRoomCoreClient.h"
#import "YPRoomCoreV2Help.h"
#import "YPImRoomCoreV2.h"
#import "HJImRoomCoreClientV2.h"
#import "YPGiftPurseRank.h"
#import "YPZJScrollSegmentView.h"

//#import "UITableView+Refresh.h"

typedef NS_ENUM(NSUInteger, ZaJinDanRankListType) {
    ZaJinDanRankListTypeToday = 1, //今日
    ZaJinDanRankListTypeYestday,   //昨日
    ZaJinDanRankListTypeWeek,     //周榜
};

@interface YPZaJinDanRankList () <UITableViewDelegate,UITableViewDataSource,HJRoomCoreClient,HJImRoomCoreClientV2>

@property (nonatomic, assign) ZaJinDanRankListType type;
//@property (nonatomic, assign) NSInteger page;
//@property (nonatomic, assign) NSInteger pageSize;
@property (weak, nonatomic) IBOutlet UIView *topContentView;
@property (nonatomic, strong) YPZJScrollSegmentView *segmentView;

@property (nonatomic, strong) NSMutableArray *dataArr;

@end

@implementation YPZaJinDanRankList

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)removeCore {
    RemoveCoreClientAll(self);
}

- (void)awakeFromNib {
    [super awakeFromNib];
//    self.layer.cornerRadius = 24;
//    self.layer.masksToBounds = YES;
    self.type = ZaJinDanRankListTypeToday;
    
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.separatorInset = UIEdgeInsetsMake(0, 15, 0, 15);
    self.tableView.tableFooterView = [UIView new];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.tableView registerNib:[UINib nibWithNibName:@"YPZaJinDanRankListCell" bundle:nil] forCellReuseIdentifier:@"YPZaJinDanRankListCell"];
    
    @weakify(self);
//    self.page = 1;
//    self.pageSize = 50;
//    [self.tableView pullDownRefresh:^(int page) {
//        @strongify(self);
//        [self requestForNewData];
//    }];
//
//    [self.tableView pullUpRefresh:^(int page, BOOL isLastPage) {
//        @strongify(self);
//        [self requestForMoreData];
//    }];
//
    [self updateView];
    
    
    self.lvOrmlSeg.layer.masksToBounds = YES;
    self.lvOrmlSeg.layer.cornerRadius = 15;
    self.lvOrmlSeg.layer.borderColor = UIColorHex(00C2FF).CGColor;
    self.lvOrmlSeg.layer.borderWidth = 1;
    [self.lvOrmlSeg setTitleTextAttributes:@{NSFontAttributeName : [UIFont systemFontOfSize:16]}forState:UIControlStateSelected];
    [self.lvOrmlSeg setTitleTextAttributes:@{NSFontAttributeName : [UIFont systemFontOfSize:16]}forState:UIControlStateNormal];
    
    
    [[self.lvOrmlSeg rac_newSelectedSegmentIndexChannelWithNilValue:@0] subscribeNext:^(id x) {
        @strongify(self);
        NSInteger type = self.type;
        if ([x intValue] == 0) {
            self.type = ZaJinDanRankListTypeToday;
        } else if ([x intValue] == 1) {
            self.type = ZaJinDanRankListTypeYestday;
        } else if ([x intValue] == 2) {
            self.type = ZaJinDanRankListTypeWeek;
        }
        
        if (type != self.type) {
            self.dataArr = [NSMutableArray array];
            [self.tableView reloadData];
        }
        
        [self updateView];
    }];
    
    [self.topContentView addSubview:self.segmentView];
    [self.segmentView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.topContentView);
    }];
}

- (void)updateView {
    
    [self requestForNewData];
}

- (void)requestForNewData {
//    self.page = 1;
//    self.tableView.mj_footer.hidden = YES;
    [GetCore(YPRoomCoreV2Help) userGiftPurseGetRankWithType:self.type];
}

//- (void)requestForMoreData {
//    self.page++;
//}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    YPZaJinDanRankListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPZaJinDanRankListCell"];
    cell.model = self.dataArr[indexPath.row];

    
    if (indexPath.row == 0 ) {
        cell.crownIcon.hidden = NO;
        [cell.crownIcon setImage:[UIImage imageNamed:@"yp_dacall_call_gold"]];
        cell.rankingLabel.hidden = YES;
    }else if (indexPath.row == 1) {
        cell.crownIcon.hidden = NO;
        cell.rankingLabel.hidden = YES;
        [cell.crownIcon setImage:[UIImage imageNamed:@"yp_dacall_call_silvery"]];
       
    }
    else if (indexPath.row == 2) {
        cell.crownIcon.hidden = NO;
        cell.rankingLabel.hidden = YES;
        [cell.crownIcon setImage:[UIImage imageNamed:@"yp_dacall_call_orange"]];
       
    }
    else {
        cell.rankingLabel.hidden = NO;
        cell.crownIcon.hidden = YES;
       
    }
    cell.rankingLabel.text = [NSString stringWithFormat:@"%zd",indexPath.row+1];
    cell.rankingLabel.textColor = [UIColor colorWithHexString:@"#8A58FF"];
    if (indexPath.row%2 == 0) {
//        cell.cellBg.image = [UIImage imageNamed:@"yp_room_dacall_rank_cellBg"];
    }
    
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataArr.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 52.f;
}

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (self.dataArr.count == 0) {return;}
    YPGiftPurseRank *rank = self.dataArr[indexPath.row];
    if (rank) {
        [self avatarClick:rank.uid];
    }
}

#pragma mark - ImRoomCoreClientV2
//房间信息变更
- (void)onCurrentRoomInfoChanged{
    [self updateView];
}

#pragma mark - RoomCoreClient
- (void)userGiftPurseGetRankSuccessWithType:(NSInteger)type list:(NSMutableArray *)list {
    
//    [self.tableView.mj_header endRefreshing];
//    [self.tableView.mj_footer endRefreshing];
//
//    if (self.page == 1) {
//        self.dataArr = [NSMutableArray array];
//    }
//
//    [self.dataArr addObjectsFromArray:arr];
//    [self.tableView reloadData];
//
//    if (arr.count >= self.pageSize) {
//        self.tableView.mj_footer.hidden = NO;
//        self.tableView.mj_footer.state = MJRefreshStateIdle;
//    }
//    else {
//        self.tableView.mj_footer.hidden = YES;
//    }
    
    if (type == self.type) {
        self.dataArr = list;
        [self.tableView reloadData];
    }
}

- (void)userGiftPurseGetRankFailWithType:(NSInteger)type message:(NSString *)message {
//    [self.tableView.mj_header endRefreshing];
//    [self.tableView.mj_footer endRefreshing];
    [UIView showToastInKeyWindow:message duration:1 position:YYToastPositionCenter];
}

#pragma mark - private method
- (void)avatarClick:(UserID)uid {
    if(self.alertUserInformation) {
        self.alertUserInformation(uid);
    }
}
- (YPZJScrollSegmentView *)segmentView {
    if (!_segmentView) {
        YPZJSegmentStyle *style = [[YPZJSegmentStyle alloc] init];
        //显示滚动条
        style.showLine = YES;
        // 颜色渐变
        style.gradualChangeTitleColor = YES;
        style.normalTitleColor = UIColorHex(8A58FF);
        style.selectedTitleColor = UIColorHex(FFFFFC);;
        style.titleFont = [UIFont fontWithName:@"PingFang-SC-Medium" size:14];
        style.segmentViewComponent = SegmentViewComponentAdjustCoverOrLineWidth;
        style.scrollTitle = false;
        style.scrollLineColor = UIColorHex(65A5FF);
        style.titleMargin = 6;
        style.scrollLineHeight = 4;
        style.scrollLineImage = [UIImage imageNamed:@"yp_room_dacall_rank_slider"];
        style.scrollLineIsBackground = YES;
        
        @weakify(self);
        YPZJScrollSegmentView *segment = [[YPZJScrollSegmentView alloc] initWithFrame:CGRectMake(0, 0, 252, 34) segmentStyle:style delegate:nil titles:@[@"今日",@"昨日",@"周榜"] titleDidClick:^(YPZJTitleView *titleView, NSInteger index) {
            @strongify(self);
            NSInteger type = self.type;
            if (index == 0) {
                self.type = ZaJinDanRankListTypeToday;
            } else if (index == 1) {
                self.type = ZaJinDanRankListTypeYestday;
            } else {
                self.type = ZaJinDanRankListTypeWeek;
            }
            if (type != self.type) {
                self.dataArr = [NSMutableArray array];
                [self.tableView reloadData];
            }
            [self updateView];
        }];
        [segment setSelectedIndex:0 animated:NO];
        segment.backgroundColor = [UIColor whiteColor];
        segment.layer.cornerRadius = 17;
        segment.layer.masksToBounds = YES;
        segment.layer.borderWidth = 2;
        segment.layer.borderColor = [UIColor colorWithHexString:@"#D8C8FF"].CGColor;
        _segmentView = segment;
    }
    return _segmentView;
}



@end
