//
//  YPGameRoomPositionView.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomPositionView.h"
#import "YPGameRoomPositionCell.h"
#import "YPGameRoomSpeakingPositionCell.h"

//core
#import "YPImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"
#import "YPRoomQueueInfo.h"
#import "YPRoomQueueCoreV2Help.h"
#import "HJRoomQueueCoreClient.h"
#import "YPRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"

#import "YPMeetingCore.h"
#import "HJMeetingCoreClient.h"
#import "HJFaceCoreClient.h"
#import "YPFaceCore.h"
#import "RoomUIClient.h"
#import "HJRoomCoreClient.h"
#import "YPUserCoreHelp.h"
#import "YPAuthCoreHelp.h"
#import "HJBalanceErrorClient.h"
#import "HJLongZhuCoreClient.h"
#import "YPLongZhuCore.h"

//sdk
#import <UIImageView+WebCache.h>
#import <FLAnimatedImageView.h>
#import "UIImage+extension.h"

#import "YPAlertControllerCenter.h"
#import "TYAlertController.h"
#import "YPPurseViewControllerFactory.h"
#import "YPRoomViewControllerFactory.h"
#import "YPOnlineListVC.h"
#import "YPYYActionSheetViewController.h"
#import "YPUserViewControllerFactory.h"
#import "YPRoomViewControllerCenter.h"

//view
#import "YPFaceImageTool.h"

//m
#import "YPGiftSendAllMicroAvatarInfo.h"
#import "UIColor+UIColor_Hex.h"

#import "HJIReachability.h"
#import "YPReachabilityCore.h"
#import "YPIMQueueItem.h"

#import "NSString+Utils.h"

#import "UIView+getTopVC.h"

#import "YPRoomOnlineListVC.h"

#import "YPGiftBoxView.h"

//讲话动画View
#import "YPRippleAnimationView.h"

#define EACHCELLWIDTH (([UIScreen mainScreen].bounds.size.width-30) / 4)
#define EACHCELLHEIGHT 96
#define EACHLINECELNUM 4

#define STATUSVIEWHEIGHT 65
#define ALLCELLNUM 8
#define LINENUM (ALLCELLNUM/EACHLINECELNUM)
#define KMargin 5

@interface YPGameRoomPositionView ()
<
UICollectionViewDelegate,
UICollectionViewDataSource,
UICollectionViewDelegateFlowLayout,
HJRippleAnimationDelagte,
HJImRoomCoreClient,
HJRoomCoreClient,
HJRoomQueueCoreClient,
HJMeetingCoreClient,
HJFaceCoreClient,
HJBalanceErrorClient,
RoomUIClient,
HJRoomCoreClient,
HJLongZhuCoreClient,
ReachabilityClient
>

@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (weak, nonatomic) IBOutlet UICollectionView *speakingCollectionView;
@property (strong, nonatomic) NSMutableArray<YPIMQueueItem *> *mircoList;
@property (strong ,nonatomic) YPChatRoomInfo *roomInfo;
@property (assign, nonatomic) BOOL isSpeaking;
@property (strong, nonatomic) NSMutableArray *faceArr;
@property (strong, nonatomic) TYAlertController *giftAlertView;
@property (strong, nonatomic) NSMutableArray<FLAnimatedImageView *> *faceImageViewArr;
@property (strong, nonatomic) NSMutableArray<UIImageView *> *speakingImageView;
@property (strong, nonatomic) NSMutableArray *speakingAnimatePicArr;
@property (strong, nonatomic) NSMutableArray *speakingAnimatePicArrForGirl;

@property (nonatomic ,assign) BOOL enableCharm;//开启魅力值功能
@property (strong, nonatomic) NSMutableDictionary <NSString *,id> *charmInfoMap;//魅力值map


@end


@implementation YPGameRoomPositionView
#pragma mark - life cycle
+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"YPGameRoomPositionView" owner:self options:nil].lastObject;
}
- (void)layoutSubviews {
    [super layoutSubviews];
    @weakify(self);
    dispatch_async(dispatch_get_main_queue(), ^{
        //刷新完成
        @strongify(self);
        [self saveThePositionInCore];
    });
    [self updateView];
}

- (void)awakeFromNib {
    [super awakeFromNib];

    [self addCore];
    
    self.collectionView.userInteractionEnabled = NO;
    [self.collectionView registerNib:[UINib nibWithNibName:@"YPGameRoomPositionCell" bundle:nil] forCellWithReuseIdentifier:@"YPGameRoomPositionCell"];
    [self.speakingCollectionView registerNib:[UINib nibWithNibName:@"YPGameRoomPositionCell" bundle:nil] forCellWithReuseIdentifier:@"YPGameRoomPositionCell"];
    [self.speakingCollectionView registerNib:[UINib nibWithNibName:@"YPGameRoomSpeakingPositionCell" bundle:nil] forCellWithReuseIdentifier:@"YPGameRoomSpeakingPositionCell"];
    [self.collectionView reloadData];
    
    self.charmInfoMap = [GetCore(YPRoomCoreV2Help) getRoomCharmInfoMap];
//    self.enableCharm = self.charmInfoMap.allKeys.count > 0;
    YPChatRoomInfo *roomInfo = GetCore(YPImRoomCoreV2).currentRoomInfo;
    if (roomInfo.charmOpen ==0) {//用于控制魅力值开关[ 0.隐藏; 1.显示 ]
        self.enableCharm = NO;
    }else{
        self.enableCharm = YES;
    }
 
}

//更新麦位魅力值
- (void)updateMicCharm:(NSMutableArray<YPRoomMemberCharmInfoModel *> *)memberCharmInfoList {
    
    self.mircoList = GetCore(YPImRoomCoreV2).micQueue;
    
    for (YPRoomMemberCharmInfoModel *charmInfoModel in memberCharmInfoList) {
        [self.charmInfoMap setValue:@{@"value" : JX_STR_AVOID_nil(charmInfoModel.value),
                                      @"withHat" : [NSNumber numberWithBool:charmInfoModel.withHat]
                                      } forKey:JX_STR_AVOID_nil(charmInfoModel.uid)];
    }
    ///保存魅力值map
    [GetCore(YPRoomCoreV2Help) saveCharmInfoMap:self.charmInfoMap];
//    self.enableCharm = YES;
    YPChatRoomInfo *roomInfo = GetCore(YPImRoomCoreV2).currentRoomInfo;
    if (roomInfo.charmOpen ==0) {//用于控制魅力值开关[ 0.隐藏; 1.显示 ]
        self.enableCharm = NO;
    }else{
        self.enableCharm = YES;
    }
    [self.collectionView reloadData];
}


- (void)dealloc {
    RemoveCoreClientAll(self);
}



