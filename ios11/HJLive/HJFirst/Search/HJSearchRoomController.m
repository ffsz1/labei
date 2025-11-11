//
//  HJSearchRoomController.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSearchRoomController.h"
#import "HJMySpaceVC.h"
#import "HJRoomViewControllerFactory.h"
#import "HJRoomViewControllerCenter.h"
#import "HJUserViewControllerFactory.h"
#import "YYViewControllerCenter.h"

//m
#import "HJSearchResultInfo.h"
//v
#import "HJSearchResultCell.h"
//core
#import "HJSearchCoreHelp.h"
#import "HJSearchCoreClient.h"
//tool
#import "UIView+XCToast.h"
#import "UIImage+extension.h"
#import "YYDefaultTheme.h"
#import <SDWebImageManager.h>
#import "HJSearchTagView.h"

#import "HJSearchHistoryHeaderView.h"
#import "HJSearchHistoryFooterView.h"
#import "HJSearchHistoryTableCell.h"

@interface HJSearchRoomController ()
<
    UITextFieldDelegate,
    HJSearchCoreClient,
    SearchTagViewDelegate
>

@property (weak, nonatomic) IBOutlet UITextField *searchTextField;
@property (weak, nonatomic) IBOutlet UIView *searchContainerView;
@property (copy, nonatomic) NSArray<HJSearchResultInfo *> *resultList;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, assign) int inputCount;
@property (weak, nonatomic) IBOutlet HJSearchTagView *tagView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *tagViewHeightCons;
@property (weak, nonatomic) IBOutlet UIView *BottomTagView;
@property (copy, nonatomic) NSString *tagName;

@property (nonatomic, strong) NSArray *calculateTagArr;

@property (nonatomic, assign) BOOL isShowHistory;
@property (nonatomic, assign) BOOL isShowing;
@property (nonatomic, copy) NSString *currentText;
@end

@implementation HJSearchRoomController

#pragma mark - life cycle
- (void)viewDidLoad {
    [super viewDidLoad];
    //做用户区分
    NSString *uid = [GetCore(HJAuthCoreHelp) getUid];
    self.tagName = [NSString stringWithFormat:@"%@_userSearchHistory",uid];
    self.isShowHistory = YES;
    AddCoreClient(HJSearchCoreClient, self);
    self.tagView.delegate = self;
    [self initView];
    
    
    @weakify(self);
    [self.searchTextField.rac_textSignal subscribeNext:^(NSString *text) {
        
        @strongify(self);
        if (self.isShowing) {
            
            if (![text isEqualToString:self.currentText]) {
                
                [self.tableView hideToastView];
                if (text.length > 0) {
                    self.inputCount ++;
                    self.resultList = nil;
                    [self performSelector:@selector(requestKeyWorld:) withObject:@{@"count":@(self.inputCount),@"key":text} afterDelay:.5];
                    self.isShowHistory = NO;
                }
                else {
                    self.isShowHistory = YES;
                }
                [self.tableView reloadData];
                
                self.currentText = text;
            }
        }
    }];
}

//计算高度
- (void)calculateTagViewHeight {
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if ([userDefault objectForKey:self.tagName]) {
        NSArray *arr = [userDefault objectForKey:self.tagName];
        self.tagView.arr = arr;
        if (self.tagView.arr == 0) {
            self.tagViewHeightCons.constant = 0;
        } else {
            self.tagViewHeightCons.constant = self.tagView.height+40;
        }
    } else {
        self.tagViewHeightCons.constant = 0;
    }
}

- (void)clearSearchHistoryBtn {
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if ([userDefault objectForKey:self.tagName]) {
        [userDefault removeObjectForKey:self.tagName];
    }
    [userDefault synchronize];
    self.calculateTagArr = [NSArray array];
    [self.tableView reloadData];
}

- (void)handleSelectTag:(NSInteger)keyWordTag {
    self.searchTextField.text = [self.tagView.arr safeObjectAtIndex:keyWordTag];
    [GetCore(HJSearchCoreHelp) searchWithKey:self.searchTextField.text];
}

