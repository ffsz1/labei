//
//  HJGameRoomVC+Alert.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC+Alert.h"
#import "HJGameRoomVC+Delegate.h"
#import "HJWKWebViewController.h"
#import "TYAlertController.h"
#import "HJRoomViewControllerCenter.h"
#import "HJPraiseCore.h"

#import "HJUserViewControllerFactory.h"
#import "HJMySpaceVC.h"

#import "HJImRoomCoreV2.h"
#import "HJManagerSettingController.h"
#import "HJRoomViewControllerFactory.h"
#import "HJPurseViewControllerFactory.h"
#import "HJAlertControllerCenter.h"
#import "YYViewControllerCenter.h"

#import "HJOnlineListTwoView.h"
#import "HJContributionListView.h"

#import "HJRoomlotteryView.h"
#import "HJPlacardView.h"
#import "HJGiftListView.h"
#import "HJFansListViewController.h"
#import "HJAlertControllerCenter.h"

#import "HJMessageListView.h"
#import "HJSessionListViewController.h"
#import "HJSessionViewController.h"
#import "HJRoomMessageview.h"
#import "HJGameRoomVC+ToolBar.h"
#import "HJZaJinDanRankList.h"
#import "HJZaJinDanRecordeList.h"
#import "HJSceretGiftView.h"

#import "HJRoomMessageViewController.h"
#import "HJRoomMessageTableViewController.h"
#import "HJNotiFriendVC.h"

#import "JXAuthorizationAlertHelper.h"
#import "PurseCore.h"
#import "HJSpaceCardView.h"

#import "MMPopupWindow.h"

#import "HJShareView.h"
#import "HJRoomNoticeView.h"

#import "HJGiftBoxView.h"
#import "HJRoomRankView.h"

#import "HJRuleView.h"

@implementation HJGameRoomVC (Alert)

- (void)showPlacardView {
    
    
    NSString *title = self.roomInfo.roomDesc.length>0?self.roomInfo.roomDesc:@"暂无房间话题";
    NSString *content = self.roomInfo.roomNotice.length>0?self.roomInfo.roomNotice:@"暂无房间内容";

    [HJRoomNoticeView show:title content:content];
    
//    HJPlacardView *HJPlacardView = [[NSBundle mainBundle] loadNibNamed:@"HJPlacardView" owner:nil options:nil][0];
//    if (self.roomInfo.roomDesc.length > 0) {
//        HJPlacardView.tomiclabel.text = self.roomInfo.roomDesc;
//    } else {
//        HJPlacardView.tomiclabel.text = NSLocalizedString(XCRoomNoTopicJustNow, nil);
//    }
//    if (self.roomInfo.roomNotice.length > 0) {
//        HJPlacardView.contentLabel.text = self.roomInfo.roomNotice;
//    } else {
//        HJPlacardView.contentLabel.text = NSLocalizedString(XCRoomNoContenJustNow, nil);
//    }
//    [HJAlertControllerCenter defaultCenter].alertViewOriginY = 0;
//    [[HJAlertControllerCenter defaultCenter] presentAlertWith:self view:HJPlacardView preferredStyle:TYAlertControllerStyleAlert dismissBlock:nil completionBlock:nil];
//    [HJPlacardView setCloseClick:^{
//        [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
//    }];
}

#pragma mark - show More Sheet
- (void)showAlertMicroQueue {
//    __block MicroQueueView *microQueueView = [[NSBundle mainBundle] loadNibNamed:@"MicroQueueView" owner:nil options:nil][0];
//    if ([HJRoomViewControllerCenter defaultCenter].systemOperationStatusBarIsShow) {
//        [HJAlertControllerCenter defaultCenter].alertViewOriginY = -20;
//    }else {
//        [HJAlertControllerCenter defaultCenter].alertViewOriginY = 0;
//    }
//    [[HJAlertControllerCenter defaultCenter] presentAlertWith:[HJRoomViewControllerCenter defaultCenter].current view:microQueueView preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleActionSheet dismissBlock:nil completionBlock:^{
//    }];
}
    

#pragma mark ---------- 抽奖 ------------
- (void)showlotteryView {
    HJRoomlotteryView *view = [[NSBundle mainBundle] loadNibNamed:@"HJRoomlotteryView" owner:nil options:nil][0];
    
    view.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight);
    
    if (self.lotteryBoxBigGift.length) {
        
        view.roomlotteryLabel.text = [NSString stringWithFormat:@"20开心一次，最高%@",self.lotteryBoxBigGift];
//        view.showTip.text = [NSString stringWithFormat:@"2.最高可以砸中”%@“",self.lotteryBoxBigGift];
    }
    
