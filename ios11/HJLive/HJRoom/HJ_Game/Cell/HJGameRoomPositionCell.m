//
//  HJGameRoomPositionCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomPositionCell.h"

//core
#import "HJRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "HJRoomQueueCoreV2Help.h"
#import "HJRoomQueueCoreClient.h"
#import "HJImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJUserCoreHelp.h"
#import "HJUserCoreClient.h"
#import "HJPraiseCore.h"
#import "HJPraiseCoreClient.h"
#import "HJAuthCoreHelp.h"
#import "HJBalanceErrorClient.h"

//Category
#import "UIImageView+YYWebImage.h"
#import "UIImage+extension.h"

//m
#import "HJUserViewControllerFactory.h"
#import "TYAlertController.h"
#import "HJRoomViewControllerCenter.h"
//view
//vc
#import "HJMySpaceVC.h"
#import "YYActionSheetViewController.h"

@interface HJGameRoomPositionCell ()
<
HJImRoomCoreClient,
HJRoomCoreClient,
HJRoomQueueCoreClient,
HJPraiseCoreClient,
HJBalanceErrorClient
>

@property (strong, nonatomic) NSMutableDictionary *micro;
@property (strong, nonatomic) ChatRoomInfo *roomInfo;
@property (strong, nonatomic) UserInfo *roomOwner;
@property (assign, nonatomic) BOOL isLike;
@property (strong, nonatomic) TYAlertController *giftAlertView;


@end

@implementation HJGameRoomPositionCell

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
    self.micro = GetCore(HJImRoomCoreV2).micQueue;
    self.roomInfo = [GetCore(HJRoomCoreV2Help) getCurrentRoomInfo];
//    if (self.roomInfo != nil) {
//        @weakify(self);
//        [GetCore(HJUserCoreHelp)getUserInfo:self.roomInfo.uid refresh:YES success:^(UserInfo *info) {
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
    UserID myUid = [GetCore(HJAuthCoreHelp)getUid].userIDValue;
    if (myUid == self.info.uid) {
        [self showSelfPanel:myUid];
    }else if (myUid == self.roomInfo.uid) {
        [self showOwnerPanelSheet:self.info.uid position:self.position];
    }
    else {
        if (GetCore(HJRoomQueueCoreV2Help).myMember.is_manager) {
            [self showManagerPanelSheetWithUid:self.info.uid];
        }else {
            [self showPersonPanelSheet:self.info.uid];
        }
    }
}

//- (void)upMicroBtnClick:(UITapGestureRecognizer *)sender {
//    UserID myUid = [GetCore(HJAuthCoreHelp)getUid].userIDValue;
//    
//    if (self.roomOwner) {
//        if (myUid == self.roomOwner.uid) { //房主
//            [GetCore(HJRoomQueueCoreV2Help) lockMicPlace:self.position.intValue]; //锁麦位
//        }else if (GetCore(HJRoomQueueCoreV2Help).myMember.is_manager) {
//            [self showManagerPanelSheet];
//        }else { //用户
//            [GetCore(HJRoomQueueCoreV2Help) upMic:self.position.intValue];//上麦
//        }
//    }
//}
//
//- (void)freeTheMicroClick:(UITapGestureRecognizer *)sender {
//    if (self.roomOwner) {
//        UserID myUid = [GetCore(HJAuthCoreHelp)getUid].userIDValue;
//        if (myUid == self.roomOwner.uid) { //房主
//            [GetCore(HJRoomQueueCoreV2Help) freeMicPlace:self.position.intValue];//解锁麦位
//            
//        }else if (GetCore(HJRoomQueueCoreV2Help).myMember.is_manager) {
//            [GetCore(HJRoomQueueCoreV2Help) freeMicPlace:self.position.intValue];//解锁麦位
//        }
//    }
//}

#pragma mark - setter

