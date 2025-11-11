//
//  YPJiaoYouViewController.m
//  YPaya
//
//  Created by feiyin on 2020/10/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPJiaoYouViewController.h"
#import "YPHomePeipeiTableCell.h"
#import "YPHomeRecommedTableCell.h"
#import "YPFirstHomeSectionView.h"
#import "YPHomeHeaderView.h"
#import "UITableView+MJRefresh.h"
#import "UIButton+JXLayout.h"
#import "YPRoomViewControllerCenter.h"
#import "YPHttpRequestHelper+Home.h"
#import "YPWKWebViewController.h"

#import "YPIMRequestManager.h"
#import "YPIMRequestManager+PublicRoom.h"
#import "YPHttpRequestHelper+Alert.h"
#import "YPPushVC.h"
#import "YPGiftCore.h"
#import "Healp.h"
#import "YPHomePeipeiModel.h"
#import "YPFileCore.h"
#import "YPMediaCore.h"
#import "YPHomeRoomCell.h"
#import "HJMediaCoreClient.h"
#import "HJFileCoreClient.h"
#import "YPRoomPusher.h"
#import "YPEmptyView.h"
#import "YPSexPopVC.h"
#import "YPMySpaceVC.h"
#import "YPSessionViewController.h"
#import "HJHomeCoreClient.h"
#import "HJAuthCoreClient.h"
#import "HJImLoginCoreClient.h"
#import "HJIReachability.h"
#import "YPReachabilityCore.h"
#import "HJRoomCoreClient.h"
#import "YPVersionCoreHelp.h"
#import "HJUserCoreClient.h"



#define HJHomeTopViewHeight (74 + (iPhoneX?15:0))
#define HJHomeTableViewHeight XC_SCREE_H - XC_Height_TabBar +18
@interface YPJiaoYouViewController ()<UIScrollViewDelegate,UITableViewDelegate,UITableViewDataSource,HJWebSocketCoreClient,UIPopoverPresentationControllerDelegate>
@property (nonatomic,strong) UITableView *hotTableView;
@property (nonatomic, strong) NSMutableArray *peipeiRooms;//
@property (nonatomic, strong) NSMutableArray *mengxinRooms;//
@property (nonatomic,assign) int peipeiPage;
@property (nonatomic,assign) int mengxinPage;
@property (nonatomic,assign) BOOL isSelectPeipeiFlag;
@property (nonatomic,assign) BOOL isSelectMengxinFlag;
@property (nonatomic,assign) BOOL isHotEndData;
@property (nonatomic,assign) BOOL isPeipeiEndData;
@property (nonatomic,assign) BOOL isMengxinEndData;
@property (nonatomic, strong)YPFirstHomeSectionView *homeSectionView;
@property (nonatomic, strong)YPHomePeipeiTableCell * _Nonnull voiceCell;
@property (nonatomic, strong)YPHomePeipeiModel * _Nonnull peipeiModel;
@property (nonatomic,assign) BOOL isThis;
@property (nonatomic, strong) NSString *selectSexForPeipei;//1 男 2女 0全部
@property (nonatomic, strong) YPHomeHeaderView *radioFriendView;//交友滚栏
@property (nonatomic, strong)NSMutableArray* tempAllPeipeiArray;
@property (nonatomic,strong) YPEmptyView *tipView;





@end

