//
//  YPActivityViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPActivityViewController.h"
#import "YPActivityCell.h"
#import "YPActivityTabView.h"
#import "YPActivityCore.h"
#import "HJActivityCoreClient.h"
#import "YPAccountInfo.h"
#import "YPWKWebViewController.h"

#import "YPHttpRequestHelper+Activity.h"

@interface YPActivityViewController ()<UITableViewDelegate,UITableViewDataSource,HJActivityCoreClient>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSArray *arr;

@end

@implementation YPActivityViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    self.title = @"活动";
    
    [self.tableView registerNib:[UINib nibWithNibName:@"YPActivityCell" bundle:nil] forCellReuseIdentifier:@"YPActivityCell"];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.tableView registerClass:[YPActivityTabView class] forHeaderFooterViewReuseIdentifier:ActivityTabViewHeaderViewID];

    self.tableView.rowHeight = 100;
    
    
    AddCoreClient(HJActivityCoreClient, self);
//    [GetCore(YPActivityCore) getAllActivity];
    
    [self getNewActivitys];
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    YPBlackStatusBar
}
- (UIStatusBarStyle)preferredStatusBarStyle
{
//    return UIStatusBarStyleLightContent; //返回白色
    return UIStatusBarStyleDefault;    //返回黑色
}
- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)getNewActivitys
{
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper getNewActivity:^(NSArray *arr) {
        
        self.arr = arr;
        [self.tableView reloadData];
        
    } failure:^(NSNumber *code, NSString *msg) {
        
    }];
}

//- (void)getAllActivityInfoSuccess:(NSArray *)arr {
//    self.arr = arr;
//    [self.tableView reloadData];
//}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (section == 0) {
        return (self.arr.count > 0?1:0);
    }
        return self.arr.count - 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    YPActivityCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPActivityCell"];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    YPActivityInfo *info;
    if (indexPath.section == 0 && self.arr.count > 0) {
        info = [self.arr safeObjectAtIndex:0];
    } else {
        info = [self.arr safeObjectAtIndex:indexPath.row+1];
    }
    [cell.img sd_setImageWithURL:[NSURL URLWithString:info.alertWinPic] placeholderImage:[UIImage imageNamed:placeholder_image_square]];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    YPActivityInfo *info;
    if(indexPath.section == 0) {
        info = [self.arr safeObjectAtIndex:0];
    } else {
        info = [self.arr safeObjectAtIndex:indexPath.row+1];
    }
    YPWKWebViewController *vc = [[YPWKWebViewController alloc]init];
    NSString *url = [NSString stringWithFormat:@"%@?uid=%@",info.skipUrl,[GetCore(YPAuthCoreHelp)getUid]];
    vc.url = [NSURL URLWithString:url];
    [self.navigationController pushViewController:vc animated:YES];
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    YPActivityTabView *tabView = [tableView dequeueReusableHeaderFooterViewWithIdentifier:ActivityTabViewHeaderViewID];
    if (section == 0) {
        tabView.textLabel.text = @"最新活动";
    } else {
        tabView.textLabel.text = @"全部活动";
    }
    return tabView;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 30;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    return CGFLOAT_MIN;
}

@end
