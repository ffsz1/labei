//
//  HJSessionViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSessionViewController.h"

#import "HJSessionViewController.h"
#import "HJSessionConfig.h"
#import "HJOpenLiveAlertMessageContentView.h"
#import "HJRecPacketConentMessageView.h"
#import "HJNewsNoticeContentMessageView.h"
#import "HJGiftContentMessageView.h"
#import "HJGiftAttachment.h"
#import "TurntableAttachment.h"
#import "HJRedPacketInfoAttachment.h"
#import "HJNewsInfoAttachment.h"
#import "HJWKWebViewController.h"
#import "SDPhotoBrowser.h"
#import "NTESGlobalMacro.h"
#import "TYAlertController.h"
#import "HJUserCoreHelp.h"
#import "HJPurseViewControllerFactory.h"
#import "HJImagePreViewVC.h"
#import "HJAlertControllerCenter.h"
#import "HJRoomViewControllerCenter.h"
#import "HJUserViewControllerFactory.h"
#import "HJMySpaceVC.h"
#import "NTESVideoViewController.h"
#import "HJBalanceErrorClient.h"
#import "HJVersionCoreHelp.h"
#import "HJImFriendCore.h"
#import "HJGiftCore.h"
#import "HJImMessageCoreClient.h"
#import "HJImMessageCore.h"
#import "UIView+XCToast.h"

#import "YYActionSheetViewController.h"

#import "HJUserHandler.h"
#import "PurseCore.h"

#import "HJGiftBoxView.h"
#import "NIMMessageMaker.h"

#import "HJSendGoldVC.h"
#import "HJSendGoldModel.h"
#import "HJHttpRequestHelper+Home.h"
@interface HJSessionViewController ()<NIMMessageContentViewDelegate,NIMMessageCellDelegate,SDPhotoBrowserDelegate,HJImMessageCoreClient>
@property (nonatomic,strong)    HJSessionConfig       *sessionConfig;
@property (nonatomic, strong) TYAlertController *giftAlert;

@property (nonatomic, strong) UINavigationController *temNav;
@property (nonatomic, strong) NIMMessage *currentMsg;
@property (nonatomic, copy) NSString *text;
@end

@implementation HJSessionViewController

- (void)viewDidLoad {
    [super viewDidLoad];
//    UIImageView *backImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"hj_nav_bar_back"]];
//
//    UIBarButtonItem *backBarBtn = [[UIBarButtonItem alloc] initWithCustomView:backImageView];
//    self.navigationItem.backBarButtonItem = backBarBtn;
    self.navigationItem.backBarButtonItem.tintColor = [UIColor redColor];
//    backBarBtn.customView = backImageView;
//    for (UIView *view in backBarBtn.customView.subviews) {
//        if ([view isKindOfClass:[UIImageView class]]) {
//            [UIImage imageNamed:@""]
//        }
//    }
    
    if (self.session.sessionType != 1) {
        UIBarButtonItem *rigthItem = [[UIBarButtonItem alloc]initWithImage:[UIImage imageNamed:@"hj_home_hot_more"] style:UIBarButtonItemStylePlain target:self action:@selector(showReport)];
        self.navigationItem.rightBarButtonItem = rigthItem;
    }
   
    AddCoreClient(HJBalanceErrorClient, self);
    AddCoreClient(HJImMessageCoreClient, self);
    [self getUserInfoForEnterChat:self.session.sessionId.userIDValue];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [GetCore(HJGiftCore) requestGiftList];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    HJBlackStatusBar
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)showReport {
    
    [HJUserHandler showReport:self.sessionConfig.session.sessionId.userIDValue cancelFollowBlock:^{
//        weakSelf.isAttentioned = NO;
//        weakSelf.attentBtn.userInteractionEnabled = YES;
    }];
    
//    YYActionSheetViewController *sheet = [[YYActionSheetViewController alloc] init];
//
//    __weak typeof(self) weakSelf = self;
//
//    if ([GetCore(HJImFriendCore) isUserInBlackList:[NSString stringWithFormat:@"%@",self.sessionConfig.session.sessionId]]) {
//        [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoOutBlackList, nil) block:^(YYActionSheetViewController *controller) {
//            [weakSelf removeAlerView];
//        }];
//    } else {
//        [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoInBlackList, nil) block:^(YYActionSheetViewController *controller) {
//            [weakSelf showAlerView];
//        }];
//    }
//
//    [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoJubao, nil) block:^(YYActionSheetViewController *controller) {
//        [weakSelf report];
//    }];
//
//
//
//    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YYActionSheetViewController *controller) {
//
//    }];
//
//    [sheet show];
    
}

