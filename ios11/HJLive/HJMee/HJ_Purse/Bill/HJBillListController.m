//
//  HJBillListController.m
//  HJLive
//
//  Created by feiyin on 2020/6/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBillListController.h"
#import <MJRefresh.h>

#import "PurseBillCore.h"
#import "HJPurseBillCoreClient.h"

#import "GiftBillInfo.h"
#import "HJChatBillInfo.h"
#import "HJWithDrawlBillInfo.h"
#import "HJRedBillInfo.h"
#import "HJRechargeBillInfo.h"

#import "PurseCore.h"

#import "HJPurseViewControllerFactory.h"

#import "HJGiftBillCell.h"
#import "HJChatBillCell.h"
#import "HJWithdrawalOrRechargeCell.h"

#import "PLTimeUtil.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "YYTheme.h"
#import "UIColor+UIColor_Hex.h"

#import "TYAlertController.h"
#import "XCDatePicker.h"

#import "UIView+XCToast.h"

@interface HJBillListController ()<UITableViewDelegate, UITableViewDataSource, HJPurseBillCoreClient, XCDatePickerDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (strong, nonatomic)NSMutableArray<NSMutableArray *> *disPlayArr;
@property (strong, nonatomic)NSMutableArray *keysArr;
@property (assign, nonatomic) NSInteger page; //请求页数
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottomViewHeightConstraint;
@property (weak, nonatomic) IBOutlet UILabel *coinNumLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *topToolBarHeightConstraint;
@property (weak, nonatomic) IBOutlet UIView *topToolBar;
@property (weak, nonatomic) IBOutlet UIView *bottomBar;

@property (strong, nonatomic) XCDatePicker *datePicker;
@property (strong, nonatomic) TYAlertController *datePickerAlert;
@property (weak, nonatomic) IBOutlet UILabel *selectedDateLabel;
@property (weak, nonatomic) IBOutlet UIButton *todayBtn;



@property (assign, nonatomic)NSInteger pageCount;
@property (assign, nonatomic)NSInteger listCount; //返回的list中的元素个数
@end

@implementation HJBillListController


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.page = 1;
    AddCoreClient(HJPurseBillCoreClient, self);
    [self initView];
    
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
//    [self reloadTableViewData];
}

- (UIStatusBarStyle)preferredStatusBarStyle
{
//    return UIStatusBarStyleLightContent; //返回白色
    return UIStatusBarStyleDefault;    //返回黑色
}