@implementation YPJiaoYouViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"交友广场";
    [self.view addSubview:self.hotTableView];
    self.homeSectionView.lineView2.hidden = YES;
    self.homeSectionView.middleBtn.titleLabel.font = JXFontPingFangSCRegular(18);
     [self.homeSectionView.middleBtn setTitleColor:[UIColor colorWithHexString:@"#333333"] forState:UIControlStateNormal];
    self.peipeiRooms = [[NSMutableArray alloc] init];
    self.mengxinRooms = [[NSMutableArray alloc] init];
    self.tempAllPeipeiArray =[NSMutableArray array];
    [self setReflash];
    [self getData];
    AddCoreClient(HJHomeCoreClient, self);
    AddCoreClient(HJAuthCoreClient, self);
    AddCoreClient(HJImLoginCoreClient, self);
    AddCoreClient(HJAuthCoreClient, self);
    AddCoreClient(HJUserCoreClient, self);
    AddCoreClient(HJWebSocketCoreClient, self);
    AddCoreClient(HJMediaCoreClient, self);
    AddCoreClient(HJFileCoreClient, self);
   

}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    YPBlackStatusBar
}
-(void)getData{
    //请求页码
   
    self.peipeiPage = 1;
    self.mengxinPage = 1;
    self.isSelectPeipeiFlag = YES;
 
    [self requestPeipeiData];//请求陪陪数据
    [self requestMengxinData];
    [self getIMRoomMsgData];
}
-(void)setReflash{
    __weak typeof(self)weakSelf = self;
    [self.hotTableView setHeaderBlock:^{
       
        weakSelf.peipeiPage = 1;
        weakSelf.mengxinPage = 1;
        
       
        [weakSelf getIMRoomMsgData];
        
        if (weakSelf.isSelectPeipeiFlag) {
             [weakSelf requestPeipeiData];//请求陪陪数据
        }else if (weakSelf.isSelectMengxinFlag){
             [weakSelf requestMengxinData];
        }
       
          
    }];
    
    [self.hotTableView setFooterBlock:^{
       
        if (weakSelf.isSelectPeipeiFlag){
            weakSelf.peipeiPage += 1;
             [weakSelf requestPeipeiData];
        }else if (weakSelf.isSelectMengxinFlag){
            weakSelf.mengxinPage += 1;
            [weakSelf requestMengxinData];
        }

    }];
}





//MARK: - private function

-(void)setupRadioFriendView{
    
    
    self.radioFriendView.frame = CGRectMake(0, -110, XC_SCREE_W, 150);
    self.radioFriendView.bannerContainerView.hidden = YES;
    self.radioFriendView.naviController = self.navigationController;
}

