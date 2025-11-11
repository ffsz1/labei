//
//  HJGameRoomVC+CoreClient.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC+CoreClient.h"
#import "HJGameRoomVC+Animation.h"
#import "HJGameRoomVC+Alert.h"
#import "HJGameRoomVC+ToolBar.h"
#import "HJGameRoomVC+PK.h"

//gif播放
#import <FLAnimatedImageView.h>

#import "UIView+XCToast.h"

#import "HJRoomViewControllerCenter.h"
#import "HJPurseViewControllerFactory.h"
#import <UIImageView+WebCache.h>

#import "HJFaceCore.h"
#import "HJFaceReceiveInfo.h"
#import <UIImageView+WebCache.h>
#import "HJWKWebViewController.h"
#import "PurseCore.h"
#import <UIImageView+WebCache.h>
#import "HJAlertControllerCenter.h"
#import "HJVersionCoreHelp.h"
#import "HJFaceImageTool.h"
#import "HJMusicCore.h"
#import "YYViewControllerCenter.h"
#import "HJMusicViewController.h"
#import "HJAddMusicViewController.h"
#import "HJGameRoomContainerVC.h"
#import "HJLongZhuCoreClient.h"
#import "HJLongZhuCore.h"
#import "HJGameRoomVC+LongZhu.h"

@implementation HJGameRoomVC (CoreClient)

#pragma mark - GiftCoreClient
// 送礼物爆出神秘礼物
- (void)didReceiveSecretGiftWithInfo:(HJGiftSecretInfo *)giftInfo {
    
    if (giftInfo) {
        [self showSceretGiftViewWithGiftInfo:giftInfo];
    }
}

- (void)mySelfIsInBalckList:(BOOL)state
{
    if (state) {
        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomYouAlreadyInBlackList, nil) duration:3 position:YYToastPositionCenter];
        [[HJRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
    }
}

#pragma mark - BalanceErrorClient
//余额额不足
- (void)onBalanceNotEnough {
    @weakify(self);
    [HJAlertControllerCenter defaultCenter].dismissComplete = ^{
        @strongify(self);
        [self showBalanceNotEnougth];
    };
    [[HJAlertControllerCenter defaultCenter]dismissAlertNeedBlock:YES];
}

#pragma mark - MeetCoreClient

//用户本人说话
- (void)onMySpeakStateUpdate:(BOOL)speaking
{
    //判断是否为房主
    NSString *uid = GetCore(HJAuthCoreHelp).getUid;
    UserID myUID = [uid userIDValue];
    if (self.roomInfo.uid == myUID) {
        self.isSpeaking = speaking;
        [self updateMainPositionMicroAnimation];
    }
}

- (void)onSpeakUsersReport:(NSArray *)userInfos {
//    UserID myUid = [GetCore(AuthCore) getUid].userIDValue;
    for (int i = 0; i < userInfos.count; i++) {
        NSNumber *temp = [userInfos objectAtIndex:i];
        if ([temp intValue] == [self.mainPositionMember.account longLongValue]) {
            [self updateMainPositionMicroAnimation];
        }
    }
}


- (void)onJoinMeetingSuccess {
    
    [self updateToolBar];

    
}


#pragma mark - FaceCoreClient
//播放gif表情
- (void)onReceiveFace:(NSMutableArray<HJFaceReceiveInfo *> *)faceRecieveInfos {
    for (HJFaceReceiveInfo *item in faceRecieveInfos) {
        if (item.uid == [self.mainPositionMember.account longLongValue]) {
//            HJFaceConfigInfo *faceInfo = [GetCore(FaceCore)findFaceInfoById:item.faceId];
            [[HJFaceImageTool shareFaceImageTool] queryImage:item imageView:self.playImageView success:^(HJFaceReceiveInfo *info) {
                
            } failure:^(NSError *error) {
                
            }];
//                [self.playImageView sd_setImageWithURL:[NSURL URLWithString:faceInfo.faceGifUrl]];
                @weakify(self);
//                self.playImageView.loopCompletionBlock = ^(NSUInteger loopCountRemaining) {
//                    @strongify(self);
//                    [self.playImageView stopAnimating];
//                    self.playImageView.animatedImage = nil;
//                };
        }
        
    }
}

