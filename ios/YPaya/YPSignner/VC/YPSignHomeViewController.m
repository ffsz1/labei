//
//  YPSignHomeViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPSignHomeViewController.h"

#import "YPSignerCell.h"
#import "YPSignSectionHeaderView.h"
#import "YPSignSectionFooterView.h"

#import "YPMMHomeInfoModel.h"

#import "YPHttpRequestHelper+Sign.h"

#import "YPJXMCDefines.h"

#import "YPShareView.h"
#import "YPAlertControllerCenter.h"
#import "HJShareCoreClient.h"

#import "YPWKWebViewController.h"

@interface YPSignHomeViewController ()<UITableViewDelegate,UITableViewDataSource,HJShareCoreClient>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (weak, nonatomic) IBOutlet UILabel *coinLabel;

//签到
@property (weak, nonatomic) IBOutlet GGButton *dayBtn1;
@property (weak, nonatomic) IBOutlet GGButton *dayBtn2;
@property (weak, nonatomic) IBOutlet GGButton *dayBtn3;
@property (weak, nonatomic) IBOutlet GGButton *dayBtn4;
@property (weak, nonatomic) IBOutlet GGButton *dayBtn5;
@property (weak, nonatomic) IBOutlet GGButton *dayBtn6;
@property (weak, nonatomic) IBOutlet GGButton *dayBtn7;
@property (weak, nonatomic) IBOutlet UIButton *signBtn;
@property (strong, nonatomic) IBOutlet UIView *ruleView;

@property (nonatomic, strong) YPMMHomeInfoModel *homeInfo;

@end

@implementation YPSignHomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.headerView.frame = CGRectMake(0, 0, kScreenWidth, kScreenWidth/375*563);
    self.tableView.tableHeaderView = self.headerView;
    
    [self.tableView registerNib:[UINib nibWithNibName:@"YPSignSectionHeaderView" bundle:nil] forHeaderFooterViewReuseIdentifier:@"YPSignSectionHeaderView"];
    
    [self.tableView registerClass:[YPSignSectionFooterView class] forHeaderFooterViewReuseIdentifier:@"YPSignSectionFooterView"];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self setShareItem];
    });
    
    AddCoreClient(HJShareCoreClient, self);
    
    [self checkFirstTime];
    
}

- (void)dealloc
{
    
    RemoveCoreClientAll(self);
}

- (void)checkFirstTime
{
    NSString *isFirst = [[NSUserDefaults standardUserDefaults]objectForKey:@"isShowSignRule"];
    if (![isFirst isEqualToString:@"1"]) {
        
        [[NSUserDefaults standardUserDefaults]setObject:@"1" forKey:@"isShowSignRule"];
        [[NSUserDefaults standardUserDefaults]synchronize];

        [self ruleBtnAction:nil];
        
    }
}

- (void)setShareItem
{
    
    BOOL hasWx = [WXApi isWXAppInstalled];
    BOOL hasQQ = ([TencentOAuth iphoneQQInstalled] || [TencentOAuth iphoneTIMInstalled]);
    
    if (!hasWx && !hasQQ) {
        return;
    }
    
    UIButton *shareBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [shareBtn setImage:[UIImage imageNamed:@"yp_sign_share"] forState:UIControlStateNormal];
    shareBtn.frame = CGRectMake(0, 0, 40, 40);
    [shareBtn addTarget:self action:@selector(shareAction) forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem * shareItem = [[UIBarButtonItem alloc] initWithCustomView:shareBtn];
    self.navigationItem.rightBarButtonItem = shareItem;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    YPBlackStatusBar
    
    [self getData];
}

#pragma mark <UITableViewDelegate,UITableViewDataSource>

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return self.homeInfo.beginnerMissions.count;
    }
    return self.homeInfo.dailyMissions.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 42;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    YPSignSectionHeaderView *header = (YPSignSectionHeaderView *)[tableView dequeueReusableHeaderFooterViewWithIdentifier:@"YPSignSectionHeaderView"];
    header.tipLabel.text = section == 0? @"新手任务": @"日常任务";
    return header;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return section==0?CGFLOAT_MIN:20;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    if (section == 0) {
        return [UIView new];
    }
    
    YPSignSectionFooterView *footer = (YPSignSectionFooterView *)[tableView dequeueReusableHeaderFooterViewWithIdentifier:@"YPSignSectionFooterView"];
    return footer;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 61;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    YPSignerCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPSignerCell" forIndexPath:indexPath];
    
    __weak typeof(self)weakSelf = self;
    cell.updateBlock = ^{
        [weakSelf getData];
    };
    
    if (indexPath.section == 0) {
        
        YPMMHomeItemModel *model = self.homeInfo.beginnerMissions[indexPath.row];
        cell.model = model;
    }else{
        YPMMHomeItemModel *model = self.homeInfo.dailyMissions[indexPath.row];
        cell.model = model;
    }
    
    return cell;
}