- (void)initView {
//    NSDate *date = [NSDate date];
    //self.sortTiemStamp = 0;
    if (self.keysArr.count > 0) {
        self.selectedDateLabel.text = self.keysArr[0];
    }
//    if (@available(iOS 11.0, *)) {
//        self.tableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
//    } else {
//        self.automaticallyAdjustsScrollViewInsets = NO;
//    }
    if (self.type == BillType_GiftOut) { //礼物支出
        self.title = NSLocalizedString(XCPurseBillGiftOut, nil);
        [self.tableView registerNib:[UINib nibWithNibName:@"HJGiftBillCell" bundle:nil] forCellReuseIdentifier:@"HJGiftBillCell"];
        [self addRechargeBarButtonItem];
        self.bottomViewHeightConstraint.constant = 0;
        self.topToolBarHeightConstraint.constant = 40;
//        self.bottomBar.hidden = NO;
        self.topToolBar.hidden = NO;
        self.coinNumLabel.text = [NSString stringWithFormat:@"%@:%@%@",NSLocalizedString(XCPurseMoney, nil),GetCore(PurseCore).balanceInfo.goldNum,NSLocalizedString(XCPurseCorn, nil)];
    } else if (self.type == BillType_GiftIn ) {//BillType_daiChong
        self.title = NSLocalizedString(XCPurseBillGiftIn, nil);
        [self.tableView registerNib:[UINib nibWithNibName:@"HJGiftBillCell" bundle:nil] forCellReuseIdentifier:@"HJGiftBillCell"];
        [self addRechargeBarButtonItem];
        self.topToolBarHeightConstraint.constant = 40;
        self.bottomViewHeightConstraint.constant = 0;
        self.bottomBar.hidden = NO;
        self.topToolBar.hidden = NO;
        self.coinNumLabel.text = [NSString stringWithFormat:@"%@:%@%@",NSLocalizedString(XCPurseMoney, nil),GetCore(PurseCore).balanceInfo.goldNum,NSLocalizedString(XCPurseCorn, nil)];
    } else if (self.type == BillType_Recharge || self.type == BillType_Withdraw ||self.type == BillType_RedWithdraw||self.type == BillType_daiChong) {
//        self.title = NSLocalizedString(XCPurseBillInCharge, nil);
        [self.tableView registerNib:[UINib nibWithNibName:@"HJWithdrawalOrRechargeCell" bundle:nil] forCellReuseIdentifier:@"HJWithdrawalOrRechargeCell"];
        if (self.type == BillType_daiChong) {
            
        }else{
              [self addRechargeBarButtonItem];
        }
      
        self.bottomViewHeightConstraint.constant = 0;
        self.topToolBarHeightConstraint.constant = 40;
//        self.bottomBar.hidden = NO;
        self.topToolBar.hidden = NO;
        self.coinNumLabel.text = [NSString stringWithFormat:@"%@:%@%@",NSLocalizedString(XCPurseMoney, nil),GetCore(PurseCore).balanceInfo.goldNum,NSLocalizedString(XCPurseCorn, nil)];
    }
    
    
    
    
    [self initMJRefresh];
//    self.tableView.mj_footer.hidden =  YES;
    [self reloadTableViewDataWithPageNo:1];
//    self.tableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenWidth, 1)];
    self.tableView.estimatedRowHeight= 82.0;
    self.tableView.rowHeight=UITableViewAutomaticDimension;//高度设置为自适应
    
    
}

- (void)initMJRefresh {
    @weakify(self);
    MJRefreshNormalHeader *header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        @strongify(self);
        self.sortTiemStamp = 0;
        if (self.keysArr.count > 0) {
            self.selectedDateLabel.text = self.keysArr[0];
        }
        [self.tableView.mj_footer endRefreshing];
        [self reloadTableViewDataWithPageNo:1];
        
        if ([_delegate respondsToSelector:@selector(resetDateLabel)]) {
            [_delegate resetDateLabel];
        }
        [self.tableView.mj_footer setHidden:NO];
    }];
    header.lastUpdatedTimeLabel.hidden = YES;
    header.stateLabel.hidden = YES;
    self.tableView.mj_header = header;
    

   MJRefreshBackNormalFooter *footer = [MJRefreshBackNormalFooter footerWithRefreshingBlock:^{
        @strongify(self);
        if (self.listCount != 0) {
            [self reloadTableViewDataWithPageNo:self.page + 1];
        } else {
            [MBProgressHUD showError:NSLocalizedString(XCPurseBillAlreadyNoData, nil)];
            [self.tableView.mj_footer endRefreshing];
//            [self.tableView.mj_footer setHidden:YES];
        }
        
    }];
//    footer.automaticallyHidden = YES;
    self.tableView.mj_footer = footer;
}

- (void)reloadTableViewDataWithPageNo:(NSInteger)pageNo {
    if (self.type == BillType_GiftOut) { //礼物支出
        [GetCore(PurseBillCore)getOutGiftListPageNo:pageNo time:self.sortTiemStamp pageSize:50];
    } else if (self.type == BillType_GiftIn) {
        [GetCore(PurseBillCore)getInGiftListPageNo:pageNo time:self.sortTiemStamp pageSize:50];
    } else if (self.type == BillType_Recharge) {
        [GetCore(PurseBillCore)getRechargeListPageNo:pageNo time:self.sortTiemStamp pageSize:50];
    }else if (self.type == BillType_Withdraw) {//提现
         [GetCore(PurseBillCore)getWithdrawListPageNo:pageNo time:self.sortTiemStamp pageSize:50];
    }else if (self.type == BillType_RedWithdraw) {//红包提现
//         [GetCore(PurseBillCore)getRedWithdrawListPageNo:pageNo time:self.sortTiemStamp pageSize:50];
    }else if (self.type == BillType_daiChong) {//代充记录
//        NSString *uid = [GetCore(XBDAuthCore)getUid];
//           [GetCore(PurseBillCore) getExchangeRecordWithUid:uid type:2 PageNo:pageNo pageSize:50];//type:1 钻石兑换开心记录 ,2:钻石转赠开心记录
    }
}

