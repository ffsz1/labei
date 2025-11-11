//
//  YPGameRoomVC+Alert.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomVC+Alert.h"
#import "YPGameRoomVC+Delegate.h"

#import "TYAlertController.h"
#import "YPRoomViewControllerCenter.h"
#import "YPPraiseCore.h"

#import "YPUserViewControllerFactory.h"
#import "YPMySpaceVC.h"

#import "YPImRoomCoreV2.h"
#import "YPManagerSettingController.h"
#import "YPRoomViewControllerFactory.h"
#import "YPPurseViewControllerFactory.h"
#import "YPAlertControllerCenter.h"
#import "YPYYViewControllerCenter.h"

#import "YPOnlineListTwoView.h"
#import "YPContributionListView.h"

#import "YPRoomlotteryView.h"
#import "YPPlacardView.h"
#import "YPGiftListView.h"
#import "YPFansListViewController.h"
#import "YPAlertControllerCenter.h"

#import "YPMessageListView.h"
#import "YPSessionListViewController.h"
#import "YPSessionViewController.h"
#import "YPRoomMessageview.h"
#import "YPGameRoomVC+ToolBar.h"
#import "YPZaJinDanRankList.h"
#import "YPZaJinDanRecordeList.h"
#import "YPSceretGiftView.h"

#import "YPRoomMessageViewController.h"
#import "YPRoomMessageTableViewController.h"
#import "YPNotiFriendVC.h"

#import "YPJXAuthorizationAlertHelper.h"
#import "YPPurseCore.h"
#import "YPSpaceCardView.h"

#import "MMPopupWindow.h"

#import "YPShareView.h"
#import "YPRoomNoticeView.h"

#import "YPGiftBoxView.h"
#import "YPRoomRankView.h"

#import "YPRuleView.h"

@implementation YPGameRoomVC (Alert)

- (void)showPlacardView {
    
    
    NSString *title = self.roomInfo.roomDesc.length>0?self.roomInfo.roomDesc:@"暂无房间话题";
    NSString *content = self.roomInfo.roomNotice.length>0?self.roomInfo.roomNotice:@"暂无房间内容";

    [YPRoomNoticeView show:title content:content];
    
//    YPPlacardView *YPPlacardView = [[NSBundle mainBundle] loadNibNamed:@"YPPlacardView" owner:nil options:nil][0];
//    if (self.roomInfo.roomDesc.length > 0) {
//        YPPlacardView.tomiclabel.text = self.roomInfo.roomDesc;
//    } else {
//        YPPlacardView.tomiclabel.text = NSLocalizedString(XCRoomNoTopicJustNow, nil);
//    }
//    if (self.roomInfo.roomNotice.length > 0) {
//        YPPlacardView.contentLabel.text = self.roomInfo.roomNotice;
//    } else {
//        YPPlacardView.contentLabel.text = NSLocalizedString(XCRoomNoContenJustNow, nil);
//    }
//    [YPAlertControllerCenter defaultCenter].alertViewOriginY = 0;
//    [[YPAlertControllerCenter defaultCenter] presentAlertWith:self view:YPPlacardView preferredStyle:TYAlertControllerStyleAlert dismissBlock:nil completionBlock:nil];
//    [YPPlacardView setCloseClick:^{
//        [[YPAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
//    }];
}

#pragma mark - show More Sheet
- (void)showAlertMicroQueue {
//    __block MicroQueueView *microQueueView = [[NSBundle mainBundle] loadNibNamed:@"MicroQueueView" owner:nil options:nil][0];
//    if ([YPRoomViewControllerCenter defaultCenter].systemOperationStatusBarIsShow) {
//        [YPAlertControllerCenter defaultCenter].alertViewOriginY = -20;
//    }else {
//        [YPAlertControllerCenter defaultCenter].alertViewOriginY = 0;
//    }
//    [[YPAlertControllerCenter defaultCenter] presentAlertWith:[YPRoomViewControllerCenter defaultCenter].current view:microQueueView preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleActionSheet dismissBlock:nil completionBlock:^{
//    }];
}
    

