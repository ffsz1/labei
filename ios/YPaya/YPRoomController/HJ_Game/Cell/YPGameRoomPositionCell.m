//
//  YPGameRoomPositionCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomPositionCell.h"

//core
#import "YPRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "YPRoomQueueCoreV2Help.h"
#import "HJRoomQueueCoreClient.h"
#import "YPImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "YPUserCoreHelp.h"
#import "HJUserCoreClient.h"
#import "YPPraiseCore.h"
#import "HJPraiseCoreClient.h"
#import "YPAuthCoreHelp.h"
#import "HJBalanceErrorClient.h"

//Category
#import "UIImageView+YYWebImage.h"
#import "UIImage+extension.h"

//m
#import "YPUserViewControllerFactory.h"
#import "TYAlertController.h"
#import "YPRoomViewControllerCenter.h"
//view
//vc
#import "YPMySpaceVC.h"
#import "YPYYActionSheetViewController.h"

@interface YPGameRoomPositionCell ()
<
HJImRoomCoreClient,
HJRoomCoreClient,
HJRoomQueueCoreClient,
HJPraiseCoreClient,
HJBalanceErrorClient
>

@property (strong, nonatomic) NSMutableDictionary *micro;
@property (strong, nonatomic) YPChatRoomInfo *roomInfo;
@property (strong, nonatomic) UserInfo *roomOwner;
@property (assign, nonatomic) BOOL isLike;
@property (strong, nonatomic) TYAlertController *giftAlertView;


@end

@implementation YPGameRoomPositionCell

- (void)awakeFromNib {
    [super awakeFromNib];
    AddCoreClient(HJImRoomCoreClient, self);
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJRoomQueueCoreClient, self);
    AddCoreClient(HJPraiseCoreClient, self);
    AddCoreClient(HJBalanceErrorClient, self);
    [self addGestureToImageView];
    self.micOrderLabel.layer.masksToBounds = YES;
    self.micOrderLabel.layer.cornerRadius = CGRectGetWidth(self.micOrderLabel.frame)/2
    ;
    
}

- (void)updateView {
    self.micro = GetCore(YPImRoomCoreV2).micQueue;
    self.roomInfo = [GetCore(YPRoomCoreV2Help) getCurrentRoomInfo];
//    if (self.roomInfo != nil) {
//        @weakify(self);
//        [GetCore(YPUserCoreHelp)getUserInfo:self.roomInfo.uid refresh:YES success:^(UserInfo *info) {
//            @strongify(self);
//            self.roomOwner = info;
//
//        }];
//    }
}


- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)addGestureToImageView {
    self.positionStatusNormalBg.userInteractionEnabled = NO;
    self.avatar.userInteractionEnabled = NO;
    self.positionStatusLockBg.userInteractionEnabled = NO;
}

+ (void)awakeFromNib {
    [super awakeFromNib];
    
}

#pragma mark - BalanceErrorClient
- (void)onBalanceNotEnough {
    if (self.giftAlertView != nil) {
        [self.giftAlertView dismissViewControllerAnimated:YES];
    }
}

#pragma mark - Gesture Method
- (void)avatarClick:(UITapGestureRecognizer *)sender {
    UserID myUid = [GetCore(YPAuthCoreHelp)getUid].userIDValue;
    if (myUid == self.info.uid) {
        [self showSelfPanel:myUid];
    }else if (myUid == self.roomInfo.uid) {
        [self showOwnerPanelSheet:self.info.uid position:self.position];
    }
    else {
        if (GetCore(YPRoomQueueCoreV2Help).myMember.is_manager) {
            [self showManagerPanelSheetWithUid:self.info.uid];
        }else {
            [self showPersonPanelSheet:self.info.uid];
        }
    }
}

//- (void)upMicroBtnClick:(UITapGestureRecognizer *)sender {
//    UserID myUid = [GetCore(YPAuthCoreHelp)getUid].userIDValue;
//    
//    if (self.roomOwner) {
//        if (myUid == self.roomOwner.uid) { //房主
//            [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:self.position.intValue]; //锁麦位
//        }else if (GetCore(YPRoomQueueCoreV2Help).myMember.is_manager) {
//            [self showManagerPanelSheet];
//        }else { //用户
//            [GetCore(YPRoomQueueCoreV2Help) upMic:self.position.intValue];//上麦
//        }
//    }
//}
//
//- (void)freeTheMicroClick:(UITapGestureRecognizer *)sender {
//    if (self.roomOwner) {
//        UserID myUid = [GetCore(YPAuthCoreHelp)getUid].userIDValue;
//        if (myUid == self.roomOwner.uid) { //房主
//            [GetCore(YPRoomQueueCoreV2Help) freeMicPlace:self.position.intValue];//解锁麦位
//            
//        }else if (GetCore(YPRoomQueueCoreV2Help).myMember.is_manager) {
//            [GetCore(YPRoomQueueCoreV2Help) freeMicPlace:self.position.intValue];//解锁麦位
//        }
//    }
//}

