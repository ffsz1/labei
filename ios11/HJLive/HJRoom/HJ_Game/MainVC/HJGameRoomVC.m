//
//  HJGameRoomVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC.h"
#import "HJGameRoomVC+Alert.h"
#import "HJGameRoomVC+Intput.h"
#import "HJGameRoomVC+Delegate.h"
#import "HJGameRoomVC+Constraint.h"
#import "HJGameRoomVC+CoreClient.h"
#import "HJGameRoomVC+ToolBar.h"
#import "HJGameRoomVC+Gesture.h"
#import "HJRoomViewControllerCenter.h"
#import "HJRoomSettingVC.h"
#import "HJRoomViewControllerFactory.h"
#import "HJPurseViewControllerFactory.h"

#import "NSString+Utils.h"
//Category
#import "UIImage+ImageEffects.h"
#import "NSObject+YYModel.h"
#import "UIImageView+YYWebImage.h"
#import "UIView+XCToast.h"
#import "UIViewController+Cloudox.h"
#import "UINavigationController+Cloudox.h"
#import <FLAnimatedImageView.h>

#import "HJRoomQueueCoreClient.h"
#import "HJPraiseCoreClient.h"
#import "HJGameRoomFaceView.h"
#import "HJFaceCoreClient.h"

#import "HJFaceCore.h"
#import "RoomUIClient.h"


#import "HJRedPacketActivityView.h"
#import "HJAlertControllerCenter.h"
#import <IQKeyboardManager.h>
#import <IQUIView+IQKeyboardToolbar.h>
#import "HJToolBar.h"
#import "HJVersionCoreHelp.h"

#import "DESEncrypt.h"

#import <SVGAParser.h>
#import <SVGAImageView.h>
#import "HJHttpRequestHelper+Room.h"
#import "HJHttpRequestHelper+Home.h"
#import "HJMusicViewController.h"
#import "HJMusicCore.h"
#import "HJGiftCore.h"

#import "YYViewControllerCenter.h"
#import "HJRoomSettingTopicVC.h"
#import "HJOnlineCoreClient.h"
#import "HJVersionCoreHelp.h"
#import "HJImMessageCore.h"

#import "HJGameRoomVC+LongZhu.h"
#import "HJLongZhuCoreClient.h"
#import "HJRoomBounsListInfo.h"

#import "HJHttpRequestHelper+Praise.h"

#import "HJImRoomCoreV2.h"
#import "HJGameRoomVC+PK.h"

#import "HJRoomShenHaoView.h"
#import "HJRoomPlayerView.h"

#import "HJGiftBoxView.h"
#import "HJCallView.h"
#import "HJDaCallModel.h"
#import "PurseCore.h"
#import "NSTimer+JXBase.h"
#import "HJRoomQueueCustomAttachment.h"
#import "HJWKWebViewController.h"

@interface HJGameRoomVC ()
<
UITextFieldDelegate,
UINavigationBarDelegate,
UINavigationControllerDelegate,
HJGiftViewContainerDelegate,
HJRoomCoreClient,
HJBalanceErrorClient,
HJGiftCoreClient,
RoomUIClient,
HJMeetingCoreClient,
HJImRoomCoreClient,
HJImRoomCoreClientV2,
HJRoomQueueCoreClient,
HJImMessageCoreClient,
HJPraiseCoreClient,
HJActivityCoreClient,
SVGAPlayerDelegate,
HJLongZhuCoreClient,
SDCycleScrollViewDelegate
>


@property (assign, nonatomic) BOOL isRoomOwnerUpmicSuccess;

@property (strong, nonatomic) SVGAParser *parser;
@property (strong, nonatomic) SVGAImageView *svgaDisplayView;

@property (nonatomic, assign) BOOL isFollowing;

@property (nonatomic, assign) BOOL hasCheckFollow;
@property (weak, nonatomic) IBOutlet UIView *topBgView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *topViewWidth;
@property (weak, nonatomic) IBOutlet UIImageView *shenhaoImageView;

@property (weak, nonatomic) IBOutlet HJMarqueeLabel *roomerNameLabel;

@property (strong,nonatomic) HJRoomBounsListInfo *shenHaoTopMemeber;

@property (strong, nonatomic) NSMutableDictionary <NSString *,id> *charmInfoMap;//魅力值map

@property (weak, nonatomic) IBOutlet UIImageView *charmFangzhuImageView;//房主魅力值图片

@property (weak, nonatomic) IBOutlet UILabel *charmFangzhuLabel;//房主魅力值数字
@property (weak, nonatomic) IBOutlet UIImageView *hatwearImageview;//房主魅力值帽子
@property (nonatomic ,assign) BOOL enableCharm;//开启魅力值功能

@end

@implementation HJGameRoomVC

#pragma mark - Getter & Setter
- (SVGAParser *)parser {
    if (_parser == nil) {
        _parser = [[SVGAParser alloc]init];
    }
    return _parser;
}

- (SVGAImageView *)svgaDisplayView {
    if (!_svgaDisplayView) {
        _svgaDisplayView = [SVGAImageView new];
        _svgaDisplayView.userInteractionEnabled = false;
    }
    return _svgaDisplayView;
}

- (IBAction)popClick:(UIButton *)sender {
    [[HJRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:NO];
}
- (UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent; //返回白色
//    return UIStatusBarStyleDefault;    //返回黑色
}
#pragma mark - life cycle

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
//    [GetCore(HJImRoomCoreV2) checkRoomerIsInRoom];
    
    [IQKeyboardManager sharedManager].enable = NO;
    self.navBarBgAlpha = @"0.0";
//    [self.navigationController setNavigationBarHidden:YES animated:YES];
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
    
    //????
//    [[GetCore(HJUserCoreHelp) getUserInfoByUidV2:[[GetCore(HJAuthCoreHelp) getUid] intValue]] subscribeNext:^(id x) {}];
    
    [GetCore(HJMusicCore) playBackgroundMusicList];
    [self configEggSysBtn];
    

}


- (void)configEggSysBtn {
    

        //改为用giftDrawEnable判断
        self.eggSysBtn.hidden = self.roomInfo.giftDrawEnable==1 ? NO : YES;
        
//        self.eggSysBtn.hidden = false;
        
//        self.eggSysBtn.hidden = !self.lottery_box_option;
        if (!self.eggSysBtn.hidden) {
            [self.view bringSubviewToFront:self.eggSysBtn];
        }
//    self.eggSysBtn.hidden = NO;
}


- (void)checkRoomerOnline:(BOOL)online {
    self.userLeaveImg.hidden = online;
}



- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self updateToolBar];
    
    if (GetCore(HJImRoomCoreV2).currentRoomInfo.roomId > 0) {
    }
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [IQKeyboardManager sharedManager].enable = YES;
    [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
    NotifyCoreClient(RoomUIClient, @selector(roomVCWillDisappear), roomVCWillDisappear);
    self.navBarBgAlpha = @"1.0";
}