#pragma mark - UICollectionViewDataSource
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return 8;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    YPIMQueueItem *item = [self.mircoList safeObjectAtIndex:indexPath.item+1];
    YPChatRoomMember *member = item.queueInfo.chatRoomMember;
    YPRoomMicInfo *state = item.queueInfo.mic_info;
    if (collectionView == self.collectionView) {
        YPGameRoomPositionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPGameRoomPositionCell" forIndexPath:indexPath];
        NSInteger gender = member.gender;
        cell.nicknameLabel.text = [NSString stringWithFormat:@"%zd号麦位",indexPath.row+1];
         cell.nickName_leading_layout.constant = 5;
        cell.nicknameLabel.textAlignment = NSTextAlignmentCenter;
        if (indexPath.item==7) {
            cell.nicknameLabel.alpha = 1;
            cell.nicknameLabel.textColor = UIColorHex(FFE66A);
        }else{
            cell.nicknameLabel.alpha = 0.6;
            cell.nicknameLabel.textColor = UIColorHex(FFFFFF);
        }
        
        
        cell.positionStatusNormalBg.image = [UIImage imageNamed:indexPath.item==7?@"yp_room_8set":@"xc_room_icon_paimai"];
        
        if (member) {
            cell.sexBtn.hidden = false;
            cell.nicknameLabel.alpha = 1;
        } else {
            cell.sexBtn.hidden = YES;
        }
//        cell.nicknameLabel.textColor = [UIColor whiteColor];
        cell.micOrderLabel.textColor = [[YPYYTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.4];

        if (state.posState == JXIMRoomMicPostionStateUnlock) {
            cell.positionStatusLockBg.hidden = YES;
            cell.positionStatusLockBg.userInteractionEnabled = NO;
            cell.positionStatusNormalBg.hidden = NO;
            cell.positionStatusNormalBg.userInteractionEnabled = YES;
            
            if (member) {
                cell.positionStatusLockBg.userInteractionEnabled = NO;
                cell.positionStatusNormalBg.userInteractionEnabled = NO;
                cell.charmView.hidden = !self.enableCharm;

                cell.avatar.hidden = NO;
//                cell.nicknameLabel.hidden = NO;
                cell.genderImageview.hidden = NO;
                cell.avatar.layer.cornerRadius = cell.avatar.frame.size.width / 2;
                cell.avatar.layer.masksToBounds = YES;
                [cell.avatar qn_setImageImageWithUrl:member.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
                
                [cell.headwearImg sd_setImageWithURL:[NSURL URLWithString:member.headwear_url]];
                
                NSString *charmValue = [[self.charmInfoMap valueForKey:member.account] valueForKey:@"value"];
                BOOL hasHat = [[[self.charmInfoMap valueForKey:member.account] valueForKey:@"withHat"] boolValue];
                cell.charmLabel.text = [NSString numberStringFromNumber:[NSNumber numberWithInteger:charmValue.integerValue] withPrefixString:@""];
                cell.hatImageView.hidden = !hasHat;
                
                if (gender == 1) {
                    //男
                    cell.micOrderLabel.backgroundColor = [UIColor colorWithHexString:@"#0D9EFE"];
//                    [cell.sexBtn setBackgroundImage:[UIImage imageNamed:@"xc_room_icon_man"] forState:UIControlStateNormal];
                    
                    [cell.sexBtn setBackgroundColor:UIColorHex(18aaff)];
                }else if (gender == 2) {
                    //女
                    cell.micOrderLabel.backgroundColor = [UIColor colorWithHexString:@"#FD2772"];
//                    [cell.sexBtn setBackgroundImage:[UIImage imageNamed:@"xc_room_icon_wem"] forState:UIControlStateNormal];
                    [cell.sexBtn setBackgroundColor:UIColorHex(ff5c8d)];
                }
                [cell.sexBtn setTitle:[NSString stringWithFormat:@"%zd",indexPath.row+1] forState:UIControlStateNormal];

                cell.nicknameLabel.text = member.nick;
                cell.nickName_leading_layout.constant = (kScreenWidth>380)?45:40;
                cell.nicknameLabel.textAlignment = NSTextAlignmentLeft;
                cell.micOrderLabel.textColor = [UIColor whiteColor];
            } else {
                cell.charmView.hidden = YES;
                cell.hatImageView.hidden = YES;

                cell.genderImageview.hidden = YES;
                cell.playingImageView.hidden = YES;
                cell.positionStatusNormalBg.hidden = NO;
                cell.positionStatusNormalBg.userInteractionEnabled = YES;
                cell.avatar.hidden = YES;
                [cell.headwearImg sd_setImageWithURL:[NSURL URLWithString:@""]];
//                cell.nicknameLabel.hidden = YES;
                cell.micOrderLabel.backgroundColor = [[YPYYTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.1];
                cell.micOrderLabel.textColor = [[YPYYTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.4];
                [cell.sexBtn setTitle:@"" forState:UIControlStateNormal];
            }
        } else if (state.posState == JXIMRoomMicPostionStateLock) {
            if (member) {
                cell.positionStatusLockBg.userInteractionEnabled = NO;
                cell.positionStatusNormalBg.userInteractionEnabled = NO;
                cell.genderImageview.hidden = false;
                cell.charmView.hidden =  !self.enableCharm;

                if (gender == 1) {
                    //男
                    cell.micOrderLabel.backgroundColor = [UIColor colorWithHexString:@"#0D9EFE"];
//                    [cell.sexBtn setBackgroundImage:[UIImage imageNamed:@"xc_room_icon_man"] forState:UIControlStateNormal];
//                    [cell.sexBtn setTitleColor:UIColorHex(18aaff) forState:UIControlStateNormal];
                    
                    [cell.sexBtn setBackgroundColor:UIColorHex(18aaff)];

                }else if (gender == 2) {
                    //女
                    cell.micOrderLabel.backgroundColor = [UIColor colorWithHexString:@"#FD2772"];
//                    [cell.sexBtn setBackgroundImage:[UIImage imageNamed:@"xc_room_icon_wem"] forState:UIControlStateNormal];
//                    [cell.sexBtn setTitleColor:UIColorHex(ff5c8d) forState:UIControlStateNormal];
                    [cell.sexBtn setBackgroundColor:UIColorHex(ff5c8d)];

                }
                [cell.sexBtn setTitle:[NSString stringWithFormat:@"%zd",indexPath.row+1] forState:UIControlStateNormal];

               
                cell.avatar.hidden = NO;
                cell.avatar.layer.cornerRadius = cell.avatar.frame.size.width / 2;
                cell.avatar.layer.masksToBounds = YES;
                [cell.avatar qn_setImageImageWithUrl:member.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
                [cell.headwearImg sd_setImageWithURL:[NSURL URLWithString:member.headwear_url]];

                cell.nicknameLabel.text = member.nick;
                 cell.nickName_leading_layout.constant = (kScreenWidth>380)?45:40;
                 cell.nicknameLabel.textAlignment = NSTextAlignmentLeft;
                cell.micOrderLabel.textColor = [[YPYYTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:1];
                
                NSString *charmValue = [[self.charmInfoMap valueForKey:member.account] valueForKey:@"value"];
                BOOL hasHat = [[[self.charmInfoMap valueForKey:member.account] valueForKey:@"withHat"] boolValue];
                cell.charmLabel.text = [NSString numberStringFromNumber:[NSNumber numberWithInteger:charmValue.integerValue] withPrefixString:@""];
                cell.hatImageView.hidden = !hasHat;

            } else {
                cell.charmView.hidden = YES;
                cell.hatImageView.hidden = YES;
                cell.playingImageView.hidden = YES;
                cell.avatar.hidden = YES;
                [cell.headwearImg sd_setImageWithURL:[NSURL URLWithString:@""]];
                cell.genderImageview.hidden = YES;
                cell.positionStatusNormalBg.hidden = YES;
                cell.positionStatusNormalBg.userInteractionEnabled = NO;
                cell.positionStatusLockBg.hidden = NO;
                cell.positionStatusLockBg.userInteractionEnabled = YES;
                cell.micOrderLabel.backgroundColor = [[YPYYTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.1];
                cell.micOrderLabel.textColor = [[YPYYTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.4];
                [cell.sexBtn setTitle:@"" forState:UIControlStateNormal];

            }
        }
        
        if (state.micState == JXIMRoomMicPostionStateLock) {
            cell.be_BlockMicro.hidden = NO;
        }else {
            cell.be_BlockMicro.hidden = YES;
        }

        cell.position = [NSString stringWithFormat:@"%ld",(long)indexPath.row];
        cell.navigationController = self.navigationController;
        cell.YPRoomMicInfo = state;
        cell.micOrderLabel.text = [NSString stringWithFormat:@"%d",indexPath.item + 1];
        return cell;
    } else if (collectionView == self.speakingCollectionView) {
        YPGameRoomSpeakingPositionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPGameRoomSpeakingPositionCell" forIndexPath:indexPath];
        UserID myUid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
        if ([self checkUserIsSpeakingWithUserId:[member.account longLongValue]] || (myUid == [member.account longLongValue]  && _isSpeaking)) {
            if (![cell.speakingAnimateImageView isAnimating]) {
//                NSMutableArray  *arrayM=[NSMutableArray array];
//                for (int i=0; i<11; i++) {
//                    [arrayM addObject:[UIImage imageNamed:[NSString stringWithFormat:@"room_speaking_logo%d",i]]];
//                }
//                [cell.speakingAnimateImageView setAnimationImages:arrayM];
//                [cell.speakingAnimateImageView setAnimationRepeatCount:1];
//                [cell.speakingAnimateImageView setAnimationDuration:0.6f];
//                [cell.speakingAnimateImageView startAnimating];
                
//                YPRippleAnimationView *animationView = [[YPRippleAnimationView alloc]initWithFrame:CGRectMake(0, 0, cell.speakingAnimateImageView.width, cell.speakingAnimateImageView.height) gender:member.gender];
//
//                animationView.delegate = self;
//                [cell.speakingAnimateImageView addSubview:animationView];
            }
        } else {
//            [cell.speakingAnimateImageView stopAnimating];
//            [cell.speakingAnimateImageView setImage:nil];
//            [cell.speakingAnimateImageView removeAllSubviews];
        }
        return cell;
    }else {
        YPGameRoomSpeakingPositionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPGameRoomSpeakingPositionCell" forIndexPath:indexPath];
        return cell;
    }
    
    
}

#pragma mark - HJRippleAnimationDelagte
//- (void)rippleAnimationFinishedWithAnimationView:(UIView *)animationView{
//    [self.mainPositionSpeakingAnimationImageView removeAllSubviews];
//    [animationView removeFromSuperview];
//}

#pragma mark - UICollectionViewDelegate

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    
    YPIMQueueItem *item = [self.mircoList safeObjectAtIndex:indexPath.item+1];
    YPRoomMicInfo *state = item.queueInfo.mic_info;
    YPChatRoomMember *member = item.queueInfo.chatRoomMember;
    if (collectionView == self.collectionView) {
        UserID myUid = [GetCore(YPAuthCoreHelp)getUid].userIDValue;
        YPChatRoomMember *myMember = GetCore(YPRoomQueueCoreV2Help).myMember;
        if (state.posState == JXIMRoomMicPostionStateUnlock) {
            if (member != nil) {//麦序上有人
               [self showMenu:myMember withUid:myUid member:member index:indexPath];
            }else { //麦序上没人
                if (myMember.is_creator || myMember.is_manager) {
                    [self showManagerPanelSheet:indexPath];
                }else if (!myMember.is_creator || !myMember.is_manager) {
                    [GetCore(YPRoomQueueCoreV2Help) upMic:(int)indexPath.row];//上指定麦
                }
            }
        } else if (state.posState == JXIMRoomMicPostionStateLock) { //麦序是锁的
            if (member != nil) {//麦序上有人
                [self showMenu:myMember withUid:myUid member:member index:indexPath];
            }else{
                if (myMember.is_creator || myMember.is_manager) {
                    if (myUid == [member.account longLongValue]) { //点击自己
                        [self showSelfPanel:myUid andIndexPath:indexPath];
                    }else { //点击别人
                        [self showManagerPanelSheet:indexPath];
                    }
                }else{
                    [MBProgressHUD showError:@"麦位已经上锁无法上麦"];
                }
            }
        }
    }
}

- (void)showMenu:(YPChatRoomMember *)myMember withUid:(UserID)myUid member:(YPChatRoomMember *)member index:(NSIndexPath *)indexPath{
    if (myMember.is_manager) { //我是管理员
        if (myUid == [member.account longLongValue]) { //点击自己
            [self showSelfPanel:myUid andIndexPath:indexPath];
        }else { //点击别人
            [self showManagerPanelSheetWithUid:[member.account longLongValue] andIndexPath:indexPath];

        }
    }else if (myMember.is_creator) { //我是房主
        if (myUid == [member.account longLongValue]) { //点击自己
            [self showSelfPanel:myUid andIndexPath:indexPath];
        }else { //点击别人
            [self showOwnerPanelSheet:[member.account longLongValue] indexPath:indexPath member:member];

        }
    }else if (!myMember.is_creator || !myMember.is_manager) { //我是普通用户或游客
        //点击自己
        if (myUid == [member.account longLongValue]) {
            [self showSelfPanel:myUid andIndexPath:indexPath];
        }else {
            [self showGiftView:[member.account longLongValue]];
        }
    }
}

#pragma mark - UICollectionViewDelegateFlowLayout

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake((self.frame.size.width-30) / 4, EACHCELLHEIGHT);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(0, 15, 0, 15);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section {
    return 10;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section {
    return 0;
    
}


#pragma mark - RoomUIClient
- (void)roomVCWillDisappear {
    [self.giftAlertView dismissViewControllerAnimated:YES];
}

#pragma mark - ImRoomCoreClientV2
//获取队列
- (void)onGetRoomQueueSuccessV2:(NSMutableArray<YPIMQueueItem *> *)info {
    self.mircoList = info;
    [self cleanLocationCharmData];
}

//清除本地队列中的
- (void)cleanLocationCharmData
{
    for (NSString *key in self.charmInfoMap.allKeys) {
        
        BOOL hasKey = NO;
        
        YPIMQueueItem *hatModel;
        
        for (YPIMQueueItem *item in self.mircoList) {
            YPChatRoomMember *member = item.queueInfo.chatRoomMember;
            if ([key isEqualToString:member.account]) {
                hasKey = YES;
            }
            
        }
        if (!hasKey) {
            [self.charmInfoMap removeObjectForKey:key];
        }
        
    }
    
    [self.collectionView reloadData];
}


#pragma mark - RoomCoreClient
- (void)onManagerAdd:(YPChatRoomMember *)member{
    self.mircoList = GetCore(YPImRoomCoreV2).micQueue;
    [self.collectionView reloadData];
}
- (void)onManagerRemove:(YPChatRoomMember *)member{
    self.mircoList = GetCore(YPImRoomCoreV2).micQueue;
    [self.collectionView reloadData];
}

- (void)userBeAddBlack:(YPChatRoomMember *)member{
    [GetCore(YPImRoomCoreV2) queryQueueWithRoomId:[NSString stringWithFormat:@"%ld",self.roomInfo.roomId]];
}
- (void)userBeRemoveBlack:(NSString *)userId {
    self.mircoList = GetCore(YPImRoomCoreV2).micQueue;
    [self.collectionView reloadData];
}

- (void)bekillSuccess {
    self.mircoList = GetCore(YPImRoomCoreV2).micQueue;
    [self.collectionView reloadData];
    
    //重新获取最新的麦序
    [GetCore(YPImRoomCoreV2) queryQueueWithRoomId:[NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId]];
}

#pragma mark - ReachabilityClient
- (void)reachabilityNetStateDidChange:(ReachabilityNetState)currentNetState {
    
    if (GetCore(YPImRoomCoreV2).currentRoomInfo.roomId != 0) {
        
        [GetCore(YPImRoomCoreV2) queryQueueWithRoomId:[NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId]];
    }
}

#pragma mark - RoomQueueCoreClient
- (void)onMicroQueueUpdate:(NSMutableArray<YPIMQueueItem *> *)micQueue {
    [self updateView];
}

#pragma mark - MeetCoreClient
- (void)onMySpeakStateUpdate:(BOOL)speaking {
//    if (GetCore(RoomQueueCore).isLoad) {
    
    //判断是否为房主
    NSString *uid = GetCore(YPAuthCoreHelp).getUid;
    UserID myUID = [uid userIDValue];
    if (self.roomInfo.uid != myUID) {
        self.isSpeaking = speaking;
        [self reloadMySpeakingView:myUID];
    }
//    }
}

- (void)reloadMySpeakingView:(UserID)myUID{
    
    for (int i = 0; i < 8; i++) {
        
        YPIMQueueItem *item = [self.mircoList safeObjectAtIndex:i+1];
        YPChatRoomMember *member = item.queueInfo.chatRoomMember;
        UIImageView *speakingView = self.speakingImageView[i];
        if ([member.account longLongValue] == myUID) {
            if (![speakingView isAnimating]) {
                if (member.gender== JXIMUserGenderTypeMale) {
                     [speakingView setAnimationImages:self.speakingAnimatePicArr];
                }else{
                     [speakingView setAnimationImages:self.speakingAnimatePicArrForGirl];

                }
               
                [speakingView setAnimationRepeatCount:1];
                [speakingView setAnimationDuration:1.0f];
                [speakingView startAnimating];
                
//                YPRippleAnimationView *animationView = [[YPRippleAnimationView alloc]initWithFrame:CGRectMake(0, 0, speakingView.width, speakingView.height) gender:member.gender];
//
//                animationView.delegate = self;
//                [speakingView addSubview:animationView];
            }
        }
    }
}

- (void)onSpeakUsersReport:(NSArray *)userInfos {
//    if (GetCore(RoomQueueCore).isLoad) {
        [self reloadTheSpeakingView];
//    }
}

- (void)onJoinMeetingSuccess {
    self.mircoList = GetCore(YPImRoomCoreV2).micQueue;
    [self.collectionView reloadData];
}

#pragma mark - FaceCoreClient
- (void)onReceiveFace:(NSMutableArray<YPFaceReceiveInfo *> *)faceRecieveInfos {
//    if (GetCore(RoomQueueCore).isLoad) {
        self.faceArr = faceRecieveInfos;
        
        for (YPFaceReceiveInfo *item in faceRecieveInfos) {
            NSString *position = [GetCore(YPRoomQueueCoreV2Help) findThePositionByUid:item.uid];
            if (position != nil && item.uid != self.roomInfo.uid) {
                FLAnimatedImageView *imageView = [self findTheImageViewByPosition:position];
                [self bringSubviewToFront:imageView];
                YPFaceConfigInfo *faceInfo = [GetCore(YPFaceCore)findFaceInfoById:item.faceId];
                [[YPFaceImageTool shareFaceImageTool]queryImage:item imageView:imageView success:^(YPFaceReceiveInfo *info) {

                } failure:^(NSError *error) {
                    imageView.transform = CGAffineTransformMakeScale(1, 1);
                }];
                if (item.uid == [GetCore(YPAuthCoreHelp) getUid].userIDValue && faceInfo.resultCount > 0) {
                    GetCore(YPFaceCore).isShowingFace = YES;
                }
            }
        }
}

- (YPFaceReceiveInfo *)findIsNeedPlayFaceBy:(UserID)uid {
    for (YPFaceReceiveInfo *item in self.faceArr) {
        if (item.uid == uid) {
            return item;
        }
    }
    return nil;
}

#pragma mark - LongZhuCoreClient
- (void)onReceiveLongZhuWithModel:(YPRoomLongZhuMsgModel *)model isSuPei:(BOOL)isSuPei {
    
    NSString *position = [GetCore(YPRoomQueueCoreV2Help) findThePositionByUid:model.uid.integerValue];
    
    if (position.length && model.uid.integerValue != self.roomInfo.uid && model.numArr.count) {
        
        FLAnimatedImageView *imageView = [self findTheImageViewByPosition:position];
        
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
                
                imageView.image = [GetCore(YPLongZhuCore) composeImgWithImge1:[UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%zd",num1]] img2:[UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%zd",num2]] img3:[UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%zd",num3]]];
            }
            else {
                imageView.image = [GetCore(YPLongZhuCore) composeImgWithImge1:[UIImage imageNamed:@"room_game_longzhu_help"] img2:[UIImage imageNamed:@"room_game_longzhu_help"] img3:[UIImage imageNamed:@"room_game_longzhu_help"]];
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
                                 
                                 if (!GetCore(YPFaceCore).isShowingFace) {
                                     
                                     if (imageView.image == temImage) {
                                         
                                         imageView.image = nil;
                                         imageView.transform = CGAffineTransformMakeScale(1, 1);
                                     }
                                 }
                             });
                         }];
        
    }
    
}

#pragma mark - BalanceErrorClient

- (void)onBalanceNotEnough {
    [self.giftAlertView dismissViewControllerAnimated:YES completion:^{
        [self showBalanceNotEnough];
    }];
}

- (void)showBalanceNotEnough {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPurseNoMoneyTitle, nil) message:NSLocalizedString(XCPurseNoMoneyMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        
        UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
        [[YPRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];

    [[YPRoomViewControllerCenter defaultCenter].current presentViewController:alert animated:YES completion:^{
        
    }];
}

#pragma mark - Alert
//点用户
- (void)showPersonPanelSheet:(UserID)uid {
    UserID myUid = [GetCore(YPAuthCoreHelp)getUid].userIDValue;
    
    
    
    __weak typeof(self)weakSelf = self;
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"送礼物" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        if (myUid != uid) {
            [weakSelf showGiftView:uid];
        }
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"查看资料" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf showPersonalPage:uid];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }]];
    [[self topViewController] presentViewController:alter animated:YES completion:nil];

}

//点自己
- (void)showSelfPanel:(UserID)uid andIndexPath:(NSIndexPath *)indexPath {
  
    
    __weak typeof(self)weakSelf = self;
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"查看资料" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf showPersonalPage:uid];

    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"下麦旁听" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPRoomQueueCoreV2Help) downMic];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }]];
    
    
    YPChatRoomMember *myMember = GetCore(YPRoomQueueCoreV2Help).myMember;
    YPIMQueueItem *item = [self.mircoList safeObjectAtIndex:indexPath.item+1];
    YPRoomMicInfo *state = item.queueInfo.mic_info;
    if (myMember.is_creator || myMember.is_manager) {
        if (state.micState == JXIMRoomMicPostionStateLock) {
            [alter addAction:[UIAlertAction actionWithTitle:@"解除此座位禁麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [GetCore(YPRoomQueueCoreV2Help) openMic:(int)indexPath.row];
            }]];
        }
    }
    
    [[self topViewController] presentViewController:alter animated:YES completion:nil];
}

