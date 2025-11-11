//
//  YPGameRoomVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
//  轰趴房控制器

#import "YPYYViewController.h"


#import "YPGameRoomPositionView.h" //坑位（麦序）
#import "YPMessageTableView.h" //公屏

//房间相关&麦序相关
#import "YPRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "YPRoomQueueCoreV2Help.h"
#import "HJRoomQueueCoreClient.h"
#import "YPFinishLiveView.h"

//IM相关
#import "YPImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"

//礼物相关
#import "YPGiftCore.h"
#import "HJGiftCoreClient.h"
#import "YPLiveGiftShowCustom.h"
#import <FLAnimatedImage/FLAnimatedImageView.h>
#import "YPGiftViewContainer.h"

//分享相关
#import "HJShareCoreClient.h"


//控件
#import "YPYYActionSheetViewController.h"
#import "TYAlertController.h"
#import "YPMarqueeLabel.h"

//会议相关
#import "YPMeetingCore.h"
#import "HJMeetingCoreClient.h"

//登录系统
#import "YPAuthCoreHelp.h"

//余额不足
#import "HJBalanceErrorClient.h"

#import <FLAnimatedImageView.h>

#import "HJImMessageCoreClient.h"
#import "HJImMessageSendCoreClient.h"


#import "YPUserViewControllerFactory.h"

#import "YPPraiseCore.h"
#import "YPRedPacketActivityView.h"

#import "YPActivityInfo.h"
#import "HJActivityCoreClient.h"
#import "YPActivityCore.h"

#import "YPToolBar.h"
#import "YPGameRoomFaceView.h"
#import "YPAlertControllerCenter.h"
#import "UIView+XCToast.h"

#import "MMAlertView.h"
#import "MMSheetView.h"
#import <YYImage/YYImage.h>

#import "YPSessionListViewController.h"
#import "YPWMLabel.h"

#import "YPGameRoomPeiDuiEnterView.h"


#import "YPGiftSecretInfo.h"
#import "YPRoomComeView.h"
#import "YPRoomBounsListInfo.h"
#import "YPSDCycleScrollView.h"

@protocol HJGameRoomVCDelagte <NSObject>

- (void)roomExit;
- (void)roomInitSuccessWithType:(RoomType)roomtype;
- (void)roomCloseButtonClick;
- (void)updateBackPicWith:(YPChatRoomInfo *)info userInfo:(UserInfo *)userInfo;
- (void)auctionListButtonClick;
- (void)scrollToListView;
@end

@interface YPGameRoomVC : UIViewController

@property (nonatomic, copy) NSString *lotteryBoxBigGift;

@property (nonatomic, assign) BOOL isAll;
@property (nonatomic, strong) YPGiftReceiveInfo *giftInfo;

//=======================麦序坑位=========================//
@property (strong, nonatomic) YPGameRoomPositionView *positionView;

//=======================公屏=======================//
@property (strong, nonatomic) YPMessageTableView *messageTableView;
@property (strong, nonatomic) IBOutlet UIView *messageTableViewHeader;//公屏警告语
@property (weak, nonatomic) IBOutlet YPWMLabel *headerViewTip;
@property (weak, nonatomic) IBOutlet UIImageView *bgImg;


//===================输入框=====================//
@property (weak, nonatomic) IBOutlet UIView *editView;//输入容器
@property (weak, nonatomic) IBOutlet UITextField *editText; //输入框
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *editViewBottomConstraint;
@property (assign, nonatomic) BOOL keyboardIsShow;
@property (weak, nonatomic) IBOutlet UIButton *eggSysBtn;

//====================主坑=======================//
@property (weak, nonatomic) IBOutlet YPMarqueeLabel *ownerNameLabel;//房间名字
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_roomName;
@property (weak, nonatomic) IBOutlet UILabel *roomIDLabel;//房间ID
@property (weak, nonatomic) IBOutlet UIImageView *lockIcon;// 锁的标志
@property (weak, nonatomic) IBOutlet UILabel *onlineNumberLabel;//在线用户人数
@property (weak, nonatomic) IBOutlet UIButton *onlineNumberBtn;



@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UIImageView *userLeaveImg;

@property (weak, nonatomic) IBOutlet UIImageView *recordingImg;



@property (strong , nonatomic) YPChatRoomMember *mainPositionMember;
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
@property (strong, nonatomic) YPToolBar *toolBar;
@property (weak, nonatomic) IBOutlet UIButton *closeButton;//关闭房间按钮
@property (strong, nonatomic) TYAlertController *faceAlertView; //表情弹窗
@property (weak, nonatomic) IBOutlet UIImageView *roommerHeadwearImg;

//==================礼物View====================//
@property (strong, nonatomic) TYAlertController *giftAlertView;

@property (strong, nonatomic) UIView *giftContainer;
@property (strong, nonatomic) NSMutableArray<YPGiftInfo *> *gifQueueArr; //礼物队列
@property (nonatomic, strong) FLAnimatedImageView *gifImageView;//gif播放
@property(nonatomic, strong)YPLiveGiftShowCustom* customGiftShow;

//====================房间======================//
@property(nonatomic, strong) YPChatRoomInfo *roomInfo;


//====================弹窗=====================//
@property (nonatomic, strong) YPRedPacketActivityView *activityView; //活动view
@property (nonatomic, strong) YPSDCycleScrollView *banaCommonView; //活动view
@property (nonatomic, strong) NSArray *banaCommonArray;
@property (nonatomic, strong) YPActivityInfo *activityInfo;
@property (nonatomic, strong) TYAlertController* sharePanelSheet;
@property (nonatomic, strong) YPYYActionSheetViewController* moreActionSheet;
@property (nonatomic, strong) TYAlertController* giftContainerSheet;
@property (assign, nonatomic) BOOL isLike;
@property (nonatomic, strong) TYAlertController *alertViewController;//公用弹窗
//贡献榜按钮
@property (weak, nonatomic) IBOutlet UIButton *roomShenhaoBtn;
@property (strong, nonatomic) UIButton *roomContributionBtn;
@property (weak, nonatomic) id<HJGameRoomVCDelagte> delegate;

//====================表情=====================//
@property (nonatomic, strong) YPGameRoomFaceView *faceView;
@property (nonatomic, strong) TYAlertController *faceAlert;

/***--------播放音乐图标----------****/
@property (weak, nonatomic) IBOutlet UIButton *musicBtn;

@property (assign, nonatomic) BOOL micInListOption;
@property (assign, nonatomic) BOOL  lottery_box_option;

@property (weak, nonatomic) IBOutlet UIButton *waitMicroUsersButton;




/***----------进入房间提示了---------***/
@property (strong,nonatomic) YPRoomComeView *enterTipView;

@property (nonatomic, strong) UIWindow *messageListWindow;
@property (nonatomic, strong) UIWindow *messageWindow;

/***----------龙珠---------***/
@property (nonatomic, weak) YPGameRoomPeiDuiEnterView *longZhuView;
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
- (void)receiveMicGift:(YPGiftAllMicroSendInfo *)giftInfo;



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