#pragma mark ---------- 抽奖 ------------
- (void)showlotteryView {
    YPRoomlotteryView *view = [[NSBundle mainBundle] loadNibNamed:@"YPRoomlotteryView" owner:nil options:nil][0];
    
    view.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight);
    
    if (self.lotteryBoxBigGift.length) {
        
        view.roomlotteryLabel.text = [NSString stringWithFormat:@"20金币一次，最高%@",self.lotteryBoxBigGift];
//        view.showTip.text = [NSString stringWithFormat:@"2.最高可以砸中”%@“",self.lotteryBoxBigGift];
    }
    
//    userInfoCard.navigationController = self.navigationController;
    [YPAlertControllerCenter defaultCenter].alertViewOriginY = 0;
    [[YPAlertControllerCenter defaultCenter]presentAlertWith:self view:view preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:nil completionBlock:nil];
    [view setCloseRoomLotteryView:^{
        [[YPAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
        
    }];
    
    @weakify(self);
//    @weakify(view);
//    view.roomLotteryOtherView
    [view setHelpClickBlock:^{
//        @strongify(self);
//        @strongify(view);
//        view.roomLotteryOtherView.hidden = false;
        [YPRuleView showRule];
    }];
    
    [view setRankClickBlock:^{
        @strongify(self);
//        [[YPAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
        [self showZajindanView];
    }];
    
    [view setRecordeClickBlock:^{
        @strongify(self);
//        @strongify(view);
        [[YPAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
        [self showZajinDanRecordeView];
    }];
}


    
- (void)presentUserCard:(UserID)uid {
    
    
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        __weak typeof(self)weakSelf = self;
        
        [YPSpaceCardView show:uid sendGiftBlock:^(UserID userID) {
            [weakSelf showGiftView:userID];
        } roomFollow:^(BOOL isFoloow) {
            
            if (uid == weakSelf.roomOwner.uid) {
                weakSelf.attendBtn.selected = isFoloow;
            }
        } onView:self.view];
        
//    });
    
    
    

}

- (void)showContributionListView {
    
    __weak typeof(self)weakSelf = self;
    [YPRoomRankView show:^(UserID tagUID) {
        [weakSelf presentUserCard:tagUID];
    }];
    
//    self.bV = [UIView new];
//    self.bV.backgroundColor = [UIColor clearColor];
//    self.bV.frame = self.view.bounds;
//    [[UIApplication sharedApplication].keyWindow addSubview:self.bV];
//
//    UIView *bbV = [UIView new];
//    bbV.backgroundColor = [UIColor blackColor];
//    bbV.alpha = 0.5;
//    bbV.frame = self.view.bounds;
//    [self.bV addSubview:bbV];
//
//    YPContributionListView *v = [[NSBundle mainBundle] loadNibNamed:@"YPContributionListView" owner:nil options:nil][0];
//    [self.bV addSubview:v];
//    @weakify(v);
//    @weakify(self);
//    [v mas_makeConstraints:^(MASConstraintMaker *make) {
//        @strongify(self);
//        make.left.equalTo(@2);
//        make.right.equalTo(@-2);
//        if (iPhoneX) {
//            if (@available(iOS 11.0, *)) {
//                make.top.equalTo(self.bV.mas_safeAreaLayoutGuideTop).offset(180);
//                make.bottom.equalTo(self.bV.mas_safeAreaLayoutGuideBottom);
//            }
//        } else {
//            make.top.equalTo(self.bV.mas_top).offset(180);
//            make.bottom.equalTo(self.bV.mas_bottom);
//        }
//    }];
//
//
//    [v.closeButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
//        @strongify(v);
//        @strongify(self);
//        [self.bV removeFromSuperview];
//        [v removeCore];
//        self.bV = nil;
//    }];
//
//    [v setAlertUserInformation:^(long long uid) {
//        @strongify(v);
//        @strongify(self);
//
//        [self presentUserCard:uid];
//
//    }];
//
//    [bbV addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
//        @strongify(v);
//        @strongify(self);
//        [self.bV removeFromSuperview];
//        [v removeCore];
//        self.bV = nil;
//    }]];
}

- (void)showZajindanView {
    
    self.bV = [UIView new];
    self.bV.backgroundColor = [UIColor clearColor];
    
    CGFloat height = UIApplication.sharedApplication.statusBarFrame.size.height;
    if (height >= 44.0) {
          // 是机型iPhoneX/iPhoneXR/iPhoneXS/iPhoneXSMax
        self.bV.frame = CGRectMake(0, XC_SCREE_H, XC_SCREE_W, XC_SCREE_H+20);
    }else{
        self.bV.frame = CGRectMake(0, XC_SCREE_H, XC_SCREE_W, XC_SCREE_H);
    }
    //self.view.bounds;
    [[UIApplication sharedApplication].keyWindow addSubview:self.bV];
    
//    UIView *bbV = [UIView new];
//    bbV.backgroundColor = [UIColor blackColor];
//    bbV.alpha = 0.0;
//    bbV.frame = self.view.bounds;
//    [self.bV addSubview:bbV];
    
    YPZaJinDanRankList *listView = [[NSBundle mainBundle] loadNibNamed:@"YPZaJinDanRankList" owner:nil options:nil][0];
    //    if (iPhoneX) {
    //        v.frame = CGRectMake(2, 44, kScreenWidth-4, kScreenHeight-20);
    //    } else {
    //        v.frame = CGRectMake(2, 20, kScreenWidth-4, kScreenHeight-20);
    //    }
    [self.bV addSubview:listView];
    
    [UIView animateWithDuration:0.3 animations:^{
        self.bV.transform = CGAffineTransformMakeTranslation(0, -XC_SCREE_H);
    } completion:^(BOOL finished) {
        
    }];
    @weakify(self);
    [listView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.left.equalTo(@0);
        make.right.equalTo(@0);
        if (iPhoneX) {
            if (@available(iOS 11.0, *)) {
                make.top.equalTo(self.bV.mas_safeAreaLayoutGuideTop).offset(210);
                make.bottom.equalTo(self.bV.mas_safeAreaLayoutGuideBottom).offset(0);
            }
        } else {
            make.top.equalTo(self.bV.mas_top).offset(210);
            make.bottom.equalTo(self.bV.mas_bottom).offset(0);
        }
    }];
    
    @weakify(listView);
    [listView.closeButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
        @strongify(listView);
        @strongify(self);
        
        [UIView animateWithDuration:0.3 animations:^{
            
            self.bV.transform = CGAffineTransformIdentity;
        } completion:^(BOOL finished) {
            
            [self.bV removeFromSuperview];
            [listView removeCore];
            self.bV = nil;
        }];
        
    }];
    
    [listView setAlertUserInformation:^(long long uid) {
        @strongify(self);
//        @strongify(v);
        
        [self presentUserCard:uid];
        
    }];
    
//    [bbV addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
//        @strongify(v);
//        @strongify(self);
//        [UIView animateWithDuration:0.3 animations:^{
//
//            self.bV.transform = CGAffineTransformIdentity;
//        } completion:^(BOOL finished) {
//
//            [self.bV removeFromSuperview];
//            [v removeCore];
//            self.bV = nil;
//        }];
//
//    }]];
}

- (void)showZajinDanRecordeView {
    
    self.bV = [UIView new];
    self.bV.backgroundColor = [UIColor clearColor];
    self.bV.frame = self.view.bounds;
    [[UIApplication sharedApplication].keyWindow addSubview:self.bV];
    
    UIView *bbV = [UIView new];
    bbV.backgroundColor = [UIColor blackColor];
    bbV.alpha = 0.5;
    bbV.frame = self.view.bounds;
    [self.bV addSubview:bbV];
    
    YPZaJinDanRecordeList *v = [[NSBundle mainBundle] loadNibNamed:@"YPZaJinDanRecordeList" owner:nil options:nil][0];
    //    if (iPhoneX) {
    //        v.frame = CGRectMake(2, 44, kScreenWidth-4, kScreenHeight-20);
    //    } else {
    //        v.frame = CGRectMake(2, 20, kScreenWidth-4, kScreenHeight-20);
    //    }
    [self.bV addSubview:v];
    @weakify(self);
    [v mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.left.equalTo(@2);
        make.right.equalTo(@-2);
        if (iPhoneX) {
            if (@available(iOS 11.0, *)) {
                make.top.equalTo(self.bV.mas_safeAreaLayoutGuideTop).offset(180);
                make.bottom.equalTo(self.bV.mas_safeAreaLayoutGuideBottom);
            }
        } else {
            make.top.equalTo(self.bV.mas_top).offset(180);
            make.bottom.equalTo(self.bV.mas_bottom);
        }
    }];
    
    @weakify(v);
    [v setCloseActionBlock:^{
        @strongify(v);
        @strongify(self);
        [self.bV removeFromSuperview];
        [v removeCore];
        self.bV = nil;
    }];
    
    [bbV addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        @strongify(v);
        @strongify(self);
        [self.bV removeFromSuperview];
        [v removeCore];
        self.bV = nil;
    }]];
}