//MARK: - getData数据
//交友大厅
- (void)getIMRoomMsgData
{
    NSString *roomId = [self getRoomid];
    __weak typeof(self)weakSelf = self;
    [YPIMRequestManager enterPublicWithRoomId:roomId success:^(NSArray * _Nonnull messages) {
        NSMutableArray *tmpArr = [[NSMutableArray alloc] initWithArray:messages];
        if (messages.count>20) {
            [tmpArr removeObjectsInRange:NSMakeRange(20, messages.count-20)];
        }
        
//        weakSelf.headerView.imRoomMsgArr = tmpArr;
        weakSelf.radioFriendView.imRoomMsgArr = tmpArr;
        
        [[NIMSDK sharedSDK].chatroomManager exitChatroom:roomId completion:nil];
        
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {

    }];
}
- (NSString *)getRoomid {
    if (GetCore(YPVersionCoreHelp).isReleaseEnv) {
        if (GetCore(YPVersionCoreHelp).checkIn) return JXIMPublicDebugAuditRoomId;
        
        return JXIMPublicReleaseNormalRoomId;
    } else {
        if (GetCore(YPVersionCoreHelp).checkIn) return JXIMPublicDebugAuditRoomId;
        return JXIMPublicDebugNormalRoomId;
    }
}
//萌新数据
-(void)requestMengxinData{
    
     [YPHttpRequestHelper requestHomeNewUsersDataWithPage:self.mengxinPage success:^(NSArray *homeDataArray) {
                
                [self.hotTableView.mj_header endRefreshing];
                [self.hotTableView.mj_footer endRefreshing];
                
                if (self.mengxinPage == 1) {
                    [self.mengxinRooms removeAllObjects];
                }
             [self.mengxinRooms addObjectsFromArray:homeDataArray];

                [self.hotTableView reloadData];

         if (homeDataArray != nil) {
                         if (homeDataArray.count<25 ) {
                             [self.hotTableView.mj_footer endRefreshingWithNoMoreData];
                             self.isMengxinEndData = YES;
                         }else{
                             self.isMengxinEndData = NO;
                              [self.hotTableView.mj_footer resetNoMoreData];
                         }
                     }else{
                          [self.hotTableView.mj_footer endRefreshingWithNoMoreData];
                         self.isMengxinEndData = YES;
                     }
         
                [MBProgressHUD hideHUD];
                
            } failure:^(NSNumber *resCode, NSString *message) {
                if (self.peipeiRooms.count == 0) {
                    self.hotTableView.tableFooterView = self.tipView;
                }
                [self.hotTableView.mj_header endRefreshing];
                [self.hotTableView.mj_footer endRefreshing];
                
                [MBProgressHUD hideHUD];
            }];
}

//陪陪数据
-(void)requestPeipeiData{
    
     [YPHttpRequestHelper requestHomeBestCompaniesDataWithPage:self.peipeiPage gender:[self.selectSexForPeipei integerValue] success:^(NSArray *homeDataArray) {
            
            [self.hotTableView.mj_header endRefreshing];
            [self.hotTableView.mj_footer endRefreshing];
            
            if (self.peipeiPage == 1 ) {
                [self.peipeiRooms removeAllObjects];
                [self.tempAllPeipeiArray removeAllObjects];
    //            self.bannerInfos = nil;
               
            }
         if ([self.selectSexForPeipei isEqualToString:@"1"]) {//男 1 男 2女 3全部
             for (YPHomePeipeiModel* model in homeDataArray) {
                 if (model.gender == 1) {
                     [self.peipeiRooms addObject:model];
                 }
             }
         }else if ([self.selectSexForPeipei isEqualToString:@"2"]){
             for (YPHomePeipeiModel* model in homeDataArray) {
                 if (model.gender == 2) {
                     [self.peipeiRooms addObject:model];
                 }
             }
         }else{
              [self.peipeiRooms addObjectsFromArray:homeDataArray];
         }
        
         [self.tempAllPeipeiArray addObjectsFromArray:homeDataArray];
            [self.hotTableView reloadData];
            
         if (homeDataArray != nil) {
                   if (homeDataArray.count<25 ) {
                       [self.hotTableView.mj_footer endRefreshingWithNoMoreData];
                       self.isPeipeiEndData = YES;
                   }else{
                       self.isPeipeiEndData = NO;
                        [self.hotTableView.mj_footer resetNoMoreData];
                   }
               }else{
                    [self.hotTableView.mj_footer endRefreshingWithNoMoreData];
                   self.isPeipeiEndData = YES;
               }
            
            [MBProgressHUD hideHUD];
            
//         if (self.peipeiPage == 1 ) {
//              if (self.isSelectPeipeiFlag) {
//                                self.homeSectionView.middleBtnBlock();
//                            }
//         }
         
        } failure:^(NSNumber *resCode, NSString *message) {
            if (self.peipeiRooms.count == 0) {
                self.hotTableView.tableFooterView = self.tipView;
            }
            [self.hotTableView.mj_header endRefreshing];
            [self.hotTableView.mj_footer endRefreshing];
            
            [MBProgressHUD hideHUD];
        }];
}




//MARK: - UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
  
    if (section == 0) {
        return 1;
    }
    
    if (self.isSelectMengxinFlag) {
           return self.mengxinRooms.count;
      }else{
//           return self.peipeiRooms.count;
           return self.peipeiRooms.count/2+(self.peipeiRooms.count%2>0?1:0);
      }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{

    if (section == 1 && self.peipeiRooms.count>0) {
        return 50;
    }
    
    return CGFLOAT_MIN;
}
- (void)showPopView {
//    self.bottomView.sendBtn.selected = YES;
  __block YPSexPopVC *contentVC = [[YPSexPopVC alloc] init];
    
    contentVC.modalPresentationStyle = UIModalPresentationPopover;
    contentVC.popoverPresentationController.sourceView = self.homeSectionView.sexImageView;
    contentVC.popoverPresentationController.sourceRect = CGRectMake(0, 0, 0, self.homeSectionView.sexImageView.bounds.size.height+5);
    contentVC.popoverPresentationController.permittedArrowDirections = UIPopoverArrowDirectionUp;
    contentVC.popoverPresentationController.backgroundColor = [UIColor whiteColor];
    // 设置代理
    contentVC.popoverPresentationController.delegate = self;
    @weakify(contentVC);
    
    contentVC.allBtnBlock = ^{
        self.selectSexForPeipei = @"0";
        self.peipeiPage = 1;
        [self.homeSectionView.sexImageView setImage:[UIImage imageNamed:@"home_peipei_tegetder"]];
        [self.homeSectionView.sexImageViewFlag setImage:[UIImage imageNamed:@"home_peipei_down"]];
        [self requestPeipeiData];

        [contentVC_weak_ dismissViewControllerAnimated:YES completion:nil];
    };
    contentVC.manBtnBlock = ^{
         self.selectSexForPeipei = @"1";
        self.peipeiPage = 1;
        [self.homeSectionView.sexImageView setImage:[UIImage imageNamed:@"home_peipei_man"]];
        [self.homeSectionView.sexImageViewFlag setImage:[UIImage imageNamed:@"home_peipei_down"]];
        [self requestPeipeiData];

        [contentVC_weak_ dismissViewControllerAnimated:YES completion:nil];
    };
    contentVC.wemanBtnBlock = ^{
         self.selectSexForPeipei = @"2";
        self.peipeiPage = 1;
        [self.homeSectionView.sexImageView setImage:[UIImage imageNamed:@"home_peipei_weman"]];
        [self.homeSectionView.sexImageViewFlag setImage:[UIImage imageNamed:@"home_peipei_down"]];
        [self requestPeipeiData];

        [contentVC_weak_ dismissViewControllerAnimated:YES completion:nil];
    };
    
    [self.homeSectionView.sexImageViewFlag setImage:[UIImage imageNamed:@"home_peipei_up"]];
    [self presentViewController:contentVC animated:YES completion:nil];
}

//UIPopoverPresentationControllerDelegate
-(UIModalPresentationStyle)adaptivePresentationStyleForPresentationController:(UIPresentationController *)controller{
    return UIModalPresentationNone; //不适配
}

 - (BOOL)popoverPresentationControllerShouldDismissPopover:(UIPopoverPresentationController *)popoverPresentationController{
//    self.bottomView.sendBtn.selected = NO;
     [self.homeSectionView.sexImageViewFlag setImage:[UIImage imageNamed:@"home_peipei_down"]];
    return YES;   //点击蒙版popover消失， 默认YES
}
- (UIView*)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    if (section == 0 ) {
//        if (self.hotRooms.count ==0) {
//            return [UIView new];
//        }
         return [UIView new];
    }
    
    if (section == 1) {
        if (self.peipeiRooms.count ==0) {
            return [UIView new];
        }else{
               __weak typeof(self)weakself = self;
            self.homeSectionView.sexBtnBlock = ^{
                
                [weakself showPopView];
                
            };
//            self.homeSectionView.leftBtnBlock = ^{
//
//                if (weakself.isHotEndData) {
//                [weakself.hotTableView.mj_footer endRefreshingWithNoMoreData];
//                }else{
//                     [weakself.hotTableView.mj_footer resetNoMoreData];
//                }
//
//
//                weakself.isSelectPeipeiFlag = NO;
//                weakself.isSelectMengxinFlag = NO;
//             //UI处理
////                [weakself.homeSectionView.leftBtn setTitleColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_wenzhi_img_80"]] forState:UIControlStateNormal];
//                [weakself.homeSectionView.leftBtn setTitleColor:[UIColor colorWithHexString:@"#333333"] forState:UIControlStateNormal];
//
//
//                [weakself.homeSectionView.middleBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
//                 [weakself.homeSectionView.rightBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
//                weakself.homeSectionView.lineView.hidden = NO;
//                weakself.homeSectionView.lineView2.hidden = YES;
//                weakself.homeSectionView.lineView3.hidden = YES;
//                weakself.homeSectionView.leftBtn.titleLabel.font = JXFontPingFangSCRegular(18);
//                 weakself.homeSectionView.middleBtn.titleLabel.font = JXFontPingFangSCRegular(16);
//                 weakself.homeSectionView.rightBtn.titleLabel.font = JXFontPingFangSCRegular(16);
////                  [weakself.hotTableView reloadData];
//                [UIView performWithoutAnimation:^{
//                    [tableView beginUpdates];
//                     NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:2];
//                      [weakself.hotTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
//                    [tableView endUpdates];
//                }];
//                //事件处理
//            };
            _homeSectionView.middleBtnBlock = ^{

                if (weakself.isPeipeiEndData) {
                              [weakself.hotTableView.mj_footer endRefreshingWithNoMoreData];
                              }else{
                                   [weakself.hotTableView.mj_footer resetNoMoreData];
                              }
                
              weakself.isSelectPeipeiFlag = YES;
            weakself.isSelectMengxinFlag = NO;
            
//                [weakself.homeSectionView.middleBtn setTitleColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_wenzhi_img_80"]] forState:UIControlStateNormal];
                  [weakself.homeSectionView.middleBtn setTitleColor:[UIColor colorWithHexString:@"#333333"] forState:UIControlStateNormal];
                
                
//                [weakself.homeSectionView.leftBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
                [weakself.homeSectionView.rightBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
//                weakself.homeSectionView.lineView.hidden = YES;
                              weakself.homeSectionView.lineView2.hidden = YES;
                              weakself.homeSectionView.lineView3.hidden = YES;
//                              weakself.homeSectionView.leftBtn.titleLabel.font = JXFontPingFangSCRegular(16);
                               weakself.homeSectionView.middleBtn.titleLabel.font = JXFontPingFangSCRegular(18);
                               weakself.homeSectionView.rightBtn.titleLabel.font = JXFontPingFangSCRegular(16);
//                [weakself.hotTableView reloadData];
               [UIView performWithoutAnimation:^{
                   [tableView beginUpdates];
                   NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:1];
                     [weakself.hotTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
                   [tableView endUpdates];
               }];
               
                
            };
            _homeSectionView.rightBtnBlock = ^{
                
                if (weakself.isMengxinEndData) {
                              [weakself.hotTableView.mj_footer endRefreshingWithNoMoreData];
                              }else{
                                   [weakself.hotTableView.mj_footer resetNoMoreData];
                              }
                
                weakself.isSelectMengxinFlag = YES;
                weakself.isSelectPeipeiFlag = NO;
            
//               [weakself.homeSectionView.rightBtn setTitleColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_wenzhi_img_80"]] forState:UIControlStateNormal];
                [weakself.homeSectionView.rightBtn setTitleColor:[UIColor colorWithHexString:@"#333333"] forState:UIControlStateNormal];
                
                [weakself.homeSectionView.middleBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
//                [weakself.homeSectionView.leftBtn setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateNormal];
//                weakself.homeSectionView.lineView.hidden = YES;
                              weakself.homeSectionView.lineView2.hidden = YES;
                              weakself.homeSectionView.lineView3.hidden = YES;
//                              weakself.homeSectionView.leftBtn.titleLabel.font = JXFontPingFangSCRegular(16);
                               weakself.homeSectionView.middleBtn.titleLabel.font = JXFontPingFangSCRegular(16);
                               weakself.homeSectionView.rightBtn.titleLabel.font = JXFontPingFangSCRegular(18);
              
                [UIView performWithoutAnimation:^{
                    [tableView beginUpdates];
                    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:1];
                      [weakself.hotTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
                    [tableView endUpdates];
                }];
            };
             return _homeSectionView;
        }
    }
    
   
    return [UIView new];
    
    

   
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return CGFLOAT_MIN;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
//    if (indexPath.section == 0) {
//
////        return JXHomeRecommedCellWidth*(113.3/109.0)+30;
//          return JXHomeRecommedCellWidth*(147/116.0);
//    }
    if (indexPath.section == 0) {
        return 120*(kScreenWidth/375.0);
    }
//    if (self.isSelectHotFlag) {
//        return 187*(kScreenWidth/375.0);
//    }
    if (self.isSelectPeipeiFlag) {
           return 187*(kScreenWidth/375.0);
       }
    
    return 114;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
//    if (indexPath.section == 0) {
//
//        YPHomeRecommedTableCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPHomeRecommedTableCell"];
//        if (indexPath.row*3+1 == self.hotRooms.count) {
//            cell.roomArr = @[self.hotRooms[indexPath.row*3]];
//        }else{
//
//            if (indexPath.row*3+2 == self.hotRooms.count) {
//                cell.roomArr = @[self.hotRooms[indexPath.row*3],self.hotRooms[indexPath.row*3+1]];
//            }else{
//                cell.roomArr = @[self.hotRooms[indexPath.row*3],self.hotRooms[indexPath.row*3+1],self.hotRooms[indexPath.row*3+2]];
//
//            }
//
//        }
//
//        return cell;
//    }
    if (indexPath.section ==0) {
        UITableViewCell* cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"cellID"];
//        self.leftView = [self getFirstViewForCellWithTitle:@"交友广场" des:@"聊天打招呼" imgName:@"yp_home_jiaoyou_icon"];
//        self.rightView = [self getFirstViewForCellWithTitle:@"排行榜" des:@"喜翻名人榜" imgName:@"yp_home_phb_icon"];
//        [cell.contentView addSubview:self.leftView];
//        [cell.contentView addSubview:self.rightView];
//        cell.backgroundColor = [UIColor clearColor];
        
//        UITapGestureRecognizer *tapGesLeft = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesLeftClick:)];
//               [self.leftView addGestureRecognizer:tapGesLeft];
//
//        UITapGestureRecognizer *tapGesRight = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesRightClick:)];
//               [self.rightView addGestureRecognizer:tapGesRight];
        
        
        [cell.contentView addSubview:self.radioFriendView];
        [self setupRadioFriendView];

//        YPFirstHomeSectionView* qualityView = [[YPFirstHomeSectionView alloc] initWithFrame:CGRectMake(0, 10, XC_SCREE_W, 30)];
//                    qualityView.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
//           qualityView.logoImageView.image = [UIImage imageNamed:@"yp_home_guangbojiaoyou"];
//           qualityView.tipLabel.text = @"广播交友";
//          [cell.contentView addSubview:qualityView];
        cell.contentView.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
    }
    
//    if (self.isSelectHotFlag) {
//        YPHomeHotTableCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPHomeHotTableCell"];
//        if (indexPath.row*2+1 == self.listRooms.count) {
//                   cell.roomArr = @[self.listRooms[indexPath.row*2]];
//               }else{
//                    cell.roomArr = @[self.listRooms[indexPath.row*2],self.listRooms[indexPath.row*2+1]];
//               }
//        return cell;
//    }else
    
    
    if (self.isSelectPeipeiFlag){

        YPHomePeipeiTableCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPHomePeipeiTableCell"];
        //去找ta
        
            cell.tapGotoTaLeftBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel, YPHomePeipeiTableCell * _Nonnull voiceCell) {
                [YPRoomPusher pushUserInRoomByID:peipeiModel.uid];
            };
        
        cell.tapGotoTaRightBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel, YPHomePeipeiTableCell * _Nonnull voiceCell) {
            [YPRoomPusher pushUserInRoomByID:peipeiModel.uid];
        };
        
        cell.tapTilteLeftBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel, YPHomePeipeiTableCell * _Nonnull voiceCell) {
            YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
                                vc.userID = peipeiModel.uid;
                                [self.navigationController pushViewController:vc animated:YES];
        };
        cell.tapTilteRightBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel, YPHomePeipeiTableCell * _Nonnull voiceCell) {
            YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
            vc.userID = peipeiModel.uid;
                                [self.navigationController pushViewController:vc animated:YES];
        };
        
        
        //点击语音播放
           cell.tapVoiceBtnForPeiPeiBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel ,YPHomePeipeiTableCell * _Nonnull voiceCell){
               if (self.peipeiModel.isLeft) {
                   [self.voiceCell.peopleImageView stopAnimating];
                   if ([GetCore(YPMediaCore) isPlaying]) {
                       [self.voiceCell.voiceButton setImage:[UIImage imageNamed:@"home_first_peipei_play"] forState:UIControlStateNormal];
                   }
               }
               
               if (self.peipeiModel && !self.peipeiModel.isLeft){
                   [self.voiceCell.peopleImageView2 stopAnimating];
                   if ([GetCore(YPMediaCore) isPlaying]) {
                       [self.voiceCell.voiceButton2 setImage:[UIImage imageNamed:@"home_first_peipei_play"] forState:UIControlStateNormal];
                   }
               }
               
               BOOL isSameCell = NO;
               if (self.voiceCell && [self.voiceCell.filePath isEqualToString:voiceCell.filePath]) {
                   isSameCell = YES;
               }
               
                self.voiceCell = voiceCell;
               self.peipeiModel = peipeiModel;
               if (voiceCell.filePath == nil) {
                      if (peipeiModel.voiceDuration==0) {
                                      [MBProgressHUD showError:@"TA还没有录制声音，快去提醒TA吧~"];
                                     return;
                                 }
                      [MBProgressHUD showMessage:@"下载中..."];
                      [GetCore(YPFileCore) downloadVoice:peipeiModel.userVoice];
                  } else{
                      if ([GetCore(YPMediaCore) isPlaying]) {
                          if (isSameCell) {
                              [GetCore(YPMediaCore) stopPlay];
                          }else{
                              [GetCore(YPMediaCore) play:voiceCell.filePath];
                          }
                      } else {
                          [GetCore(YPMediaCore) play:voiceCell.filePath];
                      }
                  }
           };
        
        
        if (indexPath.row*2+1 == self.peipeiRooms.count) {
                   cell.roomArr = @[self.peipeiRooms[indexPath.row*2]];
               }else{
                    cell.roomArr = @[self.peipeiRooms[indexPath.row*2],self.peipeiRooms[indexPath.row*2+1]];
               }
        cell.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
        return cell;
    }
    
    
    
    
    
    
    YPHomeRoomCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPHomeRoomCell"];
    cell.tapAvatarBlock = ^(UserID uid, YPHomeRoomCell * _Nonnull voiceCell, NSInteger type) {
          YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
                     vc.userID = uid;
                     [self.navigationController pushViewController:vc animated:YES];
      };