//导航栏右侧按钮点击方法
- (void)report {
    
    __weak typeof(self)weakSelf = self;
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"政治敏感" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:[self.sessionConfig.session.sessionId integerValue] reportType:1 type:1 phoneNo:nil requestId:@"XBDPersonVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"色情低俗" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:[self.sessionConfig.session.sessionId integerValue] reportType:2 type:1 phoneNo:nil requestId:@"XBDPersonVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"广告骚扰" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:[self.sessionConfig.session.sessionId integerValue] reportType:3 type:1 phoneNo:nil requestId:@"XBDPersonVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"人身攻击" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:[self.sessionConfig.session.sessionId integerValue] reportType:4 type:1 phoneNo:nil requestId:@"XBDPersonVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
    
    [self presentViewController:alter animated:YES completion:nil];
    
    

}


//移出黑名单
- (void)removeAlerView {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPersonalInfoOutBlackListTitle, nil) message:NSLocalizedString(XCPersonalInfoOutBlackListMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJImFriendCore) removeFromBlackList:[NSString stringWithFormat:@"%@",self.sessionConfig.session.sessionId]];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}

//加入黑名单
- (void)showAlerView {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPersonalInfoInBlackListTitle, nil) message:NSLocalizedString(XCPersonalInfoInBlackListMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJImFriendCore) addToBlackList:[NSString stringWithFormat:@"%@",self.sessionConfig.session.sessionId]];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}

- (void)showMessage:(NSString *)msg {
    [MBProgressHUD showSuccess:msg];
}

- (id<NIMSessionConfig>)sessionConfig
{
    if (_sessionConfig == nil) {
        _sessionConfig = [[HJSessionConfig alloc] init];
        _sessionConfig.session = self.session;
    }
    return _sessionConfig;
}

- (NSDictionary *)cellActions
{
    static NSDictionary *actions = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        actions = @{@(NIMMessageTypeImage) :    @"showImage:",
                    @(NIMMessageTypeVideo) :    @"showVideo:",
                    @(NIMMessageTypeLocation) : @"showLocation:",
                    @(NIMMessageTypeFile)  :    @"showFile:",
                    @(NIMMessageTypeCustom):    @"showCustom:"};
    });
    return actions;
}

- (BOOL)onTapCell:(NIMKitEvent *)event {
    BOOL handled = [super onTapCell:event];
    NSString *eventName = event.eventName;
    if ([eventName isEqualToString:NIMKitEventNameTapContent])
    {
        if (self.roomMessageListDidSelectCell) {
            self.temNav = self.roomMessageListDidSelectCell();
        }
        else {
            self.temNav = self.navigationController;
        }
        NIMMessage *message = event.messageModel.message;
        NSDictionary *actions = [self cellActions];
        NSString *value = actions[@(message.messageType)];
        if (value) {
            SEL selector = NSSelectorFromString(value);
            if (selector && [self respondsToSelector:selector]) {
                SuppressPerformSelectorLeakWarning([self performSelector:selector withObject:message]);
                handled = YES;
            }
        }
    }else if ([eventName isEqualToString:@"HJNewsNoticeContentMessageViewClick"]) {
        if (self.roomMessageListDidSelectCell) {
            self.temNav = self.roomMessageListDidSelectCell();
        }
        else {
            self.temNav = self.navigationController;
        }
        NIMMessage *message = event.messageModel.message;
        [self showCustom:message];
    }else if ([eventName isEqualToString:@"XCOnRedPacketNoticClick"]) {
        if (self.roomMessageListDidSelectCell) {
            self.temNav = self.roomMessageListDidSelectCell();
        }
        else {
            self.temNav = self.navigationController;
        }
        NIMMessage *message = event.messageModel.message;
        [self showCustom:message];
    }else if ([eventName isEqualToString:@"HJTurntableMessageViewClick"]) {
        if (self.roomMessageListDidSelectCell) {
            self.temNav = self.roomMessageListDidSelectCell();
        }
        else {
            self.temNav = self.navigationController;
        }
        NIMMessage *message = event.messageModel.message;
        [self showCustom:message];
    }
    return handled;
}