- (void)setInfo:(UserInfo *)info {
    _info = info;
    
//    UserID myUid = [GetCore(HJAuthCoreHelp)getUid].userIDValue;
//    [GetCore(HJPraiseCore)isLike:info.uid isLikeUid:myUid];
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
    YYActionSheetViewController *sheet = [[YYActionSheetViewController alloc]init];
    @weakify(self);
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomSendGift, nil) block:^(YYActionSheetViewController *controller) {
        @strongify(self);
        [self showGiftView];
    }];
    if (self.HJRoomMicInfo.micState == JXIMRoomMicPostionStateLock) {
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetThatMicBeUnQuie, nil) block:^(YYActionSheetViewController *controller) {
            [GetCore(HJRoomQueueCoreV2Help) openMic:position.intValue];
            
        }];
    } else {
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetThatMicBeQuiet, nil) block:^(YYActionSheetViewController *controller) {
            [GetCore(HJRoomQueueCoreV2Help) closeMic:position.intValue];
        }];
    }
    
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomKickOutTheRoom, nil) block:^(YYActionSheetViewController *controller) {
        [HJMidSure kickUser:uid didKickFinish:nil];
    }];
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomKickHimDownMic, nil) block:^(YYActionSheetViewController *controller) {
        [GetCore(HJRoomQueueCoreV2Help) kickDownMic:uid position:position.intValue];
    }];
    
    if (self.member.is_manager) {
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomMoveOutManager, nil) block:^(YYActionSheetViewController *controller) {
            [GetCore(HJImRoomCoreV2) markManagerList:uid enable:NO];
        }];
        
    }else if (!self.member.is_manager && !self.member.is_creator) {
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetToBeManager, nil) block:^(YYActionSheetViewController *controller) {
            [GetCore(HJImRoomCoreV2) markManagerList:uid enable:YES];
        }];
    }
    
    [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoInBlackList, nil) block:^(YYActionSheetViewController *controller) {
        [self showAlertWithAddBlackList];
    }];
    
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomCheckInfo, nil) block:^(YYActionSheetViewController *controller) {
        @strongify(self);
        [self showPersonalPage:self.info.uid];
    }];
    
//    if (!self.isLike) {
//        [sheet addButtonWithTitle:NSLocalizedString(XCPraiseTitle, nil) block:^(YYActionSheetViewController *controller) {
//            UserID myUid = [GetCore(HJAuthCoreHelp)getUid].userIDValue;
//            [GetCore(HJPraiseCore) praise:myUid bePraisedUid:uid];
//        }];
//    }
    
    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YYActionSheetViewController *controller) {
        
    }];
    
    [sheet show];
}

- (void)showManagerPanelSheet {
    YYActionSheetViewController *sheet = [[YYActionSheetViewController alloc]init];
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomUpMic, nil) block:^(YYActionSheetViewController *controller) {
        [GetCore(HJRoomQueueCoreV2Help) upMic:self.position.intValue];
    }];
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomLockTheMicSort, nil) block:^(YYActionSheetViewController *controller) {
        [GetCore(HJRoomQueueCoreV2Help) lockMicPlace:self.position.intValue];
    }];
    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YYActionSheetViewController *controller) {
        
    }];
    [sheet show];
}


- (void)showManagerPanelSheetWithUid:(UserID)uid {
    if (uid > 0) {
        ChatRoomMember *member = [GetCore(HJRoomQueueCoreV2Help) findTheMemberByUserId:uid];
        YYActionSheetViewController *sheet = [[YYActionSheetViewController alloc]init];
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomSendGift, nil) block:^(YYActionSheetViewController *controller) {
            [self showGiftView];
        }];
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomCheckInfo, nil) block:^(YYActionSheetViewController *controller) {
            [self showPersonalPage:self.info.uid];
        }];
        
        if (!member.is_manager || !member.is_creator) {
            
            [sheet addButtonWithTitle:NSLocalizedString(XCRoomKickOutTheRoom, nil) block:^(YYActionSheetViewController *controller) {
                [HJMidSure kickUser:uid didKickFinish:nil];
            }];
            [sheet addButtonWithTitle:NSLocalizedString(XCRoomKickHimDownMic, nil) block:^(YYActionSheetViewController *controller) {
                [GetCore(HJRoomQueueCoreV2Help) kickDownMic:uid position:self.position.intValue];
            }];
            if (self.HJRoomMicInfo.micState == JXIMRoomMicPostionStateLock) {
                [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetThatMicBeUnQuie, nil) block:^(YYActionSheetViewController *controller) {
                    [GetCore(HJRoomQueueCoreV2Help) openMic:self.position.intValue];
                    
                }];
            } else {
                [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetThatMicBeQuiet, nil) block:^(YYActionSheetViewController *controller) {
                    [GetCore(HJRoomQueueCoreV2Help) closeMic:self.position.intValue];
                    
                }];
            }

            [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoInBlackList, nil) block:^(YYActionSheetViewController *controller) {
                [self showAlertWithAddBlackList];
            }];
        }
        [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YYActionSheetViewController *controller) {
            
        }];
        
        [sheet show];
    }
}