- (void)addRechargeBarButtonItem {
//    UIBarButtonItem *rightBarButtonItem = [[UIBarButtonItem alloc]initWithTitle:NSLocalizedString(XCPurseRecharge, nil) style:UIBarButtonItemStylePlain target:self action:@selector(jumpToRechargeVC)];
//    self.navigationItem.rightBarButtonItem = rightBarButtonItem;
//    UIBarButtonItem *rightBarButtonItem = [[UIBarButtonItem alloc]initWithImage:[UIImage imageNamed:@"hj_bill_icon_rili"] style:UIBarButtonItemStylePlain target:self action:@selector(popupCalendar)];
//    self.navigationItem.rightBarButtonItem = rightBarButtonItem;
    
    UIButton *shareBtn = [UIButton buttonWithType:UIButtonTypeCustom];
          [shareBtn setImage:[UIImage imageNamed:@"hj_bill_icon_rili"] forState:UIControlStateNormal];
          shareBtn.frame = CGRectMake(0, 0, 40, 40);
          [shareBtn addTarget:self action:@selector(popupCalendar) forControlEvents:UIControlEventTouchUpInside];
          UIBarButtonItem * shareItem = [[UIBarButtonItem alloc] initWithCustomView:shareBtn];
          self.navigationItem.rightBarButtonItem = shareItem;
    
    
}

//弹出日历
-(void)popupCalendar{
    self.datePicker = [XCDatePicker loadFromNib];
    self.datePicker.delegate = self;
    self.datePickerAlert = [TYAlertController alertControllerWithAlertView:self.datePicker preferredStyle:TYAlertControllerStyleActionSheet transitionAnimation:TYAlertTransitionAnimationFade];
    
    if (self.keysArr.count > 0) {
        self.datePicker.dateStr = self.keysArr[0];
    }
    
    self.datePickerAlert.backgoundTapDismissEnable = YES;
    [self.navigationController presentViewController:self.datePickerAlert animated:YES completion:nil];
    
}

- (void)jumpToRechargeVC {
    UIViewController *vc = [[HJPurseViewControllerFactory sharedFactory]instantiateHJMyWalletVC];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
}

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80;
//    return 82;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
//    if (section == 0) {
//        return 0.01;
//    }
    return 42;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    return 0.01;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
//    if (section == 0) {
//        return nil;
//    }
        
    return _keysArr[section];
}


-(void)tableView:(UITableView *)tableView willDisplayHeaderView:(UIView *)view forSection:(NSInteger)section

{

view.tintColor = [UIColor whiteColor];

UITableViewHeaderFooterView *header = (UITableViewHeaderFooterView *)view;

    header.contentView.backgroundColor= [UIColor colorWithHexString:@"#FAFAFA"];

//header.textLabel.textAlignment=NSTextAlignmentCenter;

[header.textLabel setTextColor:[UIColor colorWithHexString:@"#999999"]];

}