- (void)showImage:(NIMMessage *)message {
    NIMImageObject *object = (NIMImageObject *)message.messageObject;
    HJImagePreViewVC *vc = [[HJImagePreViewVC alloc]init];
    vc.ImageUrl = [object url];
    [self.temNav pushViewController:vc animated:YES];
    
}

- (void)showCustom:(NIMMessage *)message
{
    
    NIMCustomObject *customObject = (NIMCustomObject*)message.messageObject;
    
    //普通的自定义消息点击事件可以在这里做哦~
    if ([customObject.attachment isKindOfClass:[HJNewsInfoAttachment class]]) {
        HJNewsInfoAttachment *info = (HJNewsInfoAttachment *)customObject.attachment;
        HJWKWebViewController *webView = [[HJWKWebViewController alloc]init];
        webView.url = [NSURL URLWithString:info.skipUrl];
        [self.temNav pushViewController:webView animated:YES];
    }else if ([customObject.attachment isKindOfClass:[HJRedPacketInfoAttachment class]]) {
        

    }else if ([customObject.attachment isKindOfClass:[TurntableAttachment class]]) {
        HJWKWebViewController *webView = [[HJWKWebViewController alloc]init];
        NSString *urlSting = [NSString stringWithFormat:@"%@/front/luckdraw/index.html",[HJHttpRequestHelper getHostUrl]];
        webView.url = [NSURL URLWithString:urlSting];
        [self.temNav pushViewController:webView animated:YES];
    }
}

- (void)onTapSendGift:(NIMMediaItem *)item {
//    NSLog(@"%d",[GetCore(ImFriendCore) isUserInBlackList:[GetCore(AuthCore) getUid]]);
    
    [HJGiftBoxView showChat:self.session.sessionId.userIDValue];
    
}
- (UserInfo*)getUserInfoForEnterChat:(UserID)uid
{
   
   __block UserInfo * userInfo = [GetCore(HJUserCoreHelp) getUserInfoInDB:uid];
    
    if (userInfo.uid == 0) {
       [GetCore(HJUserCoreHelp) getUserInfo:uid refresh:YES success:^(UserInfo *info) {
           userInfo = info;
           
        }];
    }
    return userInfo;
    
}

//MARK:- 转金币 音友
-(void)onTapSendGold:(NIMMediaItem *)item {
    HJSendGoldVC* vc = [[HJSendGoldVC alloc] init];
     UserInfo * userInfo = [GetCore(HJUserCoreHelp) getUserInfoInDB:self.session.sessionId.userIDValue];
    vc.userInfo = userInfo;
    vc.recvUid = self.session.sessionId.userIDValue;
    vc.sendGlodBlock = ^(HJSendGoldModel * _Nonnull model) {
        //发送信息
        
//        self.sessionInputView.toolBar.showsKeyboard = NO;
         Attachment *attachement = [[Attachment alloc]init];
                attachement.first = 51;
                attachement.second = 1;
       
        NSDictionary *dict = @{
                                  @"sendUid"          :  @(model.sendUid),
                                  @"recvUid"    :  @(model.recvUid),
                                  @"recvName"         :  model.recvName,
                                  @"sendName"         :  model.sendName,
                                  @"recvAvatar" : model.recvAvatar,
                                  @"sendAvatar" : model.sendAvatar,
                                  @"goldNum"   : @(model.goldNum)
                                  };
         attachement.data = dict;
//        [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",model.recvUid] type:JXIMSessionTypeP2P needApns:YES apnsContent:[NSString stringWithFormat:@"%@给你转账了%ld个金币",model.sendName,model.goldNum] yidunEnable:false];
        //刷新金币
        [GetCore(PurseCore) requestBalanceInfo:GetCore(HJAuthCoreHelp).getUid.userIDValue];
        
    };
    
//    [self.navigationController pushViewController:vc animated:YES];
    
    [self presentViewController:vc animated:YES completion:nil];
    
//    self.sessionInputView.toolBar.showsKeyboard = NO;
}