//    userInfoCard.navigationController = self.navigationController;
    [HJAlertControllerCenter defaultCenter].alertViewOriginY = 0;
    [[HJAlertControllerCenter defaultCenter]presentAlertWith:self view:view preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:nil completionBlock:nil];
    [view setCloseRoomLotteryView:^{
        [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
        
    }];
    
    @weakify(self);
//    @weakify(view);
//    view.roomLotteryOtherView
    [view setHelpClickBlock:^{
//        @strongify(self);
//        @strongify(view);
//        view.roomLotteryOtherView.hidden = false;
//        [HJRuleView showRule];
        
        HJWKWebViewController *viewController = [[HJWKWebViewController alloc] init];
        NSString *URLString = [NSString stringWithFormat:@"%@/front/drawRule/index.html", [HJHttpRequestHelper getHostUrl]];
        viewController.url = [NSURL URLWithString:URLString];
        [self.navigationController pushViewController:viewController animated:YES];

    }];
    
    [view setRankClickBlock:^{
        @strongify(self);
//        [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
        [self showZajindanView];
    }];
    
    [view setRecordeClickBlock:^{
        @strongify(self);
//        @strongify(view);
        [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
        [self showZajinDanRecordeView];
    }];
}


    
- (void)presentUserCard:(UserID)uid {
    
    
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        __weak typeof(self)weakSelf = self;
        
        [HJSpaceCardView show:uid sendGiftBlock:^(UserID userID) {
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
    [HJRoomRankView show:^(UserID tagUID) {
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
//    HJContributionListView *v = [[NSBundle mainBundle] loadNibNamed:@"HJContributionListView" owner:nil options:nil][0];
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
    
    HJZaJinDanRankList *listView = [[NSBundle mainBundle] loadNibNamed:@"HJZaJinDanRankList" owner:nil options:nil][0];
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
                make.top.equalTo(self.bV.mas_safeAreaLayoutGuideTop).offset(230);
                make.bottom.equalTo(self.bV.mas_safeAreaLayoutGuideBottom).offset(18);
            }
        } else {
            make.top.equalTo(self.bV.mas_top).offset(230);
            make.bottom.equalTo(self.bV.mas_bottom).offset(18);
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
    
    HJZaJinDanRecordeList *v = [[NSBundle mainBundle] loadNibNamed:@"HJZaJinDanRecordeList" owner:nil options:nil][0];
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
    
    HJGiftListView *listView = [[NSBundle mainBundle] loadNibNamed:@"HJGiftListView" owner:nil options:nil][0];
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
    
    
    [HJGiftBoxView show:uid];
    
}

// 弹出神秘礼物
- (void)showSceretGiftViewWithGiftInfo:(HJGiftSecretInfo *)giftInfo {
    
    [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        
        HJSceretGiftView *view = [[[NSBundle mainBundle] loadNibNamed:@"HJSceretGiftView" owner:self options:nil] lastObject];
        view.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
        view.info = giftInfo;
        [view setCloseActionBlock:^{
            [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
        }];
        @weakify(view);
        [[HJAlertControllerCenter defaultCenter] presentAlertWith:[HJRoomViewControllerCenter defaultCenter].current view:view preferredStyle:TYAlertControllerStyleAlert dismissBlock:^{
            
        } completionBlock:^{
            @strongify(view);
            [view showPhoto];
        }];
    });
    
}

- (void)showMessageView {
    
    UIWindow *window = [[UIWindow alloc] initWithFrame:[UIApplication sharedApplication].keyWindow.bounds];
    window.backgroundColor = [UIColor clearColor];
    
    HJRoomMessageViewController *rootVC = [HJRoomMessageViewController new];
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
    HJNotiFriendVC *vc = [[HJNotiFriendVC alloc] init];
    [vc updateData];
    [self.navigationController pushViewController:vc animated:YES];
}



- (void)showMessageViewWithRecentSession:(NIMRecentSession *)recentSession title:(NSString *)title {
    UIWindow *window = [[UIWindow alloc] initWithFrame:[UIApplication sharedApplication].keyWindow.bounds];
    window.backgroundColor = [UIColor clearColor];
    
    HJRoomMessageTableViewController *rootVC = [HJRoomMessageTableViewController new];
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
    
    HJOnlineListTwoView *v = [[NSBundle mainBundle] loadNibNamed:@"HJOnlineListTwoView" owner:nil options:nil][0];
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
    HJShareInfo *shareInfo = [[HJShareInfo alloc] init];
    shareInfo.roomOwnerUID = self.roomInfo.uid;
    shareInfo.type = HJShareTypeRoom;
    [HJShareView show:shareInfo];
    
}

- (void)dismissSharePanel {
    [[HJAlertControllerCenter defaultCenter]dismissAlertNeedBlock:NO];
}

//邀请好友
- (void)inviteFriend {
    HJAlertControllerCenter *center = [HJAlertControllerCenter defaultCenter];
    [center dismissAlertNeedBlock:NO];
    HJFansListViewController *vc = HJMessageBoard(@"HJFansListViewController");
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)showSecretGiftWithInfo:(HJGiftSecretInfo *)giftInfo {
    
    
}

//弹出送礼物框
- (void)showGiftContainerView
{
    [HJGiftBoxView showAllMic:0];
}



#pragma mark - 显示房主已关闭直播间
- (void) showRoomOwnnerExit {
    
    [MMAlertView hideAll];
    [MMSheetView hideAll];
    if ([[YYViewControllerCenter currentViewController] isKindOfClass:[TYAlertController class]]) {
        [(TYAlertController *)[YYViewControllerCenter currentViewController] dismissViewControllerAnimated:NO];
        [self showRoomOwnnerExit];
        return;
    }

    if ([self.delegate respondsToSelector:@selector(roomExit)]) {
        [self.delegate roomExit];
    }
    [self.sharePanelSheet dismissViewControllerAnimated:YES];

    HJFinishLiveView *finishLiveView = [HJFinishLiveView loadFromNIB];
    finishLiveView.frame = CGRectMake(0, 0, self.view.frame.size.width, [UIScreen mainScreen].bounds.size.height);
    finishLiveView.uid = self.roomOwner.uid;
    [self.view addSubview:finishLiveView];
}




//举报
- (void)onReportButtonClick {
    
    __weak typeof(self)weakSelf = self;
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"政治敏感" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:GetCore(HJImRoomCoreV2).roomOwnerInfo.uid reportType:1 type:2 phoneNo:nil requestId:@"HJGameRoomVC+Alert"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"色情低俗" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:GetCore(HJImRoomCoreV2).roomOwnerInfo.uid reportType:2 type:2 phoneNo:nil requestId:@"HJGameRoomVC+Alert"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"广告骚扰" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:GetCore(HJImRoomCoreV2).roomOwnerInfo.uid reportType:3 type:2 phoneNo:nil requestId:@"HJGameRoomVC+Alert"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"人身攻击" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:GetCore(HJImRoomCoreV2).roomOwnerInfo.uid reportType:4 type:2 phoneNo:nil requestId:@"HJGameRoomVC+Alert"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"加入房间黑名单" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf showBlackListAlerView];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
    
    [self presentViewController:alter animated:YES completion:nil];
    
}

//举报用户/房间
- (void)userReportSaveSuccessWithType:(NSInteger)type requestId:(NSString *)requestId {
    if ([requestId isEqualToString:@"HJGameRoomVC+Alert"]) {
        [MBProgressHUD showSuccess:@"举报成功，我们将尽快为你处理" toView:self.view];
        
    }
}
- (void)userReportSaveFailth:(NSString *)message type:(NSInteger)type requestId:(NSString *)requestId {
    if ([requestId isEqualToString:@"HJGameRoomVC+Alert"]) {
        
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
        UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
        [GetCore(HJRoomCoreV2Help) closeRoom:uid];
        [[HJRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
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
    
    [HJGiftBoxView showAllMic:0];
    
    
}

- (void)showPersonalPage:(UserID)uid {
    
    
    HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
    vc.userID = uid;
    
    [[HJRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];


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
        
//        [[HJGiftBoxView shareGiftBoxView] close];
        UIViewController *vc = [[HJPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
        [self.navigationController pushViewController:vc animated:YES];
        
        
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}

//加入黑名单
- (void)showAlertWithAddBlackList:(ChatRoomMember *)member {
    NSString *title = [NSString stringWithFormat:@"%@%@",NSLocalizedString(XCRoomMoveToBlackTitle, nil),member.nick];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:NSLocalizedString(XCRoomMoveToBlackMsg, nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJImRoomCoreV2) markBlackList:[member.account longLongValue] enable:YES];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:enter];
    [alert addAction:cancel];
    [[HJRoomViewControllerCenter defaultCenter].current presentViewController:alert animated:YES completion:nil];
    
}

- (void)showAuthorizationWithCode:(NSInteger)code errorMessage:(NSString *)errorMessage {
    @weakify(self);
    [JXAuthorizationAlertHelper showAuthorizationAlertWithViewController:self code:code message:errorMessage didTapActionHandler:^(UIViewController * _Nullable toViewController) {
        @strongify(self);
        
        if (toViewController) {
            [self.navigationController pushViewController:toViewController animated:YES];
        }
    }];
}



@end