//    //去找ta
//    cell.tapGotoTaBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel, YPHomeRoomCell * _Nonnull voiceCell) {
//        [YPRoomPusher pushUserInRoomByID:peipeiModel.uid];
//    };
    //聊聊天
    cell.tapChatBlock = ^(YPHomeMengxinModel * _Nonnull mengxinModel, YPHomeRoomCell * _Nonnull voiceCell) {
        NIMSession *session = [NIMSession session:[NSString stringWithFormat:@"%lld",mengxinModel.uid] type:0];
                  YPSessionViewController *vc = [[YPSessionViewController alloc] initWithSession:session];
                  [self.navigationController pushViewController:vc animated:YES];
    };
//    //点击语音播放
//    cell.clickVoiceBtnBlock = ^(YPHomePeipeiModel * _Nonnull peipeiModel ,YPHomeRoomCell * _Nonnull voiceCell){
//         [self.voiceCell.gifImageview stopAnimating];
//         self.voiceCell = voiceCell;
//        if (voiceCell.filePath == nil) {
//               if (peipeiModel.voiceDuration==0) {
//                               [MBProgressHUD showError:@"TA还没有录制声音，快去提醒TA吧~"];
//                              return;
//                          }
//               [MBProgressHUD showMessage:@"下载中..."];
//               [GetCore(YPFileCore) downloadVoice:peipeiModel.userVoice];
//           } else {
//               if ([GetCore(YPMediaCore) isPlaying]) {
//                   [GetCore(YPMediaCore) stopPlay];
//               } else {
//                   [GetCore(YPMediaCore) play:voiceCell.filePath];
//               }
//           }
//    };
   
 //判断按钮
    if (self.isSelectMengxinFlag) {
         cell.mengxinModel = self.mengxinRooms[indexPath.row];
    }else if (self.isSelectPeipeiFlag){
//         cell.peipeiModel = self.peipeiRooms[indexPath.row];
    }else{
//         cell.model = self.listRooms[indexPath.row];
    }
    
    return cell;
}