#pragma mark - UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    if (self.disPlayArr.count == self.keysArr.count) {
        return self.keysArr.count;
    }else {
        return 0;
    }
   
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    if (self.disPlayArr.count == self.keysArr.count) {
        return self.disPlayArr[section].count;
    }else {
        return 0;
    }
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (self.type == BillType_GiftIn) {
        HJGiftBillCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJGiftBillCell"];
        GiftBillInfo *info = self.disPlayArr[indexPath.section][indexPath.row];
        cell.giftIcon.hidden = YES;
        [cell.giftLogoImageView sd_setImageWithURL:[NSURL URLWithString:info.giftPict] placeholderImage:nil];

        cell.giftNameLabel.text = [NSString stringWithFormat:@"%@",info.giftName];
        
        cell.timeLabel.text = [PLTimeUtil getDateWithHHMMSS:[NSString stringWithFormat:@"%ld",(long)info.recordTime]];
        cell.giftSenderNameLabel.text = [NSString stringWithFormat:@"%@:%@",NSLocalizedString(XCPurseBillSendingSide, nil),info.targetNick];
        cell.countLabel.text = [NSString stringWithFormat:@"+%@ 钻石",info.diamondNum];
        cell.countLabel.textColor = UIColorHex(EE47B7);
//        cell.countLabel.textColor = UIColorHex(18B0FE);
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
        
    } else if (self.type == BillType_GiftOut) {
        HJGiftBillCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJGiftBillCell"];
        GiftBillInfo *info = self.disPlayArr[indexPath.section][indexPath.row];
        cell.giftIcon.hidden = YES;
        [cell.giftLogoImageView sd_setImageWithURL:[NSURL URLWithString:info.giftPict] placeholderImage:nil];
        cell.giftNameLabel.text = [NSString stringWithFormat:@"%@",info.giftName];
        cell.timeLabel.text = [PLTimeUtil getDateWithHHMMSS:[NSString stringWithFormat:@"%ld",(long)info.recordTime]];
        cell.giftSenderNameLabel.text = [NSString stringWithFormat:@"%@:%@",NSLocalizedString(XCPurseBillRecieveSide, nil),info.targetNick];
        cell.countLabel.text = [NSString stringWithFormat:@"-%@ 开心",info.goldNum];
        cell.countLabel.textColor = UIColorHex(EE47B7);
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
        
    }else if (self.type == BillType_Recharge) {//充值
        HJWithdrawalOrRechargeCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJWithdrawalOrRechargeCell"];
        HJRechargeBillInfo *info = self.disPlayArr[indexPath.section][indexPath.row];
        cell.titleLabel.text = info.showStr;
        cell.countLabel.text = [NSString stringWithFormat:@"+%ld %@",labs(info.goldNum.integerValue),NSLocalizedString(XCPurseCorn, nil)];
        cell.countLabel.textColor = UIColorHex(EE47B7);
        cell.subTitleLabel.text = [PLTimeUtil getDateWithHHMMSS:[NSString stringWithFormat:@"%ld",(long)info.recordTime]];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
        
    }else if(self.type == BillType_Withdraw){
        HJWithdrawalOrRechargeCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJWithdrawalOrRechargeCell"];
        HJRechargeBillInfo *info = self.disPlayArr[indexPath.section][indexPath.row];
        cell.titleLabel.text = [NSString stringWithFormat:@"提现 %@钻石",info.diamondNum];
        cell.countLabel.text = [NSString stringWithFormat:@"+%@%@",info.money,NSLocalizedString(@"元", nil)];
        cell.countLabel.textColor = UIColorHex(EE47B7);
        cell.subTitleLabel.text = [PLTimeUtil getDateWithHHMMSS:[NSString stringWithFormat:@"%ld",(long)info.recordTime]];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
        
    }else if(self.type == BillType_RedWithdraw){
       HJWithdrawalOrRechargeCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJWithdrawalOrRechargeCell"];
              HJRedBillInfo *info = self.disPlayArr[indexPath.section][indexPath.row];
              cell.titleLabel.text = info.typeStr;
              cell.subTitleLabel.text = [PLTimeUtil getDateWithHHMMSS:[NSString stringWithFormat:@"%ld",(long)info.recordTime]];
              cell.countLabel.text = [NSString stringWithFormat:@"%.2lf 元",[info.packetNum doubleValue]];
              cell.countLabel.textColor = UIColorHex(EE47B7);
              cell.selectionStyle = UITableViewCellSelectionStyleNone;
              return cell;
        
    }else if (self.type == BillType_daiChong) {//代充记录
        HJWithdrawalOrRechargeCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJWithdrawalOrRechargeCell"];
        HJRechargeBillInfo *info = self.disPlayArr[indexPath.section][indexPath.row];
        cell.titleLabel.text = [NSString stringWithFormat:@"%@ (ID:%ld) ",info.receiveUserNick,info.receiveUserErnos];
        cell.countLabel.text = [NSString stringWithFormat:@"+%ld %@",info.exGoldNum,NSLocalizedString(XCPurseCorn, nil)];
        cell.countLabel.textColor = UIColorHex(EE47B7);
        cell.subTitleLabel.text = [PLTimeUtil getDateWithHHMMSS:[NSString stringWithFormat:@"%ld",(long)info.createTime]];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
        
    }  else  {
        HJWithdrawalOrRechargeCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJWithdrawalOrRechargeCell"];
        HJRedBillInfo *info = self.disPlayArr[indexPath.section][indexPath.row];
        cell.titleLabel.text = info.typeStr;
        cell.subTitleLabel.text = [PLTimeUtil getDateWithHHMMSS:[NSString stringWithFormat:@"%ld",(long)info.recordTime]];
        cell.countLabel.text = [NSString stringWithFormat:@"%.2lf 元",[info.packetNum doubleValue]];
        cell.countLabel.textColor = UIColorHex(EE47B7);
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
    }
}