#pragma mark - setter

- (void)setInfo:(UserInfo *)info {
    _info = info;
    
//    UserID myUid = [GetCore(YPAuthCoreHelp)getUid].userIDValue;
//    [GetCore(YPPraiseCore)isLike:info.uid isLikeUid:myUid];
}

#pragma mark - RoomCoreClient

- (void)onCurrentRoomInfoChanged {
    [self updateView];
}

#pragma mark - PraiseCoreClient

- (void)onRequestIsLikeSuccess:(BOOL)isLike islikeUid:(UserID)islikeUid {
    self.isLike = isLike;
}

- (void)onPraiseSuccess:(UserID)uid {
    if (uid == self.info.uid) {
        self.isLike = YES;
    }
}

- (void)onCancelSuccess:(UserID)uid {
    if (uid == self.info.uid) {
        self.isLike = NO;
    }
}

- (void)onPraiseFailth:(NSString *)msg {
//    [MBProgressHUD showError:msg];
    [MBProgressHUD hideHUD];
}

#pragma mark - Alert

- (void)showOwnerPanelSheet:(UserID)uid position:(NSString *)position {
    YPYYActionSheetViewController *sheet = [[YPYYActionSheetViewController alloc]init];
    @weakify(self);
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomSendGift, nil) block:^(YPYYActionSheetViewController *controller) {
        @strongify(self);
        [self showGiftView];
    }];
    if (self.YPRoomMicInfo.micState == JXIMRoomMicPostionStateLock) {
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetThatMicBeUnQuie, nil) block:^(YPYYActionSheetViewController *controller) {
            [GetCore(YPRoomQueueCoreV2Help) openMic:position.intValue];
            
        }];
    } else {
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetThatMicBeQuiet, nil) block:^(YPYYActionSheetViewController *controller) {
            [GetCore(YPRoomQueueCoreV2Help) closeMic:position.intValue];
        }];
    }
    
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomKickOutTheRoom, nil) block:^(YPYYActionSheetViewController *controller) {
        [YPMidSure kickUser:uid didKickFinish:nil];
    }];
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomKickHimDownMic, nil) block:^(YPYYActionSheetViewController *controller) {
        [GetCore(YPRoomQueueCoreV2Help) kickDownMic:uid position:position.intValue];
    }];
    
    if (self.member.is_manager) {
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomMoveOutManager, nil) block:^(YPYYActionSheetViewController *controller) {
            [GetCore(YPImRoomCoreV2) markManagerList:uid enable:NO];
        }];
        
    }else if (!self.member.is_manager && !self.member.is_creator) {
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetToBeManager, nil) block:^(YPYYActionSheetViewController *controller) {
            [GetCore(YPImRoomCoreV2) markManagerList:uid enable:YES];
        }];
    }
    
    [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoInBlackList, nil) block:^(YPYYActionSheetViewController *controller) {
        [self showAlertWithAddBlackList];
    }];
    
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomCheckInfo, nil) block:^(YPYYActionSheetViewController *controller) {
        @strongify(self);
        [self showPersonalPage:self.info.uid];
    }];
    
//    if (!self.isLike) {
//        [sheet addButtonWithTitle:NSLocalizedString(XCPraiseTitle, nil) block:^(YPYYActionSheetViewController *controller) {
//            UserID myUid = [GetCore(YPAuthCoreHelp)getUid].userIDValue;
//            [GetCore(YPPraiseCore) praise:myUid bePraisedUid:uid];
//        }];
//    }
    
    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YPYYActionSheetViewController *controller) {
        
    }];
    
    [sheet show];
}

- (void)showManagerPanelSheet {
    YPYYActionSheetViewController *sheet = [[YPYYActionSheetViewController alloc]init];
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomUpMic, nil) block:^(YPYYActionSheetViewController *controller) {
        [GetCore(YPRoomQueueCoreV2Help) upMic:self.position.intValue];
    }];
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomLockTheMicSort, nil) block:^(YPYYActionSheetViewController *controller) {
        [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:self.position.intValue];
    }];
    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YPYYActionSheetViewController *controller) {
        
    }];
    [sheet show];
}