//存历史记录
- (void)savaSearchHisotry {
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if ([userDefault objectForKey:self.tagName]) {
        NSArray *arr = [userDefault objectForKey:self.tagName];
        if (![arr containsObject:self.searchTextField.text]) {
            NSMutableArray *muArr = [NSMutableArray arrayWithArray:arr];
            if (muArr.count > 9) {
                [muArr removeObject:muArr.lastObject];
            }
            [muArr insertObject:self.searchTextField.text atIndex:0];
            [userDefault setObject:muArr forKey:self.tagName];
            self.calculateTagArr = [muArr copy];
        } else {
            NSMutableArray *muArr = [NSMutableArray arrayWithArray:arr];
            [muArr removeObject:self.searchTextField.text];
            [muArr insertObject:self.searchTextField.text atIndex:0];
            [userDefault setObject:muArr forKey:self.tagName];
            self.calculateTagArr = [muArr copy];
        }
        
    } else {
        NSMutableArray *muArr = [NSMutableArray array];
        [muArr insertObject:self.searchTextField.text atIndex:0];
        [userDefault setObject:muArr forKey:self.tagName];
        self.calculateTagArr = [muArr copy];
    }
    [userDefault synchronize];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    [[SDWebImageManager sharedManager].imageCache clearMemory];
}

- (void)initView {

    self.tableView.backgroundColor = [[YYDefaultTheme defaultTheme]colorWithHexString:@"#F5F5F5" alpha:1];
    self.tableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectZero];
    self.searchContainerView.layer.cornerRadius = 14.5;
    self.searchContainerView.layer.masksToBounds = YES;
    self.tableView.estimatedRowHeight = 50.0;
    self.tableView.rowHeight=UITableViewAutomaticDimension;//高度设置为自适应
}

- (UIStatusBarStyle)preferredStatusBarStyle
{
//    return UIStatusBarStyleLightContent; //返回白色
    return UIStatusBarStyleDefault;    //返回黑色
}


- (void)dealloc {
    self.resultList = nil;
    self.tagView.delegate = nil;
    RemoveCoreClientAll(self);
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    [self calculateTagViewHeight];
    
    HJBlackStatusBar
    
}

- (void)viewDidAppear:(BOOL)animated {
    
    [super viewDidAppear:animated];
    self.isShowing = YES;
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated {
    
    [super viewDidDisappear:animated];
    self.isShowing = NO;
}

#pragma mark - UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    if (self.isShowHistory) {
        return self.calculateTagArr.count ? 1 : 0;
    }
    else {
        
        return 1;
    }
}

- (NSInteger)tableView:(UITableView *)tableView
 numberOfRowsInSection:(NSInteger)section
{
    return self.isShowHistory ? self.calculateTagArr.count : self.resultList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.isShowHistory) {
        HJSearchHistoryTableCell *cell = [HJSearchHistoryTableCell cellWithTableView:tableView];
        cell.textLabel.text = self.calculateTagArr[indexPath.row];
        return cell;
    }
    else {
        
        HJSearchResultCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJSearchResultCell" forIndexPath:indexPath];
        [self configureCell:cell forRowAtIndexPath:indexPath];
        return cell;
    }
}

- (nullable UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    
    if (self.isShowHistory) {
        HJSearchHistoryHeaderView *headerView = [[HJSearchHistoryHeaderView alloc] init];
        return headerView;
    }
    else {
        return [UIView new];
    }
}

- (nullable UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section {
    
    if (self.isShowHistory) {
        HJSearchHistoryFooterView *footerView = [HJSearchHistoryFooterView new];
        
        __weak typeof(self) weakSelf = self;
        [footerView setClearBtnActionBlock:^{
            [weakSelf clearSearchHistoryBtn];
        }];
        return footerView;
    }
    else {
        return [UIView new];
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return self.isShowHistory ? 44 : 70;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return self.isShowHistory ? 40 : CGFLOAT_MIN;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    return self.isShowHistory ? 40 : CGFLOAT_MIN;
}

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView
didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (self.isShowHistory) {
        [self.tableView hideToastView];
        self.resultList = nil;
        self.isShowHistory = NO;
        self.searchTextField.text = [self.calculateTagArr safeObjectAtIndex:indexPath.row];
        [GetCore(HJSearchCoreHelp) searchWithKey:self.searchTextField.text];
        [tableView reloadData];
    }
    else {
        
        HJSearchResultInfo *info = self.resultList[indexPath.row];

        HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
        vc.userID = info.uid;
        [self.navigationController pushViewController:vc animated:YES];
        
        [self savaSearchHisotry];
    }
}

#pragma mark - UITextFieldDelegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    return YES;
}

//- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
//    NSString *text = [textField.text stringByReplacingCharactersInRange:range withString:string];
//    if (text.length > 0) {
//        self.inputCount ++;
//        [self performSelector:@selector(requestKeyWorld:) withObject:@{@"count":@(self.inputCount),@"key":text} afterDelay:.5];
////        [GetCore(SearchCore) searchWithKey:text];
//    }
//
//    return YES;
//}

#pragma mark - SearchCoreClient

- (void)onSearchSuccess:(NSArray *)arr {
    [MBProgressHUD hideHUD];
    if (arr.count == 0) {
        self.resultList = nil;
        [self.tableView showEmptyContentToastWithTitle:NSLocalizedString(XCSearchNoResult, nil) andImage:[UIImage imageNamed:@"blank"]];
    }else {
        [self.tableView hideToastView];
        self.resultList = arr;
        [self.tableView reloadData];
    }
}

- (void)onSearchFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
    [self.tableView showEmptyContentToastWithTitle:NSLocalizedString(XCSearchNoResult, nil) andImage:[UIImage imageNamed:@"blank"]];
}

#pragma mark - event
- (IBAction)cancelButtonClick:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)searchAction:(id)sender {
    [self.view endEditing:YES];
    
    if (self.isShowing) {
        [self.tableView hideToastView];
        if (self.currentText.length > 0) {
            self.inputCount ++;
            self.resultList = nil;
            [self performSelector:@selector(requestKeyWorld:) withObject:@{@"count":@(self.inputCount),@"key":self.currentText} afterDelay:.5];
            self.isShowHistory = NO;
        }
        else {
            self.isShowHistory = YES;
        }
        [self.tableView reloadData];
    }
    
}

- (void)requestKeyWorld:(NSDictionary *)info{
    if (self.inputCount == [info[@"count"] integerValue]) {
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        [GetCore(HJSearchCoreHelp) searchWithKey:info[@"key"]];
    }
}

#pragma mark - private method
- (void)configureCell:(HJSearchResultCell *)cell
    forRowAtIndexPath:(NSIndexPath *)indexPath {
    HJSearchResultInfo *info = self.resultList[indexPath.row];
    cell.nicknameLabel.text = info.nick;
    [cell.avatar qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
    if (info.title.length > 0) { // 有房间
        NSString *formateTitle = [NSString stringWithFormat:@" %@",info.title];
        NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:formateTitle];
        NSTextAttachment *imageAttachment = [[NSTextAttachment alloc]init];
        imageAttachment.bounds = CGRectMake(0, -3, 27, 15);
        NSString *idText = [NSString stringWithFormat:@" ID:%@",info.erbanNo];
        
        [[SDWebImageDownloader sharedDownloader] downloadImageWithURL:[NSURL URLWithString:info.tagPict] options:SDWebImageDownloaderContinueInBackground progress:nil completed:^(UIImage * _Nullable image, NSData * _Nullable data, NSError * _Nullable error, BOOL finished) {
            imageAttachment.image = image;
            NSAttributedString *imageString = [NSAttributedString attributedStringWithAttachment:imageAttachment];
            NSAttributedString *idStr = [[NSAttributedString alloc]initWithString:@""];
            [str insertAttributedString:imageString atIndex:0];
            [str appendAttributedString:idStr];
            cell.idLabel.attributedText = str;
        }];
        
//        imageAttachment.image = [UIImage imageNamed:@"hj_main_home_party_tag"];
//        NSAttributedString *imageString = [NSAttributedString attributedStringWithAttachment:imageAttachment];
        NSAttributedString *idStr = [[NSAttributedString alloc]initWithString:idText];
//        [str insertAttributedString:imageString atIndex:0];
        [str appendAttributedString:idStr];
        cell.idLabel.attributedText = str;
    } else {
        NSString *idText = [NSString stringWithFormat:@" ID:%@  ",info.erbanNo];
        cell.idLabel.text = idText;
    }
}

- (NSArray *)calculateTagArr {
    
    if (!_calculateTagArr) {
     
        NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
        NSArray *arr = [userDefault objectForKey:self.tagName];
        _calculateTagArr = [arr copy];
    }
    
    return _calculateTagArr;
}

@end