- (void)showGiftListView {
    
    self.bV = [UIView new];
    self.bV.backgroundColor = [UIColor clearColor];
    self.bV.frame = CGRectMake(XC_SCREE_W, 0, XC_SCREE_W, XC_SCREE_H);//self.view.bounds;
    [[UIApplication sharedApplication].keyWindow addSubview:self.bV];
    
   
    
    
    [UIView animateWithDuration:0.3 animations:^{
        
        self.bV.transform = CGAffineTransformMakeTranslation(-XC_SCREE_W, 0);
    } completion:^(BOOL finished) {
        
    }];
    
    YPGiftListView *listView = [[NSBundle mainBundle] loadNibNamed:@"YPGiftListView" owner:nil options:nil][0];
    [self.bV addSubview:listView];
    
    UIView *bbV = [[UIView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W-280, XC_SCREE_H)];
    bbV.backgroundColor = [UIColor clearColor];
    //    bbV.alpha = 0.1;
    [self.bV addSubview:bbV];
    
    @weakify(self);
    [listView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.left.equalTo(@0);
        make.right.equalTo(@0);
        if (iPhoneX) {
            if (@available(iOS 11.0, *)) {
                make.top.equalTo(self.bV.mas_safeAreaLayoutGuideTop).offset(-24);
                make.bottom.equalTo(self.bV.mas_safeAreaLayoutGuideBottom);
            }
        } else {
            make.top.equalTo(self.bV.mas_top).offset(-24);
            make.bottom.equalTo(self.bV.mas_bottom);
        }
    }];
    
    @weakify(listView);
    [listView setDidClickCloseBtnBlock:^{
        @strongify(listView);
        @strongify(self);
        [UIView animateWithDuration:0.3 animations:^{
            
            self.bV.transform = CGAffineTransformIdentity;
        } completion:^(BOOL finished) {
            [listView removeFromSuperview];
            [self.bV removeFromSuperview];
            [listView removeCore];
            self.bV = nil;
        }];
    }];
    
    [listView setAlertUserInformation:^(long long uid) {
//        @strongify(v);
        @strongify(self);
        
        [self presentUserCard:uid];

    }];
    
    [bbV addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        @strongify(listView);
        @strongify(self);
        
        [UIView animateWithDuration:0.3 animations:^{
            
            self.bV.transform = CGAffineTransformIdentity;
        } completion:^(BOOL finished) {
            [listView removeFromSuperview];
            [self.bV removeFromSuperview];
            [listView removeCore];
            self.bV = nil;
        }];
        
        //        [self.bV removeFromSuperview];
//        [v removeCore];
//        self.bV = nil;
    }]];
}

