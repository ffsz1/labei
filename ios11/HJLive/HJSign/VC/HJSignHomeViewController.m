//
//  HJSignHomeViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSignHomeViewController.h"

#import "HJSignerCell.h"
#import "HJSignSectionHeaderView.h"
#import "HJSignSectionFooterView.h"

#import "HJMMHomeInfoModel.h"

#import "HJHttpRequestHelper+Sign.h"

#import "JXMCDefines.h"

#import "HJShareView.h"
#import "HJAlertControllerCenter.h"
#import "HJShareCoreClient.h"

#import "HJWKWebViewController.h"

@interface HJSignHomeViewController ()<UITableViewDelegate,UITableViewDataSource,HJShareCoreClient>

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

@property (nonatomic, strong) HJMMHomeInfoModel *homeInfo;

@end

@implementation HJSignHomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.headerView.frame = CGRectMake(0, 0, kScreenWidth, kScreenWidth/375*563);
    self.tableView.tableHeaderView = self.headerView;
    
    [self.tableView registerNib:[UINib nibWithNibName:@"HJSignSectionHeaderView" bundle:nil] forHeaderFooterViewReuseIdentifier:@"HJSignSectionHeaderView"];
    
    [self.tableView registerClass:[HJSignSectionFooterView class] forHeaderFooterViewReuseIdentifier:@"HJSignSectionFooterView"];
    
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
    [shareBtn setImage:[UIImage imageNamed:@"hj_sign_share"] forState:UIControlStateNormal];
    shareBtn.frame = CGRectMake(0, 0, 40, 40);
    [shareBtn addTarget:self action:@selector(shareAction) forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem * shareItem = [[UIBarButtonItem alloc] initWithCustomView:shareBtn];
    self.navigationItem.rightBarButtonItem = shareItem;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    HJBlackStatusBar
    
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
    HJSignSectionHeaderView *header = (HJSignSectionHeaderView *)[tableView dequeueReusableHeaderFooterViewWithIdentifier:@"HJSignSectionHeaderView"];
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
    
    HJSignSectionFooterView *footer = (HJSignSectionFooterView *)[tableView dequeueReusableHeaderFooterViewWithIdentifier:@"HJSignSectionFooterView"];
    return footer;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 61;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    HJSignerCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJSignerCell" forIndexPath:indexPath];
    
    __weak typeof(self)weakSelf = self;
    cell.updateBlock = ^{
        [weakSelf getData];
    };
    
    if (indexPath.section == 0) {
        
        HJMMHomeItemModel *model = self.homeInfo.beginnerMissions[indexPath.row];
        cell.model = model;
    }else{
        HJMMHomeItemModel *model = self.homeInfo.dailyMissions[indexPath.row];
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
    HJWKWebViewController *viewController = [[HJWKWebViewController alloc] init];
    NSString *URLString = [NSString stringWithFormat:@"%@/front/mbpk/index.html", [HJHttpRequestHelper getHostUrl]];
    viewController.url = [NSURL URLWithString:URLString];
    [self.navigationController pushViewController:viewController animated:YES];
}

//幸运点点抽
- (IBAction)rankAction:(id)sender {
    HJWKWebViewController *viewController = [[HJWKWebViewController alloc] init];
    NSString *URLString = [NSString stringWithFormat:@"%@/front/luck_issue/index.html", [HJHttpRequestHelper getHostUrl]];
    viewController.url = [NSURL URLWithString:URLString];
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)shareAction {

    HJShareInfo *shareInfo = [[HJShareInfo alloc] init];
    shareInfo.type = HJShareTypeNormol;
    shareInfo.desc = NSLocalizedString(ShareCoreDes2, nil);
    shareInfo.imgUrl = @"https://pic.hnyueqiang.com/logo.png";
    
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/download/download.html?",[HJHttpRequestHelper getHostUrl]];
    shareInfo.showUrl = urlSting;
    shareInfo.title = NSLocalizedString(ShareCoreTitle2, nil);
    
    [HJShareView show:shareInfo];
    
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
            HJMMHomeItemModel *model = self.homeInfo.weeklyMissions[i];
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
    [HJHttpRequestHelper requestMengCoinListSuccess:^(HJMMHomeInfoModel * _Nonnull data) {
        weakSelf.homeInfo = data;
        [MBProgressHUD hideHUD];
        [weakSelf updateData];
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        [MBProgressHUD hideHUD];
        [weakSelf updateData];
    }];
}


@end