- (void)showManagerPanelSheetWithUid:(UserID)uid andIndexPath:(NSIndexPath *)indexPath {
    if (uid > 0) {
        @weakify(self);
        
        YPChatRoomMember *member = [GetCore(YPRoomQueueCoreV2Help) findTheMemberByUserId:uid];
        YPIMQueueItem *item = [self.mircoList safeObjectAtIndex:indexPath.item+1];
        YPRoomMicInfo *state = item.queueInfo.mic_info;
        
        __weak typeof(self)weakSelf = self;
        
        UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
        
        [alter addAction:[UIAlertAction actionWithTitle:@"送礼物" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [weakSelf showGiftView:[member.account longLongValue]];
        }]];
        
        [alter addAction:[UIAlertAction actionWithTitle:@"查看资料" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [weakSelf showPersonalPage:uid];
        }]];
        
        [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            
        }]];
        
        
        
//        NSMutableArray *items = [NSMutableArray array];
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomSendGift, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [self showGiftView:[member.account longLongValue]];
//        })];
        
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomCheckInfo, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [self showPersonalPage:uid];
//        })];
        if (!member.is_creator) {
            
            [alter addAction:[UIAlertAction actionWithTitle:@"抱Ta下麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [GetCore(YPRoomQueueCoreV2Help) kickDownMic:uid position:indexPath.row];
            }]];
