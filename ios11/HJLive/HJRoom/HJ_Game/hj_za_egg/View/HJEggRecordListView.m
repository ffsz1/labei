//
//  HJEggRecordListView.m
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJEggRecordListView.h"

#import "HJHttpRequestHelper+Egg.h"
#import "UITableView+MJRefresh.h"

@implementation HJEggRecordListView

+ (void)showRecord
{
    [HJEggRecordListView loadXib];
}


+ (void)loadXib
{
    HJEggRecordListView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJEggRecordListView" owner:self options:nil].lastObject;
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    shareView.backgroundColor = [UIColor clearColor];
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    [shareView show];
    
    
    CGFloat height = -487;
    shareView.bottom_bgView.constant = height;
    
    //展示底部上浮动画
    [shareView layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        shareView.bottom_bgView.constant = -12;
        [shareView layoutIfNeeded];
    }];
    
    //展示底部下沉动画
    __weak typeof(shareView)weakView = shareView;
    shareView.dismissBlock = ^{
        [weakView layoutIfNeeded];
        [UIView animateWithDuration:0.3 animations:^{
            
            weakView.bottom_bgView.constant = height;
            
            [weakView layoutIfNeeded];
        }];
    };
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    self.dataArr = [[NSMutableArray alloc] init];
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    [self.tableView registerNib:[UINib nibWithNibName:@"HJEggRecordCell" bundle:[NSBundle mainBundle]] forCellReuseIdentifier:@"HJEggRecordCell"];
    [self getData];
    
    __weak typeof(self)weakSelf = self;
    self.tableView.headerBlock = ^{
        [weakSelf getData];
    };
    
    self.tableView.footerBlock = ^{
        [weakSelf getData];
    };
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 20;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.01;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 30;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view = [UIView new];
    view.backgroundColor = [UIColor clearColor];
    return view;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    HJEggRecordCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJEggRecordCell" forIndexPath:indexPath];
    if (indexPath.row<self.dataArr.count) {
        cell.model = self.dataArr[indexPath.row];
    }
    
    if (indexPath.row%2 == 0) {
        cell.cellBg.image = [UIImage imageNamed:@"hj_room_dacall_rank_cellBg"];
    }
    return cell;
}

- (void)getData
{
    __weak typeof(self)weakSelf = self;

    [HJHttpRequestHelper getEggRecord:self.tableView.page+1 success:^(NSArray * _Nonnull arr) {
        
        [weakSelf.tableView.mj_header endRefreshing];
        [weakSelf.tableView.mj_footer endRefreshing];

        if (weakSelf.tableView.page == 0) {
            weakSelf.dataArr = [NSMutableArray arrayWithArray:arr];
        }else{
            [weakSelf.dataArr addObjectsFromArray:arr];
        }
        
        [weakSelf.tableView reloadData];
        
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        [weakSelf.tableView.mj_header endRefreshing];
        [weakSelf.tableView.mj_footer endRefreshing];

    }];
}

- (IBAction)closeBtnAction:(id)sender {
    
    [self dismiss];
}

@end
