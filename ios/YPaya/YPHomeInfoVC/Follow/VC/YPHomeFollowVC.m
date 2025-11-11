//
//  YPHomeFollowVC.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHomeFollowVC.h"
#import "YPMySpaceVC.h"
#import "YPFollowListViewController.h"

#import "YPHomeMyFollowCell.h"
#import "YPHomeFollowCell.h"
#import "YPHomeMyFollowCCell.h"
#import "YPFirstHomeSectionView.h"

#import "YPHttpRequestHelper+Praise.h"
#import "YPHttpRequestHelper+Home.h"

#import "UITableView+MJRefresh.h"
#import "YPFileCore.h"
#import "YPMediaCore.h"
#import "HJAuthCoreClient.h"
#import "HJMediaCoreClient.h"
#import "HJFileCoreClient.h"
@interface YPHomeFollowVC ()<UITableViewDelegate,UITableViewDataSource,HJMediaCoreClient,HJFileCoreClient>
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (strong,nonatomic) NSMutableArray *myFollowArr;
@property (strong,nonatomic) NSMutableArray *followArr;

@property (strong,nonatomic) UIView *topRadiusView;
@property (strong,nonatomic) YPHomeFollowCell * _Nonnull voiceCell;
@end

@implementation YPHomeFollowVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    __weak typeof(self)weakSelf = self;
    self.tableView.headerBlock = ^{
        [weakSelf getMyFollowData];
        [weakSelf getRecommendFollowData];
    };
    
    AddCoreClient(HJAuthCoreClient, self);
    AddCoreClient(HJMediaCoreClient, self);
    AddCoreClient(HJFileCoreClient, self);
    
    self.tableView.tableHeaderView = [[UIView alloc] initWithFrame:CGRectMake(0,0,0,CGFLOAT_MIN)];
}

- (void)onLoginSuccess
{
    [self getMyFollowData];
    [self getRecommendFollowData];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    YPBlackStatusBar
}

#pragma mark <UITableViewDelegate,UITableViewDataSource>
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return self.myFollowArr.count>0?1:0;
    }
    return self.followArr.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section == 0) {
        return self.myFollowArr.count>0?50:0;
    }
    
//    if (self.followArr.count>0 && section ==1) {
//        return 16;
//    }
    
    return CGFLOAT_MIN;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    if (section == 0) {
        return 50;
    }
    

        
    
    return CGFLOAT_MIN;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section ==0) {
        return 140;
    }
    return 112;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    if (self.myFollowArr.count>0 && section == 0) {
        YPFirstHomeSectionView *header = [[YPFirstHomeSectionView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, 50)];
        header.logoImageView.image = [UIImage imageNamed:@"yp_home_attend_myAttend"];
        header.tipLabel.text = @"我的关注";
        __weak typeof(self)weakSelf = self;
        header.detailBlock = ^{
            [weakSelf myFollowListAction];
        };
        header.backgroundColor = [UIColor clearColor];
        
        return header;
    }
    
//    if (self.followArr.count>0 && section ==1) {
//        return self.topRadiusView;
//    }
    
    return [UIView new];
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    if (section == 0) {
        YPFirstHomeSectionView *footer = [[YPFirstHomeSectionView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, 50)];
        footer.logoImageView.image = [UIImage imageNamed:@"yp_home_attend_tuijian"];
        footer.tipLabel.text = @"推荐关注";
        footer.backgroundColor = [UIColor clearColor];
        return footer;
    }
    return [UIView new];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.myFollowArr.count>0 && indexPath.section==0) {
        YPHomeMyFollowCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPHomeMyFollowCell" forIndexPath:indexPath];
        cell.myFollowArr = self.myFollowArr;
        return cell;
    }
    
    YPHomeFollowCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPHomeFollowCell" forIndexPath:indexPath];
    if (indexPath.row<self.followArr.count) {
        cell.model = self.followArr[indexPath.row];
    }
    
    __weak typeof(self)weakSelf = self;
    cell.followBlock = ^(UserID uid,BOOL isFan) {
        [weakSelf followByUID:uid isFan:isFan];
    };
    
    //点击语音播放
    cell.clickVoiceBtnBlock = ^(UserInfo * _Nonnull folloModel ,YPHomeFollowCell * _Nonnull voiceCell){
         [self.voiceCell.gifImageview stopAnimating];
         self.voiceCell = voiceCell;
        if (voiceCell.filePath == nil) {
               if (folloModel.voiceDura == 0) {
                                         [MBProgressHUD showError:@"TA还没有录制声音，快去提醒TA吧~"];
                                        return;
                                    }
               [MBProgressHUD showMessage:@"下载中..."];
               [GetCore(YPFileCore) downloadVoice:folloModel.userVoice];
           } else {
               if ([GetCore(YPMediaCore) isPlaying]) {
                   [GetCore(YPMediaCore) stopPlay];
               } else {
                   [GetCore(YPMediaCore) play:voiceCell.filePath];
               }
           }
    };
    
    
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 1) {
        
        if (indexPath.item<self.followArr.count) {
            UserInfo *model = self.followArr[indexPath.item];
            YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
            vc.userID = model.uid;
            [self.navigationController pushViewController:vc animated:YES];
        }
    }
}
#pragma mark - FileCoreClient
- (void)onDownloadVoiceSuccess:(NSString *)filePath
{
    self.voiceCell.filePath = filePath;
    
    if ([GetCore(YPMediaCore) isPlaying]) {
        [GetCore(YPMediaCore) stopPlay];
    }
    
    [MBProgressHUD hideHUD];
    
    
    [GetCore(YPMediaCore) play:filePath];
}