//            XCRoomKickHimDownMic
//            [items addObject:MMItemMake(NSLocalizedString(XCRoomKickHimDownMic, nil), MMItemTypeNormal, ^(NSInteger index) {
//                [GetCore(YPRoomQueueCoreV2Help) kickDownMic:uid position:indexPath.row];
//            })];
        }
//        if (!member.is_manager && !member.is_creator) {
//            [items addObject:MMItemMake(@"抱TA下麦", MMItemTypeNormal, ^(NSInteger index) {
//                [GetCore(RoomQueueCoreV2) kickDownMic:uid position:indexPath.row];
//            })];
//        }
        
        if (state.micState == JXIMRoomMicPostionStateLock) {
            [alter addAction:[UIAlertAction actionWithTitle:@"解除此座位禁麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [GetCore(YPRoomQueueCoreV2Help) openMic:(int)indexPath.row];
            }]];
            
//            [items addObject:MMItemMake(NSLocalizedString(XCRoomSetThatMicBeUnQuie, nil), MMItemTypeNormal, ^(NSInteger index) {
//                [GetCore(YPRoomQueueCoreV2Help) openMic:(int)indexPath.row];
//            })];
        } else {
            if (!member.is_creator) {
                
                [alter addAction:[UIAlertAction actionWithTitle:@"禁麦此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                    [GetCore(YPRoomQueueCoreV2Help) closeMic:(int)indexPath.row];
                }]];
                
//                [items addObject:MMItemMake(NSLocalizedString(XCRoomSetThatMicBeQuiet, nil), MMItemTypeNormal, ^(NSInteger index) {
//                    [GetCore(YPRoomQueueCoreV2Help) closeMic:(int)indexPath.row];
//                })];
            }