#pragma mark - Getter

- (NSInteger)sortTiemStamp {
    if (_sortTiemStamp == 0) {
        _sortTiemStamp = [self getNowTimeTimestampMillisecond];
    }
    return _sortTiemStamp;
}

#pragma mark - Private Method

- (void)reloadDateByDate:(NSDate *)date {
    NSInteger timeSp = [[NSNumber numberWithDouble:[date timeIntervalSince1970]] integerValue] *1000;
    self.sortTiemStamp = timeSp;
    self.page = 1;
    if (self.type == BillType_GiftOut) { //礼物支出
        [GetCore(PurseBillCore)getOutGiftListPageNo:1 time:timeSp pageSize:50];
    } else if (self.type == BillType_GiftIn) {
        [GetCore(PurseBillCore)getInGiftListPageNo:1 time:timeSp pageSize:50];
    } else if (self.type == BillType_Recharge) {
        [GetCore(PurseBillCore)getRechargeListPageNo:1 time:timeSp pageSize:50];
    }else if (self.type == BillType_Withdraw) {//提现
         [GetCore(PurseBillCore)getWithdrawListPageNo:1 time:self.sortTiemStamp pageSize:50];
    }else if (self.type == BillType_RedWithdraw) {//红包提现
//         [GetCore(PurseBillCore)getRedWithdrawListPageNo:1 time:self.sortTiemStamp pageSize:50];
    }else if (self.type == BillType_daiChong) {//代充记录
//        NSString *uid = [GetCore(XBDAuthCore)getUid];
//           [GetCore(PurseBillCore) getExchangeRecordWithUid:uid type:2 PageNo:1 pageSize:50];//type:1 钻石兑换开心记录 ,2:钻石转赠开心记录
    }
    
}



#pragma mark - XCDatePickerDelegate

- (void)dismissDatePicker {
    [self.datePickerAlert dismissViewControllerAnimated:YES];
}

- (void)hadSelectedADate:(NSDate *)date {
//    if (self.keysArr.count > 0) {
//        self.selectedDateLabel.text = self.keysArr[0];
//    }
    
    NSString *dateStr = [date stringWithFormat:@"YYYY年MM月dd日"];
    self.selectedDateLabel.text = dateStr;
    
    [self reloadDateByDate:date];
    [self.datePickerAlert dismissViewControllerAnimated:YES];
    //重新刷新
}

- (NSInteger)getNowTimeTimestampMillisecond {
    return [[PLTimeUtil getNowTimeTimestampMillisecond]integerValue];
}

#pragma mark - PurseBillCoreClient

- (void)getOutGiftListFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}

- (void)getOutGiftListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo {
    if (self.type == BillType_GiftOut) {
       // self.pageCount = pageCount;
        [self combineDatelist:list keys:keys pageNo:pageNo];
    }
}

- (void)getInGiftListFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}

- (void)getInGiftListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo{
    if (self.type == BillType_GiftIn) {
        //self.pageCount = pageCount;
        [self combineDatelist:list keys:keys pageNo:pageNo];
    }
}


- (void)getChatListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo{
    
}
- (void)getChatListFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}