- (void)onDownloadVoiceFailth:(NSError *)error
{
    [MBProgressHUD showError:XCHudNetError];
}

#pragma mark - MediaCoreClient
- (void) onPlayAudioBegan:(NSString *)filePath
{
    if (self.voiceCell.filePath != nil && [self.voiceCell.filePath isEqualToString:filePath]) {
    }
    
    [self setPlayGif];
    
}

- (void)onPlayAudioComplete:(NSString *)filePath
{
    [self.voiceCell.gifImageview stopAnimating];
}

- (void)setPlayGif {
    NSMutableArray *arr = [NSMutableArray new];
    for (int i = 0; i < 3; i ++) {
        [arr addObject:[UIImage imageNamed:[NSString stringWithFormat:@"yp_space_voice_play%d",i+1]]];
    }
    [self.voiceCell.gifImageview setAnimationImages:arr];
    [self.voiceCell.gifImageview setAnimationDuration:1];
    self.voiceCell.gifImageview.animationRepeatCount = 0;
    [self.voiceCell.gifImageview startAnimating];
}
#pragma mark priveate method


- (void)myFollowListAction
{
    YPFollowListViewController *vc = YPMessageBoard(@"YPFollowListViewController");
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)followByUID:(UserID)uid isFan:(BOOL)isFan
{
    
    if (isFan) {
        [MBProgressHUD showMessage:@"取消关注中..."];
        
        __weak typeof(self)weakSelf = self;
        
        [YPHttpRequestHelper cancel:[GetCore(YPAuthCoreHelp) getUid].userIDValue beCanceledUid:uid success:^{
            [MBProgressHUD showSuccess:@"取消关注成功"];
            
            [weakSelf getMyFollowData];
            [weakSelf getRecommendFollowData];
            
        } failure:^(NSNumber *resCode, NSString *message) {
            
        }];
        
    }else{
        [MBProgressHUD showMessage:@"关注中..."];
        
        __weak typeof(self)weakSelf = self;
        [YPHttpRequestHelper praise:[GetCore(YPAuthCoreHelp) getUid].userIDValue bePraisedUid:uid success:^{
            
            [MBProgressHUD showSuccess:@"关注成功"];
            
            [weakSelf getMyFollowData];
            [weakSelf getRecommendFollowData];
        } failure:^(NSNumber *resCode, NSString *message) {
            
        }];
    }
    
    
    
}

- (void)getMyFollowData
{
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;

    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper requestAttentionList:uid state:0 page:1 success:^(NSArray *userInfos) {
        
        weakSelf.myFollowArr = [NSMutableArray arrayWithArray:userInfos];
        [weakSelf.tableView reloadData];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

- (void)getRecommendFollowData
{
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper requestRecommendUserListSuccess:^(NSArray<UserInfo *> *list) {
        
        [weakSelf.tableView.mj_header endRefreshing];
        weakSelf.followArr = [NSMutableArray arrayWithArray:list];
        [weakSelf.tableView reloadData];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

- (UIView *)topRadiusView
{
    if (!_topRadiusView) {
        _topRadiusView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 24)];
        _topRadiusView.backgroundColor = [UIColor clearColor];
        
        UIImageView *imageVIew=  [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"yp_home_attend_bg_top"]];
        imageVIew.frame = CGRectMake(10, 0, kScreenWidth-20, 24);
        [_topRadiusView addSubview:imageVIew];
        
    }
    return _topRadiusView;
}



@end