//            if (!member.is_manager && !member.is_creator) {
//                [items addObject:MMItemMake(@"禁麦此座位", MMItemTypeNormal, ^(NSInteger index) {
//                    [GetCore(RoomQueueCoreV2) closeMic:(int)indexPath.row];
//                })];
//            }
        }
        
        if (state.posState == JXIMRoomMicPostionStateUnlock) {
            
//            [alter addAction:[UIAlertAction actionWithTitle:@"封锁此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//                [GetCore(YPRoomQueueCoreV2Help) removeChatroomQueueWithPosition:[NSString stringWithFormat:@"%ld",(long)indexPath.row] success:^(BOOL success) {
//                    [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
//                } failure:^(NSString *message) {
//                    [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
//                }];
//
//            }]];
            
//            [items addObject:MMItemMake(NSLocalizedString(XCRoomLockThatSeat, nil), MMItemTypeNormal, ^(NSInteger index) {
//                @strongify(self);
//                [GetCore(YPRoomQueueCoreV2Help) removeChatroomQueueWithPosition:[NSString stringWithFormat:@"%ld",(long)indexPath.row] success:^(BOOL success) {
//                    [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
//                } failure:^(NSString *message) {
//                    [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
//                }];
//            })];
            
        } else {
            
//            [alter addAction:[UIAlertAction actionWithTitle:@"解封此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//                [GetCore(YPRoomQueueCoreV2Help) freeMicPlace:(int)indexPath.row];
//
//
//            }]];
            
//            [items addObject:MMItemMake(NSLocalizedString(XCRoomUnLockThatSeat, nil), MMItemTypeNormal, ^(NSInteger index) {
//                @strongify(self);
//                [GetCore(YPRoomQueueCoreV2Help) freeMicPlace:(int)indexPath.row];
//            })];
            
        }
        
        if (!member.is_manager && !member.is_creator) {
            
            [alter addAction:[UIAlertAction actionWithTitle:@"踢出房间" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [YPMidSure kickUser:uid didKickFinish:nil];
                
            }]];
            