#pragma mark - NIMMessageCellDelegate
- (BOOL)onTapAvatar:(NSString *)userId {
    
    if (self.roomMessageListDidSelectCell) {
        self.temNav = self.roomMessageListDidSelectCell();
    }
    else {
        self.temNav = self.navigationController;
    }

    HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
    vc.userID = userId.userIDValue;
    
    [self.temNav pushViewController:vc animated:YES];
    return YES;
}

- (void)showVideo:(NIMMessage *)message
{
    NIMVideoObject *object = (NIMVideoObject *)message.messageObject;
    NTESVideoViewController *playerViewController = [[NTESVideoViewController alloc] initWithVideoObject:object];
    [self.temNav pushViewController:playerViewController animated:YES];
    if(![[NSFileManager defaultManager] fileExistsAtPath:object.coverPath]){
        //如果封面图下跪了，点进视频的时候再去下一把封面图
        __weak typeof(self) wself = self;
        [[NIMSDK sharedSDK].resourceManager download:object.coverUrl filepath:object.coverPath progress:nil completion:^(NSError *error) {
            if (!error) {
                [wself uiUpdateMessage:message];
            }
        }];
    }
}

#pragma mark - BalanceErrorClient
- (void)onBalanceNotEnough {
    @weakify(self);
    [HJAlertControllerCenter defaultCenter].dismissComplete = ^{
        @strongify(self);
        [self showBalanceNotEnougth];
        
    };
    [[HJAlertControllerCenter defaultCenter]dismissAlertNeedBlock:YES];
    
}


