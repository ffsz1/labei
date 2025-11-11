//
//  HJGameRoomVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
//  轰趴房控制器

#import "YYViewController.h"


#import "HJGameRoomPositionView.h" //坑位（麦序）
#import "MessageTableView.h" //公屏

//房间相关&麦序相关
#import "HJRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "HJRoomQueueCoreV2Help.h"
#import "HJRoomQueueCoreClient.h"
#import "HJFinishLiveView.h"

//IM相关
#import "HJImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"

//礼物相关
#import "HJGiftCore.h"
#import "HJGiftCoreClient.h"
#import "HJLiveGiftShowCustom.h"
#import <FLAnimatedImage/FLAnimatedImageView.h>
#import "HJGiftViewContainer.h"

//分享相关
#import "HJShareCoreClient.h"


//控件
#import "YYActionSheetViewController.h"
#import "TYAlertController.h"
#import "HJMarqueeLabel.h"

//会议相关
#import "HJMeetingCore.h"
#import "HJMeetingCoreClient.h"

//登录系统
#import "HJAuthCoreHelp.h"

//余额不足
#import "HJBalanceErrorClient.h"

#import <FLAnimatedImageView.h>

#import "HJImMessageCoreClient.h"
#import "HJImMessageSendCoreClient.h"


#import "HJUserViewControllerFactory.h"

#import "HJPraiseCore.h"
#import "HJRedPacketActivityView.h"

#import "ActivityInfo.h"
#import "HJActivityCoreClient.h"
#import "HJActivityCore.h"

#import "HJToolBar.h"
#import "HJGameRoomFaceView.h"
#import "HJAlertControllerCenter.h"
#import "UIView+XCToast.h"

#import "MMAlertView.h"
#import "MMSheetView.h"
#import <YYImage/YYImage.h>

#import "HJSessionListViewController.h"
#import "WMLabel.h"

#import "HJGameRoomPeiDuiEnterView.h"


#import "HJGiftSecretInfo.h"
#import "HJRoomComeView.h"
#import "HJRoomBounsListInfo.h"
#import "SDCycleScrollView.h"

@protocol HJGameRoomVCDelagte <NSObject>

- (void)roomExit;
- (void)roomInitSuccessWithType:(RoomType)roomtype;
- (void)roomCloseButtonClick;
- (void)roomMoreShowMenuActionIsOnMic:(BOOL)isMic;
- (void)updateBackPicWith:(ChatRoomInfo *)info userInfo:(UserInfo *)userInfo;
- (void)auctionListButtonClick;
- (void)scrollToListView;
@end

@interface HJGameRoomVC : UIViewController

@property (nonatomic, copy) NSString *lotteryBoxBigGift;

@property (nonatomic, assign) BOOL isAll;
@property (nonatomic, strong) GiftReceiveInfo *giftInfo;

//=======================麦序坑位=========================//
@property (strong, nonatomic) HJGameRoomPositionView *positionView;

//=======================公屏=======================//
@property (strong, nonatomic) MessageTableView *messageTableView;
@property (strong, nonatomic) IBOutlet UIView *messageTableViewHeader;//公屏警告语
@property (weak, nonatomic) IBOutlet WMLabel *headerViewTip;
@property (weak, nonatomic) IBOutlet UIImageView *bgImg;


//===================输入框=====================//
@property (weak, nonatomic) IBOutlet UIView *editView;//输入容器
@property (weak, nonatomic) IBOutlet UITextField *editText; //输入框
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *editViewBottomConstraint;
@property (assign, nonatomic) BOOL keyboardIsShow;
@property (weak, nonatomic) IBOutlet UIButton *eggSysBtn;

//====================主坑=======================//
@property (weak, nonatomic) IBOutlet HJMarqueeLabel *ownerNameLabel;//房间名字
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_roomName;
@property (weak, nonatomic) IBOutlet UILabel *roomIDLabel;//房间ID
@property (weak, nonatomic) IBOutlet UIImageView *lockIcon;// 锁的标志
@property (weak, nonatomic) IBOutlet UILabel *onlineNumberLabel;//在线用户人数
@property (weak, nonatomic) IBOutlet UIButton *onlineNumberBtn;



@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UIImageView *userLeaveImg;

@property (weak, nonatomic) IBOutlet UIImageView *recordingImg;



@property (strong , nonatomic) ChatRoomMember *mainPositionMember;
@property(nonatomic, strong) UserInfo *roomOwner;
@property (nonatomic, assign) BOOL isSpeaking;