//立即签到
- (IBAction)signBtnAction:(id)sender {
    
    if (self.signBtn.selected) {
        return;
    }
    
    
}

//规则
- (IBAction)ruleBtnAction:(id)sender {
    
    self.ruleView.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight);
    [[UIApplication sharedApplication].keyWindow addSubview:self.ruleView];
    
}
- (IBAction)closeRuleAlter:(id)sender {
    
    [self.ruleView removeFromSuperview];
}

//点点pk
- (IBAction)pkAction:(id)sender {
    YPWKWebViewController *viewController = [[YPWKWebViewController alloc] init];
    NSString *URLString = [NSString stringWithFormat:@"%@/front/mbpk/index.html", [YPHttpRequestHelper getHostUrl]];
    viewController.url = [NSURL URLWithString:URLString];
    [self.navigationController pushViewController:viewController animated:YES];
}

//幸运点点抽
- (IBAction)rankAction:(id)sender {
    YPWKWebViewController *viewController = [[YPWKWebViewController alloc] init];
    NSString *URLString = [NSString stringWithFormat:@"%@/front/luck_issue/index.html", [YPHttpRequestHelper getHostUrl]];
    viewController.url = [NSURL URLWithString:URLString];
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)shareAction {

    YPShareInfo *shareInfo = [[YPShareInfo alloc] init];
    shareInfo.type = HJShareTypeNormol;
    shareInfo.desc = NSLocalizedString(ShareCoreDes2, nil);
    shareInfo.imgUrl = @"https://pic.hnyueqiang.com/logo.png";
    
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/download/download.html?",[YPHttpRequestHelper getHostUrl]];
    shareInfo.showUrl = urlSting;
    shareInfo.title = NSLocalizedString(ShareCoreTitle2, nil);
    
    [YPShareView show:shareInfo];
    
}


- (void)onShareH5Success
{
    [self getData];
}

- (void)updateData
{
    [self.tableView reloadData];
    self.coinLabel.text = self.homeInfo.mcoinNum;
    
    
    if (self.homeInfo.weeklyMissions.count==7) {
        for (int i = 0; i<self.homeInfo.weeklyMissions.count; i++) {
            YPMMHomeItemModel *model = self.homeInfo.weeklyMissions[i];
            JXMCHomeItemStatus status = [model.missionStatus integerValue];

            UIButton *btn = [self.view viewWithTag:3000+i];
            if (i!=6) {
                [btn setTitle:[NSString stringWithFormat:@"+%@",model.mcoinAmount] forState:UIControlStateNormal];
            }
            
            if (status == JXMCHomeItemStatusFinished) {
                btn.selected = YES;
            }else{
                btn.selected = NO;
            }
        }
    }
    
    
}

- (void)getData{
    
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper requestMengCoinListSuccess:^(YPMMHomeInfoModel * _Nonnull data) {
        weakSelf.homeInfo = data;
        [MBProgressHUD hideHUD];
        [weakSelf updateData];
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        [MBProgressHUD hideHUD];
        [weakSelf updateData];
    }];
}


@end