- (void)showBalanceNotEnougth {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPurseNoMoneyTitle, nil) message:NSLocalizedString(XCPurseNoMoneyMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    @weakify(self);
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        UIViewController *vc = [[HJPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
        [self.temNav pushViewController:vc animated:YES];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}
- (NSTimeInterval)audioDurationFromURL:(NSString *)url {
    AVURLAsset *audioAsset = nil;
    NSDictionary *dic = @{AVURLAssetPreferPreciseDurationAndTimingKey:@(YES)};
    if ([url hasPrefix:@"http://"]) {
        audioAsset = [AVURLAsset URLAssetWithURL:[NSURL URLWithString:url] options:dic];
    }else {
        audioAsset = [AVURLAsset URLAssetWithURL:[NSURL fileURLWithPath:url] options:dic];
    }
    CMTime audioDuration = audioAsset.duration;
    float audioDurationSeconds = CMTimeGetSeconds(audioDuration);
    return audioDurationSeconds;
}

- (BOOL)recordFileCanBeSend:(NSString *)filepath {
    //根据本地文件路径获取录音文件时长
    CGFloat time = [self audioDurationFromURL:filepath];
    if (time < 1) {
        [MBProgressHUD showError:@"说话时间太短"];
        return NO;
    }else {
        return YES;
    }
}

- (void)sendMessage:(NIMMessage *)message {
    
    if (message.messageType == NIMMessageTypeText) {
        if (message.text.length && !self.text.length) {
            
            self.currentMsg = message;
            self.text = [message.text copy];
            @weakify(self);
            [GetCore(HJImMessageCore) sensitiveWordRegexWithText:message.text requestId:NSStringFromClass([self class]) finishBlock:^(BOOL isCanSend, NSString *msg) {
                @strongify(self);
                if (isCanSend) {
                    [self sendMessage:self.currentMsg];
                    self.text = nil;
                }
                else {
                    [MBProgressHUD showError:msg toView:[UIApplication sharedApplication].keyWindow];
                    self.text = nil;
                }
            }];
        }
        else {
            [super sendMessage:message];
        }
    }
//    else if (message.messageType == NIMMessageTypeAudio){
//        NIMAudioObject *audioObject = (NIMAudioObject*)message.messageObject;
//        NSLog(@"message----%@",message);
//        NSLog(@"audioObject----%@",audioObject);
//        NSLog(@"audioObject.duration----%zd",audioObject.duration);
//        NSInteger time = (audioObject.duration+500)/1000;
////        audioObject.sourcePathP;
//        if (time == 0) {
//            [MBProgressHUD showError:@"说话时间太短"];
//        }else {
//            [super sendMessage:message];
//        }
//    }
    else {
        [super sendMessage:message];
    }
}
- (void)viewDidLayoutSubviews
{
    [super viewDidLayoutSubviews];
    
    //设置所有消息的右边气泡底图
    [self setNomralBubbleStytle];
       [self setLeftNomralBubbleStytle];
    [self setHighlightBubbleStytle];
}
- (void)setLeftNomralBubbleStytle{
    [NIMKit sharedKit].config.leftBubbleSettings.textSetting.textColor = [UIColor colorWithHexString:@"#316AFF"];
       [NIMKit sharedKit].config.leftBubbleSettings.audioSetting.textColor= [UIColor colorWithHexString:@"#316AFF"];
    [NIMKit sharedKit].config.leftBubbleSettings.textSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       [NIMKit sharedKit].config.leftBubbleSettings.audioSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       [NIMKit sharedKit].config.leftBubbleSettings.imageSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       [NIMKit sharedKit].config.leftBubbleSettings.fileSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [NIMKit sharedKit].config.leftBubbleSettings.videoSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [NIMKit sharedKit].config.leftBubbleSettings.locationSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [NIMKit sharedKit].config.leftBubbleSettings.tipSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [NIMKit sharedKit].config.leftBubbleSettings.robotSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [NIMKit sharedKit].config.leftBubbleSettings.unsupportSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [NIMKit sharedKit].config.leftBubbleSettings.teamNotificationSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [NIMKit sharedKit].config.leftBubbleSettings.chatroomNotificationSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [NIMKit sharedKit].config.leftBubbleSettings.netcallNotificationSetting.normalBackgroundImage =  [self getBubbleLeftImage];
}
//设置右气泡正常颜色
- (void)setNomralBubbleStytle
{
    [NIMKit sharedKit].config.rightBubbleSettings.textSetting.textColor = [UIColor colorWithHexString:@"#316AFF"];
       [NIMKit sharedKit].config.rightBubbleSettings.audioSetting.textColor= [UIColor colorWithHexString:@"#316AFF"];
    [NIMKit sharedKit].config.rightBubbleSettings.textSetting.normalBackgroundImage =  [self getBubbleImage];
    [NIMKit sharedKit].config.rightBubbleSettings.audioSetting.normalBackgroundImage =  [self getBubbleImage];
    [NIMKit sharedKit].config.rightBubbleSettings.imageSetting.normalBackgroundImage =  [self getBubbleImage];
    [NIMKit sharedKit].config.rightBubbleSettings.fileSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.videoSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.locationSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.tipSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.robotSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.unsupportSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.teamNotificationSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.chatroomNotificationSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.netcallNotificationSetting.normalBackgroundImage =  [self getBubbleImage];
}

//设置右气泡高亮颜色
- (void)setHighlightBubbleStytle
{
    [NIMKit sharedKit].config.rightBubbleSettings.textSetting.highLightBackgroundImage =  [self getBubbleImage];
    [NIMKit sharedKit].config.rightBubbleSettings.audioSetting.highLightBackgroundImage =  [self getBubbleImage];
    [NIMKit sharedKit].config.rightBubbleSettings.imageSetting.highLightBackgroundImage =  [self getBubbleImage];
    [NIMKit sharedKit].config.rightBubbleSettings.fileSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.videoSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.locationSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.tipSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.robotSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.unsupportSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.teamNotificationSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.chatroomNotificationSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [NIMKit sharedKit].config.rightBubbleSettings.netcallNotificationSetting.highLightBackgroundImage =  [self getBubbleImage];
}
- (UIImage *)getBubbleLeftImage
{
    return [[UIImage imageNamed:@"hj_message_session_bg_left"] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{18,25,17,25}") resizingMode:UIImageResizingModeStretch];
}
- (UIImage *)getBubbleImage
{
    return [[UIImage imageNamed:@"hj_message_session_bg_right"] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{18,25,17,25}") resizingMode:UIImageResizingModeStretch];
}

- (UIImage *)getBubbleImage_Sel
{
    return [[UIImage imageNamed:@"hj_message_session_bg_highlight_right"] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{18,25,17,25}") resizingMode:UIImageResizingModeStretch];
}


@end