- (void)showGiftView:(UserID)uid {
    
    
    [YPGiftBoxView show:uid];
    
}

// 弹出神秘礼物
- (void)showSceretGiftViewWithGiftInfo:(YPGiftSecretInfo *)giftInfo {
    
    [[YPAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        
        YPSceretGiftView *view = [[[NSBundle mainBundle] loadNibNamed:@"YPSceretGiftView" owner:self options:nil] lastObject];
        view.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
        view.info = giftInfo;
        [view setCloseActionBlock:^{
            [[YPAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
        }];
        @weakify(view);
        [[YPAlertControllerCenter defaultCenter] presentAlertWith:[YPRoomViewControllerCenter defaultCenter].current view:view preferredStyle:TYAlertControllerStyleAlert dismissBlock:^{
            
        } completionBlock:^{
            @strongify(view);
            [view showPhoto];
        }];
    });
    
}

- (void)showMessageView {
    
    UIWindow *window = [[UIWindow alloc] initWithFrame:[UIApplication sharedApplication].keyWindow.bounds];
    window.backgroundColor = [UIColor clearColor];
    
    YPRoomMessageViewController *rootVC = [YPRoomMessageViewController new];
    rootVC.messageListWindow = window;
    @weakify(self);
    [rootVC setRoomMessageListDidSelectCell:^(NIMRecentSession *recent, NSString *title) {
        @strongify(self);
        [self showMessageViewWithRecentSession:recent title:title];
    }];
    
    [rootVC setAddBadgeBlock:^{
        @strongify(self);
        [self addBadge];
    }];
    
    [rootVC setDidCloseBlock:^{
        @strongify(self);
        self.messageListWindow = nil;
    }];
    
    [rootVC setSquareBlock:^{
        @strongify(self);
        [self showSquareView];
    }];
    
    window.rootViewController = rootVC;
    
    [window makeKeyAndVisible];
    self.messageListWindow = window;
}

- (void)showSquareView
{
    YPNotiFriendVC *vc = [[YPNotiFriendVC alloc] init];
    [vc updateData];
    [self.navigationController pushViewController:vc animated:YES];
}



- (void)showMessageViewWithRecentSession:(NIMRecentSession *)recentSession title:(NSString *)title {
    UIWindow *window = [[UIWindow alloc] initWithFrame:[UIApplication sharedApplication].keyWindow.bounds];
    window.backgroundColor = [UIColor clearColor];
    
    YPRoomMessageTableViewController *rootVC = [YPRoomMessageTableViewController new];
    rootVC.messageListWindow = self.messageListWindow;
    rootVC.messageWindow = window;
    rootVC.topTitle = title;
    rootVC.recentSession = recentSession;
    @weakify(self);
    [rootVC setRoomMessageListDidSelectCell:^UINavigationController *{
        @strongify(self);
        return self.navigationController;
    }];
    [rootVC setAddBadgeBlock:^{
        @strongify(self);
        [self addBadge];
    }];
    [rootVC setDidCloseBlock:^{
        @strongify(self);
        self.messageWindow = nil;
    }];
    [rootVC setDidCloseAllBlock:^{
        @strongify(self);
        self.messageListWindow = nil;
        self.messageWindow = nil;
    }];
    window.rootViewController = rootVC;
    
    [window makeKeyAndVisible];
    self.messageWindow = window;
    
    
}

- (void)showOnlineUsersView {
    
    self.bV = [UIView new];
    self.bV.backgroundColor = [UIColor clearColor];
    self.bV.frame = self.view.bounds;
    [[UIApplication sharedApplication].keyWindow addSubview:self.bV];
    
    UIView *bbV = [UIView new];
    bbV.backgroundColor = [UIColor blackColor];
    bbV.alpha = 0.5;
    bbV.frame = self.view.bounds;
    [self.bV addSubview:bbV];
    
    YPOnlineListTwoView *v = [[NSBundle mainBundle] loadNibNamed:@"YPOnlineListTwoView" owner:nil options:nil][0];
    v.backgroundColor = [UIColorHex(04000F) colorWithAlphaComponent:0.9];
    [self.bV addSubview:v];
    @weakify(v);
    @weakify(self);
    [v mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.left.right.equalTo(self.bV);
        if (iPhoneX) {
            if (@available(iOS 11.0, *)) {
                make.top.equalTo(self.bV.mas_safeAreaLayoutGuideTop).offset(180);
                make.bottom.equalTo(self.bV.mas_safeAreaLayoutGuideBottom);
            }
        } else {
            make.top.equalTo(self.bV.mas_top).offset(180);
            make.bottom.equalTo(self.bV.mas_bottom);
        }
    }];

    
    [v.closeButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
        @strongify(v);
        @strongify(self);
        [v removeFromSuperview];
        [self.bV removeFromSuperview];
        [v removeCore];
        self.bV = nil;
    }];
    
    [v setSendGiftBlock:^(long long uid, NSString *nick) {
        @strongify(v);
        @strongify(self);
        [v removeFromSuperview];
        [self.bV removeFromSuperview];
        [v removeCore];
        self.bV = nil;
        
        [self showGiftView:uid];
    }];
    
    [v setAlertUserInformation:^(long long uid) {
        @strongify(v);
        @strongify(self);
        [self.bV removeFromSuperview];
        [v removeCore];
        self.bV = nil;

        [self presentUserCard:uid];
    }];
    
    [bbV addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        @strongify(v);
        @strongify(self);
        [self.bV removeFromSuperview];
        [v removeCore];
        self.bV = nil;
    }]];
}

