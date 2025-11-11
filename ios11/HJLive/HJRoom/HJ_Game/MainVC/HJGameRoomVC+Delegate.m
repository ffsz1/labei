//
//  HJGameRoomVC+Delegate.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC+Delegate.h"
#import "HJGameRoomVC+Alert.h"
#import "HJRoomViewControllerCenter.h"
#import "HJPurseViewControllerFactory.h"
#import "HJAlertControllerCenter.h"
#import "HJSessionViewController.h"
#import "HJGiftBoxView.h"

@implementation HJGameRoomVC (Delegate)


- (void)microresetNetSuccess {
    
}

- (void)intoMicroSuccess {
    [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
}

#pragma mark - HJGameRoomPositionViewDelegate
- (void)positionViewShowUserInfoCardWithUid:(UserID)uid{
    [self presentUserCard:uid];
}
#pragma mark - MessageTableViewDelegate
//公屏查看用户信息
- (void)showUserInfoCardWithUid:(UserID)uid {
    [self presentUserCard:uid];
}

- (void)showGitToUidFromOnline:(UserID)uid withName:(NSString *)name {
    [self showGiftWithUid:uid withName:name];
}

- (void)showGiftWithUid:(UserID)uid withName:(NSString *)name {
    
    
    [HJGiftBoxView show:uid];
    
    
}


- (void)onSharePanelCancelClicked {
    [self.sharePanelSheet dismissViewControllerAnimated:YES];
}

#pragma mark - HJGiftViewContainerDelegate
//充值
- (void)onHJGiftViewContainerRechargeClicked
{
    [self.giftContainerSheet dismissViewControllerAnimated:YES];
    UIViewController *vc = [[HJPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
    [self.navigationController pushViewController:vc animated:YES];
}



@end