@property (weak, nonatomic) IBOutlet UIImageView *playImageView;
@property (weak, nonatomic) IBOutlet UIImageView *mainPositionSpeakingAnimationImageView;

@property (weak, nonatomic) IBOutlet GGImageView *yellowPoint;
@property (weak, nonatomic) IBOutlet UIButton *attendBtn;

@property (strong, nonatomic) UIView *bV;
@property (weak, nonatomic) IBOutlet UIButton *shareButton;

@property (copy, nonatomic) NSString *backPic;


//=================房间下方工具条=================//
@property (strong, nonatomic) HJToolBar *toolBar;
@property (weak, nonatomic) IBOutlet UIButton *closeButton;//关闭房间按钮
@property (strong, nonatomic) TYAlertController *faceAlertView; //表情弹窗
@property (weak, nonatomic) IBOutlet UIImageView *roommerHeadwearImg;

//==================礼物View====================//
@property (strong, nonatomic) TYAlertController *giftAlertView;

@property (strong, nonatomic) UIView *giftContainer;
@property (strong, nonatomic) NSMutableArray<GiftInfo *> *gifQueueArr; //礼物队列
@property (nonatomic, strong) FLAnimatedImageView *gifImageView;//gif播放
@property(nonatomic, strong)HJLiveGiftShowCustom* customGiftShow;

//====================房间======================//
@property(nonatomic, strong) ChatRoomInfo *roomInfo;


//====================弹窗=====================//
@property (nonatomic, strong) HJRedPacketActivityView *activityView; //活动view
@property (nonatomic, strong) SDCycleScrollView *banaCommonView; //活动view
@property (nonatomic, strong) NSArray *banaCommonArray;
@property (nonatomic, strong) ActivityInfo *activityInfo;
@property (nonatomic, strong) TYAlertController* sharePanelSheet;
@property (nonatomic, strong) YYActionSheetViewController* moreActionSheet;
@property (nonatomic, strong) TYAlertController* giftContainerSheet;
@property (assign, nonatomic) BOOL isLike;
@property (nonatomic, strong) TYAlertController *alertViewController;//公用弹窗
//贡献榜按钮
@property (weak, nonatomic) IBOutlet UIButton *roomShenhaoBtn;
@property (strong, nonatomic) UIButton *roomContributionBtn;
@property (weak, nonatomic) id<HJGameRoomVCDelagte> delegate;

//====================表情=====================//
@property (nonatomic, strong) HJGameRoomFaceView *faceView;
@property (nonatomic, strong) TYAlertController *faceAlert;

/***--------播放音乐图标----------****/
@property (weak, nonatomic) IBOutlet UIButton *musicBtn;

@property (assign, nonatomic) BOOL isIsOnMic;

@property (assign, nonatomic) BOOL micInListOption;
@property (assign, nonatomic) BOOL  lottery_box_option;

@property (weak, nonatomic) IBOutlet UIButton *waitMicroUsersButton;




/***----------进入房间提示了---------***/
@property (strong,nonatomic) HJRoomComeView *enterTipView;

@property (nonatomic, strong) UIWindow *messageListWindow;
@property (nonatomic, strong) UIWindow *messageWindow;

/***----------龙珠---------***/
@property (nonatomic, weak) HJGameRoomPeiDuiEnterView *longZhuView;
@property (nonatomic, assign) BOOL isLongZhuViewOpen;
@property (nonatomic, assign) NSInteger didSeletedNum;
@property (nonatomic, assign) NSInteger longZhuViewStatus;

@property (nonatomic,strong) UIButton *pkBtn;



/***-----------送礼物提示-------------***/

@property (nonatomic, strong) UIButton *giftRecordBtn;//
@property (nonatomic, strong) UIButton *conchRecordBtn;//捡海螺记录
@property (nonatomic, assign) BOOL hasCheckPK;

//聊天按钮-----筛选聊天消息内容
@property (nonatomic, strong) UIButton *talkMessageSelectBtn;
@property (nonatomic, assign) HJMessageType messageType;


//接收到礼物
- (void)receiveMicGift:(HJGiftAllMicroSendInfo *)giftInfo;



- (void)beKicked;
- (void)beInBlackList;
- (void)updateCoverView;


- (IBAction)onShareBtnClicked:(id)sender;//分享
//更新人数
- (void)updateOnlineCount;


@property (weak, nonatomic) IBOutlet NSLayoutConstraint *guanzhu_height_layout;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *guanzhu_width_layout;
//hjtoolbar
@property (nonatomic, assign) BOOL isFirstAddToolbar;
@end