//弹出分享框
- (void)showSharePanelView
{
    YPShareInfo *shareInfo = [[YPShareInfo alloc] init];
    shareInfo.roomOwnerUID = self.roomInfo.uid;
    shareInfo.type = HJShareTypeRoom;
    [YPShareView show:shareInfo];
    
}

- (void)dismissSharePanel {
    [[YPAlertControllerCenter defaultCenter]dismissAlertNeedBlock:NO];
}

//邀请好友
- (void)inviteFriend {
    YPAlertControllerCenter *center = [YPAlertControllerCenter defaultCenter];
    [center dismissAlertNeedBlock:NO];
    YPFansListViewController *vc = YPMessageBoard(@"YPFansListViewController");
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)showSecretGiftWithInfo:(YPGiftSecretInfo *)giftInfo {
    
    
}

//弹出送礼物框
- (void)showGiftContainerView
{
    [YPGiftBoxView showAllMic:0];
}



#pragma mark - 显示房主已关闭直播间
- (void) showRoomOwnnerExit {
    
    [MMAlertView hideAll];
    [MMSheetView hideAll];
    if ([[YPYYViewControllerCenter currentViewController] isKindOfClass:[TYAlertController class]]) {
        [(TYAlertController *)[YPYYViewControllerCenter currentViewController] dismissViewControllerAnimated:NO];
        [self showRoomOwnnerExit];
        return;
    }

    if ([self.delegate respondsToSelector:@selector(roomExit)]) {
        [self.delegate roomExit];
    }
    [self.sharePanelSheet dismissViewControllerAnimated:YES];

    YPFinishLiveView *finishLiveView = [YPFinishLiveView loadFromNIB];
    finishLiveView.frame = CGRectMake(0, 0, self.view.frame.size.width, [UIScreen mainScreen].bounds.size.height);
    finishLiveView.uid = self.roomOwner.uid;
    [self.view addSubview:finishLiveView];
}




