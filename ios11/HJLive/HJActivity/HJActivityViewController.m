//
//  HJActivityViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJActivityViewController.h"
#import "HJActivityCell.h"
#import "HJActivityTabView.h"
#import "HJActivityCore.h"
#import "HJActivityCoreClient.h"
#import "AccountInfo.h"
#import "HJWKWebViewController.h"

#import "HJHttpRequestHelper+Activity.h"

@interface HJActivityViewController ()<UITableViewDelegate,UITableViewDataSource,HJActivityCoreClient>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSArray *arr;

@end

@implementation HJActivityViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    self.title = @"活动";
    
    [self.tableView registerNib:[UINib nibWithNibName:@"HJActivityCell" bundle:nil] forCellReuseIdentifier:@"HJActivityCell"];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.tableView registerClass:[HJActivityTabView class] forHeaderFooterViewReuseIdentifier:ActivityTabViewHeaderViewID];

    self.tableView.rowHeight = 100;
    
    
    AddCoreClient(HJActivityCoreClient, self);
//    [GetCore(HJActivityCore) getAllActivity];
    
    [self getNewActivitys];
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    HJBlackStatusBar
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
    [HJHttpRequestHelper getNewActivity:^(NSArray *arr) {
        
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
    HJActivityCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJActivityCell"];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    ActivityInfo *info;
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
    ActivityInfo *info;
    if(indexPath.section == 0) {
        info = [self.arr safeObjectAtIndex:0];
    } else {
        info = [self.arr safeObjectAtIndex:indexPath.row+1];
    }
    HJWKWebViewController *vc = [[HJWKWebViewController alloc]init];
    NSString *url = [NSString stringWithFormat:@"%@?uid=%@",info.skipUrl,[GetCore(HJAuthCoreHelp)getUid]];
    vc.url = [NSURL URLWithString:url];
    [self.navigationController pushViewController:vc animated:YES];
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    HJActivityTabView *tabView = [tableView dequeueReusableHeaderFooterViewWithIdentifier:ActivityTabViewHeaderViewID];
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