- (void)viewDidLoad {
    [super viewDidLoad];
//    [NSTimer jx_timerWithTimeInterval:5 block:^(NSTimer * _Nonnull timer) {
//            [GetCore(PurseCore) requestBalanceInfo:GetCore(HJAuthCoreHelp).getUid.userIDValue];
//      } repeats:YES];
       self.banaCommonArray = [[NSArray alloc] init];
     self.eggSysBtn.hidden = YES;
    self.charmInfoMap = [GetCore(HJRoomCoreV2Help) getRoomCharmInfoMap];
    NSString* uid = [NSString stringWithFormat:@"%lld",GetCore(HJImRoomCoreV2).currentRoomInfo.uid];
    NSString *charmValue = [[self.charmInfoMap valueForKey:uid] valueForKey:@"value"];
    self.charmFangzhuLabel.text = (charmValue == nil) ?@"0":[NSString numberStringFromNumber:[NSNumber numberWithInteger:charmValue.integerValue] withPrefixString:@""];
//    self.charmFangzhuLabel.hidden = YES;
//    NSLog(@"-----%@",[NSString numberStringFromNumber:[NSNumber numberWithInteger:121212121] withPrefixString:@""]);
    NSUserDefaults *ur = [NSUserDefaults standardUserDefaults];
    if ([ur objectForKey:@"micInListOption"]) {
        NSString *tag = [ur objectForKey:@"micInListOption"];
        self.micInListOption = [tag boolValue];
    }
    
    if ([ur objectForKey:@"lottery_box_option"]) {
        NSString *tag = [ur objectForKey:@"lottery_box_option"];
        self.lottery_box_option = [tag boolValue];
    }
    
    if ([ur objectForKey:@"lotteryBoxBigGift"]) {
        NSString *lotteryBoxBigGift = [ur objectForKey:@"lotteryBoxBigGift"];
        self.lotteryBoxBigGift = lotteryBoxBigGift;
    }
    
    self.navigationController.delegate = self;
    [self addCore];
    [self addNotificationCenter];
    [self initView];
    [self initGesture];
    [self updateView];
    [self configPlayImageView];
    
    
    [self.view addSubview:self.svgaDisplayView];
    [self.svgaDisplayView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.bottom.top.equalTo(self.view);
    }];
    
    self.svgaDisplayView.delegate = self;
    self.svgaDisplayView.alpha = 0;
    
    [self.waitMicroUsersButton addTarget:self action:@selector(showAlertMicroQueue) forControlEvents:UIControlEventTouchUpInside];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(dismissChannelViewNotification:) name:@"dismissChannelViewNotification" object:nil];
    
//    [self checkQQWX];
    
    [self setBgRaduis];
     [GetCore(HJGiftCore) requestGiftList];
    
    self.messageType = HJMessageTypeAll;

}

- (void)setBgRaduis
{
    //背景图左上、右上圆角
    CGRect frame = CGRectMake(0, 0, 250, 45);
    if (self.roomOwner.uid == [GetCore(HJAuthCoreHelp) getUid].userIDValue) {
        self.topViewWidth.constant = 190+20;
        frame = CGRectMake(0, 0, 190+20, 45);
    }
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerBottomRight cornerRadii:CGSizeMake(22, 22)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.topBgView.layer.mask = maskLayer;
    
    
}

//坚持微信、qq安装情况，隐藏分享按钮
- (void)checkQQWX
{
    BOOL hasWx = [WXApi isWXAppInstalled];
    BOOL hasQQ = ([TencentOAuth iphoneQQInstalled] || [TencentOAuth iphoneTIMInstalled]);
    
    self.shareButton.hidden = (!hasWx && !hasQQ);
}

//更新mic、扬声器状态
- (void)checkDeviceStatus
{
    [self updateMicAndSpeakerStauts];
}

// 最小化/退出通知
- (void)dismissChannelViewNotification:(NSNotification *)notification {
    
    BOOL isQuit = [notification.object[@"isQuit"] boolValue];
    
    if (isQuit) {
        GetCore(HJGiftCore).currentGiftMsgArr = [NSMutableArray array];
        GetCore(HJGiftCore).lastGiftIsAll = NO;
        GetCore(HJGiftCore).lastGiftInfo = nil;
        
    }
    
    self.hasCheckFollow = NO;
    self.hasCheckPK = NO;
}

- (CGSize)getGiftTextSizeWithFont:(UIFont *)font text:(NSString *)text {
    return [text boundingRectWithSize:CGSizeMake(CGFLOAT_MAX, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : font} context:nil].size;
}

- (void)svgaPlayerDidFinishedAnimation:(SVGAPlayer *)player {
    [UIView animateWithDuration:0.5 animations:^{
        self.svgaDisplayView.alpha = 0;
//        self.animationLodding = false;
    }];
}

- (void)configPlayImageView {
    @weakify(self);
    [self.playImageView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        

        @strongify(self);
        if(self.roomOwner.uid == [GetCore(HJAuthCoreHelp) getUid].userIDValue) {
            [self presentUserCard:self.roomOwner.uid];
            
            return;
        }
        
        if (GetCore(HJImRoomCoreV2).myMember.is_manager) {
            
            UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
            
            __weak typeof(self)weakSelf = self;
            [alter addAction:[UIAlertAction actionWithTitle:@"送礼物" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [weakSelf showGit];
            }]];
            
            [alter addAction:[UIAlertAction actionWithTitle:@"查看资料" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [weakSelf presentUserCard:weakSelf.roomOwner.uid];
            }]];
            
            [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
                
            }]];
            [self presentViewController:alter animated:YES completion:nil];
            
            return;
        }
        
        
        [self showGit];

        
        
    }]];
}

- (void)onSelectMicUserInfoBtnClick:(UserID)userid {
    [[HJAlertControllerCenter defaultCenter]dismissAlertNeedBlock:NO];
//    [self presentUserCard:self.roomOwner.uid];
    [self presentUserCard:userid];
}

- (void)showGit {
    
    
    [HJGiftBoxView showAllMic:0];
    
}

- (void)dealloc {
    RemoveCoreClientAll(self);
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [NSTimer initialize];
}

- (void)sendMessage{
    NSLog(@"==");
}