//举报
- (void)onReportButtonClick {
    
    __weak typeof(self)weakSelf = self;
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"政治敏感" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPUserCoreHelp) userReportSaveWithUid:GetCore(YPImRoomCoreV2).roomOwnerInfo.uid reportType:1 type:2 phoneNo:nil requestId:@"YPGameRoomVC+Alert"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"色情低俗" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPUserCoreHelp) userReportSaveWithUid:GetCore(YPImRoomCoreV2).roomOwnerInfo.uid reportType:2 type:2 phoneNo:nil requestId:@"YPGameRoomVC+Alert"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"广告骚扰" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPUserCoreHelp) userReportSaveWithUid:GetCore(YPImRoomCoreV2).roomOwnerInfo.uid reportType:3 type:2 phoneNo:nil requestId:@"YPGameRoomVC+Alert"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"人身攻击" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPUserCoreHelp) userReportSaveWithUid:GetCore(YPImRoomCoreV2).roomOwnerInfo.uid reportType:4 type:2 phoneNo:nil requestId:@"YPGameRoomVC+Alert"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"加入房间黑名单" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf showBlackListAlerView];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
    
    [self presentViewController:alter animated:YES completion:nil];
    
}

//举报用户/房间
- (void)userReportSaveSuccessWithType:(NSInteger)type requestId:(NSString *)requestId {
    if ([requestId isEqualToString:@"YPGameRoomVC+Alert"]) {
        [MBProgressHUD showSuccess:@"举报成功，我们将尽快为你处理" toView:self.view];
        
    }
}
- (void)userReportSaveFailth:(NSString *)message type:(NSInteger)type requestId:(NSString *)requestId {
    if ([requestId isEqualToString:@"YPGameRoomVC+Alert"]) {
        
    }
}


