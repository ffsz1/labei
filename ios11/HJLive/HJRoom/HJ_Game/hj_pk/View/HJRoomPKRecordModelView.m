//
//  HJRoomPKRecordModelView.m
//  HJLive
//
//  Created by apple on 2019/6/19.
//

#import "HJRoomPKRecordModelView.h"

#import "HJRoomPKRecordModelCell.h"

#import "HJRoomPKRecordModel.h"

#import "HJHttpRequestHelper+PK.h"
#import "UITableView+MJRefresh.h"


@interface HJRoomPKRecordModelView ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic,strong) NSMutableArray *dataArr;

@end

@implementation HJRoomPKRecordModelView

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        

        
        
        
       
        
        [self getRecordData];
        
    }
    return self;
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self.tableView registerNib:[UINib nibWithNibName:@"HJRoomPKRecordModelCell" bundle:[NSBundle mainBundle]] forCellReuseIdentifier:@"HJRoomPKRecordModelCell"];
    
    __weak typeof(self)weakSelf = self;
    self.tableView.headerBlock = ^{
        [weakSelf getRecordData];
    };
    
    self.tableView.footerBlock = ^{
        [weakSelf getRecordData];
    };
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 80;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (self.dataArr.count == 0) {
        return [UITableViewCell new];
    }
    
    HJRoomPKRecordModelCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJRoomPKRecordModelCell" forIndexPath:indexPath];
    
    if (indexPath.row<self.dataArr.count) {
        cell.model = self.dataArr[indexPath.row];
    }
    
    return cell;
}

- (IBAction)closeBtnAction:(id)sender {
    self.closeBlock();
}

- (void)getRecordData
{
    __weak typeof(self)ws = self;
    [HJHttpRequestHelper pk_record:self.tableView.page success:^(NSArray *arr) {
        
        [ws.tableView.mj_header endRefreshing];
        [ws.tableView.mj_footer endRefreshing];

        if (ws.tableView.page == 0) {
            ws.dataArr = [[NSMutableArray alloc] init];
        }
        
        NSArray *tmpArr = [NSArray yy_modelArrayWithClass:[HJRoomPKRecordModel class] json:arr];
        
        [ws.dataArr addObjectsFromArray:tmpArr];
        [ws.tableView reloadData];
        
        if (arr.count<20) {
            [ws.tableView.mj_footer endRefreshingWithNoMoreData];
        }
        
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        [ws.tableView.mj_header endRefreshing];
        [ws.tableView.mj_footer endRefreshing];
    }];
}


@end