- (void)showPersonPanelSheet:(UserID)uid {
    UserID myUid = [GetCore(HJAuthCoreHelp)getUid].userIDValue;
    
    YYActionSheetViewController *sheet = [[YYActionSheetViewController alloc]init];
    if (myUid != uid) {
        [sheet addButtonWithTitle:NSLocalizedString(XCRoomSendGift, nil) block:^(YYActionSheetViewController *controller) {
            [self showGiftView];
        }];
    }
    
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomCheckInfo, nil) block:^(YYActionSheetViewController *controller) {
        [self showPersonalPage:self.info.uid];
    }];
    
//    if (myUid != uid) {
//        if (!self.isLike) {
//            [sheet addButtonWithTitle:NSLocalizedString(XCPraiseTitle, nil) block:^(YYActionSheetViewController *controller) {
//                UserID myUid = [GetCore(HJAuthCoreHelp)getUid].userIDValue;
//                [GetCore(HJPraiseCore) praise:myUid bePraisedUid:uid];
//            }];
//        }
//    }
    
    
    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YYActionSheetViewController *controller) {
        
    }];
    
    [sheet show];
}

- (void)showSelfPanel:(UserID)uid {
    YYActionSheetViewController *sheet = [[YYActionSheetViewController alloc]init];
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomOutMicAndListen, nil) block:^(YYActionSheetViewController *controller) {
        [GetCore(HJRoomQueueCoreV2Help) downMic];
    }];
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomCheckInfo, nil) block:^(YYActionSheetViewController *controller) {
        [self showPersonalPage:self.info.uid];
    }];
    
    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YYActionSheetViewController *controller) {
        
    }];
    
    [sheet show];
}


- (void)showAlertWithAddBlackList {
    NSString *title = [NSString stringWithFormat:@"%@%@",NSLocalizedString(XCRoomMoveToBlackTitle, nil),self.info.nick];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:NSLocalizedString(XCRoomMoveToBlackMsg, nil) preferredStyle:UIAlertControllerStyleAlert];
    @weakify(self);
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        [GetCore(HJImRoomCoreV2) markBlackList:self.info.uid enable:YES];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:enter];
    [alert addAction:cancel];
    [[HJRoomViewControllerCenter defaultCenter].current presentViewController:alert animated:YES completion:nil];
    
}

#pragma mark - Private Method

- (void)showPersonalPage:(UserID)uid {
    
    
    HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
    vc.userID = uid;
    
    [[HJRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];


}

- (void)showGiftView {
//    self.giftView = [XCGameRoomGiftView loadFromNib];
//    self.giftAlertView = [TYAlertController alertControllerWithAlertView:self.giftView preferredStyle:TYAlertControllerStyleActionSheet transitionAnimation:TYAlertTransitionAnimationFade];
//    self.giftView.info = self.info;
//    self.giftView.isInRoom  = YES;
//    self.giftAlertView.backgoundTapDismissEnable = YES;
//    //    self.giftView.delegate =
//    [[HJRoomViewControllerCenter defaultCenter].current presentViewController:self.giftAlertView animated:YES completion:^{
//        
//    }];
}

@end