#pragma mark - LongZhuCoreClient
- (void)onReceiveLongZhuWithModel:(HJRoomLongZhuMsgModel *)model isSuPei:(BOOL)isSuPei {
    
    
    if (model.uid.integerValue == [self.mainPositionMember.account longLongValue] && model.numArr.count) {
        
        UIImageView *imageView = self.playImageView;
        
        if (isSuPei) {
            NSInteger num1 = 0;
            NSInteger num2 = 0;
            NSInteger num3 = 0;
            
            if (model.numArr.count >= 1) {
                num1 = [model.numArr[0] integerValue];
            }
            
            if (model.numArr.count >= 2) {
                num2 = [model.numArr[1] integerValue];
            }
            if (model.numArr.count >= 3) {
                num3 = [model.numArr[2] integerValue];
            }
            
            if (model.isShowd) {
                
                imageView.image = [GetCore(HJLongZhuCore) composeImgWithImge1:[UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%zd",num1]] img2:[UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%zd",num2]] img3:[UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%zd",num3]]];
            }
            else {
                imageView.image = [GetCore(HJLongZhuCore) composeImgWithImge1:[UIImage imageNamed:@"room_game_longzhu_help"] img2:[UIImage imageNamed:@"room_game_longzhu_help"] img3:[UIImage imageNamed:@"room_game_longzhu_help"]];
            }
        }
        else {
            
            if (model.isShowd) {
                
                imageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%@",model.numArr[0]]];
            }
            else {
                imageView.image = [UIImage imageNamed:@"room_game_longzhu_help"];
            }
        }
        
        imageView.transform = CGAffineTransformMakeScale(0.3, 0.3);
        
        __block UIImage *temImage = imageView.image;
        
        [UIView animateWithDuration:1
                              delay:0
             usingSpringWithDamping:0.1
              initialSpringVelocity:1.5
                            options:UIViewAnimationOptionCurveEaseOut | UIViewAnimationOptionBeginFromCurrentState
                         animations:^{
                             
                             imageView.transform = CGAffineTransformMakeScale(0.6, 0.6);
                             
                         } completion:^(BOOL finished) {
                             
                             NSInteger time = 0.5f;
                             if (model.isShowd) {
                                 time = 2.f;
                             }
                             
                             dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(time * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                                 
                                 if (!GetCore(HJFaceCore).isShowingFace) {
                                     
                                     if (imageView.image == temImage) {
                                         
                                         imageView.image = nil;
                                         imageView.transform = CGAffineTransformMakeScale(1, 1);
                                     }
                                 }
                                 
                             });
                         }];
    }
    
}

#pragma mark - ImRoomCoreV2
- (void)onGetRoomQueueSuccessV2:(NSMutableArray<HJIMQueueItem *> *)info{
    [self updateToolBar];
    [self updateRoomOwnerMicState];
    [self updateLongZhu];
}

#pragma mark - RoomQueueCoreClient
- (void)onHJRoomMicInfoChange {
    [self updateToolBar];
}

- (void)onMicroQueueUpdate:(NSMutableArray<HJIMQueueItem *> *)micQueue {
    [self updateRoomOwnerMicState];
    [self updateToolBar];
}

- (void)onManagerAdd:(ChatRoomMember *)member{
    [self isShowDiscButton];

}
- (void)onManagerRemove:(ChatRoomMember *)member{
   [self isShowDiscButton];
}




#pragma mark - ImMessageCoreClient

- (void)onMeInterChatRoomSuccess {
    [self isShowDiscButton];
    [GetCore(HJMusicCore) playBackgroundMusicList];
    
    [self checkPKState:NO];
}


- (void)onSendMessageSuccess:(NIMMessage *)msg {
    NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
    if (msg.messageType == 100) {
        if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
            Attachment *attachment = (Attachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_Face) {
                if (attachment.second == Custom_Noti_Sub_Face_Send) {
                    [self.faceAlertView dismissViewControllerAnimated:YES];
                }
            }
        }
    }
}

//房主邀请上麦
- (void)onMicroBeInvite {
    [self showInviteAlert];
}

