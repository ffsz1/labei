//
//  YPFamilyMessageController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyMessageController.h"
#import "YPXCFamilyMessageCell.h"

#import "UIImage+_1x1Color.h"
#import "HJFamilyCoreClient.h"
#import "YPFamilyCore.h"
#import "UIView+XCToast.h"

#import "YPFamilyMessage.h"

@interface YPFamilyMessageController ()<UITableViewDataSource,UITableViewDelegate,HJFamilyCoreClient>

@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) NSMutableArray *dataArr;

@property (nonatomic, assign) NSInteger erbanNo;

@end

@implementation YPFamilyMessageController

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    AddCoreClient(HJFamilyCoreClient, self);
    
    self.title = @"家族消息";
    
    [self.navigationController.navigationBar setBackgroundImage:[UIImage instantiate1x1ImageWithColor:[UIColor whiteColor]] forBarMetrics:UIBarMetricsDefault];
    self.view.backgroundColor = [UIColor whiteColor];
    [self setupHairlineImageViewHidden:NO];
    
    [self.view addSubview:self.tableView];
    
//    [GetCore(FamilyCore) familyGetFamilyMessageWithFamilyId:GetCore(FamilyCore).myFamily.familyId];
}

#pragma mark - FamilyCoreClient
// 家族消息
- (void)familyGetFamilyMessageSuccessWithDataArr:(NSArray *)dataArr {
    self.dataArr = [NSMutableArray arrayWithArray:dataArr];
    
    [self.tableView reloadData];
    
    if (self.dataArr.count == 0) {
        [self.view showEmptyContentToastWithTitle:@"暂无家族消息" andImage:[UIImage imageNamed:@"blank"]];
    }else{
        [self.view hideToastView];
    }
}
- (void)familyGetFamilyMessageFailedWithMessage:(NSString *)message {
    [self.tableView showEmptyContentToastWithTitle:@"暂无家族消息" andImage:[UIImage imageNamed:@"blank"]];
}


- (void)familyApplyFamilySuccess {
    
    [MBProgressHUD hideHUD];

    for (YPFamilyMessage *msg in self.dataArr) {
        if (msg.erbanNo == self.erbanNo) {
//            msg.isHandle = YES;
            break;
        }
    }
    self.erbanNo = 0;
    [self.tableView reloadData];
}

- (void)familyApplyFamilyFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
    self.erbanNo = 0;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - UITableViewDataSource && UITableViewDelegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    YPXCFamilyMessageCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPXCFamilyMessageCell"];
    
    YPFamilyMessage *model = self.dataArr[indexPath.row];
    cell.model = model;
    
    @weakify(self);
    [cell setAgreeBtnActionBlock:^{
        @strongify(self);
        if (self.erbanNo == 0) {
            [MBProgressHUD showMessage:@""];
            self.erbanNo = model.erbanNo;
            [GetCore(YPFamilyCore) familyApplyFamilyWithFamilyId:GetCore(YPFamilyCore).myFamily.familyId userId:[@(model.uid) description] type:model.type status:1];
        }
    }];
    
    [cell setRefuseBtnActionBlock:^{
        @strongify(self);
        if (self.erbanNo == 0) {
            [MBProgressHUD showMessage:@""];
            self.erbanNo = model.erbanNo;
            [GetCore(YPFamilyCore) familyApplyFamilyWithFamilyId:GetCore(YPFamilyCore).myFamily.familyId userId:[@(model.uid) description] type:model.type status:2];
        }
    }];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (UITableView *)tableView {
    
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H - XC_Height_NavBar) style:UITableViewStylePlain];
        _tableView.dataSource = self;
        _tableView.delegate = self;
        _tableView.separatorColor = UIColorHex(F7F7F7);
        _tableView.separatorInset = UIEdgeInsetsMake(0, 15.f, 0, 0);
        _tableView.rowHeight = 80.f;
        _tableView.tableFooterView = [UIView new];
        [_tableView registerNib:[UINib nibWithNibName:@"YPXCFamilyMessageCell" bundle:[NSBundle mainBundle]] forCellReuseIdentifier:@"YPXCFamilyMessageCell"];
    }
    return _tableView;
}

@end