//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    if (indexPath.section == 2) {
//
//          if (self.isSelectMengxinFlag) {
////             YPHomeMengxinModel* mengxinModel = self.mengxinRooms[indexPath.row];
////               [self showAlerView:mengxinModel.uid];
//         }else if (self.isSelectPeipeiFlag){
////             YPHomePeipeiModel* peipeiModel = self.peipeiRooms[indexPath.row];
////               [self showAlerView:peipeiModel.uid];
//         }else{
//             YPHomePageInfo *info = [self.listRooms safeObjectAtIndex:indexPath.row];
//              if (info) {
//                  [self showAlerView:info.uid];
//              }
//         }
//
//    }
//}


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
    if (self.peipeiModel.isLeft) {
        [self.voiceCell.voiceButton setImage:[UIImage imageNamed:@"home_first_peipei_stop"] forState:UIControlStateNormal];
    }else{
        [self.voiceCell.voiceButton2 setImage:[UIImage imageNamed:@"home_first_peipei_stop"] forState:UIControlStateNormal];
    }
    
    if (self.voiceCell.filePath != nil && [self.voiceCell.filePath isEqualToString:filePath]) {
    }
    
    [self setPlayGif];
    
}

- (void)onPlayAudioComplete:(NSString *)filePath
{
    if (self.peipeiModel.isLeft) {
         [self.voiceCell.peopleImageView stopAnimating];
        [self.voiceCell.voiceButton setImage:[UIImage imageNamed:@"home_first_peipei_play"]  forState:UIControlStateNormal];
    }else{
        [self.voiceCell.voiceButton2 setImage:[UIImage imageNamed:@"home_first_peipei_play"]  forState:UIControlStateNormal];
        [self.voiceCell.peopleImageView2 stopAnimating];
    }
    
   
    
}