- (void)onMicroBeKicked {
    [UIView showToastInKeyWindow:NSLocalizedString(XCRoomYouAlreadyBeDownByOwnerOrManager, nil) duration:3.0 position:(YYToastPosition)YYToastPositionBottomWithRecordButton];
    if ([[YYViewControllerCenter currentViewController] isKindOfClass:[HJMusicViewController class]] || [[YYViewControllerCenter currentViewController] isKindOfClass:[HJAddMusicViewController class]]) {
        [self.navigationController popToRootViewControllerAnimated:YES];
    }
}

#pragma mark - PraiseCoreClient
- (void)onPraiseSuccess:(UserID)uid {
    if (uid == self.roomOwner.uid) {
        self.isLike = YES;
    }
}

- (void)onCancelSuccess:(UserID)uid {
    if (uid == self.roomOwner.uid) {
        self.isLike = NO;
    }
}

- (void) onRequestIsLikeSuccess:(BOOL)isLike islikeUid:(UserID)islikeUid {
    if (islikeUid == self.roomOwner.uid) {
        self.isLike = isLike;
    }
    
}
- (void) onRequestIsLikeFailth:(NSString *)msg {
//    [MBProgressHUD showError:msg];
    [MBProgressHUD hideHUD];
}

#pragma mark - ActivityCoreClient
- (void)getActivityInfoSuccess {
    
        
        self.activityInfo = GetCore(HJActivityCore).activityInfo;
        
        if (self.activityInfo) {
            self.activityView.hidden = NO;
            self.activityView.activityImageView.contentMode = UIViewContentModeScaleAspectFill;
            [self.activityView.activityImageView sd_setImageWithURL:[NSURL URLWithString:self.activityInfo.alertWinPic] placeholderImage:[UIImage imageNamed:placeholder_image_square] completed:^(UIImage * _Nullable image, NSError * _Nullable error, SDImageCacheType cacheType, NSURL * _Nullable imageURL) {
            }];
            UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(skipActivityVC:)];
            self.activityView.userInteractionEnabled = YES;
            [self.activityView addGestureRecognizer:tap];
        }else {
            self.activityView.userInteractionEnabled = NO;
        }
}

- (void)getAllActivityInfoSuccess:(NSArray *)arr {
  
    if (arr.count>0) {
        self.banaCommonArray = arr;
        if (arr != nil && arr.count > 0) {
                     NSMutableArray *array = [NSMutableArray array];
                     for (ActivityInfo *info in arr) {
                         [array addObject:info.alertWinPic];
                     }
            self.banaCommonView.imageURLStringsGroup = [array copy];
                 }
   }else {
       self.activityView.userInteractionEnabled = NO;
   }
   
}
//跳转活动页
- (void)skipActivityVC:(UITapGestureRecognizer *)tap {

    HJWKWebViewController *vc = [[HJWKWebViewController alloc]init];
    vc.url = [NSURL URLWithString:self.activityInfo.skipUrl];
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - private method
- (void)isShowDiscButton{
//    if (GetCore(ImRoomCoreV2).myMember.is_manager || GetCore(ImRoomCoreV2).myMember.is_creator) {
//        self.discButton.hidden = NO; //骰子
//    }else {
//        self.discButton.hidden = YES;
//    }
}


- (void)updateRoomOwnerMicState {
    HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:0];
    HJRoomQueueInfo *temp = item.queueInfo;
    if (temp) {
        self.mainPositionMember = temp.chatRoomMember;
        
        
    }
    
    
    [self updateCoverView];
    [self updateToolBar];
}


#pragma mark - GiftCoreClient
- (void)onReceiveGift:(HJGiftAllMicroSendInfo *)giftReceiveInfo isALLChannelSend:(BOOL)isALLChannelSend {
    
    if (!isALLChannelSend) {
        
        HJGiftAllMicroSendInfo *info = giftReceiveInfo;
        BOOL isAll = NO;
        
        if (info.targetUids.count >= 2) {
            isAll = YES;
        }
        if (info) {
            self.isAll = isAll;
            self.giftInfo = info;

            self.giftRecordBtn.hidden = NO;
            
        }
        else {
            self.giftRecordBtn.hidden = YES;
        }
    }
    self.giftRecordBtn.hidden = YES;
}

@end