#pragma mark - show Exit Alert
- (void)showExitAlert {
    
    // 准备初始化配置参数
    NSString *title = NSLocalizedString(XCRoomCloseRoom, nil);
    NSString *message = NSLocalizedString(XCRoomDidSureCloseRoom, nil);
    NSString *okButtonTitle = NSLocalizedString(XCRoomConfirm, nil);
    NSString *cancelButtonTitle = NSLocalizedString(XCRoomCancel, nil);
    
    // 初始化
    UIAlertController *alertDialog = [UIAlertController alertControllerWithTitle:title message:message preferredStyle:UIAlertControllerStyleAlert];
    
    // 创建操作
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:okButtonTitle style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
        [GetCore(YPRoomCoreV2Help) closeRoom:uid];
        [[YPRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
    }];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:cancelButtonTitle style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        
    }];
    
    // 添加操作
    [alertDialog addAction:okAction];
    [alertDialog addAction:cancelAction];
    
    // 呈现警告视图
    [self presentViewController:alertDialog animated:YES completion:nil];
}

//弹出拉黑名单提醒
- (void)showBlackListAlerView {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPersonalInfoInBlackListTitle, nil) message:NSLocalizedString(XCPersonalInfoInBlackListMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [MBProgressHUD showSuccess:NSLocalizedString(XCPersonalInfoInBlackListSusccess, nil)];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil)style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}




- (void)showGiftView {
    
    [YPGiftBoxView showAllMic:0];
    
    
}

- (void)showPersonalPage:(UserID)uid {
    
    
    YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
    vc.userID = uid;
    
    [[YPRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];


}

- (void)showInviteAlert {
    UIAlertController *beInviteAlert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCRoomOwnerOrManagerBringYouUp, nil) message:NSLocalizedString(XCRoomOwnerOrManagerBringYouUpMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];

//    @weakify(self);
    UIAlertAction *agree = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
//        @strongify(self);
    }];
    [beInviteAlert addAction:agree];
    [self presentViewController:beInviteAlert animated:YES completion:nil];

}

- (void)showBalanceNotEnougth {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPurseNoMoneyTitle, nil) message:NSLocalizedString(XCPurseNoMoneyMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    @weakify(self);
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        if (self.giftContainerSheet != nil) {
            [self.giftContainerSheet dismissViewControllerAnimated:YES];
        }
        
//        [[YPGiftBoxView shareGiftBoxView] close];
        UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
        [self.navigationController pushViewController:vc animated:YES];
        
        
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}

//加入黑名单
- (void)showAlertWithAddBlackList:(YPChatRoomMember *)member {
    NSString *title = [NSString stringWithFormat:@"%@%@",NSLocalizedString(XCRoomMoveToBlackTitle, nil),member.nick];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:NSLocalizedString(XCRoomMoveToBlackMsg, nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPImRoomCoreV2) markBlackList:[member.account longLongValue] enable:YES];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:enter];
    [alert addAction:cancel];
    [[YPRoomViewControllerCenter defaultCenter].current presentViewController:alert animated:YES completion:nil];
    
}

- (void)showAuthorizationWithCode:(NSInteger)code errorMessage:(NSString *)errorMessage {
    @weakify(self);
    [YPJXAuthorizationAlertHelper showAuthorizationAlertWithViewController:self code:code message:errorMessage didTapActionHandler:^(UIViewController * _Nullable toViewController) {
        @strongify(self);
        
        if (toViewController) {
            [self.navigationController pushViewController:toViewController animated:YES];
        }
    }];
}



@end