//            [items addObject:MMItemMake(NSLocalizedString(XCRoomKickOutTheRoom, nil), MMItemTypeNormal, ^(NSInteger index) {
//                [YPMidSure kickUser:uid didKickFinish:nil];
//            })];
            
            [alter addAction:[UIAlertAction actionWithTitle:@"加入房间黑名单" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [weakSelf showAlertWithAddBlackList:member];

            }]];

//            [items addObject:MMItemMake(NSLocalizedString(XCPersonalInfoInBlackList, nil), MMItemTypeNormal, ^(NSInteger index) {
//                @strongify(self);
//                [self showAlertWithAddBlackList:member];
//            })];

        }
        
//        MMSheetView *alert = [[MMSheetView alloc]initWithTitle:nil items:items];
//        alert.type = MMPopupTypeSheet;
//        [MMPopupWindow sharedWindow].touchWildToHide = YES;
//        alert.animationDuration = 0.1;
//        [alert show];
        
        [[self topViewController] presentViewController:alter animated:YES completion:nil];


    }
}

- (void)showManagerPanelSheet:(NSIndexPath *)indexPath{
    @weakify(self);
    YPIMQueueItem *item = [self.mircoList safeObjectAtIndex:indexPath.item+1];
    YPRoomMicInfo *state = item.queueInfo.mic_info;
    NSMutableArray *items = [NSMutableArray array];
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"抱TA上麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//        YPOnlineListVC *vc =(YPOnlineListVC *)[[YPRoomViewControllerFactory sharedFactory]instantiateOnlineListViewController];
//        vc.pos = (int)indexPath.row;
//        [[YPRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];
        
        YPRoomOnlineListVC *vc = YPRoomStoryBoard(@"YPRoomOnlineListVC");
        vc.pos = (int)indexPath.row;
        [[YPRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];

        
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }]];
    
    YPChatRoomMember *myMember = GetCore(YPRoomQueueCoreV2Help).myMember;
    if (!myMember.is_creator) {
        
        [alter addAction:[UIAlertAction actionWithTitle:@"移动到此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            if(state.posState == JXIMRoomMicPostionStateUnlock){
                [GetCore(YPRoomQueueCoreV2Help) upMic:(int)indexPath.row];
            }else{
                if (myMember.is_manager) {
                   [MBProgressHUD showError:@"此座位已经被封锁，请先解锁此座位"];
                }
            }
            
            
        }]];
        //        [items addObject:MMItemMake(NSLocalizedString(XCRoomMoveToThatSeat, nil), MMItemTypeNormal, ^(NSInteger index) {
        //            @strongify(self);
        //            [GetCore(YPRoomQueueCoreV2Help) upMic:(int)indexPath.row];
        //        })];
        }
    
    
//    [items addObject:MMItemMake(@"抱TA上麦", MMItemTypeNormal, ^(NSInteger index) {
//        @strongify(self);
//        YPOnlineListVC *vc =(YPOnlineListVC *)[[YPRoomViewControllerFactory sharedFactory]instantiateOnlineListViewController];
//        vc.pos = (int)indexPath.row;
//        [[YPRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];
//    })];
    
    if (state.micState == JXIMRoomMicStateOpen) {
        
        [alter addAction:[UIAlertAction actionWithTitle:@"禁麦此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [GetCore(YPRoomQueueCoreV2Help) closeMic:(int)indexPath.row];
        }]];
        
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomSetThatMicBeQuiet, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [GetCore(YPRoomQueueCoreV2Help) closeMic:(int)indexPath.row];
//        })];

    }else {
        
        [alter addAction:[UIAlertAction actionWithTitle:@"解除此座位禁麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [GetCore(YPRoomQueueCoreV2Help)openMic:(int)indexPath.row];
        }]];
        
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomSetThatMicBeUnQuie, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [GetCore(YPRoomQueueCoreV2Help)openMic:(int)indexPath.row];
//        })];

    }
    if (state.posState == JXIMRoomMicPostionStateUnlock) {
        
        
        [alter addAction:[UIAlertAction actionWithTitle:@"封锁此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [GetCore(YPRoomQueueCoreV2Help) removeChatroomQueueWithPosition:[NSString stringWithFormat:@"%ld",(long)indexPath.row] success:^(BOOL success) {
                [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
            } failure:^(NSString *message) {
                [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
            }];
            
        }]];
        
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomLockThatSeat, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [GetCore(YPRoomQueueCoreV2Help) removeChatroomQueueWithPosition:[NSString stringWithFormat:@"%ld",(long)indexPath.row] success:^(BOOL success) {
//                [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
//            } failure:^(NSString *message) {
//                [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
//            }];
//        })];

    }else {
        
        [alter addAction:[UIAlertAction actionWithTitle:@"解封此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [GetCore(YPRoomQueueCoreV2Help) freeMicPlace:(int)indexPath.row];

            
        }]];
//
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomUnLockThatSeat, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [GetCore(YPRoomQueueCoreV2Help) freeMicPlace:(int)indexPath.row];
//        })];

    }
    

//    MMSheetView *alert = [[MMSheetView alloc]initWithTitle:nil items:items];
//    alert.type = MMPopupTypeSheet;
//    [MMPopupWindow sharedWindow].touchWildToHide = YES;
//    alert.animationDuration = 0.1;
//    [alert show];
    
    [[self topViewController] presentViewController:alter animated:YES completion:nil];

    
}

- (void)showOwnerPanelSheet:(UserID)uid indexPath:(NSIndexPath *)indexPath member:(YPChatRoomMember *)member {
    YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;;
    YPIMQueueItem *item = [self.mircoList safeObjectAtIndex:indexPath.item+1];
    YPRoomMicInfo *state = item.queueInfo.mic_info;
    @weakify(self);
    
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"送礼物" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        [self showGiftView:uid];
        
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }]];
    