- (void)getRechargeListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo{
    if (self.type == BillType_Recharge) {
       // self.pageCount = pageCount;
        [self combineDatelist:list keys:keys pageNo:pageNo];
    }
   
}

- (void)getRechargeListFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}

- (void)getWithdrawlListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo{
     [self combineDatelist:list keys:keys pageNo:pageNo];//life
}

- (void)getWithdrawlListFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}


- (void)getRedWithdrawlListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo{
     [self combineDatelist:list keys:keys pageNo:pageNo];//life
}

- (void)getRedWithdrawlListFailth:(NSString *)message {
    [MBProgressHUD showError:message];
//    [MBProgressHUD hideHUD];
}

//代充记录 success
- (void)getExchangeRecordSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo{
     [self combineDatelist:list keys:keys pageNo:pageNo];//life
}
//代充记录 fail
- (void)getExchangeRecordFailth:(NSString *)message{
     [MBProgressHUD showError:message];
}


- (void)getRedGiftListSuccess:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo{

}

- (void)getRedGiftListFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}


//整理数据
- (void)combineDatelist:(NSMutableArray *)list keys:(NSMutableArray *)keys pageNo:(NSInteger)pageNo {
    if (pageNo == 1) {
        if (list.count > 0) {
            self.page = 1;
            self.keysArr = keys;
            self.disPlayArr = list;
            self.listCount = list.count;
            [self.tableView.mj_header endRefreshing];
            [self.tableView.mj_footer endRefreshing];
            [self.tableView hideToastView];
            if (self.keysArr.count > 0) {
                self.selectedDateLabel.text = self.keysArr[0];
            }
        } else {
            [self.tableView.mj_header endRefreshing];
            [self.disPlayArr removeAllObjects];
            [self.tableView reloadData];
            [self.tableView showEmptyContentToastWithTitle:NSLocalizedString(XCPurseBillNoData, nil) andImage:[UIImage imageNamed:@"fy_blank"]];
        }
    } else if (pageNo > self.page) {
        self.listCount = list.count;
        if (list.count == 0) {
            self.page = self.page - 1;
        } else {
            self.page = self.page + 1;
        }
        //如果返回的第一个keys跟最后一个keys相同，那么就是说还是同一天的数据，需要合并数组
        if ([keys.firstObject isEqualToString:self.keysArr.lastObject]) {
            [keys removeObjectAtIndex:0];
            [self.disPlayArr.lastObject addObjectsFromArray:list.firstObject];
            [list removeObjectAtIndex:0];
            if (list.count > 0) {
                [self.disPlayArr addObjectsFromArray:list];
            }
            if (keys.count > 0) {
                [self.keysArr addObjectsFromArray:keys];
            }
        } else {
            [self.disPlayArr addObjectsFromArray:list];
            [self.keysArr addObjectsFromArray:keys];
        }
        
        
        [self.tableView.mj_footer endRefreshing];
        
    }
    [self.tableView reloadData];
    
    if (list.count >= 50) {
        self.tableView.mj_footer.hidden = NO;
    }
    else {
//        self.tableView.mj_footer.hidden = YES;
    }
}

-(IBAction)todayBtnClicked:(id)sender{
//    self.sortTiemStamp = 0;
//    if (self.keysArr.count > 0) {
//        self.selectedDateLabel.text = self.keysArr[0];
//    }
//    [self reloadTableViewDataWithPageNo:1];
//    [self initMJRefresh];
//    NSLog(@"哈哈哈哈哈");
    
    [self.tableView scrollToTop];
}

- (NSString *)getYYMMDDWithDate:(NSDate *)date {
    //    NSTimeZone *zome = [NSTimeZone systemTimeZone];
    //    NSTimeInterval seconds1 = [zome secondsFromGMTForDate:date];
    //    NSDate *date2 = [date dateByAddingTimeInterval:seconds1];
    NSDateFormatter *formatter = [[NSDateFormatter alloc]init];
    NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"Asia/Beijing"];
    [formatter setTimeZone:timeZone];
    formatter.dateFormat = @"yyyy年MM月dd日";
    NSString *dateStr = [formatter stringFromDate:date];
    return dateStr;
}


//


@end