- (void)setPlayGif {
    NSMutableArray *arr = [NSMutableArray new];
    for (int i = 0; i < 2; i ++) {
        [arr addObject:[UIImage imageNamed:[NSString stringWithFormat:@"home_first_peipei_bolang%d",i+1]]];
    }
    
    if (self.peipeiModel.isLeft) {
        [self.voiceCell.peopleImageView setAnimationImages:arr];
           [self.voiceCell.peopleImageView setAnimationDuration:1];
           self.voiceCell.peopleImageView.animationRepeatCount = 0;
           [self.voiceCell.peopleImageView startAnimating];
        
    }else{
        
        [self.voiceCell.peopleImageView2 setAnimationImages:arr];
           [self.voiceCell.peopleImageView2 setAnimationDuration:1];
           self.voiceCell.peopleImageView2.animationRepeatCount = 0;
           [self.voiceCell.peopleImageView2 startAnimating];
        
    }

}

- (void)showAlerView:(UserID )uid{
    NSString *isFirst = [[NSUserDefaults standardUserDefaults]objectForKey:@"isFirstEnterRoom"];
    if (![isFirst isEqualToString:@"1"]) {
        
        @weakify(self);
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"房间使用协议" message:NSLocalizedString(@"      房间是你与别人互动的地方，官方倡导绿色健康的房间体验，请务必文明用语。严禁涉及色情，政治等不良信息，若封面、背景含低俗、引导、暴露等不良内容都将会被永久封号，对于引起不适的内容请用户及时举报，我们会迅速响应处理！\n同意即可开始使用房间功能！", nil) preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *enter = [UIAlertAction actionWithTitle:@"同意" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            @strongify(self);
            [self pushToRoom:uid];
            [[NSUserDefaults standardUserDefaults]setObject:@"1" forKey:@"isFirstEnterRoom"];
            [[NSUserDefaults standardUserDefaults]synchronize];
        }];
        UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            
        }];
        [alert addAction:cancel];
        [alert addAction:enter];
        [self presentViewController:alert animated:YES completion:nil];
    }else{
        [self pushToRoom:uid];
    }
    
}