- (void)showManagerPanelSheetWithUid:(UserID)uid {
    if (uid > 0) {
        YPChatRoomMember *member = [GetCore(YPRoomQueueCoreV2Help) findTheMemberByUserId:uid];
        YPYYActionSheetViewController *sheet = [[YPYYActionSheetViewController alloc]init];
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomSendGift, nil) block:^(YPYYActionSheetViewController *controller) {
            [self showGiftView];
        }];
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomCheckInfo, nil) block:^(YPYYActionSheetViewController *controller) {
            [self showPersonalPage:self.info.uid];
        }];
        
        if (!member.is_manager || !member.is_creator) {
            
            [sheet addButtonWithTitle:NSLocalizedString(XCRoomKickOutTheRoom, nil) block:^(YPYYActionSheetViewController *controller) {
                [YPMidSure kickUser:uid didKickFinish:nil];
            }];
            [sheet addButtonWithTitle:NSLocalizedString(XCRoomKickHimDownMic, nil) block:^(YPYYActionSheetViewController *controller) {
                [GetCore(YPRoomQueueCoreV2Help) kickDownMic:uid position:self.position.intValue];
            }];
            if (self.YPRoomMicInfo.micState == JXIMRoomMicPostionStateLock) {
                [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetThatMicBeUnQuie, nil) block:^(YPYYActionSheetViewController *controller) {
                    [GetCore(YPRoomQueueCoreV2Help) openMic:self.position.intValue];
                    
                }];
            } else {
                [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetThatMicBeQuiet, nil) block:^(YPYYActionSheetViewController *controller) {
                    [GetCore(YPRoomQueueCoreV2Help) closeMic:self.position.intValue];
                    
                }];
            }

            [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoInBlackList, nil) block:^(YPYYActionSheetViewController *controller) {
                [self showAlertWithAddBlackList];
            }];
        }
        [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YPYYActionSheetViewController *controller) {
            
        }];
        
        [sheet show];
    }
}

- (void)showPersonPanelSheet:(UserID)uid {
    UserID myUid = [GetCore(YPAuthCoreHelp)getUid].userIDValue;
    
    YPYYActionSheetViewController *sheet = [[YPYYActionSheetViewController alloc]init];
    if (myUid != uid) {
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomSendGift, nil) block:^(YPYYActionSheetViewController *controller) {
            [self showGiftView];
        }];
    }
    
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomCheckInfo, nil) block:^(YPYYActionSheetViewController *controller) {
        [self showPersonalPage:self.info.uid];
    }];
    
//    if (myUid != uid) {
//        if (!self.isLike) {
//            [sheet addButtonWithTitle:NSLocalizedString(XCPraiseTitle, nil) block:^(YPYYActionSheetViewController *controller) {
//                UserID myUid = [GetCore(YPAuthCoreHelp)getUid].userIDValue;
//                [GetCore(YPPraiseCore) praise:myUid bePraisedUid:uid];
//            }];
//        }
//    }
    
    
    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YPYYActionSheetViewController *controller) {
        
    }];
    
    [sheet show];
}

- (void)showSelfPanel:(UserID)uid {
    YPYYActionSheetViewController *sheet = [[YPYYActionSheetViewController alloc]init];
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomOutMicAndListen, nil) block:^(YPYYActionSheetViewController *controller) {
        [GetCore(YPRoomQueueCoreV2Help) downMic];
    }];
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomCheckInfo, nil) block:^(YPYYActionSheetViewController *controller) {
        [self showPersonalPage:self.info.uid];
    }];
    
    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YPYYActionSheetViewController *controller) {
        
    }];
    
    [sheet show];
}


- (void)showAlertWithAddBlackList {
    NSString *title = [NSString stringWithFormat:@"%@%@",NSLocalizedString(XCRoomMoveToBlackTitle, nil),self.info.nick];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:NSLocalizedString(XCRoomMoveToBlackMsg, nil) preferredStyle:UIAlertControllerStyleAlert];
    @weakify(self);
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        [GetCore(YPImRoomCoreV2) markBlackList:self.info.uid enable:YES];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:enter];
    [alert addAction:cancel];
    [[YPRoomViewControllerCenter defaultCenter].current presentViewController:alert animated:YES completion:nil];
    
}

#pragma mark - Private Method

- (void)showPersonalPage:(UserID)uid {
    
    
    YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
    vc.userID = uid;
    
    [[YPRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];


}

- (void)showGiftView {
//    self.giftView = [XCGameRoomGiftView loadFromNib];
//    self.giftAlertView = [TYAlertController alertControllerWithAlertView:self.giftView preferredStyle:TYAlertControllerStyleActionSheet transitionAnimation:TYAlertTransitionAnimationFade];
//    self.giftView.info = self.info;
//    self.giftView.isInRoom  = YES;
//    self.giftAlertView.backgoundTapDismissEnable = YES;
//    //    self.giftView.delegate =
//    [[YPRoomViewControllerCenter defaultCenter].current presentViewController:self.giftAlertView animated:YES completion:^{
//        
//    }];
}

@end