//    NSMutableArray *items = [NSMutableArray array];
//    [items addObject:MMItemMake(NSLocalizedString(XCRoomSendGift, nil), MMItemTypeNormal, ^(NSInteger index) {
//        @strongify(self);
//        [self showGiftView:uid];
//    })];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"查看资料" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        [self showPersonalPage:uid];
        
    }]];
    
    
    [alter addAction:[UIAlertAction actionWithTitle:@"抱Ta下麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPRoomQueueCoreV2Help) kickDownMic:uid position:(int)indexPath.row];
    }]];
    

    if (state.micState == JXIMRoomMicPostionStateLock) {
        
        [alter addAction:[UIAlertAction actionWithTitle:@"解除此座位禁麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            @strongify(self);
            [GetCore(YPRoomQueueCoreV2Help) openMic:(int)indexPath.row];
            
        }]];
        
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomSetThatMicBeUnQuie, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [GetCore(YPRoomQueueCoreV2Help) openMic:(int)indexPath.row];
//        })];
    } else {
        
        [alter addAction:[UIAlertAction actionWithTitle:@"禁麦此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            @strongify(self);
            [GetCore(YPRoomQueueCoreV2Help) closeMic:(int)indexPath.row];

        }]];
        
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomSetThatMicBeQuiet, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [GetCore(YPRoomQueueCoreV2Help) closeMic:(int)indexPath.row];
//        })];
    }
    
    if (state.posState == JXIMRoomMicPostionStateUnlock) {
        
//        [alter addAction:[UIAlertAction actionWithTitle:@"封锁此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//            @strongify(self);
//            [GetCore(YPRoomQueueCoreV2Help) removeChatroomQueueWithPosition:[NSString stringWithFormat:@"%ld",(long)indexPath.row] success:^(BOOL success) {
//                [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
//            } failure:^(NSString *message) {
//                [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
//            }];
//        }]];
        
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomLockThatSeat, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [GetCore(YPRoomQueueCoreV2Help) removeChatroomQueueWithPosition:[NSString stringWithFormat:@"%ld",(long)indexPath.row] success:^(BOOL success) {
//                [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
//            } failure:^(NSString *message) {
//                [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:indexPath.row];
//            }];
//        })];
        
    } else {
        
        
//        [alter addAction:[UIAlertAction actionWithTitle:@"解封此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//            @strongify(self);
//            [GetCore(YPRoomQueueCoreV2Help) freeMicPlace:(int)indexPath.row];
//
//        }]];
        
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomUnLockThatSeat, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [GetCore(YPRoomQueueCoreV2Help) freeMicPlace:(int)indexPath.row];
//        })];
        
    }
    
    if (!(myMember.is_manager && member.is_manager)) {
        
        [alter addAction:[UIAlertAction actionWithTitle:@"踢出房间" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [YPMidSure kickUser:uid didKickFinish:nil];
        }]];
        
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomKickOutTheRoom, nil), MMItemTypeNormal, ^(NSInteger index) {
//            [YPMidSure kickUser:uid didKickFinish:nil];
//        })];
    }
    
    
    if (member.is_manager) {
        @weakify(member)
        [alter addAction:[UIAlertAction actionWithTitle:@"移除管理员" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            @strongify(member)
            [GetCore(YPImRoomCoreV2) markManagerList:uid enable:NO];
            member.is_manager = NO;
            
        }]];
        
//        @weakify(member)
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomMoveOutManager, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(member)
//            [GetCore(YPImRoomCoreV2) markManagerList:uid enable:NO];
//            member.is_manager = NO;
//        })];
        
    }else if (!member.is_manager && !member.is_creator) {
        
        @weakify(member)
        [alter addAction:[UIAlertAction actionWithTitle:@"设置管理员" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            @strongify(member)
            [GetCore(YPImRoomCoreV2) markManagerList:uid enable:YES];
            member.is_manager = YES;
            
        }]];
        
//        @weakify(member)
//        [items addObject:MMItemMake(NSLocalizedString(XCRoomSetToBeManager, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(member)
//            [GetCore(YPImRoomCoreV2) markManagerList:uid enable:YES];
//            member.is_manager = YES;
//        })];
    }
    if (!(myMember.is_manager && member.is_manager)) {
        
        [alter addAction:[UIAlertAction actionWithTitle:@"加入房间黑名单" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            @strongify(self);
            [self showAlertWithAddBlackList:member];
            
        }]];
        
//        [items addObject:MMItemMake(NSLocalizedString(XCPersonalInfoInBlackList, nil), MMItemTypeNormal, ^(NSInteger index) {
//            @strongify(self);
//            [self showAlertWithAddBlackList:member];
//        })];
    }
//
//    MMSheetView *alert = [[MMSheetView alloc]initWithTitle:nil items:items];
//    alert.type = MMPopupTypeSheet;
//    [MMPopupWindow sharedWindow].touchWildToHide = YES;
//    alert.animationDuration = 0.1;
//    [alert show];

    [[self topViewController] presentViewController:alter animated:YES completion:nil];

}

- (void)showPersonalPage:(UserID)uid {
    if ([_delegate respondsToSelector:@selector(positionViewShowUserInfoCardWithUid:)]) {
        [_delegate positionViewShowUserInfoCardWithUid:uid];
    }

}

- (void)showAlertWithAddBlackList:(YPChatRoomMember *)member {
    NSString *title = [NSString stringWithFormat:@"%@%@",NSLocalizedString(XCRoomMoveToBlackTitle, nil),member.nick];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:NSLocalizedString(XCRoomMoveToBlackMsg, nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(MMPopViewConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPImRoomCoreV2) markBlackList:[member.account longLongValue] enable:YES];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:enter];
    [alert addAction:cancel];
    [[YPRoomViewControllerCenter defaultCenter].current presentViewController:alert animated:YES completion:nil];
}

- (void)showGiftView:(UserID)uid {
    
    
    
    [YPGiftBoxView showAllMic:uid];
    
    
}

#pragma mark - private method
- (void)addCore{
    AddCoreClient(RoomUIClient, self);
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJRoomQueueCoreClient, self);
    AddCoreClient(HJImRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    AddCoreClient(HJMeetingCoreClient, self);
    AddCoreClient(HJFaceCoreClient, self);
    AddCoreClient(HJBalanceErrorClient, self);
    AddCoreClient(HJLongZhuCoreClient, self);
    AddCoreClient(ReachabilityClient, self);
}