- (void)initView {
//    [GetCore(HJActivityCore) getActivity:2];
     [GetCore(HJActivityCore) getAllActivity];
    self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc]initWithTitle:@"" style:(UIBarButtonItemStyleDone) target:self action:nil];
        
    self.messageTableView = [MessageTableView loadFromNib];
    self.messageTableView.tableView.estimatedRowHeight=36;
    self.messageTableView.navigationController = self.navigationController;
    self.messageTableView.tableView.rowHeight=UITableViewAutomaticDimension;//高度设置为自适应
    self.messageTableView.delegate = self;
    [self.view addSubview:self.messageTableView];
    
    //gif播放图
    FLAnimatedImageView *gifView = [FLAnimatedImageView new];
    gifView.contentMode = UIViewContentModeScaleAspectFit;
    gifView.backgroundColor = [UIColor clearColor];
    gifView.runLoopMode = NSRunLoopCommonModes;
    self.gifImageView = gifView;
    self.gifImageView.animationDuration = 1.f;
    [self.view addSubview:self.gifImageView];
    
    //房主View
    self.avatar.layer.cornerRadius = self.avatar.frame.size.width / 2;
    self.avatar.layer.masksToBounds = YES;
    
    //获取房主信息
    self.roomOwner = GetCore(HJImRoomCoreV2).roomOwnerInfo;
    
    //麦序
    self.positionView = [HJGameRoomPositionView loadFromNib];
    self.positionView.delegate = self;
    self.positionView.navigationController = self.navigationController;
    [self.view addSubview:self.positionView];
  
    //礼物显示View
    self.giftContainer = [[UIView alloc]init];
    self.giftContainer.userInteractionEnabled = NO;
    [self.view addSubview:self.giftContainer];
    
//    self.discButton.hidden = YES; //默认先隐藏骰子按钮
    self.editView.hidden = YES;
    //活动
//    self.activityView = [HJRedPacketActivityView loadFromNib];
//    [self.view addSubview:self.activityView];
//    self.activityView.hidden = YES;
        [self.view addSubview:self.banaCommonView];
    self.roomContributionBtn.hidden = YES;
    
    self.shenhaoImageView.layer.cornerRadius = self.shenhaoImageView.width/2;
    

//    self.recordingImg.hidden = NO;
    self.activityView.hidden = NO;
//    self.roomContributionBtn.hidden = NO;
    
//    if (GetCore(ImRoomCoreV2).roomOwner) {
    [self.avatar qn_setImageImageWithUrl:self.roomOwner.avatar placeholderImage:nil type:ImageTypeUserIcon];
    [self.roommerHeadwearImg sd_setImageWithURL:[NSURL URLWithString:self.roomOwner.headwearUrl]];
//    }else {
//        self.avatar.hidden = YES;
//    }
    
    [self updateRoomOwnerMicState];
    //贡献榜
    [self.view addSubview:self.roomContributionBtn];

    [self setupLongZhuView];
    
    [self initConstraint];

    [self roomOwnerUpMic];
    
    
}

