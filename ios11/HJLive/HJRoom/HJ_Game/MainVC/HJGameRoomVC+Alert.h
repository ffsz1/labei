//
//  HJGameRoomVC+Alert.h
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC.h"
#import "HJUserCoreHelp.h"
#import "HJUserCoreClient.h"


@interface HJGameRoomVC (Alert) <HJUserCoreClient>
- (void)presentUserCard:(UserID)uid;

//送礼物给uid
- (void)showGiftView:(UserID)uid;
//弹出公告
- (void)showPlacardView;
//弹出排麦
- (void)showAlertMicroQueue;
//弹出砸蛋
- (void)showlotteryView;
/** 弹出房间贡献榜*/
- (void)showContributionListView;
//弹出在线用户
- (void)showOnlineUsersView;
// 弹出礼物记录
- (void)showGiftListView;
// 弹出神秘礼物
- (void)showSceretGiftViewWithGiftInfo:(HJGiftSecretInfo *)giftInfo;
//弹出分享框
- (void)showSharePanelView;
//弹出送礼物框
- (void)showGiftContainerView;
//举报
- (void)onReportButtonClick;
//房主关播
- (void) showRoomOwnnerExit;
//关播弹窗
- (void)showExitAlert;
//弹出拉黑名单提醒
- (void)showBlackListAlerView;

//邀请弹窗
- (void)showInviteAlert;
//余额不足弹窗
- (void)showBalanceNotEnougth;
//加入黑名单
- (void)showAlertWithAddBlackList:(ChatRoomMember *)member;

//消息弹窗
- (void)showMessageView;
//砸金蛋排行榜
- (void)showContributionListView;
//砸金蛋中奖纪录
- (void)showZajinDanRecordeView;

//实名验证弹窗
- (void)showAuthorizationWithCode:(NSInteger)code errorMessage:(NSString *)errorMessage;
@end