- (void)pushToRoom:(UserID )uid
{
    //根据房间所有者id。获取房间信息
    [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
    [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomOwnerUid:uid succ:^(YPChatRoomInfo *roomInfo) {
        if (roomInfo != nil && roomInfo.title.length > 0) {
            //            [MBProgressHUD hideHUD];
            //根据房间信息开房
            [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
        }else {
            [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
        }
    } fail:^(NSString *errorMsg) {
        [MBProgressHUD showError:errorMsg];
    }];
    
}



//MARK: - get/set
- (UITableView *)hotTableView
{
    if (!_hotTableView) {
        _hotTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, HJHomeTableViewHeight-130+ (iPhoneX?24:44))];
        _hotTableView.backgroundColor = [UIColor clearColor];
        _hotTableView.delegate = self;
        _hotTableView.dataSource = self;
        _hotTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        _hotTableView.showsVerticalScrollIndicator = NO;
        [_hotTableView registerClass:[YPHomeRecommedTableCell class] forCellReuseIdentifier:@"YPHomeRecommedTableCell"];
        
        
         [_hotTableView registerClass:[YPHomePeipeiTableCell class] forCellReuseIdentifier:@"YPHomePeipeiTableCell"];
        
        
        [_hotTableView registerNib:[UINib nibWithNibName:@"YPHomeRoomCell" bundle:nil] forCellReuseIdentifier:@"YPHomeRoomCell"];
        
//        [self.view addSubview:_hotTableView];
    }
    return _hotTableView;
}

- (YPHomeHeaderView *)radioFriendView
{
    if (!_radioFriendView) {
        _radioFriendView = [[NSBundle mainBundle] loadNibNamed:@"YPHomeHeaderView" owner:self options:nil][0];
        
        
    }
    return _radioFriendView;
}
- (YPEmptyView *)tipView
{
    if (!_tipView) {
        _tipView = [[YPEmptyView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 428)];
        [_tipView setTitle:@"列表里没有任何内容呢" image:@"blank"];
    }
    return _tipView;
}

-(YPFirstHomeSectionView*)homeSectionView{
    if (!_homeSectionView) {
       
         _homeSectionView = [[YPFirstHomeSectionView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, 50)];
          _homeSectionView.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
    }
    return _homeSectionView;
    
}
@end