-(void)updateView {
    self.mircoList  = GetCore(YPImRoomCoreV2).micQueue;
    self.collectionView.userInteractionEnabled = YES;
    YPChatRoomInfo *roomInfo = GetCore(YPImRoomCoreV2).currentRoomInfo;
      if (roomInfo.charmOpen ==0) {//用于控制魅力值开关[ 0.隐藏; 1.显示 ]
          self.enableCharm = NO;
      }else{
          self.enableCharm = YES;
      }
    [self.collectionView reloadData];
}


- (void)saveThePositionInCore {
    NSMutableArray *positionArr = [NSMutableArray array];
    CGFloat x = 0;
    CGFloat y = 0;
    for (int i = 0; i < ALLCELLNUM; i++) {
        
        if (i < 4) { //第一行
            x = 15+(i + 1) * EACHCELLWIDTH - EACHCELLWIDTH / 2;
            y = (STATUSVIEWHEIGHT+KMargin)/2;
        }else if (i >= 4 && i < 8) {
            x = 15+(i - EACHLINECELNUM + 1) * EACHCELLWIDTH - EACHCELLWIDTH / 2;
            y = EACHCELLHEIGHT + 10 + (STATUSVIEWHEIGHT+KMargin)/2;
        }
        CGPoint center = CGPointMake(x, y);
        [positionArr addObject:[NSValue valueWithCGPoint:center]];
    }
    [GetCore(YPRoomCoreV2Help) savePosition:positionArr];
    
}
//GIF表情
- (void)initFaceView {
  
    for (int i = 0; i < 8; i++) {
        NSMutableArray *arr = GetCore(YPRoomCoreV2Help).positionArr;
        NSValue *pointValue = [arr safeObjectAtIndex:i];
        CGPoint point = [pointValue CGPointValue];
        FLAnimatedImageView *faceImage = [[FLAnimatedImageView alloc]initWithFrame:CGRectMake(0 , 0 , 90, 130)];
        faceImage.contentMode = UIViewContentModeScaleAspectFit;
        faceImage.center = point;
        [_faceImageViewArr addObject:faceImage];
        [self addSubview:faceImage];
        [self bringSubviewToFront:faceImage];
    }
}
//speak光圈
- (void)initSpeakingView {
    for (int i = 0; i < 8; i++) {
        NSMutableArray *arr = GetCore(YPRoomCoreV2Help).positionArr;
        NSValue *pointValue = [arr safeObjectAtIndex:i];
        CGPoint point = [pointValue CGPointValue];
        
        UIImageView *speakingImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0 , 70, 70)];
        speakingImage.center = CGPointMake(point.x, point.y-2);
        [_speakingImageView addObject:speakingImage];
        [self addSubview:speakingImage];
        [self sendSubviewToBack:speakingImage];
    }
}

- (void)reloadTheSpeakingView {
    for (int i = 0; i < 8; i++) {
        YPIMQueueItem *item = [self.mircoList safeObjectAtIndex:i+1];
        YPChatRoomMember *member = item.queueInfo.chatRoomMember;
        UIImageView *speakingView = self.speakingImageView[i];
        if ([self checkUserIsSpeakingWithUserId:[member.account longLongValue]]) {
            if (![speakingView isAnimating]) {
                if (member.gender== JXIMUserGenderTypeMale) {
                     [speakingView setAnimationImages:self.speakingAnimatePicArr];
                }else{
                     [speakingView setAnimationImages:self.speakingAnimatePicArrForGirl];

                }
                [speakingView setAnimationRepeatCount:1];
                [speakingView setAnimationDuration:1.0f];
                [speakingView startAnimating];
                
//                YPRippleAnimationView *animationView = [[YPRippleAnimationView alloc]initWithFrame:CGRectMake(0, 0, speakingView.width, speakingView.height) gender:member.gender];
//
//                animationView.delegate = self;
//                [speakingView addSubview:animationView];
                
            }
        } else {
            [speakingView stopAnimating];
            [speakingView setAnimationImages:nil];
            [speakingView removeAllSubviews];
        }
    }
}

- (BOOL)checkUserIsSpeakingWithUserId:(UserID)userId
{
    NSArray *speakerList = GetCore(YPRoomCoreV2Help).speakingList;
    for (NSNumber *speaker in speakerList) {
        if (speaker.userIDValue == userId) {
            return YES;
        }
    }
    return NO;
}

- (CGPoint)findThePositionCenterByPosition:(NSString *)position {
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:[position integerValue] inSection:0];
    YPGameRoomPositionCell *cell = (YPGameRoomPositionCell *)[self.collectionView cellForItemAtIndexPath:indexPath];
    if (cell != nil) {
        return cell.center;
    }else {
        return CGPointMake(0, 0);
    }
}


- (FLAnimatedImageView *)findTheImageViewByPosition:(NSString *)position {
    return [self.faceImageViewArr safeObjectAtIndex:[position integerValue]];
}


#pragma mark - Getter
- (NSMutableArray<FLAnimatedImageView *> *)faceImageViewArr {
    if (_faceImageViewArr == nil || _faceImageViewArr.count == 0) {
        _faceImageViewArr = [NSMutableArray array];
        [self initFaceView];
    }
    return _faceImageViewArr;
}

- (NSMutableArray<UIImageView *> *)speakingImageView {
    if (_speakingImageView == nil  || _speakingImageView.count == 0) {
        _speakingImageView = [NSMutableArray array];
        [self initSpeakingView];
    }
    return _speakingImageView;
}

- (NSMutableDictionary<NSString *,id> *)charmInfoMap{
    if (!_charmInfoMap) {
        _charmInfoMap = [NSMutableDictionary dictionary];
    }
    return _charmInfoMap;
}

- (NSMutableArray *)speakingAnimatePicArr {
    if (_speakingAnimatePicArr == nil && _speakingAnimatePicArr.count == 0) {
        _speakingAnimatePicArr = [NSMutableArray array];
        for (int i=0; i<30; i++) {
            [_speakingAnimatePicArr addObject:[UIImage imageNamed:[NSString stringWithFormat:@"xc_room_speak_1_0000%d",i]]];
//            [_speakingAnimatePicArr addObject:[UIImage imageNamed:[NSString stringWithFormat:@"yp_room_speak_1_0000%d",i]]];
        }
    }
    return _speakingAnimatePicArr;
}

- (NSMutableArray *)speakingAnimatePicArrForGirl {
    if (_speakingAnimatePicArrForGirl == nil && _speakingAnimatePicArrForGirl.count == 0) {
        _speakingAnimatePicArrForGirl = [NSMutableArray array];
        for (int i=0; i<30; i++) {
           
            [_speakingAnimatePicArrForGirl addObject:[UIImage imageNamed:[NSString stringWithFormat:@"xc_room_speak_1_0000%d",i]]];//@"yp_room_speak_1_0000%d"
        }
    }
    return _speakingAnimatePicArrForGirl;
}


@end