//房主自动上麦
- (void)roomOwnerUpMic {
    if (self.isRoomOwnerUpmicSuccess) {
        return;
    }
    if(self.roomOwner.uid == [GetCore(HJAuthCoreHelp) getUid].userIDValue) {
        HJIMQueueItem *item = [GetCore(HJImRoomCoreV2).micQueue safeObjectAtIndex:0];
        HJRoomQueueInfo *temp = item.queueInfo;
        if (temp && [temp.chatRoomMember.account longLongValue] == self.roomOwner.uid) {
            self.isRoomOwnerUpmicSuccess = YES;
            return;
        }
        
        //主坑 设置为房主
        [GetCore(HJRoomQueueCoreV2Help)upMic:-1];
        @weakify(self);
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            @strongify(self);
            //两秒钟上麦一次如果上麦失败
            [self roomOwnerUpMic];
        });
    }
}
#pragma mark - SDCycleScrollViewDelegate
- (void)cycleScrollView:(SDCycleScrollView *)cycleScrollView didSelectItemAtIndex:(NSInteger)index {
    
    ActivityInfo *info = [self.banaCommonArray safeObjectAtIndex:index];
    if (info.skipType == 3) {
        HJWKWebViewController *vc = [[HJWKWebViewController alloc]init];
        NSString *url = [NSString stringWithFormat:@"%@?uid=%@",info.skipUrl,[GetCore(HJAuthCoreHelp)getUid]];
        vc.url = [NSURL URLWithString:url];
        [self.navigationController pushViewController:vc animated:YES];
    }else if (info.skipType == 2) {
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        [[HJRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomOwnerUid:info.skipUrl.userIDValue succ:^(ChatRoomInfo *roomInfo) {
            if (roomInfo != nil) {
                [[HJRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomInfo:roomInfo];
            }else {
                [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
            }
        } fail:^(NSString *errorMsg) {
            [MBProgressHUD showError:errorMsg];
        }];
    }
}
#pragma mark - textFieldDelegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    if (textField.text.length > 0) {
        [self sendText:textField.text];
    }
    return YES;
}


- (void)sendText:(NSString *)text {
    if(!text.length) return;
    
    @weakify(self);
    [GetCore(HJImMessageCore) sensitiveWordRegexWithText:text requestId:NSStringFromClass([self class]) finishBlock:^(BOOL isCanSend, NSString *msg) {
        @strongify(self);
        if (isCanSend) {
            NSString *aText = [NSString stringWithString:JX_STR_AVOID_nil(text)];
            if (aText.length > 500) {
                aText = [aText substringToIndex:500];
            }
            @weakify(self);
            
            [GetCore(HJImRoomCoreV2) sendMessage:aText success:^{
            } failure:^(NSInteger code, NSString *errorMessage) {
                @strongify(self);
                [self showAuthorizationWithCode:code errorMessage:errorMessage];
            }];
            self.editText.text = @"";
            [self.editText resignFirstResponder];
        }
        else {
            [MBProgressHUD showError:msg toView:[UIApplication sharedApplication].keyWindow];
        }
    }];
}

- (void)onSendMessageBanned {
    [UIView showToastInKeyWindow:@"亲，由于您的发言违反平台规定，现已被禁言，若有疑问可联系小熊语音客服。" duration:1 position:YYToastPositionCenter];
}

//关注
- (IBAction)attendBtnAction:(id)sender {
    
    if (self.isFollowing) {
        return;
    }
    
    NSUInteger myUid = GetCore(HJAuthCoreHelp).getUid.userIDValue;
    
    self.isFollowing = YES;
    if (self.attendBtn.selected) {
        [HJHttpRequestHelper cancel:myUid beCanceledUid:self.roomOwner.uid success:^{
            [MBProgressHUD showSuccess:@"取消关注成功"];
            self.attendBtn.selected = NO;
            [self.attendBtn setBackgroundColor:UIColorHex(#FF95B7)];
            self.isFollowing = NO;
//            self.guanzhu_width_layout.constant = 65;//原28
//            self.guanzhu_height_layout.constant = 32;//原28
        } failure:^(NSNumber *resCode, NSString *message) {
            self.isFollowing = NO;
        }];
    }else{
        [HJHttpRequestHelper praise:myUid bePraisedUid:self.roomOwner.uid success:^{
            [MBProgressHUD showSuccess:@"关注成功"];
            self.attendBtn.selected = YES;
            [self.attendBtn setBackgroundColor:[UIColor colorWithWhite:1.0 alpha:0.17]];
            self.isFollowing = NO;
//            self.guanzhu_width_layout.constant = 65;//原44
//            self.guanzhu_height_layout.constant = 32;//原22
        } failure:^(NSNumber *resCode, NSString *message) {
            self.isFollowing = NO;
        }];
    }
    
}

- (void)checkIsFollow:(UserInfo *)info
{
    
    if (!(info.uid>0)) {
        return;
    }
    
    NSUInteger myUid = GetCore(HJAuthCoreHelp).getUid.userIDValue;
    if (info.uid == myUid) {
        
        self.attendBtn.hidden = YES;
        return;
    }
    
    self.attendBtn.hidden = NO;
    
    [HJHttpRequestHelper isLike:myUid isLikeUid:info.uid success:^(BOOL isLike) {
        
        self.attendBtn.selected = isLike;
        if (self.attendBtn.selected) {
//           self.guanzhu_height_layout.constant = 32;//原22
//           self.guanzhu_width_layout.constant = 65;//原44
            
            [self.attendBtn setBackgroundColor:[UIColor colorWithWhite:1.0 alpha:0.17]];
        }else{
//            self.guanzhu_height_layout.constant = 32;//原28
//            self.guanzhu_width_layout.constant = 65;//原28
            [self.attendBtn setBackgroundColor:UIColorHex(#FF95B7)];
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

//神豪榜首数据
- (void)getShenHaoRankData:(BOOL)isShow
{
    if (isShow) {
        [MBProgressHUD showMessage:@"获取中..."];
    }
    
    NSString *roomUId = [NSString stringWithFormat:@"%lld",_roomOwner.uid];
    [HJHttpRequestHelper getRoomShenHaoTop:roomUId Success:^(NSMutableArray *bounsInfoList) {
        if (bounsInfoList.count>0) {
            
            HJRoomBounsListInfo *first = bounsInfoList.firstObject;
            
            if ([first.sumGold floatValue]>=1000) {
                _shenHaoTopMemeber = first;
                [self.shenhaoImageView qn_setImageImageWithUrl:_shenHaoTopMemeber.avatar placeholderImage:@"xbd_room_shenhao_avatar" type:ImageTypeUserIcon];
//                [self.roomShenhaoBtn.imageView qn_setImageImageWithUrl:_shenHaoTopMemeber.avatar placeholderImage:@"xbd_room_shenhao_avatar" type:ImageTypeUserIcon];
                
//                NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:_shenHaoTopMemeber.avatar]];
//                [self.roomShenhaoBtn setImage:[UIImage imageWithData:imageData] forState:UIControlStateNormal];
            }
        }
        
        if (isShow) {
            [HJRoomShenHaoView show:self.shenHaoTopMemeber];
            [MBProgressHUD hideHUD];
        }
        
    } failure:^(NSNumber *code, NSString *msg) {
        if (isShow) {
            [MBProgressHUD hideHUD];
        }
    }];
    
}
//MARK:- 更新麦位魅力值
- (void)updateMicCharm:(NSMutableArray<HJRoomMemberCharmInfoModel *> *)memberCharmInfoList {
    
  for (HJRoomMemberCharmInfoModel *charmInfoModel in memberCharmInfoList) {
      [self.charmInfoMap setValue:@{@"value" : JX_STR_AVOID_nil(charmInfoModel.value),
                                    @"withHat" : [NSNumber numberWithBool:charmInfoModel.withHat]
                                    } forKey:JX_STR_AVOID_nil(charmInfoModel.uid)];
  }

    NSString* uid = [NSString stringWithFormat:@"%lld",GetCore(HJImRoomCoreV2).currentRoomInfo.uid];
    NSString *charmValue = [[self.charmInfoMap valueForKey:uid] valueForKey:@"value"];
    self.charmFangzhuLabel.text = (charmValue == nil) ?@"0":[NSString numberStringFromNumber:[NSNumber numberWithInteger:charmValue.integerValue] withPrefixString:@""];
    
//     BOOL hasHat = [[[self.charmInfoMap valueForKey:uid] valueForKey:@"withHat"] boolValue];
    
     ChatRoomInfo *roomInfo = [GetCore(HJRoomCoreV2Help) getCurrentRoomInfo];
    if (roomInfo.charmOpen ==0) {//用于控制魅力值开关[ 0.隐藏; 1.显示 ]
        self.enableCharm = NO;
    }else{
        self.enableCharm = YES;
    }
    
//    self.hatwearImageview.hidden = !hasHat;
//    self.charmFangzhuLabel.hidden = !self.enableCharm;
//    self.charmFangzhuImageView.hidden = !self.enableCharm;
    
//    self.charmFangzhuLabel.hidden =YES;
//    self.charmFangzhuImageView.hidden =YES;
    
    [GetCore(HJRoomCoreV2Help) saveCharmInfoMap:self.charmInfoMap];
}

//更新房主魅力值
-(void)updateRoomerCharmValue{
    ChatRoomInfo *roomInfo = GetCore(HJImRoomCoreV2).currentRoomInfo;
    if (roomInfo.charmOpen ==0) {//用于控制魅力值开关[ 0.隐藏; 1.显示 ]
        self.enableCharm = NO;
    }else{
        self.enableCharm = YES;
    }

//    self.charmFangzhuLabel.hidden = !self.enableCharm;
//    self.charmFangzhuImageView.hidden = !self.enableCharm;
    
//    self.charmFangzhuLabel.hidden =YES;
//    self.charmFangzhuImageView.hidden =YES;
}



- (void)sendCharmValueRoomIMMsg
{
    ChatRoomMember *myMember = GetCore(HJImRoomCoreV2).myMember;;
    
    HJRoomQueueCustomAttachment *roomQueueAttachment = [[HJRoomQueueCustomAttachment alloc]init];
    roomQueueAttachment.uid = [myMember.account longLongValue];
    
    Attachment *attachement = [[Attachment alloc]init];
    attachement.first = Custom_Noti_Header_Queue;
     attachement.second = Custom_Noti_Header_ClearCharmValue;
    attachement.data = roomQueueAttachment.encodeAttachment;
    
    [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
}
//发送设置、移出管理员消息
- (void)sendManagerMemberRoomIMMsg:(BOOL)isManager uid:(UserID)uid
{
    ChatRoomMember *myMember = GetCore(HJImRoomCoreV2).myMember;
    
    __block UserInfo *infoModel = [GetCore(HJUserCoreHelp) getUserInfoInDB:uid];
    if (infoModel == nil) {
         [GetCore(HJUserCoreHelp) getUserInfo:uid refresh:YES success:^(UserInfo *info) {

                   infoModel = info;
             
             HJRoomQueueCustomAttachment *roomQueueAttachment = [[HJRoomQueueCustomAttachment alloc]init];
               roomQueueAttachment.uid = [myMember.account longLongValue];
               roomQueueAttachment.micName = infoModel.nick;
               Attachment *attachement = [[Attachment alloc]init];
               attachement.first = Custom_Noti_Header_Queue;
               if (isManager) {
                    attachement.second = Custom_Noti_Header_Set_Second_Manager_Open;
               }else{
                    attachement.second = Custom_Noti_Header_Set_Second_Manager_Close;
               }
               
               attachement.data = roomQueueAttachment.encodeAttachmentForManager;
               
               [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
             
               }];
    }else{
        HJRoomQueueCustomAttachment *roomQueueAttachment = [[HJRoomQueueCustomAttachment alloc]init];
           roomQueueAttachment.uid = [myMember.account longLongValue];
           roomQueueAttachment.micName = infoModel.nick;
           Attachment *attachement = [[Attachment alloc]init];
           attachement.first = Custom_Noti_Header_Queue;
           if (isManager) {
                attachement.second = Custom_Noti_Header_Set_Second_Manager_Open;
           }else{
                attachement.second = Custom_Noti_Header_Set_Second_Manager_Close;
           }
           
           attachement.data = roomQueueAttachment.encodeAttachmentForManager;
           
           [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
    }
    
}

//MARK:-发送设置、移出管理员成功回调 
-(void)setManagerMemberSuccess:(BOOL)isManager uid:(UserID)uid{
    
    [self sendManagerMemberRoomIMMsg:isManager uid:uid];
}




//MARK:-清空魅力值成功回调
-(void)userReceiveRoomMicMsgSuccess{
    [self sendCharmValueRoomIMMsg];
    NSLog(@"ddduserReceiveRoomMicMsgSuccess");
}
//清空魅力值失败回调
-(void)userReceiveRoomMicMsgFail{
    NSLog(@"ddduserReceiveRoomMicMsgFail");
}
//房间公告
- (IBAction)roomNoticeAction:(id)sender {
    [self topicLabelDidClick];//showPlacardView]
}

#pragma mark - RoomCoreClient
- (void)onCurrentRoomInfoChanged {
    
//    BOOL online = GetCore(HJImRoomCoreV2).roomOwner.is_online;
//    self.userLeaveImg.hidden = online;
    
    [self updateOnLineCount];

    
    [self updateView];
}

/** 用户进入退出加数组和动画*/
- (void)onUserEnterChatRoomWith:(HJIMMessage *)message {
    JXIMChatroomNotificationContent *content = message.messageObject.notificationContent;
    if (content.eventType == NIMChatroomEventTypeEnter) {
        
        if (message.member.account.userIDValue == self.roomOwner.uid) {
            self.userLeaveImg.hidden = YES;
        }
        
        [self.enterTipView show:message.member];
        
    }
}





#pragma mark <HJImRoomCoreClient>
- (void)minimizeEnterSuccess
{
    [GetCore(HJImRoomCoreV2) checkRoomerIsInRoom];
    [self checkPKState:YES];
}

#pragma mark - Event Response
// 播放音乐
- (IBAction)playMusicClickAction:(id)sender {
    
    [HJRoomPlayerView show];
    

}




//今日神豪
- (IBAction)shenhaoAction:(id)sender {
    
//    [self getShenHaoRankData:YES];
    [self showContributionListView];
    
}

//点击神豪头像
- (IBAction)shenhaoBtnDidClick:(id)sender {
    [self getShenHaoRankData:YES];
}


- (void)topicLabelDidClick{
    
    ChatRoomMember *myMember = GetCore(HJImRoomCoreV2).myMember;;
    if (myMember.is_creator || myMember.is_manager) {
        HJRoomSettingTopicVC *vc = (HJRoomSettingTopicVC *)[[HJRoomViewControllerFactory sharedFactory] instantiateGameHJRoomSettingTopicVC];
        [self.navigationController pushViewController:vc animated:YES];
        
    } else {
        [self showPlacardView];
    }
}





//点击礼物按钮
- (IBAction)onGiftBtnClicked:(id)sender {
    [self.view bringSubviewToFront:self.giftContainer];
    [self showGiftContainerView];
}

- (void)getWalletData
{
    UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
    [GetCore(PurseCore) requestBalanceInfo:uid];
}
#pragma mark ------------------ 抽奖 --------------------
- (IBAction)lotteryClick:(UIButton *)sender {
    [self getWalletData];
    
    [self showlotteryView];//life-hj
    
    //1、筛选打call礼物
//    NSMutableArray* callGiftList = [NSMutableArray array];
//   NSMutableArray* giftList = [HJGiftInfoStorage getGiftInfosWithtype:GiftTypeNormal];
//    for (GiftInfo* infoModel in giftList) {
//        if (infoModel.giftType ==1) {
//            [callGiftList addObject:infoModel];
//        }
//    }
//
//    [callGiftList sortUsingComparator:^NSComparisonResult(GiftInfo* _Nonnull obj1, GiftInfo* _Nonnull obj2)
//    {
//        if (obj1.goldPrice > obj2.goldPrice){
//            return NSOrderedAscending;
//        }else{
//            return NSOrderedDescending;
//        }
//    }];
//
//
//
//    __weak typeof(self)weakSelf = self;
//    [HJCallView showCall:^(HJCallType type,HJDaCallIntoModel* model) {
//        switch (type) {
//                   case HJCallTypeDaCall:
//                   {
//                       [weakSelf daCallAction:model];
//                   }
//                       break;
//                   case HJCallTypePickupConch:
//                   {
//                       [weakSelf pickupConchAction];
//                   }
//                       break;
//
//                   default:
//                       break;
//               }
//    } list:callGiftList];
    
    
}


-(void)daCallAction:(HJDaCallIntoModel*)model{
   
    //1、请求数据
    
    //MARK: - 打call /gift/callForUser  给单个用户打call
    [HJHttpRequestHelper getRoomCallForUser:model Success:^(HJDaCallModel *callModel) {
        [[NSNotificationCenter defaultCenter] postNotificationName:@"NotificationCallModel" object:callModel];
        [self sendWanfaMessageForChangeInfo:callModel];
        
    } failure:^(NSNumber *code, NSString *error) {
     
        if ([error isEqualToString:@"2103:账户余额不足，请充值"]) {
             [self showBalanceNotEnougthForPlayCall];
        }
        
    }];
    
}


- (void)showBalanceNotEnougthForPlayCall {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPurseNoMoneyTitle, nil) message:NSLocalizedString(XCPurseNoMoneyMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    @weakify(self);
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        if (self.giftContainerSheet != nil) {
            [self.giftContainerSheet dismissViewControllerAnimated:YES];
        }
        
        UIViewController *vc = [[HJPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
        [self.navigationController pushViewController:vc animated:YES];
        [[NSNotificationCenter defaultCenter] postNotificationName:@"closeForPlayCall" object:nil];
        
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}






//打call发送消息
- (void)sendWanfaMessageForChangeInfo:(HJDaCallModel *)callModel{
    
    Attachment *attachement = [[Attachment alloc]init];
    attachement.first = Custom_Noti_Header_Playcall;
    attachement.second = Custom_Noti_Header_Playcall;

    NSMutableDictionary *buffer = [NSMutableDictionary dictionary];
    [buffer setObject:callModel.giftName forKey:@"giftName"];
//    [buffer setObject:@(callModel.giftNum) forKey:@"giftNum"];
    [buffer setObject:callModel.giftPic forKey:@"giftUrl"];
    [buffer setObject:callModel.nick forKey:@"sendName"];
    [buffer setObject:@(callModel.targetUid) forKey:@"targetUid"];
     [buffer setObject:@(callModel.uid) forKey:@"uid"];
    [buffer setObject:callModel.targetNick forKey:@"targetName"];
     [buffer setObject:[NSString stringWithFormat:@"%lld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] forKey:@"roomId"];
    NSDictionary *attMessageDic = @{@"params": buffer};
    attachement.data = attMessageDic;
    
    [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%lld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:(JXIMSessionType) NIMSessionTypeChatroom];
}


//
-(void)pickupConchAction{
      NSLog(@"dddcccc");
    [self showlotteryView];
}

//点击分享按钮
- (IBAction)onShareBtnClicked:(id)sender {
    [self showSharePanelView];
}

//关闭房间按钮
- (IBAction)closeBtnClick:(id)sender {
    [self onCloseRoomBtnClicked];
}


//贡献榜
//- (IBAction)onContributionBtnClick:(UIButton *)sender {
//    [self showContributionListView];
////    if (_delegate) {
////        [_delegate scrollToListView];
////    }
//}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.editText resignFirstResponder];
}

- (void)touchesMoved:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.editText resignFirstResponder];
}

#pragma mark - overload
- (void)beKicked {
    [self updateOnLineCount];
    [UIView showToastInKeyWindow:NSLocalizedString(XCRoomYouAlreadyBeKicked, nil) duration:3 position:YYToastPositionCenter];
    [[HJRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
    
//    if (reson == 2) {
//        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomYouAlreadyBeKickedByManager, nil) duration:3 position:YYToastPositionCenter];
//        [[HJRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
//    } else if (reson == 5){
//        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomYouAlreadyInBlackListByManager, nil) duration:3 position:YYToastPositionCenter];
//        [[HJRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
//    } else {
//        [self showRoomOwnnerExit];
//    }
    
    if ([_delegate respondsToSelector:@selector(roomExit)]) {
        [_delegate roomExit];
    }
}

- (void)beInBlackList {
    [UIView showToastInKeyWindow:NSLocalizedString(XCRoomYouAlreadyInBlackListByManager, nil) duration:3 position:YYToastPositionCenter];
    [[HJRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
}

#pragma mark - RoomUIClient

- (void)dismissFaceView {
    @weakify(self);
    [self.faceAlert dismissViewControllerAnimated:YES completion:^{
        @strongify(self);
        self.faceAlert = nil;
    }];
}

- (void)onSendGiftFail {
    [self showBalanceNotEnougth];
}

#pragma mark - Private Method

- (void)addCore{
    AddCoreClient(RoomUIClient, self);
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJBalanceErrorClient, self);
    AddCoreClient(HJMeetingCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    AddCoreClient(HJImRoomCoreClient, self);
    AddCoreClient(HJRoomQueueCoreClient, self);
    AddCoreClient(HJFaceCoreClient, self);
    AddCoreClient(HJImMessageCoreClient, self);
    AddCoreClient(HJPraiseCoreClient, self);
    AddCoreClient(HJActivityCoreClient, self);
    AddCoreClient(HJGiftCoreClient, self);
    AddCoreClient(HJImMessageSendCoreClient, self);
    AddCoreClient(HJOnlineCoreClient, self);
    AddCoreClient(HJUserCoreClient, self);
    AddCoreClient(HJLongZhuCoreClient, self);
}

- (void)addNotificationCenter{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHidden:) name:UIKeyboardWillHideNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
}

- (void)removeNotificationCenter {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
}

//更新房间信息
- (void)updateView {
    self.roomInfo = GetCore(HJImRoomCoreV2).currentRoomInfo;
    if (self.roomInfo) {
        
        [self updateRoomerCharmValue];
        [self configEggSysBtn];
        
        [self.positionView layoutSubviews];
        
        GetCore(HJGiftCore).isOpenAnimation = !self.roomInfo.giftEffectSwitch;
        
        if (([self.roomInfo.backPicUrl hasPrefix:@"http"] || [self.roomInfo.backPicUrl hasPrefix:@"https"])) {
            [self.bgImg sd_setImageWithURL:[NSURL URLWithString:self.roomInfo.backPicUrl] placeholderImage:[UIImage imageNamed:@"hj_room_bg_default"]];
        } else {
            NSString *imageName = @"hj_room_bg_default";
            self.bgImg.image = [UIImage imageNamed:imageName];
        }
        
        
        if ([_delegate respondsToSelector:@selector(roomInitSuccessWithType:)]) {
            [_delegate roomInitSuccessWithType:self.roomInfo.type];
        }
        
        if (self.roomInfo != nil) {
            @weakify(self);
            if (self.roomInfo != nil && !self.roomInfo.valid) {
                //房主不在房间
                [self showRoomOwnnerExit];
            }
            
            //获取房主个人信息（服务器）
            self.roomOwner = GetCore(HJImRoomCoreV2).roomOwnerInfo;
            [self.avatar qn_setImageImageWithUrl:self.roomOwner.avatar placeholderImage:nil type:ImageTypeUserIcon];
            [self.roommerHeadwearImg sd_setImageWithURL:[NSURL URLWithString:self.roomOwner.headwearUrl]];
            
            self.ownerNameLabel.text = self.roomInfo.title;
            //房间名称滚动
            self.ownerNameLabel.text = self.roomInfo.title;
            self.ownerNameLabel.marqueeType = MLContinuous;
            self.ownerNameLabel.scrollDuration = 8.0; //滚动持续时间
            self.ownerNameLabel.fadeLength = 15.0f; //靠屏幕两边褪色长度
            
            //房主昵称
            self.roomerNameLabel.text = self.roomOwner.nick;
//            self.roomerNameLabel.marqueeType = MLContinuous;
//            self.roomerNameLabel.scrollDuration = 8.0; //滚动持续时间
//            self.roomerNameLabel.fadeLength = 15.0f; //靠屏幕两边褪色长度
            
            
            if (self.roomInfo.roomPwd.length > 0) { //房间有密码
                self.lockIcon.hidden = NO;
                self.left_roomName.constant = 24;
//                self.yellowPoint.hidden = NO;
            }else {
                self.lockIcon.hidden = YES;
                self.left_roomName.constant = 9;
//                self.yellowPoint.hidden = NO;
            }
            
//            self.onlineNumberLabel.text = [NSString stringWithFormat:@"%ld人在线",GetCore(HJImRoomCoreV2).onlineNumber];
                NSInteger onlineNumber = GetCore(HJImRoomCoreV2).onlineNumber;
            if (onlineNumber<10) {
                [self.onlineNumberBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_onlineBg1"] forState:UIControlStateNormal];
            }else if(onlineNumber<100){
                [self.onlineNumberBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_onlineBg2"] forState:UIControlStateNormal];
            }else{
                [self.onlineNumberBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_onlineBg3"] forState:UIControlStateNormal];
            }
            [self.onlineNumberBtn setTitle:[NSString stringWithFormat:@"%ld",onlineNumber] forState:UIControlStateNormal];
            
            CGFloat widCons = [[NSString stringWithFormat:@"ID:%@",self.roomOwner.erbanNo] widthForFont:[UIFont systemFontOfSize:11]];
            self.roomIDLabel.text = [NSString stringWithFormat:@"ID:%@",self.roomOwner.erbanNo];
            
            if (self.roomOwner != nil) {
                [self updateCoverView]; //更新房间信息
            }
            
        }
        [self.messageTableView reloadData];
    } else {
        NSString *imageName = @"hj_room_bg_default";
        self.bgImg.image = [UIImage imageNamed:imageName];
        GetCore(HJGiftCore).isOpenAnimation = YES;
    }
    
    [self setupHeaderView];
    
//    if (GetCore(HJGiftCore).lastGiftInfo) {
//        self.giftRecordBtn.hidden = NO;
//    }
//    else {
////        self.giftRecordBtn.hidden = YES;
//         self.giftRecordBtn.hidden = NO;
//    }
    self.giftRecordBtn.hidden = YES;
    
//Mark: -判断捡海螺按钮是否显示
    
    
    
    
    
}

- (void)showSVGWithindexPath:(NSString *)vga {
    vga = [DESEncrypt decryptUseDES:vga key:@"MIIBIjANBgkqhkiG9w0B"];
    if (!vga || vga.length == 0) {return;}
    [self.parser parseWithURL:[NSURL URLWithString:vga] completionBlock:^(SVGAVideoEntity * _Nullable videoItem) {
        if (videoItem != nil) {
            dispatch_async(dispatch_get_main_queue(), ^{
                self.svgaDisplayView.contentMode = UIViewContentModeScaleAspectFit;
                self.svgaDisplayView.alpha = 1;
            });
            self.svgaDisplayView.loops = 1;
            self.svgaDisplayView.clearsAfterStop = YES;
            self.svgaDisplayView.videoItem = videoItem;
            [self.svgaDisplayView startAnimation];
        }
    } failureBlock:nil];
}


#pragma mark - ReceiveGift  HJGameRoomContainerVC 调用
- (void)receiveMicGift:(HJGiftAllMicroSendInfo *)giftInfo {
    self.messageTableView.headerView.model = giftInfo;
    self.messageTableView.headerView.giftView.hidden = NO;
}

- (void)showIsBeDownMic {
    [MBProgressHUD showSuccess:@"您已被挤下麦～"];
}

#pragma mark - ImRoomCoreClientV2
- (void)onCurrentRoomOnLineUserCountUpdate{
    [self updateOnLineCount];
}

#pragma mark - ImRoomCoreClient
- (void)onUserInterChatRoom:(ChatRoomMember *)member {
    [self updateOnLineCount];
}
- (void)onUserExitChatRoom:(NSString *)roomId uid:(NSString *)uid {
    [self updateOnLineCount];
}
- (void)onUserBeAddBlack:(NSString *)uid {
    [self updateOnLineCount];
    
    if (uid.integerValue == [[GetCore(HJAuthCoreHelp) getUid] integerValue]) {
        // 自己被加入黑名单 退出房间
        [[HJRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
    }
}
- (void)onConnectionStateChanged:(NIMChatroomConnectionState)state{
    [self updateOnLineCount];
}

- (void)updateOnLineCount{
//    self.onlineNumberLabel.text = [NSString stringWithFormat:@"%ld人在线",(long)GetCore(HJImRoomCoreV2).onlineNumber];
    [self.onlineNumberBtn setTitle:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).onlineNumber] forState:UIControlStateNormal];
}


//更新背景
- (void)updateCoverView {
    if ([_delegate respondsToSelector:@selector(updateBackPicWith:userInfo:)]) {
        [_delegate updateBackPicWith:self.roomInfo userInfo:self.roomOwner];
    }
}



//关闭房间
- (void)onCloseRoomBtnClicked
{
    if ([self.delegate respondsToSelector:@selector(roomMoreShowMenuActionIsOnMic:)]) {
        [self.delegate roomMoreShowMenuActionIsOnMic:self.isIsOnMic];
    }
}

- (void)setupHeaderView {
    
//    self.messageTableView.headerView.frame = CGRectMake(0, 0, XC_SCREE_W, 72);
//    self.messageTableView.tableView.tableHeaderView = self.messageTableView.headerView;
    self.messageTableView.headerView.giftView.hidden = YES;
    
    self.headerViewTip.textInsets = UIEdgeInsetsMake(2, 8, 2, 8);
    [self.headerViewTip setLineBreakMode:NSLineBreakByCharWrapping];
    CGSize textSize = [self.headerViewTip.text boundingRectWithSize:CGSizeMake(kScreenWidth - 114, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : self.headerViewTip.font} context:nil].size;
    self.messageTableViewHeader.frame = CGRectMake(0, 0, kScreenWidth - 94, textSize.height + 16);
    
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth - 94, textSize.height + 16)];
    headerView.backgroundColor = [UIColor clearColor];
    [headerView addSubview:self.messageTableViewHeader];
    
    if (self.roomInfo.playInfo.length) {
        
        CGSize playInfoSize = [self.roomInfo.playInfo boundingRectWithSize:CGSizeMake(kScreenWidth - 114, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:13.f]} context:nil].size;
        UIView *playInfoView = [UIView new];
        playInfoView.backgroundColor = [UIColor clearColor];
        playInfoView.frame = CGRectMake(5, textSize.height + 16 + 5, kScreenWidth - 99, playInfoSize.height + 20);
        
        UIView *bgView = [UIView new];
        bgView.frame = CGRectMake(0, 0, kScreenWidth - 99, playInfoSize.height + 20);
        bgView.backgroundColor = [UIColor blackColor];
        bgView.alpha = 0.2f;
        bgView.layer.cornerRadius = 4.f;
        bgView.layer.masksToBounds = YES;
        [playInfoView addSubview:bgView];
        
        UILabel *playInfoLabel = [UILabel new];
        playInfoLabel.numberOfLines = 0;
        playInfoLabel.font = [UIFont systemFontOfSize:13.f];
        playInfoLabel.textColor = UIColorHex(FF4ECF);
        playInfoLabel.text = self.roomInfo.playInfo;
        playInfoLabel.frame = CGRectMake(6, 10, playInfoSize.width, playInfoSize.height);
        [playInfoView addSubview:playInfoLabel];
        
        [headerView addSubview:playInfoView];
        
        headerView.frame = CGRectMake(0, 0, kScreenWidth - 94, textSize.height + 16 + 5 + playInfoSize.height + 20 + 6);
    }
    
    self.messageTableView.tableView.tableHeaderView = headerView;
}


#pragma mark - Getter
- (NSMutableArray<GiftInfo *> *)gifQueueArr {
    if (_gifQueueArr == nil) {
        _gifQueueArr = [NSMutableArray array];
    }
    return _gifQueueArr;
}

-(HJToolBar*)toolBar{
    if (_toolBar == nil) {
        _toolBar = [[HJToolBar alloc] init];
        [self.view addSubview:_toolBar];
    }
    return _toolBar;
}

//MARK: - Action
- (void)giftHintViewDidClick {
    
    [self showGiftListView];
}

//点击聊天按钮()
- (void)talkMessageSelectClick{
    //发送筛选消息区分
    if (self.messageType == HJMessageTypeAll) {
        self.messageType = HJMessageTypeTalk;
        [self.talkMessageSelectBtn setImage:[UIImage imageNamed:@"hj_room_talkType"] forState:UIControlStateNormal];
    }else if(self.messageType == HJMessageTypeTalk){
        self.messageType = HJMessageTypeGift;
        [self.talkMessageSelectBtn setImage:[UIImage imageNamed:@"hj_room_giftType"] forState:UIControlStateNormal];
    }else if(self.messageType == HJMessageTypeGift){
        self.messageType = HJMessageTypeDaCall;
        [self.talkMessageSelectBtn setImage:[UIImage imageNamed:@"hj_room_dacallType"] forState:UIControlStateNormal];
    }else if(self.messageType == HJMessageTypeDaCall){
        self.messageType = HJMessageTypeAll;
        [self.talkMessageSelectBtn setImage:[UIImage imageNamed:@"hj_room_allType"] forState:UIControlStateNormal];
    }
    //通知公屏刷新
    NSString *msgType = [NSString stringWithFormat:@"%ld",(long)self.messageType];
    [[NSNotificationCenter defaultCenter] postNotificationName:@"NotificationSelectTalkMessage" object:nil userInfo:@{@"messageType" : msgType}];
}

//捡海螺 Action
- (void)conchHintViewDidClick {
    
    [self showGiftListView];
}


- (void)setRoomInfo:(ChatRoomInfo *)roomInfo
{
    _roomInfo = roomInfo;
}

- (void)setRoomOwner:(UserInfo *)roomOwner
{
    _roomOwner = roomOwner;
    
    //防止重复请求关注接口
    if (!self.hasCheckFollow) {
        self.hasCheckFollow = YES;
        [self checkIsFollow:roomOwner];
        
        [self getShenHaoRankData:NO];
        
        
    }
}

- (UIButton *)pkBtn
{
    if (!_pkBtn) {
        _pkBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_pkBtn setImage:[UIImage imageNamed:@"xbd_pk_logo"] forState:UIControlStateNormal];
    }
    return _pkBtn;
}

- (UIButton *)giftRecordBtn
{
    if (!_giftRecordBtn) {
        [_giftRecordBtn setHidden:YES];
        _giftRecordBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_giftRecordBtn setImage:[UIImage imageNamed:@"hj_room_giftRecord"] forState:UIControlStateNormal];
        [_giftRecordBtn addTarget:self action:@selector(giftHintViewDidClick) forControlEvents:UIControlEventTouchUpInside];
        [self.view addSubview:_giftRecordBtn];
    }
    return _giftRecordBtn;
}

- (UIButton *)talkMessageSelectBtn{
    if (!_talkMessageSelectBtn) {
        _talkMessageSelectBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_talkMessageSelectBtn setImage:[UIImage imageNamed:@"hj_room_allType"] forState:UIControlStateNormal];
        [_talkMessageSelectBtn addTarget:self action:@selector(talkMessageSelectClick) forControlEvents:UIControlEventTouchUpInside];
//        [self.view addSubview:_talkMessageSelectBtn];
    }
    return _talkMessageSelectBtn;
}


- (UIButton *)conchRecordBtn
{
    if (!_conchRecordBtn) {
        _conchRecordBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_conchRecordBtn setImage:[UIImage imageNamed:@"hj_room_jianhailu_record"] forState:UIControlStateNormal];
        [_conchRecordBtn addTarget:self action:@selector(conchHintViewDidClick) forControlEvents:UIControlEventTouchUpInside];
        [self.view addSubview:_conchRecordBtn];
    }
    return _conchRecordBtn;
}


- (HJRoomComeView *)enterTipView
{
    if (!_enterTipView) {
        _enterTipView = [[NSBundle mainBundle] loadNibNamed:@"HJRoomComeView" owner:self options:nil].firstObject;
        
        __weak typeof(self)weakSelf = self;
        _enterTipView.carBlock = ^(NSString *carUrl) {
            
            if (weakSelf != nil) {
                
                if (!GetCore(HJImRoomCoreV2).enterRoomInfo.giftCardSwitch) {
                    [weakSelf showSVGWithindexPath:carUrl];
                }
            }
        };
        
        [self.view addSubview:_enterTipView];
    }
    return _enterTipView;
}
- (NSMutableDictionary<NSString *,id> *)charmInfoMap{
    if (!_charmInfoMap) {
        _charmInfoMap = [NSMutableDictionary dictionary];
    }
    return _charmInfoMap;
}
- (SDCycleScrollView *)banaCommonView
{
    if (!_banaCommonView) {
        _banaCommonView = [[SDCycleScrollView alloc] init];
        _banaCommonView.frame= CGRectMake(12, 0, kScreenWidth-24, 120);
        _banaCommonView.delegate = self;
        _banaCommonView.pageControlAliment = SDCycleScrollViewPageContolAlimentCenter;
        _banaCommonView.backgroundColor = [UIColor clearColor];
        _banaCommonView.bannerImageViewContentMode = UIViewContentModeScaleToFill;
        _banaCommonView.showPageControl = NO;
        _banaCommonView.layer.cornerRadius = 7;
        _banaCommonView.layer.masksToBounds = YES;
    }
    return _banaCommonView;
}
@end
